package com.ext.adarsh.Utils;

import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceHandler {

    public final static String GET = "get";
    public final static String POST = "post";

    public ServiceHandler() {
    }

    public static String makeServiceCall(String webUrl, String reqParam, String method) {

        String jsonString = "";

        try {

            System.setProperty("http.keepAlive", "false");

            URL url = new URL(webUrl);

            HttpURLConnection httpURLConnection = null;
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setFixedLengthStreamingMode(reqParam.getBytes().length);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                httpURLConnection.setRequestProperty("Connection", "close");
            }

            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoOutput(true);

            PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
            out.print(reqParam);
            out.close();

            InputStream is = httpURLConnection.getInputStream();
            if (is != null) {
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);

                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                jsonString = stringBuffer.toString();

                if (is != null) {
                    is.close();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}