package com.columbasms.columbasms.fragment;

/**
 * Created by Matteo Brienza on 1/29/16.
 */
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.columbasms.columbasms.listener.HidingScrollListener;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.TopicsAdapter;
import com.columbasms.columbasms.model.TopicsType;

import java.util.ArrayList;
import java.util.List;


public class TopicsFragment extends Fragment{

    private TopicsAdapter adapter;
    private List<TopicsType> topicsList;
    RecyclerView rvTopics;
    Toolbar tb;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Resources res = getResources();
        String[] colorArray = res.getStringArray(R.array.topics_color);
        String[] topics_name = res.getStringArray(R.array.topics_name);
        topicsList = new ArrayList<>();
        for (int i = 0; i < topics_name.length; i++) {
            topicsList.add(new TopicsType(topics_name[i], false));
        }

        // Set layout manager to position the items
        rvTopics.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvTopics.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        // Create adapter passing in the sample user data
        final float s = getResources().getDisplayMetrics().density;
        adapter = new TopicsAdapter(topicsList,colorArray,s);

        // Attach the adapter to the recyclerview to populate items
        rvTopics.setAdapter(adapter);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_topics, container, false);
        rvTopics = (RecyclerView)v.findViewById(R.id.rv_topics);
        tb = (Toolbar)getActivity().findViewById(R.id.toolbar_bottom);
        return v;
    }


    private void hideViews() {
        tb.animate().translationY(tb.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        tb.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

}