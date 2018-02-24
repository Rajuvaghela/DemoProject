package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bumptech.glide.Glide;
import com.ext.adarsh.Adapter.AdapterPhotoComment;
import com.ext.adarsh.Bean.BeanPhotoComment;
import com.ext.adarsh.Bean.BeanPhotoDetail;
import com.ext.adarsh.Bean.BeanPhotoSubComment;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class LoadFullPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog pd;
    Activity activity;
    Dialog dd;


    @BindView(R.id.iv_full_photo)
    ImageView iv_full_photo;


    @BindView(R.id.fblikegrey)
    ImageView fblikegrey;

    @BindView(R.id.fbLikered)
    ImageView fbLikered;

    @BindView(R.id.likecount)
    TextView likecount;

    @BindView(R.id.tv_like)
    TextView tv_like;

    @BindView(R.id.tv_comment)
    TextView tv_comment;

    @BindView(R.id.tv_share)
    TextView tv_share;

    @BindView(R.id.tv_total_comment)
    TextView tv_total_comment;

    @BindView(R.id.lnfblike1)
    LinearLayout lnfblike1;

    @BindView(R.id.lncomment)
    LinearLayout lncomment;

    @BindView(R.id.share1)
    LinearLayout share1;

    String albumDetailId,postid;
    ArrayList<BeanPhotoDetail> beanPhotoDetails = new ArrayList<>();
    ArrayList<BeanPhotoComment> beanPhotoComments = new ArrayList<>();
    AdapterPhotoComment adapterPhotoComment;
    BeanPhotoDetail photodetail;
    ImageView iv_fb_like, iv_fb_like_red;
    RelativeLayout ll_popup_topic_comment;
    public static EditText et_comment;
    RecyclerView rv_main_comment;
    AdapterPhotoComment coment_adaptert;
    String filepath;
    boolean like_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photodetailactivity);

        activity = this;
        ButterKnife.bind(this);
        pd = Utility.getDialog(activity);
        Bundle bundle = getIntent().getExtras();
        albumDetailId = bundle.getString("AlbumDetailId");
        postid = bundle.getString("postid");

        lncomment.setOnClickListener(this);
        lnfblike1.setOnClickListener(this);
        getPhotoDeatilByPhoto(albumDetailId);

      /*  photodetail = new BeanPhotoDetail();
Log.e("photodetail",photodetail.photoLikeDislikeFlag);
        if(photodetail.photoLikeDislikeFlag.equalsIgnoreCase("D")) {
           fblikegrey.setVisibility(View.VISIBLE);
            fbLikered.setVisibility(View.GONE);
        } else {
           fblikegrey.setVisibility(View.GONE);
            fbLikered.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void getPhotoDeatilByPhoto(final String albumDetailId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Photo_Detail_By_PhotoId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Photo_Detail_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Photo_Detail_Array");
                            if (jsonArray.length() != 0) {
                                beanPhotoDetails.clear();
                                beanPhotoDetails.addAll((Collection<? extends BeanPhotoDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPhotoDetail>>() {
                                }.getType()));
                                Glide.with(activity).load(beanPhotoDetails.get(0).filePath).into(iv_full_photo);
                                tv_total_comment.setText(beanPhotoDetails.get(0).totalComment + " Comment");
                                likecount.setText(beanPhotoDetails.get(0).likeCount);
                                if (beanPhotoDetails.get(0).photoLikeDislikeFlag.equalsIgnoreCase("D")) {
                                    fblikegrey.setVisibility(View.VISIBLE);
                                    fbLikered.setVisibility(View.GONE);
                                } else {
                                    fblikegrey.setVisibility(View.GONE);
                                    fbLikered.setVisibility(View.VISIBLE);
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
                    map.put("PhotoId", albumDetailId);
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
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
        like_status = false;
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
                                       /* if (fblikegrey.getVisibility() == View.VISIBLE) {
                                            fblikegrey.setVisibility(View.GONE);
                                            fbLikered.setVisibility(View.VISIBLE);
                                        } else {
                                            fblikegrey.setVisibility(View.VISIBLE);
                                            fbLikered.setVisibility(View.GONE);
                                        }*/
                                        like_status = true;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lncomment:
                photoCommentByPhotoId();
                break;

            case R.id.lnfblike1:
                PhotoLikeDislike(albumDetailId);
                        // if (like_status) {
                        if (fblikegrey.getVisibility() == View.VISIBLE) {
                            fblikegrey.setVisibility(View.GONE);
                            fbLikered.setVisibility(View.VISIBLE);
                            int a = Integer.parseInt(likecount.getText().toString());
                            likecount.setText(String.valueOf(a + 1));
                        } else {
                           fblikegrey.setVisibility(View.VISIBLE);
                            fbLikered.setVisibility(View.GONE);
                            int a = Integer.parseInt(likecount.getText().toString());
                            likecount.setText(String.valueOf(a - 1));
                        }
                break;
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
        tv_total_like.setText(likecount.getText().toString());

        LinearLayout ll_send_comment = (LinearLayout) dd.findViewById(R.id.ll_send_comment);
        rv_main_comment = (RecyclerView) dd.findViewById(R.id.rv_main_comment);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_main_comment.setLayoutManager(mLayoutManager);
        rv_main_comment.setItemAnimator(new DefaultItemAnimator());

        if (fblikegrey.getVisibility() == View.VISIBLE) {
            iv_fb_like.setVisibility(View.VISIBLE);
            iv_fb_like_red.setVisibility(View.GONE);
        } else {
            iv_fb_like.setVisibility(View.GONE);
            iv_fb_like_red.setVisibility(View.VISIBLE);
        }

        ll_popup_topic_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhotoLikeDislike(albumDetailId);

                if (iv_fb_like.getVisibility() == View.VISIBLE) {
                    iv_fb_like.setVisibility(View.GONE);
                    iv_fb_like_red.setVisibility(View.VISIBLE);

                    fblikegrey.setVisibility(View.GONE);
                    fbLikered.setVisibility(View.VISIBLE);
                    int a = Integer.parseInt(tv_total_like.getText().toString());
                    tv_total_like.setText(String.valueOf(a + 1));
                   // tv_total_like.setText(String.valueOf(a + 1));

                } else {
                    iv_fb_like.setVisibility(View.VISIBLE);
                    iv_fb_like_red.setVisibility(View.GONE);

                    fblikegrey.setVisibility(View.VISIBLE);
                    fbLikered.setVisibility(View.GONE);
                    int a = Integer.parseInt(tv_total_like.getText().toString());
                    tv_total_like.setText(String.valueOf(a - 1));
                   // tv_total_like.setText(String.valueOf(a - 1));

                }
            }
        });

        coment_adaptert = new AdapterPhotoComment(beanPhotoComments, activity, "N",albumDetailId);
        rv_main_comment.setAdapter(coment_adaptert);

        ll_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoComment(albumDetailId, postId, peopleId, et_comment.getText().toString());
            }
        });
        dd.show();
    }

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
                                    /*    if (dialog_sub_comment_close) {
                                            // dialog_sub_comment.dismiss();
                                            dialog_sub_comment_close = false;
                                        }
                                        if (dialog_main_comment_close) {
                                            //  dd.dismiss();
                                            et_comment.setText("");
                                            dialog_main_comment_close = false;
                                        }
                                        int a  = Integer.parseInt(tv_total_comment.getText().toString());
                                        tv_total_comment.setText(String.valueOf(a+1));*/

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

                                    JSONArray jsonArray = array.optJSONObject(i).getJSONArray("Photo_Comment_Replay_Array");

                                    ArrayList<BeanPhotoSubComment> beanPhotoSubComment = new ArrayList<>();
                                   // beanPhotoSubComment = null;

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


                    Log.e("AlbumDetailId",""+albumDetailId);
                    Log.e("PostId", ""+postId);
                    Log.e("PostPeopleId", ""+peopleId);
                    Log.e("Comment", ""+comment);
                    Log.e("LoginId", ""+Utility.getPeopleIdPreference());

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

    private void photoCommentByPhotoId() {
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
                                CommentDialod(beanPhotoDetails.get(0).albumDetailId, beanPhotoDetails.get(0).postId, beanPhotoDetails.get(0).peopleId);
                                pd.dismiss();
                            } else {
                                CommentDialod(beanPhotoDetails.get(0).albumDetailId, beanPhotoDetails.get(0).postId, beanPhotoDetails.get(0).peopleId);
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
                    map.put("Hashkey",Utility.getHashKeyPreference());
                    map.put("PostId", postid);

                    Log.e("PhotoId", ""+albumDetailId);
                    Log.e("LoginId", ""+ Utility.getPeopleIdPreference());
                    Log.e("Hashkey", ""+Utility.getHashKeyPreference());
                    Log.e("PostId",  ""+postid);

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
}
