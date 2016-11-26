package com.example.http_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 胡钰 on 2016/7/21.
 */
//像加载网页这种东西一般比较耗时，所以放在子线程中执行
public class HandlerThread extends Thread {

    private WebView mweb;

    private Handler mhandler;//handler机制来更新

    private String url;
    private ImageView mimag;

    public HandlerThread(String url,WebView mweb,Handler mhandler){
        this.url=url;
        this.mweb=mweb;
        this.mhandler=mhandler;
    }
    public HandlerThread(String url,ImageView mimag,Handler mhandler){
        this.url=url;
        this.mimag=mimag;
        this.mhandler=mhandler;
    }

    @Override
    public void run() {
        try {
            //这个异常是解析url能否正常解析
            URL httpurl=new URL(url);

            //对他进行http的访问,通过httpurl.openConnection的方法打开一个httpurlconnection的对象
            HttpURLConnection con= (HttpURLConnection) httpurl.openConnection();

            //设置一个请求超时等待的时间，再设置他访问网络的方式
            con.setReadTimeout(5000);
            con.setRequestMethod("GET");

          /*  //创建StringBuffer作为缓冲，可变的字符串
            final StringBuffer stringBuffer=new StringBuffer();

            //inputStreamReader输入流转化为字节流,con.getInputStream()拿到一个输入流，这个是通过网址拿到页面信息的一个流
            BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));

            Log.i("reader------------","reader");
            //拿到他返回的数据
            String str;
            while((str=reader.readLine())!=null){
                //向StringBuffer里面添加数据
                stringBuffer.append(str);
            }
            Log.i("str------------","str");
            //更新
            mhandler.post(new Runnable() {
                @Override
                public void run() {
            mweb.loadDataWithBaseURL(url,stringBuffer.toString(),"text/html","utf-8",null);

                }
            });

*/

            /*
            * inputstream输入流是用来读取文件到程序中
            * fileoutputstream输出流是用来从程序中读出数据，写入文件目录中
            * 他创建的两种方法：
            * FileInputStream inOne = new FileInputStream("hello.test");
              * (2)File f = new File("hello.test");
            * FileInputStream inTwo = new FileInputStream(f);
            *
             */
            //让他可以有输入流的通道
            con.setDoInput(true);
            //创建一个输入流
            InputStream in=con.getInputStream();

            //在SD卡中新建一个文件用来存放下载的图片
            FileOutputStream out=null;
            File downloadFile=null;
            //以现在的时间作为文件名，并把它转化为字符串
            String filename=String.valueOf(System.currentTimeMillis());

            //如果SD卡挂载的情况下
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                //获得SD卡的一个目录
                File parent=Environment.getExternalStorageDirectory();
                //创建下载目录
                downloadFile=new File(parent,filename);
                //向下载目录里写入数据
                out= new FileOutputStream(downloadFile);
            }

            //先读数据流，在往里面写数据

            //创建一个缓冲区，以字节的形式进行
            byte[] b=new byte[2*1024];
            int len;
            //如果读取流存在SD卡存在
            if(out!=null){
                //len不等于-1的情况下就默认他有数据
                //(len = in.read(b)读取内容到字节数组中
                while((len = in.read(b))!=-1){
                    //往文件里面写数据
                    out.write(b,0,len);

                }
            }

            //mhandler还更新UI
            //图片要用bitmap用文件路径名来读取文件数据
            final Bitmap bitmap=BitmapFactory.decodeFile(downloadFile.getAbsolutePath());
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    mimag.setImageBitmap(bitmap);

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
