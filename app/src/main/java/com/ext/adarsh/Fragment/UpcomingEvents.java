package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ext.adarsh.Adapter.AdapterUpcomingEvents;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ExT-Emp-001 on 11-07-2017.
 */
public class UpcomingEvents extends Fragment {

    public static RecyclerView recylerupcomingevents;
    @BindView(R.id.createevent_float)
    FloatingActionButton createevent_float;
    Dialog dd;
    Activity activity;
    AdapterUpcomingEvents adapterUpcomingEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.events_upcomingevents, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();

        recylerupcomingevents = (RecyclerView) view.findViewById(R.id.recylerupcomingevents);
        recylerupcomingevents.setHasFixedSize(true);
        LinearLayoutManager lnmanager = new LinearLayoutManager(activity);
        recylerupcomingevents.setLayoutManager(lnmanager);
        recylerupcomingevents.setItemAnimator(new DefaultItemAnimator());

        createevent_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAlertDialog3();
            }
        });
    }


}
