package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ext.adarsh.Activity.Annoucement;
import com.ext.adarsh.Bean.BeanAnnoucementDetail;
import com.ext.adarsh.Bean.BeanMyAnnoucement;
import com.ext.adarsh.Fragment.Myannouncement;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterMyAnnoucement extends RecyclerView.Adapter<AdapterMyAnnoucement.MyViewHolder> {


    private ArrayList<BeanMyAnnoucement> annoucementList;
    Activity activity;
    Dialog announcementDetail;
    ProgressDialog pd;
    RecyclerView rv_announcement_detail;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_month)
        TextView txt_month;

        @BindView(R.id.txt_date)
        TextView txt_date;

        @BindView(R.id.txtx_title1)
        TextView txtx_title1;

        @BindView(R.id.txt_title2)
        TextView txt_title2;


        @BindView(R.id.ln_line)
        LinearLayout ln_line;

        @BindView(R.id.publish)
        TextView publish;

        @BindView(R.id.edit_my_announcement)
        TextView edit_my_announcement;

        @BindView(R.id.delete)
        TextView delete;


        @BindView(R.id.ln_edit)
        LinearLayout ln_edit;

        @BindView(R.id.ln_delete)
        LinearLayout ln_delete;

        @BindView(R.id.ll_main)
        LinearLayout ll_main;

        @BindView(R.id.ln_publish)
        LinearLayout ln_publish;

        @BindView(R.id.ln_edit_delete)
        LinearLayout ln_edit_delete;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public AdapterMyAnnoucement(ArrayList<BeanMyAnnoucement> annoucementList, Activity activity) {
        this.annoucementList = annoucementList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myannouncementlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);

        if (Utility.getAnnouncementPublish().equalsIgnoreCase("Y")) {
            holder.ln_edit_delete.setVisibility(View.VISIBLE);
        } else {
            holder.ln_edit_delete.setVisibility(View.GONE);
        }

        holder.txt_date.setText(annoucementList.get(position).day);
        holder.txt_title2.setText(annoucementList.get(position).announcementDetail);
        holder.txtx_title1.setText(annoucementList.get(position).announcementTitle);

        holder.txt_month.setText(annoucementList.get(position).month);
        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                announcementDetail(annoucementList.get(position).announcementId);
            }
        });
        holder.edit_my_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.announcement_id = annoucementList.get(position).announcementId;
                AppConstant.adapter_to_myAnnouncment = "yes";
                Myannouncement.item_list.clear();
                Myannouncement.item_list2.clear();
                AppConstant.publishFlag = annoucementList.get(position).publishFlag;
                Myannouncement.addMyAnnouncement();
/*                Intent intent = new Intent(activity, EditAnnouncementActivity.class);
                intent.putExtra("announcement_id", annoucementList.get(position).announcementId);
                activity.startActivity(intent);*/

            }
        });

        if (annoucementList.get(position).publishFlag.equalsIgnoreCase("Y")) {
            holder.publish.setText("UNPUBLISH");
        }

        holder.publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.publish.getText().toString().equalsIgnoreCase("UNPUBLISH")) {
                    unpublishdata(annoucementList.get(position).announcementId);
                } else {
                    publishdata(annoucementList.get(position).announcementId);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialoge(position);
            }
        });
        if (annoucementList.get(position).referenceToPost.equalsIgnoreCase("0") && annoucementList.get(position).announcement_Attachments_Array.size() == 0) {
            holder.ln_line.setVisibility(View.GONE);
        }

        holder.txt_date.setTypeface(Utility.getTypeFaceTab());
        holder.publish.setTypeface(Utility.getTypeFaceTab());
        holder.edit_my_announcement.setTypeface(Utility.getTypeFaceTab());
        holder.delete.setTypeface(Utility.getTypeFaceTab());
        holder.txtx_title1.setTypeface(Utility.getTypeFace());
        //  holder.txt_important.setTypeface(Utility.getTypeFace());
        holder.txt_title2.setTypeface(Utility.getTypeFace());
        holder.txt_month.setTypeface(Utility.getTypeFace());
    }

    private void deleteDialoge(final int position) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Delete")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        deletedata(annoucementList.get(position).announcementId, position);
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void announcementDetail(String announcemenId) {
        announcementDetail = new Dialog(activity);
        announcementDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        announcementDetail.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        announcementDetail.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        announcementDetail.setContentView(R.layout.announcement_detail);
        Window window = announcementDetail.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        rv_announcement_detail = (RecyclerView) announcementDetail.findViewById(R.id.rv_announcement_detail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_announcement_detail.setLayoutManager(mLayoutManager);
        rv_announcement_detail.setItemAnimator(new DefaultItemAnimator());

//txt_month,txt_date,txtx_title1,txt_title2,txt_detail,header_attachment,header_imaportant,txt_important
        /*TextView tv_reg_heading = (TextView) announcementDetail.findViewById(R.id.tv_reg_heading);
        txt_attchment_name = (TextView) announcementDetail.findViewById(R.id.txt_attchment_name);
        txt_month = (TextView) announcementDetail.findViewById(R.id.txt_month);
        txt_date = (TextView) announcementDetail.findViewById(R.id.txt_date);
        txtx_title1 = (TextView) announcementDetail.findViewById(R.id.txtx_title1);
        txt_title2 = (TextView) announcementDetail.findViewById(R.id.txt_title2);
        txt_detail = (TextView) announcementDetail.findViewById(R.id.txt_detail);
        header_attachment = (TextView) announcementDetail.findViewById(R.id.header_attachment);
        header_imaportant = (TextView) announcementDetail.findViewById(R.id.header_imaportant);
        txt_important = (TextView) announcementDetail.findViewById(R.id.txt_important);

        tv_reg_heading.setTypeface(Utility.getTypeFaceTab());
        txt_date.setTypeface(Utility.getTypeFaceTab());
        txtx_title1.setTypeface(Utility.getTypeFace());
        txt_important.setTypeface(Utility.getTypeFace());
        txt_title2.setTypeface(Utility.getTypeFace());
        txt_month.setTypeface(Utility.getTypeFace());
        txt_detail.setTypeface(Utility.getTypeFace());
        header_imaportant.setTypeface(Utility.getTypeFace());
        header_attachment.setTypeface(Utility.getTypeFace());*/


        LinearLayout lnmainback = (LinearLayout) announcementDetail.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                announcementDetail.dismiss();
            }
        });


        getAnnocementDetail(announcemenId);

        announcementDetail.show();
    }

    private void getAnnocementDetail(final String announcemenId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Announcement_Detail_By_AnnouncementId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Announcement_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Announcement_Array");
                            /*if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    txt_important.setText(jsonArray.optJSONObject(i).getString("ReferenceToPostTitle").toString());
                                    txt_detail.setText(jsonArray.optJSONObject(i).getString("AnnouncementDetail").toString());
                                    txtx_title1.setText(jsonArray.optJSONObject(i).getString("AnnouncementTitle").toString());
                                    txt_title2.setText(jsonArray.optJSONObject(i).getString("PublishBy").toString());
                                    txt_month.setText(jsonArray.optJSONObject(i).getString("Month").toString());
                                    txt_date.setText(jsonArray.optJSONObject(i).getString("Day").toString());

                                    if (object.has("Announcement_Attachments_Array")) {
                                        JSONArray jsonArray1 = object.getJSONArray("Announcement_Attachments_Array");
                                        if (jsonArray1.length() != 0) {
                                            for (int j = 0; i < jsonArray1.length(); j++) {
                                                txt_attchment_name.setText(jsonArray1.optJSONObject(i).getString("Day").toString());
                                            }
                                        }
                                    }
                        *//*            jsonArray.optJSONObject(i).getString("isAttachment");
                                    jsonArray.optJSONObject(i).getString("ReferenceToPostTitle");*//*

                        *//*            jsonArray.optJSONObject(i).getString("Status");
                                    jsonArray.optJSONObject(i).getString("Status");
                                    jsonArray.optJSONObject(i).getString("Status");
                                    jsonArray.optJSONObject(i).getString("Status");*//*
                                }
                                pd.dismiss();
                            }*/
                            if (jsonArray.length() != 0) {

                                ArrayList<BeanAnnoucementDetail> beanAnnoucements = new ArrayList<>();
                                beanAnnoucements.addAll((Collection<? extends BeanAnnoucementDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAnnoucementDetail>>() {
                                }.getType()));
                                AdapterAnnoucementDetail adapter = new AdapterAnnoucementDetail(beanAnnoucements, activity);
                                rv_announcement_detail.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                rv_announcement_detail.setAdapter(null);
                                pd.dismiss();
                            }
                        }


                    } catch (JSONException e) {
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    pd.dismiss();
                    try {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            showMsg(R.string.time_out_message);
                        } else if (error instanceof AuthFailureError) {
                            showMsg(R.string.authentication_message);
                        } else if (error instanceof ServerError) {
                            showMsg(R.string.server_message);
                        } else if (error instanceof NetworkError) {
                            showMsg(R.string.connection_message);
                        } else if (error instanceof ParseError) {
                            showMsg(R.string.parsing_message);
                        } else {
                            showMsg(R.string.server_message);
                        }
                    } catch (Exception e) {
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("AnnouncementId", announcemenId);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return annoucementList.size();
    }

    public void publishdata(final String pid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Announcement_Publish, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Announcement_Publish")) {
                            JSONArray jsonArray = object.getJSONArray("Announcement_Publish");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (jsonArray.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + jsonArray.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        Annoucement.getAnnocementDetail();
                                        pd.dismiss();
                                    } else {
                                        Toast.makeText(activity, "" + jsonArray.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }
                                pd.dismiss();
                            }
                        }

                    } catch (JSONException e) {
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    pd.dismiss();
                    try {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            showMsg(R.string.time_out_message);
                        } else if (error instanceof AuthFailureError) {
                            showMsg(R.string.authentication_message);
                        } else if (error instanceof ServerError) {
                            showMsg(R.string.server_message);
                        } else if (error instanceof NetworkError) {
                            showMsg(R.string.connection_message);
                        } else if (error instanceof ParseError) {
                            showMsg(R.string.parsing_message);
                        } else {
                            showMsg(R.string.server_message);
                        }
                    } catch (Exception e) {
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("AnnouncementId", pid);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void unpublishdata(final String pid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Announcement_Unpublish, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Announcement_Unpublish")) {
                            JSONArray jsonArray = object.getJSONArray("Announcement_Unpublish");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (jsonArray.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + jsonArray.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        Annoucement.getAnnocementDetail();
                                        pd.dismiss();
                                    } else {
                                        Toast.makeText(activity, "" + jsonArray.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }
                                pd.dismiss();
                            }
                        }

                    } catch (JSONException e) {
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    pd.dismiss();
                    try {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            showMsg(R.string.time_out_message);
                        } else if (error instanceof AuthFailureError) {
                            showMsg(R.string.authentication_message);
                        } else if (error instanceof ServerError) {
                            showMsg(R.string.server_message);
                        } else if (error instanceof NetworkError) {
                            showMsg(R.string.connection_message);
                        } else if (error instanceof ParseError) {
                            showMsg(R.string.parsing_message);
                        } else {
                            showMsg(R.string.server_message);
                        }
                    } catch (Exception e) {
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("AnnouncementId", pid);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void deletedata(final String pid, final int position) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Announcement_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Announcement_Delete")) {
                            JSONArray jsonArray = object.getJSONArray("Announcement_Delete");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (jsonArray.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + jsonArray.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        annoucementList.remove(position);
                                        Annoucement.adapter.notifyDataSetChanged();
                                        Annoucement.getAnnocementDetail();
                                        pd.dismiss();
                                    } else {
                                        Toast.makeText(activity, "" + jsonArray.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }
                                pd.dismiss();
                            }
                        }

                    } catch (JSONException e) {
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    pd.dismiss();
                    try {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            showMsg(R.string.time_out_message);
                        } else if (error instanceof AuthFailureError) {
                            showMsg(R.string.authentication_message);
                        } else if (error instanceof ServerError) {
                            showMsg(R.string.server_message);
                        } else if (error instanceof NetworkError) {
                            showMsg(R.string.connection_message);
                        } else if (error instanceof ParseError) {
                            showMsg(R.string.parsing_message);
                        } else {
                            showMsg(R.string.server_message);
                        }
                    } catch (Exception e) {
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("AnnouncementId", pid);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

}
