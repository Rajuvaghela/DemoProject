package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

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

import com.ext.adarsh.Activity.FeedPostActivity;
import com.ext.adarsh.Adapter.AdapterFeedNews;
import com.ext.adarsh.Bean.BeanFeedNews;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.ServiceHandler;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

/**
 * Created by ExT-Emp-001 on 11-07-2017.
 */
public class activty_feed extends Fragment {


    public static RecyclerView rv_news_feed;

    public static Activity activity;
    ProgressDialog pd;
    static List<BeanFeedNews> bean_feed_List = new ArrayList<>();

    public static AdapterFeedNews adapter_feed_news;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;
    public static String response, method = "POST";
    public static String str = "";
    public static String is_activityfeed_comment = "yes";
    LinearLayout ll_whats_on_your_mind;
    ImageView iv_profile_img;
    TextView share_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_feed, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        pd = Utility.getDialog(activity);

        iv_profile_img = (ImageView) view.findViewById(R.id.iv_profile_img);
        ll_whats_on_your_mind = (LinearLayout) view.findViewById(R.id.ll_whats_on_your_mind);
        share_txt = (TextView) view.findViewById(R.id.share_txt);

        share_txt.setText("Share an update, " + Utility.getFullNamePreference());
        share_txt.setTypeface(Utility.getTypeFace());
        Glide.with(activity).load(Utility.getPeopleProfileImgPreference()).into(iv_profile_img);

        share_txt.setTypeface(Utility.getTypeFace());
        ll_whats_on_your_mind.setVisibility(View.VISIBLE);
        ll_whats_on_your_mind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i8 = new Intent(activity, FeedPostActivity.class);
                i8.putExtra("key", "profile");
                i8.putExtra("people_id", Utility.getPeopleIdPreference());
                i8.putExtra("login_id", Utility.getPeopleIdPreference());
                i8.putExtra("people_name", Utility.getFullNamePreference());
                i8.putExtra("people_profile", Utility.getPeopleProfileImgPreference());
                activity.startActivity(i8);
            }
        });
        //  Glide.with(activity).load(pos.profileImage).into(holder.iv_profile_img);
        rv_news_feed = (RecyclerView) view.findViewById(R.id.rv_news_feed);
/*        rv_news_feed.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_news_feed.setLayoutManager(mLayoutManager);
        rv_news_feed.setItemAnimator(new DefaultItemAnimator());*/

        rv_news_feed.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivityFeedList2();
            }
        });
        getActivityFeedList();
    }

    void getActivityFeedList() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_SelectAll, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("feed_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Post_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Post_Array");
                            if (jsonArray.length() != 0) {
                                bean_feed_List.clear();
                                bean_feed_List.addAll((Collection<? extends BeanFeedNews>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFeedNews>>() {
                                }.getType()));

                                AppConstant.beanFeedNew.clear();
                                AppConstant.beanFeedNew.addAll((Collection<? extends BeanFeedNews>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFeedNews>>() {
                                }.getType()));
                                adapter_feed_news = new AdapterFeedNews(activity, bean_feed_List);
                                rv_news_feed.setAdapter(adapter_feed_news);
            /*                    adapter_feed_news = new AdapterFeedNews(activity, bean_feed_List);
                                final SkeletonScreen skeletonScreen = Skeleton.bind(rv_news_feed)
                                        .adapter(adapter_feed_news)
                                        .load(R.layout.item_skeleton_news)
                                        .show(); //default count is 10
                                rv_news_feed.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        skeletonScreen.hide();
                                    }
                                }, 3000);
*/
                                pd.dismiss();
                            } else {
                                rv_news_feed.setAdapter(null);
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

        }
    }

    void getActivityFeedList2() {
        swipe_refresh_layout.setRefreshing(true);
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_SelectAll, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("feed_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Post_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Post_Array");
                            if (jsonArray.length() != 0) {
                                bean_feed_List.clear();
                                bean_feed_List.addAll((Collection<? extends BeanFeedNews>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFeedNews>>() {
                                }.getType()));
                                adapter_feed_news = new AdapterFeedNews(activity, bean_feed_List);
                                rv_news_feed.setAdapter(adapter_feed_news);
                                swipe_refresh_layout.setRefreshing(false);
                            } else {
                                swipe_refresh_layout.setRefreshing(false);
                            }
                            swipe_refresh_layout.setRefreshing(false);
                        }
                        swipe_refresh_layout.setRefreshing(false);
                    } catch (JSONException e) {
                        swipe_refresh_layout.setRefreshing(false);
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    swipe_refresh_layout.setRefreshing(false);
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
        }
    }

    public static class postBackgroundRefresh extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {

                String reqParam = "";

                reqParam += "Hashkey=" + Utility.getHashKeyPreference();
                reqParam += "&PeopleId=" + Utility.getPeopleIdPreference();

                response = ServiceHandler.makeServiceCall(AppConstant.Activity_Feed_Post_SelectAll, reqParam, method);
                Log.e("post validation", "++++++++++++++++++++++" + response);
                Log.e("post validation", "++++++++++++++++++++++" + reqParam);

            } catch (Exception e) {
                e.printStackTrace();
            }
            str = response.toString();
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(str);
                if (object.has("Post_Array")) {
                    JSONArray jsonArray = object.getJSONArray("Post_Array");
                    if (jsonArray.length() != 0) {
                        bean_feed_List.clear();
                        bean_feed_List.addAll((Collection<? extends BeanFeedNews>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFeedNews>>() {
                        }.getType()));
                        adapter_feed_news = new AdapterFeedNews(activity, bean_feed_List);
                        adapter_feed_news.notifyDataSetChanged();
                        rv_news_feed.setAdapter(adapter_feed_news);
                        adapter_feed_news.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void showPopupWindow(View view) {
        PopupMenu popup = new PopupMenu(activity, view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            pd.dismiss();
            showMsg(R.string.json_error);
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getApplicationContext(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }

}

