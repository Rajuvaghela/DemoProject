package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ext.adarsh.Activity.PollsActivity;
import com.ext.adarsh.Bean.BeanPoll;
import com.ext.adarsh.Fragment.polls;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterPolls extends RecyclerView.Adapter<AdapterPolls.MyViewHolder> {


    Activity activity;
    Dialog dd;
    ArrayList<BeanPoll> beanPolls = new ArrayList<>();
    String voteid = "0";
    ProgressDialog pd;
    AdapterPollsList adapterPollsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.question)
        TextView question;

        @BindView(R.id.vote)
        TextView vote;

        @BindView(R.id.btn_vote)
        TextView btn_vote;
/*

        @BindView(R.id.radiogroup)
        RadioGroup radioGroup;
*/

/*        @BindView(R.id.ln_dynamic_res)
        LinearLayout ln_dynamic_res;*/

     /*   @BindView(R.id.lnmain_choice)
        LinearLayout lnmain_choice;*/

        @BindView(R.id.ln_vote)
        LinearLayout ln_vote;
        RecyclerView rv_polls_list;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rv_polls_list = (RecyclerView) view.findViewById(R.id.rv_polls_list);
        }
    }

    public AdapterPolls(Activity activity, ArrayList<BeanPoll> beanPolls) {
        this.activity = activity;
        this.beanPolls = beanPolls;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.polls_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);

        holder.rv_polls_list.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        holder.rv_polls_list.setLayoutManager(lnmanager2);
        holder.rv_polls_list.setItemAnimator(new DefaultItemAnimator());
        adapterPollsList = new AdapterPollsList(activity, beanPolls.get(position).polls_Choice_Array, beanPolls, position);//BeanPollsChoice
        holder.rv_polls_list.setAdapter(adapterPollsList);

        if (Utility.getPollsVote().equalsIgnoreCase("Y")) {
            holder.ln_vote.setVisibility(View.VISIBLE);
        } else {
            holder.ln_vote.setVisibility(View.GONE);
        }

        holder.name.setTypeface(Utility.getTypeFace());
        holder.question.setTypeface(Utility.getTypeFace());
        holder.vote.setTypeface(Utility.getTypeFace());
        holder.btn_vote.setTypeface(Utility.getTypeFaceTab());

        holder.name.setText("By, " + beanPolls.get(position).createdByName);
        holder.question.setText(beanPolls.get(position).pollQuestion);

        if (beanPolls.get(position).isVoted.equalsIgnoreCase("C")) {

            // AppConstant.vote.set(position, "CHANGE_VOTE");
            holder.btn_vote.setText("CHANGE VOTE");
        }


        holder.btn_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConstant.position_for_give_vote == position) {
                    if (!AppConstant.choiceId.equalsIgnoreCase("")) {
                        if (holder.btn_vote.getText().toString().equalsIgnoreCase("CHANGE VOTE")) {
                            updatevote(beanPolls.get(position).pollId);
                        } else {
                            givevote(beanPolls.get(position).pollId);
                        }
                    } else {
                        Toast.makeText(activity, "Please select your choice", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });

       /* if (beanPolls.get(position).isPollActive.equalsIgnoreCase("A")) {
            holder.vote.setText(beanPolls.get(position).totalVotes + " Votes.");

            if (PollsActivity.polls_radio == false) {
                for (int row = 0; row < 1; row++) {
                    for (int i = 0; i < beanPolls.get(position).polls_Choice_Array.size(); i++) {
                        rdbtn = new RadioButton(activity);
                        rdbtn.setId((row * 2) + i);
                        rdbtn.setText(beanPolls.get(position).polls_Choice_Array.get(i).choice);
                        holder.radioGroup.addView(rdbtn);
                    }
                }
            }
        } else {

            holder.vote.setText(beanPolls.get(position).totalVotes + " Votes. Final Result");
            holder.ln_vote.setVisibility(View.GONE);
            holder.lnmain_choice.setVisibility(View.GONE);

            if (PollsActivity.polls_linear == false) {
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
            }
        }*/
        holder.vote.setText("Total Votes : " + beanPolls.get(position).totalVotes);
        if (beanPolls.get(position).isShowResult.equalsIgnoreCase("N")) {
            holder.vote.setVisibility(View.GONE);
        }

        if (beanPolls.size() - 1 == position) {
            PollsActivity.polls_radio = true;
            PollsActivity.polls_linear = true;
        }

    }

    @Override
    public int getItemCount() {
        return beanPolls.size();
    }

    public void givevote(final String pid) {
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
                                        AppConstant.choiceId = "";
                                        AppConstant.position_for_give_vote=0;
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
                    map.put("ChoiceId", AppConstant.choiceId);
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

    public void updatevote(final String pid) {
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
                                        AppConstant.choiceId = "";
                                        AppConstant.position_for_give_vote=0;
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
                    map.put("ChoiceId", AppConstant.choiceId);
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
