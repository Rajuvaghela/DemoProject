package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.ext.adarsh.Activity.MessageCompose;
import com.ext.adarsh.Bean.BeanAttachmentData;
import com.ext.adarsh.Bean.BeanMessageDetail;
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

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

/**
 * Created by ExT-Emp-008 on 02-02-2018.
 */

public class AdapterMessageDetail extends RecyclerView.Adapter<AdapterMessageDetail.ViewHolder>  {
    List<BeanMessageDetail> list;
    Activity activity;
    View view1;
    ViewHolder viewHolder1;
    String parentID;
    List<BeanAttachmentData> attachlist;
    AdapterAttachment adapterAttachment;
    ProgressDialog pd;

    ArrayList<BeanAttachmentData> beanAttachmentData = new ArrayList<>();

    String  sub,body,fl = "E";

    public AdapterMessageDetail(Activity activity, List<BeanMessageDetail> list,List<BeanAttachmentData> attachlist) {
        this.list = list;
        this.activity = activity;
        this.attachlist = attachlist;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fromName,time,body,firstname,view_details,tv1,tv2,tv3,tv4,
                 tv_from,tv_to,tv_date,tv_sub,tv_time;
        LinearLayout view_detail,ll_reply_message,ll_reply_all,ll_forward;
        RecyclerView rv_attachment;

        int i=0;

        public ViewHolder(View v) {
            super(v);
            fromName = (TextView) v.findViewById(R.id.text2);
            time = (TextView) v.findViewById(R.id.text4);
            body = (TextView) v.findViewById(R.id.text6);
            firstname = (TextView) v.findViewById(R.id.firstname);
            view_details = (TextView) v.findViewById(R.id.view_details);
            view_detail = (LinearLayout) v.findViewById(R.id.view_detail);
            ll_reply_message = (LinearLayout) v.findViewById(R.id.ll_reply_message);
            ll_reply_all = (LinearLayout) v.findViewById(R.id.ll_reply_all);
            ll_forward = (LinearLayout) v.findViewById(R.id.ll_forward);

            rv_attachment = (RecyclerView) v.findViewById(R.id.rv_attachment);

            tv1 = (TextView) v.findViewById(R.id.tv1);
            tv2 = (TextView) v.findViewById(R.id.tv2);
            tv3 = (TextView) v.findViewById(R.id.tv3);
            tv4 = (TextView) v.findViewById(R.id.tv4);
            tv_from = (TextView) v.findViewById(R.id.tv_from);
            tv_to = (TextView) v.findViewById(R.id.tv_to);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_sub = (TextView) v.findViewById(R.id.tv_sub);

            tv_time = (TextView) v.findViewById(R.id.tv_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view1 = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.viewmailpage, parent, false);
        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.rv_attachment.setHasFixedSize(true);
            LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true);
            holder.rv_attachment.setLayoutManager(lnmanager2);
            holder.rv_attachment.setItemAnimator(new DefaultItemAnimator());

            if(list.get(position).attachmentFlag.equalsIgnoreCase("A")){
                adapterAttachment = new AdapterAttachment(activity,attachlist);
                holder.rv_attachment.setAdapter(adapterAttachment);
            }

            holder.view_detail.setVisibility(View.GONE);

            holder.firstname.setTypeface(Utility.getTypeFace());
            holder.fromName.setTypeface(Utility.getTypeFace());
            holder.time.setTypeface(Utility.getTypeFace());
            holder.body.setTypeface(Utility.getTypeFace());
            holder.tv1.setTypeface(Utility.getTypeFace());
            holder.tv2.setTypeface(Utility.getTypeFace());
            holder.tv3.setTypeface(Utility.getTypeFace());
            holder.tv4.setTypeface(Utility.getTypeFace());
            holder.tv_from.setTypeface(Utility.getTypeFace());
            holder.tv_to.setTypeface(Utility.getTypeFace());
            holder.tv_date.setTypeface(Utility.getTypeFace());
            holder.tv_sub.setTypeface(Utility.getTypeFace());
            holder.tv_time.setTypeface(Utility.getTypeFace());

            parentID = list.get(position).parentId;
            holder.firstname.setText(list.get(position).fromName.substring(0,1));
            holder.fromName.setText(list.get(position).fromName);
            holder.time.setText(list.get(position).time);
            holder.body.setText(Html.fromHtml(Html.fromHtml(list.get(position).body).toString()));
            showMessageDetails(list.get(position).messageId);

           holder.view_details.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   switch(holder.i){
                       case 0:Log.e("i=0",String.valueOf(holder.i));
                           holder.view_details.setText("Hide Details");
                           holder.view_detail.setVisibility(View.VISIBLE);
                           holder.time.setVisibility(View.GONE);
                           holder.tv_from.setText(list.get(position).fromName);
                           holder.tv_to.setText(list.get(position).toPeopleNames);
                           holder.tv_date.setText(list.get(position).date);
                           holder.tv_time.setText(list.get(position).time);
                           holder.tv_sub.setText(list.get(position).subject);
                           holder.i=1;
                           break;
                       case 1:Log.e("i=1",String.valueOf(holder.i));
                           holder.view_details.setText("View Details");
                           holder.view_detail.setVisibility(View.GONE);
                           holder.time.setVisibility(View.VISIBLE);
                           holder.i=0;
                           break;
                   }
               }
           });


         holder.ll_reply_message.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(activity, MessageCompose.class);
                 intent.putExtra("Key","R");
                 intent.putExtra("parentID",parentID);
                 intent.putExtra("subject",list.get(position).subject);
                 activity.startActivity(intent);
             }
         });

        holder.ll_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showMessageDetails(list.get(position).messageId);
                Log.e("Click","Clicking");
                Intent intent = new Intent(activity, MessageCompose.class);
                intent.putExtra("Key","F");
                intent.putExtra("parentID",parentID);
                intent.putExtra("subject",sub);
                String tmp = ""+ Html.fromHtml(Html.fromHtml(list.get(position).body).toString());
                intent.putExtra("body",tmp);
                intent.putExtra("fl",fl);

                Log.e("subject",""+ sub);
                Log.e("body","" + Html.fromHtml(Html.fromHtml(list.get(position).body).toString()));
                Log.e("fl",""+fl);

          //      intent.putParcelableArrayListExtra("attachment_list_array", beanAttachmentData);
                activity.startActivity(intent);
            }
        });

        holder.ll_reply_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MessageCompose.class);
                intent.putExtra("Key","RA");
                intent.putExtra("parentID",parentID);
                intent.putExtra("subject",list.get(position).subject);
                activity.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void showMessageDetails(final String messageId) {
        /*pd = Utility.getDialog(activity);
        pd.show();*/
        Log.e("pd","start");
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Forward_Detail, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Forward_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Message_Forward_Detail_Array");
                            JSONObject object1 = jsonArray.getJSONObject(0);
                            sub = object1.getString("Subject");
                            body = object1.getString("MainBodyString");
                           /* pd.dismiss();
                            Log.e("pd","dismiss");*/

                        }

                        if (object.has("Message_Attachment_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Message_Attachment_Array");
                            if (jsonArray.length() != 0) {
                                fl = "NE";
                                beanAttachmentData.addAll((Collection<? extends BeanAttachmentData>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAttachmentData>>() {
                                }.getType()));
                            }else{
                                fl = "E";
                            }
                           /* pd.dismiss();
                            Log.e("pd","dismiss");*/
                        }

                        /*pd.dismiss();
                        Log.e("pd","dismiss");*/
                    } catch (JSONException e) {
                       /* pd.dismiss();
                        Log.e("pd","dismiss");*/
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    /*pd.dismiss();
                    Log.e("pd","dismiss");*/
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
                        /*pd.dismiss();
                        Log.e("pd","dismiss");*/
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("MessageId", messageId);

                    Log.e("Message","Message_Forward_Detail");
                    Log.e("Hashkey",""+ Utility.getHashKeyPreference());
                    Log.e("LoginId",""+ Utility.getPeopleIdPreference());
                    Log.e("MessageId",""+ messageId);

                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            showSuccessAlertDialog(activity,activity.getResources().getString(R.string.network_message));
        }
    }
}


