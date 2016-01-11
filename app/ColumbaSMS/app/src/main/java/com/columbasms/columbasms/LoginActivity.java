package com.columbasms.columbasms;

/**
 * Created by Matteo Brienza on 23/12/15.
 */

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "aia3X5hWUyqgTZBdWNy2DVjfR";
    private static final String TWITTER_SECRET = "JKEWdIt0Q0h4xliBhvhxUP5ls5tMaZWFXo0uWiLvTEuZPWALu2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

                //FIRST BACKEND COMMUNICATION CODE HERE WITH <LORENZO-ALESSIO>

                //Change activity after successfully authentication
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
