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
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.activity.MainActivity;
import com.columbasms.columbasms.model.ContactsGroup;
import com.columbasms.columbasms.utils.Utils;
import com.columbasms.columbasms.utils.network.API_URL;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GCMService extends GcmListenerService {

    private static JSONArray j;
    private static String ASSOCIATION_ID;
    private static String ASSOCIATION_NAME;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        SharedPreferences state = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ASSOCIATION_ID = from.split("_")[1];
        String USER_ID = state.getString("user_id", "");
        final String message = data.getString("message");
        String CAMPAIGN_ID = data.getString("campaign_id");
        ASSOCIATION_NAME = data.getString("organization_name");

        Log.d("App", "from: " + from);
        Log.d("App", "message: " + message);

        if (from.startsWith("/topics/")) {

            String contacts = state.getString(ASSOCIATION_ID + "_contacts_forTrusting", "");
            String groupsForTrustingString = state.getString(ASSOCIATION_ID + "_groups_forTrusting", "");


            if(!contacts.equals("")){

                System.out.println("AUTOMATIC SMS SENDING TO SELECT CONTACS: " + contacts);

                //SEND SMS TO CONTACTS SELECTED WHEN TRUSTING (EVEN IF YOU DON'T SAVE THEM TO A GROUP)
                final String URL = API_URL.USERS_URL + "/" + USER_ID + API_URL.CAMPAIGNS + "/" + CAMPAIGN_ID;

                System.out.println(URL);

                RequestQueue requestQueue = Volley.newRequestQueue(this);

                JSONObject body = new JSONObject();
                try {
                    j = new JSONArray(contacts);
                    body.put("users", j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("AUTOMATIC SENDING TO: " + body.toString());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("Invio a: ");
                        try {
                            JSONArray c = new JSONArray(response.getString("users"));
                            System.out.println(c.toString());
                            for (int i = 0; i < c.length(); i++) {
                                try {
                                    JSONObject temp = new JSONObject(j.getString(i));
                                    String number = temp.getString("number");
                                    System.out.println("NUMERO: " + number);
                                    Utils.sendSMS(ASSOCIATION_NAME, number, message, getResources());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            sendNotification(ASSOCIATION_NAME,message,true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        String credentials = "47ccf9098174f48be281f86103b9" + ":" + "c5906274ba1a14711a816db53f0d";
                        String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
                        headers.put("Authorization", "Basic " + credBase64);
                        return headers;
                    }

                };

                requestQueue.add(jsonObjectRequest);
            }else if(!groupsForTrustingString.equals("")){
                //SEND SMS TO GROUPS SELECTED WHEN TRUSTING
                System.out.println("AUTOMATIC SMS SENDING TO GROUP: " + groupsForTrustingString);

                //STEP1: RETRIEVE GROUP FOR THIS ASSOCIATION
                JSONArray groupsForTrusting = null;
                try {
                    groupsForTrusting = new JSONArray(groupsForTrustingString);
                    for(int i = 0;i<groupsForTrusting.length(); i++){

                        //FRO EACH GROUPS DO FOLLOWING STEPS:

                        JSONObject g = new JSONObject(groupsForTrusting.get(i).toString());

                        ContactsGroup group = new ContactsGroup(g.getString("name"),new JSONArray(g.getString("contacts")),true);

                        //STEP2: CREATE JSON ARRAY FOR COLLISION DETECTION
                        final JSONArray contactsList = group.getContactList();
                        JSONArray j = new JSONArray();
                        for(int x = 0; x<contactsList.length(); x++){
                            JSONObject singleContact = new JSONObject(contactsList.getString(x));
                            JSONObject temp = new JSONObject();
                            temp.put("number",singleContact.getString("number"));
                            j.put(temp);
                        }

                        //STEP3: SEND JSON OBJECT DERIVED FROM JSONARRAY TO SERVER (IF THE GROUP IS NON EMPTY)
                        if (contactsList.length() != 0) {

                            final String URL = API_URL.USERS_URL + "/" + USER_ID + API_URL.CAMPAIGNS + "/" + CAMPAIGN_ID;

                            System.out.println(URL);

                            RequestQueue requestQueue = Volley.newRequestQueue(this);

                            JSONObject body = new JSONObject();

                            try {
                                body.put("users", j);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, body, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //STEP4: ON RESPONSE FROM SERVER SEND SMS TO ALLOWED NUMBER
                                    System.out.println("Invio a: ");
                                    try {
                                        JSONArray contacts = new JSONArray(response.getString("users"));
                                        System.out.println(contacts.toString());
                                        for (int i = 0; i < contacts.length(); i++) {
                                            try {
                                                JSONObject j =  new JSONObject(contactsList.getString((int) contacts.get(i)));
                                                System.out.println("NUMERO: " + j.getString("number"));
                                                Utils.sendSMS(ASSOCIATION_NAME, j.getString("number"), message, getResources());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        sendNotification(ASSOCIATION_NAME,message,true);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println(error.toString());
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    HashMap<String, String> headers = new HashMap<String, String>();
                                    String credentials = "47ccf9098174f48be281f86103b9" + ":" + "c5906274ba1a14711a816db53f0d";
                                    String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
                                    headers.put("Authorization", "Basic " + credBase64);
                                    return headers;
                                }

                            };

                            requestQueue.add(jsonObjectRequest);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                sendNotification(ASSOCIATION_NAME,message,false);
            }

        } else {
            //do NOTHING
        }
    }











    public void sendNotification(String associationName,String message,boolean isForTrust){

        String notificationContent = "";

        if(isForTrust == false){
            notificationContent = associationName + " " + getResources().getString(R.string.notification_follow_message);
        }else notificationContent = getResources().getString(R.string.notification_trust_message) + " " + associationName;

        Intent resultIntent = new Intent(this, AssociationProfileActivity.class);
        resultIntent.putExtra("ass_id",ASSOCIATION_ID);
        resultIntent.putExtra("ass_name",ASSOCIATION_NAME);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addParentStack(AssociationProfileActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.colomba_icns)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(new long[] {1, 1, 1})
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentTitle("ColumbaSMS")
                .setContentIntent(pendingIntent)
                .setContentText(notificationContent);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());

        /*
        Intent intent = new Intent(this, NotificationReceiverActivity.class);
    PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

    // Build notification
    // Actions are just fake
    Notification noti = new Notification.Builder(this)
        .setContentTitle("New mail from " + "test@gmail.com")
        .setContentText("Subject").setSmallIcon(R.drawable.icon)
        .setContentIntent(pIntent)
        .addAction(R.drawable.icon, "Call", pIntent)
        .addAction(R.drawable.icon, "More", pIntent)
        .addAction(R.drawable.icon, "And more", pIntent).build();
    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    // hide the notification after its selected
    noti.flags |= Notification.FLAG_AUTO_CANCEL;

    notificationManager.notify(0, noti);
         */



    }


}
