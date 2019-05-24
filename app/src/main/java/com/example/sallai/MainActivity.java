package com.example.sallai;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
     //每日一句
    private TextView textshow;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;
    private InputStream inputStream;
    private int GET_DATA_SUCCESS=100;
    private int logostatus =101;
    private int WEVCODE=102;
    private int WEBerror=103;
    private int passerror=104;

    //logo
    private Button logo;
    private EditText name;
    private EditText password;
    private int num=-1;
    final int ERRJW = 105;
   // final String BB = "1.0";
    final int updata= 106;
    //===========hanndler处理消息==========================
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if(msg.what== GET_DATA_SUCCESS) {
                String data;
                data = msg.getData().getString("data");
                textshow.setText(data);
            }else if (msg.what==logostatus){
                String data;
                data = msg.getData().getString("data");
                if (data!=null){
                    String str = "JSESSIONID";
                    Boolean Y= data.contains(str);
                    Log.i("tag","数据为1："+data);
                    if (Y){
                        Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(getBaseContext(),ZhuActivity.class);
                        intent.putExtra("JSESSIONID",data);
                        startActivity(intent);
                        finish();

                    }

                }else{

                    Toast.makeText(MainActivity.this, "登陆失败，请稍后重试", Toast.LENGTH_LONG).show();
                }


                waiting.cancel();

            }else if(msg.what==ERRJW){
                num++;
                if (num<4){
                    Toast.makeText(MainActivity.this, "官方教务在线爆炸，请稍后重试！", Toast.LENGTH_LONG).show();
                }else if (num==4){
                    Toast.makeText(MainActivity.this, "歇一会吧，我的主人", Toast.LENGTH_LONG).show();
                }else if (num==5){
                    Toast.makeText(MainActivity.this, "哦买尬，你为什么还在点", Toast.LENGTH_LONG).show();
                }else if (num==6){
                    Toast.makeText(MainActivity.this, "再点我就要崩溃了！", Toast.LENGTH_LONG).show();
                }else if (num==7){
                    Toast.makeText(MainActivity.this, "无语中。。。。", Toast.LENGTH_LONG).show();
                }else if (num==8){
                    Toast.makeText(MainActivity.this, "拿个小本记下来。"+num+"次", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "第"+num+"次被你欺负", Toast.LENGTH_LONG).show();
                }

                waiting.cancel();
            }else if (msg.what==WEBerror){
                Toast.makeText(MainActivity.this, "软件服务器故障，请咨询开发者/检查网络", Toast.LENGTH_LONG).show();
                waiting.cancel();
            }else if (msg.what == passerror){
                waiting.cancel();
                Toast.makeText(MainActivity.this, "账号或密码不正确！", Toast.LENGTH_LONG).show();

            }else if (msg.what==updata){
                String updataweb =  msg.getData().getString("updata");
                String edition=  updataweb.substring(updataweb.indexOf("z1")+2,updataweb.indexOf("z2")).trim();
                final String info = updataweb.substring(updataweb.indexOf("z2")+2)
                Log.i("tag",edition);
//如有更新弹出对话框

                if (!edition.equals(getVersionCode())) {
                    Log.i("tag","success");
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("更新提示：");
                    alertDialog.setMessage("本次更新版本为：" + edition + "\n摘要：" + info);
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "先不更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "立即下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://w.sallai1.tk/apk/sallai.apk"));
                            //设置在什么网络情况下进行下载
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
                            //设置通知栏标题
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setTitle("sallai.apk");
                            request.setDescription("小精灵新版本正在下载");
                            request.setAllowedOverRoaming(false);
                            //设置文件存放目录
                            request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS, "sallai.apk");
                            downManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            id = downManager.enqueue(request);
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("正在下载新版本");
                            alertDialog.setMessage("下载完成后，通知栏会有显示。点击即可安装，如果下载失败，请点击网页进行更新，自行下载。");
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "移步网页更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("http://w.sallai1.tk/updata/updataweb.php"));
                                    startActivity(intent);
                                }

                            });
                            alertDialog.show();
                        }
                    });
                    alertDialog.show();

                }else {

                }
            }


            return true;
        }
    });

    private String postdata;
    private CheckBox checkBox;
    private File file;
    private FileOutputStream fos;
    private String user_name;
    private String user_password;
    private ProgressBar progress;
    private ProgressDialog waiting;
    private DownloadManager downManager;
    private long id;
    private Animation tran;
    private Animation tran1;
    private Animation tran2;
    private ImageView img_70;
    private Animation rotate;

    //=====================启动加载===================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //初始化界面
        ininUI();
        //logo处理
        ininlogo();
        //加载账号
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //updata();
        updata();
     //   Toast.makeText(this, "code"+getVersionCode(), Toast.LENGTH_SHORT).show();
    }

    private void updata() {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                updataweb();
            }
        }).start();
    }

    private void updataweb() {
    String  updataweb = HTTP_updata.internet(2,"","http://www.sallai.tk/updata.php");
    Log.i("tag",updataweb);
    Bundle bundle = new Bundle();
    bundle.putString("updata",updataweb);
    Message message = new Message();
    message.what = updata;
    message.setData(bundle);
    mHandler.sendMessage(message);



    }


    //=====================登陆按钮单击事件=====================
    private void ininlogo() {
        logo = (Button)findViewById(R.id.btn_dl);
        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        checkBox = findViewById(R.id.checkbox);



        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = name.getText().toString();
                user_password = password.getText().toString();
                Log.i("tag","name"+ user_name +"password"+ user_password);
                if (!user_name.equals("")&&!user_password.equals("")&& user_name.length()==10) {
                             showWaitDialog();
                    if(checkBox.isChecked()){

                        try {
                            file = new File(getFilesDir(),"user.txt");
                            fos = new FileOutputStream(file);
                            fos.write((user_name +"##"+ user_password).getBytes());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else{
                        //没有勾选记住密码，删除记录文件
                        if (file.exists()){
                            file.delete();

                        }else{

                        }

                    }
                    postdata = "username=" + user_name + "&password=" + user_password + "&state=1";
                    final String zy = "http://www.sallai.tk";
                    getData(2, postdata, zy);
                }else if (user_name.equals("")&& user_password.equals("")){

                    Toast.makeText(MainActivity.this, "账号密码，都咩输入，我带你去月球吧。", Toast.LENGTH_SHORT).show();
                }else if (user_password.equals("")){
                    Toast.makeText(MainActivity.this, "让我踩踩，你一定是忘记输密码了。", Toast.LENGTH_SHORT).show();
                }else if (user_name.equals("")){
                    Toast.makeText(MainActivity.this, "让我踩踩，你一定是忘记输账号了。", Toast.LENGTH_SHORT).show();
                }else if (user_name.length()!=10){
                    Toast.makeText(MainActivity.this, "建议您检查一下，账号格式吧", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "出现未定义问题", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
//登陆等待
    private void showWaitDialog() {
        waiting = new ProgressDialog(MainActivity.this);
        waiting.setTitle("小精灵");
        waiting.setMessage("登陆中...");
        waiting.setIndeterminate(true);
        waiting.setCancelable(false);
        waiting.show();
    }


    //=================初始化界面获取语录============================
    private void ininUI() {
        tran = AnimationUtils.loadAnimation(this,R.anim.anim);
        tran1 = AnimationUtils.loadAnimation(this,R.anim.anim1);
        tran2 = AnimationUtils.loadAnimation(this,R.anim.anim2);
        rotate = AnimationUtils.loadAnimation(this,R.anim.anim4);
         ImageView yun = findViewById(R.id.yun);
         ImageView yun1 = findViewById(R.id.yun1);
         ImageView yun2 = findViewById(R.id.yun2);
         ImageView yun3 = findViewById(R.id.yun3);
        ImageView ri = findViewById(R.id.ri);
        yun.startAnimation(tran2);
        yun1.startAnimation(tran2);
        yun2.startAnimation(tran1);
        yun3.startAnimation(tran);
        ri.startAnimation(rotate);
        textshow = findViewById(R.id.text_show);
        getData(1,"","");
    }
//========================子线程
    private void getData(final int a, final String post, final String wz) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("tag",post);
               String data=HTTP.internet(a,post,wz);

                Message message=Message.obtain();
                Bundle bundle=new Bundle();
                Log.i("tag","二次获取："+data);
                bundle.putString("data",data);
                message.setData(bundle);
               if(wz==""){

                    message.what = GET_DATA_SUCCESS;

                }else if (data.indexOf("JSESSIONID")!=-1){
                   //登陆成功
                   message.what = logostatus;
                }else if (data.indexOf("error")!=-1){
                   //密码错误
                   message.what = passerror;
               }else if (data.trim().equals("200")){
                   //教务系统爆炸
                   message.what = ERRJW;

               }else{
                   //web服务器爆炸
                   message.what = WEBerror;
               }
                mHandler.sendMessage(message);
            }
        }).start();
    }

//加载账号信息
public void load() throws IOException {
    FileInputStream fiStream = null;
    BufferedReader br = null;
    file = new File(getFilesDir(), "user.txt");
    if (file.exists()) {
        try {
            fiStream = new FileInputStream(file);
            /* 将字节流转化为字符流，转化是因为我们知道info.txt
             * 只有一行数据，为了使用readLine()方法，所以我们这里
             * 转化为字符流，其实用字节流也是可以做的。但比较麻烦
             */
            br = new BufferedReader(new InputStreamReader(fiStream));
            //读取info.txt
            String str = br.readLine();
            //分割info.txt里面的内容。这就是为什么写入的时候要加入##的原因
            String arr[] = str.split("##");
            name.setText(arr[0]);
            password.setText(arr[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    } else {

    }
}



//接受广播

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        }
    }
//
private String getVersionCode(){
    PackageManager packageManager = getApplication().getPackageManager();
    String versionCode = "";
    try {
        PackageInfo packageInfo = packageManager.getPackageInfo(getApplication().getPackageName(), 0);
        versionCode = packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
    }
    return versionCode;
}

}
