package com.florabreak.app.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class DeviceLocationService {

    public interface LocationCallback {
        void onLocationReady(double latitude, double longitude, boolean isRealLocation);
    }

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;

    // Fallback: Köln Innenstadt, falls Emulator/Standort nicht verfügbar ist
    private static final double FALLBACK_LATITUDE = 50.9375;
    private static final double FALLBACK_LONGITUDE = 6.9603;

    public DeviceLocationService(@NonNull Context context) {
        this.context = context.getApplicationContext();
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getCurrentLocation(@NonNull LocationCallback callback) {
        boolean fineLocationGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocationGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        if (!fineLocationGranted && !coarseLocationGranted) {
            callback.onLocationReady(FALLBACK_LATITUDE, FALLBACK_LONGITUDE, false);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> handleLocation(location, callback))
                .addOnFailureListener(error ->
                        callback.onLocationReady(FALLBACK_LATITUDE, FALLBACK_LONGITUDE, false)
                );
    }

    private void handleLocation(Location location, LocationCallback callback) {
        if (location == null) {
            callback.onLocationReady(FALLBACK_LATITUDE, FALLBACK_LONGITUDE, false);
            return;
        }

        callback.onLocationReady(
                location.getLatitude(),
                location.getLongitude(),
                true
        );
    }
}