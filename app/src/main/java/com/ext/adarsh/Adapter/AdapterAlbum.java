package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
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
import com.bumptech.glide.Glide;
import com.ext.adarsh.Bean.BeanAlbum;
import com.ext.adarsh.Bean.BeanAlbumPhoto;
import com.ext.adarsh.Bean.BeanPhotoDetail;
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
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterAlbum extends RecyclerView.Adapter<AdapterAlbum.MyViewHolder> {

    private ArrayList<BeanAlbum> albumList;
    Activity activity;
    Dialog addAlbum, albumPhoto, dd;
    ProgressDialog pd;
    GridView gridview;
    ImageView fblikegrey;
    ImageView fbLikered;
    String flag = "";
    public static ArrayList<BeanPhotoDetail> beanPhotoDetails = new ArrayList<>();
    ArrayList<BeanAlbumPhoto> beanPhotos = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, txt;
        public ImageView image;
        @BindView(R.id.lnplus)
        LinearLayout lnplus;

        @BindView(R.id.view1)
        View view1;

        @BindView(R.id.view2)
        View view2;

        LinearLayout ll_album_more;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            title = (TextView) view.findViewById(R.id.title);
            txt = (TextView) view.findViewById(R.id.txt);
            image = (ImageView) view.findViewById(R.id.imageview);
            ll_album_more = (LinearLayout) view.findViewById(R.id.ll_album_more);
        }
    }


    public AdapterAlbum(Activity activity, ArrayList<BeanAlbum> albumList, String flag) {
        this.albumList = albumList;
        this.activity = activity;
        this.flag = flag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);

        final BeanAlbum album = albumList.get(position);
        holder.title.setTypeface(Utility.getTypeFace());
        holder.txt.setTypeface(Utility.getTypeFace());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // addAlbum();
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumDetailByPhoto(position);
            }
        });
        if (flag.equalsIgnoreCase("other")) {
            holder.ll_album_more.setVisibility(View.GONE);
        }
        holder.ll_album_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupmenu(view, position, album.albumId);
            }
        });

        holder.lnplus.setVisibility(View.GONE);
        holder.title.setText(album.albumName);
        holder.txt.setText(album.totalPhotos + " Photo");
        Glide.with(activity).load(album.filePath).into(holder.image);

    }

    public void showpopupmenu(View view, final int position, final String album) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.photo_album_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
/*                    case R.id.menu_feture_on_profile:
                        break;

                    case R.id.menu_edit:
                        break;
                    case R.id.menu_add_contributor:
                        break;*/
                    case R.id.menu_delete_album:
                        deleteDialoge(position, album);
                        break;

                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    private void deleteDialoge(final int position, final String album) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Delete")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        DeleteAlbum(position, album);
                        // DeletePhoto(position, beanPhotos.get(position).albumDetailId);
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void DeleteAlbum(final int position, final String album) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Album_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Album_Delete")) {
                            JSONArray array = object.getJSONArray("Album_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        albumList.remove(position);
                                        notifyDataSetChanged();
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
                    map.put("AlbumId", album);
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

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    private void addAlbum() {
        addAlbum = new Dialog(activity);
        addAlbum.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addAlbum.getWindow().setWindowAnimations(R.style.DialogAnimation);
        addAlbum.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addAlbum.setContentView(R.layout.create_album);

        Window window = addAlbum.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView h1 = (TextView) addAlbum.findViewById(R.id.h1);

        TextView tv1 = (TextView) addAlbum.findViewById(R.id.tv1);
        TextView tv3 = (TextView) addAlbum.findViewById(R.id.tv3);
        TextView tv4 = (TextView) addAlbum.findViewById(R.id.tv4);
        TextView tv5 = (TextView) addAlbum.findViewById(R.id.tv5);
        TextView tv6 = (TextView) addAlbum.findViewById(R.id.tv6);
        TextView tv7 = (TextView) addAlbum.findViewById(R.id.tv7);
        TextView tv8 = (TextView) addAlbum.findViewById(R.id.tv8);

        EditText edit1 = (EditText) addAlbum.findViewById(R.id.edit1);
        EditText edit2 = (EditText) addAlbum.findViewById(R.id.edit2);

        h1.setTypeface(Utility.getTypeFace());
        tv1.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        edit1.setTypeface(Utility.getTypeFace());
        edit2.setTypeface(Utility.getTypeFace());


        LinearLayout drawericon = (LinearLayout) addAlbum.findViewById(R.id.drawericon);

        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbum.dismiss();
            }
        });
        addAlbum.show();
    }

    private void albumDetailByPhoto(int position) {
        albumPhoto = new Dialog(activity);
        albumPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        albumPhoto.getWindow().setWindowAnimations(R.style.DialogAnimation);
        albumPhoto.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        albumPhoto.setContentView(R.layout.photo_album_view_dialog);

        TextView header = (TextView) albumPhoto.findViewById(R.id.header);
        gridview = (GridView) albumPhoto.findViewById(R.id.gridview);
        LinearLayout lnback = (LinearLayout) albumPhoto.findViewById(R.id.lnback);

        header.setText(albumList.get(position).albumName);

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPhoto.dismiss();
            }
        });


        getPhotoByAlbumData(albumList.get(position).albumId);

        header.setTypeface(Utility.getTypeFaceTab());
        Window window = albumPhoto.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        albumPhoto.show();
    }

    public void getPhotoByAlbumData(final String albumid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Album_Photo_Video_By_AlbumId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Album_Photos_Videos_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Album_Photos_Videos_Array");
                            if (jsonArray.length() != 0) {
                                beanPhotos.clear();
                                beanPhotos.addAll((Collection<? extends BeanAlbumPhoto>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAlbumPhoto>>() {
                                }.getType()));

                                AlbumImageAdapter adapter = new AlbumImageAdapter(activity, beanPhotos);
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
                    map.put("AlbumId", albumid);
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
