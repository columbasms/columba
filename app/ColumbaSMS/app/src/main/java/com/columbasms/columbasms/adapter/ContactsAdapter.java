package com.columbasms.columbasms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.model.Contact;

import java.util.List;

/**
 * Created by Federico Fanara,edited by Matteo Brienza on 10/01/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private static List<Contact> contacts;

    // Pass in the contact array into the constructor
    public ContactsAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    // Provide a direct reference to each of the views within a data item
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public ImageView favourite;
        public LinearLayout cl;

        public ViewHolder(View itemView) {
            super(itemView);
            cl = (LinearLayout)itemView.findViewById(R.id.contact_layout);
            cl.setOnClickListener(this);
            nameTextView = (TextView) itemView.findViewById(R.id.contacts_name);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
            favourite.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Contact n = contacts.get(pos);
            if(n.isSelected()) {
                favourite.setBackgroundResource(R.drawable.check_circle_deselected);
                n.setSelected(false);
            }else{
                favourite.setBackgroundResource(R.drawable.check_circle_selected);
                n.setSelected(true);
            }
            contacts.set(pos, n);
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
        Contact c = contacts.get(position);
        String type_name = c.getContact_name();
        boolean isSelected = c.isSelected();

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
        return contacts.size();
    }
    public List<Contact> getContacts(){
        return contacts;
    }
}

