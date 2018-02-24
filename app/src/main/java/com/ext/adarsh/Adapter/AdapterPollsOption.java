package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 1/16/2018.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.ext.adarsh.Bean.ModelPollsAnswer;
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

public class AdapterPollsOption extends RecyclerView.Adapter<AdapterPollsOption.ViewHolder> {
    Activity activity;
    List<ModelPollsAnswer> list = new ArrayList<>();
    ProgressDialog pd2;

    public AdapterPollsOption(Activity activity, List<ModelPollsAnswer> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.polls_option, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.edit_option1.setText(list.get(position).getPolls_answer());
        holder.ll_add_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveChoice(list.get(position).getPolls_id(), list.get(position).getChoice_id(), position);

            }
        });
    }

    void RemoveChoice(final String Polls_id, final String Choice_id, final int position) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Choice_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Choice_Delete")) {
                            JSONArray array = object.getJSONArray("Polls_Choice_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        list.remove(position);
                                        notifyDataSetChanged();
                                        pd2.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();

                                    } else {
                                        pd2.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                pd2.dismiss();
                            } else {
                                pd2.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        pd2.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd2.dismiss();
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
                        pd2.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("PollId", Polls_id);
                    map.put("ChoiceId", Choice_id);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd2.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText edit_option1;
        LinearLayout ll_add_option;

        public ViewHolder(View itemView) {
            super(itemView);
            pd2 = Utility.getDialog(activity);
            edit_option1 = (EditText) itemView.findViewById(R.id.edit_option1);
            ll_add_option = (LinearLayout) itemView.findViewById(R.id.ll_add_option);
        }

    }

}


