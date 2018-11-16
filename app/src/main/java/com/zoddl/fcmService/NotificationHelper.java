package com.zoddl.fcmService;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zoddl.activities.HomeActivity;

public class NotificationHelper {
    public static final int NOTIFICATION_ID_UNKNOWN = 0;


    public static int notificationId(String notificationType) {
                return NOTIFICATION_ID_UNKNOWN;

    }

    public static PendingIntent targetPendingIntent(Context context, String notificationType, String extra) {
        Intent defaultIntent = new Intent(context, HomeActivity.class);
        PendingIntent defaultPendingIntent = PendingIntent.getActivity(context,
                notificationId(notificationType)/* Request code */,
                defaultIntent,
                PendingIntent.FLAG_ONE_SHOT);
        return defaultPendingIntent;
    }

}
