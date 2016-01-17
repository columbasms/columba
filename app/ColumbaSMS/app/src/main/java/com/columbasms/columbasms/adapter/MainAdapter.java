package com.columbasms.columbasms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.columbasms.columbasms.R;

import java.util.List;

/**
 * Created by Federico on 10/01/16.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<String> topics;
    static int s = 0;

    // Pass in the contact array into the constructor
    public MainAdapter(List<String> top) {
        topics = top;
    }

    // Provide a direct reference to each of the views within a data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView nameTextView;
        //public ImageView favourite;

        public ViewHolder(View itemView) {
            super(itemView);


            //nameTextView = (TextView) itemView.findViewById(R.id.topics_name);

        }
    }


    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View mainView = inflater.inflate(R.layout.item_topic, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(mainView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String t = topics.get(position);

        // Set item views based on the data model
        //TextView textView = viewHolder.nameTextView;
        //textView.setText(t);

        //ImageView button = viewHolder.favourite;
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return topics.size();
    }
}

