package com.example.sallai;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Fragment_cj extends Fragment {
    private TextView textshow;
    private String postdata;
    private int GET_DATA_SUCCESS = 101;
    private int logostatus = 100;
    private ListView cjlist;
    //   private Handler mHandler;
    Handler mes = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == GET_DATA_SUCCESS) {
                String data;
                data = msg.getData().getString("data");
                textshow.setText(data);
            } else if (msg.what == logostatus) {
                Log.i("tag", "测试点二：消息已接受");
                String data;
                data = msg.getData().getString("data");
                int m=0;
                String jidian= data.substring(m=(data.indexOf("分绩是"))+3,data.indexOf("，",m)).trim();
                if (Float.parseFloat(jidian)>2.0)
                {
                    text_1.setText("你获得的绩点为："+jidian+"分   很棒！");
                }else if(Float.parseFloat(jidian)<2.0){
                    text_1.setText("你获得的绩点为："+jidian+"分   继续加油！");
                }else if(Float.parseFloat(jidian)>3.5){
                    text_1.setText("你获得的绩点为："+jidian+"分   超级厉害！");
                }else {

                }


                int first = data.lastIndexOf("cidalls[0]");
                int last = data.lastIndexOf("</table>");
                String jq = data.substring(first, last);
                array = jq.split("language");
                StringBuffer buffer = new StringBuffer();
                int n = array.length;

                //后继截取索引
                int x;

                for (int i = 0; i < n; i++) {
                    //获取课程号
                    buffer.append("课程号" + array[i].substring(array[i].indexOf("]=") + 3, array[i].indexOf("ytalls") - 3).trim());
                    //判断学期分类存储
                    buffer.append("学期" + array[i].substring(array[i].lastIndexOf("]=") + 3, array[i].lastIndexOf("]=") + 7).trim());
                    buffer.append("课程名" + array[i].substring(array[i].indexOf("<td>") + 4, x = (array[i].indexOf("</td>", array[i].indexOf("<td>") + 2))).trim());
                    buffer.append("课序号" + array[i].substring(array[i].indexOf("<td>", x + 3) + 4, x = (array[i].indexOf("</td>", x + 3))).trim());
                    if ((array[i].substring(array[i].indexOf(">", x + 7) + 2, array[i].indexOf("</", x + 7) - 1).trim()).length() > 8) {


                        String score = array[i].substring(array[i].indexOf(">", x + 7) + 2, x = (array[i].indexOf("</td>", x + 7) )).trim();
                        buffer.append("分数" + score.substring(score.indexOf(">")+2 ,score.indexOf("<")).trim());
                        x=x+8;
                    } else {
                        buffer.append("分数" + array[i].substring(array[i].indexOf(">", x + 7) + 1, x = (array[i].indexOf("</", x + 7) - 1)).trim());
                        Log.i("tag", "X=" + x + "标记1" + array[i].indexOf("ter", x + 7) + "标记二" + array[i].indexOf("</", x + 7) + "array" + array[i].length());
                    }
                    //


                    buffer.append("学分" + array[i].substring(array[i].indexOf(">", x + 7) + 1, x = (array[i].indexOf("</td>", x + 7))).trim());
                    buffer.append("类型" + array[i].substring( array[i].indexOf(">",x=(array[i].indexOf("</td>",    x=(array[i].indexOf("</td>" ,    x = (array[i].indexOf("</td>", x + 7)+7)))+7)+7)+7)+1  ,  array[i].indexOf("</td>",x)).trim());

                    //x3=array[i].length();
                    //  Log.i("tag","x,x1为"+x+"第yi个"+x1+"第二个"+x2+"字符串长度"+x3);
                    array[i] = buffer.toString();
                    buffer.setLength(0);
                    Log.i("tag", array[i]);

                    handler.sendEmptyMessage(200);
                }

        }
            return true;
        }
    });
    private TextView text_1;
    private String[] array;
    private Spinner spinner;
    private Handler handler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        if (savedInstanceState==null) {
            View view = inflater.inflate(R.layout.fragment_cj, null);
            cjlist = view.findViewById(R.id.Fragment_list_cj);
            text_1 = view.findViewById(R.id.text_1);
            spinner = view.findViewById(R.id.cj_spinner);
            spinner.setSelection(5);
            ZhuActivity activity = (ZhuActivity) getActivity();
            handler = activity.handler;
            return view;
        }else {
            return null;
        }
}

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void getData ( final int a, final String post, final String wz){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = HTTP1.internet(a, post, wz);

               if(data.indexOf("课程成绩查询")!=-1){
                   Log.i("tag", "二次获取：" + data);
                   Message message = Message.obtain();
                   Bundle bundle = new Bundle();
                   bundle.putString("data", data);
                   message.setData(bundle);
                   message.what = logostatus;
                   mes.sendMessage(message);
               }else{


               }


                //向主线程发送数据

            }
        }).start();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments()!= null) {
            Log.i("tag", "fragment创建完成已经进入");



            String jsession = getArguments().getString("jsession").trim();
            postdata = "state=2&Jsessionid"+jsession;
            Log.i("tag", "buffer" + postdata);

            //调用子线程
            getData(2, postdata, "http://www.sallai.tk");
        }
        //分类显示列表内容
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String item= parent.getItemAtPosition(position).toString();
               if (item.equals("显示所有成绩")){
                   cjdata("1");
               }
                 else if (item.equals("2020秋季学期")){
                   cjdata("40-2");

               }else if (item.equals("2020春季学期")){
                   cjdata("40-1");
               }else if (item.equals("2019秋季学期")){
                   cjdata("39-2");
               }else if (item.equals("2019春季学期")){
                   cjdata("39-1");
               }else if (item.equals("2018秋季学期")){
                   cjdata("38-2");
                   Log.i("tag","测试点击了");
               }else if (item.equals("2018春季学期")){
                   cjdata("38-1");
               }else if (item.equals("2017秋季学期")){
                   cjdata("37-2");
               }else if (item.equals("2017春季学期")){
                   cjdata("37-1");
               }else if (item.equals("2016秋季学期")){
                   cjdata("36-2");
               }else if (item.equals("2016春季学期")) {
                   cjdata("36-1");
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void cjdata(String state) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
             Log.i("tag","测试"+array[1]);

        if (state.equals("1")){

            for (int i = 0; i < array.length; i++) {
                Map<String, Object> map = new HashMap<String, Object>();

                map.put("kch", array[i].substring(array[i].indexOf("课程号") + 3, array[i].indexOf("学期")));
                map.put("kcm", array[i].substring(array[i].indexOf("课程名") + 3, array[i].indexOf("课序号")));
                map.put("kcf", array[i].substring(array[i].indexOf("分数") + 2, array[i].indexOf("学分")));
                map.put("kcxf", array[i].substring(array[i].indexOf("学分") + 2, array[i].indexOf("类型")));
                map.put("kclx", array[i].substring(array[i].indexOf("类型") + 2));
                mapList.add(map);
            }
        }
           else {
            for (int i = 0; i < array.length; i++) {


                if (array[i].substring(array[i].indexOf("学期") + 2, array[i].indexOf("课程名")).equals(state)) {


                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("kch", array[i].substring(array[i].indexOf("课程号") + 3, array[i].indexOf("学期")));
                    map.put("kcm", array[i].substring(array[i].indexOf("课程名") + 3, array[i].indexOf("课序号")));
                    map.put("kcf", array[i].substring(array[i].indexOf("分数") + 2, array[i].indexOf("学分")));
                    map.put("kcxf", array[i].substring(array[i].indexOf("学分") + 2, array[i].indexOf("类型")));
                    map.put("kclx", array[i].substring(array[i].indexOf("类型") + 2));
                    mapList.add(map);

                } else {

                }
            }

        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), mapList, R.layout.list_item, new String[]{"kch", "kcm", "kcxf", "kclx", "kcf"}, new int[]{R.id.list_1, R.id.list_2, R.id.list_3, R.id.list_4, R.id.list_5});

        cjlist.setAdapter(adapter);



    }
}
