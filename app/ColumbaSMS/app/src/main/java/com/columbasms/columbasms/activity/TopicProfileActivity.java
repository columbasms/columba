package com.columbasms.columbasms.activity;

import android.os.Bundle;
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
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.utils.CacheRequest;

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

    private static String URL_API= "https://www.columbasms.com/api/v1/organizations";
    private static AssociationListAdapter associationListAdapter;
    @Bind(R.id.toolbar_topic)Toolbar toolbar;
    private static RecyclerView rv_associations_list;
    @Bind(R.id.rv_campaigns_list)RecyclerView rv_campaigns_list;

    private static List<Association> associations_list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_profile);

        ButterKnife.bind(this);

        String topic = getIntent().getStringExtra("topic_name");

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

        rv_associations_list = (RecyclerView)findViewById(R.id.rv_associations_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_associations_list.setLayoutManager(mLayoutManager);
        AssociationListAdapter mAdapter = new AssociationListAdapter(associations_list);
        rv_associations_list.setAdapter(mAdapter);

        //SETUP CAMPAIGNS LIST ADAPTER
        List<CharityCampaign> campaigns_list = new ArrayList<>();
        campaigns_list.add(new CharityCampaign("WWF",topic,"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",null));
        campaigns_list.add(new CharityCampaign("ADDA",topic,"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",null));
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_campaigns_list.setLayoutManager(lm);
        MainAdapter ma = new MainAdapter(campaigns_list,getFragmentManager(),getResources(),getParent());
        rv_campaigns_list.setAdapter(ma);

        getData();
    }

    private static void getData(){

        //mySwipeRefreshLayout.setRefreshing(true);

        CacheRequest cacheRequest = get();

        MyApplication.getInstance().addToRequestQueue(cacheRequest);

        //mySwipeRefreshLayout.setRefreshing(false);

        //if(!isNetworkConnected())showSnackbar();

    }


    private static CacheRequest get(){
        return new CacheRequest(0, URL_API, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONArray jsonArray = new JSONArray(jsonString);

                    associations_list.clear();


                    Association a = null;
                    if (jsonArray.length() > 0) {

                        // looping through json and adding to movies list
                        for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject o = jsonArray.getJSONObject(i);

                            a = new Association(o.getString("organization_name"),o.getString("description"),o.getString("avatar_normal"),o.getString("cover_normal"),1000,false);
                            associations_list.add(a);

                        } catch (JSONException e) {
                            System.out.println("JSON Parsing error: " + e.getMessage());
                        }
                        }
                    }
                    // Create adapter passing in the sample user data
                    associationListAdapter = new AssociationListAdapter(associations_list);

                    // Attach the adapter to the recyclerview to populate items
                    rv_associations_list.setAdapter(associationListAdapter);

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
}
