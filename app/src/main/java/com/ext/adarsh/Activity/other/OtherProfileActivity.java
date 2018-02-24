package com.ext.adarsh.Activity.other;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ext.adarsh.Activity.EventAddActivity;
import com.ext.adarsh.Activity.FeedPostActivity;
import com.ext.adarsh.Activity.MainActivity;
import com.ext.adarsh.Activity.MarketActivity;
import com.ext.adarsh.Activity.Organization_Chart;
import com.ext.adarsh.Activity.PeopleActivity;
import com.ext.adarsh.Activity.PhotoActivity;
import com.ext.adarsh.Adapter.AdapterAboutUsEducationList;
import com.ext.adarsh.Adapter.AdapterAboutUsWorkList;
import com.ext.adarsh.Adapter.AdapterProfileColleguesFriends;
import com.ext.adarsh.Adapter.AdapterProfilePost;
import com.ext.adarsh.Bean.BeanAboutUsEducationList;
import com.ext.adarsh.Bean.BeanAboutUsWorkDetailList;
import com.ext.adarsh.Bean.BeanFeedNews;
import com.ext.adarsh.Bean.BeanPeopleDetail;
import com.ext.adarsh.Bean.BeanProfileColleguesFriends;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

/**
 * Created by ExT-Emp-008 on 19-01-2018.
 */

public class OtherProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity activity;
    @BindView(R.id.rl_editcoverprofile)
    RelativeLayout rl_editcoverprofile;

    @BindView(R.id.whatsyour)
    LinearLayout whatsyour;

    @BindView(R.id.lnphoto)
    LinearLayout lnphoto;

    @BindView(R.id.lnback)
    LinearLayout lnback;

    @BindView(R.id.iv_user_edit_profile)
    ImageView iv_user_edit_profile;

    @BindView(R.id.rv_collegues_friends)
    RecyclerView rv_collegues_friends;

    @BindView(R.id.tv_about_us)
    TextView tv_about_us;

    @BindView(R.id.tv_about_title)
    TextView tv_about_title;

    @BindView(R.id.tv_no_of_collegaues)
    TextView tv_no_of_collegaues;

    public static RecyclerView rv_profile_post;

    @BindView(R.id.iv_profile_img)
    ImageView iv_profile_img;

    @BindView(R.id.share_txt)
    TextView share_txt;

    @BindView(R.id.see_all_friend)
    LinearLayout see_all_friend;

    String InfoId;

    @BindView(R.id.lnabout)
    LinearLayout lnabout;


    public static TextView tv_about_us_popup, tv_designation, tv_previously_work_at, tv_live_in, tv_with, tv_department;
    public static RecyclerView rv_about_us_work_list, rv_about_us_education;

    public static LinearLayout lndesignation, lnprework, lnlivesin, lnwith, lndepartment;

    public static AdapterProfileColleguesFriends adapterProfileColleguesFriends;
    public static AdapterAboutUsWorkList adapterAboutUsWorkList;
    public static AdapterAboutUsEducationList adapterAboutUsEducationList;

    public static ArrayList<BeanProfileColleguesFriends> beanProfileColleguesFriendses_list = new ArrayList<>();
    public static List<BeanAboutUsWorkDetailList> beanAboutUsWorkDetailLists_list = new ArrayList<>();
    public static List<BeanAboutUsEducationList> beanAboutUsEducationLists = new ArrayList<>();

    private static List<BeanFeedNews> bean_feed_List = new ArrayList<>();

    static AdapterProfilePost adapter_feed_news;
    String mobile, my_email_id, birth_date, gender, blood_grp;

    String backmanage = "";
    public static ProgressDialog pd;

    @BindView(R.id.tv_user_profile_name)
    TextView tv_user_profile_name;

    @BindView(R.id.lnfriends)
    LinearLayout lnfriends;

    @BindView(R.id.lnAds)
    LinearLayout lnAds;

    @BindView(R.id.ll_edit_cover_img)
    LinearLayout ll_edit_cover_img;

    @BindView(R.id.ll_edit_profile_img)
    LinearLayout ll_edit_profile_img;

    public Dialog about;
    String totalcolleagues;

    public static String department_name, city_name, branch_name, previouslyWorkedAt, jonining_date;

    public static String response, method = "POST";
    public static String str = "";

    ExpandableLayout expandable_overview, expandable_work, expandable_education;
    TextView tv_my_birthday, tv_gender, tv_blood_group, tv_my_email_id, tv_my_mobile_no;
    ArrayList<BeanPeopleDetail> beanPeopleDetails = new ArrayList<>();
    String peopleId;

    String loadprofileimage = "", loadprofilename = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);
        // backmanage = getIntent().getStringExtra("key");

        Bundle extra = getIntent().getExtras();
        peopleId = extra.getString("peopleId");
        rl_editcoverprofile.setFocusable(true);
        // profileImage = extra.getString("profileImage");
        //fullName = extra.getString("fullName");
        ll_edit_cover_img.setVisibility(GONE);
        ll_edit_profile_img.setVisibility(GONE);

        rv_profile_post = (RecyclerView) findViewById(R.id.rv_profile_post);
        tv_about_us.setTypeface(Utility.getTypeFace());
        tv_user_profile_name.setTypeface(Utility.getTypeFace());

        rv_collegues_friends.setHasFixedSize(true);
        GridLayoutManager gaggeredGridLayoutManager = new GridLayoutManager(activity, 3);
        rv_collegues_friends.setLayoutManager(gaggeredGridLayoutManager);
        rv_collegues_friends.setItemAnimator(new DefaultItemAnimator());

        rv_profile_post.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_profile_post.setLayoutManager(mLayoutManager);
        rv_profile_post.setItemAnimator(new DefaultItemAnimator());
        rv_profile_post.setNestedScrollingEnabled(false);

        lnabout.setOnClickListener(this);
        lnback.setOnClickListener(this);
        lnAds.setOnClickListener(this);
        see_all_friend.setOnClickListener(this);
        lnfriends.setOnClickListener(this);
        whatsyour.setOnClickListener(this);
        lnphoto.setOnClickListener(this);

        getProfileDetail(peopleId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnabout:
                aboutus();
                break;
            case R.id.lnback:

                if (backmanage == null) {
                    startActivity(new Intent(activity, MainActivity.class));
                    finish();
                } else if (backmanage.equalsIgnoreCase("PeopleFragment")) {
                    startActivity(new Intent(activity, PeopleActivity.class));
                    finish();
                } else if (backmanage.equalsIgnoreCase("market")) {
                    startActivity(new Intent(activity, MarketActivity.class));
                    finish();
                } else if (backmanage.equalsIgnoreCase("orgo")) {
                    startActivity(new Intent(activity, Organization_Chart.class));
                    finish();
                } else {
                    startActivity(new Intent(activity, MainActivity.class));
                    finish();
                }
                break;
            case R.id.lnAds:
                Intent i4 = new Intent(activity, MarketActivity.class);
                i4.putExtra("key", "profile");
                startActivity(i4);
                finish();
                break;
            case R.id.see_all_friend:/*
                if (!totalcolleagues.equalsIgnoreCase("0")) {
                    Intent i6 = new Intent(activity, Colleagues.class);
                    i6.putExtra("key", "profile");
                    i6.putParcelableArrayListExtra("Data", beanProfileColleguesFriendses_list);
                    //  i6.putExtra("arraylist",beanProfileColleguesFriendses_list);
                    startActivity(i6);
                    finish();
                }
                break;*/
            case R.id.lnfriends:
                if (!totalcolleagues.equalsIgnoreCase("0")) {
                    Intent i6 = new Intent(activity, OtherColleagues.class);
                    i6.putExtra("key", "profile");
                    i6.putExtra("peopleId", peopleId);
                    i6.putParcelableArrayListExtra("Data", beanProfileColleguesFriendses_list);
                    //  i6.putExtra("arraylist",beanProfileColleguesFriendses_list);
                    startActivity(i6);
                    finish();
                }
                break;
            case R.id.whatsyour:
                Intent i8 = new Intent(activity, OtherFeedPostActivity.class);
                i8.putExtra("key", "profile");
                i8.putExtra("people_id", peopleId);
                i8.putExtra("login_id", Utility.getPeopleIdPreference());
                i8.putExtra("people_name", loadprofilename);
                i8.putExtra("people_profile", loadprofileimage);
                startActivity(i8);
                finish();
                break;
            case R.id.lnphoto:
                Intent i7 = new Intent(activity, OtherPhotoActivity.class);
                i7.putExtra("key", "collegue_profile");
                i7.putExtra("people_id", peopleId);
                i7.putExtra("people_full_name", loadprofilename);
                startActivity(i7);
                break;
        }
    }

    public void popup(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popupshare, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView tv1 = (TextView) popupView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) popupView.findViewById(R.id.tv2);

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(20);
        popupWindow.setOutsideTouchable(true);
        //  popupWindow.showAsDropDown(v,Gravity.RIGHT, -50, 0);
        popupWindow.showAsDropDown(v);
    }

    @Override
    public void onBackPressed() {
        if (backmanage == null) {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        } else if (backmanage.equalsIgnoreCase("PeopleFragment")) {
            startActivity(new Intent(activity, PeopleActivity.class));
            finish();
        } else if (backmanage.equalsIgnoreCase("market")) {
            startActivity(new Intent(activity, MarketActivity.class));
            finish();
        } else if (backmanage.equalsIgnoreCase("orgo")) {
            startActivity(new Intent(activity, Organization_Chart.class));
            finish();
        } else {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }
    }

    private void aboutus() {
        about = new Dialog(this);
        about.requestWindowFeature(Window.FEATURE_NO_TITLE);
        about.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        about.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        about.setContentView(R.layout.user_profile);

        Window window = about.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        final LinearLayout lnback = (LinearLayout) about.findViewById(R.id.lnback);
        rv_about_us_work_list = (RecyclerView) about.findViewById(R.id.rv_about_us_work_list);
        rv_about_us_education = (RecyclerView) about.findViewById(R.id.rv_about_us_education);

        lndesignation = (LinearLayout) about.findViewById(R.id.lndesignation);
        lnprework = (LinearLayout) about.findViewById(R.id.lnprework);
        lnwith = (LinearLayout) about.findViewById(R.id.lnwith);
        lndepartment = (LinearLayout) about.findViewById(R.id.lndepartment);
        lnlivesin = (LinearLayout) about.findViewById(R.id.lnlivesin);

        rv_about_us_work_list.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_about_us_work_list.setLayoutManager(mLayoutManager);
        rv_about_us_work_list.setItemAnimator(new DefaultItemAnimator());

        rv_about_us_education.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(activity);
        rv_about_us_education.setLayoutManager(mLayoutManager2);


        TextView tv_add_work = (TextView) about.findViewById(R.id.tv_add_work);
        TextView tv_add_education = (TextView) about.findViewById(R.id.tv_add_education);

        tv_about_us_popup = (TextView) about.findViewById(R.id.tv_about_us_popup);
        tv_designation = (TextView) about.findViewById(R.id.tv_designation);
        TextView tv3 = (TextView) about.findViewById(R.id.tv3);
        tv_previously_work_at = (TextView) about.findViewById(R.id.tv_previously_work_at);
        TextView tv5 = (TextView) about.findViewById(R.id.tv5);
        tv_live_in = (TextView) about.findViewById(R.id.tv_live_in);
        TextView tv_edit_about_us = (TextView) about.findViewById(R.id.tv_edit_about_us);
        TextView tv7 = (TextView) about.findViewById(R.id.tv7);
        tv_with = (TextView) about.findViewById(R.id.tv8);
        TextView tv9 = (TextView) about.findViewById(R.id.tv9);
        TextView tv10 = (TextView) about.findViewById(R.id.tv10);
        TextView tv11 = (TextView) about.findViewById(R.id.tv11);
        TextView tv12 = (TextView) about.findViewById(R.id.tv12);
        TextView tv14 = (TextView) about.findViewById(R.id.tv14);
        TextView tv15 = (TextView) about.findViewById(R.id.tv15);
        TextView tv16 = (TextView) about.findViewById(R.id.tv16);
        TextView tv18 = (TextView) about.findViewById(R.id.tv18);
        TextView tv19 = (TextView) about.findViewById(R.id.tv19);
        TextView tv20 = (TextView) about.findViewById(R.id.tv20);
        TextView tv21 = (TextView) about.findViewById(R.id.tv21);
        TextView tv_edit_basic_info = (TextView) about.findViewById(R.id.tv_edit_basic_info);
        TextView tv_edit_contact_info = (TextView) about.findViewById(R.id.tv_edit_contact_info);
        tv_edit_contact_info.setVisibility(GONE);
        tv_edit_basic_info.setVisibility(GONE);


        tv_department = (TextView) about.findViewById(R.id.tv_department);

        tv_my_birthday = (TextView) about.findViewById(R.id.tv_my_birthday);
        tv_gender = (TextView) about.findViewById(R.id.tv_gender);
        tv_blood_group = (TextView) about.findViewById(R.id.tv_blood_group);
        tv_my_email_id = (TextView) about.findViewById(R.id.tv_my_email_id);
        tv_my_mobile_no = (TextView) about.findViewById(R.id.tv_my_mobile_no);
        tv_about_us_popup.setTypeface(Utility.getTypeFace());
        tv_designation.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv_previously_work_at.setTypeface(Utility.getTypeFace());

        tv5.setTypeface(Utility.getTypeFace());
        tv_live_in.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv_with.setTypeface(Utility.getTypeFace());
        tv15.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());
        tv10.setTypeface(Utility.getTypeFace());
        tv11.setTypeface(Utility.getTypeFace());
        tv12.setTypeface(Utility.getTypeFace());
        tv14.setTypeface(Utility.getTypeFace());
        tv16.setTypeface(Utility.getTypeFace());
        tv18.setTypeface(Utility.getTypeFace());
        tv19.setTypeface(Utility.getTypeFace());
        tv20.setTypeface(Utility.getTypeFace());
        tv_add_work.setTypeface(Utility.getTypeFace());
        tv_add_education.setTypeface(Utility.getTypeFace());
        tv_blood_group.setTypeface(Utility.getTypeFace());
        tv_gender.setTypeface(Utility.getTypeFace());
        tv_edit_contact_info.setTypeface(Utility.getTypeFace());
        tv_my_birthday.setTypeface(Utility.getTypeFace());
        tv_my_email_id.setTypeface(Utility.getTypeFace());
        tv_my_mobile_no.setTypeface(Utility.getTypeFace());
        tv_department.setTypeface(Utility.getTypeFace());

        tv_edit_about_us.setTypeface(Utility.getTypeFace());

        getAboutUsDetail(peopleId);
        getContactInformation(peopleId);
        getBasicInformation(peopleId);
        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about.dismiss();
            }
        });

        about.show();
    }

    void getProfileDetail(final String people_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Timeline_Detail_Select_By_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("prof_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Profile_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Profile_Detail_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    loadprofilename = jsonArray.optJSONObject(i).getString("FullName").toString();
                                    loadprofileimage = jsonArray.optJSONObject(i).getString("ProfileImage").toString();
                                    tv_about_title.setText("About " + loadprofilename);
                                    tv_user_profile_name.setText(loadprofilename);
                                    Glide.with(activity).load(loadprofileimage).into(iv_user_edit_profile);
                                    Glide.with(activity).load(loadprofileimage).into(iv_profile_img);
                                    share_txt.setText("Share an update on , " + loadprofilename);
                                    // Utility.setPeopleProfileImgPreference(jsonArray.optJSONObject(i).getString("ProfileImage").toString());
                                    pd.dismiss();
                                }
                            } else {
                                pd.dismiss();
                            }
                            pd.dismiss();
                        }
                        if (object.has("Basic_Info_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Basic_Info_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Glide.with(activity).load(jsonArray.optJSONObject(i).getString("BannerImage").toString()).asBitmap().into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                            Drawable drawable = new BitmapDrawable(resource);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                rl_editcoverprofile.setBackground(drawable);
                                            }
                                        }
                                    });
                                }
                            } else {
                                pd.dismiss();
                            }
                            pd.dismiss();
                        }

                        if (object.has("Overview_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Overview_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    tv_about_us.setText(jsonArray.optJSONObject(i).getString("About").toString());
                                    pd.dismiss();
                                }
                            } else {
                                pd.dismiss();
                            }
                            pd.dismiss();
                        }

                        if (object.has("Total_Colleagues_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Total_Colleagues_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    totalcolleagues = jsonArray.optJSONObject(i).getString("Totalcolleagues").toString();
                                    tv_no_of_collegaues.setText("(" + totalcolleagues + " People)");
                                    pd.dismiss();
                                }
                            } else {
                                pd.dismiss();
                            }
                            pd.dismiss();
                        }
                        if (object.has("Contact_List_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Contact_List_Array");
                            if (jsonArray.length() != 0) {
                            //    ArrayList<BeanProfileColleguesFriends> colleguesFriendses = new ArrayList<>();

                                beanProfileColleguesFriendses_list.clear();
                                beanProfileColleguesFriendses_list.addAll((Collection<? extends BeanProfileColleguesFriends>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanProfileColleguesFriends>>() {
                                }.getType()));

                                adapterProfileColleguesFriends = new AdapterProfileColleguesFriends(beanProfileColleguesFriendses_list, activity);
                                rv_collegues_friends.setAdapter(adapterProfileColleguesFriends);
/*
                                if (beanProfileColleguesFriendses_list.size() > 6) {

                                    for (int j = 0; j < 7; j++) {
                                        colleguesFriendses.add(new BeanProfileColleguesFriends(beanProfileColleguesFriendses_list.get(j).contactId, beanProfileColleguesFriendses_list.get(j).fullName, beanProfileColleguesFriendses_list.get(j).profileImage));
                                    }
                                    adapterProfileColleguesFriends = new AdapterProfileColleguesFriends(beanProfileColleguesFriendses_list, activity);
                                    rv_collegues_friends.setAdapter(adapterProfileColleguesFriends);
                                } else {
                                    for (int j = 0; j < beanProfileColleguesFriendses_list.size(); j++) {
                                        colleguesFriendses.add(new BeanProfileColleguesFriends(beanProfileColleguesFriendses_list.get(j).contactId, beanProfileColleguesFriendses_list.get(j).fullName, beanProfileColleguesFriendses_list.get(j).profileImage));
                                    }
                                    adapterProfileColleguesFriends = new AdapterProfileColleguesFriends(colleguesFriendses, activity);
                                    rv_collegues_friends.setAdapter(adapterProfileColleguesFriends);
                                }*/


                                pd.dismiss();
                            } else {
                                rv_collegues_friends.setAdapter(null);
                                pd.dismiss();
                            }
                        }
                        if (object.has("Post_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Post_Array");
                            if (jsonArray.length() != 0) {
                                bean_feed_List.clear();
                                bean_feed_List.addAll((Collection<? extends BeanFeedNews>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFeedNews>>() {
                                }.getType()));
                                adapter_feed_news = new AdapterProfilePost(activity, bean_feed_List);
                                rv_profile_post.setAdapter(adapter_feed_news);
                                pd.dismiss();
                            } else {
                                rv_profile_post.setAdapter(null);
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
                    map.put("PeopleId", people_id);
                    map.put("LoginId", Utility.getPeopleIdPreference());

                    Log.e("Hashkey", "" + Utility.getHashKeyPreference());
                    Log.e("PeopleId", "" + people_id);
                    Log.e("LoginId", "" + Utility.getPeopleIdPreference());

                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd.dismiss();

        }
    }

    public static void getAboutUsDetail(final String people_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.About_Select_By_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("aboutus_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("About_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("About_Detail_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    tv_about_us_popup.setText("About " + jsonArray.optJSONObject(i).getString("FullName").toString());

                                    if (jsonArray.optJSONObject(i).getString("DepartmentName").toString() == null || jsonArray.optJSONObject(i).getString("DepartmentName").toString().equalsIgnoreCase("")) {
                                        lndepartment.setVisibility(GONE);
                                    } else {
                                        tv_department.setText(jsonArray.optJSONObject(i).getString("DepartmentName").toString());
                                    }

                                    if (jsonArray.optJSONObject(i).getString("DesignationName").toString() == null || jsonArray.optJSONObject(i).getString("DesignationName").toString().equalsIgnoreCase("")) {
                                        lndesignation.setVisibility(GONE);
                                    } else {
                                        tv_designation.setText(jsonArray.optJSONObject(i).getString("DesignationName").toString());
                                    }

                                    if (jsonArray.optJSONObject(i).getString("CityName").toString() == null || jsonArray.optJSONObject(i).getString("CityName").toString().equalsIgnoreCase("")) {
                                        lnlivesin.setVisibility(GONE);
                                    } else {
                                        tv_live_in.setText(jsonArray.optJSONObject(i).getString("CityName").toString());
                                    }

                                    if (jsonArray.optJSONObject(i).getString("BranchName").toString() == null || jsonArray.optJSONObject(i).getString("BranchName").toString().equalsIgnoreCase("")) {
                                        lnwith.setVisibility(GONE);
                                    } else {
                                        tv_with.setText(jsonArray.optJSONObject(i).getString("BranchName").toString());
                                    }

                                    city_name = jsonArray.optJSONObject(i).getString("CityName").toString();
                                    department_name = jsonArray.optJSONObject(i).getString("DepartmentName").toString();
                                    // tv_designation.setText(jsonArray.optJSONObject(i).getString("DepartmentName").toString());
                                    //tv_designation.setText(jsonArray.optJSONObject(i).getString("DepartmentName").toString());
                                    //Glide.with(activity).load(jsonArray.optJSONObject(i).getString("ProfileImage").toString()).into(iv_user_edit_profile);
                                    pd.dismiss();
                                }
                            } else {
                                pd.dismiss();
                            }
                            pd.dismiss();
                        }
                        if (object.has("About_Other_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("About_Other_Detail_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (jsonArray.optJSONObject(i).getString("PreviouslyWorkedAt").toString() == null || jsonArray.optJSONObject(i).getString("PreviouslyWorkedAt").toString().equalsIgnoreCase("")) {
                                        lnprework.setVisibility(GONE);
                                    } else {
                                        tv_previously_work_at.setText(jsonArray.optJSONObject(i).getString("PreviouslyWorkedAt").toString());
                                    }

                                    previouslyWorkedAt = jsonArray.optJSONObject(i).getString("PreviouslyWorkedAt").toString();
                                    jonining_date = jsonArray.optJSONObject(i).getString("JoinBy").toString();
                                    //  tv_about_us_popup.setText( jsonArray.optJSONObject(i).getString("JoinBy").toString());

                                }
                            } else {
                                lnprework.setVisibility(GONE);
                                pd.dismiss();
                            }
                            pd.dismiss();
                        }


                        if (object.has("Work_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Work_Detail_Array");
                            if (jsonArray.length() != 0) {
                                beanAboutUsWorkDetailLists_list.clear();
                                beanAboutUsWorkDetailLists_list.addAll((Collection<? extends BeanAboutUsWorkDetailList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAboutUsWorkDetailList>>() {
                                }.getType()));
                                adapterAboutUsWorkList = new AdapterAboutUsWorkList(beanAboutUsWorkDetailLists_list, activity);
                                rv_about_us_work_list.setAdapter(adapterAboutUsWorkList);
                                pd.dismiss();
                            } else {
                                rv_about_us_work_list.setAdapter(null);
                                pd.dismiss();
                            }
                        }


                        if (object.has("Education_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Education_Detail_Array");
                            if (jsonArray.length() != 0) {
                                beanAboutUsEducationLists.clear();
                                beanAboutUsEducationLists.addAll((Collection<? extends BeanAboutUsEducationList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAboutUsEducationList>>() {
                                }.getType()));
                                adapterAboutUsEducationList = new AdapterAboutUsEducationList(beanAboutUsEducationLists, activity);
                                rv_about_us_education.setAdapter(adapterAboutUsEducationList);
                                pd.dismiss();
                            } else {
                                rv_about_us_education.setAdapter(null);
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
                    map.put("PeopleId", people_id);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd.dismiss();

        }
    }

    private void getBasicInformation(final String people_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Basic_Information_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("like_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Basic_Information_Array")) {
                            JSONArray array = object.getJSONArray("Basic_Information_Array");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    array.optJSONObject(i).getString("PeopleId");
                                    array.optJSONObject(i).getString("InfoId");

                                    String date = null;
                                    try {
                                        date = EventAddActivity.dateFormatter(array.optJSONObject(i).getString("BirthDate"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    tv_my_birthday.setText(date);
                                    birth_date = date;


                                    if (array.optJSONObject(i).getString("Gender").equalsIgnoreCase("M")) {
                                        tv_gender.setText("Male");
                                        gender = "Male";
                                    } else if (array.optJSONObject(i).getString("Gender").equalsIgnoreCase("F")) {
                                        tv_gender.setText("Female");
                                        gender = "Female";
                                    } else {
                                        tv_gender.setText("");
                                    }

                                    tv_blood_group.setText(array.optJSONObject(i).getString("BloodGroup"));
                                    blood_grp = array.optJSONObject(i).getString("BloodGroup");

                                }
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }

                    } catch (JSONException e) {
                        pd.dismiss();
                        showMsg(R.string.json_error);
                        // pd2.dismiss();e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("like_erro", error.toString());
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
                    map.put("PeopleId", people_id);//please check People id =who's profile you are open.

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

    private void getContactInformation(final String people_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_Information_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("like_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Contact_Information_Array")) {
                            JSONArray array = object.getJSONArray("Contact_Information_Array");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    array.optJSONObject(i).getString("PeopleId");
                                    array.optJSONObject(i).getString("InfoId");
                                    InfoId = array.optJSONObject(i).getString("InfoId");
                                    if (!array.optJSONObject(i).getString("MobileNo").equalsIgnoreCase("")) {
                                        tv_my_mobile_no.setText(array.optJSONObject(i).getString("MobileNo"));
                                        mobile = array.optJSONObject(i).getString("MobileNo");
                                    }
                                    if (!array.optJSONObject(i).getString("EmailAddress").equalsIgnoreCase("")) {
                                        tv_my_email_id.setText(array.optJSONObject(i).getString("EmailAddress"));
                                        my_email_id = array.optJSONObject(i).getString("EmailAddress");
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
                        // pd2.dismiss();e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("like_erro", error.toString());
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
                    map.put("PeopleId", people_id);//please check People id =who's profile you are open.
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
