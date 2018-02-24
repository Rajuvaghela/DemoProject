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
import com.ext.adarsh.Bean.ModelPostImageList;
import com.ext.adarsh.R;
import java.util.ArrayList;
import java.util.List;

public class AdapterFeedPostImage extends RecyclerView.Adapter<AdapterFeedPostImage.ViewHolder> {
    Activity activity;
     List<ModelPostImageList> list = new ArrayList<>();

    public AdapterFeedPostImage(Activity activity, List<ModelPostImageList> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_image_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.iv_post_image.setImageBitmap(list.get(position).getBitmap());
        holder.ll_remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_post_image;
        LinearLayout ll_remove_image;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_post_image = (ImageView) itemView.findViewById(R.id.iv_post_image);
            ll_remove_image = (LinearLayout) itemView.findViewById(R.id.ll_remove_image);
        }
    }
}

