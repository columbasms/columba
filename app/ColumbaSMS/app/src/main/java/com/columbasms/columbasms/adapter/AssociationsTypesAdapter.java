package com.columbasms.columbasms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.model.AssociationType;

import java.util.List;

/**
 * Created by Matteo Brienza on 24/12/2015
 */

public class AssociationsTypesAdapter extends RecyclerView.Adapter<AssociationsTypesAdapter.ViewHolder> {

    private static List<AssociationType> associationTypes;

    // Pass in the contact array into the constructor
    public AssociationsTypesAdapter(List<AssociationType> ass) {
        associationTypes = ass;
    }

    // Provide a direct reference to each of the views within a data item
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public ImageView favourite;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.association_name);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
            favourite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            AssociationType n = associationTypes.get(pos);
            if(n.isSelected()) {
                v.setBackgroundResource(R.drawable.check_circle_deselected);
                n.setSelected(false);
            }else{
                v.setBackgroundResource(R.drawable.check_circle_selected);
                n.setSelected(true);
            }
            associationTypes.set(pos, n);
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
        AssociationType ass = associationTypes.get(position);
        String type_name = ass.getType_name();
        boolean isSelected = ass.isSelected();

        // Set item views based on the data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(type_name);

        ImageView button = viewHolder.favourite;
        if (isSelected==false){
            button.setBackgroundResource(R.drawable.check_circle_deselected);
        }else button.setBackgroundResource(R.drawable.check_circle_selected);

    }


    // Return the total count of items
    @Override
    public int getItemCount() {
        return associationTypes.size();
    }

    public List<AssociationType> getAssociationTypes(){
        return associationTypes;
    }

}