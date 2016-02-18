package com.columbasms.columbasms.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.widget.ImageView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.MainActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matteo on 15/01/2016.
 */
public class Utils {

    //TIMESTAMP CAMPAIGN

    public static String getTimestamp(String time, Activity a){
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        String dtArrival = time;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dtDeparture = format.format(currentLocalTime);
        String w = a.getResources().getString(R.string.week);
        String d = a.getResources().getString(R.string.day);
        String h = a.getResources().getString(R.string.hour);
        String m = a.getResources().getString(R.string.minute);

        try {
            Date dateDeparture = format.parse(dtDeparture);
            Date dateArrival = format.parse(dtArrival);
            dateDeparture.compareTo(dateArrival);
            long diff = dateDeparture.getTime() - dateArrival.getTime();
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) - hours*60;

            if(hours>168) {
                return Integer.toString((int)(hours/168)) + w;
            }else if(hours>=24 && hours<168){
                return Integer.toString((int)(hours/24)) + d;
            }else if(hours<24 && hours>1){
                return Integer.toString((int)(hours)) + h;
            }else {
                return Integer.toString((int)(minutes)) + m;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    //SEND SMS
    public static void sendSMS(String associationSender,String phoneNumber, String message,Resources res ){


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
