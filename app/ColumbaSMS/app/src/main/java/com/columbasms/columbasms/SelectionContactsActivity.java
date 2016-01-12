package com.columbasms.columbasms;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.columbasms.columbasms.adapter.ContactsAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Federico on 10/01/16.
 */
public class SelectionContactsActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<JSONObject> contacts = new ArrayList<>();

    /*public void addContacts(){

        //to store name-number pair
        JSONObject obj = new JSONObject();

        try {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                obj.put(name, phoneNumber);
                contacts.add(obj);

                //Log.e("Contact list with name & numbers", " " + contacts);
            }
            phones.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selection);

        Button selectAll_button = (Button)findViewById(R.id.selectAll_button);
        selectAll_button.setOnClickListener(this);

        Button done_button = (Button)findViewById(R.id.button_done_flat_contacts);
        done_button.setOnClickListener(this);

        Resources res = getResources();
        String[] contacts_names = res.getStringArray(R.array.contacts_array);


        List<String> contacts_list = Arrays.asList(contacts_names);

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rv_contacts);

        // Set layout manager to position the items
        rvContacts.setLayoutManager(new GridLayoutManager(this,1));


        // Create adapter passing in the sample user data
        ContactsAdapter adapter = new ContactsAdapter(contacts_list);

        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);



    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.selectAll_button){
            ((Button)v).setBackgroundResource(R.drawable.check_circle_deselected);
        }
        if(v.getId()==R.id.button_done_flat_contacts){
        SelectionContactsActivity.this.finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        }

    }
}
