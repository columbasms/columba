package com.columbasms.columbasms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.activity.EditProfileActivity;
import com.columbasms.columbasms.activity.TopicProfileActivity;
import com.columbasms.columbasms.fragment.ChooseContactsFragment;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.model.Topic;
import com.columbasms.columbasms.model.User;
import com.columbasms.columbasms.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Federico on 12/02/16.
 */
public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder> {

    private List<CharityCampaign> mItemList;
    private User user;
    private int card_size;
    private Drawable profile_image;
    private Resources res;
    private Activity activity;

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
        @Bind(R.id.ass_name)TextView assName;
        @Bind(R.id.send)ImageView send;
        @Bind(R.id.share)ImageView share;
        @Bind(R.id.timestamp)TextView timestamp;
        @Bind(R.id.profile_image)ImageView profile_image;

        public GroupViewHolder(View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }
    }

    public class ProfileViewHolder extends ViewHolder {

        @Bind(R.id.card_layout_usr)LinearLayout lc;
        @Bind(R.id.lc_background_usr)LinearLayout lc_background;
        @Bind(R.id.profile_card_usr)CardView cardView;
        @Bind(R.id.profile_usr_name)TextView usrName;
        @Bind(R.id.supported_campaigns)TextView usrCampaignsTitle;
        @Bind(R.id.edit)Button edit;
        @Bind(R.id.cover_image_usr) ImageView coverImage;
        @Bind(R.id.thumbnail_image) ImageView thumbnailImage;


        public ProfileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }




    public UserProfileAdapter(List<CharityCampaign> itemList,User a,Resources res, Activity activity) {
        mItemList = itemList;
        this.user = a;
        this.res = res;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ProfileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_profile, parent, false));
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
                SharedPreferences.Editor editor_account_information = sp.edit();

                holder1.usrName.setText(user.getFullName());

                String info = "100" + " followers" + " - " + mItemList.size() + " campaigns";

                final CardView v = holder1.cardView;

                v.post(new Runnable() {
                    @Override
                    public void run() {
                        card_size = v.getHeight();
                    }

                });

                final ImageView cover = holder1.coverImage;
                Utils.downloadImage(user.getCover_image(), cover, false, false);

                final ImageView p = holder1.thumbnailImage;
                Utils.downloadImage(user.getProfile_image(),p,true,true);

                editor_account_information.putString("url_profile", user.getProfile_image());
                editor_account_information.putString("url_cover", user.getCover_image());
                editor_account_information.apply();

                final Button edit = holder1.edit;
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), EditProfileActivity.class);
                        i.putExtra("usr_name", user.getFullName());
                        v.getContext().startActivity(i);
                    }
                });


                break;

            case TYPE_GROUP:


                GroupViewHolder holder2 = (GroupViewHolder) viewHolder;
                final CharityCampaign c = mItemList.get(position - 1);
                final Association a = c.getOrganization();


                TextView an = holder2.assName;
                an.setText(a.getOrganization_name());
                an.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), AssociationProfileActivity.class);
                        i.putExtra("ass_id", a.getId());
                        i.putExtra("ass_name", a.getOrganization_name());
                        v.getContext().startActivity(i);
                    }
                });

                final List<Topic> topicList = c.getTopics();
                String topics = "";
                for (int i = 0; i<topicList.size(); i++){
                    topics += topicList.get(i).getName(); //IF MULTITOPIC ADD "\N"
                }
                TextView topic = holder2.topic;
                topic.setText(topicList.get(0).getName());
                topic.setTextColor(Color.parseColor(topicList.get(0).getMainColor()));
                topic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TEMPORARY SUPPORT FOR ONLY ONE TOPIC FOR CAMPAIGN
                        Intent i = new Intent(v.getContext(), TopicProfileActivity.class);
                        i.putExtra("topic_name", topicList.get(0).getName());
                        i.putExtra("topic_id", topicList.get(0).getId());
                        v.getContext().startActivity(i);
                    }
                });

                TextView message = holder2.message;
                message.setText(c.getMessage());

                final ImageView pi = holder2.profile_image;
                pi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), AssociationProfileActivity.class);
                        i.putExtra("ass_id", a.getId());
                        i.putExtra("ass_name", a.getOrganization_name());
                        v.getContext().startActivity(i);
                    }
                });
                Utils.downloadImage(a.getAvatar_normal(), pi, true, false);

                ImageView send_hided = holder2.send;
                send_hided.setVisibility(View.GONE);

                TextView time = holder2.timestamp;
                time.setText(c.getTimestamp());

                ImageView share_hided = holder2.share;
                share_hided.setVisibility(View.GONE);

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
