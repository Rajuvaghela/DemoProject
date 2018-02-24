package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.ext.adarsh.Activity.KnowledgeMain;
import com.ext.adarsh.Bean.BeanKnowledge;
import com.ext.adarsh.Fragment.AllKnowledgeFragment;
import com.ext.adarsh.Fragment.PopularKnowledge;
import com.ext.adarsh.Fragment.RecentyAddedKnowledge;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

/**
 * Created by ExT-Emp-001 on 11-08-2017.
 */

public class AdapterKnowledgeAll extends RecyclerView.Adapter<AdapterKnowledgeAll.MyViewHolder> {

    private List<BeanKnowledge> knowledgeList;
    Activity activity;
    Dialog edit_topic_dialog;
    ProgressDialog pd;
    String parent_class;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtname, txtartical, txtdate;

        @BindView(R.id.lnmain)
        LinearLayout lnmain;


        LinearLayout ll_know_topic_menu, ll_topic_info;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            pd = Utility.getDialog(activity);

            txtname = (TextView) view.findViewById(R.id.txtname);
            txtartical = (TextView) view.findViewById(R.id.txtartical);
            txtdate = (TextView) view.findViewById(R.id.date);
            ll_know_topic_menu = (LinearLayout) view.findViewById(R.id.ll_know_topic_menu);
            ll_topic_info = (LinearLayout) view.findViewById(R.id.ll_topic_info);


        }
    }


    public AdapterKnowledgeAll(List<BeanKnowledge> knowledgeList, Activity activity, String parent_class) {
        this.knowledgeList = knowledgeList;
        this.activity = activity;
        this.parent_class = parent_class;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.knowledge_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final BeanKnowledge knowledge = knowledgeList.get(position);


        if (Utility.getKnowledgeUpdate().equalsIgnoreCase("Y")){
            holder.ll_know_topic_menu.setVisibility(View.VISIBLE);
        }else {
            holder.ll_know_topic_menu.setVisibility(View.GONE);
        }

        holder.txtname.setTypeface(Utility.getTypeFaceTab());
        holder.txtartical.setTypeface(Utility.getTypeFace());
        holder.txtdate.setTypeface(Utility.getTypeFace());

        holder.txtname.setText(Html.fromHtml(knowledge.title).toString());
        holder.txtartical.setText(knowledge.totalArticale + " Articles");
        holder.txtdate.setText(knowledge.modifiedDate);
        holder.ll_topic_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  BeanKnowledge knowledge = knowledgeList.get(position);
                AppConstant.topicId = knowledge.topicId;
                Log.e("topic_id", "" + knowledge.topicId);
                activity.startActivity(new Intent(activity, KnowledgeMain.class));
                activity.finish();
            }
        });
        holder.ll_know_topic_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupmenu(view, position, knowledge.title, knowledge.description, knowledge.topicId);
            }
        });


    }

    public void showpopupmenu(View view, final int pos, final String title, final String description, final String topic_id) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.know_topic_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_rename:
                        renameTopic(title, description, topic_id);
                        break;

                    case R.id.menu_delete:
                        deleteDialoge(pos, topic_id);

                        break;

                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    private void deleteDialoge(final int position, final String topic_id) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Delete")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        deleteKnowledgeTopic(topic_id, position);
                        //  android.os.Process.killProcess(android.os.Process.myPid());
                        /// android.os.Process.killProcess(android.os.Process.myPid());
                        //  finish();
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void renameTopic(String title, String description, final String topic_id) {
        edit_topic_dialog = new Dialog(activity);
        edit_topic_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        edit_topic_dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        edit_topic_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        edit_topic_dialog.setContentView(R.layout.popup_know_topic_rename);
        Window window = edit_topic_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView text1 = (TextView) edit_topic_dialog.findViewById(R.id.text1);
        TextView tv_add_topic = (TextView) edit_topic_dialog.findViewById(R.id.tv_add_topic);
        TextView tv_cancel = (TextView) edit_topic_dialog.findViewById(R.id.tv_cancel);
        final EditText et_topic_title = (EditText) edit_topic_dialog.findViewById(R.id.et_topic_title);
        final EditText et_topic_des = (EditText) edit_topic_dialog.findViewById(R.id.et_topic_des);

        et_topic_title.setTypeface(Utility.getTypeFace());
        et_topic_des.setTypeface(Utility.getTypeFace());
        text1.setTypeface(Utility.getTypeFaceTab());
        tv_add_topic.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());

        et_topic_title.setText(title);
        et_topic_des.setText(description);

        LinearLayout lnback = (LinearLayout) edit_topic_dialog.findViewById(R.id.lnback);
        LinearLayout ll_cancel = (LinearLayout) edit_topic_dialog.findViewById(R.id.ll_cancel);
        LinearLayout ll_update_topic = (LinearLayout) edit_topic_dialog.findViewById(R.id.ll_update_topic);

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_topic_dialog.dismiss();
            }
        });

        ll_update_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateKnowledgeTopic(et_topic_title.getText().toString(), et_topic_des.getText().toString(), topic_id);
            }
        });

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_topic_dialog.dismiss();
            }
        });
        edit_topic_dialog.show();
    }

    public void UpdateKnowledgeTopic(final String title, final String des, final String topic_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Topic_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Topic_Update")) {
                            JSONArray array = object.getJSONArray("Knowledge_Topic_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        edit_topic_dialog.dismiss();
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
                    map.put("Title", title);
                    map.put("Description", des);
                    map.put("TopicId", topic_id);
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

    public void deleteKnowledgeTopic(final String topic_id, final int pos) {

        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Topic_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Topic_Delete")) {
                            JSONArray array = object.getJSONArray("Knowledge_Topic_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        AllKnowledgeFragment.getAllDataKnowledge();
                                        PopularKnowledge.getAllDataKnowledge();
                                        RecentyAddedKnowledge.getAllDataKnowledge();
                               /*         if (parent_class.equalsIgnoreCase("all_know")) {
                                            knowledgeList.remove(pos);
                                            AllKnowledgeFragment.mAdapter.notifyDataSetChanged();
                                        } else if (parent_class.equalsIgnoreCase("popular")) {
                                            knowledgeList.remove(pos);
                                            PopularKnowledge.mAdapter.notifyDataSetChanged();
                                        } else {
                                            knowledgeList.remove(pos);
                                            RecentyAddedKnowledge.mAdapter.notifyDataSetChanged();
                                        }*/
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {

                            }
                        }
                    } catch (JSONException e) {

                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());

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
                    map.put("TopicId", topic_id);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }
}