package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

import java.util.List;
import com.florabreak.app.maps.RealMapsBreakService;
import com.florabreak.app.model.RouteResult;

public class BreakSuggestionActivity extends AppCompatActivity {

    private Button backButton;
    private Button startBreakButton;

    private TextView suggestionStressScoreText;
    private TextView suggestionStressLabelText;

    private TextView routeOneNameText;
    private TextView routeOneInfoText;
    private TextView routeOneTypeText;

    private TextView routeTwoNameText;
    private TextView routeTwoInfoText;
    private TextView routeTwoTypeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verbindet diese Java-Datei mit dem Layout activity_break_suggestion.xml
        setContentView(R.layout.activity_break_suggestion);

        // Buttons aus dem Layout holen
        backButton = findViewById(R.id.backButton);
        startBreakButton = findViewById(R.id.startBreakButton);

        // Stress-Anzeige aus dem Layout holen
        suggestionStressScoreText = findViewById(R.id.suggestionStressScoreText);
        suggestionStressLabelText = findViewById(R.id.suggestionStressLabelText);

        // Routen-Anzeigen aus dem Layout holen
        routeOneNameText = findViewById(R.id.routeOneNameText);
        routeOneInfoText = findViewById(R.id.routeOneInfoText);
        routeOneTypeText = findViewById(R.id.routeOneTypeText);

        routeTwoNameText = findViewById(R.id.routeTwoNameText);
        routeTwoInfoText = findViewById(R.id.routeTwoInfoText);
        routeTwoTypeText = findViewById(R.id.routeTwoTypeText);

        // Mock-Daten laden.
        // Später kommen diese Werte aus Stress Engine und Maps/OSM.
        UiStressState stressState = MockUiDataProvider.getCurrentStressState();


        // Stresswerte anzeigen
        suggestionStressScoreText.setText(String.valueOf(stressState.getStressScore()));
        suggestionStressLabelText.setText(stressState.getStressLabel());

        // Echte Maps-Empfehlung laden.
// Der Ablauf ist:
// Standort holen -> Grünfläche suchen -> Gehzeit berechnen -> Empfehlung anzeigen.
        routeOneNameText.setText("Route wird berechnet...");
        routeOneInfoText.setText("Standort, Grünfläche und Gehzeit werden geprüft.");
        routeOneTypeText.setText("🗺️ Google Maps");

        routeTwoNameText.setText("Fallback bereit");
        routeTwoInfoText.setText("Falls Google Maps keine Daten liefert, nutzt Flora Break eine sichere Ersatzempfehlung.");
        routeTwoTypeText.setText("🌿 / 🏠");

        RealMapsBreakService realMapsBreakService = new RealMapsBreakService(this);

        realMapsBreakService.getBreakDecision(
                (recommendationTitle, recommendationText, routeResult, usedRealLocation, foundRealPlace, usedRealRoute) -> {
                    runOnUiThread(() -> {
                        routeOneNameText.setText(recommendationTitle);
                        routeOneInfoText.setText(recommendationText);

                        if (routeResult != null && routeResult.isReachable()) {
                            routeOneTypeText.setText("🌿 Urban Walk");
                        } else {
                            routeOneTypeText.setText("🏠 Indoor-Pause");
                        }

                        if (routeResult != null) {
                            routeTwoNameText.setText(routeResult.getDestinationName());
                            routeTwoInfoText.setText(
                                    "Gehzeit: "
                                            + routeResult.getWalkingTimeMinutes()
                                            + " Minuten | Standort: "
                                            + (usedRealLocation ? "echt" : "Fallback")
                                            + " | Ort: "
                                            + (foundRealPlace ? "Google Places" : "Fallback")
                            );
                            routeTwoTypeText.setText(usedRealRoute ? "✅ Echte Route" : "🧪 Fallback-Route");
                        } else {
                            routeTwoNameText.setText("Keine Route verfügbar");
                            routeTwoInfoText.setText("Es konnte keine Route berechnet werden.");
                            routeTwoTypeText.setText("Fallback");
                        }
                    });
                }
        );

        // Zurück zum Dashboard
        backButton.setOnClickListener(view -> finish());

        // Öffnet den aktiven Pausen-Screen
        startBreakButton.setOnClickListener(view -> {
            Intent intent = new Intent(BreakSuggestionActivity.this, ActiveBreakActivity.class);
            startActivity(intent);
        });
    }
}