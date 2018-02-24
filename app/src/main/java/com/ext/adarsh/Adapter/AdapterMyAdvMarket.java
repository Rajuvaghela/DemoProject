package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.ext.adarsh.Activity.MarketCreateMyAddsActivity;
import com.ext.adarsh.Activity.ProfileActivity;
import com.ext.adarsh.Bean.BeanMarket;
import com.ext.adarsh.Bean.BeanMarketCategory;
import com.ext.adarsh.Fragment.MarketBuyingFragment;
import com.ext.adarsh.Fragment.MarketMyAddFragment;
import com.ext.adarsh.Fragment.MarketSellingFragment;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class AdapterMyAdvMarket extends RecyclerView.Adapter<AdapterMyAdvMarket.MyViewHolder> implements Filterable {

    private ArrayList<BeanMarket> beanMarkets;
    ArrayList<String> images;
    Dialog marketDetail;
    Activity activity;
    int pos = 1;
    private List<BeanMarket> array;
    BeanMarket movie;
    ProgressDialog pd;
    String title, offer_price, offer_description;
    AdapterMarket adapter;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.market_rs)
        TextView market_rs;

        @BindView(R.id.market_desc)
        TextView market_desc;

        @BindView(R.id.market_photo)
        ImageView market_photo;

        @BindView(R.id.main_relative)
        RelativeLayout main_relative;

        @BindView(R.id.edit_market)
        LinearLayout edit_market;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public AdapterMyAdvMarket(Activity activity, ArrayList<BeanMarket> beanMarkets) {
        this.beanMarkets = beanMarkets;
        this.array = beanMarkets;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myadv_market_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        movie = beanMarkets.get(position);


        if (Utility.getMarketUpdate().equalsIgnoreCase("Y")) {
            holder.edit_market.setVisibility(View.VISIBLE);
        } else {
            holder.edit_market.setVisibility(View.GONE);
        }

        holder.market_rs.setTypeface(Utility.getTypeFace());
        holder.market_desc.setTypeface(Utility.getTypeFace());

        title = beanMarkets.get(position).advertisementTitle;
        offer_price = beanMarkets.get(position).offerPrice;
        offer_description = beanMarkets.get(position).description;

        Glide.with(activity).load(movie.imagePath1).into(holder.market_photo);
        holder.market_rs.setText(movie.offerPrice);
        holder.market_desc.setText(movie.advertisementTitle);
        holder.main_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketDetail(position);
            }
        });
        holder.edit_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopupmenu(v, position);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String s = String.valueOf(charSequence);

                if (s != null && s.length() > 0) {
                    ArrayList<BeanMarket> temp = new ArrayList<>();
                    for (BeanMarket category : array) {
                        if (category.advertisementTitle.toLowerCase().contains(s.toLowerCase())) {
                            temp.add(category);
                        }
                    }
                    results.values = temp;
                    results.count = temp.size();
                } else {
                    results.values = array;
                    results.count = array.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                beanMarkets = (ArrayList<BeanMarket>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return beanMarkets.size();
    }

    private void marketDetail(final int position) {
        marketDetail = new Dialog(activity);
        marketDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        marketDetail.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        marketDetail.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        marketDetail.setContentView(R.layout.market_product_detail);

        Window window = marketDetail.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView txt_fname = (TextView) marketDetail.findViewById(R.id.txt_fname);
        TextView txt_fullname = (TextView) marketDetail.findViewById(R.id.txt_fullname);
        TextView txt_title = (TextView) marketDetail.findViewById(R.id.txt_title);
        TextView category_name = (TextView) marketDetail.findViewById(R.id.category_name);
        TextView txt_desc = (TextView) marketDetail.findViewById(R.id.txt_desc);
        TextView txt_rs = (TextView) marketDetail.findViewById(R.id.txt_rs);
        final ImageView market_image = (ImageView) marketDetail.findViewById(R.id.market_image);
        ImageView iv_next = (ImageView) marketDetail.findViewById(R.id.iv_next);
        ImageView iv_pre = (ImageView) marketDetail.findViewById(R.id.iv_pre);

        images = new ArrayList<>();
        images.add(beanMarkets.get(position).imagePath1);
        images.add(beanMarkets.get(position).imagePath2);
        images.add(beanMarkets.get(position).imagePath3);
        images.add(beanMarkets.get(position).imagePath4);
        images.add(beanMarkets.get(position).imagePath5);

        for(String x : images)
        {
            Log.e("x",x);
        }

        ViewPager viewPager = (ViewPager) marketDetail.findViewById(R.id.viewPager);
        SellingImageAdapter adapter = new SellingImageAdapter(activity,images);
        viewPager.setAdapter(adapter);

        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                market_image.setVisibility(View.VISIBLE);
                movie = beanMarkets.get(position);
                if (pos < 5) {
                    switch (pos) {
                        case 1:
                            Log.e("image1->", "image2");
                            Glide.with(activity).load(movie.imagePath2).into(market_image);
                            pos++;
                            break;
                        case 2:
                            Log.e("image2->", movie.imagePath3);
                            Glide.with(activity).load(movie.imagePath3).into(market_image);
                            pos++;
                            break;
                        case 3:
                            Log.e("image3->", movie.imagePath4);
                            Glide.with(activity).load(movie.imagePath4).into(market_image);
                            pos++;
                            break;
                        case 4:
                            Log.e("image4->", "image5");
                            Glide.with(activity).load(movie.imagePath5).into(market_image);
                            pos++;
                            break;

                    }

                }
            }
        });
        iv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                market_image.setVisibility(View.VISIBLE);
                movie = beanMarkets.get(position);
                if (pos > 1) {
                    switch (pos) {
                        case 2:
                            Log.e("image2->", "image1");
                            Glide.with(activity).load(movie.imagePath1).into(market_image);
                            pos--;
                            break;
                        case 3:
                            Log.e("image3->", "image2");
                            Glide.with(activity).load(movie.imagePath2).into(market_image);
                            pos--;
                            break;
                        case 4:
                            Log.e("image4->", "image3");
                            Glide.with(activity).load(movie.imagePath3).into(market_image);
                            pos--;
                            break;
                        case 5:
                            Log.e("image5->", "image4");
                            Glide.with(activity).load(movie.imagePath4).into(market_image);
                            pos--;
                            break;

                    }

                }
            }
        });


        txt_fname.setTypeface(Utility.getTypeFace());
        txt_fullname.setTypeface(Utility.getTypeFace());
        category_name.setTypeface(Utility.getTypeFace());
        txt_desc.setTypeface(Utility.getTypeFace());
        txt_rs.setTypeface(Utility.getTypeFace());

        txt_fullname.setText(beanMarkets.get(position).fullName);
        txt_title.setText(beanMarkets.get(position).advertisementTitle);
        txt_fname.setText(beanMarkets.get(position).fullName.substring(0, 1));
        category_name.setText(beanMarkets.get(position).marketCategoryName);
        txt_desc.setText(Html.fromHtml(beanMarkets.get(position).description));
        txt_rs.setText(beanMarkets.get(position).offerPrice);
        Glide.with(activity).load(beanMarkets.get(position).imagePath1).into(market_image);


        LinearLayout lnback = (LinearLayout) marketDetail.findViewById(R.id.lnbacksharing);
        LinearLayout lnprofile = (LinearLayout) marketDetail.findViewById(R.id.lnprofile);

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketDetail.dismiss();
            }
        });

        lnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity, ProfileActivity.class);
                in.putExtra("key", "market");
                activity.startActivity(in);
                activity.finish();
            }
        });

        marketDetail.show();
    }

    //delete task
    void deleteAds(final int position) {
        pd = Utility.getDialog(activity);
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Market_Advertisement_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Advertisement_Delete")) {
                            JSONArray array = object.getJSONArray("Advertisement_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();

                                        MarketMyAddFragment.getMarketData();
                                        MarketBuyingFragment.getMarketData();
                                        MarketSellingFragment.getMarketData();

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
                    map.put("AdvertisementId", beanMarkets.get(position).advertisementId);
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

    private void deleteDialoge(final int position) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Delete")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        deleteAds(position);
                        //  android.os.Process.killProcess(android.os.Process.myPid());
                        /// android.os.Process.killProcess(android.os.Process.myPid());
                        //  finish();
                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void showpopupmenu(View view, final int position) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater()
                .inflate(R.menu.task_todo_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        Intent in = new Intent(activity, MarketCreateMyAddsActivity.class);
                        in.putExtra("add_or_edit", "edit");
                        in.putExtra("advertisementId", beanMarkets.get(position).advertisementId);
                        activity.startActivity(in);
                        break;

                    case R.id.menu_delete:
                        deleteDialoge(position);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

}
