package com.florabreak.app.ui;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.florabreak.app.R;

/**
 * Lokale Benachrichtigungen für aktive Flora-Break-Pausen.
 *
 * Hotlinks:
 * - Halbzeit öffnet direkt RouteProofActivity
 * - Ende öffnet direkt BreakFeedbackActivity
 */
public class BreakReminderReceiver extends BroadcastReceiver {

    public static final String EXTRA_REMINDER_TYPE = "reminderType";

    public static final String TYPE_PHOTO_PROOF = "PHOTO_PROOF";
    public static final String TYPE_END_BREAK = "END_BREAK";

    private static final String CHANNEL_ID = "flora_break_reminder_channel";

    private static final int NOTIFICATION_ID_PHOTO_PROOF = 2027;
    private static final int NOTIFICATION_ID_END_BREAK = 2028;

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        String reminderType = intent.getStringExtra(EXTRA_REMINDER_TYPE);
        long breakSessionId = intent.getLongExtra("breakSessionId", -1L);
        int plannedDurationMinutes = intent.getIntExtra("plannedDurationMinutes", 5);

        if (TYPE_PHOTO_PROOF.equals(reminderType)) {
            showPhotoProofNotification(context, breakSessionId);
        } else {
            showEndBreakNotification(context, breakSessionId, plannedDurationMinutes);
        }
    }

    private void showPhotoProofNotification(Context context, long breakSessionId) {
        Intent openProofIntent = new Intent(context, RouteProofActivity.class);
        openProofIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openProofIntent.putExtra("breakSessionId", breakSessionId);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                NOTIFICATION_ID_PHOTO_PROOF,
                openProofIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Flora Break Halbzeit")
                .setContentText("Halbzeit erreicht. Nimm jetzt dein Foto als Streckenbeweis auf.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        showNotification(context, NOTIFICATION_ID_PHOTO_PROOF, builder);
    }

    private void showEndBreakNotification(
            Context context,
            long breakSessionId,
            int plannedDurationMinutes
    ) {
        Intent openFeedbackIntent = new Intent(context, BreakFeedbackActivity.class);
        openFeedbackIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openFeedbackIntent.putExtra("breakSessionId", breakSessionId);
        openFeedbackIntent.putExtra("elapsedDurationMinutes", plannedDurationMinutes);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                NOTIFICATION_ID_END_BREAK,
                openFeedbackIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Flora Break beendet")
                .setContentText("Deine geplante Pause ist vorbei. Beenden und Feedback geben?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        showNotification(context, NOTIFICATION_ID_END_BREAK, builder);
    }

    private void showNotification(
            Context context,
            int notificationId,
            NotificationCompat.Builder builder
    ) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(notificationId, builder.build());
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Flora Break Erinnerungen",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Erinnert an Foto-Beweis und Pausenende während einer Flora Break.");

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
