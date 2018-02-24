package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 1/19/2018.
 */


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Activity.other.OtherProfileActivity;
import com.ext.adarsh.Bean.BeanFeedNewsTagFriendMore;
import com.ext.adarsh.Bean.ModelProfileTimelinePostWithOther;
import com.ext.adarsh.Bean.ModelTagFriendName;
import com.ext.adarsh.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterOthersTimelinePostHeader extends RecyclerView.Adapter<AdapterOthersTimelinePostHeader.ViewHolder> {
    Activity activity;
    List<ModelProfileTimelinePostWithOther> list = new ArrayList<>();
    Dialog tag_people_list;

    public AdapterOthersTimelinePostHeader(Activity activity, List<ModelProfileTimelinePostWithOther> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tag_friens_name, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (list.get(position).getIndex().equalsIgnoreCase("1")) {
            holder.tv_post_people_name.setText(list.get(position).getPeopleName() + " -> ");
            //  holder.tv_post_people_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_launcher, 0);
        } else {
            holder.tv_post_people_name.setText(list.get(position).getPeopleName());
        }
    }

    @Override
    public int getItemCount() {
        Log.e("return_size", "" + list.size());
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_post_people_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_post_people_name = (TextView) itemView.findViewById(R.id.tv_post_people_name);
            //  tv_post_other_text = (TextView) itemView.findViewById(R.id.tv_post_other_text);
        }
    }
}


