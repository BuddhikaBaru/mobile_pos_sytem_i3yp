package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashingActivity extends AppCompatActivity {

    //animation variables
    Animation topAnim, botAnim;
    ImageView splashImg;
    TextView appName;

    private static int waitTime=3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashing);
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        splashImg=findViewById(R.id.splashImg);
        appName=findViewById(R.id.appNameText);
        splashImg.setAnimation(topAnim);
        appName.setAnimation(botAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }, waitTime);






    }
}