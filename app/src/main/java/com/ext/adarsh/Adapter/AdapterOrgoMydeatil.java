package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ext.adarsh.Activity.other.OtherProfileActivity;
import com.ext.adarsh.Bean.BeanOrgoMydetail;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterOrgoMydeatil extends RecyclerView.Adapter<AdapterOrgoMydeatil.ViewHolder> {


    Activity activity;
    ArrayList<BeanOrgoMydetail> beanOrgoMydetails = new ArrayList<>();
    Dialog dd;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv_name,tv_dept,tv_branch,tv_contactno,tv_email_id;
    String name,department,branch,contactno,emailid;


    public AdapterOrgoMydeatil(Activity context, ArrayList<BeanOrgoMydetail> beanOrgoMydetails) {
        this.activity = context;
        this.beanOrgoMydetails = beanOrgoMydetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_orgo_manager, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_name.setTypeface(Utility.getTypeFace());
        holder.tv_designatin.setTypeface(Utility.getTypeFace());
        holder.tv_place.setTypeface(Utility.getTypeFace());

        holder.tv_name.setText(beanOrgoMydetails.get(position).fullName);
        holder.tv_place.setText(beanOrgoMydetails.get(position).branchName);
        holder.tv_designatin.setText(beanOrgoMydetails.get(position).designationName);
        Log.e("my profile", "" + beanOrgoMydetails.get(position).profileImage);
        Glide.with(activity).load(beanOrgoMydetails.get(position).profileImage).into(holder.proimage);

        holder.proimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beanOrgoMydetails.get(position).peopleId != null || beanOrgoMydetails.get(position).peopleId.equalsIgnoreCase("")){
                    Intent intent = new Intent(activity, OtherProfileActivity.class);
                    intent.putExtra("peopleId", beanOrgoMydetails.get(position).peopleId);
                    activity.startActivity(intent);
                }
            }
        });



        holder.lnprofile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name =  beanOrgoMydetails.get(position).fullName;
                department =  beanOrgoMydetails.get(position).designationName;
                branch =  beanOrgoMydetails.get(position).branchName;
                contactno =  beanOrgoMydetails.get(position).mobileNo;
                emailid =  beanOrgoMydetails.get(position).emailAddress;

                showAlertDialog(name,department,branch,contactno,emailid);
            }
        });
    }

    private void showAlertDialog(String name,String department,String branch,String contactno,String emailid) {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.activity_orgchart_detail);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnback = (LinearLayout)dd.findViewById(R.id.lnback);

        tv1 = (TextView)dd.findViewById(R.id.tv1);
        tv2 = (TextView)dd.findViewById(R.id.tv2);
        tv3 = (TextView)dd.findViewById(R.id.tv3);
        tv4 = (TextView)dd.findViewById(R.id.tv4);
        tv5 = (TextView)dd.findViewById(R.id.tv5);
        tv6 = (TextView)dd.findViewById(R.id.tv6);

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());

        tv_name = (TextView)dd.findViewById(R.id.tv_name);
        tv_dept = (TextView)dd.findViewById(R.id.tv_dept);
        tv_branch = (TextView)dd.findViewById(R.id.tv_branch);
        tv_contactno = (TextView)dd.findViewById(R.id.tv_contact_no);
        tv_email_id = (TextView)dd.findViewById(R.id.tv_email_id);

        tv_name.setText(name);
        tv_dept.setText(department);
        tv_branch.setText(branch);
        tv_contactno.setText(contactno);
        tv_email_id.setText(emailid);

        tv_name.setTypeface(Utility.getTypeFace());
        tv_dept.setTypeface(Utility.getTypeFace());
        tv_branch.setTypeface(Utility.getTypeFace());
        tv_contactno.setTypeface(Utility.getTypeFace());
        tv_email_id.setTypeface(Utility.getTypeFace());



        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
            }
        });
        dd.show();

    }

    @Override
    public int getItemCount() {
        return beanOrgoMydetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView proimage;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_designatin)
        TextView tv_designatin;

        @BindView(R.id.tv_place)
        TextView tv_place;

        @BindView(R.id.lnprofile1)
        RelativeLayout lnprofile1;

        @BindView(R.id.proimg1)
        ImageView proimg1;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            proimage = (ImageView) itemView.findViewById(R.id.proimage);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

