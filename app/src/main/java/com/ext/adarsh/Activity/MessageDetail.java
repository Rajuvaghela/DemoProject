package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.ext.adarsh.Adapter.AdapterMessageDetail;
import com.ext.adarsh.Bean.BeanAttachmentData;
import com.ext.adarsh.Bean.BeanMessageDetail;
import com.ext.adarsh.Fragment.InboxFragment;
import com.ext.adarsh.Fragment.SentFragment;
import com.ext.adarsh.Fragment.StarredFragment;
import com.ext.adarsh.Fragment.TrashFragment;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
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

public class MessageDetail extends BaseActivity {

    static Activity activity;
    static List<BeanMessageDetail> messageDetails = new ArrayList<>();

    @BindView(R.id.lnmessageback)
    LinearLayout lnmessageback;

    /*@BindView(R.id.firstname)
    TextView firstname;*/

    public static ProgressDialog pd;
    // ProgressDialog pDialog;
    BeanMessageDetail movie = new BeanMessageDetail();
    TextView text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12, text13, text14;
    Button btn1;
    LinearLayout lnback,ln_favourite;
    static String messageId;
    static AdapterMessageDetail adapterMessageDetail;
    static ArrayList<BeanMessageDetail> beanMessageDetails = new ArrayList();
    static ArrayList<BeanAttachmentData> beanAttachmentData = new ArrayList();

    ImageView iv_favourite,iv_favourite_red;

    public static RecyclerView recylermessagedetail;
    Dialog dd;
    String IsFavourite,Frag;
    Boolean is_favourite;
    static TextView text1;
    LinearLayout ll_message_delete,ll_recover,v_g;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);

        messageId = getIntent().getExtras().getString("MessageId");
        IsFavourite = getIntent().getExtras().getString("IsFavourite");
        Frag = getIntent().getExtras().getString("frag");

        recylermessagedetail = (RecyclerView) findViewById(R.id.rv_message_detail);
        recylermessagedetail.setHasFixedSize(true);
        LinearLayoutManager lnmanager = new LinearLayoutManager(activity);
        recylermessagedetail.setLayoutManager(lnmanager);
        recylermessagedetail.setItemAnimator(new DefaultItemAnimator());

        text1 = (TextView) findViewById(R.id.text1);
        ln_favourite = (LinearLayout) findViewById(R.id.ln_favourite);
        ll_message_delete = (LinearLayout) findViewById(R.id.ll_message_delete);
        ll_recover = (LinearLayout) findViewById(R.id.ll_recover);
        v_g = (LinearLayout) findViewById(R.id.v_g);
        iv_favourite = (ImageView) findViewById(R.id.iv_favourite);
        iv_favourite_red = (ImageView) findViewById(R.id.iv_favourite_red);

        if(IsFavourite == null){
            iv_favourite_red.setVisibility(View.GONE);
            iv_favourite.setVisibility(View.GONE);
        }

        showMessageDetails();

        lnmessageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(Frag.equalsIgnoreCase("Trash")){
            ll_recover.setVisibility(View.VISIBLE);
            v_g.setVisibility(View.GONE);
        }


        if (IsFavourite.equalsIgnoreCase("A")) {
            iv_favourite_red.setVisibility(View.VISIBLE);
            iv_favourite.setVisibility(View.GONE);
        } else if(IsFavourite.equalsIgnoreCase("D")){
            iv_favourite_red.setVisibility(View.GONE);
            iv_favourite.setVisibility(View.VISIBLE);
        }

        ln_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Addtostarred(messageId);
                if (iv_favourite.getVisibility() == View.VISIBLE) {
                    iv_favourite.setVisibility(View.GONE);
                    iv_favourite_red.setVisibility(View.VISIBLE);
                } else {
                    iv_favourite.setVisibility(View.VISIBLE);
                    iv_favourite_red.setVisibility(View.GONE);
                }
            }
        });

        ll_message_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Frag.equalsIgnoreCase("Sent")||Frag.equalsIgnoreCase("Inbox")){
                    deletemessage(messageId);
                }else if(Frag.equalsIgnoreCase("Trash")){

                    deleteTrashmessage(messageId);
                }
            }
        });

        ll_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recovermessage(messageId);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, Message.class));
        finish();
    }

    public static void showMessageDetails() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Detail, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Subject")) {
                            JSONArray jsonArray = object.getJSONArray("Message_Subject");

                            if (jsonArray.length() != 0) {
                                JSONObject object1 = jsonArray.getJSONObject(0);
                                Log.e("subject",object1.getString("Subject"));
                                text1.setText(object1.getString("Subject"));
                            }
                        }
                        if (object.has("Message_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Message_Detail_Array");
                            if (jsonArray.length() != 0) {
                                beanMessageDetails.clear();
                                beanMessageDetails.addAll((Collection<? extends BeanMessageDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMessageDetail>>() {
                                }.getType()));
                                Log.e("size", String.valueOf(beanMessageDetails.size()));

                            }
                        }
                        if (object.has("Message_Attachment_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Message_Attachment_Array");
                            if (jsonArray.length() != 0) {
                                beanAttachmentData.addAll((Collection<? extends BeanAttachmentData>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAttachmentData>>() {
                                }.getType()));
                            }
                        }

                        adapterMessageDetail = new AdapterMessageDetail(activity,beanMessageDetails,beanAttachmentData);
                        recylermessagedetail.setAdapter(adapterMessageDetail);

                        pd.dismiss();
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
                    map.put("MessageId", messageId);

                    Log.e("Hashkey", Utility.getHashKeyPreference());
                    Log.e("LoginId", Utility.getPeopleIdPreference());
                    Log.e("MessageId", messageId);

                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            showSuccessAlertDialog(activity,activity.getResources().getString(R.string.network_message));
        }
    }

    private void Addtostarred(final String messageId) {
        is_favourite = false;
        if (checkConnectivity()) {
            //    pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Add_To_Starred, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Add_To_Starred")) {
                            JSONArray array = object.getJSONArray("Message_Add_To_Starred");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        is_favourite = true;
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        StarredFragment.message();
                                        /*InboxFragment.message();
                                        SentFragment.message();
                                        TrashFragment.message();*/
                                    } else {
                                        //    pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        //   pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    //    pd.dismiss();
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
                        //      pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("MessageId", messageId);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            //   pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }

    }

    private void recovermessage(final String messageId) {
        if (checkConnectivity()) {
            //    pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Recover, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Recover")) {
                            JSONArray array = object.getJSONArray("Message_Recover");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        SentFragment.message();
                                        InboxFragment.message();
                                        StarredFragment.message();
                                        TrashFragment.message();
                                        finish();
                                    } else {
                                        //    pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        //   pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    //    pd.dismiss();
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
                        //      pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("MessageId",messageId);
                    map.put("LoginId", Utility.getPeopleIdPreference());

                    Log.e("API","Message_Recover");
                    Log.e("MessageId",""+messageId);
                    Log.e("LoginId", ""+Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            //   pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletemessage(final String messageId) {
        if (checkConnectivity()) {
            //    pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Delete")) {
                            JSONArray array = object.getJSONArray("Message_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        SentFragment.message();
                                        InboxFragment.message();
                                        StarredFragment.message();
                                        finish();
                                    } else {
                                        //    pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        //   pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    //    pd.dismiss();
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
                        //      pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("MessageId",messageId);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            //   pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteTrashmessage(final String messageId) {
        if (checkConnectivity()) {
            //    pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Trash_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Trash_Delete")) {
                            JSONArray array = object.getJSONArray("Message_Trash_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        TrashFragment.message();
                                        finish();
                                    } else {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        //   pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    //    pd.dismiss();
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
                        //      pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("MessageId", messageId);
                    map.put("LoginId", Utility.getPeopleIdPreference());


                    Log.e("MessageId", messageId);
                    Log.e("LoginId", Utility.getPeopleIdPreference());

                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            //   pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }

    }

}
