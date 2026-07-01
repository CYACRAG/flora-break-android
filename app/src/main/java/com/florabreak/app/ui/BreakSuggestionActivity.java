package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
    private View stressMarkerView;
    private Button generateNewRouteButton;
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
    private String routeTwoName = "Weiter Pausenroute";

    private int routeOneWalkingTimeMinutes = 0;
    private int routeTwoWalkingTimeMinutes = 5;

    private String routeOneType = "MAPS_LOADING";
    private String routeTwoType = "Weitere Pausenroute";

    private double routeOneLatitude = 0.0;
    private double routeOneLongitude = 0.0;
    private double routeTwoLatitude = 0.0;
    private double routeTwoLongitude = 0.0;

    private int currentStressScore = 0;
    private String currentStressLabel = "Unbekannt";

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
        stressMarkerView = findViewById(R.id.stressMarkerView);

        routeOneNameText = findViewById(R.id.routeOneNameText);
        routeOneInfoText = findViewById(R.id.routeOneInfoText);
        routeOneTypeText = findViewById(R.id.routeOneTypeText);

        routeTwoNameText = findViewById(R.id.routeTwoNameText);
        routeTwoInfoText = findViewById(R.id.routeTwoInfoText);
        routeTwoTypeText = findViewById(R.id.routeTwoTypeText);
	generateNewRouteButton = findViewById(R.id.generateNewRouteButton);
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
	generateNewRouteButton.setOnClickListener(view -> {
	    generateAnotherPauseRoute();
	    selectedRouteIndex = 1;
	    updateSelectedRoute();

	    Toast.makeText(
	            this,
	            "Neue Pausenroute erstellt",
	            Toast.LENGTH_SHORT
	    ).show();
	});
        startBreakButton.setOnClickListener(view -> {
            String selectedRouteName;
            int selectedWalkingTime;
            String selectedRouteType;
            double selectedLatitude;
            double selectedLongitude;

            if (selectedRouteIndex == 0) {
                selectedRouteName = routeOneName;
                selectedWalkingTime = routeOneWalkingTimeMinutes;
                selectedRouteType = routeOneType;
                selectedLatitude = routeOneLatitude;
                selectedLongitude = routeOneLongitude;
            } else {
                selectedRouteName = routeTwoName;
                selectedWalkingTime = routeTwoWalkingTimeMinutes;
                selectedRouteType = routeTwoType;
                selectedLatitude = routeTwoLatitude;
                selectedLongitude = routeTwoLongitude;
            }

            if (selectedLatitude == 0.0 && selectedLongitude == 0.0) {
                Toast.makeText(
                        this,
                        "Für diese Route ist noch kein Kartenziel verfügbar.",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                Toast.makeText(this, selectedRouteName + " ausgewählt", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(BreakSuggestionActivity.this, ActiveBreakActivity.class);
            intent.putExtra("selectedRouteName", selectedRouteName);
            intent.putExtra("selectedWalkingTimeMinutes", selectedWalkingTime);
            intent.putExtra("selectedRouteType", selectedRouteType);
            intent.putExtra("selectedLatitude", selectedLatitude);
            intent.putExtra("selectedLongitude", selectedLongitude);
            intent.putExtra("stressScore", currentStressScore);
            intent.putExtra("stressLabel", currentStressLabel);

            if (sessionResult != null) {
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

        currentStressScore = stressResult.getScore();
        currentStressLabel = stressResult.getLabel();

        suggestionStressScoreText.setText(String.valueOf(currentStressScore));
        suggestionStressLabelText.setText(
                currentStressLabel + " · " + recommendation.getTitle()
        );

        updateStressMarker(currentStressScore);
    }

    private void updateStressMarker(int stressScore) {
        if (stressMarkerView == null) {
            return;
        }

        stressMarkerView.post(() -> {
            int barWidth = dp(240);
            int markerWidth = dp(18);

            float ratio = Math.max(0f, Math.min(10f, stressScore)) / 10f;
            int marginStart = Math.round(ratio * (barWidth - markerWidth));

            FrameLayout.LayoutParams params =
                    (FrameLayout.LayoutParams) stressMarkerView.getLayoutParams();

            params.leftMargin = marginStart;
            stressMarkerView.setLayoutParams(params);
        });
    }

    private void loadRealMapsRoutes() {
        routeOneName = "Route wird berechnet";
        routeOneWalkingTimeMinutes = 0;
        routeOneType = "MAPS_LOADING";

        routeOneNameText.setText("Route wird berechnet...");
        routeOneInfoText.setText("Standort, Grünfläche und Gehzeit werden geprüft.");
        routeOneTypeText.setText("Kartenziel");

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
            routeOneName = "Weitere Pausenroute";
            routeOneLatitude = 0.0;
            routeOneLongitude = 0.0;
            routeOneWalkingTimeMinutes = 5;
            routeOneType = "Weitere Pausenroute";

            routeOneNameText.setText("Weitere Pausenroute empfohlen");
            routeOneInfoText.setText("Es konnte kein Kartenziel berechnet werden. Starte einen kurzen Rundgang in deiner Umgebung.");
            routeOneTypeText.setText("Aktive Pause");

            showControllerFallbackRoute();
            return;
        }

        int oneWayMinutes = Math.max(1, routeResult.getWalkingTimeMinutes());
        int totalMinutes = oneWayMinutes * 2;

        routeOneName = routeResult.getDestinationName();
        routeOneLatitude = routeResult.getLatitude();
        routeOneLongitude = routeResult.getLongitude();
        routeOneWalkingTimeMinutes = totalMinutes;

        boolean hasCoordinates = !(routeOneLatitude == 0.0 && routeOneLongitude == 0.0);

        if (foundRealPlace && usedRealRoute && routeResult.isReachable()) {
            routeOneType = "PARK_ROUTE";

            routeOneNameText.setText("Parkroute empfohlen");
            routeOneInfoText.setText(
                    oneWayMinutes
                            + " Min hin · "
                            + totalMinutes
                            + " Min gesamt bis "
                            + routeResult.getDestinationName()
            );
            routeOneTypeText.setText("Grünfläche");
        } else if (hasCoordinates && usedRealRoute && routeResult.isReachable()) {
            routeOneType = "Weitere Pausenroute";

            routeOneNameText.setText("Weitere Pausenroute");
            routeOneInfoText.setText(
                    oneWayMinutes
                            + " Min hin · "
                            + totalMinutes
                            + " Min gesamt bis "
                            + routeResult.getDestinationName()
            );
            routeOneTypeText.setText("Aktive Route");
        } else if (hasCoordinates && usedRealRoute) {
            routeOneType = "LONG_ROUTE";

            routeOneNameText.setText("Route zu lang");
            routeOneInfoText.setText(
                    oneWayMinutes
                            + " Min hin · "
                            + totalMinutes
                            + " Min gesamt. Flora Break empfiehlt stattdessen eine kurze Alternative."
            );
            routeOneTypeText.setText("Zu lang");
        } else {
            routeOneType = "Weitere Pausenroute";

            routeOneNameText.setText("Andere Route empfohlen");
            routeOneInfoText.setText(recommendationText);
            routeOneTypeText.setText("Aktive Pause");
        }

        setSecondPauseRoute();
    }

    private void showControllerFallbackRoute() {
        if (sessionResult == null) {
            return;
        }

        BreakRecommendation recommendation = sessionResult.getBreakRecommendation();

        setSecondPauseRoute();
    }


    private void setSecondPauseRoute() {
        routeTwoName = "Weitere Pausenroute";
        routeTwoWalkingTimeMinutes = 5;
        routeTwoType = "Weitere Pausenroute";

        if (routeOneLatitude != 0.0 || routeOneLongitude != 0.0) {
            routeTwoLatitude = routeOneLatitude + 0.0035;
            routeTwoLongitude = routeOneLongitude - 0.0035;
        } else {
            routeTwoLatitude = 0.0;
            routeTwoLongitude = 0.0;
        }

        routeTwoNameText.setText("Weitere Route");
        routeTwoInfoText.setText("Eigene alternative Route. Öffnet ein anderes Ziel als Route 1.");
        routeTwoTypeText.setText("Pausenroute");
    }
	private void generateAnotherPauseRoute() {
	    routeTwoName = "Neue Pausenroute";
	    routeTwoWalkingTimeMinutes = 10;
	    routeTwoType = "PAUSE_ROUTE";

	    if (routeOneLatitude != 0.0 || routeOneLongitude != 0.0) {
	        long seed = System.currentTimeMillis() / 1000L;
	        int direction = (int) (seed % 4);

	        if (direction == 0) {
	            routeTwoLatitude = routeOneLatitude + 0.0045;
	            routeTwoLongitude = routeOneLongitude + 0.0015;
	        } else if (direction == 1) {
	            routeTwoLatitude = routeOneLatitude - 0.0045;
	            routeTwoLongitude = routeOneLongitude - 0.0015;
	        } else if (direction == 2) {
	            routeTwoLatitude = routeOneLatitude + 0.0015;
	            routeTwoLongitude = routeOneLongitude - 0.0045;
	        } else {
	            routeTwoLatitude = routeOneLatitude - 0.0015;
	            routeTwoLongitude = routeOneLongitude + 0.0045;
	        }
	    } else {
	        routeTwoLatitude = 0.0;
	        routeTwoLongitude = 0.0;
	    }

	    routeTwoNameText.setText("Neue Pausenroute");
	    routeTwoInfoText.setText("10 Min gesamt · Noch nicht bewertet");
	    routeTwoTypeText.setText("Pausenroute");
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

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
