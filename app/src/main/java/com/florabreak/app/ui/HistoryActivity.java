package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.local.BreakEntity;
import com.florabreak.app.data.repository.BreakSessionRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private TextView backFromHistoryButton;
    private TextView historyHeaderText;

    private LinearLayout navHomeFromHistory;
    private LinearLayout navStatsFromHistory;
    private LinearLayout navProfileFromHistory;

    private LinearLayout historyListContainer;
    private LinearLayout historyEmptyCard;

    private BreakSessionRepository breakSessionRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        breakSessionRepository = new BreakSessionRepository(this);

        bindViews();
        updateHistory();
        setupNavigation();
    }

    private void bindViews() {
        backFromHistoryButton = findViewById(R.id.backFromHistoryButton);
        historyHeaderText = findViewById(R.id.historyHeaderText);

        navHomeFromHistory = findViewById(R.id.navHomeFromHistory);
        navStatsFromHistory = findViewById(R.id.navStatsFromHistory);
        navProfileFromHistory = findViewById(R.id.navProfileFromHistory);

        historyListContainer = findViewById(R.id.historyListContainer);
        historyEmptyCard = findViewById(R.id.historyEmptyCard);
    }

    private void setupNavigation() {
        backFromHistoryButton.setOnClickListener(view -> finish());

        navHomeFromHistory.setOnClickListener(view -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        navStatsFromHistory.setOnClickListener(view -> {
            Intent intent = new Intent(HistoryActivity.this, StatsActivity.class);
            startActivity(intent);
        });

        navProfileFromHistory.setOnClickListener(view -> {
            Intent intent = new Intent(HistoryActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void updateHistory() {
        List<BreakEntity> breaks = breakSessionRepository.getAllBreaks();

        if (breaks == null || breaks.isEmpty()) {
            historyHeaderText.setText("Gespeicherte Pausen");
            historyEmptyCard.setVisibility(View.VISIBLE);
            return;
        }

        historyHeaderText.setText(breaks.size() + " gespeicherte Pausen");
        historyEmptyCard.setVisibility(View.GONE);

        for (BreakEntity breakEntity : breaks) {
            historyListContainer.addView(createBreakCard(breakEntity));
        }
    }

    private View createBreakCard(BreakEntity breakEntity) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(android.view.Gravity.CENTER_VERTICAL);
        card.setBackgroundResource(R.drawable.bg_soft_card);
        card.setElevation(dp(3));
        card.setPadding(dp(16), dp(16), dp(16), dp(16));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dp(14));
        card.setLayoutParams(cardParams);

        TextView icon = new TextView(this);
        icon.setText("🌿");
        icon.setTextSize(26);
        icon.setGravity(android.view.Gravity.CENTER);
        icon.setBackgroundResource(R.drawable.bg_green_pill);

        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(54), dp(54));
        icon.setLayoutParams(iconParams);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        contentParams.setMargins(dp(14), 0, 0, 0);
        content.setLayoutParams(contentParams);

        TextView title = new TextView(this);
        title.setText(formatDate(breakEntity.startedAt));
        title.setTextSize(17);
        title.setTextColor(android.graphics.Color.parseColor("#06140C"));
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView details = new TextView(this);
        details.setText(
                breakEntity.durationMinutes
                        + " Min · "
                        + safeText(breakEntity.routeName, "Route")
                        + " · "
                        + formatRouteType(breakEntity.routeType)
        );
        details.setTextSize(14);
        details.setTextColor(android.graphics.Color.parseColor("#637568"));

        TextView rating = new TextView(this);
        rating.setText(createStarRating(breakEntity.rating));
        rating.setTextSize(15);
        rating.setTextColor(android.graphics.Color.parseColor("#D79B1E"));

        TextView stress = new TextView(this);
        String photoText = breakEntity.photoProofTaken
                ? " · Foto vorhanden"
                : " · kein Foto";

        stress.setText(
                "Stress: "
                        + breakEntity.stressScore
                        + "/10 · "
                        + safeText(breakEntity.stressLabel, "Unbekannt")
                        + photoText
        );
        stress.setTextSize(14);
        stress.setTextColor(android.graphics.Color.parseColor("#2F6B45"));

        content.addView(title);
        content.addView(details);
        content.addView(rating);
        content.addView(stress);

        card.addView(icon);
        card.addView(content);

        return card;
    }

    private String formatDate(long timestamp) {
        if (timestamp <= 0) {
            return "Gespeicherte Pause";
        }

        SimpleDateFormat formatter =
                new SimpleDateFormat("dd.MM.yyyy · HH:mm", Locale.GERMANY);

        return formatter.format(new Date(timestamp));
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private String safeText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }

        return value;
    }

    private String formatRouteType(String routeType) {
        if (routeType == null) {
            return "Aktive Pause";
        }

        if (routeType.contains("PARK")) {
            return "Parkroute";
        }

        if (routeType.contains("URBAN")) {
            return "Pausenroute";
        }

        return "Aktive Pause";
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
