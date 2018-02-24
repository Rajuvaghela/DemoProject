package com.ext.adarsh.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.ext.adarsh.Bean.BeanBranchList;
import com.ext.adarsh.Bean.BeanColleaguesList;
import com.ext.adarsh.Bean.BeanFeedNews;
import com.ext.adarsh.Bean.BeanFeedNewsSubComment;
import com.ext.adarsh.Bean.BeanFeedNewsSubSubComment;
import com.ext.adarsh.Bean.ModelClass2;
import com.ext.adarsh.Bean.ModelTagFriendName;
import com.ext.adarsh.Fragment.activty_feed;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.RecyclerItemClickListener;
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

import cn.refactor.lib.colordialog.PromptDialog;
import fm.jiecao.jcvideoplayer_lib.VMVideoView;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterFeedNews10Image extends RecyclerView.Adapter<AdapterFeedNews10Image.ViewHolder> {

    Activity activity;
    static Activity activity2;
    static List<BeanFeedNews> bean_feed_news_list = new ArrayList<>();
    Dialog dialog_share_with_perrticular_office, dialog_share_with_Colleagues, dialog_open_liked_PersonName;
    static Dialog dd, open_tag_dialog;
    Dialog open_10_image_dialog;
    ProgressDialog pd;
    static ProgressDialog pd2;
    RelativeLayout ll_popup_topic_comment;
    public static EditText et_comment;
    RecyclerView rv_main_comment;
    public static AdapterActivityNewsFeedComent coment_adaptert;
    static boolean dialog_sub_comment_close, dialog_main_comment_close;
    String total_like;
    boolean like_status;
    ImageView iv_fb_like, iv_fb_like_red;
    List<ModelTagFriendName> modelTagFriendNames = new ArrayList<>();
    static RecyclerView rv_select_visible_branch;

    public static List<ModelClass2> item_list2 = new ArrayList<>();
    static TextView tv_select_visible_branch;
    static RecyclerView recyclerview3;
    static RecyclerView recyclerview4;
    static RecyclerView.LayoutManager recylerViewLayoutManager2;
    static Adapter1BranchSharePost adapter1branch;
    public static List<BeanBranchList> branch_List = new ArrayList<>();
    public List<BeanColleaguesList> beanColleaguesLists = new ArrayList<>();
    public static RecyclerView.Adapter recyclerview_adapter2;
    Spinner spiner_colleagues;
    String peopleId = "", people_name = "";

    public AdapterFeedNews10Image(Activity context, List<BeanFeedNews> bean_feed_news_list) {
        this.activity = context;
        this.activity2 = context;
        this.bean_feed_news_list = bean_feed_news_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_feed_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BeanFeedNews pos = bean_feed_news_list.get(position);
        pd2 = Utility.getDialog(activity2);
        pd = Utility.getDialog(activity);
        modelTagFriendNames.clear();

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        holder.rv_post_heding.setLayoutManager(flowLayoutManager);
        //  iv_1, iv21, iv_22, iv31, iv_32, iv_33, iv_41, iv_42, iv_43, iv44, iv_51, iv_52, iv_53, iv_54, iv_55;

        if (pos.post_Image_Array.size() == 0) {

            // holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.GONE);
        } else if (pos.post_Image_Array.size() == 1) {
            //    holder.ll_image1.setVisibility(View.VISIBLE);
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
            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv21);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_22);
        } else if (pos.post_Image_Array.size() == 3) {
            //   holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.VISIBLE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.GONE);
            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv31);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_32);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).centerCrop().into(holder.iv_33);
        } else if (pos.post_Image_Array.size() == 4) {
            //  holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.VISIBLE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.GONE);
            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv_41);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_42);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).centerCrop().into(holder.iv_43);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).centerCrop().into(holder.iv44);
        } else if (pos.post_Image_Array.size() == 5) {
            //  holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.VISIBLE);
            holder.ll_image6.setVisibility(View.GONE);
            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv_51);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_52);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).centerCrop().into(holder.iv_53);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).centerCrop().into(holder.iv_54);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).centerCrop().into(holder.iv_55);
        } else if (pos.post_Image_Array.size() == 6) {
            //  holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).centerCrop().into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).centerCrop().into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).centerCrop().into(holder.iv_65);
            holder.tv_more_number_of_image.setText("1 More Image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (pos.post_Image_Array.size() == 7) {
            //   holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).centerCrop().into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).centerCrop().into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).centerCrop().into(holder.iv_65);
            holder.tv_more_number_of_image.setText("2 More Image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (pos.post_Image_Array.size() == 8) {
            //holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).centerCrop().into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).centerCrop().into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).centerCrop().into(holder.iv_65);
            holder.tv_more_number_of_image.setText("3 More Image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (pos.post_Image_Array.size() == 9) {
            //  holder.ll_image1.setVisibility(View.GONE);
            holder.ll_image2.setVisibility(View.GONE);
            holder.ll_image3.setVisibility(View.GONE);
            holder.ll_image4.setVisibility(View.GONE);
            holder.ll_image5.setVisibility(View.GONE);
            holder.ll_image6.setVisibility(View.VISIBLE);

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).centerCrop().into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).centerCrop().into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).centerCrop().into(holder.iv_65);
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

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).centerCrop().into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).centerCrop().into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).centerCrop().into(holder.iv_65);
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

            Glide.with(activity).load(pos.post_Image_Array.get(0).filePath).centerCrop().into(holder.iv_61);
            Glide.with(activity).load(pos.post_Image_Array.get(1).filePath).centerCrop().into(holder.iv_62);
            Glide.with(activity).load(pos.post_Image_Array.get(2).filePath).centerCrop().into(holder.iv_63);
            Glide.with(activity).load(pos.post_Image_Array.get(3).filePath).centerCrop().into(holder.iv_64);
            Glide.with(activity).load(pos.post_Image_Array.get(4).filePath).centerCrop().into(holder.iv_65);
            holder.tv_more_number_of_image.setText("5+ image");
            holder.tv_more_number_of_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if (pos.peopleId.equalsIgnoreCase(Utility.getPeopleIdPreference())) {
            holder.iv_delete_post.setVisibility(View.VISIBLE);
        } else {
            holder.iv_delete_post.setVisibility(View.GONE);
        }
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


        holder.ll_share_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showpopupmenu(view, pos.sharePostId, pos.postPeopleId);
            }
        });

        holder.ll_comment_on_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total_like = pos.likeCount;
                Log.e("post_people_id", "" + pos.peopleId);
                CommentDialod(pos.postId, position, pos.albumDetailId, pos.peopleId, holder, pos.postProfileImage);
            }
        });

        holder.lnfblike2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFeedPostLike(pos.postId);
                // if (like_status) {
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
            }
        });

        holder.ll_post_image_or_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        Glide.with(activity).load(pos.profileImage).into(holder.iv_post_profile_img);

        if (pos.likeStatus.equals("D")) {
            holder.fblikegrey.setVisibility(View.VISIBLE);
            holder.fbLikered.setVisibility(View.GONE);
        } else {
            holder.fblikegrey.setVisibility(View.GONE);
            holder.fbLikered.setVisibility(View.VISIBLE);
        }

        if (pos.tagFlag.equalsIgnoreCase("Y")) {
            holder.rv_post_heding.setVisibility(View.VISIBLE);
            holder.tv_post_people_name.setVisibility(View.GONE);
        } else {
            holder.tv_post_people_name.setVisibility(View.VISIBLE);
            holder.rv_post_heding.setVisibility(View.GONE);
            holder.tv_post_people_name.setText(pos.fullName);
        }


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
        holder.tv_post_date_time.setText(pos.postDateTime);

        if (pos.description.equalsIgnoreCase("")) {
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
                //  holder.iv_user_post_image.setVisibility(View.VISIBLE);
                holder.videoplayer.setVisibility(View.GONE);
                //   Glide.with(activity).load(pos.post).into(holder.iv_user_post_image);
            } else {
                //      holder.iv_user_post_image.setVisibility(View.GONE);
                holder.videoplayer.setVisibility(View.VISIBLE);
                setVideo(pos.post, holder);
            }
            holder.tv_post_des.setVisibility(View.GONE);
        }
        holder.tv_total_like.setText(pos.likeCount);
        holder.tv_total_comment.setText(pos.totalComment);
    }

    private void Open10Image() {
        open_10_image_dialog = new Dialog(activity2);
        open_10_image_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_10_image_dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_10_image_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_10_image_dialog.setContentView(R.layout.list_of_like_friend_friends);

        Window window = open_10_image_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnmainback = (LinearLayout) open_10_image_dialog.findViewById(R.id.lnmainback);
        RecyclerView rv_10image_list = (RecyclerView) open_10_image_dialog.findViewById(R.id.rv_10image_list);
        rv_10image_list.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true);
        rv_10image_list.setLayoutManager(lnmanager2);
        rv_10image_list.setItemAnimator(new DefaultItemAnimator());

/*        AdapterLikePeopleList adapterLikePeopleList = new AdapterLikePeopleList(activity, bean_feed_news_list.get(position).post_Like_People_Array);
        rv_10image_list.setAdapter(adapterLikePeopleList);*/
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_10_image_dialog.dismiss();
            }
        });

        open_10_image_dialog.show();
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

    //showpopupmenu_delete
    public void showpopupmenu_delete(View view, final String postId, final int position) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.delete_post_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete_post:
                        deleteDialoge(postId, position);
                        break;
                }
                return true;
            }
        });
        popup.show(); //showing popup menu
    }

    private void deleteDialoge(final String postId, final int position) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Exit")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        DeletePost(postId, position);
                        //  DeleteFile(fileid);

                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void showpopupmenu(View view, final String sharePostId, final String postPeopleId) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.share_post_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_with_my_office:
                        SharePostWithMyOffice(sharePostId, postPeopleId);
                        break;
                    case R.id.share_with_par_office:
                        getBranchData(sharePostId, postPeopleId);
                        // SharePostWithPartiOffice(sharePostId, postPeopleId);
                        break;
                    case R.id.share_with_clg_timeline:
                        getColleaguesData(sharePostId, postPeopleId);
                        // SharePostWithColleagueTimeLine(sharePostId, postPeopleId);
                        break;
                    case R.id.share_public:
                        SharePostPublic(sharePostId, postPeopleId);
                        break;
                }
                return true;
            }
        });
        popup.show(); //showing popup menu
    }

    void getColleaguesData(final String sharePostId, final String postPeopleId) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_People_Colleagues_List, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("branch_list_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Post_People_Colleagues_List_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Activity_Feed_Post_People_Colleagues_List_Array");
                            if (jsonArray.length() != 0) {
                                beanColleaguesLists.add(new BeanColleaguesList("0", "Select Colleagues"));
                                if (jsonArray.length() != 0) {
                                    beanColleaguesLists.addAll((Collection<? extends BeanColleaguesList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanColleaguesList>>() {
                                    }.getType()));
                                    pd2.dismiss();
                                    DialogColleesTimeLine(sharePostId, postPeopleId);
                                } else {
                                    pd2.dismiss();
                                }
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
                    Log.e("branch_list_error", error.toString());
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd2.dismiss();

        }
    }

    private void DialogColleesTimeLine(final String sharePostId, final String postPeopleId) {
        dialog_share_with_Colleagues = new Dialog(activity2);
        dialog_share_with_Colleagues.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_share_with_Colleagues.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog_share_with_Colleagues.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_share_with_Colleagues.setContentView(R.layout.popup_share_with_colleagues);

        Window window = dialog_share_with_Colleagues.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView tv_post = (TextView) dialog_share_with_Colleagues.findViewById(R.id.tv_post);
        TextView tv_cancel = (TextView) dialog_share_with_Colleagues.findViewById(R.id.tv_cancel);
        TextView tv_reg_heading = (TextView) dialog_share_with_Colleagues.findViewById(R.id.tv_reg_heading);
        final EditText et_say_something = (EditText) dialog_share_with_Colleagues.findViewById(R.id.et_say_something);
        TextView tv_say_something = (TextView) dialog_share_with_Colleagues.findViewById(R.id.tv_say_something);

        spiner_colleagues = (Spinner) dialog_share_with_Colleagues.findViewById(R.id.spiner_colleagues);
        AdapterColleguesList adapterColleguesList = new AdapterColleguesList(activity, beanColleaguesLists);
        spiner_colleagues.setAdapter(adapterColleguesList);

        spiner_colleagues.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                peopleId = beanColleaguesLists.get(i).peopleId;
                people_name = beanColleaguesLists.get(i).fullName;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tv_post.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        tv_reg_heading.setTypeface(Utility.getTypeFaceTab());
        et_say_something.setTypeface(Utility.getTypeFace());
        tv_say_something.setTypeface(Utility.getTypeFace());


        LinearLayout lnmainback = (LinearLayout) dialog_share_with_Colleagues.findViewById(R.id.lnmainback);
        LinearLayout ll_cancel = (LinearLayout) dialog_share_with_Colleagues.findViewById(R.id.ll_cancel);
        LinearLayout ll_share_with_Colleagues = (LinearLayout) dialog_share_with_Colleagues.findViewById(R.id.ll_share_with_Colleagues);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_share_with_Colleagues.dismiss();
            }
        });
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_share_with_Colleagues.dismiss();
            }
        });

        ll_share_with_Colleagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareWithColleagues(sharePostId, postPeopleId, et_say_something.getText().toString(), peopleId, people_name);
                //    SharePostWithPartiOffice(sharePostId, postPeopleId, et_say_something.getText().toString(), bid);
            }
        });
        dialog_share_with_Colleagues.show();
    }

    private void ShareWithColleagues(final String sharePostId, final String postPeopleId, final String des,
                                     final String people_id, final String people_name) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_Share_on_Colleagues_Timeline, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Post_Share_on_Colleagues_Timeline")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Post_Share_on_Colleagues_Timeline");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        dialog_share_with_Colleagues.dismiss();
                                        pd.dismiss();
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
                    map.put("SharePostId", sharePostId);
                    map.put("PostPeopleId", postPeopleId);
                    map.put("ShareDescription", des);
                    map.put("PersonIdSList", people_id);
                    map.put("PersonNamesList", people_name);
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

    void getBranchData(final String sharePostId, final String postPeopleId) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Branch_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("branch_list_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Branch_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Branch_Array");
                            if (jsonArray.length() != 0) {
                                branch_List.clear();
                                item_list2.clear();
                                branch_List.addAll((Collection<? extends BeanBranchList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanBranchList>>() {
                                }.getType()));
                                OpenDialogSharePostWithPartiOffice(sharePostId, postPeopleId);
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
                    Log.e("branch_list_error", error.toString());
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
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    Log.e("hash_key", "" + Utility.getHashKeyPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);

        } else {
            pd2.dismiss();

        }
    }

    private void OpenDialogSharePostWithPartiOffice(final String sharePostId, final String postPeopleId) {
        dialog_share_with_perrticular_office = new Dialog(activity2);
        dialog_share_with_perrticular_office.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_share_with_perrticular_office.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog_share_with_perrticular_office.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_share_with_perrticular_office.setContentView(R.layout.popup_share_with_perticular_office);

        Window window = dialog_share_with_perrticular_office.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView tv_post = (TextView) dialog_share_with_perrticular_office.findViewById(R.id.tv_post);
        TextView tv_cancel = (TextView) dialog_share_with_perrticular_office.findViewById(R.id.tv_cancel);
        TextView tv_reg_heading = (TextView) dialog_share_with_perrticular_office.findViewById(R.id.tv_reg_heading);
        final EditText et_say_something = (EditText) dialog_share_with_perrticular_office.findViewById(R.id.et_say_something);
        TextView tv_say_something = (TextView) dialog_share_with_perrticular_office.findViewById(R.id.tv_say_something);
        tv_select_visible_branch = (TextView) dialog_share_with_perrticular_office.findViewById(R.id.tv_select_visible_branch);
        TextView tv_select_visible_branch2 = (TextView) dialog_share_with_perrticular_office.findViewById(R.id.tv_select_visible_branch2);

        tv_post.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        tv_reg_heading.setTypeface(Utility.getTypeFaceTab());
        et_say_something.setTypeface(Utility.getTypeFace());
        tv_say_something.setTypeface(Utility.getTypeFace());
        tv_select_visible_branch.setTypeface(Utility.getTypeFace());
        tv_select_visible_branch2.setTypeface(Utility.getTypeFace());

        rv_select_visible_branch = (RecyclerView) dialog_share_with_perrticular_office.findViewById(R.id.rv_select_visible_branch);
        FlowLayoutManager flowLayoutManager2 = new FlowLayoutManager();
        flowLayoutManager2.setAutoMeasureEnabled(true);
        rv_select_visible_branch.setLayoutManager(flowLayoutManager2);

        LinearLayout ll_share_with_particular_office = (LinearLayout) dialog_share_with_perrticular_office.findViewById(R.id.ll_share_with_particular_office);
        LinearLayout ll_select_branch = (LinearLayout) dialog_share_with_perrticular_office.findViewById(R.id.ll_select_branch);
        LinearLayout lnmainback = (LinearLayout) dialog_share_with_perrticular_office.findViewById(R.id.lnmainback);
        LinearLayout ll_cancel = (LinearLayout) dialog_share_with_perrticular_office.findViewById(R.id.ll_cancel);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_share_with_perrticular_office.dismiss();
            }
        });
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_share_with_perrticular_office.dismiss();
            }
        });
        ll_select_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagPopupBranch();
                // dialog_share_with_perrticular_office.dismiss();
            }
        });
        ll_share_with_particular_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bid = "";
                String branchID = "";
                for (int i = 0; i < item_list2.size(); i++) {
                    branchID += item_list2.get(i).getId() + ",";
                }
                if (branchID.length() > 0) {
                    bid = branchID.substring(0, branchID.length() - 1);
                }
                SharePostWithPartiOffice(sharePostId, postPeopleId, et_say_something.getText().toString(), bid);
            }
        });

        dialog_share_with_perrticular_office.show();
    }

    public static void openTagPopupBranch() {
        open_tag_dialog = new Dialog(activity2);
        open_tag_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_tag_dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_tag_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_tag_dialog.setContentView(R.layout.tag_popup_item_layout);

        Window window = open_tag_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        EditText et_search = (EditText) open_tag_dialog.findViewById(R.id.et_search);
        TextView iv_done = (TextView) open_tag_dialog.findViewById(R.id.iv_done);

        TextView header = (TextView) open_tag_dialog.findViewById(R.id.header);
        header.setText("Select Branch");
        header.setTypeface(Utility.getTypeFaceTab());

        LinearLayout lnmainback = (LinearLayout) open_tag_dialog.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_tag_dialog.dismiss();
            }
        });

        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onClick: branchhh ", "branchhh ++++" + String.valueOf(item_list2.size()));
                if (item_list2.size() > 0) {
                    tv_select_visible_branch.setVisibility(View.GONE);
                    rv_select_visible_branch.setVisibility(View.VISIBLE);
                } else {
                    tv_select_visible_branch.setVisibility(View.VISIBLE);
                    rv_select_visible_branch.setVisibility(View.GONE);
                }
                callOnBackPress2();
                open_tag_dialog.dismiss();
            }
        });

        recyclerview3 = (RecyclerView) open_tag_dialog.findViewById(R.id.recyclerview1);
        recyclerview4 = (RecyclerView) open_tag_dialog.findViewById(R.id.recyclerview2);

        recylerViewLayoutManager2 = new LinearLayoutManager(activity2);
        recyclerview3.setLayoutManager(recylerViewLayoutManager2);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerview4.setLayoutManager(flowLayoutManager);

        adapter1branch = new Adapter1BranchSharePost(activity2, branch_List);
        recyclerview3.setAdapter(adapter1branch);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter1branch.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        open_tag_dialog.show();
    }

    public static void callOnBackPress2() {
        recyclerview_adapter2 = new Adapter2BranchSharePost(activity2, item_list2);
        rv_onchangelistner2();

    }

    public static void rv_onchangelistner2() {

        rv_select_visible_branch.setAdapter(recyclerview_adapter2);
        rv_select_visible_branch.addOnItemTouchListener(
                new RecyclerItemClickListener(activity2, rv_select_visible_branch, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        openTagPopupBranch();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        recyclerview_adapter2.notifyDataSetChanged();
        recyclerview4.setAdapter(recyclerview_adapter2);
        recyclerview_adapter2.notifyDataSetChanged();

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
                                        activty_feed.adapter_feed_news.notifyDataSetChanged();
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

    private void SharePostWithPartiOffice(final String sharePostId, final String postPeopleId,
                                          final String et_say_something, final String branch_id) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_Share_with_Particular_Office, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Post_Share_with_Particular_Office")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Post_Share_with_Particular_Office");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        dialog_share_with_perrticular_office.dismiss();
                                        pd.dismiss();
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
                    map.put("SharePostId", sharePostId);
                    map.put("PostPeopleId", postPeopleId);
                    map.put("ShareDescription", et_say_something);
                    map.put("BranchIdSList", branch_id);
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

    private void SharePostPublic(final String sharePostId, final String postPeopleId) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_Share_Now_Public, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Post_Share_Now_Public")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Post_Share_Now_Public");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
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
                    map.put("SharePostId", sharePostId);
                    map.put("PostPeopleId", postPeopleId);
                    map.put("ShareDescription", "");
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

    private void SharePostWithMyOffice(final String sharePostId, final String postPeopleId) {
        if (checkConnectivity()) {
            pd.show();
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_Share_with_my_Office, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Post_Share_with_my_Office")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Post_Share_with_my_Office");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
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
                    map.put("SharePostId", sharePostId);
                    map.put("PostPeopleId", postPeopleId);
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

    @SuppressLint("WrongViewCast")
    private void CommentDialod(final String post_id, final int positon, final String albumDetailId, final String postPeopleId,
                               final ViewHolder holder, String postProfileImage) {
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
        tv_total_like.setText(holder.tv_total_like.getText().toString());


        LinearLayout ll_send_comment = (LinearLayout) dd.findViewById(R.id.ll_send_comment);
        rv_main_comment = (RecyclerView) dd.findViewById(R.id.rv_main_comment);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_main_comment.setLayoutManager(mLayoutManager);
        rv_main_comment.setItemAnimator(new DefaultItemAnimator());

        if (holder.fblikegrey.getVisibility() == View.VISIBLE) {
            iv_fb_like.setVisibility(View.VISIBLE);
            iv_fb_like_red.setVisibility(View.GONE);
        } else {
            iv_fb_like.setVisibility(View.GONE);
            iv_fb_like_red.setVisibility(View.VISIBLE);
        }

        ll_popup_topic_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFeedPostLike(post_id);
                //   if (like_status) {
                if (iv_fb_like.getVisibility() == View.VISIBLE) {
                    iv_fb_like.setVisibility(View.GONE);
                    iv_fb_like_red.setVisibility(View.VISIBLE);

                    holder.fblikegrey.setVisibility(View.GONE);
                    holder.fbLikered.setVisibility(View.VISIBLE);
                    int a = Integer.parseInt(holder.tv_total_like.getText().toString());
                    holder.tv_total_like.setText(String.valueOf(a + 1));
                    tv_total_like.setText(String.valueOf(a + 1));

                } else {
                    iv_fb_like.setVisibility(View.VISIBLE);
                    iv_fb_like_red.setVisibility(View.GONE);

                    holder.fblikegrey.setVisibility(View.VISIBLE);
                    holder.fbLikered.setVisibility(View.GONE);
                    int a = Integer.parseInt(holder.tv_total_like.getText().toString());
                    holder.tv_total_like.setText(String.valueOf(a - 1));
                    tv_total_like.setText(String.valueOf(a - 1));

                }
                //    }
            }
        });


        coment_adaptert = new AdapterActivityNewsFeedComent(bean_feed_news_list.get(positon).sub_Comment_Array, activity, "N");
        rv_main_comment.setAdapter(coment_adaptert);

        ll_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_main_comment_close = true;
                ActivityFeedNewsComment1(et_comment.getText().toString(), post_id, albumDetailId, postPeopleId, positon, holder);
            }
        });

        dd.show();
    }

    static void ActivityFeedNewsComment1(final String comment, final String post_id, final String albumDetailId, final String postPeopleId, final int positon, final ViewHolder holder) {
        pd2.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Activity_Feed_Post_Comment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("coment1_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Activity_Feed_Post_Comment")) {
                            JSONArray array = object.getJSONArray("Activity_Feed_Post_Comment");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd2.dismiss();
                                        if (dialog_sub_comment_close) {
                                            // dialog_sub_comment.dismiss();
                                            dialog_sub_comment_close = false;
                                        }
                                        if (dialog_main_comment_close) {
                                            //  dd.dismiss();
                                            et_comment.setText("");
                                            dialog_main_comment_close = false;

                                        }
                                        int a = Integer.parseInt(holder.tv_total_comment.getText().toString());
                                        holder.tv_total_comment.setText(String.valueOf(a + 1));
                                        activty_feed.postBackgroundRefresh postBackgroundRefresh = new activty_feed.postBackgroundRefresh();
                                        postBackgroundRefresh.execute();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();

                                    } else {
                                        pd2.dismiss();
                                        Toast.makeText(activity2, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                pd2.dismiss();
                            } else {
                                pd2.dismiss();
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

                                    ArrayList<BeanFeedNewsSubSubComment> sub_Comment_Array = new ArrayList<>();
                                    bean_feed_news_list.get(positon).sub_Comment_Array.add(new BeanFeedNewsSubComment(FullName, ProfileImage, PostProfileImage, PostCommentId, AlbumId, AlbumDetailId, PostId, PostPeopleId, CommentPeopleId, ParentId, Comment, LikesUserId, LikeCount, LikeStatus, sub_Comment_Array));
                                    coment_adaptert.notifyDataSetChanged();

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
                    Log.e("coment1_erro", error.toString());
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
                    map.put("AlbumDetailId", albumDetailId);
                    map.put("PostId", post_id);
                    map.put("PostPeopleId", postPeopleId);
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("Comment", comment);
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
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                       /* activty_feed.postBackgroundRefresh postBackgroundRefresh = new activty_feed.postBackgroundRefresh();
                                        postBackgroundRefresh.execute();*/
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

        TextView tv_post_date_time, tv_post_title, tv_post_people_name;
        TextView tv_post_des, tv_total_like, tv_total_comment, tv_like, tv_comment, tv_share;
        LinearLayout ll_share_post, ll_comment_on_post, lnfblike2, ln_desc;
        LinearLayout ll_post_image_or_des;
        ImageView iv_post_profile_img, iv_delete_post;//iv_user_post_image
        ImageView fblikegrey, fbLikered, iv_like_person_list;
        VMVideoView videoplayer;
        RecyclerView rv_post_heding;
        TextView tv2, tv3, tv_total_comment_text;
        ImageView iv_1, iv21, iv_22, iv31, iv_32, iv_33, iv_41, iv_42, iv_43, iv44, iv_51, iv_52, iv_53, iv_54, iv_55;
        ImageView iv_61, iv_62, iv_63, iv_64, iv_65;
        LinearLayout /*ll_image1,*/ ll_image2, ll_image3, ll_image4, ll_image5, ll_image6;
        TextView tv_more_number_of_image;
        // ImageView iv_user_post_image;

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
            //ll_image1 = (LinearLayout) itemView.findViewById(R.id.ll_image1);
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

            //iv_user_post_image = (ImageView) itemView.findViewById(R.id.iv_user_post_image);
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
            videoplayer = (VMVideoView) itemView.findViewById(R.id.videoplayer);

            tv_total_comment_text = (TextView) itemView.findViewById(R.id.tv_total_comment_text);
        }
    }

    private void setVideo(String uri, ViewHolder holder) {
        holder.videoplayer.init(activity);
        holder.videoplayer.setUp(activity, uri, VMVideoView.SCREEN_LAYOUT_NORMAL);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

