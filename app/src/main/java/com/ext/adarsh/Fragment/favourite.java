package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.ext.adarsh.Adapter.AdapterPeoplefavourite;
import com.ext.adarsh.Bean.BeanPeoplefavourite;
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

public class favourite extends Fragment {

    public static RecyclerView recylerpeople;
    static Activity activity;
    static ProgressDialog pd;
    static AdapterPeoplefavourite adapter;
    static ArrayList<BeanPeoplefavourite> beanPeopleNews = new ArrayList<>();
    @BindView(R.id.search_people)
    TextView search_people;

  static TextView tv_no_record_found;
    public static Handler handler;
    static int pageCount = 10;

    Dialog dd;
    RecyclerView recylerpeoplesearch;
    public static ArrayList<BeanPeoplefavourite> beanPeopleNewsSearch = new ArrayList<>();

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourite_people, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        pd = Utility.getDialog(activity);
        handler = new Handler();
        search_people.setTypeface(Utility.getTypeFace());
        recylerpeople = (RecyclerView) view.findViewById(R.id.recylerpeople);
        tv_no_record_found = (TextView) view.findViewById(R.id.tv_no_record_found);
        recylerpeople.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        recylerpeople.setLayoutManager(lnmanager2);
        recylerpeople.setItemAnimator(new DefaultItemAnimator());

        search_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog3();
            }
        });
        getPeopleData();
    }

    public static void getPeopleData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Favourites_Select_By_More_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("fav", response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("My_Favourites_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Favourites_Array");
                            if (jsonArray.length() != 0) {
                                beanPeopleNews.clear();
                                beanPeopleNews.addAll((Collection<? extends BeanPeoplefavourite>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeoplefavourite>>() {
                                }.getType()));
                                pd.dismiss();
                            } else {
                                tv_no_record_found.setVisibility(View.VISIBLE);
                                beanPeopleNews.clear();
                                recylerpeople.setAdapter(null);
                                pd.dismiss();
                            }
                        }
                        adapter = new AdapterPeoplefavourite(beanPeopleNews, activity, recylerpeople);
                        recylerpeople.setAdapter(adapter);
                        adapter.setOnLoadMoreListener(new AdapterPeoplefavourite.OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                beanPeopleNews.add(null);
                                adapter.notifyItemInserted(beanPeopleNews.size() - 1);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        beanPeopleNews.remove(beanPeopleNews.size() - 1);
                                        adapter.notifyItemRemoved(beanPeopleNews.size());
                                        Log.e("run: ", "top value first ++++++++" + pageCount);
                                        pageCount += 10;
                                        Log.e("run: ", "top value ++++++++" + pageCount);
                                        getPeopleDataSecond(String.valueOf(pageCount));

                                    }
                                }, 2000);
                                System.out.println("load");
                            }
                        });
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

    public static void getPeopleDataSecond(final String value) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Favourites_Select_By_More_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("My_Favourites_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Favourites_Array");
                            if (jsonArray.length() != 0) {
                                beanPeopleNews.addAll((Collection<? extends BeanPeoplefavourite>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeoplefavourite>>() {
                                }.getType()));

                                adapter.notifyItemInserted(beanPeopleNews.size());
                                adapter.setLoaded();

                                pd.dismiss();
                            } else {
                                adapter.setLoaded();
                                pd.dismiss();
                            }
                        }
                        adapter.setLoaded();
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
                    map.put("TopValue", value);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    private void showAlertDialog3() {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.searchingpeoplelist);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnbacksharing = (LinearLayout)dd.findViewById(R.id.lnbacksharing);

        EditText ed_search_people= (EditText) dd.findViewById(R.id.search_people);
        recylerpeoplesearch = (RecyclerView) dd.findViewById(R.id.recylerpeoplesearch);

        recylerpeoplesearch.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        recylerpeoplesearch.setLayoutManager(lnmanager2);
        recylerpeoplesearch.setItemAnimator(new DefaultItemAnimator());

        ed_search_people.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.length();
                if (len > 3){
                    getPeopleDataSearching(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ed_search_people.setTypeface(Utility.getTypeFace());

        lnbacksharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
            }
        });

        dd.show();
    }

    public  void getPeopleDataSearching(final String searchvalue) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Search_favourites_People_List, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("My_Favourites_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Favourites_Array");
                            if (jsonArray.length() != 0) {
                                beanPeopleNewsSearch.clear();
                                beanPeopleNewsSearch.addAll((Collection<? extends BeanPeoplefavourite>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeoplefavourite>>() {}.getType()));
                                adapter = new AdapterPeoplefavourite(beanPeopleNewsSearch, activity,recylerpeoplesearch);
                                recylerpeoplesearch.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                recylerpeoplesearch.setAdapter(null);
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
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("SearchString", searchvalue);
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
}

