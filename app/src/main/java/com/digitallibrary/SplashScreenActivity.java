package com.digitallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    ImageView im_logo;
    TextView tv_theme, tv_slogan, tv_copyright;
    Animation topAnim, bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.splash_top);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splash_bottom);

        im_logo = findViewById(R.id.tv_logo);
        tv_slogan = findViewById(R.id.tv_slogan);
        tv_copyright = findViewById(R.id.tv_copyright);
        tv_theme = findViewById(R.id.tv_theme);

        im_logo.setAnimation(topAnim);
        tv_theme.setAnimation(bottomAnim);
        tv_slogan.setAnimation(bottomAnim);
        tv_copyright.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}