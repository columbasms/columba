package com.columbasms.columbasms.adapter;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.model.Contact;

import java.util.List;

/**
 * Created by Matteo Brienza on 2/2/16.
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
        public ImageView contacts_image;
        public ImageView favourite;
        public LinearLayout cl;

        public ViewHolder(View itemView) {
            super(itemView);
            cl = (LinearLayout)itemView.findViewById(R.id.contact_layout);
            cl.setOnClickListener(this);
            nameTextView = (TextView) itemView.findViewById(R.id.contacts_name);
            contacts_image = (ImageView)itemView.findViewById(R.id.contacts_image);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
            favourite.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Contact n = contacts.get(pos);

            if(n.isSelected()) {
                favourite.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                n.setSelected(false);
            }else{
                favourite.setBackgroundResource(R.drawable.ic_check_box_black_24dp);
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

        ImageView contacts_image = viewHolder.contacts_image;
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(type_name.substring(0, 1), color1);
        contacts_image.setImageDrawable(drawable);


        ImageView button = viewHolder.favourite;
        if (isSelected==false){
            button.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }else button.setBackgroundResource(R.drawable.ic_check_box_black_24dp);

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

