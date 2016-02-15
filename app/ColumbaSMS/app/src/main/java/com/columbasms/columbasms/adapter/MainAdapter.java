package com.columbasms.columbasms.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.activity.TopicProfileActivity;
import com.columbasms.columbasms.fragment.ChooseContactsFragment;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.model.Topic;
import com.columbasms.columbasms.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo Brienza on 10/01/16.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CharityCampaign> mItemList;
    private FragmentManager fragmentManager;
    private Resources res;
    private Activity mainActivity;

    private int lastPosition;

    public MainAdapter(List<CharityCampaign> itemList,FragmentManager ft,Resources r,Activity a) {
        mItemList = itemList;
        fragmentManager = ft;
        res = r;
        mainActivity = a;
        lastPosition = -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return RecyclerItemViewHolder.newInstance(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
            final RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
            final CharityCampaign c = mItemList.get(position);
            final Association a = c.getOrganization();

            ImageView profile_image = holder.profile_image;
            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), AssociationProfileActivity.class);
                    i.putExtra("ass_id",a.getId());
                    i.putExtra("ass_name",a.getOrganization_name());
                    v.getContext().startActivity(i);
                }
            });

            TextView an = holder.associationName;
            an.setText(a.getOrganization_name());
            an.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), AssociationProfileActivity.class);
                    i.putExtra("ass_id",a.getId());
                    i.putExtra("ass_name",a.getOrganization_name());
                    v.getContext().startActivity(i);
                }
            });


            final List<Topic> topicList = c.getTopics();
            String topics = "";
            for (int i = 0; i<topicList.size(); i++){
                topics += topicList.get(i).getName(); //IF MULTITOPIC ADD "\N"
            }
            TextView topic = holder.topic;
            topic.setText(topics);
            topic.setTextColor(Color.parseColor(c.getTopics().get(0).getMainColor()));
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


            final TextView message = holder.message;
            message.setText(c.getMessage());

            ImageView send = holder.send;
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("message", c.getMessage());
                    bundle.putString("campaign_id", c.getId());
                    ChooseContactsFragment newFragment = new ChooseContactsFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(fragmentManager, a.getOrganization_name());
                }
            });

            ImageView share = holder.share;
            share.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, c.getMessage());
                    intent.setPackage("com.google.android.apps.plus");
                    mainActivity.startActivity(intent);
                }
            });

        TextView time = holder.timestamp;
        time.setText(c.getTimestamp());
        final ImageView p = holder.profile_image;
        Utils.downloadImage(a.getAvatar_normal(), p, true, false);


    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    public static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.topic)TextView topic;
        @Bind(R.id.message)TextView message;
        @Bind(R.id.ass_name)TextView associationName;
        @Bind(R.id.send)ImageView send;
        @Bind(R.id.share)ImageView share;
        @Bind(R.id.timestamp)TextView timestamp;
        @Bind(R.id.profile_image)ImageView profile_image;

        public RecyclerItemViewHolder(final View parent) {
            super(parent);
            ButterKnife.bind(this,parent);
        }

        public static RecyclerItemViewHolder newInstance(View parent) {
            return new RecyclerItemViewHolder(parent);
        }
    }



}