package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ext.adarsh.Adapter.Adapter1BranchMyPolls;
import com.ext.adarsh.Adapter.Adapter1MyPolls;
import com.ext.adarsh.Adapter.Adapter2Branch;
import com.ext.adarsh.Adapter.Adapter2BranchMyPolls;
import com.ext.adarsh.Adapter.Adapter2MyPolls;
import com.ext.adarsh.Adapter.AdapterMyPolls;
import com.ext.adarsh.Adapter.AdapterPolls;
import com.ext.adarsh.Adapter.AdapterPollsOption;
import com.ext.adarsh.Bean.BeanBranchList;
import com.ext.adarsh.Bean.BeanDepartmentList;
import com.ext.adarsh.Bean.BeanPoll;
import com.ext.adarsh.Bean.BeanPollsDetail;
import com.ext.adarsh.Bean.ModelClass;
import com.ext.adarsh.Bean.ModelClass2;
import com.ext.adarsh.Bean.ModelPollsAnswer;
import com.ext.adarsh.Fragment.mypolls;
import com.ext.adarsh.Fragment.polls;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.RecyclerItemClickListener;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

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

public class AddPollsActivity extends AppCompatActivity implements View.OnClickListener {


    Activity activity;
    static Activity activity2;

    ImageView img_plus, img_minus;
    LinearLayout choice3, choice4;

    ProgressDialog pd;
    static ProgressDialog pd2;
    public static List<ModelClass> item_list = new ArrayList<>();
    public static List<ModelClass2> item_list2 = new ArrayList<>();

    static RecyclerView recyclerview1;
    static RecyclerView recyclerview2;

    static RecyclerView recyclerview3;
    static RecyclerView recyclerview4;

    List<ModelClass> list = new ArrayList<>();
    public static List<BeanDepartmentList> department_List = new ArrayList<>();
    public static List<BeanBranchList> branch_List = new ArrayList<>();
    static Adapter1MyPolls adapter1;
    static Adapter1BranchMyPolls adapter1branch;
    static RecyclerView.LayoutManager recylerViewLayoutManager;
    static RecyclerView.LayoutManager recylerViewLayoutManager2;
    public static RecyclerView.Adapter recyclerview_adapter;
    public static RecyclerView.Adapter recyclerview_adapter2;
    static Dialog open_tag_dialog;
    static RecyclerView rv_select_visible_branch;
    static RecyclerView rv_select_visible_department;
    static TextView tv_select_visible_department, tv_select_visible_branch;

    EditText et_add_polls_question, edit_option1, edit_option3, edit_option4;
    TextView txt_days_count;
    TextView tv2, tv3, tv5, tv6, tv7, tv8, tv9, txt_cancle, tv11, tv1, txt_save_polls;
    LinearLayout lnmainback, ll_select_department, ll_select_branch;
    Spinner spinner;
    String branchID = "", departId = "";
    RecyclerView rv_polls_answer;
    List<ModelPollsAnswer> modelPollsAnswers = new ArrayList<>();

    static ArrayList<String> department_name = new ArrayList<>();
    static ArrayList<String> department_id = new ArrayList<>();
    static ArrayList<String> branch_name = new ArrayList<>();
    static ArrayList<String> branch_id = new ArrayList<>();

    LinearLayout ll_add_option, ll_header_edit_polls, ll_header_add_polls, ll_create_polls, ll_save_polls,ll_cancel;
    String add_or_edit;
    private static String is_polls_edit = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_polls);

        activity = this;
        activity2 = this;
        ButterKnife.bind(this, activity);
        pd2 = Utility.getDialog(activity);
        pd = Utility.getDialog(activity);
        Bundle bundle = getIntent().getExtras();
        add_or_edit = bundle.getString("add_or_edit");


        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_create_polls = (LinearLayout) findViewById(R.id.ll_create_polls);
        ll_save_polls = (LinearLayout) findViewById(R.id.ll_save_polls);
        ll_header_edit_polls = (LinearLayout) findViewById(R.id.ll_header_edit_polls);
        ll_header_add_polls = (LinearLayout) findViewById(R.id.ll_header_add_polls);
        et_add_polls_question = (EditText) findViewById(R.id.et_add_polls_question);
        edit_option1 = (EditText) findViewById(R.id.edit_option1);
        edit_option3 = (EditText) findViewById(R.id.edit_option3);
        edit_option4 = (EditText) findViewById(R.id.edit_option4);
        txt_days_count = (TextView) findViewById(R.id.txt_days_count);
        txt_save_polls = (TextView) findViewById(R.id.txt_save_polls);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv5 = (TextView) findViewById(R.id.tv5);

        tv6 = (TextView) findViewById(R.id.tv6);
        tv7 = (TextView) findViewById(R.id.tv7);
        tv8 = (TextView) findViewById(R.id.tv8);
        tv9 = (TextView) findViewById(R.id.tv9);
        tv11 = (TextView) findViewById(R.id.tv11);

        et_add_polls_question.setTypeface(Utility.getTypeFace());
        edit_option1.setTypeface(Utility.getTypeFace());
        edit_option3.setTypeface(Utility.getTypeFace());
        edit_option4.setTypeface(Utility.getTypeFace());
        txt_days_count.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv11.setTypeface(Utility.getTypeFaceTab());
        tv1.setTypeface(Utility.getTypeFaceTab());
        txt_save_polls.setTypeface(Utility.getTypeFaceTab());

        lnmainback = (LinearLayout) findViewById(R.id.lnmainback);
        choice3 = (LinearLayout) findViewById(R.id.liner_choice3);
        choice4 = (LinearLayout) findViewById(R.id.liner_choice4);
        ll_select_department = (LinearLayout) findViewById(R.id.ll_select_department);
        ll_select_branch = (LinearLayout) findViewById(R.id.ll_select_branch);
        ll_add_option = (LinearLayout) findViewById(R.id.ll_add_option);


        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Yes");
        categories.add("No");

        spinner = (Spinner) findViewById(R.id.spiner);
        img_plus = (ImageView) findViewById(R.id.img_plus);
        img_minus = (ImageView) findViewById(R.id.img_minus);

        final TextView txt_days_count = (TextView) findViewById(R.id.txt_days_count);
        tv_select_visible_department = (TextView) findViewById(R.id.tv_select_visible_department);
        tv_select_visible_branch = (TextView) findViewById(R.id.tv_select_visible_branch);
        rv_select_visible_branch = (RecyclerView) findViewById(R.id.rv_select_visible_branch);
        rv_select_visible_department = (RecyclerView) findViewById(R.id.rv_select_visible_department);
        rv_polls_answer = (RecyclerView) findViewById(R.id.rv_polls_answer);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        rv_select_visible_department.setLayoutManager(flowLayoutManager);

        FlowLayoutManager flowLayoutManager2 = new FlowLayoutManager();
        flowLayoutManager2.setAutoMeasureEnabled(true);
        rv_select_visible_branch.setLayoutManager(flowLayoutManager2);

        rv_polls_answer.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        rv_polls_answer.setLayoutManager(lnmanager2);
        rv_polls_answer.setItemAnimator(new DefaultItemAnimator());

        TextView txt_createpolls = (TextView) findViewById(R.id.txt_createpolls);
        txt_cancle = (TextView) findViewById(R.id.txt_cancle);
        TextView tv8 = (TextView) findViewById(R.id.tv8);
        TextView tv9 = (TextView) findViewById(R.id.tv9);
        TextView tv10 = (TextView) findViewById(R.id.tv10);

        txt_days_count.setTypeface(Utility.getTypeFace());
        tv_select_visible_department.setTypeface(Utility.getTypeFace());
        tv_select_visible_branch.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv10.setTypeface(Utility.getTypeFace());

        txt_createpolls.setTypeface(Utility.getTypeFaceTab());
        txt_cancle.setTypeface(Utility.getTypeFaceTab());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ll_cancel.setOnClickListener(this);
        ll_create_polls.setOnClickListener(this);
        ll_select_department.setOnClickListener(this);
        ll_select_branch.setOnClickListener(this);
        img_minus.setOnClickListener(this);
        ll_add_option.setOnClickListener(this);
        ll_save_polls.setOnClickListener(this);
        img_plus.setOnClickListener(this);
        lnmainback.setOnClickListener(this);
        txt_days_count.setText("1");

        if (add_or_edit.equalsIgnoreCase("add")) {
            ll_header_edit_polls.setVisibility(View.GONE);
            ll_save_polls.setVisibility(View.GONE);
            ll_header_add_polls.setVisibility(View.VISIBLE);
            ll_create_polls.setVisibility(View.VISIBLE);
        } else {
            ll_header_edit_polls.setVisibility(View.VISIBLE);
            ll_save_polls.setVisibility(View.VISIBLE);
            ll_header_add_polls.setVisibility(View.GONE);
            ll_create_polls.setVisibility(View.GONE);
            getPollsDetail();
        }
        getDepartmentBranchData();
    }

    public void getPollsDetail() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Select_By_PollId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Polls_Array");
                            if (jsonArray.length() != 0) {
                                ArrayList<BeanPollsDetail> beanPolls = new ArrayList<>();
                                beanPolls.addAll((Collection<? extends BeanPollsDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPollsDetail>>() {
                                }.getType()));
                                for (int i = 0; i < beanPolls.size(); i++) {
                                    for (int j = 0; j < beanPolls.get(i).polls_Choice_Array.size(); j++) {
                                        ModelPollsAnswer temp = new ModelPollsAnswer();
                                        temp.setPolls_answer(beanPolls.get(i).polls_Choice_Array.get(j).choice);
                                        temp.setPolls_id(beanPolls.get(i).pollId);
                                        temp.setChoice_id(beanPolls.get(i).polls_Choice_Array.get(j).choiceId);
                                        modelPollsAnswers.add(temp);
                                    }
                                }
                                AdapterPollsOption adapterPollsOption = new AdapterPollsOption(activity, modelPollsAnswers);
                                rv_polls_answer.setAdapter(adapterPollsOption);
                                edit_option1.setText("");

                                et_add_polls_question.setText(beanPolls.get(0).pollQuestion);
                                txt_days_count.setText(beanPolls.get(0).length);


                                String data = beanPolls.get(0).departmentNameList;
                                String[] items = data.split(",");
                                for (String item : items) {
                                    department_name.add(item);
                                }

                                String data2 = beanPolls.get(0).departments;
                                String[] items2 = data2.split(",");
                                for (String item : items2) {
                                    department_id.add(item);
                                }

                                String data3 = beanPolls.get(0).branchNameList;
                                String[] items3 = data3.split(",");
                                for (String item : items3) {
                                    branch_name.add(item);
                                }

                                String data4 = beanPolls.get(0).branches;
                                String[] items4 = data4.split(",");
                                for (String item : items4) {
                                    branch_id.add(item);
                                }

                                for (int i = 0; i < department_name.size(); i++) {
                                    ModelClass temp = new ModelClass();
                                    temp.setId(department_id.get(i).toString());
                                    temp.setName(department_name.get(i).toString());
                                    item_list.add(temp);

                                }
                                for (int i = 0; i < branch_name.size(); i++) {
                                    ModelClass2 temp = new ModelClass2();
                                    temp.setId(branch_id.get(i).toString());
                                    temp.setName(branch_name.get(i).toString());
                                    item_list2.add(temp);
                                }

                                is_polls_edit = "yes";
                                callOnBackPress();
                                callOnBackPress2();

                                pd.dismiss();
                            } else {
                                rv_polls_answer.setAdapter(null);
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
                    map.put("PollId", AppConstant.PollId);
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

    void UpdatePolls(final String question, final String length, final String show, final String dep, final String branch,
                     final String department_flag, final String Branch_flag) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("add_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Update")) {
                            JSONArray array = object.getJSONArray("Polls_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd2.dismiss();
                                        Log.e("onResponse: ", "sizee is " + String.valueOf(modelPollsAnswers.size()));
                                        for (int j = 0; j < modelPollsAnswers.size(); j++) {
                                            PollsChoiceUpdate(modelPollsAnswers.get(j).getPolls_answer(),
                                                    modelPollsAnswers.get(j).getChoice_id(), AppConstant.PollId);
                                        }
                                    } else {
                                        pd2.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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
                    map.put("PollQuestion", question);
                    map.put("Length", length);
                    map.put("Startdate", AppConstant.startdate);
                    map.put("isShowResult", show);
                    map.put("Departments", dep);
                    map.put("Branches", branch);
                    map.put("CreatedBy", Utility.getPeopleIdPreference());
                    map.put("PublishFlag", AppConstant.publishFlag);
                    map.put("PollId", AppConstant.PollId);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("DepartmentsFlag", department_flag);
                    map.put("BranchesFlag", Branch_flag);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd2.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private void PollsChoiceUpdate(final String choice, final String choice_id, final String polls_id) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Choice_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("add_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Choice_Update")) {
                            JSONArray array = object.getJSONArray("Polls_Choice_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd2.dismiss();
                                        Intent intent = new Intent(activity, PollsActivity.class);
                                        intent.putExtra("id", "1");
                                        startActivity(intent);
                                        finish();

                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        pd2.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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
                    map.put("PollId", polls_id);
                    map.put("Choice", choice);
                    map.put("ChoiceId", choice_id);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;

                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd2.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    void CreatePolls(final String question, final String length, final String show, final String dep,
                     final String branch, final String departmentsFlag, final String branchesFlag) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("add_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Add")) {
                            JSONArray array = object.getJSONArray("Polls_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd2.dismiss();
                                        String pid = array.optJSONObject(i).getString("PollId");
                                        for (int j = 0; j < modelPollsAnswers.size(); j++) {
                                            String option = modelPollsAnswers.get(j).getPolls_answer();
                                            PollsChoiceAdd(pid, option);
                                        }

                                    } else {
                                        pd2.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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
                    map.put("PollQuestion", question);
                    map.put("Length", length);
                    map.put("isShowResult", show);
                    map.put("Departments", dep);
                    map.put("Branches", branch);
                    map.put("DepartmentsFlag", departmentsFlag);
                    map.put("BranchesFlag", branchesFlag);
                    map.put("CreatedBy", Utility.getPeopleIdPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd2.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }


    public static void callOnBackPress() {
        recyclerview_adapter = new Adapter2MyPolls(activity2, item_list);
        rv_onchangelistner();
    }

    public static void callOnBackPress2() {
        recyclerview_adapter2 = new Adapter2BranchMyPolls(activity2, item_list2);
        rv_onchangelistner2();

    }

    static void openTagPopup() {
        openTagDialog();
    }

    static void getDepartmentBranchData() {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Add_Bind_Data, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Department_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Department_Array");
                            if (jsonArray.length() != 0) {
                                department_List.clear();
                                item_list.clear();
                                department_List.addAll((Collection<? extends BeanDepartmentList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanDepartmentList>>() {
                                }.getType()));
                                pd2.dismiss();
                            } else {
                                pd2.dismiss();
                            }
                        }

                        if (object.has("Branch_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Branch_Array");
                            if (jsonArray.length() != 0) {
                                branch_List.clear();
                                item_list2.clear();
                                branch_List.addAll((Collection<? extends BeanBranchList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanBranchList>>() {
                                }.getType()));
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd2.dismiss();

        }
    }

    public static void rv_onchangelistner() {
        rv_select_visible_department.setAdapter(recyclerview_adapter);
        rv_select_visible_department.addOnItemTouchListener(
                new RecyclerItemClickListener(activity2, rv_select_visible_department, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        openTagPopup();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        if (is_polls_edit.equalsIgnoreCase("yes")) {

        } else {
            recyclerview_adapter.notifyDataSetChanged();
            recyclerview2.setAdapter(recyclerview_adapter);
            recyclerview_adapter.notifyDataSetChanged();
        }
    }

    public static void rv_onchangelistner2() {

        rv_select_visible_branch.setAdapter(recyclerview_adapter2);
        rv_select_visible_branch.addOnItemTouchListener(
                new RecyclerItemClickListener(activity2, rv_select_visible_branch, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        openTagPopupBranch();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        if (is_polls_edit.equalsIgnoreCase("yes")) {

        } else {
            recyclerview_adapter2.notifyDataSetChanged();
            recyclerview4.setAdapter(recyclerview_adapter2);
            recyclerview_adapter2.notifyDataSetChanged();
        }

       /* recyclerview_adapter2.notifyDataSetChanged();
        recyclerview4.setAdapter(recyclerview_adapter2);
        recyclerview_adapter2.notifyDataSetChanged();*/
    }

    public static void openTagPopupBranch() {
        open_tag_dialog = new Dialog(activity2);
        open_tag_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_tag_dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_tag_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_tag_dialog.setContentView(R.layout.tag_popup_item_layout);

        Window window = open_tag_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        EditText et_search = (EditText) open_tag_dialog.findViewById(R.id.et_search);
        TextView iv_done = (TextView) open_tag_dialog.findViewById(R.id.iv_done);

        TextView header = (TextView) open_tag_dialog.findViewById(R.id.header);
        header.setText("Select Branch");
        header.setTypeface(Utility.getTypeFaceTab());

        LinearLayout lnmainback = (LinearLayout) open_tag_dialog.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_tag_dialog.dismiss();
            }
        });

        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onClick: branchhh ", "branchhh ++++" + String.valueOf(item_list2.size()));
                if (item_list2.size() > 0) {
                    tv_select_visible_branch.setVisibility(View.GONE);
                    rv_select_visible_branch.setVisibility(View.VISIBLE);
                } else {
                    tv_select_visible_branch.setVisibility(View.VISIBLE);
                    rv_select_visible_branch.setVisibility(View.GONE);
                }
                callOnBackPress2();
                open_tag_dialog.dismiss();
            }
        });

        recyclerview3 = (RecyclerView) open_tag_dialog.findViewById(R.id.recyclerview1);
        recyclerview4 = (RecyclerView) open_tag_dialog.findViewById(R.id.recyclerview2);

        recylerViewLayoutManager2 = new LinearLayoutManager(activity2);
        recyclerview3.setLayoutManager(recylerViewLayoutManager2);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerview4.setLayoutManager(flowLayoutManager);

        adapter1branch = new Adapter1BranchMyPolls(activity2, branch_List);
        recyclerview3.setAdapter(adapter1branch);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter1branch.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        open_tag_dialog.show();
    }

    public static void openTagDialog() {
        open_tag_dialog = new Dialog(activity2);
        open_tag_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_tag_dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_tag_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_tag_dialog.setContentView(R.layout.tag_popup_item_layout);

        Window window = open_tag_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        EditText et_search = (EditText) open_tag_dialog.findViewById(R.id.et_search);
        TextView iv_done = (TextView) open_tag_dialog.findViewById(R.id.iv_done);

        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_list.size() > 0) {
                    tv_select_visible_department.setVisibility(View.GONE);
                    rv_select_visible_department.setVisibility(View.VISIBLE);
                } else {
                    tv_select_visible_department.setVisibility(View.VISIBLE);
                    rv_select_visible_department.setVisibility(View.GONE);
                }
                callOnBackPress();
                open_tag_dialog.dismiss();
            }
        });

        recyclerview1 = (RecyclerView) open_tag_dialog.findViewById(R.id.recyclerview1);
        recyclerview2 = (RecyclerView) open_tag_dialog.findViewById(R.id.recyclerview2);
        TextView header = (TextView) open_tag_dialog.findViewById(R.id.header);
        header.setText("Select Department");
        header.setTypeface(Utility.getTypeFaceTab());

        LinearLayout lnmainback = (LinearLayout) open_tag_dialog.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_tag_dialog.dismiss();
            }
        });

        recylerViewLayoutManager = new LinearLayoutManager(activity2);
        recyclerview1.setLayoutManager(recylerViewLayoutManager);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerview2.setLayoutManager(flowLayoutManager);

        adapter1 = new Adapter1MyPolls(activity2, department_List);
        recyclerview1.setAdapter(adapter1);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter1.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        open_tag_dialog.show();
    }


    void PollsChoiceAdd(final String id, final String choice) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_Choice_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Choice_Add")) {
                            JSONArray array = object.getJSONArray("Polls_Choice_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd2.dismiss();
                                        Intent intent = new Intent(activity, PollsActivity.class);
                                        intent.putExtra("id", "1");
                                        startActivity(intent);
                                        Toast.makeText(activity2, "Polls Add successfully.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        pd2.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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
                    map.put("PollId", id);
                    map.put("Choice", choice);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd2.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_create_polls:
                if (et_add_polls_question.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter Question", Toast.LENGTH_SHORT).show();
                } else if (modelPollsAnswers.size() < 0) {
                    Toast.makeText(activity, "Please Enter Option", Toast.LENGTH_SHORT).show();
                } else {

                    String bid = "";
                    String did = "";

                    String department_flag = "";
                    String Branch_flag = "";

                    if (item_list.size() < 1) {
                        department_flag = "A";
                        for (int i = 0; i < department_List.size(); i++) {
                            departId += department_List.get(i).departmentId + ",";
                        }

                    } else {
                        department_flag = "D";
                        for (int i = 0; i < item_list.size(); i++) {
                            departId += item_list.get(i).getId() + ",";
                        }
                    }

                    if (item_list2.size() < 1) {
                        Branch_flag = "A";
                        for (int i = 0; i < branch_List.size(); i++) {
                            branchID += branch_List.get(i).branchId + ",";
                        }
                    } else {
                        Branch_flag = "D";
                        for (int i = 0; i < item_list2.size(); i++) {
                            branchID += item_list2.get(i).getId() + ",";
                        }
                    }

                    if (branchID.length() > 0) {
                        bid = branchID.substring(0, branchID.length() - 1);
                    }
                    if (departId.length() > 0) {
                        did = departId.substring(0, departId.length() - 1);
                    }
                    String showtext;
                    if (spinner.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                        showtext = "Y";
                    } else {
                        showtext = "N";
                    }
                    CreatePolls(et_add_polls_question.getText().toString(), txt_days_count.getText().toString(),
                            showtext, did, bid, department_flag, Branch_flag);
                }
                break;
            case R.id.ll_save_polls:
                if (et_add_polls_question.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter Question", Toast.LENGTH_SHORT).show();
                } else if (modelPollsAnswers.size() < 0) {
                    Toast.makeText(activity, "Please Enter Option", Toast.LENGTH_SHORT).show();
                } else {

                    String bid = "";
                    String did = "";

                    String department_flag = "";
                    String Branch_flag = "";

                    if (item_list.size() < 1) {
                        department_flag = "A";
                        for (int i = 0; i < department_List.size(); i++) {
                            departId += department_List.get(i).departmentId + ",";
                        }

                    } else {
                        department_flag = "D";
                        for (int i = 0; i < item_list.size(); i++) {
                            departId += item_list.get(i).getId() + ",";
                        }
                    }

                    if (item_list2.size() < 1) {
                        Branch_flag = "A";
                        for (int i = 0; i < branch_List.size(); i++) {
                            branchID += branch_List.get(i).branchId + ",";
                        }
                    } else {
                        Branch_flag = "D";
                        for (int i = 0; i < item_list2.size(); i++) {
                            branchID += item_list2.get(i).getId() + ",";
                        }
                    }


                    if (branchID.length() > 0) {
                        bid = branchID.substring(0, branchID.length() - 1);
                    }
                    if (departId.length() > 0) {
                        did = departId.substring(0, departId.length() - 1);
                    }


                    String showtext;
                    if (spinner.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                        showtext = "Y";
                    } else {
                        showtext = "N";
                    }


                    UpdatePolls(et_add_polls_question.getText().toString(), txt_days_count.getText().toString(), showtext,
                            did, bid, department_flag, Branch_flag);
                }
                break;
            //img_minus,ll_select_branch,ll_select_department,img_plus,add,add2,lnmainback,txt_cancle

            case R.id.img_minus:
                int a = Integer.parseInt(txt_days_count.getText().toString());
                if (a > 1) {
                    txt_days_count.setText(String.valueOf(a - 1));
                }
                break;
            case R.id.ll_select_branch:
                openTagPopupBranch();
                break;
            case R.id.ll_select_department:
                openTagPopup();
                break;
            case R.id.img_plus:
                int b = Integer.parseInt(txt_days_count.getText().toString());
                txt_days_count.setText(String.valueOf(b + 1));
                break;

            case R.id.lnmainback:
                Intent intent = new Intent(activity, PollsActivity.class);
                intent.putExtra("id", "1");
                startActivity(intent);
                break;
            case R.id.ll_cancel/*txt_cancle*/:
                Intent intent2 = new Intent(activity, PollsActivity.class);
                intent2.putExtra("id", "1");
                startActivity(intent2);
                break;

            case R.id.ll_add_option:
                if (edit_option1.getText().toString().trim().equalsIgnoreCase("")) {

                } else {
                    ModelPollsAnswer temp = new ModelPollsAnswer();
                    temp.setPolls_answer(edit_option1.getText().toString());
                    temp.setChoice_id("");
                    modelPollsAnswers.add(temp);
                    AdapterPollsOption adapterPollsOption = new AdapterPollsOption(activity, modelPollsAnswers);
                    rv_polls_answer.setAdapter(adapterPollsOption);
                    edit_option1.setText("");
                }
                //rv_onchangelistner2();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity, PollsActivity.class);
        intent.putExtra("id", "1");
        startActivity(intent);
    }
}
