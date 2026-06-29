package com.florabreak.app.ui;

import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

import java.util.List;

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
    private LinearLayout routeOneCard;
    private LinearLayout routeTwoCard;

    private int selectedRouteIndex = 0;

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
        routeOneCard = findViewById(R.id.routeOneCard);
        routeTwoCard = findViewById(R.id.routeTwoCard);

        // Mock-Daten laden.
        // Später kommen diese Werte aus Stress Engine und Maps/OSM.
        UiStressState stressState = MockUiDataProvider.getCurrentStressState();
        List<UiRouteSuggestion> routes = MockUiDataProvider.getRouteSuggestions();

        // Stresswerte anzeigen
        suggestionStressScoreText.setText(String.valueOf(stressState.getStressScore()));
        suggestionStressLabelText.setText(stressState.getStressLabel());

        // Routenvorschläge anzeigen
        if (routes.size() >= 2) {
            UiRouteSuggestion routeOne = routes.get(0);
            UiRouteSuggestion routeTwo = routes.get(1);

            routeOneNameText.setText(routeOne.getRouteName());
            routeOneInfoText.setText("📍 " + routeOne.getDistance() + "   ⏱ " + routeOne.getDuration());
            routeOneTypeText.setText("🌳  " + routeOne.getRouteType());

            routeTwoNameText.setText(routeTwo.getRouteName());
            routeTwoInfoText.setText("📍 " + routeTwo.getDistance() + "   ⏱ " + routeTwo.getDuration());

            if (routeTwo.isUrbanFallback()) {
                routeTwoTypeText.setText("🏙️  " + routeTwo.getRouteType());
            } else {
                routeTwoTypeText.setText("🌲  " + routeTwo.getRouteType());
            }
        }
        routeOneCard.setOnClickListener(view -> {
            selectedRouteIndex = 0;
            updateSelectedRoute();
        });

        routeTwoCard.setOnClickListener(view -> {
            selectedRouteIndex = 1;
            updateSelectedRoute();
        });

        updateSelectedRoute();
        // Zurück zum Dashboard
        backButton.setOnClickListener(view -> finish());


        startBreakButton.setOnClickListener(view -> {
            String selectedRouteName;

            if (selectedRouteIndex == 0) {
                selectedRouteName = routeOneNameText.getText().toString();
            } else {
                selectedRouteName = routeTwoNameText.getText().toString();
            }

            Toast.makeText(this, selectedRouteName + " ausgewählt", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(BreakSuggestionActivity.this, ActiveBreakActivity.class);
            intent.putExtra("selectedRouteName", selectedRouteName);
            startActivity(intent);
        });
    }
    private void updateSelectedRoute() {
        if (selectedRouteIndex == 0) {
            routeOneCard.setBackgroundResource(R.drawable.bg_route_selected);
            routeTwoCard.setBackgroundResource(R.drawable.bg_soft_card);
        } else {
            routeOneCard.setBackgroundResource(R.drawable.bg_soft_card);
            routeTwoCard.setBackgroundResource(R.drawable.bg_route_selected);
        }
    }
}