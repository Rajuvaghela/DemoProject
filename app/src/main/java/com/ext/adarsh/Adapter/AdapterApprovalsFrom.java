package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ext.adarsh.Bean.BeanApprovalsFrom;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import java.util.ArrayList;
import java.util.List;


public class AdapterApprovalsFrom extends RecyclerView.Adapter<AdapterApprovalsFrom.ViewHolder> {


    Activity activity;
    List<BeanApprovalsFrom> list = new ArrayList<>();

    public AdapterApprovalsFrom(ArrayList<BeanApprovalsFrom> list, Activity activity) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_approvals_from_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tv_approval_from_name.setText(list.get(position).approvalFromName);
        holder.tv_designation.setText(list.get(position).designation);

        holder.tv_approval_from_name.setTypeface(Utility.getTypeFace());
        holder.tv_designation.setTypeface(Utility.getTypeFace());

        Glide.with(activity).load(list.get(position).profileImage).error(R.drawable.usericon).into(holder.iv_aproved_req_img);

        if (list.get(position).statusFlag.equals("A")) {
            holder.ll_approved.setVisibility(View.VISIBLE);
            holder.ll_pending.setVisibility(View.GONE);

        } else {
            holder.ll_approved.setVisibility(View.GONE);
            holder.ll_pending.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_aproved_req_img;
        TextView tv_approval_from_name, tv_designation;
        LinearLayout ll_pending, ll_approved;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_approval_from_name = (TextView) itemView.findViewById(R.id.tv_approval_from_name);
            tv_designation = (TextView) itemView.findViewById(R.id.tv_designation);
            ll_pending = (LinearLayout) itemView.findViewById(R.id.ll_pending);
            ll_approved = (LinearLayout) itemView.findViewById(R.id.ll_approved);
            iv_aproved_req_img = (ImageView) itemView.findViewById(R.id.iv_aproved_req_img);

        }

    }

}

