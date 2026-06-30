package com.florabreak.app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.florabreak.app.R;
import com.florabreak.app.data.repository.DemoStressSettingsRepository;
import com.florabreak.app.data.repository.ProfileRepository;
import com.florabreak.app.health.HealthConnectDataProvider;
import com.florabreak.app.maps.DeviceLocationService;
import com.florabreak.app.model.DemoStressSettings;
import com.florabreak.app.model.UserProfile;

/**
 * Profil-Screen für Flora Break.
 *
 * Enthält:
 * - Profilanzeige
 * - Demo-Regler für Stressdaten
 * - Arbeitsort speichern für Maps-Routen
 */
public class ProfileActivity extends AppCompatActivity {

    private static final int HRV_MIN = 30;
    private static final int HEART_RATE_MIN = 50;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 91;

    private TextView backFromProfileButton;
    private Button editProfileButton;

    private LinearLayout navHomeFromProfile;
    private LinearLayout navHistoryFromProfile;
    private LinearLayout navStatsFromProfile;

    private TextView profileNameText;
    private TextView workTimeText;
    private TextView workloadText;

    private TextView healthConnectStatusText;
    private TextView stressBaselineText;

    private TextView workLocationStatusText;
    private Button saveWorkLocationButton;
    private Button clearWorkLocationButton;

    private Switch demoModeSwitch;
    private SeekBar currentHrvSeekBar;
    private SeekBar normalHrvSeekBar;
    private SeekBar heartRateSeekBar;

    private TextView currentHrvValueText;
    private TextView normalHrvValueText;
    private TextView heartRateValueText;
    private TextView demoStressPreviewText;

    private DemoStressSettingsRepository demoStressSettingsRepository;
    private ProfileRepository profileRepository;
    private DeviceLocationService deviceLocationService;

    private DemoStressSettings currentSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        demoStressSettingsRepository = new DemoStressSettingsRepository(this);
        profileRepository = new ProfileRepository(this);
        deviceLocationService = new DeviceLocationService(this);

        bindViews();
        loadProfileIntoUi();
        loadHealthConnectStatus();
        loadDemoSettingsIntoUi();
        setupDemoSliderListeners();
        setupWorkLocationButtons();
        setupNavigation();
    }

    private void bindViews() {
        backFromProfileButton = findViewById(R.id.backFromProfileButton);
        editProfileButton = findViewById(R.id.editProfileButton);

        navHomeFromProfile = findViewById(R.id.navHomeFromProfile);
        navHistoryFromProfile = findViewById(R.id.navHistoryFromProfile);
        navStatsFromProfile = findViewById(R.id.navStatsFromProfile);

        profileNameText = findViewById(R.id.profileNameText);
        workTimeText = findViewById(R.id.workTimeText);
        workloadText = findViewById(R.id.workloadText);

        healthConnectStatusText = findViewById(R.id.healthConnectStatusText);
        stressBaselineText = findViewById(R.id.stressBaselineText);

        workLocationStatusText = findViewById(R.id.workLocationStatusText);
        saveWorkLocationButton = findViewById(R.id.saveWorkLocationButton);
        clearWorkLocationButton = findViewById(R.id.clearWorkLocationButton);

        demoModeSwitch = findViewById(R.id.demoModeSwitch);
        currentHrvSeekBar = findViewById(R.id.currentHrvSeekBar);
        normalHrvSeekBar = findViewById(R.id.normalHrvSeekBar);
        heartRateSeekBar = findViewById(R.id.heartRateSeekBar);

        currentHrvValueText = findViewById(R.id.currentHrvValueText);
        normalHrvValueText = findViewById(R.id.normalHrvValueText);
        heartRateValueText = findViewById(R.id.heartRateValueText);
        demoStressPreviewText = findViewById(R.id.demoStressPreviewText);
    }

    private void loadProfileIntoUi() {
        UserProfile profile = profileRepository.getProfile();

        String displayName = profile.getName();

        if (displayName == null || displayName.trim().isEmpty()) {
            displayName = "Flora Nutzer";
        }

        profileNameText.setText(displayName);

        workTimeText.setText(
                "Arbeitszeit: "
                        + profile.getWorkStartTime()
                        + "–"
                        + profile.getWorkEndTime()
        );

        workloadText.setText(
                "Belastung: "
                        + profile.getSubjectiveStressLevel()
                        + "/10"
        );

        updateWorkLocationStatus();
    }

    private void updateWorkLocationStatus() {
        UserProfile profile = profileRepository.getProfile();

        if (profile.isWorkLocationSaved()) {
            workLocationStatusText.setText(
                    "Arbeitsort gespeichert: "
                            + profile.getWorkLocationName()
                            + " ("
                            + formatCoordinate(profile.getWorkLatitude())
                            + ", "
                            + formatCoordinate(profile.getWorkLongitude())
                            + ")"
            );
        } else {
            workLocationStatusText.setText(
                    "Noch kein Arbeitsort gespeichert. Maps nutzt aktuell den Gerätestandort."
            );
        }
    }

    private String formatCoordinate(double value) {
        return String.format(java.util.Locale.GERMANY, "%.4f", value);
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
            Toast.makeText(this, "Profil- und Demo-Werte gespeichert", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupWorkLocationButtons() {
        saveWorkLocationButton.setOnClickListener(view -> saveCurrentLocationAsWorkLocation());

        clearWorkLocationButton.setOnClickListener(view -> {
            profileRepository.clearWorkLocation();
            updateWorkLocationStatus();
            Toast.makeText(this, "Arbeitsort entfernt", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveCurrentLocationAsWorkLocation() {
        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return;
        }

        deviceLocationService.getCurrentLocation((latitude, longitude, isRealLocation) -> {
            if (!isRealLocation) {
                Toast.makeText(
                        this,
                        "Kein echter Standort verfügbar. Arbeitsort wurde nicht gespeichert.",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            profileRepository.saveWorkLocation(
                    "Arbeitsort",
                    latitude,
                    longitude
            );

            updateWorkLocationStatus();

            Toast.makeText(
                    this,
                    "Aktueller Standort wurde als Arbeitsort gespeichert",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private boolean hasLocationPermission() {
        boolean fineLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        return fineLocationGranted || coarseLocationGranted;
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

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveCurrentLocationAsWorkLocation();
            } else {
                Toast.makeText(
                        this,
                        "Standortberechtigung fehlt. Arbeitsort wurde nicht gespeichert.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
