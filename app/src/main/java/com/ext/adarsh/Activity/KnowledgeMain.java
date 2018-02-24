package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.ext.adarsh.Adapter.AdapterKnowledgeArticles;
import com.ext.adarsh.Adapter.AdapterKnowledgeSubTopic;
import com.ext.adarsh.Bean.BeanKnowledgeArticles;
import com.ext.adarsh.Bean.BeanKnowledgeSubTopic;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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

public class KnowledgeMain extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.llback)
    LinearLayout llback;

    static   TextView tv_knowledge_des;

    static TextView tv_actionbar;


    static TextView tv_header;

    @BindView(R.id.fab_article)
    FloatingActionButton fab_article;

    @BindView(R.id.fab_topic)
    FloatingActionButton fab_topic;

    @BindView(R.id.fab_menu)
    FloatingActionMenu fab_menu;


    static RecyclerView rv_knowledge_sub_topic;

    @BindView(R.id.tv_sub_topic_title)
    TextView tv_sub_topic_title;

    static Activity activity;
    static Activity activity2;
    private static List<BeanKnowledgeArticles> knowledgeList = new ArrayList<>();

    static List<BeanKnowledgeSubTopic> knowledgeSubTopics_list = new ArrayList<>();
    private static RecyclerView rv_knowledge_artical;
    public static AdapterKnowledgeArticles mAdapter;

   public static AdapterKnowledgeSubTopic sub_topic_adapter;

    Dialog addArticle, addtopic;
    static ProgressDialog pd;
    static ProgressDialog pd2;
    static String total_like;
    static String like_status;
    EditText et_artical_title, et_artical_des;
    EditText et_sub_topic_des, edit_sub_topic_title;

    static String tv_header_for_next_act = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_article_list);
        ButterKnife.bind(this);
        activity = this;
        activity2 = this;
        pd2 = Utility.getDialog(activity2);
        pd = Utility.getDialog(activity);

        tv_knowledge_des=(TextView)findViewById(R.id.tv_knowledge_des);
        tv_actionbar=(TextView)findViewById(R.id.tv_actionbar);
        tv_header=(TextView)findViewById(R.id.tv_header);
        rv_knowledge_sub_topic=(RecyclerView) findViewById(R.id.rv_knowledge_sub_topic);

        if (Utility.getKnowledgeAdd().equalsIgnoreCase("Y")) {
            fab_menu.setVisibility(View.VISIBLE);
        } else {
            fab_menu.setVisibility(View.GONE);
        }

        setFont();
        callOnCreateMethod();
    }


    void callOnCreateMethod() {
        tv_sub_topic_title.setTypeface(Utility.getTypeFaceTab());
        llback.setOnClickListener(this);
        fab_article.setOnClickListener(this);
        fab_topic.setOnClickListener(this);


        rv_knowledge_artical = (RecyclerView) findViewById(R.id.rv_knowledge_artical);
        rv_knowledge_artical.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_knowledge_artical.setLayoutManager(mLayoutManager);
        rv_knowledge_artical.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        rv_knowledge_sub_topic.setNestedScrollingEnabled(false);
        rv_knowledge_sub_topic.setLayoutManager(mLayoutManager2);
        rv_knowledge_sub_topic.setItemAnimator(new DefaultItemAnimator());
        getAllDataKnowledge();
    }

    public static void getAllDataKnowledge() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Topic_Select_By_TopicId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("know_artical_list", response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Knowledge_Topic_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Knowledge_Topic_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    like_status = jsonArray.optJSONObject(i).getString("LikeStatus").toString();
                                    total_like = jsonArray.optJSONObject(i).getString("TotalLike").toString();
                                    AppConstant.TopicId = jsonArray.optJSONObject(i).getString("TopicId").toString();
                                    tv_header_for_next_act = jsonArray.optJSONObject(i).getString("Title").toString();
                                    tv_header.setText(jsonArray.optJSONObject(i).getString("Title").toString());
                                    tv_actionbar.setText(jsonArray.optJSONObject(i).getString("Title").toString());
                                    tv_knowledge_des.setText((Html.fromHtml(jsonArray.optJSONObject(i).getString("Description").toString())));
                                }
                            } else {
                                pd.dismiss();
                            }
                            pd.dismiss();
                        }
                        if (object.has("Knowledge_Artical_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Knowledge_Artical_Array");
                            if (jsonArray.length() != 0) {
                                knowledgeList.clear();
                                knowledgeList.addAll((Collection<? extends BeanKnowledgeArticles>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanKnowledgeArticles>>() {
                                }.getType()));
                                mAdapter = new AdapterKnowledgeArticles(knowledgeList, activity,tv_header_for_next_act,"main");
                                rv_knowledge_artical.setAdapter(mAdapter);
                                pd.dismiss();
                            } else {
                                rv_knowledge_artical.setAdapter(null);
                                pd.dismiss();
                            }
                        }

                        //sub topic start
                        if (object.has("Knowledge_SubTopic_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Knowledge_SubTopic_Array");
                            if (jsonArray.length() != 0) {
                                knowledgeSubTopics_list.clear();
                                knowledgeSubTopics_list.addAll((Collection<? extends BeanKnowledgeSubTopic>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanKnowledgeSubTopic>>() {
                                }.getType()));
                                sub_topic_adapter = new AdapterKnowledgeSubTopic(knowledgeSubTopics_list, activity,tv_header_for_next_act);
                                rv_knowledge_sub_topic.setAdapter(sub_topic_adapter);
                                pd.dismiss();
                            } else {
                                rv_knowledge_sub_topic.setAdapter(null);
                                pd.dismiss();
                            }
                        }
                        //sub topic over

                        //comment code

                        //comment code over


                    } catch (JSONException e) {
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
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("TopicId", AppConstant.topicId);
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd.dismiss();

        }
    }

    private void addArticle() {
        addArticle = new Dialog(this);
        addArticle.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addArticle.getWindow().setWindowAnimations(R.style.DialogAnimation);
        addArticle.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addArticle.setContentView(R.layout.add_article);

        Window window = addArticle.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        TextView tv2 = (TextView) addArticle.findViewById(R.id.tv2);
        TextView tv3 = (TextView) addArticle.findViewById(R.id.tv3);
        TextView tv4 = (TextView) addArticle.findViewById(R.id.tv4);
        TextView tv5 = (TextView) addArticle.findViewById(R.id.tv5);
        TextView tv6 = (TextView) addArticle.findViewById(R.id.tv6);
        TextView tv7 = (TextView) addArticle.findViewById(R.id.tv7);
        TextView tv8 = (TextView) addArticle.findViewById(R.id.tv8);
        TextView tv9 = (TextView) addArticle.findViewById(R.id.tv9);
        TextView tv1 = (TextView) addArticle.findViewById(R.id.tv1);
        TextView tv11 = (TextView) addArticle.findViewById(R.id.tv11);
        TextView tv12 = (TextView) addArticle.findViewById(R.id.tv12);

        EditText edit1 = (EditText) addArticle.findViewById(R.id.edit1);
        et_artical_title = (EditText) addArticle.findViewById(R.id.et_artical_title);
        et_artical_des = (EditText) addArticle.findViewById(R.id.et_artical_des);

        et_artical_title.addTextChangedListener(new MyTextWatcher(et_artical_title));
        et_artical_des.addTextChangedListener(new MyTextWatcher(et_artical_des));

        LinearLayout ll_artical_publish = (LinearLayout) addArticle.findViewById(R.id.ll_artical_publish);
        LinearLayout ll_add_artical_cancel = (LinearLayout) addArticle.findViewById(R.id.ll_add_artical_cancel);


        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());
        tv9.setTypeface(Utility.getTypeFace());

        tv1.setTypeface(Utility.getTypeFaceTab());
        tv11.setTypeface(Utility.getTypeFaceTab());
        tv12.setTypeface(Utility.getTypeFaceTab());

        edit1.setTypeface(Utility.getTypeFace());
        et_artical_title.setTypeface(Utility.getTypeFace());
        et_artical_des.setTypeface(Utility.getTypeFace());


        LinearLayout lnback = (LinearLayout) addArticle.findViewById(R.id.lnback);
        ll_artical_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_artical_title.getText().toString().trim().equalsIgnoreCase("")) {
                    et_artical_title.setError("Please Write name of artical");
                    requestFocus(et_artical_title);
                } else if (et_artical_des.getText().toString().trim().equalsIgnoreCase("")) {
                    et_artical_des.setError("Please Write Some description");
                    requestFocus(et_artical_des);
                } else {
                    PublishArtical(et_artical_title.getText().toString(), et_artical_des.getText().toString());
                }


            }
        });


        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArticle.dismiss();
            }
        });
        ll_add_artical_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArticle.dismiss();
            }
        });

        addArticle.show();
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
                case R.id.et_artical_title:
                    validateTitle();

                    break;

                case R.id.et_artical_des:
                    validateDescription();
                    break;


                case R.id.edit_sub_topic_title:
                    validateSubTopicTitle();
                    break;


                case R.id.et_sub_topic_des:
                    validateSubTopicDes();
                    break;


            }
        }
    }

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
    }

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
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    void PublishArtical(final String title, final String des) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Article_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Article_Add")) {
                            JSONArray array = object.getJSONArray("Knowledge_Article_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        addArticle.dismiss();
                                        getAllDataKnowledge();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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
                    map.put("TopicId", AppConstant.topicId);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }

    }

    private void addSubArtical() {
        addtopic = new Dialog(this);
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
                    addSubTopic(edit_sub_topic_title.getText().toString(), et_sub_topic_des.getText().toString());
                }
            }
        });


        addtopic.show();
    }

    void addSubTopic(final String title, final String des) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_SubTopic_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_SubTopic_Add")) {
                            JSONArray array = object.getJSONArray("Knowledge_SubTopic_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {

                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        addtopic.dismiss();
                                        getAllDataKnowledge();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
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
                    map.put("TopicId", AppConstant.topicId);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void setFont() {
        tv_knowledge_des.setTypeface(Utility.getTypeFace());
        tv_actionbar.setTypeface(Utility.getTypeFace());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lncomment:

                break;
            case R.id.llback:
                startActivity(new Intent(activity, Knowledge.class));
                finish();
                break;
            case R.id.fab_article:
                addArticle();
                fab_menu.close(true);
                break;
            case R.id.fab_topic:
                addSubArtical();
                fab_menu.close(true);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, Knowledge.class));
        finish();
    }
}
