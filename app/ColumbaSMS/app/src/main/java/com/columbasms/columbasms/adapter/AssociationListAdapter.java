package com.columbasms.columbasms.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo Brienza on 2/2/16.
 */
public class AssociationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Association> mItemList;

    public AssociationListAdapter(List<Association> itemList) {
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_association, parent, false);
        return AssociationViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AssociationViewHolder holder = (AssociationViewHolder) viewHolder;

        Association a = mItemList.get(position);

        TextView an = holder.name;
        an.setText(a.getOrganization_name());


        //TextView f = holder.follower;
        //f.setText(Integer.toString(a.getFollower()));

       // ImageView favourite = holder.favourite;


    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    public static class AssociationViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.name)TextView name;
        @Bind(R.id.follower)TextView follower;
        //@Bind(R.id.favourite)ImageView favourite;

        public AssociationViewHolder(final View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }

        public static AssociationViewHolder newInstance(View parent) {
            return new AssociationViewHolder(parent);
        }
    }
}
