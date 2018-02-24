package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.ext.adarsh.Bean.BeanAttachmentData;
import com.ext.adarsh.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ExT-Emp-008 on 03-02-2018.
 */


public class AdapterAttachment extends RecyclerView.Adapter<AdapterAttachment.ViewHolder> {
    static Activity activity;
    List<BeanAttachmentData> list = new ArrayList<>();
    static ProgressDialog pDialog;

    public AdapterAttachment(Activity activity, List<BeanAttachmentData> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.messages_compose_attachment, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String filepath = list.get(position).FilePath;
        String filename=filepath.substring(filepath.lastIndexOf("/")+1);
        holder.tv_file_name.setText(filename);
        holder.iv_remove_file.setImageResource(R.drawable.download);

        holder.iv_remove_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownFile(list.get(position).FilePath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_remove_file;
        TextView tv_file_name;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_remove_file = (ImageView) itemView.findViewById(R.id.iv_remove_file);
            tv_file_name = (TextView) itemView.findViewById(R.id.tv_file_name);
        }
    }


    public static void DownFile(String s1) {
        new DownloadFileFromURL(s1).execute();
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
            Uri uri = Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
           // Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Download/");
            intent.setDataAndType(uri, "*/*");
            remoteViews.setTextViewText(R.id.download_custometitle, "Download completed.");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(activity).setTicker("Download completed.").setContent(remoteViews);
            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notifiBuilder.setSmallIcon(R.drawable.ic_download);
            remoteViews.setTextViewText(R.id.tv_timer, s);
            notifiBuilder.setAutoCancel(true);
            notifiBuilder.setSound(notificationSound);
            notifiBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notifiBuilder.build());
        }
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

    protected static Dialog dismissDialog() {
        pDialog.dismiss();
        return pDialog;
    }
}

