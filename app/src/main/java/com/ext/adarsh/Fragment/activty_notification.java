package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ext.adarsh.Adapter.AdapterNotification;
import com.ext.adarsh.Bean.BeanNotifiactionlist;
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
import java.util.Map;

import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

/**
 * Created by ExT-Emp-001 on 11-07-2017.
 */
public class activty_notification extends Fragment {
    public static Activity activity;

    private static RecyclerView recyclerView;
    public static AdapterNotification mAdapter;
    public static ArrayList<BeanNotifiactionlist> notification_list = new ArrayList<>();


   /* @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.tv4)
    TextView tv4;

    @BindView(R.id.tv5)
    TextView tv5;

    @BindView(R.id.tv6)
    TextView tv6;

    @BindView(R.id.tv7)
    TextView tv7;

    @BindView(R.id.tv8)
    TextView tv8;

    @BindView(R.id.tv9)
    TextView tv9;

    @BindView(R.id.tv10)
    TextView tv10;

    @BindView(R.id.tv11)
    TextView tv11;

    @BindView(R.id.tv12)
    TextView tv12;

    @BindView(R.id.tv13)
    TextView tv13;

    @BindView(R.id.tv14)
    TextView tv14;

    @BindView(R.id.tv15)
    TextView tv15;

    @BindView(R.id.tv16)
    TextView tv16;

    @BindView(R.id.tv17)
    TextView tv17;

    @BindView(R.id.tv18)
    TextView tv18;

    @BindView(R.id.tv19)
    TextView tv19;

    @BindView(R.id.tv20)
    TextView tv20;

    @BindView(R.id.commingsoon)
    TextView commingsoon;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_notification, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        //   setFonat();
        activity = getActivity();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        mAdapter = new AdapterNotification(notification_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        notification();

       /* composemail_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,MessageCompose.class);
                intent.putExtra("Key","C");
                startActivity(intent);
            }
        });*/
    }

    public static void notification() {
        if (checkConnectivity()) {
            //    pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Notification_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Notification_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Notification_Array");
                            if (jsonArray.length() != 0) {
                                notification_list.clear();
                                notification_list.addAll((Collection<? extends BeanNotifiactionlist>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanNotifiactionlist>>() {
                                }.getType()));
                                mAdapter = new AdapterNotification(notification_list,activity);
                                recyclerView.setAdapter(mAdapter);
                                //  pd.dismiss();
                            }
                        }

                        //   pd.dismiss();
                    } catch (JSONException e) {
                        //  pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    //  pd.dismiss();
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            //    pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

}

 /*   public void setFonat (){
        commingsoon.setTypeface(Utility.getTypeFaceTab());
        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv10.setTypeface(Utility.getTypeFace());
        tv11.setTypeface(Utility.getTypeFace());
        tv12.setTypeface(Utility.getTypeFace());
        tv13.setTypeface(Utility.getTypeFace());
        tv14.setTypeface(Utility.getTypeFace());
        tv15.setTypeface(Utility.getTypeFace());
        tv16.setTypeface(Utility.getTypeFace());
        tv17.setTypeface(Utility.getTypeFace());
        tv18.setTypeface(Utility.getTypeFace());
        tv19.setTypeface(Utility.getTypeFace());
        tv20.setTypeface(Utility.getTypeFace());

    }*/


