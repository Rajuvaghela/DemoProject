package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.ext.adarsh.Adapter.Adapter1CalanderPeople;
import com.ext.adarsh.Adapter.Adapter2MyEventsPeopleList;
import com.ext.adarsh.Adapter.AdapterFileSharePeopleList;
import com.ext.adarsh.Bean.BeanCalanderEventDetail;
import com.ext.adarsh.Bean.BeanCalanderPeople;
import com.ext.adarsh.Bean.BeanPeopleFileShare;
import com.ext.adarsh.Bean.ModelClass3;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class AddCalanderEventActivity extends AppCompatActivity implements View.OnClickListener {


    RelativeLayout rl_event_start_date, rl_event_start_time, rl_event_end_date, rl_event_end_time;
    EditText et_event_title, et_event_notes, et_event_loc;
    TextView tv_event_start_date, tv_event_start_time, tv_event_end_date, tv_event_end_time;
    TextView tv_header, tv_create_event, tv_save_event, tv_cancel;
    LinearLayout ll_select_person, lnmainback;
    Activity activity;
    ProgressDialog pd;
    ImageView iv_event_c1, iv_event_c2, iv_event_c3, iv_event_c4, iv_event_c5, iv_event_c6, iv_event_c7, iv_event_c8, iv_event_c9,
            iv_event_c10, iv_event_c11, iv_event_c12;

    static RecyclerView rv_select_person;
    static Dialog open_tag_dialog_people;
    static Activity activity2;
    static RecyclerView recyclerview5;
    static RecyclerView recyclerview6;
    static Adapter1CalanderPeople adapter1people;
    static RecyclerView.LayoutManager recylerViewLayoutManager3;
    public static RecyclerView.Adapter recyclerview_adapter3;
    public static List<ModelClass3> item_list3 = new ArrayList<>();
    private ArrayList<BeanCalanderEventDetail> beanCalanderEventDetails = new ArrayList<>();
    private static ArrayList<BeanCalanderPeople> beanCalanderPeoples = new ArrayList<>();
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv12, tv7, tv14;
    String add_or_edit = "", editid = "";
    String EventBgColor = "#FE7642", EventFontColor = "#FFFFFF";
    View view_selected_color;//ll_edit_event , ll_add_event,ll_create_events,ll_save_events
    LinearLayout ll_edit_event, ll_add_event, ll_create_events, ll_save_events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calander_event);
        activity = this;
        activity2 = this;
        pd = Utility.getDialog(activity);

        Bundle bundle = getIntent().getExtras();
        add_or_edit = bundle.getString("add_or_edit");
        editid = bundle.getString("id");
        item_list3.clear();
        iv_event_c1 = (ImageView) findViewById(R.id.iv_event_c1);
        iv_event_c2 = (ImageView) findViewById(R.id.iv_event_c2);
        iv_event_c3 = (ImageView) findViewById(R.id.iv_event_c3);
        iv_event_c4 = (ImageView) findViewById(R.id.iv_event_c4);
        iv_event_c5 = (ImageView) findViewById(R.id.iv_event_c5);
        iv_event_c6 = (ImageView) findViewById(R.id.iv_event_c6);
        iv_event_c7 = (ImageView) findViewById(R.id.iv_event_c7);
        iv_event_c8 = (ImageView) findViewById(R.id.iv_event_c8);
        iv_event_c9 = (ImageView) findViewById(R.id.iv_event_c9);
        iv_event_c10 = (ImageView) findViewById(R.id.iv_event_c10);
        iv_event_c11 = (ImageView) findViewById(R.id.iv_event_c11);
        iv_event_c12 = (ImageView) findViewById(R.id.iv_event_c12);
        view_selected_color = (View) findViewById(R.id.view_selected_color);

        rv_select_person = (RecyclerView) findViewById(R.id.rv_select_person);
        FlowLayoutManager flowLayoutManager3 = new FlowLayoutManager();
        flowLayoutManager3.setAutoMeasureEnabled(true);
        rv_select_person.setLayoutManager(flowLayoutManager3);

        rl_event_start_date = (RelativeLayout) findViewById(R.id.rl_event_start_date);
        rl_event_start_time = (RelativeLayout) findViewById(R.id.rl_event_start_time);
        rl_event_end_date = (RelativeLayout) findViewById(R.id.rl_event_end_date);
        rl_event_end_time = (RelativeLayout) findViewById(R.id.rl_event_end_time);

        ll_select_person = (LinearLayout) findViewById(R.id.ll_select_person);
        lnmainback = (LinearLayout) findViewById(R.id.lnmainback);
        ll_create_events = (LinearLayout) findViewById(R.id.ll_create_events);
        ll_save_events = (LinearLayout) findViewById(R.id.ll_save_events);
        ll_edit_event = (LinearLayout) findViewById(R.id.ll_edit_event);
        ll_add_event = (LinearLayout) findViewById(R.id.ll_add_event);

        if (add_or_edit.equalsIgnoreCase("add")) {
            ll_edit_event.setVisibility(View.GONE);
            ll_save_events.setVisibility(View.GONE);
            ll_add_event.setVisibility(View.VISIBLE);
            ll_create_events.setVisibility(View.VISIBLE);
        } else {
            ll_edit_event.setVisibility(View.VISIBLE);
            ll_save_events.setVisibility(View.VISIBLE);
            ll_add_event.setVisibility(View.GONE);
            ll_create_events.setVisibility(View.GONE);
        }

        et_event_title = (EditText) findViewById(R.id.et_event_title);
        et_event_notes = (EditText) findViewById(R.id.et_event_notes);
        et_event_loc = (EditText) findViewById(R.id.et_event_loc);

        et_event_title.addTextChangedListener(new MyTextWatcher(et_event_title));

        tv_event_start_date = (TextView) findViewById(R.id.tv_event_start_date);
        tv_event_start_time = (TextView) findViewById(R.id.tv_event_start_time);
        tv_event_end_date = (TextView) findViewById(R.id.tv_event_end_date);
        tv_event_end_time = (TextView) findViewById(R.id.tv_event_end_time);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tv6 = (TextView) findViewById(R.id.tv6);
        tv7 = (TextView) findViewById(R.id.tv7);
        tv12 = (TextView) findViewById(R.id.tv12);
        tv14 = (TextView) findViewById(R.id.tv14);

        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_create_event = (TextView) findViewById(R.id.tv_create_event);
        tv_save_event = (TextView) findViewById(R.id.tv_save_event);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        et_event_title.setTypeface(Utility.getTypeFace());
        et_event_notes.setTypeface(Utility.getTypeFace());
        et_event_loc.setTypeface(Utility.getTypeFace());

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv12.setTypeface(Utility.getTypeFace());
        tv14.setTypeface(Utility.getTypeFace());


        tv_header.setTypeface(Utility.getTypeFace());
        tv_create_event.setTypeface(Utility.getTypeFaceTab());
        tv_save_event.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());

        tv_event_start_date.setTypeface(Utility.getTypeFace());
        tv_event_start_time.setTypeface(Utility.getTypeFace());
        tv_event_end_date.setTypeface(Utility.getTypeFace());
        tv_event_end_time.setTypeface(Utility.getTypeFace());


        rl_event_start_date.setOnClickListener(this);
        rl_event_start_time.setOnClickListener(this);
        rl_event_end_date.setOnClickListener(this);
        rl_event_end_time.setOnClickListener(this);
        ll_create_events.setOnClickListener(this);
        ll_select_person.setOnClickListener(this);
        lnmainback.setOnClickListener(this);
        ll_save_events.setOnClickListener(this);

        iv_event_c1.setOnClickListener(this);
        iv_event_c2.setOnClickListener(this);
        iv_event_c3.setOnClickListener(this);
        iv_event_c4.setOnClickListener(this);
        iv_event_c5.setOnClickListener(this);
        iv_event_c6.setOnClickListener(this);
        iv_event_c7.setOnClickListener(this);
        iv_event_c8.setOnClickListener(this);
        iv_event_c9.setOnClickListener(this);
        iv_event_c10.setOnClickListener(this);
        iv_event_c11.setOnClickListener(this);
        iv_event_c12.setOnClickListener(this);
        if (add_or_edit == null) {

        } else if (add_or_edit.equalsIgnoreCase("edit")) {
            GetEditData();
        } else {

        }
        //GetEditData();
    }

    private void GetCalanderPeopleData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_People, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("edit_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if (object.has("Calender_Event_People")) {
                            JSONArray jsonArray = object.getJSONArray("Calender_Event_People");
                            if (jsonArray.length() != 0) {
                                beanCalanderPeoples.clear();
                                beanCalanderPeoples.addAll((Collection<? extends BeanCalanderPeople>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanCalanderPeople>>() {
                                }.getType()));
                                openTagPopuppeople();
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
                    Log.e("error", error.toString());
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
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    private void GetEditData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_Edit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("edit_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if (object.has("Calender_Event_Edit")) {
                            JSONArray jsonArray = object.getJSONArray("Calender_Event_Edit");
                            if (jsonArray.length() != 0) {
                                beanCalanderEventDetails.clear();
                                beanCalanderEventDetails.addAll((Collection<? extends BeanCalanderEventDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanCalanderEventDetail>>() {
                                }.getType()));

                                //et_event_title ,  tv_event_start_date , tv_event_start_time ,
                                // tv_event_end_date,tv_event_end_time,et_event_notes,et_event_loc
                                et_event_title.setText(beanCalanderEventDetails.get(0).eventTitle);
                                tv_event_start_time.setText(beanCalanderEventDetails.get(0).startTime);
                                tv_event_end_time.setText(beanCalanderEventDetails.get(0).endTime);
                                et_event_notes.setText(beanCalanderEventDetails.get(0).description);
                                et_event_loc.setText(beanCalanderEventDetails.get(0).address);
                                String start_date = null, end_date = null;
                                try {
                                    start_date = dateFormatter(beanCalanderEventDetails.get(0).startDate);
                                    end_date = dateFormatter(beanCalanderEventDetails.get(0).endDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                tv_event_start_date.setText(start_date);
                                tv_event_end_date.setText(end_date);

                               /* et_event_title.setText(edit_List.get(0).eventTitle);
                                String start_date = null, end_date = null;
                                try {
                                    start_date = dateFormatter(edit_List.get(0).startDate);
                                    end_date = dateFormatter(edit_List.get(0).endDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                tv_event_start_date.setText(start_date);
                                tv_event_end_date.setText(end_date);
                                tv_event_start_time.setText(edit_List.get(0).startTime);
                                tv_event_end_time.setText(edit_List.get(0).endTime);
                                et_address.setText(edit_List.get(0).address);
                                et_validate_days.setText(edit_List.get(0).validDay);
                                et_event_des.setText(edit_List.get(0).description);
                                Glide.with(activity).load(edit_List.get(0).eventImage).into(iv_user_profile);
                                imgString = edit_List.get(0).eventImage;

                                latitude = Double.parseDouble("23.2156");
                                longitude = Double.parseDouble("72.6369");

                                String data = edit_List.get(0).departmentNameList;
                                String[] items = data.split(",");
                                for (String item : items) {
                                    department_name.add(item);
                                }

                                String data2 = edit_List.get(0).departmentId;
                                String[] items2 = data2.split(",");
                                for (String item : items2) {
                                    department_id.add(item);
                                }

                                String data3 = edit_List.get(0).branchNameList;
                                String[] items3 = data3.split(",");
                                for (String item : items3) {
                                    branch_name.add(item);
                                }

                                String data4 = edit_List.get(0).branchId;
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

                                is_first_time = "yes";
                                callOnBackPress();
                                callOnBackPress2();*/
                                //  callOnBackPress3();

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
                    Log.e("error", error.toString());
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
                    map.put("CalenderId", editid);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    public static String dateFormatter(String s) throws ParseException {

        Date date;

        DateFormat inputFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        date = inputFormatter.parse(s);
        DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
        String str = outputFormatter.format(date);

        return str;

    }


    private class MyTextWatcher implements TextWatcher {
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
                //     et_validate_days,et_event_title,et_address,et_event_des
                case R.id.et_event_title:
                    validatetitle();
                    break;

                case R.id.et_event_loc:
                    validateAddress();
                    break;

                case R.id.et_event_notes:
                    validate_des();
                    break;


            }
        }
    }

    private boolean validatetitle() {
        String email = et_event_title.getText().toString().trim();
        if (email.isEmpty()) {
            et_event_title.setError("Please Write Event Title");
            requestFocus(et_event_title);
            return false;
        } else {
            et_event_title.setError(null);
        }
        return true;
    }


    private boolean validateAddress() {
        String email = et_event_loc.getText().toString().trim();
        if (email.isEmpty()) {
            et_event_loc.setError("Please Enter Address");
            requestFocus(et_event_loc);
            return false;
        } else {
            et_event_loc.setError(null);
        }
        return true;
    }

    private boolean validate_des() {
        String email = et_event_notes.getText().toString().trim();
        if (email.isEmpty()) {
            et_event_notes.setError("Please Write Event Note");
            requestFocus(et_event_notes);
            return false;
        } else {
            et_event_notes.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_event_c1:
                EventBgColor = "#FE7642";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FE7642"));
                break;

            case R.id.iv_event_c2:
                EventBgColor = "#E32B23";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#E32B23"));
                break;

            case R.id.iv_event_c3:
                EventBgColor = "#DEADFA";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#DEADFA"));
                break;

            case R.id.iv_event_c4:
                EventBgColor = "#A0C0F8";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#A0C0F8"));
                break;

            case R.id.iv_event_c5:
                EventBgColor = "#5884E2";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#5884E2"));
                break;

            case R.id.iv_event_c6:
                EventBgColor = "#22D6E0";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#22D6E0"));
                break;

            case R.id.iv_event_c7:
                EventBgColor = "#6AE7C1";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#6AE7C1"));
                break;

            case R.id.iv_event_c8:
                EventBgColor = "#43B35A";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#43B35A"));
                break;

            case R.id.iv_event_c9:
                EventBgColor = "#FFD56F";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FFD56F"));
                break;

            case R.id.iv_event_c10:
                EventBgColor = "#FFB97A";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FFB97A"));
                break;

            case R.id.iv_event_c11:
                EventBgColor = "#FF8A80";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FF8A80"));
                break;

            case R.id.iv_event_c12:
                EventBgColor = "#E1E1E1";
                EventFontColor = "#000000";
                view_selected_color.setBackgroundColor(Color.parseColor("#E1E1E1"));
                break;

            case R.id.lnmainback:
                startActivity(new Intent(activity, CalendarActivity.class));
                finish();
                break;
            case R.id.rl_event_start_date:
                eventStartDate();
                break;
            case R.id.rl_event_start_time:
                eventStartTime();
                break;
            case R.id.rl_event_end_date:
                eventEndDate();
                break;
            case R.id.rl_event_end_time:
                eventEndTime();
                break;
            case R.id.ll_save_events:
                String people_list1 = "";
                String pid1 = "";

                String people_list_name1 = "";
                String p_name1 = "";
                for (int i = 0; i < item_list3.size(); i++) {
                    pid1 += item_list3.get(i).getId() + ",";
                }
                for (int i = 0; i < item_list3.size(); i++) {
                    p_name1 += item_list3.get(i).getName() + ",";
                }
                if (p_name1.length() > 0) {
                    people_list_name1 = p_name1.substring(0, p_name1.length() - 1);
                }

                if (pid1.length() > 0) {
                    people_list1 = pid1.substring(0, pid1.length() - 1);
                }

                if (et_event_title.getText().toString().equalsIgnoreCase("")) {
                    et_event_title.setError("Enter Event Title");
                    requestFocus(et_event_title);
                } else if (tv_event_start_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
                    Toast.makeText(activity, "Please Select event start date", Toast.LENGTH_SHORT).show();
                } else if (tv_event_start_time.getText().toString().equalsIgnoreCase("hh:mm")) {
                    Toast.makeText(activity, "Please Select event start time", Toast.LENGTH_SHORT).show();
                } else if (tv_event_end_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
                    Toast.makeText(activity, "Please Select event end date", Toast.LENGTH_SHORT).show();
                } else if (tv_event_end_time.getText().toString().equalsIgnoreCase("hh:mm")) {
                    Toast.makeText(activity, "Please Select event end time", Toast.LENGTH_SHORT).show();
                } else if (et_event_notes.getText().toString().equalsIgnoreCase("")) {
                    et_event_notes.setError("Please Write Event Note");
                    requestFocus(et_event_notes);
                } else if (et_event_loc.getText().toString().equalsIgnoreCase("")) {
                    et_event_loc.setError("Please Enter Address");
                    requestFocus(et_event_loc);
                } else{
                    SaveEvent(et_event_title.getText().toString(), tv_event_start_date.getText().toString(), tv_event_end_date.getText().toString(),
                            tv_event_start_time.getText().toString(), tv_event_end_time.getText().toString(), et_event_loc.getText().toString(),
                            et_event_notes.getText().toString(), people_list1, people_list_name1);
                }

                break;

            case R.id.ll_select_person:
                GetCalanderPeopleData();
                break;
            case R.id.ll_create_events:
                String people_list = "";
                String pid = "";

                String people_list_name = "";
                String p_name = "";
                for (int i = 0; i < item_list3.size(); i++) {
                    pid += item_list3.get(i).getId() + ",";
                }
                for (int i = 0; i < item_list3.size(); i++) {
                    p_name += item_list3.get(i).getName() + ",";
                }
                if (p_name.length() > 0) {
                    people_list_name = p_name.substring(0, p_name.length() - 1);
                }

                if (pid.length() > 0) {
                    people_list = pid.substring(0, pid.length() - 1);
                }

                if (et_event_title.getText().toString().equalsIgnoreCase("")) {
                    et_event_title.setError("Enter Event Title");
                    requestFocus(et_event_title);
                } else if (tv_event_start_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
                    Toast.makeText(activity, "Please Select event start date", Toast.LENGTH_SHORT).show();
                } else if (tv_event_start_time.getText().toString().equalsIgnoreCase("hh:mm")) {
                    Toast.makeText(activity, "Please Select event start time", Toast.LENGTH_SHORT).show();
                } else if (tv_event_end_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
                    Toast.makeText(activity, "Please Select event end date", Toast.LENGTH_SHORT).show();
                } else if (tv_event_end_time.getText().toString().equalsIgnoreCase("hh:mm")) {
                    Toast.makeText(activity, "Please Select event end time", Toast.LENGTH_SHORT).show();
                } else if (et_event_notes.getText().toString().equalsIgnoreCase("")) {
                    et_event_notes.setError("Please Write Event Note");
                    requestFocus(et_event_notes);
                } else if (et_event_loc.getText().toString().equalsIgnoreCase("")) {
                    et_event_loc.setError("Please Enter Address");
                    requestFocus(et_event_loc);
                } else{
                    CreateEvent(et_event_title.getText().toString(), tv_event_start_date.getText().toString(), tv_event_end_date.getText().toString(),
                            tv_event_start_time.getText().toString(), tv_event_end_time.getText().toString(), et_event_loc.getText().toString(),
                            et_event_notes.getText().toString(), people_list, people_list_name);
                }

                break;
        }
    }

    private static void openTagPopuppeople() {
        open_tag_dialog_people = new Dialog(activity2);
        open_tag_dialog_people.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_tag_dialog_people.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_tag_dialog_people.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_tag_dialog_people.setContentView(R.layout.tag_popup_item_layout);

        Window window = open_tag_dialog_people.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        EditText et_search = (EditText) open_tag_dialog_people.findViewById(R.id.et_search);
        final TextView iv_done = (TextView) open_tag_dialog_people.findViewById(R.id.iv_done);
        TextView header = (TextView) open_tag_dialog_people.findViewById(R.id.header);
        header.setText("Select People");
        header.setTypeface(Utility.getTypeFaceTab());
        LinearLayout lnmainback = (LinearLayout) open_tag_dialog_people.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_tag_dialog_people.dismiss();
            }
        });

        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_list3.size() > 0) {
                    //tv_select_person,rv_select_person
                    //   tv_select_person.setVisibility(View.GONE);
                    //  rv_select_person.setVisibility(View.VISIBLE);
                } else {
                    //  tv_select_person.setVisibility(View.VISIBLE);
                    //   rv_select_person.setVisibility(View.GONE);
                }

                callOnBackPress3();
                open_tag_dialog_people.dismiss();
            }
        });

        recyclerview5 = (RecyclerView) open_tag_dialog_people.findViewById(R.id.recyclerview1);
        recyclerview6 = (RecyclerView) open_tag_dialog_people.findViewById(R.id.recyclerview2);

        recylerViewLayoutManager3 = new LinearLayoutManager(activity2);
        recyclerview5.setLayoutManager(recylerViewLayoutManager3);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerview6.setLayoutManager(flowLayoutManager);

        adapter1people = new Adapter1CalanderPeople(activity2, beanCalanderPeoples);
        recyclerview5.setAdapter(adapter1people);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter1people.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        open_tag_dialog_people.show();
    }

    public static void callOnBackPress3() {
        recyclerview_adapter3 = new Adapter2MyEventsPeopleList(activity2, item_list3);
        rv_onchangelistner3();

    }

    public static void rv_onchangelistner3() {

        rv_select_person.setAdapter(recyclerview_adapter3);
        rv_select_person.addOnItemTouchListener(
                new RecyclerItemClickListener(activity2, rv_select_person, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        openTagPopuppeople();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        recyclerview_adapter3.notifyDataSetChanged();
        recyclerview6.setAdapter(recyclerview_adapter3);
        recyclerview_adapter3.notifyDataSetChanged();

    }

    private void SaveEvent(final String et_event_title, final String tv_event_start_date, final String tv_event_end_date, final String tv_event_start_time,
                           final String tv_event_end_time, final String et_event_loc, final String et_event_notes,
                           final String people_list, final String people_list_name) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Calender_Event_Update")) {
                            JSONArray array = object.getJSONArray("Calender_Event_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        startActivity(new Intent(activity, CalendarActivity.class));
                                        finish();
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
                    map.put("CalenderId", editid);
                    map.put("EventTitle", et_event_title);
                    map.put("StartDate", tv_event_start_date);
                    map.put("EndDate", tv_event_end_date);
                    map.put("StartTime", tv_event_start_time);
                    map.put("EndTime", tv_event_end_time);
                    map.put("Address", et_event_loc);
                    map.put("Description", et_event_notes);
                    map.put("EventBgColor", EventBgColor);
                    map.put("EventFontColor", EventFontColor);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("PersonListId", people_list);
                    map.put("PersonList", people_list_name);
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

    private void CreateEvent(final String et_event_title, final String tv_event_start_date, final String tv_event_end_date, final String tv_event_start_time,
                             final String tv_event_end_time, final String et_event_loc, final String et_event_notes,
                             final String people_list, final String people_list_name) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Calender_Event_Add")) {
                            JSONArray array = object.getJSONArray("Calender_Event_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        startActivity(new Intent(activity, CalendarActivity.class));
                                        finish();
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
                    map.put("EventTitle", et_event_title);
                    map.put("StartDate", tv_event_start_date);
                    map.put("EndDate", tv_event_end_date);
                    map.put("StartTime", tv_event_start_time);
                    map.put("EndTime", tv_event_end_time);
                    map.put("Address", et_event_loc);
                    map.put("Description", et_event_notes);
                    map.put("EventBgColor", EventBgColor);
                    map.put("EventFontColor", EventFontColor);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("PersonListId", people_list);
                    map.put("PersonList", people_list_name);
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

    private void GetSharedFilePeopleList(final String fileid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Folder_Share_People_List, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
                            activity.finish();
                        }
/*                        if (object.has("File_Folder_Share_People_List_Array")) {
                            JSONArray jsonArray = object.getJSONArray("File_Folder_Share_People_List_Array");
                            if (jsonArray.length() != 0) {
                                beanFileFolderSharePeopleLists.clear();
                                beanFileFolderSharePeopleLists.addAll((Collection<? extends BeanFileFolderSharePeopleList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFileFolderSharePeopleList>>() {
                                }.getType()));
                                adapter3 = new AdapterFileSharePeopleList(beanFileFolderSharePeopleLists, activity);
                                rv_allready_shared_person.setAdapter(adapter3);
                                pd.dismiss();
                            } else {
                                rv_allready_shared_person.setAdapter(null);
                                pd.dismiss();
                            }
                        }*/
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
                        showMsg( R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("FileId", fileid);
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

    private void eventEndTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                final String time = selectedHour + ":" + selectedMinute;
                tv_event_end_time.setText(time);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void eventEndDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String formattedDate = df.format(c.getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date1 = null, date2 = null;
                        try {
                            date1 = sdf.parse(date);
                            date2 = sdf.parse(formattedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (date1.equals(date2)) {
                            tv_event_end_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            //tv_late_publish_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        } else if (date1.after(date2)) {
                            tv_event_end_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            // tv_late_publish_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        } else {

                            Toast.makeText(activity, "You can not select past date", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void eventStartTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                final String time = selectedHour + ":" + selectedMinute;
                tv_event_start_time.setText(time);
            }
        }, hour, minute, true);//Yes 24 hour time

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void eventStartDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String formattedDate = df.format(c.getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date1 = null, date2 = null;
                        try {
                            date1 = sdf.parse(date);
                            date2 = sdf.parse(formattedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (date1.equals(date2)) {
                            tv_event_start_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        } else if (date1.after(date2)) {
                            tv_event_start_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        } else {
                            Toast.makeText(activity, "You can not select past date", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(activity, CalendarActivity.class));
        finish();
    }
}
