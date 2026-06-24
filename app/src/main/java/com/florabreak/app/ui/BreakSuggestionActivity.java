package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.florabreak.R;

public class BreakSuggestionActivity extends AppCompatActivity {

    private Button backButton;
    private Button startBreakButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verbindet diese Java-Datei mit dem Layout activity_break_suggestion.xml
        setContentView(R.layout.activity_break_suggestion);

        backButton = findViewById(R.id.backButton);
        startBreakButton = findViewById(R.id.startBreakButton);

        // Zurück zum Dashboard
        backButton.setOnClickListener(view -> finish());

        // Öffnet den aktiven Pausen-Screen
        startBreakButton.setOnClickListener(view -> {
            Intent intent = new Intent(BreakSuggestionActivity.this, ActiveBreakActivity.class);
            startActivity(intent);
        });
    }
}