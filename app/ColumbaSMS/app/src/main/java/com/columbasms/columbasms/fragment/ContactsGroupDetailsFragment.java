package com.columbasms.columbasms.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.ContactsGroupAdapter;
import com.columbasms.columbasms.model.Contact;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo Brienza on 2/17/16. //CLASS NOT USED IN THIS MILESTONE
 */
public class ContactsGroupDetailsFragment extends DialogFragment {

    private ContactsGroupAdapter adapter;
    private List<Contact> contactList;
    private ImageView sab;
    private ImageView save_as_a_group;
    private String assName;
    private String key;
    private String message;
    private static AlertDialog dialog;
    private static JSONArray jsonArray;

    private String CAMPAIGN_ID;
    private String USER_ID;
    private static RecyclerView rvGroups;
    private static JSONArray contacts;
    private static List<String> groupList;
    private static Resources res;
    private static Activity activity;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getActivity());

        int index = getArguments().getInt("index");

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        //RETRIEVE GROUPS
        JSONArray groups_keys = null;
        JSONObject groups = null;
        try {
            String s  = p.getString("keyForGroups","");
            if(s.equals("")){
                groups_keys = new JSONArray();
            }else groups_keys = new JSONArray(s);
            groups = new JSONObject(p.getString("groups",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String groupName = "";
        try {
            groupName = groups_keys.getString(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray contactsGroupList = null;
        try {
            String s = groups.getString(groupName);
            contactsGroupList = new JSONArray(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Contact> contactList = new ArrayList<>();
        for (int i = 0; i<contactsGroupList.length(); i++){
            try {
                JSONObject temp = new JSONObject(contactsGroupList.getString(i));
                contactList.add(new Contact(temp.getString("name"),temp.getString("number"),true));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_contacts_group_details, null);
        TextView group_name = (TextView)v.findViewById(R.id.group_name);
        group_name.setText(groupName);
        builder.setView(v).setPositiveButton("send",null).setNegativeButton("cancel",null);
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface d) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();


        return dialog;
    }

}
