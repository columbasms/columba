package com.columbasms.columbasms;

/**
 * Created by Matteo Brienza on 23/12/15.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SelectionMexAmountActivity extends AppCompatActivity implements View.OnClickListener {

    EditText message_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_message_amount);
        message_amount = (EditText)findViewById(R.id.message_amount);
        Button next_button = (Button)findViewById(R.id.button_next);
        next_button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        String ma = message_amount.getText().toString();
        if(!ma.equals("")) {
            SharedPreferences account_information = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor_account_information = account_information.edit();
            editor_account_information.putString("messageAmount", ma);
            editor_account_information.commit();
            SelectionMexAmountActivity.this.finish();
            Intent intent = new Intent(this, SelectionOrgTypesActivity.class);
            startActivity(intent);
        }
    }
}
