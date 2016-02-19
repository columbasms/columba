package com.columbasms.columbasms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.activity.TopicProfileActivity;
import com.columbasms.columbasms.fragment.AskContactsInputFragment;
import com.columbasms.columbasms.fragment.ChooseContactsFragment;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.model.Topic;
import com.columbasms.columbasms.utils.Utils;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo Brienza on 2/10/16.
 */
public class CampaignsTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CharityCampaign> mItemList;
    private FragmentManager fragmentManager;
    private Resources res;
    private Activity mainActivity;

    private int lastPosition;

    public CampaignsTabAdapter (List<CharityCampaign> itemList,FragmentManager ft,Resources r,Activity a) {
        mItemList = itemList;
        fragmentManager = ft;
        res = r;
        mainActivity = a;
        lastPosition = -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaign_tab, parent, false);
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
                i.putExtra("ass_id", a.getId());
                i.putExtra("ass_name", a.getOrganization_name());
                v.getContext().startActivity(i);
            }
        });

        TextView message = holder.message;
        message.setText(c.getMessage());

        ImageView s = holder.send;
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("message", c.getMessage());
                bundle.putString("campaign_id", c.getId());
                bundle.putString("ass_id", a.getId());
                SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(mainActivity);

                if (p.getString("thereIsaGroup", "").equals("")){
                    ChooseContactsFragment newFragment = new ChooseContactsFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(fragmentManager, a.getOrganization_name());
                }else{
                    AskContactsInputFragment newFragment = new AskContactsInputFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(fragmentManager, a.getOrganization_name());
                }
            }
        });

        ImageView share = holder.share;
        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = c.getMessage();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                mainActivity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        final ImageView p = holder.profile_image;
        Utils.downloadImage(a.getAvatar_normal(), p, true, false);

        TextView time = holder.timestamp;
        time.setText(c.getTimestamp());


    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    public static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.message)TextView message;
        @Bind(R.id.ass_name)TextView associationName;
        @Bind(R.id.send)ImageView send;
        @Bind(R.id.timestamp)TextView timestamp;
        @Bind(R.id.profile_image)ImageView profile_image;
        @Bind(R.id.share)ImageView share;

        public RecyclerItemViewHolder(final View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }

        public static RecyclerItemViewHolder newInstance(View parent) {
            return new RecyclerItemViewHolder(parent);
        }
    }


}