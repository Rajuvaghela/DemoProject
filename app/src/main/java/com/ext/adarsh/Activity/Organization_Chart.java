package com.ext.adarsh.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.ext.adarsh.Adapter.AdapterApprovals;
import com.ext.adarsh.Adapter.AdapterOrgoEmployee;
import com.ext.adarsh.Adapter.AdapterOrgoManager;
import com.ext.adarsh.Adapter.AdapterOrgoMydeatil;
import com.ext.adarsh.Bean.BeanApprovalsList;
import com.ext.adarsh.Bean.BeanOrgoEmployee;
import com.ext.adarsh.Bean.BeanOrgoManager;
import com.ext.adarsh.Bean.BeanOrgoMydetail;
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

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class Organization_Chart extends BaseActivity {

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.tv4)
    TextView tv4;

    Dialog dd,contactDialog;

    @BindView(R.id.recyler_manager)
    RecyclerView recyler_manager;

    @BindView(R.id.recyler_employee)
    RecyclerView recyler_employee;

    @BindView(R.id.recyler_mydetail)
    RecyclerView recyler_mydetail;

    @BindView(R.id.lnmanager)
    LinearLayout lnmanager;

    @BindView(R.id.lnmydetail)
    LinearLayout lnmydetail;

    @BindView(R.id.lnemployee)
    LinearLayout lnemployee;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_organization,frameLayout);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);
        setFonat();

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        recyler_manager.setHasFixedSize(true);
        LinearLayoutManager lnmanager = new LinearLayoutManager(activity);
        recyler_manager.setLayoutManager(lnmanager);
        recyler_manager.setItemAnimator(new DefaultItemAnimator());

        recyler_mydetail.setHasFixedSize(true);
        LinearLayoutManager lnmanager1 = new LinearLayoutManager(activity);
        recyler_mydetail.setLayoutManager(lnmanager1);
        recyler_mydetail.setItemAnimator(new DefaultItemAnimator());

        recyler_employee.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        recyler_employee.setLayoutManager(lnmanager2);
        recyler_employee.setItemAnimator(new DefaultItemAnimator());

        getOrgoAllData();

    }

    public void setFonat (){
        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity,MainActivity.class));
        finish();
    }

    void getOrgoAllData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Organization_Chart_By_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Manager_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Manager_Array");
                            ArrayList<BeanOrgoManager> beanOrgoManagers = new ArrayList<>();
                            if (jsonArray.length() != 0) {
                                beanOrgoManagers.addAll((Collection<? extends BeanOrgoManager>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanOrgoManager>>() {
                                }.getType()));
                                AdapterOrgoManager adapterOrgoManager = new AdapterOrgoManager(activity,beanOrgoManagers);
                                recyler_manager.setAdapter(adapterOrgoManager);
                                pd.dismiss();
                            } else {
                                recyler_manager.setAdapter(null);
                                lnmanager.setVisibility(View.GONE);
                                pd.dismiss();
                            }
                        }

                        if (object.has("My_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Detail_Array");
                            ArrayList<BeanOrgoMydetail> beanOrgoMydetails = new ArrayList<>();
                            if (jsonArray.length() != 0) {
                                beanOrgoMydetails.addAll((Collection<? extends BeanOrgoMydetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanOrgoMydetail>>() {
                                }.getType()));
                                AdapterOrgoMydeatil adapterOrgoMydeatil = new AdapterOrgoMydeatil(activity,beanOrgoMydetails);
                                recyler_mydetail.setAdapter(adapterOrgoMydeatil);
                                pd.dismiss();
                            } else {
                                recyler_mydetail.setAdapter(null);
                                lnmydetail.setVisibility(View.GONE);
                                pd.dismiss();
                            }
                        }

                        if (object.has("Employee_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Employee_Array");
                            ArrayList<BeanOrgoEmployee> beanOrgoEmployees = new ArrayList<>();
                            if (jsonArray.length() != 0) {
                                beanOrgoEmployees.addAll((Collection<? extends BeanOrgoEmployee>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanOrgoEmployee>>() {
                                }.getType()));
                                AdapterOrgoEmployee adapterOrgoEmployee = new AdapterOrgoEmployee(activity,beanOrgoEmployees);
                                recyler_employee.setAdapter(adapterOrgoEmployee);
                                pd.dismiss();
                            } else {
                                recyler_employee.setAdapter(null);
                                lnemployee.setVisibility(View.GONE);
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

    private void showAlertDialog3() {
        dd = new Dialog(this);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.detail_organization__chart);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnback = (LinearLayout)dd.findViewById(R.id.drawericon);

        TextView tv1 = (TextView)dd.findViewById(R.id.tv1);
        TextView tv2 = (TextView)dd.findViewById(R.id.tv2);
        TextView tv3 = (TextView)dd.findViewById(R.id.tv3);
        TextView tv4 = (TextView)dd.findViewById(R.id.tv4);
        TextView tv5 = (TextView)dd.findViewById(R.id.tv5);
        TextView tv6 = (TextView)dd.findViewById(R.id.tv6);
        TextView tv7 = (TextView)dd.findViewById(R.id.tv7);
        TextView tv8 = (TextView)dd.findViewById(R.id.tv8);
        TextView tv9 = (TextView)dd.findViewById(R.id.tv9);
        TextView tv10 = (TextView)dd.findViewById(R.id.tv10);
        TextView tv11 = (TextView)dd.findViewById(R.id.tv11);
        TextView tv12 = (TextView)dd.findViewById(R.id.tv12);
        TextView tv13 = (TextView)dd.findViewById(R.id.tv13);
        TextView tv14 = (TextView)dd.findViewById(R.id.tv14);


        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
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

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
            }
        });
        dd.show();
    }

    private void contactDetail() {
        contactDialog = new Dialog(activity);
        contactDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        contactDialog.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        contactDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        contactDialog.setContentView(R.layout.contact_detail_page);

        Window window = contactDialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnback = (LinearLayout)contactDialog.findViewById(R.id.lnback);
        LinearLayout lnviewprofile = (LinearLayout)contactDialog.findViewById(R.id.lnviewprofile);

        TextView tv1 = (TextView) contactDialog.findViewById(R.id.tv1);
        TextView tv2 = (TextView) contactDialog.findViewById(R.id.tv2);
        TextView tv3 = (TextView) contactDialog.findViewById(R.id.tv3);
        TextView tv4 = (TextView) contactDialog.findViewById(R.id.tv4);
        TextView tv5 = (TextView) contactDialog.findViewById(R.id.tv5);
        TextView tv6  = (TextView) contactDialog.findViewById(R.id.tv6);
        TextView tv7  = (TextView) contactDialog.findViewById(R.id.tv7);
        TextView tv8  = (TextView) contactDialog.findViewById(R.id.tv8);
        TextView tv9  = (TextView) contactDialog.findViewById(R.id.tv9);
        TextView tv10 = (TextView) contactDialog.findViewById(R.id.tv10);
        TextView tv11 = (TextView) contactDialog.findViewById(R.id.tv11);
        TextView tv12 = (TextView) contactDialog.findViewById(R.id.tv12);
        TextView tv13 = (TextView) contactDialog.findViewById(R.id.tv13);
        TextView tv14 = (TextView) contactDialog.findViewById(R.id.tv14);
        TextView tv15 = (TextView) contactDialog.findViewById(R.id.tv15);

        lnviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity, ProfileActivity.class);
                in.putExtra("key","orgo");
                activity.startActivity(in);
                activity.finish();
            }
        });


        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6 .setTypeface(Utility.getTypeFace());
        tv7 .setTypeface(Utility.getTypeFace());
        tv8 .setTypeface(Utility.getTypeFace());
        tv9 .setTypeface(Utility.getTypeFace());
        tv10.setTypeface(Utility.getTypeFace());
        tv11.setTypeface(Utility.getTypeFace());
        tv12.setTypeface(Utility.getTypeFace());
        tv13.setTypeface(Utility.getTypeFace());
        tv14.setTypeface(Utility.getTypeFace());
        tv15.setTypeface(Utility.getTypeFace());

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
            }
        });
        contactDialog.show();
    }

}
