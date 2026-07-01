package com.florabreak.app.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.CancellationTokenSource;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class DeviceLocationService {

    public interface LocationCallback {
        void onLocationReady(double latitude, double longitude, boolean isRealLocation);
    }

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;

    public DeviceLocationService(@NonNull Context context) {
        this.context = context.getApplicationContext();
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getCurrentLocation(@NonNull LocationCallback callback) {
        if (!hasLocationPermission()) {
            callback.onLocationReady(0.0, 0.0, false);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        callback.onLocationReady(
                                location.getLatitude(),
                                location.getLongitude(),
                                true
                        );
                    } else {
                        requestFreshLocation(callback);
                    }
                })
                .addOnFailureListener(error -> requestFreshLocation(callback));
    }

    private void requestFreshLocation(@NonNull LocationCallback callback) {
        if (!hasLocationPermission()) {
            callback.onLocationReady(0.0, 0.0, false);
            return;
        }

        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

        fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        cancellationTokenSource.getToken()
                )
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        callback.onLocationReady(
                                location.getLatitude(),
                                location.getLongitude(),
                                true
                        );
                    } else {
                        callback.onLocationReady(0.0, 0.0, false);
                    }
                })
                .addOnFailureListener(error ->
                        callback.onLocationReady(0.0, 0.0, false)
                );
    }

    private boolean hasLocationPermission() {
        boolean fineLocationGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocationGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        return fineLocationGranted || coarseLocationGranted;
    }

    public String createLocationKey(double latitude, double longitude, boolean isRealLocation) {
        if (!isRealLocation) {
            return "no_real_location";
        }

        double roundedLatitude = Math.round(latitude * 100.0) / 100.0;
        double roundedLongitude = Math.round(longitude * 100.0) / 100.0;

        return roundedLatitude + "_" + roundedLongitude;
    }

    public boolean isFallbackLocation(double latitude, double longitude) {
        return !isValidLocation(latitude, longitude);
    }

    public boolean isValidLocation(double latitude, double longitude) {
        return !(latitude == 0.0 && longitude == 0.0);
    }
}
