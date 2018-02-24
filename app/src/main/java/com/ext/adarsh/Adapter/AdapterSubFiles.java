package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.ext.adarsh.Activity.FileActivity;
import com.ext.adarsh.Bean.BeanFiles;
import com.ext.adarsh.Bean.BeanSubFiles;
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

public class AdapterSubFiles extends RecyclerView.Adapter<AdapterSubFiles.MyViewHolder> {

    private List<BeanSubFiles> filesList;

    Dialog fileshare,file,subfile,dd;
    Activity activity;
    int count = 0;
    int count2 = 0;
    ProgressDialog pd;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        @BindView(R.id.lnshareopen)
        LinearLayout lnshareopen;

        @BindView(R.id.lnmain2)
        LinearLayout lnmain2;

        @BindView(R.id.ln_messagelistmain)
        LinearLayout ln_messagelistmain;

        @BindView(R.id.profile_image)
        LinearLayout profile_image;

        @BindView(R.id.profile_image2)
        LinearLayout profile_image2;

        @BindView(R.id.firstname2)
        TextView firstname2;

        @BindView(R.id.folderimage)
        ImageView folderimage;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            title = (TextView) view.findViewById(R.id.file_title);
           // message = (TextView) view.findViewById(R.id.file_message);

        }
    }

    public AdapterSubFiles(List<BeanSubFiles> messqageList, Activity activity) {
        this.filesList = messqageList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_list_item2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
        holder.title.setTypeface(Utility.getTypeFace());

        final BeanSubFiles movie = filesList.get(position);
        holder.title.setText(movie.folderName);

        Glide.with(activity).load(movie.icon).into(holder.folderimage);


        holder.lnshareopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    filesharingDialog(position);
            }
        });

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((LinearLayout)activity.findViewById(R.id.header_message_grey)).getVisibility() == View.VISIBLE){
                    holder.profile_image.setVisibility(View.GONE);
                    holder.profile_image2.setVisibility(View.VISIBLE);
                    holder.lnmain2.setBackgroundColor(activity.getResources().getColor(R.color.lightgrey));
                    count = count+1;
                    ((TextView)activity.findViewById(R.id.count)).setText(String.valueOf(count));
                }else {
                    ((LinearLayout)activity.findViewById(R.id.header_message_grey)).setVisibility(View.VISIBLE);
                    ((LinearLayout)activity.findViewById(R.id.header_message)).setVisibility(View.GONE);
                    holder.lnmain2.setBackgroundColor(activity.getResources().getColor(R.color.lightgrey));
                    holder.profile_image.setVisibility(View.GONE);
                    holder.profile_image2.setVisibility(View.VISIBLE);
                    count = count+1;
                    ((TextView)activity.findViewById(R.id.count)).setText(String.valueOf(count));
                }
            }
        });

        ((LinearLayout)activity.findViewById(R.id.lnbackoriginal)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, FileActivity.class));
                activity.finish();
            }
        });

        holder.firstname2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.profile_image.setVisibility(View.VISIBLE);
                holder.profile_image2.setVisibility(View.GONE);
                holder.lnmain2.setBackgroundColor(activity.getResources().getColor(R.color.white));
                count = count-1;
                ((TextView)activity.findViewById(R.id.count)).setText(String.valueOf(count));
                if (count == 0){
                    ((LinearLayout)activity.findViewById(R.id.header_message_grey)).setVisibility(View.GONE);
                    ((LinearLayout)activity.findViewById(R.id.header_message)).setVisibility(View.VISIBLE);
                }
            }
        });

     /*   holder.ln_messagelistmain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((LinearLayout)activity.findViewById(R.id.header_message_grey)).setVisibility(View.VISIBLE);
                ((LinearLayout)activity.findViewById(R.id.header_message)).setVisibility(View.GONE);
                holder.ln_messagelistmain.setBackgroundColor(activity.getResources().getColor(R.color.lightgrey));

                count = count+1;
                ((TextView)activity.findViewById(R.id.count)).setText(String.valueOf(count));
                return true;
            }
        });*/

        holder.ln_messagelistmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!movie.isFile.equalsIgnoreCase("Y")){
                   // getSubFileData(movie.fileId,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    private void subfile(int position) {
        subfile = new Dialog(activity);
        subfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        subfile.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        subfile.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        subfile.setContentView(R.layout.subfile);

        Window window = subfile.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView h1 = (TextView) subfile.findViewById(R.id.h1);
        LinearLayout lnback = (LinearLayout) subfile.findViewById(R.id.drawericon);
        RecyclerView recylersubfiles = (RecyclerView) subfile.findViewById(R.id.recylersubfiles);

        h1.setText(filesList.get(position).folderName);
        h1.setTypeface(Utility.getTypeFace());

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subfile.dismiss();
            }
        });

        subfile.show();
    }

    private void filesharingDialog(int pos) {
        fileshare = new Dialog(activity);
        fileshare.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fileshare.getWindow().setWindowAnimations(R.style.DialogAnimation);
        fileshare.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        fileshare.setContentView(R.layout.file_shareing);

        Window window = fileshare.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView file_name = (TextView) fileshare.findViewById(R.id.file_name);
        TextView txt1 = (TextView) fileshare.findViewById(R.id.txt1);
        TextView txt2 = (TextView) fileshare.findViewById(R.id.txt2);
        TextView txt4 = (TextView) fileshare.findViewById(R.id.txt4);
        TextView txt5 = (TextView) fileshare.findViewById(R.id.txt5);
        TextView txt6 = (TextView) fileshare.findViewById(R.id.txt6);
        TextView txt10 = (TextView) fileshare.findViewById(R.id.txt10);

        file_name.setTypeface(Utility.getTypeFace());
        txt1.setTypeface(Utility.getTypeFace());
        txt2.setTypeface(Utility.getTypeFace());
        txt4.setTypeface(Utility.getTypeFace());
        txt5.setTypeface(Utility.getTypeFace());
        txt6.setTypeface(Utility.getTypeFace());
        txt10.setTypeface(Utility.getTypeFace());

        LinearLayout lnfile_close = (LinearLayout) fileshare.findViewById(R.id.lnfile_close);


        lnfile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileshare.dismiss();
            }
        });




      //  file_name.setText(filesList.get(pos).getTitle());

        fileshare.show();
    }

    private void filesharingDialog2()  {
        fileshare = new Dialog(activity);
        fileshare.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fileshare.getWindow().setWindowAnimations(R.style.DialogAnimation);
        fileshare.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        fileshare.setContentView(R.layout.file_shareing2);

        Window window = fileshare.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView file_name = (TextView) fileshare.findViewById(R.id.file_name);
        TextView txt1 = (TextView) fileshare.findViewById(R.id.txt1);
        TextView txt2 = (TextView) fileshare.findViewById(R.id.txt2);
        TextView txt3 = (TextView) fileshare.findViewById(R.id.txt3);
        TextView txt4 = (TextView) fileshare.findViewById(R.id.txt4);
        TextView txt5 = (TextView) fileshare.findViewById(R.id.txt5);
        TextView txt6 = (TextView) fileshare.findViewById(R.id.txt6);
        TextView txt7 = (TextView) fileshare.findViewById(R.id.txt7);
        TextView txt8 = (TextView) fileshare.findViewById(R.id.txt8);
        TextView txt10 = (TextView) fileshare.findViewById(R.id.txt10);

        file_name.setTypeface(Utility.getTypeFace());
        txt1.setTypeface(Utility.getTypeFace());
        txt2.setTypeface(Utility.getTypeFace());
        txt3.setTypeface(Utility.getTypeFace());
        txt4.setTypeface(Utility.getTypeFace());
        txt5.setTypeface(Utility.getTypeFace());
        txt6.setTypeface(Utility.getTypeFace());
        txt7.setTypeface(Utility.getTypeFace());
        txt8.setTypeface(Utility.getTypeFace());

        txt10.setTypeface(Utility.getTypeFace());

        LinearLayout lnfile_close = (LinearLayout) fileshare.findViewById(R.id.lnfile_close);


        lnfile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileshare.dismiss();
            }
        });

        fileshare.show();
    }

    private void FileDialog() {
        file = new Dialog(activity);
        file.requestWindowFeature(Window.FEATURE_NO_TITLE);
        file.getWindow().setWindowAnimations(R.style.DialogAnimation);
        file.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        file.setContentView(R.layout.filelistdialog);

        Window window = file.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView tv1 = (TextView) file.findViewById(R.id.tv1);
        TextView tv2 = (TextView) file.findViewById(R.id.tv2);
        TextView tv3 = (TextView) file.findViewById(R.id.tv3);
        TextView tv4 = (TextView) file.findViewById(R.id.tv4);
        TextView tv5 = (TextView) file.findViewById(R.id.tv5);


        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());

        file.show();
    }

    private void showAlertDialog3() {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.addtofile);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        //  LinearLayout lnback = (LinearLayout)dd.findViewById(R.id.lnback);
        TextView tv1 = (TextView)dd.findViewById(R.id.tv1);
        TextView tv2 = (TextView)dd.findViewById(R.id.tv2);
        TextView tv3 = (TextView)dd.findViewById(R.id.tv3);
        TextView tv4 = (TextView)dd.findViewById(R.id.tv4);
        TextView tv5 = (TextView)dd.findViewById(R.id.tv5);
        TextView tv6 = (TextView)dd.findViewById(R.id.tv6);
        TextView tv7 = (TextView)dd.findViewById(R.id.tv7);
        TextView tv8 = (TextView)dd.findViewById(R.id.tv8);

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());

      /*  lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
            }
        });*/

        LinearLayout lnfile_close = (LinearLayout) dd.findViewById(R.id.lnfile_close);


        lnfile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dd.dismiss();
            }
        });

        dd.show();
    }

   /* public void getSubFileData(final String fileid,final int position) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("File_Array")) {
                            JSONArray jsonArray = object.getJSONArray("File_Array");
                            if (jsonArray.length() != 0) {
                                subFilesList.clear();
                                subFilesList.addAll((Collection<? extends BeanSubFiles>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanSubFiles>>() {}.getType()));
                                AdapterSubFiles adapter = new AdapterSubFiles(filesList, activity);
                                recylerfiles.setAdapter(adapter);
                                pd.dismiss();
                                subfile(position);
                            } else {
                                recylerfiles.setAdapter(null);
                                pd.dismiss();
                            }
                        }

                    } catch (JSONException e) {
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
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("FileId", fileid);
                    map.put("LoginId", Utility.getPeopleIdPreference());
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
    }*/

}
