package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.ext.adarsh.Bean.BeanMarket;
import com.ext.adarsh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ExT-Emp-001 on 07-02-2018.
 */
public class SellingImageAdapter extends PagerAdapter {
    private ArrayList<String> IMAGES;
    private LayoutInflater layoutInflater;
    Activity activity;


    public SellingImageAdapter(Activity activity, ArrayList<String> IMAGES) {
        this.activity = activity;
        this.IMAGES=IMAGES;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      //  LayoutInflater inflater = (activity).getLayoutInflater();
        View itemView = layoutInflater.inflate(R.layout.imageview_pager, container, false);
        // assert container != null;
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.market_image);
        Glide.with(activity).load(IMAGES.get(position)).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}