package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
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
import com.ext.adarsh.Adapter.AdapterAnnoucement;
import com.ext.adarsh.Adapter.AdapterMyAnnoucement;
import com.ext.adarsh.Adapter.AnnoucementPagerAdapter;
import com.ext.adarsh.Bean.BeanAnnoucement;
import com.ext.adarsh.Bean.BeanMyAnnoucement;
import com.ext.adarsh.Fragment.Myannouncement;
import com.ext.adarsh.Fragment.announcement;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class Annoucement extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    AnnoucementPagerAdapter mAdapter;

    @BindView(R.id.drawericon)
    LinearLayout ivDrawer;

    @BindView(R.id.h1)
    TextView h1;

    static Activity activity;
    static ProgressDialog pd;

    public static boolean attachmentcheck = false;
    public static boolean myattachmentcheck = false;
    public static final int PICKFILE_RESULT_CODE = 1;

    public static AdapterMyAnnoucement adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_annoucement, frameLayout);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);

        getAnnocementDetail();

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer("homenew");
            }
        });

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mAdapter = new AnnoucementPagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setOnTabSelectedListener(this);

        Utility.changeTabsFont(tabLayout);
        h1.setTypeface(Utility.getTypeFace());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Myannouncement.PICKFILE_RESULT_CODE && resultCode == RESULT_OK && null != data) {

            Myannouncement.arrayList_file_name.clear();
            Myannouncement.arrayList_file_extension.clear();
            Myannouncement.arrayList_file_base64.clear();

            if (data.getData() != null) {
                Uri FilePath = data.getData();
                Log.e("onActivityResult: ", "++++++++++++++" + String.valueOf(FilePath));
                String path = null;
                try {
                    path = Myannouncement.getFilePath(activity, FilePath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                String file = path.substring(path.lastIndexOf("/") + 1);

                Myannouncement.file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                Myannouncement.filename = file.substring(0, file.lastIndexOf('.'));
                Myannouncement.arrayList_file_name.add(Myannouncement.filename);
                Myannouncement.arrayList_file_extension.add(Myannouncement.file_extension);
                Log.e("onActivityResult: ", "path++++++++++++" + path);
                Log.e("log", "64+++++++++++++++" + Myannouncement.readFileAsBase64String(path));
                Myannouncement.arrayList_file_base64.add(Myannouncement.readFileAsBase64String(path));
                Myannouncement.tv_file_name.setText(String.valueOf(Myannouncement.arrayList_file_base64.size()) + " file attached");

            } else {
                if (data != null) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Log.i("Path:", item.toString());

                        Uri uri = item.getUri();
                        Log.e("uri", "" + uri);
                        String path = null;
                        try {
                            path = Myannouncement.getFilePath(activity, uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        String file = path.substring(path.lastIndexOf("/") + 1);

                        Myannouncement.file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                        Myannouncement.filename = file.substring(0, file.lastIndexOf('.'));
                        Myannouncement.arrayList_file_name.add(Myannouncement.filename);
                        Myannouncement.arrayList_file_extension.add(Myannouncement.file_extension);
                        Log.e("onActivityResult: ", "path++++++++++++" + path);
                        Log.e("log", "64+++++++++++++++" + Myannouncement.readFileAsBase64String(path));
                        Myannouncement.arrayList_file_base64.add(Myannouncement.readFileAsBase64String(path));

                        Myannouncement.tv_file_name.setText(String.valueOf(Myannouncement.arrayList_file_base64.size()) + " file attached");
                        Log.e("size arraylist", "++++++++++++" + Myannouncement.arrayList_file_base64.size());
                        Log.e("size arraylist", "+++++++++++++++" + Myannouncement.arrayList_file_extension.size());


                    }
                }
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            // When an Image is picked
//            if (requestCode == Myannouncement.PICKFILE_RESULT_CODE && resultCode == RESULT_OK
//                    && null != data) {
//                // Get the Image from data
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Myannouncement.imagesEncodedList = new ArrayList<String>();
//                if (data.getData() != null) {
//                    Uri mImageUri = data.getData();
//                    // Get the cursor
//                    Cursor cursor = activity.getContentResolver().query(mImageUri, filePathColumn, null, null, null);
//                    // Move to first row
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    Myannouncement.imageEncoded = cursor.getString(columnIndex);
//                    cursor.close();
//
//                } else {
//                    if (data.getClipData() != null) {
//                        ClipData mClipData = data.getClipData();
//                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                        for (int i = 0; i < mClipData.getItemCount(); i++) {
//
//                            ClipData.Item item = mClipData.getItemAt(i);
//                            Uri uri = item.getUri();
//                            Log.e("uri", "" + uri);
//                            String path = null;
//                            try {
//                                path = Myannouncement.getFilePath(activity, uri);
//                            } catch (URISyntaxException e) {
//                                e.printStackTrace();
//                            }
//
//                            String file = path.substring(path.lastIndexOf("/") + 1);
//
//                            Myannouncement.file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
//                            Myannouncement.filename = file.substring(0, file.lastIndexOf('.'));
//                            Myannouncement.arrayList_file_name.add(Myannouncement.filename);
//                            Myannouncement.arrayList_file_extension.add(Myannouncement.file_extension);
//                            Log.e("onActivityResult: ", "path++++++++++++" + path);
//                            Log.e("log", "64+++++++++++++++" + Myannouncement.readFileAsBase64String(path));
//                            Myannouncement.arrayList_file_base64.add(Myannouncement.readFileAsBase64String(path));
//
//                            mArrayUri.add(uri);
//                            // Get the cursor
//                            Cursor cursor = activity.getContentResolver().query(uri, filePathColumn, null, null, null);
//                            // Move to first row
//                            cursor.moveToFirst();
//
//                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                            Myannouncement.imageEncoded = cursor.getString(columnIndex);
//                            Myannouncement.imagesEncodedList.add(Myannouncement.imageEncoded);
//                            cursor.close();
//                        }
//                        Myannouncement.tv_file_name.setText(mArrayUri.size() );
//                    }
//                }
//            } else {
//                Toast.makeText(activity, "You haven't picked file",
//                        Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG)
//                    .show();
//        }
//    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
    }

    public static void getAnnocementDetail() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Announcement_Select_By_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Announcement_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Announcement_Array");
                            if (jsonArray.length() != 0) {
                                attachmentcheck = false;
                                ArrayList<BeanAnnoucement> beanAnnoucements = new ArrayList<>();
                                beanAnnoucements.addAll((Collection<? extends BeanAnnoucement>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAnnoucement>>() {
                                }.getType()));
                                AdapterAnnoucement adapter = new AdapterAnnoucement(beanAnnoucements, activity);
                                announcement.recylerannouncement.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                announcement.recylerannouncement.setAdapter(null);
                                pd.dismiss();
                            }
                        }

                        if (object.has("My_Announcement_Array")) {
                            JSONArray jsonArray = object.getJSONArray("My_Announcement_Array");
                            if (jsonArray.length() != 0) {
                                myattachmentcheck = false;
                                ArrayList<BeanMyAnnoucement> beanAnnoucements = new ArrayList<>();
                                beanAnnoucements.addAll((Collection<? extends BeanMyAnnoucement>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMyAnnoucement>>() {
                                }.getType()));

                                adapter = new AdapterMyAnnoucement(beanAnnoucements, activity);
                                Myannouncement.recylermyannouncement.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                Myannouncement.recylermyannouncement.setAdapter(null);
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
}
