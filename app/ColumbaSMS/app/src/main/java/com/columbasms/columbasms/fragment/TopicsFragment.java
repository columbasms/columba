package com.columbasms.columbasms.fragment;

/**
 * Created by Matteo Brienza on 1/29/16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.columbasms.columbasms.AdapterCallback;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.listener.HidingScrollListener;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.TopicsAdapter;
import com.columbasms.columbasms.model.Topic;
import com.columbasms.columbasms.utils.network.API_URL;
import com.columbasms.columbasms.utils.network.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class TopicsFragment extends Fragment implements AdapterCallback {

    private static TopicsAdapter adapter;
    private static float s;
    private static RecyclerView rvTopics;
    private static List<Topic> topics_list;
    private static SwipeRefreshLayout mySwipeRefreshLayout;
    private static CoordinatorLayout coordinatorLayout;
    private static Activity mainActivity;
    private static Toolbar tb;
    private static String USER_ID;
    private static AdapterCallback adapterCallback;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Resources res = getResources();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        USER_ID =  sp.getString("user_id","");
        topics_list = new ArrayList<>();
        mainActivity = getActivity();
        adapterCallback = this;

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

        if(!isNetworkConnected())showSnackbar();

        CacheRequest cacheRequest = getTopics();

        MyApplication.getInstance().addToRequestQueue(cacheRequest);

    }


    private static CacheRequest getTopics(){

        String URL = API_URL.USERS_URL + "/" + USER_ID + API_URL.TOPICS + "?locale=" + Locale.getDefault().getLanguage();

        System.out.println(URL);

        return new CacheRequest(0, URL, new Response.Listener<NetworkResponse>() {

            @Override
            public void onResponse(NetworkResponse response) {
                try {

                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONArray jsonArray = new JSONArray(jsonString);

                    topics_list.clear();

                    System.out.println(jsonString);

                    if (jsonArray.length() > 0) {

                        // looping through json and adding to topics list
                        for (int i = jsonArray.length()-1; i >=0; i--) {
                            try {
                                JSONObject o = jsonArray.getJSONObject(i);

                                Topic t = new Topic(o.getString("id"),o.getString("name"),o.getBoolean("following"),o.getString("main_color"), o.getString("status_color"),o.getString("image_mobile"));

                                topics_list.add(t);

                            } catch (JSONException e) {
                                System.out.println("JSON Parsing error: " + e.getMessage());
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();

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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mySwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                adapter = new TopicsAdapter(topics_list, mainActivity, s, adapterCallback);

                // Attach the adapter to the recyclerview to populate items

                rvTopics.setAdapter(adapter);

                getData();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onMethodCallback() {
        getData();
    }
}