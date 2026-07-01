package com.florabreak.app.ui;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.florabreak.app.R;
import com.florabreak.app.data.repository.BreakSessionRepository;

public class ActiveBreakActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 44;

    private TextView timerText;
    private TextView selectedRouteNameText;
    private TextView navigationMainText;
    private TextView navigationSubText;
    private TextView remainingTimeText;
    private TextView distanceText;
    private TextView proofCameraPlaceholder;

    private Button openMapsButton;
    private Button finishBreakButton;

    private final Handler handler = new Handler();

    private BreakSessionRepository breakSessionRepository;

    private int seconds = 0;
    private boolean timerRunning = true;
    private boolean reminderScheduled = false;

    private long breakSessionId = -1L;

    private String selectedRouteName = "Grünfläche in der Nähe";
    private int selectedWalkingTimeMinutes = 0;
    private String selectedRouteType = "ROUTE";

    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;

    private int stressScore = 0;
    private String stressLabel = "Unbekannt";

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (timerRunning) {
                seconds++;

                int minutes = seconds / 60;
                int remainingSeconds = seconds % 60;

                String time = String.format(java.util.Locale.GERMANY, "%02d:%02d", minutes, remainingSeconds);
                timerText.setText(time);

                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_break);

        breakSessionRepository = new BreakSessionRepository(this);

        bindViews();
        readIntentData();
        startBreakSessionIfNeeded();
        updateRouteUi();
        setupButtons();

        handler.postDelayed(timerRunnable, 1000);
    }

    private void bindViews() {
        timerText = findViewById(R.id.timerText);
        selectedRouteNameText = findViewById(R.id.selectedRouteNameText);
        navigationMainText = findViewById(R.id.navigationMainText);
        navigationSubText = findViewById(R.id.navigationSubText);
        remainingTimeText = findViewById(R.id.remainingTimeText);
        distanceText = findViewById(R.id.distanceText);

        finishBreakButton = findViewById(R.id.finishBreakButton);
        proofCameraPlaceholder = findViewById(R.id.proofCameraPlaceholder);
        openMapsButton = findViewById(R.id.openMapsButton);
    }

    private void readIntentData() {
        String routeNameExtra = getIntent().getStringExtra("selectedRouteName");
        String routeTypeExtra = getIntent().getStringExtra("selectedRouteType");
        String stressLabelExtra = getIntent().getStringExtra("stressLabel");

        if (routeNameExtra != null && !routeNameExtra.trim().isEmpty()) {
            selectedRouteName = routeNameExtra;
        }

        if (routeTypeExtra != null && !routeTypeExtra.trim().isEmpty()) {
            selectedRouteType = routeTypeExtra;
        }

        if (stressLabelExtra != null && !stressLabelExtra.trim().isEmpty()) {
            stressLabel = stressLabelExtra;
        }

        selectedWalkingTimeMinutes = getIntent().getIntExtra("selectedWalkingTimeMinutes", 0);
        selectedLatitude = getIntent().getDoubleExtra("selectedLatitude", 0.0);
        selectedLongitude = getIntent().getDoubleExtra("selectedLongitude", 0.0);
        stressScore = getIntent().getIntExtra("stressScore", 0);
    }

    private void startBreakSessionIfNeeded() {
        if (breakSessionId > 0) {
            return;
        }

        int plannedDuration = selectedWalkingTimeMinutes;

        if (plannedDuration <= 0) {
            plannedDuration = 5;
        }

        breakSessionId = breakSessionRepository.startBreak(
                selectedRouteName,
                selectedRouteType,
                selectedLatitude,
                selectedLongitude,
                plannedDuration,
                stressScore,
                stressLabel
        );
    }

    private void updateRouteUi() {
        selectedRouteNameText.setText("📍 " + selectedRouteName);

        if (selectedWalkingTimeMinutes > 0) {
            remainingTimeText.setText(selectedWalkingTimeMinutes + " Min");
        } else {
            remainingTimeText.setText("— Min");
        }

        if ("REAL_URBAN_WALK".equals(selectedRouteType)) {
            navigationMainText.setText("Urban Walk aktiv");
            navigationSubText.setText("Route bis " + selectedRouteName);
            distanceText.setText("Route aktiv");
        } else if ("REAL_ROUTE_TOO_FAR".equals(selectedRouteType)) {
            navigationMainText.setText("Längere Route aktiv");
            navigationSubText.setText("Gehe deine ausgewählte Route bis " + selectedRouteName);
            distanceText.setText("Route aktiv");
        } else if ("FALLBACK_URBAN_WALK".equals(selectedRouteType)
                || "FALLBACK_ROUTE_INFO".equals(selectedRouteType)) {
            navigationMainText.setText("Urban Walk aktiv");
            navigationSubText.setText("Gehe deine ausgewählte Route bis " + selectedRouteName);
            distanceText.setText("Aktive Pause");
        } else {
            navigationMainText.setText("Pause aktiv");
            navigationSubText.setText("Route: " + selectedRouteName);
            distanceText.setText("—");
        }
    }

    private void setupButtons() {
        openMapsButton.setOnClickListener(view -> {
            requestNotificationPermissionIfNeeded();
            scheduleBreakReminder();
            openRouteInGoogleMaps();
        });

        proofCameraPlaceholder.setOnClickListener(view -> {
            Intent intent = new Intent(ActiveBreakActivity.this, RouteProofActivity.class);
            intent.putExtra("breakSessionId", breakSessionId);
            startActivity(intent);
        });

        finishBreakButton.setOnClickListener(view -> {
            timerRunning = false;

            Intent intent = new Intent(ActiveBreakActivity.this, BreakFeedbackActivity.class);
            intent.putExtra("breakSessionId", breakSessionId);
            intent.putExtra("selectedRouteName", selectedRouteName);
            intent.putExtra("selectedWalkingTimeMinutes", selectedWalkingTimeMinutes);
            intent.putExtra("selectedRouteType", selectedRouteType);
            intent.putExtra("elapsedDurationMinutes", getElapsedDurationMinutes());
            startActivity(intent);
        });
    }

    private int getElapsedDurationMinutes() {
        int elapsedMinutes = Math.max(1, seconds / 60);

        if (selectedWalkingTimeMinutes > 0) {
            return Math.max(elapsedMinutes, selectedWalkingTimeMinutes);
        }

        return elapsedMinutes;
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            }
        }
    }

    private void scheduleBreakReminder() {
        if (reminderScheduled) {
            return;
        }

        int reminderMinutes = selectedWalkingTimeMinutes;

        if (reminderMinutes <= 0) {
            reminderMinutes = 5;
        }

        int halfTimeMinutes = Math.max(1, reminderMinutes / 2);

        scheduleSingleReminder(
                halfTimeMinutes,
                BreakReminderReceiver.TYPE_PHOTO_PROOF,
                2027
        );

        scheduleSingleReminder(
                reminderMinutes,
                BreakReminderReceiver.TYPE_END_BREAK,
                2028
        );

        reminderScheduled = true;
    }

    private void scheduleSingleReminder(
            int minutesFromNow,
            String reminderType,
            int requestCode
    ) {
        Intent intent = new Intent(this, BreakReminderReceiver.class);
        intent.putExtra(BreakReminderReceiver.EXTRA_REMINDER_TYPE, reminderType);
        intent.putExtra("breakSessionId", breakSessionId);

        int plannedDuration = selectedWalkingTimeMinutes;

	if (plannedDuration <= 0) {
	    plannedDuration = 5;
	}

	intent.putExtra("plannedDurationMinutes", plannedDuration);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            long triggerAtMillis = System.currentTimeMillis() + minutesFromNow * 60L * 1000L;

            alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
            );
        }
    }

    private void openRouteInGoogleMaps() {
        if (selectedLatitude == 0.0 && selectedLongitude == 0.0) {
            return;
        }

        Uri uri = Uri.parse(
                "https://www.google.com/maps/dir/?api=1"
                        + "&destination="
                        + selectedLatitude
                        + ","
                        + selectedLongitude
                        + "&travelmode=walking"
        );

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(browserIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        timerRunning = false;
        handler.removeCallbacks(timerRunnable);
    }
}
