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
    private TextView recentBreaksText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verbindet diese Java-Datei mit dem Layout activity_main.xml
        setContentView(R.layout.activity_main);

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
            recentBreaksText.setText("Noch keine gespeicherten Pausen");
            return;
        }

        StringBuilder builder = new StringBuilder();

        for (UiSavedBreak savedBreak : MockUiDataProvider.getSavedBreaks()) {
            builder.append(savedBreak.getTitle())
                    .append("\n")
                    .append(savedBreak.getDetails())
                    .append("\n\n");
        }

        recentBreaksText.setText(builder.toString().trim());
    }
}