package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 03-12-2017.
 */

import android.app.Activity;
import android.app.Dialog;
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
import com.ext.adarsh.Bean.BeanApprovalsActionLog;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.List;

public class AdapterApprovalsdetailsLogs extends RecyclerView.Adapter<AdapterApprovalsdetailsLogs.ViewHolder> {
    Activity activity;
    Dialog openImage;
    List<BeanApprovalsActionLog> list = new ArrayList<>();

    public AdapterApprovalsdetailsLogs(Activity activity, ArrayList<BeanApprovalsActionLog> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_approvals_logs, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

/*        tv_note_description,tv_note_by
        tv_log_request_sent_to,tv_log_title
        tv_approvalsname*/

        holder.tv_approvalsname.setTypeface(Utility.getTypeFaceTab());
        holder.tv_event_date.setTypeface(Utility.getTypeFace());
        holder.tv_log_request_sent_to.setTypeface(Utility.getTypeFaceTab());
        holder.tv_note_by.setTypeface(Utility.getTypeFaceTab());
        holder.tv_note_description.setTypeface(Utility.getTypeFace());
        holder.tv_log_title.setTypeface(Utility.getTypeFace());

        if (list.get(position).description == null || list.get(position).description.equalsIgnoreCase("")){
            holder.lnyellowback.setVisibility(View.GONE);
        }


        holder.tv_approvalsname.setText(list.get(position).fullName);
        String[] separated = list.get(position).date.split(" ");
        holder.tv_event_date.setText(separated[0] + " " +list.get(position).time);
        holder.tv_log_request_sent_to.setText(list.get(position).requestSendTo);
        holder.tv_note_description.setText(list.get(position).description);
        holder.tv_note_by.setText("Note By "+list.get(position).fullName);
        holder.tv_log_title.setText(list.get(position).logTitle);


        final String url = list.get(position).filePath;
        holder.tv_filepath.setText(url.substring(url.lastIndexOf('/') + 1));

        holder.tv_filepath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String file = url.substring(url.lastIndexOf("/") + 1);

                String file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                Log.e("filename: ", "" + file);
                Log.e("extension: ", "" + file_extension);

                if (file_extension.equalsIgnoreCase(".jpg")) {
                    openImage(list.get(position).filePath, file);
                } else if (file_extension.equalsIgnoreCase(".pdf")) {
        /*            Intent intent = new Intent(activity, OpenPdfActivity.class);
                    intent.putExtra("file_name", file);
                    intent.putExtra("file_path", list.get(position).filePath);
                    activity.startActivity(intent);*/
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_approvalsname, tv_log_request_sent_to, tv_log_title;
        TextView tv_note_by, tv_note_description,tv_filepath,tv_event_date;
        LinearLayout lnyellowback;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_approvalsname = (TextView) itemView.findViewById(R.id.tv_approvalsname);
            tv_log_request_sent_to = (TextView) itemView.findViewById(R.id.tv_log_request_sent_to);
            tv_log_title = (TextView) itemView.findViewById(R.id.tv_log_title);
            tv_note_description = (TextView) itemView.findViewById(R.id.tv_note_description);
            tv_note_by = (TextView) itemView.findViewById(R.id.tv_note_by);
            tv_filepath = (TextView) itemView.findViewById(R.id.tv_filepath);
            tv_event_date = (TextView) itemView.findViewById(R.id.tv_event_date);
            lnyellowback = (LinearLayout) itemView.findViewById(R.id.lnyellowback);

        }
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
}



