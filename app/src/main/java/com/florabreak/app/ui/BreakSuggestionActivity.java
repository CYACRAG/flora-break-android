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
import com.florabreak.app.model.RouteResult;
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

    private String routeOneName = "Route wird berechnet";
    private String routeTwoName = "Fallback";
    private int routeOneWalkingTimeMinutes = 0;
    private int routeTwoWalkingTimeMinutes = 5;
    private String routeOneType = "ROUTE";
    private String routeTwoType = "FALLBACK";

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
            int selectedWalkingTime;
            String selectedRouteType;

            if (selectedRouteIndex == 0) {
                selectedRouteName = routeOneName;
                selectedWalkingTime = routeOneWalkingTimeMinutes;
                selectedRouteType = routeOneType;
            } else {
                selectedRouteName = routeTwoName;
                selectedWalkingTime = routeTwoWalkingTimeMinutes;
                selectedRouteType = routeTwoType;
            }

            Toast.makeText(this, selectedRouteName + " ausgewählt", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(BreakSuggestionActivity.this, ActiveBreakActivity.class);
            intent.putExtra("selectedRouteName", selectedRouteName);
            intent.putExtra("selectedWalkingTimeMinutes", selectedWalkingTime);
            intent.putExtra("selectedRouteType", selectedRouteType);

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
        routeOneName = "Route wird berechnet";
        routeOneWalkingTimeMinutes = 0;
        routeOneType = "MAPS_LOADING";

        routeOneNameText.setText("Route wird berechnet...");
        routeOneInfoText.setText("Standort, Grünfläche und Gehzeit werden geprüft.");
        routeOneTypeText.setText("🗺️ Google Maps");

        showControllerFallbackRoute();

        RealMapsBreakService realMapsBreakService = new RealMapsBreakService(this);

        realMapsBreakService.getBreakDecision(
                (recommendationTitle, recommendationText, routeResult, usedRealLocation, foundRealPlace, usedRealRoute) -> {
                    runOnUiThread(() -> showRealMapsResult(
                            recommendationTitle,
                            recommendationText,
                            routeResult,
                            usedRealLocation,
                            foundRealPlace,
                            usedRealRoute
                    ));
                }
        );
    }

    private void showRealMapsResult(
            String recommendationTitle,
            String recommendationText,
            RouteResult routeResult,
            boolean usedRealLocation,
            boolean foundRealPlace,
            boolean usedRealRoute
    ) {
        if (routeResult == null) {
            routeOneName = "Keine Route verfügbar";
            routeOneWalkingTimeMinutes = 0;
            routeOneType = "NO_ROUTE";

            routeOneNameText.setText("Keine Route verfügbar");
            routeOneInfoText.setText("Es konnte keine passende Route berechnet werden.");
            routeOneTypeText.setText("Fallback");

            showControllerFallbackRoute();
            return;
        }

        routeOneName = routeResult.getDestinationName();
        routeOneWalkingTimeMinutes = routeResult.getWalkingTimeMinutes();

        if (usedRealRoute && routeResult.isReachable()) {
            routeOneType = "REAL_URBAN_WALK";

            routeOneNameText.setText("Urban Walk empfohlen");
            routeOneInfoText.setText(
                    routeResult.getWalkingTimeMinutes()
                            + " Minuten bis "
                            + routeResult.getDestinationName()
                            + "."
            );
            routeOneTypeText.setText("✅ Echte Route");
        } else if (usedRealRoute) {
            routeOneType = "REAL_ROUTE_TOO_FAR";

            routeOneNameText.setText("Route gefunden, aber zu weit");
            routeOneInfoText.setText(
                    routeResult.getDestinationName()
                            + " ist ca. "
                            + routeResult.getWalkingTimeMinutes()
                            + " Minuten entfernt. Ziel sind 10–15 Minuten."
            );
            routeOneTypeText.setText("⚠️ Echte Route zu lang");
        } else if (routeResult.isReachable()) {
            routeOneType = "FALLBACK_URBAN_WALK";

            routeOneNameText.setText("Fallback-Route empfohlen");
            routeOneInfoText.setText(
                    routeResult.getWalkingTimeMinutes()
                            + " Minuten bis "
                            + routeResult.getDestinationName()
                            + "."
            );
            routeOneTypeText.setText("🧪 Fallback-Route");
        } else {
            routeOneType = "FALLBACK_ROUTE_TOO_FAR";

            routeOneNameText.setText("Keine passende Route");
            routeOneInfoText.setText(recommendationText);
            routeOneTypeText.setText("Fallback");
        }

        routeTwoName = routeResult.getDestinationName();
        routeTwoWalkingTimeMinutes = routeResult.getWalkingTimeMinutes();
        routeTwoType = usedRealRoute ? "REAL_ROUTE_INFO" : "FALLBACK_ROUTE_INFO";

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
    }

    private void showControllerFallbackRoute() {
        if (sessionResult == null) {
            return;
        }

        BreakRecommendation recommendation = sessionResult.getBreakRecommendation();

        routeTwoName = recommendation.getTitle();
        routeTwoWalkingTimeMinutes = recommendation.getDurationMinutes() > 0
                ? recommendation.getDurationMinutes()
                : 5;
        routeTwoType = recommendation.getType();

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
