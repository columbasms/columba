package com.columbasms.columbasms;

/**
 * Created by Matteo Brienza on 25/12/15.
 */

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.columbasms.columbasms.adapter.AssociationsTypesAdapter;

import java.util.Arrays;
import java.util.List;

public class SelectionOrgTypesActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_organization_types);

        Button selectAll_button = (Button)findViewById(R.id.selectAll_button);
        selectAll_button.setOnClickListener(this);


        Button next_button = (Button)findViewById(R.id.button_next_flat_associations);
        next_button.setOnClickListener(this);


        Resources res = getResources();
        String[] associations_types = res.getStringArray(R.array.associations_types_array);


        List<String> associations_types_list = Arrays.asList(associations_types);

        // Lookup the recyclerview in activity layout
        RecyclerView rvAssociations = (RecyclerView) findViewById(R.id.rv_associations);

        // Set layout manager to position the items
        rvAssociations.setLayoutManager(new GridLayoutManager(this,2));


        // Create adapter passing in the sample user data
        AssociationsTypesAdapter adapter = new AssociationsTypesAdapter(associations_types_list);

        // Attach the adapter to the recyclerview to populate items
        rvAssociations.setAdapter(adapter);



    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.selectAll_button){
            ((Button)v).setBackgroundResource(R.drawable.check_circle_deselected);
        }else if(v.getId()==R.id.button_next_flat_associations){
            SelectionOrgTypesActivity.this.finish();
            Intent intent = new Intent(getApplicationContext(), SelectionContactsActivity.class);
            startActivity(intent);
        }

    }
}
