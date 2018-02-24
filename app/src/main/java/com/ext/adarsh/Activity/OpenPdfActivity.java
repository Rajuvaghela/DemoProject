package com.ext.adarsh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;
import com.github.barteksc.pdfviewer.PDFView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenPdfActivity extends AppCompatActivity {
    PDFView pdfView;
    @BindView(R.id.tv_header)
    TextView tv_header;

    @BindView(R.id.lnback)
    LinearLayout lnback;
    private String file_path;
    private String file_name;
    Activity activity;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);
        activity = this;
        ButterKnife.bind(this);
        pd = Utility.getDialog(activity);

        Bundle bundle = getIntent().getExtras();
        file_name = bundle.getString("file_name");
        file_path = bundle.getString("file_path");


        lnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_header.setTypeface(Utility.getTypeFaceTab());
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromUri(Uri.parse(file_path))
                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true)
                .enableDoubletap(true)
                .swipeVertical(false)
                .defaultPage(1)
                .showMinimap(false)
                .enableAnnotationRendering(false)
                .password(null)
                .showPageWithAnimation(true)
                .load();


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
