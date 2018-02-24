package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 08-11-2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ext.adarsh.Activity.AddPollsActivity;
import com.ext.adarsh.Bean.ModelClass;

import com.ext.adarsh.Fragment.mypolls;
import com.ext.adarsh.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter2MyPolls extends RecyclerView.Adapter<Adapter2MyPolls.ViewHolder> {


    Context context;
    static List<ModelClass> list = new ArrayList<>();

    public Adapter2MyPolls(Context context, List<ModelClass> list) {
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

                AddPollsActivity.rv_onchangelistner();
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
