package ch.epfl.sweng.eventmanager.notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationManagerCompat;

/**
 * Receives notification and broadcast them to the user
 */
public class NotificationPublisher extends BroadcastReceiver {

    private static final String NOTIFICATION_ID = "notification-id";
    private static final String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        notificationManager.notify(id, notification);
    }

    static String getNotificationId() {
        return NOTIFICATION_ID;
    }

    static String getNOTIFICATION() {
        return NOTIFICATION;
    }
}
