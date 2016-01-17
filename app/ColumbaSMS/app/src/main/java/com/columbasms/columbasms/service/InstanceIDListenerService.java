package com.columbasms.columbasms.service;

import android.content.Intent;

public class InstanceIDListenerService extends com.google.android.gms.iid.InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
    }
}
