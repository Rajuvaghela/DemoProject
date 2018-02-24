package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.ext.adarsh.Adapter.NavigationDrawerListAdapter;
import com.ext.adarsh.Bean.Items;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

public class BaseActivity extends AppCompatActivity {

    protected FrameLayout frameLayout;
    public static ListView mDrawerList;
    protected ArrayList<Items> _items;
    Activity activity;

    protected static int position;
    TextView tvHeaderName, tvNo;
    LinearLayout llsignup;
    ImageView imgview;
    ProgressDialog pd;
    // public static int drawer=0;

    LinearLayout ll_notsignup;

    /**
     * This flag is used just to check that launcher activity is called first time
     * so that we can open appropriate Activity on launch and make list item position selected accordingly.
     */
    private static boolean isLaunch = true;

    /**
     * Base layout node of this Activity.
     */
    public static DrawerLayout mDrawerLayout;

    /**
     * Drawer listner class for drawer open, close etc.
     */
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String nav = "1";
    public static String actually;

    int imggrey[] = {R.drawable.activityfeed, R.drawable.people, R.drawable.annocment, R.drawable.events, R.drawable.task, R.drawable.market, R.drawable.knowledge, R.drawable.files, R.drawable.photo, R.drawable.message, R.drawable.collaborate, R.drawable.organization, R.drawable.polls, R.drawable.department, R.drawable.calender};
    int imgred[] = {R.drawable.activityfeed_red, R.drawable.people_red, R.drawable.annocment_red, R.drawable.events_red, R.drawable.task_red, R.drawable.market_red, R.drawable.knowledge_red, R.drawable.files_red, R.drawable.photos_red, R.drawable.message_red, R.drawable.collaborate_red, R.drawable.organization_red, R.drawable.polls_red, R.drawable.department_red, R.drawable.calender_red};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_base_layout);
        activity = this;
        pd = Utility.getDialog(activity);
        //  pd = getDialog(BaseActivity.this);
        //   setProgressDialog(BaseActivity.this);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        _items = new ArrayList<Items>();
        _items.add(new Items("Activity Feed", R.drawable.activityfeed));
        _items.add(new Items("People", R.drawable.people));
        _items.add(new Items("Announcements", R.drawable.annocment));
        _items.add(new Items("Events", R.drawable.events));
        _items.add(new Items("Task", R.drawable.task));
        _items.add(new Items("Approval", R.drawable.task));
        _items.add(new Items("Market", R.drawable.market));
        _items.add(new Items("Knowledge", R.drawable.knowledge));
        /*_items.add(new Items("Files", R.drawable.files));*/
        _items.add(new Items("Photo", R.drawable.photos));
        // _items.add(new Items("Message", R.drawable.message));
        //  _items.add(new Items("Collaborate", R.drawable.collaborate));
        _items.add(new Items("Polls", R.drawable.polls));
        _items.add(new Items("Organization Chart", R.drawable.organization));
        _items.add(new Items("Files", R.drawable.files));
        _items.add(new Items("Calendar", R.drawable.calender));
        _items.add(new Items("Change Password", R.drawable.calender));
        _items.add(new Items("Message", R.drawable.message));
        _items.add(new Items("Logout", R.drawable.exit));

        mDrawerList.setAdapter(new NavigationDrawerListAdapter(this, _items));
        View header = (View) getLayoutInflater().inflate(R.layout.list_view_header_layout, mDrawerList, false);
        mDrawerList.addHeaderView(header);

        llsignup = (LinearLayout) header.findViewById(R.id.nav_header_ll_signup);
        TextView tv1 = (TextView) header.findViewById(R.id.header_mail);
        TextView tv2 = (TextView) header.findViewById(R.id.header_name);
        ImageView profile_image = (ImageView) header.findViewById(R.id.profile_image);

        tv1.setText(Utility.getEmailAddressPreference());
        tv2.setText(Utility.getFullNamePreference());

        Glide.with(activity).load(Utility.getPeopleProfileImgPreference()).into(profile_image);

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());

        llsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                finish();
            }
        });


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgview = (ImageView) view.findViewById(R.id.imageView);
                openActivity(position, "sec");
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.mipmap.ic_launcher, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
//				getActionBar().setTitle(listArray[position]);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
//				getActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);


        /**
         * As we are calling BaseActivity from manifest file and this base activity is intended just to add navigation drawer in our app.
         * We have to open some activity with layout on launch. So we are checking if this BaseActivity is called first time then we are opening our first activity.
         * */
        if (isLaunch) {
            /**
             *Setting this flag false so that next time it will not open our first activity.
             *We have to use this flag because we are using this BaseActivity as parent activity to our other activity.
             *In this case this base activity will always be call when any child activity will launch.
             */
            isLaunch = false;
            openActivity(0, "first");
        }
    }

    /**
     * @param position Launching activity when any list item is clicked.
     */
    protected void openActivity(int position, String check) {

        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.
       /* if (check.equalsIgnoreCase("sec")){
            textView.setTextColor(Color.parseColor("#000000"));
            imgview.setImageResource(imggrey[position-1]);
        }*/

        switch (position) {
            case 1:
                Intent i1 = new Intent(this, MainActivity.class);
                startActivity(i1);
                finish();
                break;

            case 2:
                Intent i9 = new Intent(this, PeopleActivity.class);
                i9.putExtra("key", "home");
                startActivity(i9);
                finish();
                break;

            case 3:
                Intent i12 = new Intent(this, Annoucement.class);
                startActivity(i12);
                finish();
                break;

            case 4:
                Intent i8 = new Intent(this, EventsActivity.class);
                startActivity(i8);
                finish();
                break;

            case 5:
                Intent i11 = new Intent(this, TaskNewActivity.class);
                startActivity(i11);
                finish();
                break;

            case 6:
                Intent i19 = new Intent(this, Approval_Activity.class);
                startActivity(i19);
                finish();
                break;

            case 7:
                Intent i7 = new Intent(this, MarketActivity.class);
                i7.putExtra("id","0");
                i7.putExtra("key", "");
                startActivity(i7);
                finish();
                break;

            case 8:
                Intent i10 = new Intent(this, Knowledge.class);
                startActivity(i10);
                finish();
                break;

           /* case 9:
                Intent i4 = new Intent(this, FileActivity.class);
                startActivity(i4);
                finish();
                break;
*/
            case 9:
                Intent i6 = new Intent(this, PhotoActivity.class);
                i6.putExtra("key", "home");
                startActivity(i6);
                finish();
                break;


            case 11:
                Intent i13 = new Intent(this, Organization_Chart.class);
                startActivity(i13);
                finish();
                break;

            case 10:
                Intent intent = new Intent(activity, PollsActivity.class);
                intent.putExtra("id", "0");
                startActivity(intent);
                break;

            case 13:
                Intent i5 = new Intent(this, CalendarActivity.class);
                startActivity(i5);
                finish();
                break;

            case 12:
                Intent i4 = new Intent(activity, FileActivity.class);
                i4.putExtra("file_move_or_not", "");
                startActivity(i4);
                finish();

                break;

            case 14:
                Intent i14 = new Intent(this, ChangePassword.class);
                startActivity(i14);
                finish();
                break;
            case 15:
                Intent i3 = new Intent(this, Message.class);
                startActivity(i3);
                finish();
                break;
            case 16:
                showAlertDialog();
                break;

            default:
                break;
        }
    }

    public void showAlertDialog() {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to logout?")
                .setNegativeListener(activity.getString(R.string.negativeButton), new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setPositiveListener(activity.getString(R.string.positiveButton), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        onLogOut();
                    }
                }).show();
    }

    public void onLogOut() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Logout, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Logout_Array")) {
                            JSONArray array = object.optJSONArray("Logout_Array");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        emptyPref();
                                        startActivity(new Intent(activity, LoginActivity.class));
                                        finish();
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            pd.dismiss();
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
                    map.put("PeopleId", Utility.getPeopleIdPreference());
                    map.put("Tokan", Utility.getTokenIdPreference());
                    map.put("DeviceType", "Android");
                    map.put("DeviceName", Utility.getDeviceName());
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            showMsg(R.string.network_message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void emptyPref() {
        Utility.setTokenIdBank();
        Utility.setPeopleIdBank();
        Utility.setFullNameBank();
        Utility.setEmailAddressBank();
        Utility.setMobileNoBank();
        Utility.setDepartmentIdBank();
        Utility.setBranchIdBank();
        Utility.setRegionIdBank();
        Utility.setPeopleProfileImgBank();
        Utility.setHashKeyBank();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /* We can override onBackPressed method to toggle navigation drawer*/
    @Override
    public void onBackPressed() {
        // getDrawer();
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            mDrawerLayout.openDrawer(mDrawerList);
        }
    }

    public static void getDrawer(String key) {
        actually = key;
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            mDrawerLayout.openDrawer(mDrawerList);
        }
    }

}
