package com.columbasms.columbasms.activity;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.AssociationProfileAdapter;
import com.columbasms.columbasms.adapter.UserProfileAdapter;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.model.Topic;
import com.columbasms.columbasms.model.User;
import com.columbasms.columbasms.utils.network.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Federico on 07/02/16.
 */


//COPIARE ASSOCIATIONPROFILEACTIVITY SOSTITUENDO LE PARTI PER LO USER

public class UserProfileActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_profile)Toolbar toolbar;
    private static RecyclerView rvUserProfile;

    private static SwipeRefreshLayout mySwipeRefreshLayout;
    private static CoordinatorLayout coordinatorLayout;
    private static ImageView fav;
    private static List<CharityCampaign> campaigns_list;
    private static UserProfileAdapter userProfileAdapter;
    private static String URL_API= "https://www.columbasms.com/api/v1/";
    private static String usrName;
    private static Activity activity;
    int toolbar_size;
    ColorDrawable cd;
    static Resources res;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ButterKnife.bind(this);

        res = getResources();
        activity = this;

        mySwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh_usr_profile);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3, R.color.refresh_progress_4);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //getData();
                    }
                }
        );

        rvUserProfile = (RecyclerView)findViewById(R.id.rv_user_profile);

        cd = new ColorDrawable(getResources().getColor(R.color.colorPrimary));

        //TOP TOOLBAR SETUP
        toolbar.bringToFront();
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setBackgroundDrawable(cd);
        //GET HEIGHT OF TOOLBAR TO FADE ANIMATION
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar_size = toolbar.getHeight();
            }

        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileActivity.this.finish();
            }
        });

        campaigns_list = new ArrayList<>();

        campaigns_list.add(new CharityCampaign("", "", null, null));
        campaigns_list.add(new CharityCampaign("", "", null, null));
        campaigns_list.add(new CharityCampaign("", "", null, null));
        campaigns_list.add(new CharityCampaign("", "", null, null));


        // Set layout manager to position the items
        rvUserProfile.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rvUserProfile.setOnScrollListener(new RecyclerView.OnScrollListener() {

            int scrollDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                /*int card_size = userProfileAdapter.getCardSize();
                scrollDy += dy;

                if (scrollDy > card_size) {
                    cd.setAlpha(255);
                    toolbar.setTitle(usrName);
                } else if (scrollDy <= 0) {
                    cd.setAlpha(0);
                    toolbar.setTitle("");
                } else {
                    cd.setAlpha((int) ((255.0 / card_size) * scrollDy));
                    toolbar.setTitle("");
                }*/
            }
        });

        //getData();

        UserProfileAdapter a = new UserProfileAdapter(campaigns_list, new User("", "", "", ""), res, activity );

        rvUserProfile.setAdapter(a);
    }



    /*private static void getData(){

        mySwipeRefreshLayout.setRefreshing(true);

        CacheRequest cacheRequest = get();

        MyApplication.getInstance().addToRequestQueue(cacheRequest);

        mySwipeRefreshLayout.setRefreshing(false);

        //if(!isNetworkConnected())showSnackbar();

    }


    private static CacheRequest get(){
        return new CacheRequest(0, URL_API, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONObject o = new JSONObject(jsonString);

                    User a = new User("","","","");
                    //Association a = new Association(o.getString("organization_name"),o.getString("description"),o.getString("topic"),o.getString("avatar_normal"),o.getString("cover_normal"),1000,false);;

                    JSONArray jsonArray = new JSONArray(o.getString("campaigns"));

                    campaigns_list.clear();

                    if (jsonArray.length() > 0) {

                        // looping through json and adding to movies list
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {

                                JSONObject temp = jsonArray.getJSONObject(i);

                                usrName = a.getFullName();


                                campaigns_list.add(null);

                            } catch (JSONException e) {
                                System.out.println("JSON Parsing error: " + e.getMessage());
                            }
                        }
                    }

                    Association ass = new Association("id", "name", "avatar", "cover", "desc");
                    Topic top = new Topic("id", "name", false, "color", "status");
                    List<Topic> topics = new ArrayList<Topic>();
                    topics.add(top);
                    CharityCampaign c = new CharityCampaign("1", "prova", ass, topics);

                    campaigns_list.add(c);

                    // Create adapter passing in the sample user data
                    userProfileAdapter = new UserProfileAdapter(campaigns_list,a,res,activity);

                    // Attach the adapter to the recyclerview to populate items
                    rvUserProfile.setAdapter(userProfileAdapter);

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
        CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.bottomMargin = tb.getHeight();
        view.setLayoutParams(params);
        snackbar.show();
    }

    private static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    */


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
