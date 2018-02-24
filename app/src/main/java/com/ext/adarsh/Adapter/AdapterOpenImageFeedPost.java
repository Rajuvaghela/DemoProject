package com.ext.adarsh.Adapter;

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
import com.bumptech.glide.Glide;
import com.ext.adarsh.Bean.BeanFeddPostImage;
import com.ext.adarsh.Bean.BeanFeedNews;
import com.ext.adarsh.Bean.BeanPhotoComment;
import com.ext.adarsh.Bean.BeanPhotoSubComment;
import com.ext.adarsh.Bean.ModelTagFriendName;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.VMVideoView;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

/**
 * Created by ExT-Emp-001 on 02-11-2017.
 */

public class AdapterOpenImageFeedPost extends RecyclerView.Adapter<AdapterOpenImageFeedPost.ViewHolder> {
    Activity activity;
    static Activity activity2;
    static List<BeanFeddPostImage> bean_feed_news_list = new ArrayList<>();

    static Dialog dd;
    ProgressDialog pd;
    static ProgressDialog pd2;
    String total_like;
    boolean like_status;
    Dialog dialog_open_liked_PersonName;
    List<ModelTagFriendName> modelTagFriendNames = new ArrayList<>();
    List<BeanFeedNews> beanFeedNewses;
    int new_position_from_pre_class;

    ImageView iv_fb_like, iv_fb_like_red;
    RelativeLayout ll_popup_topic_comment;
    public static EditText et_comment;
    RecyclerView rv_main_comment;
    AdapterPhotoComment coment_adaptert;
    String filepath;
    ArrayList<BeanPhotoComment> beanPhotoComments = new ArrayList<>();
    public AdapterOpenImageFeedPost(Activity context, List<BeanFeddPostImage> bean_feed_news_list, int new_position_from_pre_class, List<BeanFeedNews> beanFeedNewses) {
        this.activity = context;
        this.activity2 = context;
        this.beanFeedNewses = beanFeedNewses;
        this.bean_feed_news_list = bean_feed_news_list;
        this.new_position_from_pre_class = new_position_from_pre_class;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_multi_image_open, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int p) {

        final BeanFeddPostImage pos = bean_feed_news_list.get(p);
        pd2 = Utility.getDialog(activity2);
        pd = Utility.getDialog(activity);


        holder.iv_delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupmenu_delete(view, pos.postId, p);
            }
        });

        holder.iv_like_person_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLikedPersonName(p);
            }
        });

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        holder.rv_post_heding.setLayoutManager(flowLayoutManager);

        holder.tv_post_date_time.setTypeface(Utility.getTypeFace());
        holder.tv_post_title.setTypeface(Utility.getTypeFace());
        holder.tv_post_des.setTypeface(Utility.getTypeFace());
        holder.tv_total_like.setTypeface(Utility.getTypeFace());
        holder.tv_total_comment.setTypeface(Utility.getTypeFace());
        holder.tv_like.setTypeface(Utility.getTypeFace());
        holder.tv_comment.setTypeface(Utility.getTypeFace());
        holder.tv_share.setTypeface(Utility.getTypeFace());
        holder.ll_share_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.ll_comment_on_post.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        photoCommentByPhotoId(pos.albumDetailId, pos.postId, beanFeedNewses.get(new_position_from_pre_class).peopleId);
                      /*  total_like = pos.likeCount;
                        Log.e("post_people_id", "" + pos.peopleId);
                        //  CommentDialod(pos.postId, position, pos.albumDetailId, pos.peopleId);*/
                    }
                });

        holder.lnfblike2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFeedPostLike(pos.postId);
                //   if (like_status) {
/*                if (holder.fblikegrey.getVisibility() == View.VISIBLE) {
                    holder.fblikegrey.setVisibility(View.GONE);
                    holder.fbLikered.setVisibility(View.VISIBLE);
                    int a = Integer.parseInt(holder.tv_total_like.getText().toString());
                    holder.tv_total_like.setText(String.valueOf(a + 1));
                } else {
                    holder.fblikegrey.setVisibility(View.VISIBLE);
                    holder.fbLikered.setVisibility(View.GONE);
                    int a = Integer.parseInt(holder.tv_total_like.getText().toString());
                    holder.tv_total_like.setText(String.valueOf(a - 1));
                }*/
                //  }
            }
        });

        holder.ll_post_image_or_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        holder.tv_post_person_name.setText(beanFeedNewses.get(new_position_from_pre_class).fullName);

        Glide.with(activity).load(beanFeedNewses.get(new_position_from_pre_class).profileImage).into(holder.iv_small_profile_img);
/*        if (pos.likeStatus.equals("D")) {
            holder.fblikegrey.setVisibility(View.VISIBLE);
            holder.fbLikered.setVisibility(View.GONE);

        } else {
            holder.fblikegrey.setVisibility(View.GONE);
            holder.fbLikered.setVisibility(View.VISIBLE);
        }*/
        holder.tv_post_date_time.setText(beanFeedNewses.get(new_position_from_pre_class).postDateTime);
/*
        if (pos.description == null) {
            holder.ln_desc.setVisibility(View.GONE);
        } else {
            holder.tv_post_title.setText(pos.description);
        }
        if (pos.isFile.equalsIgnoreCase("D")) {
            holder.tv_post_des.setText(pos.post);
            //  holder.iv_user_post_image.setVisibility(View.GONE);
            holder.tv_post_des.setVisibility(View.VISIBLE);
        } else {
            if (pos.fileType.equals("I")) {

            } else {
                //   holder.iv_user_post_image.setVisibility(View.GONE);
                holder.videoplayer.setVisibility(View.VISIBLE);
                setVideo(pos.post, holder);
            }
            holder.tv_post_des.setVisibility(View.GONE);
        }*/

        holder.iv_user_post_image.setVisibility(View.VISIBLE);
        holder.videoplayer.setVisibility(View.GONE);
        Glide.with(activity).load(pos.filePath).into(holder.iv_user_post_image);

        //  holder.tv_total_like.setText(pos.likeCount);
        //  holder.tv_total_comment.setText(pos.totalComment + " Comments");

     /*   modelTagFriendNames.clear();
        ModelTagFriendName temp = new ModelTagFriendName();
        temp.setPeople_name(pos.fullName);
        temp.setPeople_id(pos.peopleId);
        temp.setFlag("Y");
        temp.setPeople_index("1");
        modelTagFriendNames.add(temp);

        if (pos.tagFlag.equalsIgnoreCase("Y")) {
            ModelTagFriendName temp5 = new ModelTagFriendName();
            temp5.setPeople_name(" is with ");
            temp5.setPeople_id("");
            temp5.setFlag("N");
            temp5.setPeople_index("0");
            modelTagFriendNames.add(temp5);

            ModelTagFriendName temp2 = new ModelTagFriendName();
            temp2.setPeople_name(pos.tag_Friend_One_Array.get(0).personsName);
            temp2.setPeople_id(pos.tag_Friend_One_Array.get(0).personsId);
            temp2.setFlag("Y");
            temp2.setPeople_index("2");
            modelTagFriendNames.add(temp2);

        } else {

        }
        if (pos.totalPersons.equalsIgnoreCase("2")) {
            ModelTagFriendName temp4 = new ModelTagFriendName();
            temp4.setPeople_name(" and ");
            temp4.setPeople_id("");
            temp4.setFlag("N");
            temp4.setPeople_index("0");
            modelTagFriendNames.add(temp4);
            ModelTagFriendName temp3 = new ModelTagFriendName();
            temp3.setPeople_name(pos.tag_Friend_More_Array.get(0).personsName);
            temp3.setPeople_id(pos.tag_Friend_More_Array.get(0).personsId);
            temp3.setFlag("Y");
            temp3.setPeople_index("3");
            modelTagFriendNames.add(temp3);
        } else if (pos.tag_Friend_More_Array.isEmpty()) {
            ModelTagFriendName temp2 = new ModelTagFriendName();
            temp2.setPeople_name("");
            temp2.setPeople_id("0");
            temp2.setFlag("Y");
            temp2.setPeople_index("0");
            modelTagFriendNames.add(temp2);
        } else {
            ModelTagFriendName temp4 = new ModelTagFriendName();
            temp4.setPeople_name(" and ");
            temp4.setPeople_id("");
            temp4.setFlag("N");
            temp4.setPeople_index("0");
            modelTagFriendNames.add(temp4);

            ModelTagFriendName temp2 = new ModelTagFriendName();
            temp2.setPeople_name(" " + pos.totalPersons + " Others");
            temp2.setPeople_id("0");
            temp2.setFlag("Y");
            temp2.setPeople_index("3");
            modelTagFriendNames.add(temp2);
        }

        AdapterTagPersonName adapterPollsOption = new AdapterTagPersonName(activity, modelTagFriendNames, pos.tag_Friend_More_Array);
        holder.rv_post_heding.setAdapter(adapterPollsOption);*/
    }

    private void photoCommentByPhotoId(final String albumDetailId, final String postId, final String peopleId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Photo_Comment_By_PhotoId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Photo_Comment_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Photo_Comment_Array");
                            if (jsonArray.length() != 0) {
                                beanPhotoComments.clear();
                                beanPhotoComments.addAll((Collection<? extends BeanPhotoComment>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPhotoComment>>() {
                                }.getType()));
                                CommentDialod(albumDetailId, postId, peopleId);
                                pd.dismiss();
                            } else {
                                CommentDialod(albumDetailId, postId, peopleId);
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
                    map.put("PhotoId", albumDetailId);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("PostId", postId);
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

    private void CommentDialod(final String albumDetailId, final String postId, final String peopleId) {
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

        ll_popup_topic_comment = (RelativeLayout) dd.findViewById(R.id.ll_popup_topic_comment);

        final TextView tv_total_like = (TextView) dd.findViewById(R.id.tv_total_like);
        et_comment = (EditText) dd.findViewById(R.id.et_comment);

        et_comment.setTypeface(Utility.getTypeFace());
        tv_total_like.setTypeface(Utility.getTypeFace());
        tv_total_like.setText(tv_total_like.getText().toString());

        LinearLayout ll_send_comment = (LinearLayout) dd.findViewById(R.id.ll_send_comment);
        rv_main_comment = (RecyclerView) dd.findViewById(R.id.rv_main_comment);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_main_comment.setLayoutManager(mLayoutManager);
        rv_main_comment.setItemAnimator(new DefaultItemAnimator());

/*        if (fblikegrey.getVisibility() == View.VISIBLE) {
            iv_fb_like.setVisibility(View.VISIBLE);
            iv_fb_like_red.setVisibility(View.GONE);
        } else {
            iv_fb_like.setVisibility(View.GONE);
            iv_fb_like_red.setVisibility(View.VISIBLE);
        }*/

        ll_popup_topic_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhotoLikeDislike(albumDetailId);
                //   if (like_status) {
              /*  if (iv_fb_like.getVisibility() == View.VISIBLE) {
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

                }*/
                //    }
            }
        });

        coment_adaptert = new AdapterPhotoComment(beanPhotoComments, activity, "N", albumDetailId);
        rv_main_comment.setAdapter(coment_adaptert);
        ll_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoComment(albumDetailId, postId, peopleId, et_comment.getText().toString());
            }
        });
        dd.show();
    }


    /*  public void PhotoComment(final String albumDetailId, final String postId, final String peopleId, final String comment) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Photo_Comment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment1_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Photo_Comment")) {
                            JSONArray array = object.getJSONArray("Photo_Comment");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        et_comment.setText("");
                                    *//*    if (dialog_sub_comment_close) {
                                            // dialog_sub_comment.dismiss();
                                            dialog_sub_comment_close = false;
                                        }
                                        if (dialog_main_comment_close) {
                                            //  dd.dismiss();
                                            et_comment.setText("");
                                            dialog_main_comment_close = false;
                                        }
                                        int a  = Integer.parseInt(tv_total_comment.getText().toString());
                                        tv_total_comment.setText(String.valueOf(a+1));*//*

                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }

                        if (object.has("Sub_Comment_Array")) {
                            JSONArray array = object.getJSONArray("Sub_Comment_Array");
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

                                    *//*ArrayList<BeanFeedNewsSubSubComment> sub_Comment_Array = new ArrayList<>();
                                    bean_feed_news_list.get(positon).sub_Comment_Array.add(new BeanFeedNewsSubComment(FullName,ProfileImage,PostProfileImage,PostCommentId,AlbumId,AlbumDetailId,PostId,PostPeopleId,CommentPeopleId,ParentId,Comment,LikesUserId,LikeCount,LikeStatus,sub_Comment_Array));
                                    coment_adaptert.notifyDataSetChanged();*//*

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
                    Log.e("coment1_erro", error.toString());
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
                    map.put("PostPeopleId", peopleId);
                    map.put("Comment", comment);
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
    }*/


    public void PhotoComment(final String albumDetailId, final String postId, final String peopleId, final String comment) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Photo_Comment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment1_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Photo_Comment")) {
                            JSONArray array = object.getJSONArray("Photo_Comment");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        et_comment.setText("");
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                            }
                        }

                        if (object.has("Photo_Comment_Array")) {
                            JSONArray array = object.getJSONArray("Photo_Comment_Array");
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

                                    ArrayList<BeanPhotoSubComment> beanPhotoSubComment = new ArrayList<>();
                                    beanPhotoComments.add(new BeanPhotoComment(FullName,ProfileImage,PostProfileImage,PostCommentId,AlbumId,AlbumDetailId,PostId,PostPeopleId,CommentPeopleId,ParentId,Comment,LikesUserId,LikeCount,LikeStatus,beanPhotoSubComment));

                                    coment_adaptert.notifyDataSetChanged();
                                    coment_adaptert.notifyItemInserted(beanPhotoComments.size());
                                    rv_main_comment.scrollToPosition(beanPhotoComments.size());
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
                    Log.e("coment1_erro", error.toString());
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
                    map.put("PostPeopleId", peopleId);
                    map.put("Comment", comment);
                    map.put("LoginId", Utility.getPeopleIdPreference());


                    Log.e("AlbumDetailId", albumDetailId);
                    Log.e("PostId", postId);
                    Log.e("PostPeopleId", peopleId);
                    Log.e("Comment", comment);
                    Log.e("LoginId", Utility.getPeopleIdPreference());

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

    void PhotoLikeDislike(final String post_id) {
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Photo_Like_Dislike, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Photo_Like")) {
                            JSONArray array = object.getJSONArray("Photo_Like");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                      /*                  if (fblikegrey.getVisibility() == View.VISIBLE) {
                                            fblikegrey.setVisibility(View.GONE);
                                            fbLikered.setVisibility(View.VISIBLE);
                                        } else {
                                            fblikegrey.setVisibility(View.VISIBLE);
                                            fbLikered.setVisibility(View.GONE);
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
                    Log.e(AppConstant.TAG, error.toString());
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
                    map.put("AlbumDetailId", post_id);
                    map.put("LoginId", Utility.getPeopleIdPreference());
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

    private void OpenLikedPersonName(int position) {
        dialog_open_liked_PersonName = new Dialog(activity2);
        dialog_open_liked_PersonName.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_open_liked_PersonName.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog_open_liked_PersonName.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_open_liked_PersonName.setContentView(R.layout.list_of_like_friend_friends);

        Window window = dialog_open_liked_PersonName.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnmainback = (LinearLayout) dialog_open_liked_PersonName.findViewById(R.id.lnmainback);
        RecyclerView rv_tag_friend_list = (RecyclerView) dialog_open_liked_PersonName.findViewById(R.id.rv_tag_friend_list);
        rv_tag_friend_list.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true);
        rv_tag_friend_list.setLayoutManager(lnmanager2);
        rv_tag_friend_list.setItemAnimator(new DefaultItemAnimator());

/*        AdapterLikePeopleList adapterLikePeopleList = new AdapterLikePeopleList(activity, bean_feed_news_list.get(position).post_Like_People_Array);
        rv_tag_friend_list.setAdapter(adapterLikePeopleList);*/
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_open_liked_PersonName.dismiss();
            }
        });

        dialog_open_liked_PersonName.show();
    }

    public void showpopupmenu_delete(View view, final String postId, final int position) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.delete_post_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete_post:
                        DeletePost(postId, position);
                        break;
                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    private void DeletePost(final String postId, final int possition) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Post_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Post_Delete")) {
                            JSONArray array = object.getJSONArray("Post_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        bean_feed_news_list.remove(possition);
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
                    map.put("PostId", postId);
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

    void ActivityFeedPostLike(final String post_id) {
        like_status = false;
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_Like, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Post_Like")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Post_Like");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        like_status = true;
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();

                                    } else {
                                        // pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                }
                                //pd.dismiss();
                            } else {
                                // pd.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        //  pd.dismiss();

                        showMsg(R.string.json_error);
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(AppConstant.TAG, error.toString());
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
                    map.put("PostId", post_id);
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
        return bean_feed_news_list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView tv_post_date_time, tv_post_title;
        TextView tv_post_des, tv_total_like, tv_total_comment, tv_like, tv_comment, tv_share;
        LinearLayout ll_share_post, ll_comment_on_post, lnfblike2, ln_desc;
        LinearLayout ll_post_image_or_des, ll_whats_on_your_mind;
        ImageView iv_small_profile_img, iv_profile_img;
        ImageView fblikegrey, fbLikered, iv_delete_post, iv_like_person_list;
        VMVideoView videoplayer;

        TextView tv1, tv2, tv3;
        RecyclerView rv_post_heding;

        ImageView iv_1, iv21, iv_22, iv31, iv_32, iv_33, iv_41, iv_42, iv_43, iv44, iv_51, iv_52, iv_53, iv_54, iv_55;
        ImageView iv_61, iv_62, iv_63, iv_64, iv_65;
        LinearLayout /*ll_image1,*/ ll_image2, ll_image3, ll_image4, ll_image5, ll_image6;
        TextView tv_more_number_of_image;
        ImageView iv_user_post_image;
        TextView tv_post_person_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_post_person_name = (TextView) itemView.findViewById(R.id.tv_post_person_name);
            tv_more_number_of_image = (TextView) itemView.findViewById(R.id.tv_more_number_of_image);
            iv_1 = (ImageView) itemView.findViewById(R.id.iv_1);
            iv21 = (ImageView) itemView.findViewById(R.id.iv21);
            iv_22 = (ImageView) itemView.findViewById(R.id.iv_22);
            iv31 = (ImageView) itemView.findViewById(R.id.iv31);
            iv_32 = (ImageView) itemView.findViewById(R.id.iv_32);
            iv_33 = (ImageView) itemView.findViewById(R.id.iv_33);
            iv_33 = (ImageView) itemView.findViewById(R.id.iv_33);
            iv_41 = (ImageView) itemView.findViewById(R.id.iv_41);
            iv_41 = (ImageView) itemView.findViewById(R.id.iv_41);
            iv_42 = (ImageView) itemView.findViewById(R.id.iv_42);
            iv_43 = (ImageView) itemView.findViewById(R.id.iv_43);
            iv44 = (ImageView) itemView.findViewById(R.id.iv44);
            iv_51 = (ImageView) itemView.findViewById(R.id.iv_51);
            iv_52 = (ImageView) itemView.findViewById(R.id.iv_52);
            iv_53 = (ImageView) itemView.findViewById(R.id.iv_53);
            iv_54 = (ImageView) itemView.findViewById(R.id.iv_54);
            iv_55 = (ImageView) itemView.findViewById(R.id.iv_55);
            iv_61 = (ImageView) itemView.findViewById(R.id.iv_61);
            iv_62 = (ImageView) itemView.findViewById(R.id.iv_62);
            iv_63 = (ImageView) itemView.findViewById(R.id.iv_63);
            iv_64 = (ImageView) itemView.findViewById(R.id.iv_64);
            iv_65 = (ImageView) itemView.findViewById(R.id.iv_65);
            //  ll_image1 = (LinearLayout) itemView.findViewById(R.id.ll_image1);
            ll_image2 = (LinearLayout) itemView.findViewById(R.id.ll_image2);
            ll_image3 = (LinearLayout) itemView.findViewById(R.id.ll_image3);
            ll_image4 = (LinearLayout) itemView.findViewById(R.id.ll_image4);
            ll_image5 = (LinearLayout) itemView.findViewById(R.id.ll_image5);
            ll_image6 = (LinearLayout) itemView.findViewById(R.id.ll_image6);

            rv_post_heding = (RecyclerView) itemView.findViewById(R.id.rv_post_heding);
            iv_delete_post = (ImageView) itemView.findViewById(R.id.iv_delete_post);
            fblikegrey = (ImageView) itemView.findViewById(R.id.fblikegrey);
            fbLikered = (ImageView) itemView.findViewById(R.id.fbLikered);
            iv_small_profile_img = (ImageView) itemView.findViewById(R.id.iv_small_profile_img);
            iv_like_person_list = (ImageView) itemView.findViewById(R.id.iv_like_person_list);
            iv_user_post_image = (ImageView) itemView.findViewById(R.id.iv_user_post_image);
            ll_share_post = (LinearLayout) itemView.findViewById(R.id.ll_share_post);
            ll_comment_on_post = (LinearLayout) itemView.findViewById(R.id.ll_comment_on_post);
            lnfblike2 = (LinearLayout) itemView.findViewById(R.id.lnfblike2);
            ll_post_image_or_des = (LinearLayout) itemView.findViewById(R.id.ll_post_image_or_des);
            ll_whats_on_your_mind = (LinearLayout) itemView.findViewById(R.id.ll_whats_on_your_mind);
            ln_desc = (LinearLayout) itemView.findViewById(R.id.ln_desc);
            videoplayer = (VMVideoView) itemView.findViewById(R.id.videoplayer);

            tv_post_date_time = (TextView) itemView.findViewById(R.id.tv_post_date_time);
            tv_post_title = (TextView) itemView.findViewById(R.id.tv_post_title);
            tv_post_des = (TextView) itemView.findViewById(R.id.tv_post_des);
            tv_total_like = (TextView) itemView.findViewById(R.id.tv_total_like);
            tv_total_comment = (TextView) itemView.findViewById(R.id.tv_total_comment);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_share = (TextView) itemView.findViewById(R.id.tv_share);

            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
        }

    }

    private void setVideo(String uri, ViewHolder holder) {
        holder.videoplayer.init(activity);
        holder.videoplayer.setUp(activity, uri, VMVideoView.SCREEN_LAYOUT_NORMAL);
    }

}



/*

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.Bean.BeanFeddPostImage;
import com.ext.adarsh.Bean.ModelClass2;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class AdapterOpenImageFeedPost extends RecyclerView.Adapter<AdapterOpenImageFeedPost.ViewHolder> {

    Activity activity;
    static Activity activity2;
    static List<BeanFeddPostImage> beanFeddPostImage = new ArrayList<>();
    ProgressDialog pd;
    public static List<ModelClass2> item_list2 = new ArrayList<>();
    public static RecyclerView.Adapter recyclerview_adapter2;

    public AdapterOpenImageFeedPost(Activity context, List<BeanFeddPostImage> beanFeddPostImage) {
        this.activity = context;
        this.activity2 = context;
        this.beanFeddPostImage = beanFeddPostImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_feed_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        pd = Utility.getDialog(activity);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        holder.rv_post_heding.setLayoutManager(flowLayoutManager);
        holder.tv_post_people_name.setTypeface(Utility.getTypeFaceTab());
        holder.tv_post_date_time.setTypeface(Utility.getTypeFace());
        holder.tv_post_title.setTypeface(Utility.getTypeFace());
        holder.tv_post_des.setTypeface(Utility.getTypeFace());
        holder.tv_total_like.setTypeface(Utility.getTypeFace());
        holder.tv_total_comment.setTypeface(Utility.getTypeFace());
        holder.tv_like.setTypeface(Utility.getTypeFace());
        holder.tv_comment.setTypeface(Utility.getTypeFace());
        holder.tv_share.setTypeface(Utility.getTypeFace());
        holder.tv_total_comment_text.setTypeface(Utility.getTypeFace());

        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).albumDetailId);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).albumId);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).description);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).filePath);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).fileType);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).likeIds);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).noImageFlag);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).notificationFlag);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).pinFlag);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).postDate);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).postId);
        Log.e("BeanFeddPostImage",""+beanFeddPostImage.get(position).privacyFlag);

    }

    @Override
    public int getItemCount() {
        return beanFeddPostImage.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_post_date_time, tv_post_title, tv_post_people_name;
        TextView tv_post_des, tv_total_like, tv_total_comment, tv_like, tv_comment, tv_share;
        LinearLayout ll_share_post, ll_comment_on_post, lnfblike2, ln_desc;
        LinearLayout ll_post_image_or_des;
        ImageView iv_post_profile_img, iv_delete_post;
        ImageView fblikegrey, fbLikered, iv_like_person_list;
        RecyclerView rv_post_heding;
        TextView tv2, tv3, tv_total_comment_text;
        ImageView iv_1, iv21, iv_22, iv31, iv_32, iv_33, iv_41, iv_42, iv_43, iv44, iv_51, iv_52, iv_53, iv_54, iv_55;
        ImageView iv_61, iv_62, iv_63, iv_64, iv_65;
        LinearLayout ll_image2, ll_image3, ll_image4, ll_image5, ll_image6;
        TextView tv_more_number_of_image;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_more_number_of_image = (TextView) itemView.findViewById(R.id.tv_more_number_of_image);
            iv_1 = (ImageView) itemView.findViewById(R.id.iv_1);
            iv21 = (ImageView) itemView.findViewById(R.id.iv21);
            iv_22 = (ImageView) itemView.findViewById(R.id.iv_22);
            iv31 = (ImageView) itemView.findViewById(R.id.iv31);
            iv_32 = (ImageView) itemView.findViewById(R.id.iv_32);
            iv_33 = (ImageView) itemView.findViewById(R.id.iv_33);
            iv_33 = (ImageView) itemView.findViewById(R.id.iv_33);
            iv_41 = (ImageView) itemView.findViewById(R.id.iv_41);
            iv_41 = (ImageView) itemView.findViewById(R.id.iv_41);
            iv_42 = (ImageView) itemView.findViewById(R.id.iv_42);
            iv_43 = (ImageView) itemView.findViewById(R.id.iv_43);
            iv44 = (ImageView) itemView.findViewById(R.id.iv44);
            iv_51 = (ImageView) itemView.findViewById(R.id.iv_51);
            iv_52 = (ImageView) itemView.findViewById(R.id.iv_52);
            iv_53 = (ImageView) itemView.findViewById(R.id.iv_53);
            iv_54 = (ImageView) itemView.findViewById(R.id.iv_54);
            iv_55 = (ImageView) itemView.findViewById(R.id.iv_55);
            iv_61 = (ImageView) itemView.findViewById(R.id.iv_61);
            iv_62 = (ImageView) itemView.findViewById(R.id.iv_62);
            iv_63 = (ImageView) itemView.findViewById(R.id.iv_63);
            iv_64 = (ImageView) itemView.findViewById(R.id.iv_64);
            iv_65 = (ImageView) itemView.findViewById(R.id.iv_65);

            ll_image2 = (LinearLayout) itemView.findViewById(R.id.ll_image2);
            ll_image3 = (LinearLayout) itemView.findViewById(R.id.ll_image3);
            ll_image4 = (LinearLayout) itemView.findViewById(R.id.ll_image4);
            ll_image5 = (LinearLayout) itemView.findViewById(R.id.ll_image5);
            ll_image6 = (LinearLayout) itemView.findViewById(R.id.ll_image6);

            rv_post_heding = (RecyclerView) itemView.findViewById(R.id.rv_post_heding);
            iv_like_person_list = (ImageView) itemView.findViewById(R.id.iv_like_person_list);
            iv_delete_post = (ImageView) itemView.findViewById(R.id.iv_delete_post);
            fblikegrey = (ImageView) itemView.findViewById(R.id.fblikegrey);
            fbLikered = (ImageView) itemView.findViewById(R.id.fbLikered);
            iv_post_profile_img = (ImageView) itemView.findViewById(R.id.iv_small_profile_img);

            ll_share_post = (LinearLayout) itemView.findViewById(R.id.ll_share_post);
            ll_comment_on_post = (LinearLayout) itemView.findViewById(R.id.ll_comment_on_post);
            lnfblike2 = (LinearLayout) itemView.findViewById(R.id.lnfblike2);
            ll_post_image_or_des = (LinearLayout) itemView.findViewById(R.id.ll_post_image_or_des);
            ln_desc = (LinearLayout) itemView.findViewById(R.id.ln_desc);

            tv_post_people_name = (TextView) itemView.findViewById(R.id.tv_post_people_name);
            tv_post_date_time = (TextView) itemView.findViewById(R.id.tv_post_date_time);
            tv_post_title = (TextView) itemView.findViewById(R.id.tv_post_title);
            tv_post_des = (TextView) itemView.findViewById(R.id.tv_post_des);
            tv_total_like = (TextView) itemView.findViewById(R.id.tv_total_like);
            tv_total_comment = (TextView) itemView.findViewById(R.id.tv_total_comment);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_share = (TextView) itemView.findViewById(R.id.tv_share);

            tv_total_comment_text = (TextView) itemView.findViewById(R.id.tv_total_comment_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
*/

