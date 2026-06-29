package com.florabreak.app.health;

import android.content.Context;

import androidx.health.connect.client.HealthConnectClient;
import androidx.health.connect.client.permission.HealthPermission;
import androidx.health.connect.client.records.HeartRateRecord;
import androidx.health.connect.client.records.metadata.DataOrigin;
import androidx.health.connect.client.request.ReadRecordsRequest;
import androidx.health.connect.client.response.ReadRecordsResponse;
import androidx.health.connect.client.time.TimeRangeFilter;

import com.florabreak.app.data.HealthDataProvider;
import com.florabreak.app.model.StressData;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;

public class HealthConnectDataProvider implements HealthDataProvider {

    private final HealthConnectClient healthConnectClient;

    public HealthConnectDataProvider(Context context) {
        this.healthConnectClient = HealthConnectClient.getOrCreate(context);
    }

    public static boolean isAvailable(Context context) {
        int status = HealthConnectClient.getSdkStatus(context);
        return status == HealthConnectClient.SDK_AVAILABLE;
    }

    public static Set<String> getRequiredPermissions() {
        Set<String> permissions = new HashSet<>();

        permissions.add(
                HealthPermission.getReadPermission(
                        (KClass<HeartRateRecord>) JvmClassMappingKt.getKotlinClass(HeartRateRecord.class)
                )
        );

        return permissions;
    }

    @Override
    public StressData getCurrentStressData() {
        int heartRate = 80;

        // Blutdruck bleibt erstmal Fallback,
        // damit die App stabil baut und Health Connect erstmal Puls lesen kann.
        int systolic = 120;
        int diastolic = 80;

        try {
            Instant now = Instant.now();
            Instant oneHourAgo = now.minus(1, ChronoUnit.HOURS);

            ReadRecordsRequest<HeartRateRecord> heartRateRequest =
                    new ReadRecordsRequest<HeartRateRecord>(
                            (KClass<HeartRateRecord>) JvmClassMappingKt.getKotlinClass(HeartRateRecord.class),
                            TimeRangeFilter.between(oneHourAgo, now),
                            Collections.<DataOrigin>emptySet(),
                            false,
                            1000,
                            null
                    );

            ReadRecordsResponse<HeartRateRecord> heartRateResponse =
                    (ReadRecordsResponse<HeartRateRecord>) BuildersKt.runBlocking(
                            null,
                            (CoroutineScope scope,
                             kotlin.coroutines.Continuation<? super ReadRecordsResponse<HeartRateRecord>> continuation) ->
                                    healthConnectClient.readRecords(heartRateRequest, continuation)
                    );

            if (!heartRateResponse.getRecords().isEmpty()) {
                HeartRateRecord latestHeartRate =
                        heartRateResponse.getRecords().get(
                                heartRateResponse.getRecords().size() - 1
                        );

                if (!latestHeartRate.getSamples().isEmpty()) {
                    HeartRateRecord.Sample latestSample =
                            latestHeartRate.getSamples().get(
                                    latestHeartRate.getSamples().size() - 1
                            );

                    heartRate = (int) latestSample.getBeatsPerMinute();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new StressData(
                45.0,
                50.0,
                heartRate,
                systolic,
                diastolic,
                System.currentTimeMillis()
        );
    }
}