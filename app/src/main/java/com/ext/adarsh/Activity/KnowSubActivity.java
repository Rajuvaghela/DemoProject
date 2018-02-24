package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.ext.adarsh.Bean.BeanKnowledgeArticles;
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

public class KnowSubActivity extends AppCompatActivity implements View.OnClickListener {


    static Activity activity2;
       static TextView tv_sub_topic_des;

    TextView tv2;
    static TextView tv3;

    static TextView tv_actionbar;
    @BindView(R.id.llback)
    LinearLayout llback;

    static RecyclerView rv_knowledge_artical;
    static ProgressDialog pd;
    ProgressDialog pd2;
    static Activity activity;
    static String no_of_sub_topic_like;
    static String total_like;
    static String like_status;
    @BindView(R.id.add_artical_float)
    FloatingActionButton add_artical_float;
    Dialog addArticle;
    private static List<BeanKnowledgeArticles> knowledgeList = new ArrayList<>();
    private static AdapterKnowledgeArticles mAdapter;
    private static String title;

    public static void knowledge_coment_like() {
        //  pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Topic_Comment_Like, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("like_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Sub_Topic_Like")) {
                            JSONArray array = object.getJSONArray("Knowledge_Sub_Topic_Like");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        //  pd2.dismiss();
                                        //  dd.dismiss();
                                        AppConstant.TopicCommentId = null;
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();

                                    } else {
                                        //  pd2.dismiss();
                                        AppConstant.TopicCommentId = null;
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                }
                                AppConstant.TopicCommentId = null;
                                //  pd2.dismiss();
                            } else {
                                AppConstant.TopicCommentId = null;
                                //    pd2.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        //  pd2.dismiss();
                        AppConstant.TopicCommentId = null;
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AppConstant.TopicCommentId = null;
                    Log.e("like_erro", error.toString());
                    //  pd2.dismiss();
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
                        AppConstant.TopicCommentId = null;
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("TopicCommentId", AppConstant.TopicCommentId);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            AppConstant.TopicCommentId = null;
            //  pd2.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_sub_topic);
        ButterKnife.bind(this);
        activity = this;
        activity2 = this;
        pd2 = Utility.getDialog(activity2);
        pd = Utility.getDialog(activity);
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");

        tv_sub_topic_des=(TextView)findViewById(R.id.tv_sub_topic_des);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv_actionbar=(TextView)findViewById(R.id.tv_actionbar);
        rv_knowledge_artical=(RecyclerView)findViewById(R.id.rv_knowledge_artical);
        tv2.setText(title);



        if (Utility.getKnowledgeAdd().equalsIgnoreCase("Y")) {
            add_artical_float.setVisibility(View.VISIBLE);
        } else {
            add_artical_float.setVisibility(View.GONE);
        }

        llback.setOnClickListener(this);
        add_artical_float.setOnClickListener(this);


        rv_knowledge_artical.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager_coment = new LinearLayoutManager(getApplicationContext());
        rv_knowledge_artical.setLayoutManager(mLayoutManager_coment);
        rv_knowledge_artical.setItemAnimator(new DefaultItemAnimator());


        tv_actionbar.setTypeface(Utility.getTypeFaceTab());
        tv_sub_topic_des.setTypeface(Utility.getTypeFace());

        getAllDataKnowledgeSubTopics();

    }

   public static void getAllDataKnowledgeSubTopics() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_SubTopic_Select_By_SubTopicId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    AppConstant.SubTopicId = null;
                    Log.e("know_sub_topic", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("SubTopic_Array")) {
                            JSONArray jsonArray = object.getJSONArray("SubTopic_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    like_status = jsonArray.optJSONObject(i).getString("LikeStatus").toString();
                                    total_like = jsonArray.optJSONObject(i).getString("TotalLike").toString();
                                    AppConstant.TopicId = jsonArray.optJSONObject(i).getString("TopicId").toString();
                                    AppConstant.SubTopicId = jsonArray.optJSONObject(i).getString("SubTopicId").toString();
                                    tv_actionbar.setText(jsonArray.optJSONObject(i).getString("SubTopicTitle").toString());
                                    tv3.setText(jsonArray.optJSONObject(i).getString("SubTopicTitle").toString());
                                    tv_sub_topic_des.setText((Html.fromHtml(jsonArray.optJSONObject(i).getString("SubTopicDescription").toString())));
                                }
                            } else {
                                pd.dismiss();
                            }
                            pd.dismiss();
                        }

                        if (object.has("Knowledge_SubTopic_Artical_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Knowledge_SubTopic_Artical_Array");
                            if (jsonArray.length() != 0) {
                                knowledgeList.clear();
                                knowledgeList.addAll((Collection<? extends BeanKnowledgeArticles>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanKnowledgeArticles>>() {
                                }.getType()));
                                mAdapter = new AdapterKnowledgeArticles(knowledgeList, activity,title,"sub");
                                rv_knowledge_artical.setAdapter(mAdapter);

                                pd.dismiss();
                            } else {
                                rv_knowledge_artical.setAdapter(null);
                                pd.dismiss();
                            }
                        }


/*                        if (object.has("SubTopic_Comment_Array")) {
                            JSONArray jsonArray = object.getJSONArray("SubTopic_Comment_Array");
                            if (jsonArray.length() != 0) {
                                knowledge_coment_List.clear();
                                knowledge_coment_List.addAll((Collection<? extends BeanKnowledgeComment>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanKnowledgeComment>>() {
                                }.getType()));

                                coment_adaptert = new AdapterKnowledgeSubTopicComent(knowledge_coment_List, activity);
                                rv_knowledge_artical.setAdapter(coment_adaptert);

                                pd.dismiss();
                            } else {
                                //  rv_comment_list.setAdapter(null);
                                pd.dismiss();
                            }
                        }*/
                        //comment code over

                        if (object.has("SubTopic_Total_Comment_Array")) {
                            JSONArray jsonArray = object.getJSONArray("SubTopic_Total_Comment_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    no_of_sub_topic_like = jsonArray.optJSONObject(i).getString("TotalComment").toString();
                                }
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
                    map.put("SubTopicId", AppConstant.SubTopicId);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.llback:
                startActivity(new Intent(activity, KnowledgeMain.class));
                finish();
                break;
            case R.id.add_artical_float:
                addArticle(AppConstant.TopicId, AppConstant.SubTopicId);
                break;
        }
    }

    private void addArticle(final String topic_id, final String sub_topic_id) {
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


        TextView tv1 = (TextView) addArticle.findViewById(R.id.tv1);
        TextView tv2 = (TextView) addArticle.findViewById(R.id.tv2);
        TextView tv3 = (TextView) addArticle.findViewById(R.id.tv3);
        TextView tv4 = (TextView) addArticle.findViewById(R.id.tv4);
        TextView tv5 = (TextView) addArticle.findViewById(R.id.tv5);
        TextView tv6 = (TextView) addArticle.findViewById(R.id.tv6);
        TextView tv7 = (TextView) addArticle.findViewById(R.id.tv7);
        TextView tv8 = (TextView) addArticle.findViewById(R.id.tv8);
        TextView tv9 = (TextView) addArticle.findViewById(R.id.tv9);

        TextView tv11 = (TextView) addArticle.findViewById(R.id.tv11);
        TextView tv12 = (TextView) addArticle.findViewById(R.id.tv12);

        EditText edit1 = (EditText) addArticle.findViewById(R.id.edit1);
        final EditText et_artical_title = (EditText) addArticle.findViewById(R.id.et_artical_title);
        final EditText et_artical_des = (EditText) addArticle.findViewById(R.id.et_artical_des);


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
                PublishArtical(et_artical_title.getText().toString(), et_artical_des.getText().toString(), topic_id, sub_topic_id);
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

    void PublishArtical(final String title, final String des, final String topic_id, final String subTopic_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_SubTopic_Article_Add, new Response.Listener<String>() {
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
                                        getAllDataKnowledgeSubTopics();
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
                    map.put("TopicId", topic_id);
                    map.put("SubTopicId", subTopic_id);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, KnowledgeMain.class));
        finish();
    }
}
