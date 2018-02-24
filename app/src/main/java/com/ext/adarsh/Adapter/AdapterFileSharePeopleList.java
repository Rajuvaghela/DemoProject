package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.ext.adarsh.Bean.BeanFileFolderSharePeopleList;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class AdapterFileSharePeopleList extends RecyclerView.Adapter<AdapterFileSharePeopleList.MyViewHolder> {
    private ArrayList<BeanFileFolderSharePeopleList> contacteList;
    Activity activity;
    ArrayList<String> categories;
    String canview_or_canedit = "V";
    ProgressDialog pd;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtname, txtdiscription;
        ImageView imageview;
        Spinner spiner_canview_or_canedit;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            pd = Utility.getDialog(activity);
            categories = new ArrayList<String>();
            categories.add("Can View");
            categories.add("Can Edit");
            categories.add("Remove");
            txtname = (TextView) view.findViewById(R.id.txtname);
            txtdiscription = (TextView) view.findViewById(R.id.txtdiscription);
            imageview = (ImageView) view.findViewById(R.id.image);
            spiner_canview_or_canedit = (Spinner) view.findViewById(R.id.spiner_canview_or_canedit);
        }
    }

    public AdapterFileSharePeopleList(ArrayList<BeanFileFolderSharePeopleList> contacteList, Activity activity) {
        this.contacteList = contacteList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_shared_people_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BeanFileFolderSharePeopleList contact = contacteList.get(position);

        holder.txtname.setTypeface(Utility.getTypeFaceTab());
        holder.txtdiscription.setTypeface(Utility.getTypeFace());

        holder.txtname.setText(contact.fullName);
        holder.txtdiscription.setText(contacteList.get(position).emailAddress);
        Glide.with(activity).load(contacteList.get(position).profileImage).into(holder.imageview);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spiner_canview_or_canedit.setAdapter(dataAdapter);
        holder.spiner_canview_or_canedit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    canview_or_canedit = "V";
                } else if (i == 1) {
                    canview_or_canedit = "E";
                } else {
                    RemovePeople(contact.shareId, position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void RemovePeople(final String shareId, final int position) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Folder_Share_People_Remove, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("File_Folder_Share_People_Remove")) {
                            JSONArray array = object.getJSONArray("File_Folder_Share_People_Remove");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        contacteList.remove(position);
                                        AdapterMyDrive.adapter3.notifyDataSetChanged();
                                        pd.dismiss();
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
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
                    map.put("ShareId", shareId);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            showSuccessAlertDialog(activity, activity.getResources().getString(R.string.network_message));
        }
    }

    @Override
    public int getItemCount() {
        return contacteList.size();
    }


}