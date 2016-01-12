package com.columbasms.columbasms;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.columbasms.columbasms.adapter.MainAdapter;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Resources res = getResources();
        String[] topic_types = res.getStringArray(R.array.topics_array);


        List<String> topic_types_list = Arrays.asList(topic_types);

        RecyclerView rvTopics = (RecyclerView) findViewById(R.id.rv_topics);

        // Set layout manager to position the items
        rvTopics.setLayoutManager(new GridLayoutManager(this,1));


        // Create adapter passing in the sample user data
        MainAdapter adapter = new MainAdapter(topic_types_list);

        // Attach the adapter to the recyclerview to populate items
        rvTopics.setAdapter(adapter);


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
