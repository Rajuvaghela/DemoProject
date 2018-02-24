package com.ext.adarsh.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
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
import com.ext.adarsh.Adapter.Adapter1TakeActionPeopleList;
import com.ext.adarsh.Adapter.Adapter2MyEventsPeopleList;
import com.ext.adarsh.Adapter.AdapterApprovals;
import com.ext.adarsh.Adapter.AdapterApprovalsPeople;
import com.ext.adarsh.Adapter.AdapterApprovalsPeopleByDep;
import com.ext.adarsh.Adapter.AdapterDepartment;
import com.ext.adarsh.Adapter.AdapterReviewPeople;
import com.ext.adarsh.Bean.BeanApprovalsApprovalPeople;
import com.ext.adarsh.Bean.BeanApprovalsFrom;
import com.ext.adarsh.Bean.BeanApprovalsReviewPeople;
import com.ext.adarsh.Bean.BeanDepartmentList;
import com.ext.adarsh.Bean.BeanForwardForCommentRequestArray;
import com.ext.adarsh.Bean.BeanPeopleSelectByDepartmentId;
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

public class TakeActionActivity extends AppCompatActivity {
    Activity activity;
    ProgressDialog pd;
    ArrayList<String> categories, categories2, categories3;
    ArrayList<String> categories_3F, categories_2R, categories_select_res;
    ArrayList<String> sub_categories2;
    public static Spinner spiner_acton_type1, spiner_acton_type2, spiner_acton_type3, spinner_people_list;
    Spinner sub_spiner2, spiner_acton_type2R, spiner_acton_type3F, spiner_forward_comment_res;
    String approvalFromId = "";
    List<BeanApprovalsReviewPeople> beanApprovalsReviewPeoples = new ArrayList<>();
    AdapterReviewPeople adapterReviewPeople;
    public static List<BeanApprovalsApprovalPeople> beanApprovalsApprovalPeoples = new ArrayList<>();
    private static final int PICKFILE_RESULT_CODE = 1;
    public static AdapterApprovalsPeople adapterApprovalsPeople;
    public AdapterApprovalsPeopleByDep adapterApprovalsPeopleByDep;
    String fileBase64 = "";
    String filename = "";
    String file_extension = "";
    TextView tv_attachment;
    EditText et_some_note, et_password;
    String approval_id = "", spinnervalueset = "";
    String spiner_people_name = "", spiner_people_id = "";
    String spiner1_TakeActionId = "", spiner1_TaskActionTitle = "";
    String spiner2_TakeActionId, spiner2_TaskActionTitle = "";
    private String TakeActionId, TaskActionTitle = "";
    @BindView(R.id.ll_people)
    LinearLayout ll_people;
    boolean can_select_people = false;
    ArrayList<BeanApprovalsFrom> beanApprovalsFroms_list = new ArrayList<>();
    Spinner spinner_department_list;
    public List<BeanDepartmentList> department_List = new ArrayList<>();
    public List<BeanForwardForCommentRequestArray> beanForwardForCommentRequestArrays = new ArrayList<>();
    AdapterDepartment adapter_department;
    private String department_id = "";
    static RecyclerView rv_select_person;
    static ProgressDialog pd2;
    static Activity activity2;
    static Dialog open_tag_dialog_people;
    public static RecyclerView.Adapter recyclerview_adapter3;
    static RecyclerView recyclerview6;
    static RecyclerView recyclerview5;
    static RecyclerView.LayoutManager recylerViewLayoutManager3;
    private static ArrayList<BeanPeopleSelectByDepartmentId> contact_list = new ArrayList<>();
    static Adapter1TakeActionPeopleList adapter1people;
    public static List<ModelClass3> item_list3 = new ArrayList<>();
    static TextView tv_select_person;
    public static RecyclerView.Adapter recyclerview_adapter;
    LinearLayout lnpassword;
    LinearLayout ll_sub_spiner2, ll_department, ll_select_person;
    LinearLayout ll_spiner2R, ll_spiner3F, ll_forward_comment_spiner2;
    String Response_Id = "", Response_Title = "";
    String RequestSendTo = "", RequestSendToId = "";
    String ApprovalFrom = "", ApprovalFromId = "";
    String Priority = "";
    String Forward_For_Comment_Flag = "", Recommended_For_Approval_Flag = "";
    String req_send_to_name = "";
    String req_send_to_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_action);

        ButterKnife.bind(this);
        activity = this;
        activity2 = this;
        pd = Utility.getDialog(activity);
        pd2 = Utility.getDialog(activity2);


        Bundle bundle = getIntent().getExtras();
        approval_id = bundle.getString("approval_id");
        spinnervalueset = bundle.getString("spinnervalue");


        categories_3F = new ArrayList<>();
        categories_3F.add("Please Select Action");
        categories_3F.add("Forwarded for comments");
        categories_3F.add("Recommended for approval");

        categories_select_res = new ArrayList<>();
        categories_select_res.add("No response");
        categories_select_res.add("Comment");
        categories_select_res.add("Comment with approval");
        categories_select_res.add("Comment with rejection");

        categories_2R = new ArrayList<>();
        categories_2R.add("No response");
        categories_2R.add("Approve");
        categories_2R.add("Reject");

        categories = new ArrayList<>();
        categories.add("Please Select Action");
        categories.add("Approve");
        categories.add("Reject");
        categories.add("Forwarded for comments");
        categories.add("Recommended for approval");

        categories2 = new ArrayList<>();
        categories2.add("Please Select Action");
        categories2.add("Forwarded for comments");
        categories2.add("Recommended for approval");

        sub_categories2 = new ArrayList<>();
        sub_categories2.add("Please Select Response");
        sub_categories2.add("No response");
        sub_categories2.add("Approve");
        sub_categories2.add("Reject");


        categories3 = new ArrayList<>();
        categories3.add("Please Select Action");
        categories3.add("Comment with file");
        beanApprovalsFroms_list = AdapterApprovals.beanApprovalsFroms_list;
        rv_select_person = (RecyclerView) findViewById(R.id.rv_select_person);
        FlowLayoutManager flowLayoutManager3 = new FlowLayoutManager();
        flowLayoutManager3.setAutoMeasureEnabled(true);
        rv_select_person.setLayoutManager(flowLayoutManager3);
        LinearLayout lnmainback = (LinearLayout) findViewById(R.id.lnmainback);
        LinearLayout ll_spiner3 = (LinearLayout) findViewById(R.id.ll_spiner3);
        LinearLayout ll_spiner2 = (LinearLayout) findViewById(R.id.ll_spiner2);
        LinearLayout ll_spiner1 = (LinearLayout) findViewById(R.id.ll_spiner1);
        ll_sub_spiner2 = (LinearLayout) findViewById(R.id.ll_sub_spiner2);
        ll_department = (LinearLayout) findViewById(R.id.ll_department);
        ll_select_person = (LinearLayout) findViewById(R.id.ll_select_person);
        ll_spiner2R = (LinearLayout) findViewById(R.id.ll_spiner2R);
        ll_spiner3F = (LinearLayout) findViewById(R.id.ll_spiner3F);
        ll_forward_comment_spiner2 = (LinearLayout) findViewById(R.id.ll_forward_comment_spiner2);

        lnpassword = (LinearLayout) findViewById(R.id.lnpassword);

        RelativeLayout rl_select_file = (RelativeLayout) findViewById(R.id.rl_select_file);
        spinner_department_list = (Spinner) findViewById(R.id.spinner_department_list);
        spinner_people_list = (Spinner) findViewById(R.id.spinner_people_list);
        spiner_acton_type1 = (Spinner) findViewById(R.id.spiner_acton_type1);
        spiner_acton_type2 = (Spinner) findViewById(R.id.spiner_acton_type2);
        sub_spiner2 = (Spinner) findViewById(R.id.sub_spiner2);
        spiner_acton_type3 = (Spinner) findViewById(R.id.spiner_acton_type3);
        spiner_acton_type2R = (Spinner) findViewById(R.id.spiner_acton_type2R);
        spiner_acton_type3F = (Spinner) findViewById(R.id.spiner_acton_type3F);
        spiner_forward_comment_res = (Spinner) findViewById(R.id.spiner_forward_comment_res);


        TextView tv_reg_heading = (TextView) findViewById(R.id.tv_reg_heading);
        TextView tv8 = (TextView) findViewById(R.id.tv8);
        TextView tv9 = (TextView) findViewById(R.id.tv9);
        TextView tv10 = (TextView) findViewById(R.id.tv10);
        TextView tv11 = (TextView) findViewById(R.id.tv11);
        TextView tv_select_attachment = (TextView) findViewById(R.id.tv_select_attachment);

        tv_select_person = (TextView) findViewById(R.id.tv_select_person);
        ll_select_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagPopuppeople();
            }
        });

        TextView tv_next1 = (TextView) findViewById(R.id.tv_next1);
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_attachment = (TextView) findViewById(R.id.tv_attachment);
        et_some_note = (EditText) findViewById(R.id.et_some_note);
        et_password = (EditText) findViewById(R.id.et_password);
        et_some_note.addTextChangedListener(new MyTextWatcher(et_some_note));
        tv_next1.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        tv_reg_heading.setTypeface(Utility.getTypeFaceTab());
        et_some_note.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv10.setTypeface(Utility.getTypeFace());
        tv11.setTypeface(Utility.getTypeFace());
        tv_select_attachment.setTypeface(Utility.getTypeFace());
        tv_select_attachment.setTypeface(Utility.getTypeFace());
        tv_select_attachment.setTypeface(Utility.getTypeFace());


        ArrayAdapter<String> dataAdapter_comment_res = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories_select_res);
        dataAdapter_comment_res.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_forward_comment_res.setAdapter(dataAdapter_comment_res);
        spiner_forward_comment_res.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Response_Id = "";
                    Response_Title = "";
                } else if (i == 1) {
                    Response_Id = "1";
                    Response_Title = "Comment";
                } else if (i == 2) {
                    Response_Id = "2";
                    Response_Title = "Comment with approval";
                } else if (i == 3) {
                    Response_Id = "3";
                    Response_Title = "Comment with rejection";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> dataAdapter2R = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories_2R);
        dataAdapter2R.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_acton_type2R.setAdapter(dataAdapter2R);
        spiner_acton_type2R.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Response_Id = "";
                    Response_Title = "";
                } else if (i == 1) {
                    Response_Id = "1";
                    Response_Title = "Approve";
                } else if (i == 2) {
                    Response_Id = "2";
                    Response_Title = "Reject";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter<String> dataAdapter3F = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories_3F);
        dataAdapter3F.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_acton_type3F.setAdapter(dataAdapter3F);
        spiner_acton_type3F.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    spiner1_TakeActionId = "";
                    spiner1_TaskActionTitle = "";
                    ll_forward_comment_spiner2.setVisibility(View.GONE);
                    ll_spiner2R.setVisibility(View.GONE);
                } else if (i == 1) {
                    spiner1_TakeActionId = "3";
                    spiner1_TaskActionTitle = "Forwarded for Comments";
                    Forward_For_Comment_Flag = "A";
                    Recommended_For_Approval_Flag = "";
                    ll_forward_comment_spiner2.setVisibility(View.VISIBLE);
                    ll_spiner2R.setVisibility(View.GONE);
                } else if (i == 2) {
                    spiner1_TakeActionId = "4";
                    Recommended_For_Approval_Flag = "A";
                    Forward_For_Comment_Flag = "";
                    spiner1_TaskActionTitle = "Recommended for Approval";
                    ll_forward_comment_spiner2.setVisibility(View.GONE);
                    ll_spiner2R.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_acton_type1.setAdapter(dataAdapter);
        spiner_acton_type1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Forward_For_Comment_Flag = "";
                    Recommended_For_Approval_Flag = "";
                    spiner1_TakeActionId = "";
                    spiner1_TaskActionTitle = "";
                } else if (i == 1) {
                    spiner1_TakeActionId = "1";
                    spiner1_TaskActionTitle = "Approve";
                    can_select_people = false;
                    ll_people.setVisibility(View.VISIBLE);
                    lnpassword.setVisibility(View.VISIBLE);
                    ll_select_person.setVisibility(View.GONE);
                    Forward_For_Comment_Flag = "";
                    Recommended_For_Approval_Flag = "";
                } else if (i == 2) {
                    Forward_For_Comment_Flag = "";
                    Recommended_For_Approval_Flag = "";
                    spiner1_TakeActionId = "2";
                    spiner1_TaskActionTitle = "Reject";
                    can_select_people = false;
                    ll_people.setVisibility(View.VISIBLE);
                    lnpassword.setVisibility(View.VISIBLE);
                    ll_select_person.setVisibility(View.GONE);
                } else if (i == 3) {
                    spiner1_TakeActionId = "3";
                    spiner1_TaskActionTitle = "Forwarded for Comments";
                    Forward_For_Comment_Flag = "";
                    Recommended_For_Approval_Flag = "";
                    can_select_people = true;
                    ll_people.setVisibility(View.GONE);
                    lnpassword.setVisibility(View.GONE);
                    ll_sub_spiner2.setVisibility(View.GONE);
                    ll_select_person.setVisibility(View.VISIBLE);
                } else if (i == 4) {
                    spiner1_TakeActionId = "4";
                    spiner1_TaskActionTitle = "Recommended for Approval";
                    Forward_For_Comment_Flag = "";
                    Recommended_For_Approval_Flag = "";
                    can_select_people = true;
                    ll_people.setVisibility(View.GONE);
                    lnpassword.setVisibility(View.GONE);
                    ll_sub_spiner2.setVisibility(View.GONE);
                    ll_select_person.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_department_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    department_id = "";
                } else {
                    department_id = department_List.get(i).departmentId;
                    Log.e("dep_id", department_id);
                    item_list3.clear();
                    getPeopleDetaSpiner(department_id);
                    getPeopleData(department_id);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_acton_type2.setAdapter(dataAdapter2);
        spiner_acton_type2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    spiner1_TakeActionId = "";
                    spiner1_TaskActionTitle = "";
                    Forward_For_Comment_Flag = "";
                    Recommended_For_Approval_Flag = "";
                    ll_sub_spiner2.setVisibility(View.GONE);
                } else if (i == 1) {
                    spiner1_TakeActionId = "3";
                    spiner1_TaskActionTitle = "Forwarded for Comments";
                    Response_Id = "1";
                    Response_Title = "Comment";
                    Forward_For_Comment_Flag = "";
                    Recommended_For_Approval_Flag = "";
                    ll_sub_spiner2.setVisibility(View.GONE);
                } else if (i == 2) {
                    spiner1_TakeActionId = "4";
                    spiner1_TaskActionTitle = "Recommended for Approval";
                    Response_Id = "2";
                    Response_Title = "Comment";
                    Forward_For_Comment_Flag = "";
                    Recommended_For_Approval_Flag = "";
                    ll_sub_spiner2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, sub_categories2);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sub_spiner2.setAdapter(dataAdapter4);
        sub_spiner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 1) {

                } else if (i == 2) {
                    Response_Id = "0";
                    Response_Title = "No Response";
                } else if (i == 3) {
                    Response_Id = "1";
                    Response_Title = "Approve";
                } else if (i == 4) {
                    Response_Id = "2";
                    Response_Title = "Reject";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories3);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_acton_type3.setAdapter(dataAdapter3);
        spiner_acton_type3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    spiner1_TakeActionId = "";
                    spiner1_TaskActionTitle = "";
                } else if (i == 1) {
                    spiner1_TakeActionId = "5";
                    spiner1_TaskActionTitle = "Comment with file";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile();
            }
        });
        if (spinnervalueset.equals("2R")) {
            ll_spiner2R.setVisibility(View.VISIBLE);
            ll_spiner3F.setVisibility(View.GONE);
            ll_spiner1.setVisibility(View.GONE);
            ll_spiner2.setVisibility(View.GONE);
            ll_spiner3.setVisibility(View.GONE);
            ll_sub_spiner2.setVisibility(View.GONE);
            ll_people.setVisibility(View.GONE);
            ll_forward_comment_spiner2.setVisibility(View.GONE);
        }
        if (spinnervalueset.equals("3F")) {
            ll_forward_comment_spiner2.setVisibility(View.VISIBLE);
            ll_spiner3F.setVisibility(View.VISIBLE);
            ll_spiner2R.setVisibility(View.GONE);
            ll_spiner1.setVisibility(View.GONE);
            ll_spiner2.setVisibility(View.GONE);
            ll_spiner3.setVisibility(View.GONE);
            ll_sub_spiner2.setVisibility(View.GONE);
            ll_people.setVisibility(View.GONE);
        } else if (spinnervalueset.equals("1")) {
            ll_spiner1.setVisibility(View.GONE);
            ll_spiner2.setVisibility(View.GONE);
            ll_sub_spiner2.setVisibility(View.GONE);
            ll_department.setVisibility(View.GONE);
            ll_people.setVisibility(View.GONE);
            ll_select_person.setVisibility(View.GONE);
            ll_spiner2R.setVisibility(View.GONE);
            ll_spiner3F.setVisibility(View.GONE);
            ll_forward_comment_spiner2.setVisibility(View.GONE);
            ll_spiner3.setVisibility(View.VISIBLE);

        } else if (spinnervalueset.equals("2")) {
            ll_spiner1.setVisibility(View.GONE);
            ll_spiner2.setVisibility(View.VISIBLE);
            ll_spiner3.setVisibility(View.GONE);
            ll_spiner2R.setVisibility(View.GONE);
            ll_spiner3F.setVisibility(View.GONE);
            ll_forward_comment_spiner2.setVisibility(View.GONE);

        } else {
            ll_spiner1.setVisibility(View.VISIBLE);
            ll_spiner2.setVisibility(View.GONE);
            ll_spiner3.setVisibility(View.GONE);
            ll_spiner2R.setVisibility(View.GONE);
            ll_spiner3F.setVisibility(View.GONE);
            ll_sub_spiner2.setVisibility(View.GONE);
            ll_forward_comment_spiner2.setVisibility(View.GONE);
        }


        tv_next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p_name = "";
                String p_id = "";

                for (int i = 0; i < item_list3.size(); i++) {
                    p_name += item_list3.get(i).getName() + ",";
                    p_id += item_list3.get(i).getId() + ",";
                }
                if (p_name.length() > 0) {
                    req_send_to_name = p_name.substring(0, p_name.length() - 1);
                }
                if (p_id.length() > 0) {
                    req_send_to_id = p_id.substring(0, p_id.length() - 1);
                }

                if (spinnervalueset.equals("2R")) {
                    if (spiner1_TakeActionId.equalsIgnoreCase("")) {
                        Toast.makeText(activity, "select action", Toast.LENGTH_SHORT).show();
                    } else if (spiner1_TakeActionId.equalsIgnoreCase("3")) {
                        Verification3();
                    } else if (spiner1_TakeActionId.equalsIgnoreCase("4")) {
                        Verification3();
                    }
                }
                if (spinnervalueset.equals("3F")) {
                    if (spiner1_TakeActionId.equalsIgnoreCase("")) {
                        Toast.makeText(activity, "select action", Toast.LENGTH_SHORT).show();
                    } else if (spiner1_TakeActionId.equalsIgnoreCase("3")) {
                        Verification3();
                    } else if (spiner1_TakeActionId.equalsIgnoreCase("4")) {
                        Verification3();
                    }
                } else if (spinnervalueset.equals("1")) {
                    if (spiner1_TakeActionId.equalsIgnoreCase("")) {
                        Toast.makeText(activity, "select action", Toast.LENGTH_SHORT).show();
                    } else if (spiner1_TakeActionId.equalsIgnoreCase("5")) {
                        Verification4();
                    }
                } else if (spinnervalueset.equals("2")) {
                    if (spiner1_TakeActionId.equalsIgnoreCase("")) {
                        Toast.makeText(activity, "select action", Toast.LENGTH_SHORT).show();
                    } else if (spiner1_TakeActionId.equalsIgnoreCase("3")) {
                        Verification3();
                    } else if (spiner1_TakeActionId.equalsIgnoreCase("4")) {
                        Verification3();
                    }
                } else {
                    if (spiner1_TakeActionId.equalsIgnoreCase("")) {
                        Toast.makeText(activity, "select action", Toast.LENGTH_SHORT).show();

                    } else if (spiner1_TakeActionId.equalsIgnoreCase("1")) {
                        Verification1();

                    } else if (spiner1_TakeActionId.equalsIgnoreCase("2")) {
                        Verification1();
                    } else if (spiner1_TakeActionId.equalsIgnoreCase("3")) {
                        Verificaton2();
                    } else if (spiner1_TakeActionId.equalsIgnoreCase("4")) {
                        Verificaton2();
                    } else if (department_id.equalsIgnoreCase("")) {

                    }
                }


         /*       ApprovalActionAdd(approval_id, et_some_note.getText().toString(), spiner1_TakeActionId,
                        spiner1_TaskActionTitle, Response_Id, Response_Title, req_send_to_name, req_send_to_id,
                        ApprovalFrom, ApprovalFromId, Priority, et_password.getText().toString(),
                        spiner_people_id, spiner_people_name, Forward_For_Comment_Flag, Recommended_For_Approval_Flag);*/
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getApprovalMoreData(approval_id);
    }

    private void Verification4() {
        if (et_some_note.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "enter note", Toast.LENGTH_SHORT).show();
        } else {
            ApprovalActionAdd(approval_id, et_some_note.getText().toString(), spiner1_TakeActionId,
                    spiner1_TaskActionTitle, Response_Id, Response_Title, req_send_to_name, req_send_to_id,
                    ApprovalFrom, ApprovalFromId, Priority, et_password.getText().toString(),
                    spiner_people_id, spiner_people_name, Forward_For_Comment_Flag, Recommended_For_Approval_Flag);
        }
    }

    private void Verification3() {
        if (department_id.equalsIgnoreCase("")) {
            Toast.makeText(activity, "select department", Toast.LENGTH_SHORT).show();
        } else if (req_send_to_name.length() <= 0) {
            //select person
            Toast.makeText(activity, "select people", Toast.LENGTH_SHORT).show();
        } else if (et_some_note.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "enter note", Toast.LENGTH_SHORT).show();
        } else {
            ApprovalActionAdd(approval_id, et_some_note.getText().toString(), spiner1_TakeActionId,
                    spiner1_TaskActionTitle, Response_Id, Response_Title, req_send_to_name, req_send_to_id,
                    ApprovalFrom, ApprovalFromId, Priority, et_password.getText().toString(),
                    spiner_people_id, spiner_people_name, Forward_For_Comment_Flag, Recommended_For_Approval_Flag);
        }

    }

    private void Verificaton2() {
        if (department_id.equalsIgnoreCase("")) {
            Toast.makeText(activity, "select dep", Toast.LENGTH_SHORT).show();
        } else if (req_send_to_name.length() <= 0) {
            //select person
            Toast.makeText(activity, "select people", Toast.LENGTH_SHORT).show();
        } else if (et_some_note.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "enter note", Toast.LENGTH_SHORT).show();
        } else {
            ApprovalActionAdd(approval_id, et_some_note.getText().toString(), spiner1_TakeActionId,
                    spiner1_TaskActionTitle, Response_Id, Response_Title, req_send_to_name, req_send_to_id,
                    ApprovalFrom, ApprovalFromId, Priority, et_password.getText().toString(),
                    spiner_people_id, spiner_people_name, Forward_For_Comment_Flag, Recommended_For_Approval_Flag);
        }
    }

    private void Verification1() {
        if (et_password.getText().toString().equalsIgnoreCase("")) {
            //pleasee select right password
            Toast.makeText(activity, "Enter password", Toast.LENGTH_SHORT).show();
        } else if (!et_password.getText().toString().equalsIgnoreCase(Utility.getPeoplePasswordPreference())) {
            Toast.makeText(activity, "Enter Corrent Password", Toast.LENGTH_SHORT).show();
        } else if (department_id.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Select dep", Toast.LENGTH_SHORT).show();
        } else if (spiner_people_id.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Select people", Toast.LENGTH_SHORT).show();
        } else if (et_some_note.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Enter note", Toast.LENGTH_SHORT).show();
        } else {
            ApprovalActionAdd(approval_id, et_some_note.getText().toString(), spiner1_TakeActionId,
                    spiner1_TaskActionTitle, Response_Id, Response_Title, req_send_to_name, req_send_to_id,
                    ApprovalFrom, ApprovalFromId, Priority, et_password.getText().toString(),
                    spiner_people_id, spiner_people_name, Forward_For_Comment_Flag, Recommended_For_Approval_Flag);
        }
    }

    private void CheckValidation() {
        if (et_some_note.getText().toString().trim().equalsIgnoreCase("")) {

        }
    }

    private void getPeopleDetaSpiner(final String departmentid) {
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.People_Select_by_DepartmentId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res_people", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            Toast.makeText(activity2, "Please Try again", Toast.LENGTH_SHORT).show();
                            activity2.finish();
                        }
                        if (object.has("People_Select_by_DepartmentId_Array")) {
                            JSONArray jsonArray = object.getJSONArray("People_Select_by_DepartmentId_Array");
                            if (jsonArray.length() != 0) {
                                contact_list.clear();
                                //   contact_list.add(new BeanPeopleSelectByDepartmentId("0", "Select People"));
                                contact_list.addAll((Collection<? extends BeanPeopleSelectByDepartmentId>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeopleSelectByDepartmentId>>() {
                                }.getType()));

                                adapterApprovalsPeopleByDep = new AdapterApprovalsPeopleByDep(activity, contact_list);
                                spinner_people_list.setAdapter(adapterApprovalsPeopleByDep);
                                spinner_people_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        spiner_people_id = contact_list.get(i).peopleId;
                                        spiner_people_name = contact_list.get(i).peopleName;
                                        Log.e("spiner_people_id", spiner_people_id);
                                        Log.e("spiner_people_name", spiner_people_name);
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

                    map.put("DepartmentId", departmentid);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {

            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }


    private void getApprovalMoreData(final String approval_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Request_And_Log_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    String peopleid = "";
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Department_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Department_Array");
                            if (jsonArray.length() != 0) {
                                department_List.clear();
                                department_List.add(new BeanDepartmentList("0", "Select Department"));
                                department_List.addAll((Collection<? extends BeanDepartmentList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanDepartmentList>>() {
                                }.getType()));
                                adapter_department = new AdapterDepartment(activity, department_List);
                                spinner_department_list.setAdapter(adapter_department);
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (object.has("Next_Priority_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Next_Priority_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Priority = jsonArray.optJSONObject(i).getString("Priority");
                                }
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }
                        if (object.has("Forward_For_Comment_Request_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Forward_For_Comment_Request_Array");
                            if (jsonArray.length() != 0) {
                                beanForwardForCommentRequestArrays.clear();
                                beanForwardForCommentRequestArrays.addAll((Collection<? extends BeanForwardForCommentRequestArray>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanForwardForCommentRequestArray>>() {
                                }.getType()));
                      /*          adapter_department = new AdapterDepartment(activity, department_List);
                                spinner_department_list.setAdapter(adapter_department);*/

                                String RequestSendToname = "";
                                String RequestSendTo_id = "";

                                for (int i = 0; i < beanForwardForCommentRequestArrays.size(); i++) {
                                    RequestSendToname += beanForwardForCommentRequestArrays.get(i).requestFromName + ",";
                                    RequestSendTo_id += beanForwardForCommentRequestArrays.get(i).requestFromId + ",";
                                }
                                if (RequestSendToname.length() > 0) {
                                    RequestSendTo = RequestSendToname.substring(0, RequestSendToname.length() - 1);
                                }
                                if (RequestSendTo_id.length() > 0) {
                                    RequestSendToId = RequestSendTo_id.substring(0, RequestSendTo_id.length() - 1);
                                }
                                String approval_name = "";
                                String approval_id = "";

                                for (int i = 0; i < beanForwardForCommentRequestArrays.size(); i++) {
                                    approval_name += beanForwardForCommentRequestArrays.get(i).requestSendToName + ",";
                                    approval_id += beanForwardForCommentRequestArrays.get(i).requestSendToId + ",";
                                }
                                if (approval_name.length() > 0) {
                                    ApprovalFrom = approval_name.substring(0, approval_name.length() - 1);
                                }
                                if (approval_id.length() > 0) {
                                    ApprovalFromId = approval_id.substring(0, approval_id.length() - 1);
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("ApprovalId", approval_id);
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
        recyclerview_adapter3 = new Adapter2MyEventsPeopleList(activity2, item_list3);
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

                    tv_select_person.setVisibility(View.VISIBLE);
                    rv_select_person.setVisibility(View.VISIBLE);
                } else {
                    tv_select_person.setVisibility(View.VISIBLE);
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

        adapter1people = new Adapter1TakeActionPeopleList(activity2, contact_list);
        recyclerview5.setAdapter(adapter1people);

        adapterApprovalsPeople = new AdapterApprovalsPeople(activity2, contact_list);
        spinner_people_list.setAdapter(adapterApprovalsPeople);

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

    public static void getPeopleData(final String did) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.People_Select_by_DepartmentId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res_people", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            Toast.makeText(activity2, "Please Try again", Toast.LENGTH_SHORT).show();
                            activity2.finish();
                            pd2.dismiss();
                        }
                        if (object.has("People_Select_by_DepartmentId_Array")) {
                            JSONArray jsonArray = object.getJSONArray("People_Select_by_DepartmentId_Array");
                            if (jsonArray.length() != 0) {
                                contact_list.clear();
                                contact_list.addAll((Collection<? extends BeanPeopleSelectByDepartmentId>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeopleSelectByDepartmentId>>() {
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
                    Log.e("res_error", error.toString());
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

                    map.put("DepartmentId", did);
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
                case R.id.et_some_note:
                    validateFname();
                    break;


            }
        }
    }

    private boolean validateFname() {
        String email = et_some_note.getText().toString().trim();
        if (email.isEmpty()) {
            et_some_note.setError("Please Write Some Note");
            requestFocus(et_some_note);
            return false;
        } else {
            et_some_note.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void ApprovalActionAdd(final String approvalid, final String note, final String ActionId, final String ActionTitle
            , final String Res_Id, final String Res_Title, final String Req_send_to_name, final String Req_send_to_id,
                                   final String approval_from, final String approval_id, final String priority,
                                   final String pass, final String people_id, final String people_name,
                                   final String forward_for_comment_flag, final String recomended_for_approvals) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Approvals_Action_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    Log.e("action add", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                            pd.dismiss();

                        }
                        if (object.has("Task_Action_Add")) {
                            JSONArray array = object.getJSONArray("Task_Action_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    pd.dismiss();
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(activity, Approval_Activity.class));
                                        finish();
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
                    Log.e("take_action_erro", error.toString());
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

                    map.put("ApprovalId", approvalid);
                    map.put("Description", note);
                    map.put("TakeActionId", ActionId);
                    map.put("TakeAction", ActionTitle);
                    map.put("ResponseId", Res_Id);
                    map.put("ResponseTitle", Res_Title);
                    map.put("RequestSendTo", Req_send_to_name);
                    map.put("RequestSendToId", Req_send_to_id);
                    map.put("ApprovalFrom", approval_from);
                    map.put("ApprovalFromId", approval_id);
                    map.put("Priority", priority);
                    map.put("Password", Utility.getPeoplePasswordPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("LoginName", Utility.getFullNamePreference());
                    map.put("Forward_For_Comment_Flag", forward_for_comment_flag);
                    map.put("Recommended_For_Approval_Flag", recomended_for_approvals);
                    map.put("NotificationId", people_id);
                    map.put("NotificationName", people_name);
                    map.put("FileTitle", filename);
                    map.put("FileExten", file_extension);
                    map.put("Path", fileBase64);


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
                    tv_attachment.setText(file);
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


    @Override
    public void onBackPressed() {
        finish();
    }

}
