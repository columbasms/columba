package com.columbasms.columbasms.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.fragment.HomeFragment;
import com.columbasms.columbasms.fragment.MapFragment;
import com.columbasms.columbasms.fragment.NotificationsFragment;
import com.columbasms.columbasms.fragment.SplashScreenFragment;
import com.columbasms.columbasms.fragment.TopicsFragment;
import com.columbasms.columbasms.utils.Utils;
import com.columbasms.columbasms.utils.network.API_URL;
import com.columbasms.columbasms.utils.network.CacheRequest;
import com.google.android.gms.gcm.GcmReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static long SPLASH_SCREEN_DELAY = 1500;
    private ActionBarDrawerToggle mToggle;
    private static View header;
    private static String USER_ID;
    private static Activity activity;

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

        System.out.println("APERTA");


        //TOP TOOLBAR SETUP
        toolbar_top.setTitle("Home");
        toolbar_top.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar_top.setVisibility(View.INVISIBLE);
        toolbar_bottom.setVisibility(View.INVISIBLE);

        setSupportActionBar(toolbar_top);


        activity = this;

        navView.setNavigationItemSelectedListener(this);

        mToggle = new ActionBarDrawerToggle(this, drawer, toolbar_top, R.string.app_name, R.string.app_name);

        drawer.setDrawerListener(mToggle);

        mToggle.syncState();


        final SharedPreferences state = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        if (state.getString("firstLaunch",null)==null && state.getString("splashed",null)==null) {
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
                    MainActivity.this.finish();

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


        Intent intentGCMListen = new Intent(this,GcmReceiver.class);
        startService(intentGCMListen);


    }

    @Override
    public void onResume(){
        super.onResume();
        final SharedPreferences state = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        header= navView.getHeaderView(0);

        TextView navHeader_phoneNumber = (TextView)header.findViewById(R.id.phone_number);
        String phone_number = state.getString("phone_number", null);
        TextView navHeader_userName = (TextView)header.findViewById(R.id.name);
        String userName = state.getString("user_name", null);

        if(phone_number==null) {
            navHeader_phoneNumber.setText(getIntent().getStringExtra("phone_number"));
        }navHeader_phoneNumber.setText(phone_number);

        if(userName==null) {
            navHeader_userName.setText(getIntent().getStringExtra("user_name"));
        }navHeader_userName.setText(userName);

        USER_ID = state.getString("user_id", null);
        if(USER_ID==null)getIntent().getStringExtra("user_id");

        getUser();

    }

    private static void getUser(){

        String URL = API_URL.USERS_URL + "/" + USER_ID;

        System.out.println(URL);

        CacheRequest request = new CacheRequest(0, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    JSONObject o = new JSONObject(jsonString);
                    ImageView profile = (ImageView)header.findViewById(R.id.profile_image);
                    ImageView cover = (ImageView)header.findViewById(R.id.cover_image);
                    Utils.downloadImage(o.getString("avatar_normal"), profile, true, true);
                    Utils.downloadImage(o.getString("cover_normal"), cover, false, false);

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        //Adding request to the queue
        requestQueue.add(request);

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
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_info:
                i = new Intent(this,InfoActivity.class);
                startActivity(i);
                return true;
            case R.id.action_feedback:
                Intent j = new Intent(Intent.ACTION_SEND);
                j.setType("message/rfc822");
                j.putExtra(Intent.EXTRA_EMAIL  , new String[]{"columbasms@gmail.com"});
                j.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                j.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(j, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_guide:
                //i = new Intent(this,GuideActivity.class);
                //startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
