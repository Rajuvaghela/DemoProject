package com.ext.adarsh.Adapter;

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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.ext.adarsh.Activity.TaskNewActivity;
import com.ext.adarsh.Bean.BeanTODO;
import com.ext.adarsh.Bean.BeanTaskTodoPeopleList;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Fragment.Myannouncement.dateFormatter;
import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;


public class AdapterTaskToDo extends RecyclerView.Adapter<AdapterTaskToDo.MyViewHolder> {

    Activity activity;
    ArrayList<BeanTODO> beanTODOs = new ArrayList<>();
    ProgressDialog pd;
    Dialog editTODOTask;
    EditText edt_subject, edt_note;
    ArrayList<BeanTaskTodoPeopleList> beanPeopleLists = new ArrayList<>();

    Spinner spiner_people;
    String peopleid;
    String peopleName;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.check_task)
        CheckBox check_task;

        @BindView(R.id.frame_ans)
        FrameLayout frame_ans;

        @BindView(R.id.txt_logcat)
        TextView txt_logcat;

        @BindView(R.id.ln_logcat)
        LinearLayout ln_logcat;

        @BindView(R.id.txt_note)
        TextView txt_note;

        @BindView(R.id.ln_edit)
        LinearLayout ln_edit;

        @BindView(R.id.ln_delete)
        LinearLayout ln_delete;

        @BindView(R.id.edit_my_announcement)
        TextView edit_my_announcement;

        @BindView(R.id.delete)
        TextView delete;

        @BindView(R.id.menu)
        ImageView menu;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void showpopupmenu(View view, final int position) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.task_todo_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_edit:
                        editTaskTODODialog(beanTODOs.get(position).nameOfToDo, beanTODOs.get(position).note, beanTODOs.get(position).dueDate, position, beanTODOs.get(position).personId);
                        break;

                    case R.id.menu_delete:
                        deleteDialoge(position);
                        break;

                }
                return true;
            }
        });
        popup.show();
    }

    public AdapterTaskToDo(ArrayList<BeanTODO> beanTODOs, Activity activity) {
        this.beanTODOs = beanTODOs;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daynamiclinear_task, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        pd = Utility.getDialog(activity);


        if (Utility.getTodo().equalsIgnoreCase("Y")) {
            holder.menu.setVisibility(View.VISIBLE);
        } else {
            holder.menu.setVisibility(View.GONE);
        }


        if (beanTODOs.get(position).IsPersonExit.equalsIgnoreCase("A")) {
            holder.frame_ans.setVisibility(View.VISIBLE);
        } else {
            holder.frame_ans.setVisibility(View.GONE);
        }

        if (beanTODOs.get(position).statusFlag.equalsIgnoreCase("P")) {
            holder.check_task.setChecked(false);
            holder.ln_logcat.setVisibility(View.GONE);
        } else {
            holder.check_task.setChecked(true);
            holder.ln_logcat.setVisibility(View.VISIBLE);
            holder.txt_logcat.setText(beanTODOs.get(position).fullName + " completed this to-do");
        }

        holder.edit_my_announcement.setTypeface(Utility.getTypeFaceTab());
        holder.delete.setTypeface(Utility.getTypeFaceTab());

        holder.ln_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialoge(position);
            }
        });

        holder.ln_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTaskTODODialog(beanTODOs.get(position).nameOfToDo, beanTODOs.get(position).note, beanTODOs.get(position).dueDate, position, beanTODOs.get(position).personId);
            }
        });

        if (beanTODOs.get(position).assignById.equalsIgnoreCase(Utility.getPeopleIdPreference())) {
            holder.menu.setVisibility(View.VISIBLE);
        } else {
            holder.menu.setVisibility(View.GONE);
        }

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopupmenu(v, position);
            }
        });

        holder.title.setText(beanTODOs.get(position).nameOfToDo);
        holder.name.setText("To, " + beanTODOs.get(position).name);

        String task_date = "";
        try {
            task_date = dateFormatter(beanTODOs.get(position).dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.date.setText("By, " + task_date);

        holder.txt_note.setText(beanTODOs.get(position).note);

        holder.title.setTypeface(Utility.getTypeFaceTab());
        holder.name.setTypeface(Utility.getTypeFace());
        holder.date.setTypeface(Utility.getTypeFace());
        holder.txt_logcat.setTypeface(Utility.getTypeFace());
        holder.txt_note.setTypeface(Utility.getTypeFace());


        holder.check_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                todoUpdateStatus(beanTODOs.get(position).taskDoId);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return beanTODOs.size();
    }

    void todoUpdateStatus(final String id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Task_To_Do_Update_Status, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Task_To_Do_Update_Status")) {
                            JSONArray array = object.getJSONArray("Task_To_Do_Update_Status");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        TaskNewActivity.getTaskData();
                                        //  Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
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
                    map.put("TaskDoId", id);
                    map.put("LoginId", Utility.getPeopleIdPreference());
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


    void deleteToDoTask(final int position) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Task_To_Do_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Task_To_Do_Delete")) {
                            JSONArray array = object.getJSONArray("Task_To_Do_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        TaskNewActivity.getTaskData();
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
                    map.put("TaskDoId", beanTODOs.get(position).taskDoId);
                    map.put("LoginId", Utility.getPeopleIdPreference());
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


    private void editTaskTODODialog(final String nametodo, final String note, final String date, final int position,
                                    final String personID) {
        editTODOTask = new Dialog(activity);
        editTODOTask.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editTODOTask.getWindow().setWindowAnimations(R.style.DialogAnimation);
        editTODOTask.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        editTODOTask.setContentView(R.layout.m_add_to_do);

        Window window = editTODOTask.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView tv1 = (TextView) editTODOTask.findViewById(R.id.tv1);
        TextView tv2 = (TextView) editTODOTask.findViewById(R.id.tv2);
        TextView tv3 = (TextView) editTODOTask.findViewById(R.id.tv3);
        TextView tv4 = (TextView) editTODOTask.findViewById(R.id.tv4);

        TextView header = (TextView) editTODOTask.findViewById(R.id.header);
        TextView txt_addtotdo = (TextView) editTODOTask.findViewById(R.id.txt_addtotdo);
        TextView txt_cancle = (TextView) editTODOTask.findViewById(R.id.txt_cancle);

        header.setText("Edit to do");
        txt_addtotdo.setText("SAVE TO DO");

        edt_subject = (EditText) editTODOTask.findViewById(R.id.edt_subject);
        edt_note = (EditText) editTODOTask.findViewById(R.id.edt_note);

        edt_subject.addTextChangedListener(new MyTextWatcher(edt_subject));
        edt_note.addTextChangedListener(new MyTextWatcher(edt_note));

        edt_subject.setText(nametodo);
        edt_note.setText(note);
        peopleid = personID;

        spiner_people = (Spinner) editTODOTask.findViewById(R.id.spiner_people);
        RelativeLayout rl_date1 = (RelativeLayout) editTODOTask.findViewById(R.id.rl_date1);

        final EditText tv_task_date = (EditText) editTODOTask.findViewById(R.id.tv_task_date);
        tv_task_date.setEnabled(false);

        String task_date = "";
        try {
            task_date = dateFormatter(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_task_date.setText(task_date);

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());

        header.setTypeface(Utility.getTypeFace());
        edt_subject.setTypeface(Utility.getTypeFace());
        tv_task_date.setTypeface(Utility.getTypeFace());
        edt_note.setTypeface(Utility.getTypeFace());

        txt_addtotdo.setTypeface(Utility.getTypeFaceTab());
        txt_cancle.setTypeface(Utility.getTypeFaceTab());

        rl_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tv_task_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        getPeopleList();

        spiner_people.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                peopleid = beanPeopleLists.get(i).peopleId;
                peopleName = beanPeopleLists.get(i).fullName;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        LinearLayout drawericon = (LinearLayout) editTODOTask.findViewById(R.id.lnmainback);

        txt_addtotdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_subject.getText().toString().trim().equalsIgnoreCase("")) {
                    edt_subject.setError("Please enter announcement title.");
                    requestFocus(edt_subject);
                } else if (edt_note.getText().toString().trim().equalsIgnoreCase("")) {
                    edt_note.setError("Please enter announcement title.");
                    requestFocus(edt_note);
                } else if (peopleid.equalsIgnoreCase("0")) {
                    Toast.makeText(activity, "Select People", Toast.LENGTH_SHORT).show();

                } else if (tv_task_date.getText().toString().equalsIgnoreCase("")) {
                    tv_task_date.setError("Select Date");
                    requestFocus(tv_task_date);
                } else {
                    updateTODOTask(edt_subject.getText().toString(), peopleName, peopleid, tv_task_date.getText().toString(), edt_note.getText().toString(), beanTODOs.get(position).taskId, position);

                }
            }
        });

        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTODOTask.dismiss();
            }
        });

        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTODOTask.dismiss();
            }
        });

        editTODOTask.show();
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
                case R.id.edt_subject:
                    validateTitle();
                    break;
                case R.id.edt_note:
                    validateDetail();
                    break;
            }
        }
    }

    void updateTODOTask(final String name_of_todo, final String name, final String person_id, final String duedate,
                        final String note, final String task_id, final int position) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Task_To_Do_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Task_To_Do_Update")) {
                            JSONArray array = object.getJSONArray("Task_To_Do_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        editTODOTask.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        TaskNewActivity.getTaskData();
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
                    map.put("NameOfToDo", name_of_todo);
                    map.put("Name", name);
                    map.put("PersonId", person_id);
                    map.put("DueDate", duedate);
                    map.put("Note", note);
                    map.put("TaskId", task_id);
                    map.put("TaskDoId", beanTODOs.get(position).taskDoId);
                    map.put("LoginId", Utility.getPeopleIdPreference());
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateTitle() {
        String email = edt_subject.getText().toString().trim();
        if (email.isEmpty()) {
            edt_subject.setError("Please enter to do name.");
            requestFocus(edt_subject);
            return false;
        } else {
            edt_subject.setError(null);
        }
        return true;
    }

    private boolean validateDetail() {
        String email = edt_note.getText().toString().trim();
        if (email.isEmpty()) {
            edt_note.setError("Please enter note.");
            requestFocus(edt_note);
            return false;
        } else {
            edt_note.setError(null);
        }
        return true;
    }

    public void getPeopleList() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Task_To_Do_Select_Persons, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Task_To_Do_Persons_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Task_To_Do_Persons_Array");
                            if (jsonArray.length() != 0) {
                                beanPeopleLists.clear();
                                beanPeopleLists.add(new BeanTaskTodoPeopleList("0", "Select People"));
                                beanPeopleLists.addAll((Collection<? extends BeanTaskTodoPeopleList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanTaskTodoPeopleList>>() {
                                }.getType()));
                                AdapterPeopleList adapter = new AdapterPeopleList(activity, beanPeopleLists);
                                spiner_people.setAdapter(adapter);
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("PeopleId", Utility.getPeopleIdPreference());
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

    private void deleteDialoge(final int position) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Delete")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        deleteToDoTask(position);
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

}