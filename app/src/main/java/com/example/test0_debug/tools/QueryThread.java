package com.example.test0_debug.tools;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.Objects;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QueryThread extends Thread {
    public final static int SUCCESS = 0;
    public final static int FAIL = 1;
    private final String url;
    private final Handler handler;
    private final Message message;

    public QueryThread(String url, Handler handler) {
        this.url = url;
        this.handler = handler;
        message = Message.obtain();
    }

    @Override
    public void run() {
        super.run();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            // publishProgress(++i);
            message.what = SUCCESS;
            message.obj = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            message.what = FAIL;
            message.obj = e.getMessage();
        } finally {
            handler.sendMessage(message);
        }
    }
}
