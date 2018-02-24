package com.ext.adarsh.Activity.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import com.bumptech.glide.Glide;
import com.ext.adarsh.Activity.MainActivity;
import com.ext.adarsh.Activity.ProfileActivity;
import com.ext.adarsh.Adapter.Adapter1FeedPostPeopleList;
import com.ext.adarsh.Adapter.Adapter2FeedPostPeopleList;
import com.ext.adarsh.Adapter.AdapterFeedPostImage;
import com.ext.adarsh.Bean.BeanPeopleSelectByDepartmentId;
import com.ext.adarsh.Bean.ModelClass3;
import com.ext.adarsh.Bean.ModelPostImageList;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class OtherFeedPostActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;
    ProgressDialog pd;

    @BindView(R.id.h1)
    TextView h1;

    @BindView(R.id.tv2)
    TextView tv2;


    static TextView tv_post_person_name;

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


    static TextView tv10;

    @BindView(R.id.edt_whats_on_your_mind)
    EditText edt_whats_on_your_mind;

   /* @BindView(R.id.iv_photo_video_selected)
    ImageView iv_photo_video_selected;*/


    @BindView(R.id.ll_select_photo_video)
    LinearLayout ll_select_photo_video;

    @BindView(R.id.ll_check_in)
    LinearLayout ll_check_in;

    @BindView(R.id.lnbacksharing)
    LinearLayout lnbacksharing;

    @BindView(R.id.iv_profile_image)
    ImageView iv_profile_image;

    @BindView(R.id.spiner_post_public_or_private)
    Spinner spiner_post_public_or_private;

    @BindView(R.id.ll_publish_post)
    LinearLayout ll_publish_post;

    String public_or_pricate;


    static RecyclerView rv_select_person;
    static ProgressDialog pd2;
    static Activity activity2;
    static Dialog open_tag_dialog_people;
    public static RecyclerView.Adapter recyclerview_adapter3;
    static RecyclerView recyclerview6;
    static RecyclerView recyclerview5;
    static RecyclerView.LayoutManager recylerViewLayoutManager3;
    private static ArrayList<BeanPeopleSelectByDepartmentId> contact_list = new ArrayList<>();
    static Adapter1FeedPostPeopleList adapter1people;
    public static List<ModelClass3> item_list3 = new ArrayList<>();
    public List<ModelPostImageList> modelPostImageLists = new ArrayList<>();

    public static RecyclerView.Adapter recyclerview_adapter;
    LinearLayout ll_tag_friends;
    private String backmanage;

    public ArrayList<String> arrayList_file_base64 = new ArrayList<>();
    public ArrayList<String> arrayList_file_name = new ArrayList<>();
    public ArrayList<String> arrayList_file_extension = new ArrayList<>();
    public final int PICKFILE_RESULT_CODE = 1;
    public String file_extension;
    public String filename;
    String people_id = "", people_name = "", people_profile = "", login_id = "";
    RecyclerView rv_selected_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_post);
        ButterKnife.bind(this);
        activity = this;
        activity2 = this;
        pd = Utility.getDialog(activity);
        pd2 = Utility.getDialog(activity2);
        backmanage = getIntent().getStringExtra("key");
        people_id = getIntent().getStringExtra("people_id");
        login_id = getIntent().getStringExtra("login_id");
        people_name = getIntent().getStringExtra("people_name");
        people_profile = getIntent().getStringExtra("people_profile");
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Public");
        categories.add("Colleagues");
        categories.add("Only Me");
        tv10 = (TextView) findViewById(R.id.tv10);
        tv_post_person_name = (TextView) findViewById(R.id.tv_post_person_name);

        rv_selected_image = (RecyclerView) findViewById(R.id.rv_selected_image);
        rv_selected_image.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true);
        rv_selected_image.setLayoutManager(lnmanager2);
        rv_selected_image.setItemAnimator(new DefaultItemAnimator());

        rv_select_person = (RecyclerView) findViewById(R.id.rv_select_person);
        FlowLayoutManager flowLayoutManager3 = new FlowLayoutManager();
        flowLayoutManager3.setAutoMeasureEnabled(true);

        rv_select_person.setLayoutManager(flowLayoutManager3);
        ll_tag_friends = (LinearLayout) findViewById(R.id.ll_tag_friends);
        ll_tag_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagPopuppeople();
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_post_public_or_private.setAdapter(dataAdapter);
        spiner_post_public_or_private.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    public_or_pricate = "P";
                } else if (i == 1) {
                    public_or_pricate = "C";
                } else {
                    public_or_pricate = "O";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tv_post_person_name.setText(people_name);
        Glide.with(activity).load(people_profile).into(iv_profile_image);
        ll_select_photo_video.setOnClickListener(this);
        ll_check_in.setOnClickListener(this);
        ll_tag_friends.setOnClickListener(this);
        lnbacksharing.setOnClickListener(this);
        ll_publish_post.setOnClickListener(this);

        settypeface();
        getTagFriend();
    }

    @Override
    public void onBackPressed() {
        if (backmanage == null) {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        } else if (backmanage.equalsIgnoreCase("profile")) {
            startActivity(new Intent(activity, ProfileActivity.class));
            finish();
        } else {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }
    }


    private void settypeface() {
        h1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv_post_person_name.setTypeface(Utility.getTypeFace());

        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv10.setTypeface(Utility.getTypeFace());
        edt_whats_on_your_mind.setTypeface(Utility.getTypeFace());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_select_photo_video:
                selectImage();
                break;
            case R.id.ll_check_in:
                break;
            case R.id.ll_tag_friends:
                openTagPopuppeople();
                break;
            case R.id.lnbacksharing:
                finish();
                break;
            case R.id.ll_publish_post:
                String IsImagePostFlag;
                if (modelPostImageLists == null || modelPostImageLists.size() < 1) {
                    IsImagePostFlag = "D";
                } else {
                    IsImagePostFlag = "A";
                }
                String PostTagFlag;
                String p_name = "";
                String p_id = "";
                String people_name = "";
                String people_id = "";
                if (item_list3.size() > 0) {
                    PostTagFlag = "Y";
                    for (int i = 0; i < item_list3.size(); i++) {
                        p_name += item_list3.get(i).getName() + ",";
                        p_id += item_list3.get(i).getId() + ",";
                    }
                    if (p_name.length() > 0) {
                        people_name = p_name.substring(0, p_name.length() - 1);
                    }
                    if (p_id.length() > 0) {
                        people_id = p_id.substring(0, p_id.length() - 1);
                    }
                } else {
                    PostTagFlag = "N";
                }
                if (edt_whats_on_your_mind.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Enter Description", Toast.LENGTH_SHORT).show();
                } else {
                    publishMyPost(edt_whats_on_your_mind.getText().toString(), IsImagePostFlag, PostTagFlag, people_id, people_name);
                }
                break;
        }
    }

    private void selectImage() {
        Intent filePickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        filePickerIntent.setType("*/*");
        filePickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(filePickerIntent, PICKFILE_RESULT_CODE);
    }

    private void getTagFriend() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_Tag_People_List, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res_people", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            Toast.makeText(activity2, "Please Try again", Toast.LENGTH_SHORT).show();
                            activity2.finish();
                        }
                        if (object.has("Activity_Feed_Post_Tag_People_List_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Activity_Feed_Post_Tag_People_List_Array");
                            if (jsonArray.length() != 0) {
                                contact_list.clear();
                                //   contact_list.add(new BeanPeopleSelectByDepartmentId("0", "Select People"));
                                contact_list.addAll((Collection<? extends BeanPeopleSelectByDepartmentId>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeopleSelectByDepartmentId>>() {
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
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

    public static void callOnBackPress3() {
        recyclerview_adapter3 = new Adapter2FeedPostPeopleList(activity2, item_list3);
        rv_onchangelistner3();
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
                    rv_select_person.setVisibility(View.VISIBLE);
                    tv_post_person_name.setText(Utility.getFullNamePreference() + " is with " + item_list3.size() + " Others");
                } else {
                    rv_select_person.setVisibility(View.VISIBLE);
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

        adapter1people = new Adapter1FeedPostPeopleList(activity2, contact_list);
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

    private void publishMyPost(final String edt_whats_on_your_mind, final String IsImagePostFlag, final String PostTagFlag,
                               final String People_Id, final String People_Name) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Timeline_Post_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Post_Add")) {
                            JSONArray array = object.getJSONArray("Post_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        String PostId = array.optJSONObject(i).getString("PostId");
                                        if (IsImagePostFlag.equalsIgnoreCase("A")) {
                                            for (i = 0; i < modelPostImageLists.size(); i++) {
                                                if (i == modelPostImageLists.size() - 1) {
                                                    Log.e("onResponse: ", "last record done");
                                                    PostImageUsingPostId(PostId, edt_whats_on_your_mind, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "L");
                                                    pd.dismiss();
                                                } else {
                                                    PostImageUsingPostId(PostId, edt_whats_on_your_mind, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "F");
                                                }
                                            }
                                        } else {
                                            Intent intent = new Intent(activity, OtherProfileActivity.class);
                                            intent.putExtra("peopleId", people_id);
                                            startActivity(intent);
                                            finish();

                                         /*   startActivity(new Intent(activity, MainActivity.class));
                                            finish();*/
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
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("PeopleId", people_id);
                    map.put("IsImagePostFlag", IsImagePostFlag);
                    map.put("Description", edt_whats_on_your_mind);
                    map.put("PrivacyFlag", public_or_pricate);
                    map.put("PostTagFlag", PostTagFlag);
                    map.put("TagPeopleIdList", People_Id);
                    map.put("TagPeopleNameList", People_Name);
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

    private void PostImageUsingPostId(final String PostId, final String edt_whats_on_your_mind, final String path, final String imgname, final String imgext, final String check) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_Image_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Post_Image")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Post_Image");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        if (check.equalsIgnoreCase("L")) {
                                            Intent intent = new Intent(activity, OtherProfileActivity.class);
                                            intent.putExtra("peopleId", people_id);
                                            startActivity(intent);
                                            finish();
                                            pd.dismiss();
                                            Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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
                    map.put("PostId", PostId);
                    map.put("Description", edt_whats_on_your_mind);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("PostImagePath", path);
                    map.put("PostImageName", imgname);
                    map.put("PostImageExten", imgext);
                    map.put("PrivacyFlag", public_or_pricate);
                    map.put("MultiFlag", "N");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && null != data) {
            arrayList_file_name.clear();
            arrayList_file_extension.clear();
            arrayList_file_base64.clear();

            if (data.getData() != null) {
                Uri FilePath = data.getData();
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

                //iv_photo_video_selected.setImageBitmap(bm);
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
                ModelPostImageList temp = new ModelPostImageList();
                temp.setBitmap(bm);
                temp.setImagepath(readFileAsBase64String(path));
                temp.setImage_name(filename);
                temp.setImage_extension(file_extension);
                modelPostImageLists.add(temp);

                AdapterFeedPostImage adapterPollsOption = new AdapterFeedPostImage(activity, modelPostImageLists);
                rv_selected_image.setAdapter(adapterPollsOption);
                // tv_file_name.setText(String.valueOf(arrayList_file_base64.size()) + " file attached");

            } else {
                if (data != null) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Log.i("Path:", item.toString());


                        Uri uri = item.getUri();
                        Bitmap bitmap = null;
                        try {
                            bitmap = getThumbnail(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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

                        ModelPostImageList temp = new ModelPostImageList();
                        temp.setBitmap(bitmap);
                        temp.setImagepath(readFileAsBase64String(path));
                        temp.setImage_name(filename);
                        temp.setImage_extension(file_extension);
                        modelPostImageLists.add(temp);

                        AdapterFeedPostImage adapterPollsOption = new AdapterFeedPostImage(activity, modelPostImageLists);
                        rv_selected_image.setAdapter(adapterPollsOption);
                        Log.e("size arraylist", "++++++++++++" + arrayList_file_base64.size());
                        Log.e("size arraylist", "+++++++++++++++" + arrayList_file_extension.size());
                    }
                }
            }
        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 512) ? (originalSize / 320) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
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

}
