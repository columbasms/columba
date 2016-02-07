package com.columbasms.columbasms.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.widget.ImageView;

import com.columbasms.columbasms.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Matteo on 15/01/2016.
 */
public class Utils {

    private static Transformation t;

    public static JSONObject getOauthAccessToken(JSONObject j){
        String url = "https://www.columbasms.com/oauth/v2/token?";
        JSONArray ja = j.names();
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        try {
            params.add(new BasicNameValuePair("client_id", j.getString("client_id").toString()));
            params.add(new BasicNameValuePair("client_secret", j.getString("client_secret").toString()));
            params.add(new BasicNameValuePair("username", j.getString("username").toString()));
            params.add(new BasicNameValuePair("password", j.getString("password").toString()));
            params.add(new BasicNameValuePair("grant_type","password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        //System.out.println("COMPLETE: " + url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            response = httpClient.execute(httpGet);
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = builder.toString();
        JSONObject at = null;
        try {
            at = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1).replaceAll("\\\\",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("##########HTTP GET RESPONSE from " + url + " :" + json.replaceAll("\\\\",""));
        return at;
    }

    public static JSONObject apiTest(JSONObject j) throws MalformedURLException {


        String url = "https://www.columbasms.com/api/random?";
        JSONArray ja = j.names();

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        try {
            params.add(new BasicNameValuePair("access_token", j.getString("access_token").toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;

        URL url_req = new URL(url);
        URLConnection urlConnection = null;
        try {
            urlConnection = url_req.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            is = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("RESPONSE HTTPS from: " + url + " " + sb.toString());

        /*
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        try {
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = builder.toString();
        System.out.println("##########HTTP GET RESPONSE FOR ACCESS_TOKEN from " + url + " :" + json.replaceAll("\\\\",""));
        */

        JSONObject at = new JSONObject();
        return at;
    }

    public static void sendSMS(String associationSender,String phoneNumber, String message,Resources res){
        System.out.println("Send message: " + message + " to " + phoneNumber);
        SmsManager sms = SmsManager.getDefault();

        String format_message =
                associationSender + ":\n"+
                message + "\n" +
                res.getString(R.string.sms_footer) + "\n" +
                res.getString(R.string.sms_stop) +
                phoneNumber.replace(" ", "");

        ArrayList<String> parts = sms.divideMessage(format_message);
        sms.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
    }


    public static void downloadImage(final String URL, final ImageView im, final boolean applyTrasformation, boolean applyBorder){

        t = null;

        if(applyBorder==true) {
            t = new RoundedTransformationBuilder()
                    .cornerRadiusDp(50)
                    .oval(false)
                    .borderWidthDp(1)
                    .borderColor(Color.parseColor("#ffffff"))
                    .build();
        }else{
            t = new RoundedTransformationBuilder()
                    .cornerRadiusDp(50)
                    .oval(false)
                    .build();
        }


        RequestCreator request = Picasso.with(im.getContext()).load(URL);

        if(applyTrasformation==true){
             request.transform(t)
                    .placeholder(R.drawable.error_thumbnail_image);
        }else {
            request.placeholder(R.drawable.error_cover_image);
        }

        request.networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .into(im, new Callback() {

                    /*
                    Picasso will keep looking for it offline in cache and fail,
                    the following code looks at the local cache, if not found offline,
                    it goes online and replenishes the cache
                    */

                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        RequestCreator request2 = Picasso.with(im.getContext()).load(URL);
                        if(applyTrasformation==true){
                            request2.transform(t)
                                    .placeholder(R.drawable.error_thumbnail_image);
                        }else {
                            request2.placeholder(R.drawable.error_cover_image);
                        }

                        request2.fit().into(im, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                            }
                        });

                    }
                });

    }
}
