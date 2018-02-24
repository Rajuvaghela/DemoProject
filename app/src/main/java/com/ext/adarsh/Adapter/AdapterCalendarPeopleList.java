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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.desai.vatsal.mydynamiccalendar.AppConstants;
import com.desai.vatsal.mydynamiccalendar.EventModel;
import com.desai.vatsal.mydynamiccalendar.GetEventListListener;
import com.ext.adarsh.Activity.CalendarActivity;
import com.ext.adarsh.Activity.MainActivity;
import com.ext.adarsh.Bean.BeanCalendarPeopleShare;
import com.ext.adarsh.Bean.BeanCalenderEventArray;
import com.ext.adarsh.Bean.ModelClass2;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;


public class AdapterCalendarPeopleList extends RecyclerView.Adapter<AdapterCalendarPeopleList.ViewHolder>  {

    ArrayList<BeanCalendarPeopleShare> list;
    Activity activity;
    Dialog colorpickerdemo;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    ProgressDialog pd;
    ImageView iv_event_c1, iv_event_c2, iv_event_c3, iv_event_c4, iv_event_c5, iv_event_c6, iv_event_c7, iv_event_c8, iv_event_c9,
            iv_event_c10, iv_event_c11, iv_event_c12;
    View view_selected_color;
    String EventBgColor = "#FE7642", EventFontColor = "#FFFFFF";

    public AdapterCalendarPeopleList(Activity activity, ArrayList<BeanCalendarPeopleShare> list) {
        this.list = list;
        this.activity = activity;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        CheckBox chkSelected;
        LinearLayout ln_main,lnbackground;

        public ViewHolder(View v) {
            super(v);
            ln_main = (LinearLayout) v.findViewById(R.id.ln_main);
            lnbackground = (LinearLayout) v.findViewById(R.id.lnbackground);
            textView = (TextView) v.findViewById(R.id.subject_textview);
            chkSelected = (CheckBox) v.findViewById(R.id.chkSelected);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view1 = LayoutInflater.from(activity).inflate(R.layout.calendar_people_list, parent, false);
        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        pd = Utility.getDialog(activity);
        holder.textView.setText(list.get(position).fullName);
        holder.textView.setTypeface(Utility.getTypeFace());

        if (list.get(position).bgColor == null || list.get(position).bgColor.equalsIgnoreCase("")){
            Log.e("onBindViewHolder: ","+++++++++++++++++color code "+list.get(position).bgColor);
            holder.lnbackground.setBackgroundColor(Color.parseColor("#bd1820"));
        }else {
            holder.lnbackground.setBackgroundColor(Color.parseColor(list.get(position).bgColor));
        }

        holder.chkSelected.setTag(list.get(position));
        holder.chkSelected.setChecked(true);
        list.get(position).setSelected(true);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorpickerdemo(position);
            }
        });

        holder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (list.get(position).isSelected() == false) {
                    String checkedlist = "";
                    holder.chkSelected.setChecked(true);
                    list.get(position).setSelected(true);
                    Log.e("onCheckedChanged: ","+++++++++++++ checkedddd");
                    String finalcheckedlist = "";

                    for (int i=0; i<list.size(); i++){
                        if (list.get(i).isSelected()){
                            checkedlist += list.get(i).peopleId + ",";
                        }
                    }
                    if (checkedlist.equalsIgnoreCase("")){
                        finalcheckedlist = "0";
                    }else {
                        finalcheckedlist = checkedlist.substring(0, checkedlist.length() - 1);
                    }
                    getCalanderData(finalcheckedlist);
                } else {
                    String checkedlist = "";
                    holder.chkSelected.setChecked(false);
                    list.get(position).setSelected(false);
                    String finalcheckedlist = "";

                    for (int i=0; i<list.size(); i++){
                        if (list.get(i).isSelected()){
                            checkedlist += list.get(i).peopleId + ",";
                        }
                    }
                    if (checkedlist.equalsIgnoreCase("")){
                        finalcheckedlist = "0";
                    }else {
                        finalcheckedlist = checkedlist.substring(0, checkedlist.length() - 1);
                    }
                    getCalanderData(finalcheckedlist);
                }

            }
        });

    }

    private void colorpickerdemo(final int pos) {
        colorpickerdemo = new Dialog(activity);
        colorpickerdemo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        colorpickerdemo.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        colorpickerdemo.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        colorpickerdemo.setContentView(R.layout.calender_color_picker_dialog);

            Window window = colorpickerdemo.getWindow();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);


         iv_event_c1 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c1);
         iv_event_c2 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c2);
         iv_event_c3 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c3);
         iv_event_c4 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c4);
        iv_event_c5 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c5);
         iv_event_c6 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c6);
         iv_event_c7 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c7);
        iv_event_c8 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c8);
         iv_event_c9 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c9);
         iv_event_c10 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c10);
         iv_event_c11 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c11);
         iv_event_c12 = (ImageView) colorpickerdemo.findViewById(R.id.iv_event_c12);
        view_selected_color = (View) colorpickerdemo. findViewById(R.id.view_selected_color);

            TextView title = (TextView) colorpickerdemo.findViewById(R.id.title);
            Button btn_submit = (Button) colorpickerdemo.findViewById(R.id.btn_submit);
            ImageView iv_close = (ImageView) colorpickerdemo.findViewById(R.id.iv_close);


        iv_event_c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#FE7642";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FE7642"));
            }
        });
        iv_event_c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#E32B23";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#E32B23"));
            }
        });
        iv_event_c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#DEADFA";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#DEADFA"));
            }
        });
        iv_event_c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#A0C0F8";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#A0C0F8"));
            }
        });
        iv_event_c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#5884E2";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#5884E2"));
            }
        });
        iv_event_c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#22D6E0";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#22D6E0"));
            }
        });
        iv_event_c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#6AE7C1";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#6AE7C1"));
            }
        });
        iv_event_c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#43B35A";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#43B35A"));
            }
        });
        iv_event_c9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#FFD56F";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FFD56F"));
            }
        });
        iv_event_c10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#FFB97A";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FFB97A"));
            }
        });
        iv_event_c11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#FF8A80";
                EventFontColor = "#FFFFFF";
                view_selected_color.setBackgroundColor(Color.parseColor("#FF8A80"));
            }
        });
        iv_event_c12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBgColor = "#E1E1E1";
                EventFontColor = "#000000";
                view_selected_color.setBackgroundColor(Color.parseColor("#E1E1E1"));
            }
        });

        title.setTypeface(Utility.getTypeFace());
        btn_submit.setTypeface(Utility.getTypeFaceTab());


            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickcolor(EventBgColor,EventFontColor,pos);
                }
            });

            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorpickerdemo.dismiss();
                }
            });
        colorpickerdemo.show();
        }

    private void pickcolor(final String eventBgColor, final String eventFontColor, final int pos) {
        pd.show();
            if (checkConnectivity()) {
                StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Shared_With_People_Color_Update, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("res", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.has("Calender_Shared_With_People_Color_Update")) {
                                JSONArray array = object.getJSONArray("Calender_Shared_With_People_Color_Update");
                                if (array.length() != 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                            pd.dismiss();
                                            Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                            colorpickerdemo.dismiss();
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
                        map.put("SelectPeopleId",list.get(pos).peopleId);
                        map.put("LoginId",Utility.getPeopleIdPreference());
                        map.put("BgColor",eventBgColor);
                        map.put("FontColor",eventFontColor);
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



    @Override
    public int getItemCount() {
        return list.size();
    }

    private void getCalanderData(final String peopleId) {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Calender_Event_Select_People_Remove, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Responseee + ", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("ErrorMessage")) {
                        pd.dismiss();
                        Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
                    }

                    if (object.has("Calender_Event_Array")) {
                        JSONArray jsonArray = object.getJSONArray("Calender_Event_Array");
                        if (jsonArray.length() != 0) {
                            CalendarActivity.rv_list.setAdapter(null);
                            for (int j = 0; j < jsonArray.length(); j++) {
                                CalendarActivity.beanCalenderEventArray.clear();
                                CalendarActivity.beanCalenderEventArray.addAll((Collection<? extends BeanCalenderEventArray>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanCalenderEventArray>>() {
                                }.getType()));
                                AppConstants.eventList.clear();

                                for (int i = 0; i < CalendarActivity.beanCalenderEventArray.size(); i++) {

                                    if (!CalendarActivity.beanCalenderEventArray.isEmpty()) {
                                        String duedate = dateFormater(CalendarActivity.beanCalenderEventArray.get(i).dueDate);

                                        CalendarActivity.myCalendar.addEvent(duedate + "", "8:00", "8:15", "" + CalendarActivity.beanCalenderEventArray.get(i).nameOfToDo, R.drawable.event_view, -1, "" + CalendarActivity.beanCalenderEventArray.get(i).personId, "" + CalendarActivity.beanCalenderEventArray.get(i).peopleId, "" + CalendarActivity.beanCalenderEventArray.get(i).id, "" + CalendarActivity.beanCalenderEventArray.get(i).eventType, CalendarActivity.beanCalenderEventArray.get(i).fontColor, "" + CalendarActivity.beanCalenderEventArray.get(i).bgColor);

                                    } else {
                                        Toast.makeText(activity, "No Data", Toast.LENGTH_LONG).show();
                                    }


                                }

                                CalendarActivity.myCalendar.getEventList(new GetEventListListener() {
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

                            CalendarActivity.myCalendar.showMonthView();
                            pd.dismiss();


                        } else {
                            AppConstants.eventList.clear();
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
                map.put("SelectUserIdList",peopleId);
                return map;
            }
        };
        Utility.SetvollyTime30Sec(request);
        Infranet.getInstance().getRequestQueue().add(request);
    }

    private String dateFormater(String date1) {
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
}

