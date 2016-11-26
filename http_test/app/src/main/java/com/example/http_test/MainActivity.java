package com.example.http_test;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private WebView mweb;

    private ImageView mimg;
    private Handler mhandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mweb= (WebView) findViewById(R.id.web);
        mimg= (ImageView) findViewById(R.id.image);
        new HandlerThread("http://f.hiphotos.baidu.com/baike/pic/item/30adcbef76094b3651e759c6a2cc7cd98d109da0.jpg",mimg,mhandler).start();
    }
}
