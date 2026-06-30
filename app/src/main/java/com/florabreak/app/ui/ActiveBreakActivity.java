package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.florabreak.app.R;

public class ActiveBreakActivity extends AppCompatActivity {

    private TextView timerText;
    private TextView selectedRouteNameText;
    private TextView navigationMainText;
    private TextView navigationSubText;
    private TextView remainingTimeText;
    private TextView distanceText;

    private Button openMapsButton;

    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;
    private boolean reminderScheduled = false;

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 44;

    private Button finishBreakButton;
    private TextView proofCameraPlaceholder;

    private final Handler handler = new Handler();
    private int seconds = 0;
    private boolean timerRunning = true;

    private String selectedRouteName = "Grünfläche in der Nähe";
    private int selectedWalkingTimeMinutes = 0;
    private String selectedRouteType = "ROUTE";

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (timerRunning) {
                seconds++;

                int minutes = seconds / 60;
                int remainingSeconds = seconds % 60;

                String time = String.format("%02d:%02d", minutes, remainingSeconds);
                timerText.setText(time);

                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_break);

        bindViews();
        readIntentData();
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

        if (routeNameExtra != null && !routeNameExtra.trim().isEmpty()) {
            selectedRouteName = routeNameExtra;
        }

        if (routeTypeExtra != null && !routeTypeExtra.trim().isEmpty()) {
            selectedRouteType = routeTypeExtra;
        }

        selectedWalkingTimeMinutes = getIntent().getIntExtra("selectedWalkingTimeMinutes", 0);
        selectedLatitude = getIntent().getDoubleExtra("selectedLatitude", 0.0);
        selectedLongitude = getIntent().getDoubleExtra("selectedLongitude", 0.0);
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
            navigationSubText.setText("Echte Google-Route bis " + selectedRouteName);
            distanceText.setText("Route aktiv");
        } else if ("REAL_ROUTE_TOO_FAR".equals(selectedRouteType)) {
            navigationMainText.setText("Route ist zu lang");
            navigationSubText.setText("Für den Prototyp wird sie trotzdem angezeigt.");
            distanceText.setText("zu weit");
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
            startActivity(intent);
        });

        finishBreakButton.setOnClickListener(view -> {
            timerRunning = false;

            Intent intent = new Intent(ActiveBreakActivity.this, BreakFeedbackActivity.class);
            intent.putExtra("selectedRouteName", selectedRouteName);
            intent.putExtra("selectedWalkingTimeMinutes", selectedWalkingTimeMinutes);
            intent.putExtra("selectedRouteType", selectedRouteType);
            startActivity(intent);
        });
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

        Intent intent = new Intent(this, BreakReminderReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                2026,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            long triggerAtMillis = System.currentTimeMillis() + reminderMinutes * 60L * 1000L;

            alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
            );

            reminderScheduled = true;
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
