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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.ext.adarsh.Adapter.Adapter1MessagesPeopleList1;
import com.ext.adarsh.Adapter.Adapter1MessagesPeopleList2;
import com.ext.adarsh.Adapter.Adapter1MessagesPeopleList3;
import com.ext.adarsh.Adapter.Adapter2MessagesPeople1;
import com.ext.adarsh.Adapter.Adapter2MessagesPeople2;
import com.ext.adarsh.Adapter.Adapter2MyEventsPeopleList;
import com.ext.adarsh.Adapter.AdapterMessagesAttachment;
import com.ext.adarsh.Bean.BeanAttachmentData;
import com.ext.adarsh.Bean.BeanMessagePeopleList;
import com.ext.adarsh.Bean.ModelClass;
import com.ext.adarsh.Bean.ModelClass2;
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

public class MessageCompose extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.lnback)
    LinearLayout lnback;

    @BindView(R.id.lnattach)
    LinearLayout lnattach;

    @BindView(R.id.lnsend)
    LinearLayout lnsend;

    @BindView(R.id.tv1)
    TextView tv1;

    EditText edt4, edt5;
    String ed_sub, ed_body;
    LinearLayout ll_select_recipients, ll_select_cc, ll_select_bcc;

    static Dialog open_tag_dialog;
    static Dialog open_tag_dialog_people;

    ProgressDialog pd;
    static ProgressDialog pd2;
    static Activity activity2;
    Activity activity;
    static RecyclerView rv_select_cc, rv_select_bcc;
    static RecyclerView rv_recipients;
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
    // static Adapter1MyEvents adapter1;
    static Adapter1MessagesPeopleList1 adapter1;
    static Adapter1MessagesPeopleList2 adapter1branch;
    static Adapter1MessagesPeopleList3 adapter1people;
    private static ArrayList<BeanMessagePeopleList> beanMessagePeopleLists = new ArrayList<>();
    private ArrayList<ModelPostImageList> modelPostImageLists = new ArrayList<>();

    private static final int PICKFILE_RESULT_CODE = 2;
    String filename = "";
    String file_extension = "";
    public static AdapterMessagesAttachment adapterMessagesAttachment;
    RecyclerView rv_select_file;

    String Flag,parentID,fromName,subject,body,fl="null";
    String AttachmentFlag;
    ArrayList<BeanAttachmentData> beanAttachmentData = new ArrayList<>();
    String fieldtype = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.composemail);
        ButterKnife.bind(this);
        activity = this;
        activity2 = this;

        Bundle extra = getIntent().getExtras();
        Flag = extra.getString("Key");

        if(Flag.equalsIgnoreCase("R") || Flag.equalsIgnoreCase("RA")){
            parentID = extra.getString("parentID");
            subject = extra.getString("subject");
         //   fromName = extra.getString("fromName");
        }else if(Flag.equalsIgnoreCase("F")){
            parentID = extra.getString("parentID");
            subject = extra.getString("subject");
            body = extra.getString("body");
            fl = extra.getString("fl");

            if(fl.equalsIgnoreCase("E")){
                AttachmentFlag = "D";
            }
            else if(fl.equalsIgnoreCase("NE")){
                AttachmentFlag = "A";
                for(int i=0;i<beanAttachmentData.size();i++){
                    String filename= beanAttachmentData.get(i).FilePath.substring(beanAttachmentData.get(i).FilePath.lastIndexOf("/")+1);
                    String extension = beanAttachmentData.get(i).FilePath.substring(beanAttachmentData.get(i).FilePath.lastIndexOf("."));;
                    Bitmap image = null;

                    /*try {
                        URL url = new URL("http://....");
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch(IOException e) {
                        System.out.println(e);
                    }
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    String base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);*/

                    Log.e("imagePath", beanAttachmentData.get(i).FilePath);
                    Log.e("filename",filename);
                    Log.e("extension",extension);

                    modelPostImageLists.clear();

                    ModelPostImageList temp = new ModelPostImageList();
                    temp.setImagepath(beanAttachmentData.get(i).mainFilePath);
                    temp.setImage_name(filename);
                    temp.setImage_extension(extension);
                    temp.setFlag("O");
                    fieldtype = "O";
                    modelPostImageLists.add(temp);
                }
            }
        }

        pd = Utility.getDialog(activity);
        pd2 = Utility.getDialog(activity2);
        tv1.setTypeface(Utility.getTypeFace());
        edt4 = (EditText) findViewById(R.id.edt4);
        edt5 = (EditText) findViewById(R.id.edt5);

        edt4.setTypeface(Utility.getTypeFace());
        edt5.setTypeface(Utility.getTypeFace());

        if(Flag.equalsIgnoreCase("R")){
            tv1.setText("Reply");
            edt4.setText(subject);
        }else if(Flag.equalsIgnoreCase("RA")){
            tv1.setText("Reply All");
            edt4.setText(subject);
        }else if(Flag.equalsIgnoreCase("F")){
            tv1.setText("Forward");
            edt4.setText(subject);
            Log.e("body",""+body);
            edt5.setText(body);
        }

        ll_select_recipients = (LinearLayout) findViewById(R.id.ll_select_recipients);
        ll_select_cc = (LinearLayout) findViewById(R.id.ll_select_cc);
        ll_select_bcc = (LinearLayout) findViewById(R.id.ll_select_bcc);

        rv_select_cc = (RecyclerView) findViewById(R.id.rv_select_cc);
        rv_recipients = (RecyclerView) findViewById(R.id.rv_recipients);
        rv_select_bcc = (RecyclerView) findViewById(R.id.rv_select_bcc);
        rv_select_file = (RecyclerView) findViewById(R.id.rv_select_file);

        rv_select_file.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true);
        rv_select_file.setLayoutManager(lnmanager2);
        rv_select_file.setItemAnimator(new DefaultItemAnimator());

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        rv_recipients.setLayoutManager(flowLayoutManager);

        FlowLayoutManager flowLayoutManager2 = new FlowLayoutManager();
        flowLayoutManager2.setAutoMeasureEnabled(true);
        rv_select_cc.setLayoutManager(flowLayoutManager2);

        FlowLayoutManager flowLayoutManager3 = new FlowLayoutManager();
        flowLayoutManager3.setAutoMeasureEnabled(true);
        rv_select_bcc.setLayoutManager(flowLayoutManager3);

        if(fl.equalsIgnoreCase("NE")){
            Log.e("attaching","attach");
            attach();
        }

        GetMessage_People_List();

        lnback.setOnClickListener(this);
        lnattach.setOnClickListener(this);
        lnsend.setOnClickListener(this);
        ll_select_recipients.setOnClickListener(this);
        ll_select_cc.setOnClickListener(this);
        ll_select_bcc.setOnClickListener(this);

    }

    private void GetMessage_People_List() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_People_List, new Response.Listener<String>() {
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
                        if (object.has("Message_People_List_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Message_People_List_Array");
                            if (jsonArray.length() != 0) {
                                beanMessagePeopleLists.clear();
                                beanMessagePeopleLists.addAll((Collection<? extends BeanMessagePeopleList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMessagePeopleList>>() {
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
                    map.put("LoginId", Utility.getPeopleIdPreference());


                    Log.e("Message","Message_People_List_Array");
                    Log.e("Hashkey", Utility.getHashKeyPreference());
                    Log.e("LoginId", Utility.getPeopleIdPreference());

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
            case R.id.lnback:

                if(Flag.equalsIgnoreCase("C")){
                    Intent intent = new Intent(activity, Message.class);
                    startActivity(intent);
                }else if(Flag.equalsIgnoreCase("R")){
                    activity.finish();
                    activity2.finish();
                }else if(Flag.equalsIgnoreCase("RA")){
                    activity.finish();
                    activity2.finish();
                }else if(Flag.equalsIgnoreCase("F")){
                    activity.finish();
                    activity2.finish();
                }

                break;
            case R.id.lnattach:
                SelectFile();
                //  messageAttach();
                break;
            case R.id.lnsend:

                if (modelPostImageLists == null || modelPostImageLists.size() < 1) {
                    AttachmentFlag = "D";
                } else {
                    AttachmentFlag = "A";
                }


                String people_id = "";
                String people_name = "";
                String people_email = "";
                String bcc_id = "";
                String bcc_name = "";
                String bcc_email = "";
                String cc_id = "";
                String cc_name = "";
                String cc_email = "";

                String people_list = "";
                String people_list_name = "";
                String p_email = "";
                String bcc_list = "";
                String bcc_list_name = "";
                String b_email = "";
                String cc_list = "";
                String cc_list_name = "";
                String c_email = "";


                for (int i = 0; i < item_list.size(); i++) {
                    people_id += item_list.get(i).getId() + ",";
                    people_name += item_list.get(i).getName() + ",";
                    people_email += item_list.get(i).getEmail_id() + ",";
                }

                //for bcc
                for (int i = 0; i < item_list3.size(); i++) {
                    bcc_id += item_list3.get(i).getId() + ",";
                    bcc_name += item_list3.get(i).getName() + ",";
                    bcc_email += item_list3.get(i).getEmail_id() + ",";
                }

                //for cc
                for (int i = 0; i < item_list2.size(); i++) {
                    cc_id += item_list2.get(i).getId() + ",";
                    cc_name += item_list2.get(i).getName() + ",";
                    cc_email += item_list2.get(i).getEmail_id() + ",";
                }

                if (cc_id.length() > 0) {
                    cc_list = cc_id.substring(0, cc_id.length() - 1);
                }
                if (cc_name.length() > 0) {
                    cc_list_name = cc_name.substring(0, cc_name.length() - 1);
                }
                if (cc_email.length() > 0) {
                    c_email = cc_email.substring(0, cc_email.length() - 1);
                }

                if (bcc_id.length() > 0) {
                    bcc_list = bcc_id.substring(0, bcc_id.length() - 1);
                }
                if (bcc_name.length() > 0) {
                    bcc_list_name = bcc_name.substring(0, bcc_name.length() - 1);
                }
                if (bcc_email.length() > 0) {
                    b_email = bcc_email.substring(0, bcc_email.length() - 1);
                }

                if (people_id.length() > 0) {
                    people_list = people_id.substring(0, people_id.length() - 1);
                }
                if (people_name.length() > 0) {
                    people_list_name = people_name.substring(0, people_name.length() - 1);
                }
                if (people_email.length() > 0) {
                    p_email = people_email.substring(0, people_email.length() - 1);
                }

                if(Flag.equalsIgnoreCase("C")){
                    messagesend(people_list, people_list_name, p_email, bcc_list, bcc_list_name, b_email, cc_list, cc_list_name,
                            c_email, AttachmentFlag);
                }else if(Flag.equalsIgnoreCase("R")){
                    replysend(people_list, people_list_name, p_email, bcc_list, bcc_list_name, b_email, cc_list, cc_list_name,
                            c_email, AttachmentFlag,parentID);
                }else if(Flag.equalsIgnoreCase("RA")){
                    replyAllsend(people_list, people_list_name, p_email, bcc_list, bcc_list_name, b_email, cc_list, cc_list_name,
                            c_email, AttachmentFlag,parentID);
                }else if(Flag.equalsIgnoreCase("F")){
                    Forwardsend(people_list, people_list_name, p_email, bcc_list, bcc_list_name, b_email, cc_list, cc_list_name,
                            c_email, AttachmentFlag,parentID);
                }

                break;

            case R.id.ll_select_recipients:
                openTagDialog();
                break;

            case R.id.ll_select_cc:
                openTagPopupBranch();
                break;

            case R.id.ll_select_bcc:
                openTagPopuppeople();
                break;
        }
    }

    private void SelectFile() {
        Intent filePickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        filePickerIntent.setType("*/*");
        filePickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(filePickerIntent, PICKFILE_RESULT_CODE);


        //  Intent filePickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //   filePickerIntent.setType("*/*");
        //   filePickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //  startActivityForResult(filePickerIntent, PICKFILE_RESULT_CODE);
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
    /*            if (item_list.size() > 0) {
                   // tv_select_visible_department.setVisibility(View.GONE);
                    rv_recipients.setVisibility(View.VISIBLE);
                } else {
                    tv_select_visible_department.setVisibility(View.VISIBLE);
                    rv_recipients.setVisibility(View.GONE);
                }*/
                callOnBackPress();
                open_tag_dialog.dismiss();
            }
        });

        recyclerview1 = (RecyclerView) open_tag_dialog.findViewById(R.id.recyclerview1);
        recyclerview2 = (RecyclerView) open_tag_dialog.findViewById(R.id.recyclerview2);
        TextView header = (TextView) open_tag_dialog.findViewById(R.id.header);
        header.setText("Select Recipients");
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

        adapter1 = new Adapter1MessagesPeopleList1(activity2, beanMessagePeopleLists);
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
        recyclerview_adapter = new Adapter2MessagesPeople1(activity2, item_list);
        rv_onchangelistner();
    }
    public static void callOnBackPress2() {
        recyclerview_adapter2 = new Adapter2MessagesPeople2(activity2, item_list2);
        rv_onchangelistner2();
    }
    public static void rv_onchangelistner() {
        rv_recipients.setAdapter(recyclerview_adapter);
        rv_recipients.addOnItemTouchListener(
                new RecyclerItemClickListener(activity2, rv_recipients, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        openTagDialog();
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

        rv_select_cc.setAdapter(recyclerview_adapter2);
        rv_select_cc.addOnItemTouchListener(
                new RecyclerItemClickListener(activity2, rv_select_cc, new RecyclerItemClickListener.OnItemClickListener() {
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
    public static void callOnBackPress3() {
        recyclerview_adapter3 = new Adapter2MyEventsPeopleList(activity2, item_list3);
        rv_onchangelistner3();

    }
    public static void rv_onchangelistner3() {

        rv_select_bcc.setAdapter(recyclerview_adapter3);
        rv_select_bcc.addOnItemTouchListener(
                new RecyclerItemClickListener(activity2, rv_select_bcc, new RecyclerItemClickListener.OnItemClickListener() {
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
        header.setText("Select CC");
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
  /*              if (item_list2.size() > 0) {
                    tv_select_visible_branch.setVisibility(View.GONE);
                    rv_select_cc.setVisibility(View.VISIBLE);
                } else {
                    tv_select_visible_branch.setVisibility(View.VISIBLE);
                    rv_select_cc.setVisibility(View.GONE);
                }*/
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

        adapter1branch = new Adapter1MessagesPeopleList2(activity2, beanMessagePeopleLists);
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
        header.setText("Select BCC");
        header.setTypeface(Utility.getTypeFaceTab());
        LinearLayout lnmainback = (LinearLayout) open_tag_dialog_people.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        adapter1people = new Adapter1MessagesPeopleList3(activity2, beanMessagePeopleLists);
        recyclerview5.setAdapter(adapter1people);

        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                if (item_list3.size() > 0) {
                    //tv_select_person,rv_select_bcc
                    tv_select_person.setVisibility(View.GONE);
                    rv_select_bcc.setVisibility(View.VISIBLE);
                } else {
                    tv_select_person.setVisibility(View.VISIBLE);
                    rv_select_bcc.setVisibility(View.GONE);
                }*/

                callOnBackPress3();
                open_tag_dialog_people.dismiss();
            }
        });
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

    private void messagesend(final String people_ids, final String people_names, final String people_emails,
                             final String bcc_ids, final String bcc_names, final String bcc_emails,
                             final String cc_ids, final String cc_names, final String cc_emails, final String attachmentFlag) {
        ed_sub = edt4.getText().toString();
        ed_body = edt5.getText().toString();
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Compose, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Compose")) {
                            JSONArray array = object.getJSONArray("Message_Compose");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        String messageId = array.optJSONObject(i).getString("MessageId");

                                        if (attachmentFlag.equalsIgnoreCase("A")) {
                                            for (i = 0; i < modelPostImageLists.size(); i++) {
                                                if (i == modelPostImageLists.size() - 1) {
                                                    attachmentsend(messageId, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "L");
                                                    pd.dismiss();
                                                } else {
                                                    attachmentsend(messageId, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "F");
                                                }
                                            }
                                        } else {
                                            Intent intent1 = new Intent(activity, Message.class);
                                            startActivity(intent1);
                                            finish();
                                        }

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
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
                    map.put("PeopleIds", people_ids);
                    map.put("PeopleEmails", people_emails);
                    map.put("PeopleNames", people_names);
                    map.put("BccIds", bcc_ids);
                    map.put("BccEmails", bcc_emails);
                    map.put("BccNames", bcc_names);
                    map.put("CcIds", cc_ids);
                    map.put("CcEmails", cc_emails);
                    map.put("CcNames", cc_names);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Subject", ed_sub);
                    map.put("Body", ed_body);
                    map.put("AttachmentFlag", attachmentFlag);
                    map.put("LoginName", Utility.getFullNamePreference());
                    map.put("LoginUserProfileImage", Utility.getPeopleProfileImgPreference());

                    Log.e("PeopleIds", "" + people_ids);
                    Log.e("PeopleEmails", "" + people_emails);
                    Log.e("PeopleNames", "" + people_names);
                    Log.e("BccIds", "" + bcc_ids);
                    Log.e("BccEmails", "" + bcc_emails);
                    Log.e("BccNames", "" + bcc_names);
                    Log.e("CcIds", "" + cc_ids);
                    Log.e("CcEmails", "" + cc_emails);
                    Log.e("CcNames", "" + cc_names);
                    Log.e("LoginId", "" + Utility.getPeopleIdPreference());
                    Log.e("Subject", "" + ed_sub);
                    Log.e("Body", "" + ed_body);
                    Log.e("AttachmentFlag", "" + attachmentFlag);
                    Log.e("LoginName", "" + Utility.getFullNamePreference());
                    Log.e("LoginUserProfileImage", "" + Utility.getPeopleProfileImgPreference());

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


    private void attachmentsend(final String message_id, final String image_path, final String image_name,
                                final String image_extension, final String check) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Compose_Attachment_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Compose_Attachment_Add")) {
                            JSONArray array = object.getJSONArray("Message_Compose_Attachment_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        if (check.equalsIgnoreCase("L")) {
                                            Intent intent1 = new Intent(activity, Message.class);
                                            startActivity(intent1);
                                            finish();
                                        }

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
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

                    map.put("MessageId", message_id);
                    map.put("FilePath", image_path);
                    map.put("FilePathName", image_name);
                    map.put("FileExtension", image_extension);
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
    public void onBackPressed() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && null != data) {
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
                //  arrayList_file_name.add(filename);
                // arrayList_file_extension.add(file_extension);
                Log.e("onActivityResult: ", "path++++++++++++" + path);
                Log.e("log", "64+++++++++++++++" + readFileAsBase64String(path));
                // arrayList_file_base64.add(readFileAsBase64String(path));
                ModelPostImageList temp = new ModelPostImageList();
                temp.setBitmap(bm);
                temp.setImagepath(readFileAsBase64String(path));
                temp.setImage_name(filename);
                temp.setImage_extension(file_extension);
                temp.setFlag("N");
                fieldtype = "N";
                modelPostImageLists.add(temp);

                adapterMessagesAttachment = new AdapterMessagesAttachment(activity, modelPostImageLists);
                rv_select_file.setAdapter(adapterMessagesAttachment);
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
                        //arrayList_file_name.add(filename);
                        //  arrayList_file_extension.add(file_extension);
                        Log.e("onActivityResult: ", "path++++++++++++" + path);
                        Log.e("log", "64+++++++++++++++" + readFileAsBase64String(path));
                        // arrayList_file_base64.add(readFileAsBase64String(path));

                        ModelPostImageList temp = new ModelPostImageList();
                        temp.setBitmap(bitmap);
                        temp.setImagepath(readFileAsBase64String(path));
                        temp.setImage_name(filename);
                        temp.setImage_extension(file_extension);
                        temp.setFlag("N");
                        fieldtype = "N";
                        modelPostImageLists.add(temp);

                        adapterMessagesAttachment = new AdapterMessagesAttachment(activity, modelPostImageLists);
                        rv_select_file.setAdapter(adapterMessagesAttachment);
                        Log.e("size arraylist", "++++++++++++" + modelPostImageLists.size());

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
    @Nullable
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

    private void replysend(final String people_ids, final String people_names, final String people_emails,
                             final String bcc_ids, final String bcc_names, final String bcc_emails,
                             final String cc_ids, final String cc_names, final String cc_emails, final String attachmentFlag,
                           final String parent_id) {
        ed_sub = edt4.getText().toString();
        ed_body = edt5.getText().toString();
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Replay, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Replay")) {
                            JSONArray array = object.getJSONArray("Message_Replay");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        String messageId = array.optJSONObject(i).getString("MessageId");

                                        if (attachmentFlag.equalsIgnoreCase("A")) {
                                            for (i = 0; i < modelPostImageLists.size(); i++) {
                                                if (i == modelPostImageLists.size() - 1) {
                                                    replyattachment(messageId, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "L");
                                                    pd.dismiss();
                                                } else {
                                                    replyattachment(messageId, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "F");
                                                }
                                            }
                                        } else {
                                            Intent intent1 = new Intent(activity, Message.class);
                                            startActivity(intent1);
                                            finish();
                                        }

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
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
                    map.put("PeopleIds", people_ids);
                    map.put("PeopleEmails", people_emails);
                    map.put("PeopleNames", people_names);
                    map.put("BccIds", bcc_ids);
                    map.put("BccEmails", bcc_emails);
                    map.put("BccNames", bcc_names);
                    map.put("CcIds", cc_ids);
                    map.put("CcEmails", cc_emails);
                    map.put("CcNames", cc_names);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Subject", ed_sub);
                    map.put("Body", ed_body);
                    map.put("ParentId", parent_id);
                    map.put("AttachmentFlag", attachmentFlag);
                    map.put("LoginName", Utility.getFullNamePreference());
                    map.put("LoginUserProfileImage", Utility.getPeopleProfileImgPreference());

                    Log.e("PeopleIds", "" + people_ids);
                    Log.e("PeopleEmails", "" + people_emails);
                    Log.e("PeopleNames", "" + people_names);
                    Log.e("BccIds", "" + bcc_ids);
                    Log.e("BccEmails", "" + bcc_emails);
                    Log.e("BccNames", "" + bcc_names);
                    Log.e("CcIds", "" + cc_ids);
                    Log.e("CcEmails", "" + cc_emails);
                    Log.e("CcNames", "" + cc_names);
                    Log.e("LoginId", "" + Utility.getPeopleIdPreference());
                    Log.e("Subject", "" + ed_sub);
                    Log.e("Body", "" + ed_body);
                    Log.e("ParentId",""+ parent_id);
                    Log.e("AttachmentFlag", "" + attachmentFlag);
                    Log.e("LoginName", "" + Utility.getFullNamePreference());
                    Log.e("LoginUserProfileImage", "" + Utility.getPeopleProfileImgPreference());

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

    private void replyattachment(final String message_id, final String image_path, final String image_name,
                                final String image_extension, final String check) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Replay_Attachment_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Replay_Attachment_Add")) {
                            JSONArray array = object.getJSONArray("Message_Replay_Attachment_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        if (check.equalsIgnoreCase("L")) {
                                            Intent intent1 = new Intent(activity, Message.class);
                                            startActivity(intent1);
                                            finish();
                                        }

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
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

                    map.put("MessageId", message_id);
                    map.put("FilePath", image_path);
                    map.put("FilePathName", image_name);
                    map.put("FileExtension", image_extension);
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


    private void replyAllsend(final String people_ids, final String people_names, final String people_emails,
                           final String bcc_ids, final String bcc_names, final String bcc_emails,
                           final String cc_ids, final String cc_names, final String cc_emails, final String attachmentFlag,
                           final String parent_id) {
        ed_sub = edt4.getText().toString();
        ed_body = edt5.getText().toString();
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Replay_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Replay_All")) {
                            JSONArray array = object.getJSONArray("Message_Replay_All");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        String messageId = array.optJSONObject(i).getString("MessageId");

                                        if (attachmentFlag.equalsIgnoreCase("A")) {
                                            for (i = 0; i < modelPostImageLists.size(); i++) {
                                                if (i == modelPostImageLists.size() - 1) {
                                                    replyattachment(messageId, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "L");
                                                    pd.dismiss();
                                                } else {
                                                    replyattachment(messageId, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "F");
                                                }
                                            }
                                        } else {
                                            Intent intent1 = new Intent(activity, Message.class);
                                            startActivity(intent1);
                                            finish();
                                        }
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
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
                    map.put("PeopleIds", people_ids);
                    map.put("PeopleEmails", people_emails);
                    map.put("PeopleNames", people_names);
                    map.put("BccIds", bcc_ids);
                    map.put("BccEmails", bcc_emails);
                    map.put("BccNames", bcc_names);
                    map.put("CcIds", cc_ids);
                    map.put("CcEmails", cc_emails);
                    map.put("CcNames", cc_names);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Subject", ed_sub);
                    map.put("Body", ed_body);
                    map.put("ParentId", parent_id);
                    map.put("AttachmentFlag", attachmentFlag);
                    map.put("LoginName", Utility.getFullNamePreference());
                    map.put("LoginUserProfileImage", Utility.getPeopleProfileImgPreference());

                    Log.e("PeopleIds", "" + people_ids);
                    Log.e("PeopleEmails", "" + people_emails);
                    Log.e("PeopleNames", "" + people_names);
                    Log.e("BccIds", "" + bcc_ids);
                    Log.e("BccEmails", "" + bcc_emails);
                    Log.e("BccNames", "" + bcc_names);
                    Log.e("CcIds", "" + cc_ids);
                    Log.e("CcEmails", "" + cc_emails);
                    Log.e("CcNames", "" + cc_names);
                    Log.e("LoginId", "" + Utility.getPeopleIdPreference());
                    Log.e("Subject", "" + ed_sub);
                    Log.e("Body", "" + ed_body);
                    Log.e("ParentId",""+ parent_id);
                    Log.e("AttachmentFlag", "" + attachmentFlag);
                    Log.e("LoginName", "" + Utility.getFullNamePreference());
                    Log.e("LoginUserProfileImage", "" + Utility.getPeopleProfileImgPreference());

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


    private void Forwardsend(final String people_ids, final String people_names, final String people_emails,
                               final String bcc_ids, final String bcc_names, final String bcc_emails,
                               final String cc_ids, final String cc_names, final String cc_emails, final String attachmentFlag,
                               final String parent_id) {
            ed_sub = edt4.getText().toString();
            ed_body = edt5.getText().toString();
            if (checkConnectivity()) {
                pd.show();
                StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Forward, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("res", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.has("Message_Forward")) {
                                JSONArray array = object.getJSONArray("Message_Forward");
                                if (array.length() != 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                            Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                            String messageId = array.optJSONObject(i).getString("MessageId");

                                            if (attachmentFlag.equalsIgnoreCase("A")) {
                                                for (i = 0; i < modelPostImageLists.size(); i++) {
                                                    if (i == modelPostImageLists.size() - 1) {
                                                        forwardattachment(messageId, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "L",modelPostImageLists.get(i).getFlag());
                                                        pd.dismiss();
                                                    } else {
                                                        forwardattachment(messageId, modelPostImageLists.get(i).getImagepath(), modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImage_extension(), "F",modelPostImageLists.get(i).getFlag());
                                                    }
                                                }
                                            } else {
                                                Intent intent1 = new Intent(activity, Message.class);
                                                startActivity(intent1);
                                                finish();
                                            }
                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        }
                                    }
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
                        map.put("PeopleIds", people_ids);
                        map.put("PeopleEmails", people_emails);
                        map.put("PeopleNames", people_names);
                        map.put("BccIds", bcc_ids);
                        map.put("BccEmails", bcc_emails);
                        map.put("BccNames", bcc_names);
                        map.put("CcIds", cc_ids);
                        map.put("CcEmails", cc_emails);
                        map.put("CcNames", cc_names);
                        map.put("LoginId", Utility.getPeopleIdPreference());
                        map.put("Subject", ed_sub);
                        map.put("Body", ed_body);
                        map.put("ParentId", parent_id);
                        map.put("AttachmentFlag", attachmentFlag);
                        map.put("LoginName", Utility.getFullNamePreference());
                        map.put("LoginUserProfileImage", Utility.getPeopleProfileImgPreference());

                        Log.e("PeopleIds", "" + people_ids);
                        Log.e("PeopleEmails", "" + people_emails);
                        Log.e("PeopleNames", "" + people_names);
                        Log.e("BccIds", "" + bcc_ids);
                        Log.e("BccEmails", "" + bcc_emails);
                        Log.e("BccNames", "" + bcc_names);
                        Log.e("CcIds", "" + cc_ids);
                        Log.e("CcEmails", "" + cc_emails);
                        Log.e("CcNames", "" + cc_names);
                        Log.e("LoginId", "" + Utility.getPeopleIdPreference());
                        Log.e("Subject", "" + ed_sub);
                        Log.e("Body", "" + ed_body);
                        Log.e("ParentId",""+ parent_id);
                        Log.e("AttachmentFlag", "" + attachmentFlag);
                        Log.e("LoginName", "" + Utility.getFullNamePreference());
                        Log.e("LoginUserProfileImage", "" + Utility.getPeopleProfileImgPreference());

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


    private void forwardattachment(final String message_id, final String image_path, final String image_name,
                                 final String image_extension, final String check,final String flag) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Forward_Attachment_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Forward_Attachment_Add")) {
                            JSONArray array = object.getJSONArray("Message_Forward_Attachment_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        if (check.equalsIgnoreCase("L")) {
                                            Intent intent1 = new Intent(activity, Message.class);
                                            startActivity(intent1);
                                            finish();
                                        }

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
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

                    map.put("MessageId", message_id);
                    map.put("FilePath", image_path);
                    map.put("FilePathName", image_name);
                    map.put("FileExtension", image_extension);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("FileType",flag);

                    Log.e("MessageId", message_id);
                    Log.e("FilePath", image_path);
                    Log.e("FilePathName", image_name);
                    Log.e("FileExtension", image_extension);
                    Log.e("LoginId", Utility.getPeopleIdPreference());
                    Log.e("FileType","" + flag);

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


    public void attach(){
        adapterMessagesAttachment = new AdapterMessagesAttachment(activity, modelPostImageLists);
        rv_select_file.setAdapter(adapterMessagesAttachment);
    }

}
