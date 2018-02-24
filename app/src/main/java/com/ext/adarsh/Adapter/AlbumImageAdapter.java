package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ext.adarsh.Activity.LoadFullPhotoActivity;
import com.ext.adarsh.Bean.BeanAlbumPhoto;
import com.ext.adarsh.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumImageAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<BeanAlbumPhoto> beanPhotos = new ArrayList<>();

    // Constructor
    public AlbumImageAdapter(Activity activity, ArrayList<BeanAlbumPhoto> beanPhotos) {
        this.activity = activity;
        this.beanPhotos = beanPhotos;
    }

    public int getCount() {
        return beanPhotos.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ViewHolder holder;

        view = LayoutInflater.from(activity).inflate(R.layout.list_photo, viewGroup, false);
        holder = new ViewHolder(view);
        view.setTag(holder);

        Glide.with(activity).load(beanPhotos.get(position).filePath).into(holder.list_img);
        holder.list_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LoadFullPhotoActivity.class);
                intent.putExtra("AlbumDetailId", beanPhotos.get(position).albumDetailId);
                intent.putExtra("postid", beanPhotos.get(position).postId);
                activity.startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder {

        @BindView(R.id.list_img)
        ImageView list_img;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
