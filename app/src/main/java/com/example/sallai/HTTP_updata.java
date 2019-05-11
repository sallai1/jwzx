package com.example.sallai;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telecom.Call;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.FloatBuffer;

public class HTTP_updata {

    private static URL url;
    private static HttpURLConnection connection;
    private static InputStream inputStream;
    private static BufferedReader bufferedReader;
    private static int code;
    private static Bitmap bm;
    private static Bitmap bitmap;

    //    ======================参数格式=状态码=post数据==访问网址=============================
    static String internet(int state, String post, String wz) {

        try {
            //1.创建一个url
            if (state == 1) {
                url = new URL("https://v1.hitokoto.cn/?encode=text");
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
            } else {
                url = new URL(wz);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestMethod("POST");

                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.writeBytes(post);
                dos.flush();
                dos.close();
                Log.i("tag", "post" + post);
                //connection.setRequestProperty("Cookie",);//传入Cookie

            }

            Log.i("tag", "code：" + connection.getResponseCode());

            int code = connection.getResponseCode();
            //判断并处理结果
            if (code == 200) {
                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                for (line = ""; (line = bufferedReader.readLine()) != null; ) {
                    stringBuilder.append(line);
                }
                Log.i("tag", "数据：" + stringBuilder.toString());
                return stringBuilder.toString();


            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (bufferedReader != null) bufferedReader.close();
                if (inputStream != null) inputStream.close();
                if (connection != null) connection.disconnect();
                Log.i("tag", "输入流关闭");


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return Integer.toString(code);
    }
}