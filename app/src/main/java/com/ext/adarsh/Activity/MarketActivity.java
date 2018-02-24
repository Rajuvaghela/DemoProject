package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.ext.adarsh.Adapter.MarketPageAdapter;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {


    @BindView(R.id.pager)
    ViewPager marketviewPager;


    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
/*
    @BindView(R.id.h1)
    TextView h1;*/

    MarketPageAdapter mAdapter;
    public static Activity activity;
    public static ProgressDialog pd;
    String backmanage = "", id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_market, frameLayout);
        ButterKnife.bind(this);
        activity = this;
        backmanage = getIntent().getStringExtra("key");
        id = getIntent().getStringExtra("id");


        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        // tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mAdapter = new MarketPageAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(marketviewPager);
        marketviewPager.setAdapter(mAdapter);
        if (id == null) {
            marketviewPager.setCurrentItem(0);
        } else if (id.equalsIgnoreCase("2")) {
            marketviewPager.setCurrentItem(2);
        } else {
            marketviewPager.setCurrentItem(0);
        }
        marketviewPager.setOffscreenPageLimit(4);
        tabLayout.setOnTabSelectedListener(this);

        Utility.changeTabsFont(tabLayout);


    }


    @Override
    public void onBackPressed() {
        if (backmanage == null) {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        } else if (backmanage.equalsIgnoreCase("profile")) {
            startActivity(new Intent(activity, ProfileActivity.class));
            finish();
        } else {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        marketviewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
