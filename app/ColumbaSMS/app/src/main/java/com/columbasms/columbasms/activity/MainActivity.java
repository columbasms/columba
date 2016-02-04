package com.columbasms.columbasms.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.fragment.HomeFragment;
import com.columbasms.columbasms.fragment.MessagesFragment;
import com.columbasms.columbasms.fragment.NotificationsFragment;
import com.columbasms.columbasms.fragment.SplashScreenFragment;
import com.columbasms.columbasms.fragment.TopicsFragment;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    private static long SPLASH_SCREEN_DELAY = 1500;

    @Bind(R.id.drawer_layout)DrawerLayout drawer;
    @Bind(R.id.toolbar_top)Toolbar toolbar_top;
    @Bind(R.id.toolbar_bottom)Toolbar toolbar_bottom;
    @Bind(R.id.home)LinearLayout home;
    @Bind(R.id.topics)LinearLayout topics;
    @Bind(R.id.messages)LinearLayout messages;
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
            toolbar_top.setTitle(R.string.mex);
            fr = new MessagesFragment();
        }else{
            toolbar_top.setTitle(R.string.not);
            fr = new NotificationsFragment();
        }
        FragmentManager fm = getFragmentManager();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_top.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
            }
        });


        SharedPreferences state = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (state.getString("firstLaunch",null)==null) {
            // Show the splash screen at the beginning
            SharedPreferences.Editor editor_state = state.edit();
            editor_state.putString("firstLaunch", "false");
            editor_state.commit();
            getFragmentManager().beginTransaction().add(R.id.fragment_place, new SplashScreenFragment()).commit();
        }else SPLASH_SCREEN_DELAY = 0;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.fragment_place, new HomeFragment()).commit();

                // Show action bar when the main fragment is visible
                runOnUiThread(new Runnable() {
                    public void run() {
                        toolbar_top.setVisibility(View.VISIBLE);
                        toolbar_bottom.setVisibility(View.VISIBLE);
                    }
                });

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