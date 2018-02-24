package com.ext.adarsh.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ext.adarsh.Bean.BeanGroup;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdGroupSp extends BaseAdapter {

    ArrayList<BeanGroup> area = new ArrayList<>();

    public AdGroupSp(ArrayList<BeanGroup> monthses) {
        this.area = monthses;
    }

    @Override
    public int getCount() {
        return area.size();
    }

    @Override
    public Object getItem(int position) {
        return area.get(position).categoryName;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(area.get(position).categoryId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_item, parent, false);
            holder = new MyHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.tv.setTextColor(Color.parseColor("#7F801F"));
        } else {
            holder.tv.setTextColor(parent.getContext().getResources().getColor(R.color.appcolor));
        }

        holder.tv.setText((String) getItem(position));
        holder.tv.setTypeface(Utility.getTypeFace());

        return convertView;
    }

    class MyHolder {
        @BindView(R.id.sp_tv)
        TextView tv;

        public MyHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
