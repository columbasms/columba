package com.columbasms.columbasms.adapter;

import android.content.Context;
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
import com.columbasms.columbasms.model.ContactsGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.List;

/**
 * Created by Matteo Brienza on 2/16/16.
 */
public class ContactsGroupAdapter extends RecyclerView.Adapter<ContactsGroupAdapter.ViewHolder> {

    private int[] colors;
    private boolean colorAlreadySelected;
    private static List<ContactsGroup> contactsGroupList;

    // Pass in the contact array into the constructor
    public ContactsGroupAdapter(List<ContactsGroup> contactsGroupList, int[] colors) {
        this.contactsGroupList = contactsGroupList;
        this.colors = colors;
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return contactsGroupList == null ? 0 : contactsGroupList.size();
    }

    public List<ContactsGroup> getContactsGroupList() {
        return contactsGroupList;
    }


    @Override
    public ContactsGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        colorAlreadySelected = false;

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_contact, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ContactsGroupAdapter.ViewHolder viewHolder, int position) {

        ContactsGroup g = contactsGroupList.get(position);
        String group_name = g.getName();
        boolean isSelected = g.isSelected();

        // Set item views based on the data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(group_name);

        ImageView contacts_image = viewHolder.contacts_image;
        TextDrawable drawable = TextDrawable.builder().buildRound(group_name.substring(0, 1), colors[position]);
        contacts_image.setImageDrawable(drawable);


    }

    // Provide a direct reference to each of the views within a data item
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public ImageView contacts_image;
        public ImageView favourite;
        public LinearLayout cl;

        public ViewHolder(View itemView) {
            super(itemView);
            cl = (LinearLayout) itemView.findViewById(R.id.contact_layout);
            cl.setOnClickListener(this);
            nameTextView = (TextView) itemView.findViewById(R.id.contacts_name);
            contacts_image = (ImageView) itemView.findViewById(R.id.contacts_image);
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder().buildRound("", color1);
            contacts_image.setImageDrawable(drawable);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
            favourite.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            ContactsGroup g = contactsGroupList.get(pos);
            if (g.isSelected()) {
                favourite.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                g.setSelected(false);
            } else {
                favourite.setBackgroundResource(R.drawable.ic_check_box_black_24dp);
                g.setSelected(true);
            }
            contactsGroupList.set(pos, g);
        }
    }
}

