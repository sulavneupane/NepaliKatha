package com.nepalicoders.nepalikatha.utils;

import android.app.Activity;
import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Sabin on 12/7/2015.
 */
public class BackgroundThread implements Callback {

    com.nepalicoders.nepalikatha.interfaces.Callback listener;
    Map<String, String> params;
    String url;
    String requestType;
    Activity activity;

    public void setBackgroundListener(Context context, com.nepalicoders.nepalikatha.interfaces.Callback listener) {
        this.activity = (Activity) context;
        this.listener = listener;
    }


    public BackgroundThread(String url, Map params, String requestType) {
        this.url = url;
        this.params = params;
        this.requestType = requestType;
    }


    public void execute() {
        try {
            Connection.fetchJson(url, params, requestType, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(final Request request, final IOException e) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onFailure(request, e);
            }
        });
    }

    @Override
    public void onResponse(final Response response) throws IOException {
        final String result = response.body().string();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onResponse(response, result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
