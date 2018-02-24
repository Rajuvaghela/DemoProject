package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ext.adarsh.Adapter.AdapterArticalComent;
import com.ext.adarsh.Adapter.AdapterKnowledgeSubComent;
import com.ext.adarsh.Bean.BeanArticalComment;
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

public class KnowledgeArticleDetailActivity extends AppCompatActivity implements View.OnClickListener {


    static EditText et_sub_comment;
    static boolean dialog_sub_comment_close, dialog_main_comment_close;
    @BindView(R.id.llback)
    LinearLayout llback;

    @BindView(R.id.tv_knowledge_des)
    TextView tv_knowledge_des;

    @BindView(R.id.tv_actionbar)
    TextView tv_actionbar;

    @BindView(R.id.rv_comment_list)
    RecyclerView rv_comment_list;

    @BindView(R.id.ll_comment_on_post)
    LinearLayout ll_comment_on_post;
    @BindView(R.id.lnfblike2)
    LinearLayout lnfblike2;
    @BindView(R.id.fblikegrey)
    ImageView fblikegrey;

    @BindView(R.id.fbLikered)
    ImageView fbLikered;

    @BindView(R.id.tv_artical_title)
    TextView tv_artical_title;


    String artical_title;

    @BindView(R.id.tv_total_like)
    TextView tv_total_like;

    @BindView(R.id.tv_total_comment)
    TextView tv_total_comment;
    @BindView(R.id.tv2)
    TextView tv2;


    Activity activity;
    static Activity activity2;


    ImageView iv_fb_like, iv_fb_like_red;
    static List<BeanArticalComment> artical_comment_list = new ArrayList<>();

    private RecyclerView rv_knowledge_artical;
    private AdapterArticalComent coment_adaptert;
    static AdapterKnowledgeSubComent sub_comment_adapter;
    RelativeLayout ll_popup_topic_comment;

    ProgressDialog pd;
    static ProgressDialog pd2;
    String total_like, no_of_like, like_status;
    static Dialog dd;
    RecyclerView rv_main_comment;
    static RecyclerView rv_sub_comment;

    static Dialog dialog_sub_comment;
    EditText et_comment;

    private static List<BeanArticalComment> knowledgeList;

    String TopicId, ArticalId, SubTopicId, PeopleId, parentId = "0";
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_article_detail);
        ButterKnife.bind(this);
        activity = this;
        activity2 = this;
        pd2 = Utility.getDialog(activity2);
        pd = Utility.getDialog(activity);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        tv2.setText(title);

        setFont();
        callOnCreateMethod();
        lnfblike2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knowledge_topic_like();
                if (fblikegrey.getVisibility() == View.VISIBLE) {
                    fblikegrey.setVisibility(View.GONE);
                    fbLikered.setVisibility(View.VISIBLE);
                    int a = Integer.parseInt(tv_total_like.getText().toString());
                    tv_total_like.setText(String.valueOf(a + 1));
                } else {
                    fblikegrey.setVisibility(View.VISIBLE);
                    fbLikered.setVisibility(View.GONE);
                    int a = Integer.parseInt(tv_total_like.getText().toString());
                    tv_total_like.setText(String.valueOf(a - 1));
                }
            }
        });
    }

    void knowledge_topic_like() {
        //  pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Article_Like, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("like_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Article_Like")) {
                            JSONArray array = object.getJSONArray("Knowledge_Article_Like");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                          /*              if (fblikegrey.getVisibility() == View.VISIBLE) {
                                            fblikegrey.setVisibility(View.GONE);
                                            fbLikered.setVisibility(View.VISIBLE);
                                        } else {
                                            fblikegrey.setVisibility(View.VISIBLE);
                                            fbLikered.setVisibility(View.GONE);
                                        }*/
                                        // pd2.dismiss();
                                        //  dd.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();

                                    } else {
                                        //pd2.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                // pd2.dismiss();
                            } else {
                                // pd2.dismiss();
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
                    Log.e("like_erro", error.toString());
                    // pd2.dismiss();
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
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("ArticalId", AppConstant.ArticalId);

                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            // pd2.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }

    }

    public static void knowledge_coment_like() {
        //  pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Article_Comment_Like, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("like_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Sub_Article_Like")) {
                            JSONArray array = object.getJSONArray("Knowledge_Sub_Article_Like");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {

                                        AppConstant.TopicCommentId = null;
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();

                                    } else {

                                        AppConstant.TopicCommentId = null;
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                AppConstant.TopicCommentId = null;
                                ;
                            } else {
                                AppConstant.TopicCommentId = null;

                            }
                        }
                    } catch (JSONException e) {
                        showMsg(R.string.json_error);
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
                        showMsg(R.string.json_error);
                        AppConstant.TopicCommentId = null;
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("ArticleCommentId", AppConstant.ArticleCommentId);
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


    void callOnCreateMethod() {


        llback.setOnClickListener(this);
        ll_comment_on_post.setOnClickListener(this);


        rv_knowledge_artical = (RecyclerView) findViewById(R.id.rv_knowledge_artical);
        rv_knowledge_artical.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_knowledge_artical.setLayoutManager(mLayoutManager);
        rv_knowledge_artical.setItemAnimator(new DefaultItemAnimator());

        rv_comment_list.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager_coment = new LinearLayoutManager(getApplicationContext());
        rv_comment_list.setLayoutManager(mLayoutManager_coment);
        rv_comment_list.setItemAnimator(new DefaultItemAnimator());
        getAllDataKnowledge();
    }


    void getAllDataKnowledge() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Article_Select_By_ArticalId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("know_artical_detail", response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Article_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Article_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    tv_total_like.setText(jsonArray.optJSONObject(i).getString("TotalLike").toString());
                                    like_status = jsonArray.optJSONObject(i).getString("LikeStatus").toString();
                                    total_like = jsonArray.optJSONObject(i).getString("TotalLike").toString();
                                    TopicId = AppConstant.TopicId = jsonArray.optJSONObject(i).getString("TopicId").toString();
                                    ArticalId = AppConstant.ArticalId = jsonArray.optJSONObject(i).getString("ArticalId").toString();
                                    SubTopicId = AppConstant.SubTopicId = jsonArray.optJSONObject(i).getString("SubTopicId").toString();
                                    PeopleId = AppConstant.PeopleId = "0";

                                    if (like_status.equals("A")) {
                                        fblikegrey.setVisibility(View.GONE);
                                        fbLikered.setVisibility(View.VISIBLE);
                                    } else if (like_status.equals("D")) {
                                        fblikegrey.setVisibility(View.VISIBLE);
                                        fbLikered.setVisibility(View.GONE);
                                    } else {
                                        fblikegrey.setVisibility(View.VISIBLE);
                                        fbLikered.setVisibility(View.GONE);
                                    }
                                    tv_artical_title.setText(jsonArray.optJSONObject(i).getString("Title").toString());
                                    artical_title = jsonArray.optJSONObject(i).getString("Title").toString();
                                    tv_actionbar.setText(jsonArray.optJSONObject(i).getString("Title").toString());
                                    tv_knowledge_des.setText(Html.fromHtml(jsonArray.optJSONObject(i).getString("Description").toString()));
                                }
                            } else {
                                pd.dismiss();
                            }
                            pd.dismiss();
                        }
                        /*if (object.has("Knowledge_Artical_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Knowledge_Artical_Array");
                            if (jsonArray.length() != 0) {
                                knowledgeList.clear();
                                knowledgeList.addAll((Collection<? extends BeanKnowledgeArticles>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanKnowledgeArticles>>() {
                                }.getType()));
                                mAdapter = new AdapterKnowledgeArticles(knowledgeList, activity);
                                rv_knowledge_artical.setAdapter(mAdapter);
                                rv_knowledge_artical.addOnItemTouchListener(
                                        new RecyclerItemClickListener(getApplicationContext(), rv_knowledge_artical, new RecyclerItemClickListener.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                BeanKnowledgeArticles knowledge = knowledgeList.get(position);
                                                AppConstant.ArticalId = knowledge.articalId;
                                                Log.e("ArticalId", "" + knowledge.articalId);


                                                //   startActivity(new Intent(getApplicationContext(), KnowledgeMain.class));
                                            }

                                            @Override
                                            public void onLongItemClick(View view, int position) {

                                            }
                                        })
                                );

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
                                sub_topic_adapter = new AdapterKnowledgeSubTopic(knowledgeSubTopics_list, activity);
                                rv_knowledge_sub_topic.setAdapter(sub_topic_adapter);
                                rv_knowledge_sub_topic.addOnItemTouchListener(
                                        new RecyclerItemClickListener(activity, rv_knowledge_sub_topic, new RecyclerItemClickListener.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                BeanKnowledgeSubTopic knowledge = knowledgeSubTopics_list.get(position);
                                                AppConstant.topicId = knowledge.topicId;
                                                Log.e("topicId", "" + knowledge.topicId);
                                                AppConstant.SubTopicId = knowledge.subTopicId;
                                                startActivity(new Intent(activity, KnowSubActivity.class));
                                            }

                                            @Override
                                            public void onLongItemClick(View view, int position) {

                                            }
                                        })
                                );

                                pd.dismiss();
                            } else {
                                rv_knowledge_sub_topic.setAdapter(null);
                                pd.dismiss();
                            }
                        }*/
                        //sub topic over

                        //comment code
                        if (object.has("Article_Comment_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Article_Comment_Array");
                            if (jsonArray.length() != 0) {
                                artical_comment_list.clear();
                                artical_comment_list.addAll((Collection<? extends BeanArticalComment>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanArticalComment>>() {
                                }.getType()));
                                parentId = jsonArray.optJSONObject(0).getString("ParentId").toString();
                                Log.e("parentId",parentId);
                                coment_adaptert = new AdapterArticalComent(artical_comment_list, activity);
                                rv_comment_list.setAdapter(coment_adaptert);
                                pd.dismiss();
                            } else {
                                rv_comment_list.setAdapter(null);
                                pd.dismiss();
                            }
                        }
                        //comment code over

                        if (object.has("Article_Total_Comment_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Article_Total_Comment_Array");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    no_of_like = jsonArray.optJSONObject(i).getString("TotalComment").toString();
                                    tv_total_comment.setText(jsonArray.optJSONObject(i).getString("TotalComment").toString());

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
                    map.put("ArticalId", AppConstant.ArticalId);
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
    public void onBackPressed() {
        finish();
        //startActivity(new Intent(activity, Knowledge.class));

    }

    public void setFont() {
        tv_knowledge_des.setTypeface(Utility.getTypeFace());
        tv_actionbar.setTypeface(Utility.getTypeFace());
    }


    static public void commentReply(final String topicId,
                                    final String subTopicId,
                                    final String articalId,
                                    final String parentId,
                                    final int position) {
        dialog_sub_comment = new Dialog(activity2);
        dialog_sub_comment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_sub_comment.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dialog_sub_comment.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_sub_comment.setContentView(R.layout.sub_comment_layout);

        Window window = dialog_sub_comment.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView tv_total_like = (TextView) dialog_sub_comment.findViewById(R.id.tv_total_like);
        tv_total_like.setTypeface(Utility.getTypeFace());
        tv_total_like.setText(AppConstant.likes);

        LinearLayout ll_sub_like = (LinearLayout) dialog_sub_comment.findViewById(R.id.ll_sub_like);
        ll_sub_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final BeanArticalComment knowledge = knowledgeList.get(position);
                AppConstant.ArticleCommentId = knowledge.articleCommentId;
                knowledge_coment_like();
            }
        });
        LinearLayout ll_reply_comment = (LinearLayout) dialog_sub_comment.findViewById(R.id.ll_reply_comment);

        et_sub_comment = (EditText) dialog_sub_comment.findViewById(R.id.et_sub_comment);
        rv_sub_comment = (RecyclerView) dialog_sub_comment.findViewById(R.id.rv_sub_comment);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity2);
        rv_sub_comment.setLayoutManager(mLayoutManager);
        rv_sub_comment.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager mLayoutManager_coment = new LinearLayoutManager(activity2);
        rv_sub_comment.setLayoutManager(mLayoutManager_coment);
        rv_sub_comment.setItemAnimator(new DefaultItemAnimator());

        sub_comment_adapter = new AdapterKnowledgeSubComent(artical_comment_list.get(position).article_Reply_Array, activity2);
        rv_sub_comment.setAdapter(sub_comment_adapter);

        ll_reply_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_sub_comment_close = true;
                knowledge_main_comment(topicId,
                        subTopicId,
                        articalId,
                        parentId,
                        et_sub_comment.getText().toString());

                //knowledge_sub_comment_submit(et_sub_comment.getText().toString());
            }
        });
        dialog_sub_comment.show();
    }


    private void showAlertDialog3(final String topicId, final String subTopicId, final String articalId, final String parentId) {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.comment_activityfile);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        iv_fb_like = (ImageView) dd.findViewById(R.id.iv_fb_like);
        iv_fb_like_red = (ImageView) dd.findViewById(R.id.iv_fb_like_red);

       final TextView tv_total_like = (TextView) dd.findViewById(R.id.tv_total_like);
        et_comment = (EditText) dd.findViewById(R.id.et_comment);

        et_comment.setTypeface(Utility.getTypeFace());
        tv_total_like.setTypeface(Utility.getTypeFace());
        tv_total_like.setText(total_like);


        ll_popup_topic_comment = (RelativeLayout) dd.findViewById(R.id.ll_popup_topic_comment);
        ll_popup_topic_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                knowledge_topic_like();
                if (iv_fb_like.getVisibility() == View.VISIBLE) {
                    iv_fb_like.setVisibility(View.GONE);
                    iv_fb_like_red.setVisibility(View.VISIBLE);

                    fblikegrey.setVisibility(View.GONE);
                    fbLikered.setVisibility(View.VISIBLE);
                    int a = Integer.parseInt(tv_total_like.getText().toString());
                    tv_total_like.setText(String.valueOf(a + 1));
                    tv_total_like.setText(String.valueOf(a + 1));

                } else {
                    iv_fb_like.setVisibility(View.VISIBLE);
                    iv_fb_like_red.setVisibility(View.GONE);

                    fblikegrey.setVisibility(View.VISIBLE);
                    fbLikered.setVisibility(View.GONE);
                    int a = Integer.parseInt(tv_total_like.getText().toString());
                    tv_total_like.setText(String.valueOf(a - 1));
                    tv_total_like.setText(String.valueOf(a - 1));

                }


            }
        });

        LinearLayout ll_send_comment = (LinearLayout) dd.findViewById(R.id.ll_send_comment);
        rv_main_comment = (RecyclerView) dd.findViewById(R.id.rv_main_comment);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_main_comment.setLayoutManager(mLayoutManager);
        rv_main_comment.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager mLayoutManager_coment = new LinearLayoutManager(getApplicationContext());
        rv_main_comment.setLayoutManager(mLayoutManager_coment);
        rv_main_comment.setItemAnimator(new DefaultItemAnimator());

        coment_adaptert = new AdapterArticalComent(artical_comment_list, activity);
        rv_main_comment.setAdapter(coment_adaptert);
        ll_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.TopicCommentId = "0";
                dialog_main_comment_close = true;
/*
            final String topicId,
            final String subTopicId,
            final String articalId,
            final String parentId,
            final String comment*/
                //TopicId, ArticalId, SubTopicId, PeopleId = "0";

                knowledge_main_comment(topicId, subTopicId, articalId, parentId, et_comment.getText().toString());
            }
        });


        dd.show();
    }

    static void knowledge_main_comment(final String topicId,
                                       final String subTopicId,
                                       final String articalId,
                                       final String parentId,
                                       final String comment) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Article_Comment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Knowledge_Article_Comment")) {
                            JSONArray array = object.getJSONArray("Knowledge_Article_Comment");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd2.dismiss();
                                        if (dialog_sub_comment_close) {
                                            dialog_sub_comment.dismiss();
                                            dialog_sub_comment_close = false;
                                        }
                                        if (dialog_main_comment_close) {
                                            dd.dismiss();
                                            dialog_main_comment_close = false;
                                        }
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();

                                    } else {
                                        pd2.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                pd2.dismiss();
                            } else {
                                pd2.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        pd2.dismiss();
                        showMsg(R.string.json_error);

                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("coment_erro", error.toString());
                    pd2.dismiss();
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
                        pd2.dismiss();
                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();

                    map.put("TopicId", topicId);
                    map.put("SubTopicId", subTopicId);
                    map.put("ArticalId", articalId);
                    map.put("ParentId", parentId);
                    map.put("Comment", comment);
                    map.put("LoginId", Utility.getPeopleIdPreference());

                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd2.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_comment_on_post:
                showAlertDialog3(TopicId, SubTopicId, ArticalId, parentId);
                break;
            case R.id.llback:
                finish();
                break;


        }
    }
}


