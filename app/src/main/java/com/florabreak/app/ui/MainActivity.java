package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

public class MainActivity extends AppCompatActivity {

    private Button showBreakButton;
    private Button refreshButton;

    private TextView stressScoreText;
    private TextView stressLabelText;
    private TextView recentBreakTitleText;
    private TextView recentBreaksText;

    private StressGaugeView stressGaugeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verbindet diese Java-Datei mit dem Layout activity_main.xml
        setContentView(R.layout.activity_main);
        showBreakButton = findViewById(R.id.showBreakButton);
        refreshButton = findViewById(R.id.refreshButton);

        stressGaugeView = findViewById(R.id.stressGaugeView);
        stressScoreText = findViewById(R.id.stressScoreText);
        stressLabelText = findViewById(R.id.stressLabelText);
        recentBreakTitleText = findViewById(R.id.recentBreakTitleText);
        recentBreaksText = findViewById(R.id.recentBreaksText);

// Mock-Werte für die Vorschau
        float mockStress = 7.6f;

        stressGaugeView.setStressScore(mockStress);
        stressScoreText.setText(String.format(java.util.Locale.US, "%.1f", mockStress));
        stressLabelText.setText("Sehr gestresst");

        updateRecentBreaks();

        // Buttons aus dem Layout holen
        showBreakButton = findViewById(R.id.showBreakButton);
        refreshButton = findViewById(R.id.refreshButton);
        recentBreaksText = findViewById(R.id.recentBreaksText);
        updateRecentBreaks();

        // Wenn auf "Pause ansehen" geklickt wird,
        // wird der zweite Screen geöffnet.
        showBreakButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BreakSuggestionActivity.class);
            startActivity(intent);
        });

        // Beim Klick auf "Daten aktualisieren" zeigen wir erstmal nur eine Meldung.
        refreshButton.setOnClickListener(view -> {
            Toast.makeText(this, "Mock-Daten wurden aktualisiert", Toast.LENGTH_SHORT).show();
        });
    }
    private void updateRecentBreaks() {
        if (MockUiDataProvider.getSavedBreaks().isEmpty()) {
            recentBreakTitleText.setText("Keine Pause gespeichert");
            recentBreaksText.setText("Noch keine gespeicherten Pausen");
            return;
        }

        UiSavedBreak savedBreak = MockUiDataProvider.getSavedBreaks().get(0);
        recentBreakTitleText.setText(savedBreak.getTitle());
        recentBreaksText.setText(savedBreak.getDetails());
    }

}