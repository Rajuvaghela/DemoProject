package com.ext.adarsh.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.ext.adarsh.Adapter.Adapter1BranchMyEvents;
import com.ext.adarsh.Adapter.Adapter1MyEvents;
import com.ext.adarsh.Adapter.Adapter1MyEventsPeopleList;
import com.ext.adarsh.Adapter.Adapter2BranchMyEvents;
import com.ext.adarsh.Adapter.Adapter2MyEvents;
import com.ext.adarsh.Adapter.Adapter2MyEventsPeopleList;
import com.ext.adarsh.Adapter.AdapterCity;
import com.ext.adarsh.Adapter.AdapterRegion;
import com.ext.adarsh.Adapter.AdapterState;
import com.ext.adarsh.Bean.BeanBranchList;
import com.ext.adarsh.Bean.BeanCityList;
import com.ext.adarsh.Bean.BeanDepartmentList;
import com.ext.adarsh.Bean.BeanEventEditDetail;
import com.ext.adarsh.Bean.BeanEventSelectAllPeople;
import com.ext.adarsh.Bean.BeanRegionList;
import com.ext.adarsh.Bean.BeanStateList;
import com.ext.adarsh.Bean.ModelClass;
import com.ext.adarsh.Bean.ModelClass2;
import com.ext.adarsh.Bean.ModelClass3;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.GeocodingLocation;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.RecyclerItemClickListener;
import com.ext.adarsh.Utils.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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

import butterknife.BindView;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class EventAddActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    @BindView(R.id.createevent_float)
    FloatingActionButton createevent_float;

    static Dialog open_tag_dialog;
    static Dialog open_tag_dialog_people;

    ProgressDialog pd;
    static ProgressDialog pd2;
    static Activity activity2;
    Activity activity;
    static RecyclerView rv_select_visible_branch, rv_select_person;
    static RecyclerView rv_select_visible_department;
    static TextView tv_select_visible_department, tv_select_visible_branch, tv_select_person;
    static RecyclerView recyclerview1;
    static RecyclerView recyclerview2;
    static RecyclerView recyclerview3;
    static RecyclerView recyclerview4;
    static RecyclerView recyclerview5;
    static RecyclerView recyclerview6;
    static RecyclerView.LayoutManager recylerViewLayoutManager;
    static RecyclerView.LayoutManager recylerViewLayoutManager2;
    static RecyclerView.LayoutManager recylerViewLayoutManager3;
    public static RecyclerView.Adapter recyclerview_adapter;
    public static RecyclerView.Adapter recyclerview_adapter2;
    public static RecyclerView.Adapter recyclerview_adapter3;
    public static List<ModelClass> item_list = new ArrayList<>();
    public static List<ModelClass2> item_list2 = new ArrayList<>();
    public static List<ModelClass3> item_list3 = new ArrayList<>();
    static Adapter1MyEvents adapter1;
    static Adapter1BranchMyEvents adapter1branch;
    static Adapter1MyEventsPeopleList adapter1people;
    public static List<BeanDepartmentList> department_List = new ArrayList<>();
    List<BeanEventEditDetail> edit_List = new ArrayList<>();
    public static List<BeanBranchList> branch_List = new ArrayList<>();
    private static ArrayList<BeanEventSelectAllPeople> contact_list = new ArrayList<>();
    private RadioButton radioSexButton;
    private RadioGroup radioSexGroup;
    List<BeanStateList> state_List = new ArrayList<>();
    AdapterState adapter_State;
    Spinner spiner_city, spiner_region, spiner_state;
    String state_id = "0", city_id = "0", region_id = "0", cityid = "";
    List<BeanCityList> city_List = new ArrayList<>();
    List<BeanRegionList> region_List = new ArrayList<>();
    AdapterCity adapter_ciy;
    AdapterRegion adapter_region;
    private static final int PICKFILE_RESULT_CODE = 2;
    String departName = "";
    String branchName = "";
    String peopleName = "";
    String fileBase64 = "";
    String filename = "";
    String file_extension = "";
    String imgString = "";

    String imageBase64 = "";
    String image_name = "";
    String image_extension = "";

    TextView tv_file_name;
    String public_or_private = "G";
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    FileOutputStream fo;
    String profileimg = "";
    String imagename;
    String bname = "", dname = "", pname = "";
    ImageView iv_user_profile;
    EditText et_event_title, et_validate_days, et_address, et_event_des, et_event_address;
    TextView tv_event_start_date, tv_event_start_time, tv_event_end_date, tv_event_end_time;
    ImageView iv_search_lat_long;

    LinearLayout ll_private_or_oublic, ll_add_event, ll_edit_event, ll_create_events, ll_save_events;

    public static double latitude = 0;
    public static double longitude = 0;
    CheckBox checkbox_evet_go_or_not;
    private String evet_go_or_not, add_or_edit, eventId;
    static String is_first_time = "";

    ArrayList<String> department_name = new ArrayList<>();
    ArrayList<String> department_id = new ArrayList<>();
    ArrayList<String> branch_name = new ArrayList<>();
    ArrayList<String> branch_id = new ArrayList<>();
    RadioButton radioButton, radioButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_information);
        activity = this;
        activity2 = this;
        pd = Utility.getDialog(activity);
        pd2 = Utility.getDialog(activity2);
        ll_edit_event = (LinearLayout) findViewById(R.id.ll_edit_event);
        ll_add_event = (LinearLayout) findViewById(R.id.ll_add_event);
        ll_save_events = (LinearLayout) findViewById(R.id.ll_save_events);
        ll_create_events = (LinearLayout) findViewById(R.id.ll_create_events);

        Bundle bundle = getIntent().getExtras();
        add_or_edit = bundle.getString("add_or_edit");

        if (add_or_edit.equalsIgnoreCase("add")) {
            ll_edit_event.setVisibility(View.GONE);
            ll_save_events.setVisibility(View.GONE);
            ll_create_events.setVisibility(View.VISIBLE);
            ll_add_event.setVisibility(View.VISIBLE);
        } else {
            eventId = bundle.getString("eventId");
            ll_edit_event.setVisibility(View.VISIBLE);
            ll_save_events.setVisibility(View.VISIBLE);
            ll_add_event.setVisibility(View.GONE);
            ll_create_events.setVisibility(View.GONE);
        }

        checkbox_evet_go_or_not = (CheckBox) findViewById(R.id.checkbox_evet_go_or_not);
        iv_search_lat_long = (ImageView) findViewById(R.id.iv_search_lat_long);
        iv_search_lat_long.setOnClickListener(this);
        RelativeLayout rl_select_file = (RelativeLayout) findViewById(R.id.rl_select_file);
        FrameLayout fl_user_profile = (FrameLayout) findViewById(R.id.fl_user_profile);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        spiner_state = (Spinner) findViewById(R.id.spiner_state);
        spiner_city = (Spinner) findViewById(R.id.spiner_city);
        spiner_region = (Spinner) findViewById(R.id.spiner_region);
        iv_user_profile = (ImageView) findViewById(R.id.iv_user_profile);
        ll_private_or_oublic = (LinearLayout) findViewById(R.id.ll_private_or_oublic);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        tv_file_name = (TextView) findViewById(R.id.tv_file_name);
        tv_select_visible_department = (TextView) findViewById(R.id.tv_select_visible_department);
        tv_select_visible_branch = (TextView) findViewById(R.id.tv_select_visible_branch);
        rv_select_visible_branch = (RecyclerView) findViewById(R.id.rv_select_visible_branch);
        rv_select_visible_department = (RecyclerView) findViewById(R.id.rv_select_visible_department);
        rv_select_person = (RecyclerView) findViewById(R.id.rv_select_person);

        TextView tv1 = (TextView) findViewById(R.id.tv1);
        TextView tv2 = (TextView) findViewById(R.id.tv2);
        TextView tv3 = (TextView) findViewById(R.id.tv3);
        TextView tv4 = (TextView) findViewById(R.id.tv4);
        TextView tv5 = (TextView) findViewById(R.id.tv5);
        TextView tv6 = (TextView) findViewById(R.id.tv6);
        TextView tv7 = (TextView) findViewById(R.id.tv7);
        TextView tv8 = (TextView) findViewById(R.id.tv8);
        TextView tv9 = (TextView) findViewById(R.id.tv9);
        TextView tv10 = (TextView) findViewById(R.id.tv10);
        TextView tv11 = (TextView) findViewById(R.id.tv11);
        TextView tv12 = (TextView) findViewById(R.id.tv12);
        TextView tv13 = (TextView) findViewById(R.id.tv13);
        TextView tv14 = (TextView) findViewById(R.id.tv14);
        TextView tv15 = (TextView) findViewById(R.id.tv15);
        TextView tv16 = (TextView) findViewById(R.id.tv16);
        TextView tv17 = (TextView) findViewById(R.id.tv17);
        TextView tv18 = (TextView) findViewById(R.id.tv18);
        TextView tv20 = (TextView) findViewById(R.id.tv20);
        TextView tv21 = (TextView) findViewById(R.id.tv21);
        tv_event_start_date = (TextView) findViewById(R.id.tv_event_start_date);
        TextView tv_file_name = (TextView) findViewById(R.id.tv_file_name);
        tv_event_start_time = (TextView) findViewById(R.id.tv_event_start_time);
        tv_event_end_date = (TextView) findViewById(R.id.tv_event_end_date);
        tv_event_end_time = (TextView) findViewById(R.id.tv_event_end_time);
        tv_select_person = (TextView) findViewById(R.id.tv_select_person);

        et_event_title = (EditText) findViewById(R.id.et_event_title);
        et_validate_days = (EditText) findViewById(R.id.et_validate_days);
        et_address = (EditText) findViewById(R.id.et_address);
        et_event_des = (EditText) findViewById(R.id.et_event_des);

        spiner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city_id = city_List.get(i).cityId;
                getRegion(city_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spiner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                region_id = region_List.get(i).regionName;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (radioSexButton.getText().toString().equals("Public")) {
        }

        if (radioSexButton.getText().toString().equals("Private")) {
        }

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                public_or_private = "P";
                ll_private_or_oublic.setVisibility(View.GONE);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                public_or_private = "G";
                ll_private_or_oublic.setVisibility(View.VISIBLE);
            }
        });

        fl_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        rv_select_visible_department.setLayoutManager(flowLayoutManager);

        FlowLayoutManager flowLayoutManager2 = new FlowLayoutManager();
        flowLayoutManager2.setAutoMeasureEnabled(true);
        rv_select_visible_branch.setLayoutManager(flowLayoutManager2);

        FlowLayoutManager flowLayoutManager3 = new FlowLayoutManager();
        flowLayoutManager3.setAutoMeasureEnabled(true);
        rv_select_person.setLayoutManager(flowLayoutManager3);

        et_event_title.addTextChangedListener(new MyTextWatcher(et_event_title));
        et_validate_days.addTextChangedListener(new MyTextWatcher(et_validate_days));
        et_address.addTextChangedListener(new MyTextWatcher(et_address));
        et_event_des.addTextChangedListener(new MyTextWatcher(et_event_des));

        tv_event_start_date.setTypeface(Utility.getTypeFace());
        tv_event_start_time.setTypeface(Utility.getTypeFace());
        tv_event_end_date.setTypeface(Utility.getTypeFace());
        tv_event_end_time.setTypeface(Utility.getTypeFace());
        tv_select_person.setTypeface(Utility.getTypeFace());
        //  tv_file_name.setTypeface(Utility.getTypeFace());
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
        //  tv13.setTypeface(Utility.getTypeFace());
        tv14.setTypeface(Utility.getTypeFace());
        tv15.setTypeface(Utility.getTypeFace());
        //tv19.setTypeface(Utility.getTypeFaceTab());
        tv20.setTypeface(Utility.getTypeFaceTab());
        tv21.setTypeface(Utility.getTypeFaceTab());
        tv16.setTypeface(Utility.getTypeFaceTab());
        tv17.setTypeface(Utility.getTypeFaceTab());
        tv18.setTypeface(Utility.getTypeFaceTab());

        et_event_title.setTypeface(Utility.getTypeFace());
        et_validate_days.setTypeface(Utility.getTypeFace());
        et_address.setTypeface(Utility.getTypeFace());
        et_event_des.setTypeface(Utility.getTypeFace());

        radioButton.setTypeface(Utility.getTypeFace());
        radioButton2.setTypeface(Utility.getTypeFace());

        LinearLayout lnmainback = (LinearLayout) findViewById(R.id.lnmainback);
        LinearLayout ll_select_department = (LinearLayout) findViewById(R.id.ll_select_department);
        LinearLayout ll_select_branch = (LinearLayout) findViewById(R.id.ll_select_branch);
        LinearLayout ll_select_person = (LinearLayout) findViewById(R.id.ll_select_person);

        RelativeLayout rl_event_start_date = (RelativeLayout) findViewById(R.id.rl_event_start_date);
        RelativeLayout rl_event_start_time = (RelativeLayout) findViewById(R.id.rl_event_start_time);
        RelativeLayout rl_event_end_date = (RelativeLayout) findViewById(R.id.rl_event_end_date);
        RelativeLayout rl_event_end_time = (RelativeLayout) findViewById(R.id.rl_event_end_time);

        rl_event_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventStartDate();
            }
        });
        rl_event_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //time picker
                eventStartTime();
            }
        });
        rl_event_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventEndDate();
            }
        });
        rl_event_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventEndTime();
            }
        });
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_select_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagPopup();

            }
        });
        ll_select_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagPopupBranch();
            }
        });
        ll_select_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagPopuppeople();
            }
        });
        ll_save_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_or_update_event();
            }
        });
        ll_create_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_or_update_event();
            }
        });

        getEventAddBindData();
        if (!add_or_edit.equalsIgnoreCase("add")) {
            getEventEditData();
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

    private void create_or_update_event() {
        for (int i = 0; i < item_list2.size(); i++) {
            branchName += item_list2.get(i).getName() + ",";
        }
        for (int i = 0; i < item_list.size(); i++) {
            departName += item_list.get(i).getName() + ",";
        }
        for (int i = 0; i < item_list3.size(); i++) {
            peopleName += item_list3.get(i).getName() + ",";
        }
        if (branchName.length() > 0) {
            bname = branchName.substring(0, branchName.length() - 1);
        }
        if (departName.length() > 0) {
            dname = departName.substring(0, departName.length() - 1);
        }
        if (peopleName.length() > 0) {
            pname = peopleName.substring(0, peopleName.length() - 1);
        }

        if (public_or_private.equalsIgnoreCase("P")) {
            if (add_or_edit.equalsIgnoreCase("add")) {
                validatePublic();
            } else {
                validatePublicEdit(eventId);
            }
        } else {
            if (add_or_edit.equalsIgnoreCase("add")) {
                validatePrivaate();
            } else {
                validatePrivaateEdit(eventId);
            }
        }
    }

    private void validatePrivaateEdit(final String event_id) {
        String departmentid_list = Utility.getDepartmentIdPreference();
        for (int i = 0; i < item_list.size(); i++) {
            departmentid_list += item_list.get(i).getId() + ",";
        }

        String did = "";
        if (departmentid_list.length() > 0) {
            did = departmentid_list.substring(0, departmentid_list.length() - 1);
        }

        String branchid_list = Utility.getBranchIdPreference();
        for (int i = 0; i < item_list2.size(); i++) {
            branchid_list += item_list2.get(i).getId() + ",";
        }

        String bid = "";
        if (branchid_list.length() > 0) {
            bid = branchid_list.substring(0, branchid_list.length() - 1);
        }

        Log.e("size", "" + item_list3.size());

        String peopleid_list = "";
        for (int i = 0; i < item_list3.size(); i++) {
            peopleid_list += item_list3.get(i).getId() + ",";
        }

        String pid = "";
        if (peopleid_list.length() > 0) {
            pid = peopleid_list.substring(0, peopleid_list.length() - 1);
        }
        String peoplename_list = "";
        for (int i = 0; i < item_list3.size(); i++) {
            peoplename_list += item_list3.get(i).getName() + ",";
        }

        String pname = "";
        if (peoplename_list.length() > 0) {
            pname = peoplename_list.substring(0, peoplename_list.length() - 1);
        }
        Log.e("did", "" + did);
        Log.e("bid", "" + bid);
        Log.e("pid", "" + pid);
        Log.e("pname", "" + pname);
        String valid_date = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date myDate = dateFormat.parse(tv_event_start_date.getText().toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            if (!et_validate_days.getText().toString().equalsIgnoreCase("")) {
                int day = Integer.parseInt(et_validate_days.getText().toString()) + 1;
                calendar.add(Calendar.DAY_OF_YEAR, -day);
                Date newDate = calendar.getTime();
                valid_date = dateFormat.format(newDate);
                Log.e("validate", "" + valid_date);
            } else {
              /*  et_validate_days.setError("Please Write Event Validity");
                requestFocus(et_validate_days);*/
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (checkbox_evet_go_or_not.isChecked()) {
            evet_go_or_not = "Y";
        } else {
            evet_go_or_not = "N";
        }
        Log.e(imgString, imgString);

        if (et_event_title.getText().toString().trim().isEmpty()) {
            et_event_title.setError("Please Write Event Title");

        } else if (tv_event_start_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event start date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_start_time.getText().toString().equalsIgnoreCase("hh:mm")) {
            Toast.makeText(activity, "Please Select event start time", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event end date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_time.getText().toString().equalsIgnoreCase("hh:mm")) {
            Toast.makeText(activity, "Please Select event end time", Toast.LENGTH_SHORT).show();
        } else if (et_validate_days.getText().toString().trim().isEmpty()) {
            et_validate_days.setError("Please Write Event Validity");
        } else if (dname.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select Department", Toast.LENGTH_SHORT).show();
        } else if (bname.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select Branch", Toast.LENGTH_SHORT).show();
        } else if (pname.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select People", Toast.LENGTH_SHORT).show();
        } else if (state_id.equalsIgnoreCase("0")) {
            Toast.makeText(activity, "Please Select State", Toast.LENGTH_SHORT).show();
        } else if (city_id.equalsIgnoreCase("0")) {
            Toast.makeText(activity, "Please Select City", Toast.LENGTH_SHORT).show();
        } else if (et_event_des.getText().toString().trim().isEmpty()) {
            et_event_des.setError("Please Write Event Title Description");
        } else if (et_address.getText().toString().trim().isEmpty()) {
            et_address.setError("Please Enter Address");
        } else {
            UpdateEvents(event_id, et_event_title.getText().toString(),
                    tv_event_start_date.getText().toString(),
                    tv_event_start_time.getText().toString(),
                    tv_event_end_date.getText().toString(),
                    tv_event_end_time.getText().toString(),
                    et_validate_days.getText().toString(),
                    "P",
                    state_id,
                    city_id,
                    region_id,
                    et_address.getText().toString(),
                    et_event_des.getText().toString(), did, bid, pid,
                    pname, valid_date
            );
        }
    }

    private void validatePublicEdit(final String event_id) {


        String departName = "";
        String branchName = "";
        String bname = "";
        String dname = "";

        if (item_list2.size() > 0) {
            for (int i = 0; i < item_list2.size(); i++) {
                branchName += item_list2.get(i).getId() + ",";
            }
        } else {
            for (int i = 0; i < branch_List.size(); i++) {
                branchName += branch_List.get(i).branchId + ",";
            }
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size() - 1; i++) {
                departName += item_list.get(i).getId() + ",";
            }
        } else {
            for (int i = 0; i < department_List.size() - 1; i++) {
                departName += department_List.get(i).departmentId + ",";
            }
        }

        if (branchName.length() > 0) {
            bname = branchName.substring(0, branchName.length() - 1);
        }
        if (departName.length() > 0) {
            dname = departName.substring(0, departName.length() - 1);
        }

        Log.e("branch", "" + bname);
        Log.e("department", "" + dname);

        String valid_date = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date myDate = dateFormat.parse(tv_event_start_date.getText().toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            if (!et_validate_days.getText().toString().equalsIgnoreCase("")) {
                int day = Integer.parseInt(et_validate_days.getText().toString()) + 1;
                calendar.add(Calendar.DAY_OF_YEAR, -day);
                Date newDate = calendar.getTime();
                valid_date = dateFormat.format(newDate);
                Log.e("validate", "" + valid_date);
            } else {
              /*  et_validate_days.setError("Please Write Event Validity");
                requestFocus(et_validate_days);*/
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (checkbox_evet_go_or_not.isChecked()) {
            evet_go_or_not = "Y";
        } else {
            evet_go_or_not = "N";
        }

        if (et_event_title.getText().toString().trim().isEmpty()) {
            et_event_title.setError("Please Write Event Title");
        } else if (tv_event_start_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event start date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_start_time.getText().toString().equalsIgnoreCase("hh:mm")) {
            Toast.makeText(activity, "Please Select event start time", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event end date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_time.getText().toString().equalsIgnoreCase("hh:mm")) {
            Toast.makeText(activity, "Please Select event end time", Toast.LENGTH_SHORT).show();
        } else if (et_validate_days.getText().toString().trim().isEmpty()) {
            et_validate_days.setError("Please Write Event Validity");

        } else if (et_event_des.getText().toString().trim().isEmpty()) {
            et_event_des.setError("Please Write Event Title Description");
        } else if (et_address.getText().toString().trim().isEmpty()) {
            et_address.setError("Please Write Event Address");

        } else {
            UpdateEvents(event_id, et_event_title.getText().toString(),
                    tv_event_start_date.getText().toString(),
                    tv_event_start_time.getText().toString(),
                    tv_event_end_date.getText().toString(),
                    tv_event_end_time.getText().toString(),
                    et_validate_days.getText().toString(),
                    "G",
                    state_id,
                    city_id,
                    region_id,
                    et_address.getText().toString(),
                    et_event_des.getText().toString(), dname, bname, "",
                    "", valid_date

            );
        }
    }
    private void getEventEditData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Event_Edit, new Response.Listener<String>() {
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
                        if (object.has("Event_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Event_Detail_Array");
                            if (jsonArray.length() != 0) {
                                edit_List.clear();
                                edit_List.addAll((Collection<? extends BeanEventEditDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEventEditDetail>>() {
                                }.getType()));

                                final String stateid = edit_List.get(0).stateId;
                                cityid = edit_List.get(0).cityId;
                                for (int x = 0; state_List.size() > x; x++) {
                                    String inner_state_id = state_List.get(x).stateId;
                                    if (stateid.equalsIgnoreCase(inner_state_id)) {
                                        int pos = x;
                                        Log.e("xxxxxxxxx", "xxxxxxxx++" + x);
                                        spiner_state.setSelection(x);

                                        spiner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                getCity(stateid);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    }
                                }
                                et_event_title.setText(edit_List.get(0).eventTitle);
                                String start_date = null, end_date = null;
                                try {
                                    start_date = dateFormatter(edit_List.get(0).startDate);
                                    end_date = dateFormatter(edit_List.get(0).endDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (edit_List.get(0).eventType.equalsIgnoreCase("P")) {
                                    radioButton2.setChecked(true);
                                } else {
                                    radioButton.setChecked(true);
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
                                callOnBackPress2();
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
                    map.put("EventId", eventId);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    void getEventAddBindData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Event_Add_Bind_Data, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("event_bind_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if (object.has("Department_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Department_Array");
                            if (jsonArray.length() != 0) {
                                department_List.clear();
                                // item_list.clear();
                                department_List.addAll((Collection<? extends BeanDepartmentList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanDepartmentList>>() {
                                }.getType()));
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (object.has("Branch_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Branch_Array");
                            if (jsonArray.length() != 0) {
                                branch_List.clear();
                                //   item_list2.clear();
                                branch_List.addAll((Collection<? extends BeanBranchList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanBranchList>>() {
                                }.getType()));
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (object.has("State_Array")) {
                            JSONArray jsonArray = object.getJSONArray("State_Array");
                            if (jsonArray.length() != 0) {
                                state_List.clear();
                                state_List.add(new BeanStateList("0", "Select State"));
                                state_List.addAll((Collection<? extends BeanStateList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanStateList>>() {
                                }.getType()));
                                adapter_State = new AdapterState(activity, state_List);
                                adapter_State.notifyDataSetChanged();
                                spiner_state.setAdapter(adapter_State);
                                spiner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        state_id = state_List.get(i).stateId;
                                        getCity(state_id);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                    }
                                });
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


    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private String readFileAsBase64String(String path) {
        try {
            InputStream is = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Base64OutputStream b64os = new Base64OutputStream(baos, Base64.DEFAULT);
            byte[] buffer = new byte[8192];
            int bytesRead;
            try {
                while ((bytesRead = is.read(buffer)) > -1) {
                    b64os.write(buffer, 0, bytesRead);
                }
                return baos.toString();
            } catch (IOException e) {
                Log.e("can not read", "Cannot read file " + path, e);
                // Or throw if you prefer
                return "";
            } finally {
                closeQuietly(is);
                closeQuietly(b64os); // This also closes baos
            }
        } catch (FileNotFoundException e) {
            Log.e("file not found", "File not found " + path, e);
            // Or throw if you prefer
            return "";
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    //region Select Gallery or Camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
                Uri FilePath = data.getData();
                String path = null;
                try {
                    path = getFilePath(activity, FilePath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                String file = path.substring(path.lastIndexOf("/") + 1);

                image_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                Log.e("path: ", "" + path);
                Log.e("filename: ", "" + file);
                Log.e("extension: ", "" + image_extension);

                image_name = file.substring(0, file.lastIndexOf('.'));

                Log.e("log", "64+++++++++++++++" + readFileAsBase64String(path));
                imageBase64 = readFileAsBase64String(path);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
                Uri FilePath = data.getData();
                String path = null;
                try {
                    path = getFilePath(activity, FilePath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                String file = path.substring(path.lastIndexOf("/") + 1);
                image_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                Log.e("path: ", "" + path);
                Log.e("filename: ", "" + file);
                Log.e("extension: ", "" + image_extension);

                image_name = file.substring(0, file.lastIndexOf('.'));

                Log.e("log", "64+++++++++++++++" + readFileAsBase64String(path));
                imageBase64 = readFileAsBase64String(path);
            } else if (requestCode == PICKFILE_RESULT_CODE) {
                Uri FilePath = data.getData();
                String path = null;
                try {
                    path = getFilePath(activity, FilePath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                String file = path.substring(path.lastIndexOf("/") + 1);
                tv_file_name.setText(file);
                file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                Log.e("path: ", "" + path);
                Log.e("filename: ", "" + file);
                Log.e("extension: ", "" + file_extension);

                filename = file.substring(0, file.lastIndexOf('.'));

                Log.e("log", "64+++++++++++++++" + readFileAsBase64String(path));
                fileBase64 = readFileAsBase64String(path);
            }
        }
    }
    //endregion

    //region Gallery
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
            } catch (IOException e) {
                pd.dismiss();
                showMsg(R.string.json_error);
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] imageBytes = bytes.toByteArray();
        profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        File destination = new File(Environment.getExternalStorageDirectory() + "/Intranet/UserProfile");

        if (!destination.exists()) {
            File wallpaperDirectory = new File("/sdcard/Intranet/UserProfile");
            wallpaperDirectory.mkdirs();
        }

        imagename = "intranet" + System.currentTimeMillis();
        File file = new File(new File("/sdcard/Intranet/UserProfile"), imagename);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv_user_profile.setImageBitmap(bm);

    }

    //endregion
    //region Camera_
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] imageBytes = bytes.toByteArray();
        profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        File destination = new File(Environment.getExternalStorageDirectory() + "/Intranet/UserProfile");

        if (!destination.exists()) {
            File wallpaperDirectory = new File("/sdcard/Intranet/UserProfile");
            wallpaperDirectory.mkdirs();
        }

        imagename = "intranet" + System.currentTimeMillis();
        File file = new File(new File("/sdcard/Intranet/UserProfile"), imagename);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv_user_profile.setImageBitmap(thumbnail);
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
                        } else if (date1.after(date2)) {
                            tv_event_end_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        } else {
                            Toast.makeText(activity2, "You can not select past date", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(activity2, "You can not select past date", Toast.LENGTH_SHORT).show();
                        }
                        // tv_event_start_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void validatePrivaate() {

        String departmentid_list = Utility.getDepartmentIdPreference();
        for (int i = 0; i < item_list.size(); i++) {
            departmentid_list += item_list.get(i).getId() + ",";
        }

        String did = "";
        if (departmentid_list.length() > 0) {
            did = departmentid_list.substring(0, departmentid_list.length() - 1);
        }

        String branchid_list = Utility.getBranchIdPreference();
        for (int i = 0; i < item_list2.size(); i++) {
            branchid_list += item_list2.get(i).getId() + ",";
        }

        String bid = "";
        if (branchid_list.length() > 0) {
            bid = branchid_list.substring(0, branchid_list.length() - 1);
        }

        Log.e("size", "" + item_list3.size());

        String peopleid_list = "";
        for (int i = 0; i < item_list3.size(); i++) {
            peopleid_list += item_list3.get(i).getId() + ",";
        }

        String pid = "";
        if (peopleid_list.length() > 0) {
            pid = peopleid_list.substring(0, peopleid_list.length() - 1);
        }
        String peoplename_list = "";
        for (int i = 0; i < item_list3.size(); i++) {
            peoplename_list += item_list3.get(i).getName() + ",";
        }

        String pname = "";
        if (peoplename_list.length() > 0) {
            pname = peoplename_list.substring(0, peoplename_list.length() - 1);
        }
        Log.e("did", "" + did);
        Log.e("bid", "" + bid);
        Log.e("pid", "" + pid);
        Log.e("pname", "" + pname);
        String valid_date = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date myDate = dateFormat.parse(tv_event_start_date.getText().toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            if (!et_validate_days.getText().toString().equalsIgnoreCase("")) {
                int day = Integer.parseInt(et_validate_days.getText().toString()) + 1;
                calendar.add(Calendar.DAY_OF_YEAR, -day);
                Date newDate = calendar.getTime();
                valid_date = dateFormat.format(newDate);
                Log.e("validate", "" + valid_date);
            } else {
              /*  et_validate_days.setError("Please Write Event Validity");
                requestFocus(et_validate_days);*/
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (checkbox_evet_go_or_not.isChecked()) {
            evet_go_or_not = "Y";
        } else {
            evet_go_or_not = "N";
        }

        if (et_event_title.getText().toString().trim().isEmpty()) {
            et_event_title.setError("Please Write Event Title");

        } else if (tv_event_start_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event start date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_start_time.getText().toString().equalsIgnoreCase("hh:mm")) {
            Toast.makeText(activity, "Please Select event start time", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event end date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_time.getText().toString().equalsIgnoreCase("hh:mm")) {
            Toast.makeText(activity, "Please Select event end time", Toast.LENGTH_SHORT).show();
        } else if (et_validate_days.getText().toString().trim().isEmpty()) {
            et_validate_days.setError("Please Write Event Validity");

        } else if (dname.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select Department", Toast.LENGTH_SHORT).show();
        } else if (bname.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select Branch", Toast.LENGTH_SHORT).show();
        } else if (pname.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select People", Toast.LENGTH_SHORT).show();
        } else if (state_id.equalsIgnoreCase("0")) {
            Toast.makeText(activity, "Please Select State", Toast.LENGTH_SHORT).show();
        } else if (city_id.equalsIgnoreCase("0")) {
            Toast.makeText(activity, "Please Select City", Toast.LENGTH_SHORT).show();
        } /*else if (region_id.equalsIgnoreCase("0")) {
            Toast.makeText(activity, "Please Select Region", Toast.LENGTH_SHORT).show();

        }*/ else if (et_event_des.getText().toString().trim().isEmpty()) {
            et_event_des.setError("Please Write Event Title Description");
           /* else if (fileBase64.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please select file", Toast.LENGTH_SHORT).show();
        } */
        } else if (et_address.getText().toString().trim().isEmpty()) {
            et_address.setError("Please Write Event Address");


        } else {
            CreateEvents(et_event_title.getText().toString(),
                    tv_event_start_date.getText().toString(),
                    tv_event_start_time.getText().toString(),
                    tv_event_end_date.getText().toString(),
                    tv_event_end_time.getText().toString(),
                    et_validate_days.getText().toString(),
                    "P",
                    state_id,
                    city_id,
                    region_id,
                    et_address.getText().toString(),
                    et_event_des.getText().toString(), did, bid, pid,
                    pname, valid_date
            );
        }
    }

    private void validatePublic() {
        String departName = "";
        String branchName = "";
        String bname = "";
        String dname = "";

        if (item_list2.size() > 0) {
            for (int i = 0; i < item_list2.size(); i++) {
                branchName += item_list2.get(i).getId() + ",";
            }
        } else {
            for (int i = 0; i < branch_List.size(); i++) {
                branchName += branch_List.get(i).branchId + ",";
            }
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size() - 1; i++) {
                departName += item_list.get(i).getId() + ",";
            }
        } else {
            for (int i = 0; i < department_List.size() - 1; i++) {
                departName += department_List.get(i).departmentId + ",";
            }
        }

        if (branchName.length() > 0) {
            bname = branchName.substring(0, branchName.length() - 1);
        }
        if (departName.length() > 0) {
            dname = departName.substring(0, departName.length() - 1);
        }

        Log.e("branch", "" + bname);
        Log.e("department", "" + dname);


        String valid_date = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date myDate = dateFormat.parse(tv_event_start_date.getText().toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            if (!et_validate_days.getText().toString().equalsIgnoreCase("")) {
                int day = Integer.parseInt(et_validate_days.getText().toString()) + 1;
                calendar.add(Calendar.DAY_OF_YEAR, -day);
                Date newDate = calendar.getTime();
                valid_date = dateFormat.format(newDate);
                Log.e("validate", "" + valid_date);
            } else {
               /* et_validate_days.setError("Please Write Event Validity");
                requestFocus(et_validate_days);*/
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (checkbox_evet_go_or_not.isChecked()) {
            evet_go_or_not = "Y";
        } else {
            evet_go_or_not = "N";
        }
        if (et_event_title.getText().toString().trim().isEmpty()) {
            et_event_title.setError("Please Write Event Title");
        } else if (tv_event_start_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event start date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_start_time.getText().toString().equalsIgnoreCase("hh:mm")) {
            Toast.makeText(activity, "Please Select event start time", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event end date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_time.getText().toString().equalsIgnoreCase("hh:mm")) {
            Toast.makeText(activity, "Please Select event end time", Toast.LENGTH_SHORT).show();
        } else if (et_validate_days.getText().toString().trim().isEmpty()) {
            et_validate_days.setError("Please Write Event Validity");

        } else if (et_event_des.getText().toString().trim().isEmpty()) {
            et_event_des.setError("Please Write Event Title Description");

        } else if (et_address.getText().toString().trim().isEmpty()) {
            et_address.setError("Please Write Event Address");

        }/*else if (fileBase64.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please select file", Toast.LENGTH_SHORT).show();
        }*/ else {
            CreateEvents(et_event_title.getText().toString(),
                    tv_event_start_date.getText().toString(),
                    tv_event_start_time.getText().toString(),
                    tv_event_end_date.getText().toString(),
                    tv_event_end_time.getText().toString(),
                    et_validate_days.getText().toString(),
                    "G",
                    state_id,
                    city_id,
                    region_id,
                    et_address.getText().toString(),
                    et_event_des.getText().toString(), dname, bname, "",
                    "", valid_date

            );
        }
    }

    private void initMap() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng latLng = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.addMarker(new MarkerOptions().position(latLng).title("Event Place" +
                ""));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_lat_long:
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(et_address.getText().toString(), getApplicationContext(), new GeocoderHandler());
                break;
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message message) {
            String locationAddress;
            Log.e("message", "" + message);
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            initMap();
            // LoadMap();
            Log.e("address", "" + locationAddress.toString());
            //  latLongTV.setText(locationAddress);
        }
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

                case R.id.et_validate_days:
                    validatedays();

                    break;

                case R.id.et_address:
                    validateAddress();
                    break;

                case R.id.et_event_des:
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

    private boolean validatedays() {
        String email = et_validate_days.getText().toString().trim();
        if (email.isEmpty()) {
            et_validate_days.setError("Please Write Event Validity");
            requestFocus(et_validate_days);
            return false;
        } else {
            et_validate_days.setError(null);
        }
        return true;
    }

    private boolean validateAddress() {
        String email = et_address.getText().toString().trim();
        if (email.isEmpty()) {
            et_address.setError("Please Write Event Address");
            requestFocus(et_address);
            return false;
        } else {
            et_address.setError(null);
        }
        return true;
    }

    private boolean validate_des() {
        String email = et_event_des.getText().toString().trim();
        if (email.isEmpty()) {
            et_event_des.setError("Please Write Event Description");
            requestFocus(et_event_des);
            return false;
        } else {
            et_event_des.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                    tv_select_person.setVisibility(View.GONE);
                    rv_select_person.setVisibility(View.VISIBLE);
                } else {
                    tv_select_person.setVisibility(View.VISIBLE);
                    rv_select_person.setVisibility(View.GONE);
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

        adapter1people = new Adapter1MyEventsPeopleList(activity2, contact_list);
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

    private void UpdateEvents(final String event_id, final String event_title,
                              final String event_start_date,
                              final String event_start_time,
                              final String event_end_date,
                              final String event_end_time,
                              final String validate_days,
                              final String private_or_public,
                              final String state_id,
                              final String city_id,
                              final String region_id,
                              final String event_address,
                              final String event_event_des, final String did, final String bid, final String pid,
                              final String pname, final String valid_date) {

        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Event_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("eve_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Event_Update")) {
                            JSONArray array = object.getJSONArray("Event_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        EventsActivity.getEventsData();
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
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
                            finish();
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
                    Log.e("EventId", event_id);
                    Log.e("Path", imageBase64);
                    Log.e("FileName", image_name);
                    Log.e("FileExten", image_extension);
                    Log.e("EventTitle", event_title);
                    Log.e("StartDate", event_start_date);
                    Log.e("EndDate", event_end_date);
                    Log.e("StartTime", event_start_time);
                    Log.e("EndTime", event_end_time);
                    Log.e("Address", event_address);
                    Log.e("EventType", private_or_public);
                    Log.e("DepartmentsList", did);
                    Log.e("BranchesList", bid);
                    Log.e("InvitedIds", pid);
                    Log.e("InvitedNames", pname);
                    Log.e("RegionsList", city_id);
                    Log.e("Description", event_event_des);
                    Log.e("GoingFlag", evet_go_or_not);
                    Log.e("LoginId", Utility.getPeopleIdPreference());
                    Log.e("Latitude", String.valueOf(latitude));
                    Log.e("Longitude", String.valueOf(longitude));
                    Log.e("ValidDay", validate_days);
                    Log.e("ValidaDate", valid_date);


                    map.put("EventId", event_id);
                    map.put("Path", imageBase64);
                    map.put("FileName", image_name);
                    map.put("FileExten", image_extension);
                    map.put("EventTitle", event_title);
                    map.put("StartDate", event_start_date);
                    map.put("EndDate", event_end_date);
                    map.put("StartTime", event_start_time);
                    map.put("EndTime", event_end_time);
                    map.put("Address", event_address);
                    map.put("EventType", private_or_public);
                    map.put("DepartmentsList", did);
                    map.put("BranchesList", bid);
                    map.put("InvitedIds", pid);
                    map.put("InvitedNames", pname);
                    map.put("RegionsList", region_id);
                    map.put("Description", event_event_des);
                    map.put("GoingFlag", evet_go_or_not);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Latitude", String.valueOf(latitude));
                    map.put("Longitude", String.valueOf(longitude));
                    map.put("ValidDay", validate_days);
                    map.put("ValidaDate", valid_date);

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

    private void CreateEvents(final String event_title,
                              final String event_start_date,
                              final String event_start_time,
                              final String event_end_date,
                              final String event_end_time,
                              final String validate_days,
                              final String private_or_public,
                              String state_id,
                              final String city_id,
                              final String region_id,
                              final String event_address,
                              final String event_event_des, final String did, final String bid, final String pid,
                              final String pname, final String valid_date) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Event_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("eve_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Event_Add")) {
                            JSONArray array = object.getJSONArray("Event_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        EventsActivity.getEventsData();
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
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
                            finish();
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

                    map.put("Path", imageBase64);
                    map.put("FileName", image_name);
                    map.put("FileExten", image_extension);
                    map.put("EventTitle", event_title);
                    map.put("StartDate", event_start_date);
                    map.put("EndDate", event_end_date);
                    map.put("StartTime", event_start_time);
                    map.put("EndTime", event_end_time);
                    map.put("Address", event_address);
                    map.put("EventType", private_or_public);
                    map.put("DepartmentsList", did);
                    map.put("BranchesList", bid);
                    map.put("InvitedIds", pid);
                    map.put("InvitedNames", pname);
                    map.put("RegionsId", city_id);
                    map.put("Description", event_event_des);
                    map.put("GoingFlag", evet_go_or_not);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Latitude", String.valueOf(latitude));
                    map.put("Longitude", String.valueOf(longitude));

                   /* map.put("Latitude", String.valueOf(latitude));
                    map.put("Longitude", String.valueOf(longitude));*/

                    map.put("ValidDay", validate_days);
                    map.put("ValidaDate", valid_date);

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

    void getRegion(final String city_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Region_Select_By_CityId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("region_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Region_Array")) {
                            region_List.add(new BeanRegionList("0", "Select Region"));
                            JSONArray jsonArray = object.getJSONArray("Region_Array");
                            if (jsonArray.length() != 0) {
                                region_List.clear();
                                region_List.add(new BeanRegionList("0", "Select Region"));
                                region_List.addAll((Collection<? extends BeanRegionList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanRegionList>>() {
                                }.getType()));
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                            adapter_region = new AdapterRegion(activity, region_List);
                            adapter_region.notifyDataSetChanged();
                            spiner_region.setAdapter(adapter_region);
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
                    Log.e("region_error", error.toString());
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
                    map.put("Hashkey", "register");
                    map.put("CityId", city_id);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    void getCity(final String state_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.City_Select_By_StateId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("state_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("City_Array")) {
                            city_List.add(new BeanCityList("0", "Select City"));
                            JSONArray jsonArray = object.getJSONArray("City_Array");
                            city_List.clear();
                            city_List.add(new BeanCityList("0", "Select City"));
                            if (jsonArray.length() != 0) {
                                city_List.addAll((Collection<? extends BeanCityList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanCityList>>() {
                                }.getType()));
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                            adapter_ciy = new AdapterCity(activity, city_List);
                            adapter_ciy.notifyDataSetChanged();
                            spiner_city.setAdapter(adapter_ciy);

                            for (int x = 0; city_List.size() > x; x++) {
                                String inner_state_id = city_List.get(x).cityId;
                                if (cityid.equalsIgnoreCase(inner_state_id)) {
                                    int pos = x;
                                    Log.e("xxxxxxxxx", "xxxxxxxx++" + x);
                                    spiner_city.setSelection(x);
                                    spiner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            city_id = city_List.get(i).cityId;
                                            //  getRegion(city_id);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    });

                                   /*spiner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            getCity(stateid);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    });*/
                                }
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
                    Log.e("dep_list_error", error.toString());
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
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Hashkey", "register");
                    map.put("StateId", state_id);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    static void openTagPopup() {

        openTagDialog();

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

        adapter1 = new Adapter1MyEvents(activity2, department_List);
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

    public static void callOnBackPress() {
        recyclerview_adapter = new Adapter2MyEvents(activity2, item_list);
        rv_onchangelistner();
    }

    public static void callOnBackPress2() {
        recyclerview_adapter2 = new Adapter2BranchMyEvents(activity2, item_list2);
        rv_onchangelistner2();

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

        if (is_first_time.equalsIgnoreCase("yes")) {

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

        if (is_first_time.equalsIgnoreCase("yes")) {

        } else {
            recyclerview_adapter2.notifyDataSetChanged();
            recyclerview4.setAdapter(recyclerview_adapter2);
            recyclerview_adapter2.notifyDataSetChanged();
        }

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

        if (is_first_time.equalsIgnoreCase("yes")) {

        } else {
            recyclerview_adapter3.notifyDataSetChanged();
            recyclerview6.setAdapter(recyclerview_adapter3);
            recyclerview_adapter3.notifyDataSetChanged();
        }
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
        final TextView iv_done = (TextView) open_tag_dialog.findViewById(R.id.iv_done);

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
                if (item_list2.size() > 0) {
                    tv_select_visible_branch.setVisibility(View.GONE);
                    rv_select_visible_branch.setVisibility(View.VISIBLE);
                } else {
                    tv_select_visible_branch.setVisibility(View.VISIBLE);
                    rv_select_visible_branch.setVisibility(View.GONE);
                }
                callOnBackPress2();
                String branchid_list = "";
                for (int i = 0; i < item_list2.size(); i++) {
                    branchid_list += item_list2.get(i).getId() + "|";
                }

                String bid = "";
                if (branchid_list.length() > 0) {
                    bid = branchid_list.substring(0, branchid_list.length() - 1);
                }
                getPeopleData(bid);

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

        adapter1branch = new Adapter1BranchMyEvents(activity2, branch_List);
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

    public static void getPeopleData(final String bid) {

        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Event_Person_Select_By_BranchId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res_people", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {

                            Toast.makeText(activity2, "Please Try again", Toast.LENGTH_SHORT).show();
                            activity2.finish();
                        }
                        if (object.has("Event_Persons_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Event_Persons_Array");
                            if (jsonArray.length() != 0) {
                                contact_list.clear();
                                contact_list.addAll((Collection<? extends BeanEventSelectAllPeople>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEventSelectAllPeople>>() {
                                }.getType()));

                            } else {
                            }
                        }
                    } catch (JSONException e) {

                        showMsg(R.string.json_error);
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("res_error", error.toString());

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

                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("BranchId", bid);
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {

            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

}
