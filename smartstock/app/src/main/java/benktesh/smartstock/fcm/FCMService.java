package benktesh.smartstock.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import benktesh.smartstock.MainActivity;
import benktesh.smartstock.R;
import benktesh.smartstock.Utils.NetworkUtilities;
import benktesh.smartstock.Utils.SmartStockConstant;

public class FCMService extends FirebaseMessagingService {
    private static final int NOTIFICATION_MAX_CHARACTERS = 100;
    private static final String TAG = FCMService.class.getSimpleName();

    private static final String JSON_KEY_MESSAGE = SmartStockConstant.ADMIN_MESSAGE_KEY;

    public FCMService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Map<String, String> data = remoteMessage.getData();


        if (data.size() > 0) {
            Log.d(TAG, "Message data payload: " + data);

            // Send a notification that we have got admin upate message
            sendNotification("App updating..." + data.get(JSON_KEY_MESSAGE));
            new NetworkTask().execute();

        }
        // Send a notification that we some message that are not update specific
        else if (remoteMessage.getNotification() != null) {
            String messageBody = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + messageBody);
            sendNotification(messageBody);
        }

    }

    public void showMessage(final String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(String message) {
        if (message == null || message.length() <= 0)
            return;
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Log.d(TAG, "sendNotification. message=" + message);
        //trim the message to 100 characters
        if (message != null && message.length() > NOTIFICATION_MAX_CHARACTERS) {

            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = getString(R.string.Channel);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.Channel);
            String Description = getString(R.string.Channel);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(JSON_KEY_MESSAGE + " Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        showMessage(message);
    }


    private class NetworkTask extends AsyncTask<String, Integer, Boolean> {


        protected Boolean doInBackground(String... params) {
            Log.d(TAG, "network task starting");
            boolean result = false;
            try {
                result = NetworkUtilities.populateSymbol(getApplicationContext(), true);
            } catch (Exception ex) {
                Log.d(TAG, ex.toString());
            }

            return result;

        }


        protected void onPostExecute(boolean result) {
            Log.d(TAG, "network task completed");

        }
    }

}
