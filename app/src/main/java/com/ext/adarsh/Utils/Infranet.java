package com.ext.adarsh.Utils;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ExT-Emp-005 on 16-03-2017.
 */

public class Infranet extends MultiDexApplication {

    public static final String TAG = Infranet.class.getSimpleName();
    public static Context context;
    public static Infranet rest;
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        rest = this;
        context = getApplicationContext();
    }

    public static synchronized Infranet getInstance() {
        return rest;
    }

    public static Context getAppContext() {
        return Infranet.context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
