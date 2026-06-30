package com.florabreak.app.maps;

import android.content.Context;

import androidx.annotation.NonNull;
import com.florabreak.app.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.CircularBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.SearchNearbyRequest;

import java.util.Arrays;
import java.util.List;

public class NearbyGreenSpaceService {

    public interface GreenSpaceCallback {
        void onGreenSpaceFound(String name, double latitude, double longitude, boolean foundRealPlace);
    }

    private static final int SEARCH_RADIUS_METERS = 1500;

    private final Context context;
    private final PlacesClient placesClient;

    public NearbyGreenSpaceService(@NonNull Context context) {
        this.context = context.getApplicationContext();

        String apiKey = this.context.getString(R.string.google_maps_key);

        if (!Places.isInitialized()) {
            Places.initialize(this.context, apiKey);
        }

        this.placesClient = Places.createClient(this.context);
    }

    public void findNearbyGreenSpace(
            double userLatitude,
            double userLongitude,
            @NonNull GreenSpaceCallback callback
    ) {
        LatLng center = new LatLng(userLatitude, userLongitude);
        CircularBounds searchArea = CircularBounds.newInstance(center, SEARCH_RADIUS_METERS);

        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.LOCATION
        );

        List<String> includedTypes = Arrays.asList(
                "park",
                "garden",
                "national_park"
        );

        SearchNearbyRequest request = SearchNearbyRequest.builder(searchArea, placeFields)
                .setIncludedTypes(includedTypes)
                .setMaxResultCount(5)
                .build();

        placesClient.searchNearby(request)
                .addOnSuccessListener(response -> {
                    List<Place> places = response.getPlaces();

                    if (places == null || places.isEmpty()) {
                        useFallbackGreenSpace(callback);
                        return;
                    }

                    Place firstPlace = places.get(0);
                    LatLng placeLocation = firstPlace.getLocation();

                    if (placeLocation == null) {
                        useFallbackGreenSpace(callback);
                        return;
                    }

                    String placeName = firstPlace.getDisplayName();

                    if (placeName == null || placeName.trim().isEmpty()) {
                        placeName = "Grünfläche in der Nähe";
                    }

                    callback.onGreenSpaceFound(
                            placeName,
                            placeLocation.latitude,
                            placeLocation.longitude,
                            true
                    );
                })
                .addOnFailureListener(error -> useFallbackGreenSpace(callback));
    }

    private void useFallbackGreenSpace(@NonNull GreenSpaceCallback callback) {
        // Fallback: Rheinpark Köln
        callback.onGreenSpaceFound(
                "Rheinpark Köln",
                50.9473,
                6.9828,
                false
        );
    }
}