package com.columbasms.columbasms.fragment;

/**
 * Created by matteobrienza on 1/29/16.
 */

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.columbasms.columbasms.R;

public class SplashScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash_screen, container,false);
        TextView app_name = (TextView)v.findViewById(R.id.appName);
        Typeface font_roundedElegance = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Rounded_Elegance.ttf");
        app_name.setTypeface(font_roundedElegance);

        return v;
    }

}