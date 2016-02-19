package com.columbasms.columbasms.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.AssociationsTabAdapter;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.utils.network.API_URL;
import com.columbasms.columbasms.utils.network.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo Brienza on 2/9/16.
 */
public class AssociationsTabFragment extends Fragment {

    private static Toolbar toolbar;
    private static String TOPIC_ID;
    private static TextView hide;
    private static RecyclerView rv_associations;
    private static SwipeRefreshLayout mySwipeRefreshLayout;
    private static CoordinatorLayout coordinatorLayout;
    private static List<Association> associations_list;
    private static Activity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_associations, container, false);

        TOPIC_ID = getArguments().getString("topic_id");
        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar_topic);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        associations_list = new ArrayList<>();
        mainActivity = getActivity();

        coordinatorLayout = (CoordinatorLayout)v.findViewById(R.id.topic_coordinatorLayout);
        mySwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.topic_swiperefresh_more_association);
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

        rv_associations = (RecyclerView)v.findViewById(R.id.rv_more_association);
        rv_associations.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        mySwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mySwipeRefreshLayout.setRefreshing(true);
                getData();
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

    private static void getData(){

        if(!isNetworkConnected())showSnackbar();

        CacheRequest associationsRequest = getAssociationsList();

        MyApplication.getInstance().addToRequestQueue(associationsRequest);

    }


    private static CacheRequest getAssociationsList(){

        String URL = API_URL.TOPICS_URL + "/" + TOPIC_ID + API_URL.ASSOCIATIONS;


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

                                Association a = new Association(o.getString("id"),o.getString("organization_name"),o.getString("avatar_normal"),o.getString("cover_normal"), o.getString("description"),o.getInt("followers"),false);

                                associations_list.add(a);

                            } catch (JSONException e) {
                                System.out.println("JSON Parsing error: " + e.getMessage());
                            }
                        }
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

                AssociationsTabAdapter ata = new AssociationsTabAdapter(associations_list);

                rv_associations.setAdapter(ata);
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
        snackbar.show();
    }

    private static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
