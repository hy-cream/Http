package com.example.http2_test;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.security.PublicKey;

public class MainActivity extends AppCompatActivity {

    private Handler handler=new Handler();
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView= (TextView) findViewById(R.id.tv);

        /*
        * 网络请求是一个典型的耗时操作，一定要异步*/

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //从网络获取数据
//                final String response=NetUtils.get("http://www.baidu.com");
//
//                //向Handle发送处理操作
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //在UI线程 更新UI
//                        mTextView.setText(response);
//                    }
//                });
//            }
//        }).start();

        //更好的写法，调用它的回调
        /*这个接口就是用来练习内外类的数据，不需要把子线程的运行代码写在MainActivity中
        在正常的写法中自定义一个接口，并且在接口中返回这个数据*/

        /*
        * 学到这里的一些缺点：
        * 每次都new Thread，new Handler消耗过大
          没有异常处理机制
          没有缓存机制
          没有完善的API(请求头,参数,编码,拦截器等)与调试模式
          没有Https */

        AsynNetUtils.get("http://www.baidu.com", new AsynNetUtils.Callback() {
            @Override
            public void onResponse(String response) {
                mTextView.setText(response);
            }
        });
    }
}
