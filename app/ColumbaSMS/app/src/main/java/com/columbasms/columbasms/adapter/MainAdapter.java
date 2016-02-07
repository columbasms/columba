package com.columbasms.columbasms.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.activity.TopicProfileActivity;
import com.columbasms.columbasms.fragment.ChooseContactsFragment;
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
 * Created by Matteo Brienza on 10/01/16.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CharityCampaign> mItemList;
    private FragmentManager fragmentManager;
    private Resources res;
    private Activity mainActivity;

    public MainAdapter(List<CharityCampaign> itemList,FragmentManager ft,Resources r,Activity a) {
        mItemList = itemList;
        fragmentManager = ft;
        res = r;
        mainActivity = a;
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

        ImageView profile_image = holder.profile_image;
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AssociationProfileActivity.class);
                v.getContext().startActivity(i);
            }
        });

        TextView an = holder.associationName;
        an.setText(c.getAssociationName());
        an.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AssociationProfileActivity.class);
                v.getContext().startActivity(i);
            }
        });

        TextView topic = holder.topic;
        topic.setText(c.getTopic());
        topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), TopicProfileActivity.class);
                i.putExtra("topic_name", c.getTopic());
                v.getContext().startActivity(i);
            }
        });
        selectColor(topic, c.getTopic());


        TextView message = holder.message;
        message.setText(c.getMessage());

        ImageView s = holder.send;
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

        final ImageView p = holder.profile_image;
        Utils.downloadImage(c.getImage_url(), p, true,false);

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
        @Bind(R.id.profile_image)ImageView profile_image;

        public RecyclerItemViewHolder(final View parent) {
            super(parent);
            ButterKnife.bind(this,parent);
        }

        public static RecyclerItemViewHolder newInstance(View parent) {
            return new RecyclerItemViewHolder(parent);
        }
    }

    private void selectColor(TextView t, String topicName){
        String[] colorArray = res.getStringArray(R.array.topics_color);
        String[] topics_name = res.getStringArray(R.array.topics_name);
        for (int i = 0; i<topics_name.length; i++){
            if(topicName.equals(topics_name[i])){
                t.setTextColor(Color.parseColor(colorArray[i]));
                break;
            }
        }
    }

}