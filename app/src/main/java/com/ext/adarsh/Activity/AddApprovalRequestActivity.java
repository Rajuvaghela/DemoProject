package com.ext.adarsh.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
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
import com.ext.adarsh.Adapter.AdapterApprovalsEdit;
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

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;


public class AddApprovalRequestActivity extends AppCompatActivity implements View.OnClickListener {


    ProgressDialog pd;
    Activity activity;

    RecyclerView rv_list_of_attateched_file;
    ArrayList<ModelClass> modelClasses = new ArrayList<>();
    AdapterApprovalsAddPeopleList adapterApprovalsAddPeopleList;
    private String fullName;
    TextView tv_attachment, et_approv_from, tv_approve_from;
    EditText et_name_of_approvals_task;
    EditText et_note;
    private final int PICKFILE_RESULT_CODE = 1;
    String fileBase64 = "";
    String filename = "";
    String file_extension = "";
    private String str_people_id = "";
    private String str_people_name = "";
    //  public ArrayList<String> arrayList_file_base64 = new ArrayList<>();
    public ArrayList<String> arrayList_file_base64 = new ArrayList<>();
    public ArrayList<String> arrayList_file_name = new ArrayList<>();
    public ArrayList<String> arrayList_file_extension = new ArrayList<>();
    public ArrayList<BeanApprovalsFrom> beanApprovalsFroms_list = new ArrayList<>();
    public ArrayList<BeanApprovalsDetail> beanApprovalsDetails_list = new ArrayList<>();
    public ArrayList<BeanApprovalsAttachmentArray> beanApprovalsAttachmentArrays = new ArrayList<>();
    String add_or_edit = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_approval_request);
        activity = this;
        pd = Utility.getDialog(activity);
        Bundle bundle = getIntent().getExtras();
        add_or_edit = bundle.getString("add_or_edit");


        tv_attachment = (TextView) findViewById(R.id.tv_attachment);
        et_approv_from = (TextView) findViewById(R.id.et_approv_from);
        tv_approve_from = (TextView) findViewById(R.id.tv_approve_from);

        rv_list_of_attateched_file = (RecyclerView) findViewById(R.id.rv_list_of_attateched_file);
        rv_list_of_attateched_file.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        rv_list_of_attateched_file.setLayoutManager(lnmanager2);
        rv_list_of_attateched_file.setItemAnimator(new DefaultItemAnimator());

        LinearLayout ll_register_next1 = (LinearLayout) findViewById(R.id.ll_register_next1);
        LinearLayout ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
        LinearLayout lnmainback = (LinearLayout) findViewById(R.id.lnmainback);
        RelativeLayout rl_select_file = (RelativeLayout) findViewById(R.id.rl_select_file);
        TextView tv_name_of_approvals_task = (TextView) findViewById(R.id.tv_name_of_approvals_task);
        TextView tv_note = (TextView) findViewById(R.id.tv_note);

        TextView tv_select_attachment = (TextView) findViewById(R.id.tv_select_attachment);

        TextView tv_create = (TextView) findViewById(R.id.tv_create);
        TextView tv_submit = (TextView) findViewById(R.id.tv_submit);
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        TextView tv_reg_heading = (TextView) findViewById(R.id.tv_reg_heading);
        TextView tv_edit_heading = (TextView) findViewById(R.id.tv_edit_heading);
        et_name_of_approvals_task = (EditText) findViewById(R.id.et_name_of_approvals_task);
        et_note = (EditText) findViewById(R.id.et_note);
        et_name_of_approvals_task.addTextChangedListener(new MyTextWatcher(et_name_of_approvals_task));
        et_note.addTextChangedListener(new MyTextWatcher(et_note));


        tv_reg_heading.setTypeface(Utility.getTypeFaceTab());
        tv_edit_heading.setTypeface(Utility.getTypeFaceTab());
        tv_create.setTypeface(Utility.getTypeFaceTab());
        tv_submit.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        et_name_of_approvals_task.setTypeface(Utility.getTypeFace());
        et_note.setTypeface(Utility.getTypeFace());
        tv_name_of_approvals_task.setTypeface(Utility.getTypeFace());
        tv_note.setTypeface(Utility.getTypeFace());

        tv_attachment.setTypeface(Utility.getTypeFace());
        et_approv_from.setTypeface(Utility.getTypeFace());
        tv_approve_from.setTypeface(Utility.getTypeFace());
        tv_select_attachment.setTypeface(Utility.getTypeFace());
        Log.e("manager_name", Utility.getManagerNamePreference());
        Log.e("manager_id", Utility.getManagerIdPreference());
        et_approv_from.setText(Utility.getManagerNamePreference());

        tv_cancel.setOnClickListener(this);
        lnmainback.setOnClickListener(this);
        rl_select_file.setOnClickListener(this);

        if (add_or_edit.equalsIgnoreCase("edit")) {
            tv_edit_heading.setVisibility(View.VISIBLE);
            ll_submit.setVisibility(View.VISIBLE);
            tv_reg_heading.setVisibility(View.GONE);
            ll_register_next1.setVisibility(View.GONE);
        } else {
            tv_edit_heading.setVisibility(View.GONE);
            ll_submit.setVisibility(View.GONE);
            tv_reg_heading.setVisibility(View.VISIBLE);
            ll_register_next1.setVisibility(View.VISIBLE);
        }


        if (add_or_edit.equalsIgnoreCase("edit")) {
            getApprovalMoreData();
            // AppConstant.adapter_to_approvals = "";
        }
        tv_create.setOnClickListener(this);
        tv_submit.setOnClickListener(this);


    }

    private void selectFile() {
        Intent filePickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        filePickerIntent.setType("*/*");
        filePickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(filePickerIntent, PICKFILE_RESULT_CODE);
        startActivityForResult(filePickerIntent, PICKFILE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && null != data) {

            arrayList_file_name.clear();
            arrayList_file_extension.clear();
            arrayList_file_base64.clear();

            if (data.getData() != null) {
                Uri FilePath = data.getData();
                Log.e("onActivityResult: ", "++++++++++++++" + String.valueOf(FilePath));
                String path = null;
                try {
                    path = getFilePath(activity, FilePath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                String file = path.substring(path.lastIndexOf("/") + 1);

                file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                filename = file.substring(0, file.lastIndexOf('.'));
                arrayList_file_name.add(filename);
                arrayList_file_extension.add(file_extension);
                Log.e("onActivityResult: ", "path++++++++++++" + path);
                Log.e("log", "64+++++++++++++++" + readFileAsBase64String(path));
                arrayList_file_base64.add(readFileAsBase64String(path));
                tv_attachment.setText(String.valueOf(arrayList_file_base64.size()) + " file attached");

            } else {
                if (data != null) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Log.i("Path:", item.toString());

                        Uri uri = item.getUri();
                        Log.e("uri", "" + uri);
                        String path = null;
                        try {
                            path = getFilePath(activity, uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        String file = path.substring(path.lastIndexOf("/") + 1);

                        file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                        filename = file.substring(0, file.lastIndexOf('.'));
                        arrayList_file_name.add(filename);
                        arrayList_file_extension.add(file_extension);
                        Log.e("onActivityResult: ", "path++++++++++++" + path);
                        Log.e("log", "64+++++++++++++++" + readFileAsBase64String(path));
                        arrayList_file_base64.add(readFileAsBase64String(path));

                        tv_attachment.setText(String.valueOf(arrayList_file_base64.size()) + " file attached");
                        Log.e("size arraylist", "++++++++++++" + arrayList_file_base64.size());
                        Log.e("size arraylist", "+++++++++++++++" + arrayList_file_extension.size());


                    }
                }
            }
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

    public static String readFileAsBase64String(String path) {
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


    private void getApprovalMoreData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Request_Select_By_ApprovalId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Approvals_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Approvals_Array");
                            if (jsonArray.length() != 0) {
                                beanApprovalsDetails_list.clear();
                                beanApprovalsDetails_list.addAll((Collection<? extends BeanApprovalsDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanApprovalsDetail>>() {
                                }.getType()));

                                et_note.setText(beanApprovalsDetails_list.get(0).note);
                                et_name_of_approvals_task.setText(beanApprovalsDetails_list.get(0).taskApprovalName);
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }

                        if (object.has("Approvals_From_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Approvals_From_Array");
                            if (jsonArray.length() != 0) {
                                beanApprovalsFroms_list.clear();
                                modelClasses.clear();
                                beanApprovalsFroms_list.addAll((Collection<? extends BeanApprovalsFrom>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanApprovalsFrom>>() {
                                }.getType()));

                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (object.has("Approvals_Attachments_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Approvals_Attachments_Array");
                            if (jsonArray.length() != 0) {
                                beanApprovalsAttachmentArrays.clear();
                                beanApprovalsAttachmentArrays.addAll((Collection<? extends BeanApprovalsAttachmentArray>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanApprovalsAttachmentArray>>() {
                                }.getType()));
                                AdapterApprovalsEdit adapterApprovalsEdit = new AdapterApprovalsEdit(beanApprovalsAttachmentArrays, activity);
                                rv_list_of_attateched_file.setAdapter(adapterApprovalsEdit);
                                pd.dismiss();
                            } else {
                                rv_list_of_attateched_file.setAdapter(null);
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
                    map.put("ApprovalId", AppConstant.Approvals_id);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_select_file:
                selectFile();
                break;

            case R.id.lnmainback:
                Intent i19 = new Intent(this, Approval_Activity.class);
                startActivity(i19);
                finish();
                break;

            case R.id.tv_cancel:
                Intent i = new Intent(this, Approval_Activity.class);
                startActivity(i);
                finish();
                break;

            case R.id.tv_create:
                String approval_from = "";
                String approval_from_id = "";
                if (et_approv_from.getText().toString().equalsIgnoreCase("")) {
                    approval_from = Utility.getFullNamePreference();
                    approval_from_id = Utility.getPeopleIdPreference();
                } else {
                    approval_from = Utility.getManagerNamePreference();
                    approval_from_id = Utility.getManagerIdPreference();
                }


                if (et_name_of_approvals_task.getText().toString().trim().equalsIgnoreCase("")) {
                    et_name_of_approvals_task.setError("Please Write Task Name");
                    requestFocus(et_name_of_approvals_task);
                } else if (et_note.getText().toString().trim().equalsIgnoreCase("")) {
                    et_note.setError("Please Write Some Note");
                    requestFocus(et_note);
                } else {
                    createApprovalRequest(et_name_of_approvals_task.getText().toString(), approval_from, approval_from_id, et_note.getText().toString());
                }
                break;
            case R.id.tv_submit:
                String approval_from1 = "";
                String approval_from_id1 = "";
                if (et_approv_from.getText().toString().equalsIgnoreCase("")) {
                    approval_from1 = Utility.getFullNamePreference();
                    approval_from_id1 = Utility.getPeopleIdPreference();
                } else {
                    approval_from1 = Utility.getManagerNamePreference();
                    approval_from_id1 = Utility.getManagerIdPreference();
                }


                if (et_name_of_approvals_task.getText().toString().trim().equalsIgnoreCase("")) {
                    et_name_of_approvals_task.setError("Please Write Task Name");
                    requestFocus(et_name_of_approvals_task);
                } else if (et_note.getText().toString().trim().equalsIgnoreCase("")) {
                    et_note.setError("Please Write Some Note");
                    requestFocus(et_note);
                } else {
                    updateApprovalRequest(et_name_of_approvals_task.getText().toString(), approval_from1, approval_from_id1, et_note.getText().toString());
                }
                break;
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
                case R.id.et_note:
                    validateNote();
                    break;

                case R.id.et_name_of_approvals_task:
                    validateTask();
                    break;


            }
        }
    }

    private boolean validateNote() {
        String email = et_note.getText().toString().trim();
        if (email.isEmpty()) {
            et_note.setError("Please Write Some Note");
            requestFocus(et_note);
            return false;
        } else {
            et_note.setError(null);
        }
        return true;
    }

    private boolean validateTask() {
        String email = et_name_of_approvals_task.getText().toString().trim();
        if (email.isEmpty()) {
            et_name_of_approvals_task.setError("Please Write Task Name");
            requestFocus(et_name_of_approvals_task);
            return false;
        } else {
            et_name_of_approvals_task.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void createApprovalRequest(final String task, final String approve_from, final String approval_from_id, final String note) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Request_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Approvals_Request_Add")) {
                            JSONArray array = object.getJSONArray("Approvals_Request_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    pd.dismiss();
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Intent i19 = new Intent(activity, Approval_Activity.class);
                                        startActivity(i19);
                                        finish();
                                        for (int j = 0; j < arrayList_file_base64.size(); j++) {
                                            addApprovalAttachmentFile(array.optJSONObject(i).getString("Approval_Id"), j);
                                        }

                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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
                    map.put("TaskApprovalName", task);
                    map.put("Note", note);
                    map.put("ApprovalFromId", approval_from_id);
                    map.put("ApprovalFromName", approve_from);
                    map.put("Priority", "0");//Priority is 0. as discuss with JD sir.
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

    public void addApprovalAttachmentFile(final String Approval_Id, final int pos) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Request_Attachment_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Approvals_Request_Attachment_Add")) {
                            JSONArray array = object.getJSONArray("Approvals_Request_Attachment_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        if (pos == arrayList_file_base64.size()) {
                                            pd.dismiss();
                                            Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        }
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
                    map.put("ApprovalId", Approval_Id);
                    map.put("FileTitle", arrayList_file_name.get(pos));
                    map.put("FileExten", arrayList_file_extension.get(pos));
                    map.put("Path", arrayList_file_base64.get(pos));
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

    private void updateApprovalRequest(final String task, final String approve_from, final String approval_from_id1, final String note) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Request_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Approvals_Request_Update")) {
                            JSONArray array = object.getJSONArray("Approvals_Request_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    pd.dismiss();
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Intent i19 = new Intent(activity, Approval_Activity.class);
                                        startActivity(i19);
                                        finish();
                                        for (int j = 0; j < arrayList_file_base64.size(); j++) {
                                            addApprovalAttachmentFile(AppConstant.Approvals_id, j);
                                        }
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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

                    map.put("ApprovalId", AppConstant.Approvals_id);
                    map.put("TaskApprovalName", task);
                    map.put("Note", note);
                    map.put("ApprovalFromId", approval_from_id1);
                    map.put("ApprovalFromName", approve_from);
                    map.put("Priority", "0");//Priority is 0. as discuss with JD sir.
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
