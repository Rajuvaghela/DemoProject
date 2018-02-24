package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 03-11-2017.
 */


import android.app.Activity;
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
import com.ext.adarsh.Bean.BeanPhotoSubComment;
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

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterPhotoComentReply extends RecyclerView.Adapter<AdapterPhotoComentReply.ViewHolder> {
    Activity activity;
    static List<BeanPhotoSubComment> list = new ArrayList<>();
    ProgressDialog pd;

    public AdapterPhotoComentReply(ArrayList<BeanPhotoSubComment> list, Activity context) {
        super();
        this.activity = context;
        this.list = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_knowledge_sub_comment, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final BeanPhotoSubComment temp = list.get(position);
        holder.tv_coment_person.setTypeface(Utility.getTypeFace());
        holder.tv_comment.setTypeface(Utility.getTypeFace());
        holder.tv_comment_like.setTypeface(Utility.getTypeFace());
        holder.tv_comment_reply.setTypeface(Utility.getTypeFace());
        holder.tv_no_of_like.setTypeface(Utility.getTypeFace());
        holder.tv_coment_person.setText(temp.commentPeopleFullName);
        holder.tv_comment.setText(temp.comment);
        holder.tv_no_of_like.setText(temp.likeCount);
        holder.iv_delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopupmenu_delete(v, temp.postCommentId, position);
            }
        });

        holder.tv_comment_reply.setVisibility(View.GONE);

        if (temp.commentReplayLikeDislikeFlag.equals("A")) {
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
                likeSubSubComment(temp.postCommentId);
                int a = Integer.parseInt(holder.tv_no_of_like.getText().toString());
                holder.tv_no_of_like.setText(String.valueOf(a+1));
            }
        });
        holder.tv_comment_like_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.tv_comment_like_red.setVisibility(View.GONE);
                holder.tv_comment_like.setVisibility(View.VISIBLE);
                likeSubSubComment(temp.postCommentId);
                int a = Integer.parseInt(holder.tv_no_of_like.getText().toString());
                holder.tv_no_of_like.setText(String.valueOf(a-1));
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
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Post_Comment_Replay_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Post_Comment_Replay_Delete")) {
                            JSONArray array = object.getJSONArray("Post_Comment_Replay_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        list.remove(possition);
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

    void likeSubSubComment(final String postCommentId) {
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Sub_Sub_Post_Like, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("sub_pos_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Sub_Sub_Post_Like")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Sub_Sub_Post_Like");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        //  pd.dismiss();
                                        //  add_task_dialog.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                       /* if (AdapterActivityNewsFeedComent.manage.equals("P")) {
                                            ProfileActivity.postBackgroundRefresh postBackgroundRefresh = new ProfileActivity.postBackgroundRefresh();
                                            postBackgroundRefresh.execute();
                                        }else {
                                            activty_feed.postBackgroundRefresh postBackgroundRefresh = new activty_feed.postBackgroundRefresh();
                                            postBackgroundRefresh.execute();
                                        }*/
                                    } else {
                                        // pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //pd.dismiss();
                            } else {
                                // pd.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        showMsg(R.string.json_error);
                        //  pd.dismiss();
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("sub_pos_error", error.toString());
                    //   pd.dismiss();
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
                    map.put("PostCommentId", postCommentId);
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            // pd.dismiss();
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
          return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_coment_person, tv_comment,tv_comment_reply;
        TextView tv_comment_like, tv_no_of_like,tv_comment_like_red;
        ImageView iv_delete_comment;


        public ViewHolder(View itemView) {
            super(itemView);
            pd=Utility.getDialog(activity);
            iv_delete_comment=(ImageView)itemView.findViewById(R.id.iv_delete_comment);
            tv_coment_person = (TextView) itemView.findViewById(R.id.tv_coment_person);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_comment_reply = (TextView) itemView.findViewById(R.id.tv_comment_reply);
            tv_comment_like = (TextView) itemView.findViewById(R.id.tv_comment_like);
            tv_no_of_like = (TextView) itemView.findViewById(R.id.tv_no_of_like);
            tv_comment_like_red = (TextView) itemView.findViewById(R.id.tv_comment_like_red);

        }

    }

}
