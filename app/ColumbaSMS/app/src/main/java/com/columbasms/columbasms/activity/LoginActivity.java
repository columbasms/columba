package com.columbasms.columbasms.activity;

/**
 * Created by Matteo Brienza on 23/12/15.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.columbasms.columbasms.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
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


//CLASS NOT USED--SEE INTROACTIVITY
public class LoginActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "aia3X5hWUyqgTZBdWNy2DVjfR";
    private static final String TWITTER_SECRET = "JKEWdIt0Q0h4xliBhvhxUP5ls5tMaZWFXo0uWiLvTEuZPWALu2";
    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
    public static final String PROPERTY_REG_ID = "registration_id";
    GoogleCloudMessaging gcm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
        */

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
            DigitsAuthButton digitsButton = new DigitsAuthButton(this);
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
                    httpPost.addHeader(token_name,token);
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
                    LoginActivity.this.finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void failure(DigitsException exception) {
                    Log.d("Digits", "Sign in with Digits failure", exception);
                }
            });
        //}


    }

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



}//end
