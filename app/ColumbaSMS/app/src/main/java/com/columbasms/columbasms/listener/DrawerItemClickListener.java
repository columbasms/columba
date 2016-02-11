package com.columbasms.columbasms.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Federico on 09/02/16.
 */
public class DrawerItemClickListener extends AppCompatActivity implements ListView.OnItemClickListener{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Activity activity;

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }

    public void onAttach(Activity activity){
        this.activity = activity;
    }

    private void selectItem(int position) {

        switch (position) {
            case 0: {
                /*
                Intent intent = new Intent(activity, UserProfileActivity.class);
                startActivity(intent);
                break;
                */
            }

            /*
            case 1: {
                Intent intent = new Intent(MainActivity.this, UserProfil.class);
                startActivity(intent);
                break;
            }
            */

            default:
                break;
        }

        mDrawerLayout.closeDrawer(mDrawerList);

    }

}
