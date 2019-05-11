package com.example.sallai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room_activity extends AppCompatActivity {

    private ListView roomlist;
    private Spinner sp1;
    private Spinner sp2;
    private Spinner sp3;
    private Spinner sp4;
    private Button btn1;
    private String jsession;
    final int SUCCESS = 100;
    final int FALSE = 101;
    private  ProgressDialog progressDialog;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            progressDialog.cancel();
            if (msg.what == SUCCESS) {
                String data = msg.getData().getString("data");
                Log.i("tag", data);
              //  String[] dataarray = data.substring(data.indexOf("<tr style="), data.lastIndexOf("</table></td>")).split("</table></td>");
              //  StringBuffer stringBuffer = new StringBuffer();
               // int x = 0;
                List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                try
                {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray user = jsonObject.optJSONArray("user");
                    Log.i("tag",Integer.toString(user.length()));
                    for (int t=1;t < user.length();t++){
                        JSONObject jsonObject1 = user.optJSONObject(t);
                        Log.i("tag",jsonObject1.optString("name"));
                        JSONArray jsonArray =jsonObject1.optJSONArray("array1");
                            Map<String, Object> map = new HashMap<String, Object>();
                            //教室状态图标

                            for (int j = 0; j < jsonArray.length(); j++) {

                                if (jsonArray.optInt(j)==0) {
                                    map.put(Integer.toString(j), R.mipmap.kong);//空
                                } else {
                                    map.put(Integer.toString(j), R.mipmap.zhan);//占
                                }


                            }





                            map.put("number",jsonObject1.optString("name"));//房间
                            map.put("type",jsonObject1.optString("type"));//type
                            map.put("num",jsonObject1.optString("Num"));//num

                            mapList.add(map);



                        }




                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }







                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), mapList, R.layout.room_list_item, new String[]{"number", "type", "num", "0","1","2","3","4"},
                        new int[]{R.id.text_1, R.id.text_2, R.id.text_3, R.id.state_image1,R.id.state_image2,R.id.state_image3,R.id.state_image4,
                                R.id.state_image5});


                roomlist.setAdapter(adapter);



        }else if(msg.what==FALSE)

        {
            Toast.makeText(Room_activity.this, "出错了~", Toast.LENGTH_SHORT).show();
        }


            return true;
    }

    });





    private String da;
    private String js;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.left_activity_room);
        ininUi();
        getdata();
        String testdata = "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"计算机基础教程\",\n" +
                "    \"press\": \"清华大学出版社\",\n" +
                "    \"author\": \"李晓云\",\n" +
                "    \"price\": 30.5\n" +
                "  }";
        parseJSONWithJSONObject(testdata);


    }
    private void parseJSONWithJSONObject(String JsonData) {
        Log.i("tag","11");
        try
        {
            JSONObject jsonObject = new JSONObject(JsonData);


              //  JSONObject jsonObject = jsonArray.getJSONObject(0);
                String id = jsonObject.getString("name");
                Log.i("tag",id);




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void ininUi() {

          LinearLayout linearLayout= findViewById(R.id.box);
          linearLayout.getBackground().setAlpha(100);

    }

    private void getdata() {
        roomlist = findViewById(R.id.room_list);
        sp1 = findViewById(R.id.spinner1); //校区
        sp2 = findViewById(R.id.spinner2);//教学楼
        sp3 = findViewById(R.id.spinner3);//第几周
        sp4 = findViewById(R.id.spinner4);//星期几
        btn1 = findViewById(R.id.btn_1);
        js = getIntent().getExtras().getString("JSESSIONID").trim();

        btn1.setOnClickListener(btn);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
         /*
         *   校本部 1
         *   校本部北2
         *   葫芦岛校区3
         *   朝阳校区4
         * */
                if (item.equals("校本部")){
                    List<String> list = new ArrayList<String>();
                    list.add("博雅楼");//5
                    list.add("物理实验室");//10
                    list.add("新华楼");//4
                    list.add("知行楼");//15
                    list.add("致远楼");//16
                    list.add("中和楼");//12
                    list.add("主楼机房");//8
                    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, list);
                    sp2.setAdapter(arrayAdapter);
                }else if (item.equals("校本部北")){
                    List<String> list = new ArrayList<String>();
                    list.add("文轩楼"); // 3720
                    list.add("育龙二号实验楼");//2437
                    list.add("育龙实验楼");//2023
                    list.add("育龙主楼");//1913
                    list.add("综合实验楼");//3716
                    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, list);
                    sp2.setAdapter(arrayAdapter);
                }else if(item.equals("葫芦岛")){
                    List<String> list = new ArrayList<String>();
                    list.add("尔雅楼"); // 6
                    list.add("静远楼");//14
                    list.add("耘慧楼");//7
                    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, list);
                    sp2.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    View.OnClickListener btn =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressDialog = new ProgressDialog(Room_activity.this);
            progressDialog.setTitle("查询中");
            progressDialog.setMessage("请等待 \n提示：红色代表占用/绿色代表未占用");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
             String sp1_text=  sp1.getSelectedItem().toString();
             String sp2_text=  sp2.getSelectedItem().toString();
             String sp3_text=  sp3.getSelectedItem().toString();
             String sp4_text=  sp4.getSelectedItem().toString();
             if (!sp1_text.equals("")&&!sp2_text.equals("")&&!sp3_text.equals("")){

                 String aid = "";
                 String buildingid = "";
                 String whichweek = "";
                 String week="";
                 if (sp1_text.equals("校本部")) {
                     aid = "1";
                     if (sp2_text.equals("博雅楼")){
                         buildingid="5";
                     }else if (sp2_text.equals("物理实验室")){
                         buildingid="10";
                     }else if (sp2_text.equals("新华楼")){
                         buildingid="4";
                     }else if (sp2_text.equals("知行楼")){
                         buildingid="15";
                     }else if (sp2_text.equals("致远楼")){
                         buildingid="16";
                     }else if (sp2_text.equals("中和楼")){
                         buildingid="12";
                     }else if (sp2_text.equals("主楼机房")){
                         buildingid="8";
                     }
                 } else if (sp1_text.equals("校本部北")) {
                     aid = "2";
                 } else if (sp1_text.equals("葫芦岛")) {
                     aid = "3";
                     if (sp2_text.equals("尔雅楼")){
                         buildingid="6";
                     }else if (sp2_text.equals("静远楼")){
                         buildingid="14";
                     }else if (sp2_text.equals("耘慧楼")){
                         buildingid="7";
                     }else{

                     }


                 } else {

                 }
                 jsession = "week="+sp3_text+"&build="+buildingid;
                 Log.i("tag",jsession);


                 getdataweb(jsession);
             }


        }
    };
//从web获取源码
    private void getdataweb(final String jss) {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                String webdata = HTTP.internet(2,jss,"http://sallai.mao2234.cn/jwzx/room/room.php");
                Message message = new Message();
                if(webdata.indexOf("user")!=-1){
                    Bundle bundle= new Bundle();
                    bundle.putString("data",webdata);
                    message.setData(bundle);
                    message.what = SUCCESS;



                }else{
                    message.what= FALSE;
                }
                handler.sendMessage(message);
            }
        }).start();

    }
//解析json 房间数据
//方法一：使用JSONObject



}
