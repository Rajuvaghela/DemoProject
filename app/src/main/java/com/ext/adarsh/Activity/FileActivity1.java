package com.ext.adarsh.Activity;

/**
 * Created by ExT-Emp-011 on 2/1/2018.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ext.adarsh.Adapter.AdapterMyContact;
import com.ext.adarsh.Adapter.AdapterPeopleNew;
import com.ext.adarsh.Adapter.AdapterPeoplefavourite;
import com.ext.adarsh.Adapter.FilePagerAdapter;
import com.ext.adarsh.Adapter.PeoplePagerAdapter;
import com.ext.adarsh.Bean.BeanMyContact;
import com.ext.adarsh.Bean.BeanPeopleNew;
import com.ext.adarsh.Bean.BeanPeoplefavourite;
import com.ext.adarsh.Fragment.MyContact;
import com.ext.adarsh.Fragment.PeopleFragment;
import com.ext.adarsh.Fragment.favourite;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class FileActivity1 extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.h1)
    TextView h1;

    FilePagerAdapter mAdapter;
    public static Activity activity;
    public static ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_file_home, frameLayout);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);
        h1.setTypeface(Utility.getTypeFace());

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mAdapter = new FilePagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setOnTabSelectedListener(this);
        Utility.changeTabsFont(tabLayout);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    public void onClick(View view) {
    }
}


