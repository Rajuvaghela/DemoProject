package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.ext.adarsh.Adapter.AdapterApprovals;
import com.ext.adarsh.Bean.BeanApprovalsList;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.RecyclerItemClickListener;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

/**
 * Created by ExT-Emp-001 on 11-07-2017.
 */
public class Approvals extends Fragment implements View.OnClickListener {

    Dialog approvalDetail;
    @BindView(R.id.add_approvals_float)
    FloatingActionButton add_approvals_float;

    ProgressDialog pd;
    Activity activity;
    public static RecyclerView rv_appeovals;
    private List<BeanApprovalsList> approvals_list = new ArrayList<>();
    AdapterApprovals adapter_approvals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.approvals_items, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        pd = Utility.getDialog(activity);

        rv_appeovals = (RecyclerView) view.findViewById(R.id.rv_appeovals);
        add_approvals_float.setOnClickListener(this);
        rv_appeovals.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        rv_appeovals.setLayoutManager(lnmanager2);
        rv_appeovals.setItemAnimator(new DefaultItemAnimator());
        getApprovalsAllData();
    }

    void getApprovalsAllData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST,AppConstant.Approvals_Request_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Approval_res", response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Approvals_Request_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Approvals_Request_Array");
                            if (jsonArray.length() != 0) {
                                approvals_list.clear();
                                approvals_list.addAll((Collection<? extends BeanApprovalsList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanApprovalsList>>() {
                                }.getType()));
                                adapter_approvals = new AdapterApprovals(approvals_list, activity);
                                rv_appeovals.setAdapter(adapter_approvals);
                                rv_appeovals.addOnItemTouchListener(
                                        new RecyclerItemClickListener(getContext(), rv_appeovals, new RecyclerItemClickListener.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                approvalDetail();
                                            }
                                            @Override
                                            public void onLongItemClick(View view, int position) {

                                            }
                                        })
                                );
                                pd.dismiss();
                            } else {
                                rv_appeovals.setAdapter(null);
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

                    Log.e("Approvals_err", error.toString());
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
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    private void approvalDetail() {
        approvalDetail = new Dialog(getActivity());
        approvalDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        approvalDetail.getWindow().setWindowAnimations(R.style.DialogAnimation);
        approvalDetail.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        approvalDetail.setContentView(R.layout.m_task_approval_detail);

        Window window = approvalDetail.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView h1 = (TextView) approvalDetail.findViewById(R.id.h1);
        TextView tv1 = (TextView) approvalDetail.findViewById(R.id.tv1);
        TextView tv2 = (TextView) approvalDetail.findViewById(R.id.tv2);
        TextView tv3 = (TextView) approvalDetail.findViewById(R.id.tv3);
        TextView tv4 = (TextView) approvalDetail.findViewById(R.id.tv4);
        TextView tv5 = (TextView) approvalDetail.findViewById(R.id.tv5);
        TextView tv6 = (TextView) approvalDetail.findViewById(R.id.tv6);
        TextView tv7 = (TextView) approvalDetail.findViewById(R.id.tv7);
        TextView tv8 = (TextView) approvalDetail.findViewById(R.id.tv8);
        TextView tv9 = (TextView) approvalDetail.findViewById(R.id.tv9);
        TextView tv10 = (TextView) approvalDetail.findViewById(R.id.tv10);
        TextView tv11 = (TextView) approvalDetail.findViewById(R.id.tv11);
        TextView tv12 = (TextView) approvalDetail.findViewById(R.id.tv12);
        TextView tv13 = (TextView) approvalDetail.findViewById(R.id.tv13);
        TextView tv14 = (TextView) approvalDetail.findViewById(R.id.tv14);

        h1.setTypeface(Utility.getTypeFace());
        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFaceTab());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFaceTab());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv10.setTypeface(Utility.getTypeFace());
        tv11.setTypeface(Utility.getTypeFace());
        tv12.setTypeface(Utility.getTypeFace());
        tv13.setTypeface(Utility.getTypeFace());
        tv14.setTypeface(Utility.getTypeFace());


        LinearLayout drawericon = (LinearLayout) approvalDetail.findViewById(R.id.drawericon);

        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvalDetail.dismiss();
            }
        });

        approvalDetail.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_approvals_float:

                break;
        }
    }
}

