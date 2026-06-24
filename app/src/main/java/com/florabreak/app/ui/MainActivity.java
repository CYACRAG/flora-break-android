package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.FloraBreakController;
import com.florabreak.app.health.MockHealthDataProvider;
import com.florabreak.app.maps.MockRouteProvider;
import com.florabreak.app.model.BreakRecommendation;
import com.florabreak.app.model.StressResult;

public class MainActivity extends AppCompatActivity {

    private TextView greetingText;
    private TextView stressLabelText;
    private TextView stressScoreText;

    private Button showBreakButton;
    private Button refreshButton;

    private FloraBreakController controller;
    private StressResult currentStressResult;
    private BreakRecommendation currentRecommendation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI-Elemente aus dem Layout holen
        greetingText = findViewById(R.id.greetingText);
        stressLabelText = findViewById(R.id.stressLabelText);
        stressScoreText = findViewById(R.id.stressScoreText);
        showBreakButton = findViewById(R.id.showBreakButton);
        refreshButton = findViewById(R.id.refreshButton);

        // Controller verbindet Mock-Health-Daten, Stress Engine und Mock-Route.
        controller = new FloraBreakController(
                new MockHealthDataProvider(),
                new MockRouteProvider()
        );

        // Erste Berechnung direkt beim Start
        updateStressData();

        showBreakButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BreakSuggestionActivity.class);
            startActivity(intent);
        });

        refreshButton.setOnClickListener(view -> {
            updateStressData();

            Toast.makeText(
                    this,
                    "Daten aktualisiert: " + currentStressResult.getScore() + "/6",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void updateStressData() {
        currentStressResult = controller.getCurrentStressResult();
        currentRecommendation = controller.getCurrentBreakRecommendation();

        greetingText.setText("Willkommen bei Flora Break");
        stressScoreText.setText(currentStressResult.getScore() + " / 6");
        stressLabelText.setText(currentStressResult.getLabel());

        if (currentStressResult.isBreakRecommended()) {
            showBreakButton.setText(currentRecommendation.getTitle());
        } else {
            showBreakButton.setText("Pause ansehen");
        }
    }
}
