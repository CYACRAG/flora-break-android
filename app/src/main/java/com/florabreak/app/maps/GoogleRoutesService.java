package com.florabreak.app.maps;

import android.content.Context;

import androidx.annotation.NonNull;

import com.florabreak.app.R;
import com.florabreak.app.model.RouteResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GoogleRoutesService {

    public interface RouteCallback {
        void onRouteReady(RouteResult routeResult, boolean usedRealRoute);
    }

    private static final String ROUTES_API_URL =
            "https://routes.googleapis.com/directions/v2:computeRoutes";

    private static final int MAX_WALKING_TIME_MINUTES = 15;

    private final Context context;

    public GoogleRoutesService(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    public void calculateWalkingRoute(
            double startLatitude,
            double startLongitude,
            String destinationName,
            double destinationLatitude,
            double destinationLongitude,
            @NonNull RouteCallback callback
    ) {
        new Thread(() -> {
            try {
                String apiKey = context.getString(R.string.google_maps_key);

                if (apiKey == null || apiKey.trim().isEmpty()) {
                    callback.onRouteReady(
                            createFallbackRoute(destinationName, destinationLatitude, destinationLongitude),
                            false
                    );
                    return;
                }

                URL url = new URL(ROUTES_API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setDoOutput(true);

                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("X-Goog-Api-Key", apiKey);
                connection.setRequestProperty("X-Goog-FieldMask", "routes.duration,routes.distanceMeters");

                String requestBody = buildRequestBody(
                        startLatitude,
                        startLongitude,
                        destinationLatitude,
                        destinationLongitude
                );

                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();

                InputStream inputStream = responseCode >= 200 && responseCode < 300
                        ? connection.getInputStream()
                        : connection.getErrorStream();

                String responseText = readStream(inputStream);

                if (responseCode < 200 || responseCode >= 300) {
                    callback.onRouteReady(
                            createFallbackRoute(destinationName, destinationLatitude, destinationLongitude),
                            false
                    );
                    return;
                }

                int walkingTimeMinutes = parseWalkingTimeMinutes(responseText);
                boolean reachable = walkingTimeMinutes <= MAX_WALKING_TIME_MINUTES;

                RouteResult routeResult = new RouteResult(
                        destinationName,
                        destinationLatitude,
                        destinationLongitude,
                        walkingTimeMinutes,
                        reachable
                );

                callback.onRouteReady(routeResult, true);

            } catch (Exception exception) {
                callback.onRouteReady(
                        createFallbackRoute(destinationName, destinationLatitude, destinationLongitude),
                        false
                );
            }
        }).start();
    }

    private String buildRequestBody(
            double startLatitude,
            double startLongitude,
            double destinationLatitude,
            double destinationLongitude
    ) {
        return "{"
                + "\"origin\": {"
                + "\"location\": {"
                + "\"latLng\": {"
                + "\"latitude\": " + startLatitude + ","
                + "\"longitude\": " + startLongitude
                + "}"
                + "}"
                + "},"
                + "\"destination\": {"
                + "\"location\": {"
                + "\"latLng\": {"
                + "\"latitude\": " + destinationLatitude + ","
                + "\"longitude\": " + destinationLongitude
                + "}"
                + "}"
                + "},"
                + "\"travelMode\": \"WALK\","
                + "\"routingPreference\": \"ROUTING_PREFERENCE_UNSPECIFIED\","
                + "\"computeAlternativeRoutes\": false,"
                + "\"languageCode\": \"de-DE\","
                + "\"units\": \"METRIC\""
                + "}";
    }

    private int parseWalkingTimeMinutes(String responseText) throws Exception {
        JSONObject root = new JSONObject(responseText);
        JSONArray routes = root.getJSONArray("routes");

        if (routes.length() == 0) {
            return 99;
        }

        JSONObject firstRoute = routes.getJSONObject(0);
        String duration = firstRoute.getString("duration");

        int seconds = parseDurationSeconds(duration);
        int minutes = (int) Math.ceil(seconds / 60.0);

        return Math.max(minutes, 1);
    }

    private int parseDurationSeconds(String duration) {
        if (duration == null || !duration.endsWith("s")) {
            return 99 * 60;
        }

        String secondsText = duration.replace("s", "");

        try {
            return Integer.parseInt(secondsText);
        } catch (NumberFormatException exception) {
            return 99 * 60;
        }
    }

    private String readStream(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        )) {
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }

        return result.toString();
    }

    private RouteResult createFallbackRoute(
            String destinationName,
            double destinationLatitude,
            double destinationLongitude
    ) {
        int fallbackWalkingTimeMinutes = 12;
        boolean reachable = fallbackWalkingTimeMinutes <= MAX_WALKING_TIME_MINUTES;

        return new RouteResult(
                destinationName,
                destinationLatitude,
                destinationLongitude,
                fallbackWalkingTimeMinutes,
                reachable
        );
    }
}