package com.ext.adarsh.Fragment;

/**
 * Created by ExT-Emp-008 on 11-01-2018.
 */


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ext.adarsh.Activity.PeopleActivity;
import com.ext.adarsh.Adapter.AdapterPeopleGroups;
import com.ext.adarsh.Bean.BeanPeopleGroups;
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
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class PeopleGroupsFragment extends Fragment implements View.OnClickListener {

    //  AdapterAnnoucement mAdapter;

    public static RecyclerView rv_groups_list;
    public static AdapterPeopleGroups adapterPeopleGroups;
    Dialog addgroup;
    Activity activity;
    public static Activity activity2;
    ProgressDialog pd;
    public static ProgressDialog pd2;

    @BindView(R.id.add_groups_float)
    FloatingActionButton add_groups_float;

    static TextView tv_no_record_found;

    public static ArrayList<BeanPeopleGroups> beanPeopleGroupses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people_group, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        activity2 = getActivity();
        pd = Utility.getDialog(activity);
        pd2 = Utility.getDialog(activity);

        tv_no_record_found = (TextView) view.findViewById(R.id.tv_no_record_found);
        rv_groups_list = (RecyclerView) view.findViewById(R.id.rv_groups_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_groups_list.setLayoutManager(mLayoutManager);
        rv_groups_list.setItemAnimator(new DefaultItemAnimator());
        getPeopleGroup();

        add_groups_float.setOnClickListener(this);

    }

    public static void getPeopleGroup() {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Category_Select_By_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("ErrorMessage")) {
                            pd2.dismiss();
                            Toast.makeText(activity2, "Please Try again", Toast.LENGTH_SHORT).show();
                        }

                        if (object.has("Category_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Category_Array");
                            if (jsonArray.length() != 0) {
                                beanPeopleGroupses.clear();
                                beanPeopleGroupses.addAll((Collection<? extends BeanPeopleGroups>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeopleGroups>>() {
                                }.getType()));
                                adapterPeopleGroups = new AdapterPeopleGroups(beanPeopleGroupses, activity2);
                                rv_groups_list.setAdapter(adapterPeopleGroups);
                                pd2.dismiss();
                            } else {
                                tv_no_record_found.setVisibility(View.VISIBLE);
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
                    Log.e(AppConstant.TAG, error.toString());
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd2.dismiss();
            showMsg(R.string.network_message);
            //  showSuccessAlertDialog(activity2, getResources().getString(R.string.network_message));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_groups_float:
                addgroupdemo();
                break;
        }
    }

    private void addgroupdemo() {

        addgroup = new Dialog(activity);
        addgroup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addgroup.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        addgroup.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addgroup.setContentView(R.layout.add_new_groping_dialog);

        Window window = addgroup.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView title = (TextView) addgroup.findViewById(R.id.title);
        final TextView edt_groupname = (EditText) addgroup.findViewById(R.id.edt_groupname);
        Button btn_save = (Button) addgroup.findViewById(R.id.btn_save);
        ImageView iv_close = (ImageView) addgroup.findViewById(R.id.iv_close);


        title.setTypeface(Utility.getTypeFace());
        edt_groupname.setTypeface(Utility.getTypeFace());
        btn_save.setTypeface(Utility.getTypeFace());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_groupname.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                } else {
                    addGroupPeople(edt_groupname.getText().toString());
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addgroup.dismiss();
            }
        });
        addgroup.show();
    }

    public void addGroupPeople(final String name) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.GroupName_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("GroupName_Add")) {
                            JSONArray array = object.getJSONArray("GroupName_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        addgroup.dismiss();
                                        getPeopleGroup();
                                        //   PeopleActivity.getPeopleData();
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
                    map.put("CategoryName", name);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            showSuccessAlertDialog(activity, activity.getResources().getString(R.string.network_message));
        }
    }
}


