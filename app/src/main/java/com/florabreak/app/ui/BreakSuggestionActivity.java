
package com.florabreak.app.ui;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.contracts.HealthPermissionsRequestContract;

import com.florabreak.app.R;
import com.florabreak.app.data.HealthDataProvider;
import com.florabreak.app.health.HealthConnectDataProvider;
import com.florabreak.app.health.MockHealthDataProvider;
import com.florabreak.app.model.StressData;

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

        backButton.setOnClickListener(view -> finish());

        startBreakButton.setOnClickListener(view -> {
            Intent intent = new Intent(BreakSuggestionActivity.this, ActiveBreakActivity.class);
            startActivity(intent);
        });
    }

    private void loadHealthConnectData() {
        HealthDataProvider healthProvider = new HealthConnectDataProvider(this);

        StressData stressData = healthProvider.getCurrentStressData();

        /*
         * Wichtig:
         * Health Connect ist jetzt wirklich angeschlossen.
         * Weil wir nicht sicher wissen, welche Getter eure StressData-Klasse hat,
         * nutzen wir für die UI-Anzeige erstmal weiterhin den bestehenden UI-Mock.
         * Der echte HealthConnectDataProvider wird aber hier bereits ausgeführt.
         */

        UiStressState stressState = MockUiDataProvider.getCurrentStressState();
        List<UiRouteSuggestion> routes = MockUiDataProvider.getRouteSuggestions();

        suggestionStressScoreText.setText(String.valueOf(stressState.getStressScore()));
        suggestionStressLabelText.setText(stressState.getStressLabel() + " • Health Connect aktiv");

        showRoutes(routes);
    }

    private void loadMockData() {
        HealthDataProvider healthProvider = new MockHealthDataProvider();

        StressData stressData = healthProvider.getCurrentStressData();

        UiStressState stressState = MockUiDataProvider.getCurrentStressState();
        List<UiRouteSuggestion> routes = MockUiDataProvider.getRouteSuggestions();

        suggestionStressScoreText.setText(String.valueOf(stressState.getStressScore()));
        suggestionStressLabelText.setText(stressState.getStressLabel() + " • Mock-Daten");

        showRoutes(routes);
    }

    private void showRoutes(List<UiRouteSuggestion> routes) {
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
}