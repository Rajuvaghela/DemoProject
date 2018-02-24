package com.ext.adarsh.Fragment;

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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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

import android.location.Location;
import android.location.LocationListener;

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

import com.ext.adarsh.Activity.Approval_Activity;
import com.ext.adarsh.Activity.EventAddActivity;
import com.ext.adarsh.Activity.TakeActionActivity;
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
import com.ext.adarsh.Bean.BeanEventAddPeopleList;
import com.ext.adarsh.Bean.BeanEventSelectAllPeople;
import com.ext.adarsh.Bean.BeanMyContact;
import com.ext.adarsh.Bean.BeanPeopleNew;
import com.ext.adarsh.Bean.BeanRegionList;
import com.ext.adarsh.Bean.BeanStateList;
import com.ext.adarsh.Bean.ModelClass;
import com.ext.adarsh.Bean.ModelClass2;
import com.ext.adarsh.Bean.ModelClass3;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.RecyclerItemClickListener;

import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;
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
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;


public class MyEvents extends Fragment implements LocationListener {

    public static RecyclerView recylerupcomingevents;
    @BindView(R.id.createevent_float)
    FloatingActionButton createevent_float;
    Dialog dd;
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
    public static List<BeanBranchList> branch_List = new ArrayList<>();
    private static ArrayList<BeanEventSelectAllPeople> contact_list = new ArrayList<>();
    private RadioButton radioSexButton;
    private RadioGroup radioSexGroup;
    List<BeanStateList> state_List = new ArrayList<>();
    AdapterState adapter_State;
    Spinner spiner_city, spiner_region, spiner_state;
    String state_id = "0", city_id = "0", region_id = "0";
    List<BeanCityList> city_List = new ArrayList<>();
    List<BeanRegionList> region_List = new ArrayList<>();
    AdapterCity adapter_ciy;
    AdapterRegion adapter_region;
    private static final int PICKFILE_RESULT_CODE = 2;
    String departName = "";
    String branchName = "";
    String fileBase64 = "";
    String filename;
    String file_extension;
    TextView tv_file_name;
    String public_or_private = "";
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    FileOutputStream fo;
    String profileimg = "";
    String imagename;
    String bname = "", dname = "";
    ImageView iv_user_profile;
    EditText et_event_title, et_validate_days, et_address, et_event_des;
    TextView tv_event_start_date, tv_event_start_time, tv_event_end_date, tv_event_end_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.event_myevents, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        activity = getActivity();
        activity2 = getActivity();
        pd = Utility.getDialog(activity);
        pd2 = Utility.getDialog(activity2);

        if (Utility.getEventAdd().equalsIgnoreCase("Y")){
            createevent_float.setVisibility(View.VISIBLE);
        }else {
            createevent_float.setVisibility(View.GONE);
        }
        recylerupcomingevents = (RecyclerView) view.findViewById(R.id.recylermyevents);
        recylerupcomingevents.setHasFixedSize(true);
        LinearLayoutManager lnmanager = new LinearLayoutManager(activity);
        recylerupcomingevents.setLayoutManager(lnmanager);
        recylerupcomingevents.setItemAnimator(new DefaultItemAnimator());

        createevent_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EventAddActivity.class);
                intent.putExtra("add_or_edit", "add");
                startActivity(intent);
            }
        });
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
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
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == PICKFILE_RESULT_CODE) {
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

    //region Camera
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

    private void addMyEvent() {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.event_add_information);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        getBranchData();
        getDepartmentData();
        getPeopleData();


        RelativeLayout rl_select_file = (RelativeLayout) dd.findViewById(R.id.rl_select_file);
        FrameLayout fl_user_profile = (FrameLayout) dd.findViewById(R.id.fl_user_profile);

        RadioButton radioButton = (RadioButton) dd.findViewById(R.id.radioButton);
        RadioButton radioButton2 = (RadioButton) dd.findViewById(R.id.radioButton2);
        spiner_state = (Spinner) dd.findViewById(R.id.spiner_state);
        spiner_city = (Spinner) dd.findViewById(R.id.spiner_city);
        spiner_region = (Spinner) dd.findViewById(R.id.spiner_region);

        iv_user_profile = (ImageView) dd.findViewById(R.id.iv_user_profile);

        final LinearLayout ll_private_or_oublic = (LinearLayout) dd.findViewById(R.id.ll_private_or_oublic);


        spiner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city_id = city_List.get(i).cityId;
                getRegion(city_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spiner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                region_id = region_List.get(i).regionId;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        radioSexGroup = (RadioGroup) dd.findViewById(R.id.radioGroup);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) dd.findViewById(selectedId);
        if (radioSexButton.getText().toString().equals("Public")) {

            //    gender = "public";
        }
        if (radioSexButton.getText().toString().equals("Private")) {

            // gender = "private";
        }
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                public_or_private = "Public";
                ll_private_or_oublic.setVisibility(View.GONE);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                public_or_private = "Private";
                ll_private_or_oublic.setVisibility(View.VISIBLE);
            }
        });
        rl_select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });
        fl_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        tv_file_name = (TextView) dd.findViewById(R.id.tv_file_name);
        tv_select_visible_department = (TextView) dd.findViewById(R.id.tv_select_visible_department);
        tv_select_visible_branch = (TextView) dd.findViewById(R.id.tv_select_visible_branch);
        rv_select_visible_branch = (RecyclerView) dd.findViewById(R.id.rv_select_visible_branch);
        rv_select_visible_department = (RecyclerView) dd.findViewById(R.id.rv_select_visible_department);
        rv_select_person = (RecyclerView) dd.findViewById(R.id.rv_select_person);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        rv_select_visible_department.setLayoutManager(flowLayoutManager);

        FlowLayoutManager flowLayoutManager2 = new FlowLayoutManager();
        flowLayoutManager2.setAutoMeasureEnabled(true);
        rv_select_visible_branch.setLayoutManager(flowLayoutManager2);

        FlowLayoutManager flowLayoutManager3 = new FlowLayoutManager();
        flowLayoutManager3.setAutoMeasureEnabled(true);
        rv_select_person.setLayoutManager(flowLayoutManager3);

        TextView tv1 = (TextView) dd.findViewById(R.id.tv1);
        TextView tv2 = (TextView) dd.findViewById(R.id.tv2);
        TextView tv3 = (TextView) dd.findViewById(R.id.tv3);
        TextView tv4 = (TextView) dd.findViewById(R.id.tv4);
        TextView tv5 = (TextView) dd.findViewById(R.id.tv5);
        TextView tv6 = (TextView) dd.findViewById(R.id.tv6);
        TextView tv7 = (TextView) dd.findViewById(R.id.tv7);
        TextView tv8 = (TextView) dd.findViewById(R.id.tv8);
        TextView tv9 = (TextView) dd.findViewById(R.id.tv9);
        TextView tv10 = (TextView) dd.findViewById(R.id.tv10);
        TextView tv11 = (TextView) dd.findViewById(R.id.tv11);
        TextView tv12 = (TextView) dd.findViewById(R.id.tv12);
        TextView tv13 = (TextView) dd.findViewById(R.id.tv13);
        TextView tv14 = (TextView) dd.findViewById(R.id.tv14);
        TextView tv15 = (TextView) dd.findViewById(R.id.tv15);
        TextView tv16 = (TextView) dd.findViewById(R.id.tv16);
        TextView tv17 = (TextView) dd.findViewById(R.id.tv17);
        TextView tv18 = (TextView) dd.findViewById(R.id.tv18);
        TextView tv19 = (TextView) dd.findViewById(R.id.tv19);
        TextView tv_file_name = (TextView) dd.findViewById(R.id.tv_file_name);
        tv_event_start_date = (TextView) dd.findViewById(R.id.tv_event_start_date);
        tv_event_start_time = (TextView) dd.findViewById(R.id.tv_event_start_time);
        tv_event_end_date = (TextView) dd.findViewById(R.id.tv_event_end_date);
        tv_event_end_time = (TextView) dd.findViewById(R.id.tv_event_end_time);
        tv_select_person = (TextView) dd.findViewById(R.id.tv_select_person);


        et_event_title = (EditText) dd.findViewById(R.id.et_event_title);
        et_validate_days = (EditText) dd.findViewById(R.id.et_validate_days);
        et_address = (EditText) dd.findViewById(R.id.et_address);
        et_event_des = (EditText) dd.findViewById(R.id.et_event_des);

        et_event_title.addTextChangedListener(new MyTextWatcher(et_event_title));
        et_validate_days.addTextChangedListener(new MyTextWatcher(et_validate_days));
        et_address.addTextChangedListener(new MyTextWatcher(et_address));
        et_event_des.addTextChangedListener(new MyTextWatcher(et_event_des));

        tv_event_start_date.setTypeface(Utility.getTypeFace());
        tv_event_start_time.setTypeface(Utility.getTypeFace());
        tv_event_end_date.setTypeface(Utility.getTypeFace());
        tv_event_end_time.setTypeface(Utility.getTypeFace());
        tv_select_person.setTypeface(Utility.getTypeFace());
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
        tv10.setTypeface(Utility.getTypeFace());
        tv11.setTypeface(Utility.getTypeFace());
        tv12.setTypeface(Utility.getTypeFace());
        tv13.setTypeface(Utility.getTypeFace());
        tv14.setTypeface(Utility.getTypeFace());
        tv15.setTypeface(Utility.getTypeFace());
        tv19.setTypeface(Utility.getTypeFace());
        tv16.setTypeface(Utility.getTypeFaceTab());
        tv17.setTypeface(Utility.getTypeFaceTab());
        tv18.setTypeface(Utility.getTypeFaceTab());

        et_event_title.setTypeface(Utility.getTypeFace());
        et_validate_days.setTypeface(Utility.getTypeFace());
        et_address.setTypeface(Utility.getTypeFace());
        et_event_des.setTypeface(Utility.getTypeFace());


        radioButton.setTypeface(Utility.getTypeFace());
        radioButton2.setTypeface(Utility.getTypeFace());

        LinearLayout lnmainback = (LinearLayout) dd.findViewById(R.id.lnmainback);
        LinearLayout ll_select_department = (LinearLayout) dd.findViewById(R.id.ll_select_department);
        LinearLayout ll_select_branch = (LinearLayout) dd.findViewById(R.id.ll_select_branch);
        LinearLayout ll_select_person = (LinearLayout) dd.findViewById(R.id.ll_select_person);
        LinearLayout ll_create_events = (LinearLayout) dd.findViewById(R.id.ll_create_events);


        RelativeLayout rl_event_start_date = (RelativeLayout) dd.findViewById(R.id.rl_event_start_date);
        RelativeLayout rl_event_start_time = (RelativeLayout) dd.findViewById(R.id.rl_event_start_time);
        RelativeLayout rl_event_end_date = (RelativeLayout) dd.findViewById(R.id.rl_event_end_date);
        RelativeLayout rl_event_end_time = (RelativeLayout) dd.findViewById(R.id.rl_event_end_time);

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
                dd.dismiss();
            }
        });

        tv17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
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
        ll_create_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < item_list2.size(); i++) {
                    branchName += item_list2.get(i).getName() + ",";
                }
                for (int i = 0; i < item_list.size(); i++) {
                    departName += item_list.get(i).getName() + ",";
                }
                if (branchName.length() > 0) {
                    bname = branchName.substring(0, branchName.length() - 1);
                }
                if (departName.length() > 0) {
                    dname = departName.substring(0, departName.length() - 1);
                }
                if (public_or_private.equalsIgnoreCase("Public")) {
                    validatePublic();
                } else {
                    validatePrivaate();
                }

            }
        });

        getState();
        dd.show();
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
                Date dateObj = null;
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    dateObj = sdf.parse(time);
                    System.out.println(dateObj);
                    System.out.println(new SimpleDateFormat("K:mm a").format(dateObj));
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
                tv_event_end_time.setText(new SimpleDateFormat("K:mm a").format(dateObj));
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

                        tv_event_end_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

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
                Date dateObj = null;
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    dateObj = sdf.parse(time);
                    System.out.println(dateObj);
                    System.out.println(new SimpleDateFormat("K:mm a").format(dateObj));
                } catch (final ParseException e) {
                    e.printStackTrace();
                }

                tv_event_start_time.setText(new SimpleDateFormat("K:mm a").format(dateObj));
            }
        }, hour, minute, false);//Yes 24 hour time

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

                        tv_event_start_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void validatePrivaate() {
        if (et_event_title.getText().toString().trim().isEmpty()) {
            et_event_title.setError("Please Write Event Title");
            requestFocus(et_event_title);
        } else if (tv_event_start_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event start date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_start_time.getText().toString().equalsIgnoreCase("hh:mm am")) {
            Toast.makeText(activity, "Please Select event start time", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event end date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_time.getText().toString().equalsIgnoreCase("hh:mm am")) {
            Toast.makeText(activity, "Please Select event end time", Toast.LENGTH_SHORT).show();
        } else if (et_validate_days.getText().toString().trim().isEmpty()) {
            et_validate_days.setError("Please Write Event Validity");
            requestFocus(et_validate_days);
        } else if (dname.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select Department", Toast.LENGTH_SHORT).show();
        } else if (bname.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select Branch", Toast.LENGTH_SHORT).show();
        } else if (state_id.equalsIgnoreCase("0")) {
            Toast.makeText(activity, "Please Select State", Toast.LENGTH_SHORT).show();
        } else if (city_id.equalsIgnoreCase("0")) {
            Toast.makeText(activity, "Please Select City", Toast.LENGTH_SHORT).show();
        } else if (region_id.equalsIgnoreCase("0")) {
            Toast.makeText(activity, "Please Select Region", Toast.LENGTH_SHORT).show();

        } else if (et_address.getText().toString().trim().isEmpty()) {
            et_address.setError("Please Write Event Address");
            requestFocus(et_address);

        } else if (et_event_des.getText().toString().trim().isEmpty()) {
            et_event_des.setError("Please Write Event Title Description");
            requestFocus(et_event_des);
        } else if (fileBase64.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please select file", Toast.LENGTH_SHORT).show();
        } else if (profileimg.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select Image", Toast.LENGTH_SHORT).show();
        } else {
/*                CreateEvents(et_event_title.getText().toString(),
                        tv_event_start_date.getText().toString(),
                        tv_event_start_time.getText().toString(),
                        tv_event_end_date.getText().toString(),
                        tv_event_end_time.getText().toString(),
                        et_validate_days.getText().toString(),
                        public_or_private,
                        state_id,
                        city_id,
                        region_id,
                        et_address.getText().toString(),
                        et_event_des.getText().toString()
                        );*/
        }
    }
    private void validatePublic() {
        if (et_event_title.getText().toString().trim().isEmpty()) {
            et_event_title.setError("Please Write Event Title");
            requestFocus(et_event_title);
        } else if (tv_event_start_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event start date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_start_time.getText().toString().equalsIgnoreCase("hh:mm am")) {
            Toast.makeText(activity, "Please Select event start time", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_date.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
            Toast.makeText(activity, "Please Select event end date", Toast.LENGTH_SHORT).show();
        } else if (tv_event_end_time.getText().toString().equalsIgnoreCase("hh:mm am")) {
            Toast.makeText(activity, "Please Select event end time", Toast.LENGTH_SHORT).show();
        } else if (et_validate_days.getText().toString().trim().isEmpty()) {
            et_validate_days.setError("Please Write Event Validity");
            requestFocus(et_validate_days);
        } else if (false) {

        } else if (et_address.getText().toString().trim().isEmpty()) {
            et_address.setError("Please Write Event Address");
            requestFocus(et_address);
        } else if (et_event_des.getText().toString().trim().isEmpty()) {
            et_event_des.setError("Please Write Event Title Description");
            requestFocus(et_event_des);
        } else if (fileBase64.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please select file", Toast.LENGTH_SHORT).show();
        } else if (profileimg.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Select Image", Toast.LENGTH_SHORT).show();
        } else {
/*                CreateEvents(et_event_title.getText().toString(),
                        tv_event_start_date.getText().toString(),
                        tv_event_start_time.getText().toString(),
                        tv_event_end_date.getText().toString(),
                        tv_event_end_time.getText().toString(),
                        et_validate_days.getText().toString(),
                        public_or_private,
                        state_id,
                        city_id,
                        region_id,
                        et_address.getText().toString(),
                        et_event_des.getText().toString()
                        );*/
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

    private void CreateEvents(String event_title,
                              String event_start_date,
                              String event_start_time,
                              String event_end_date,
                              String event_end_time,
                              String validate_days,
                              String public_or_private,
                              String state_id,
                              String city_id,
                              String region_id,
                              String event_address,
                              String event_event_des) {
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
                                        //  addAnnouncementAttachmentFile(array.optJSONObject(i).getString("AnnouncementId"));
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
      /*              map.put("Title", title);
                    map.put("AnnouncementDetails", des);
                    map.put("ReferenceToPostId", "1");
                    map.put("DepartmentList", dep_name);
                    map.put("AnnouncementDate", announcement_date);
                    map.put("SchedulePublishOnDate", publish_date);
                    map.put("BranchIdList", bra_name);
                    map.put("PeopleId", Utility.getPeopleIdPreference());*/
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

    void getState() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.State_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("state_res", response);
                    try {
                        JSONObject object = new JSONObject(response);

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
                    map.put("Hashkey", "register");
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd.dismiss();

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

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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

    public static void callOnBackPress() {
        recyclerview_adapter = new Adapter2MyEvents(activity2, MyEvents.item_list);
        rv_onchangelistner();
    }

    public static void callOnBackPress2() {
        recyclerview_adapter2 = new Adapter2BranchMyEvents(activity2, MyEvents.item_list2);
        rv_onchangelistner2();

    }

    public static void callOnBackPress3() {
        recyclerview_adapter3 = new Adapter2MyEventsPeopleList(activity2, MyEvents.item_list3);
        rv_onchangelistner3();

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

        recyclerview_adapter.notifyDataSetChanged();

        recyclerview2.setAdapter(recyclerview_adapter);
        recyclerview_adapter.notifyDataSetChanged();
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

        recyclerview_adapter2.notifyDataSetChanged();

        recyclerview4.setAdapter(recyclerview_adapter2);
        recyclerview_adapter2.notifyDataSetChanged();
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

    public void getPeopleData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.People_Select_All_for_Event, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res_people", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("People_Array")) {
                            JSONArray jsonArray = object.getJSONArray("People_Array");
                            if (jsonArray.length() != 0) {
                                contact_list.clear();
                                contact_list.addAll((Collection<? extends BeanEventSelectAllPeople>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEventSelectAllPeople>>() {
                                }.getType()));
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
                    Log.e("res_error", error.toString());
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
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            showSuccessAlertDialog(activity, getResources().getString(R.string.network_message));
        }
    }

}
