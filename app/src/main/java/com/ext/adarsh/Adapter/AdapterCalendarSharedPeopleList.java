package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.ext.adarsh.Bean.BeanCalendarPeopleShare;
import com.ext.adarsh.Bean.BeanCalendarSharePeopleList;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;


public class AdapterCalendarSharedPeopleList extends RecyclerView.Adapter<AdapterCalendarSharedPeopleList.ViewHolder> {

    ArrayList<BeanCalendarSharePeopleList> list;
    Activity activity;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    ProgressDialog pd;

    public AdapterCalendarSharedPeopleList(Activity activity, ArrayList<BeanCalendarSharePeopleList> list) {
        this.list = list;
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        LinearLayout ln_cross;

        public ViewHolder(View v) {
            super(v);
            ln_cross = (LinearLayout) v.findViewById(R.id.ln_cross);
            textView = (TextView) v.findViewById(R.id.subject_textview);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view1 = LayoutInflater.from(activity).inflate(R.layout.calendar_share_people_list, parent, false);
        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        pd = Utility.getDialog(activity);
        holder.textView.setText(list.get(position).fullName);
        holder.textView.setTypeface(Utility.getTypeFace());

        holder.ln_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpeopleremove(list.get(position).shareId,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void sharedpeopleremove(final String shareid,final int position) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Shared_With_People_Remove, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Calender_Shared_With_People_Remove")) {
                            JSONArray array = object.getJSONArray("Calender_Shared_With_People_Remove");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        list.remove(position);
                                        notifyDataSetChanged();
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
                    map.put("ShareId", shareid);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
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

