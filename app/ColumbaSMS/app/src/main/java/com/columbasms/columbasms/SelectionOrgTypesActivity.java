package com.columbasms.columbasms;

/**
 * Created by Matteo Brienza on 25/12/15.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.columbasms.columbasms.adapter.AssociationsTypesAdapter;
import com.columbasms.columbasms.model.AssociationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectionOrgTypesActivity extends AppCompatActivity implements View.OnClickListener {

    private AssociationsTypesAdapter adapter;
    private List<AssociationType> associationTypesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_organization_types);

        Button selectAll_button = (Button) findViewById(R.id.selectAll_button);
        selectAll_button.setOnClickListener(this);


        Button next_button = (Button) findViewById(R.id.button_next_flat_associations);
        next_button.setOnClickListener(this);


        Resources res = getResources();
        String[] associations_types_name = res.getStringArray(R.array.associations_types_array);
        associationTypesList = new ArrayList<AssociationType>();
        for (int i = 0; i < associations_types_name.length; i++) {
            associationTypesList.add(new AssociationType(associations_types_name[i], true));
        }


        // Lookup the recyclerview in activity layout
        RecyclerView rvAssociations = (RecyclerView) findViewById(R.id.rv_associations);

        // Set layout manager to position the items
        rvAssociations.setLayoutManager(new GridLayoutManager(this, 2));

        // Create adapter passing in the sample user data
        adapter = new AssociationsTypesAdapter(associationTypesList);

        // Attach the adapter to the recyclerview to populate items
        rvAssociations.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.selectAll_button) {
                    Button b = (Button)v;
                    if(b.getTag().equals("1")){
                        b.setBackgroundResource(R.drawable.check_circle_deselected);
                        b.setTag("0");
                        for (int i = 0; i <associationTypesList.size() ; i++) {
                            associationTypesList.set(i,new AssociationType(associationTypesList.get(i).getType_name(),false));
                        }
                    }else{
                        b.setBackgroundResource(R.drawable.check_circle);
                        b.setTag("1");
                        for (int i = 0; i <associationTypesList.size() ; i++) {
                            associationTypesList.set(i,new AssociationType(associationTypesList.get(i).getType_name(),true));
                        }
                    }
                    adapter.notifyDataSetChanged();

        } else if (v.getId() == R.id.button_next_flat_associations) {

                    //store selected org types
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    JSONArray jsonArray = new JSONArray();
                    String jsonObjName = "favourite_associations_types";
                    List<AssociationType> associationTypes_withSelection = adapter.getAssociationTypes();
                    AssociationType temp;
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        temp = associationTypes_withSelection.get(i);
                        if(temp.isSelected())jsonArray.put(temp.getType_name());
                    }
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(jsonObjName, jsonArray.toString());
                    System.out.println(jsonArray.toString());
                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put(jsonObjName, jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(jsonObj.toString());
                    editor.commit();

                    SelectionOrgTypesActivity.this.finish();
                    Intent intent = new Intent(getApplicationContext(), SelectionContactsActivity.class);
                    startActivity(intent);
        }

    }

}
