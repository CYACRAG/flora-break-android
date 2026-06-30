package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.FloraBreakController;
import com.florabreak.app.data.FloraBreakControllerFactory;
import com.florabreak.app.model.FloraBreakSessionResult;
import com.florabreak.app.model.StressResult;
import com.florabreak.app.data.repository.BreakHistoryRepository;
import com.florabreak.app.model.SavedBreak;

public class MainActivity extends AppCompatActivity {

    private Button showBreakButton;
    private LinearLayout navHistoryButton;
    private LinearLayout navStatsButton;
    private LinearLayout navProfileButton;
    private Button refreshButton;

    private TextView stressScoreText;
    private TextView stressLabelText;
    private TextView recentBreakTitleText;
    private TextView recentBreaksText;

    private StressGaugeView stressGaugeView;
    private FloraBreakController floraBreakController;
    private BreakHistoryRepository breakHistoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floraBreakController = FloraBreakControllerFactory.create(this);
        breakHistoryRepository = new BreakHistoryRepository(this);

        bindViews();
        setupNavigation();
        updateStressFromController();
        updateRecentBreaks();

        refreshButton.setOnClickListener(view -> {
            updateStressFromController();
            Toast.makeText(this, "Stressdaten wurden aktualisiert", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Wenn im Profil Demo-Regler geändert wurden,
        // wird der Home-Screen beim Zurückkommen automatisch aktualisiert.
        floraBreakController = FloraBreakControllerFactory.create(this);
        updateStressFromController();
        updateRecentBreaks();
    }

    private void bindViews() {
        showBreakButton = findViewById(R.id.showBreakButton);
        refreshButton = findViewById(R.id.refreshButton);

        stressGaugeView = findViewById(R.id.stressGaugeView);
        stressScoreText = findViewById(R.id.stressScoreText);
        stressLabelText = findViewById(R.id.stressLabelText);

        recentBreakTitleText = findViewById(R.id.recentBreakTitleText);
        recentBreaksText = findViewById(R.id.recentBreaksText);

        navHistoryButton = findViewById(R.id.navHistoryButton);
        navStatsButton = findViewById(R.id.navStatsButton);
        navProfileButton = findViewById(R.id.navProfileButton);
    }

    private void setupNavigation() {
        showBreakButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BreakSuggestionActivity.class);
            startActivity(intent);
        });

        navHistoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        navStatsButton.setOnClickListener(view ->
                Toast.makeText(this, "Statistik wird später ergänzt", Toast.LENGTH_SHORT).show()
        );

        navProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void updateStressFromController() {
        FloraBreakSessionResult sessionResult =
                floraBreakController.evaluateCurrentSituation();

        StressResult stressResult = sessionResult.getStressResult();

        int stressScore = stressResult.getScore();

        stressGaugeView.setStressScore(stressScore);
        stressScoreText.setText(String.valueOf(stressScore));
        stressLabelText.setText(stressResult.getLabel());
    }

    private void updateRecentBreaks() {
        SavedBreak savedBreak = breakHistoryRepository.getLatestBreak();

        if (savedBreak == null) {
            recentBreakTitleText.setText("Keine Pause gespeichert");
            recentBreaksText.setText("Noch keine gespeicherten Pausen");
            return;
        }

        recentBreakTitleText.setText(savedBreak.getTitle());

        String details =
                savedBreak.getDurationMinutes()
                        + " Min · "
                        + savedBreak.getRouteName()
                        + " · Stress "
                        + savedBreak.getStressScore()
                        + "/10 · "
                        + createStarRating(savedBreak.getRating());

        recentBreaksText.setText(details);
    }

    private String createStarRating(int rating) {
        StringBuilder stars = new StringBuilder();

            for (int i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }

        return stars.toString();
   }
}
