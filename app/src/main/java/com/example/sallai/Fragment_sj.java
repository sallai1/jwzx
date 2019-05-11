package com.example.sallai;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.PrivateCredentialPermission;

public class Fragment_sj<Private> extends Fragment {

    private int GET_DATA_SUCCESS=100;
    private  int GET_TIME=101;
    private String postdata;


    Handler mes = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == GET_DATA_SUCCESS) {

                 String tt= msg.getData().getString("time").substring(13,24);
                 Log.i("tag","time"+tt);

                Log.i("tag", "测试点二：消息已接受");
                String data;
                data = msg.getData().getString("data");
                Log.i("tag", "html"+data);

                int first = data.lastIndexOf("<!--<th>考试方式</th>-->");
                int last = data.lastIndexOf(">双学位报");
                String jq = data.substring(first, last);
                String[] array = jq.split("<!--<td></td>-->");
                StringBuffer buffer = new StringBuffer();
                int n = array.length;
              //  Log.i("tag", "length"+n+"第一个"+array[0]+"最后一个"+array[n-1]);


             //后继截取索引
                int x;

                for (int i = 0; i < n-1; i++) {
                    //获取课程号
                    buffer.append("课程" + array[i].substring(array[i].indexOf("<td>") + 4, x=(array[i].indexOf("</td>"))).trim());
                    buffer.append("时间" + array[i].substring(array[i].indexOf(">",x+6) + 1, x= (array[i].indexOf("</td>",x+6))).trim());
                    buffer.append("地点" + array[i].substring(array[i].indexOf("<td>",x+5) + 4, (array[i].indexOf("&nbsp", x+5))).trim());
          //Log.i("tag", "yi"+array[i].indexOf("<td>",x+5)+"er"+array[i].indexOf("</td>", x+5));

/*
                    buffer.append("学分" + array[i].substring(array[i].indexOf(">", x + 7) + 1, x = (array[i].indexOf("</td>", x + 7))).trim());
                    buffer.append("考核方式" + array[i].substring(array[i].indexOf(">", x + 7) + 1, x = (array[i].indexOf("</td>", x + 7))).trim());
                    buffer.append("选课" + array[i].substring(array[i].indexOf(">", x + 7) + 1, x = (array[i].indexOf("</td>", x + 7))).trim());
*/

                    array[i] = buffer.toString();
                    buffer.setLength(0);
                    Log.i("tag", array[i]);

                                    }

                List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                StringBuffer t = new StringBuffer();
                for (int i = 0; i < array.length-1; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("kc", array[i].substring(array[i].indexOf("课程") + 2, array[i].indexOf("时间")));
                    map.put("sj", array[i].substring(array[i].indexOf("时间") + 2, array[i].indexOf("地点")));
                    //判断
                    try {
                        int nn =jstime(tt,(array[i].substring(array[i].indexOf("时间") + 2, array[i].indexOf(" "))));
                        if (nn>0){
                            map.put("time",nn+"天");

                        }else if(nn==0){
                            map.put("time","today");

                        }else  if (nn<0){
                            map.put("time","完成");
                        }else{
                            map.put("time","null");
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    map.put("dd", array[i].substring(array[i].indexOf("地点") + 2));
                    mapList.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(getActivity(), mapList, R.layout.list_sj, new String[]{"kc", "sj", "dd","time"}, new int[]{ R.id.text_1, R.id.text_2,  R.id.text_3,R.id.text_4});


                sjview.setAdapter(adapter);
            }else if(msg.what==GET_TIME){


            }
            return true;
        }
    });

    private ListView sjview;
    private String data;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState==null) {

            View view = inflater.inflate(R.layout.fragment_sj, null);
            return view;
        }else{
            return null;
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sjview = view.findViewById(R.id.list_sj);
        Log.i("tag", "fragment创建完成已经进入");



        String jsession = getArguments().getString("jsession");
  //      postdata = "&state=3&Jsessionid"+jsession;
        jsession = jsession.substring(jsession.indexOf("=")+1);
        postdata = "state=3&Jsessionid="+jsession;
        Log.i("tag", "buffer" + postdata);

        //调用子线程
      //  getData(2, postdata, "http://202.199.224.24:11082/academic/student/exam/index.jsdo?groupId=&moduleId=2030");
        getData(2, postdata, "http://www.sallai.tk");

    }
//计算考试剩余天数
    private void getData ( final int a, final String post, final String wz){
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = HTTP.internet(a, post, wz);
                String time =HTTP.internet(2,"","http://quan.suning.com/getSysTime.do");
                Log.i("tag", "二次获取：" + data);
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("data", data);
                bundle.putString("time",time);
                message.setData(bundle);
                message.what = GET_DATA_SUCCESS;
                //向主线程发送数据
                mes.sendMessage(message);
            }
        }).start();

    }
    public static int jstime(String start,String end) throws ParseException {

        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=new GregorianCalendar();
        java.util.Date d1=df.parse(start);
        java.util.Date d2=df.parse(end);
        return (int)((d2.getTime()-d1.getTime())/(60*60*1000*24));
    }

}
