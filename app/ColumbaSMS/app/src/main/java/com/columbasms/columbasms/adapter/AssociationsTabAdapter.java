package com.columbasms.columbasms.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matteo Brienza on 2/10/16.
 */
public class AssociationsTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {



    public static class AssociationsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.taba_background_layout)LinearLayout taba_background;
        @Bind(R.id.taba_ass_name)TextView taba_assName;
        @Bind(R.id.taba_description)TextView taba_description;
        @Bind(R.id.taba_follower)TextView taba_follower;
        @Bind(R.id.taba_profile_image)ImageView taba_profile_image;

        public AssociationsViewHolder(final View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }

        public static AssociationsViewHolder newInstance(View parent) {
            return new AssociationsViewHolder(parent);
        }
    }

    private List<Association> associationList;

    public AssociationsTabAdapter(List<Association> associationList){
        this.associationList = associationList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_association_tab, parent, false);
        return AssociationsViewHolder.newInstance(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final AssociationsViewHolder viewHolder = (AssociationsViewHolder)holder;
        final Association a = associationList.get(position);

        viewHolder.taba_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AssociationProfileActivity.class);
                i.putExtra("ass_id",a.getId());
                i.putExtra("ass_name",a.getOrganization_name());
                v.getContext().startActivity(i);
            }
        });

        viewHolder.taba_assName.setText(a.getOrganization_name());
        viewHolder.taba_description.setText(a.getDescription());


        //viewHolder.taba_follower.setText(a.getFollower());
        final ImageView p = viewHolder.taba_profile_image;
        Utils.downloadImage(a.getAvatar_normal(), p, false, false);


    }

    @Override
    public int getItemCount() {
        return associationList == null ? 0 : associationList.size();
    }
}
