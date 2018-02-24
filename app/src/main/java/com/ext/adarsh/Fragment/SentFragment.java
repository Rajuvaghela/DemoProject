package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.ext.adarsh.Adapter.AdapterSentMessage;
import com.ext.adarsh.Bean.BeanMessage;
import com.ext.adarsh.Bean.BeanMessagePagination;
import com.ext.adarsh.Bean.BeanMessageTotal;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class SentFragment extends Fragment {

    @BindView(R.id.composemail_float)
    FloatingActionButton composemail_float;
    public static Handler handler;
    static int pageCount = 0;
    SwipeRefreshLayout swipeRefreshLayout;

    public static Activity activity;
    public static ProgressDialog pd;
    private static List<BeanMessage> messageList = new ArrayList<>();
    private static List<BeanMessagePagination> messageListpagination = new ArrayList<>();
    private static List<BeanMessageTotal> messageListtotal = new ArrayList<>();
    private static RecyclerView recyclerView;
    private static AdapterSentMessage mAdapter;
    EditText search_people;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sent, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        pd = Utility.getDialog(activity);

        search_people = (EditText) view.findViewById(R.id.search_people);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Swipe_Refresh);

        mAdapter = new AdapterSentMessage(messageList, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        message();


        composemail_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,MessageCompose.class);
                intent.putExtra("Key","C");
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // implement Handler to wait for 3 seconds and then update UI means update value of TextView
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        message();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        search_people.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static void message() {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Sent_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Sent_Select_All_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Message_Sent_Select_All_Array");
                            if (jsonArray.length() != 0) {
                                messageList.clear();
                                messageList.addAll((Collection<? extends BeanMessage>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMessage>>() {
                                }.getType()));
                                mAdapter = new AdapterSentMessage(messageList,activity);
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyItemInserted(messageList.size());
                                mAdapter.setLoaded();



                                mAdapter.setOnLoadMoreListener(new AdapterSentMessage.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        messageList.add(null);
                                        mAdapter.notifyItemInserted(messageList.size() - 1);
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                messageList.remove(messageList.size() - 1);
                                                mAdapter.notifyItemRemoved(messageList.size());
                                                pageCount += 10;
                                                message();
                                            }
                                        }, 2000);
                                        System.out.println("load");
                                    }
                                });


                                pd.dismiss();
                            }
                        }
                        if (object.has("Message_Sent_Pagination_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Message_Sent_Pagination_Array");
                            if (jsonArray.length() != 0) {
                                messageListpagination.clear();
                                messageListpagination.addAll((Collection<? extends BeanMessagePagination>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMessagePagination>>() {
                                }.getType()));

                                //   mAdapter = new AdapterMessage(messageList,activity);
                                // recyclerView.setAdapter(mAdapter);
                                pd.dismiss();
                            }
                        }
                        if (object.has("Message_Sent_Total_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Message_Sent_Total_Array");
                            if (jsonArray.length() != 0) {
                                messageListtotal.clear();
                                messageListtotal.addAll((Collection<? extends BeanMessageTotal>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMessageTotal>>() {
                                }.getType()));
                                //  mAdapter = new AdapterMessage(messageList,activity);
                                // recyclerView.setAdapter(mAdapter);
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
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Topval", String.valueOf(pageCount));

                    Log.e("Hashkey",""+Utility.getHashKeyPreference());
                    Log.e("LoginId",""+ Utility.getPeopleIdPreference());
                    Log.e("Topval",""+ String.valueOf(pageCount));
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

    public static void refresh(){
        mAdapter = new AdapterSentMessage(messageList,activity);
        recyclerView.setAdapter(mAdapter);
    }
}

