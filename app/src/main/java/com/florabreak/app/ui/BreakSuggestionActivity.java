package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.FloraBreakController;
import com.florabreak.app.data.FloraBreakControllerFactory;
import com.florabreak.app.maps.RealMapsBreakService;
import com.florabreak.app.model.BreakRecommendation;
import com.florabreak.app.model.FloraBreakSessionResult;
import com.florabreak.app.model.StressResult;

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

    private FloraBreakController floraBreakController;
    private FloraBreakSessionResult sessionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_suggestion);

        floraBreakController = FloraBreakControllerFactory.create(this);

        bindViews();
        setupRouteSelection();
        setupButtons();

        loadControllerData();
        loadRealMapsRoutes();

        updateSelectedRoute();
    }

    private void bindViews() {
        backButton = findViewById(R.id.backButton);
        startBreakButton = findViewById(R.id.startBreakButton);

        suggestionStressScoreText = findViewById(R.id.suggestionStressScoreText);
        suggestionStressLabelText = findViewById(R.id.suggestionStressLabelText);

        routeOneNameText = findViewById(R.id.routeOneNameText);
        routeOneInfoText = findViewById(R.id.routeOneInfoText);
        routeOneTypeText = findViewById(R.id.routeOneTypeText);

        routeTwoNameText = findViewById(R.id.routeTwoNameText);
        routeTwoInfoText = findViewById(R.id.routeTwoInfoText);
        routeTwoTypeText = findViewById(R.id.routeTwoTypeText);

        routeOneCard = findViewById(R.id.routeOneCard);
        routeTwoCard = findViewById(R.id.routeTwoCard);
    }

    private void setupRouteSelection() {
        routeOneCard.setOnClickListener(view -> {
            selectedRouteIndex = 0;
            updateSelectedRoute();
        });

        routeTwoCard.setOnClickListener(view -> {
            selectedRouteIndex = 1;
            updateSelectedRoute();
        });
    }

    private void setupButtons() {
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

            if (sessionResult != null) {
                intent.putExtra("stressScore", sessionResult.getStressResult().getScore());
                intent.putExtra("stressLabel", sessionResult.getStressResult().getLabel());
                intent.putExtra("recommendationTitle", sessionResult.getBreakRecommendation().getTitle());
                intent.putExtra("recommendationType", sessionResult.getBreakRecommendation().getType());
            }

            startActivity(intent);
        });
    }

    private void loadControllerData() {
        sessionResult = floraBreakController.evaluateCurrentSituation();

        StressResult stressResult = sessionResult.getStressResult();
        BreakRecommendation recommendation = sessionResult.getBreakRecommendation();

        suggestionStressScoreText.setText(String.valueOf(stressResult.getScore()));
        suggestionStressLabelText.setText(
                stressResult.getLabel() + " · " + recommendation.getTitle()
        );
    }

    private void loadRealMapsRoutes() {
        routeOneNameText.setText("Route wird berechnet...");
        routeOneInfoText.setText("Standort, Grünfläche und Gehzeit werden geprüft.");
        routeOneTypeText.setText("🗺️ Google Maps");

        showControllerFallbackRoute();

        RealMapsBreakService realMapsBreakService = new RealMapsBreakService(this);

        realMapsBreakService.getBreakDecision(
                (recommendationTitle, recommendationText, routeResult, usedRealLocation, foundRealPlace, usedRealRoute) -> {
                    runOnUiThread(() -> {
                        routeOneNameText.setText(recommendationTitle);
                        routeOneInfoText.setText(recommendationText);

                        if (routeResult != null && routeResult.isReachable()) {
                            routeOneTypeText.setText("🌿 Urban Walk");
                        } else {
                            routeOneTypeText.setText("🌿 Fallback-Route");
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
                            showControllerFallbackRoute();
                        }
                    });
                }
        );
    }

    private void showControllerFallbackRoute() {
        if (sessionResult == null) {
            return;
        }

        BreakRecommendation recommendation = sessionResult.getBreakRecommendation();

        routeTwoNameText.setText(recommendation.getTitle());
        routeTwoInfoText.setText(recommendation.getDescription());
        routeTwoTypeText.setText(recommendation.getType());
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
