package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

public class MainActivity extends AppCompatActivity {

    private Button showBreakButton;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verbindet diese Java-Datei mit dem Layout activity_main.xml
        setContentView(R.layout.activity_main);

        // Buttons aus dem Layout holen
        showBreakButton = findViewById(R.id.showBreakButton);
        refreshButton = findViewById(R.id.refreshButton);

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
}