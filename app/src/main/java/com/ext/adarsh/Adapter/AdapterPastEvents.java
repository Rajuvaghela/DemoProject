package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.bumptech.glide.Glide;
import com.ext.adarsh.Bean.BeanEventDetail;
import com.ext.adarsh.Bean.BeanEventPeople;
import com.ext.adarsh.Bean.BeanEvents;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterPastEvents extends RecyclerView.Adapter<AdapterPastEvents.MyViewHolder> {

    Dialog dd;
    Activity activity;
    ArrayList<BeanEvents> beanEventsArrayList = new ArrayList<>();
    ProgressDialog pd;
    ArrayList<BeanEventDetail> beanEventDetails = new ArrayList<>();
    ArrayList<BeanEventPeople> beanEventPeoples = new ArrayList<>();
    ArrayList<BeanEventPeople> beanEventPeoplesGoing = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_month)
        TextView txt_month;

        @BindView(R.id.txt_date)
        TextView txt_date;

        @BindView(R.id.txt_eventInfo)
        TextView txt_eventInfo;

        @BindView(R.id.txt_eventplace)
        TextView txt_eventplace;

        @BindView(R.id.txt_eventTitle)
        TextView txt_eventTitle;


        @BindView(R.id.txt_private)
        TextView txt_private;

        @BindView(R.id.card_view)
        LinearLayout ln_card;

        @BindView(R.id.imageViewEvent)
        ImageView imageViewEvent;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AdapterPastEvents(Activity activity, ArrayList<BeanEvents> beanEventsArrayList) {
        this.activity = activity;
        this.beanEventsArrayList = beanEventsArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_list_past, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
        holder.txt_month.setTypeface(Utility.getTypeFace());

        holder.txt_eventInfo.setTypeface(Utility.getTypeFace());
        holder.txt_eventplace.setTypeface(Utility.getTypeFace());
        holder.txt_eventTitle.setTypeface(Utility.getTypeFace());
        holder.txt_private.setTypeface(Utility.getTypeFace());

        holder.txt_date.setTypeface(Utility.getTypeFaceTab());

        holder.txt_month.setText(beanEventsArrayList.get(position).month);
        holder.txt_date.setText(beanEventsArrayList.get(position).day);
        holder.txt_eventTitle.setText(beanEventsArrayList.get(position).eventTitle);
        holder.txt_eventplace.setText(beanEventsArrayList.get(position).address);
        holder.txt_eventInfo.setText(beanEventsArrayList.get(position).dayName + " at " + beanEventsArrayList.get(position).time);

        Glide.with(activity).load(beanEventsArrayList.get(position).eventImage).into(holder.imageViewEvent);

        if (beanEventsArrayList.get(position).eventType.equalsIgnoreCase("G")) {
            holder.txt_private.setText("Public");
        } else {
            holder.txt_private.setText("Private");
        }
        holder.ln_card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getEventDetail(beanEventsArrayList.get(position).eventId, position, beanEventsArrayList.get(position).eventType);
                // getEventDetail(beanEventsArrayList.get(position).eventId, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanEventsArrayList.size();
    }

    public void getEventDetail(final String eid, final int position, final String event_type) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Event_Detail_By_Event_Id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Event_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Event_Detail_Array");
                            if (jsonArray.length() != 0) {
                                beanEventDetails.clear();
                                beanEventDetails.addAll((Collection<? extends BeanEventDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEventDetail>>() {
                                }.getType()));
                            }
                        }

                        if (object.has("Event_People_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Event_People_Array");
                            if (jsonArray.length() != 0) {
                                beanEventPeoples.clear();
                                beanEventPeoples.addAll((Collection<? extends BeanEventPeople>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEventPeople>>() {
                                }.getType()));
                            }
                        }

                        if (object.has("Event_People_Going_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Event_People_Going_Array");
                            if (jsonArray.length() != 0) {
                                beanEventPeoplesGoing.clear();
                                beanEventPeoplesGoing.addAll((Collection<? extends BeanEventPeople>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEventPeople>>() {
                                }.getType()));
                            }
                        }

                        pd.dismiss();
                        showAlertDialog3(position, event_type);

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
                    map.put("EventId", eid);
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

    private void showAlertDialog3(int position, final String event_type) {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.activity_event_detail);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnsharingback = (LinearLayout) dd.findViewById(R.id.lnbacksharing);

        TextView d_title = (TextView) dd.findViewById(R.id.d_title);
        TextView d_location = (TextView) dd.findViewById(R.id.d_location);
        TextView d_place = (TextView) dd.findViewById(R.id.d_place);
        TextView d_time = (TextView) dd.findViewById(R.id.d_time);
        TextView d_contact = (TextView) dd.findViewById(R.id.d_contact);
        TextView d_name = (TextView) dd.findViewById(R.id.d_name);
        TextView d_department = (TextView) dd.findViewById(R.id.d_department);
        TextView d_number = (TextView) dd.findViewById(R.id.d_number);
        TextView d_email = (TextView) dd.findViewById(R.id.d_email);
        TextView d_desc = (TextView) dd.findViewById(R.id.d_desc);
        TextView d_attending_people = (TextView) dd.findViewById(R.id.d_attending_people);

        TextView tv_month = (TextView) dd.findViewById(R.id.tv1);
        TextView tv_day = (TextView) dd.findViewById(R.id.tv2);
        TextView tv_status = (TextView) dd.findViewById(R.id.txt222);

        ImageView d_image = (ImageView) dd.findViewById(R.id.d_image);
        ImageView image = (ImageView) dd.findViewById(R.id.image);
        LinearLayout ln_going_add = (LinearLayout) dd.findViewById(R.id.ln_going_add);

        GoogleMap googleMap;

        MapView mMapView;
        MapsInitializer.initialize(activity);

        mMapView = (MapView) dd.findViewById(R.id.map_view);
        mMapView.onCreate(dd.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately
        Double lat = 0.0;
        Double log = 0.0;
        if (!(beanEventDetails.get(0).latitude == null || beanEventDetails.get(0).latitude.equalsIgnoreCase(""))) {
            lat = Double.parseDouble(beanEventDetails.get(0).latitude);
            log = Double.parseDouble(beanEventDetails.get(0).longitude);
        } else {
            mMapView.setVisibility(View.GONE);
        }

        final LatLng lng = new LatLng(lat, log);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //add marker here
                googleMap.addMarker(new MarkerOptions()
                        .position(lng)
                        .title("Marker Title"));

                googleMap.setOnMarkerClickListener(
                        new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker arg0) {
                                // Marker source is clicked
                                return true;
                            }

                        });
            }
        });


        //  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 15));

        for (int i = 0; i < beanEventPeoples.size(); i++) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.daynamiclinear_event, null);

            TextView txt_attchment_name = (TextView) addView.findViewById(R.id.name);
            TextView txt_going_no = (TextView) addView.findViewById(R.id.going_no);
            View view = (View) addView.findViewById(R.id.view);
            txt_attchment_name.setText(beanEventPeoples.get(i).invitedPersonsName);

            if (beanEventPeoples.get(i).goingFlag.equalsIgnoreCase("Y")) {
                txt_going_no.setText("GOING");
                txt_going_no.setBackgroundColor(activity.getResources().getColor(R.color.green2));
            } else {
                txt_going_no.setText("NOT GOING");
                txt_going_no.setBackgroundColor(activity.getResources().getColor(R.color.appcolor));
            }

            txt_attchment_name.setTypeface(Utility.getTypeFace());
            ln_going_add.addView(addView);

            if (i == beanEventPeoples.size() - 1) {
                view.setVisibility(View.GONE);
            }
        }

        String date = beanEventDetails.get(0).startDate;
        String month = null;
        String day = null;
        String full_day = null;
        try {
            month = monthExtracter(date);
            day = dayExtracter(date);
            full_day = dayExtract(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("DATE", month);
        Log.e("DATE", day);
        Log.e("DATE", full_day);

        Glide.with(activity).load(beanEventDetails.get(0).eventImage).into(d_image);
        Glide.with(activity).load(beanEventDetails.get(0).profileImage).into(image);

        d_title.setText(beanEventDetails.get(0).eventTitle);
        d_place.setText(beanEventDetails.get(0).address);
        d_time.setText(full_day + " at " + beanEventDetails.get(0).startTime);
        d_name.setText(beanEventDetails.get(0).contactPersonName);
        d_department.setText(beanEventDetails.get(0).designationName);
        d_number.setText(beanEventDetails.get(0).mobileNo);
        d_email.setText(beanEventDetails.get(0).emailAddress);
        d_desc.setText(beanEventDetails.get(0).description);
        d_attending_people.setText(String.valueOf(beanEventPeoplesGoing.size()) + " People  attending  the event");

        tv_month.setText(month);
        tv_day.setText(day);

        if (event_type.equalsIgnoreCase("G")) {
            tv_status.setText("Public");
        } else {
            tv_status.setText("Private");
        }

        d_title.setTypeface(Utility.getTypeFace());
        d_location.setTypeface(Utility.getTypeFace());
        d_place.setTypeface(Utility.getTypeFace());
        d_time.setTypeface(Utility.getTypeFace());
        d_contact.setTypeface(Utility.getTypeFace());
        d_name.setTypeface(Utility.getTypeFace());
        d_department.setTypeface(Utility.getTypeFace());
        d_number.setTypeface(Utility.getTypeFace());
        d_email.setTypeface(Utility.getTypeFace());
        d_desc.setTypeface(Utility.getTypeFace());
        tv_month.setTypeface(Utility.getTypeFace());
        tv_day.setTypeface(Utility.getTypeFace());
        tv_status.setTypeface(Utility.getTypeFace());

        lnsharingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dd.dismiss();
            }
        });
        dd.show();
    }

    public String monthExtracter(String s) throws ParseException {

        java.util.Date date;

        DateFormat inputFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        date = inputFormatter.parse(s);
        DateFormat outputFormatter = new SimpleDateFormat("MMM");
        String month = outputFormatter.format(date);

        return month;
    }

    public String dayExtracter(String s) throws ParseException {

        java.util.Date date;

        DateFormat inputFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        date = inputFormatter.parse(s);
        DateFormat outputFormatter = new SimpleDateFormat("dd");
        String day = outputFormatter.format(date);

        return day;
    }

    public String dayExtract(String s) throws ParseException {

        java.util.Date date;

        DateFormat inputFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        date = inputFormatter.parse(s);
        DateFormat outputFormatter = new SimpleDateFormat("EEEE");
        String day = outputFormatter.format(date);

        return day;
    }

/*    private void showAlertDialog3(int position) {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.activity_event_detail);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnsharingback = (LinearLayout) dd.findViewById(R.id.lnbacksharing);

        TextView d_title = (TextView) dd.findViewById(R.id.d_title);
        TextView d_location = (TextView) dd.findViewById(R.id.d_location);
        TextView d_place = (TextView) dd.findViewById(R.id.d_place);
        TextView d_time = (TextView) dd.findViewById(R.id.d_time);
        TextView d_contact = (TextView) dd.findViewById(R.id.d_contact);
        TextView d_name = (TextView) dd.findViewById(R.id.d_name);
        TextView d_department = (TextView) dd.findViewById(R.id.d_department);
        TextView d_number = (TextView) dd.findViewById(R.id.d_number);
        TextView d_email = (TextView) dd.findViewById(R.id.d_email);
        TextView d_desc = (TextView) dd.findViewById(R.id.d_desc);
        TextView d_attending_people = (TextView) dd.findViewById(R.id.d_attending_people);

        ImageView d_image = (ImageView) dd.findViewById(R.id.d_image);
        ImageView image = (ImageView) dd.findViewById(R.id.image);
        LinearLayout ln_going_add = (LinearLayout) dd.findViewById(R.id.ln_going_add);

        GoogleMap googleMap;


        MapView mMapView ;
        MapsInitializer.initialize(activity);

        mMapView = (MapView) dd.findViewById(R.id.map_view);
        mMapView.onCreate(dd.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately

        Double lat = 0.0;
        Double log = 0.0;
        if (!(beanEventDetails.get(0).latitude == null || beanEventDetails.get(0).latitude.equalsIgnoreCase(""))){
            lat = Double.parseDouble(beanEventDetails.get(0).latitude);
            log = Double.parseDouble(beanEventDetails.get(0).longitude);
        }else {
            mMapView.setVisibility(View.GONE);
        }

        final LatLng lng = new LatLng(lat,log);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //add marker here
                googleMap.addMarker(new MarkerOptions()
                        .position(lng)
                        .title("Marker Title"));

                googleMap.setOnMarkerClickListener(
                        new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker arg0) {
                                // Marker source is clicked
                                return true;
                            }

                        });
            }
        });

        for (int i = 0; i < beanEventPeoples.size(); i++) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.daynamiclinear_event, null);

            TextView txt_attchment_name = (TextView) addView.findViewById(R.id.name);
            TextView txt_going_no = (TextView) addView.findViewById(R.id.going_no);
            View view = (View) addView.findViewById(R.id.view);
            txt_attchment_name.setText(beanEventPeoples.get(i).invitedPersonsName);

            if (beanEventPeoples.get(i).goingFlag.equalsIgnoreCase("Y")){
                txt_going_no.setText("GOING");
                txt_going_no.setBackgroundColor(activity.getResources().getColor(R.color.green2));
            }else {
                txt_going_no.setText("NOT GOING");
                txt_going_no.setBackgroundColor(activity.getResources().getColor(R.color.appcolor));
            }

            txt_attchment_name.setTypeface(Utility.getTypeFace());
            ln_going_add.addView(addView);

            if (i == beanEventPeoples.size()-1){
                view.setVisibility(View.GONE);
            }

        }



        d_title.setTypeface(Utility.getTypeFace());
        d_location.setTypeface(Utility.getTypeFace());
        d_place.setTypeface(Utility.getTypeFace());
        d_time.setTypeface(Utility.getTypeFace());
        d_contact.setTypeface(Utility.getTypeFace());
        d_name.setTypeface(Utility.getTypeFace());
        d_department.setTypeface(Utility.getTypeFace());
        d_number.setTypeface(Utility.getTypeFace());
        d_email.setTypeface(Utility.getTypeFace());
        d_desc.setTypeface(Utility.getTypeFace());


        lnsharingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dd.dismiss();
            }
        });
        dd.show();
    }

    public void getEventDetail(final String eid, final int position) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Event_Detail_By_Event_Id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Event_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Event_Detail_Array");
                            if (jsonArray.length() != 0) {
                                beanEventDetails.clear();
                                beanEventDetails.addAll((Collection<? extends BeanEventDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEventDetail>>() {
                                }.getType()));
                            }
                        }
                        if (object.has("Event_People_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Event_People_Array");
                            if (jsonArray.length() != 0) {
                                beanEventPeoples.clear();
                                beanEventPeoples.addAll((Collection<? extends BeanEventPeople>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEventPeople>>() {
                                }.getType()));
                            }
                        }
                        if (object.has("Event_People_Going_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Event_People_Going_Array");
                            if (jsonArray.length() != 0) {
                                beanEventPeoplesGoing.clear();
                                beanEventPeoplesGoing.addAll((Collection<? extends BeanEventPeople>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanEventPeople>>() {
                                }.getType()));
                            }
                        }
                        pd.dismiss();
                        showAlertDialog3(position);
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
                    map.put("EventId", eid);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }*/
}
