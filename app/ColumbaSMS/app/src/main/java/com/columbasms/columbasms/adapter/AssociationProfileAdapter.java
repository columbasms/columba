package com.columbasms.columbasms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.utils.Utils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

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
    Activity activity;

    private static final int TYPE_PROFILE = 0;
    private static final int TYPE_GROUP = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }
    public class GroupViewHolder extends ViewHolder {

        //@Bind(R.id.topic)TextView topic;
        @Bind(R.id.message)TextView message;
        //@Bind(R.id.ass_name)TextView associationName;
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




    public AssociationProfileAdapter(List<CharityCampaign> itemList,Association a,Resources res, Activity activity) {
        mItemList = itemList;
        this.association = a;
        this.res = res;
        this.activity = activity;
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

                /*
                int color = selectColor(association.getTopic());
                activity.getWindow().setStatusBarColor(color);

                holder1.lc_background.setBackgroundColor(color);

                holder1.assName.setText(association.getName());

                String info = association.getFollower() + " followers" + " - " + mItemList.size() + " campaigns";

                holder1.assOtherInfo.setText(info);

                holder1.assDescription.setText(association.getDescription());

                String title = association.getName() + "'s campaign";

                holder1.assCampaignsTitle.setText(title);

                final CardView v = holder1.cardView;
                holder1.trust.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button trust = holder1.trust;
                        if(trust.getTag().equals("0")){
                            trust.setBackgroundResource(R.drawable.button_trusted);
                            trust.setText("TRUSTED");
                            trust.setTextColor(Color.parseColor("#ffffff"));
                            trust.setTag("1");
                        }else{
                            trust.setBackgroundResource(android.R.color.white);
                            trust.setText("TRUST");
                            trust.setTextColor(res.getColor(R.color.colorPrimaryDark));
                            trust.setTag("0");
                        }

                    }
                });
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        card_size = v.getHeight();
                    }

                });
                holder1.favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView fav = holder1.favourite;
                        Button trust = holder1.trust;
                        if(fav.getTag().equals("0")){
                            fav.setBackgroundResource(R.drawable.ic_favorite_white_36dp);
                            trust.setVisibility(View.VISIBLE);
                            trust.setBackgroundResource(android.R.color.white);
                            trust.setText("TRUST");
                            trust.setTextColor(res.getColor(R.color.colorPrimaryDark));
                            trust.setTag("0");
                            fav.setTag("1");
                        }else{
                            fav.setBackgroundResource(R.drawable.ic_favorite_border_white_36dp);
                            trust.setVisibility(View.GONE);
                            fav.setTag("0");
                        }

                    }
                });

                final ImageView cover = holder1.coverImage;
                Utils.downloadImage(association.getCover_image_url(),cover,false,false);

                final ImageView p = holder1.thumbnailImage;
                Utils.downloadImage(association.getThumbnail_image_url(),p,true,true);

                */

                break;

            case TYPE_GROUP:

                /*
                GroupViewHolder holder2 = (GroupViewHolder) viewHolder;
                CharityCampaign c = mItemList.get(position);


                TextView an = holder2.associationName;
                an.setText(c.getAssociationName());

                TextView topic = holder2.topic;
                topic.setText(c.getTopic());
                topic.setTextColor(selectColor(c.getTopic()));

                TextView message = holder2.message;
                message.setText(c.getMessage());

                final ImageView pi = holder2.profile_image;
                Utils.downloadImage(association.getThumbnail_image_url(),pi,true,false);
                */
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    public int getCardSize(){
        return card_size;
    }

    private int selectColor(String topicName){
        int color = 0;
        String[] colorArray = res.getStringArray(R.array.topics_color);
        String[] topics_name = res.getStringArray(R.array.topics_name);
        for (int i = 0; i<topics_name.length; i++){
            if(topicName.equals(topics_name[i])){
                color = Color.parseColor(colorArray[i]);
                break;
            }
        }
        return color;
    }

}