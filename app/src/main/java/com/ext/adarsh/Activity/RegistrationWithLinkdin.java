package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class RegistrationWithLinkdin extends AppCompatActivity {

    ProgressDialog pd;

    @BindView(R.id.tv_reg_heading)
    TextView tv_reg_heading;

    @BindView(R.id.tv_skip)
    TextView tv_skip;

    @BindView(R.id.heder1)
    TextView heder1;

    @BindView(R.id.heder2)
    TextView heder2;

    @BindView(R.id.ll_skip)
    LinearLayout ll_skip;

    @BindView(R.id.lnmainback)
    LinearLayout lnmainback;

    @BindView(R.id.imgLinkdinLogin)
    ImageView imgLinkdinLogin;

    Activity activity;
    String year = "", company_name = "", start_date = "", title = "", cname = "", month = "";
    private String emp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_with_linkdin);
        activity = this;
        pd = Utility.getDialog(activity);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        emp_id = bundle.getString("emp_id");

        computePakageHash();

        tv_reg_heading.setTypeface(Utility.getTypeFace());
        tv_skip.setTypeface(Utility.getTypeFaceTab());
        heder1.setTypeface(Utility.getTypeFace());
        heder2.setTypeface(Utility.getTypeFace());

        imgLinkdinLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                handleLogin();
            }
        });

        ll_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, LoginActivity.class));
                finish();
            }
        });

        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, RegistrationActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, RegistrationActivity.class));
        finish();
    }

    private void computePakageHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ext.adarsh",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }

    private void handleLogin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                Log.e("succes", "sucess");
                fetchPersonalInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                Log.e("error", error.toString());
            }
        }, true);
    }

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void fetchPersonalInfo() {
        Log.e("Access token->", LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue());
        String access_token = LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue();

        //  String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address,picture-urls::(original))";
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,picture-url,industry,summary,specialties,positions:(id,title,summary,start-date,end-date,is-current,company:(id,name,type,size,industry,ticker)),educations:(id,school-name,field-of-study,start-date,end-date,degree,activities,notes),associations,interests,num-recommenders,date-of-birth,publications:(id,title,publisher:(name),authors:(id,name),date,url,summary),patents:(id,title,summary,number,status:(id,name),office:(name),inventors:(id,name),date,url),languages:(id,language:(name),proficiency:(level,name)),skills:(id,skill:(name)),certifications:(id,name,authority:(name),number,start-date,end-date),courses:(id,name,number),recommendations-received:(id,recommendation-type,recommendation-text,recommender),honors-awards,three-current-positions,three-past-positions,volunteer)?format=json";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                Log.e("res", "" + apiResponse.getResponseDataAsJson().toString());
                // Success!
                try {
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
       /*             if (jsonObject.has("positions")) {
                        JSONArray array = jsonObject.getJSONArray("positions");
                        if (array.length() != 0) {
                            Log.e("length", "" +array.length());
                        }
                    }*/
                    String firstName = jsonObject.getString("firstName");
                    String lastName = jsonObject.getString("lastName");
                    String pictureUrl = jsonObject.getString("pictureUrl");
                    //    String emailAddress = jsonObject.getString("emailAddress");
                    String headline = jsonObject.getString("headline");
                    String positions = jsonObject.getString("positions");
                    JSONObject object = new JSONObject(positions);


                    if (object.has("values")) {
                        JSONArray array = object.getJSONArray("values");
                        if (array.length() != 0) {
                            company_name = array.optJSONObject(0).getString("company");
                            String id = array.optJSONObject(0).getString("id");
                            String is_current = array.optJSONObject(0).getString("isCurrent");
                            start_date = array.optJSONObject(0).getString("startDate");
                            title = array.optJSONObject(0).getString("title");
                            Log.e("company_name", "" + company_name);
                            Log.e("id", "" + id);
                            Log.e("is_current", "" + is_current);
                            Log.e("start_date", "" + start_date);
                            Log.e("title", "" + title);
                        }
                    }

                    JSONObject object_company_name = new JSONObject(company_name);
                    if (object_company_name.has("name")) {
                        cname = object_company_name.getString("name");
                        Log.e("name", "" + cname);

                    }

                    JSONObject object_company_start = new JSONObject(start_date);
                    if (object_company_start.has("month")) {
                        month = object_company_start.getString("month");
                        Log.e("month", "" + month);
                    }
                    if (object_company_start.has("year")) {
                        year = object_company_start.getString("year");
                        Log.e("year", "" + year);
                    }


                /*        JSONArray array = object.getJSONArray("values");
                        if (array.length() != 0) {
                            Log.e("length", "" +array.length());
                        }*/


                    Log.e("fname", "" + firstName);
                    Log.e("lastName", "" + lastName);
                    Log.e("pictureUrl", "" + pictureUrl);
                    //    Log.e("emailAddress",""+emailAddress);
                    Log.e("headline", "" + headline);
                    Log.e("positions", "" + positions);


                    StringBuilder sb = new StringBuilder();
                    sb.append("First Name: " + firstName);
                    sb.append("\n\n");
                    sb.append("Last Name: " + lastName);
                    sb.append("\n\n");
                    String start_date = month + "/" + "01" + "/" + year;
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                    String end_date = df.format(c.getTime());
                    pd.dismiss();
                    AccountCreateLinkdin(title, cname, "place", start_date, end_date, emp_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                Log.e("error", liApiError.getMessage());
            }
        });
    }

    public void AccountCreateLinkdin(final String possition, final String company_name,
                                     final String company_place, final String start_date,
                                     final String end_date, final String employee_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.People_Create_Account_Linkdin_Step2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment1_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("People_Create_Account_Linkdin_Step2")) {
                            JSONArray array = object.getJSONArray("People_Create_Account_Linkdin_Step2");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Intent intent = new Intent(activity, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
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
                    map.put("Position", possition);
                    map.put("CompanyName", company_name);
                    map.put("CompanyPlace", company_place);
                    map.put("StartDate", start_date);
                    map.put("EndDate", end_date);
                    map.put("EmployeeCode", employee_id);
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
