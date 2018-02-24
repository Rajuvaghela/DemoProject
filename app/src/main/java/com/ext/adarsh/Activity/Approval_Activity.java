package com.ext.adarsh.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.ext.adarsh.Adapter.AdapterApprovalSelectPeople;
import com.ext.adarsh.Adapter.AdapterApprovals;
import com.ext.adarsh.Adapter.AdapterApprovalsAddPeopleList;
import com.ext.adarsh.Bean.BeanApprovalsAttachmentArray;
import com.ext.adarsh.Bean.BeanApprovalsDetail;
import com.ext.adarsh.Bean.BeanApprovalsFrom;
import com.ext.adarsh.Bean.BeanApprovalsList;
import com.ext.adarsh.Bean.BeanPeopleList;
import com.ext.adarsh.Bean.ModelClass;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class Approval_Activity extends BaseActivity implements View.OnClickListener {

    static Dialog approvalDetail;

    @BindView(R.id.h1)
    TextView h1;

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.add_approvals_float)
    FloatingActionButton add_approvals_float;

    static ProgressDialog pd;
    static Activity activity;
    public static RecyclerView rv_appeovals;
    private List<BeanApprovalsList> approvals_list = new ArrayList<>();
    public static AdapterApprovals adapter_approvals;
    static ArrayList<BeanPeopleList> beanPeopleLists = new ArrayList<>();
    static Spinner spiner_select_assign_to;
    static AdapterApprovalSelectPeople adapterApprovalSelectPeople;
    private static String people_id = "0";
    public static RecyclerView rv_people_list;
    static ArrayList<ModelClass> modelClasses = new ArrayList<>();
    static AdapterApprovalsAddPeopleList adapterApprovalsAddPeopleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_approval_, frameLayout);
        ButterKnife.bind(this);
        activity = this;
        h1.setTypeface(Utility.getTypeFace());
        pd = Utility.getDialog(activity);

        if (Utility.getApprovalAdd().equalsIgnoreCase("Y")) {
            add_approvals_float.setVisibility(View.VISIBLE);
        } else {
            add_approvals_float.setVisibility(View.GONE);
        }

        add_approvals_float.setOnClickListener(this);

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        rv_appeovals = (RecyclerView) findViewById(R.id.rv_appeovals);
        rv_appeovals.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        rv_appeovals.setLayoutManager(lnmanager2);
        rv_appeovals.setItemAnimator(new DefaultItemAnimator());
        getApprovalsAllData();
    }

    public static void rv_onchangelistner() {
        rv_people_list.setAdapter(adapterApprovalsAddPeopleList);
        adapterApprovalsAddPeopleList.notifyDataSetChanged();
    }

    void getApprovalsAllData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Request_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_approvals_float:
                Intent intent = new Intent(activity, AddApprovalRequestActivity.class);
                intent.putExtra("add_or_edit", "add");
                startActivity(intent);
                finish();
                //  approvalAddDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }
}
