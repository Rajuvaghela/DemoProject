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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.ext.adarsh.Activity.PeopleActivity;
import com.ext.adarsh.Activity.ProfileActivity;
import com.ext.adarsh.Activity.other.OtherProfileActivity;
import com.ext.adarsh.Bean.BeanMyContact;
import com.ext.adarsh.Bean.BeanPeopleDetail;
import com.ext.adarsh.Bean.BeanPeopleNew;
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

public class AdapterMyContact extends RecyclerView.Adapter<MyViewHolderContact> implements Filterable {

    private ArrayList<BeanPeopleDetail> beanPeopleDetails = new ArrayList<>();
    // private List<BeanPeopleNew> peopleList;

    private ArrayList<BeanMyContact> contacteList = new ArrayList<>();
    ArrayList<BeanMyContact> array = new ArrayList<>();
    Activity activity;
    Dialog dd;
    ProgressDialog pd;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public AdapterMyContact(ArrayList<BeanMyContact> contacteList, Activity activity, RecyclerView recyclerView) {
        this.contacteList = contacteList;
        this.array = contacteList;
        this.activity = activity;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public MyViewHolderContact onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolderContact viewHolder = null;
        if (viewType == 1) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list, parent, false);
            viewHolder = new MyViewHolderContact(layoutView);
        } else {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            viewHolder = new ProgressViewHolderContact(layoutView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolderContact holder, final int position) {
        pd = Utility.getDialog(activity);

        if (holder instanceof ProgressViewHolderContact) {
            //    ((ProgressViewHolderContact)holder).progressBar.setIndeterminate(true);
        } else {
            final BeanMyContact contact = contacteList.get(position);

            ((MyViewHolderContact) holder).txtname.setTypeface(Utility.getTypeFaceTab());
            ((MyViewHolderContact) holder).txtdiscription.setTypeface(Utility.getTypeFace());

            ((MyViewHolderContact) holder).txtname.setText(contact.fullName);
            ((MyViewHolderContact) holder).txtdiscription.setText(contact.mobileNo);
            Glide.with(activity).load(contacteList.get(position).profileImage).into(((MyViewHolderContact) holder).imageview);

            ((MyViewHolderContact) holder).lnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPeopleDetail(position, contact.contactId);
                }
            });

        /*holder.contactmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup(holder);
                }
            });*/
            ((MyViewHolderContact) holder).contactmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showpopupmenu(view, position);
                }
            });
        }
    }

    public void showpopupmenu(View view, final int position) {
        PopupMenu popup = new PopupMenu(activity, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popupmenu_contact, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.addtofavourite:
                        addToFavPeople(contacteList.get(position).contactId);
                        break;
                    case R.id.remove_contact:
                        deletePeople(contacteList.get(position).contactId);
                        break;

                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    public void deletePeople(final String id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_People_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Contact_People_Delete")) {
                            JSONArray array = object.getJSONArray("Contact_People_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        MyContact.getPeopleData();
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
                    map.put("ContactId", id);
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

    @Override
    public int getItemCount() {
        if (contacteList == null) {
            return 0;
        } else {
            return contacteList.size();
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

        LinearLayout lnback = (LinearLayout) dd.findViewById(R.id.lnback);
        LinearLayout lnviewprofile = (LinearLayout) dd.findViewById(R.id.lnviewprofile);

        TextView name = (TextView) dd.findViewById(R.id.name);
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
        txt_bday_name.setText(beanPeopleDetails.get(0).fullName + "'s Birthday");

        if (beanPeopleDetails.get(0).homeNo.equalsIgnoreCase("")) {
            rl_home.setVisibility(View.GONE);
        }

        if (beanPeopleDetails.get(0).address.equalsIgnoreCase("")) {
            ln_address.setVisibility(View.GONE);
        }

        lnviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contacteList.get(pos).peopleFlag.equalsIgnoreCase("I")) {
                    Intent intent = new Intent(activity, OtherProfileActivity.class);
                    intent.putExtra("peopleId", contacteList.get(pos).peopleId);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Can't open profile of " + contacteList.get(pos).fullName, Toast.LENGTH_SHORT).show();

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
        View popupView = layoutInflater.inflate(R.layout.popupcontact, null);
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
        popupWindow.showAtLocation(popupView, Gravity.RIGHT, 50, -350);
        popupWindow.showAsDropDown(holder.contactmenu, Gravity.RIGHT, -50, 0);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return contacteList.get(position) != null ? 1 : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String s = String.valueOf(charSequence);

                if (s != null && s.length() > 0) {
                    ArrayList<BeanMyContact> temp = new ArrayList<>();
                    for (BeanMyContact category : array) {
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
                contacteList = (ArrayList<BeanMyContact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    void getPeopleDetail(final int position, final String id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_People_Profile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Contact_People_Profile_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Contact_People_Profile_Array");
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
                    map.put("ContactId", id);
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

    public void addToFavPeople(final String id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Contact_People_Add_To_Favorite, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Contact_Add_To_Favorite")) {
                            JSONArray array = object.getJSONArray("Contact_Add_To_Favorite");
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
                    map.put("ContactId", id);
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

}

class MyViewHolderContact extends RecyclerView.ViewHolder {

    TextView txtname, txtdiscription;
    ImageView imageview;

    LinearLayout lnmain, contactmenu;

    public MyViewHolderContact(View view) {
        super(view);
        ButterKnife.bind(this, view);
        txtname = (TextView) view.findViewById(R.id.txtname);
        txtdiscription = (TextView) view.findViewById(R.id.txtdiscription);
        imageview = (ImageView) view.findViewById(R.id.image);
        lnmain = (LinearLayout) view.findViewById(R.id.lnmain);
        contactmenu = (LinearLayout) view.findViewById(R.id.contactmenu);
    }
}

class ProgressViewHolderContact extends MyViewHolderContact {

    public ProgressBar progressBar;

    public ProgressViewHolderContact(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }
}