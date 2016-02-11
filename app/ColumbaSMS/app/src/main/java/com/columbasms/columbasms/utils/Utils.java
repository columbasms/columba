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

    //SEND SMS
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


    //DOWNLOAD IMAGE AND APPLY A TRASFORMATION
    private static Transformation t;
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
