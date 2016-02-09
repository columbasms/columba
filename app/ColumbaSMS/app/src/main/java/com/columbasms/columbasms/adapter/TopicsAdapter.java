package com.columbasms.columbasms.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.TopicProfileActivity;
import com.columbasms.columbasms.model.Topic;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matteo Brienza on 24/12/2015
 */

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {

    private static List<Topic> topics;
    private static String[] topicsColor;
    private float scale;

    // Pass in the contact array into the constructor
    public TopicsAdapter(List<Topic> ass, float s) {
            topics = ass;
            scale = s;
    }

    @Override
    public TopicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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

        final Topic topic = topics.get(position);
        String type_name = topic.getName();
        boolean isSelected = topic.isSelected();

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
        //bc.setBackgroundColor(topic.getMainColor());



        TextView follow = viewHolder.follow;
        if (isSelected==false){
            follow.setText("FOLLOW");
        }else follow.setText("FOLLOWING");


    }


    // Return the total count of items
    @Override
    public int getItemCount() {
        return topics.size();
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
        @OnClick(R.id.follow)
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Topic n = topics.get(pos);
            if(n.isSelected()) {
                follow.setText("FOLLOW");
                n.setSelected(false);
            }else{
                follow.setText("FOLLOWING");
                n.setSelected(true);
            }
            topics.set(pos, n);
        }

        public ViewHolder(final View parent) {
            super(parent);
            ButterKnife.bind(this,parent);
        }

    }

}