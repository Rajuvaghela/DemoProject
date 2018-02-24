package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ext.adarsh.Activity.MessageCompose;
import com.ext.adarsh.Activity.ProfileActivity;
import com.ext.adarsh.Adapter.AdapterColleagues;
import com.ext.adarsh.Adapter.AdapterMessage;
import com.ext.adarsh.Bean.BeanProfileColleguesFriends;
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

public class activty_fried_request extends Fragment {

    @BindView(R.id.recyclercontact)
    RecyclerView recyclercontact;

    @BindView(R.id.search_people)
    EditText search_people;
    Activity activity;
    private ProgressDialog pd;
    private ArrayList<BeanProfileColleguesFriends> beanProfileColleguesFriends = new ArrayList<>();
    private AdapterColleagues mAdapter;
    public static ArrayList<BeanProfileColleguesFriends> beanProfileColleguesFriendses_list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_friends_request, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        pd = Utility.getDialog(activity);
        recyclercontact = (RecyclerView)view.findViewById(R.id.recyclercontact);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclercontact.setLayoutManager(mLayoutManager);
        recyclercontact.setItemAnimator(new DefaultItemAnimator());
        recyclercontact.setAdapter(mAdapter);
       /* Bundle extra = getIntent().getExtras();
        if (null != extra) {
            beanProfileColleguesFriendses_list = extra.getParcelableArrayList("Data");
        }*/
        search_people.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  getLength(s.toString());
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // getLength(s.toString());
                mAdapter.getFilter().filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // getLength(s.toString());
            }
        });

    }

    public void getColleaguesData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Colleagues_Select_By_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Contact_List_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Contact_List_Array");
                            if (jsonArray.length() != 0) {
                                beanProfileColleguesFriendses_list.clear();
                                beanProfileColleguesFriendses_list.addAll((Collection<? extends BeanProfileColleguesFriends>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanProfileColleguesFriends>>() {
                                }.getType()));
                                mAdapter = new AdapterColleagues(beanProfileColleguesFriendses_list, activity);
                                recyclercontact.setAdapter(mAdapter);
                                pd.dismiss();
                            } else {
                                recyclercontact.setAdapter(null);
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

    @Override
    public void onStart() {
        super.onStart();
        getColleaguesData();
    }
}

