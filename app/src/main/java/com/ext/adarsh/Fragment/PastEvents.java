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

import com.ext.adarsh.Adapter.AdapterPastEvents;
import com.ext.adarsh.Adapter.AdapterUpcomingEvents;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ExT-Emp-001 on 11-07-2017.
 */
public class PastEvents extends Fragment {


    public static RecyclerView recylerupcomingevents;

    @BindView(R.id.createevent_float)
    FloatingActionButton createevent_float;

    Dialog dd;

    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.events_pastevents, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        activity = getActivity();

        recylerupcomingevents = (RecyclerView)view.findViewById(R.id.recylerupcomingevents);
        recylerupcomingevents.setHasFixedSize(true);
        LinearLayoutManager lnmanager = new LinearLayoutManager(activity);
        recylerupcomingevents.setLayoutManager(lnmanager);
        recylerupcomingevents.setItemAnimator(new DefaultItemAnimator());

        createevent_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showAlertDialog3();
            }
        });
    }

    /*private void showAlertDialog3() {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.event_add_information);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView tv1 = (TextView) dd.findViewById(R.id.tv1);
        TextView tv2 = (TextView) dd.findViewById(R.id.tv2);
        TextView tv3 = (TextView) dd.findViewById(R.id.tv3);
        TextView tv4 = (TextView) dd.findViewById(R.id.tv4);
        TextView tv5 = (TextView) dd.findViewById(R.id.tv5);
        TextView tv6 = (TextView) dd.findViewById(R.id.tv6);
        TextView tv7 = (TextView) dd.findViewById(R.id.tv7);
        TextView tv8 = (TextView) dd.findViewById(R.id.tv8);
        TextView tv9 = (TextView) dd.findViewById(R.id.tv9);
        TextView tv10 = (TextView) dd.findViewById(R.id.tv10);
        TextView tv11 = (TextView) dd.findViewById(R.id.tv11);
        TextView tv12 = (TextView) dd.findViewById(R.id.tv12);
        TextView tv14 = (TextView) dd.findViewById(R.id.tv14);

        EditText edit1 = (EditText) dd.findViewById(R.id.edit1);
        EditText edit2 = (EditText) dd.findViewById(R.id.edit2);
        EditText edit3 = (EditText) dd.findViewById(R.id.edit3);
        EditText edit4 = (EditText) dd.findViewById(R.id.edit4);
        EditText edit5 = (EditText) dd.findViewById(R.id.edit5);
        EditText edit6 = (EditText) dd.findViewById(R.id.edit6);
        EditText edit7 = (EditText) dd.findViewById(R.id.edit7);
        EditText edit8 = (EditText) dd.findViewById(R.id.edit8);
        EditText edit9 = (EditText) dd.findViewById(R.id.edit9);


        RadioButton radio1 = (RadioButton) dd.findViewById(R.id.radio1);
        RadioButton radio2 = (RadioButton) dd.findViewById(R.id.radio2);


        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv10.setTypeface(Utility.getTypeFace());
        tv11.setTypeface(Utility.getTypeFaceTab());
        tv12.setTypeface(Utility.getTypeFaceTab());
        tv14.setTypeface(Utility.getTypeFace());

        edit1.setTypeface(Utility.getTypeFace());
        edit2.setTypeface(Utility.getTypeFace());
        edit3.setTypeface(Utility.getTypeFace());
        edit4.setTypeface(Utility.getTypeFace());
        edit5.setTypeface(Utility.getTypeFace());
        edit6.setTypeface(Utility.getTypeFace());
        edit7.setTypeface(Utility.getTypeFace());
        edit8.setTypeface(Utility.getTypeFace());
        edit9.setTypeface(Utility.getTypeFace());

        radio1.setTypeface(Utility.getTypeFace());
        radio2.setTypeface(Utility.getTypeFace());

        LinearLayout lnmainback = (LinearLayout)dd.findViewById(R.id.lnmainback);

        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
            }
        });

        tv12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
            }
        });

        dd.show();
    }*/
}

