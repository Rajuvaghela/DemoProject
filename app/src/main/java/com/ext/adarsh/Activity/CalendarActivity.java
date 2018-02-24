package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.desai.vatsal.mydynamiccalendar.AppConstants;
import com.desai.vatsal.mydynamiccalendar.EventModel;
import com.desai.vatsal.mydynamiccalendar.GetEventListListener;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;
import com.desai.vatsal.mydynamiccalendar.OnEventClickListener;
import com.desai.vatsal.mydynamiccalendar.OnWeekDayViewClickListener;
import com.ext.adarsh.Adapter.Adapter1CalanderSharePeople;
import com.ext.adarsh.Adapter.Adapter2CalanderPeople;
import com.ext.adarsh.Adapter.AdapterCalendarPeopleList;
import com.ext.adarsh.Adapter.AdapterCalendarSharedPeopleList;
import com.ext.adarsh.Adapter.AdapterCalenderList;
import com.ext.adarsh.Adapter.ModelCalendar;
import com.ext.adarsh.Bean.BeanCalanderPeople;
import com.ext.adarsh.Bean.BeanCalendarPeopleShare;
import com.ext.adarsh.Bean.BeanCalendarSharePeopleList;
import com.ext.adarsh.Bean.BeanCalenderEventArray;
import com.ext.adarsh.Bean.ModelClass3;
import com.ext.adarsh.Bean.ModelMutilpleDelete;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.RecyclerItemClickListener;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.desai.vatsal.mydynamiccalendar.AppConstants.eventList;
import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class CalendarActivity extends BaseActivity implements View.OnClickListener {

    public static MyDynamicCalendar myCalendar;
    TextView textView, share1, share2;

    private static ProgressDialog pd;
    Dialog Dialog_share, dialog_event_list;
    static Dialog open_tag_dialog_people;
    Activity activity;
    static Activity activity2;

    public static ArrayList<BeanCalenderEventArray> beanCalenderEventArray = new ArrayList<>();
    public static ArrayList<BeanCalendarPeopleShare> beanCalendarPeopleShares = new ArrayList<>();
    public static ArrayList<BeanCalendarSharePeopleList> beanCalendarSharePeopleLists = new ArrayList<>();
    public static LinearLayout lnback, ln_shared_peoplelist, ln_shared_you_peoplelist;

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.actionmenu)
    ImageView actionmenu;

    @BindView(R.id.tv1)
    TextView tv1;

    public static RecyclerView rv_list, rv_shared_peoplelist, rv_shared_you_peoplelist;
    ArrayList<ModelCalendar> list;

    FloatingActionButton fab_add_calander_event;
    LinearLayout ll_share_calander;
    private static ArrayList<BeanCalanderPeople> beanCalanderPeoples = new ArrayList<>();
    static RecyclerView recyclerview5;
    static RecyclerView recyclerview6;
    static Adapter1CalanderSharePeople adapter1people;
    static RecyclerView.LayoutManager recylerViewLayoutManager3;
    public static RecyclerView.Adapter recyclerview_adapter3;
    public static List<ModelClass3> item_list3 = new ArrayList<>();
    static RecyclerView rv_select_person;

    ImageView iv_event_c1, iv_event_c2, iv_event_c3, iv_event_c4, iv_event_c5, iv_event_c6, iv_event_c7, iv_event_c8, iv_event_c9,
            iv_event_c10, iv_event_c11, iv_event_c12;
    String EventBgColor = "#FE7642", EventFontColor = "#FFFFFF";
    View view_selected_color;//ll_edit_event , ll_add_event,ll_create_events,ll_save_events

    static ArrayList<ModelMutilpleDelete> multipleImageCode = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_calendar, frameLayout);
        ButterKnife.bind(this);
        pd = Utility.getDialog(this);
        activity = this;
        activity2 = this;

        multipleImageCode.clear();
        multipleImageCode.add(new ModelMutilpleDelete("#FE7642",1));
        multipleImageCode.add(new ModelMutilpleDelete("#E32B23",2));
        multipleImageCode.add(new ModelMutilpleDelete("#DEADFA",3));
        multipleImageCode.add(new ModelMutilpleDelete("#A0C0F8",3));
        multipleImageCode.add(new ModelMutilpleDelete("#A0C0F8",4));
        multipleImageCode.add(new ModelMutilpleDelete("#5884E2",5));
        multipleImageCode.add(new ModelMutilpleDelete("#22D6E0",6));
        multipleImageCode.add(new ModelMutilpleDelete("#6AE7C1",7));
        multipleImageCode.add(new ModelMutilpleDelete("#43B35A",8));
        multipleImageCode.add(new ModelMutilpleDelete("#FFD56F",9));
        multipleImageCode.add(new ModelMutilpleDelete("#FFB97A",10));
        multipleImageCode.add(new ModelMutilpleDelete("#FF8A80",11));
        multipleImageCode.add(new ModelMutilpleDelete("#E1E1E1",12));

        tv1.setTypeface(Utility.getTypeFace());
        ll_share_calander = (LinearLayout) findViewById(R.id.ll_share_calander);
        fab_add_calander_event = (FloatingActionButton) findViewById(R.id.fab_add_calander_event);
        myCalendar = (MyDynamicCalendar) findViewById(R.id.myCalendar);

        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        rv_shared_peoplelist = (RecyclerView) findViewById(R.id.rv_shared_peoplelist);
        rv_shared_you_peoplelist = (RecyclerView) findViewById(R.id.rv_shared_you_peoplelist);
        ln_shared_you_peoplelist = (LinearLayout) findViewById(R.id.ln_shared_you_peoplelist);
        ln_shared_peoplelist = (LinearLayout) findViewById(R.id.ln_shared_peoplelist);

        share1 = (TextView) findViewById(R.id.share1);
        share2 = (TextView) findViewById(R.id.share2);
        share1.setTypeface(Utility.getTypeFace());
        share2.setTypeface(Utility.getTypeFace());


        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(activity);
        rv_shared_peoplelist.setLayoutManager(mLayoutManager2);
        rv_shared_peoplelist.setItemAnimator(new DefaultItemAnimator());
        rv_shared_peoplelist.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(activity);
        rv_shared_you_peoplelist.setLayoutManager(mLayoutManager3);
        rv_shared_you_peoplelist.setItemAnimator(new DefaultItemAnimator());
        rv_shared_you_peoplelist.setNestedScrollingEnabled(false);


        fab_add_calander_event.setOnClickListener(this);

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });
        tv1.setTypeface(Utility.getTypeFace());
        getCalanderData();

        list = new ArrayList<>();

        ll_share_calander.setOnClickListener(this);
        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(final Date date, int position) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                list.clear();
                for (int i = 0; i < eventList.size(); i++) {
                    String dt = eventList.get(i).getStrDate();
                    if (sdf.format(date).equalsIgnoreCase(dt)) {

                        list.add(new ModelCalendar(AppConstants.eventList.get(i).getStrName(), AppConstants.eventList.get(i).getId(), AppConstants.eventList.get(i).getBgcolor(), AppConstants.eventList.get(i).getFontcolor(), AppConstants.eventList.get(i).getEventType(), AppConstants.eventList.get(i).getPersonId(), AppConstants.eventList.get(i).getPeopleId()));
                    }
                }
                CalanderEventListDialog();
              /*  AdapterCalenderList adapterCalenderList = new AdapterCalenderList(activity, list);
                rv_list.setAdapter(adapterCalenderList);*/

            }

            @Override
            public void onLongClick(Date date) {

            }
        });

        actionmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });

        myCalendar.setNextPreviousIndicatorColor(R.color.appcolor);

        myCalendar.getEventList(new GetEventListListener() {
            @Override
            public void eventList(ArrayList<EventModel> eventList) {
                Log.e("tag", "eventList.size():-" + eventList.size());
                for (int i = 0; i < eventList.size(); i++) {
                    Log.e("tag", "eventList.getStrName:-" + eventList.get(i).getStrName());
                }
            }
        });

    }

    void showPopupWindow(View view) {
        PopupMenu popup = new PopupMenu(CalendarActivity.this, view);
        popup.getMenuInflater().inflate(R.menu.popupmenu2, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_month:
                        showMonthView();
                        return true;
                    case R.id.action_month_with_below_events:
                        showMonthViewWithBelowEvents();
                        return true;
                    case R.id.action_week:
                        showWeekView();
                        return true;
                    case R.id.action_day:
                        showDayView();
                        return true;
                    case R.id.action_agenda:
                        showAgendaView();
                        return true;
                    case R.id.action_today:
                        myCalendar.goToCurrentDate();
                        return true;

                }
                return true;
            }
        });

        popup.show();
    }

    private void showMonthView() {

        myCalendar.showMonthView();

        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date, int position) {
                Log.e("date", String.valueOf(date));
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }

    private void showMonthViewWithBelowEvents() {

        myCalendar.showMonthViewWithBelowEvents();

        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date, int position) {
                Log.e("date", String.valueOf(date));
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }

    private void showWeekView() {

        myCalendar.showWeekView();

        myCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onClick() {
                Log.e("showWeekView", "from setOnEventClickListener onClick");
            }

            @Override
            public void onLongClick() {
                Log.e("showWeekView", "from setOnEventClickListener onLongClick");

            }
        });

        myCalendar.setOnWeekDayViewClickListener(new OnWeekDayViewClickListener() {
            @Override
            public void onClick(String date, String time) {
                Log.e("showWeekView", "from setOnWeekDayViewClickListener onClick");
                Log.e("tag", "date:-" + date + " time:-" + time);

            }

            @Override
            public void onLongClick(String date, String time) {
                Log.e("showWeekView", "from setOnWeekDayViewClickListener onLongClick");
                Log.e("tag", "date:-" + date + " time:-" + time);

            }
        });


    }

    private void showDayView() {

        myCalendar.showDayView();

        myCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onClick() {
                Log.e("showDayView", "from setOnEventClickListener onClick");

            }

            @Override
            public void onLongClick() {
                Log.e("showDayView", "from setOnEventClickListener onLongClick");

            }
        });

        myCalendar.setOnWeekDayViewClickListener(new OnWeekDayViewClickListener() {
            @Override
            public void onClick(String date, String time) {
                Log.e("showDayView", "from setOnWeekDayViewClickListener onClick");
                Log.e("tag", "date:-" + date + " time:-" + time);
            }

            @Override
            public void onLongClick(String date, String time) {
                Log.e("showDayView", "from setOnWeekDayViewClickListener onLongClick");
                Log.e("tag", "date:-" + date + " time:-" + time);
            }
        });

    }

    private void showAgendaView() {

        myCalendar.showAgendaView();

        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date, int position) {
                Log.e("date", String.valueOf(date));
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    public static void getCalanderData() {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_Select_All, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Calender_Event_Array -", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("ErrorMessage")) {
                        pd.dismiss();
                        Toast.makeText(activity2, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                    if (object.has("Calender_Event_Array")) {
                        JSONArray jsonArray = object.getJSONArray("Calender_Event_Array");
                        if (jsonArray.length() != 0) {

                            for (int j = 0; j < jsonArray.length(); j++) {
                                    /*if (jsonArray.optJSONObject(j).getString("Status").equalsIgnoreCase("Fail")) {
                                        Toast.makeText(getApplicationContext(), "" + jsonArray.optJSONObject(j).getString("Status"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    } else {*/
                                beanCalenderEventArray.clear();
                                beanCalenderEventArray.addAll((Collection<? extends BeanCalenderEventArray>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanCalenderEventArray>>() {
                                }.getType()));

                                AppConstants.eventList.clear();

                               int fl=0;

                                for (int i = 0; i < beanCalenderEventArray.size(); i++) {


                                    if (!beanCalenderEventArray.isEmpty()) {

                                        for(int k=0;k<multipleImageCode.size();k++){
                                            if(beanCalenderEventArray.get(i).bgColor.equalsIgnoreCase(multipleImageCode.get(k).getId())){
                                                fl = multipleImageCode.get(k).getPosition();
                                                Log.e("imageId",String.valueOf(fl));
                                            }
                                        }

                                        String duedate = dateFormater(beanCalenderEventArray.get(i).dueDate);

                                        myCalendar.addEvent(duedate + "", "8:00", "8:15", "" + beanCalenderEventArray.get(i).nameOfToDo,fl, -1, "" + beanCalenderEventArray.get(i).personId, "" + beanCalenderEventArray.get(i).peopleId, "" + beanCalenderEventArray.get(i).id, "" + beanCalenderEventArray.get(i).eventType, beanCalenderEventArray.get(i).fontColor, "" + beanCalenderEventArray.get(i).bgColor);

                                    } else {
                                        Toast.makeText(activity2, "No Data", Toast.LENGTH_LONG).show();
                                    }
                                }

                                myCalendar.getEventList(new GetEventListListener() {
                                    @Override
                                    public void eventList(ArrayList<EventModel> eventList) {

                                        Log.e("tag", "eventList.size():-" + eventList.size());
                                        for (int i = 0; i < eventList.size(); i++) {
                                            Log.e("tag", "eventList.getStrName:-" + eventList.get(i).getStrName());

                                        }
                                    }
                                });
                                //}
                            }

                            myCalendar.showMonthView();
                            pd.dismiss();

                        } else {
                            myCalendar.showMonthView();
                            pd.dismiss();
                        }
                    }


                    if (object.has("Calender_Event_People")) {
                        JSONArray jsonArray = object.getJSONArray("Calender_Event_People");

                        if (jsonArray.length() != 0) {

                            for (int j = 0; j < jsonArray.length(); j++) {
                                beanCalendarPeopleShares.clear();
                                beanCalendarPeopleShares.addAll((Collection<? extends BeanCalendarPeopleShare>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanCalendarPeopleShare>>() {
                                }.getType()));

                                AdapterCalendarPeopleList adapter = new AdapterCalendarPeopleList(activity2, beanCalendarPeopleShares);
                                rv_shared_peoplelist.setAdapter(adapter);
                            }
                            pd.dismiss();
                        } else {
                            ln_shared_peoplelist.setVisibility(View.GONE);
                            pd.dismiss();
                        }
                    }

                    if (object.has("Calender_Event_Shared_With_People")) {
                        JSONArray jsonArray = object.getJSONArray("Calender_Event_Shared_With_People");
                        if (jsonArray.length() != 0) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                beanCalendarSharePeopleLists.clear();
                                beanCalendarSharePeopleLists.addAll((Collection<? extends BeanCalendarSharePeopleList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanCalendarSharePeopleList>>() {
                                }.getType()));

                                AdapterCalendarSharedPeopleList adapter = new AdapterCalendarSharedPeopleList(activity2, beanCalendarSharePeopleLists);
                                rv_shared_you_peoplelist.setAdapter(adapter);
                            }
                            pd.dismiss();
                        } else {
                            ln_shared_you_peoplelist.setVisibility(View.GONE);
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
                Log.e("error_list", error.toString());
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
    }

    public static String dateFormater(String date1) {
        String date = date1;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String newFormat = formatter.format(testDate);
        Log.e("date", "" + newFormat);
        return newFormat;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_calander_event:
                Intent intent = new Intent(activity, AddCalanderEventActivity.class);
                intent.putExtra("add_or_edit", "add");
                startActivity(intent);
                finish();
                break;

            case R.id.ll_share_calander:
                GetCalanderPeopleData();
                break;

            case R.id.iv_event_c1:
                EventBgColor = "#FE7642";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FE7642"));
                break;

            case R.id.iv_event_c2:
                EventBgColor = "#E32B23";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#E32B23"));
                break;

            case R.id.iv_event_c3:
                EventBgColor = "#DEADFA";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#DEADFA"));
                break;

            case R.id.iv_event_c4:
                EventBgColor = "#A0C0F8";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#A0C0F8"));
                break;

            case R.id.iv_event_c5:
                EventBgColor = "#5884E2";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#5884E2"));
                break;

            case R.id.iv_event_c6:
                EventBgColor = "#22D6E0";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#22D6E0"));
                break;

            case R.id.iv_event_c7:
                EventBgColor = "#6AE7C1";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#6AE7C1"));
                break;

            case R.id.iv_event_c8:
                EventBgColor = "#43B35A";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#43B35A"));
                break;

            case R.id.iv_event_c9:
                EventBgColor = "#FFD56F";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FFD56F"));
                break;

            case R.id.iv_event_c10:
                EventBgColor = "#FFB97A";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FFB97A"));
                break;

            case R.id.iv_event_c11:
                EventBgColor = "#FF8A80";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FF8A80"));
                break;

            case R.id.iv_event_c12:
                EventBgColor = "#E1E1E1";
                EventFontColor = "#000000";
                view_selected_color.setBackgroundColor(Color.parseColor("#E1E1E1"));
                break;
        }
    }

    private void GetCalanderPeopleData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_People, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("edit_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if (object.has("Calender_Event_People")) {
                            JSONArray jsonArray = object.getJSONArray("Calender_Event_People");
                            if (jsonArray.length() != 0) {
                                beanCalanderPeoples.clear();
                                beanCalanderPeoples.addAll((Collection<? extends BeanCalanderPeople>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanCalanderPeople>>() {
                                }.getType()));
                                ShareCalanderEventDialog();

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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }


    private static void openTagPopuppeople() {
        open_tag_dialog_people = new Dialog(activity2);
        open_tag_dialog_people.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_tag_dialog_people.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_tag_dialog_people.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_tag_dialog_people.setContentView(R.layout.tag_popup_item_layout);

        Window window = open_tag_dialog_people.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        EditText et_search = (EditText) open_tag_dialog_people.findViewById(R.id.et_search);
        final TextView iv_done = (TextView) open_tag_dialog_people.findViewById(R.id.iv_done);
        TextView header = (TextView) open_tag_dialog_people.findViewById(R.id.header);
        header.setText("Select People");
        header.setTypeface(Utility.getTypeFaceTab());
        LinearLayout lnmainback = (LinearLayout) open_tag_dialog_people.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_tag_dialog_people.dismiss();
            }
        });

        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_list3.size() > 0) {
                    //tv_select_person,rv_select_person
                    //   tv_select_person.setVisibility(View.GONE);
                    //  rv_select_person.setVisibility(View.VISIBLE);
                } else {
                    //  tv_select_person.setVisibility(View.VISIBLE);
                    //   rv_select_person.setVisibility(View.GONE);
                }
                callOnBackPress3();
                open_tag_dialog_people.dismiss();
            }
        });

        recyclerview5 = (RecyclerView) open_tag_dialog_people.findViewById(R.id.recyclerview1);
        recyclerview6 = (RecyclerView) open_tag_dialog_people.findViewById(R.id.recyclerview2);

        recylerViewLayoutManager3 = new LinearLayoutManager(activity2);
        recyclerview5.setLayoutManager(recylerViewLayoutManager3);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerview6.setLayoutManager(flowLayoutManager);

        adapter1people = new Adapter1CalanderSharePeople(activity2, beanCalanderPeoples);
        recyclerview5.setAdapter(adapter1people);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter1people.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        open_tag_dialog_people.show();
    }

    public static void callOnBackPress3() {
        recyclerview_adapter3 = new Adapter2CalanderPeople(activity2, item_list3);
        rv_onchangelistner3();
    }

    public static void rv_onchangelistner3() {
        rv_select_person.setAdapter(recyclerview_adapter3);
        rv_select_person.addOnItemTouchListener(
                new RecyclerItemClickListener(activity2, rv_select_person, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        openTagPopuppeople();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
        recyclerview_adapter3.notifyDataSetChanged();
        recyclerview6.setAdapter(recyclerview_adapter3);
        recyclerview_adapter3.notifyDataSetChanged();
    }

    private void ShareCalanderEventDialog() {
        Dialog_share = new Dialog(activity);
        Dialog_share.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog_share.getWindow().setWindowAnimations(R.style.DialogAnimation);
        Dialog_share.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Dialog_share.setContentView(R.layout.popup_share_calander);
        Window window = Dialog_share.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnmainback = (LinearLayout) Dialog_share.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_share.dismiss();
            }
        });

        LinearLayout ll_share_now = (LinearLayout) Dialog_share.findViewById(R.id.ll_share_now);
        LinearLayout ll_cancel = (LinearLayout) Dialog_share.findViewById(R.id.ll_cancel);
        LinearLayout ll_select_person = (LinearLayout) Dialog_share.findViewById(R.id.ll_select_person);
        view_selected_color = (View) Dialog_share.findViewById(R.id.view_selected_color);

        rv_select_person = (RecyclerView) Dialog_share.findViewById(R.id.rv_select_person);
        FlowLayoutManager flowLayoutManager3 = new FlowLayoutManager();
        flowLayoutManager3.setAutoMeasureEnabled(true);
        rv_select_person.setLayoutManager(flowLayoutManager3);
        //tv_select_person , tv_visible_person,tv6

        TextView tv_reg_heading = (TextView) Dialog_share.findViewById(R.id.tv_reg_heading);
        TextView tv_share = (TextView) Dialog_share.findViewById(R.id.tv_share);
        TextView tv_cancel = (TextView) Dialog_share.findViewById(R.id.tv_cancel);
        TextView tv_select_person = (TextView) Dialog_share.findViewById(R.id.tv_select_person);
        TextView tv_visible_person = (TextView) Dialog_share.findViewById(R.id.tv_visible_person);
        TextView tv6 = (TextView) Dialog_share.findViewById(R.id.tv6);

        tv_reg_heading.setTypeface(Utility.getTypeFaceTab());
        tv_share.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        tv_select_person.setTypeface(Utility.getTypeFace());
        tv_visible_person.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());

        iv_event_c1 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c1);
        iv_event_c2 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c2);
        iv_event_c3 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c3);
        iv_event_c4 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c4);
        iv_event_c5 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c5);
        iv_event_c6 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c6);
        iv_event_c7 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c7);
        iv_event_c8 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c8);
        iv_event_c9 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c9);
        iv_event_c10 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c10);
        iv_event_c11 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c11);
        iv_event_c12 = (ImageView) Dialog_share.findViewById(R.id.iv_event_c12);


        iv_event_c1.setOnClickListener(this);
        iv_event_c2.setOnClickListener(this);
        iv_event_c3.setOnClickListener(this);
        iv_event_c4.setOnClickListener(this);
        iv_event_c5.setOnClickListener(this);
        iv_event_c6.setOnClickListener(this);
        iv_event_c7.setOnClickListener(this);
        iv_event_c8.setOnClickListener(this);
        iv_event_c9.setOnClickListener(this);
        iv_event_c10.setOnClickListener(this);
        iv_event_c11.setOnClickListener(this);
        iv_event_c12.setOnClickListener(this);
        ll_select_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagPopuppeople();
                //   GetCalanderPeopleData();
            }
        });

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_share.dismiss();
            }
        });
        ll_share_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String people_list_id = "";
                String pid1 = "";

                String people_list_name = "";
                String p_name1 = "";
                for (int i = 0; i < item_list3.size(); i++) {
                    pid1 += item_list3.get(i).getId() + ",";
                }
                for (int i = 0; i < item_list3.size(); i++) {
                    p_name1 += item_list3.get(i).getName() + ",";
                }
                if (p_name1.length() > 0) {
                    people_list_name = p_name1.substring(0, p_name1.length() - 1);
                }
                if (pid1.length() > 0) {
                    people_list_id = pid1.substring(0, pid1.length() - 1);
                }
                ShareMyCalander(people_list_id, people_list_name);
            }
        });
        Dialog_share.show();
    }

    private void ShareMyCalander(final String people_list_id, final String people_list_name) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_Share, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Calender_Event_Share")) {
                            JSONArray array = object.getJSONArray("Calender_Event_Share");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        Dialog_share.dismiss();
                                        pd.dismiss();
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
                    map.put("PersonsIds", people_list_id);
                    map.put("PersondsNames", people_list_name);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("BgColor", EventBgColor);
                    map.put("FontColor", EventFontColor);
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

    private void CalanderEventListDialog() {
        dialog_event_list = new Dialog(activity);
        dialog_event_list.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_event_list.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog_event_list.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_event_list.setContentView(R.layout.popup_calander_event_list);
        Window window = dialog_event_list.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnmainback = (LinearLayout) dialog_event_list.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_event_list.dismiss();
            }
        });

        rv_list = (RecyclerView) dialog_event_list.findViewById(R.id.rv_list);
        rv_list.setAdapter(null);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_list.setLayoutManager(mLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.setNestedScrollingEnabled(false);
        AdapterCalenderList adapterCalenderList = new AdapterCalenderList(activity, list);
        rv_list.setAdapter(adapterCalenderList);
        TextView tv_reg_heading = (TextView) dialog_event_list.findViewById(R.id.tv_reg_heading);
        tv_reg_heading.setTypeface(Utility.getTypeFaceTab());
        dialog_event_list.show();
    }
}
