package com.columbasms.columbasms.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.columbasms.columbasms.AdapterCallback;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.TopicProfileActivity;
import com.columbasms.columbasms.fragment.TopicsFragment;
import com.columbasms.columbasms.model.Topic;
import com.columbasms.columbasms.utils.Utils;
import com.columbasms.columbasms.utils.network.API_URL;
import com.google.android.gms.gcm.GcmPubSub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matteo Brienza on 24/12/2015
 */

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {

    private static List<Topic> topics;
    private static Activity activity;
    private static AdapterCallback adapterCallback;
    private float scale;
    private static String URL;
    private Context context;

    // Pass in the contact array into the constructor
    public TopicsAdapter(List<Topic> ass, Activity a, float s,AdapterCallback ac) {
            topics = ass;
            scale = s;
            activity = a;
            adapterCallback = ac;
    }

    public List<Topic> getTopics(){
        return topics;
    }

    @Override
    public TopicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_topics, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);



        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TopicsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position

        String follow_text = context.getResources().getString(R.string.follow);
        String following_text = context.getResources().getString(R.string.following);

        final Topic topic = topics.get(position);
        String type_name = topic.getName();
        boolean isSelected = topic.isFollowed();

        TextView follow = viewHolder.follow;
        if (isSelected==false){
            follow.setText(follow_text);
        }else {
            follow.setText(following_text);
        }


        CardView cv = viewHolder.cl;
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), TopicProfileActivity.class);
                i.putExtra("topic_name",topic.getName());
                i.putExtra("topic_id",topic.getId());
                v.getContext().startActivity(i);
            }
        });

        int margin_2 = (int) (2 * scale + 0.5f);
        int margin_4 = (int) (4 * scale + 0.5f);
        int height = (int) (230 * scale + 0.5f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height);
        if(position%2==0)  layoutParams.setMargins(margin_4,margin_2,margin_2,margin_2);
        else layoutParams.setMargins(margin_2,margin_2,margin_4,margin_2);
        cv.setLayoutParams(layoutParams);

        // Set item views based on the data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(type_name);

        LinearLayout bc =viewHolder.bc;
        bc.setBackgroundColor(Color.parseColor(topic.getMainColor()));

        ImageView im = viewHolder.image;
        Utils.downloadImage(topic.getImage(), im, false, false);
    }


    // Return the total count of items
    @Override
    public int getItemCount() {
        if(topics==null)return 0;
        else return topics.size();
    }

    public List<Topic> getAssociationTypes(){
        return topics;
    }


    // Provide a direct reference to each of the views within a data item
    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.card_layout)CardView cl;
        @Bind(R.id.background_card)LinearLayout bc;
        @Bind(R.id.association_name)TextView nameTextView;
        @Bind(R.id.follow)TextView follow;
        @Bind(R.id.cover_image)ImageView image;
        @OnClick(R.id.follow)
        public void onClick(View v) {

            final int pos = getAdapterPosition();
            final Topic n = topics.get(pos);
            //SETUP URL
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
            URL = API_URL.USERS_URL + "/" + sp.getString("user_id","") +  API_URL.TOPICS + "/" + n.getId();
            System.out.println(URL);


            final ProgressDialog dialog = new ProgressDialog(activity);
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_progress);

            RequestQueue requestQueue = Volley.newRequestQueue(activity);

            StringRequest putRequest = new StringRequest(Request.Method.PUT, URL,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            if(n.isFollowed()) {
                                n.setFollowed(false);
                            }else{
                                n.setFollowed(true);
                            }
                            topics.set(pos, n);

                            dialog.dismiss();
                            adapterCallback.onMethodCallback();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.toString());
                            dialog.dismiss();
                        }
                    }
            ) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    String credentials = "47ccf9098174f48be281f86103b9" + ":" + "c5906274ba1a14711a816db53f0d";
                    String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
                    headers.put("Authorization", "Basic " + credBase64);
                    return headers;
                }

            };



            requestQueue.add(putRequest);
        }

        public ViewHolder(final View parent) {
            super(parent);
            ButterKnife.bind(this,parent);
        }

    }




}


