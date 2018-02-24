package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class TopicAddingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_topic_title)
    EditText et_topic_title;

    @BindView(R.id.et_topic_des)
    EditText et_topic_des;

    @BindView(R.id.tv_add_topic)
    TextView tv_add_topic;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;


    @BindView(R.id.text1)
    TextView text1;


    @BindView(R.id.lnback)
    LinearLayout lnback;

    ProgressDialog pd;
    Activity activity;

    @BindView(R.id.ll_cancel)
    LinearLayout ll_cancel;

    @BindView(R.id.ll_add_topic)
    LinearLayout ll_add_topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_adding);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);

        et_topic_title.setTypeface(Utility.getTypeFace());
        et_topic_des.setTypeface(Utility.getTypeFace());
        et_topic_title.addTextChangedListener(new MyTextWatcher(et_topic_title));
        et_topic_des.addTextChangedListener(new MyTextWatcher(et_topic_des));

        tv_add_topic.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());

        text1.setTypeface(Utility.getTypeFaceTab());
        lnback.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        ll_add_topic.setOnClickListener(this);

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
                case R.id.et_topic_des:
                    validateFname();
                    break;

                case R.id.et_topic_title:
                    validateTitle();
                    break;


            }
        }
    }

    private boolean validateTitle() {
        String email = et_topic_title.getText().toString().trim();
        if (email.isEmpty()) {
            et_topic_title.setError("Please Write name of topic");
            requestFocus(et_topic_title);
            return false;
        } else {
            et_topic_title.setError(null);
        }
        return true;
    }

    private boolean validateFname() {
        String email = et_topic_des.getText().toString().trim();
        if (email.isEmpty()) {
            et_topic_des.setError("Please Write Some description");
            requestFocus(et_topic_des);
            return false;
        } else {
            et_topic_des.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, Knowledge.class));
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnback:
                startActivity(new Intent(activity, Knowledge.class));
                finish();
                break;

            case R.id.ll_cancel:
                startActivity(new Intent(activity, Knowledge.class));
                finish();
                break;

            case R.id.ll_add_topic:

                if (et_topic_title.getText().toString().trim().equalsIgnoreCase("")) {
                    et_topic_title.setError("Please Write name of topic");
                    requestFocus(et_topic_title);
                } else if (et_topic_des.getText().toString().trim().equalsIgnoreCase("")) {
                    et_topic_des.setError("Please Write Some description");
                    requestFocus(et_topic_des);
                } else {
                    AddKnowledgeTopic(et_topic_title.getText().toString(), et_topic_des.getText().toString());
                }


                break;
        }
    }

    public void AddKnowledgeTopic(final String title, final String des) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Topic_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Topic_Add")) {
                            JSONArray array = object.getJSONArray("Knowledge_Topic_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        startActivity(new Intent(activity, Knowledge.class));
                                        finish();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();

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
                    map.put("Title", title);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Description", des);
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
