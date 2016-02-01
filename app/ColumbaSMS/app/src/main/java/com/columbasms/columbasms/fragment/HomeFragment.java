package com.columbasms.columbasms.fragment;

/**
 * Created by Matteo Brienza on 1/29/16.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.listener.HidingScrollListener;
import com.columbasms.columbasms.adapter.MainAdapter;
import com.columbasms.columbasms.model.CharityCampaign;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment{

    private static String URL_API= "http://www.architettura204.it/test.json";
    private static RecyclerView rvFeed;
    private static List<CharityCampaign> campaigns_list;
    private Toolbar tb;
    private static SwipeRefreshLayout mySwipeRefreshLayout;
    private static MainAdapter adapter;


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


        //Get data from server
        JsonArrayRequest req = getData("get");
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rvFeed= (RecyclerView)v.findViewById(R.id.rv_feed);
        tb = (Toolbar)getActivity().findViewById(R.id.toolbar_bottom);
        mySwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mySwipeRefreshLayout.setRefreshing(true);
                        JsonArrayRequest req = getData("refresh");
                        MyApplication.getInstance().addToRequestQueue(req);
                    }
                }
        );
        return v;
    }



    private static JsonArrayRequest getData(final String r){
        return new JsonArrayRequest(URL_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        campaigns_list.clear();
                        if (response.length() > 0) {

                            // looping through json and adding to movies list
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject campaignObj = response.getJSONObject(i);
                                    String associationName = campaignObj.getString("associationName");
                                    String topic = campaignObj.getString("topic");
                                    String message = campaignObj.getString("message");
                                    CharityCampaign m = new CharityCampaign(associationName,topic,message);
                                    campaigns_list.add(0, m);

                                } catch (JSONException e) {
                                    System.out.println("JSON Parsing error: " + e.getMessage());
                                }
                            }


                        }
                        if(r.equals("get")) {
                            // Create adapter passing in the sample user data
                            adapter = new MainAdapter(campaigns_list);
                            // Attach the adapter to the recyclerview to populate items
                            rvFeed.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Server Error: " + error.getMessage());
            }
        });
    }
}