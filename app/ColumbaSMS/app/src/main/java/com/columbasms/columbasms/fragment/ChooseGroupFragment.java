package com.columbasms.columbasms.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.ContactsAdapter;
import com.columbasms.columbasms.adapter.ContactsGroupAdapter;
import com.columbasms.columbasms.model.Contact;
import com.columbasms.columbasms.model.ContactsGroup;
import com.columbasms.columbasms.utils.Utils;
import com.columbasms.columbasms.utils.network.API_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matteo Brienza on 2/16/16.
 */
public class ChooseGroupFragment extends DialogFragment {

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

        res = getActivity().getResources();
        activity = getActivity();

        //GET ASSOCIATION NAME FOR THIS CAMPAIGN AND CREATE KEY
        assName = getTag();
        key =  assName + "_contacts";
        message = getArguments().getString("message");

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CAMPAIGN_ID = getArguments().getString("campaign_id");
        USER_ID = p.getString("user_id", "NOID");

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        //RETRIEVE GROUPS
        JSONArray groups = null;
        try {
            groups = new JSONArray(p.getString("groups",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<ContactsGroup> contactsGroupList = new ArrayList<>();
        for (int i = 0; i<groups.length(); i++){
            try {
                JSONObject o = new JSONObject(groups.getString(i));
                contactsGroupList.add(new ContactsGroup(o.getString("name"),new JSONArray(o.getString("contacts")),false));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_select_groups, null);
        if(getArguments().getString("flag")!=null){
            builder.setView(v).setPositiveButton("confirm",null).setNegativeButton("cancel",null);
        }else  builder.setView(v).setPositiveButton("send",null).setNegativeButton("cancel",null);
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface d) {

                final JSONArray key_forTrust = new JSONArray();
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONArray groupsForTrusting = new JSONArray();
                        List<ContactsGroup> allGroups = adapter.getContactsGroupList();
                        for(int i = 0; i<allGroups.size(); i++){
                            ContactsGroup temp = allGroups.get(i);
                            if(temp.isSelected()){
                                if(getArguments().getString("flag")!=null){
                                    //YOU CAME FROM TRUST CLICK
                                    JSONObject group = new JSONObject();
                                    try {
                                        group.put("name",temp.getName());
                                        group.put("contacts",temp.getContactList().toString());
                                        groupsForTrusting.put(group);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    try {
                                        sendSmsToGroup(temp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor_account_information = sp.edit();
                        editor_account_information.putString(getArguments().getString("ass_id") +"_groups_forTrusting", groupsForTrusting.toString());
                        editor_account_information.remove(getArguments().getString("ass_id") +"_contacts_forTrusting");
                        editor_account_information.apply();
                        dialog.dismiss();
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

        rvGroups = (RecyclerView)v.findViewById(R.id.rv_groupsList);
        // Set layout manager to position the items
        rvGroups.setLayoutManager(new GridLayoutManager(getActivity(),1));

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int[] colors = new int[groups.length()];
        for(int i = 0; i<colors.length; i++){
            colors[i] = generator.getRandomColor();
        }

        // Create adapter passing in the sample user data
        adapter = new ContactsGroupAdapter(contactsGroupList,colors);

        // Attach the adapter to the recyclerview to populate items
        rvGroups.setAdapter(adapter);


        return dialog;
        //return builder.create();
    }


    public void sendSmsToGroup(ContactsGroup group) throws JSONException {
        final JSONArray contactsList = group.getContactList();
        JSONArray j = new JSONArray();
        for(int i = 0; i<contactsList.length(); i++){
            JSONObject temp = new JSONObject();
            temp.put("number",contactsList.getString(i));
            j.put(temp);
        }

        if (contactsList.length() != 0) {

            final String URL = API_URL.USERS_URL + "/" + USER_ID + API_URL.CAMPAIGNS + "/" + CAMPAIGN_ID;

            System.out.println(URL);

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            JSONObject body = new JSONObject();

            try {
                body.put("users", j);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    System.out.println("Invio a: ");
                    try {
                        contacts = new JSONArray(response.getString("users"));
                        System.out.println(contacts.toString());
                        for (int i = 0; i < contacts.length(); i++) {
                            try {
                                JSONObject j =  new JSONObject(contactsList.getString((int) contacts.get(i)));
                                System.out.println("NUMERO: " + j.getString("number"));
                                Utils.sendSMS(assName, j.getString("number"), message,res);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    String credentials = "47ccf9098174f48be281f86103b9" + ":" + "c5906274ba1a14711a816db53f0d";
                    String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
                    headers.put("Authorization", "Basic " + credBase64);
                    return headers;
                }

            };

            requestQueue.add(jsonObjectRequest);
        }

    }
}
