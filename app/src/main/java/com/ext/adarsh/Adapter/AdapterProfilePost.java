package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 02-11-2017.
 */


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.ext.adarsh.Activity.OpenImageFeedPostActivity;
import com.ext.adarsh.Bean.BeanFeedNews;
import com.ext.adarsh.Bean.ModelProfileTimelinePostWithOther;
import com.ext.adarsh.Bean.ModelTagFriendName;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.VMVideoView;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterProfilePost extends RecyclerView.Adapter<AdapterProfilePost.ViewHolder> {


    Activity activity;
    static Activity activity2;
    static List<BeanFeedNews> bean_feed_news_list = new ArrayList<>();

    static Dialog dd;
    ProgressDialog pd;
    static ProgressDialog pd2;
    String total_like;
    boolean like_status;
    Dialog dialog_open_liked_PersonName;
    List<ModelTagFriendName> modelTagFriendNames = new ArrayList<>();
    List<ModelProfileTimelinePostWithOther> modelProfileTimelinePostWithOthers = new ArrayList<>();

    public AdapterProfilePost(Activity context, List<BeanFeedNews> bean_feed_news_list) {
        this.activity = context;
        this.activity2 = context;
        this.bean_feed_news_list = bean_feed_news_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_profile_post_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BeanFeedNews pos = bean_feed_news_list.get(position);
        pd2 = Utility.getDialog(activity2);
        pd = Utility.getDialog(activity);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        holder.rv_post_heding.setLayoutManager(flowLayoutManager);

        FlowLayoutManager flowLayoutManager2 = new FlowLayoutManager();
        flowLayoutManager2.setAutoMeasureEnabled(true);
        holder.rv_other_post_heding.setLayoutManager(flowLayoutManager2);

        if (pos.peopleId.equalsIgnoreCase(Utility.getPeopleIdPreference())) {
            holder.iv_delete_post.setVisibility(View.VISIBLE);
        } else {
            holder.iv_delete_post.setVisibility(View.GONE);
        }

        if (pos.post_Image_Array.size() == 0) {
            //    holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.GONE);
        } else if (pos.post_Image_Array.size() == 1) {
            //  holder.ll_image1.setVisibility(View.VISIBLE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.GONE);
            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv_1);
        } else if (pos.post_Image_Array.size() == 2) {
            //   holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.VISIBLE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.GONE);
            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv21);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_22);
        } else if (pos.post_Image_Array.size() == 3) {
            //  holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.VISIBLE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.GONE);
            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv31);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_32);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).into(holder.iv_33);
        } else if (pos.post_Image_Array.size() == 4) {
            // holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.VISIBLE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.GONE);
            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv_41);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_42);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).into(holder.iv_43);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).into(holder.iv44);
        } else if (pos.post_Image_Array.size() == 5) {
            //  holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.VISIBLE);
            holder.ll_image6.setVisibility(View.GONE);
            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv_51);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_52);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).into(holder.iv_53);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).into(holder.iv_54);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).into(holder.iv_55);
        } else if (pos.post_Image_Array.size() == 6) {
            //  holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).into(holder.iv_65);
            holder.tv_more_number_of_image.setText("1 More Image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (pos.post_Image_Array.size() == 7) {
            //holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).into(holder.iv_65);
            holder.tv_more_number_of_image.setText("2 More Image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (pos.post_Image_Array.size() == 8) {
            //   holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).into(holder.iv_65);
            holder.tv_more_number_of_image.setText("3 More Image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (pos.post_Image_Array.size() == 9) {
            // holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).into(holder.iv_65);
            holder.tv_more_number_of_image.setText("4 More Image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (pos.post_Image_Array.size() == 10) {
            //  holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).into(holder.iv_65);
            holder.tv_more_number_of_image.setText("5 More Image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (pos.post_Image_Array.size() > 0) {
            //  holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).into(holder.iv_65);
            holder.tv_more_number_of_image.setText("5+ image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        holder.iv21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });
        holder.iv_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });
        holder.iv_32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });
        holder.iv_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_52.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_53.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_54.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_61.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_62.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_63.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        holder.iv_65.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "clicking");
                Intent i = new Intent(activity, OpenImageFeedPostActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });


        holder.iv_delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupmenu_delete(view, pos.postId, position);
            }
        });

        holder.iv_like_person_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLikedPersonName(position);
            }
        });


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
                        total_like = pos.likeCount;
                        Log.e("post_people_id", "" + pos.peopleId);
                        //  CommentDialod(pos.postId, position, pos.albumDetailId, pos.peopleId);
                    }
                });

        holder.lnfblike2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFeedPostLike(pos.postId);
                //   if (like_status) {
                if (holder.fblikegrey.getVisibility() == View.VISIBLE) {
                    holder.fblikegrey.setVisibility(View.GONE);
                    holder.fbLikered.setVisibility(View.VISIBLE);
                    int a = Integer.parseInt(holder.tv_total_like.getText().toString());
                    holder.tv_total_like.setText(String.valueOf(a + 1));
                } else {
                    holder.fblikegrey.setVisibility(View.VISIBLE);
                    holder.fbLikered.setVisibility(View.GONE);
                    int a = Integer.parseInt(holder.tv_total_like.getText().toString());
                    holder.tv_total_like.setText(String.valueOf(a - 1));
                }
                //  }
            }
        });

        holder.ll_post_image_or_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        Glide.with(activity).load(pos.postProfileImage).into(holder.iv_small_profile_img);
        if (pos.likeStatus.equals("D")) {
            holder.fblikegrey.setVisibility(View.VISIBLE);
            holder.fbLikered.setVisibility(View.GONE);

        } else {
            holder.fblikegrey.setVisibility(View.GONE);
            holder.fbLikered.setVisibility(View.VISIBLE);
        }
        holder.tv_post_date_time.setText(pos.postDateTime);

        if (pos.description == null) {
            holder.ln_desc.setVisibility(View.GONE);
        } else {
            holder.tv_post_title.setText(pos.description);
        }
        if (pos.isFile.equalsIgnoreCase("D")) {
            holder.tv_post_des.setText(pos.post);
            //   holder.iv_user_post_image.setVisibility(View.GONE);
            holder.tv_post_des.setVisibility(View.VISIBLE);
        } else {
            if (pos.fileType.equals("I")) {
                // holder.iv_user_post_image.setVisibility(View.VISIBLE);
                holder.videoplayer.setVisibility(View.GONE);
                //    Glide.with(activity).load(pos.post).into(holder.iv_user_post_image);
            } else {
                //  holder.iv_user_post_image.setVisibility(View.GONE);
                holder.videoplayer.setVisibility(View.VISIBLE);
                setVideo(pos.post, holder);
            }
            holder.tv_post_des.setVisibility(View.GONE);
        }
        holder.tv_total_like.setText(pos.likeCount);
        holder.tv_total_comment.setText(pos.totalComment + " Comments");


        if (pos.toPeopleFlag.equalsIgnoreCase("A")) {
            modelProfileTimelinePostWithOthers.clear();
            ModelProfileTimelinePostWithOther model1 = new ModelProfileTimelinePostWithOther();
            model1.setPeopleId(pos.peopleId);
            model1.setPeopleName(pos.fullName);
            model1.setIndex("1");
            modelProfileTimelinePostWithOthers.add(model1);
/*

            ModelProfileTimelinePostWithOther model2 = new ModelProfileTimelinePostWithOther();
            model2.setPeopleId("");
            model2.setPeopleName("");
            model2.setIndex("2");
            modelProfileTimelinePostWithOthers.add(model2);*/

            ModelProfileTimelinePostWithOther model3 = new ModelProfileTimelinePostWithOther();
            model3.setPeopleId("");
            model3.setPeopleName(pos.toFullName);
            model3.setIndex("3");
            modelProfileTimelinePostWithOthers.add(model3);

            if (pos.tagFlag.equalsIgnoreCase("Y")) {
                ModelProfileTimelinePostWithOther temp4 = new ModelProfileTimelinePostWithOther();
                temp4.setPeopleName(" is with ");
                temp4.setPeopleId("");
                temp4.setFlag("N");
                temp4.setIndex("4");
                modelProfileTimelinePostWithOthers.add(temp4);

                ModelProfileTimelinePostWithOther temp5 = new ModelProfileTimelinePostWithOther();
                temp5.setPeopleName(pos.tag_Friend_One_Array.get(0).personsName);
                temp5.setPeopleId(pos.tag_Friend_One_Array.get(0).personsId);
                temp5.setFlag("Y");
                temp5.setIndex("5");
                modelProfileTimelinePostWithOthers.add(temp5);
            } else {

            }
            if (pos.totalPersons.equalsIgnoreCase("2")) {


                ModelProfileTimelinePostWithOther temp6 = new ModelProfileTimelinePostWithOther();
                temp6.setPeopleName(" and ");
                temp6.setPeopleId("");
                temp6.setFlag("N");
                temp6.setIndex("6");
                modelProfileTimelinePostWithOthers.add(temp6);

                ModelProfileTimelinePostWithOther temp7 = new ModelProfileTimelinePostWithOther();
                temp7.setPeopleName(pos.tag_Friend_More_Array.get(0).personsName);
                temp7.setPeopleId(pos.tag_Friend_More_Array.get(0).personsId);
                temp7.setFlag("Y");
                temp7.setIndex("7");
                modelProfileTimelinePostWithOthers.add(temp7);

            } else if (pos.tag_Friend_More_Array.isEmpty()) {
                ModelProfileTimelinePostWithOther temp8 = new ModelProfileTimelinePostWithOther();
                temp8.setPeopleName("");
                temp8.setPeopleId("0");
                temp8.setFlag("Y");
                temp8.setIndex("8");
                modelProfileTimelinePostWithOthers.add(temp8);
            } else {
                ModelProfileTimelinePostWithOther temp9 = new ModelProfileTimelinePostWithOther();
                temp9.setPeopleName(" and ");
                temp9.setPeopleId("");
                temp9.setFlag("N");
                temp9.setIndex("9");
                modelProfileTimelinePostWithOthers.add(temp9);

                ModelProfileTimelinePostWithOther temp10 = new ModelProfileTimelinePostWithOther();
                temp10.setPeopleName(" " + pos.totalPersons + " Others");
                temp10.setPeopleId("0");
                temp10.setFlag("Y");
                temp10.setIndex("10");
                modelProfileTimelinePostWithOthers.add(temp10);
            }

            AdapterOthersTimelinePostHeader adapterPollsOption = new AdapterOthersTimelinePostHeader(activity, modelProfileTimelinePostWithOthers);
            holder.rv_other_post_heding.setAdapter(adapterPollsOption);


        }
        //modelProfileTimelinePostWithOthers


        modelTagFriendNames.clear();
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
        holder.rv_post_heding.setAdapter(adapterPollsOption);

        if (pos.toPeopleFlag.equalsIgnoreCase("A")) {
            holder.rv_other_post_heding.setVisibility(View.VISIBLE);
            holder.rv_post_heding.setVisibility(View.GONE);
        } else {
            holder.rv_other_post_heding.setVisibility(View.GONE);
            holder.rv_post_heding.setVisibility(View.VISIBLE);
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

        AdapterLikePeopleList adapterLikePeopleList = new AdapterLikePeopleList(activity, bean_feed_news_list.get(position).post_Like_People_Array);
        rv_tag_friend_list.setAdapter(adapterLikePeopleList);
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
        RecyclerView rv_other_post_heding;

        public ViewHolder(View itemView) {
            super(itemView);
            rv_other_post_heding = (RecyclerView) itemView.findViewById(R.id.rv_other_post_heding);
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
            //  iv_user_post_image = (ImageView) itemView.findViewById(R.id.iv_user_post_image);
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

