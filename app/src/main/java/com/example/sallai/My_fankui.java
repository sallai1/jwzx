package com.example.sallai;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;


public class My_fankui extends AppCompatActivity {

    private Button btn1;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private RatingBar ratingBar;
    private StringBuffer stringBuffer;
    private String code;
    private int t = 1;
    private int success = 2;
    private int False = 3;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            progressDialog.cancel();
            if(msg.what==success){
                 Toast.makeText(My_fankui.this, "你的意见已成功发送给管理员，感谢您的使用", Toast.LENGTH_SHORT).show();
             }else if (msg.what==False){
                 Toast.makeText(My_fankui.this, "发送异常请稍后再试！", Toast.LENGTH_SHORT).show();

             }else {
                 Toast.makeText(My_fankui.this, "未知异常！", Toast.LENGTH_SHORT).show();
             }


            return true;
        }
    });
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_fankui);
        ininUI();

    }




    private void ininUI() {
        btn1 = findViewById(R.id.fk_btn1);
        et1 = findViewById(R.id.fk_et1);
        et2 = findViewById(R.id.fk_et2);
        et3 = findViewById(R.id.fk_et3);
        ratingBar = findViewById(R.id.fk_rbar);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(My_fankui.this);
                progressDialog.setTitle("正在通信中");
                progressDialog.setMessage("请等待");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();

                stringBuffer = new StringBuffer();
                stringBuffer.append("name="+et1.getText());
                stringBuffer.append("&type="+et2.getText());
                stringBuffer.append("&text="+et3.getText());
                stringBuffer.append("&rating="+ratingBar.getRating());
                if (t==1){
                    postdata();
                }else{
                    Toast.makeText(My_fankui.this, "请不要频繁提交！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void postdata() {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                t=0;
                Log.i("tag","biaoji"+stringBuffer.toString());
                code = HTTP_TQ.internet(4,stringBuffer.toString(),"http://w.sallai1.tk/email/email.php").trim();
                Message message = new Message();
                if (code.indexOf("SUCCESS")!=-1){
                    t=1;
                    message.what=success;

                }else{
                  t=1;
                  message.what=False;
                }
                handler.sendMessage(message);
            }
        }).start();

    }
}
