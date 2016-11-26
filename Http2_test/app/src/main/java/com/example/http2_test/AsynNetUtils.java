package com.example.http2_test;

import android.os.Handler;

/**
 * Created by 胡钰 on 2016/9/6.
 */

//异步一般搜会伴随回调
public class AsynNetUtils {

    public interface Callback{
        void onResponse(String response);
    }

    public static void get(final String url, final Callback callback){
        final Handler handler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {

                final String response=NetUtils.get(url);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        }).start();
    }
    public static void post(final String url,final String content,final Callback callback){
        final Handler handler=new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response=NetUtils.post(url,content);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        }).start();
    }
}
