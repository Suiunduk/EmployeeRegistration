package com.example.employeeregistration.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.employeeregistration.R;
import com.example.employeeregistration.loginActivity.LoginActivity;


public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 3000;

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo = findViewById(R.id.logo);

        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.splashanimation);
        logo.setAnimation(loadAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        }, SPLASH_TIMEOUT);

    }
}
