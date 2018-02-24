package com.ext.adarsh.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ext.adarsh.Adapter.AdapterSubFileRecent;
import com.ext.adarsh.Bean.BeanFileArray;
import com.ext.adarsh.Bean.BeanFileFullPath;
import com.ext.adarsh.Bean.BeanParentToParentId;
import com.ext.adarsh.Bean.BeanSubFiles;
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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;
import static com.ext.adarsh.Utils.Utility.showMsg;
import static com.ext.adarsh.Utils.Utility.showSuccessAlertDialog;

public class SubFolderActivityRecent extends AppCompatActivity implements View.OnClickListener {
    private static ProgressDialog pd;

    LinearLayout ll_back;
    public static FloatingActionButton upload_float;
    static RecyclerView recyler_view_files;
    Dialog dd, addfile;
    static List<BeanSubFiles> filesList = new ArrayList<>();
    public static List<BeanFileArray> parent_filesList = new ArrayList<>();
    public static List<BeanFileFullPath> bean_file_path = new ArrayList<>();
    public static List<BeanParentToParentId> beanParentToParentIds = new ArrayList<>();
    static ArrayList<String> parent_folder_id = new ArrayList<>();

    private static Activity activity;
    private final int PICKFILE_RESULT_CODE = 1;
    public ArrayList<String> imagesEncodedList;
    public String imageEncoded;
    public String file_extension;
    public String filename;
    public List<ModelPostImageList> modelPostImageLists = new ArrayList<>();
    LinearLayout ll_cancel, ll_move, ll_add_folder;
    public static RelativeLayout rl_move_or_copy;
    public static String file_move_or_not = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_folder);
        ButterKnife.bind(this);
        activity = this;
        pd = Utility.getDialog(activity);


        Intent intent = getIntent();
        String file_id = intent.getExtras().getString("file_id");
        String parent_id = intent.getExtras().getString("parent_id");
        file_move_or_not = intent.getExtras().getString("file_move_or_not");

        rl_move_or_copy = (RelativeLayout) findViewById(R.id.rl_move_or_copy);
        ll_move = (LinearLayout) findViewById(R.id.ll_move);
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_add_folder = (LinearLayout) findViewById(R.id.ll_add_folder);

        recyler_view_files = (RecyclerView) findViewById(R.id.recyler_view_files);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyler_view_files.setLayoutManager(mLayoutManager);
        recyler_view_files.setItemAnimator(new DefaultItemAnimator());
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        upload_float = (FloatingActionButton) findViewById(R.id.upload_float);
        upload_float.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        parent_folder_id.add(parent_id);

        ll_add_folder.setOnClickListener(this);
        ll_move.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);

        if (file_move_or_not == null) {

        } else if (file_move_or_not.equalsIgnoreCase("yes")) {
            upload_float.setVisibility(View.GONE);
            rl_move_or_copy.setVisibility(View.VISIBLE);
        } else {
            upload_float.setVisibility(View.VISIBLE);
            rl_move_or_copy.setVisibility(View.GONE);
        }

        getSubFileData(file_id, parent_id);
    }

    public static void getSubFileData(final String fileid, final String parent_id) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.File_Sub_File_Select_All, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("res_sub_folder", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("ErrorMessage")) {
                            pd.dismiss();
                            Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                        if (object.has("File_Array")) {
                            JSONArray jsonArray = object.getJSONArray("File_Array");
                            if (jsonArray.length() != 0) {
                                parent_filesList.clear();
                                parent_filesList.addAll((Collection<? extends BeanFileArray>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanFileArray>>() {
                                }.getType()));
                                pd.dismiss();
                            } else {
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
                        if (object.has("File_Sub_File_Array")) {
                            JSONArray jsonArray = object.getJSONArray("File_Sub_File_Array");
                            if (jsonArray.length() != 0) {
                                filesList.clear();
                                filesList.addAll((Collection<? extends BeanSubFiles>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanSubFiles>>() {
                                }.getType()));
                                AdapterSubFileRecent adapter = new AdapterSubFileRecent(filesList, activity);
                                recyler_view_files.setAdapter(adapter);
                   /*             if (onback.equalsIgnoreCase("onback")) {
                                } else {
                                    parent_folder_id.add(filesList.get(0).parentId.toString());
                                }*/
                            } else {
                                recyler_view_files.setAdapter(null);
                                pd.dismiss();
                            }
                        }

                        if (object.has("ParentIdNew_Array")) {
                            JSONArray jsonArray = object.getJSONArray("ParentIdNew_Array");
                            if (jsonArray.length() != 0) {
                                beanParentToParentIds.clear();
                                beanParentToParentIds.addAll((Collection<? extends BeanParentToParentId>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanParentToParentId>>() {
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
                    map.put("FileId", fileid);
                    map.put("LoginId", Utility.getPeopleIdPreference());
                    map.put("Hashkey", Utility.getHashKeyPreference());
                    map.put("ParentId", parent_id);

                    Log.e("FileId", fileid);
                    Log.e("LoginId", Utility.getPeopleIdPreference());
                    Log.e("Hashkey", Utility.getHashKeyPreference());
                    Log.e("ParentId", parent_id);
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


    @Override
    public void onBackPressed() {
        if (beanParentToParentIds.size() > 0) {
            if (beanParentToParentIds.get(0).parentIdNew.equalsIgnoreCase("0")) {
                if (file_move_or_not.equalsIgnoreCase("yes")) {
                    Intent i = new Intent(activity, FileActivity.class);
                    i.putExtra("file_move_or_not", file_move_or_not);
                    i.putExtra("id", "2");
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(activity, FileActivity.class);
                    i.putExtra("file_move_or_not", "");
                    i.putExtra("id", "2");
                    startActivity(i);
                    finish();
                }

            } else {
                getSubFileData(beanParentToParentIds.get(0).parentIdNew, "0");
            }
        } else {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                if (beanParentToParentIds.size() > 0) {
                    if (beanParentToParentIds.get(0).parentIdNew.equalsIgnoreCase("0")) {
                        if (file_move_or_not.equalsIgnoreCase("yes")) {
                            Intent i = new Intent(activity, FileActivity.class);
                            i.putExtra("file_move_or_not", file_move_or_not);
                            i.putExtra("id", "2");
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(activity, FileActivity.class);
                            i.putExtra("file_move_or_not", "");
                            i.putExtra("id", "2");
                            startActivity(i);
                            finish();
                        }

                    } else {
                        getSubFileData(beanParentToParentIds.get(0).parentIdNew, "0");
                    }
                } else {

                }

    /*            if (beanParentToParentIds.get(0).parentIdNew == null) {
                    getSubFileData("0", "0");
                } else {
                    getSubFileData(beanParentToParentIds.get(0).parentIdNew, "0");
                }*/
                break;
            case R.id.upload_float:
                showAlertDialog3();
                break;
            case R.id.ll_move:
                String mainSelectedFileId = "";//parent_filesList
                String mainSelectFileName = "";//parent_filesList
                String mainSelectFilePath = "";//parent_filesList
                String mainSelectIsFileFlag = "";//parent_filesList

                mainSelectedFileId = parent_filesList.get(0).fileId;
                mainSelectFileName = parent_filesList.get(0).folderName;
                mainSelectFilePath = parent_filesList.get(0).mainFilePath;
                mainSelectIsFileFlag = "N";


                // String parentId = parent_filesList.get(0).fileId;
                MoveFileOrFolder(mainSelectedFileId);
                break;
            case R.id.ll_cancel:
                upload_float.setVisibility(View.VISIBLE);
                rl_move_or_copy.setVisibility(View.GONE);
                AppConstant.MoveFileId = "";
                AppConstant.MoveFileName = "";
                AppConstant.MoveFilePath = "";
                AppConstant.MoveIsFileFlag = "";
                file_move_or_not = "";
                break;
            case R.id.ll_add_folder:
                addfiledialog("move_time_add_folder");
                break;
        }
    }

    private void MoveFileOrFolder(final String mainSelectedFileId) {
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
                                        file_move_or_not = "";
                                        getSubFileData(mainSelectedFileId, mainSelectedFileId);
                                        //getFileData();
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
                    map.put("MainSelectFileId", mainSelectedFileId);
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

    private void showAlertDialog3() {
        dd = new Dialog(this);
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

        ln_newfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addfiledialog("normal_folder_add");
            }
        });

        file_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelPostImageLists.clear();
                selectFile();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                String fid = parent_filesList.get(0).fileId;
                String fpath = parent_filesList.get(0).mainFilePath;
                String file_full_path = bean_file_path.get(0).fullPath;
                String parentId = parent_filesList.get(0).fileId;
                for (int i = 0; i < modelPostImageLists.size(); i++) {
                    UploadFile(modelPostImageLists.get(i).getImage_name(), modelPostImageLists.get(i).getImagepath(),
                            modelPostImageLists.get(i).getImage_extension(), fid, fpath, file_full_path, parentId);
                }

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
                        temp.setBitmap(bitmap);
                        temp.setImagepath(readFileAsBase64String(path));
                        temp.setImage_name(filename);
                        temp.setImage_extension(file_extension);
                        modelPostImageLists.add(temp);
                        String fid = parent_filesList.get(0).fileId;
                        String fpath = parent_filesList.get(0).mainFilePath;
                        String file_full_path = bean_file_path.get(0).fullPath;
                        String parentId = parent_filesList.get(0).fileId;
                        for (int j = 0; j < modelPostImageLists.size(); j++) {
                            UploadFile(modelPostImageLists.get(j).getImage_name(), modelPostImageLists.get(j).getImagepath(),
                                    modelPostImageLists.get(j).getImage_extension(), fid, fpath, file_full_path, parentId);
                        }
                        Log.e("size arraylist", "++++++++++++" + modelPostImageLists.size());
                    }
                }
            }
        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

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
        input = this.getContentResolver().openInputStream(uri);
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

    private void UploadFile(final String file_name, final String file_path, final String file_extension, final String fid,
                            final String fpath, final String file_full_path, final String parentId) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Folder_Sub_File_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Folder_Sub_File_Add")) {
                            JSONArray array = object.getJSONArray("Folder_Sub_File_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        dd.dismiss();
                                        getSubFileData(fid, parentId);
                                        //     getSubFileData(fid, "1", "onback");
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
                    map.put("FolderId", fid);
                    map.put("SubFileName", file_name);
                    map.put("MainFolderFullPath", file_full_path);
                    map.put("MainFolderPath", fpath);
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

    private void addfiledialog(final String how_to_add_folder) {
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
                    String fid = parent_filesList.get(0).fileId;
                    String fpath = parent_filesList.get(0).mainFilePath;
                    String MainFolderFullPath = bean_file_path.get(0).fullPath;
                    addFolder(edt_groupname.getText().toString(), fid, fpath, MainFolderFullPath, how_to_add_folder);
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

    public void addFolder(final String folder_name, final String fid, final String fpath, final String MainFolderFullPath,
                          final String how_to_add_folder) {
        pd.show();
        if (checkConnectivity()) {
            StringRequest request = new StringRequest(Request.Method.POST, AppConstant.Folder_Sub_Folder_Add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(AppConstant.TAG, response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("Folder_Sub_Folder_Add")) {
                            JSONArray array = object.getJSONArray("Folder_Sub_Folder_Add");
                            if (array.length() != 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.optJSONObject(i).getString("Status").equalsIgnoreCase("Success")) {
                                        Toast.makeText(activity, "" + array.optJSONObject(i).getString("Status_Message"), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        addfile.dismiss();
                                        if (how_to_add_folder.equalsIgnoreCase("normal_folder_add")) {
                                            dd.dismiss();
                                        }
                                        //   getFileData();
                                        getSubFileData(fid, "1");
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
                    map.put("FolderId", fid);
                    map.put("SubFolderName", folder_name);
                    map.put("MainFolderPath", fpath);
                    map.put("MainFolderFullPath", MainFolderFullPath);
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
