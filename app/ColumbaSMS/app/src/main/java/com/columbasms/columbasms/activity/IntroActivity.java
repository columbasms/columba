package com.columbasms.columbasms.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.fragment.SplashScreenFragment;
import com.columbasms.columbasms.utils.Utils;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Federico on 04/02/16.
 */
public class IntroActivity extends AppIntro {


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "aia3X5hWUyqgTZBdWNy2DVjfR";
    private static final String TWITTER_SECRET = "JKEWdIt0Q0h4xliBhvhxUP5ls5tMaZWFXo0uWiLvTEuZPWALu2";
    DigitsAuthButton digitsButton;


    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {


            addSlide(AppIntroFragment.newInstance("Scegli", "Seleziona le campagne che vuoi supportare e"
                            + " decidi chi rendere partecipe!", R.drawable.colomba_icns,
                    Color.parseColor("#009688")));
            addSlide(AppIntroFragment.newInstance("Dona", "Diffondi iniziative benifiche donando i tuoi "
                    + "SMS inutilizzati!", R.drawable.colomba_icns, Color.parseColor("#009688")));
            addSlide(AppIntroFragment.newInstance("Condividi", "Sii social! Nella schermata principale "
                            + "potrai trovare e mostrare tutte le campagne e le iniziative promosse e molto altro!",
                    R.drawable.colomba_icns, Color.parseColor("#009688")));
            addSlide(AppIntroFragment.newInstance("Competi!", "Mostra a tutti chi è il migliore! "
                            + "Diventa il miglior Columber della tua zona e scala la classifica!",
                    R.drawable.colomba_icns, Color.parseColor("#009688")));

            setBarColor(Color.parseColor("#00796B"));
            setSeparatorColor(Color.parseColor("#B2DFDB"));


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //if (checkPlayServices()) {
        //App name <-> font matching

        //Digits by Twitter Authentication with phone number
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        //Customize SIGN IN button
        digitsButton = new DigitsAuthButton(this);
        digitsButton.setAuthTheme(R.style.AppTheme_Digits);
        digitsButton.setCallback(new AuthCallback() {

            @Override
            public void success(DigitsSession session, String phoneNumber) {

                //BACKEND COMMUNICATION
                TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
                TwitterAuthToken authToken = (TwitterAuthToken) session.getAuthToken();
                DigitsOAuthSigning oauthSigning = new DigitsOAuthSigning(authConfig, authToken);
                Map<String, String> authHeaders = oauthSigning.getOAuthEchoHeadersForVerifyCredentials();


                String url = "https://www.columbasms.com/api/register";
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
                    nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }

                //PERFORM HTTP POST
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                //System.out.println("POST to: " + url);
                ListIterator<NameValuePair> it = nameValuePair.listIterator();
                while (it.hasNext()) {
                    NameValuePair temp = it.next();
                    httpPost.addHeader(temp.getName(), temp.getValue());
                }
                SharedPreferences sp;
                String token_name = "gcm-token";
                String token = null;
                sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                token = sp.getString(token_name, null);
                httpPost.addHeader(token_name, token);
                //making POST request.

                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null; ) {
                        builder.append(line).append("\n");
                    }

                    String json = builder.toString();
                    System.out.println("###############HTTP POST RESPONSE TEXT: " + builder.toString());
                    JSONObject finalResult = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1).replaceAll("\\\\", ""));

                    //GET OAUTH TOKEN
                    JSONObject forTest = Utils.getOauthAccessToken(finalResult);
                    //API TEST
                    Utils.apiTest(forTest);

                } catch (ClientProtocolException e) {
                    // Log exception
                    e.printStackTrace();
                } catch (IOException e) {
                    // Log exception
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Change activity after successful authentication, and save the state
                System.out.println("AUTENTICATO");
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });

            // Hide Skip/Done button.
            showSkipButton(true);
            setProgressButtonEnabled(true);

    }

    @Override
    public void onSkipPressed() {
    }

    @Override
    public void onDonePressed() {
        digitsButton.performClick();
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }


}
