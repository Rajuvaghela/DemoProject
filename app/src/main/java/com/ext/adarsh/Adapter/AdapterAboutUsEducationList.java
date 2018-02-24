package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 11/7/2017.
 */


import android.app.Activity;
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
import com.ext.adarsh.Activity.other.OtherProfileActivity;
import com.ext.adarsh.Activity.ProfileActivity;
import com.ext.adarsh.Bean.BeanAboutUsEducationList;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterAboutUsEducationList extends RecyclerView.Adapter<AdapterAboutUsEducationList.ViewHolder> {


    Activity activity;
    List<BeanAboutUsEducationList> list = new ArrayList<>();
    Dialog open_dialog_edit_work;
    ProgressDialog pd;
    EditText et_institute_name,et_year,et_degree,et_place;


    public AdapterAboutUsEducationList(List<BeanAboutUsEducationList> list, Activity activity) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_about_us_education_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BeanAboutUsEducationList pos = list.get(position);
        pd = Utility.getDialog(activity);
        holder.tv_university_name.setTypeface(Utility.getTypeFace());
        holder.tv_education_deatil.setTypeface(Utility.getTypeFace());

        holder.tv_university_name.setText(list.get(position).institiuteName);
        String str = "Class of " + list.get(position).yaer + " , " + list.get(position).degree + " , " + list.get(position).institiutePlace;
        holder.tv_education_deatil.setText(str);

        if(activity instanceof OtherProfileActivity) {
            holder.contactmenu.setVisibility(View.GONE);
        } else if(activity instanceof ProfileActivity) {
            holder.contactmenu.setVisibility(View.VISIBLE);
        }

        holder.contactmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupmenu(view, position, pos.institiuteName, pos.yaer, pos.degree, pos.institiutePlace, pos.educationId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_university_name, tv_education_deatil;
        LinearLayout contactmenu;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_university_name = (TextView) itemView.findViewById(R.id.tv_university_name);
            tv_education_deatil = (TextView) itemView.findViewById(R.id.tv_education_deatil);
            contactmenu = (LinearLayout) itemView.findViewById(R.id.contactmenu);
        }

    }

    public void showpopupmenu(View view, final int position,
                              final String ins_name,
                              final String year,
                              final String degree,
                              final String place,
                              final String educationId) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater().inflate(R.menu.menu_popup_aboutus_work_list, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.update_work:
                        OpenEditWorkDialog(ins_name, year, degree, place, educationId);
                        break;
                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    void OpenEditWorkDialog(String ins_name, String year, String degree, String place, final String educationId) {

        open_dialog_edit_work = new Dialog(activity);
        open_dialog_edit_work.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_dialog_edit_work.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_dialog_edit_work.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_dialog_edit_work.setContentView(R.layout.popup_aboutus_edit_education);

        Window window = open_dialog_edit_work.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        LinearLayout lnmainback = (LinearLayout) open_dialog_edit_work.findViewById(R.id.lnmainback);
        LinearLayout ll_save_work = (LinearLayout) open_dialog_edit_work.findViewById(R.id.ll_save_work);
        LinearLayout ll_work_cancel = (LinearLayout) open_dialog_edit_work.findViewById(R.id.ll_work_cancel);
        LinearLayout ll_add_header = (LinearLayout) open_dialog_edit_work.findViewById(R.id.ll_add_header);
        LinearLayout ll_edit_header = (LinearLayout) open_dialog_edit_work.findViewById(R.id.ll_edit_header);

      /*  TextView tv_reg_heading = (TextView) open_dialog_edit_work.findViewById(R.id.tv_reg_heading);
        tv_reg_heading.setText("Add Educational Details");*/

        ll_add_header.setVisibility(View.VISIBLE);
        ll_edit_header.setVisibility(GONE);

        /*ll_add_header.setVisibility(View.GONE);
        ll_edit_header.setVisibility(View.VISIBLE);*/

        TextView tv_institute_name = (TextView) open_dialog_edit_work.findViewById(R.id.tv_institute_name);
        TextView tv_place = (TextView) open_dialog_edit_work.findViewById(R.id.tv_place);
        TextView tv_degree = (TextView) open_dialog_edit_work.findViewById(R.id.tv_degree);
        TextView tv_year = (TextView) open_dialog_edit_work.findViewById(R.id.tv_year);

        TextView tv_next1 = (TextView) open_dialog_edit_work.findViewById(R.id.tv_next1);
        TextView tv12 = (TextView) open_dialog_edit_work.findViewById(R.id.tv12);

        et_institute_name = (EditText) open_dialog_edit_work.findViewById(R.id.et_institute_name);
       et_year = (EditText) open_dialog_edit_work.findViewById(R.id.et_year);
       et_degree = (EditText) open_dialog_edit_work.findViewById(R.id.et_degree);
       et_place = (EditText) open_dialog_edit_work.findViewById(R.id.et_place);

        et_institute_name.addTextChangedListener(new MyTextWatcher(et_institute_name));
        et_year.addTextChangedListener(new MyTextWatcher(et_year));
        et_degree.addTextChangedListener(new MyTextWatcher(et_degree));
        et_place.addTextChangedListener(new MyTextWatcher(et_place));


        tv_next1.setTypeface(Utility.getTypeFaceTab());
        tv12.setTypeface(Utility.getTypeFaceTab());

        tv_institute_name.setTypeface(Utility.getTypeFace());
        tv_place.setTypeface(Utility.getTypeFace());
        tv_degree.setTypeface(Utility.getTypeFace());
        tv_year.setTypeface(Utility.getTypeFace());


        et_institute_name.setTypeface(Utility.getTypeFace());
        et_year.setTypeface(Utility.getTypeFace());
        et_degree.setTypeface(Utility.getTypeFace());
        et_place.setTypeface(Utility.getTypeFace());


        et_institute_name.setText(ins_name);
        et_year.setText(year);
        et_degree.setText(degree);
        et_place.setText(place);


        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_dialog_edit_work.dismiss();
            }
        });
        ll_save_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String institute_name = et_institute_name.getText().toString();
                String year = et_year.getText().toString();
                String degree = et_degree.getText().toString();
                String place = et_place.getText().toString();

                if(institute_name.equalsIgnoreCase("")){
                    et_institute_name.setError("Enter Institute Name");
                    requestFocus(et_institute_name);
                }else if(year.equalsIgnoreCase("")){
                    et_year.setError("Enter year");
                    requestFocus(et_year);
                }else if(degree.equalsIgnoreCase("")){
                    et_degree.setError("Enter Degree");
                    requestFocus(et_degree);
                }else if(place.equalsIgnoreCase("")){
                    et_place.setError("Enter Place");
                    requestFocus(et_place);
                }else {
                    saveAboutUsEducationList(institute_name,year, degree, place,educationId);
                }
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

    void saveAboutUsEducationList(final String clg_name, final String pass_year,
                                  final String degree, final String place, final String educationId
    ) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Education_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Education_Update")) {
                            JSONArray array = object.getJSONArray("Education_Update");
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
                    map.put("EducationId", educationId);
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("InstitiuteName", clg_name);
                    map.put("Yaer", pass_year);
                    map.put("Degree", degree);
                    map.put("InstitiutePlace", place);

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


    private boolean validateInstituteName() {
        String name = et_institute_name.getText().toString().trim();
        if (name.isEmpty()) {
            et_institute_name.setError("Enter Institute Name");
            requestFocus(et_institute_name);
            return false;
        } else {
            et_institute_name.setError(null);
        }
        return true;
    }
    private boolean validateYear() {
        String name = et_year.getText().toString().trim();
        if (name.isEmpty()) {
            et_year.setError("Enter Year");
            requestFocus(et_year);
            return false;
        } else {
            et_year.setError(null);
        }
        return true;
    }
    private boolean validateDegree() {
        String name = et_degree.getText().toString().trim();
        if (name.isEmpty()) {
            et_degree.setError("Enter Degree");
            requestFocus(et_degree);
            return false;
        } else {
            et_degree.setError(null);
        }
        return true;
    }
    private boolean validatePlace() {
        String name = et_place.getText().toString().trim();
        if (name.isEmpty()) {
            et_place.setError("Enter Place");
            requestFocus(et_degree);
            return false;
        } else {
            et_place.setError(null);
        }
        return true;
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
                case R.id.et_institute_name:
                    validateInstituteName();
                    break;
                case R.id.et_year:
                    validateYear();
                    break;
                case R.id.et_degree:
                    validateDegree();
                    break;
                case R.id.et_place:
                    validatePlace();
                    break;



            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}

