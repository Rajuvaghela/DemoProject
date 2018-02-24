package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.ext.adarsh.Adapter.AdapterMyContact;
import com.ext.adarsh.Bean.BeanMyContact;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class MyContact extends Fragment {

    public static RecyclerView recyclercontact;
    static Activity activity;
    static ProgressDialog pd;
    private static ArrayList<BeanMyContact> contactList = new ArrayList<>();
    private static AdapterMyContact adapter;
    @BindView(R.id.search_people)
    TextView search_people;
    FloatingActionButton fab_contact;
    String Token = "";
    Dialog addgroup;
    Dialog add_contact;
    EditText et_fname, et_lname, et_mobile_no, et_home, et_email, et_website, et_address;
    TextView et_birthday;
    Spinner spinner_group_name;
    ImageView iv_user_profile;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String profileimg = "";
    FileOutputStream fo;
    String contact_group_id;
    List<String> group_list = new ArrayList<>();
    List<String> group_id = new ArrayList<>();
    public static Handler handler;
    static int pageCount = 10;
    static TextView tv_no_record_found;
    Dialog dd;

    RecyclerView recylerpeoplesearch;
    public static ArrayList<BeanMyContact> beanPeopleNewsSearch = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.my_contact, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        pd = Utility.getDialog(activity);
        handler = new Handler();
        search_people.setTypeface(Utility.getTypeFace());

        fab_contact = (FloatingActionButton) view.findViewById(R.id.fab_contact);
        recyclercontact = (RecyclerView) view.findViewById(R.id.recyclercontact);
        tv_no_record_found = (TextView) view.findViewById(R.id.tv_no_record_found);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclercontact.setLayoutManager(mLayoutManager);
        recyclercontact.setItemAnimator(new DefaultItemAnimator());

        search_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog3();
            }
        });

        fab_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });
        getPeopleData();
        getPeopleGroup();
    }

    public void getPeopleGroup() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Category_Select_By_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("Category_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Category_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    group_list.add(jsonArray.getJSONObject(i).getString("CategoryName"));
                                    group_id.add(jsonArray.getJSONObject(i).getString("CategoryId"));
                                    pd.dismiss();
                                }
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

    public static void getPeopleData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_Select_By_More_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("My_Contact_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Contact_Array");
                            if (jsonArray.length() != 0) {
                                contactList.clear();
                                contactList.addAll((Collection<? extends BeanMyContact>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMyContact>>() {
                                }.getType()));
                                pd.dismiss();
                            } else {
                                tv_no_record_found.setVisibility(View.VISIBLE);
                                contactList.clear();
                                recyclercontact.setAdapter(null);
                                pd.dismiss();
                            }
                        }

                        adapter = new AdapterMyContact(contactList, activity, recyclercontact);
                        recyclercontact.setAdapter(adapter);

                        adapter.setOnLoadMoreListener(new AdapterMyContact.OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                contactList.add(null);
                                adapter.notifyItemInserted(contactList.size() - 1);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        contactList.remove(contactList.size() - 1);
                                        adapter.notifyItemRemoved(contactList.size());
                                        pageCount += 10;
                                        getPeopleDataSecond(String.valueOf(pageCount));

                                    }
                                }, 2000);
                                System.out.println("load");
                            }
                        });

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
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("TopValue", "0");
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    public static void getPeopleDataSecond(final String topvalue) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_Select_By_More_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("My_Contact_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Contact_Array");
                            if (jsonArray.length() != 0) {
                                contactList.addAll((Collection<? extends BeanMyContact>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMyContact>>() {
                                }.getType()));
                                adapter.notifyItemInserted(contactList.size());
                                adapter.setLoaded();
                                pd.dismiss();
                            } else {
                                adapter.setLoaded();
                                pd.dismiss();
                            }
                        }
                        adapter.setLoaded();
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
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("TopValue", topvalue);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();

        }
    }

    private void addgroupdemo() {

        addgroup = new Dialog(activity);
        addgroup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addgroup.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        addgroup.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addgroup.setContentView(R.layout.add_new_groping_dialog);

        Window window = addgroup.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView title = (TextView) addgroup.findViewById(R.id.title);
        final TextView edt_groupname = (EditText) addgroup.findViewById(R.id.edt_groupname);
        Button btn_save = (Button) addgroup.findViewById(R.id.btn_save);
        ImageView iv_close = (ImageView) addgroup.findViewById(R.id.iv_close);


        title.setTypeface(Utility.getTypeFace());
        edt_groupname.setTypeface(Utility.getTypeFace());
        btn_save.setTypeface(Utility.getTypeFace());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_groupname.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                } else {
                    addGroupPeople(edt_groupname.getText().toString());
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addgroup.dismiss();
            }
        });
        addgroup.show();
    }

    public void addGroupPeople(final String name) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.GroupName_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("GroupName_Add")) {
                            JSONArray array = object.getJSONArray("GroupName_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        addgroup.dismiss();
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
                    map.put("CategoryName", name);
                    map.put("LoginId", Utility.getPeopleIdPreference());
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

    private void addContact() {
        add_contact = new Dialog(activity);
        add_contact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_contact.getWindow().setWindowAnimations(R.style.DialogAnimation);
        add_contact.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        add_contact.setContentView(R.layout.add_contact_dialog);

        Window window = add_contact.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        RelativeLayout rl_date = (RelativeLayout) add_contact.findViewById(R.id.rl_date);

        spinner_group_name = (Spinner) add_contact.findViewById(R.id.spinner_group_name);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, R.layout.custom_spiner_contact_group, group_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_group_name.setAdapter(dataAdapter);

        spinner_group_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (group_id.size() == 0) {
                    contact_group_id = "";
                } else {
                    contact_group_id = group_id.get(i);
                    Log.e("contact_group_id", "" + contact_group_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // et_fname,et_lname,et_mobile_no,et_home,et_email,et_website,et_birthday,et_address
        et_fname = (EditText) add_contact.findViewById(R.id.et_fname);
        et_lname = (EditText) add_contact.findViewById(R.id.et_lname);
        et_mobile_no = (EditText) add_contact.findViewById(R.id.et_mobile_no);
        et_home = (EditText) add_contact.findViewById(R.id.et_home);
        et_email = (EditText) add_contact.findViewById(R.id.et_email);
        et_website = (EditText) add_contact.findViewById(R.id.et_website);
        et_birthday = (TextView) add_contact.findViewById(R.id.et_birthday);
        et_address = (EditText) add_contact.findViewById(R.id.et_address);

        et_fname.addTextChangedListener(new MyTextWatcher(et_fname));
        et_lname.addTextChangedListener(new MyTextWatcher(et_lname));
        et_mobile_no.addTextChangedListener(new MyTextWatcher(et_mobile_no));
        et_home.addTextChangedListener(new MyTextWatcher(et_home));
        et_email.addTextChangedListener(new MyTextWatcher(et_email));
        et_website.addTextChangedListener(new MyTextWatcher(et_website));
        et_birthday.addTextChangedListener(new MyTextWatcher(et_birthday));
        et_address.addTextChangedListener(new MyTextWatcher(et_address));


        //tv_cancel,tv_save_contact,tv_address,tv_birthday,tv_website,tv_email,tv_home,tv_mobile_no,tv_lname,tv_fname
        TextView tv_cancel = (TextView) add_contact.findViewById(R.id.tv_cancel);
        TextView tv_save_contact = (TextView) add_contact.findViewById(R.id.tv_save_contact);
        TextView tv_address = (TextView) add_contact.findViewById(R.id.tv_address);
        TextView tv_birthday = (TextView) add_contact.findViewById(R.id.tv_birthday);
        TextView tv_website = (TextView) add_contact.findViewById(R.id.tv_website);
        TextView tv_email = (TextView) add_contact.findViewById(R.id.tv_email);
        TextView tv_home = (TextView) add_contact.findViewById(R.id.tv_home);
        TextView tv_mobile_no = (TextView) add_contact.findViewById(R.id.tv_mobile_no);
        TextView tv_lname = (TextView) add_contact.findViewById(R.id.tv_lname);
        TextView tv_fname = (TextView) add_contact.findViewById(R.id.tv_fname);

        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        tv_save_contact.setTypeface(Utility.getTypeFaceTab());
        tv_address.setTypeface(Utility.getTypeFace());
        tv_birthday.setTypeface(Utility.getTypeFace());
        tv_website.setTypeface(Utility.getTypeFace());
        tv_email.setTypeface(Utility.getTypeFace());
        tv_home.setTypeface(Utility.getTypeFace());
        tv_mobile_no.setTypeface(Utility.getTypeFace());
        tv_lname.setTypeface(Utility.getTypeFace());
        tv_fname.setTypeface(Utility.getTypeFace());


        et_fname.setTypeface(Utility.getTypeFace());
        et_lname.setTypeface(Utility.getTypeFace());
        et_mobile_no.setTypeface(Utility.getTypeFace());
        et_home.setTypeface(Utility.getTypeFace());
        et_email.setTypeface(Utility.getTypeFace());
        et_website.setTypeface(Utility.getTypeFace());
        et_birthday.setTypeface(Utility.getTypeFace());
        et_address.setTypeface(Utility.getTypeFace());
        iv_user_profile = (ImageView) add_contact.findViewById(R.id.iv_user_profile);
        FrameLayout fl_user_profile = (FrameLayout) add_contact.findViewById(R.id.fl_user_profile);

        rl_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                et_birthday.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        fl_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        LinearLayout ln_save = (LinearLayout) add_contact.findViewById(R.id.ln_save);

        ln_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String birth_date = "";
                if (et_birthday.getText().toString().equalsIgnoreCase("mm-dd-yyyy")) {
                    birth_date = "";
                } else {
                    birth_date = et_birthday.getText().toString();
                }

                if (et_fname.getText().toString().trim().equalsIgnoreCase("")) {
                    et_fname.setError("Please enter first name.");
                } else if (et_lname.getText().toString().trim().equalsIgnoreCase("")) {
                    et_lname.setError("Please enter last name.");
                } else if (et_mobile_no.getText().toString().trim().equalsIgnoreCase("")) {
                    et_mobile_no.setError("Please enter mobile no.");
                } else {
                    saveContact(et_fname.getText().toString(),
                            et_lname.getText().toString(),
                            et_mobile_no.getText().toString(),
                            et_home.getText().toString(),
                            et_email.getText().toString(),
                            et_website.getText().toString(),
                            birth_date,
                            et_address.getText().toString(), profileimg);
                }
            }
        });

        LinearLayout lnback = (LinearLayout) add_contact.findViewById(R.id.lnback);
        LinearLayout ln_cancel = (LinearLayout) add_contact.findViewById(R.id.ln_cancel);

        ln_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_contact.dismiss();
            }
        });

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_contact.dismiss();
            }
        });

        add_contact.show();
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
                case R.id.et_fname:
                    validateFname();
                    break;
                case R.id.et_lname:
                    validateLname();
                    break;
                case R.id.et_mobile_no:
                    validateMobile();
                    break;

                case R.id.et_home:
                    validateHome();
                    break;

                case R.id.et_email:
                    validateEmail();

                    break;
                case R.id.et_website:
                    validateWebsite();
                    break;
                case R.id.et_address:
                    validateAddress();
                    break;
            }
        }
    }

    private boolean validateFname() {
        String email = et_fname.getText().toString().trim();
        if (email.isEmpty()) {
            et_fname.setError("Please enter first name.");
            requestFocus(et_fname);
            return false;
        } else {
            et_fname.setError(null);
        }
        return true;
    }

    private boolean validateLname() {
        String email = et_lname.getText().toString().trim();
        if (email.isEmpty()) {
            et_lname.setError("Please enter last name.");
            requestFocus(et_lname);
            return false;
        } else {
            et_lname.setError(null);
        }
        return true;
    }


    private boolean validateMobile() {
        String email = et_mobile_no.getText().toString().trim();
        if (email.isEmpty()) {
            et_mobile_no.setError("Please enter mobile no.");
            requestFocus(et_mobile_no);
            return false;
        } else {
            et_mobile_no.setError(null);
        }
        return true;
    }


    private boolean validateHome() {
        String email = et_home.getText().toString().trim();
        if (email.isEmpty()) {
            et_home.setError("Please enter home number.");
            requestFocus(et_home);
            return false;
        } else {
            et_home.setError(null);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = et_email.getText().toString().trim();
        if (email.isEmpty()) {
            et_email.setError("Please enter email.");
            requestFocus(et_email);
            return false;
        } else if (!isValidEmail(email)) {
            et_email.setError("Please enter valid email.");
            requestFocus(et_email);
            return false;
        } else {
            et_email.setError(null);
        }
        return true;
    }

    private boolean validateWebsite() {
        String email = et_website.getText().toString().trim();
        if (email.isEmpty()) {
            et_website.setError("Please enter website.");
            requestFocus(et_website);
            return false;
        } else {
            et_website.setError(null);
        }
        return true;
    }

    private boolean validateAddress() {
        String email = et_address.getText().toString().trim();
        if (email.isEmpty()) {
            et_address.setError("Please enter address.");
            requestFocus(et_address);
            return false;
        } else {
            et_address.setError(null);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        String emailPattern1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
        return !TextUtils.isEmpty(email) && email.matches(emailPattern1) || !TextUtils.isEmpty(email) && email.matches(emailPattern2);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    void saveContact(final String fname, final String lname, final String mobile, final String home, final String email, final String website, final String birthday, final String address, final String user_profile) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_People_New_Add_To_Contact, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("my_cont_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("Contact_Pepole_New_Add_To_Contact")) {
                            JSONArray array = object.getJSONArray("Contact_Pepole_New_Add_To_Contact");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        add_contact.dismiss();
                                        getPeopleData();
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
                    Log.e("my_contact_erro", error.toString());
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
                    map.put("FirstName", fname);
                    map.put("LastName", lname);
                    map.put("MobileNo", mobile);
                    map.put("HomeNo", home);
                    map.put("EmailAddress", email);
                    map.put("Website", website);
                    map.put("CategoryId", contact_group_id);
                    map.put("BirthDate", birthday);
                    map.put("Address", address);
                    map.put("ProfileImagePath", user_profile);
                    // map.put("FirstName",fname);
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
        }
    }
    //endregion

    //region Gallery
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap thumbnail = null;
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] imageBytes = bytes.toByteArray();
        profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        File destination = new File(Environment.getExternalStorageDirectory() + "/Intranet/UserProfile");

        if (!destination.exists()) {
            File wallpaperDirectory = new File("/sdcard/Intranet/UserProfile");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/Intranet/UserProfile"), "intranet" + System.currentTimeMillis() + ".jpg");
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


/*        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        iv_user_profile.setImageBitmap(bm);*/
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

        File file = new File(new File("/sdcard/Intranet/UserProfile"), "intranet" + System.currentTimeMillis() + ".jpg");
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

    private void showAlertDialog3() {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.searchingpeoplelist);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnbacksharing = (LinearLayout) dd.findViewById(R.id.lnbacksharing);

        EditText ed_search_people = (EditText) dd.findViewById(R.id.search_people);
        recylerpeoplesearch = (RecyclerView) dd.findViewById(R.id.recylerpeoplesearch);

        recylerpeoplesearch.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        recylerpeoplesearch.setLayoutManager(lnmanager2);
        recylerpeoplesearch.setItemAnimator(new DefaultItemAnimator());

        ed_search_people.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.length();
                Log.e("onTextChanged: ", "+++++++++++++ count is " + String.valueOf(len));
                Log.e("onTextChanged: ", "+++++++++++++ value is " + s.toString());
                //adapter.getFilter().filter(s.toString());
                if (len > 3) {
                    getPeopleDataSearching(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ed_search_people.setTypeface(Utility.getTypeFace());

        lnbacksharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
            }
        });

        dd.show();
    }

    public void getPeopleDataSearching(final String searchvalue) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Search_Contacts_People_List, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("My_Contact_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Contact_Array");
                            if (jsonArray.length() != 0) {
                                beanPeopleNewsSearch.clear();
                                beanPeopleNewsSearch.addAll((Collection<? extends BeanMyContact>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMyContact>>() {
                                }.getType()));
                                adapter = new AdapterMyContact(beanPeopleNewsSearch, activity, recylerpeoplesearch);
                                recylerpeoplesearch.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                recylerpeoplesearch.setAdapter(null);
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
                    map.put("SearchString", searchvalue);
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
}

