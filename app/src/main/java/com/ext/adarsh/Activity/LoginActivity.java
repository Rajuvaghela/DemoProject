package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.ext.adarsh.Adapter.AdapterApprovals;
import com.ext.adarsh.Adapter.SellingImageAdapter;
import com.ext.adarsh.Bean.BeanApprovalsList;
import com.ext.adarsh.Bean.BeanRolesRights;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;

    /*@BindView(R.id.tv5)
    TextView tv5;*/

    @BindView(R.id.edt_username)
    EditText edt_username;

    @BindView(R.id.edt_password)
    EditText edt_password;

    @BindView(R.id.lnlogin)
    LinearLayout lnlogin;

    @BindView(R.id.ll_register)
    LinearLayout ll_register;

    @BindView(R.id.lnforgot)
    LinearLayout lnforgot;

    @BindView(R.id.txt_reg)
    TextView txt_reg;

    String Token = "";
    Activity activity;
    ProgressDialog pd;
    private List<BeanRolesRights> beanRolesRightses = new ArrayList<>();
    Dialog forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        activity = this;
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        Token = FirebaseInstanceId.getInstance().getToken();
        Log.e("onCreate: ", "++++++++++" + Token);
        pd = Utility.getDialog(activity);
        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        //  tv5.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFaceTab());
        tv4.setTypeface(Utility.getTypeFaceTab());
        txt_reg.setTypeface(Utility.getTypeFaceTab());
        edt_username.setTypeface(Utility.getTypeFace());
        edt_password.setTypeface(Utility.getTypeFace());
        edt_username.addTextChangedListener(new MyTextWatcher(edt_username));
        edt_password.addTextChangedListener(new MyTextWatcher(edt_password));

        // tv5.setText("@All rights reserved by Adarsh Intranet");

        ll_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, RegistrationActivity.class));
            }
        });
        lnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(edt_username.getText().toString().trim().isEmpty() || edt_password.getText().toString().isEmpty())) {
                    getLoginData(edt_username.getText().toString(), edt_password.getText().toString());
                } else {
                    Utility.showDialog(activity, "Please enter required fields.");
                }
            }
        });

        lnforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword();
            }
        });
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

                case R.id.edt_username:
                    validateEmail();
                    break;
                case R.id.edt_password:
                    validatePass();
                    break;
            }
        }
    }

    private boolean validateEmail() {
        String email = edt_username.getText().toString().trim();
        if (email.isEmpty()) {
            edt_username.setError("Please enter email.");
            requestFocus(edt_username);
            return false;
        }/* else if (!isValidEmail(email)) {
            edt_username.setError("Please enter valid email.");
            requestFocus(edt_username);
            return false;
        }*/ else {
            edt_username.setError(null);
        }
        return true;
    }

/*    private static boolean isValidEmail(String email) {
        String emailPattern1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
        return !TextUtils.isEmpty(email) && email.matches(emailPattern1) || !TextUtils.isEmpty(email) && email.matches(emailPattern2);
    }*/

    /*private static boolean isValidId(String email) {
        String subString = email.substring(0, Math.min(email.length(), 2));
        Log.e("Validation",subString);
        return !TextUtils.isEmpty(email) && !subString.equalsIgnoreCase("ACC");
    }*/

    public boolean validatePass() {
        String email = edt_password.getText().toString();
        if (email.isEmpty()) {
            edt_password.setError("Please enter password.");
            requestFocus(edt_password);
            return false;
        } else {
            edt_password.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void getLoginData(final String uname, final String password) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Login, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Pepole_Login")) {
                            JSONArray array = object.getJSONArray("Pepole_Login");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        JSONArray array1 = object.optJSONArray("Pepole_Profile_Detail");
                                        if (array1.length() != 0) {
                                            for (int i2 = 0; i2 < array1.length(); i2++) {
                                                Utility.setPeopleIdPreference(array1.optJSONObject(i).getString("PeopleId").toString());
                                                Utility.setEmployeeCode(array1.optJSONObject(i).getString("EmployeeCode").toString());
                                                Utility.setFullNamePreference(array1.optJSONObject(i).getString("FullName").toString());
                                                Utility.setEmailAddressPreference(array1.optJSONObject(i).getString("EmailAddress").toString());
                                                Utility.setMobileNoPreference(array1.optJSONObject(i).getString("MobileNo").toString());
                                                Utility.setDepartmentIdPreference(array1.optJSONObject(i).getString("DepartmentId").toString());
                                                Utility.setBranchIdPreference(array1.optJSONObject(i).getString("BranchId").toString());
                                                Utility.setRegionIdPreference(array1.optJSONObject(i).getString("RegionId").toString());
                                                Utility.setHashKeyPreference(array1.optJSONObject(i).getString("HashKey").toString());
                                                Utility.setPeopleProfileImgPreference(array1.optJSONObject(i).getString("ProfileImage").toString());
                                                Utility.setPeoplePasswordPreference(array1.optJSONObject(i).getString("Password").toString());
                                                Utility.setManagerCodePreference(array1.optJSONObject(i).getString("ManagerCode").toString());
                                                Utility.setManagerNamePreference(array1.optJSONObject(i).getString("ManagerName").toString());
                                                Utility.setManagerIdPreference(array1.optJSONObject(i).getString("ManagerId").toString());
                                                Utility.setTokenIdPreference(Token);
                                            }
                                        }
                                        pd.dismiss();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                        if (object.has("Roles_Rights_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Roles_Rights_Array");
                            if (jsonArray.length() != 0) {
                                beanRolesRightses.clear();
                                beanRolesRightses.addAll((Collection<? extends BeanRolesRights>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanRolesRights>>() {
                                }.getType()));
                                for (int i = 0; i < beanRolesRightses.size(); i++) {
                                    switch (i) {
                                        case 0:
                                            if (beanRolesRightses.get(0).title.equalsIgnoreCase("Announcement")) {
                                                Utility.setAnnouncementAdd(beanRolesRightses.get(0).r1Flag);
                                                Utility.setAnnouncementUpdate(beanRolesRightses.get(0).r2Flag);
                                                Utility.setAnnouncementDelete(beanRolesRightses.get(0).r3Flag);
                                                Utility.setAnnouncementPublish(beanRolesRightses.get(0).r4Flag);
                                            }
                                            break;
                                        case 1:
                                            if (beanRolesRightses.get(1).title.equalsIgnoreCase("Event")) {
                                                Utility.setEventAdd(beanRolesRightses.get(1).r1Flag);
                                                Utility.setEventUpdate(beanRolesRightses.get(1).r2Flag);
                                                Utility.setEventDelete(beanRolesRightses.get(1).r3Flag);
                                                Utility.setEventPublish(beanRolesRightses.get(1).r4Flag);
                                            }
                                            break;
                                        case 2:
                                            if (beanRolesRightses.get(2).title.equalsIgnoreCase("Task")) {
                                                Utility.setTaskAdd(beanRolesRightses.get(2).r1Flag);
                                                Utility.setTaskUpdate(beanRolesRightses.get(2).r2Flag);
                                                Utility.setTaskDelete(beanRolesRightses.get(2).r3Flag);
                                                Utility.setTodo(beanRolesRightses.get(2).r4Flag);
                                            }
                                            break;
                                        case 3:
                                            if (beanRolesRightses.get(3).title.equalsIgnoreCase("Approval")) {
                                                Utility.setApprovalAdd(beanRolesRightses.get(3).r1Flag);
                                                Utility.setApprovalUpdate(beanRolesRightses.get(3).r2Flag);
                                                Utility.setApprovalDelete(beanRolesRightses.get(3).r3Flag);
                                                Utility.setApprovalTakeAction(beanRolesRightses.get(3).r4Flag);
                                            }
                                            break;
                                        case 4:
                                            if (beanRolesRightses.get(4).title.equalsIgnoreCase("Market")) {
                                                Utility.setMarketAdd(beanRolesRightses.get(4).r1Flag);
                                                Utility.setMarketUpdate(beanRolesRightses.get(4).r2Flag);
                                                Utility.setMarketDelete(beanRolesRightses.get(4).r3Flag);
                                            }
                                            break;
                                        case 5:
                                            if (beanRolesRightses.get(5).title.equalsIgnoreCase("Knowledge")) {
                                                Utility.setKnowledgeAdd(beanRolesRightses.get(5).r1Flag);
                                                Utility.setKnowledgeUpdate(beanRolesRightses.get(5).r2Flag);
                                                Utility.setKnowledgeDelete(beanRolesRightses.get(5).r3Flag);
                                            }
                                            break;
                                        case 6:
                                            if (beanRolesRightses.get(6).title.equalsIgnoreCase("File")) {
                                                Utility.setFileAdd(beanRolesRightses.get(6).r1Flag);
                                                Utility.setFileUpdate(beanRolesRightses.get(6).r2Flag);
                                                Utility.setFileDelete(beanRolesRightses.get(6).r3Flag);
                                            }
                                            break;
                                        case 7:
                                            if (beanRolesRightses.get(7).title.equalsIgnoreCase("Photo")) {
                                                Utility.setPhotoAdd(beanRolesRightses.get(7).r1Flag);
                                            }
                                            break;
                                        case 8:
                                            if (beanRolesRightses.get(8).title.equalsIgnoreCase("Polls")) {
                                                Utility.setPollsAdd(beanRolesRightses.get(8).r1Flag);
                                                Utility.setPollsUpdate(beanRolesRightses.get(8).r2Flag);
                                                Utility.setPollsDelete(beanRolesRightses.get(8).r3Flag);
                                                Utility.setPollsPublish(beanRolesRightses.get(8).r4Flag);
                                                Utility.setPollsVote(beanRolesRightses.get(8).r5Flag);
                                            }
                                            break;
                                        case 9:
                                            if (beanRolesRightses.get(9).title.equalsIgnoreCase("Calender")) {
                                                Utility.setCalenderAdd(beanRolesRightses.get(9).r1Flag);
                                            }
                                            break;
                                        case 10:
                                            if (beanRolesRightses.get(10).title.equalsIgnoreCase("Settings")) {
                                                Utility.setSettingsAdd(beanRolesRightses.get(10).r1Flag);
                                            }
                                            break;
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
                    map.put("EmailId", uname);
                    map.put("Password", password);
                    map.put("Tokan", Token);
                    map.put("DeviceType", "Android");
                    map.put("DeviceName", Utility.getDeviceName());
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

    private void forgotpassword(){
        forgotpassword = new Dialog(activity);
        forgotpassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotpassword.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        forgotpassword.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        forgotpassword.setContentView(R.layout.activity_forgot_password);
        Window window = forgotpassword.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout ll_forgot_pass = (LinearLayout) forgotpassword.findViewById(R.id.ll_forgot_pass);
        LinearLayout ll_cancel = (LinearLayout) forgotpassword.findViewById(R.id.ll_cancel);
        LinearLayout lnmainback = (LinearLayout) forgotpassword.findViewById(R.id.lnmainback);
        final EditText et_email = (EditText) forgotpassword.findViewById(R.id.et_email);
        TextView header = (TextView) forgotpassword.findViewById(R.id.header);
        TextView tv1 = (TextView) forgotpassword.findViewById(R.id.tv1);
        TextView tv_forgot_pass = (TextView) forgotpassword.findViewById(R.id.tv_forgot_password);
        TextView tv_cancel = (TextView) forgotpassword.findViewById(R.id.tv_cancel);
        et_email.setTypeface(Utility.getTypeFace());
        tv1.setTypeface(Utility.getTypeFace());
        tv_forgot_pass.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        header.setTypeface(Utility.getTypeFaceTab());
        ll_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email = et_email.getText().toString();
               forgotpass(email);
            }
        });
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword.dismiss();
            }
        });
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword.dismiss();
            }
        });
        forgotpassword.show();
    }

    private void forgotpass(final String email) {
        pd.show();
            if (checkConnectivity()) {
                StringRequest request = new StringRequest(Request.Method.POST, AppConstant.ForgotPassword, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("res", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.has("ForgotPassword")) {
                                JSONArray array = object.getJSONArray("ForgotPassword");
                                if (array.length() != 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                            pd.dismiss();
                                            Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                            forgotpassword.dismiss();
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
                            //  pd.dismiss();
                            showMsg(R.string.json_error);
                            e.printStackTrace();
                        }
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("EmailAddress",email);
                        return map;
                    }
                };
                Utility.SetvollyTime30Sec(request);
                Infranet.getInstance().getRequestQueue().add(request);
            } else {
                //  pd.dismiss();
                Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }

