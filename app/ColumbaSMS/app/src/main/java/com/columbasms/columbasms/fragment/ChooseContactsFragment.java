package com.columbasms.columbasms.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.ContactsAdapter;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.model.Contact;
import com.columbasms.columbasms.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo Brienza on 2/2/16.
 */
public class ChooseContactsFragment extends DialogFragment {

    private ContactsAdapter adapter;
    private List<Contact> contactList;
    private String assName;
    private String key;
    private String message;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //GET ASSOCIATION NAME FOR THIS CAMPAIGN AND CREATE KEY
        assName = getTag();
        key =  assName + "_contacts";
        message = "";

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
                        //SEND MESSAGES TO SELECTED CONTACTS (AND SAVE SELECTION FOR ASSOCIATION)
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        JSONArray jsonArray = new JSONArray();
                        List<Contact> contacts_withSelection = adapter.getContacts();
                        Contact temp;
                        for (int i = 0; i < adapter.getItemCount(); i++) {
                            temp = contacts_withSelection.get(i);
                            if(temp.isSelected())jsonArray.put(temp.getContact_number());
                        }
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(key, jsonArray.toString());
                        editor.commit();

                        System.out.println("SELECTED CONTACTS: " + jsonArray.toString());
                        for(int i = 0; i<jsonArray.length(); i++){
                            try {
                                Utils.sendSMS(assName,jsonArray.get(i).toString(),message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //NO ACTION
                    }
                });

        //ADD CONTACTS TO DIALOG (WITH CONTACTS FILTERING BASED ON USERS PREFERENCES FOR THE CURRENT ASSOCIATION)
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String fc = prefs.getString(key, null);
        JSONArray contacts_selected = null;
        try {
            if(fc!=null)
            contacts_selected = new JSONArray(fc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        contactList = new ArrayList<Contact>();

        try {
            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if (phones != null) {
                while (phones.moveToNext()) {
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Contact c = new Contact(name,phoneNumber,false);
                    if(contacts_selected!=null){
                        if(findContactByPhone(contacts_selected,phoneNumber))c.setSelected(true);
                    }
                    contactList.add(c);

                }
            }
            phones.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    boolean findContactByPhone(JSONArray a, String n){
        for (int i = 0; i < a.length(); i++) {
            try {
                if(a.get(i).toString().equals(n))return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
