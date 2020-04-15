package com.example.employeeregistration;


import android.content.Context;
import android.util.Log;

import com.example.employeeregistration.GlobalClass;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class WebReq {
    public static AsyncHttpClient client;

    static{
        //create object of loopj client
        //443 will save you from ssl exception
        client = new AsyncHttpClient(true,80,443);
    }

    public static void get(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        client.get(context, getAbsoluteUrl(url), params, responseHandler);
    }


    //concatenation of base url and file name
    private static String getAbsoluteUrl(String relativeUrl) {
        Log.d("response URL: ", GlobalClass.getInstance().BASE_URL + relativeUrl+" ");
        return GlobalClass.getInstance().BASE_URL + relativeUrl;
    }

    public static void post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        client.post(context, getAbsoluteUrl(url), params, responseHandler);
    }
}
