package com.ext.adarsh.Adapter;

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
import com.ext.adarsh.Activity.MessageCompose;
import com.ext.adarsh.Bean.BeanMessagePeopleList;
import com.ext.adarsh.Bean.ModelClass3;
import com.ext.adarsh.R;
import java.util.ArrayList;
import java.util.List;

public class Adapter1MessagesPeopleList3 extends RecyclerView.Adapter<Adapter1MessagesPeopleList3.ViewHolder> implements Filterable {

    List<BeanMessagePeopleList> list;
    List<BeanMessagePeopleList> array;
    Context context;
    View view1;
    ViewHolder viewHolder1;

    public Adapter1MessagesPeopleList3(Context context, List<BeanMessagePeopleList> list) {
        this.list = list;
        this.array = list;
        this.context = context;
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
        holder.chkSelected.setOnCheckedChangeListener(null);

        final BeanMessagePeopleList itemlist = list.get(position);

        holder.textView.setText(itemlist.fullName);
        holder.chkSelected.setChecked(list.get(position).isSelected());
        holder.chkSelected.setTag(list.get(position));

        MessageCompose.recyclerview_adapter3 = new Adapter2MessagesPeople3(context, MessageCompose.item_list3);
        MessageCompose.rv_onchangelistner3();

        holder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (list.get(position).isSelected() == false) {
                    ModelClass3 temp = new ModelClass3();
                    temp.setId(itemlist.peopleId);
                    temp.setName(itemlist.fullName);
                    temp.setEmail_id(itemlist.emailAddress);

                    MessageCompose.item_list3.add(temp);
                    MessageCompose.recyclerview_adapter = new Adapter2MessagesPeople3(context, MessageCompose.item_list3);
                    MessageCompose.rv_onchangelistner3();

                    holder.chkSelected.setChecked(true);
                    list.get(position).setSelected(true);

                } else {

                    for (int j = 0; j < MessageCompose.item_list3.size(); j++) {
                        if (itemlist.peopleId.equalsIgnoreCase(Adapter2MessagesPeople3.list.get(j).getId())) {
                            Adapter2MessagesPeople3.list.remove(j);
                        }
                    }

                    MessageCompose.rv_onchangelistner3();
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
                    ArrayList<BeanMessagePeopleList> temp = new ArrayList<>();
                    for (BeanMessagePeopleList category : array) {
                        if (category.fullName.toLowerCase().contains(s.toLowerCase())) {
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
                list = (ArrayList<BeanMessagePeopleList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


