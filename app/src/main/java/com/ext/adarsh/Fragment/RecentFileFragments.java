package com.ext.adarsh.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
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
import com.ext.adarsh.Activity.FileActivity;
import com.ext.adarsh.Activity.SubFolderActivity;
import com.ext.adarsh.Adapter.AdapterRecentDrive;
import com.ext.adarsh.Bean.BeanFileFullPath;
import com.ext.adarsh.Bean.BeanFiles;
import com.ext.adarsh.Bean.ModelPostImageList;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Infranet;
import com.ext.adarsh.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class RecentFileFragments extends Fragment implements View.OnClickListener {

    public static RecyclerView recylerfiles;
    static Activity activity;
    static List<BeanFiles> filesList = new ArrayList<>();
    static List<BeanFileFullPath> bean_file_path = new ArrayList<>();
    public static FloatingActionButton upload_float;
    Dialog dd, addfile;
    public static ProgressDialog pd;
    static ProgressDialog pDialog;
    private final int PICKFILE_RESULT_CODE = 1;
    public ArrayList<String> imagesEncodedList;
    public String imageEncoded;
    public String file_extension;
    public String filename;
    public List<ModelPostImageList> modelPostImageLists = new ArrayList<>();
    LinearLayout ll_cancel, ll_move, ll_add_folder;
    public static RelativeLayout rl_move_or_copy;

    EditText search_people;
    public static AdapterRecentDrive adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_file, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        activity = getActivity();
        pd = Utility.getDialog(activity);

/*        Intent intent = getIntent();
        file_move_or_not = intent.getExtras().getString("file_move_or_not");*/

        search_people = (EditText) view.findViewById(R.id.search_people);

        upload_float = (FloatingActionButton) view.findViewById(R.id.upload_float);
        recylerfiles = (RecyclerView) view.findViewById(R.id.recylerfiles);
        rl_move_or_copy = (RelativeLayout) view.findViewById(R.id.rl_move_or_copy);
        ll_move = (LinearLayout) view.findViewById(R.id.ll_move);
        ll_cancel = (LinearLayout) view.findViewById(R.id.ll_cancel);
        ll_add_folder = (LinearLayout) view.findViewById(R.id.ll_add_folder);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recylerfiles.setLayoutManager(mLayoutManager);
        recylerfiles.setItemAnimator(new DefaultItemAnimator());
        if (FileActivity.file_move_or_not == null) {

        } else if (FileActivity.file_move_or_not.equalsIgnoreCase("yes")) {
            upload_float.setVisibility(View.GONE);
            rl_move_or_copy.setVisibility(View.VISIBLE);
        } else {
            upload_float.setVisibility(View.VISIBLE);
            rl_move_or_copy.setVisibility(View.GONE);
        }
        getFileData();

        search_people.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ll_add_folder.setOnClickListener(this);
        ll_move.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        upload_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog3();
            }
        });

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
        TextView file_upload = (TextView) dd.findViewById(R.id.file_upload);
        TextView tv4 = (TextView) dd.findViewById(R.id.tv4);
        TextView tv5 = (TextView) dd.findViewById(R.id.tv5);
        TextView tv6 = (TextView) dd.findViewById(R.id.tv6);
        TextView tv7 = (TextView) dd.findViewById(R.id.tv7);
        TextView tv8 = (TextView) dd.findViewById(R.id.tv8);

        LinearLayout ln_newfolder = (LinearLayout) dd.findViewById(R.id.ln_newfolder);
        LinearLayout ll_upload_file = (LinearLayout) dd.findViewById(R.id.ll_upload_file);
        ll_upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        ln_newfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addfiledialog();
            }
        });

        file_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filePickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                filePickerIntent.setType("*/*");
                filePickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(filePickerIntent, PICKFILE_RESULT_CODE);

                // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //   intent.setType("*/*");
                //startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });

        tv1.setTypeface(Utility.getTypeFace());
        tv2.setTypeface(Utility.getTypeFace());
        file_upload.setTypeface(Utility.getTypeFace());
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

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }


    public static void DownFile(String s1) {
        new DownloadFileFromURL(s1).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_move:
                MoveFileOrFolder();
                break;
            case R.id.ll_cancel:
                upload_float.setVisibility(View.VISIBLE);
                rl_move_or_copy.setVisibility(View.GONE);
                AppConstant.MoveFileId = "";
                AppConstant.MoveFileName = "";
                AppConstant.MoveFilePath = "";
                AppConstant.MoveIsFileFlag = "";
                SubFolderActivity.file_move_or_not = "";

                break;
            case R.id.ll_add_folder:
                addfiledialog();
                break;
        }
    }

    private void MoveFileOrFolder() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Move, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("File_Move")) {
                            JSONArray array = object.getJSONArray("File_Move");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        upload_float.setVisibility(View.VISIBLE);
                                        rl_move_or_copy.setVisibility(View.GONE);
                                        AppConstant.MoveFileId = "";
                                        AppConstant.MoveFileName = "";
                                        AppConstant.MoveFilePath = "";
                                        AppConstant.MoveIsFileFlag = "";
                                        SubFolderActivity.file_move_or_not = "";
                                        getFileData();
                                        MyDrive.getFileData();

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
                        } else {
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
                    map.put("MainSelectFileId", "0");
                    map.put("MainSelectFileName", "");
                    map.put("MainSelectFilePath", "");
                    map.put("MainSelectIsFileFlag", "");
                    map.put("MoveFileId", AppConstant.MoveFileId);
                    map.put("MoveFileName", AppConstant.MoveFileName);
                    map.put("MoveFilePath", AppConstant.MoveFilePath);
                    map.put("MoveIsFileFlag", AppConstant.MoveIsFileFlag);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("EmployeeCode", Utility.getEmployeeCode());
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

    public static class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String uri;

        public DownloadFileFromURL(String uri) {
            this.uri = uri;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createDialog();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(uri);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String[] s1 = uri.split("/");
                for (int i = 0; i < s1.length; i++) {
                    Log.e("Uri", s1[i]);
                }
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Download/" + s1[5]);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog();

            String[] s1 = uri.split("/");
            RemoteViews remoteViews = new RemoteViews("com.ext.adarsh", R.layout.download_notification);

            Date date = new Date();
            String s = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Download/");
            intent.setDataAndType(uri, "*/*");
            remoteViews.setTextViewText(R.id.download_custometitle, "Download completed.");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(activity).setTicker("Download completed.").setContent(remoteViews);
            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notifiBuilder.setSmallIcon(R.drawable.ic_download);
            remoteViews.setTextViewText(R.id.tv_timer, s);
            //    remoteViews.setTextViewText(R.id.customtext, s1[5]);
            notifiBuilder.setAutoCancel(true);
            notifiBuilder.setSound(notificationSound);
            notifiBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notifiBuilder.build());
        }
    }

    protected static Dialog dismissDialog() {
        pDialog.dismiss();
        return pDialog;
    }

    protected static Dialog createDialog() {
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
        return pDialog;
    }

    public static void getFileData() {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Select_All, new Response.Listener<String>() {
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
                        if (object.has("File_Array")) {
                            JSONArray jsonArray = object.getJSONArray("File_Array");
                            if (jsonArray.length() != 0) {
                                filesList.clear();
                                filesList.addAll((Collection<? extends BeanFiles>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFiles>>() {
                                }.getType()));
                                adapter = new AdapterRecentDrive(filesList, activity);
                                recylerfiles.setAdapter(adapter);
                                pd.dismiss();
                            } else {
                                recylerfiles.setAdapter(null);
                                pd.dismiss();
                            }
                        }
                        if (object.has("FullPath_Array")) {
                            JSONArray jsonArray = object.getJSONArray("FullPath_Array");
                            if (jsonArray.length() != 0) {
                                bean_file_path.clear();
                                bean_file_path.addAll((Collection<? extends BeanFileFullPath>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFileFullPath>>() {
                                }.getType()));
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
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("ParentId", "0");
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

    private void addfiledialog() {
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

        tv_rename_title.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);

        final TextView edt_groupname = (EditText) addfile.findViewById(R.id.edt_groupname);
        Button btn_save = (Button) addfile.findViewById(R.id.btn_save);
        ImageView iv_close = (ImageView) addfile.findViewById(R.id.iv_close);


        title.setTypeface(Utility.getTypeFace());
        edt_groupname.setTypeface(Utility.getTypeFace());
        btn_save.setTypeface(Utility.getTypeFace());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_groupname.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please Enter File Name", Toast.LENGTH_SHORT).show();
                } else {
                    addFolder(edt_groupname.getText().toString());
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

    public void addFolder(final String name) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Folder_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Folder_Add")) {
                            JSONArray array = object.getJSONArray("Folder_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        addfile.dismiss();
                                        dd.dismiss();
                                        getFileData();
                                        MyDrive.getFileData();
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
                    map.put("FolderName", name);
                    map.put("FilePrivacyFlag", "P");
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("EmployeeCode", Utility.getEmployeeCode());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && null != data) {
            modelPostImageLists.clear();

            if (data.getData() != null) {
                Uri FilePath = data.getData();
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

                //iv_photo_video_selected.setImageBitmap(bm);
                Log.e("onActivityResult: ", "++++++++++++++" + String.valueOf(FilePath));
                String path = null;
                try {
                    path = getFilePath(activity, FilePath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                String file = path.substring(path.lastIndexOf("/") + 1);

                file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                filename = file.substring(0, file.lastIndexOf('.'));
                Log.e("onActivityResult: ", "path++++++++++++" + path);
                Log.e("log", "64+++++++++++++++" + readFileAsBase64String(path));
                ModelPostImageList temp = new ModelPostImageList();
                temp.setBitmap(bm);
                temp.setImagepath(readFileAsBase64String(path));
                temp.setImage_name(filename);
                temp.setImage_extension(file_extension);
                modelPostImageLists.add(temp);

                for (int i = 0; i < modelPostImageLists.size(); i++) {
                    //UploadFile(final String file_name, final String file_path, final String file_extension)
                    UploadFile(filename, readFileAsBase64String(path), file_extension);
                }

      /*          AdapterFeedPostImage adapterPollsOption = new AdapterFeedPostImage(activity, modelPostImageLists);
                rv_selected_image.setAdapter(adapterPollsOption);
                */
                // tv_file_name.setText(String.valueOf(arrayList_file_base64.size()) + " file attached");

            } else {
                if (data != null) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Log.i("Path:", item.toString());

                        Uri uri = item.getUri();
                        Bitmap bitmap = null;
                        try {
                            bitmap = getThumbnail(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.e("uri", "" + uri);
                        String path = null;
                        try {
                            path = getFilePath(activity, uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        String file = path.substring(path.lastIndexOf("/") + 1);
                        file_extension = "." + file.substring(file.lastIndexOf(".") + 1);
                        filename = file.substring(0, file.lastIndexOf('.'));
                        Log.e("onActivityResult: ", "path++++++++++++" + path);
                        Log.e("log", "64+++++++++++++++" + readFileAsBase64String(path));

                        ModelPostImageList temp = new ModelPostImageList();
                        temp.setImagepath(readFileAsBase64String(path));
                        temp.setImage_name(filename);
                        temp.setImage_extension(file_extension);
                        modelPostImageLists.add(temp);
                        for (int j = 0; j < modelPostImageLists.size(); j++) {
                            //UploadFile(final String file_name, final String file_path, final String file_extension)
                            UploadFile(modelPostImageLists.get(j).getImage_name(), modelPostImageLists.get(j).getImagepath(),
                                    modelPostImageLists.get(j).getImage_extension());
                        }

                        //  tv_file_name.setText(String.valueOf(arrayList_file_base64.size()) + " file attached");
                        Log.e("size arraylist", "++++++++++++" + modelPostImageLists.size());
                    }
                }
            }
        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = activity.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 512) ? (originalSize / 320) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = activity.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String readFileAsBase64String(String path) {
        try {
            InputStream is = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Base64OutputStream b64os = new Base64OutputStream(baos, Base64.DEFAULT);
            byte[] buffer = new byte[8192];
            int bytesRead;
            try {
                while ((bytesRead = is.read(buffer)) > -1) {
                    b64os.write(buffer, 0, bytesRead);
                }
                return baos.toString();
            } catch (IOException e) {
                Log.e("can not read", "Cannot read file " + path, e);
                // Or throw if you prefer
                return "";
            } finally {
                closeQuietly(is);
                closeQuietly(b64os); // This also closes baos
            }
        } catch (FileNotFoundException e) {
            Log.e("file not found", "File not found " + path, e);
            // Or throw if you prefer
            return "";
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
        }
    }

    private void UploadFile(final String file_name, final String file_path, final String file_extension) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("File_Add")) {
                            JSONArray array = object.getJSONArray("File_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        dd.dismiss();
                                        getFileData();
                                        MyDrive.getFileData();
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
                    map.put("FileName", file_name);
                    map.put("FilePath", file_path);
                    map.put("FileExtension", file_extension);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("EmployeeCode", Utility.getEmployeeCode());

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

/*
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ext.adarsh.Adapter.AdapterAnnoucement;
import com.ext.adarsh.R;

import butterknife.ButterKnife;

public class RecentFileFragments extends Fragment {

    AdapterAnnoucement mAdapter;

   public static RecyclerView recylerannouncement;

    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.announcement, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        activity = getActivity();

        recylerannouncement = (RecyclerView)view.findViewById(R.id.recylerannouncement);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recylerannouncement.setLayoutManager(mLayoutManager);
        recylerannouncement.setItemAnimator(new DefaultItemAnimator());
        recylerannouncement.setAdapter(mAdapter);
    }


}*/

