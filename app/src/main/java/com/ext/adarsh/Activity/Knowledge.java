package com.ext.adarsh.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Adapter.KnowledgePagerAdapter;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Knowledge extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {


    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.h1)
    TextView h1;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private KnowledgePagerAdapter mAdapter;

    @BindView(R.id.pager)
    ViewPager eventsviewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_knowledge, frameLayout);
        ButterKnife.bind(this);
        activity = this;

        setFont();

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        // tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mAdapter = new KnowledgePagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(eventsviewPager);
        eventsviewPager.setAdapter(mAdapter);
        eventsviewPager.setOffscreenPageLimit(4);
        tabLayout.setOnTabSelectedListener(this);
        Utility.changeTabsFont(tabLayout);


    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    public void setFont() {
        h1.setTypeface(Utility.getTypeFace());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        eventsviewPager.setCurrentItem(tab.getPosition());
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
