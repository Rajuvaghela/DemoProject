package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 1/19/2018.
 */


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Activity.TakeActionActivity;
import com.ext.adarsh.Activity.other.OtherProfileActivity;
import com.ext.adarsh.Bean.BeanFeedNewsTagFriendMore;
import com.ext.adarsh.Bean.ModelPostImageList;
import com.ext.adarsh.Bean.ModelTagFriendName;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class AdapterTagPersonName extends RecyclerView.Adapter<AdapterTagPersonName.ViewHolder> {
    Activity activity;
    List<ModelTagFriendName> list = new ArrayList<>();
    List<BeanFeedNewsTagFriendMore> list2 = new ArrayList<>();
    Dialog tag_people_list;

    public AdapterTagPersonName(Activity activity, List<ModelTagFriendName> list, List<BeanFeedNewsTagFriendMore> list2) {
        super();
        this.activity = activity;
        this.list = list;
        this.list2 = list2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tag_friens_name, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_post_people_name.setText(list.get(position).getPeople_name());
        if (list.get(position).getPeople_index().equalsIgnoreCase("1")) {
        } else if (list.get(position).getPeople_index().equalsIgnoreCase("2")) {
            holder.tv_post_people_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("size", "" + list.size());
                    if (list.size() > position) {//please remove this condition
                        Intent intent = new Intent(activity, OtherProfileActivity.class);
                        intent.putExtra("peopleId", list.get(position).getPeople_id());
                        activity.startActivity(intent);
                    }
                }
            });
        } else if (list.get(position).getPeople_index().equalsIgnoreCase("3")) {
            holder.tv_post_people_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogListOfTagPeople();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.e("return_size", "" + list.size());
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_post_people_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_post_people_name = (TextView) itemView.findViewById(R.id.tv_post_people_name);
            //  tv_post_other_text = (TextView) itemView.findViewById(R.id.tv_post_other_text);
        }
    }

    private void DialogListOfTagPeople() {
        tag_people_list = new Dialog(activity);
        tag_people_list.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tag_people_list.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        tag_people_list.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tag_people_list.setContentView(R.layout.list_of_tag_friends);
        Window window = tag_people_list.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnmainback = (LinearLayout) tag_people_list.findViewById(R.id.lnmainback);

        RecyclerView rv_tag_friend_list = (RecyclerView) tag_people_list.findViewById(R.id.rv_tag_friend_list);
        rv_tag_friend_list.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true);
        rv_tag_friend_list.setLayoutManager(lnmanager2);
        rv_tag_friend_list.setItemAnimator(new DefaultItemAnimator());

        AdapterTagPeopleList adapterPollsOption = new AdapterTagPeopleList(activity, list2);
        rv_tag_friend_list.setAdapter(adapterPollsOption);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag_people_list.dismiss();
            }
        });

        tag_people_list.show();
    }
}


