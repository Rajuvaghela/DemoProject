package com.ext.adarsh.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import com.ext.adarsh.Adapter.Adapter1AnnouncementEdit;
import com.ext.adarsh.Adapter.Adapter1BranchAnnouncement;
import com.ext.adarsh.Adapter.Adapter2Announcement;
import com.ext.adarsh.Adapter.Adapter2BranchAnnouncement;
import com.ext.adarsh.Adapter.AdapterAnnouncementReferenceList;
import com.ext.adarsh.Bean.BeanAnnouncementEdit;
import com.ext.adarsh.Bean.BeanAnnouncementEditAttachment;
import com.ext.adarsh.Bean.BeanAnnouncementReferenceList;
import com.ext.adarsh.Bean.BeanBranchList;
import com.ext.adarsh.Bean.BeanDepartmentList;
import com.ext.adarsh.Bean.ModelClass;
import com.ext.adarsh.Bean.ModelClass2;
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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class EditAnnouncementActivity extends AppCompatActivity implements View.OnClickListener {


    Activity activity;

    ProgressDialog pd;
    static ProgressDialog pd2;

    @BindView(R.id.et_announcement_title)
    EditText et_announcement_title;

    @BindView(R.id.et_announcement_detail)
    EditText et_announcement_detail;

    static TextView tv_select_visible_department;
    static TextView tv_select_visible_branch;

    @BindView(R.id.tv_announcement_date)
    TextView tv_announcement_date;

    @BindView(R.id.tv_late_publish_date)
    TextView tv_late_publish_date;

    @BindView(R.id.tv_file_name)
    TextView tv_file_name;

    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.tv4)
    TextView tv4;

    @BindView(R.id.tv5)
    TextView tv5;

    @BindView(R.id.tv6)
    TextView tv6;

    @BindView(R.id.tv7)
    TextView tv7;

    @BindView(R.id.tv8)
    TextView tv8;

    @BindView(R.id.tv9)
    TextView tv9;


    @BindView(R.id.tv11)
    TextView tv11;

    @BindView(R.id.tv12)
    TextView tv12;

    @BindView(R.id.rl_date1)
    RelativeLayout rl_date1;

    @BindView(R.id.rl_date2)
    RelativeLayout rl_date2;

    @BindView(R.id.rl_select_file)
    RelativeLayout rl_select_file;

    @BindView(R.id.ll_post_announcement)
    LinearLayout ll_post_announcement;

    @BindView(R.id.drawericon)
    LinearLayout drawericon;

    @BindView(R.id.ll_select_department)
    LinearLayout ll_select_department;

    @BindView(R.id.ll_select_branch)
    LinearLayout ll_select_branch;

    static RecyclerView rv_select_visible_branch;


    static RecyclerView rv_select_visible_department;

    @BindView(R.id.spiner_reference_to_post)
    Spinner spiner_reference_to_post;

    static Activity activity2;
    static Dialog open_tag_dialog;

    static RecyclerView recyclerview1;
    static RecyclerView recyclerview2;
    static RecyclerView recyclerview3;
    static RecyclerView recyclerview4;
    static RecyclerView.LayoutManager recylerViewLayoutManager;
    static RecyclerView.LayoutManager recylerViewLayoutManager2;
    public static RecyclerView.Adapter recyclerview_adapter;
    public static RecyclerView.Adapter recyclerview_adapter2;
    public static List<ModelClass> item_list = new ArrayList<>();
    public static List<ModelClass2> item_list2 = new ArrayList<>();

    static Adapter1AnnouncementEdit adapter1;
    static Adapter1BranchAnnouncement adapter1branch;

    public static List<BeanDepartmentList> department_List = new ArrayList<>();
    public static List<BeanBranchList> branch_List = new ArrayList<>();
    public List<BeanAnnouncementReferenceList> Reference_list = new ArrayList<>();
    List<BeanAnnouncementEdit> beanAnnouncementEdit1 = new ArrayList<>();
    List<BeanAnnouncementEditAttachment> beanAnnouncementEditAttachments = new ArrayList<>();
    AdapterAnnouncementReferenceList reference_to_post;
    private static final int PICKFILE_RESULT_CODE = 1;
    String departName;
    String branchName;
    String fileBase64 = null;
    String filename;
    String file_extension;
    String referenceToPostId = "0";


    String bname = "0";
    String dname = "0";
    String announcement_id;


    ArrayList<String> department_name = new ArrayList<>();
    ArrayList<String> department_id = new ArrayList<>();
    ArrayList<String> branch_name = new ArrayList<>();
    ArrayList<String> branch_id = new ArrayList<>();
    static String is_first_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_announcement);
        activity = this;
        activity2 = this;
        ButterKnife.bind(this);
        pd = Utility.getDialog(activity);
        pd2 = Utility.getDialog(activity2);

        Bundle bundle = getIntent().getExtras();
        announcement_id = bundle.getString("announcement_id");

        tv_select_visible_department = (TextView) findViewById(R.id.tv_select_visible_department);
        tv_select_visible_branch = (TextView) findViewById(R.id.tv_select_visible_branch);

        rv_select_visible_department = (RecyclerView) findViewById(R.id.rv_select_visible_department);
        rv_select_visible_branch = (RecyclerView) findViewById(R.id.rv_select_visible_branch);

        callOnCreate();

        drawericon.setOnClickListener(this);
        tv12.setOnClickListener(this);
        ll_select_department.setOnClickListener(this);
        ll_select_branch.setOnClickListener(this);
        rl_date1.setOnClickListener(this);
        rl_date2.setOnClickListener(this);
        rl_select_file.setOnClickListener(this);


        getAnnouncementReferenceList();

        spiner_reference_to_post.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                referenceToPostId = Reference_list.get(i).announcementId;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        getDataAnnouncementEdit(announcement_id);
    }

    private void getDataAnnouncementEdit(final String announcement_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Announcement_Edit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Announcement_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Announcement_Array");
                            if (jsonArray.length() != 0) {
                                beanAnnouncementEdit1.clear();
                                item_list.clear();
                                item_list2.clear();
                                beanAnnouncementEdit1.addAll((Collection<? extends BeanAnnouncementEdit>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAnnouncementEdit>>() {
                                }.getType()));
                                et_announcement_title.setText(beanAnnouncementEdit1.get(0).announcementTitle);
                                et_announcement_detail.setText(beanAnnouncementEdit1.get(0).announcementDetail);
                                tv_announcement_date.setText(beanAnnouncementEdit1.get(0).announcementDate);
                                tv_late_publish_date.setText(beanAnnouncementEdit1.get(0).schedulePublishOn);


                                String data = beanAnnouncementEdit1.get(0).departmentNameList;
                                String[] items = data.split(",");
                                for (String item : items) {
                                                           department_name.add(item);
                                    System.out.println("item = " + item);
                                }

                                String data2 = beanAnnouncementEdit1.get(0).departments;
                                String[] items2 = data2.split(",");
                                for (String item : items2) {

                                    department_id.add(item);
                                    System.out.println("item = " + item);
                                }

                                String data3 = beanAnnouncementEdit1.get(0).branchNameList;
                                String[] items3 = data3.split(",");
                                for (String item : items3) {
                                                                  branch_name.add(item);
                                    System.out.println("item = " + item);
                                }

                                String data4 = beanAnnouncementEdit1.get(0).branches;
                                String[] items4 = data4.split(",");
                                for (String item : items4) {

                                    branch_id.add(item);
                                    System.out.println("item = " + item);
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
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (object.has("Announcement_Attachments_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Announcement_Attachments_Array");
                            if (jsonArray.length() != 0) {
                                beanAnnouncementEditAttachments.clear();
                                beanAnnouncementEditAttachments.addAll((Collection<? extends BeanAnnouncementEditAttachment>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAnnouncementEditAttachment>>() {
                                }.getType()));
                                String url = beanAnnouncementEditAttachments.get(0).filePath;
                                String fileName = url.substring(url.lastIndexOf('/') + 1);
                                tv_file_name.setText(fileName);
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
                    map.put("AnnouncementId", announcement_id);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
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
                break;
        }
    }

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;

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


    static void getDepartmentData() {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Department_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("dep_list_res", response);
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
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd2.dismiss();

        }
    }

    static void getBranchData() {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Branch_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("branch_list_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
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
                    Log.e("branch_list_error", error.toString());
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
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    Log.e("hash_key", "" + Utility.getHashKeyPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd2.dismiss();

        }
    }

    private void callOnCreate() {

        et_announcement_title.addTextChangedListener(new MyTextWatcher(et_announcement_title));
        et_announcement_detail.addTextChangedListener(new MyTextWatcher(et_announcement_detail));

        et_announcement_title.setTypeface(Utility.getTypeFace());
        et_announcement_detail.setTypeface(Utility.getTypeFace());
        tv_select_visible_department.setTypeface(Utility.getTypeFace());
        tv_select_visible_branch.setTypeface(Utility.getTypeFace());
        tv_file_name.setTypeface(Utility.getTypeFace());
        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv_late_publish_date.setTypeface(Utility.getTypeFace());
        tv_announcement_date.setTypeface(Utility.getTypeFace());
        tv11.setTypeface(Utility.getTypeFaceTab());
        tv12.setTypeface(Utility.getTypeFaceTab());


        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        rv_select_visible_department.setLayoutManager(flowLayoutManager);

        FlowLayoutManager flowLayoutManager2 = new FlowLayoutManager();
        flowLayoutManager2.setAutoMeasureEnabled(true);
        rv_select_visible_branch.setLayoutManager(flowLayoutManager2);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawericon:
                finish();
                break;
            case R.id.tv12:
                finish();
                break;
            case R.id.ll_select_department:
                openTagPopup();
                break;
            case R.id.ll_select_branch:
                openTagPopupBranch();
                break;
            case R.id.rl_date1:
                rl_date1();
                break;
            case R.id.rl_date2:
                rl_date2();
                break;
            case R.id.rl_select_file:
                selectFile();
                break;
            case R.id.ll_post_announcement:
                for (int i = 0; i < branch_List.size(); i++) {
                    branchName += branch_List.get(i).branchId + ",";
                }
                for (int i = 0; i < department_List.size(); i++) {
                    departName += department_List.get(i).departmentId + ",";
                }
                bname = branchName.substring(0, branchName.length() - 1);
                dname = departName.substring(0, departName.length() - 1);

                if (et_announcement_title.getText().toString().trim().equalsIgnoreCase("")) {
                    et_announcement_title.setError("Please enter announcement title.");
                    requestFocus(et_announcement_title);
                } else if (et_announcement_detail.getText().toString().trim().equalsIgnoreCase("")) {
                    et_announcement_detail.setError("Please enter announcement detail.");
                    requestFocus(et_announcement_detail);
                } else if (referenceToPostId.equalsIgnoreCase("0")) {
                    Toast.makeText(activity, "Please select reference to post", Toast.LENGTH_SHORT).show();
                } else if (dname.equalsIgnoreCase("0")) {
                    Toast.makeText(activity, "Please select department", Toast.LENGTH_SHORT).show();
                } else if (bname.equalsIgnoreCase("0")) {
                    Toast.makeText(activity, "Please select branch", Toast.LENGTH_SHORT).show();
                } else if (tv_announcement_date.getText().toString().trim().equalsIgnoreCase("mm/dd/yyyy")) {
                    Toast.makeText(activity, "Please select announcement date", Toast.LENGTH_SHORT).show();
                    tv_announcement_date.setError("Please select date");
                } else if (tv_late_publish_date.getText().toString().trim().equalsIgnoreCase("mm/dd/yyyy")) {
                    Toast.makeText(activity, "Please select publish date", Toast.LENGTH_SHORT).show();
                    tv_late_publish_date.setError("Please select date");
                } else if (fileBase64 != null) {
                    PostAnnouncement(et_announcement_title.getText().toString(),
                            et_announcement_detail.getText().toString(),
                            tv_announcement_date.getText().toString(),
                            tv_late_publish_date.getText().toString(), dname, bname);
                } else {
                    Toast.makeText(activity, "Please select file", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    void PostAnnouncement(final String title, final String des,
                          final String announcement_date,
                          final String publish_date,
                          final String dep_name, final String bra_name
    ) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Announcement_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment1_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Announcement_Add")) {
                            JSONArray array = object.getJSONArray("Announcement_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        //   addAnnouncementAttachmentFile(array.optJSONObject(i).getString("AnnouncementId"));
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();

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
                    Log.e("coment1_erro", error.toString());
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
                    map.put("Title", title);
                    map.put("AnnouncementDetails", des);
                    map.put("ReferenceToPostId", referenceToPostId);
                    map.put("DepartmentList", dep_name);
                    map.put("AnnouncementDate", announcement_date);
                    map.put("SchedulePublishOnDate", publish_date);
                    map.put("BranchIdList", bra_name);
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

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }

    private void rl_date2() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tv_late_publish_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void rl_date1() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tv_announcement_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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

        adapter1branch = new Adapter1BranchAnnouncement(activity2, branch_List);
        recyclerview3.setAdapter(adapter1branch);

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

    public static void callOnBackPress2() {
        recyclerview_adapter2 = new Adapter2BranchAnnouncement(activity2, item_list2);
        rv_onchangelistner2();

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
/*        recyclerview_adapter2.notifyDataSetChanged();

        recyclerview4.setAdapter(recyclerview_adapter2);
        recyclerview_adapter2.notifyDataSetChanged();*/
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

        adapter1 = new Adapter1AnnouncementEdit(activity2, department_List);
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
        recyclerview_adapter = new Adapter2Announcement(activity2, item_list);
        rv_onchangelistner();

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
                case R.id.et_announcement_title:
                    validateTitle();
                    break;
                case R.id.et_announcement_detail:
                    validateDetail();
                    break;


            }
        }
    }

    private boolean validateDetail() {
        String email = et_announcement_detail.getText().toString().trim();
        if (email.isEmpty()) {
            et_announcement_detail.setError("Please enter announcement detail.");
            requestFocus(et_announcement_detail);
            return false;
        } else {
            et_announcement_detail.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private boolean validateTitle() {
        String email = et_announcement_title.getText().toString().trim();
        if (email.isEmpty()) {
            et_announcement_title.setError("Please enter announcement title.");
            requestFocus(et_announcement_title);
            return false;
        } else {
            et_announcement_title.setError(null);
        }
        return true;
    }


    void getAnnouncementReferenceList() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Announcement_List, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("dep_list_res", response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Announcement_List_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Announcement_List_Array");
                            if (jsonArray.length() != 0) {
                                Reference_list.clear();
                                Reference_list.add(new BeanAnnouncementReferenceList("0", "Select Reference To Post"));
                                Reference_list.addAll((Collection<? extends BeanAnnouncementReferenceList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAnnouncementReferenceList>>() {
                                }.getType()));

                                reference_to_post = new AdapterAnnouncementReferenceList(activity, Reference_list);
                                spiner_reference_to_post.setAdapter(reference_to_post);

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

}
