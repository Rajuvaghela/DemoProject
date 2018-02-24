package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 11/10/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
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
import com.ext.adarsh.Activity.PeopleActivity;
import com.ext.adarsh.Bean.BeanMyContact;
import com.ext.adarsh.Bean.BeanPeopleGroups;
import com.ext.adarsh.Bean.BeanPeoplefavourite;
import com.ext.adarsh.Fragment.PeopleGroupsFragment;
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
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;


public class AdapterPeopleGroups extends RecyclerView.Adapter<AdapterPeopleGroups.ViewHolder> {

    Activity activity;
    List<BeanPeopleGroups> list = new ArrayList<>();
    ProgressDialog pd;
    Dialog addgroup, dd;
    Dialog group_people;
    public static ArrayList<BeanMyContact> beanPeopleNewsSearch = new ArrayList<>();
    RecyclerView recylerpeoplesearch;
    private static AdapterGropPeopleList adapter;
    RecyclerView recyclercontact;
    TextView tv_no_record_found;
    EditText search_people;

    public AdapterPeopleGroups(ArrayList<BeanPeopleGroups> list, Activity activity) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_groups_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
        holder.tv_group_name.setText(list.get(position).categoryName);
        holder.ll_edit_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditGroupDialog(list.get(position).categoryName, list.get(position).categoryId);
            }
        });

        holder.ln_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  showAlertDialog3(list.get(position).categoryId);
            }
        });

        holder.ll_delete_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialoge(position);
            }
        });

        holder.ll_open_grop_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPeopleData(list.get(position).categoryId);
              //  OpenGroupPeople();
                GetGropPeopleData();
            }
        });
    }

    private void GetGropPeopleData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_People_Filter, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("fav", response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("My_Favourites_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Favourites_Array");
                            if (jsonArray.length() != 0) {
                /*                beanPeopleNews.clear();
                                beanPeopleNews.addAll((Collection<? extends BeanPeoplefavourite>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeoplefavourite>>() {
                                }.getType()));
                                OpenGroupPeople();*/
                                pd.dismiss();
                            } else {
                              //  tv_no_record_found.setVisibility(View.VISIBLE);
                        /*        beanPeopleNews.clear();
                                recylerpeople.setAdapter(null);*/
                                pd.dismiss();
                            }
                        }

                        //  swipeRefesh.setRefreshing(false);
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
                    map.put("TopValue", "0");
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
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
                        DeletePeopleGroups(list.get(position).categoryId, position);
                        //   deletedata(annoucementList.get(position).announcementId);
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void DeletePeopleGroups(final String categoryId, final int position) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Category_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("Category_Delete")) {
                            JSONArray array = object.getJSONArray("Category_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        list.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
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
                    map.put("CategoryId", categoryId);
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_group_name;
        LinearLayout ll_edit_groups, ll_delete_groups, ln_main, ll_open_grop_people;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_open_grop_people = (LinearLayout) itemView.findViewById(R.id.ll_open_grop_people);
            ll_edit_groups = (LinearLayout) itemView.findViewById(R.id.ll_edit_groups);
            ll_delete_groups = (LinearLayout) itemView.findViewById(R.id.ll_delete_groups);
            ln_main = (LinearLayout) itemView.findViewById(R.id.ln_main);
            tv_group_name = (TextView) itemView.findViewById(R.id.tv_group_name);
        }
    }

    private void EditGroupDialog(String categoryName, final String categoryId) {
        addgroup = new Dialog(activity);
        addgroup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addgroup.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        addgroup.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addgroup.setContentView(R.layout.add_new_groping_dialog);
        Window window = addgroup.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        TextView title = (TextView) addgroup.findViewById(R.id.title);
        final TextView edt_groupname = (EditText) addgroup.findViewById(R.id.edt_groupname);
        Button btn_save = (Button) addgroup.findViewById(R.id.btn_save);
        ImageView iv_close = (ImageView) addgroup.findViewById(R.id.iv_close);
        title.setTypeface(Utility.getTypeFace());
        edt_groupname.setTypeface(Utility.getTypeFace());
        edt_groupname.setText(categoryName);
        title.setText("Edit Your Group");
        btn_save.setTypeface(Utility.getTypeFace());
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_groupname.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                } else {
                    EditGroupPeople(edt_groupname.getText().toString(), categoryId);
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addgroup.dismiss();
            }
        });
        addgroup.show();
    }


    private void OpenGroupPeople() {
        group_people = new Dialog(activity);
        group_people.requestWindowFeature(Window.FEATURE_NO_TITLE);
        group_people.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        group_people.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        group_people.setContentView(R.layout.group_people_list);
        Window window = group_people.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        recyclercontact = (RecyclerView) group_people.findViewById(R.id.recyclercontact);
        tv_no_record_found = (TextView) group_people.findViewById(R.id.tv_no_record_found);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclercontact.setLayoutManager(mLayoutManager);
        recyclercontact.setItemAnimator(new DefaultItemAnimator());
        adapter = new AdapterGropPeopleList(beanPeopleNewsSearch, activity);
        recyclercontact.setAdapter(adapter);
        search_people=(EditText)group_people.findViewById(R.id.search_people);

        search_people.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        group_people.show();
    }

    public void EditGroupPeople(final String name, final String categoryId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Category_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("Category_Update")) {
                            JSONArray array = object.getJSONArray("Category_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        addgroup.dismiss();
                                        PeopleGroupsFragment.getPeopleGroup();
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
                    map.put("CategoryName", name);
                    map.put("CategoryId", categoryId);
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

    public void getPeopleData(final String id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_People_Filter, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("My_Contact_Pepole_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Contact_Pepole_Array");
                            if (jsonArray.length() != 0) {
                                beanPeopleNewsSearch.clear();
                                beanPeopleNewsSearch.addAll((Collection<? extends BeanMyContact>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMyContact>>() {
                                }.getType()));
                                OpenGroupPeople();
                                pd.dismiss();
                            } else {
                                beanPeopleNewsSearch.clear();
                                Toast.makeText(activity, "No Data Available!", Toast.LENGTH_SHORT).show();
                                //recylerpeoplesearch.setAdapter(null);
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
                    map.put("PepoleId", Utility.getPeopleIdPreference());
                    map.put("CategoryId", id);
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

