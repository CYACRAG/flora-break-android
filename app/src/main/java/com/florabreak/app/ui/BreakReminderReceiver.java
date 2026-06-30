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
 * Lokaler Reminder nach Ablauf der geplanten Pausen-/Gehzeit.
 *
 * Wird über AlarmManager aus ActiveBreakActivity gestartet.
 * Der Nutzer wird daran erinnert, zu Flora Break zurückzukehren
 * und die Pause zu beenden bzw. Feedback zu geben.
 */
public class BreakReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "flora_break_reminder_channel";
    private static final int NOTIFICATION_ID = 2026;

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        Intent openAppIntent = new Intent(context, ActiveBreakActivity.class);
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Flora Break")
                .setContentText("Deine geplante Pause ist vorbei. Möchtest du sie beenden?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Flora Break Erinnerungen",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Erinnert daran, eine aktive Flora-Break-Pause zu beenden.");

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
