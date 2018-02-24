package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.ext.adarsh.Bean.BeanTask;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterTask extends RecyclerView.Adapter<AdapterTask.MyViewHolder> {

    private ArrayList<BeanTask> beanTasks;
    Activity activity;
    Dialog addnewTODOTask;
    ProgressDialog pd;
    ArrayList<BeanTaskTodoPeopleList> beanPeopleLists = new ArrayList<>();

    Spinner spiner_people;
    String peopleid;
    String peopleName;
    EditText edt_subject, edt_note;
    Dialog edit_task_dialog;

    EditText et_name_of_task;
    EditText et_task_des;
    TextView edit_my_announcement, delete;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lnaddtodo)
        LinearLayout lnaddtodo;

        @BindView(R.id.txt_title)
        TextView txt_title;

        @BindView(R.id.txt_desc)
        TextView txt_desc;

        @BindView(R.id.txt_addtodo)
        TextView txt_addtodo;

        @BindView(R.id.ln_task)
        RecyclerView ln_task;

        @BindView(R.id.ln_edit)
        LinearLayout ln_edit;

        @BindView(R.id.ln_delete)
        LinearLayout ln_delete;

        @BindView(R.id.ll_edit_delete)
        LinearLayout ll_edit_delete;

        @BindView(R.id.edit_my_announcement)
        TextView edit_my_announcement;

        @BindView(R.id.delete)
        TextView delete;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public AdapterTask(Activity activity, ArrayList<BeanTask> beanTasks) {
        this.beanTasks = beanTasks;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);


        if (Utility.getTaskUpdate().equalsIgnoreCase("Y")) {
            holder.ln_edit.setVisibility(View.VISIBLE);
        } else {
            holder.ln_edit.setVisibility(View.GONE);
        }
        if (Utility.getTaskDelete().equalsIgnoreCase("Y")) {
            holder.ln_delete.setVisibility(View.VISIBLE);
        } else {
            holder.ln_delete.setVisibility(View.GONE);
        }
        if (Utility.getTaskUpdate().equalsIgnoreCase("N") && Utility.getTaskDelete().equalsIgnoreCase("N")) {
            holder.ll_edit_delete.setVisibility(View.GONE);
        } else {
            holder.ll_edit_delete.setVisibility(View.VISIBLE);
        }

        if (beanTasks.get(position).matchId.equalsIgnoreCase(beanTasks.get(position).peopleId)) {
            holder.lnaddtodo.setVisibility(View.VISIBLE);
        } else {
            holder.lnaddtodo.setVisibility(View.GONE);
        }

        if (beanTasks.get(position).peopleId.equalsIgnoreCase(Utility.getPeopleIdPreference())) {
            holder.ll_edit_delete.setVisibility(View.VISIBLE);
        } else {
            holder.ll_edit_delete.setVisibility(View.GONE);
        }

        holder.lnaddtodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskTODODialog(position);
            }
        });

        holder.edit_my_announcement.setTypeface(Utility.getTypeFaceTab());
        holder.delete.setTypeface(Utility.getTypeFaceTab());
        holder.ln_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTaskList(beanTasks.get(position).taskTitle, beanTasks.get(position).TaskDescription, position);
            }
        });

        holder.ln_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialoge(position);
            }
        });

        holder.txt_desc.setTypeface(Utility.getTypeFace());

        holder.txt_title.setText(beanTasks.get(position).taskTitle);
        holder.txt_desc.setText(beanTasks.get(position).TaskDescription);

        holder.txt_title.setTypeface(Utility.getTypeFaceTab());
        holder.txt_addtodo.setTypeface(Utility.getTypeFaceTab());

        holder.ln_task.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        holder.ln_task.setLayoutManager(lnmanager2);
        holder.ln_task.setItemAnimator(new DefaultItemAnimator());

        for (int i = 0; i < beanTasks.get(position).task_To_Do_Array.size(); i++) {

            if (!beanTasks.get(position).task_To_Do_Array.get(i).IsPersonExit.equalsIgnoreCase("A")) {
                beanTasks.get(position).task_To_Do_Array.remove(i);
            }
        }
        AdapterTaskToDo adapter = new AdapterTaskToDo(beanTasks.get(position).task_To_Do_Array, activity);
        holder.ln_task.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return beanTasks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void addTaskTODODialog(final int position) {
        addnewTODOTask = new Dialog(activity);
        addnewTODOTask.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addnewTODOTask.getWindow().setWindowAnimations(R.style.DialogAnimation);
        addnewTODOTask.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addnewTODOTask.setContentView(R.layout.m_add_to_do);

        Window window = addnewTODOTask.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView tv1 = (TextView) addnewTODOTask.findViewById(R.id.tv1);
        TextView tv2 = (TextView) addnewTODOTask.findViewById(R.id.tv2);
        TextView tv3 = (TextView) addnewTODOTask.findViewById(R.id.tv3);
        TextView tv4 = (TextView) addnewTODOTask.findViewById(R.id.tv4);

        TextView header = (TextView) addnewTODOTask.findViewById(R.id.header);
        TextView txt_addtotdo = (TextView) addnewTODOTask.findViewById(R.id.txt_addtotdo);
        TextView txt_cancle = (TextView) addnewTODOTask.findViewById(R.id.txt_cancle);


        edt_subject = (EditText) addnewTODOTask.findViewById(R.id.edt_subject);
        edt_note = (EditText) addnewTODOTask.findViewById(R.id.edt_note);

        edt_subject.addTextChangedListener(new MyTextWatcher(edt_subject));
        edt_note.addTextChangedListener(new MyTextWatcher(edt_note));

        spiner_people = (Spinner) addnewTODOTask.findViewById(R.id.spiner_people);
        RelativeLayout rl_date1 = (RelativeLayout) addnewTODOTask.findViewById(R.id.rl_date1);

        final EditText tv_task_date = (EditText) addnewTODOTask.findViewById(R.id.tv_task_date);
        tv_task_date.setEnabled(false);

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


        LinearLayout drawericon = (LinearLayout) addnewTODOTask.findViewById(R.id.lnmainback);

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
                    addTODOTask(edt_subject.getText().toString(), peopleName, peopleid, tv_task_date.getText().toString(), edt_note.getText().toString(), beanTasks.get(position).taskId);

                }
            }
        });

        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewTODOTask.dismiss();
            }
        });

        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewTODOTask.dismiss();
            }
        });

        addnewTODOTask.show();
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

                case R.id.et_name_of_task:
                    validateTask(et_name_of_task.getText().toString().trim());
                    break;

                case R.id.et_task_des:
                    validateTaskDes(et_task_des.getText().toString().trim());
                    break;

            }
        }
    }

    public boolean validateTask(String str) {
        String email = str;
        if (email.isEmpty()) {
            et_name_of_task.setError("Please Enter Name Of The Task");
            requestFocus(et_name_of_task);
            return false;
        } else {
            et_name_of_task.setError(null);
        }
        return true;
    }

    public boolean validateTaskDes(String str) {
        String email = str;
        if (email.isEmpty()) {
            et_task_des.setError("Please Enter  Task Description");
            requestFocus(et_task_des);
            return false;
        } else {
            et_task_des.setError(null);
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

    void addTODOTask(final String name_of_todo, final String name, final String person_id, final String duedate,
                     final String note, final String task_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Task_To_Do_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Task_To_Do_Add")) {
                            JSONArray array = object.getJSONArray("Task_To_Do_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        addnewTODOTask.dismiss();
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

    //delete task
    void deleteTask(final int position) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Task_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Task_Delete")) {
                            JSONArray array = object.getJSONArray("Task_Delete");
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
                    map.put("TaskId", beanTasks.get(position).taskId);
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

    //edit task
    private void EditTaskList(final String task_tile, final String task_description, final int position) {
        edit_task_dialog = new Dialog(activity);
        edit_task_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        edit_task_dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        edit_task_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        edit_task_dialog.setContentView(R.layout.add_task_list_dialog);

        Window window = edit_task_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        et_name_of_task = (EditText) edit_task_dialog.findViewById(R.id.et_name_of_task);
        et_task_des = (EditText) edit_task_dialog.findViewById(R.id.et_task_des);

        TextView text1 = (TextView) edit_task_dialog.findViewById(R.id.text1);
        TextView tv_name_of_task = (TextView) edit_task_dialog.findViewById(R.id.tv_name_of_task);
        TextView tv_task_des = (TextView) edit_task_dialog.findViewById(R.id.tv_task_des);
        TextView tv_add_task = (TextView) edit_task_dialog.findViewById(R.id.tv_add_task);
        TextView tv_cancel = (TextView) edit_task_dialog.findViewById(R.id.tv_cancel);

        et_name_of_task.setTypeface(Utility.getTypeFace());
        et_task_des.setTypeface(Utility.getTypeFace());
        tv_name_of_task.setTypeface(Utility.getTypeFace());
        tv_task_des.setTypeface(Utility.getTypeFace());

        et_name_of_task.setText(task_tile);
        et_task_des.setText(task_description);
        tv_add_task.setText("SAVE");
        text1.setText("Edit Task");

        text1.setTypeface(Utility.getTypeFaceTab());
        tv_add_task.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_task_dialog.dismiss();
            }
        });

        LinearLayout lnback = (LinearLayout) edit_task_dialog.findViewById(R.id.lnback);
        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_task_dialog.dismiss();
            }
        });

        et_name_of_task.addTextChangedListener(new MyTextWatcher(et_name_of_task));
        et_task_des.addTextChangedListener(new MyTextWatcher(et_task_des));

        LinearLayout ln_add_task = (LinearLayout) edit_task_dialog.findViewById(R.id.ln_add_task);
        ln_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_name_of_task.getText().toString().isEmpty() && !et_task_des.getText().toString().isEmpty()) {
                    EditTask(et_name_of_task.getText().toString(), et_task_des.getText().toString(), position);
                } else {
                    Utility.showDialog(activity, "Please enter required fields.");
                }
            }
        });

        edit_task_dialog.show();
    }

    void EditTask(final String name_of_task, final String task_des, final int position) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Task_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Task_Update")) {
                            JSONArray array = object.getJSONArray("Task_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        edit_task_dialog.dismiss();
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
                    map.put("TaskTitle", name_of_task);
                    map.put("TaskDescription", task_des);
                    map.put("TaskId", beanTasks.get(position).taskId);
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
                        deleteTask(position);
                        //  android.os.Process.killProcess(android.os.Process.myPid());
                        /// android.os.Process.killProcess(android.os.Process.myPid());
                        //  finish();
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
