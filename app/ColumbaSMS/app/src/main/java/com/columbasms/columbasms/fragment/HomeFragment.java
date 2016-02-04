package com.columbasms.columbasms.fragment;

/**
 * Created by Matteo Brienza on 1/29/16.
 */
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.listener.HidingScrollListener;
import com.columbasms.columbasms.adapter.MainAdapter;
import com.columbasms.columbasms.model.CharityCampaign;
import com.google.gson.JsonArray;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

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
    private Toolbar tb;
    private static SwipeRefreshLayout mySwipeRefreshLayout;
    private static MainAdapter adapter;
    private static FragmentManager fragmentManager;
    private static Resources res;
    private static Activity mainActivity;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        mySwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3, R.color.refresh_progress_4);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mySwipeRefreshLayout.setRefreshing(true);
                        CacheRequest cacheRequest = getData();
                        MyApplication.getInstance().addToRequestQueue(cacheRequest);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mySwipeRefreshLayout.post(new Runnable() {
                 @Override
                 public void run() {
                        mySwipeRefreshLayout.setRefreshing(true);
                         CacheRequest cacheRequest = getData();
                         MyApplication.getInstance().addToRequestQueue(cacheRequest);
                         mySwipeRefreshLayout.setRefreshing(false);
                 }
        });

        return v;
    }

    private static CacheRequest getData(){
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
                                JSONObject campaignObj = jsonArray.getJSONObject(i);
                                String associationName = campaignObj.getString("associationName");
                                String topic = campaignObj.getString("topic");
                                String message = campaignObj.getString("message");
                                String image_url = campaignObj.getString("thumbnail_image_url");
                                CharityCampaign m = new CharityCampaign(associationName,topic,message,image_url);
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




    public static class CacheRequest extends Request<NetworkResponse> {
        private final Response.Listener<NetworkResponse> mListener;
        private final Response.ErrorListener mErrorListener;

        public CacheRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
        }


        @Override
        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
            Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
            if (cacheEntry == null) {
                cacheEntry = new Cache.Entry();
            }
            final long cacheHitButRefreshed = 1 * 5 * 1000;    // in 5 seconds cache will be hit, but also refreshed on background
            final long cacheExpired = 24 * 60 * 60 * 1000;      // in 24 hours this cache entry expires completely
            long now = System.currentTimeMillis();
            final long softExpire = now + cacheHitButRefreshed;
            final long ttl = now + cacheExpired;
            cacheEntry.data = response.data;
            cacheEntry.softTtl = softExpire;
            cacheEntry.ttl = ttl;
            String headerValue;
            headerValue = response.headers.get("Date");
            if (headerValue != null) {
                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }
            headerValue = response.headers.get("Last-Modified");
            if (headerValue != null) {
                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }
            cacheEntry.responseHeaders = response.headers;
            return Response.success(response, cacheEntry);
        }

        @Override
        protected void deliverResponse(NetworkResponse response) {
            mListener.onResponse(response);
        }

        @Override
        protected VolleyError parseNetworkError(VolleyError volleyError) {
            return super.parseNetworkError(volleyError);
        }

        @Override
        public void deliverError(VolleyError error) {
            mErrorListener.onErrorResponse(error);
        }
    }
}