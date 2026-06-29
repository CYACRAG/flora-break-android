package com.florabreak.app.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

/**
 * UI-Prototyp für den Streckenbeweis.
 *
 * Diese Activity nutzt aktuell KEINE echte Kamera.
 * Sie zeigt nur einen Kamera-Placeholder, damit der App-Flow demonstriert werden kann.
 *
 * Später kann hier eine echte Kamera-Funktion ergänzt werden, z.B.:
 * - Kamera öffnen
 * - Foto aufnehmen
 * - Naturmoment speichern
 * - Streckenbeweis an die Pause anhängen
 */
public class RouteProofActivity extends AppCompatActivity {

    private TextView backFromProofButton;
    private Button captureProofButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_proof);

        backFromProofButton = findViewById(R.id.backFromProofButton);
        captureProofButton = findViewById(R.id.captureProofButton);

        // Zurück zur aktiven Pause.
        backFromProofButton.setOnClickListener(view -> finish());

        /*
         * Placeholder-Aktion:
         * Aktuell wird nur symbolisch bestätigt, dass ein Streckenbeweis aufgenommen wurde.
         * Später wird hier die echte Kamera-Logik angebunden.
         */
        captureProofButton.setOnClickListener(view -> {
            Toast.makeText(this, "Streckenbeweis wurde aufgenommen", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}