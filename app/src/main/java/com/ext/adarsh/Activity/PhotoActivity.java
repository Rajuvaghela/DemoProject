package com.ext.adarsh.Activity;

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

import com.ext.adarsh.Adapter.PhotoPagerAdapter;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;


    String backmanage;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


    PhotoPagerAdapter mAdapter;

    @BindView(R.id.header_text)
    TextView header_text;


    @BindView(R.id.lnAdd)
    LinearLayout lnAdd;

    Dialog addPhoto;

    LinearLayout lnmainback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_photo, frameLayout);
        ButterKnife.bind(this);

        backmanage = getIntent().getStringExtra("key");
        header_text.setText(Utility.getFullNamePreference());
        lnmainback = (LinearLayout) findViewById(R.id.lnmainback);
        lnmainback.setVisibility(View.GONE);
        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mAdapter = new PhotoPagerAdapter(getSupportFragmentManager());
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

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      /*  if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
        }*/
    }

}
