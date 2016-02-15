package com.columbasms.columbasms.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.columbasms.columbasms.AdapterCallback;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.TopicProfileActivity;
import com.columbasms.columbasms.fragment.ChooseContactsFragment;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.model.Topic;
import com.columbasms.columbasms.utils.Utils;
import com.columbasms.columbasms.utils.network.API_URL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo Brienza on 2/1/16.
 */


public class AssociationProfileAdapter extends RecyclerView.Adapter<AssociationProfileAdapter.ViewHolder> {

    private List<CharityCampaign> mItemList;
    private Association association;
    private int card_size;
    private Drawable profile_image;
    private Resources res;
    private static Activity activity;
    private FragmentManager fragmentManager;
    private AdapterCallback adapterCallback;

    private static final int TYPE_PROFILE = 0;
    private static final int TYPE_GROUP = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }
    public class GroupViewHolder extends ViewHolder {

        @Bind(R.id.topic)TextView topic;
        @Bind(R.id.message)TextView message;
        @Bind(R.id.ass_name)TextView associationName;
        @Bind(R.id.send)ImageView send;
        @Bind(R.id.profile_image)ImageView profile_image;

        public GroupViewHolder(View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }
    }

    public class ProfileViewHolder extends ViewHolder {

        @Bind(R.id.card_layout)LinearLayout lc;
        @Bind(R.id.lc_background)LinearLayout lc_background;
        @Bind(R.id.profile_card)CardView cardView;
        @Bind(R.id.profile_ass_name)TextView assName;
        @Bind(R.id.profile_ass_description)TextView assDescription;
        @Bind(R.id.profile_ass_other_info)TextView assOtherInfo;
        @Bind(R.id.association_campaigns)TextView assCampaignsTitle;
        @Bind(R.id.fol) Button trust;
        @Bind(R.id.fav) ImageView favourite;
        @Bind(R.id.cover_image) ImageView coverImage;
        @Bind(R.id.thumbnail_image) ImageView thumbnailImage;


        public ProfileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }




    public AssociationProfileAdapter(List<CharityCampaign> itemList,Association a,Resources res, Activity activity, FragmentManager fragmentManager,AdapterCallback ac) {
        mItemList = itemList;
        this.association = a;
        this.res = res;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.adapterCallback = ac;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ProfileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_association_profile, parent, false));
        }

        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        // here your custom logic to choose the view type
        return position == 0 ? TYPE_PROFILE : TYPE_GROUP;
    }

    @Override
    public void onBindViewHolder (ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {

            case TYPE_PROFILE:
                final ProfileViewHolder holder1 = (ProfileViewHolder) viewHolder;

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);



                final String URL = API_URL.USERS_URL + "/" + sp.getString("user_id","") + API_URL.ASSOCIATIONS + "/" + association.getId();

                //activity.getWindow().setStatusBarColor(Color.parseColor(color_status));
                //holder1.lc_background.setBackgroundColor(Color.parseColor(color_main));

                holder1.assName.setText(association.getOrganization_name());

                String info = association.getFollowers() + " followers" + " - " + mItemList.size() + " campaigns";

                holder1.assOtherInfo.setText(info);

                holder1.assDescription.setText(association.getDescription());

                final CardView v = holder1.cardView;
                final String parameter;
                Button t = holder1.trust;

                if(association.isTrusting()){
                    System.out.println(association.isTrusting() + "qui1");
                    t.setBackgroundResource(R.drawable.button_trusted);
                    t.setText("TRUSTED");
                    t.setTextColor(Color.parseColor("#ffffff"));
                    parameter = "false";
                    t.setTag("1");
                }else{
                    System.out.println(association.isTrusting() + "qui2");
                    t.setBackgroundResource(android.R.color.white);
                    t.setText("TRUST");
                    t.setTextColor(res.getColor(R.color.colorPrimaryDark));
                    parameter = "true";
                    t.setTag("0");
                }
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final ProgressDialog dialog = new ProgressDialog(activity);
                        dialog.show();
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_progress);

                        RequestQueue requestQueue = Volley.newRequestQueue(activity);

                        final String URL_TRUSTING = URL + "?trusted=" + parameter;

                        System.out.println(URL_TRUSTING);

                        StringRequest putRequest = new StringRequest(Request.Method.PUT, URL_TRUSTING,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
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
                });


                v.post(new Runnable() {
                    @Override
                    public void run() {
                        card_size = v.getHeight();
                    }

                });

                ImageView f = holder1.favourite;
                if (association.isFollowing()){
                    f.setBackgroundResource(R.drawable.ic_favorite_white_36dp);
                    t.setVisibility(View.VISIBLE);
                    f.setTag("1");
                }else{
                    f.setBackgroundResource(R.drawable.ic_favorite_border_white_36dp);
                    t.setVisibility(View.GONE);
                    f.setTag("0");
                }


                holder1.favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final ProgressDialog dialog = new ProgressDialog(activity);
                        dialog.show();
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_progress);

                        RequestQueue requestQueue = Volley.newRequestQueue(activity);

                        System.out.println(URL);

                        StringRequest putRequest = new StringRequest(Request.Method.PUT, URL,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
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
                });

                final ImageView cover = holder1.coverImage;
                Utils.downloadImage(association.getCover_normal(),cover,false,false);

                final ImageView p = holder1.thumbnailImage;
                Utils.downloadImage(association.getAvatar_normal(),p,true,true);


                break;

            case TYPE_GROUP:


                GroupViewHolder holder2 = (GroupViewHolder) viewHolder;
                final CharityCampaign c = mItemList.get(position-1);


                TextView an = holder2.associationName;
                an.setText(c.getOrganization().getOrganization_name());

                final List<Topic> topicList = c.getTopics();
                String topics = "";
                for (int i = 0; i<topicList.size(); i++){
                    topics += topicList.get(i).getName(); //IF MULTITOPIC ADD "\N"
                }
                TextView topic = holder2.topic;
                topic.setText(topicList.get(0).getName());
                topic.setTextColor(Color.parseColor(topicList.get(0).getMainColor()));

                TextView message = holder2.message;
                message.setText(c.getMessage());

                ImageView s = holder2.send;
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("message", c.getMessage());
                        bundle.putString("campaign_id", c.getId());
                        ChooseContactsFragment newFragment = new ChooseContactsFragment();
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, association.getOrganization_name());
                    }
                });

                final ImageView pi = holder2.profile_image;
                Utils.downloadImage(association.getAvatar_normal(),pi,true,false);

                break;
        }
    }


    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size()+1;
    }


    public int getCardSize(){
        return card_size;
    }


}