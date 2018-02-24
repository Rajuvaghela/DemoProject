package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.ext.adarsh.Activity.MessageDetail;
import com.ext.adarsh.Bean.BeanMessage;
import com.ext.adarsh.Bean.ModelMutilpleDelete;
import com.ext.adarsh.Fragment.InboxFragment;
import com.ext.adarsh.Fragment.SentFragment;
import com.ext.adarsh.Fragment.StarredFragment;
import com.ext.adarsh.Fragment.TrashFragment;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterTrashMessage extends RecyclerView.Adapter<AdapterTrashMessage.MyViewHolder> implements Filterable {

    private List<BeanMessage> messageList;
    private List<BeanMessage> array;

    Activity activity;

    // Dialog dd;
    int count = 0;
    String messageId,message_id="";
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    String index="";
    ArrayList<ModelMutilpleDelete> checklistmessage = new ArrayList<>();



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, message, time, fname;

        @BindView(R.id.ln_messagelistmain)
        LinearLayout ln_messagelistmain;

        @BindView(R.id.firstname2)
        TextView firstname2;

        @BindView(R.id.profile_image)
        LinearLayout profile_image;

        @BindView(R.id.profile_image2)
        LinearLayout profile_image2;

        @BindView(R.id.iv_favourite)
        ImageView iv_favourite;

        @BindView(R.id.lnmain2)
        LinearLayout lnmain2;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            title = (TextView) view.findViewById(R.id.title);
            message = (TextView) view.findViewById(R.id.txt_message);
            time = (TextView) view.findViewById(R.id.time);
            fname = (TextView) view.findViewById(R.id.firstname);
            iv_favourite.setVisibility(View.GONE);
        }
    }

    public AdapterTrashMessage(List<BeanMessage> messageList, Activity activity) {
        this.messageList = messageList;
        this.array = messageList;
        this.activity = activity;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_list2, parent, false);
        //   pd = Utility.getDialog(activity);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BeanMessage movie = messageList.get(position);

        holder.title.setTypeface(Utility.getTypeFace());
        holder.message.setTypeface(Utility.getTypeFace());
        holder.time.setTypeface(Utility.getTypeFace());
        holder.fname.setTypeface(Utility.getTypeFace());
        holder.firstname2.setTypeface(Utility.getTypeFace());

        holder.title.setText(movie.fromName);
        holder.message.setText(movie.subject);
        holder.time.setText(movie.time);
        String t1 = movie.fromName;
        holder.fname.setText(String.valueOf(t1.charAt(0)).toUpperCase());
        //   holder.fname.setText("K");

        holder.ln_messagelistmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeanMessage movie = messageList.get(position);
                messageId = movie.messageId;
                Intent intent = new Intent(activity, MessageDetail.class);
                intent.putExtra("MessageId", messageId);
                intent.putExtra("IsFavourite","null");
                intent.putExtra("frag","Trash");
                activity.startActivity(intent);
            }
        });

     /*   holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeanMessage movie = messageList.get(position);
                messageId = movie.messageId;
                //  Addtostarred(messageId);
            }
        });*/

        holder.fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((LinearLayout) activity.findViewById(R.id.header_message_grey)).getVisibility() == View.VISIBLE) {
                    activity.findViewById(R.id.ll_recover).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.v_g).setVisibility(View.GONE);
                    holder.profile_image.setVisibility(View.GONE);
                    holder.profile_image2.setVisibility(View.VISIBLE);
                    holder.lnmain2.setBackgroundColor(activity.getResources().getColor(R.color.lightgrey));
                    count = count + 1;

                    messageId =  messageList.get(position).messageId;
                    checklistmessage.add(new ModelMutilpleDelete(messageId,position));
                    for (int i=0;i<checklistmessage.size();i++){
                        Log.e("list",""+ checklistmessage.get(i));
                    }

                    ((TextView) activity.findViewById(R.id.count)).setText(String.valueOf(count));
                    LinearLayout ln_delete = (LinearLayout) activity.findViewById(R.id.ln_delete);
                    ln_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BeanMessage movie = messageList.get(position);
                            messageId = movie.messageId;
                            for (int i=0;i<checklistmessage.size();i++){
                                message_id = message_id + checklistmessage.get(i).getId() + ",";
                            }
                            message_id = message_id.substring(0,message_id.length()-1);
                            Log.e("messageID",message_id);
                            deletemessage(message_id);
                        }
                    });

                    activity.findViewById(R.id.ll_recover).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BeanMessage movie = messageList.get(position);
                            messageId = movie.messageId;
                            for (int i=0;i<checklistmessage.size();i++){
                                message_id = message_id + checklistmessage.get(i).getId() + ",";
                            }
                            message_id = message_id.substring(0,message_id.length()-1);
                            Log.e("messageID",message_id);
                            recovermessage(message_id);
                        }
                    });


                } else {
                    ((LinearLayout) activity.findViewById(R.id.header_message_grey)).setVisibility(View.VISIBLE);
                    ((LinearLayout) activity.findViewById(R.id.header_message)).setVisibility(View.GONE);
                    activity.findViewById(R.id.ll_recover).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.v_g).setVisibility(View.GONE);
                    holder.lnmain2.setBackgroundColor(activity.getResources().getColor(R.color.lightgrey));
                    holder.profile_image.setVisibility(View.GONE);
                    holder.profile_image2.setVisibility(View.VISIBLE);
                    count = count + 1;

                    messageId =  messageList.get(position).messageId;
                    checklistmessage.add(new ModelMutilpleDelete(messageId,position));
                    for (int i=0;i<checklistmessage.size();i++){
                        Log.e("list",""+ checklistmessage.get(i));
                    }

                    ((TextView) activity.findViewById(R.id.count)).setText(String.valueOf(count));
                    LinearLayout ln_delete = (LinearLayout) activity.findViewById(R.id.ln_delete);
                    ln_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BeanMessage movie = messageList.get(position);
                            messageId = movie.messageId;
                            for (int i=0;i<checklistmessage.size();i++){
                                message_id = message_id + checklistmessage.get(i).getId() + ",";
                            }
                            message_id = message_id.substring(0,message_id.length()-1);
                            Log.e("messageID",message_id);
                            deletemessage(message_id);
                        }
                    });

                    activity.findViewById(R.id.ll_recover).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BeanMessage movie = messageList.get(position);
                            messageId = movie.messageId;
                            for (int i=0;i<checklistmessage.size();i++){
                                message_id = message_id + checklistmessage.get(i).getId() + ",";
                            }
                            message_id = message_id.substring(0,message_id.length()-1);
                            Log.e("messageID",message_id);
                            recovermessage(message_id);
                        }
                    });
                }
            }
        });

        holder.firstname2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.profile_image.setVisibility(View.VISIBLE);
                holder.profile_image2.setVisibility(View.GONE);
                holder.lnmain2.setBackgroundColor(activity.getResources().getColor(R.color.white));
                count = count - 1;

                messageId =  messageList.get(position).messageId;
                if(checklistmessage.size()!=0){
                    for (int i=0;i<checklistmessage.size();i++){
                        if(checklistmessage.get(i).getId().equalsIgnoreCase(messageId)){
                            checklistmessage.remove(i);
                        }
                    }
                }

                ((TextView) activity.findViewById(R.id.count)).setText(String.valueOf(count));
                if (count == 0) {
                    ((LinearLayout) activity.findViewById(R.id.header_message_grey)).setVisibility(View.GONE);
                    ((LinearLayout) activity.findViewById(R.id.header_message)).setVisibility(View.VISIBLE);
                }
            }
        });

        holder.ln_messagelistmain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((LinearLayout) activity.findViewById(R.id.header_message_grey)).setVisibility(View.VISIBLE);
                ((LinearLayout) activity.findViewById(R.id.header_message)).setVisibility(View.GONE);
                holder.lnmain2.setBackgroundColor(activity.getResources().getColor(R.color.lightgrey));
                holder.profile_image.setVisibility(View.GONE);
                holder.profile_image2.setVisibility(View.VISIBLE);
                count = count + 1;
                ((TextView) activity.findViewById(R.id.count)).setText(String.valueOf(count));
                return true;
            }
        });


        activity.findViewById(R.id.lnbackoriginal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) activity.findViewById(R.id.header_message_grey)).setVisibility(View.GONE);
                ((LinearLayout) activity.findViewById(R.id.header_message)).setVisibility(View.VISIBLE);
                holder.lnmain2.setBackgroundColor(activity.getResources().getColor(R.color.white));
                count = 0;
                TrashFragment.message();
                holder.profile_image.setVisibility(View.VISIBLE);
                holder.profile_image2.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private void deletemessage(final String messageId) {
        if (checkConnectivity()) {
            //    pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Trash_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Trash_Delete")) {
                            JSONArray array = object.getJSONArray("Message_Trash_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();

                                        ((LinearLayout) activity.findViewById(R.id.header_message_grey)).setVisibility(View.GONE);
                                        ((LinearLayout) activity.findViewById(R.id.header_message)).setVisibility(View.VISIBLE);
                                        count = 0;
                                        TrashFragment.refresh();

                                    } else {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        if(object.has("ErrorMessage")) {
                            ((LinearLayout) activity.findViewById(R.id.header_message_grey)).setVisibility(View.GONE);
                            ((LinearLayout) activity.findViewById(R.id.header_message)).setVisibility(View.VISIBLE);
                            Toast.makeText(activity,"Please try again Later", Toast.LENGTH_SHORT).show();

                            count = 0;
                        }

                    } catch (JSONException e) {
                        //   pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    //    pd.dismiss();
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
                        //      pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("MessageId", messageId);
                    map.put("LoginId", Utility.getPeopleIdPreference());

                    Log.e("MessageId", messageId);
                    Log.e("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            //   pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }

    }

    private void recovermessage(final String messageId) {
        if (checkConnectivity()) {
            //    pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Recover, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Recover")) {
                            JSONArray array = object.getJSONArray("Message_Recover");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();

                                        ((LinearLayout) activity.findViewById(R.id.header_message_grey)).setVisibility(View.GONE);
                                        ((LinearLayout) activity.findViewById(R.id.header_message)).setVisibility(View.VISIBLE);
                                        count = 0;
                                        TrashFragment.refresh();
                                    } else {
                                        //    pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        //   pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                    //    pd.dismiss();
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
                        //      pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("MessageId",messageId);
                    map.put("LoginId", Utility.getPeopleIdPreference());

                    Log.e("API","Message_Recover");
                    Log.e("MessageId",""+messageId);
                    Log.e("LoginId", ""+Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            //   pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String s = String.valueOf(charSequence);

                if (s != null && s.length() > 0) {
                    ArrayList<BeanMessage> temp = new ArrayList<>();
                    for (BeanMessage category : array) {
                        if (category.fromName.toLowerCase().contains(s.toLowerCase())) {
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
                messageList = (ArrayList<BeanMessage>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

   /* private void Addtostarred(final String messageId) {
        if (checkConnectivity()) {
         //    pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Add_To_Starred, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Add_To_Starred")) {
                            JSONArray array = object.getJSONArray("Message_Add_To_Starred");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    } else {
                                   //    pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                  //   pd.dismiss();
                    showMsg(R.string.json_error);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppConstant.TAG, error.toString());
            //    pd.dismiss();
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
               //      pd.dismiss();
                    showMsg(R.string.json_error);
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("MessageId",messageId);
                map.put("LoginId", Utility.getPeopleIdPreference());
                return map;
            }
        };
        Utility.SetvollyTime30Sec(request);
        Infranet.getInstance().getRequestQueue().add(request);
    } else {
       //   pd.dismiss();
        Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
    }
}
}*/

   /* private void showAlertDialog3() {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.activity_message_detail);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        LinearLayout lnback = (LinearLayout)dd.findViewById(R.id.lnmessageback);
        TextView firstname = (TextView) dd.findViewById(R.id.firstname);
         text1 = (TextView) dd.findViewById(R.id.text1);
        TextView text2 = (TextView) dd.findViewById(R.id.text2);
        TextView text3 = (TextView) dd.findViewById(R.id.text3);
        TextView text4 = (TextView) dd.findViewById(R.id.text4);
        TextView text5 = (TextView) dd.findViewById(R.id.text5);
        TextView text6 = (TextView) dd.findViewById(R.id.text6);
        TextView text7 = (TextView) dd.findViewById(R.id.text7);
        TextView text8 = (TextView) dd.findViewById(R.id.text8);
        TextView text9 = (TextView) dd.findViewById(R.id.text9);
        TextView text10 = (TextView) dd.findViewById(R.id.text10);
        TextView text11 = (TextView) dd.findViewById(R.id.text11);
        TextView text12 = (TextView) dd.findViewById(R.id.text12);
        TextView text13 = (TextView) dd.findViewById(R.id.text13);
        TextView text14 = (TextView) dd.findViewById(R.id.text14);
        Button btn1 = (Button) dd.findViewById(R.id.btn1);


        btn1.setTypeface(Utility.getTypeFace());
        firstname.setTypeface(Utility.getTypeFace());
        text1.setTypeface(Utility.getTypeFace());
        text2.setTypeface(Utility.getTypeFace());
        text3.setTypeface(Utility.getTypeFace());
        text4.setTypeface(Utility.getTypeFace());
        text5.setTypeface(Utility.getTypeFace());
        text6.setTypeface(Utility.getTypeFace());
        text7.setTypeface(Utility.getTypeFace());
        text8.setTypeface(Utility.getTypeFace());
        text9.setTypeface(Utility.getTypeFace());
        text10.setTypeface(Utility.getTypeFace());
        text11.setTypeface(Utility.getTypeFace());
        text12.setTypeface(Utility.getTypeFace());
        text13.setTypeface(Utility.getTypeFace());
        text14.setTypeface(Utility.getTypeFace());

        messagedetail();

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
            }
        });

        dd.show();
    }

    private void messagedetail() {
        if (checkConnectivity()) {
           // pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Message_Detail, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Message_Subject")) {
                            JSONArray jsonArray = object.getJSONArray("Message_Subject");
                              if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        String Title  = array.optJSONObject(i).getString("FromName");
                                        String Message  = array.optJSONObject(i).getString("Subject");
                                        String Time = array.optJSONObject(i).getString("Time");


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
                           if (jsonArray.length() != 0) {
                               messageList.clear();
                              messqageListDetail.addAll((Collection<? extends BeanMessageDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMessageDetail>>() {
                                }.getType()));
                                text1.setText();

                               pd.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                       // pd.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
                   // pd.dismiss();
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
                       // pd.dismiss();
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
                    map.put("MessageId",messageId);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
          //  pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }*/


