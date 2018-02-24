package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.bumptech.glide.Glide;
import com.ext.adarsh.Activity.AddApprovalRequestActivity;
import com.ext.adarsh.Activity.Approval_Activity;
import com.ext.adarsh.Activity.TakeActionActivity;
import com.ext.adarsh.Bean.BeanApprovalsActionLog;
import com.ext.adarsh.Bean.BeanApprovalsApprovalPeople;
import com.ext.adarsh.Bean.BeanApprovalsAttachmentArray;
import com.ext.adarsh.Bean.BeanApprovalsFrom;
import com.ext.adarsh.Bean.BeanApprovalsList;
import com.ext.adarsh.Bean.BeanApprovalsReviewPeople;
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
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterApprovals extends RecyclerView.Adapter<AdapterApprovals.MyViewHolder> {

    List<BeanApprovalsReviewPeople> beanApprovalsReviewPeoples = new ArrayList<>();
    AdapterReviewPeople adapterReviewPeople;
    List<BeanApprovalsApprovalPeople> beanApprovalsApprovalPeoples = new ArrayList<>();
    AdapterApprovalsPeople adapterApprovalsPeople;
    Activity activity;
    Dialog approvalDetail, takeAction;
    ProgressDialog pd;
    TextView tv_task_approval_name, tv_approvall_note;
    LinearLayout ll_pending, ll_approved, ll_reject;
    public static ArrayList<BeanApprovalsFrom> beanApprovalsFroms_list = new ArrayList<>();
    public static ArrayList<BeanApprovalsAttachmentArray> beanApprovalsAttachmentArrays = new ArrayList<>();
    ArrayList<BeanApprovalsActionLog> beanApprovalsLog = new ArrayList<>();
    RecyclerView rv_approval_require_from;
    AdapterApprovalsFrom adapterApprovalsFrom;
    ImageView iv_request_initiator;
    TextView tv_request_initiator_name;
    TextView tv_designation;
    FloatingActionButton take_action_float;
    private List<BeanApprovalsList> knowledgeList;

    RecyclerView rv_approcal_detail_atachment, rv_approvals_log;
    String spinnerChecking = "";
    String hideshowbutton = "";

    public AdapterApprovals(List<BeanApprovalsList> knowledgeList, Activity activity) {
        this.knowledgeList = knowledgeList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_approvals, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final BeanApprovalsList approvals = knowledgeList.get(position);

        if (Utility.getApprovalUpdate().equalsIgnoreCase("Y")) {
            holder.ln_edit.setVisibility(View.VISIBLE);
            holder.lnaddtodo.setVisibility(View.VISIBLE);
        } else {
            holder.lnaddtodo.setVisibility(View.GONE);
            holder.ln_edit.setVisibility(View.GONE);
        }
        if (Utility.getApprovalDelete().equalsIgnoreCase("Y")) {
            holder.ln_delete.setVisibility(View.VISIBLE);
            holder.lnaddtodo.setVisibility(View.VISIBLE);
        } else {
            holder.lnaddtodo.setVisibility(View.GONE);
            holder.ln_delete.setVisibility(View.GONE);
        }

        if (approvals.peopleId.equalsIgnoreCase(Utility.getPeopleIdPreference())) {
            holder.ln_delete.setVisibility(View.VISIBLE);
            holder.lnaddtodo.setVisibility(View.VISIBLE);
        } else {
            holder.lnaddtodo.setVisibility(View.GONE);
            holder.ln_delete.setVisibility(View.GONE);
        }

        holder.tv_approvals_heading.setTypeface(Utility.getTypeFace());
        holder.tv_approvals_des.setTypeface(Utility.getTypeFace());
        holder.tv_approvals_pending.setTypeface(Utility.getTypeFace());
        holder.tv_approvals_reject.setTypeface(Utility.getTypeFace());
        holder.edit_my_announcement.setTypeface(Utility.getTypeFaceTab());
        holder.delete.setTypeface(Utility.getTypeFaceTab());
        holder.tv_approvals_heading.setText(approvals.taskApprovalName);
        holder.tv_approvals_des.setText(approvals.note);

        if (approvals.statusFlag.equals("P")) {
            holder.ll_approvals_pending.setVisibility(View.VISIBLE);
            holder.ll_approvals_aproved.setVisibility(View.GONE);
            holder.ll_approvals_reject.setVisibility(View.GONE);
        } else if (approvals.statusFlag.equals("A")) {
            holder.ll_approvals_aproved.setVisibility(View.VISIBLE);
            holder.ll_approvals_pending.setVisibility(View.GONE);
            holder.ll_approvals_reject.setVisibility(View.GONE);
        } else if (approvals.statusFlag.equals("R")) {
            holder.ll_approvals_aproved.setVisibility(View.GONE);
            holder.ll_approvals_pending.setVisibility(View.GONE);
            holder.ll_approvals_reject.setVisibility(View.VISIBLE);
        }

        holder.lnapproval1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvalDetail(approvals.approvalId, approvals.peopleId, approvals.statusFlag.equals("A"));
            }
        });


        holder.rv_approvals_attachment.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        holder.rv_approvals_attachment.setLayoutManager(lnmanager2);
        holder.rv_approvals_attachment.setItemAnimator(new DefaultItemAnimator());
        AdapterApprovalsAttachment adapterApprovalsAttachment = new AdapterApprovalsAttachment(activity, approvals.approval_Attachments_Array);
        holder.rv_approvals_attachment.setAdapter(adapterApprovalsAttachment);

        holder.ln_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.Approvals_id = approvals.approvalId;
                Intent intent = new Intent(activity, AddApprovalRequestActivity.class);
                intent.putExtra("add_or_edit", "edit");
                activity.startActivity(intent);
                activity.finish();

                // Approval_Activity.approvalAddDialog();
            }
        });
        holder.ln_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialoge(approvals.approvalId, position);


            }
        });
    }

    private void deleteDialoge(final String approvalId, final int position) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Delete")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        deleteApprovals(approvalId, position);
                        //   deletedata(beanEventsArrayList.get(position).eventId);
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void deleteApprovals(final String approvalId, final int pos) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Request_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Approvals_Request_Delete")) {
                            JSONArray array = object.getJSONArray("Approvals_Request_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        knowledgeList.remove(pos);
                                        Approval_Activity.adapter_approvals.notifyDataSetChanged();
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                pd.dismiss();
                            } else {
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
                    map.put("ApprovalId", approvalId);
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

    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }

    private void approvalDetail(final String approval_id, final String peopleId, boolean flag) {
        approvalDetail = new Dialog(activity);
        approvalDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        approvalDetail.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        approvalDetail.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        approvalDetail.setContentView(R.layout.m_task_approval_detail);
        Window window = approvalDetail.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        take_action_float = (FloatingActionButton) approvalDetail.findViewById(R.id.take_action_float);
        if (Utility.getApprovalTakeAction().equalsIgnoreCase("Y")) {
            take_action_float.setVisibility(View.VISIBLE);
        } else {
            take_action_float.setVisibility(View.GONE);
        }


        iv_request_initiator = (ImageView) approvalDetail.findViewById(R.id.iv_request_initiator);
        tv_designation = (TextView) approvalDetail.findViewById(R.id.tv_designation);
        tv_request_initiator_name = (TextView) approvalDetail.findViewById(R.id.tv_request_initiator_name);


        rv_approval_require_from = (RecyclerView) approvalDetail.findViewById(R.id.rv_approval_require_from);
        rv_approcal_detail_atachment = (RecyclerView) approvalDetail.findViewById(R.id.rv_approcal_detail_atachment);
        rv_approvals_log = (RecyclerView) approvalDetail.findViewById(R.id.rv_approvals_log);

        RecyclerView.LayoutManager mLayoutManager_coment = new LinearLayoutManager(activity);
        rv_approval_require_from.setLayoutManager(mLayoutManager_coment);
        rv_approval_require_from.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager mLayoutManager_coment2 = new LinearLayoutManager(activity);
        rv_approcal_detail_atachment.setLayoutManager(mLayoutManager_coment2);
        rv_approcal_detail_atachment.setItemAnimator(new DefaultItemAnimator());


        RecyclerView.LayoutManager mLayoutManager_coment3 = new LinearLayoutManager(activity);
        rv_approvals_log.setLayoutManager(mLayoutManager_coment3);
        rv_approvals_log.setItemAnimator(new DefaultItemAnimator());


        TextView h1 = (TextView) approvalDetail.findViewById(R.id.h1);
        TextView tv5 = (TextView) approvalDetail.findViewById(R.id.tv5);
        TextView tv8 = (TextView) approvalDetail.findViewById(R.id.tv8);
        TextView tv9 = (TextView) approvalDetail.findViewById(R.id.tv9);
        TextView tv_pending = (TextView) approvalDetail.findViewById(R.id.tv_pending);
        TextView tv_approved = (TextView) approvalDetail.findViewById(R.id.tv_approved);
        TextView tv_reject = (TextView) approvalDetail.findViewById(R.id.tv_reject);
        tv_task_approval_name = (TextView) approvalDetail.findViewById(R.id.tv_task_approval_name);
        tv_approvall_note = (TextView) approvalDetail.findViewById(R.id.tv_approvall_note);


        ll_approved = (LinearLayout) approvalDetail.findViewById(R.id.ll_approved);
        ll_pending = (LinearLayout) approvalDetail.findViewById(R.id.ll_pending);
        ll_reject = (LinearLayout) approvalDetail.findViewById(R.id.ll_reject);


        h1.setTypeface(Utility.getTypeFace());
        tv_pending.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv_approved.setTypeface(Utility.getTypeFace());
        tv_reject.setTypeface(Utility.getTypeFace());
        tv_task_approval_name.setTypeface(Utility.getTypeFaceTab());
        tv_approvall_note.setTypeface(Utility.getTypeFace());

        tv_request_initiator_name.setTypeface(Utility.getTypeFace());
        tv_designation.setTypeface(Utility.getTypeFace());

        LinearLayout drawericon = (LinearLayout) approvalDetail.findViewById(R.id.drawericon);

        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvalDetail.dismiss();
            }
        });

        getApprovalMoreData(approval_id, peopleId);

        take_action_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TakeActionActivity.class);
                intent.putExtra("approval_id", approval_id);
                intent.putExtra("spinnervalue", spinnerChecking);
                activity.startActivity(intent);
            }
        });
        approvalDetail.show();
    }

    private void getApprovalMoreData(final String approval_id, final String rePeopleID) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Request_And_Log_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    String peopleid = "";
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Approvals_Request_Array")) {
                            JSONArray array = object.getJSONArray("Approvals_Request_Array");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {

                                    Glide.with(activity).load(array.optJSONObject(i).getString("ProfileImage")).error(R.drawable.usericon).into(iv_request_initiator);
                                    tv_task_approval_name.setText(array.optJSONObject(i).getString("TaskApprovalName"));
                                    tv_approvall_note.setText(array.optJSONObject(i).getString("Note"));
                                    peopleid = array.optJSONObject(i).getString("PeopleId");

                                    tv_request_initiator_name.setText(array.optJSONObject(i).getString("FullName"));
                                    tv_designation.setText(array.optJSONObject(i).getString("Designation"));

                                    if (array.optJSONObject(i).getString("StatusFlag").toString().equals("A")) {
                                        ll_approved.setVisibility(View.VISIBLE);
                                        ll_pending.setVisibility(View.GONE);
                                        ll_reject.setVisibility(View.GONE);
                                        hideshowbutton = "Y";
                                    } else if (array.optJSONObject(i).getString("StatusFlag").toString().equals("R")) {
                                        ll_approved.setVisibility(View.GONE);
                                        ll_pending.setVisibility(View.GONE);
                                        ll_reject.setVisibility(View.VISIBLE);
                                        hideshowbutton = "Y";
                                    } else {
                                        ll_approved.setVisibility(View.GONE);
                                        ll_reject.setVisibility(View.GONE);
                                        ll_pending.setVisibility(View.VISIBLE);
                                        hideshowbutton = "N";
                                    }

                                }
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (object.has("Approvals_From_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Approvals_From_Array");
                            if (jsonArray.length() != 0) {
                                beanApprovalsFroms_list.clear();
                                beanApprovalsFroms_list.addAll((Collection<? extends BeanApprovalsFrom>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanApprovalsFrom>>() {
                                }.getType()));
                                adapterApprovalsFrom = new AdapterApprovalsFrom(beanApprovalsFroms_list, activity);
                                rv_approval_require_from.setAdapter(adapterApprovalsFrom);
                                pd.dismiss();
                            } else {
                                rv_approval_require_from.setAdapter(null);
                                pd.dismiss();
                            }
                        }
                        if (object.has("Approvals_Request_Attachement_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Approvals_Request_Attachement_Array");
                            if (jsonArray.length() != 0) {
                                beanApprovalsAttachmentArrays.clear();
                                beanApprovalsAttachmentArrays.addAll((Collection<? extends BeanApprovalsAttachmentArray>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanApprovalsAttachmentArray>>() {
                                }.getType()));
                                AdapterApprovalsdetailAttachment adapterApprovalsAttachment = new AdapterApprovalsdetailAttachment(activity, beanApprovalsAttachmentArrays);
                                rv_approcal_detail_atachment.setAdapter(adapterApprovalsAttachment);
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (object.has("Approvals_Request_Log_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Approvals_Request_Log_Array");
                            if (jsonArray.length() != 0) {
                                beanApprovalsLog.clear();
                                beanApprovalsLog.addAll((Collection<? extends BeanApprovalsActionLog>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanApprovalsActionLog>>() {
                                }.getType()));
                                AdapterApprovalsdetailsLogs adapterApprovalsAttachment = new AdapterApprovalsdetailsLogs(activity, beanApprovalsLog);
                                rv_approvals_log.setAdapter(adapterApprovalsAttachment);
                                pd.dismiss();
                            } else {

                                pd.dismiss();
                            }
                        }

                        String appid = "";
                        if (object.has("Approvals_From_Priority_One_Array")) {
                            JSONArray array = object.getJSONArray("Approvals_From_Priority_One_Array");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    appid = array.optJSONObject(i).getString("ApprovalFromId");
                                }
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        String Forward_For_Comment_Not_Null = "";
                        String Recommended_for_approval_Not_Null = "";
                        if (object.has("Forward_For_Comment_Request_Array")) {
                            JSONArray array = object.getJSONArray("Forward_For_Comment_Request_Array");
                            if (array.length() != 0) {
                                Forward_For_Comment_Not_Null = "yes";
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (object.has("Recommended_for_approval_Request_Array")) {
                            JSONArray array = object.getJSONArray("Recommended_for_approval_Request_Array");
                            if (array.length() != 0) {
                                Recommended_for_approval_Not_Null = "yes";
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (hideshowbutton.equalsIgnoreCase("Y")) {
                            take_action_float.setVisibility(View.GONE);
                        } else {

                            if (peopleid.equalsIgnoreCase(Utility.getPeopleIdPreference())) {
                                spinnerChecking = "1";
                                take_action_float.setVisibility(View.VISIBLE);
                            } else {
                                if (appid.equalsIgnoreCase(Utility.getPeopleIdPreference())) {
                                    spinnerChecking = "4";
                                    take_action_float.setVisibility(View.VISIBLE);
                                } else {
                                    if (!Forward_For_Comment_Not_Null.equalsIgnoreCase("")) {
                                        spinnerChecking = "3F";
                                        take_action_float.setVisibility(View.VISIBLE);
                                    } else if (Recommended_for_approval_Not_Null.equalsIgnoreCase("")) {
                                        spinnerChecking = "2R";
                                        take_action_float.setVisibility(View.VISIBLE);
                                    } else {
                                        spinnerChecking = "2";
                                        take_action_float.setVisibility(View.VISIBLE);
                                    }


                                }
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
                    map.put("ApprovalId", approval_id);
                    map.put("PeopleId", Utility.getPeopleIdPreference());
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


    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_approvals_aproved, ll_approvals_pending, ll_approvals_reject;
        LinearLayout lnapproval1, ln_edit, ln_delete, lnaddtodo;
        private TextView tv_approvals_heading, tv_approvals_des, tv_approvals_pending, tv_approvals_reject;
        RecyclerView rv_approvals_attachment;
        TextView edit_my_announcement, delete;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            pd = Utility.getDialog(activity);

            tv_approvals_heading = (TextView) view.findViewById(R.id.tv_approvals_heading);
            tv_approvals_des = (TextView) view.findViewById(R.id.tv_approvals_des);
            tv_approvals_pending = (TextView) view.findViewById(R.id.tv_approvals_pending);
            tv_approvals_reject = (TextView) view.findViewById(R.id.tv_approvals_reject);


            lnaddtodo = (LinearLayout) view.findViewById(R.id.lnaddtodo);
            ln_edit = (LinearLayout) view.findViewById(R.id.ln_edit);
            ln_delete = (LinearLayout) view.findViewById(R.id.ln_delete);

            ll_approvals_aproved = (LinearLayout) view.findViewById(R.id.ll_approvals_aproved);
            ll_approvals_pending = (LinearLayout) view.findViewById(R.id.ll_approvals_pending);
            ll_approvals_reject = (LinearLayout) view.findViewById(R.id.ll_approvals_reject);

            lnapproval1 = (LinearLayout) view.findViewById(R.id.lnapproval1);
            rv_approvals_attachment = (RecyclerView) view.findViewById(R.id.rv_approvals_attachment);

            edit_my_announcement = (TextView) view.findViewById(R.id.edit_my_announcement);
            delete = (TextView) view.findViewById(R.id.delete);
        }
    }

}

