package com.columbasms.columbasms;

/**
 * Created by Matteo Brienza on 23/12/15.
 */

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "aia3X5hWUyqgTZBdWNy2DVjfR";
    private static final String TWITTER_SECRET = "JKEWdIt0Q0h4xliBhvhxUP5ls5tMaZWFXo0uWiLvTEuZPWALu2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //App name <-> font matching
        TextView appName = (TextView)findViewById(R.id.appName);
        Typeface font_roboto = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface font_roundedElegance = Typeface.createFromAsset(this.getAssets(), "fonts/Rounded_Elegance.ttf");
        appName.setTypeface(font_roundedElegance);

        //Digits by Twitter Authentication with phone number
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        //Customize SIGN IN button
        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setText(getResources().getString(R.string.signin));
        digitsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        digitsButton.setTypeface(font_roboto);
        digitsButton.setAuthTheme(R.style.AppTheme_Digits);
        digitsButton.setCallback(new AuthCallback() {

            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model

                /* Debug info
                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();
                */


                //BACKEND COMMUNICATION
                TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
                TwitterAuthToken authToken = (TwitterAuthToken)session.getAuthToken();
                DigitsOAuthSigning oauthSigning = new DigitsOAuthSigning(authConfig, authToken);
                Map<String, String> authHeaders = oauthSigning.getOAuthEchoHeadersForVerifyCredentials();


                String url = "http://www.columbasms.com/api/register";
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
                    nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }



                //PERFORM HTTP POST
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                //System.out.println("#########DEBUG#########" );
                //System.out.println("POST to: " + url);
                ListIterator<NameValuePair> it = nameValuePair.listIterator();
                while(it.hasNext()){
                    NameValuePair temp = it.next();
                    httpPost.addHeader(temp.getName(),temp.getValue());
                    //System.out.println(temp.getName() + " " + temp.getValue() );
                }
                //System.out.println("#######################" );


                //making POST request.
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    // write response to log
                    StringBuilder sb = new StringBuilder();
                    try {
                        //System.out.println("###############HTTP POST RESPONSE LENGHT: " + response.getEntity().getContentLength());
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
                        String line = null;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                    catch (IOException e) { e.printStackTrace(); }
                    catch (Exception e) { e.printStackTrace(); }


                    //System.out.println("###############HTTP POST RESPONSE TEXT: " + sb.toString());
                } catch (ClientProtocolException e) {
                    // Log exception
                    e.printStackTrace();
                } catch (IOException e) {
                    // Log exception
                    e.printStackTrace();
                }


                //Change activity after successful authentication
                LoginActivity.this.finish();
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });

    }

}
