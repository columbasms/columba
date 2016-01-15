package com.columbasms.columbasms.service;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class GCMService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d("App", "from: " + from);
        Log.d("App", "message: " + message);
    }
}