package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ext.adarsh.Bean.BeanApprovalsReviewPeople;
import com.ext.adarsh.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterReviewPeople extends BaseAdapter {

    Activity act;
    List<BeanApprovalsReviewPeople> arrayList;

    public AdapterReviewPeople(Activity act, List<BeanApprovalsReviewPeople> arrayList) {
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
        return Integer.parseInt(arrayList.get(i).approvalFromId);
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

        if (arrayList.get(i).approvalFromName.equalsIgnoreCase("Select People")) {
            holder.tvId.setText(arrayList.get(i).approvalFromId);
            holder.tvName.setText(arrayList.get(i).approvalFromName);
            holder.tvName.setTextColor(act.getResources().getColor(R.color.black));
        } else {
            holder.tvId.setText(arrayList.get(i).approvalFromId);
            holder.tvName.setText(arrayList.get(i).approvalFromName);
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


