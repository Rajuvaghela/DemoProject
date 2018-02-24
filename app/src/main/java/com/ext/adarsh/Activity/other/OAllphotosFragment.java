package com.ext.adarsh.Activity.other;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
import com.ext.adarsh.Adapter.ImageAdapter;
import com.ext.adarsh.Bean.BeanPhoto;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;


public class OAllphotosFragment extends Fragment {

    public static GridView gridview;

    public static ProgressDialog pd;
    public static Activity activity;
    public ArrayList<BeanPhoto> beanPhotos = new ArrayList<>();
    FloatingActionMenu fab_menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.allphotos, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        pd = Utility.getDialog(activity);

        fab_menu = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        fab_menu.setVisibility(View.GONE);

       /* fab_add_photo = (FloatingActionButton) view.findViewById(R.id.fab_add_photo);
        fab_create_album = (FloatingActionButton) view.findViewById(R.id.fab_create_album);
        fab_add_photo.setOnClickListener(this);
        fab_create_album.setOnClickListener(this);*/
        gridview = (GridView) view.findViewById(R.id.gridview);
        getPhotoData();
    }

    public void getPhotoData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Photo_Select_By_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Photos_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Photos_Array");
                            if (jsonArray.length() != 0) {
                                beanPhotos.clear();
                                beanPhotos.addAll((Collection<? extends BeanPhoto>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPhoto>>() {
                                }.getType()));

                                ImageAdapter adapter = new ImageAdapter(activity, beanPhotos, "other");
                                gridview.setAdapter(adapter);

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
                    map.put("PeopleId", AppConstant.people_id_for_photos);
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


/*    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_add_photo:
                startActivity(new Intent(activity, AddPhotoActivity.class));
                activity.finish();

                break;
            case R.id.fab_create_album:
                startActivity(new Intent(activity, AddAlbumActivity.class));
                activity.finish();
                break;
        }
    }*/
}

