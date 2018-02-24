package com.ext.adarsh.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ext.adarsh.Bean.ModelClass3;
import com.ext.adarsh.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ExT-Emp-011 on 1/6/2018.
 */
public class Adapter2FeedPostPeopleList extends RecyclerView.Adapter<Adapter2FeedPostPeopleList.ViewHolder> {
    Context context;
    static List<ModelClass3> list = new ArrayList<>();
    public Adapter2FeedPostPeopleList(Context context, List<ModelClass3> list) {
        super();
        this.context = context;
        this.list = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tag_filter_item_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_item_name.setText(list.get(position).getName());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_close;
        TextView tv_item_name;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_close = (ImageView) itemView.findViewById(R.id.iv_close);
            tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
        }

    }

}

