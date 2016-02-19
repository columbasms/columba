package com.columbasms.columbasms.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.columbasms.columbasms.model.Contact;
import com.columbasms.columbasms.utils.Utils;
import com.columbasms.columbasms.utils.network.API_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matteo Brienza on 2/2/16.
 */
public class ChooseContactsFragment extends DialogFragment implements View.OnClickListener {

    private ContactsAdapter adapter;
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
    private static RecyclerView rvContacts;
    private static JSONArray contacts;
    private static List<Contact> contacts_withSelection;
    private static Resources res;
    private static Activity activity;
    private static TextView save_as_a_grouptext;


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
        USER_ID = p.getString("user_id","NOID");

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final View v = inflater.inflate(R.layout.dialog_select_contacts, null);
        rvContacts = (RecyclerView)v.findViewById(R.id.rv_contactList);
        save_as_a_group = (ImageView)v.findViewById(R.id.save_as_a_group);
        save_as_a_grouptext = (TextView)v.findViewById(R.id.save_as_a_group_text);
        sab = (ImageView)v.findViewById(R.id.select_all);


        if(getArguments().getString("flag")!=null){
            //HAI PREMUTO TRUST
            builder.setView(v).setPositiveButton("confirm",null).setNegativeButton("cancel",null);
        }else builder.setView(v).setPositiveButton("send",null).setNegativeButton("cancel",null);

        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface d) {
                    Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("send");
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 2);
                                } else{
                                    send();
                                    callGroupInsertion();
                                    dialog.dismiss();
                                }
                            } else{
                                send();
                                callGroupInsertion();
                                dialog.dismiss();
                            }
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



        //ADD CONTACTS TO DIALOG (WITH CONTACTS FILTERING BASED ON USERS PREFERENCES FOR THE CURRENT ASSOCIATION)
        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }else {
                addContacts();
                setupRecyclerView();
            }
        }else {
            addContacts();
            setupRecyclerView();
        }


        return dialog;
        //return builder.create();
    }



    public void callGroupInsertion(){

        if(save_as_a_group.getTag().equals("1")){
            Bundle bundle = new Bundle();
            bundle.putString("ass_id", getArguments().getString("ass_id"));
            bundle.putString("new_group", jsonArray.toString());
            if(getArguments().getString("flag")!=null){
                bundle.putString("flag", "true");
            }
            SaveGroupFragment newFragment = new SaveGroupFragment();
            newFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().add(newFragment, null).commitAllowingStateLoss();
        }

    }





    public  void send(){
        //SEND MESSAGES TO SELECTED CONTACTS (AND SAVE SELECTION FOR ASSOCIATION)
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        jsonArray = new JSONArray();
        final List<Contact> contacts_list = adapter.getContacts();
        contacts_withSelection = new ArrayList<>();
        Contact temp;
        final JSONArray j = new JSONArray();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            temp = contacts_list.get(i);
            if (temp.isSelected()) {
                contacts_withSelection.add(temp);
                String phoneNumber = temp.getContact_number();
                String name = temp.getContact_name();
                try {
                    JSONObject single_contact = new JSONObject();
                    single_contact.put("number", phoneNumber.replaceAll("\\s+", ""));
                    j.put(single_contact);
                    single_contact.put("name", name);
                    jsonArray.put(single_contact);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if(getArguments().getString("flag")!=null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key + "_forTrusting", j.toString());
            editor.commit();
        }


        if(getArguments().getString("flag")==null){


            if (contacts_withSelection.size() != 0) {

                final String URL = API_URL.USERS_URL + "/" + USER_ID + API_URL.CAMPAIGNS + "/" + CAMPAIGN_ID;

                System.out.println(URL);

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                JSONObject body = new JSONObject();

                try {
                    body.put("users", j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(body.toString());


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("Invio a: ");
                        try {
                            contacts = new JSONArray(response.getString("users"));
                            System.out.println(contacts.toString());
                            for (int i = 0; i < contacts.length(); i++) {
                                try {
                                    String number = contacts_withSelection.get((int) contacts.get(i)).getContact_number();
                                    System.out.println("NUMERO: " + number);
                                    Utils.sendSMS(assName, number, message,res);
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









    public void setupRecyclerView(){

        save_as_a_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView b = (ImageView)v;
                if(b.getTag().equals("1")){
                    b.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                    b.setTag("0");
                }else{
                    b.setBackgroundResource(R.drawable.ic_check_box_black_24dp);
                    b.setTag("1");
                }
            }
        });


        //SETUP SELECT/DESELECT ALL FUNCTION
        if(allContactsSelected()){
            sab.setBackgroundResource(R.drawable.ic_check_box_black_24dp);
            sab.setTag(1);
        }

        sab.setOnClickListener(this);

        // Set layout manager to position the items
        rvContacts.setLayoutManager(new GridLayoutManager(getActivity(),1));

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int[] colors = new int[contactList.size()];
        for(int i = 0; i<colors.length; i++){
            colors[i] = generator.getRandomColor();
        }

        // Create adapter passing in the sample user data
        adapter = new ContactsAdapter(contactList,colors);

        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
    }














    public void addContacts(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String fc = prefs.getString(key, null);
        JSONArray contacts_selected = null;
        try {
            if(fc!=null) {
                contacts_selected = new JSONArray(fc);
            }
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







    private boolean allContactsSelected(){
        int size = contactList.size();
        for (int i = 0; i < size ; i++) {
            if(contactList.get(i).isSelected()==false)return false;
        }
        return true;
    }









    @Override
    public void onClick(View v) {
        ImageView b = (ImageView)v;
        int size = contactList.size();
        Contact temp;
        if(b.getTag().equals("1")){
            b.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black_24dp);
            b.setTag("0");
            for (int i = 0; i < size ; i++) {
                temp = contactList.get(i);
                contactList.set(i,new Contact(temp.getContact_name(),temp.getContact_number(),false));
            }
        }else{
            b.setBackgroundResource(R.drawable.ic_check_box_black_24dp);
            b.setTag("1");
            for (int i = 0; i < size ; i++) {
                temp = contactList.get(i);
                contactList.set(i,new Contact(temp.getContact_name(),temp.getContact_number(),true));
            }
        }
        adapter.notifyDataSetChanged();

    }









    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    addContacts();
                    setupRecyclerView();
                } else {}
                return;
            }
            case 2: {
                System.out.println("PERMISSION GRANTED SMS");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    send();
                    callGroupInsertion();
                    dialog.dismiss();
                } else {}
                return;
            }
        }
    }
}
