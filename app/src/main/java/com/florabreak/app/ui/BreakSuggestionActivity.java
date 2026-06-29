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
import com.florabreak.app.health.MockHealthDataProvider;
import com.florabreak.app.maps.MockRouteProvider;
import com.florabreak.app.model.BreakRecommendation;
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

    private FloraBreakController controller;
    private StressResult stressResult;
    private BreakRecommendation recommendation;

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

        controller = new FloraBreakController(
                new MockHealthDataProvider(),
                new MockRouteProvider()
        );

        loadControllerData();

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

    private void loadControllerData() {
        stressResult = controller.getCurrentStressResult();
        recommendation = controller.getCurrentBreakRecommendation();

        suggestionStressScoreText.setText(stressResult.getScore() + " / 6");
        suggestionStressLabelText.setText(stressResult.getLabel());

        routeOneNameText.setText(recommendation.getTitle());
        routeOneInfoText.setText("⏱ " + recommendation.getDurationMinutes() + " Minuten");
        routeOneTypeText.setText(getTypeLabel(recommendation.getType()));

        routeTwoNameText.setText("Indoor-Pause");
        routeTwoInfoText.setText("⏱ 5 Minuten");
        routeTwoTypeText.setText("🏠 Alternative Pause");
    }

    private String getTypeLabel(String type) {
        if ("URBAN_WALK".equals(type)) {
            return "🌳 Urban Walk";
        }

        if ("INDOOR_BREAK".equals(type)) {
            return "🏠 Indoor Break";
        }

        return "🌿 Pause";
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
