package com.columbasms.columbasms.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.columbasms.columbasms.R;

/**
 * Created by Matteo Brienza on 2/3/16.
 */
public class NotificationsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifications, container,false);
        return v;
    }

}
