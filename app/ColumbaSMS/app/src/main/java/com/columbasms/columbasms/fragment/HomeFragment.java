package com.columbasms.columbasms.fragment;

/**
 * Created by Matteo Brienza on 1/29/16.
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.listener.HidingScrollListener;
import com.columbasms.columbasms.adapter.MainAdapter;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.utils.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {

    private static String URL_API= "http://www.architettura204.it/test.json";
    private static RecyclerView rvFeed;
    private static List<CharityCampaign> campaigns_list;
    private static Toolbar tb;
    private static SwipeRefreshLayout mySwipeRefreshLayout;
    private static CoordinatorLayout coordinatorLayout;
    private static MainAdapter adapter;
    private static FragmentManager fragmentManager;
    private static Resources res;
    private static Activity mainActivity;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Init campaigns list
        campaigns_list = new ArrayList<>();

        // Set layout manager to position the items
        rvFeed.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rvFeed.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                tb.animate().translationY(tb.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }

            @Override
            public void onShow() {
                tb.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }
        });

        fragmentManager = getFragmentManager();
        res = getResources();
        mainActivity = getActivity();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rvFeed= (RecyclerView)v.findViewById(R.id.rv_feed);
        tb = (Toolbar)getActivity().findViewById(R.id.toolbar_bottom);
        coordinatorLayout = (CoordinatorLayout)v.findViewById(R.id.coordinatorLayout);
        mySwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3, R.color.refresh_progress_4);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getData();
                    }
                }
        );
        /*
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mySwipeRefreshLayout.post(new Runnable() {
                 @Override
                 public void run() {
                     getData();
                 }
        });

        return v;
    }



    private static void getData(){

            mySwipeRefreshLayout.setRefreshing(true);

            CacheRequest cacheRequest = get();

            MyApplication.getInstance().addToRequestQueue(cacheRequest);

            mySwipeRefreshLayout.setRefreshing(false);

            if(!isNetworkConnected())showSnackbar();

    }


    private static CacheRequest get(){
        return new CacheRequest(0, URL_API, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,HttpHeaderParser.parseCharset(response.headers));

                    JSONArray jsonArray = new JSONArray(jsonString);

                    campaigns_list.clear();

                    if (jsonArray.length() > 0) {

                        // looping through json and adding to movies list
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject o = jsonArray.getJSONObject(i);

                                CharityCampaign m = new CharityCampaign(o.getString("associationName"),o.getString("topic"),o.getString("message"),o.getString("thumbnail_image_url"));

                                campaigns_list.add(0, m);

                            } catch (JSONException e) {
                                System.out.println("JSON Parsing error: " + e.getMessage());
                            }
                        }
                    }
                    // Create adapter passing in the sample user data
                    adapter = new MainAdapter(campaigns_list,fragmentManager,res,mainActivity);

                    // Attach the adapter to the recyclerview to populate items
                    rvFeed.setAdapter(adapter);

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