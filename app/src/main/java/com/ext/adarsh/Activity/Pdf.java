package com.ext.adarsh.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ext.adarsh.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ext.adarsh.Utils.Utility.checkConnectivity;


public class Pdf extends AppCompatActivity {

    @BindView(R.id.progress)
    LinearLayout progress;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.web_view)
    WebView wvPage;

    String url;

    private Activity act;

    public Pdf() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        ButterKnife.bind(this);
        act = this;

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        url = getIntent().getStringExtra("pdf");

        wvPage.getSettings().setJavaScriptEnabled(true);
        wvPage.getSettings().setLoadWithOverviewMode(true);
        wvPage.getSettings().setSupportZoom(true);
        wvPage.getSettings().setTextZoom(14);
        wvPage.getSettings().supportZoom();
        wvPage.getSettings().setBuiltInZoomControls(true);
        wvPage.getSettings().setUseWideViewPort(true);
        wvPage.getSettings().setDomStorageEnabled(true);

        if (checkConnectivity()) {
            wvPage.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(act, description, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    wvPage.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progress.setVisibility(View.GONE);
                    wvPage.setVisibility(View.VISIBLE);
                }
            });
            if (!url.equalsIgnoreCase("")) {
                wvPage.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
                // wvPage.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
                Log.e("onCreate: ", "++++++++++++++++++++" + "http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
                // wvPage.loadUrl(url);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(act);
            builder.setTitle("Warning!").
                    setMessage("Internet is not connected, please connect and restart.").
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).
                    show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //     startActivity(new Intent(Pdf.this, DB_Act.class));
        //    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_rigth_out);
        finish();
    }
}
