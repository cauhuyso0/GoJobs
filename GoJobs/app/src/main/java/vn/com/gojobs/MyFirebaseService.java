package vn.com.gojobs;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";
    public static final String CHANNEL_1_ID = "chanel1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // handle a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            System.out.println();
            sendNotification(remoteMessage.getNotification().getBody());

        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        System.out.println("refresh token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        System.out.println("token: " + token);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.project_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo_splash)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_splash))
                        .setContentTitle(getString(R.string.project_id))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .addAction(new NotificationCompat.Action(
                                android.R.drawable.sym_call_missed,
                                "Cancel",
                                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)))
                        .addAction(new NotificationCompat.Action(
                                android.R.drawable.sym_call_outgoing,
                                "OK",
                                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)));

       NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       notificationManager.notify(1, notificationBuilder.build());

        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_1_ID,
//                    "Chanel 1",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            channel.setDescription("This is Channel 1");
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//            notificationManager.notify(1, notificationBuilder.build());
//        }
    }
}
