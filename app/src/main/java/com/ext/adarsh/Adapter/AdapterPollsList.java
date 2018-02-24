package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.ext.adarsh.Activity.PollsActivity;
import com.ext.adarsh.Bean.BeanPoll;
import com.ext.adarsh.Bean.BeanPollsChoice;
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
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterPollsList extends RecyclerView.Adapter<AdapterPollsList.MyViewHolder> {


    Activity activity;
    Dialog dd;
    ArrayList<BeanPollsChoice> beanPolls = new ArrayList<>();
    ArrayList<BeanPoll> poll = new ArrayList<>();
    ProgressDialog pd;
    RadioButton rdbtn = null;
    int position;
    private int lastSelectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView tv_progressbar_heading;



        TextView tv_progressbar_per;
        RadioButton radiobutton;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            tv_progressbar_per = (TextView) view.findViewById(R.id.tv_progressbar_per);
            tv_progressbar_heading = (TextView) view.findViewById(R.id.tv_progressbar_heading);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

            radiobutton = (RadioButton) view.findViewById(R.id.radiobutton);
            //  rv_polls_list=(RecyclerView)view.findViewById(R.id.rv_polls_list);
            radiobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    AppConstant.choiceId = beanPolls.get(getAdapterPosition()).choiceId;
                    AppConstant.position_for_give_vote=position;
                    notifyDataSetChanged();
                }
            });
        }
    }

    public AdapterPollsList(Activity activity, ArrayList<BeanPollsChoice> beanPolls, ArrayList<BeanPoll> poll,int position) {
        this.activity = activity;
        this.beanPolls = beanPolls;
        this.position = position;
        this.poll = poll;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_polls_progressbar, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
        holder.progressBar.setMax(100);
        int i = Integer.parseInt(beanPolls.get(position).choicePer);
        holder.progressBar.setSecondaryProgress(100);
        holder.progressBar.setSecondaryProgress(i);
        holder.tv_progressbar_heading.setText(beanPolls.get(position).choice);
        holder.radiobutton.setChecked(lastSelectedPosition == position);
        holder.tv_progressbar_per.setText(beanPolls.get(position).choiceCount + " Votes");

    }

    @Override
    public int getItemCount() {
        return beanPolls.size();
    }

    public void givevote(final String pid, final String cid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Vote_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Vote_Add")) {
                            JSONArray jsonArray = object.getJSONArray("Polls_Vote_Add");
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
                    map.put("ChoiceId", cid);
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

    public void updatevote(final String pid, final String cid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Vote_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Vote_Update")) {
                            JSONArray jsonArray = object.getJSONArray("Polls_Vote_Update");
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
                    map.put("ChoiceId", cid);
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
