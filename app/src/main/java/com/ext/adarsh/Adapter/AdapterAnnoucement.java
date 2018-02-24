package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.ext.adarsh.Bean.BeanAnnoucement;
import com.ext.adarsh.Bean.BeanAnnoucementDetail;
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

public class AdapterAnnoucement extends RecyclerView.Adapter<AdapterAnnoucement.MyViewHolder> {

    private ArrayList<BeanAnnoucement> annoucementList;
    Activity activity;
    Dialog announcementDetail;
    ProgressDialog pd;
    RecyclerView rv_announcement_detail;
    ArrayList<BeanAnnoucementDetail> beanAnnoucements = new ArrayList<>();

    TextView txt_month, txt_date, txtx_title1, txt_title2, txt_detail, header_attachment, header_imaportant, txt_important;
    TextView txt_attchment_name;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_month)
        TextView txt_month;
        @BindView(R.id.txt_date)
        TextView txt_date;

        @BindView(R.id.txtx_title1)
        TextView txtx_title1;

        @BindView(R.id.txt_title2)
        TextView txt_title2;

        @BindView(R.id.ll_main)
        LinearLayout ll_main;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            pd = Utility.getDialog(activity);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public AdapterAnnoucement(ArrayList<BeanAnnoucement> AnnoucementList, Activity activity) {
        this.annoucementList = AnnoucementList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcementlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txt_date.setText(annoucementList.get(position).day);
        holder.txt_title2.setText(annoucementList.get(position).publishBy);
        holder.txt_month.setText(annoucementList.get(position).month);
        holder.txtx_title1.setText(annoucementList.get(position).announcementTitle);

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnnocementDetail(annoucementList.get(position).announcementId);
            }
        });


        holder.txt_date.setTypeface(Utility.getTypeFaceTab());
        holder.txtx_title1.setTypeface(Utility.getTypeFace());
        holder.txt_title2.setTypeface(Utility.getTypeFace());
        holder.txt_month.setTypeface(Utility.getTypeFace());

    }

    @Override
    public int getItemCount() {
        return annoucementList.size();
    }

    private void announcementDetail() {
        announcementDetail = new Dialog(activity);
        announcementDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        announcementDetail.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        announcementDetail.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        announcementDetail.setContentView(R.layout.announcement_detail);
        Window window = announcementDetail.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        rv_announcement_detail = (RecyclerView) announcementDetail.findViewById(R.id.rv_announcement_detail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_announcement_detail.setLayoutManager(mLayoutManager);
        rv_announcement_detail.setItemAnimator(new DefaultItemAnimator());

        AdapterAnnoucementDetail adapter = new AdapterAnnoucementDetail(beanAnnoucements, activity);
        rv_announcement_detail.setAdapter(adapter);


        LinearLayout lnmainback = (LinearLayout) announcementDetail.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                announcementDetail.dismiss();
            }
        });


        announcementDetail.show();
    }

    private void getAnnocementDetail(final String announcemenId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Announcement_Detail_By_AnnouncementId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Announcement_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Announcement_Array");
                            if (jsonArray.length() != 0) {
                                beanAnnoucements.clear();
                                beanAnnoucements.addAll((Collection<? extends BeanAnnoucementDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanAnnoucementDetail>>() {
                                }.getType()));
                                announcementDetail();
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("AnnouncementId", announcemenId);
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
