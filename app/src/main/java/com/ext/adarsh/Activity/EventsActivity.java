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
import com.ext.adarsh.Adapter.AdapterMyEvents;
import com.ext.adarsh.Adapter.AdapterPastEvents;
import com.ext.adarsh.Adapter.AdapterUpcomingEvents;
import com.ext.adarsh.Adapter.EventsPagerAdapter;
import com.ext.adarsh.Bean.BeanEvents;
import com.ext.adarsh.Fragment.MyEvents;
import com.ext.adarsh.Fragment.PastEvents;
import com.ext.adarsh.Fragment.UpcomingEvents;
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

public class EventsActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener{

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.pager)
    ViewPager eventsviewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.h1)
    TextView h1;

    static Activity activity;

   static ProgressDialog pd;

    private EventsPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_events, frameLayout);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);

        getEventsData();
        h1.setTypeface(Utility.getTypeFace());

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mAdapter = new EventsPagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(eventsviewPager);
        eventsviewPager.setAdapter(mAdapter);
        eventsviewPager.setOffscreenPageLimit(4);
        tabLayout.setOnTabSelectedListener(this);

        Utility.changeTabsFont(tabLayout);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        eventsviewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
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

    public static void getEventsData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Event_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Upcoming_Event_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Upcoming_Event_Array");
                            if (jsonArray.length() != 0) {
                                ArrayList<BeanEvents> beanEventses = new ArrayList<>();
                                beanEventses.addAll((Collection<? extends BeanEvents>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEvents>>() {
                                }.getType()));
                                AdapterUpcomingEvents adapter = new AdapterUpcomingEvents(activity,beanEventses);
                                UpcomingEvents.recylerupcomingevents.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                UpcomingEvents.recylerupcomingevents.setAdapter(null);
                                pd.dismiss();
                            }
                        }

                        if (object.has("My_Event_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Event_Array");
                            if (jsonArray.length() != 0) {
                                ArrayList<BeanEvents> beanEventses = new ArrayList<>();
                                beanEventses.addAll((Collection<? extends BeanEvents>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEvents>>() {
                                }.getType()));
                                AdapterMyEvents adapter = new AdapterMyEvents(activity,beanEventses);
                                MyEvents.recylerupcomingevents.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                MyEvents.recylerupcomingevents.setAdapter(null);
                                pd.dismiss();
                            }
                        }

                        if (object.has("Past_Event_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Past_Event_Array");
                            if (jsonArray.length() != 0) {
                                ArrayList<BeanEvents> beanEventses = new ArrayList<>();
                                beanEventses.addAll((Collection<? extends BeanEvents>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEvents>>() {
                                }.getType()));
                                AdapterPastEvents adapter = new AdapterPastEvents(activity,beanEventses);
                                PastEvents.recylerupcomingevents.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                PastEvents.recylerupcomingevents.setAdapter(null);
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
                    map.put("FilterDept", "");
                    map.put("FilterBranch","" );
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
