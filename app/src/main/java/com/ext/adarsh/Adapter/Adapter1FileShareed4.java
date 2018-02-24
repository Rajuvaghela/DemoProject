package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 02-11-2017.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Bean.BeanPeopleFileShare;
import com.ext.adarsh.Bean.ModelClass3;
import com.ext.adarsh.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter1FileShareed4 extends RecyclerView.Adapter<Adapter1FileShareed4.ViewHolder> implements Filterable {

    List<BeanPeopleFileShare> list;
    List<BeanPeopleFileShare> array;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;

    public Adapter1FileShareed4(Context context1, List<BeanPeopleFileShare> list) {
        this.list = list;
        this.array = list;
        context = context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        CheckBox chkSelected;
        LinearLayout ln_main;

        public ViewHolder(View v) {
            super(v);
            ln_main = (LinearLayout) v.findViewById(R.id.ln_main);
            textView = (TextView) v.findViewById(R.id.subject_textview);
            chkSelected = (CheckBox) v.findViewById(R.id.chkSelected);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view1 = LayoutInflater.from(context).inflate(R.layout.tag_item_list, parent, false);
        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BeanPeopleFileShare itemlist = list.get(position);

        holder.textView.setText(itemlist.peopleName);
        holder.chkSelected.setChecked(list.get(position).isSelected());
        holder.chkSelected.setTag(list.get(position));
        AdapterSubFileShared.recyclerview_adapter3 = new Adapter2FileShareed2(context, AdapterSubFileShared.item_list3);
        AdapterSubFileShared.rv_onchangelistner3();

        holder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (list.get(position).isSelected() == false) {
                    ModelClass3 temp = new ModelClass3();
                    temp.setId(itemlist.peopleId);
                    temp.setName(itemlist.peopleName);

                    AdapterSubFileShared.item_list3.add(temp);
                    AdapterSubFileShared.recyclerview_adapter3 = new Adapter2FileShareed2(context, AdapterSubFileShared.item_list3);
                    AdapterSubFileShared.rv_onchangelistner3();

                    holder.chkSelected.setChecked(true);
                    list.get(position).setSelected(true);

                } else {
                    for (int j = 0; j < AdapterSubFileShared.item_list3.size(); j++) {
                        if (itemlist.peopleId.equalsIgnoreCase(Adapter2FileShareed2.list.get(j).getId())) {
                            Adapter2FileShareed2.list.remove(j);
                        }
                    }
                    AdapterSubFileShared.rv_onchangelistner3();
                    holder.chkSelected.setChecked(false);
                    list.get(position).setSelected(false);
                }
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String s = String.valueOf(charSequence);

                if (s != null && s.length() > 0) {
                    ArrayList<BeanPeopleFileShare> temp = new ArrayList<>();
                    for (BeanPeopleFileShare category : array) {
                        if (category.peopleName.toLowerCase().contains(s.toLowerCase())) {
                            temp.add(category);
                        }
                    }
                    results.values = temp;
                    results.count = temp.size();
                } else {
                    results.values = array;
                    results.count = array.size();
                }
                return results;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<BeanPeopleFileShare>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

