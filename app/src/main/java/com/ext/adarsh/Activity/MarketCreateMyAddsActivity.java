package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ext.adarsh.Adapter.AdapterMarket;
import com.ext.adarsh.Adapter.AdapterMarketCategory;
import com.ext.adarsh.Bean.BeanMarket;
import com.ext.adarsh.Bean.BeanMarketCategory;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class MarketCreateMyAddsActivity extends AppCompatActivity {


    public static RecyclerView recylermarket;
    public static ProgressDialog pd, pd2;
    public static Activity activity, activity2;

    @BindView(R.id.add_my_adds)
    FloatingActionButton add_my_adds;

    @BindView(R.id.search_people)
    EditText search_people;
    AdapterMarket adapter;
    Dialog my_add_dialog;
    ArrayList<BeanMarket> beanMarkets = new ArrayList<>();
    ArrayList<BeanMarketCategory> beanMarketCategories_list = new ArrayList<>();
    AdapterMarketCategory adapterMarketCategory;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    FileOutputStream fo;
    String profileimg = "";
    String imagename;
    private String userChoosenTask;
    ImageView iv_product1, iv_product2, iv_product3, iv_product4, iv_product5;
    private String image_Id;
    int int_image_id;
    String[] product_imgBase64;
    String[] product_img_name;
    ArrayList<String> str_image_name = new ArrayList<>();
    ArrayList<String> str_image_base64 = new ArrayList<>();
    ArrayList<String> categories = new ArrayList<String>();
    Spinner spiner_add_types, spinner_market_category;
    EditText et_add_title, et_offer_price, et_offer_description;
    private int advertise_type = 0;
    private String str_advertise_type = "0";
    private String category_type = "0";

    String image1 = "0", image2 = "0", image3 = "0", image4 = "0", image5 = "0";
    LinearLayout ll_header, ll_header1, ll_artical_publish, ll_artical_submit;
    private String add_or_edit, advertisementId;
    String imagePath1, imagePath2, imagePath3, imagePath4, imagePath5;

    ArrayList<String> str_image = new ArrayList<String>();
    ArrayList<String> str_name = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_create_my_adds);

        activity = this;
        activity2 = this;
        pd = Utility.getDialog(activity);
        pd2 = Utility.getDialog(activity2);

        categories.clear();
        categories.add(0, "Select Advertisement Type");
        categories.add(1, "Selling");
        categories.add(2, "Buying");

        for (int i = 0; i < 5; i++) {
            str_image.add(i, "");
            str_name.add(i, "");
        }

        spiner_add_types = (Spinner) findViewById(R.id.spiner_add_types);
        spinner_market_category = (Spinner) findViewById(R.id.spinner_market_category);

        iv_product1 = (ImageView) findViewById(R.id.iv_product1);
        iv_product2 = (ImageView) findViewById(R.id.iv_product2);
        iv_product3 = (ImageView) findViewById(R.id.iv_product3);
        iv_product4 = (ImageView) findViewById(R.id.iv_product4);
        iv_product5 = (ImageView) findViewById(R.id.iv_product5);
        ll_artical_publish = (LinearLayout) findViewById(R.id.ll_artical_publish);
        ll_artical_submit = (LinearLayout) findViewById(R.id.ll_artical_submit);
        ll_header = (LinearLayout) findViewById(R.id.ll_header);
        ll_header1 = (LinearLayout) findViewById(R.id.ll_header1);

        Bundle bundle = getIntent().getExtras();
        add_or_edit = bundle.getString("add_or_edit");

        if (add_or_edit.equalsIgnoreCase("add")) {
            ll_header1.setVisibility(View.GONE);
            ll_artical_submit.setVisibility(View.GONE);
            ll_header.setVisibility(View.VISIBLE);
            ll_artical_publish.setVisibility(View.VISIBLE);
        } else {
            advertisementId = bundle.getString("advertisementId");
            ll_header1.setVisibility(View.VISIBLE);
            ll_artical_submit.setVisibility(View.VISIBLE);
            ll_header.setVisibility(View.GONE);
            ll_artical_publish.setVisibility(View.GONE);
        }

        //  LinearLayout ll_artical_publish = (LinearLayout) findViewById(R.id.ll_artical_publish);
        LinearLayout ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        LinearLayout lnback = (LinearLayout) findViewById(R.id.lnback);


        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i7 = new Intent(activity, MarketActivity.class);
                i7.putExtra("id","2");
                i7.putExtra("key", "");
                startActivity(i7);
                finish();

            }
        });
        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i7 = new Intent(activity, MarketActivity.class);
                i7.putExtra("id","2");
                i7.putExtra("key", "");
                startActivity(i7);
                finish();

            }
        });

        iv_product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_Id = "1";
                image1 = "1";
                int_image_id = 0;
                selectImage();
            }
        });

        iv_product2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_Id = "2";
                image2 = "2";
                int_image_id = 1;
                selectImage();
            }
        });

        iv_product3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_Id = "3";
                image3 = "3";
                int_image_id = 2;
                selectImage();
            }
        });

        iv_product4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_Id = "4";
                image4 = "4";
                int_image_id = 3;
                selectImage();
            }
        });

        iv_product5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_Id = "5";
                image5 = "5";
                int_image_id = 4;
                selectImage();
            }
        });


        et_add_title = (EditText) findViewById(R.id.et_add_title);
        et_offer_price = (EditText) findViewById(R.id.et_offer_price);
        et_offer_description = (EditText) findViewById(R.id.et_offer_description);

        et_add_title.addTextChangedListener(new MyTextWatcher(et_add_title));
        et_offer_price.addTextChangedListener(new MyTextWatcher(et_offer_price));
        et_offer_description.addTextChangedListener(new MyTextWatcher(et_offer_description));

        TextView tv_add_types = (TextView) findViewById(R.id.tv_add_types);
        TextView tv_add_title = (TextView) findViewById(R.id.tv_add_title);
        TextView tv_market_category = (TextView) findViewById(R.id.tv_market_category);
        TextView tv_offer_price = (TextView) findViewById(R.id.tv_offer_price);
        TextView tv_offer_description = (TextView) findViewById(R.id.tv_offer_description);

        TextView tv_header = (TextView) findViewById(R.id.tv_header);
        TextView tv_header1 = (TextView) findViewById(R.id.tv_header1);
        TextView tv_publish = (TextView) findViewById(R.id.tv_publish);
        TextView tv_submit = (TextView) findViewById(R.id.tv_submit);
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        tv_header.setTypeface(Utility.getTypeFaceTab());
        tv_header1.setTypeface(Utility.getTypeFaceTab());
        tv_publish.setTypeface(Utility.getTypeFaceTab());
        tv_submit.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());

        et_add_title.setTypeface(Utility.getTypeFace());
        et_offer_price.setTypeface(Utility.getTypeFace());
        et_offer_description.setTypeface(Utility.getTypeFace());
        tv_add_types.setTypeface(Utility.getTypeFace());
        tv_add_title.setTypeface(Utility.getTypeFace());
        tv_market_category.setTypeface(Utility.getTypeFace());
        tv_offer_price.setTypeface(Utility.getTypeFace());
        tv_offer_description.setTypeface(Utility.getTypeFace());

        if (add_or_edit.equalsIgnoreCase("add")) {

        } else {
            for (int i = 0; i < 5; i++) {
                str_image_base64.add("");
                str_image_name.add("");
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_add_types.setAdapter(dataAdapter);
        spiner_add_types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                advertise_type = i;
                if (i == 0) {
                    str_advertise_type = "0";
                } else if (i == 1) {
                    str_advertise_type = "S";
                } else {
                    str_advertise_type = "B";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (add_or_edit.equalsIgnoreCase("add")) {
            ll_artical_publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < str_image_base64.size(); i++) {
                        if (str_image_base64.get(i).equalsIgnoreCase("")) {
                            str_image.add(i, "");
                        } else {
                            str_image.add(i, str_image_base64.get(i));
                        }
                    }
                    for (int i = 0; i < str_image_base64.size(); i++) {
                        if (str_image_name.get(i).equalsIgnoreCase("")) {
                            str_name.add(i, "");
                        } else {
                            str_name.add(i, str_image_name.get(i) + ".jpg");
                        }
                    }
                    if (et_add_title.getText().toString().trim().isEmpty()) {
                        et_add_title.setError("Please Write Product Title");
                        requestFocus(et_add_title);
                    } else if (advertise_type == 0) {
                        Toast.makeText(activity, "Please Select Advertisement Type", Toast.LENGTH_SHORT).show();
                    } else if (category_type.equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "Please Select Category", Toast.LENGTH_SHORT).show();
                    } else if (et_offer_price.getText().toString().trim().isEmpty()) {
                        et_offer_price.setError("Please Enter Product Price");
                        requestFocus(et_offer_price);
                    } /*else if (image1.equalsIgnoreCase("0") || image2.equalsIgnoreCase("0") ||
                            image3.equalsIgnoreCase("0") || image4.equalsIgnoreCase("0") ||
                            image5.equalsIgnoreCase("0")) {

                        Toast.makeText(activity, "Please Select All Image", Toast.LENGTH_SHORT).show();
                    }*/ else if (image1.equalsIgnoreCase("0") && image2.equalsIgnoreCase("0") &&
                            image3.equalsIgnoreCase("0") && image4.equalsIgnoreCase("0") &&
                            image5.equalsIgnoreCase("0")) {

                        Toast.makeText(activity, "Please Select All Image", Toast.LENGTH_SHORT).show();
                    } else if (et_offer_description.getText().toString().trim().isEmpty()) {
                        et_offer_description.setError("Please Write Product Description");
                        requestFocus(et_offer_description);
                    } else {
                        postAdvertise(et_add_title.getText().toString(),
                                et_offer_price.getText().toString(),
                                et_offer_description.getText().toString(), str_image.get(0), str_image.get(1), str_image.get(2), str_image.get(3), str_image.get(4));
                    }
                }
            });

        } else {
            ll_artical_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    for (int i = 0; i < 5; i++) {
                        if (str_image_base64.get(i).equalsIgnoreCase("")) {
                            str_image.add(i, "");
                        } else {
                            str_image.add(i, str_image_base64.get(i));
                        }
                    }
                    for (int i = 0; i < 5; i++) {
                        if (str_image_name.get(i).equalsIgnoreCase("")) {
                            str_name.add(i, "");
                        } else {
                            str_name.add(i, str_image_name.get(i) + ".jpg");
                        }
                    }
                    if (et_add_title.getText().toString().trim().isEmpty()) {
                        et_add_title.setError("Please Write Product Title");
                        requestFocus(et_add_title);
                    } else if (advertise_type == 0) {
                        Toast.makeText(activity, "Please Select Advertisement Type", Toast.LENGTH_SHORT).show();
                    } else if (category_type.equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "Please Select Category", Toast.LENGTH_SHORT).show();
                    } else if (et_offer_price.getText().toString().trim().isEmpty()) {
                        et_offer_price.setError("Please Enter Product Price");
                        requestFocus(et_offer_price);
                    } else {
                        UpdateEvents(et_add_title.getText().toString(),
                                et_offer_price.getText().toString(),
                                et_offer_description.getText().toString(), str_image.get(0), str_image.get(1), str_image.get(2), str_image.get(3), str_image.get(4));
                    }
                }
            });
        }

        if (!add_or_edit.equalsIgnoreCase("add")) {
            getEditMyAds();
        }
        getMarketCategory();
    }


    private void getEditMyAds() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Market_Advertisement_Select_By_AdvertisementId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("edit_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if (object.has("Market_Advertisement_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Market_Advertisement_Array");
                            if (jsonArray.length() != 0) {

                                beanMarkets.clear();
                                beanMarkets.addAll((Collection<? extends BeanMarket>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMarket>>() {
                                }.getType()));
                                et_add_title.setText(beanMarkets.get(0).advertisementTitle);
                                Log.e("advertisementTitle", String.valueOf(et_add_title));
                                et_offer_price.setText(beanMarkets.get(0).offerPrice);
                                Log.e("offerPrice", String.valueOf(et_add_title));
                                et_offer_description.setText(Html.fromHtml(beanMarkets.get(0).description).toString());
                                Log.e("description", String.valueOf(et_add_title));
                                if (beanMarkets.get(0).advertisementType.equalsIgnoreCase("S")) {
                                    spiner_add_types.setSelection(1);
                                } else {
                                    spiner_add_types.setSelection(2);
                                }

                                 String marketCategoryId = beanMarkets.get(0).marketCategoryId;

                                for (int x = 0; beanMarketCategories_list.size() > x; x++) {
                                    String inner_marketCategoryId = beanMarketCategories_list.get(x).marketCategoryId;
                                    if (marketCategoryId.equalsIgnoreCase(inner_marketCategoryId)) {
                                        int pos = x;
                                        Log.e("xxxxxxxxx", "xxxxxxxx++" + x);
                                        spinner_market_category.setSelection(x);
                                        spinner_market_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                category_type = beanMarketCategories_list.get(i).marketCategoryId;
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });

                                    }
                                }

                                Glide.with(activity).load(beanMarkets.get(0).imagePath1).into(iv_product1);
                                Glide.with(activity).load(beanMarkets.get(0).imagePath2).into(iv_product2);
                                Glide.with(activity).load(beanMarkets.get(0).imagePath3).into(iv_product3);
                                Glide.with(activity).load(beanMarkets.get(0).imagePath4).into(iv_product4);
                                Glide.with(activity).load(beanMarkets.get(0).imagePath5).into(iv_product5);


                                imagePath1 = beanMarkets.get(0).imagePath1;
                                imagePath2 = beanMarkets.get(0).imagePath2;
                                imagePath3 = beanMarkets.get(0).imagePath3;
                                imagePath4 = beanMarkets.get(0).imagePath4;
                                imagePath5 = beanMarkets.get(0).imagePath5;


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
                    Log.e("error", error.toString());
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("AdvertisementId", advertisementId);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    public void getMarketCategory() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Market_Category_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Market_Category_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Market_Category_Array");
                            if (jsonArray.length() != 0) {
                                beanMarketCategories_list.clear();
                                beanMarketCategories_list.add(new BeanMarketCategory("0", "Select Category"));
                                beanMarketCategories_list.addAll((Collection<? extends BeanMarketCategory>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanMarketCategory>>() {
                                }.getType()));
                                adapterMarketCategory = new AdapterMarketCategory(activity, beanMarketCategories_list);
                                spinner_market_category.setAdapter(adapterMarketCategory);
                                spinner_market_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        category_type = beanMarketCategories_list.get(i).marketCategoryId;
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                    }
                                });
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
        }
    }

    private void postAdvertise(final String title, final String price, final String description,
                               final String image1, final String image2, final String image3, final String image4, final String image5) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Market_Advertisement_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Advertisement_Add")) {
                            JSONArray array = object.getJSONArray("Advertisement_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {

                                        Intent i7 = new Intent(activity, MarketActivity.class);
                                        i7.putExtra("id","2");
                                        i7.putExtra("key", "");
                                        startActivity(i7);
                                        finish();

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
                    Log.e("erro", error.toString());
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
                    map.put("AdvertisementTitle", title);
                    map.put("AdvertisementType", str_advertise_type);
                    map.put("MarketCategoryId", category_type);
                    map.put("OfferPrice", price);
                    map.put("Description", description);
                    map.put("Image1", str_name.get(0));
                    map.put("Path1", image1);
                    map.put("Image2", str_name.get(1));
                    map.put("Path2", image2);
                    map.put("Image3", str_name.get(2));
                    map.put("Path3", image3);
                    map.put("Image4", str_name.get(3));
                    map.put("Path4", image4);
                    map.put("Image5", str_name.get(4));
                    map.put("Path5", image5);
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

    private void UpdateEvents(final String title, final String price, final String description, final String image1, final String image2, final String image3, final String image4, final String image5) {

        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Market_Advertisement_Update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("eve_res", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Advertisement_Update")) {
                            JSONArray array = object.getJSONArray("Advertisement_Update");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        finish();
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
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
                            finish();
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
                    Log.e("error", error.toString());
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


                    map.put("AdvertisementId", advertisementId);
                    map.put("AdvertisementTitle", title);
                    map.put("AdvertisementType", str_advertise_type);
                    map.put("MarketCategoryId", category_type);
                    map.put("OfferPrice", price);
                    map.put("Description", description);
                    map.put("Image1", str_name.get(0));
                    map.put("Path1", image1);
                    map.put("Image2", str_name.get(1));
                    map.put("Path2", image2);
                    map.put("Image3", str_name.get(2));
                    map.put("Path3", image3);
                    map.put("Image4", str_name.get(3));
                    map.put("Path4", image4);
                    map.put("Image5", str_name.get(4));
                    map.put("Path5", image5);
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

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.et_add_title:
                    validatetitle();
                    break;

                case R.id.et_offer_price:
                    validatPrice();
                    break;


                case R.id.et_offer_description:
                    validate_des();
                    break;
            }
        }
    }

    private boolean validatetitle() {
        String email = et_add_title.getText().toString().trim();
        if (email.isEmpty()) {
            et_add_title.setError("Please Write Product Title");
            requestFocus(et_add_title);
            return false;
        } else {
            et_add_title.setError(null);
        }
        return true;
    }

    private boolean validatPrice() {
        String email = et_offer_price.getText().toString().trim();
        if (email.isEmpty()) {
            et_offer_price.setError("Please Enter Product Price");
            requestFocus(et_offer_price);
            return false;
        } else {
            et_offer_price.setError(null);
        }
        return true;
    }

    private boolean validate_des() {
        String email = et_offer_description.getText().toString().trim();
        if (email.isEmpty()) {
            et_offer_description.setError("Please Write Product Description");
            requestFocus(et_offer_description);
            return false;
        } else {
            et_offer_description.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    //region Select Gallery or Camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

        }
    }
    //endregion

    //region Gallery
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
            } catch (IOException e) {
                pd.dismiss();
                showMsg(R.string.json_error);
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] imageBytes = bytes.toByteArray();
        profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        str_image_base64.add(int_image_id, profileimg);
        File destination = new File(Environment.getExternalStorageDirectory() + "/Intranet/UserProfile");
        if (!destination.exists()) {
            File wallpaperDirectory = new File("/sdcard/Intranet/UserProfile");
            wallpaperDirectory.mkdirs();
        }
        imagename = "intranet" + System.currentTimeMillis();

        str_image_name.add(int_image_id, imagename);
        File file = new File(new File("/sdcard/Intranet/UserProfile"), imagename);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image_Id.equalsIgnoreCase("1")) {
            iv_product1.setImageBitmap(bm);
        } else if (image_Id.equalsIgnoreCase("2")) {
            iv_product2.setImageBitmap(bm);
        } else if (image_Id.equalsIgnoreCase("3")) {
            iv_product3.setImageBitmap(bm);
        } else if (image_Id.equalsIgnoreCase("4")) {
            iv_product4.setImageBitmap(bm);
        } else {
            iv_product5.setImageBitmap(bm);
        }


    }
    //endregion

    //region Camera
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] imageBytes = bytes.toByteArray();
        profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        str_image_base64.add(int_image_id, profileimg);
        File destination = new File(Environment.getExternalStorageDirectory() + "/Intranet/UserProfile");

        if (!destination.exists()) {
            File wallpaperDirectory = new File("/sdcard/Intranet/UserProfile");
            wallpaperDirectory.mkdirs();
        }

        imagename = "intranet" + System.currentTimeMillis();
        str_image_name.add(int_image_id, imagename);
        File file = new File(new File("/sdcard/Intranet/UserProfile"), imagename);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image_Id.equalsIgnoreCase("1")) {
            iv_product1.setImageBitmap(thumbnail);
        } else if (image_Id.equalsIgnoreCase("2")) {
            iv_product2.setImageBitmap(thumbnail);
        } else if (image_Id.equalsIgnoreCase("3")) {
            iv_product3.setImageBitmap(thumbnail);
        } else if (image_Id.equalsIgnoreCase("4")) {
            iv_product4.setImageBitmap(thumbnail);
        } else {
            iv_product5.setImageBitmap(thumbnail);
        }
    }
}
