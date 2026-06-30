package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.repository.DemoStressSettingsRepository;
import com.florabreak.app.health.HealthConnectDataProvider;
import com.florabreak.app.model.DemoStressSettings;

/**
 * Profil-Screen für Flora Break.
 *
 * Enthält neben den Profil-Platzhaltern auch Demo-Regler für Stressdaten.
 * Diese Demo-Werte werden im DemoStressSettingsRepository gespeichert und später
 * vom DemoStressDataProvider gelesen.
 */
public class ProfileActivity extends AppCompatActivity {

    private static final int HRV_MIN = 30;
    private static final int HEART_RATE_MIN = 50;

    private TextView backFromProfileButton;
    private Button editProfileButton;

    private LinearLayout navHomeFromProfile;
    private LinearLayout navHistoryFromProfile;
    private LinearLayout navStatsFromProfile;

    private TextView healthConnectStatusText;
    private TextView stressBaselineText;

    private Switch demoModeSwitch;
    private SeekBar currentHrvSeekBar;
    private SeekBar normalHrvSeekBar;
    private SeekBar heartRateSeekBar;

    private TextView currentHrvValueText;
    private TextView normalHrvValueText;
    private TextView heartRateValueText;
    private TextView demoStressPreviewText;

    private DemoStressSettingsRepository demoStressSettingsRepository;
    private DemoStressSettings currentSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        demoStressSettingsRepository = new DemoStressSettingsRepository(this);

        bindViews();
        loadHealthConnectStatus();
        loadDemoSettingsIntoUi();
        setupDemoSliderListeners();
        setupNavigation();
    }

    private void bindViews() {
        backFromProfileButton = findViewById(R.id.backFromProfileButton);
        editProfileButton = findViewById(R.id.editProfileButton);

        navHomeFromProfile = findViewById(R.id.navHomeFromProfile);
        navHistoryFromProfile = findViewById(R.id.navHistoryFromProfile);
        navStatsFromProfile = findViewById(R.id.navStatsFromProfile);

        healthConnectStatusText = findViewById(R.id.healthConnectStatusText);
        stressBaselineText = findViewById(R.id.stressBaselineText);

        demoModeSwitch = findViewById(R.id.demoModeSwitch);
        currentHrvSeekBar = findViewById(R.id.currentHrvSeekBar);
        normalHrvSeekBar = findViewById(R.id.normalHrvSeekBar);
        heartRateSeekBar = findViewById(R.id.heartRateSeekBar);

        currentHrvValueText = findViewById(R.id.currentHrvValueText);
        normalHrvValueText = findViewById(R.id.normalHrvValueText);
        heartRateValueText = findViewById(R.id.heartRateValueText);
        demoStressPreviewText = findViewById(R.id.demoStressPreviewText);
    }

    private void loadHealthConnectStatus() {
        if (HealthConnectDataProvider.isAvailable(this)) {
            healthConnectStatusText.setText(
                    "Health Connect ist auf diesem Gerät verfügbar. Für den Emulator kann zusätzlich der Demo-Modus genutzt werden."
            );
        } else {
            healthConnectStatusText.setText(
                    "Health Connect ist auf diesem Gerät nicht verfügbar. Für den Prototyp wird der Demo-Modus genutzt."
            );
        }
    }

    private void loadDemoSettingsIntoUi() {
        currentSettings = demoStressSettingsRepository.getSettings();

        demoModeSwitch.setChecked(currentSettings.isDemoModeEnabled());

        currentHrvSeekBar.setProgress((int) currentSettings.getCurrentHrv() - HRV_MIN);
        normalHrvSeekBar.setProgress((int) currentSettings.getNormalHrv() - HRV_MIN);
        heartRateSeekBar.setProgress(currentSettings.getHeartRate() - HEART_RATE_MIN);

        updateDemoValueTexts();
        updateDemoPreviewText();

        stressBaselineText.setText(
                "Demo-Werte aktiv: HRV aktuell "
                        + (int) currentSettings.getCurrentHrv()
                        + ", normale HRV "
                        + (int) currentSettings.getNormalHrv()
                        + ", Puls "
                        + currentSettings.getHeartRate()
                        + " bpm."
        );
    }

    private void setupDemoSliderListeners() {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateDemoValueTexts();
                updateDemoPreviewText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nicht benötigt.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                saveDemoSettings();
            }
        };

        currentHrvSeekBar.setOnSeekBarChangeListener(listener);
        normalHrvSeekBar.setOnSeekBarChangeListener(listener);
        heartRateSeekBar.setOnSeekBarChangeListener(listener);

        demoModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> saveDemoSettings());

        editProfileButton.setOnClickListener(view -> {
            saveDemoSettings();
            Toast.makeText(this, "Demo-Stresswerte gespeichert", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveDemoSettings() {
        DemoStressSettings newSettings = new DemoStressSettings(
                demoModeSwitch.isChecked(),
                getCurrentHrvFromSlider(),
                getNormalHrvFromSlider(),
                getHeartRateFromSlider(),
                currentSettings.getSystolicBloodPressure(),
                currentSettings.getDiastolicBloodPressure()
        );

        demoStressSettingsRepository.saveSettings(newSettings);
        currentSettings = newSettings;

        updateDemoValueTexts();
        updateDemoPreviewText();

        stressBaselineText.setText(
                "Gespeichert: HRV aktuell "
                        + (int) newSettings.getCurrentHrv()
                        + ", normale HRV "
                        + (int) newSettings.getNormalHrv()
                        + ", Puls "
                        + newSettings.getHeartRate()
                        + " bpm."
        );
    }

    private void updateDemoValueTexts() {
        currentHrvValueText.setText("Aktuelle HRV: " + (int) getCurrentHrvFromSlider());
        normalHrvValueText.setText("Normale HRV: " + (int) getNormalHrvFromSlider());
        heartRateValueText.setText("Puls: " + getHeartRateFromSlider() + " bpm");
    }

    private void updateDemoPreviewText() {
        double currentHrv = getCurrentHrvFromSlider();
        double normalHrv = getNormalHrvFromSlider();

        double ratio = 0.0;

        if (normalHrv > 0) {
            ratio = currentHrv / normalHrv;
        }

        String preview;

        if (!demoModeSwitch.isChecked()) {
            preview = "Demo-Modus aus: später kann Health Connect verwendet werden.";
        } else if (ratio < 1.1) {
            preview = "Vorschau: niedriger Stressbereich.";
        } else if (ratio < 1.2) {
            preview = "Vorschau: mittlerer Stressbereich.";
        } else {
            preview = "Vorschau: hoher Stressbereich, Pause wahrscheinlich.";
        }

        demoStressPreviewText.setText(preview);
    }

    private double getCurrentHrvFromSlider() {
        return HRV_MIN + currentHrvSeekBar.getProgress();
    }

    private double getNormalHrvFromSlider() {
        return HRV_MIN + normalHrvSeekBar.getProgress();
    }

    private int getHeartRateFromSlider() {
        return HEART_RATE_MIN + heartRateSeekBar.getProgress();
    }

    private void setupNavigation() {
        backFromProfileButton.setOnClickListener(view -> finish());

        navHomeFromProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        navHistoryFromProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        navStatsFromProfile.setOnClickListener(view ->
                Toast.makeText(this, "Statistik wird später ergänzt", Toast.LENGTH_SHORT).show()
        );
    }
}
