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
    private Button finishBreakButton;

    private Handler handler = new Handler();
    private int seconds = 0;
    private boolean timerRunning = true;

    private Runnable timerRunnable = new Runnable() {
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

        // Verbindet diese Java-Datei mit dem Layout activity_active_break.xml
        setContentView(R.layout.activity_active_break);

        timerText = findViewById(R.id.timerText);
        finishBreakButton = findViewById(R.id.finishBreakButton);

        // Timer startet automatisch
        handler.postDelayed(timerRunnable, 1000);

        // Beim Klick auf "Beenden" wird der Timer gestoppt
        finishBreakButton.setOnClickListener(view -> {
            timerRunning = false;

            Intent intent = new Intent(ActiveBreakActivity.this, BreakFeedbackActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Verhindert, dass der Timer im Hintergrund weiterläuft
        timerRunning = false;
        handler.removeCallbacks(timerRunnable);
    }
}