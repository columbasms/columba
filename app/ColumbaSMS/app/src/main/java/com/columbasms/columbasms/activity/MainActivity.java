package com.columbasms.columbasms.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.fragment.HomeFragment;
import com.columbasms.columbasms.fragment.MapFragment;
import com.columbasms.columbasms.fragment.NotificationsFragment;
import com.columbasms.columbasms.fragment.SplashScreenFragment;
import com.columbasms.columbasms.fragment.TopicsFragment;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static long SPLASH_SCREEN_DELAY = 1500;
    private ActionBarDrawerToggle mToggle;

    @Bind(R.id.drawer_layout)DrawerLayout drawer;
    @Bind(R.id.list_view_drawer)NavigationView navView;
    @Bind(R.id.toolbar_top)Toolbar toolbar_top;
    @Bind(R.id.toolbar_bottom)Toolbar toolbar_bottom;
    @Bind(R.id.home)LinearLayout home;
    @Bind(R.id.topics)LinearLayout topics;
    @Bind(R.id.messages)LinearLayout map;
    @Bind(R.id.notifications)LinearLayout notifications;
    @OnClick({ R.id.home, R.id.topics,R.id.messages,R.id.notifications})
    public void onClick(View v) {
        Fragment fr;
        if(v == findViewById(R.id.home)) {
            toolbar_top.setTitle(R.string.home);
            fr = new HomeFragment();
        }else if(v == findViewById(R.id.topics)){
            toolbar_top.setTitle(R.string.topics);
            fr = new TopicsFragment();
        }else if(v == findViewById(R.id.messages)){
            toolbar_top.setTitle(R.string.map);
            fr = new MapFragment();
        }else{
            toolbar_top.setTitle(R.string.not);
            fr = new NotificationsFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //TOP TOOLBAR SETUP
        toolbar_top.setTitle("Home");
        toolbar_top.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar_top.setVisibility(View.INVISIBLE);
        toolbar_bottom.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar_top);

        // ###########################################################################


        navView.setNavigationItemSelectedListener(this);

        mToggle = new ActionBarDrawerToggle(this, drawer, toolbar_top, R.string.app_name, R.string.app_name);

        drawer.setDrawerListener(mToggle);

        mToggle.syncState();

        // ##########################################################################

        final SharedPreferences state = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (state.getString("firstLaunch",null)==null) {
            // Show the splash screen at the beginning
            getFragmentManager().beginTransaction().add(R.id.fragment_place, new SplashScreenFragment()).commit();
        }else SPLASH_SCREEN_DELAY = 0;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //  If the activity has never started before...
                if (state.getString("firstLaunch",null)==null) {

                    //  Launch app intro

                    Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(i);


                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_place, new HomeFragment()).commit();

                    // Show action bar when the main fragment is visible
                    runOnUiThread(new Runnable() {
                        public void run() {
                            toolbar_top.setVisibility(View.VISIBLE);
                            toolbar_bottom.setVisibility(View.VISIBLE);
                        }
                    });

                }else{
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_place, new HomeFragment()).commit();

                    // Show action bar when the main fragment is visible
                    runOnUiThread(new Runnable() {
                        public void run() {
                            toolbar_top.setVisibility(View.VISIBLE);
                            toolbar_bottom.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

        /*
        Intent intentGCMListen = new Intent(this,GcmReceiver.class);
        startService(intentGCMListen);
        */

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.user_profile:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
                
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                return true;
            case R.id.action_feedback:
                return true;
            case R.id.action_guide:
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



}
