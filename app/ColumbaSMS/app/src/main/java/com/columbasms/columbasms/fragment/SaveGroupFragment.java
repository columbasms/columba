package com.columbasms.columbasms.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.ContactsAdapter;
import com.columbasms.columbasms.model.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Matteo Brienza on 2/16/16.
 */
public class SaveGroupFragment extends DialogFragment {

    private static JSONArray groupToAdd_contacts;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Intent in = new Intent();
        getActivity().setResult(3, in);

        final SharedPreferences state = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor_account_information = state.edit();

        try {
            groupToAdd_contacts = new JSONArray(getArguments().getString("new_group"));
            System.out.println(groupToAdd_contacts.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_enter_group_name, null);
        final AppCompatEditText groupName = (AppCompatEditText)v.findViewById(R.id.group_name);
        builder.setView(v);
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupToAdd_name = groupName.getText().toString();
                JSONArray allGroups = null;
                JSONObject newGroup = new JSONObject();
                try {

                    //GET ALL GROUPS ARRAY
                    String allGroupsString = state.getString("groups","");
                    if(allGroupsString.equals("")) allGroups = new JSONArray();
                    else allGroups = new JSONArray(allGroupsString);

                    //CREATE A JSON OBJECT WITH NEW GROUP DATA AND ADD IT TO ALL GROUPS LIST
                    newGroup.put("name", groupToAdd_name);
                    newGroup.put("contacts", groupToAdd_contacts.toString());
                    allGroups.put(newGroup);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(allGroups.toString());

                if (state.getString("thereIsaGroup", "").equals(""))editor_account_information.putString("thereIsaGroup","true");
                if(getArguments().getString("flag")!=null){
                    //IF FLAG != NULL THE GROUPS JUST CREATED IS THE "TRUSTED" GROUPS
                    JSONArray groupsForTrusting = new JSONArray();
                    groupsForTrusting.put(newGroup);
                    editor_account_information.putString(getArguments().getString("ass_id") + "_groups_forTrusting", groupsForTrusting.toString());
                }
                //STORE ALL GROUPS CREATED BY USER IN A SHARED PREFERENCES
                editor_account_information.putString("groups", allGroups.toString());
                editor_account_information.remove(getArguments().getString("ass_id") + "_contacts_forTrusting");
                editor_account_information.apply();
            }
        }).setNegativeButton("dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
