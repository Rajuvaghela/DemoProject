package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 11/10/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Bean.BeanFeedNewsTagFriendMore;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class AdapterTagPeopleList extends RecyclerView.Adapter<AdapterTagPeopleList.ViewHolder> {

    Activity activity;
    List<BeanFeedNewsTagFriendMore> list = new ArrayList<>();
    ProgressDialog pd;

    public AdapterTagPeopleList(Activity activity, List<BeanFeedNewsTagFriendMore> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_groups_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
        holder.tv_group_name.setText(list.get(position).personsName);
        holder.ll_edit_groups.setVisibility(View.GONE);
        holder.ll_delete_groups.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_group_name;
        LinearLayout ll_edit_groups, ll_delete_groups;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_edit_groups = (LinearLayout) itemView.findViewById(R.id.ll_edit_groups);
            ll_delete_groups = (LinearLayout) itemView.findViewById(R.id.ll_delete_groups);
            tv_group_name = (TextView) itemView.findViewById(R.id.tv_group_name);
        }
    }
}

