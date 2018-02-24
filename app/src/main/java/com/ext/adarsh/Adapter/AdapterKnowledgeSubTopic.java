package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 31-10-2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ext.adarsh.Activity.KnowledgeMain;
import com.ext.adarsh.Bean.BeanKnowledgeSubTopic;
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

/**
 * Created by ExT-Emp-001 on 11-08-2017.
 */

public class AdapterKnowledgeSubTopic extends RecyclerView.Adapter<AdapterKnowledgeSubTopic.MyViewHolder> {

    private List<BeanKnowledgeSubTopic> knowledgeList;
    Activity activity;
    Dialog dd;
    String tv_header_for_next_act = "";
    private Dialog addtopic;
    EditText edit_sub_topic_title, et_sub_topic_des;
    ProgressDialog pd;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtartical, txtname;

        LinearLayout ll_sub_topic_layout;
        LinearLayout ll_artical_menu;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            pd = Utility.getDialog(activity);
            txtname = (TextView) view.findViewById(R.id.txtname);
            txtartical = (TextView) view.findViewById(R.id.txtartical);
            ll_sub_topic_layout = (LinearLayout) view.findViewById(R.id.ll_sub_topic_layout);
            ll_artical_menu = (LinearLayout) view.findViewById(R.id.ll_artical_menu);

            txtname.setTypeface(Utility.getTypeFace());
            txtartical.setTypeface(Utility.getTypeFace());

        }
    }

    public AdapterKnowledgeSubTopic(List<BeanKnowledgeSubTopic> knowledgeList, Activity activity, String tv_header_for_next_act) {
        this.knowledgeList = knowledgeList;
        this.activity = activity;
        this.tv_header_for_next_act = tv_header_for_next_act;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_knowledge_sub_topic, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final BeanKnowledgeSubTopic knowledge = knowledgeList.get(position);

        holder.txtname.setText(knowledge.title);
        holder.txtartical.setText("by, " + knowledge.fullName + " Last modified on " + knowledge.modifiedDate);

        holder.ll_sub_topic_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstant.topicId = knowledge.topicId;
                Log.e("topicId", "" + knowledge.topicId);
                AppConstant.SubTopicId = knowledge.subTopicId;
                Intent intent = new Intent(activity, KnowSubActivity.class);
                intent.putExtra("title", tv_header_for_next_act);
                activity.startActivity(intent);
                // activity.startActivity(new Intent(activity, KnowSubActivity.class));
            }
        });
        holder.ll_artical_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopupmenu(v, position, knowledge.title, knowledge.description, knowledge.topicId, knowledge.subTopicId);

            }
        });
    }

    public void showpopupmenu(View view, final int pos, final String title, final String description, final String topic_id, final String subTopicId) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.know_topic_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_rename:
                        EditSubArtical(title, description, topic_id, subTopicId);
                        break;

                    case R.id.menu_delete:
                        deleteDialoge(topic_id, pos, subTopicId);
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
                                        KnowledgeMain.sub_topic_adapter.notifyDataSetChanged();
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

    private void EditSubArtical(String title, String description, final String topic_id, final String subTopicId) {
        addtopic = new Dialog(activity);
        addtopic.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addtopic.getWindow().setWindowAnimations(R.style.DialogAnimation);
        addtopic.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addtopic.setContentView(R.layout.taopic_add_dialog);

        Window window = addtopic.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        //text1,tv1,tv2


        TextView text1 = (TextView) addtopic.findViewById(R.id.text1);

        TextView tv1 = (TextView) addtopic.findViewById(R.id.tv1);
        TextView tv2 = (TextView) addtopic.findViewById(R.id.tv2);
        TextView tv4 = (TextView) addtopic.findViewById(R.id.tv4);
        TextView tv5 = (TextView) addtopic.findViewById(R.id.tv5);


        edit_sub_topic_title = (EditText) addtopic.findViewById(R.id.edit_sub_topic_title);
        et_sub_topic_des = (EditText) addtopic.findViewById(R.id.et_sub_topic_des);

        edit_sub_topic_title.addTextChangedListener(new MyTextWatcher(edit_sub_topic_title));
        et_sub_topic_des.addTextChangedListener(new MyTextWatcher(et_sub_topic_des));

        LinearLayout ll_add_sub_topic = (LinearLayout) addtopic.findViewById(R.id.ll_add_sub_topic);
        LinearLayout ll_sub_topic_cancel = (LinearLayout) addtopic.findViewById(R.id.ll_sub_topic_cancel);


        text1.setTypeface(Utility.getTypeFaceTab());

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        edit_sub_topic_title.setTypeface(Utility.getTypeFace());
        et_sub_topic_des.setTypeface(Utility.getTypeFace());

        text1.setText("Edit Sub-Topic");
        tv1.setText("Save");
        edit_sub_topic_title.setText(title);
        et_sub_topic_des.setText(description);

        LinearLayout lnback = (LinearLayout) addtopic.findViewById(R.id.lnback);

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtopic.dismiss();
            }
        });
        ll_sub_topic_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtopic.dismiss();
            }
        });

        ll_add_sub_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit_sub_topic_title.getText().toString().trim().equalsIgnoreCase("")) {
                    edit_sub_topic_title.setError("Please Write Name Of Sub-Topic");
                    requestFocus(edit_sub_topic_title);
                } else if (et_sub_topic_des.getText().toString().trim().equalsIgnoreCase("")) {
                    et_sub_topic_des.setError("Please Write Some description");
                    requestFocus(et_sub_topic_des);
                } else {
                    addSubTopic(edit_sub_topic_title.getText().toString(), et_sub_topic_des.getText().toString(), topic_id, subTopicId);
                }
            }
        });
        addtopic.show();
    }

    void addSubTopic(final String title, final String des, final String topic_id, final String subTopicId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_SubTopic_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_SubTopic_Update")) {
                            JSONArray array = object.getJSONArray("Knowledge_SubTopic_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {

                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        addtopic.dismiss();
                                        KnowledgeMain.getAllDataKnowledge();
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
                        } else {
                            pd.dismiss();
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
                    Log.e("coment_erro", error.toString());
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
                    map.put("SubTopicId", subTopicId);
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

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
/*                case R.id.et_artical_title:
                    validateTitle();

                    break;

                case R.id.et_artical_des:
                    validateDescription();
                    break;*/


                case R.id.edit_sub_topic_title:
                    validateSubTopicTitle();
                    break;
                case R.id.et_sub_topic_des:
                    validateSubTopicDes();
                    break;
            }
        }
    }
/*
    private boolean validateTitle() {
        String email = et_artical_title.getText().toString().trim();
        if (email.isEmpty()) {
            et_artical_title.setError("Please Write name of artical");
            requestFocus(et_artical_title);
            return false;
        } else {
            et_artical_title.setError(null);
        }
        return true;
    }

    private boolean validateDescription() {
        String email = et_artical_des.getText().toString().trim();
        if (email.isEmpty()) {
            et_artical_des.setError("Please Write Some description");
            requestFocus(et_artical_des);
            return false;
        } else {
            et_artical_des.setError(null);
        }
        return true;
    }*/

    private boolean validateSubTopicTitle() {
        String email = edit_sub_topic_title.getText().toString().trim();
        if (email.isEmpty()) {
            edit_sub_topic_title.setError("Please Write Name Of Sub-Topic");
            requestFocus(edit_sub_topic_title);
            return false;
        } else {
            edit_sub_topic_title.setError(null);
        }
        return true;
    }

    private boolean validateSubTopicDes() {
        String email = et_sub_topic_des.getText().toString().trim();
        if (email.isEmpty()) {
            et_sub_topic_des.setError("Please Write Some description");
            requestFocus(et_sub_topic_des);
            return false;
        } else {
            et_sub_topic_des.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }


}


