package com.example.employeeregistration;

import android.app.Application;

public class GlobalClass extends Application {
    public static final String BASE_URL = "http://192.168.88.149:90/";
    public static String getBaseUrl() {
        return BASE_URL;
    }

    private static GlobalClass singleton;

    @Override
    public void onCreate(){
        super.onCreate();
        singleton = this;
    }

    public static GlobalClass getInstance(){
        return singleton;
    }
}
