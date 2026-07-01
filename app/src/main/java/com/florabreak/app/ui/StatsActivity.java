package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.florabreak.app.data.local.BreakEntity;

import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.repository.BreakSessionRepository;

/**
 * Statistik-Screen.
 *
 * Liest echte Pausendaten aus der lokalen Room-Datenbank.
 * Die Daten sind damit auch für ein späteres HR-Dashboard vorbereitet.
 */
public class StatsActivity extends AppCompatActivity {

    private TextView backFromStatsButton;

    private TextView breakCountText;
    private TextView totalMinutesText;
    private TextView averageStressText;
    private TextView averageRatingText;
    private TextView photoProofCountText;
    private StressHistoryChartView stressHistoryChartView;
    private LinearLayout navHomeFromStats;
    private LinearLayout navHistoryFromStats;
    private LinearLayout navProfileFromStats;

    private BreakSessionRepository breakSessionRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        breakSessionRepository = new BreakSessionRepository(this);

        bindViews();
        updateStats();
        setupNavigation();
    }

    private void bindViews() {
        backFromStatsButton = findViewById(R.id.backFromStatsButton);
	stressHistoryChartView = findViewById(R.id.stressHistoryChartView);
        breakCountText = findViewById(R.id.breakCountText);
        totalMinutesText = findViewById(R.id.totalMinutesText);
        averageStressText = findViewById(R.id.averageStressText);
        averageRatingText = findViewById(R.id.averageRatingText);
        photoProofCountText = findViewById(R.id.photoProofCountText);

        navHomeFromStats = findViewById(R.id.navHomeFromStats);
        navHistoryFromStats = findViewById(R.id.navHistoryFromStats);
        navProfileFromStats = findViewById(R.id.navProfileFromStats);
    }

    private void updateStats() {
        int breakCount = breakSessionRepository.getBreakCountLastSevenDays();
        int totalMinutes = breakSessionRepository.getTotalBreakMinutesLastSevenDays();
        double averageStress = breakSessionRepository.getAverageStressLastSevenDays();
        double averageRating = breakSessionRepository.getAverageRatingLastSevenDays();
        int photoProofCount = breakSessionRepository.getPhotoProofCountLastSevenDays();

        breakCountText.setText(breakCount + " Pausen");
        totalMinutesText.setText(totalMinutes + " Minuten");
        averageStressText.setText(String.format(java.util.Locale.GERMANY, "%.1f/10", averageStress));
        averageRatingText.setText(String.format(java.util.Locale.GERMANY, "%.1f/5", averageRating));
        photoProofCountText.setText(photoProofCount + " Foto-Beweise");
	List<BreakEntity> breaks = breakSessionRepository.getBreaksLastMonth();
	stressHistoryChartView.setBreaks(breaks);
    }

    private void setupNavigation() {
        backFromStatsButton.setOnClickListener(view -> finish());

        navHomeFromStats.setOnClickListener(view -> {
            Intent intent = new Intent(StatsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        navHistoryFromStats.setOnClickListener(view -> {
            Intent intent = new Intent(StatsActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        navProfileFromStats.setOnClickListener(view -> {
            Intent intent = new Intent(StatsActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}
