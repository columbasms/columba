package com.columbasms.columbasms.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.AssociationProfileAdapter;
import com.columbasms.columbasms.model.CharityCampaign;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo Brienza on 2/1/16.
 */
public class AssociationProfileActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_profile)Toolbar toolbar;
    @Bind(R.id.rv_association_profile)RecyclerView  rvAssociationProfile;

    int toolbar_size;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association_profile);
        List campaigns_list = new ArrayList<>();

        ButterKnife.bind(this);

        //TOP TOOLBAR SETUP
        toolbar.bringToFront();
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setVisibility(View.VISIBLE);
        //GET HEIGHT OF TOOLBAR TO FADE ANIMATION
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar_size = toolbar.getHeight();
            }

        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssociationProfileActivity.this.finish();
            }
        });


        //HERE GET CAMPAIGN FROM SERVER
        campaigns_list.add(new CharityCampaign("", "", "", null));
        campaigns_list.add(new CharityCampaign("", "", "", null));
        campaigns_list.add(new CharityCampaign("", "", "", null));
        campaigns_list.add(new CharityCampaign("", "", "", null));

        // Set layout manager to position the items
        final AssociationProfileAdapter associationProfileAdapter = new AssociationProfileAdapter(campaigns_list);
        rvAssociationProfile.setAdapter(associationProfileAdapter);
        rvAssociationProfile.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rvAssociationProfile.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override //TEMPORARY METHOD FOR TOOLBAR FADE-IN ANIMATION
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int card_size = associationProfileAdapter.getCardSize();
                scrollDy += dy;
                if(scrollDy> (card_size - toolbar_size)) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    toolbar.setTitle("Amici di Cabbia");
                }else{
                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                    toolbar.setTitle("");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                return true;
            case R.id.action_feedback:
                return true;
            case R.id.action_guide:
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}


