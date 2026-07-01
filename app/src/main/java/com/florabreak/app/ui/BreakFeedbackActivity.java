package com.florabreak.app.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.florabreak.app.data.local.BreakEntity;
import com.florabreak.app.data.repository.RouteCacheRepository;
import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.local.BreakEntity;
import com.florabreak.app.data.repository.BreakSessionRepository;
import com.florabreak.app.data.repository.DemoStressSettingsRepository;
import com.florabreak.app.model.DemoStressSettings;

public class BreakFeedbackActivity extends AppCompatActivity {

    private TextView backToBreakButton;
    private Button saveBreakButton;
    private Button backHomeButton;

    private TextView feedbackDurationText;
    private TextView feedbackDistanceText;
    private TextView feedbackStressChangeText;
    private TextView stressBeforeText;
    private TextView stressAfterText;
    private RouteCacheRepository routeCacheRepository;
    private TextView star1;
    private TextView star2;
    private TextView star3;
    private TextView star4;
    private TextView star5;
    private TextView ratingHintText;

    private int selectedRating = 0;
    private long breakSessionId = -1L;
    private int elapsedDurationMinutes = 5;

    private int stressBefore = 0;
    private int stressAfter = 4;

    private BreakSessionRepository breakSessionRepository;
    private DemoStressSettingsRepository demoStressSettingsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_feedback);

        breakSessionRepository = new BreakSessionRepository(this);
	routeCacheRepository = new RouteCacheRepository(this);
        demoStressSettingsRepository = new DemoStressSettingsRepository(this);

        readIntentData();
        bindViews();
        loadBreakSummary();
        setupRatingStars();
        setupButtons();
    }

    private void readIntentData() {
        breakSessionId = getIntent().getLongExtra("breakSessionId", -1L);
        elapsedDurationMinutes = getIntent().getIntExtra("elapsedDurationMinutes", 5);

        if (elapsedDurationMinutes <= 0) {
            elapsedDurationMinutes = 5;
        }
    }

    private void bindViews() {
        backToBreakButton = findViewById(R.id.backToBreakButton);
        saveBreakButton = findViewById(R.id.saveBreakButton);
        backHomeButton = findViewById(R.id.backHomeButton);

        feedbackDurationText = findViewById(R.id.feedbackDurationText);
        feedbackDistanceText = findViewById(R.id.feedbackDistanceText);
        feedbackStressChangeText = findViewById(R.id.feedbackStressChangeText);
        stressBeforeText = findViewById(R.id.stressBeforeText);
        stressAfterText = findViewById(R.id.stressAfterText);

        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);

        ratingHintText = findViewById(R.id.ratingHintText);
    }

    private void loadBreakSummary() {
        BreakEntity breakEntity = null;

        if (breakSessionId > 0) {
            breakEntity = breakSessionRepository.getBreakById(breakSessionId);
        }

        if (breakEntity != null) {
            stressBefore = breakEntity.stressScore;
        } else {
            stressBefore = 0;
        }

        stressAfter = calculateStressAfterPause(stressBefore);

        feedbackDurationText.setText(String.valueOf(elapsedDurationMinutes));
        feedbackDistanceText.setText("—");

        int change = stressAfter - stressBefore;

        if (change < 0) {
            feedbackStressChangeText.setText(String.valueOf(change));
        } else if (change > 0) {
            feedbackStressChangeText.setText("+" + change);
        } else {
            feedbackStressChangeText.setText("0");
        }

        stressBeforeText.setText("Vorher: " + stressBefore);
        stressAfterText.setText("Nachher: " + stressAfter);
    }

    private int calculateStressAfterPause(int before) {
        DemoStressSettings settings = demoStressSettingsRepository.getSettings();

        if (settings.isDemoModeEnabled()) {
            return 4;
        }

        // Später im echten Betrieb: nach der Pause Health Connect erneut abfragen.
        if (before <= 0) {
            return 0;
        }

        return Math.max(0, before - 2);
    }

    private void setupRatingStars() {
        star1.setOnClickListener(view -> setRating(1));
        star2.setOnClickListener(view -> setRating(2));
        star3.setOnClickListener(view -> setRating(3));
        star4.setOnClickListener(view -> setRating(4));
        star5.setOnClickListener(view -> setRating(5));
    }

    private void setupButtons() {
        backToBreakButton.setOnClickListener(view -> finish());

        saveBreakButton.setOnClickListener(view -> {
            saveCompletedBreak();

            String message;

            if (selectedRating > 0) {
                message = "Pause gespeichert: " + selectedRating + " Sterne";
            } else {
                message = "Pause gespeichert";
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            goBackHome();
        });

        backHomeButton.setOnClickListener(view -> goBackHome());
    }
	private void saveCompletedBreak() {
	    if (breakSessionId <= 0) {
	        Toast.makeText(
	                this,
	                "Keine aktive Pause gefunden. Feedback konnte nicht gespeichert werden.",
	                Toast.LENGTH_LONG
	        ).show();
	        return;
	    }

	    BreakEntity breakEntity = breakSessionRepository.getBreakById(breakSessionId);

	    breakSessionRepository.finishBreak(
	            breakSessionId,
	            elapsedDurationMinutes,
	            selectedRating,
	            getFeedbackTextForRating(selectedRating)
	    );

	    if (breakEntity != null && selectedRating > 0) {
	        routeCacheRepository.handleRouteFeedback(
	                breakEntity.routeName,
	                breakEntity.routeLatitude,
	                breakEntity.routeLongitude,
	                selectedRating
	        );
	    }
	}

    private String getFeedbackTextForRating(int rating) {
        switch (rating) {
            case 1:
                return "Eher nicht erholsam";
            case 2:
                return "War okay";
            case 3:
                return "Ganz gut";
            case 4:
                return "Hat gut getan";
            case 5:
                return "Richtig erholsam";
            default:
                return "Keine Bewertung abgegeben";
        }
    }

    private void goBackHome() {
        Intent intent = new Intent(BreakFeedbackActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setRating(int rating) {
        selectedRating = rating;

        TextView[] stars = {star1, star2, star3, star4, star5};

        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setText("★");
                stars[i].setTextColor(Color.parseColor("#E5A93D"));
            } else {
                stars[i].setText("☆");
                stars[i].setTextColor(Color.parseColor("#C7D1C9"));
            }
        }

        updateRatingHint(rating);
    }

    private void updateRatingHint(int rating) {
        switch (rating) {
            case 1:
                ratingHintText.setText("Eher nicht erholsam");
                break;
            case 2:
                ratingHintText.setText("War okay");
                break;
            case 3:
                ratingHintText.setText("Ganz gut");
                break;
            case 4:
                ratingHintText.setText("Hat gut getan");
                break;
            case 5:
                ratingHintText.setText("Richtig erholsam");
                break;
            default:
                ratingHintText.setText("Tippe auf die Sterne");
                break;
        }
    }
}
