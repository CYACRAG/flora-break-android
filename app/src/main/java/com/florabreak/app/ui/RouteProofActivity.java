package com.florabreak.app.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

public class RouteProofActivity extends AppCompatActivity {

    private TextView backFromProofButton;
    private Button captureProofButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_proof);

        backFromProofButton = findViewById(R.id.backFromProofButton);
        captureProofButton = findViewById(R.id.captureProofButton);

        backFromProofButton.setOnClickListener(view -> finish());

        captureProofButton.setOnClickListener(view -> {
            Toast.makeText(this, "Streckenbeweis wurde aufgenommen", Toast.LENGTH_SHORT).show();

            // Aktuell nur Placeholder: später kann hier die echte Kamera/API angebunden werden.
            finish();
        });
    }
}