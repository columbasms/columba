package com.columbasms.columbasms.utils;

import android.telephony.SmsManager;

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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Matteo on 15/01/2016.
 */
public class Utils {

    public static JSONObject getOauthAccessToken(JSONObject j){
        String url = "https://www.columbasms.com/oauth/v2/token?";
        JSONArray ja = j.names();
        /*
        for(int i = 0; i< ja.length();i++){
            try {
                String name = ja.getString(i);
                System.out.println( name + " " + j.get(ja.getString(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        */
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
        //System.out.println("COMPLETE URL: " + url);
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

    public static void sendSMS(String associationSender,String phoneNumber, String message){
        System.out.println("Send message: " + message + " to " + phoneNumber);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, associationSender + ":" +  "\n" + message + "\n" + "Powered by Columba\n" + "To stop receiving this SMS: www.columbasms.com/stop/phoneNumber", null, null);
    }
}
