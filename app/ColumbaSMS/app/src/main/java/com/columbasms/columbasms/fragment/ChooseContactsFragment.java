package com.columbasms.columbasms.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.ContactsAdapter;
import com.columbasms.columbasms.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo Brienza on 2/2/16.
 */
public class ChooseContactsFragment extends DialogFragment {

    private ContactsAdapter adapter;
    private List<Contact> contactList;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_select_contacts, null);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        addContacts();
        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView)v.findViewById(R.id.rv_contactList);

        // Set layout manager to position the items
        rvContacts.setLayoutManager(new GridLayoutManager(getActivity(),1));

        // Create adapter passing in the sample user data
        adapter = new ContactsAdapter(contactList);

        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);

        return builder.create();
    }


    public void addContacts(){
        contactList = new ArrayList<Contact>();
        try {
            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if (phones != null) {
                while (phones.moveToNext()) {
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactList.add(new Contact(name,phoneNumber,true));
                }
            }
            phones.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
