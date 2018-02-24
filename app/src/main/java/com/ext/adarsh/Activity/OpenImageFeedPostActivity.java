package com.ext.adarsh.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ext.adarsh.Adapter.AdapterOpenImageFeedPost;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;

/**
 * Created by ExT-Emp-008 on 09-02-2018.
 */

public class OpenImageFeedPostActivity extends AppCompatActivity implements View.OnClickListener {

    public static RecyclerView rv_news_feed;
    public static Activity activity;
    SwipeRefreshLayout swipe_refresh_layout;
    // List<BeanFeedNews> beanFeddPostImage = new ArrayList<>();;
    int position;
    public static AdapterOpenImageFeedPost madapter;
    LinearLayout drawericon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_image_feed_post_activity);
        activity = this;

        Bundle extra = getIntent().getExtras();
        position = extra.getInt("position");
        Log.e("position", "" + String.valueOf(position));

        // BeanFeedNews pos = AppConstant.beanFeedNew.get(position);
        //  beanFeddPostImage = pos.post_Image_Array;
        //   Log.e("size", "" + beanFeddPostImage.size());
        drawericon = (LinearLayout) findViewById(R.id.drawericon);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        rv_news_feed = (RecyclerView) findViewById(R.id.rv_news_feed);
        rv_news_feed.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_news_feed.setLayoutManager(mLayoutManager);
        rv_news_feed.setItemAnimator(new DefaultItemAnimator());
        madapter = new AdapterOpenImageFeedPost(activity, AppConstant.beanFeedNew.get(position).post_Image_Array, position, AppConstant.beanFeedNew);
        rv_news_feed.setAdapter(madapter);
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getActivityFeedList2();
            }
        });
        drawericon.setOnClickListener(this);
        //getActivityFeedList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawericon:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
