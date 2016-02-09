package com.columbasms.columbasms.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.AssociationListAdapter;
import com.columbasms.columbasms.adapter.AssociationProfileAdapter;
import com.columbasms.columbasms.adapter.MainAdapter;
import com.columbasms.columbasms.adapter.TopicsProfileAdapter;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.utils.API_URL;
import com.columbasms.columbasms.utils.CacheRequest;
import com.columbasms.columbasms.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo Brienza on 2/1/16.
 */
public class TopicProfileActivity extends AppCompatActivity {

    private static String TOPIC_ID;

    private static TopicsProfileAdapter topicsProfileAdapter;
    @Bind(R.id.toolbar_topic)Toolbar toolbar;
    private static RecyclerView rv_main_list;
    private static Context mContext;
    private static List<CharityCampaign> campaigns_list;
    private static List<Association> associations_list;
    private static FragmentManager fragmentManager;
    private static SwipeRefreshLayout mySwipeRefreshLayout;
    private static CoordinatorLayout coordinatorLayout;
    private static Activity mainActivity;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_profile);

        ButterKnife.bind(this);

        String topic = getIntent().getStringExtra("topic_name");
        TOPIC_ID = getIntent().getStringExtra("topic_id");

        //TOOLBARSETUP
        toolbar.setTitle(topic);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicProfileActivity.this.finish();
            }
        });


        //SETUP ASSOCIATIONS LIST ADAPTER
        associations_list = new ArrayList<>();

        mContext = this;
        fragmentManager = getFragmentManager();
        mainActivity = this;

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.topic_coordinatorLayout);
        mySwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.topic_swiperefresh);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3, R.color.refresh_progress_4);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mySwipeRefreshLayout.setRefreshing(true);
                        getData();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        //SETUP CAMPAIGNS LIST ADAPTER
        campaigns_list = new ArrayList<>();

        rv_main_list = (RecyclerView)findViewById(R.id.rv_main_list);
        rv_main_list.setLayoutManager(new GridLayoutManager(this, 1));

        getData();
    }

    private static void getData(){

        if(!isNetworkConnected())showSnackbar();

        CacheRequest associationsRequest = getAssociationsList();

        MyApplication.getInstance().addToRequestQueue(associationsRequest);

    }


    private static CacheRequest getAssociationsList(){

        String URL = API_URL.TOPICS_URL + "/" + TOPIC_ID + API_URL.ASSOCIATIONS;

        System.out.println(URL);

        return new CacheRequest(0, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONArray jsonArray = new JSONArray(jsonString);

                    System.out.println(jsonString);

                    associations_list.clear();

                    if (jsonArray.length() > 0) {

                        // looping through json and adding to movies list
                        for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject o = jsonArray.getJSONObject(i);

                                    Association a = new Association(o.getString("id"),o.getString("organization_name"),o.getString("avatar_normal"),o.getString("cover_normal"), o.getString("description"));

                                    associations_list.add(a);

                                } catch (JSONException e) {
                                    System.out.println("JSON Parsing error: " + e.getMessage());
                                }
                        }
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
                CacheRequest campaignsRequest = getCampaignsList();

                MyApplication.getInstance().addToRequestQueue(campaignsRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

    }

    private static CacheRequest getCampaignsList(){

        String URL = API_URL.ASSOCIATIONS_URL;

        return new CacheRequest(0, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONArray jsonArray = new JSONArray(jsonString);

                    campaigns_list.clear();

                    if (jsonArray.length() > 0) {

                        // looping through json and adding to movies list
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject o = jsonArray.getJSONObject(i);

                                //CharityCampaign c = new CharityCampaign();
                                //campaigns_list.add(c);

                            } catch (JSONException e) {
                                System.out.println("JSON Parsing error: " + e.getMessage());
                            }
                        }
                    }
                    // Create adapter passing in the sample user data
                    topicsProfileAdapter = new TopicsProfileAdapter(campaigns_list,associations_list,mContext,fragmentManager);

                    // Attach the adapter to the recyclerview to populate items
                    rv_main_list.setAdapter(topicsProfileAdapter);

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

    private static void showSnackbar(){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No Internet Connection!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getData();
                    }
                });
        View view = snackbar.getView();
        snackbar.show();
    }

    private static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
