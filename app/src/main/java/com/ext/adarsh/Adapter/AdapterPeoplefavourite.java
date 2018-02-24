package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
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
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.ext.adarsh.Activity.PeopleActivity;
import com.ext.adarsh.Activity.ProfileActivity;
import com.ext.adarsh.Activity.other.OtherProfileActivity;
import com.ext.adarsh.Bean.BeanGroup;
import com.ext.adarsh.Bean.BeanPeopleDetail;
import com.ext.adarsh.Bean.BeanPeoplefavourite;
import com.ext.adarsh.Fragment.MyContact;
import com.ext.adarsh.Fragment.favourite;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class AdapterPeoplefavourite extends RecyclerView.Adapter<MyViewHolderFav> implements Filterable {

    private List<BeanPeoplefavourite> peopleList  = new ArrayList<>();
    private List<BeanPeoplefavourite> array  = new ArrayList<>();
    private ArrayList<BeanGroup> beanGroups = new ArrayList<>();
    Activity activity;
    Dialog dd;
    ProgressDialog pd;
    Dialog addGroup;
    private ArrayList<BeanPeopleDetail> beanPeopleDetails = new ArrayList<>();
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public AdapterPeoplefavourite(List<BeanPeoplefavourite> peopleList, Activity activity, RecyclerView recyclerView) {
        this.peopleList = peopleList;
        this.array = peopleList;
        this.activity = activity;

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if(!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                        if(onLoadMoreListener != null){
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public MyViewHolderFav onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolderFav viewHolder = null;
        if(viewType == 1){
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_list_item_favourite, parent, false);
            viewHolder = new MyViewHolderFav(layoutView);
        }else{
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            viewHolder = new ProgressViewHolderFav(layoutView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolderFav holder, final int position) {
        pd = Utility.getDialog(activity);
        getallgroup();
        if(holder instanceof ProgressViewHolderFav){
            //    ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }else {

            ((MyViewHolderFav)holder).txtname.setTypeface(Utility.getTypeFaceTab());
            ((MyViewHolderFav)holder).txtdiscription.setTypeface(Utility.getTypeFace());
            ((MyViewHolderFav)holder).txtname.setText(peopleList.get(position).fullName);
            ((MyViewHolderFav)holder).txtdiscription.setText(peopleList.get(position).mobileNo);
            Glide.with(activity).load(peopleList.get(position).profileImage).into(((MyViewHolderFav)holder).imageview);

            ((MyViewHolderFav)holder).lnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPeopleDetail(position,peopleList.get(position).favId);
                }
            });

            ((MyViewHolderFav)holder).contactmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showpopupmenu(view,position);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        if (peopleList == null){
            return 0;
        }else {
            return peopleList.size();
        }
    }

    private void showAlertDialog3(final int pos) {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.contact_detail_page);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout lnback = (LinearLayout)dd.findViewById(R.id.lnback);
        LinearLayout lnviewprofile = (LinearLayout)dd.findViewById(R.id.lnviewprofile);

        TextView name= (TextView) dd.findViewById(R.id.name);
        TextView txt_contact = (TextView) dd.findViewById(R.id.txt_contact);
        TextView txt_mobile = (TextView) dd.findViewById(R.id.txt_mobile);
        TextView txt_email = (TextView) dd.findViewById(R.id.txt_email);
        TextView txt_home = (TextView) dd.findViewById(R.id.txt_home);
        TextView txt_home_desc = (TextView) dd.findViewById(R.id.txt_home_desc);
        TextView txt_more = (TextView) dd.findViewById(R.id.txt_more);
        TextView txt_bday = (TextView) dd.findViewById(R.id.txt_bday);
        TextView txt_bday_name = (TextView) dd.findViewById(R.id.txt_bday_name);
        TextView txt_address = (TextView) dd.findViewById(R.id.txt_address);
        TextView txt_add = (TextView) dd.findViewById(R.id.txt_add);
        TextView viewprofile = (TextView) dd.findViewById(R.id.viewprofile);

        RelativeLayout rl_home = (RelativeLayout) dd.findViewById(R.id.rl_home);
        LinearLayout ln_address = (LinearLayout) dd.findViewById(R.id.ln_address);
        ImageView profile_img = (ImageView) dd.findViewById(R.id.profile_img);


        Glide.with(activity).load(beanPeopleDetails.get(0).profileImage).into(profile_img);
        name.setText(beanPeopleDetails.get(0).fullName);
        txt_mobile.setText(beanPeopleDetails.get(0).mobileNo);
        txt_email.setText(beanPeopleDetails.get(0).emailAddress);
        txt_home.setText(beanPeopleDetails.get(0).homeNo);
        txt_bday.setText(beanPeopleDetails.get(0).birthdate);
        txt_address.setText(beanPeopleDetails.get(0).address);
        txt_bday_name.setText(beanPeopleDetails.get(0).fullName+"'s Birthday");

        if (beanPeopleDetails.get(0).homeNo.equalsIgnoreCase("")){
            rl_home.setVisibility(View.GONE);
        }

        if (beanPeopleDetails.get(0).address.equalsIgnoreCase("")){
            ln_address.setVisibility(View.GONE);
        }

        lnviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (peopleList.get(pos).peopleId != null){
                    Intent intent = new Intent(activity, OtherProfileActivity.class);
                    intent.putExtra("peopleId", peopleList.get(pos).peopleId);
                    activity.startActivity(intent);
                }
            }
        });

        name.setTypeface(Utility.getTypeFace());
        txt_contact.setTypeface(Utility.getTypeFace());
        txt_mobile.setTypeface(Utility.getTypeFace());
        txt_email.setTypeface(Utility.getTypeFace());
        txt_home.setTypeface(Utility.getTypeFace());
        txt_home_desc.setTypeface(Utility.getTypeFace());
        txt_more.setTypeface(Utility.getTypeFace());
        txt_bday.setTypeface(Utility.getTypeFace());
        txt_bday_name.setTypeface(Utility.getTypeFace());
        txt_address.setTypeface(Utility.getTypeFace());
        txt_add.setTypeface(Utility.getTypeFace());
        viewprofile.setTypeface(Utility.getTypeFace());

        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
            }
        });
        dd.show();
    }


    public void popup(MyViewHolder holder) {

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popupcontactfavourite, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView tv1 = (TextView) popupView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) popupView.findViewById(R.id.tv2);
        TextView tv3 = (TextView) popupView.findViewById(R.id.tv3);
        TextView tv4 = (TextView) popupView.findViewById(R.id.tv4);
        TextView tv5 = (TextView) popupView.findViewById(R.id.tv5);
        TextView tv6 = (TextView) popupView.findViewById(R.id.tv6);
        TextView tv7 = (TextView) popupView.findViewById(R.id.tv7);

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(20);
        popupWindow.setOutsideTouchable(true);
        // popupWindow.showAtLocation(popupView, Gravity.RIGHT, 50,-350 );
        popupWindow.showAsDropDown(holder.contactmenu, Gravity.RIGHT, -50, 0);

    }

    public void showpopupmenu(View view){
        PopupMenu popup = new PopupMenu(activity, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.pop_up_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                       activity,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String s = String.valueOf(charSequence);

                if (s != null && s.length() > 0) {
                    ArrayList<BeanPeoplefavourite> temp = new ArrayList<>();
                    for (BeanPeoplefavourite category : array) {
                        if (category.fullName.toLowerCase().contains(s.toLowerCase())) {
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
                peopleList = (ArrayList<BeanPeoplefavourite>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return peopleList.get(position) != null ? 1 : 0;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoaded() {
        loading = false;
    }


    public void showpopupmenu(View view,final int position){
        PopupMenu popup = new PopupMenu(activity, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popupmenu_people_favourite, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.addtocontact:
                        showGroup(position);
                        break;

                    case R.id.removetofavourite:
                        removeToFavPeople(peopleList.get(position).favId);
                        break;

                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    private void showGroup(final int position) {
        addGroup = new Dialog(activity);
        addGroup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addGroup.getWindow().setWindowAnimations(R.style.DialogAnimation);
        addGroup.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addGroup.setContentView(R.layout.addgroping_dialog);

        TextView title = (TextView) addGroup.findViewById(R.id.title);

        title.setTypeface(Utility.getTypeFace());

        ImageView iv_close = (ImageView) addGroup.findViewById(R.id.iv_close);
        Button btn_save = (Button)addGroup.findViewById(R.id.btn_save);
        final Spinner spinner_group = (Spinner)addGroup.findViewById(R.id.spinner_group);

        AdGroupSp adGroupSp = new AdGroupSp(beanGroups);
        spinner_group.setAdapter(adGroupSp);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroup.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_group.getSelectedItemPosition() == 0){
                    Toast.makeText(activity, "Please Select The Group", Toast.LENGTH_SHORT).show();
                }else {
                    addToContactPeople(peopleList.get(position).emailAddress,beanGroups.get(spinner_group.getSelectedItemPosition()).categoryId);
                }
            }
        });

        Window window = addGroup.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        addGroup.show();
    }

    public void removeToFavPeople(final String id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Favorite_People_Remove_To_Favorite, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("People_Add_To_Favorite")) {
                            JSONArray array = object.getJSONArray("People_Add_To_Favorite");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        favourite.getPeopleData();
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
                    map.put("FevPeopleId", id);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            showSuccessAlertDialog(activity, activity.getResources().getString(R.string.network_message));
        }
    }

    public void addToContactPeople(final String id,final String groupid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Favorite_People_Add_To_Contact, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Favorite_Add_To_Contact")) {
                            JSONArray array = object.getJSONArray("Favorite_Add_To_Contact");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        addGroup.dismiss();
                                        MyContact.getPeopleData();
                                    } else {
                                        pd.dismiss();
                                        addGroup.dismiss();
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
                    map.put("EmailAdress", id);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("CategoryId", groupid);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            showSuccessAlertDialog(activity, activity.getResources().getString(R.string.network_message));
        }
    }

    public void getallgroup() {
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Category_Select_By_PeopleId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Category_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Category_Array");
                            if (jsonArray.length() != 0) {
                                beanGroups.clear();
                                BeanGroup beanGroup = new BeanGroup("0","Select Group");
                                beanGroups.add(beanGroup);
                                beanGroups.addAll((Collection<? extends BeanGroup>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanGroup>>() {}.getType()));
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
            }){
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
            pd.dismiss();
            showSuccessAlertDialog(activity, activity.getResources().getString(R.string.network_message));
        }
    }

    void getPeopleDetail(final int position,final String id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Favourites_People_Profile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Favourites_People_Profile_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Favourites_People_Profile_Array");
                            if (jsonArray.length() != 0) {
                                beanPeopleDetails.clear();
                                beanPeopleDetails.addAll((Collection<? extends BeanPeopleDetail>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeopleDetail>>() {
                                }.getType()));
                            }
                        }
                        pd.dismiss();
                        showAlertDialog3(position);

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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("FavouriteId", id);
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

class MyViewHolderFav extends RecyclerView.ViewHolder {

    TextView txtname, txtdiscription;
    ImageView imageview;

    LinearLayout lnmain;
    LinearLayout contactmenu;

    public MyViewHolderFav(View view) {
        super(view);
        ButterKnife.bind(this, view);
        txtname = (TextView) view.findViewById(R.id.txtname);
        txtdiscription = (TextView) view.findViewById(R.id.txtdiscription);
        imageview = (ImageView) view.findViewById(R.id.image);

        lnmain = (LinearLayout) view.findViewById(R.id.lnmain);
        contactmenu = (LinearLayout) view.findViewById(R.id.contactmenu);
    }
}

class ProgressViewHolderFav extends MyViewHolderFav{

    public ProgressBar progressBar;

    public ProgressViewHolderFav(View itemView) {
        super(itemView);
        progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
    }
}