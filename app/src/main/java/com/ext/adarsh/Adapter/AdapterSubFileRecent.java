package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-011 on 11/24/2017.
 */


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
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
import com.ext.adarsh.Activity.Pdf;
import com.ext.adarsh.Activity.SubFolderActivityRecent;
import com.ext.adarsh.Bean.BeanFileFolderSharePeopleList;
import com.ext.adarsh.Bean.BeanPeopleFileShare;
import com.ext.adarsh.Bean.BeanSubFiles;
import com.ext.adarsh.Bean.ModelClass3;
import com.ext.adarsh.Fragment.MyDrive;
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
import cn.refactor.lib.colordialog.PromptDialog;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class AdapterSubFileRecent extends RecyclerView.Adapter<AdapterSubFileRecent.MyViewHolder> {
    private List<BeanSubFiles> filesList = new ArrayList<>();
    // private List<BeanFileArray> parent_id = new ArrayList<>();
    Dialog fileshare, file, subfile, dd;
    Activity activity;
    static Activity activity2;
    int count = 0;
    int count2 = 0;
    ProgressDialog pd;
    Dialog addfile;
    Dialog openImage;
    Dialog dialog_share_file;
    static Dialog open_tag_dialog_people;
    private static ArrayList<BeanPeopleFileShare> contact_list = new ArrayList<>();
    static RecyclerView rv_select_person;
    RecyclerView rv_allready_shared_person;
    private ArrayList<BeanFileFolderSharePeopleList> beanFileFolderSharePeopleLists = new ArrayList<>();
    public static AdapterFileSharePeopleList adapter3;
    String canview_or_canedit = "V";
    static RecyclerView recyclerview5;
    static RecyclerView recyclerview6;
    static Adapter1FileShare2 adapter1people;
    static RecyclerView.LayoutManager recylerViewLayoutManager3;
    public static RecyclerView.Adapter recyclerview_adapter3;
    public static List<ModelClass3> item_list3 = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        @BindView(R.id.lnshareopen)
        LinearLayout lnshareopen;


        @BindView(R.id.ln_messagelistmain)
        LinearLayout ln_messagelistmain;


        @BindView(R.id.firstname2)
        TextView firstname2;

        @BindView(R.id.folderimage)
        ImageView folderimage;

        LinearLayout ll_file;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            title = (TextView) view.findViewById(R.id.file_title);
            ll_file = (LinearLayout) view.findViewById(R.id.ll_file);
        }
    }

    public AdapterSubFileRecent(List<BeanSubFiles> messqageList, Activity activity) {
        this.filesList = messqageList;
        this.activity = activity;
        this.activity2 = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_list_item2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        pd = Utility.getDialog(activity);
        holder.title.setTypeface(Utility.getTypeFace());

        final BeanSubFiles movie = filesList.get(position);
        holder.title.setText(movie.folderName);
        Log.e("icon", "" + movie.icon);
        Glide.with(activity).load(movie.icon).into(holder.folderimage);
        holder.lnshareopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filesharingDialog(position, movie.fileId, movie.folderName, movie.isFile, movie.icon,
                        movie.fullPath, movie.filePath);
            }
        });

        holder.ln_messagelistmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubFolderActivityRecent.getSubFileData(movie.fileId, movie.parentId);
            }
        });

        final String url = filesList.get(position).filePath;
        holder.ll_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filesList.get(position).isFile.equalsIgnoreCase("N")) {
                    Intent i = new Intent(activity, SubFolderActivityRecent.class);
                    i.putExtra("file_id", movie.fileId);
                    i.putExtra("parent_id", movie.parentId);
                    i.putExtra("file_move_or_not", SubFolderActivityRecent.file_move_or_not);
                    activity.startActivity(i);
                    activity.finish();
                } else {

                    String file = url.substring(url.lastIndexOf("/") + 1);
                    String file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                    Log.e("filename: ", "" + file);
                    Log.e("extension: ", "" + file_extension);

                    if (file_extension.equalsIgnoreCase(".jpg")) {
                        openImage(filesList.get(position).filePath, file);
                    } else if (file_extension.equalsIgnoreCase(".pdf")) {
                        Intent intent = new Intent(activity, Pdf.class);
                        intent.putExtra("file_name", file);
                        intent.putExtra("pdf", filesList.get(position).filePath);
                        // intent.putExtra("file_path", list.get(position).filePath);
                        activity.startActivity(intent);
                    } else {
                        //   DownloadFile(url);
                    }
                }

                /*
               */
            }
        });
    }

    private void openImage(String file_path, String file_name) {
        openImage = new Dialog(activity);
        openImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openImage.getWindow().setWindowAnimations(R.style.DialogAnimation);
        openImage.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        openImage.setContentView(R.layout.popup_show_image);
        Window window = openImage.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        ImageView iv_attachement_image = (ImageView) openImage.findViewById(R.id.iv_attachement_image);
        TextView tv_header = (TextView) openImage.findViewById(R.id.tv_header);
        tv_header.setTypeface(Utility.getTypeFaceTab());
        tv_header.setText(file_name);
        Glide.with(activity).load(file_path).into(iv_attachement_image);
        LinearLayout lnback = (LinearLayout) openImage.findViewById(R.id.lnback);
        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage.dismiss();
            }
        });
        openImage.show();
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }


    private void filesharingDialog(int pos, final String fileid, final String oldFileName,
                                   final String isFileFlag, String icon, final String fullPath, final String filePath) {
        fileshare = new Dialog(activity);
        fileshare.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fileshare.getWindow().setWindowAnimations(R.style.DialogAnimation);
        fileshare.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        fileshare.setContentView(R.layout.file_shareing);

        Window window = fileshare.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView file_name = (TextView) fileshare.findViewById(R.id.file_name);

        TextView txt2 = (TextView) fileshare.findViewById(R.id.txt2);
        TextView txt4 = (TextView) fileshare.findViewById(R.id.txt4);
        TextView txt5 = (TextView) fileshare.findViewById(R.id.txt5);
        TextView txt6 = (TextView) fileshare.findViewById(R.id.txt6);
        TextView txt10 = (TextView) fileshare.findViewById(R.id.txt10);
        ImageView file_icon = (ImageView) fileshare.findViewById(R.id.file_icon);

        file_name.setTypeface(Utility.getTypeFace());
        txt2.setTypeface(Utility.getTypeFace());
        txt4.setTypeface(Utility.getTypeFace());
        txt5.setTypeface(Utility.getTypeFace());
        txt6.setTypeface(Utility.getTypeFace());
        txt10.setTypeface(Utility.getTypeFace());

        file_name.setText(oldFileName);
        Glide.with(activity).load(icon).into(file_icon);
        LinearLayout lnfile_close = (LinearLayout) fileshare.findViewById(R.id.lnfile_close);
        LinearLayout ll_rename = (LinearLayout) fileshare.findViewById(R.id.ll_rename);
        LinearLayout ll_delete = (LinearLayout) fileshare.findViewById(R.id.ll_delete);
        LinearLayout ll_download = (LinearLayout) fileshare.findViewById(R.id.ll_download);
        LinearLayout ll_move_dialog = (LinearLayout) fileshare.findViewById(R.id.ll_move_dialog);
        LinearLayout ll_share_file_or_folder = (LinearLayout) fileshare.findViewById(R.id.ll_share_file_or_folder);


        ll_share_file_or_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPeopleData(fileid);
            }
        });
        ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFile(filePath);
            }
        });
        lnfile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileshare.dismiss();
            }
        });
        ll_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Renamefiledialog(fileid, oldFileName, isFileFlag);
            }
        });
        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialoge(fileid);
            }
        });
        ll_move_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubFolderActivityRecent.rl_move_or_copy.setVisibility(View.VISIBLE);
                SubFolderActivityRecent.upload_float.setVisibility(View.GONE);
                AppConstant.MoveFileId = fileid;
                AppConstant.MoveFileName = oldFileName;
                AppConstant.MoveFilePath = fullPath;
                AppConstant.MoveIsFileFlag = isFileFlag;
                SubFolderActivityRecent.file_move_or_not = "yes";
                fileshare.dismiss();
            }
        });
        fileshare.show();
    }

    public void getPeopleData(final String fileid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Share_People_List, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res_people", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            Toast.makeText(activity2, "Please Try again", Toast.LENGTH_SHORT).show();
                            activity2.finish();
                            pd.dismiss();
                        }
                        if (object.has("File_Share_People_List_Array")) {
                            JSONArray jsonArray = object.getJSONArray("File_Share_People_List_Array");
                            if (jsonArray.length() != 0) {
                                contact_list.clear();
                                item_list3.clear();
                                contact_list.addAll((Collection<? extends BeanPeopleFileShare>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanPeopleFileShare>>() {
                                }.getType()));
                                pd.dismiss();
                                DialogOpenShareWith(fileid);
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
                    Log.e("res_error", error.toString());
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
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("FileId", fileid);
                    return map;
                }
            };
            Utility.SetvollyTime30Sec(request);
            Infranet.getInstance().getRequestQueue().add(request);
        } else {
            pd.dismiss();
            Toast.makeText(activity2, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private void DialogOpenShareWith(final String fileid) {
        dialog_share_file = new Dialog(activity);
        dialog_share_file.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_share_file.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog_share_file.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_share_file.setContentView(R.layout.popup_share_with_list);
        Window window = dialog_share_file.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Can View");
        categories.add("Can Edit");
// tv_select_person ,tv_visible_person


        TextView tv_reg_heading = (TextView) dialog_share_file.findViewById(R.id.tv_reg_heading);
        TextView tv_share = (TextView) dialog_share_file.findViewById(R.id.tv_share);
        TextView tv_cancel = (TextView) dialog_share_file.findViewById(R.id.tv_cancel);
        TextView tv_select_person = (TextView) dialog_share_file.findViewById(R.id.tv_select_person);
        TextView tv_visible_person = (TextView) dialog_share_file.findViewById(R.id.tv_visible_person);
        tv_reg_heading.setTypeface(Utility.getTypeFaceTab());
        tv_share.setTypeface(Utility.getTypeFaceTab());
        tv_cancel.setTypeface(Utility.getTypeFaceTab());
        tv_select_person.setTypeface(Utility.getTypeFace());
        tv_visible_person.setTypeface(Utility.getTypeFace());

        LinearLayout lnmainback = (LinearLayout) dialog_share_file.findViewById(R.id.lnmainback);
        LinearLayout ll_share_now = (LinearLayout) dialog_share_file.findViewById(R.id.ll_share_now);
        LinearLayout ll_cancel = (LinearLayout) dialog_share_file.findViewById(R.id.ll_cancel);
        LinearLayout ll_select_person = (LinearLayout) dialog_share_file.findViewById(R.id.ll_select_person);
        rv_select_person = (RecyclerView) dialog_share_file.findViewById(R.id.rv_select_person);
        FlowLayoutManager flowLayoutManager3 = new FlowLayoutManager();
        flowLayoutManager3.setAutoMeasureEnabled(true);
        rv_select_person.setLayoutManager(flowLayoutManager3);

        rv_allready_shared_person = (RecyclerView) dialog_share_file.findViewById(R.id.rv_allready_shared_person);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_allready_shared_person.setLayoutManager(mLayoutManager);
        rv_allready_shared_person.setItemAnimator(new DefaultItemAnimator());

        Spinner spiner_canview_or_canedit = (Spinner) dialog_share_file.findViewById(R.id.spiner_canview_or_canedit);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_canview_or_canedit.setAdapter(dataAdapter);

        spiner_canview_or_canedit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    canview_or_canedit = "V";
                } else {
                    canview_or_canedit = "E";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ll_select_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagPopuppeople();
            }
        });

        ll_share_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String share_people_list = "";
                String pid = "";
                for (int i = 0; i < item_list3.size(); i++) {
                    pid += item_list3.get(i).getId() + ",";
                }
                if (pid.length() > 0) {
                    share_people_list = pid.substring(0, pid.length() - 1);
                }
                if (item_list3.size() > 0) {
                    ShareFileNow(fileid, canview_or_canedit, share_people_list);
                } else {
                    Toast.makeText(activity, "Please select person", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_share_file.dismiss();
            }
        });
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_share_file.dismiss();
            }
        });
        GetSharedFilePeopleList(fileid);
        dialog_share_file.show();
    }


    private void GetSharedFilePeopleList(final String fileid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Folder_Share_People_List, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
                            activity.finish();
                        }
                        if (object.has("File_Folder_Share_People_List_Array")) {
                            JSONArray jsonArray = object.getJSONArray("File_Folder_Share_People_List_Array");
                            if (jsonArray.length() != 0) {
                                beanFileFolderSharePeopleLists.clear();
                                beanFileFolderSharePeopleLists.addAll((Collection<? extends BeanFileFolderSharePeopleList>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFileFolderSharePeopleList>>() {
                                }.getType()));
                                adapter3 = new AdapterFileSharePeopleList(beanFileFolderSharePeopleLists, activity);
                                rv_allready_shared_person.setAdapter(adapter3);
                                pd.dismiss();
                            } else {
                                rv_allready_shared_person.setAdapter(null);
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
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("FileId", fileid);

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

    private void ShareFileNow(final String fileid, final String canview_or_canedit, final String share_people_list) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Share, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("File_share", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            Toast.makeText(activity2, "Please Try again", Toast.LENGTH_SHORT).show();
                            activity2.finish();
                            pd.dismiss();
                        }
                        if (object.has("File_Share")) {
                            JSONArray array = object.getJSONArray("File_Share");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        dialog_share_file.dismiss();
                                        // fileshare.dismiss();
                                        //   FileActivity.getFileData();
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
                    map.put("FileId", fileid);
                    map.put("FilePrivacyFlag", canview_or_canedit);
                    map.put("SharePepoleId", share_people_list);
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

    private static void openTagPopuppeople() {
        open_tag_dialog_people = new Dialog(activity2);
        open_tag_dialog_people.requestWindowFeature(Window.FEATURE_NO_TITLE);
        open_tag_dialog_people.getWindow().setWindowAnimations(R.style.DialogAnimation);
        open_tag_dialog_people.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        open_tag_dialog_people.setContentView(R.layout.tag_popup_item_layout);

        Window window = open_tag_dialog_people.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        EditText et_search = (EditText) open_tag_dialog_people.findViewById(R.id.et_search);
        final TextView iv_done = (TextView) open_tag_dialog_people.findViewById(R.id.iv_done);
        TextView header = (TextView) open_tag_dialog_people.findViewById(R.id.header);
        header.setText("Select People");
        header.setTypeface(Utility.getTypeFaceTab());
        LinearLayout lnmainback = (LinearLayout) open_tag_dialog_people.findViewById(R.id.lnmainback);
        lnmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_tag_dialog_people.dismiss();
            }
        });

        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_list3.size() > 0) {
                    //tv_select_person,rv_select_person
                    //   tv_select_person.setVisibility(View.GONE);
                    //  rv_select_person.setVisibility(View.VISIBLE);
                } else {
                    //  tv_select_person.setVisibility(View.VISIBLE);
                    //   rv_select_person.setVisibility(View.GONE);
                }

                callOnBackPress3();
                open_tag_dialog_people.dismiss();
            }
        });

        recyclerview5 = (RecyclerView) open_tag_dialog_people.findViewById(R.id.recyclerview1);
        recyclerview6 = (RecyclerView) open_tag_dialog_people.findViewById(R.id.recyclerview2);

        recylerViewLayoutManager3 = new LinearLayoutManager(activity2);
        recyclerview5.setLayoutManager(recylerViewLayoutManager3);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerview6.setLayoutManager(flowLayoutManager);

        adapter1people = new Adapter1FileShare2(activity2, contact_list);
        recyclerview5.setAdapter(adapter1people);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter1people.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        open_tag_dialog_people.show();
    }

    public static void callOnBackPress3() {
        recyclerview_adapter3 = new Adapter2MyEventsPeopleList(activity2, item_list3);
        rv_onchangelistner3();
    }

    public static void rv_onchangelistner3() {
        rv_select_person.setAdapter(recyclerview_adapter3);
        rv_select_person.addOnItemTouchListener(
                new RecyclerItemClickListener(activity2, rv_select_person, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        openTagPopuppeople();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        recyclerview_adapter3.notifyDataSetChanged();
        recyclerview6.setAdapter(recyclerview_adapter3);
        recyclerview_adapter3.notifyDataSetChanged();
    }

    private void deleteDialoge(final String fileid) {
        new PromptDialog(activity)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setContentText("Are you sure want to delete ?")
                .setTitleText("Exit")
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();

                        DeleteFile(fileid);

                    }
                })
                .setNegativeListener("cancel", new PromptDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void DeleteFile(final String fileid) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("File_Delete")) {
                            JSONArray array = object.getJSONArray("File_Delete");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        fileshare.dismiss();
                                        SubFolderActivityRecent.getSubFileData(SubFolderActivityRecent.parent_filesList.get(0).fileId, "0");

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
                    map.put("FileId", fileid);
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

    private void Renamefiledialog(final String fileid, final String oldFileName, final String isFileFlag) {

        addfile = new Dialog(activity);
        addfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addfile.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        addfile.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addfile.setContentView(R.layout.add_new_filefolder_dialog);

        Window window = addfile.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView title = (TextView) addfile.findViewById(R.id.title);
        TextView tv_rename_title = (TextView) addfile.findViewById(R.id.tv_rename_title);

        final EditText edt_groupname = (EditText) addfile.findViewById(R.id.edt_groupname);
        Button btn_save = (Button) addfile.findViewById(R.id.btn_save);
        ImageView iv_close = (ImageView) addfile.findViewById(R.id.iv_close);


        tv_rename_title.setTypeface(Utility.getTypeFace());
        title.setTypeface(Utility.getTypeFace());
        edt_groupname.setTypeface(Utility.getTypeFace());
        btn_save.setTypeface(Utility.getTypeFace());
        edt_groupname.setText(oldFileName);

        tv_rename_title.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_groupname.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter File Name", Toast.LENGTH_SHORT).show();
                } else {
                    RenameFileOrFolder(edt_groupname.getText().toString(), fileid, oldFileName, isFileFlag);
                  /*  String fid = parent_filesList.get(0).fileId;
                    String fpath = (parent_filesList.get(0)).filePath;
                    addFolder(edt_groupname.getText().toString(), fid, fpath);*/
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addfile.dismiss();
            }
        });
        addfile.show();
    }

    public void RenameFileOrFolder(final String new_folder_name, final String fileid, final String oldFileName, final String isFileFlag) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Rename, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("File_Rename")) {
                            JSONArray array = object.getJSONArray("File_Rename");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        addfile.dismiss();
                                        fileshare.dismiss();
                                        SubFolderActivityRecent.getSubFileData(SubFolderActivityRecent.parent_filesList.get(0).fileId, "0");
                                        //   getFileData();
                                        // getSubFileData(fid, "1", "onback");
                                        // getSubFileData(fid, "1", "");
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
                    map.put("FileId", fileid);
                    map.put("OldFileName", oldFileName);
                    map.put("OldIsFileFlag", isFileFlag);
                    map.put("NewFileName", new_folder_name);
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


    private void filesharingDialog2() {
        fileshare = new Dialog(activity);
        fileshare.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fileshare.getWindow().setWindowAnimations(R.style.DialogAnimation);
        fileshare.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        fileshare.setContentView(R.layout.file_shareing2);

        Window window = fileshare.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView file_name = (TextView) fileshare.findViewById(R.id.file_name);
        TextView txt1 = (TextView) fileshare.findViewById(R.id.txt1);
        TextView txt2 = (TextView) fileshare.findViewById(R.id.txt2);
        TextView txt3 = (TextView) fileshare.findViewById(R.id.txt3);
        TextView txt4 = (TextView) fileshare.findViewById(R.id.txt4);
        TextView txt5 = (TextView) fileshare.findViewById(R.id.txt5);
        TextView txt6 = (TextView) fileshare.findViewById(R.id.txt6);
        TextView txt7 = (TextView) fileshare.findViewById(R.id.txt7);
        TextView txt8 = (TextView) fileshare.findViewById(R.id.txt8);
        TextView txt10 = (TextView) fileshare.findViewById(R.id.txt10);

        file_name.setTypeface(Utility.getTypeFace());
        txt1.setTypeface(Utility.getTypeFace());
        txt2.setTypeface(Utility.getTypeFace());
        txt3.setTypeface(Utility.getTypeFace());
        txt4.setTypeface(Utility.getTypeFace());
        txt5.setTypeface(Utility.getTypeFace());
        txt6.setTypeface(Utility.getTypeFace());
        txt7.setTypeface(Utility.getTypeFace());
        txt8.setTypeface(Utility.getTypeFace());

        txt10.setTypeface(Utility.getTypeFace());

        LinearLayout lnfile_close = (LinearLayout) fileshare.findViewById(R.id.lnfile_close);


        lnfile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileshare.dismiss();
            }
        });

        fileshare.show();
    }

    private void FileDialog() {
        file = new Dialog(activity);
        file.requestWindowFeature(Window.FEATURE_NO_TITLE);
        file.getWindow().setWindowAnimations(R.style.DialogAnimation);
        file.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        file.setContentView(R.layout.filelistdialog);

        Window window = file.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        TextView tv1 = (TextView) file.findViewById(R.id.tv1);
        TextView tv2 = (TextView) file.findViewById(R.id.tv2);
        TextView tv3 = (TextView) file.findViewById(R.id.tv3);
        TextView tv4 = (TextView) file.findViewById(R.id.tv4);
        TextView tv5 = (TextView) file.findViewById(R.id.tv5);


        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());

        file.show();
    }

    private void showAlertDialog3() {
        dd = new Dialog(activity);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.addtofile);

        Window window = dd.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        //  LinearLayout lnback = (LinearLayout)dd.findViewById(R.id.lnback);
        TextView tv1 = (TextView) dd.findViewById(R.id.tv1);
        TextView tv2 = (TextView) dd.findViewById(R.id.tv2);
        TextView tv3 = (TextView) dd.findViewById(R.id.tv3);
        TextView tv4 = (TextView) dd.findViewById(R.id.tv4);
        TextView tv5 = (TextView) dd.findViewById(R.id.tv5);
        TextView tv6 = (TextView) dd.findViewById(R.id.tv6);
        TextView tv7 = (TextView) dd.findViewById(R.id.tv7);
        TextView tv8 = (TextView) dd.findViewById(R.id.tv8);

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        tv3.setTypeface(Utility.getTypeFace());
        tv4.setTypeface(Utility.getTypeFace());
        tv5.setTypeface(Utility.getTypeFace());
        tv6.setTypeface(Utility.getTypeFace());
        tv7.setTypeface(Utility.getTypeFace());
        tv8.setTypeface(Utility.getTypeFace());


        LinearLayout lnfile_close = (LinearLayout) dd.findViewById(R.id.lnfile_close);


        lnfile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dd.dismiss();
            }
        });

        dd.show();
    }
    private void DownloadFile(String file_path) {
        if (!file_path.equalsIgnoreCase("")) {
            DownFile(file_path);
        } else {
            Dialog dd = new Dialog(activity);

            dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setWindowAnimations(R.style.DialogAnimation);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_no_download);

            Window window = dd.getWindow();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
            dd.show();
        }
    }
    public static void DownFile(String s1) {
        new MyDrive.DownloadFileFromURL(s1).execute();
    }
}

