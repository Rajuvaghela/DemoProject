package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 1/11/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ext.adarsh.Activity.AddApprovalRequestActivity;
import com.ext.adarsh.Bean.BeanApprovalsAttachmentArray;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterApprovalsEdit extends RecyclerView.Adapter<AdapterApprovalsEdit.ViewHolder> {
    Activity activity;
    List<BeanApprovalsAttachmentArray> list = new ArrayList<>();
    ProgressDialog pd;

    public AdapterApprovalsEdit(ArrayList<BeanApprovalsAttachmentArray> list, Activity activity) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_approvals_attachment_edit, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tv_approvals_file_name.setTypeface(Utility.getTypeFace());
        holder.tv_approvals_file_name.setText(list.get(position).fileName);
        holder.ll_cancel_attachement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteApprovalsAttachement(list.get(position).attachmentId,list.get(position).approvalId);
            }
        });
    }
    private void deleteApprovalsAttachement(final String attachmentId,final String approvalId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Request_Attachment_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Approvals_Request_Attachment_Delete")) {
                            JSONArray array = object.getJSONArray("Approvals_Request_Attachment_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                        AppConstant.Approvals_id = approvalId;
                                        Intent intent = new Intent(activity, AddApprovalRequestActivity.class);
                                        intent.putExtra("add_or_edit", "edit");
                                        activity.startActivity(intent);
                                        activity.finish();
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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
                    map.put("AttachmentId", attachmentId);
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
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_approvals_file_name;
        RelativeLayout ll_approvals_attachment;
        LinearLayout ll_cancel_attachement;

        public ViewHolder(View itemView) {
            super(itemView);
            pd = Utility.getDialog(activity);
            ll_cancel_attachement = (LinearLayout) itemView.findViewById(R.id.ll_cancel_attachement);
            ll_approvals_attachment = (RelativeLayout) itemView.findViewById(R.id.ll_approvals_attachment);
            tv_approvals_file_name = (TextView) itemView.findViewById(R.id.tv_approvals_file_name);
        }
    }


}


