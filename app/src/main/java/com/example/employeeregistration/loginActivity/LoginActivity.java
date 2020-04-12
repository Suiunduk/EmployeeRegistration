package com.example.employeeregistration.loginActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.employeeregistration.R;
import com.example.employeeregistration.databinding.ActivityLoginBinding;
import com.example.employeeregistration.registrationActivity.RegistrationActivity;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding mainBinding;
    private static String PHONE_NUMBER = "708";
    private static String PASSWORD = "pass";
    private String newPhoneNumber;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }
    /*
    ------------------------------------------------------------------------------------------------
    Here begins the methods for LOGIN button on Main activity
    */
    public void login(View view){
        EditText phoneNumber =mainBinding.phoneNumField;
        EditText password = mainBinding.passwordField;

        newPhoneNumber = phoneNumber.getText().toString();
        newPassword = password.getText().toString();

        if(newPhoneNumber.equals(PHONE_NUMBER) && newPassword.equals(PASSWORD)) {
            animateButtonWidth();
            fadeOutTextAndSetProgressDialog();
            nextAction();

        } else {
            Toast.makeText(getApplicationContext(),"Phone number or password is incorrect",Toast.LENGTH_LONG).show();
        }
    }


    private void animateButtonWidth(){
        ValueAnimator animator = ValueAnimator.ofInt(mainBinding.loginBtn.getMeasuredWidth(), getFinalWith());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mainBinding.loginBtn.getLayoutParams();
                layoutParams.width = value;
                mainBinding.loginBtn.requestLayout();
            }
        });

        animator.setDuration(250);
        animator.start();
    }

    private void fadeOutTextAndSetProgressDialog(){
        mainBinding.loginText.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showProgressDialog();
            }
        }).start();
    }

    private void showProgressDialog(){
        mainBinding.progressCircular.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF0E3979"), PorterDuff.Mode.SRC_IN);
        mainBinding.progressCircular.setVisibility(View.VISIBLE);
    }

    private void nextAction(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealButton();
                fadeOutProgressDialog();
                delayedStartNext();

            }
        }, 2000);
    }

    private void revealButton(){
        mainBinding.loginBtn.setElevation(4f);
        mainBinding.revealView.setVisibility(View.VISIBLE);

        int x = mainBinding.revealView.getWidth();
        int y = mainBinding.revealView.getHeight();

        int startX = (int) (getFinalWith() / 2 + mainBinding.loginBtn.getX());
        int startY = (int) (getFinalWith() / 2 + mainBinding.loginBtn.getY());

        float radius = Math.max(x, y) + 1.2f;

        Animator reveal = ViewAnimationUtils.createCircularReveal(mainBinding.revealView, startX, startY, getFinalWith(), radius);
        reveal.setDuration(350);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                finish();
            }
        });

        reveal.start();
    }

    private void fadeOutProgressDialog(){
        mainBinding.progressCircular.animate().alpha(0f).setDuration(280).start();
    }

    private void delayedStartNext(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);

                finish();
            }
        }, 100);
    }


    private int getFinalWith(){
        return (int) getResources().getDimension(R.dimen.get_width);
    }
    /*
    And here is the end of LOGIN button methods
    ------------------------------------------------------------------------------------------------
     */
}

