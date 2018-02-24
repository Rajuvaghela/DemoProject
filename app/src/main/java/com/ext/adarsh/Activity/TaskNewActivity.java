package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.ext.adarsh.Adapter.AdapterTask;
import com.ext.adarsh.Adapter.TaskPagerAdapter;
import com.ext.adarsh.Bean.BeanTask;
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

public class TaskNewActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.lnfilterlist)
    LinearLayout lnfilterlist;

    public static RecyclerView recylertask;
    EditText et_name_of_task;
    EditText et_task_des;

    @BindView(R.id.addtask_float)
    FloatingActionButton addtask_float;

    Dialog addfilterlist, add_task_dialog;

    @BindView(R.id.h1)
    TextView h1;

    TaskPagerAdapter mAdapter;
    public static ProgressDialog pd;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_task_new, frameLayout);
        ButterKnife.bind(this);
        activity = this;
        h1.setTypeface(Utility.getTypeFace());
        pd = Utility.getDialog(activity);

        if (Utility.getTaskAdd().equalsIgnoreCase("Y")){
            addtask_float.setVisibility(View.VISIBLE);
        }else {
            addtask_float.setVisibility(View.GONE);
        }

        recylertask = (RecyclerView) findViewById(R.id.recylertask);
        addtask_float.setOnClickListener(this);
        recylertask.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        recylertask.setLayoutManager(lnmanager2);
        recylertask.setItemAnimator(new DefaultItemAnimator());
        getTaskData();
        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });


        lnfilterlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFilterList();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    private void addFilterList() {
        addfilterlist = new Dialog(activity);
        addfilterlist.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addfilterlist.getWindow().setWindowAnimations(R.style.DialogAnimation);
        addfilterlist.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addfilterlist.setContentView(R.layout.filter_list);

        Window window = addfilterlist.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView h1 = (TextView) addfilterlist.findViewById(R.id.h1);
        EditText edit1 = (EditText) addfilterlist.findViewById(R.id.edit1);
        EditText edit2 = (EditText) addfilterlist.findViewById(R.id.edit2);
        TextView tv2 = (TextView) addfilterlist.findViewById(R.id.tv2);
        TextView tv3 = (TextView) addfilterlist.findViewById(R.id.tv3);
        TextView tv5 = (TextView) addfilterlist.findViewById(R.id.tv5);
        TextView tv6 = (TextView) addfilterlist.findViewById(R.id.tv6);
        RadioButton radio1 = (RadioButton) addfilterlist.findViewById(R.id.radio1);
        RadioButton radio2 = (RadioButton) addfilterlist.findViewById(R.id.radio2);
        RadioButton radio3 = (RadioButton) addfilterlist.findViewById(R.id.radio3);

        h1.setTypeface(Utility.getTypeFace());
        edit1.setTypeface(Utility.getTypeFace());
        edit2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFaceTab());

        radio1.setTypeface(Utility.getTypeFace());
        radio2.setTypeface(Utility.getTypeFace());
        radio3.setTypeface(Utility.getTypeFace());

        LinearLayout drawericon = (LinearLayout) addfilterlist.findViewById(R.id.drawericon);

        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addfilterlist.dismiss();
            }
        });

        addfilterlist.show();
    }

    public static void getTaskData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Task_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Task_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Task_Array");
                            if (jsonArray.length() != 0) {
                                ArrayList<BeanTask> beanTasks = new ArrayList<>();
                                beanTasks.addAll((Collection<? extends BeanTask>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanTask>>() {
                                }.getType()));
                                AdapterTask adapter = new AdapterTask(activity, beanTasks);
                                recylertask.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                recylertask.setAdapter(null);
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


    private void addTaskList() {
        add_task_dialog = new Dialog(activity);
        add_task_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_task_dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        add_task_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        add_task_dialog.setContentView(R.layout.add_task_list_dialog);

        Window window = add_task_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        et_name_of_task = (EditText) add_task_dialog.findViewById(R.id.et_name_of_task);
        et_task_des = (EditText) add_task_dialog.findViewById(R.id.et_task_des);

        TextView text1 = (TextView) add_task_dialog.findViewById(R.id.text1);
        TextView tv_name_of_task = (TextView) add_task_dialog.findViewById(R.id.tv_name_of_task);
        TextView tv_task_des = (TextView) add_task_dialog.findViewById(R.id.tv_task_des);
        TextView tv_add_task = (TextView) add_task_dialog.findViewById(R.id.tv_add_task);
        TextView tv_cancel = (TextView) add_task_dialog.findViewById(R.id.tv_cancel);

        et_name_of_task.setTypeface(Utility.getTypeFace());
        et_task_des.setTypeface(Utility.getTypeFace());
        tv_name_of_task.setTypeface(Utility.getTypeFace());
        tv_task_des.setTypeface(Utility.getTypeFace());

        text1.setTypeface(Utility.getTypeFaceTab());
        tv_add_task.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_task_dialog.dismiss();
            }
        });

        LinearLayout lnback = (LinearLayout) add_task_dialog.findViewById(R.id.lnback);
        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_task_dialog.dismiss();
            }
        });

        et_name_of_task.addTextChangedListener(new MyTextWatcher(et_name_of_task));
        et_task_des.addTextChangedListener(new MyTextWatcher(et_task_des));

        LinearLayout ln_add_task = (LinearLayout) add_task_dialog.findViewById(R.id.ln_add_task);
        ln_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_name_of_task.getText().toString().isEmpty() && !et_task_des.getText().toString().isEmpty()) {
                    addTask(et_name_of_task.getText().toString(), et_task_des.getText().toString());
                } else {
                    Utility.showDialog(activity, "Please enter required fields.");
                }


            }
        });

        add_task_dialog.show();
    }

    public class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.et_name_of_task:
                    validateTask(et_name_of_task.getText().toString().trim());
                    break;

                case R.id.et_task_des:
                    validateTaskDes(et_task_des.getText().toString().trim());
                    break;
            }
        }
    }

    public boolean validateTask(String str) {
        String email = str;
        if (email.isEmpty()) {
            et_name_of_task.setError("Please Enter Name Of The Task");
            requestFocus(et_name_of_task);
            return false;
        } else {
            et_name_of_task.setError(null);
        }
        return true;
    }

    public boolean validateTaskDes(String str) {
        String email = str;
        if (email.isEmpty()) {
            et_task_des.setError("Please Enter  Task Description");
            requestFocus(et_task_des);
            return false;
        } else {
            et_task_des.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    void addTask(final String name_of_task, final String task_des) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Task_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Task_Add")) {
                            JSONArray array = object.getJSONArray("Task_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        add_task_dialog.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        getTaskData();
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
                    map.put("TaskTitle", name_of_task);
                    map.put("TaskDescription", task_des);
                    map.put("LoginId", Utility.getPeopleIdPreference());
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
            case R.id.addtask_float:
                addTaskList();
                break;
        }
    }
}
