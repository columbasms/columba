package com.columbasms.columbasms.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.service.RegistrationService;
import com.columbasms.columbasms.utils.network.API_URL;
import com.columbasms.columbasms.utils.network.CustomRequest;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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


    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
    public static final String PROPERTY_REG_ID = "registration_id";
    GoogleCloudMessaging gcm;


    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {

            //TUTORIAL SCREEN SETTINGS
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

            // Hide Skip/Done button.
            showSkipButton(true);
            setProgressButtonEnabled(true);

            //START GCM SERVICE
            Intent i = new Intent(this, RegistrationService.class);
            startService(i);

            if (checkPlayServices()) {
                //DIGITS AUTHENTICATION (MISSED GCM/PLAY SERVICES)
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                //Digits by Twitter Authentication with phone number
                TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
                Fabric.with(this, new TwitterCore(authConfig), new Digits());

                //Customize SIGN IN button
                digitsButton = new DigitsAuthButton(this);
                digitsButton.setAuthTheme(R.style.AppTheme_Digits);
                digitsButton.setCallback(new AuthCallback() {

                    @Override
                    public void success(DigitsSession session, final String phoneNumber) {


                        //BACKEND COMMUNICATION
                        TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
                        TwitterAuthToken authToken = (TwitterAuthToken) session.getAuthToken();
                        DigitsOAuthSigning oauthSigning = new DigitsOAuthSigning(authConfig, authToken);
                        Map<String, String> authHeaders = oauthSigning.getOAuthEchoHeadersForVerifyCredentials();

                        //ADD GCM TOKEN (STORED)
                        final SharedPreferences sp;
                        String token_name = "gcm-token";
                        String token = null;
                        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        token = sp.getString(token_name, null);
                        authHeaders.put(token_name, token);
                        System.out.println(authHeaders.toString());

                        CustomRequest authRequest = new CustomRequest(Request.Method.POST, API_URL.USERS_URL, authHeaders, new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {

                                try {

                                    final String jsonString;

                                    jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                                    System.out.println("AUTH RESPONSE: " + jsonString);

                                    JSONObject u = new JSONObject(jsonString);

                                    JSONObject digitsClient = u.getJSONObject("digits_client");

                                    SharedPreferences.Editor editor_account_information = sp.edit();

                                    editor_account_information.putString("user_id", digitsClient.getString("id"));

                                    editor_account_information.putString("user_name", digitsClient.getString("user_name"));

                                    editor_account_information.putString("phone_number", phoneNumber);

                                    editor_account_information.putString("firstLaunch", "false");

                                    editor_account_information.commit();


                                    finish();


                                } catch (UnsupportedEncodingException | JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.toString());
                            }
                        });

                        //PERFORM REQUEST
                        MyApplication.getInstance().addToRequestQueue(authRequest);

                    }

                    @Override
                    public void failure(DigitsException exception) {
                        Log.d("Digits", "Sign in with Digits failure", exception);
                    }
                });

            }



    }

    @Override
    public void onBackPressed() {
        //NO-OPERATION
    }

    @Override
    public void onSkipPressed() {
        digitsButton.performClick();
    }

    @Override
    public void onDonePressed() {
        //PERFORM A CLICK TO CALL AUTH CALLBACK
        digitsButton.performClick();
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    //GCM METHODS
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RECOVER_PLAY_SERVICES:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Google Play Services must be installed.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkPlayServices() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                showErrorDialog(status);
            } else {
                Toast.makeText(this, "This device is not supported.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    void showErrorDialog(int code) {
        GooglePlayServicesUtil.getErrorDialog(code, this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
    }


}
