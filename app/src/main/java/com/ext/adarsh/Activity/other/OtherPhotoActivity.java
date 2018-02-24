package com.ext.adarsh.Activity.other;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Activity.BaseActivity;
import com.ext.adarsh.Activity.MainActivity;
import com.ext.adarsh.Activity.ProfileActivity;
import com.ext.adarsh.Adapter.PhotoPagerAdapter;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherPhotoActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    @BindView(R.id.lnmainback)
    LinearLayout lnmainback;

    @BindView(R.id.drawericon)
    LinearLayout drawericon;

    String backmanage = "", people_id = "", people_full_name = "";

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    OPhotoPagerAdapter mAdapter;

    @BindView(R.id.header_text)
    TextView header_text;


    @BindView(R.id.lnAdd)
    LinearLayout lnAdd;

    Dialog addPhoto;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_photo, frameLayout);
        ButterKnife.bind(this);
        activity = this;

        backmanage = getIntent().getStringExtra("key");
        people_id = getIntent().getStringExtra("people_id");
        people_full_name = getIntent().getStringExtra("people_full_name");
        header_text.setText(people_full_name);
        drawericon.setVisibility(View.GONE);
        lnmainback.setOnClickListener(this);
/*        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });*/
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mAdapter = new OPhotoPagerAdapter(getSupportFragmentManager(), people_id);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setOnTabSelectedListener(this);

        Utility.changeTabsFont(tabLayout);
        // tv1.setTypeface(Utility.getTypeFace());
        header_text.setTypeface(Utility.getTypeFace());

        lnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  addPhoto();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
/*        if (backmanage == null) {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        } else if (backmanage.equalsIgnoreCase("profile")) {
            startActivity(new Intent(activity, ProfileActivity.class));
            finish();
        } else {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }*/
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnmainback:
                finish();
                break;
        }
    }
}
