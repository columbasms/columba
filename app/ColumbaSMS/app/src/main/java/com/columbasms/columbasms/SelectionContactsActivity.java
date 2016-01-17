package com.columbasms.columbasms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.columbasms.columbasms.adapter.ContactsAdapter;
import com.columbasms.columbasms.model.AssociationType;
import com.columbasms.columbasms.model.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Federico Fanara, edited by Matteo Brienza on 10/01/16.
 */
public class SelectionContactsActivity extends AppCompatActivity implements View.OnClickListener{

    private ContactsAdapter adapter;
    private List<Contact> contactList;

    public void addContacts(){
        contactList = new ArrayList<Contact>();
        try {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selection);

        Button selectAll_button = (Button)findViewById(R.id.selectAll_button);
        selectAll_button.setOnClickListener(this);

        Button done_button = (Button)findViewById(R.id.button_done_flat_contacts);
        done_button.setOnClickListener(this);

        addContacts();
        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rv_contacts);

        // Set layout manager to position the items
        rvContacts.setLayoutManager(new GridLayoutManager(this,1));

        // Create adapter passing in the sample user data
        adapter = new ContactsAdapter(contactList);

        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);



    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.selectAll_button){
            Button b = (Button)v;
            Contact temp;
            if(b.getTag().equals("1")){
                b.setBackgroundResource(R.drawable.check_circle_deselected);
                b.setTag("0");
                for (int i = 0; i <contactList.size() ; i++) {
                    temp = contactList.get(i);
                    contactList.set(i,new Contact(temp.getContact_name(),temp.getContact_number(),false));
                }
            }else{
                b.setBackgroundResource(R.drawable.check_circle);
                b.setTag("1");
                for (int i = 0; i <contactList.size() ; i++) {
                    temp = contactList.get(i);
                    contactList.set(i,new Contact(temp.getContact_name(),temp.getContact_number(),true));
                }
            }
            adapter.notifyDataSetChanged();
        }
        if(v.getId()==R.id.button_done_flat_contacts){

                //store selected contacts
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                JSONArray jsonArray = new JSONArray();
                String jsonObjName = "contacts";
                List<Contact> contacts_withSelection = adapter.getContacts();
                Contact temp;
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    temp = contacts_withSelection.get(i);
                    if(temp.isSelected())jsonArray.put(temp.getContact_number());
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(jsonObjName, jsonArray.toString());

                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put(jsonObjName, jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //SAVE AUTHENTICATION STATE
                editor.putString("isAuthenticated","true");
                editor.commit();

                SelectionContactsActivity.this.finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }

    }

}
