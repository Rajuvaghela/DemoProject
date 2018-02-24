package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ext.adarsh.Activity.PollsActivity;
import com.ext.adarsh.Adapter.Adapter1;
import com.ext.adarsh.Adapter.Adapter1Branch;
import com.ext.adarsh.Adapter.Adapter2;
import com.ext.adarsh.Adapter.Adapter2Branch;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;


public class polls extends Fragment {

    @BindView(R.id.add_new_polls_float)
    FloatingActionButton add_new_polls_float;

    Activity activity;
    static Activity activity2;
    Dialog addnewpolls, addpollsDialog;
    ImageView add, add2;
    LinearLayout choice3, choice4;

    public static RecyclerView recylerpolls;
    ProgressDialog pd;
    static ProgressDialog pd2;
    public static List<ModelClass> item_list = new ArrayList<>();
    public static List<ModelClass2> item_list2 = new ArrayList<>();
    EditText et_search;
    Context context;
    static RecyclerView recyclerview1;
    static RecyclerView recyclerview2;

    static RecyclerView recyclerview3;
    static RecyclerView recyclerview4;

    List<ModelClass> list = new ArrayList<>();
    public static List<BeanDepartmentList> department_List = new ArrayList<>();
    public static List<BeanBranchList> branch_List = new ArrayList<>();
    static Adapter1 adapter1;
    static Adapter1Branch adapter1branch;
    static RecyclerView.LayoutManager recylerViewLayoutManager;
    static RecyclerView.LayoutManager recylerViewLayoutManager2;
    public static RecyclerView.Adapter recyclerview_adapter;
    public static RecyclerView.Adapter recyclerview_adapter2;
    static Dialog open_tag_dialog;
    static RecyclerView rv_select_visible_branch;
    static RecyclerView rv_select_visible_department;
    static TextView tv_select_visible_department, tv_select_visible_branch;

    EditText et_add_polls_question, edit_option1, edit_option2, edit_option3, edit_option4;
    TextView txt_days_count;
    TextView tv2, tv3, tv5, tv4, tv6, tv7, tv8, tv9;
    LinearLayout ll_create_polls;

    String branchID = "", departId = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.polls_new_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        activity2 = getActivity();
        pd = Utility.getDialog(activity);
        pd2 = Utility.getDialog(activity2);
        recylerpolls = (RecyclerView) view.findViewById(R.id.recylerpolls);
        recylerpolls.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        recylerpolls.setLayoutManager(lnmanager2);
        recylerpolls.setItemAnimator(new DefaultItemAnimator());

        add_new_polls_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDialogPolls();
            }
        });

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

        adapter1branch = new Adapter1Branch(activity2, branch_List);
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

        adapter1 = new Adapter1(activity2, department_List);
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

    public void addNewDialogPolls() {
        addnewpolls = new Dialog(activity);
        addnewpolls.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addnewpolls.getWindow().setWindowAnimations(R.style.DialogAnimation);
        addnewpolls.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addnewpolls.setContentView(R.layout.create_new_polls_dialog);

        Window window = addnewpolls.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        ll_create_polls = (LinearLayout) addnewpolls.findViewById(R.id.ll_create_polls);
        et_add_polls_question = (EditText) addnewpolls.findViewById(R.id.et_add_polls_question);
        edit_option1 = (EditText) addnewpolls.findViewById(R.id.edit_option1);
        edit_option2 = (EditText) addnewpolls.findViewById(R.id.edit_option2);
        edit_option3 = (EditText) addnewpolls.findViewById(R.id.edit_option3);
        edit_option4 = (EditText) addnewpolls.findViewById(R.id.edit_option4);
        txt_days_count = (TextView) addnewpolls.findViewById(R.id.txt_days_count);
        tv2 = (TextView) addnewpolls.findViewById(R.id.tv2);
        tv3 = (TextView) addnewpolls.findViewById(R.id.tv3);
        tv5 = (TextView) addnewpolls.findViewById(R.id.tv5);
        tv4 = (TextView) addnewpolls.findViewById(R.id.tv4);
        tv6 = (TextView) addnewpolls.findViewById(R.id.tv6);
        tv7 = (TextView) addnewpolls.findViewById(R.id.tv7);
        tv8 = (TextView) addnewpolls.findViewById(R.id.tv8);
        tv9 = (TextView) addnewpolls.findViewById(R.id.tv9);

        et_add_polls_question.setTypeface(Utility.getTypeFace());
        edit_option1.setTypeface(Utility.getTypeFace());
        edit_option2.setTypeface(Utility.getTypeFace());
        edit_option3.setTypeface(Utility.getTypeFace());
        edit_option4.setTypeface(Utility.getTypeFace());
        txt_days_count.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());

        LinearLayout lnmainback = (LinearLayout) addnewpolls.findViewById(R.id.lnmainback);

        add = (ImageView) addnewpolls.findViewById(R.id.add);
        add2 = (ImageView) addnewpolls.findViewById(R.id.add2);
        choice3 = (LinearLayout) addnewpolls.findViewById(R.id.liner_choice3);
        choice4 = (LinearLayout) addnewpolls.findViewById(R.id.liner_choice4);
        LinearLayout ll_select_department = (LinearLayout) addnewpolls.findViewById(R.id.ll_select_department);
        LinearLayout ll_select_branch = (LinearLayout) addnewpolls.findViewById(R.id.ll_select_branch);

        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Yes");
        categories.add("No");

        final Spinner spinner = (Spinner) addnewpolls.findViewById(R.id.spiner);
        ImageView img_plus = (ImageView) addnewpolls.findViewById(R.id.img_plus);
        ImageView img_minus = (ImageView) addnewpolls.findViewById(R.id.img_minus);

        final TextView txt_days_count = (TextView) addnewpolls.findViewById(R.id.txt_days_count);
        tv_select_visible_department = (TextView) addnewpolls.findViewById(R.id.tv_select_visible_department);
        tv_select_visible_branch = (TextView) addnewpolls.findViewById(R.id.tv_select_visible_branch);
        rv_select_visible_branch = (RecyclerView) addnewpolls.findViewById(R.id.rv_select_visible_branch);
        rv_select_visible_department = (RecyclerView) addnewpolls.findViewById(R.id.rv_select_visible_department);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        rv_select_visible_department.setLayoutManager(flowLayoutManager);

        FlowLayoutManager flowLayoutManager2 = new FlowLayoutManager();
        flowLayoutManager2.setAutoMeasureEnabled(true);
        rv_select_visible_branch.setLayoutManager(flowLayoutManager2);

        TextView txt_createpolls = (TextView) addnewpolls.findViewById(R.id.txt_createpolls);
        TextView txt_cancle = (TextView) addnewpolls.findViewById(R.id.txt_cancle);
        TextView tv8 = (TextView) addnewpolls.findViewById(R.id.tv8);
        TextView tv9 = (TextView) addnewpolls.findViewById(R.id.tv9);
        TextView tv10 = (TextView) addnewpolls.findViewById(R.id.tv10);

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

        ll_create_polls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_add_polls_question.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter Question", Toast.LENGTH_SHORT).show();
                } else if (edit_option1.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter Option1", Toast.LENGTH_SHORT).show();
                } else if (edit_option2.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter Option2", Toast.LENGTH_SHORT).show();
                } else if (item_list.size() < 1) {
                    Toast.makeText(activity, "Please Select Department", Toast.LENGTH_SHORT).show();
                } else if (item_list2.size() < 1) {
                    Toast.makeText(activity, "Please Select Branch", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < branch_List.size(); i++) {
                        branchID += branch_List.get(i).branchId + ",";
                    }

                    for (int i = 0; i < department_List.size(); i++) {
                        departId += department_List.get(i).departmentId + ",";
                    }

                    String showtext;
                    if (spinner.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                        showtext = "Y";
                    } else {
                        showtext = "N";
                    }
                    String bid = branchID.substring(0, branch_List.size() - 1);
                    String did = departId.substring(0, department_List.size() - 1);
                    CreatePolls(et_add_polls_question.getText().toString(), txt_days_count.getText().toString(), showtext, did, bid);
                }

            }
        });

        ll_select_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTagPopup();
            }
        });

        ll_select_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTagPopupBranch();
            }
        });


        txt_days_count.setText("1");

        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = Integer.parseInt(txt_days_count.getText().toString());
                if (a > 1) {
                    txt_days_count.setText(String.valueOf(a - 1));
                }
            }
        });

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = Integer.parseInt(txt_days_count.getText().toString());
                txt_days_count.setText(String.valueOf(a + 1));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice3.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);
            }
        });


        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice4.setVisibility(View.VISIBLE);
                add2.setVisibility(View.GONE);
            }
        });

        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewpolls.dismiss();
            }
        });

        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewpolls.dismiss();
            }
        });

        getBranchData();
        getDepartmentData();
        addnewpolls.show();
    }

    void CreatePolls(final String question, final String length, final String show, final String dep, final String branch) {
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
                                        if (edit_option3.getText().toString().equalsIgnoreCase("") && edit_option4.getText().toString().equalsIgnoreCase("")) {
                                            for (int j = 0; j < 2; j++) {
                                                if (j == 0) {
                                                    PollsChoiceAdd(pid, edit_option1.getText().toString(), "");
                                                }
                                                if (j == 1) {
                                                    PollsChoiceAdd(pid, edit_option2.getText().toString(), "last");
                                                }
                                            }
                                        } else if (edit_option3.getText().toString().equalsIgnoreCase("") && !edit_option4.getText().toString().equalsIgnoreCase("")) {
                                            for (int j = 0; j < 3; j++) {
                                                if (j == 0) {
                                                    PollsChoiceAdd(pid, edit_option1.getText().toString(), "");
                                                }
                                                if (j == 1) {
                                                    PollsChoiceAdd(pid, edit_option2.getText().toString(), "");
                                                }
                                                if (j == 2) {
                                                    PollsChoiceAdd(pid, edit_option4.getText().toString(), "last");
                                                }
                                            }
                                        } else if (!edit_option3.getText().toString().equalsIgnoreCase("") && edit_option4.getText().toString().equalsIgnoreCase("")) {
                                            for (int j = 0; j < 3; j++) {
                                                if (j == 0) {
                                                    PollsChoiceAdd(pid, edit_option1.getText().toString(), "");
                                                }
                                                if (j == 1) {
                                                    PollsChoiceAdd(pid, edit_option2.getText().toString(), "");
                                                }
                                                if (j == 2) {
                                                    PollsChoiceAdd(pid, edit_option3.getText().toString(), "last");
                                                }
                                            }

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

    void PollsChoiceAdd(final String id, final String choice, final String a) {
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
                                        if (a.equalsIgnoreCase("last")) {
                                            addnewpolls.dismiss();
                                            Toast.makeText(activity2, "Polls Add successfully.", Toast.LENGTH_SHORT).show();
                                            PollsActivity.getPollsData();
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
        recyclerview_adapter = new Adapter2(activity2, polls.item_list);
        rv_onchangelistner();
    }

    public static void callOnBackPress2() {
        recyclerview_adapter2 = new Adapter2Branch(activity2, polls.item_list2);
        rv_onchangelistner2();
    }

    static void openTagPopup() {
        openTagDialog();
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

}
