package com.columbasms.columbasms.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.AssociationListAdapter;
import com.columbasms.columbasms.adapter.AssociationProfileAdapter;
import com.columbasms.columbasms.adapter.MainAdapter;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo Brienza on 2/1/16.
 */
public class TopicProfileActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_topic)Toolbar toolbar;
    @Bind(R.id.rv_associations_list)RecyclerView rv_associations_list;
    @Bind(R.id.rv_campaigns_list)RecyclerView rv_campaigns_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_profile);

        ButterKnife.bind(this);

        String topic = getIntent().getStringExtra("topic_name");

        //TOOLBARSETUP
        toolbar.setTitle(topic);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicProfileActivity.this.finish();
            }
        });


        //SETUP ASSOCIATIONS LIST ADAPTER
        List<Association> associations_list = new ArrayList<>();
        associations_list.add(new Association("Lipu", 120, false));
        associations_list.add(new Association("WWF", 30, false));
        associations_list.add(new Association("ADDA", 1000, false));
        associations_list.add(new Association("Lav", 45000, false));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_associations_list.setLayoutManager(mLayoutManager);
        AssociationListAdapter mAdapter = new AssociationListAdapter(associations_list);
        rv_associations_list.setAdapter(mAdapter);

        //SETUP CAMPAIGNS LIST ADAPTER
        List<CharityCampaign> campaigns_list = new ArrayList<>();
        campaigns_list.add(new CharityCampaign("Lipu",topic,"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",null));
        campaigns_list.add(new CharityCampaign("WWF",topic,"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",null));
        campaigns_list.add(new CharityCampaign("ADDA",topic,"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",null));
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_campaigns_list.setLayoutManager(lm);
        MainAdapter ma = new MainAdapter(campaigns_list,getFragmentManager(),getResources(),getParent());
        rv_campaigns_list.setAdapter(ma);
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
