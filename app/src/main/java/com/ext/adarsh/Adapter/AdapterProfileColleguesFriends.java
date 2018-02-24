package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 04-11-2017.
 */


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ext.adarsh.Activity.other.OtherProfileActivity;
import com.ext.adarsh.Bean.BeanProfileColleguesFriends;
import com.ext.adarsh.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterProfileColleguesFriends extends RecyclerView.Adapter<AdapterProfileColleguesFriends.ViewHolder> {


    Activity activity;
    List<BeanProfileColleguesFriends> list;

    public AdapterProfileColleguesFriends(List<BeanProfileColleguesFriends> list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_profile_collegues_friends, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e("name", "" + list.get(position).fullName);
        holder.tv_collegues_friend_name.setText(list.get(position).fullName);
        Glide.with(activity).load(list.get(position).profileImage).into(holder.iv_collegues_friend_image);
        holder.ln_open_collegues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OtherProfileActivity.class);
                intent.putExtra("peopleId", list.get(position).peopleId);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_collegues_friend_image;
        TextView tv_collegues_friend_name;
        LinearLayout ln_open_collegues;

        public ViewHolder(View itemView) {
            super(itemView);
            ln_open_collegues = (LinearLayout) itemView.findViewById(R.id.ln_open_collegues);
            iv_collegues_friend_image = (ImageView) itemView.findViewById(R.id.iv_collegues_friend_image);
            tv_collegues_friend_name = (TextView) itemView.findViewById(R.id.tv_collegues_friend_name);
        }

    }

}


