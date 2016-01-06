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
 * Created by Matteo Brienza on 24/12/2015
 */

public class AssociationsTypesAdapter extends RecyclerView.Adapter<AssociationsTypesAdapter.ViewHolder> {

    private List<String> associations;
    static int s = 0;

    // Pass in the contact array into the constructor
    public AssociationsTypesAdapter(List<String> ass) {
        associations = ass;
    }

    // Provide a direct reference to each of the views within a data item
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView;
        public ImageView favourite;

        public ViewHolder(View itemView) {
            super(itemView);


            nameTextView = (TextView) itemView.findViewById(R.id.association_name);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(s == 0) {
                        favourite.setBackgroundResource(R.drawable.check_circle_deselected);
                        s=1;
                    }else{
                        favourite.setBackgroundResource(R.drawable.check_circle_selected);
                        s=0;
                    }
                }
            });

        }
    }


    @Override
    public AssociationsTypesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_association, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(AssociationsTypesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String a = associations.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(a);

        ImageView button = viewHolder.favourite;
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return associations.size();
    }
}