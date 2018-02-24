package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 11/7/2017.
 */


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ext.adarsh.Activity.other.OtherProfileActivity;
import com.ext.adarsh.Activity.EventAddActivity;
import com.ext.adarsh.Activity.ProfileActivity;
import com.ext.adarsh.Bean.BeamWorkEditList;
import com.ext.adarsh.Bean.BeanAboutUsWorkDetailList;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterAboutUsWorkList extends RecyclerView.Adapter<AdapterAboutUsWorkList.ViewHolder> {


    Activity activity;
    List<BeanAboutUsWorkDetailList> list = new ArrayList<>();
    Dialog open_dialog_edit_work;
    ProgressDialog pd;
    ArrayList<BeamWorkEditList> beamWorkEditList = new ArrayList<>();
    EditText et_company_name, et_possion, et_company_place;
    TextView et_start_date, et_end_date;

    public AdapterAboutUsWorkList(List<BeanAboutUsWorkDetailList> list, Activity activity) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_about_us_work_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
        final BeanAboutUsWorkDetailList pos = list.get(position);
        holder.tv_company_name.setTypeface(Utility.getTypeFace());
        holder.tv_possisition_and_date.setTypeface(Utility.getTypeFace());

        holder.tv_company_name.setText(list.get(position).companyName);
        final String str = list.get(position).position + " , " + list.get(position).startPresent + " to " + list.get(position).endPresent + " , " + list.get(position).companyPlace;
        holder.tv_possisition_and_date.setText(str);

        if (activity instanceof OtherProfileActivity) {
            holder.contactmenu.setVisibility(View.GONE);
        } else if (activity instanceof ProfileActivity) {
            holder.contactmenu.setVisibility(View.VISIBLE);
            holder.contactmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showpopupmenu(view, position, pos.companyName, pos.position, pos.startPresent, pos.endPresent,
                            pos.companyPlace, pos.workId);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_company_name, tv_possisition_and_date;
        LinearLayout contactmenu;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_company_name = (TextView) itemView.findViewById(R.id.tv_company_name);
            tv_possisition_and_date = (TextView) itemView.findViewById(R.id.tv_possisition_and_date);
            contactmenu = (LinearLayout) itemView.findViewById(R.id.contactmenu);
        }
    }

    public void showpopupmenu(View view, final int position, final String company_name, final String employee_position,
                              final String start_date, final String end_date, final String company_place, final String workId) {
        final PopupMenu popup = new PopupMenu(activity, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.menu_popup_aboutus_work_list, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.update_work:
                        getWorkEditData(workId);
                        break;
                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    void OpenEditWorkDialog(String company_name, String emp_position, String start_date, String end_date, String company_place,
                            final String workId) {
        open_dialog_edit_work = new Dialog(activity);
        open_dialog_edit_work.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_dialog_edit_work.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_dialog_edit_work.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_dialog_edit_work.setContentView(R.layout.popup_aboutus_edit_work);

        Window window = open_dialog_edit_work.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);



        TextView tv_company_name = (TextView) open_dialog_edit_work.findViewById(R.id.tv_company_name);
        TextView tv_possion = (TextView) open_dialog_edit_work.findViewById(R.id.tv_possion);
        TextView tv_start_date = (TextView) open_dialog_edit_work.findViewById(R.id.tv_start_date);
        TextView tv_end_date = (TextView) open_dialog_edit_work.findViewById(R.id.tv_end_date);
        TextView tv_company_place = (TextView) open_dialog_edit_work.findViewById(R.id.tv_company_place);
        RelativeLayout rl_date1 = (RelativeLayout) open_dialog_edit_work.findViewById(R.id.rl_date1);
        RelativeLayout rl_date2 = (RelativeLayout) open_dialog_edit_work.findViewById(R.id.rl_date2);


        TextView tv12 = (TextView) open_dialog_edit_work.findViewById(R.id.tv12);
        LinearLayout ll_add_header = (LinearLayout) open_dialog_edit_work.findViewById(R.id.ll_add_header);
        LinearLayout ll_edit_header = (LinearLayout) open_dialog_edit_work.findViewById(R.id.ll_edit_header);

        ll_add_header.setVisibility(View.GONE);
        ll_edit_header.setVisibility(View.VISIBLE);

        et_company_name = (EditText) open_dialog_edit_work.findViewById(R.id.et_company_name);
        et_possion = (EditText) open_dialog_edit_work.findViewById(R.id.et_possion);
        et_start_date = (TextView) open_dialog_edit_work.findViewById(R.id.et_start_date);
        et_end_date = (TextView) open_dialog_edit_work.findViewById(R.id.et_end_date);
        et_company_place = (EditText) open_dialog_edit_work.findViewById(R.id.et_company_place);

        et_company_name.addTextChangedListener(new MyTextWatcher(et_company_name));
        et_possion.addTextChangedListener(new MyTextWatcher(et_possion));
        et_company_place.addTextChangedListener(new MyTextWatcher(et_company_place));

        tv_company_name.setTypeface(Utility.getTypeFace());
        tv_possion.setTypeface(Utility.getTypeFace());
        tv_start_date.setTypeface(Utility.getTypeFace());
        tv_end_date.setTypeface(Utility.getTypeFace());
        tv_company_place.setTypeface(Utility.getTypeFace());

        //  tv_next1.setTypeface(Utility.getTypeFaceTab());
        tv12.setTypeface(Utility.getTypeFaceTab());

        et_company_name.setTypeface(Utility.getTypeFace());
        et_possion.setTypeface(Utility.getTypeFace());
        et_start_date.setTypeface(Utility.getTypeFace());
        et_end_date.setTypeface(Utility.getTypeFace());
        et_company_place.setTypeface(Utility.getTypeFace());

        et_company_name.setText("" + company_name);
        et_possion.setText("" + emp_position);
        et_start_date.setText("" + start_date);
        et_end_date.setText("" + end_date);
        et_company_place.setText("" + company_place);

        LinearLayout ll_save_work = (LinearLayout) open_dialog_edit_work.findViewById(R.id.ll_save_work);
        LinearLayout ll_add_work = (LinearLayout) open_dialog_edit_work.findViewById(R.id.ll_add_work);
        ll_add_work.setVisibility(View.GONE);
        LinearLayout lnmainback = (LinearLayout) open_dialog_edit_work.findViewById(R.id.lnmainback);
        final LinearLayout ll_work_cancel = (LinearLayout) open_dialog_edit_work.findViewById(R.id.ll_work_cancel);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_dialog_edit_work.dismiss();
            }
        });

        rl_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date();
            }
        });
        rl_date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_date();
            }
        });

        ll_save_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String company_name = et_company_name.getText().toString();
                String position = et_possion.getText().toString();
                String company_place = et_company_place.getText().toString();
                String start_date = et_start_date.getText().toString();
                String end_date = et_end_date.getText().toString();

                if (company_name.equalsIgnoreCase("")) {
                    et_company_name.setError("Enter Company Name");
                    requestFocus(et_company_name);
                } else if (position.equalsIgnoreCase("")) {
                    et_possion.setError("Enter Position");
                    requestFocus(et_possion);
                } else if (start_date.equalsIgnoreCase("")) {
                    //et_start_date.setError("Enter Start Date");
                    Toast.makeText(activity, "Enter Start Date", Toast.LENGTH_LONG).show();
                    //requestFocus(et_start_date);
                } else if (end_date.equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Enter End Date", Toast.LENGTH_LONG).show();
                   /* et_end_date.setError("Enter End Date");
                    requestFocus(et_end_date);*/
                } else if (company_place.equalsIgnoreCase("")) {
                    et_company_place.setError("Enter Company Place");
                    requestFocus(et_company_place);
                } else {
                    saveAboutUsWorkList(company_name, position, start_date, end_date, company_place, workId);
                }

               /* if (!et_company_name.getText().toString().isEmpty()
                        && !et_company_name.getText().toString().isEmpty()
                        && !et_company_name.getText().toString().isEmpty()
                        && !et_company_name.getText().toString().isEmpty()) {
                    saveAboutUsWorkList(et_company_name.getText().toString(),
                            et_possion.getText().toString(),
                            et_start_date.getText().toString(),
                            et_end_date.getText().toString(),
                            et_company_place.getText().toString(),workId);
                } else {
                    Toast.makeText(activity, "Enter Valid data", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        ll_work_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_dialog_edit_work.dismiss();
            }
        });

        open_dialog_edit_work.show();
    }

    void saveAboutUsWorkList(final String company_name, final String emp_possiotion,
                             final String start_date, final String end_date,
                             final String company_place, final String workId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Work_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Work_Update")) {
                            JSONArray array = object.getJSONArray("Work_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        ProfileActivity.getAboutUsDetail();
                                        open_dialog_edit_work.dismiss();

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
                    map.put("WorkId", workId);
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("CompanyName", company_name);
                    map.put("Position", emp_possiotion);
                    map.put("StartDate", start_date);
                    map.put("EndDate", end_date);
                    map.put("CompanyPlace", company_place);

                    Log.e("WorkId", workId);
                    Log.e("PeopleId", Utility.getPeopleIdPreference());
                    Log.e("CompanyName", company_name);
                    Log.e("Position", emp_possiotion);
                    Log.e("StartDate", start_date);
                    Log.e("EndDate", end_date);
                    Log.e("CompanyPlace", company_place);

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

    private void getWorkEditData(final String workId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Work_Edit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("edit_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("Work_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Work_Detail_Array");
                            if (jsonArray.length() != 0) {

                                beamWorkEditList.clear();
                                beamWorkEditList.addAll((Collection<? extends BeamWorkEditList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeamWorkEditList>>() {
                                }.getType()));

                                String company_name = beamWorkEditList.get(0).companyName;
                                String position = beamWorkEditList.get(0).position;
                                String company_place = beamWorkEditList.get(0).companyPlace;


                               /* String start_date = beamWorkEditList.get(0).joinDate;
                                String end_date = beamWorkEditList.get(0).leftDate;*/

                                String start_date = null, end_date = null;
                                try {
                                    start_date = EventAddActivity.dateFormatter(beamWorkEditList.get(0).joinDate);
                                    end_date = EventAddActivity.dateFormatter(beamWorkEditList.get(0).leftDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Log.e("company_name", company_name);
                                Log.e("position", position);
                                Log.e("company_place", company_place);
                                Log.e("start_date", start_date);
                                Log.e("end_date", end_date);

                                OpenEditWorkDialog(company_name, position, start_date, end_date, company_place, workId);

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
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("WorkId", workId);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.et_company_name:
                    validateCompanyName();
                    break;
                case R.id.et_possion:
                    validatePosition();
                    break;
                case R.id.et_company_place:
                    validateCompanyPlace();
                    break;
            }
        }
    }

    private boolean validateCompanyName() {
        String name = et_company_name.getText().toString().trim();
        if (name.isEmpty()) {
            et_company_name.setError("Please enter company name.");
            requestFocus(et_company_name);
            return false;
        } else {
            et_company_name.setError(null);
        }
        return true;
    }

    private boolean validatePosition() {
        String name = et_possion.getText().toString().trim();
        if (name.isEmpty()) {
            et_possion.setError("Please enter Position.");
            requestFocus(et_possion);
            return false;
        } else {
            et_possion.setError(null);
        }
        return true;
    }

    private boolean validateCompanyPlace() {
        String name = et_company_place.getText().toString().trim();
        if (name.isEmpty()) {
            et_company_place.setError("Please enter company place.");
            requestFocus(et_company_place);
            return false;
        } else {
            et_company_place.setError(null);
        }
        return true;
    }

    private void start_date() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et_start_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void end_date() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et_end_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}


