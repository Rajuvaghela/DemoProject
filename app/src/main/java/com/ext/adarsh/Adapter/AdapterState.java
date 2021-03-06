package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 04-11-2017.
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ext.adarsh.Bean.BeanDepartmentList;
import com.ext.adarsh.Bean.BeanStateList;
import com.ext.adarsh.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterState extends BaseAdapter {

    Activity act;
    List<BeanStateList> arrayList;

    public AdapterState(Activity act, List<BeanStateList> arrayList) {
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
        return Integer.parseInt(arrayList.get(i).stateId);
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

        if (arrayList.get(i).stateName.equalsIgnoreCase("Select State")) {
            holder.tvId.setText(arrayList.get(i).stateId);
            holder.tvName.setText(arrayList.get(i).stateName);
            holder.tvName.setTextColor(act.getResources().getColor(R.color.black));
        } else {
            holder.tvId.setText(arrayList.get(i).stateId);
            holder.tvName.setText(arrayList.get(i).stateName);
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

