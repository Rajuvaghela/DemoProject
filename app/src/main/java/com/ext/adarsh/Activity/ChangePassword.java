package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class ChangePassword extends BaseActivity implements View.OnClickListener {

    EditText et_current_password,et_new_password,et_confirm_password;
    TextView tv_change_password,tv_cancel;
    String cur_pass,new_pass,confirm_pass;
    ProgressDialog pd;
    Activity activity;

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.header)
    TextView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_change_password, frameLayout);
        ButterKnife.bind(this);
        activity =this;
        pd = Utility.getDialog(activity);

        et_current_password = (EditText)findViewById(R.id.et_current_password);
        et_new_password = (EditText)findViewById(R.id.et_new_password);
        et_confirm_password = (EditText)findViewById(R.id.et_confirm_password);
        tv_change_password =(TextView)findViewById(R.id.tv_change_password);
        tv_cancel =(TextView)findViewById(R.id.tv_cancel);

        tv_change_password.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        header.setTypeface(Utility.getTypeFace());
        et_current_password.setTypeface(Utility.getTypeFace());
        et_new_password.setTypeface(Utility.getTypeFace());
        et_confirm_password.setTypeface(Utility.getTypeFace());


        cur_pass = et_current_password.getText().toString().trim();
        new_pass = et_new_password.getText().toString().trim();
        confirm_pass = et_confirm_password.getText().toString().trim();

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        tv_change_password.setOnClickListener(this);

        tv_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_change_password:
                if(validation()) {
                    if (Utility.getPeoplePasswordPreference().equalsIgnoreCase(cur_pass)){
                        CheckPassword(cur_pass);
                    }else {
                        Toast.makeText(activity,"Please Enter Correct Password",Toast.LENGTH_SHORT).show();
                    }

                }
            break;

            case R.id.tv_cancel:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

       }
    }

    private void CheckPassword(final String cur_pass) {
       pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Change_Password, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Change_Password")) {
                            JSONArray array = object.getJSONArray("Change_Password");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(activity, MainActivity.class));
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
                    map.put("EmployeeCode", Utility.getEmployeeCode());
                    map.put("OldPaassword", cur_pass);
                    map.put("NewPassword", new_pass);
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

    private boolean validation() {
        cur_pass = et_current_password.getText().toString().trim();
        new_pass = et_new_password.getText().toString().trim();
        confirm_pass = et_confirm_password.getText().toString().trim();

        if(cur_pass.isEmpty()) {
            et_current_password.setError("Please enter Your current Password.");
            requestFocus(et_current_password);
            return false;
        }
      else  if(new_pass.isEmpty()) {
            et_new_password.setError("Please enter new Password.");
            requestFocus(et_new_password);
            return false;
        }
       else if(confirm_pass.isEmpty()) {
            et_confirm_password.setError("Please enter confirm Password.");
            requestFocus(et_confirm_password);
            return false;
        }
       else if(!(new_pass.equals(confirm_pass))){
            et_confirm_password.setError("Password does not match");
            requestFocus(et_confirm_password);
            return false;
        }

        return  true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }
}
