package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 1/16/2018.
 */


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Activity.MessageCompose;
import com.ext.adarsh.Bean.ModelPostImageList;
import com.ext.adarsh.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMessagesAttachment extends RecyclerView.Adapter<AdapterMessagesAttachment.ViewHolder> {
    Activity activity;
    List<ModelPostImageList> list = new ArrayList<>();

    public AdapterMessagesAttachment(Activity activity, List<ModelPostImageList> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.messages_compose_attachment, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_file_name.setText(list.get(position).getImage_name());
        holder.iv_remove_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                MessageCompose.adapterMessagesAttachment.notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_remove_file;
        TextView tv_file_name;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_remove_file = (ImageView) itemView.findViewById(R.id.iv_remove_file);
            tv_file_name = (TextView) itemView.findViewById(R.id.tv_file_name);
        }
    }
}

