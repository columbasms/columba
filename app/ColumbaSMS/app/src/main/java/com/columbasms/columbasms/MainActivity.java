package com.columbasms.columbasms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.columbasms.columbasms.adapter.MainAdapter;
import com.columbasms.columbasms.service.GCMService;
import com.google.android.gms.gcm.GcmReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences app_state = PreferenceManager.getDefaultSharedPreferences(this);
        if (app_state.getString("isAuthenticated",null)==null) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            MainActivity.this.finish();
            return;
        }

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Resources res = getResources();
        String[] topic_types = res.getStringArray(R.array.topics_array);


        List<String> topic_types_list = Arrays.asList(topic_types);

        RecyclerView rvTopics = (RecyclerView) findViewById(R.id.rv_topics);

        // Set layout manager to position the items
        rvTopics.setLayoutManager(new GridLayoutManager(this, 1));


        // Create adapter passing in the sample user data
        MainAdapter adapter = new MainAdapter(topic_types_list);

        // Attach the adapter to the recyclerview to populate items
        rvTopics.setAdapter(adapter);


        SharedPreferences account_information = PreferenceManager.getDefaultSharedPreferences(this);
        String name = account_information.getString("name", null);
        String city = account_information.getString("city", null);
        String countries = account_information.getString("countries",null);
        TextView n = (TextView)findViewById(R.id.main_activity_name);
        n.setText(name);
        TextView f = (TextView)findViewById(R.id.main_activity_from);
        f.setText(city + ", " + countries);
        JSONObject jsonObj = new JSONObject();
        try {
                jsonObj.put("name",name);
                jsonObj.put("countries", countries );
                jsonObj.put("city",city);
                jsonObj.put("messageAmount",account_information.getString("messageAmount", null));
                JSONArray fat = new JSONArray(account_information.getString("favourite_associations_types",""));
                jsonObj.put("favourite_association_types",fat);
                JSONArray c = new JSONArray(account_information.getString("contacts","") );
                jsonObj.put("contacts",c);

        } catch (JSONException e) {
                e.printStackTrace();
        }
        System.out.println(jsonObj.toString().replaceAll("\\\\", ""));

        Intent intentGCMListen = new Intent(this,GcmReceiver.class);
        startService(intentGCMListen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



}
