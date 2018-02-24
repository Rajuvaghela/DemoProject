package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ext.adarsh.Adapter.AdapterAnnoucement;
import com.ext.adarsh.R;

import butterknife.ButterKnife;

public class announcement extends Fragment {

    AdapterAnnoucement mAdapter;

   public static RecyclerView recylerannouncement;

    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.announcement, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        activity = getActivity();

        recylerannouncement = (RecyclerView)view.findViewById(R.id.recylerannouncement);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recylerannouncement.setLayoutManager(mLayoutManager);
        recylerannouncement.setItemAnimator(new DefaultItemAnimator());
        recylerannouncement.setAdapter(mAdapter);
    }


}

