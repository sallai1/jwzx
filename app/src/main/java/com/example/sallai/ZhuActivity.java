package com.example.sallai;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

public class ZhuActivity extends AppCompatActivity {

    private TextView textshow;
    private String postdata;
    private int GetImage = 101;
    private int STUINF = 100;
    private int TQ = 102;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment f0;
    private Fragment f;
    private Fragment f1;
    private Fragment f2;
    private Fragment f3;
    private String jsession;
    private String jsession1;

    Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
             if (msg.what==STUINF){
                 String[] array = msg.getData().getStringArray("stuinfo");
                 left_text1.setText("姓名："+array[0]);
                 left_text2.setText("学院："+array[1]);
                 left_text3.setText("籍贯："+array[2]);
                 left_text4.setText("民族："+array[3]);
                 left_text5.setText("政治面貌："+array[4]);

             }else if(msg.what==200){
                 dialog.cancel();

             }


            return true;
        }
    });
    private WebView tq;
    private ImageView btn_0;
    private ImageView btn_3;
    private ImageView btn_2;
    private ImageView btn_1;
    private ImageView btn_4;
    private Button  left_btn1;
    private Bitmap bitmap;
    private FragmentTransaction ft1;
    private Bundle bundle;
    private ImageView leftimage;
    private TextView left_text1;
    private TextView left_text2;
    private TextView left_text3;
    private TextView left_text4;
    private TextView left_text5;
    private TextView  left_text_tq;
    private AlertDialog.Builder catDialog;
    private View dialogview;
    private AlertDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu);
       //定位到控件
        btn_0 = findViewById(R.id.btn_0);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        left_btn1 = findViewById(R.id.left_btn1);
        left_text_tq = findViewById(R.id.left_text_tq);
        leftimage=findViewById(R.id.image_1);
        tq = findViewById(R.id.web_1);
        btn_0.setOnClickListener(btn);
        btn_1.setOnClickListener(btn);
        btn_2.setOnClickListener(btn);
        btn_3.setOnClickListener(btn);
        btn_4.setOnClickListener(btn);
        left_btn1.setOnClickListener(btn);

        left_text1 = findViewById(R.id.left_text1);
        left_text2 = findViewById(R.id.left_text2);
        left_text3 = findViewById(R.id.left_text3);
        left_text4 = findViewById(R.id.left_text4);
        left_text5 = findViewById(R.id.left_text5);
        jsession = getIntent().getExtras().getString("JSESSIONID");
        jsession1 = jsession.substring(jsession.indexOf("=")).trim();
        ininUI();

    }

    private void ininUI() {
        if (isVpnUsed()==true){
            Toast.makeText(this, "vpn正在运行", Toast.LENGTH_SHORT).show();
        }
        if (isVpnUsed()){
            Toast.makeText(this, "vpn正在运行1", Toast.LENGTH_SHORT).show();
        }
        //加载动画
        showcat();
        //加载天气
        tq.loadUrl("https://tianqiapi.com/api.php?style=ta&skin=pitaya");
        //加载学籍信息
        getdatastudent();
       //加载所有fragment
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        f = null;
        f0=new  Fragment_main();
        f =new Fragment_cj();
        f1=new Fragment_sj();
        f2 =new Fragment_gg();
        f3 = new Fragment_my();

        bundle = new Bundle();
        bundle.putString("jsession", jsession1);
        ft.add(R.id.fragment,f0);
        ft.show(f0);
        ft.commitAllowingStateLoss();
        //加载成绩
        ft = fm.beginTransaction();
        ft.add(R.id.fragment,f);
        ft.hide(f);
        f.setArguments(bundle);
        ft.commitAllowingStateLoss();
       //获取头像
        // getImge();
        ft1 = fm.beginTransaction();
        f1.setArguments(bundle);
        ft1.add(R.id.fragment,f1);
        ft1.hide(f1);
        ft1.commitAllowingStateLoss();
        //加载公告Fragment
        ft = fm.beginTransaction();
        ft.add(R.id.fragment,f2);
        ft.hide(f2);
        ft.commitAllowingStateLoss();
         //加载我的
        ft = fm.beginTransaction();
        ft.add(R.id.fragment,f3);
        ft.hide(f3);
        ft.commitAllowingStateLoss();

    }
//动画
    private void showcat() {
        catDialog = new AlertDialog.Builder(ZhuActivity.this);
        catDialog.setTitle("拼命准备数据中");
        catDialog.setCancelable(false);
        catDialog.setView(R.layout.zhu_donghua);
        dialog = catDialog.create();
        dialog.show();


    }


    private void getdatastudent() {
     new  Thread(new Runnable() {
         @Override
         public void run() {
           //  String left_tq= HTTP_TQ.internet(1,"","https://www.apiopen.top/weatherApi?city=%E5%85%B4%E5%9F%8E");
             String studata = HTTP2.internet(4,"state=4&Jsessionid"+jsession1,"http://www.sallai.tk");
             if (!studata.equals("")) {
                 String[] stuinfo = {"", "", "", "", ""};
                 Log.i("tag","123"+studata);
                 int x=0;
                 stuinfo[0] = studata.substring(x=(studata.indexOf("姓名")), studata.indexOf("&nbsp",x));
                 Log.i("tag","123"+stuinfo[0]);
                 stuinfo[0] = stuinfo[0].substring(stuinfo[0].indexOf("gray") + 6).trim();
                 stuinfo[1] = studata.substring(x=studata.indexOf("院系"), studata.indexOf("&nbsp",x));
                 stuinfo[1] = stuinfo[1].substring(stuinfo[1].indexOf("<td >") + 5).trim();
                 stuinfo[2] = studata.substring(x=studata.indexOf("籍贯"), studata.indexOf("&nbsp",x));
                 stuinfo[2] = stuinfo[2].substring(stuinfo[2].indexOf("gray") + 6).trim();
                 stuinfo[3] = studata.substring(x=studata.indexOf("民族"), studata.indexOf("&nbsp",x));
                 stuinfo[3] = stuinfo[3].substring(stuinfo[3].indexOf("gray") + 6).trim();
                 stuinfo[4] = studata.substring(x=studata.indexOf("政治面貌"), studata.indexOf("&nbsp",x));
                 stuinfo[4] = stuinfo[4].substring(stuinfo[4].indexOf("gray") + 6).trim();
                 Bundle bundle =new Bundle();
                 Message message = new Message();
                 bundle.putStringArray("stuinfo",stuinfo);
                 message.setData(bundle);
                 message.what=STUINF;
                 handler.sendMessage(message);
             }else {

             }
             //处理天气数据
           /*
             Log.i("tag","天气1"+left_tq);
             if (!left_tq.equals("")) {
                  int z=0;
                  final StringBuffer stringBuffer = new StringBuffer();
                  String tq_data=(left_tq.substring(z=left_tq.indexOf("},{"),left_tq.indexOf("}",z+3)-1));
                  stringBuffer.append(tq_data.substring(z=tq_data.indexOf("ymd")+6,tq_data.indexOf(",",z)-1)+"   ");//日期
                  stringBuffer.append(tq_data.substring(z=tq_data.indexOf("type")+7,tq_data.indexOf(",",z)-1)+"\n");//天气
                  stringBuffer.append(tq_data.substring(z=tq_data.indexOf("low")+6,tq_data.indexOf(",",z)-1));//低温
                 stringBuffer.append("  "+tq_data.substring(z=tq_data.indexOf("high")+7,tq_data.indexOf(",",z)-1)+"\n");//高温
                  stringBuffer.append("风向："+tq_data.substring(z=tq_data.indexOf("fx")+5,tq_data.indexOf(",",z)-1));
                  stringBuffer.append("  风力："+tq_data.substring(z=tq_data.indexOf("fl")+5,tq_data.indexOf(",",z)-1)+"\n");
                  stringBuffer.append("日出时间："+tq_data.substring(z=tq_data.indexOf("sunrise")+10,tq_data.indexOf(",",z)-1));
                  stringBuffer.append("  日落时间："+tq_data.substring(z=tq_data.indexOf("sunset")+9,tq_data.indexOf(",",z)-1)+"\n");
                  stringBuffer.append("小提示："+tq_data.substring(z=tq_data.indexOf("notice")+9));
                  Log.i("tag","天气"+stringBuffer.toString());
                 left_text_tq.post(new Runnable() {
                     @Override
                     public void run() {
                         left_text_tq.setText(stringBuffer.toString());
                     }
                 });
             }else{

             }
             left_text1.post(new Runnable() {
                 @Override
                 public void run() {

                 }
             });
*/
         }

     }).start();



    }

    private void getImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = HTTP.getImage(1,"http://202.199.224.121:11180/newacademic/manager/studentinfo/photo/studentphoto/0002892398213288.jpg");
              leftimage.post(new Runnable() {
                  @Override
                  public void run() {
                   leftimage.setImageBitmap(bitmap);
                  }
              });
            }
        }).start();
    }


    View.OnClickListener btn =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                switch (v.getId()){
                    case R.id.btn_0:
                        jazaiFragmnet(f0,f,f1,f2,f3);
                        btn_0.setBackgroundColor(Color.rgb(255 ,240 ,245));
                        btn_3.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        btn_1.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        btn_2.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        btn_4.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        break;

                     case R.id.btn_1:
                         //重新实例化ft
                         jazaiFragmnet(f,f0,f1,f2,f3);
                         btn_1.setBackgroundColor(Color.rgb(255 ,240 ,245));
                         btn_0.setBackgroundColor(Color.rgb(255 ,255 ,255));
                         btn_3.setBackgroundColor(Color.rgb(255 ,255 ,255));
                         btn_2.setBackgroundColor(Color.rgb(255 ,255 ,255));
                         btn_4.setBackgroundColor(Color.rgb(255 ,255 ,255));
                         break;
                     case R.id.btn_2:
                         //
                         jazaiFragmnet(f1,f0,f,f2,f3);
                         btn_2.setBackgroundColor(Color.rgb(255 ,240 ,245));
                         btn_0.setBackgroundColor(Color.rgb(255 ,255 ,255));
                         btn_1.setBackgroundColor(Color.rgb(255 ,255 ,255));
                         btn_3.setBackgroundColor(Color.rgb(255 ,255 ,255));
                         btn_4.setBackgroundColor(Color.rgb(255 ,255 ,255));
                         break;
                    case  R.id.btn_3:
                        //Toast.makeText(ZhuActivity.this, "点击了按钮3", Toast.LENGTH_SHORT).show();
                         jazaiFragmnet(f2,f0,f,f1,f3);
                        btn_3.setBackgroundColor(Color.rgb(255 ,240 ,245));
                        btn_0.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        btn_1.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        btn_2.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        btn_4.setBackgroundColor(Color.rgb(255 ,255 ,255));
                         break;
                    case  R.id.btn_4:
                        btn_4.setBackgroundColor(Color.rgb(255 ,240 ,245));
                        btn_0.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        btn_1.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        btn_2.setBackgroundColor(Color.rgb(255 ,255 ,255));
                        btn_3.setBackgroundColor(Color.rgb(255 ,255 ,255));
                       jazaiFragmnet(f3,f,f0,f1,f2);

                        break;
                    case R.id.left_btn1:
                        Intent intent = new Intent();
                        intent.putExtra("JSESSIONID",jsession1);
                        intent.setClass(getApplicationContext(), Room_activity.class);
                        startActivity(intent);
                        break;
                 }

        }
    };

    private void jazaiFragmnet(Fragment fragment ,Fragment fragment1,Fragment fragment2,Fragment fragment3,Fragment fragment4) {
       Fragment cur =getSupportFragmentManager().findFragmentById(R.id.fragment);
    //   Log.i("tag","fargment"+fragment+"cur"+cur);

            ft=fm.beginTransaction();
            ft.hide(fragment1);
            ft.hide(fragment2);
            ft.hide(fragment3);
            ft.hide(fragment4);
            if (!fragment.isAdded()){
                ft.add(R.id.fragment,fragment);
                Bundle bundle1 =new Bundle();
                bundle1.putString("jsession",jsession1);
                fragment.setArguments(bundle1);
            }
            //设置fragment切换效果
            ft.setTransition(TRANSIT_FRAGMENT_OPEN);
            ft.show(fragment);
            ft.commitAllowingStateLoss();


    }

    public static boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if(niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }




}