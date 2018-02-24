package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.ext.adarsh.Activity.AddPollsActivity;
import com.ext.adarsh.Activity.EventAddActivity;
import com.ext.adarsh.Activity.PollsActivity;
import com.ext.adarsh.Bean.BeanPoll;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterMyPolls extends RecyclerView.Adapter<AdapterMyPolls.MyViewHolder> {


    Activity activity;
    Dialog dd;
    ArrayList<BeanPoll> beanPolls = new ArrayList<>();
    RadioButton rdbtn = null;
    ProgressDialog pd;
    AdapterPollsList adapterPollsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.question)
        TextView question;

        @BindView(R.id.vote)
        TextView vote;

        @BindView(R.id.publish)
        TextView publish;

        @BindView(R.id.edit)
        TextView edit;

        @BindView(R.id.delete)
        TextView delete;

        @BindView(R.id.ln_radio)
        LinearLayout ln_radio;

        @BindView(R.id.radiogroup)
        RadioGroup radioGroup;

        @BindView(R.id.ln_publish)
        LinearLayout ln_publish;


        @BindView(R.id.ln_edit)
        LinearLayout ln_edit;

        @BindView(R.id.ll_edit_delete)
        LinearLayout ll_edit_delete;

        RecyclerView rv_polls_list;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rv_polls_list = (RecyclerView) view.findViewById(R.id.rv_polls_list);
        }
    }

    public AdapterMyPolls(Activity activity, ArrayList<BeanPoll> beanPolls) {
        this.activity = activity;
        this.beanPolls = beanPolls;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.polls_items_mypolls, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        pd = Utility.getDialog(activity);
        holder.rv_polls_list.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        holder.rv_polls_list.setLayoutManager(lnmanager2);
        holder.rv_polls_list.setItemAnimator(new DefaultItemAnimator());
        adapterPollsList = new AdapterPollsList(activity, beanPolls.get(position).polls_Choice_Array, beanPolls, position);//BeanPollsChoice
        holder.rv_polls_list.setAdapter(adapterPollsList);

/*        adapterPollsList = new AdapterPollsList(activity, beanPolls.get(position).polls_Choice_Array, beanPolls);//BeanPollsChoice
        holder.rv_polls_list.setAdapter(adapterPollsList);*/

        if (Utility.getPollsUpdate().equalsIgnoreCase("Y")) {
            holder.ll_edit_delete.setVisibility(View.VISIBLE);
        } else {
            holder.ll_edit_delete.setVisibility(View.GONE);
        }

        holder.name.setTypeface(Utility.getTypeFace());
        holder.question.setTypeface(Utility.getTypeFace());

        holder.publish.setTypeface(Utility.getTypeFaceTab());
        holder.edit.setTypeface(Utility.getTypeFaceTab());
        holder.delete.setTypeface(Utility.getTypeFaceTab());

        holder.name.setText("By, " + beanPolls.get(position).createdByName);
        holder.question.setText(beanPolls.get(position).pollQuestion);

        if (beanPolls.get(position).publishFlag.equalsIgnoreCase("Y")) {
            holder.publish.setText("UNPUBLISH");
        }

        holder.publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConstant.position_for_give_vote == position) {
                    if (holder.publish.getText().toString().equalsIgnoreCase("UNPUBLISH")) {
                        unpublishpollsdata(beanPolls.get(position).pollId);
                    } else {
                        publishpollsdata(beanPolls.get(position).pollId);
                    }
                } else {
                    Toast.makeText(activity, "Please select your choice", Toast.LENGTH_SHORT).show();

                }


                /* if (AppConstant.position_for_give_vote == position){

                }
                    if (holder.publish.getText().toString().equalsIgnoreCase("UNPUBLISH")) {
                        unpublishpollsdata(beanPolls.get(position).pollId);
                    } else {
                        publishpollsdata(beanPolls.get(position).pollId);
                    }*/
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialoge(position);

            }
        });

        holder.ln_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.publishFlag = beanPolls.get(position).publishFlag;
                AppConstant.startdate = beanPolls.get(position).startdate;
                AppConstant.PollId = beanPolls.get(position).pollId;
                Intent intent = new Intent(activity, AddPollsActivity.class);
                intent.putExtra("add_or_edit", "edit");
                activity.startActivity(intent);
                activity.finish();
            }
        });


        if (beanPolls.get(position).isPollActive.equalsIgnoreCase("A")) {
            holder.vote.setText(beanPolls.get(position).totalVotes + " Votes.");
          /*  if (PollsActivity.mypolls_radio == false){
                for (int row = 0; row < 1; row++) {
                    for (int i = 0; i < beanPolls.get(position).polls_Choice_Array.size(); i++) {
                        rdbtn = new RadioButton(activity);
                        rdbtn.setId((row * 2) + i);
                        rdbtn.setText(beanPolls.get(position).polls_Choice_Array.get(i).choice);
                        holder.radioGroup.addView(rdbtn);
                    }
                }
            }*/
        } else {
            holder.vote.setText(beanPolls.get(position).totalVotes + " Votes. Final Result");
            holder.ln_publish.setVisibility(View.GONE);
            holder.ln_radio.setVisibility(View.GONE);
            holder.ln_edit.setVisibility(View.GONE);
        }

      /*  if (PollsActivity.mypolls_linear == false) {
            for (int i = 0; i < beanPolls.get(position).polls_Choice_Array.size(); i++) {
                LayoutInflater layoutInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.daynamiclinear_polls, null);

                LinearLayout lnmain = (LinearLayout) addView.findViewById(R.id.ln_result_my);
                LinearLayout sec_ln1 = (LinearLayout) addView.findViewById(R.id.sec_ln1);

                TextView txt_result = (TextView) addView.findViewById(R.id.txt_result);
                ImageView right_icon = (ImageView) addView.findViewById(R.id.right_icon);

                txt_result.setText(beanPolls.get(position).polls_Choice_Array.get(i).choicePer + " % " + beanPolls.get(position).polls_Choice_Array.get(i).choice);

                float a = Float.parseFloat(beanPolls.get(position).polls_Choice_Array.get(i).choicePer) / 100;

                float b = 1 - a;

                if (i == 0) {
                    if (!beanPolls.get(position).totalVotes.equalsIgnoreCase("0")) {
                        right_icon.setVisibility(View.VISIBLE);
                    }
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lnmain.getLayoutParams();
                params.weight = a;
                lnmain.setLayoutParams(params);

                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) sec_ln1.getLayoutParams();
                params1.weight = b;
                sec_ln1.setLayoutParams(params1);
                holder.ln_dynamic_res.addView(addView);

            }
        }*/

        if (beanPolls.size() - 1 == position) {
            PollsActivity.mypolls_radio = true;
            PollsActivity.mypolls_linear = true;
        }


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
                        deletepollsdata(beanPolls.get(position).pollId);
                        //  android.os.Process.killProcess(android.os.Process.myPid());

                        /// android.os.Process.killProcess(android.os.Process.myPid());
                        //  finish();
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return beanPolls.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void unpublishpollsdata(final String pid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Conceal, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Conceal")) {
                            JSONArray jsonArray = object.getJSONArray("Polls_Conceal");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (jsonArray.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        AppConstant.position_for_give_vote = 0;
                                        Toast.makeText(activity, "" + jsonArray.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        PollsActivity.getPollsData();
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
                    map.put("PollId", pid);
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

    public void publishpollsdata(final String pid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Publish, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Publish")) {
                            JSONArray jsonArray = object.getJSONArray("Polls_Publish");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (jsonArray.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        AppConstant.position_for_give_vote = 0;
                                        Toast.makeText(activity, "" + jsonArray.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        PollsActivity.getPollsData();
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
                    map.put("PollId", pid);
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

    public void deletepollsdata(final String pid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Delete")) {
                            JSONArray jsonArray = object.getJSONArray("Polls_Delete");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (jsonArray.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + jsonArray.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        PollsActivity.getPollsData();
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
                    map.put("PollId", pid);
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
