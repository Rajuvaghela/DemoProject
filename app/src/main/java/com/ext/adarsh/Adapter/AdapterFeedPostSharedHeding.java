package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 11/10/2017.
 */
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ext.adarsh.R;
import java.util.ArrayList;
import java.util.List;



public class AdapterFeedPostSharedHeding extends RecyclerView.Adapter<AdapterFeedPostSharedHeding.ViewHolder> {
    Activity activity;
    List<ModelFeedPostShared> list = new ArrayList<>();

    public AdapterFeedPostSharedHeding( Activity activity,List<ModelFeedPostShared> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.feed_post_heding_shared_people, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_post_people_name.setText(list.get(position).getPeopleName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_post_people_name;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_post_people_name = (TextView) itemView.findViewById(R.id.tv_post_people_name);
        }
    }
}

