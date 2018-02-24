package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
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
import com.ext.adarsh.Adapter.AdapterMyPolls;
import com.ext.adarsh.Adapter.AdapterPolls;
import com.ext.adarsh.Adapter.PollsPagerAdapter;
import com.ext.adarsh.Bean.BeanPoll;
import com.ext.adarsh.Fragment.mypolls;
import com.ext.adarsh.Fragment.polls;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class PollsActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.h1)
    TextView h1;

    PollsPagerAdapter mAdapter;
    public static ProgressDialog pd;
    public static Activity activity;

    public static boolean polls_radio = false;
    public static boolean polls_linear = false;

    public static boolean mypolls_radio = false;
    public static boolean mypolls_linear = false;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_polls, frameLayout);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);
        h1.setTypeface(Utility.getTypeFace());


        Intent intent = getIntent();
        id = intent.getExtras().getString("id");

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mAdapter = new PollsPagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(4);
        if (id == null) {
            viewPager.setCurrentItem(0);
        } else if (id.equalsIgnoreCase("1")) {
            viewPager.setCurrentItem(1);
        } else {
            viewPager.setCurrentItem(0);
        }

        tabLayout.setOnTabSelectedListener(this);
        getPollsData();
        Utility.changeTabsFont(tabLayout);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }

    public static void getPollsData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Polls_And_MyPolls_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Polls_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Polls_Array");
                            if (jsonArray.length() != 0) {
                                polls_radio = false;
                                polls_linear = false;
                                ArrayList<BeanPoll> beanPolls = new ArrayList<>();
                                beanPolls.addAll((Collection<? extends BeanPoll>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPoll>>() {
                                }.getType()));
                                AdapterPolls adapter = new AdapterPolls(activity, beanPolls);
                                polls.recylerpolls.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                polls.recylerpolls.setAdapter(null);
                                pd.dismiss();
                            }
                        }

                        if (object.has("My_Polls_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Polls_Array");
                            if (jsonArray.length() != 0) {
                                mypolls_radio = false;
                                mypolls_linear = false;
                                ArrayList<BeanPoll> beanPolls = new ArrayList<>();
                                beanPolls.addAll((Collection<? extends BeanPoll>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPoll>>() {
                                }.getType()));
                                AdapterMyPolls adapter = new AdapterMyPolls(activity, beanPolls);
                                mypolls.recylerpolls.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                mypolls.recylerpolls.setAdapter(null);
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
                    map.put("PeopleId", Utility.getPeopleIdPreference());
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
