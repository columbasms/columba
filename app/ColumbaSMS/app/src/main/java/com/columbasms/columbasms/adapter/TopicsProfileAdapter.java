package com.columbasms.columbasms.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.activity.TopicProfileActivity;
import com.columbasms.columbasms.fragment.ChooseContactsFragment;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.model.Contact;
import com.columbasms.columbasms.utils.CustomLinearLayoutManager;
import com.columbasms.columbasms.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo Brienza on 2/7/16.
 */
public class TopicsProfileAdapter extends RecyclerView.Adapter<TopicsProfileAdapter.ViewHolder>{


    private static final int TYPE_ASSOCIATION_LIST = 0;
    private static final int TYPE_CAMPAIGNS_LIST = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class AssociationListViewHolder extends ViewHolder{

        @Bind(R.id.rv_associations_list) RecyclerView associations_list;

        public  AssociationListViewHolder(View parent){
            super(parent);
            ButterKnife.bind(this, parent);
        }

    }

    public class MainListViewHolder extends ViewHolder{

        //@Bind(R.id.topic)TextView topic;
        @Bind(R.id.message)TextView message;
        //@Bind(R.id.ass_name)TextView associationName;
        @Bind(R.id.send)ImageView send;
        @Bind(R.id.profile_image)ImageView profile_image;

        public MainListViewHolder (View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }

    }

    private List<CharityCampaign> campaigns;
    private List<Association> associations;
    private Context mContext;
    private FragmentManager fragmentManager;

    public TopicsProfileAdapter(List<CharityCampaign> campaigns,List<Association> associations,Context mContext,FragmentManager fragmentManager) {
            this.campaigns = campaigns;
            this.associations = associations;
            this.mContext = mContext;
            this.fragmentManager = fragmentManager;
    }
    @Override
    public int getItemViewType(int position) {
        // here your custom logic to choose the view type
        return position == 0 ? TYPE_ASSOCIATION_LIST : TYPE_CAMPAIGNS_LIST;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            return new AssociationListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_association_horizontal_list, parent, false));
        }

        return new MainListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case TYPE_ASSOCIATION_LIST:


                AssociationListViewHolder holder1 = (AssociationListViewHolder)holder;
                holder1.associations_list.setHasFixedSize(false);
                holder1.associations_list.setHorizontalScrollBarEnabled(true);

                // Use a linear layout manager
                CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                holder1.associations_list.setLayoutManager(mLayoutManager);

                // Create adapter passing in the sample user data
                AssociationListAdapter adapter = new AssociationListAdapter(associations);

                // Attach the adapter to the recyclerview to populate items
                holder1.associations_list.setAdapter(adapter);

                break;

            case TYPE_CAMPAIGNS_LIST:

                /*
                MainListViewHolder holder2 = (MainListViewHolder)holder;
                final CharityCampaign c = campaigns.get(position-1);

                ImageView profile_image = holder2.profile_image;
                profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), AssociationProfileActivity.class);
                        v.getContext().startActivity(i);
                    }
                });

                TextView an = holder2.associationName;
                an.setText(c.getAssociationName());
                an.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), AssociationProfileActivity.class);
                        v.getContext().startActivity(i);
                    }
                });

                TextView topic = holder2.topic;
                topic.setText(c.getTopic());
                topic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), TopicProfileActivity.class);
                        i.putExtra("topic_name", c.getTopic());
                        v.getContext().startActivity(i);
                    }
                });
                //selectColor(topic, c.getTopic());


                TextView message = holder2.message;
                message.setText(c.getMessage());

                ImageView s = holder2.send;
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("message",c.getMessage());
                        ChooseContactsFragment newFragment = new ChooseContactsFragment();
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, c.getAssociationName());
                    }
                });

                final ImageView p = holder2.profile_image;
                Utils.downloadImage(c.getImage_url(), p, true,false);

                */
                break;
        }

    }

    @Override
    public int getItemCount() {
        return campaigns == null ? 0 : campaigns.size()+1;
    }
}
