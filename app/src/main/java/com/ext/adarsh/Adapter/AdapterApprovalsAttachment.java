package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.ext.adarsh.Bean.BeanApprovalAttachments;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class AdapterApprovalsAttachment extends RecyclerView.Adapter<AdapterApprovalsAttachment.ViewHolder> {
    Activity activity;
    Dialog openImage;
    List<BeanApprovalAttachments> list = new ArrayList<>();

    public AdapterApprovalsAttachment(Activity activity, List<BeanApprovalAttachments> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_approvals_attachment, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tv_approvals_file_name.setTypeface(Utility.getTypeFace());
        final String url = list.get(position).filePath;
        holder.tv_approvals_file_name.setText(url.substring(url.lastIndexOf('/') + 1));
        holder.ll_approvals_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage(list.get(position).filePath, url.substring(url.lastIndexOf('/') + 1));
            }
        });
    }

    private void openImage(String file_path, String file_name) {
        openImage = new Dialog(activity);
        openImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openImage.getWindow().setWindowAnimations(R.style.DialogAnimation);
        openImage.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        openImage.setContentView(R.layout.popup_show_image);

        Window window = openImage.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        ImageView iv_attachement_image = (ImageView) openImage.findViewById(R.id.iv_attachement_image);
        TextView tv_header = (TextView) openImage.findViewById(R.id.tv_header);
        tv_header.setTypeface(Utility.getTypeFaceTab());
        tv_header.setText(file_name);
        Glide.with(activity).load(file_path).into(iv_attachement_image);
        LinearLayout lnback = (LinearLayout) openImage.findViewById(R.id.lnback);
        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage.dismiss();
            }
        });


        openImage.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_approvals_file_name;
        LinearLayout ll_approvals_attachment;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_approvals_attachment = (LinearLayout) itemView.findViewById(R.id.ll_approvals_attachment);
            tv_approvals_file_name = (TextView) itemView.findViewById(R.id.tv_approvals_file_name);
        }
    }
}

