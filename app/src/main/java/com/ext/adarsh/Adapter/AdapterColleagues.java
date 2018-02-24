package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.ext.adarsh.Activity.other.OtherProfileActivity;
import com.ext.adarsh.Bean.BeanPeopleDetail;
import com.ext.adarsh.Bean.BeanProfileColleguesFriends;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterColleagues extends RecyclerView.Adapter<AdapterColleagues.MyViewHolder> implements Filterable {
    String peopleId;
    private ArrayList<BeanProfileColleguesFriends> contacteList;
    ArrayList<BeanProfileColleguesFriends> array = new ArrayList<>();
    ArrayList<BeanPeopleDetail> beanPeopleDetails = new ArrayList<>();
    Activity activity;
    Dialog dd;
    ProgressDialog pd;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtname, txtdiscription;
        private ImageView imageview;

        @BindView(R.id.lnmain)
        LinearLayout lnmain;

        @BindView(R.id.contactmenu)
        LinearLayout contactmenu;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            txtname = (TextView) view.findViewById(R.id.txtname);
            txtdiscription = (TextView) view.findViewById(R.id.txtdiscription);
            imageview = (ImageView) view.findViewById(R.id.image);
        }
    }

    public AdapterColleagues(ArrayList<BeanProfileColleguesFriends> contacteList, Activity activity) {
        this.contacteList = contacteList;
        this.array = contacteList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
        final BeanProfileColleguesFriends contact = contacteList.get(position);

        holder.txtname.setTypeface(Utility.getTypeFaceTab());
        holder.txtdiscription.setTypeface(Utility.getTypeFace());

        holder.txtname.setText(contact.fullName);
        holder.txtdiscription.setText(contacteList.get(position).departmentName);
        Glide.with(activity).load(contacteList.get(position).profileImage).into(holder.imageview);

        holder.lnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OtherProfileActivity.class);
                intent.putExtra("peopleId", contact.peopleId);
                activity.startActivity(intent);
            }
        });
        holder.contactmenu.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return contacteList.size();
    }

    public void popup(MyViewHolder holder) {

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popupcontact, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView tv1 = (TextView) popupView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) popupView.findViewById(R.id.tv2);
        TextView tv3 = (TextView) popupView.findViewById(R.id.tv3);
        TextView tv4 = (TextView) popupView.findViewById(R.id.tv4);
        TextView tv5 = (TextView) popupView.findViewById(R.id.tv5);
        TextView tv6 = (TextView) popupView.findViewById(R.id.tv6);
        TextView tv7 = (TextView) popupView.findViewById(R.id.tv7);

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(20);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(popupView, Gravity.RIGHT, 50, -350);
        popupWindow.showAsDropDown(holder.contactmenu, Gravity.RIGHT, -50, 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String s = String.valueOf(charSequence);

                if (s != null && s.length() > 0) {
                    ArrayList<BeanProfileColleguesFriends> temp = new ArrayList<>();
                    for (BeanProfileColleguesFriends category : array) {
                        if (category.fullName.toLowerCase().contains(s.toLowerCase())) {
                            temp.add(category);
                        }
                    }
                    results.values = temp;
                    results.count = temp.size();
                } else {
                    results.values = array;
                    results.count = array.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contacteList = (ArrayList<BeanProfileColleguesFriends>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    void getPeopleDetail(final String id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_People_Profile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Contact_People_Profile_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Contact_People_Profile_Array");
                            if (jsonArray.length() != 0) {
                                beanPeopleDetails.clear();
                                beanPeopleDetails.addAll((Collection<? extends BeanPeopleDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeopleDetail>>() {
                                }.getType()));
                            }
                        }
                        pd.dismiss();
                        //  showAlertDialog3(position);

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
                    map.put("ContactId", id);
                    Log.e("Hashkey", "" + Utility.getHashKeyPreference());
                    Log.e("ContactId", "" + id);

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