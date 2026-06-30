package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.contracts.HealthPermissionsRequestContract;

import com.florabreak.app.R;
import com.florabreak.app.health.HealthConnectDataProvider;
import com.florabreak.app.maps.RealMapsBreakService;
import com.florabreak.app.model.RouteResult;

import java.util.List;
import java.util.Set;

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

    private ActivityResultLauncher<Set<? extends String>> healthPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_break_suggestion);

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

        healthPermissionLauncher = registerForActivityResult(
                new HealthPermissionsRequestContract(),
                grantedPermissions -> {
                    if (grantedPermissions.containsAll(HealthConnectDataProvider.getRequiredPermissions())) {
                        Toast.makeText(this, "Health Connect verbunden", Toast.LENGTH_SHORT).show();
                        loadHealthConnectData();
                    } else {
                        Toast.makeText(this, "Health Connect Berechtigung fehlt - Mock-Daten werden genutzt", Toast.LENGTH_LONG).show();
                        loadMockData();
                    }
                }
        );

        if (HealthConnectDataProvider.isAvailable(this)) {
            healthPermissionLauncher.launch(HealthConnectDataProvider.getRequiredPermissions());
        } else {
            Toast.makeText(this, "Health Connect nicht verfügbar - Mock-Daten werden genutzt", Toast.LENGTH_LONG).show();
            loadMockData();
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

    private void loadHealthConnectData() {
        HealthConnectDataProvider healthProvider = new HealthConnectDataProvider(this);

        // Ruft echte Health-Connect-Daten ab.
        // Aktuell nutzt die UI noch eine stabile Mock-Stressanzeige,
        // bis StressEngine/Controller final angebunden sind.
        healthProvider.getCurrentStressData();

        UiStressState stressState = MockUiDataProvider.getCurrentStressState();

        suggestionStressScoreText.setText(String.valueOf(stressState.getStressScore()));
        suggestionStressLabelText.setText(stressState.getStressLabel() + " - Health Connect aktiv");

        loadRealMapsRoutes();
    }

    private void loadMockData() {
        UiStressState stressState = MockUiDataProvider.getCurrentStressState();

        suggestionStressScoreText.setText(String.valueOf(stressState.getStressScore()));
        suggestionStressLabelText.setText(stressState.getStressLabel() + " - Mock-Daten");

        loadRealMapsRoutes();
    }

    private void loadRealMapsRoutes() {
        routeOneNameText.setText("Route wird berechnet...");
        routeOneInfoText.setText("Standort, Grünfläche und Gehzeit werden geprüft.");
        routeOneTypeText.setText("🗺️ Google Maps");

        routeTwoNameText.setText("Fallback bereit");
        routeTwoInfoText.setText("Falls Google Maps keine Daten liefert, nutzt Flora Break eine sichere Ersatzempfehlung.");
        routeTwoTypeText.setText("🌿 Fallback");

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
                            showMockRoutesAsFallback();
                        }
                    });
                }
        );
    }

    private void showMockRoutesAsFallback() {
        List<UiRouteSuggestion> routes = MockUiDataProvider.getRouteSuggestions();

        if (routes.size() >= 2) {
            UiRouteSuggestion routeOne = routes.get(0);
            UiRouteSuggestion routeTwo = routes.get(1);

            routeOneNameText.setText(routeOne.getRouteName());
            routeOneInfoText.setText(routeOne.getDistance() + "   " + routeOne.getDuration());
            routeOneTypeText.setText(routeOne.getRouteType());

            routeTwoNameText.setText(routeTwo.getRouteName());
            routeTwoInfoText.setText(routeTwo.getDistance() + "   " + routeTwo.getDuration());

            if (routeTwo.isUrbanFallback()) {
                routeTwoTypeText.setText("Fallback: " + routeTwo.getRouteType());
            } else {
                routeTwoTypeText.setText(routeTwo.getRouteType());
            }
        }
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
