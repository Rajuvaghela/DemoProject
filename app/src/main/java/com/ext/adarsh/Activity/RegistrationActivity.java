package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.ext.adarsh.Adapter.AdapterApprovalSelectPeople;
import com.ext.adarsh.Adapter.AdapterApprovalsAddPeopleList;
import com.ext.adarsh.Bean.ModelClass;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;
    ProgressDialog pd;
    String Token;

    @BindView(R.id.tv_reg_heading)
    TextView tv_reg_heading;

    @BindView(R.id.tv_emp_id)
    TextView tv_emp_id;

    @BindView(R.id.tv_emp_bdate)
    TextView tv_emp_bdate;

    @BindView(R.id.tv_bdate)
    TextView tv_bdate;

    @BindView(R.id.tv_next)
    TextView tv_next;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.tv_password)
    TextView tv_password;

    @BindView(R.id.et_employee_ID)
    EditText et_employee_ID;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.iv_user_profile)
    ImageView iv_user_profile;

    @BindView(R.id.tv_create_account)
    TextView tv_create_account;

    @BindView(R.id.lnmainback)
    LinearLayout lnmainback;

    @BindView(R.id.ll_cancel)
    LinearLayout ll_cancel;

    @BindView(R.id.ll_create_account)
    LinearLayout ll_create_account;

    @BindView(R.id.ll_next)
    LinearLayout ll_next;

    @BindView(R.id.ll_password)
    LinearLayout ll_password;

    @BindView(R.id.ll_address)
    LinearLayout ll_address;

    @BindView(R.id.ll_birth_date)
    LinearLayout ll_birth_date;

    @BindView(R.id.ll_emp_id)
    LinearLayout ll_emp_id;

    @BindView(R.id.rl_date)
    RelativeLayout rl_date;

    @BindView(R.id.et_email)
    EditText et_email;

    Dialog user_exits_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_main);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);
        FirebaseApp.initializeApp(this);
        Token = FirebaseInstanceId.getInstance().getToken();

        et_employee_ID.addTextChangedListener(new MyTextWatcher(et_employee_ID));
        tv_emp_bdate.addTextChangedListener(new MyTextWatcher(tv_emp_bdate));
        et_password.addTextChangedListener(new MyTextWatcher(et_password));
        et_email.addTextChangedListener(new MyTextWatcher(et_email));

        tv_create_account.setTypeface(Utility.getTypeFaceTab());
        tv_reg_heading.setTypeface(Utility.getTypeFaceTab());
        tv_next.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        tv_emp_id.setTypeface(Utility.getTypeFace());
        tv_emp_bdate.setTypeface(Utility.getTypeFace());
        tv_bdate.setTypeface(Utility.getTypeFace());
        tv_password.setTypeface(Utility.getTypeFace());
        et_employee_ID.setTypeface(Utility.getTypeFace());
        et_password.setTypeface(Utility.getTypeFace());
        et_email.setTypeface(Utility.getTypeFace());
        tv_emp_id.setTypeface(Utility.getTypeFace());

        ll_cancel.setOnClickListener(this);
        lnmainback.setOnClickListener(this);
        ll_next.setOnClickListener(this);
        rl_date.setOnClickListener(this);
        ll_create_account.setOnClickListener(this);
        ll_create_account.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_date:
                SelectBirthDate();
                break;

            case R.id.ll_next:
                CheckEmployeeId(et_employee_ID.getText().toString(), tv_emp_bdate.getText().toString());
                break;

            case R.id.ll_cancel:
                finish();
                break;

            case R.id.lnmainback:
                finish();
                break;

            case R.id.ll_create_account:
                AccountCreate(et_employee_ID.getText().toString(), tv_emp_bdate.getText().toString(), et_password.getText().toString(), et_email.getText().toString());
                break;
            default:
                break;
        }
    }

    private void SelectBirthDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tv_emp_bdate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
                case R.id.et_employee_ID:
                    validateEmpID();
                    break;
                case R.id.tv_emp_bdate:
                    validateBDate();
                    break;
                case R.id.et_password:
                    validatePass();
                    break;
            }
        }
    }

    private boolean validateEmpID() {
        String email = et_employee_ID.getText().toString().trim();
        if (email.isEmpty()) {
            et_employee_ID.setError("Please enter employee ID.");
            requestFocus(et_employee_ID);
            return false;
        } else {
            et_employee_ID.setError(null);
        }
        return true;
    }

    private boolean validateBDate() {
        String email = tv_emp_bdate.getText().toString().trim();
        if (email.isEmpty()) {
            tv_emp_bdate.setError("Please select corrent birth date");
            requestFocus(tv_emp_bdate);
            return false;
        } else {
            tv_emp_bdate.setError(null);
        }
        return true;
    }

    public boolean validatePass() {
        String email = et_password.getText().toString();
        if (email.isEmpty()) {
            et_password.setError("Please enter password.");
            requestFocus(et_password);
            return false;
        } else {
            et_password.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void CheckEmployeeId(final String emp_id, final String emp_birthday) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.People_EmployeeId_Verify, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("People_EmployeeId_Check")) {
                            JSONArray array = object.getJSONArray("People_EmployeeId_Check");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        //  ll_address,ll_password
                                        ll_emp_id.setVisibility(View.GONE);
                                        ll_birth_date.setVisibility(View.GONE);
                                        ll_address.setVisibility(View.VISIBLE);
                                        ll_password.setVisibility(View.VISIBLE);
                                        ll_next.setVisibility(View.GONE);
                                        ll_create_account.setVisibility(View.VISIBLE);
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
                    map.put("EmployeeId", emp_id);
                    map.put("Birthdate", emp_birthday);
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

    public void AccountCreate(final String emp_id, final String emp_birthday, final String emp_pass, final String emp_email) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.People_Create_Account_Step1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment1_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("People_Create_Account_Step1")) {
                            JSONArray array = object.getJSONArray("People_Create_Account_Step1");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {

                                        if (array.optJSONObject(i).getString("Status_Message").equalsIgnoreCase("Step2.")) {
                                            pd.dismiss();
                                            Intent intent = new Intent(activity, RegistrationWithLinkdin.class);
                                            intent.putExtra("emp_id", emp_id);
                                            startActivity(intent);
                                            finish();
                                        } else if (array.optJSONObject(i).getString("Status_Message").equalsIgnoreCase("Exits User.")) {
                                            pd.dismiss();
                                            Intent intent = new Intent(activity, LoginActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }

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

                    map.put("EmailId", emp_email);
                    map.put("Password", emp_pass);
                    map.put("EmployeeId", emp_id);
                    map.put("Birthdate", emp_birthday);

                    Log.e("EmailId", "" + emp_email);
                    Log.e("Password", "" + emp_pass);
                    Log.e("EmployeeId", "" + emp_id);
                    Log.e("Birthdate", "" + emp_birthday);

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
