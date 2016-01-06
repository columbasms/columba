package com.columbasms.columbasms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Matteo Brienza on 1/3/16.
 */
public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{


    EditText full_name;
    Spinner countries;
    EditText city;
    EditText postal_code;
    TextView warningtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button create_button = (Button)findViewById(R.id.button_create_account);
        create_button.setOnClickListener(this);

        full_name = (EditText) findViewById(R.id.full_name);

        //POPULATE SPINNER WITH COUNTRY NAMES
        countries = (Spinner) findViewById(R.id.country_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_name_array, R.layout.item_countries_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.item_countries_spinner);
        // Apply the adapter to the spinner
        countries.setAdapter(adapter);

        city = (EditText) findViewById(R.id.city);
        postal_code = (EditText) findViewById(R.id.postal_code);
        warningtext = (TextView) findViewById(R.id.warningtext);
    }

    @Override
    public void onClick(View v) {
        if(checkField() == true) {
                CreateAccountActivity.this.finish();
                Intent intent = new Intent(getApplicationContext(), SelectionMexAmountActivity.class);
                startActivity(intent);
        }
    }

    private boolean checkField(){
        if( !full_name.getText().toString().equals("") && !city.getText().toString().equals("") && !postal_code.getText().toString().equals("") )
                return true;
        else{
            warningtext.setText("Please set all fields!");
            return false;
        }
    }
}
