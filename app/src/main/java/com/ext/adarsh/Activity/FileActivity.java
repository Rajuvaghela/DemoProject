package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Adapter.FilePagerAdapter;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

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
    public static String file_move_or_not = "";
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_file_home, frameLayout);
        ButterKnife.bind(this);
        activity = this;
        Intent intent = getIntent();
        file_move_or_not = intent.getExtras().getString("file_move_or_not");
        id = intent.getExtras().getString("id");

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
        if (id == null) {
            viewPager.setCurrentItem(0);
        } else if (id.equalsIgnoreCase("2")) {
            viewPager.setCurrentItem(1);
        } else if (id.equalsIgnoreCase("3")) {
            viewPager.setCurrentItem(2);
        } else {
            viewPager.setCurrentItem(0);
        }
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
