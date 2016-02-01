package com.columbasms.columbasms.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.model.CharityCampaign;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo Brienza on 2/1/16.
 */


public class AssociationProfileAdapter extends RecyclerView.Adapter<AssociationProfileAdapter.ViewHolder> {

    private List<CharityCampaign> mItemList;
    private int card_size;

    private static final int TYPE_PROFILE = 0;
    private static final int TYPE_GROUP = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }
    public class GroupViewHolder extends ViewHolder {
        @Bind(R.id.message)TextView message;
        @Bind(R.id.campaign_name)TextView campaignName;

        public GroupViewHolder(View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }
    }

    public class ProfileViewHolder extends ViewHolder {

        @Bind(R.id.card_layout)LinearLayout lc;
        @Bind(R.id.profile_card)CardView cardView;
        @Bind(R.id.profile_ass_name)TextView assName;
        @Bind(R.id.profile_ass_description)TextView assDescription;
        @Bind(R.id.profile_ass_other_info)TextView assOtherInfo;

        public ProfileViewHolder(View itemView) {
            super (itemView);
            ButterKnife.bind(this, itemView);
        }
    }




    public AssociationProfileAdapter(List<CharityCampaign> itemList) {
        mItemList = itemList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ProfileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_association_profile, parent, false));
        }

        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_association_profile, parent, false));
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
                ProfileViewHolder holder1 = (ProfileViewHolder) viewHolder;
                final CardView v = holder1.cardView;
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        card_size = v.getHeight();
                    }

                });
                break;

            case TYPE_GROUP:
                GroupViewHolder holder2 = (GroupViewHolder) viewHolder;
                CharityCampaign c = mItemList.get(position);

                /*
                TextView an = holder2.associationName;
                an.setText(c.getAssociationName());
                */

                TextView message = holder2.message;
                message.setText(c.getMessage());
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

}