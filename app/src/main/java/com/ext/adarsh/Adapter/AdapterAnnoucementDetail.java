package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Bean.BeanAnnoucementDetail;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterAnnoucementDetail extends RecyclerView.Adapter<AdapterAnnoucementDetail.MyViewHolder> {

    private ArrayList<BeanAnnoucementDetail> annoucementList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_month)
        TextView txt_month;

        @BindView(R.id.txt_date)
        TextView txt_date;

        @BindView(R.id.txtx_title1)
        TextView txtx_title1;

        @BindView(R.id.txt_title2)
        TextView txt_title2;

        @BindView(R.id.txt_detail)
        TextView txt_detail;

        @BindView(R.id.ln_attchment)
        LinearLayout ln_attchment;

        @BindView(R.id.ln_main_attchement)
        LinearLayout ln_main_attchement;

        @BindView(R.id.lnimportant)
        LinearLayout lnimportant;

        @BindView(R.id.txt_important)
        TextView txt_important;

        @BindView(R.id.header_imaportant)
        TextView header_imaportant;

        @BindView(R.id.header_attachment)
        TextView header_attachment;

        @BindView(R.id.ln_line)
        LinearLayout ln_line;

        @BindView(R.id.rv_announcement_attachment)
        RecyclerView rv_announcement_attachment;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rv_announcement_attachment = (RecyclerView) view.findViewById(R.id.rv_announcement_attachment);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            rv_announcement_attachment.setLayoutManager(mLayoutManager);
            rv_announcement_attachment.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public AdapterAnnoucementDetail(ArrayList<BeanAnnoucementDetail> AnnoucementList, Activity activity) {
        this.annoucementList = AnnoucementList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_detail_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txt_date.setText(annoucementList.get(position).day);
        holder.txt_title2.setText(annoucementList.get(position).announcementTitle);
        holder.txt_detail.setText(annoucementList.get(position).announcementDetail);
        holder.txt_month.setText(annoucementList.get(position).month);
        holder.txtx_title1.setText(annoucementList.get(position).publishBy);
        holder.txt_important.setText(annoucementList.get(position).referenceToPostTitle);


        if (annoucementList.get(position).announcement_Attachments_Array.size() == 0) {
            holder.ln_main_attchement.setVisibility(View.GONE);
        }

        if (annoucementList.get(position).referenceToPostTitle.equals("")) {
            holder.lnimportant.setVisibility(View.GONE);
        }

        if (annoucementList.get(position).referenceToPost.equalsIgnoreCase("0") && annoucementList.get(position).announcement_Attachments_Array.size() == 0) {
            holder.ln_line.setVisibility(View.GONE);
        }

        holder.txt_date.setTypeface(Utility.getTypeFaceTab());
        holder.txtx_title1.setTypeface(Utility.getTypeFace());
        holder.txt_important.setTypeface(Utility.getTypeFace());
        holder.txt_title2.setTypeface(Utility.getTypeFace());
        holder.txt_month.setTypeface(Utility.getTypeFace());
        holder.txt_detail.setTypeface(Utility.getTypeFace());
        holder.header_imaportant.setTypeface(Utility.getTypeFace());
        holder.header_attachment.setTypeface(Utility.getTypeFace());


        AdapterAnnouncementAttachment adapter = new AdapterAnnouncementAttachment(activity, annoucementList.get(position).announcement_Attachments_Array);
        holder.rv_announcement_attachment.setAdapter(adapter);


/*        for (int i = 0; i < annoucementList.get(position).announcement_Attachments_Array.size(); i++) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.daynamiclinear_attachment, null);

            TextView txt_attchment_name = (TextView) addView.findViewById(R.id.txt_attchment_name);
            txt_attchment_name.setText(annoucementList.get(position).announcement_Attachments_Array.get(i).fileName);

            txt_attchment_name.setTypeface(Utility.getTypeFace());
            holder.ln_attchment.addView(addView);
        }*/
    }

    @Override
    public int getItemCount() {
        return annoucementList.size();
    }

}


