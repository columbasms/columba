package com.columbasms.columbasms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.activity.AssociationProfileActivity;
import com.columbasms.columbasms.activity.TopicProfileActivity;
import com.columbasms.columbasms.activity.UserAssociationsActivity;
import com.columbasms.columbasms.fragment.AskContactsInputFragment;
import com.columbasms.columbasms.fragment.ChooseContactsFragment;
import com.columbasms.columbasms.model.Association;
import com.columbasms.columbasms.model.CharityCampaign;
import com.columbasms.columbasms.model.Topic;
import com.columbasms.columbasms.utils.Utils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matteo on 18/02/2016.
 */
public class HorizontalAssociationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Association> association_list;
    private Activity activity;

    public class PhotoListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.association_photo)ImageView association_photo;
        public PhotoListViewHolder(View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }
    }

    public HorizontalAssociationAdapter(List<Association>a, Activity activity){
        association_list = a;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followed_association_photo_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        final PhotoListViewHolder holder = (PhotoListViewHolder) viewHolder;
        final Association a = association_list.get(position);

        final ImageView im = holder.association_photo;
        im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(activity,AssociationProfileActivity.class);
                        i.putExtra("ass_id",a.getId());
                        i.putExtra("ass_name",a.getOrganization_name());
                        activity.startActivity(i);
                    }
        });

        Transformation t = new RoundedTransformationBuilder()
                .cornerRadiusDp(50)
                .oval(false)
                .borderWidthDp(1)
                .borderColor(Color.parseColor("#ffffff"))
                .build();
        RequestCreator request = Picasso.with(im.getContext()).load(a.getAvatar_normal());
        request.transform(t)
                .placeholder(R.drawable.error_thumbnail_image);
        request.networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .into(im, new Callback() {

                    /*
                    Picasso will keep looking for it offline in cache and fail,
                    the following code looks at the local cache, if not found offline,
                    it goes online and replenishes the cache
                    */

                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        RequestCreator request2 = Picasso.with(im.getContext()).load(a.getAvatar_normal());
                        request2.placeholder(R.drawable.error_thumbnail_image);
                        request2.fit().into(im, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                            }
                        });

                    }
                });
    }

    @Override
    public int getItemCount() {
        return association_list == null ? 0 : association_list.size();
    }


}
