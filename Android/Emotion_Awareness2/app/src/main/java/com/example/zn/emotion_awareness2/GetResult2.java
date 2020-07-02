package com.example.zn.emotion_awareness2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zn.emotion_awareness2.urlhttp.CallBackUtil;
import com.example.zn.emotion_awareness2.urlhttp.UrlHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;

import steelkiwi.com.library.DotsLoaderView;

public class GetResult2 extends AppCompatActivity {

    mooddatabaseHelper mooddatabaseHelper;
    DotsLoaderView dotsLoaderView;
    ArrayList<String> userinfo;
    TextView load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_result2);

        dotsLoaderView = (DotsLoaderView) findViewById(R.id.dotsLoaderView);
        load=(TextView)findViewById(R.id.load);
        mooddatabaseHelper=new mooddatabaseHelper(this);
        postserver();
        animstart();
    }

    private void getData() {
        String url = "http://192.168.137.1/FileUpload/images/test.txt";
        UrlHttpUtil.get(url, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {
                Log.d("getresult2","test.txt fail");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){

                    @Override
                    public void run() {

                        //過兩秒後要做的事情
                        Log.d("tag","我要打掃了");
                        getData();

                    }}, 3000);
            }

            @Override
            public void onResponse(String response) {

                Log.d("kwwl",response);

                String[] split=response.split("\n");
                userinfo=new ArrayList<>();
                for(String s:split){
                    //Log.i("respone",s);
                    String[] split2=s.split(":");
                    Log.i("split2", split2[1]);
                    userinfo.add(split2[1]);
                    Log.i("userinfo", String.valueOf(userinfo));
                }
                Log.i("userinfo22", userinfo.get(0));
                //Toast.makeText(getResult.this,"分析完畢",Toast.LENGTH_LONG).show();
                //
                Add(userinfo.get(0),userinfo.get(1),userinfo.get(2),userinfo.get(3),userinfo.get(4),userinfo.get(5),userinfo.get(6));
                //Toast.makeText(GetResult2.this,"存入資料庫",Toast.LENGTH_SHORT).show();
                Intent toinformation=new Intent(GetResult2.this,information.class);
                startActivity(toinformation);
                finish();
            }
        });
    }

    private void postserver() {
        String url = "http://192.168.137.1/FileUpload/test.php";
        final HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("data","moodanalysis");
        UrlHttpUtil.post(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {

            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(GetResult2.this,"分析數值...",Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){

                    @Override
                    public void run() {

                        //過兩秒後要做的事情
                        Log.d("tag","我要打掃了");
                        postserver2();

                    }}, 5000);

                //Log.i("recordnormal upload: ",response);
                //getData();
            }
        });
    }

    private void postserver2() {
        String url = "http://192.168.137.1/FileUpload/test.php";
        final HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("data","final");
        UrlHttpUtil.post(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {

            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(GetResult2.this,"整合數值...",Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){

                    @Override
                    public void run() {

                        //過兩秒後要做的事情
                        Log.d("tag","我要打掃了");
                        getData();
                    }}, 3000);

                //Log.i("recordnormal upload: ",response);
                //getData();
            }
        });
    }

    private void animstart() {
        dotsLoaderView.show();
    }

    public void Add(String angry,String sad,String anxious,String depressed,String scared,String score,String date){

        boolean isInsert=mooddatabaseHelper.insertData(angry,sad,anxious,depressed,scared,score,date);
        if (isInsert == true) {
            //Toast.makeText(this, "Data Inserted", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Data not Insert", Toast.LENGTH_LONG).show();
        }
    }
}
