package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

public class ActiveBreakActivity extends AppCompatActivity {

    private TextView timerText;
    private TextView selectedRouteNameText;
    private TextView navigationMainText;
    private TextView navigationSubText;
    private TextView remainingTimeText;
    private TextView distanceText;

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
            navigationMainText.setText("Fallback-Route aktiv");
            navigationSubText.setText("Demo-/Fallback-Weg bis " + selectedRouteName);
            distanceText.setText("Fallback");
        } else {
            navigationMainText.setText("Pause aktiv");
            navigationSubText.setText("Route: " + selectedRouteName);
            distanceText.setText("—");
        }
    }

    private void setupButtons() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        timerRunning = false;
        handler.removeCallbacks(timerRunnable);
    }
}
