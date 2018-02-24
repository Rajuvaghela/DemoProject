package com.ext.adarsh.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ext.adarsh.R;
import com.ext.adarsh.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ExT-Emp-005 on 26-08-2017.
 */

public class SplashScreen  extends Activity {

    @BindView(R.id.ProgressBar)
    ProgressBar progressBar;

    @BindView(R.id.splash_img)
    ImageView splash_img;

    private static int SPLASH_TIME_OUT = 3500;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        handler = new Handler();

        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        splash_img.startAnimation(animationFadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utility.getPeopleIdPreference().toString().equalsIgnoreCase("")){
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

    }
}
