package com.example.http2_test;

import android.accounts.NetworkErrorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by 胡钰 on 2016/9/5.
 */
public class NetUtils {

    public static String post(String url, String content){
        HttpURLConnection conn=null;

        try {
            //创建一个url
            URL mURL=new URL(url);

            //调用URL的openConnection()方法，获取HttpURLConnection对象
            conn=(HttpURLConnection) mURL.openConnection();

            //设置请求方式为post
            conn.setRequestMethod("POST");
            //设置读取超时未5s
            conn.setReadTimeout(5000);
            //设置联网时间不超过10秒
            conn.setConnectTimeout(10000);
            //设置此方法允许向服务器输出内容
            conn.setDoInput(true);

            //post的请求参数
            String data=content;
            //获得一个输出流，向服务器写数据，默认情况下，系统不允许向服务器输出内容
            OutputStream out=conn.getOutputStream();//获得一个输出流，向服务器写数据
            /*
            * 这里要用数据描述语言将对象序列化成文本，再用http传递，接收端会将文本还原成结构数据*/
            out.write(data.getBytes());
            out.flush();
            out.close();

            //响应
            int responseCode=conn.getResponseCode();//调用此方法就不必用conn.connect()方法
            if (responseCode == 200){
                InputStream is=conn.getInputStream();
                String respose=getStringFromInputStream(is);

                return respose;
            }else {
                throw new NetworkErrorException("response status is"+responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    //get主要是到服务器里拿信息

    public static String get(String url){
        HttpURLConnection conn=null;

        try {
            //利用String url 构建URL对象
            URL mURL=new URL(url);
            conn=(HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);

            int responseCode=conn.getResponseCode();

            if (responseCode == 200){
                InputStream is=conn.getInputStream();
                String response=getStringFromInputStream(is);
                return response;
            }else {
                throw new NetworkErrorException("responseCode is"+responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkErrorException e) {
            e.printStackTrace();
        }finally {
            //记住这里要关闭连接
            if (conn!=null){
                conn.disconnect();
            }
        }

        //这里不要忘记了返回null，要不然要报错了
        return null;
    }


    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        //此为模板代码，必须熟练
        byte[] buffer=new byte[1024];
        int len=-1;
        while((len = is.read(buffer))!=-1){
            os.write(buffer,0,len);
        }
        is.close();
        String state=os.toString();//将流中的数据转换成字符串，采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;

    }
}
