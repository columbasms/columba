package com.columbasms.columbasms;

/**
 * Created by Matteo Brienza on 23/12/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SelectionMexAmountActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_message_amount);
        Button next_button = (Button)findViewById(R.id.button_next);
        next_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SelectionMexAmountActivity.this.finish();
        Intent intent = new Intent(this, SelectionOrgTypesActivity.class);
        startActivity(intent);
    }
}
