package com.columbasms.columbasms.fragment;

/**
 * Created by Matteo Brienza on 1/29/16.
 */
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.adapter.MainAdapter;
import com.columbasms.columbasms.listener.HidingScrollListener;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.TopicsAdapter;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.model.Topic;
import com.columbasms.columbasms.utils.API_URL;
import com.columbasms.columbasms.utils.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;


public class TopicsFragment extends Fragment{

    private static TopicsAdapter adapter;
    private static List<Topic> topics_list;
    private static float s;
    private static RecyclerView rvTopics;
    private static SwipeRefreshLayout mySwipeRefreshLayout;
    private static CoordinatorLayout coordinatorLayout;
    private static Activity mainActivity;
    private static Toolbar tb;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Resources res = getResources();

        topics_list = new ArrayList<>();

        mainActivity = getActivity();

        // Set layout manager to position the items
        rvTopics.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvTopics.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        // Create adapter passing in the sample user data
        s = getResources().getDisplayMetrics().density;

        getData();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_topics, container, false);
        rvTopics = (RecyclerView)v.findViewById(R.id.rv_topics);
        tb = (Toolbar)getActivity().findViewById(R.id.toolbar_bottom);
        coordinatorLayout = (CoordinatorLayout)v.findViewById(R.id.coordinatorLayout);
        mySwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
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
        return v;
    }


    private void hideViews() {
        tb.animate().translationY(tb.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        tb.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    private static void getData(){

        CacheRequest cacheRequest = get();

        MyApplication.getInstance().addToRequestQueue(cacheRequest);

        if(!isNetworkConnected())showSnackbar();

    }


    private static CacheRequest get(){

        //DOPO DIGITS L'URL SARA' QUESTO ---> https://www.columbasms.com/api/v1/users/{id}/topics
        //String URL = API_URL.USERS_URL + USER_ID + API_URL.TOPICS;

        String URL = API_URL.TOPICS_URL;

        return new CacheRequest(0, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {

                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONArray jsonArray = new JSONArray(jsonString);

                    System.out.println(jsonString);

                    topics_list.clear();

                    if (jsonArray.length() > 0) {

                        // looping through json and adding to movies list
                        for (int i = jsonArray.length()-1; i >=0; i--) {
                            try {
                                JSONObject o = jsonArray.getJSONObject(i);

                                //Topic t = new Topic(o.getString("id"),o.getString("name"),false,o.getString("main_color"), o.getString("status_color"));
                                Topic t = new Topic(o.getString("id"),o.getString("name"),false);

                                topics_list.add(t);

                            } catch (JSONException e) {
                                System.out.println("JSON Parsing error: " + e.getMessage());
                            }
                        }
                    }
                    // Create adapter passing in the sample user data
                    adapter = new TopicsAdapter(topics_list,s);

                    // Attach the adapter to the recyclerview to populate items
                    rvTopics.setAdapter(adapter);

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

}