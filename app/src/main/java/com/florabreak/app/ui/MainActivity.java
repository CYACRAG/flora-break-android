package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import com.florabreak.app.data.repository.ProfileRepository;
import com.florabreak.app.R;
import com.florabreak.app.data.FloraBreakController;
import com.florabreak.app.data.FloraBreakControllerFactory;
import com.florabreak.app.data.local.BreakEntity;
import com.florabreak.app.data.repository.BreakSessionRepository;
import com.florabreak.app.model.FloraBreakSessionResult;
import com.florabreak.app.model.StressResult;

public class MainActivity extends AppCompatActivity {

    private static final String STRESS_ALERT_CHANNEL_ID = "flora_stress_alert_channel";
	private static final String PREF_STRESS_ALERTS = "flora_stress_alerts";
	private static final String KEY_LAST_STRESS_ALERT = "last_stress_alert";
	private static final int STRESS_ALERT_NOTIFICATION_ID = 2030;
	private static final int STRESS_NOTIFICATION_PERMISSION_REQUEST_CODE = 45;
	private static final long STRESS_ALERT_COOLDOWN_MS = 2L * 60L * 60L * 1000L;
    private Button showBreakButton;
    private Button refreshButton;

    private LinearLayout navHistoryButton;
    private LinearLayout navStatsButton;
    private LinearLayout navProfileButton;

    private TextView stressScoreText;
    private TextView stressLabelText;
    private TextView recentBreakTitleText;
    private TextView recentBreaksText;

    private StressGaugeView stressGaugeView;
    private ProfileRepository profileRepository;
    private FloraBreakController floraBreakController;
    private BreakSessionRepository breakSessionRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	profileRepository = new ProfileRepository(this);

	if (!profileRepository.isProfileCompleted()) {
	    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
	    intent.putExtra("firstSetup", true);
	    startActivity(intent);
	    finish();
	    return;
	}

        floraBreakController = FloraBreakControllerFactory.create(this);
        breakSessionRepository = new BreakSessionRepository(this);

        bindViews();
        setupNavigation();
        updateStressFromController();
        updateRecentBreaks();

        refreshButton.setOnClickListener(view -> {
            updateStressFromController();
            updateRecentBreaks();
            Toast.makeText(this, "Stressdaten wurden aktualisiert", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        floraBreakController = FloraBreakControllerFactory.create(this);
        updateStressFromController();
        updateRecentBreaks();
    }

    private void bindViews() {
        showBreakButton = findViewById(R.id.showBreakButton);
        refreshButton = findViewById(R.id.refreshButton);

        stressGaugeView = findViewById(R.id.stressGaugeView);
        stressScoreText = findViewById(R.id.stressScoreText);
        stressLabelText = findViewById(R.id.stressLabelText);

        recentBreakTitleText = findViewById(R.id.recentBreakTitleText);
        recentBreaksText = findViewById(R.id.recentBreaksText);

        navHistoryButton = findViewById(R.id.navHistoryButton);
        navStatsButton = findViewById(R.id.navStatsButton);
        navProfileButton = findViewById(R.id.navProfileButton);
    }

    private void setupNavigation() {
        showBreakButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BreakSuggestionActivity.class);
            startActivity(intent);
        });

        navHistoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        navStatsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            startActivity(intent);
        });

        navProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void updateStressFromController() {
        FloraBreakSessionResult sessionResult =
                floraBreakController.evaluateCurrentSituation();

        StressResult stressResult = sessionResult.getStressResult();

        int stressScore = stressResult.getScore();

        stressGaugeView.setStressScore(stressScore);
        stressScoreText.setText(String.valueOf(stressScore));
        stressLabelText.setText(stressResult.getLabel());

		maybeShowStressAlertNotification(stressResult);
    }

    private void updateRecentBreaks() {
        BreakEntity latestBreak = breakSessionRepository.getLatestBreak();

        if (latestBreak == null) {
            recentBreakTitleText.setText("Keine Pause gespeichert");
            recentBreaksText.setText("Noch keine gespeicherten Pausen");
            return;
        }

        recentBreakTitleText.setText("Letzte gespeicherte Pause");

        String details =
                latestBreak.durationMinutes
                        + " Min · "
                        + safeText(latestBreak.routeName, "Route")
                        + " · Stress "
                        + latestBreak.stressScore
                        + "/10 · "
                        + createStarRating(latestBreak.rating);

        if (latestBreak.photoProofTaken) {
            details += " · Foto";
        }

        recentBreaksText.setText(details);
    }

    private String safeText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }

        return value;
    }

    private String createStarRating(int rating) {
        StringBuilder stars = new StringBuilder();

        for (int i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }

        return stars.toString();
	    }
	private void maybeShowStressAlertNotification(StressResult stressResult) {
	    if (stressResult == null) {
	        return;
	    }

	    if (stressResult.getScore() < 6) {
	        return;
	    }

	    if (!canShowStressAlertNow()) {
	        return;
	    }

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
	        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
	                != PackageManager.PERMISSION_GRANTED) {
	            ActivityCompat.requestPermissions(
	                    this,
	                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
	                    STRESS_NOTIFICATION_PERMISSION_REQUEST_CODE
	            );
	            return;
	        }
	    }

	    showStressAlertNotification(stressResult);
	    saveStressAlertTimestamp();
	}

	private boolean canShowStressAlertNow() {
	    SharedPreferences preferences = getSharedPreferences(PREF_STRESS_ALERTS, Context.MODE_PRIVATE);
	    long lastAlertTimestamp = preferences.getLong(KEY_LAST_STRESS_ALERT, 0L);
	    long now = System.currentTimeMillis();

	    return now - lastAlertTimestamp >= STRESS_ALERT_COOLDOWN_MS;
	}

	private void saveStressAlertTimestamp() {
	    SharedPreferences preferences = getSharedPreferences(PREF_STRESS_ALERTS, Context.MODE_PRIVATE);
	    preferences.edit()
	            .putLong(KEY_LAST_STRESS_ALERT, System.currentTimeMillis())
	            .apply();
	}

	private void showStressAlertNotification(StressResult stressResult) {
	    createStressAlertChannel();

	    Intent openBreakSuggestionIntent = new Intent(this, BreakSuggestionActivity.class);
	    openBreakSuggestionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

	    PendingIntent pendingIntent = PendingIntent.getActivity(
	            this,
	            STRESS_ALERT_NOTIFICATION_ID,
	            openBreakSuggestionIntent,
	            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
	    );

	    String text = "Dein Stresslevel liegt bei "
	            + stressResult.getScore()
	            + "/10. Starte eine kurze Flora Break.";

	    NotificationCompat.Builder builder =
	            new NotificationCompat.Builder(this, STRESS_ALERT_CHANNEL_ID)
	                    .setSmallIcon(R.drawable.ic_launcher_foreground)
	                    .setContentTitle("Flora Break empfiehlt eine Pause")
	                    .setContentText(text)
	                    .setPriority(NotificationCompat.PRIORITY_HIGH)
	                    .setAutoCancel(true)
	                    .setContentIntent(pendingIntent);

	    NotificationManager notificationManager =
	            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	    if (notificationManager != null) {
	        notificationManager.notify(STRESS_ALERT_NOTIFICATION_ID, builder.build());
	    }
	}

	private void createStressAlertChannel() {
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
	        NotificationChannel channel = new NotificationChannel(
	                STRESS_ALERT_CHANNEL_ID,
	                "Flora Break Stresswarnungen",
	                NotificationManager.IMPORTANCE_HIGH
	        );

	        channel.setDescription("Benachrichtigt bei erhöhtem Stresslevel über eine empfohlene Pause.");

	        NotificationManager notificationManager =
	                getSystemService(NotificationManager.class);

	        if (notificationManager != null) {
	            notificationManager.createNotificationChannel(channel);
	        }
	    }
	}
}
