package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ext.adarsh.Activity.Annoucement;
import com.ext.adarsh.Activity.FileActivity;
import com.ext.adarsh.Activity.MessageDetail;
import com.ext.adarsh.Bean.BeanNotifiactionlist;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.MyViewHolder> {
    String peopleId;
    private ArrayList<BeanNotifiactionlist> notifiactionList= new ArrayList<>();;
    Activity activity;
    Dialog dd;
    ProgressDialog pd;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txttime, txtdiscription;
        private ImageView imageview;

        @BindView(R.id.lnmain)
        LinearLayout lnmain;

      /*  @BindView(R.id.contactmenu)
        LinearLayout contactmenu;*/

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            txtdiscription = (TextView) view.findViewById(R.id.txtdiscription);
            txttime = (TextView) view.findViewById(R.id.txttime);
            imageview = (ImageView) view.findViewById(R.id.image);
        }
    }

    public AdapterNotification(ArrayList<BeanNotifiactionlist> notifiactionList, Activity activity) {
        this.notifiactionList = notifiactionList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
         BeanNotifiactionlist notification = notifiactionList.get(position);

        holder.txttime.setTypeface(Utility.getTypeFaceTab());
        holder.txtdiscription.setTypeface(Utility.getTypeFace());

        holder.txttime.setText(notification.PostedTime);
        holder.txtdiscription.setText(notification.NotificationMessage);
        Glide.with(activity).load(notifiactionList.get(position).ProfileImage).into(holder.imageview);

      /* holder.lnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeanNotifiactionlist notification = notifiactionList.get(position);
                String moduleflag = notification.ModuleFlag;
                String link = notification.Link;
                String linkid1 = link.substring(link.lastIndexOf("=") + 1);
                Log.e("linkid1",linkid1);
                if(moduleflag.equalsIgnoreCase("N")){
                    Intent intent = new Intent(activity, Annoucement.class);
                    intent.putExtra("N",linkid1);
                    intent.putExtra("key",1);
                    activity.startActivity(intent);
                }else if(moduleflag.equalsIgnoreCase("T")){
                    Intent intent = new Intent(activity, FileActivity.class);
                    intent.putExtra("N",linkid1);
                    activity.startActivity(intent);
                }else if(moduleflag.equalsIgnoreCase("F")){
                    Intent intent = new Intent(linkid1);
                    activity.startActivity(intent);
                }else if(moduleflag.equalsIgnoreCase("P")){
                    Intent intent = new Intent(linkid1);
                    activity.startActivity(intent);
                }else if(moduleflag.equalsIgnoreCase("M")){
                    Intent intent = new Intent(activity,MessageDetail.class);
                    intent.putExtra("M",linkid1);
                    intent.putExtra("key",1);
                    activity.startActivity(intent);
                }else if(moduleflag.equalsIgnoreCase("E")){
                    Intent intent = new Intent(linkid1);
                    activity.startActivity(intent);
                } else if(moduleflag.equalsIgnoreCase("A")){
                    Intent intent = new Intent(linkid1);
                    activity.startActivity(intent);
                }
            }
        });*/
       // holder.contactmenu.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return notifiactionList.size();
    }
}