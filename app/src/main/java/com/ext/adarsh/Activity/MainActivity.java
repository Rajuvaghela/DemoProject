package com.ext.adarsh.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Adapter.ActivityFeedPagerAdapter;
import com.ext.adarsh.Fragment.activty_feed;
import com.ext.adarsh.Fragment.activty_fried_request;
import com.ext.adarsh.Fragment.activty_message;
import com.ext.adarsh.Fragment.activty_notification;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;

public class MainActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.header_name)
    TextView header_name;

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    ActivityFeedPagerAdapter mAdapter;

    activty_feed tab1;
    activty_fried_request tab2;
    activty_message tab3;
    activty_notification tab4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        ButterKnife.bind(this);
        activity = this;

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        header_name.setTypeface(Utility.getTypeFace());

    }

    private void setupViewPager(ViewPager viewPager)
    {
        mAdapter = new ActivityFeedPagerAdapter(getSupportFragmentManager());
        tab1 = new activty_feed();
        tab2 = new activty_fried_request();
        tab3 = new activty_message();
        tab4 = new activty_notification();

        mAdapter.addFragment(tab1,"CALLS");
        mAdapter.addFragment(tab3,"CHAT");
        mAdapter.addFragment(tab4,"CONTACTS");
        mAdapter.addFragment(tab2,"CONTACTS");

        viewPager.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitDialoge();
        }
    }

    private void exitDialoge() {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to exit ?")
                .setTitleText("Exit")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        finish();
                        /// android.os.Process.killProcess(android.os.Process.myPid());
                        //  finish();
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }



}
