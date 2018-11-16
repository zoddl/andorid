package com.zoddl.fcmService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.zoddl.R;
import com.zoddl.activities.HomeActivity;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.interfaces.ILogger;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.PrefManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.zoddl.interfaces.StringConstant._MESSAGE;
import static com.zoddl.interfaces.StringConstant._SUCCESS;
import static com.zoddl.utils.AppConstant.LOCAL_BROADCAST_FOR_ALERT;

/**
 * @author abhishek.tiwari
 * for Mobiloitte
 */

public class MyFireBaseMessagingService extends FirebaseMessagingService implements ILogger {
    private PrefManager prefManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Context context = MyFireBaseMessagingService.this;
        prefManager = PrefManager.getInstance(context);

        if (remoteMessage.getData() != null) {
            Log.e("firebase", remoteMessage.toString());
            Map<String, String> data = remoteMessage.getData();

            if (data.size() > 0) {

                String title = data.get("title");
                String message = data.get("message");

                Message mMessage = new Message();
                mMessage.setTitle(title);
                mMessage.setMessageId(message);
                mMessage.setAlert("");
                mMessage.setType("");

                MyRoomDatabase db = Room.databaseBuilder(context, MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();
                db.getMessageDao().insertAll(mMessage);

                sendNotification("",title,message,"");
                sendMessage();
            }

        }


        //AppController mposApplication = (AppController) context.getApplicationContext();

        /*if (remoteMessage.getData() != null) {
            Message message;
            Data data = new Gson().fromJson(remoteMessage.getData().toString(), Data.class);
            if (data.getMessage().size() > 0) {

                MyRoomDatabase db = Room.databaseBuilder(context, MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();
                message = data.getMessage().get(0);
                db.getMessageDao().insertAll(message);

                sendNotification(message.getAlert());

                switch (message.getType()) {
                    case NotificationConstant.NOTIFICATION_TYPE_1:
                        EventBus.getDefault().post(new AnimationEvent(context, true));
                        EventBus.getDefault().post(new PreOrderCountEvent(message.getPreOrderCount()));
                        new PreOrderNotificationPrint(context.getApplicationContext());
                        if (mposApplication.getAppOpen()) {
                            sendNotification(null, message.getNotiSound());
                        } else {
                            sendNotification(message.getAlert(), message.getNotiSound());
                        }
                        break;
                    case NotificationConstant.NOTIFICATION_TYPE_2:
                        EventBus.getDefault().post(new MessageDetailsEventBus(message));
                        if (!TextUtils.isEmpty(mposApplication.getCurrentMessageId()) &&
                                mposApplication.getCurrentMessageId().equalsIgnoreCase(message.getMessageId())
                                && mposApplication.getAppOpen()){
                            sendNotification(message.getNotiSound());
                        }else {

                        }
                        break;
                }
            }
        }*/
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String notificationType, String title, String messageBody, String extra) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = NotificationHelper.targetPendingIntent(this, notificationType, extra);

        String channelId = "HomeActivity";

        Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.ic_launcher);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.icon10)
                        .setLargeIcon(notificationLargeIconBitmap)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "HomeActivity",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(NotificationHelper.notificationId(notificationType),
                notificationBuilder.build());
    }
    private void sendNotification(String title, String message) {
        //increasing notification id value
        int NOTIFICATION_ID = prefManager.getKeyNotificationId() + 1;
        prefManager.setKeyNotificationId(NOTIFICATION_ID);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getString(R.string.app_name)+"("+title+")")
                    .setContentText(message)
                    .setAutoCancel(true);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void sendMessage() {
        Log.d("UploadTagDataService", "Broadcasting message");
        Intent intent = new Intent(LOCAL_BROADCAST_FOR_ALERT);
        intent.putExtra(_MESSAGE, _SUCCESS);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public String getTagName() {
        return MyFireBaseMessagingService.class.getSimpleName();
    }
}
