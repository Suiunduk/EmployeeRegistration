package com.example.employeeregistration.loginActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.employeeregistration.MainActivity;
import com.example.employeeregistration.R;
import com.example.employeeregistration.WebReq;
import com.example.employeeregistration.databinding.ActivityLoginBinding;
import com.example.employeeregistration.registrationActivity.RegistrationActivity;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class LoginActivity extends MainActivity {
    private ActivityLoginBinding mainBinding;
    private String tel_number;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        init();
    }

    public void init() {
        context = getApplicationContext();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
    }


    /*
    ------------------------------------------------------------------------------------------------
    Here begins the methods for LOGIN button on Main activity
    */
    public void login(View view){
        EditText numField =mainBinding.phoneNumField;
        EditText passwordField = mainBinding.passwordField;

        tel_number = numField.getText().toString();
        password = passwordField.getText().toString();
/*
        if(tel_number.equals(PHONE_NUMBER) && this.password.equals(PASSWORD)) {
            animateButtonWidth();
            fadeOutTextAndSetProgressDialog();
            nextAction();

        } else {
            Toast.makeText(getApplicationContext(),"Phone number or password is incorrect",Toast.LENGTH_LONG).show();
        }
*/

        if (tel_number.length()==0){
            Toast.makeText(getApplicationContext(),"Invalid number Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordField.length()<5){
            Toast.makeText(getApplicationContext(),"Minimum password length should be 5 characters.",Toast.LENGTH_SHORT).show();
            return;
        }

        //all inputs are validated now perform login request
        RequestParams params = new RequestParams();
        params.add("type","login");
        params.add("tel_number",tel_number);
        params.add("password",password);

        WebReq.post(context, "api.php", params, new LoginActivity.ResponseHandler());
    }

    private class ResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response ", response.toString() + " ");
            try {
                if (response.getBoolean("error")) {
                    // failed to login
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    // successfully logged in
                    JSONObject user = response.getJSONObject("user");
                    //save login values
                    sharedPrefEditor.putBoolean("login", true);
                    sharedPrefEditor.putString("id", user.getString("id"));
                    sharedPrefEditor.putString("name", user.getString("name"));
                    sharedPrefEditor.putString("tel_number", user.getString("tel_number"));
                    sharedPrefEditor.apply();
                    sharedPrefEditor.commit();

                    //Move to home activity
                    animateButtonWidth();
                    fadeOutTextAndSetProgressDialog();
                    nextAction();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

