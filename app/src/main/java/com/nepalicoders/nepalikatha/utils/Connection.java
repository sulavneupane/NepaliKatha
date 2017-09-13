package com.nepalicoders.nepalikatha.utils;

import android.net.Uri;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sabin on 12/7/2015.
 */
public class Connection {

    public static final String GET = "get";
    public static final String POST = "post";
    static OkHttpClient client;

    private static OkHttpClient getConnection() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    private static void run(URL url, RequestBody body, String type, Callback callback) throws IOException {
        OkHttpClient client = getConnection();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (type.equals(GET)) {
            builder.get();
        } else {
            if (body != null)
                builder.post(body);
        }
        Request request = builder.build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        // Response response = client.newCall(request).execute();
        //  String result = response.body().string();
        //  response.body().close();
    }

    public static void fetchJson(String url, Map params, String requestType, Callback callback) throws IOException {
        String result = null;
        switch (requestType) {
            case GET:
                run(buildUrlForGet(url, params), null, GET, callback);
                break;

            case POST:
                run(new URL(url), getRequestBody(params), POST, callback);
                break;
        }
    }


    private static RequestBody getRequestBody(Map params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null) {
            for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                builder.add(entry.getKey(), entry.getValue());
            }
            return builder.build();
        }
        return null;
    }

    private static URL buildUrlForGet(String url, Map params) throws MalformedURLException {
        url += "?";
        Uri.Builder builder = Uri.parse(url).buildUpon();

        if (params != null) {
            for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return new URL(builder.build().toString());
    }

}
