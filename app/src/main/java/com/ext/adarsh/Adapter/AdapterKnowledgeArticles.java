package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.ext.adarsh.Activity.KnowSubActivity;
import com.ext.adarsh.Activity.KnowledgeArticleDetailActivity;
import com.ext.adarsh.Activity.KnowledgeMain;
import com.ext.adarsh.Bean.BeanKnowledgeArticles;
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

import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterKnowledgeArticles extends RecyclerView.Adapter<AdapterKnowledgeArticles.MyViewHolder> {
    private List<BeanKnowledgeArticles> knowledgeList;
    Activity activity;
    Dialog dd;
    ProgressDialog pd;
    Dialog rename_artical_dialog;
    String tv_header_for_next_act;
    String pre_class_name;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_articaL_name, tv_artical_no;
        LinearLayout lnmain, ll_artical_image, ll_artical_title, ll_artical_menu;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            pd = Utility.getDialog(activity);
            txt_articaL_name = (TextView) view.findViewById(R.id.txt_articaL_name);
            tv_artical_no = (TextView) view.findViewById(R.id.tv_artical_no);
            ll_artical_title = (LinearLayout) view.findViewById(R.id.ll_artical_title);
            ll_artical_image = (LinearLayout) view.findViewById(R.id.ll_artical_image);
            ll_artical_menu = (LinearLayout) view.findViewById(R.id.ll_artical_menu);
        }
    }

    public AdapterKnowledgeArticles(List<BeanKnowledgeArticles> knowledgeList, Activity activity,
                                    String tv_header_for_next_act, String pre_class_name) {
        this.knowledgeList = knowledgeList;
        this.activity = activity;
        this.pre_class_name = pre_class_name;
        this.tv_header_for_next_act = tv_header_for_next_act;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.knowledge_articles_list, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final BeanKnowledgeArticles knowledge = knowledgeList.get(position);

        if (Utility.getKnowledgeUpdate().equalsIgnoreCase("Y")) {
            holder.ll_artical_menu.setVisibility(View.VISIBLE);
        } else {
            holder.ll_artical_menu.setVisibility(View.GONE);
        }

        holder.txt_articaL_name.setTypeface(Utility.getTypeFace());
        holder.tv_artical_no.setTypeface(Utility.getTypeFace());

        holder.txt_articaL_name.setText(knowledge.title);
        holder.tv_artical_no.setText("by " + knowledge.fullName + " Last modified on " + knowledge.modifiedDate);
        holder.ll_artical_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.ArticalId = knowledge.articalId;
                Log.e("ArticalId", "" + knowledge.articalId);
                Intent intent = new Intent(activity, KnowledgeArticleDetailActivity.class);
                intent.putExtra("title", tv_header_for_next_act);
                activity.startActivity(intent);
            }
        });
        holder.ll_artical_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.ArticalId = knowledge.articalId;
                Log.e("ArticalId", "" + knowledge.articalId);
                Intent intent = new Intent(activity, KnowledgeArticleDetailActivity.class);
                intent.putExtra("title", tv_header_for_next_act);
                activity.startActivity(intent);
                //activity.startActivity(new Intent(activity, KnowledgeArticleDetailActivity.class));
            }
        });

        holder.ll_artical_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showpopupmenu(view, position, knowledge.title, knowledge.description, knowledge.topicId, knowledge.articalId, knowledge.subTopicId);
            }
        });
    }


    public void showpopupmenu(View view, final int pos, final String title, final String description, final String topic_id, final String articalId, final String subtopicid) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.know_topic_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_rename:
                        renameArtical(title, description, topic_id, articalId, subtopicid);
                        break;

                    case R.id.menu_delete:
                        deleteDialoge(topic_id, pos, articalId);
                        break;

                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    private void deleteDialoge(final String topic_id, final int position, final String articalId) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Delete")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        deleteKnowledgeArtical(topic_id, position, articalId);
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void renameArtical(String title, String description, final String topic_id, final String articalId, final String subtopicid) {
        rename_artical_dialog = new Dialog(activity);
        rename_artical_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rename_artical_dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        rename_artical_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        rename_artical_dialog.setContentView(R.layout.add_article);
        Window window = rename_artical_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        TextView tv1 = (TextView) rename_artical_dialog.findViewById(R.id.tv1);
        TextView tv13 = (TextView) rename_artical_dialog.findViewById(R.id.tv13);
        TextView tv3 = (TextView) rename_artical_dialog.findViewById(R.id.tv3);
        TextView tv4 = (TextView) rename_artical_dialog.findViewById(R.id.tv4);
        TextView tv11 = (TextView) rename_artical_dialog.findViewById(R.id.tv11);
        TextView tv14 = (TextView) rename_artical_dialog.findViewById(R.id.tv14);
        TextView tv12 = (TextView) rename_artical_dialog.findViewById(R.id.tv12);


        final EditText et_artical_title = (EditText) rename_artical_dialog.findViewById(R.id.et_artical_title);
        final EditText et_artical_des = (EditText) rename_artical_dialog.findViewById(R.id.et_artical_des);

        tv1.setTypeface(Utility.getTypeFaceTab());
        tv13.setTypeface(Utility.getTypeFaceTab());
        tv11.setTypeface(Utility.getTypeFaceTab());
        tv14.setTypeface(Utility.getTypeFaceTab());
        tv12.setTypeface(Utility.getTypeFaceTab());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());

        et_artical_title.setTypeface(Utility.getTypeFace());
        et_artical_des.setTypeface(Utility.getTypeFace());

        et_artical_title.setText(title);
        et_artical_des.setText(description);

        tv1.setVisibility(View.GONE);
        tv13.setVisibility(View.VISIBLE);


        LinearLayout lnback = (LinearLayout) rename_artical_dialog.findViewById(R.id.lnback);
        LinearLayout ll_add_artical_cancel = (LinearLayout) rename_artical_dialog.findViewById(R.id.ll_add_artical_cancel);
        LinearLayout ll_artical_publish = (LinearLayout) rename_artical_dialog.findViewById(R.id.ll_artical_publish);
        LinearLayout ll_artical_rename = (LinearLayout) rename_artical_dialog.findViewById(R.id.ll_artical_rename);


        ll_artical_publish.setVisibility(View.GONE);
        ll_artical_rename.setVisibility(View.VISIBLE);


        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rename_artical_dialog.dismiss();
            }
        });

        ll_artical_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateKnowledgeArtical(articalId, et_artical_title.getText().toString(), et_artical_des.getText().toString(), topic_id, subtopicid);
            }
        });

        ll_add_artical_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rename_artical_dialog.dismiss();
            }
        });
        rename_artical_dialog.show();
    }

    public void UpdateKnowledgeArtical(final String artID, final String title, final String des, final String topic_id, final String subtopID) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Article_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Article_Update")) {
                            JSONArray array = object.getJSONArray("Knowledge_Article_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        rename_artical_dialog.dismiss();
                                        if (pre_class_name.equalsIgnoreCase("main")) {
                                            KnowledgeMain.getAllDataKnowledge();
                                        } else {
                                            KnowSubActivity.getAllDataKnowledgeSubTopics();
                                        }
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
                    map.put("ArticalId", artID);
                    map.put("Title", title);
                    map.put("Description", des);
                    map.put("TopicId", topic_id);
                    map.put("SubTopicId", subtopID);
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

    public void deleteKnowledgeArtical(final String topic_id, final int pos, final String articalId) {

        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Article_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Article_Delete")) {
                            JSONArray array = object.getJSONArray("Knowledge_Article_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        knowledgeList.remove(pos);
                                        KnowledgeMain.mAdapter.notifyDataSetChanged();
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
                    map.put("ArticalId", articalId);
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