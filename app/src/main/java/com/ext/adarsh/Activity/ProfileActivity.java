package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.ext.adarsh.Adapter.AdapterAboutUsEducationList;
import com.ext.adarsh.Adapter.AdapterAboutUsWorkList;
import com.ext.adarsh.Adapter.AdapterProfileColleguesFriends;
import com.ext.adarsh.Adapter.AdapterProfilePost;
import com.ext.adarsh.Bean.BeanAboutUsEducationList;
import com.ext.adarsh.Bean.BeanAboutUsWorkDetailList;
import com.ext.adarsh.Bean.BeanFeedNews;
import com.ext.adarsh.Bean.BeanProfileColleguesFriends;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.ServiceHandler;
import com.ext.adarsh.Utils.Utility;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity activity;
    @BindView(R.id.rl_editcoverprofile)
    RelativeLayout rl_editcoverprofile;

    @BindView(R.id.whatsyour)
    LinearLayout whatsyour;

    @BindView(R.id.imgln)
    RelativeLayout imgln;

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

    TextView et_start_date;
    TextView et_end_date;

    String image_id, InfoId;

    @BindView(R.id.lnabout)
    LinearLayout lnabout;
    TextView et_birth_date;

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

    @BindView(R.id.ll_edit_profile_img)
    LinearLayout ll_edit_profile_img;

    @BindView(R.id.ll_edit_cover_img)
    LinearLayout ll_edit_cover_img;

    public Dialog commentDialog, about, whats, add_work, add_education, editContactInfo, editBasicInfo;
    String totalcolleagues;

    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    FileOutputStream fo;
    String profileimg;
    String public_or_pricate;
    String imagename;
    boolean profil_update_status;
    Dialog open_dialog_edit_aboutus;

    public static String department_name, city_name, branch_name, previouslyWorkedAt, jonining_date;

    public static String response, method = "POST";
    public static String str = "";
    String gFlag = "";


    EditText et_company_name, et_possion, et_company_place;
    TextView tv_my_birthday, tv_gender, tv_blood_group, tv_my_email_id, tv_my_mobile_no;
    EditText et_institute_name, et_year, et_degree, et_place;
    EditText et_blood_group, et_email_id, et_mobile_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);
        backmanage = getIntent().getStringExtra("key");
        rv_profile_post = (RecyclerView) findViewById(R.id.rv_profile_post);

        setFonat();
        Glide.with(activity).load(Utility.getPeopleProfileImgPreference()).into(iv_profile_img);
        share_txt.setText("Share an update, " + Utility.getFullNamePreference());
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
        ll_edit_profile_img.setOnClickListener(this);
        ll_edit_cover_img.setOnClickListener(this);

        getProfileDetail();
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
                i4.putExtra("id","0");
                startActivity(i4);
                finish();
                break;
            case R.id.see_all_friend:
                if (!totalcolleagues.equalsIgnoreCase("0")) {
                    Intent i6 = new Intent(activity, Colleagues.class);
                    i6.putExtra("key", "profile");
                    i6.putParcelableArrayListExtra("Data", beanProfileColleguesFriendses_list);
                    startActivity(i6);
                    finish();
                }
                break;
            case R.id.lnfriends:
                if (!totalcolleagues.equalsIgnoreCase("0")) {
                    Intent i6 = new Intent(activity, Colleagues.class);
                    i6.putExtra("key", "profile");
                    i6.putParcelableArrayListExtra("Data", beanProfileColleguesFriendses_list);
                    startActivity(i6);
                    finish();
                }
                break;
            case R.id.whatsyour:
                Intent i8 = new Intent(activity, FeedPostActivity.class);
                i8.putExtra("key", "profile");
                i8.putExtra("people_id", Utility.getPeopleIdPreference());
                i8.putExtra("login_id", Utility.getPeopleIdPreference());
                i8.putExtra("people_name", Utility.getFullNamePreference());
                i8.putExtra("people_profile", Utility.getPeopleProfileImgPreference());
                startActivity(i8);
                finish();

                break;
            case R.id.lnphoto:
                Intent i7 = new Intent(activity, PhotoActivity.class);
                i7.putExtra("key", "profile");
                startActivity(i7);
                finish();
                break;
            case R.id.ll_edit_profile_img:
                image_id = "1";
                selectImage();
                break;
            case R.id.ll_edit_cover_img:
                image_id = "2";
                selectImage();
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    //region Select Gallery or Camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    //endregion

    //region Gallery
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
            } catch (IOException e) {
                pd.dismiss();
                showMsg(R.string.json_error);
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] imageBytes = bytes.toByteArray();
        profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        File destination = new File(Environment.getExternalStorageDirectory() + "/Intranet/UserProfile");

        if (!destination.exists()) {
            File wallpaperDirectory = new File("/sdcard/Intranet/UserProfile");
            wallpaperDirectory.mkdirs();
        }

        imagename = "intranet" + System.currentTimeMillis();
        File file = new File(new File("/sdcard/Intranet/UserProfile"), imagename);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image_id.equals("1")) {
            updateProfileImage(imagename, profileimg);
        } else {
            updateCoverImage(imagename, profileimg);
        }


    }


    //endregion

    //region Camera
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] imageBytes = bytes.toByteArray();
        profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        File destination = new File(Environment.getExternalStorageDirectory() + "/Intranet/UserProfile");

        if (!destination.exists()) {
            File wallpaperDirectory = new File("/sdcard/Intranet/UserProfile");
            wallpaperDirectory.mkdirs();
        }

        imagename = "intranet" + System.currentTimeMillis();
        File file = new File(new File("/sdcard/Intranet/UserProfile"), imagename);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image_id.equals("1")) {
            updateProfileImage(imagename, profileimg);
        } else {
            updateCoverImage(imagename, profileimg);
        }
    }

    void getProfileDetail() {
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
                                    tv_about_title.setText("About " + jsonArray.optJSONObject(i).getString("FullName").toString());
                                    tv_user_profile_name.setText(jsonArray.optJSONObject(i).getString("FullName").toString());
                                    Glide.with(activity).load(jsonArray.optJSONObject(i).getString("ProfileImage").toString()).into(iv_user_edit_profile);
                                    Utility.setPeopleProfileImgPreference(jsonArray.optJSONObject(i).getString("ProfileImage").toString());
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
                                beanProfileColleguesFriendses_list.clear();
                                beanProfileColleguesFriendses_list.addAll((Collection<? extends BeanProfileColleguesFriends>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanProfileColleguesFriends>>() {
                                }.getType()));

                                adapterProfileColleguesFriends = new AdapterProfileColleguesFriends(beanProfileColleguesFriendses_list, activity);
                                rv_collegues_friends.setAdapter(adapterProfileColleguesFriends);
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
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
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
                        adapter_feed_news = new AdapterProfilePost(activity, bean_feed_List);
                        rv_profile_post.setAdapter(adapter_feed_news);
                        pd.dismiss();
                    }
                }
                /*if (object.has("Post_Array")) {
                    JSONArray jsonArray = object.getJSONArray("Post_Array");

                    if (jsonArray.length() != 0) {
                        bean_feed_List.clear();
                        bean_feed_List.addAll((Collection<? extends com.ext.adarsh.Adapter.BeanFeedNews>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<com.ext.adarsh.Adapter.BeanFeedNews>>() {
                        }.getType()));
                        adapter_feed_news = new AdapterFeedNews(activity, bean_feed_List);
                        adapter_feed_news.notifyDataSetChanged();
                        rv_news_feed.setAdapter(adapter_feed_news);
                        adapter_feed_news.notifyDataSetChanged();
                    }
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        LinearLayout ll_add_work = (LinearLayout) about.findViewById(R.id.ll_add_work);
        LinearLayout ll_add_education = (LinearLayout) about.findViewById(R.id.ll_add_education);
        final LinearLayout lnback = (LinearLayout) about.findViewById(R.id.lnback);
        LinearLayout ll_edit_basic_info = (LinearLayout) about.findViewById(R.id.ll_edit_basic_info);
        LinearLayout ll_edit_contact_info = (LinearLayout) about.findViewById(R.id.ll_edit_contact_info);
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
//tv_add_work , tv_add_education
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
        tv_add_education.setTypeface(Utility.getTypeFace());
        tv_add_work.setTypeface(Utility.getTypeFace());
        tv_blood_group.setTypeface(Utility.getTypeFace());
        tv_gender.setTypeface(Utility.getTypeFace());
        tv_edit_contact_info.setTypeface(Utility.getTypeFace());
        tv_my_birthday.setTypeface(Utility.getTypeFace());
        tv_my_email_id.setTypeface(Utility.getTypeFace());
        tv_my_mobile_no.setTypeface(Utility.getTypeFace());
        tv_department.setTypeface(Utility.getTypeFace());

        tv_edit_about_us.setTypeface(Utility.getTypeFace());

        getAboutUsDetail();
        getContactInformation();
        getBasicInformation();

        /*lnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandable_overview.isExpanded()) {
                    ll_down_arrow1.setVisibility(View.GONE);
                    ll_forward_arrow1.setVisibility(View.VISIBLE);
                    expandable_overview.collapse();
                } else {
                    ll_down_arrow1.setVisibility(View.VISIBLE);
                    ll_forward_arrow1.setVisibility(View.GONE);
                    expandable_overview.expand();
                }
            }
        });
        lnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandable_work.isExpanded()) {
                    ll_down_arrow2.setVisibility(View.GONE);
                    ll_forward_arrow2.setVisibility(View.VISIBLE);
                    expandable_work.collapse();
                } else {
                    ll_down_arrow2.setVisibility(View.VISIBLE);
                    ll_forward_arrow2.setVisibility(View.GONE);
                    expandable_work.expand();
                }
            }
        });
        lnEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandable_education.isExpanded()) {
                    ll_down_arrow3.setVisibility(View.GONE);
                    ll_forward_arrow3.setVisibility(View.VISIBLE);
                    expandable_education.collapse();
                } else {
                    ll_down_arrow3.setVisibility(View.VISIBLE);
                    ll_forward_arrow3.setVisibility(View.GONE);
                    expandable_education.expand();
                }
            }
        });*/
        ll_edit_contact_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContactInfo();
            }
        });

        ll_edit_basic_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBasicInfo();
            }
        });

        ll_add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWork();
            }
        });
        ll_add_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Education();
            }
        });

        tv_edit_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAboutUs();
            }
        });
        getAboutUsDetail();

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about.dismiss();
            }
        });

        about.show();
    }

    private void AddWork() {
        add_work = new Dialog(this);
        add_work.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_work.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        add_work.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        add_work.setContentView(R.layout.popup_aboutus_edit_work);

        TextView tv_add_work_heading = (TextView) add_work.findViewById(R.id.tv_add_work_heading);
        TextView tv_edit_work_heading = (TextView) add_work.findViewById(R.id.tv_edit_work_heading);
        RelativeLayout rl_date1 = (RelativeLayout) add_work.findViewById(R.id.rl_date1);
        RelativeLayout rl_date2 = (RelativeLayout) add_work.findViewById(R.id.rl_date2);

        TextView tv_add_work = (TextView) add_work.findViewById(R.id.tv_add_work);
        TextView tv_save_work = (TextView) add_work.findViewById(R.id.tv_save_work);
        TextView tv12 = (TextView) add_work.findViewById(R.id.tv12);


        tv_add_work_heading.setTypeface(Utility.getTypeFace());
        tv_edit_work_heading.setTypeface(Utility.getTypeFace());

        tv_add_work.setTypeface(Utility.getTypeFaceTab());
        tv_save_work.setTypeface(Utility.getTypeFaceTab());
        tv12.setTypeface(Utility.getTypeFaceTab());

        LinearLayout ll_add_header = (LinearLayout) add_work.findViewById(R.id.ll_add_header);
        LinearLayout lnmainback = (LinearLayout) add_work.findViewById(R.id.lnmainback);
        LinearLayout ll_edit_header = (LinearLayout) add_work.findViewById(R.id.ll_edit_header);
        LinearLayout ll_add_work = (LinearLayout) add_work.findViewById(R.id.ll_add_work);
        LinearLayout ll_save_work = (LinearLayout) add_work.findViewById(R.id.ll_save_work);

        ll_add_header.setVisibility(View.VISIBLE);
        ll_edit_header.setVisibility(GONE);
        ll_save_work.setVisibility(GONE);
        ll_add_work.setVisibility(View.VISIBLE);

        et_company_name = (EditText) add_work.findViewById(R.id.et_company_name);
        et_possion = (EditText) add_work.findViewById(R.id.et_possion);
        et_company_place = (EditText) add_work.findViewById(R.id.et_company_place);
        et_start_date = (TextView) add_work.findViewById(R.id.et_start_date);
        et_end_date = (TextView) add_work.findViewById(R.id.et_end_date);

        et_company_name.addTextChangedListener(new MyTextWatcher(et_company_name));
        et_possion.addTextChangedListener(new MyTextWatcher(et_possion));
        et_company_place.addTextChangedListener(new MyTextWatcher(et_company_place));

       /* et_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date();
            }
        });
        et_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_date();
            }
        });*/


        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_work.dismiss();
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


        ll_add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    Toast.makeText(getApplicationContext(), "Enter Start Date", Toast.LENGTH_LONG).show();
                    //requestFocus(et_start_date);
                } else if (end_date.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter End Date", Toast.LENGTH_LONG).show();
                   /* et_end_date.setError("Enter End Date");
                    requestFocus(et_end_date);*/
                } else if (company_place.equalsIgnoreCase("")) {
                    et_company_place.setError("Enter Company Place");
                    requestFocus(et_company_place);
                } else {
                    addWork(company_name, position, company_place, start_date, end_date);
                }
            }
        });

        Window window = add_work.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        add_work.show();
    }

    public void addWork(final String company_name, final String position,
                        final String company_place, final String start_date,
                        final String end_date) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Work_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("work_add", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                        }
                        if (object.has("Work_Add")) {
                            JSONArray array = object.getJSONArray("Work_Add");

                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        ProfileActivity.getAboutUsDetail();
                                        add_work.dismiss();
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

                            pd.dismiss();
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
                    map.put("CompanyName", company_name);
                    map.put("Position", position);
                    map.put("StartDate", start_date);
                    map.put("EndDate", end_date);
                    map.put("CompanyPlace", company_place);


                    Log.e("PeopleId", "" + Utility.getPeopleIdPreference());
                    Log.e("CompanyName", "" + company_name);
                    Log.e("Position", "" + position);
                    Log.e("StartDate", "" + start_date);
                    Log.e("EndDate", "" + end_date);
                    Log.e("CompanyPlace", "" + company_place);

                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd.dismiss();

        }
    }

    public void editAboutUs() {

        open_dialog_edit_aboutus = new Dialog(activity);
        open_dialog_edit_aboutus.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_dialog_edit_aboutus.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_dialog_edit_aboutus.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_dialog_edit_aboutus.setContentView(R.layout.popup_aboutus_edit);

        Window window = open_dialog_edit_aboutus.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnmainback = (LinearLayout) open_dialog_edit_aboutus.findViewById(R.id.lnmainback);
        LinearLayout ll_save_work = (LinearLayout) open_dialog_edit_aboutus.findViewById(R.id.ll_save_work);
        LinearLayout ll_work_cancel = (LinearLayout) open_dialog_edit_aboutus.findViewById(R.id.ll_work_cancel);

        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_dialog_edit_aboutus.dismiss();
            }
        });

        ll_save_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ll_work_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_dialog_edit_aboutus.dismiss();
            }
        });

        TextView tv_join_date = (TextView) open_dialog_edit_aboutus.findViewById(R.id.tv_join_date);
        TextView tv_livein = (TextView) open_dialog_edit_aboutus.findViewById(R.id.tv_livein);
        TextView tv_preves_com = (TextView) open_dialog_edit_aboutus.findViewById(R.id.tv_preves_com);
        TextView tv_designation = (TextView) open_dialog_edit_aboutus.findViewById(R.id.tv_designation);

        EditText et_designation = (EditText) open_dialog_edit_aboutus.findViewById(R.id.et_designation);
        EditText et_preves_com = (EditText) open_dialog_edit_aboutus.findViewById(R.id.et_preves_com);
        EditText et_livein = (EditText) open_dialog_edit_aboutus.findViewById(R.id.et_livein);
        EditText et_join_date = (EditText) open_dialog_edit_aboutus.findViewById(R.id.et_join_date);

        TextView tv_next1 = (TextView) open_dialog_edit_aboutus.findViewById(R.id.tv_next1);
        TextView tv12 = (TextView) open_dialog_edit_aboutus.findViewById(R.id.tv12);

        tv_join_date.setTypeface(Utility.getTypeFace());
        tv_livein.setTypeface(Utility.getTypeFace());
        tv_preves_com.setTypeface(Utility.getTypeFace());
        tv_designation.setTypeface(Utility.getTypeFace());

        tv_next1.setTypeface(Utility.getTypeFaceTab());
        tv12.setTypeface(Utility.getTypeFaceTab());

        et_designation.setTypeface(Utility.getTypeFace());
        et_preves_com.setTypeface(Utility.getTypeFace());
        et_livein.setTypeface(Utility.getTypeFace());
        et_join_date.setTypeface(Utility.getTypeFace());

        et_designation.setText(department_name);
        et_preves_com.setText(previouslyWorkedAt);
        et_livein.setText(city_name);
        et_join_date.setText(jonining_date);

        open_dialog_edit_aboutus.show();
    }

    public void setFonat() {
        tv_about_us.setTypeface(Utility.getTypeFace());
        tv_user_profile_name.setTypeface(Utility.getTypeFace());
    }

    public static void getAboutUsDetail() {
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

    void updateProfileImage(final String file_name, final String file_string) {

        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Profile_Image_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("like_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Profile_Image_Update")) {
                            JSONArray array = object.getJSONArray("Profile_Image_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        profil_update_status = true;
                                        pd.dismiss();
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


                        if (object.has("Profile_Image_Array")) {
                            JSONArray array = object.getJSONArray("Profile_Image_Array");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (profil_update_status) {
                                        Glide.with(activity).load(array.optJSONObject(i).getString("ProfileImage")).into(iv_user_edit_profile);
                                        Utility.setPeopleProfileImgPreference(array.optJSONObject(i).getString("ProfileImage").toString());

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
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("FilePathString", file_string);
                    Log.e("FilePathString", "+++++++++++++" + file_string);
                    map.put("FileName", file_name);

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

    void updateCoverImage(final String file_name, final String file_string) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Cover_Image_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("like_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Cover_Image_Update")) {
                            JSONArray array = object.getJSONArray("Cover_Image_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        profil_update_status = true;
                                        pd.dismiss();

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


                        if (object.has("Cover_Image_Array")) {
                            JSONArray array = object.getJSONArray("Cover_Image_Array");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (profil_update_status) {
                                        Glide.with(activity).load(array.optJSONObject(i).getString("CoverImage").toString()).asBitmap().into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                Drawable drawable = new BitmapDrawable(resource);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                    rl_editcoverprofile.setBackground(drawable);
                                                }
                                            }
                                        });
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
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("FilePathString", file_string);
                    Log.e("FilePathString", file_string);
                    map.put("FileName", file_name);

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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                case R.id.et_blood_group:
                    validateBloodGroup();
                    break;
                case R.id.et_email_id:
                    validateEmail();
                    break;
                case R.id.et_mobile_number:
                    validatemobile();
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

    private void getBasicInformation() {
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
                    map.put("PeopleId", Utility.getPeopleIdPreference());//please check People id =who's profile you are open.

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

    private void getContactInformation() {
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
                    map.put("PeopleId", Utility.getPeopleIdPreference());//please check People id =who's profile you are open.

                    Log.e("Hashkey", Utility.getHashKeyPreference());
                    Log.e("LoginId", Utility.getPeopleIdPreference());
                    Log.e("PeopleId", Utility.getPeopleIdPreference());
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

    void Add_Education() {
        add_education = new Dialog(activity);
        add_education.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_education.getWindow().setWindowAnimations(R.style.DialogAnimation);
        add_education.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        add_education.setContentView(R.layout.popup_aboutus_edit_education);

        Window window = add_education.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnmainback = (LinearLayout) add_education.findViewById(R.id.lnmainback);
        LinearLayout ll_save_work = (LinearLayout) add_education.findViewById(R.id.ll_save_work);
        LinearLayout ll_work_cancel = (LinearLayout) add_education.findViewById(R.id.ll_work_cancel);
        LinearLayout ll_add_header = (LinearLayout) add_education.findViewById(R.id.ll_add_header);
        LinearLayout ll_edit_header = (LinearLayout) add_education.findViewById(R.id.ll_edit_header);
        TextView tv_reg_heading = (TextView) add_education.findViewById(R.id.tv_reg_heading);
        tv_reg_heading.setText("Add Educational Details");

        ll_add_header.setVisibility(View.VISIBLE);
        ll_edit_header.setVisibility(GONE);

        TextView tv_institute_name = (TextView) add_education.findViewById(R.id.tv_institute_name);
        TextView tv_place = (TextView) add_education.findViewById(R.id.tv_place);
        TextView tv_degree = (TextView) add_education.findViewById(R.id.tv_degree);
        TextView tv_year = (TextView) add_education.findViewById(R.id.tv_year);

        TextView tv_next1 = (TextView) add_education.findViewById(R.id.tv_next1);
        TextView tv12 = (TextView) add_education.findViewById(R.id.tv12);

        et_institute_name = (EditText) add_education.findViewById(R.id.et_institute_name);
        et_year = (EditText) add_education.findViewById(R.id.et_year);
        et_degree = (EditText) add_education.findViewById(R.id.et_degree);
        et_place = (EditText) add_education.findViewById(R.id.et_place);

       /* ll_edit_header.setVisibility(View.GONE);
        ll_add_header.setVisibility(View.VISIBLE);*/

        tv_next1.setTypeface(Utility.getTypeFaceTab());
        tv12.setTypeface(Utility.getTypeFaceTab());

        tv_institute_name.setTypeface(Utility.getTypeFace());
        tv_place.setTypeface(Utility.getTypeFace());
        tv_degree.setTypeface(Utility.getTypeFace());
        tv_year.setTypeface(Utility.getTypeFace());


        et_institute_name.addTextChangedListener(new MyTextWatcher(et_institute_name));
        et_year.addTextChangedListener(new MyTextWatcher(et_year));
        et_degree.addTextChangedListener(new MyTextWatcher(et_degree));
        et_place.addTextChangedListener(new MyTextWatcher(et_place));


        et_institute_name.setTypeface(Utility.getTypeFace());
        et_year.setTypeface(Utility.getTypeFace());
        et_degree.setTypeface(Utility.getTypeFace());
        et_place.setTypeface(Utility.getTypeFace());

        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_education.dismiss();
            }
        });
        ll_save_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String institute_name = et_institute_name.getText().toString();
                String year = et_year.getText().toString();
                String degree = et_degree.getText().toString();
                String place = et_place.getText().toString();

                if (institute_name.equalsIgnoreCase("")) {
                    et_institute_name.setError("Enter Institute Name");
                    requestFocus(et_institute_name);
                } else if (year.equalsIgnoreCase("")) {
                    et_year.setError("Enter year");
                    requestFocus(et_year);
                } else if (degree.equalsIgnoreCase("")) {
                    et_degree.setError("Enter Degree");
                    requestFocus(et_degree);
                } else if (place.equalsIgnoreCase("")) {
                    et_place.setError("Enter Place");
                    requestFocus(et_place);
                } else {
                    education_add(institute_name, year, degree, place);
                }
            }
        });

        ll_work_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_education.dismiss();
            }
        });

        add_education.show();
    }

    void education_add(final String clg_name, final String pass_year,
                       final String degree, final String place) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Education_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Education_Add")) {
                            JSONArray array = object.getJSONArray("Education_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        ProfileActivity.getAboutUsDetail();
                                        add_education.dismiss();
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


    public void editContactInfo() {
        editContactInfo = new Dialog(activity);
        editContactInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editContactInfo.getWindow().setWindowAnimations(R.style.DialogAnimation);
        editContactInfo.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        editContactInfo.setContentView(R.layout.popup_contact_info_edit);

        Window window = editContactInfo.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnmainback = (LinearLayout) editContactInfo.findViewById(R.id.lnmainback);
        LinearLayout ll_save_contact_info = (LinearLayout) editContactInfo.findViewById(R.id.ll_save_contact_info);
        LinearLayout ll_contact_info_cancel = (LinearLayout) editContactInfo.findViewById(R.id.ll_contact_info_cancel);

        et_mobile_number = (EditText) editContactInfo.findViewById(R.id.et_mobile_number);
        et_email_id = (EditText) editContactInfo.findViewById(R.id.et_email_id);

        TextView tv_next1 = (TextView) editContactInfo.findViewById(R.id.tv_next1);
        TextView tv_cancel = (TextView) editContactInfo.findViewById(R.id.tv_cancel);

        tv_next1.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        et_mobile_number.setTypeface(Utility.getTypeFace());
        et_email_id.setTypeface(Utility.getTypeFace());

        et_email_id.addTextChangedListener(new MyTextWatcher(et_email_id));
        et_mobile_number.addTextChangedListener(new MyTextWatcher(et_mobile_number));


        et_mobile_number.setText(mobile);
        et_email_id.setText(my_email_id);

        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editContactInfo.dismiss();
            }
        });

        ll_save_contact_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_mobile_number.getText().toString().isEmpty()) {
                    et_mobile_number.setError("Enter Mobile Number");
                    requestFocus(et_mobile_number);
                } else if (et_email_id.getText().toString().isEmpty()) {
                    et_email_id.setError("Enter Email");
                    requestFocus(et_email_id);
                } else {
                    SaveContactInfo(et_mobile_number.getText().toString(), et_email_id.getText().toString());
                }
            }
        });

        ll_contact_info_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editContactInfo.dismiss();
            }
        });
        editContactInfo.show();
    }

    private void SaveContactInfo(final String mobile_number, final String Email_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_Information_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Contact_Information_Update")) {
                            JSONArray array = object.getJSONArray("Contact_Information_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        getContactInformation();
                                        editContactInfo.dismiss();
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
                        // pd2.dismiss();e.printStackTrace();
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
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("EmailId", Email_id);
                    map.put("MobileNumber", mobile_number);
                    map.put("InfoId", InfoId);

                    Log.e("LoginId", Utility.getPeopleIdPreference());
                    Log.e("EmailId", Email_id);
                    Log.e("MobileNumber", mobile_number);
                    Log.e("InfoId", InfoId);

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

    public void editBasicInfo() {
        editBasicInfo = new Dialog(activity);
        editBasicInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editBasicInfo.getWindow().setWindowAnimations(R.style.DialogAnimation);
        editBasicInfo.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        editBasicInfo.setContentView(R.layout.popup_basic_info_edit);

        Window window = editBasicInfo.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnmainback = (LinearLayout) editBasicInfo.findViewById(R.id.lnmainback);
        LinearLayout ll_save_contact_info = (LinearLayout) editBasicInfo.findViewById(R.id.ll_save_contact_info);
        LinearLayout ll_contact_info_cancel = (LinearLayout) editBasicInfo.findViewById(R.id.ll_contact_info_cancel);
        RelativeLayout rl_date1 = (RelativeLayout) editBasicInfo.findViewById(R.id.rl_date1);

        et_birth_date = (TextView) editBasicInfo.findViewById(R.id.et_birth_date);
        RadioGroup radioGroup = (RadioGroup) editBasicInfo.findViewById(R.id.radioGroup);
        et_blood_group = (EditText) editBasicInfo.findViewById(R.id.et_blood_group);
        et_birth_date.addTextChangedListener(new MyTextWatcher(et_birth_date));


        TextView tv_next1 = (TextView) editBasicInfo.findViewById(R.id.tv_next1);
        TextView tv_cancel = (TextView) editBasicInfo.findViewById(R.id.tv_cancel);

        tv_next1.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        et_birth_date.setTypeface(Utility.getTypeFace());
        et_blood_group.setTypeface(Utility.getTypeFace());

        final RadioButton radio_gender_male = (RadioButton) editBasicInfo.findViewById(R.id.radio_gender_male);
        final RadioButton radio_gender_female = (RadioButton) editBasicInfo.findViewById(R.id.radio_gender_female);

        et_birth_date.setText(birth_date);
        et_blood_group.setText(blood_grp);

        if (gender != null) {

            if (gender.equalsIgnoreCase("Male")) {
                radio_gender_male.setChecked(true);
                gFlag = "M";
            } else if (gender.equalsIgnoreCase("Female")) {
                radio_gender_female.setChecked(true);
                gFlag = "F";
            }

        }

        /*et_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               rl_date1();
            }
        });*/

        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBasicInfo.dismiss();
            }
        });

        rl_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_date1();
            }
        });

        ll_save_contact_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radio_gender_male.isChecked()) {
                    gFlag = "M";
                } else if (radio_gender_female.isChecked()) {
                    gFlag = "F";
                }

                Log.e("et_birth_date", "" + et_birth_date.getText().toString());
                Log.e("et_blood_group", "" + et_blood_group.getText().toString());
                Log.e("gFlag", "" + gFlag);


                if (et_birth_date.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Birth Date", Toast.LENGTH_LONG).show();
                } else if (gFlag.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Gender", Toast.LENGTH_LONG).show();
                } else if (et_blood_group.getText().toString().isEmpty()) {
                    et_blood_group.setError("Enter Blood Group");
                    requestFocus(et_blood_group);
                } else {
                    SaveBasicInfo(et_birth_date.getText().toString(), gFlag, et_blood_group.getText().toString());
                }
            }
        });

        ll_contact_info_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBasicInfo.dismiss();
            }
        });
        editBasicInfo.show();
    }

    private boolean validateBloodGroup() {
        String name = et_blood_group.getText().toString().trim();
        if (name.isEmpty()) {
            et_blood_group.setError("Enter Blood Group");
            requestFocus(et_blood_group);
            return false;
        } else {
            et_blood_group.setError(null);
        }
        return true;
    }

    private boolean validatemobile() {
        String name = et_mobile_number.getText().toString().trim();
        if (name.isEmpty()) {
            et_mobile_number.setError("Enter Mobile Number");
            requestFocus(et_mobile_number);
            return false;
        } else if (name.length() > 16) {
            et_mobile_number.setError("Enter valid number.");
            requestFocus(et_mobile_number);
            return false;
        } else {
            et_mobile_number.setError(null);
        }
        return true;
    }

    private void SaveBasicInfo(final String birth_d, final String gFlag, final String blood_g) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Basic_Information_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Basic_Information_Update")) {
                            JSONArray array = object.getJSONArray("Basic_Information_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        getBasicInformation();
                                        editBasicInfo.dismiss();
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
                        // pd2.dismiss();e.printStackTrace();
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
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("DateOfBirth", birth_d);
                    map.put("Gender", gFlag);
                    map.put("BloodGroup", blood_g);
                    map.put("InfoId", InfoId);

                    Log.e("LoginId", Utility.getPeopleIdPreference());
                    Log.e("DateOfBirth", birth_d);
                    Log.e("Gender", gFlag);
                    Log.e("BloodGroup", blood_g);
                    Log.e("InfoId", InfoId);

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

    private void rl_date1() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et_birth_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private boolean validateEmail() {
        String email = et_email_id.getText().toString().trim();
        if (email.isEmpty()) {
            et_email_id.setError("Please enter email.");
            requestFocus(et_email_id);
            return false;
        } else if (!isValidEmail(email)) {
            et_email_id.setError("Please enter valid email.");
            requestFocus(et_email_id);
            return false;
        } else {
            et_email_id.setError(null);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        String emailPattern1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
        return !TextUtils.isEmpty(email) && email.matches(emailPattern1) || !TextUtils.isEmpty(email) && email.matches(emailPattern2);
    }
}
