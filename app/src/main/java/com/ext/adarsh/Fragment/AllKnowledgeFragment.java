package com.ext.adarsh.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.ext.adarsh.Activity.TopicAddingActivity;
import com.ext.adarsh.Adapter.AdapterKnowledgeAll;
import com.ext.adarsh.Bean.BeanKnowledge;
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

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;

/**
 * Created by ExT-Emp-001 on 11-07-2017.
 */
public class AllKnowledgeFragment extends Fragment {
    static ProgressDialog pd;

    private static List<BeanKnowledge> knowledgeList = new ArrayList<>();

    public static AdapterKnowledgeAll mAdapter;


    @BindView(R.id.topicadd_float)
    FloatingActionButton topicadd_float;

    static Activity activity;
    static RecyclerView rv_knowledge_all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.knowledge_allknowledge, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        pd = Utility.getDialog(activity);

        if (Utility.getKnowledgeAdd().equalsIgnoreCase("Y")){
            topicadd_float.setVisibility(View.VISIBLE);
        }else {
            topicadd_float.setVisibility(View.GONE);
        }
        rv_knowledge_all=(RecyclerView)view.findViewById(R.id.rv_knowledge_all);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_knowledge_all.setLayoutManager(mLayoutManager);
        rv_knowledge_all.setItemAnimator(new DefaultItemAnimator());

        topicadd_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, TopicAddingActivity.class));
                activity.finish();
            }
        });


        getAllDataKnowledge();
    }

  public static   void getAllDataKnowledge() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Knowledge_Topic_Filter_By_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("know_all", response);
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("Knowledge_Topic_Array")) {
                            JSONArray jsonArray = object.getJSONArray("Knowledge_Topic_Array");
                            if (jsonArray.length() != 0) {
                                knowledgeList.clear();
                                knowledgeList.addAll((Collection<? extends BeanKnowledge>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanKnowledge>>() {
                                }.getType()));
                                mAdapter = new AdapterKnowledgeAll(knowledgeList, activity,"all_know");
                                rv_knowledge_all.setAdapter(mAdapter);

                                pd.dismiss();
                            } else {
                                rv_knowledge_all.setAdapter(null);
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
            pd.dismiss();

        }
    }

}
