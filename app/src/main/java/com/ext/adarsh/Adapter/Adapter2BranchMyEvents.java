package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 11/11/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ext.adarsh.Activity.EventAddActivity;
import com.ext.adarsh.Bean.ModelClass2;
import com.ext.adarsh.Fragment.MyEvents;
import com.ext.adarsh.Fragment.polls;
import com.ext.adarsh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ExT-Emp-001 on 02-11-2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ext.adarsh.Bean.ModelClass;
import com.ext.adarsh.Bean.ModelClass2;
import com.ext.adarsh.Fragment.polls;
import com.ext.adarsh.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter2BranchMyEvents extends RecyclerView.Adapter<Adapter2BranchMyEvents.ViewHolder> {


    Context context;
    static List<ModelClass2> list = new ArrayList<>();

    public Adapter2BranchMyEvents(Context context, List<ModelClass2> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tag_filter_item_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.tv_item_name.setText(list.get(position).getName());
        holder.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                EventAddActivity.rv_onchangelistner2();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_close;
        TextView tv_item_name;


        public ViewHolder(View itemView) {
            super(itemView);
            iv_close = (ImageView) itemView.findViewById(R.id.iv_close);
            tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
        }

    }

}

