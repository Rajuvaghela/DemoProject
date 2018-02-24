package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 03-11-2017.
 */


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import com.bumptech.glide.Glide;
import com.ext.adarsh.Activity.ProfileActivity;
import com.ext.adarsh.Bean.BeanFeedNewsSubComment;
import com.ext.adarsh.Bean.BeanFeedNewsSubSubComment;
import com.ext.adarsh.Fragment.activty_feed;
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

public class AdapterActivityNewsFeedComent extends RecyclerView.Adapter<AdapterActivityNewsFeedComent.MyViewHolder> {

    private List<BeanFeedNewsSubComment> sub_comment_list;
    Activity activity;
    Dialog dd;
    Dialog dialog_sub_comment;
    EditText et_sub_comment;
    RecyclerView rv_sub_comment;
    TextView tv_total_like;
    LinearLayout ll_sub_like, ll_reply_comment;
    ProgressDialog pd;
    AdapterActivityNewsFeedComentReply sub_comment_adapter;
    public static String manage;

    boolean like_status;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_coment_person, tv_comment, tv_comment_like, tv_comment_reply, tv_no_of_like, tv_comment_like_red;
        ImageView iv_delete_comment;
        ImageView iv_coment_person;
        LinearLayout ll_reply_comment;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            ll_reply_comment = (LinearLayout) view.findViewById(R.id.ll_reply_comment);
            iv_coment_person = (ImageView) view.findViewById(R.id.iv_coment_person);
            iv_delete_comment = (ImageView) view.findViewById(R.id.iv_delete_comment);
            tv_coment_person = (TextView) view.findViewById(R.id.tv_coment_person);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_comment_like = (TextView) view.findViewById(R.id.tv_comment_like);
            tv_comment_reply = (TextView) view.findViewById(R.id.tv_comment_reply);
            tv_no_of_like = (TextView) view.findViewById(R.id.tv_no_of_like);
            tv_comment_like_red = (TextView) view.findViewById(R.id.tv_comment_like_red);

            tv_coment_person.setTypeface(Utility.getTypeFace());
            tv_comment.setTypeface(Utility.getTypeFace());
            tv_comment_like.setTypeface(Utility.getTypeFace());
            tv_comment_reply.setTypeface(Utility.getTypeFace());
            tv_no_of_like.setTypeface(Utility.getTypeFace());
            tv_comment_like_red.setTypeface(Utility.getTypeFace());


        }
    }


    public AdapterActivityNewsFeedComent(List<BeanFeedNewsSubComment> knowledgeList, Activity contex, String manage) {
        this.sub_comment_list = knowledgeList;
        this.activity = contex;
        this.manage = manage;
        pd = Utility.getDialog(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_knowledge_comment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BeanFeedNewsSubComment feed_comment = sub_comment_list.get(position);

        if (feed_comment.commentPeopleId.equalsIgnoreCase(Utility.getPeopleIdPreference())) {
            holder.iv_delete_comment.setVisibility(View.VISIBLE);
        } else {
            holder.iv_delete_comment.setVisibility(View.GONE);
        }
        holder.iv_delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopupmenu_delete(v, feed_comment.postCommentId, position);
            }
        });
        holder.tv_coment_person.setText(feed_comment.fullName);
        holder.tv_comment.setText(feed_comment.comment);
        holder.tv_no_of_like.setText(feed_comment.likeCount);
        if (feed_comment.likeStatus.equals("A")) {
            holder.tv_comment_like_red.setVisibility(View.VISIBLE);
            holder.tv_comment_like.setVisibility(View.GONE);
        } else {
            holder.tv_comment_like_red.setVisibility(View.GONE);
            holder.tv_comment_like.setVisibility(View.VISIBLE);
        }

        holder.tv_comment_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.tv_comment_like_red.setVisibility(View.VISIBLE);
                holder.tv_comment_like.setVisibility(View.GONE);
                LikeActivityFeedNewsCommentLike(feed_comment.postCommentId);

                int a = Integer.parseInt(holder.tv_no_of_like.getText().toString());
                holder.tv_no_of_like.setText(String.valueOf(a + 1));
            }
        });

        holder.tv_comment_like_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.tv_comment_like_red.setVisibility(View.GONE);
                holder.tv_comment_like.setVisibility(View.VISIBLE);
                LikeActivityFeedNewsCommentLike(feed_comment.postCommentId);

                int a = Integer.parseInt(holder.tv_no_of_like.getText().toString());
                holder.tv_no_of_like.setText(String.valueOf(a - 1));
            }
        });//iv_coment_person
        Log.e("comment_profile",""+feed_comment.postProfileImage);
        Glide.with(activity).load(feed_comment.postProfileImage).into(holder.iv_coment_person);

        holder.ll_reply_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFeedNewsCommentReply(position, feed_comment.likeCount,
                        feed_comment.postCommentId,
                        feed_comment.albumDetailId,
                        feed_comment.postId,
                        feed_comment.postPeopleId,
                        feed_comment.parentId);
            }
        });
        holder.tv_comment_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityFeedNewsCommentReply(position, feed_comment.likeCount,
                        feed_comment.postCommentId,
                        feed_comment.albumDetailId,
                        feed_comment.postId,
                        feed_comment.postPeopleId,
                        feed_comment.parentId);
            }
        });
    }

    public void showpopupmenu_delete(View view, final String postCommentId, final int position) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.delete_post_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete_post:
                        DeleteComment(postCommentId, position);
                        break;
                }
                return true;
            }
        });
        popup.show(); //showing popup menu
    }

    private void DeleteComment(final String postCommentId, final int possition) {
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
                                        sub_comment_list.remove(possition);
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
                    map.put("PostCommentId", postCommentId);
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

    void ActivityFeedNewsCommentReply(final int position, String like_count, final String postCommentId,
                                      final String albumDetailId, final String postId,
                                      final String postPeopleId, final String parentId) {
        dialog_sub_comment = new Dialog(activity);
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

        tv_total_like = (TextView) dialog_sub_comment.findViewById(R.id.tv_total_like);
        tv_total_like.setTypeface(Utility.getTypeFace());
        tv_total_like.setText(like_count);

        et_sub_comment = (EditText) dialog_sub_comment.findViewById(R.id.et_sub_comment);
        ll_reply_comment = (LinearLayout) dialog_sub_comment.findViewById(R.id.ll_reply_comment);
        ll_sub_like = (LinearLayout) dialog_sub_comment.findViewById(R.id.ll_sub_like);
        rv_sub_comment = (RecyclerView) dialog_sub_comment.findViewById(R.id.rv_sub_comment);
        ll_reply_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedNewsCommentReply(et_sub_comment.getText().toString(), postCommentId, albumDetailId, postId, postPeopleId, parentId, position);
            }
        });

        ll_sub_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeActivityFeedNewsCommentLike(postCommentId);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_sub_comment.setLayoutManager(mLayoutManager);
        rv_sub_comment.setItemAnimator(new DefaultItemAnimator());

        sub_comment_adapter = new AdapterActivityNewsFeedComentReply(sub_comment_list.get(position).sub_Sub_Comment_Array, activity);
        rv_sub_comment.setAdapter(sub_comment_adapter);
        dialog_sub_comment.show();

    }

    void FeedNewsCommentReply(final String comment_reply, final String postCommentId,
                              final String albumDetailId, final String postId,
                              final String postPeopleId, final String parentId, final int position) {
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Sub_Post_Comment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("sub_pos_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Sub_Post_Comment")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Sub_Post_Comment");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        //   dialog_sub_comment.dismiss();
                                        et_sub_comment.setText("");
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        if (manage.equals("P")) {
                                            ProfileActivity.postBackgroundRefresh postBackgroundRefresh = new ProfileActivity.postBackgroundRefresh();
                                            postBackgroundRefresh.execute();
                                        } else {
                                            activty_feed.postBackgroundRefresh postBackgroundRefresh = new activty_feed.postBackgroundRefresh();
                                            postBackgroundRefresh.execute();
                                        }

                                    } else {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                            }
                        }

                        if (object.has("Sub_Sub_Comment_Array")) {
                            JSONArray array = object.getJSONArray("Sub_Sub_Comment_Array");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {

                                    String FullName = array.optJSONObject(i).getString("FullName");
                                    String ProfileImage = array.optJSONObject(i).getString("ProfileImage");
                                    String PostProfileImage = array.optJSONObject(i).getString("PostProfileImage");
                                    String PostCommentId = array.optJSONObject(i).getString("PostCommentId");
                                    String AlbumId = array.optJSONObject(i).getString("AlbumId");
                                    String AlbumDetailId = array.optJSONObject(i).getString("AlbumDetailId");
                                    String PostId = array.optJSONObject(i).getString("PostId");
                                    String PostPeopleId = array.optJSONObject(i).getString("PostPeopleId");
                                    String CommentPeopleId = array.optJSONObject(i).getString("CommentPeopleId");
                                    String ParentId = array.optJSONObject(i).getString("ParentId");
                                    String Comment = array.optJSONObject(i).getString("Comment");
                                    String LikesUserId = array.optJSONObject(i).getString("LikesUserId");
                                    String LikeCount = array.optJSONObject(i).getString("LikeCount");
                                    String LikeStatus = array.optJSONObject(i).getString("LikeStatus");

                                    sub_comment_list.get(position).sub_Sub_Comment_Array.add(new BeanFeedNewsSubSubComment(FullName, ProfileImage, PostProfileImage, PostCommentId, AlbumId, AlbumDetailId, PostId, PostPeopleId, CommentPeopleId, ParentId, Comment, LikesUserId, LikeCount, LikeStatus));
                                    sub_comment_adapter.notifyDataSetChanged();

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
                    Log.e("sub_pos_error", error.toString());
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
                    map.put("AlbumDetailId", albumDetailId);
                    map.put("PostId", postId);
                    map.put("PostPeopleId", postPeopleId);
                    map.put("ParentId", postCommentId);
                    map.put("Comment", comment_reply);
                    map.put("PeopleId", Utility.getPeopleIdPreference());
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

    void LikeActivityFeedNewsCommentLike(final String post_comment_id) {
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Sub_Post_Like, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("sub_pos_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Sub_Post_Like")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Sub_Post_Like");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        if (manage.equals("P")) {
                                            ProfileActivity.postBackgroundRefresh postBackgroundRefresh = new ProfileActivity.postBackgroundRefresh();
                                            postBackgroundRefresh.execute();
                                        } else {
                                            activty_feed.postBackgroundRefresh postBackgroundRefresh = new activty_feed.postBackgroundRefresh();
                                            postBackgroundRefresh.execute();
                                        }
                                    } else {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
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
                    Log.e("sub_pos_error", error.toString());
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
                    map.put("PostCommentId", post_comment_id);
                    map.put("PeopleId", Utility.getPeopleIdPreference());
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
        return sub_comment_list.size();
    }


}
