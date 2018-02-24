package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.ext.adarsh.Activity.AddCalanderEventActivity;
import com.ext.adarsh.Activity.CalendarActivity;
import com.ext.adarsh.Activity.TaskNewActivity;
import com.ext.adarsh.Bean.BeanCalendarEventDeatilPopup;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;


/**
 * Created by ExT-Emp-008 on 29-12-2017.
 */


public class AdapterCalenderList extends RecyclerView.Adapter<AdapterCalenderList.MyViewHolder> {

    private LayoutInflater inflater;
    Activity activity;
    ArrayList<ModelCalendar> list;
    ArrayList<BeanCalendarEventDeatilPopup> beanCalendarEventDeatilPopups = new ArrayList<>();
    Dialog eventdetail;
    ProgressDialog pd;

    TextView tv_event_name, tv_event_start_date, tv_event_end_date, tv_event_start_time, tv_event_end_time, tv_event_with,
            tv_event_location, tv_edit_event,tv_remove_event;
    LinearLayout drawericon;

    public AdapterCalenderList(Activity activity, ArrayList<ModelCalendar> list) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.calander_list_adapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
        holder.textView.setText(" " + list.get(position).getNameOfToDo());
        holder.textView.setTypeface(Utility.getTypeFace());


        holder.lnback.setBackgroundColor(Color.parseColor(list.get(position).getBgcolor()));
        holder.textView.setTextColor(Color.parseColor(list.get(position).getFontcolor()));

        holder.lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (list.get(position).getEventType().equalsIgnoreCase("T")) {
                    activity.startActivity(new Intent(activity, TaskNewActivity.class));
                    activity.finish();
                } else {
                    getCalanderData(list.get(position).getId(), list.get(position).getPeopleId());
                }
            }
        });
        //  CalendarActivity.getDetailData(context,list.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout lnback;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            lnback = (LinearLayout) itemView.findViewById(R.id.lnback);
        }
    }


    public void addeventDetail(final String personid, final String id) {
        eventdetail = new Dialog(activity);
        eventdetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        eventdetail.getWindow().setWindowAnimations(R.style.DialogAnimation);
        eventdetail.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        eventdetail.setContentView(R.layout.popup_event_detail);

        Window window = eventdetail.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        tv_event_name = (TextView) eventdetail.findViewById(R.id.tv_event_name);
        tv_event_start_date = (TextView) eventdetail.findViewById(R.id.tv_event_start_date);
        tv_event_end_date = (TextView) eventdetail.findViewById(R.id.tv_event_end_date);
        tv_event_start_time = (TextView) eventdetail.findViewById(R.id.tv_event_start_time);
        tv_event_end_time = (TextView) eventdetail.findViewById(R.id.tv_event_end_time);
        tv_event_with = (TextView) eventdetail.findViewById(R.id.tv_event_with);
        tv_event_location = (TextView) eventdetail.findViewById(R.id.tv_event_location);
        tv_edit_event = (TextView) eventdetail.findViewById(R.id.tv_edit_event);
        tv_remove_event = (TextView) eventdetail.findViewById(R.id.tv_remove_event);


        tv_event_name.setText(beanCalendarEventDeatilPopups.get(0).eventTitle);
        try {
            tv_event_start_date.setText(dateFormatter(beanCalendarEventDeatilPopups.get(0).startDate));
            tv_event_end_date.setText(dateFormatter(beanCalendarEventDeatilPopups.get(0).endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_event_start_time.setText(beanCalendarEventDeatilPopups.get(0).startTime);
        tv_event_end_time.setText(beanCalendarEventDeatilPopups.get(0).endTime);
        tv_event_with.setText(beanCalendarEventDeatilPopups.get(0).personsName);
        tv_event_location.setText(beanCalendarEventDeatilPopups.get(0).address);
        drawericon = (LinearLayout) eventdetail.findViewById(R.id.drawericon);

        LinearLayout lnedit = (LinearLayout) eventdetail.findViewById(R.id.lnedit);
        LinearLayout ll_save_contact_info = (LinearLayout) eventdetail.findViewById(R.id.ll_save_contact_info);
        LinearLayout ll_remove_contact_info = (LinearLayout) eventdetail.findViewById(R.id.ll_remove_contact_info);

        if (personid.equalsIgnoreCase(Utility.getPeopleIdPreference())) {
            lnedit.setVisibility(View.VISIBLE);
        } else {
            lnedit.setVisibility(View.GONE);
        }

        tv_event_name.setTypeface(Utility.getTypeFace());
        tv_event_start_date.setTypeface(Utility.getTypeFace());
        tv_event_end_date.setTypeface(Utility.getTypeFace());
        tv_event_start_time.setTypeface(Utility.getTypeFace());
        tv_event_end_time.setTypeface(Utility.getTypeFace());
        tv_event_with.setTypeface(Utility.getTypeFace());
        tv_event_location.setTypeface(Utility.getTypeFace());
        tv_edit_event.setTypeface(Utility.getTypeFaceTab());
        tv_remove_event.setTypeface(Utility.getTypeFaceTab());

        TextView tv1 = (TextView) eventdetail.findViewById(R.id.tv1);
        TextView tv2 = (TextView) eventdetail.findViewById(R.id.tv2);
        TextView tv3 = (TextView) eventdetail.findViewById(R.id.tv3);
        TextView tv4 = (TextView) eventdetail.findViewById(R.id.tv4);
        TextView tv5 = (TextView) eventdetail.findViewById(R.id.tv5);
        TextView tv6 = (TextView) eventdetail.findViewById(R.id.tv6);
        TextView tv7 = (TextView) eventdetail.findViewById(R.id.tv7);


        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());


        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventdetail.dismiss();
            }
        });

        ll_save_contact_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity, AddCalanderEventActivity.class);
                in.putExtra("add_or_edit", "edit");
                in.putExtra("id", id);
                activity.startActivity(in);
                activity.finish();
            }
        });

        ll_remove_contact_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialoge(id);

            }
        });

        eventdetail.show();

    }

    private void getCalanderData(final String id, final String personid) {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_Detail_Select_By_CalenderId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Calender_Event_Array -", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("ErrorMessage")) {
                        pd.dismiss();
                        Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
                    }

                    if (object.has("Calender_Event_Detail_Array")) {
                        JSONArray jsonArray = object.getJSONArray("Calender_Event_Detail_Array");

                        if (jsonArray.length() != 0) {
                            beanCalendarEventDeatilPopups.clear();
                            beanCalendarEventDeatilPopups.addAll((Collection<? extends BeanCalendarEventDeatilPopup>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanCalendarEventDeatilPopup>>() {
                            }.getType()));
                            pd.dismiss();
                            addeventDetail(personid, id);
                        } else {
                            pd.dismiss();
                        }
                    }
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
                map.put("CalenderId", id);
                return map;
            }
        };
        Utility.SetvollyTime30Sec(request);
        Infranet.getInstance().getRequestQueue().add(request);
    }

    public static String dateFormatter(String s) throws ParseException {

        Date date;

        DateFormat inputFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        date = inputFormatter.parse(s);
        DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
        String str = outputFormatter.format(date);

        return str;

    }
    private void deleteDialoge(final String id) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Delete")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        deleteEvent(id);
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void deleteEvent(final String id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_Remove, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Calender_Event_Remove")) {
                            JSONArray array = object.getJSONArray("Calender_Event_Remove");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        eventdetail.dismiss();
                                        CalendarActivity.getCalanderData();
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
                    map.put("CalenderId", id);
                    map.put("LoginId", Utility.getPeopleIdPreference());
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


}