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
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<String> contacts;
    static int s = 0;

    // Pass in the contact array into the constructor
    public ContactsAdapter(List<String> cont) {
        contacts = cont;
    }

    // Provide a direct reference to each of the views within a data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView favourite;

        public ViewHolder(View itemView) {
            super(itemView);


            nameTextView = (TextView) itemView.findViewById(R.id.contacts_name);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (s == 0) {
                        favourite.setBackgroundResource(R.drawable.check_circle_deselected);
                        s = 1;
                    } else {
                        favourite.setBackgroundResource(R.drawable.check_circle_selected);
                        s = 0;
                    }
                }
            });

        }
    }


    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_contact, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String c = contacts.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(c);

        ImageView button = viewHolder.favourite;
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return contacts.size();
    }
}

