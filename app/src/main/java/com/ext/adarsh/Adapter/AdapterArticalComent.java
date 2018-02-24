package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 01-11-2017.
 */


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.ext.adarsh.Activity.Knowledge;
import com.ext.adarsh.Activity.KnowledgeArticleDetailActivity;
import com.ext.adarsh.Bean.BeanArticalComment;
import com.ext.adarsh.Bean.BeanKnowledgeComment;
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

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

/**
 * Created by ExT-Emp-001 on 11-08-2017.
 */

public class AdapterArticalComent extends RecyclerView.Adapter<AdapterArticalComent.MyViewHolder> {

    private List<BeanArticalComment> knowledgeList;
    Activity activity;
    Dialog dd;
    ProgressDialog pd;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_coment_person, tv_comment, tv_comment_like, tv_comment_reply, tv_no_of_like,tv_comment_like_red,tv_view_reply;
        ImageView iv_coment_person, iv_delete_comment;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            iv_delete_comment = (ImageView) view.findViewById(R.id.iv_delete_comment);
            iv_coment_person = (ImageView) view.findViewById(R.id.iv_coment_person);
            tv_coment_person = (TextView) view.findViewById(R.id.tv_coment_person);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_comment_like = (TextView) view.findViewById(R.id.tv_comment_like);
            tv_comment_like_red = (TextView) view.findViewById(R.id.tv_comment_like_red);
            tv_comment_reply = (TextView) view.findViewById(R.id.tv_comment_reply);
            tv_no_of_like = (TextView) view.findViewById(R.id.tv_no_of_like);
            tv_view_reply = (TextView) view.findViewById(R.id.tv_view_reply);



            tv_coment_person.setTypeface(Utility.getTypeFace());
            tv_comment.setTypeface(Utility.getTypeFace());
            tv_comment_like.setTypeface(Utility.getTypeFace());
            tv_comment_reply.setTypeface(Utility.getTypeFace());
            tv_no_of_like.setTypeface(Utility.getTypeFace());
            tv_view_reply.setTypeface(Utility.getTypeFace());
        }
    }


    public AdapterArticalComent(List<BeanArticalComment> knowledgeList, Activity activity) {
        this.knowledgeList = knowledgeList;
        this.activity = activity;
        pd = Utility.getDialog(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_knowledge_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BeanArticalComment knowledge = knowledgeList.get(position);

        holder.iv_delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopupmenu_delete(v, knowledge.articalId, position);
            }
        });
        // holder.tv_coment_person.setText(feed_comment.fullName);
        Glide.with(activity).load(knowledge.profileImage).into(holder.iv_coment_person);

        holder.tv_coment_person.setText(knowledge.fullName);
        holder.tv_comment.setText(knowledge.comments);
        holder.tv_no_of_like.setText(knowledge.likes);
        holder.tv_view_reply.setText("View "+knowledge.article_Reply_Array.size()+" replies");

        if (knowledge.likeStatus.equals("A")) {
            holder.tv_comment_like_red.setVisibility(View.VISIBLE);
            holder.tv_comment_like.setVisibility(View.GONE);
        } else {
            holder.tv_comment_like_red.setVisibility(View.GONE);
            holder.tv_comment_like.setVisibility(View.VISIBLE);
        }

        holder.tv_comment_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.ArticleCommentId = knowledge.articleCommentId;
                // KnowledgeMain.commentReply();
                holder.tv_comment_like_red.setVisibility(View.VISIBLE);
                holder.tv_comment_like.setVisibility(View.GONE);
                KnowledgeArticleDetailActivity.knowledge_coment_like();
                int a = Integer.parseInt(holder.tv_no_of_like.getText().toString());
                holder.tv_no_of_like.setText(String.valueOf(a + 1));
            }
        });

        holder.tv_comment_like_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.ArticleCommentId = knowledge.articleCommentId;
                holder.tv_comment_like_red.setVisibility(View.GONE);
                holder.tv_comment_like.setVisibility(View.VISIBLE);
                KnowledgeArticleDetailActivity.knowledge_coment_like();
                int a = Integer.parseInt(holder.tv_no_of_like.getText().toString());
                holder.tv_no_of_like.setText(String.valueOf(a - 1));
            }
        });

        holder.tv_view_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.TopicCommentId = knowledge.articleCommentId;
                AppConstant.likes = knowledge.likes;
                String topicId = knowledge.topicId;
                String subTopicId = knowledge.subTopicId;
                String articalId = knowledge.articalId;
                String parentId = knowledge.articleCommentId;
                KnowledgeArticleDetailActivity.commentReply(
                        topicId,
                        subTopicId,
                        articalId,
                        parentId,position

                );
            }
        });

        holder.tv_comment_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.TopicCommentId = knowledge.articleCommentId;
                AppConstant.likes = knowledge.likes;
                String topicId = knowledge.topicId;
                String subTopicId = knowledge.subTopicId;
                String articalId = knowledge.articalId;
                String parentId = knowledge.articleCommentId;
                KnowledgeArticleDetailActivity.commentReply(
                        topicId,
                        subTopicId,
                        articalId,
                        parentId,position
                );
            }
        });
    }

    private void showpopupmenu_delete(View v, final String articalId, final int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.getMenuInflater()
                .inflate(R.menu.delete_post_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete_post:
                        DeleteComment(articalId,position);
                        break;
                }
                return true;
            }
        });
        popup.show(); //showing popup menu
    }

    private void DeleteComment(final String articalId, final int position) {
        if (checkConnectivity()) {
                pd.show();
                StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Post_Comment_Delete, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(AppConstant.TAG, response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.has("Post_Comment_Delete")) {
                                JSONArray array = object.getJSONArray("Post_Comment_Delete");
                                if (array.length() != 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                            pd.dismiss();
                                            knowledgeList.remove(position);
                                            notifyDataSetChanged();
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
                        map.put("PostCommentId", articalId);
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


    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }


}
