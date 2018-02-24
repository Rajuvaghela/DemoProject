package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 1/1/2018.
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ext.adarsh.Bean.BeanDepartmentList;
import com.ext.adarsh.Bean.BeanPeopleSelectByDepartmentId;
import com.ext.adarsh.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterApprovalsPeopleByDep extends BaseAdapter {

    Activity act;
    List<BeanPeopleSelectByDepartmentId> arrayList;

    public AdapterApprovalsPeopleByDep(Activity act, List<BeanPeopleSelectByDepartmentId> arrayList) {
        this.act = act;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(arrayList.get(i).peopleId);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(act).inflate(R.layout.sp_area_item_design, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (arrayList.get(i).peopleName.equalsIgnoreCase("Select People")) {
            holder.tvId.setText(arrayList.get(i).peopleId);
            holder.tvName.setText(arrayList.get(i).peopleName);
            holder.tvName.setTextColor(act.getResources().getColor(R.color.black));
        } else {
            holder.tvId.setText(arrayList.get(i).peopleId);
            holder.tvName.setText(arrayList.get(i).peopleName);
            holder.tvName.setTextColor(act.getResources().getColor(R.color.black));
        }

        return view;
    }

    public class ViewHolder {

        @BindView(R.id.sp_area_id)
        TextView tvId;
        @BindView(R.id.sp_area_name)
        TextView tvName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}

/*
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ext.adarsh.Bean.BeanApprovalsApprovalPeople;
import com.ext.adarsh.Bean.BeanPeopleSelectByDepartmentId;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;




public class AdapterApprovalsPeopleByDep extends BaseAdapter {

    Activity act;
    List<BeanPeopleSelectByDepartmentId> arrayList;

    public AdapterApprovalsPeopleByDep(Activity act, List<BeanPeopleSelectByDepartmentId> arrayList) {
        this.act = act;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(arrayList.get(i).peopleId);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(act).inflate(R.layout.sp_area_item_design, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (arrayList.get(i).peopleName.equalsIgnoreCase("Select People")) {
            holder.tvId.setText(arrayList.get(i).peopleId);
            holder.tvName.setTypeface(Utility.getTypeFace());
            holder.tvName.setText(arrayList.get(i).peopleName);
            holder.tvName.setTextColor(act.getResources().getColor(R.color.black));
        } else {
        holder.tvId.setText(arrayList.get(i).peopleId);
        holder.tvName.setTypeface(Utility.getTypeFace());
        holder.tvName.setText(arrayList.get(i).peopleName);
        holder.tvName.setTextColor(act.getResources().getColor(R.color.black));
        // }
        return view;
    }

    public class ViewHolder {

        @BindView(R.id.sp_area_id)
        TextView tvId;
        @BindView(R.id.sp_area_name)
        TextView tvName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}*/
/*


public class AdapterApprovalsPeopleByDep {
}*/
