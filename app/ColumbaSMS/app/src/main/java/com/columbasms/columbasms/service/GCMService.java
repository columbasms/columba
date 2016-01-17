package com.columbasms.columbasms.service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.columbasms.columbasms.MainActivity;
import com.columbasms.columbasms.R;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONArray;
import org.json.JSONException;

public class GCMService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d("App", "from: " + from);
        Log.d("App", "message: " + message);
        sendNotification(message);
    }

    public void sendNotification(String message){



        SharedPreferences account_information = PreferenceManager.getDefaultSharedPreferences(this);
        JSONArray c = null;
        try {
            c = new JSONArray(account_information.getString("contacts","") );
            for(int i = 0; i<c.length(); i++){
                System.out.println("Send message: " + message + " to " + c.get(i).toString());
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(c.get(i).toString(), null, "Association Name:" +  "\n" + message + "\n" + "Powered by Columba\n" + "To stop receiving this SMS: www.columbasms.com/stop/phoneNumber", null, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        Intent intent = new Intent(this, NotificationManager.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.colomba_icns)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(new long[] {1, 1, 1})
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentTitle("ColumbaSMS")
                .setContentText(message);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


    }

}
