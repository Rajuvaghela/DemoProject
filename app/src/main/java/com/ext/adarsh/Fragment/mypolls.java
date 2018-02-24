package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.ext.adarsh.Activity.AddPollsActivity;
import com.ext.adarsh.Activity.PollsActivity;
import com.ext.adarsh.Adapter.Adapter1BranchMyPolls;
import com.ext.adarsh.Adapter.Adapter1MyPolls;
import com.ext.adarsh.Adapter.Adapter2BranchMyPolls;
import com.ext.adarsh.Adapter.Adapter2MyPolls;
import com.ext.adarsh.Bean.BeanBranchList;
import com.ext.adarsh.Bean.BeanDepartmentList;
import com.ext.adarsh.Bean.ModelClass;
import com.ext.adarsh.Bean.ModelClass2;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;


public class mypolls extends Fragment implements View.OnClickListener {

    @BindView(R.id.pollscreate_float)
    FloatingActionButton pollscreate_float;

    Activity activity;
    public static RecyclerView recylerpolls;
    ProgressDialog pd;
    List<ModelClass> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mypolls_new_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        activity = getActivity();

        ButterKnife.bind(this, activity);


        if (Utility.getPollsAdd().equalsIgnoreCase("Y")) {
            pollscreate_float.setVisibility(View.VISIBLE);
        } else {
            pollscreate_float.setVisibility(View.GONE);
        }

        recylerpolls = (RecyclerView) view.findViewById(R.id.recylerpolls);
        recylerpolls.setHasFixedSize(true);
        LinearLayoutManager lnmanager2 = new LinearLayoutManager(activity);
        recylerpolls.setLayoutManager(lnmanager2);
        recylerpolls.setItemAnimator(new DefaultItemAnimator());

        pollscreate_float.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pollscreate_float:
                Intent intent = new Intent(activity, AddPollsActivity.class);
                intent.putExtra("add_or_edit", "add");
                startActivity(intent);
                activity.finish();
                break;
        }
    }
}
