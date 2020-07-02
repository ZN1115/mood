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

import steelkiwi.com.library.DotsLoaderView;

public class GetResult extends AppCompatActivity {

    databaseHelper CheckFirstdb;
    DotsLoaderView dotsLoaderView;
    TextView load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_result);
        dotsLoaderView = (DotsLoaderView) findViewById(R.id.dotsLoaderView);
        load=(TextView)findViewById(R.id.load);
        CheckFirstdb=new databaseHelper(this);
        Add();
        getData();
        animstart();
        //nextpage();
    }

    private void nextpage() {
        /*final Handler samHandler=new Handler();
        samHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("samHandler","1");
                dotsLoaderView.hide();
                Animation animation = new AlphaAnimation(1.0f,0.0f);
                animation.setDuration(2500);
                load.startAnimation(animation);
                //load.setVisibility(View.INVISIBLE);
                Intent getresultintent=new Intent(GetResult.this,HomeActivity.class);
                startActivity(getresultintent);
                finish();
            }
        },5500);*/
        dotsLoaderView.hide();
        Animation animation = new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(2500);
        load.startAnimation(animation);
        //load.setVisibility(View.INVISIBLE);
        Intent getresultintent=new Intent(GetResult.this,HomeActivity.class);
        startActivity(getresultintent);
        finish();

    }

    private void getData() {
        String url = "http://192.168.137.1/FileUpload/images/a.txt";
        UrlHttpUtil.get(url, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {
                Log.i("check file","a.txt fail");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){

                    @Override
                    public void run() {

                        //過兩秒後要做的事情
                        Log.d("tag","我要打掃了");
                        getData();

                    }}, 5000);
            }

            @Override
            public void onResponse(String response) {
                //Toast.makeText(getResult.this,"Success",Toast.LENGTH_SHORT).show();
                Log.d("kwwl",response);
                nextpage();

                //String[] split=response.split("\n");
                /*userinfo=new ArrayList<>();
                for(String s:split){
                    //Log.i("respone",s);
                    String[] split2=s.split(":");
                    userinfo.add(split2[1]);
                }
                userdata.setDate(userinfo.get(0));
                userdata.setAngry(userinfo.get(1));
                userdata.setSad(userinfo.get(2));
                userdata.setAnxious(userinfo.get(3));
                userdata.setDepressed(userinfo.get(4));
                userdata.setScared(userinfo.get(5));*/



                //Toast.makeText(getResult.this,"分析完畢",Toast.LENGTH_LONG).show();
                //


            }
        });
    }

    private void animstart() {
        dotsLoaderView.show();
    }

    public void Add(){
        boolean isInsert=CheckFirstdb.insertData("1");
        if (isInsert == true) {
            //Toast.makeText(this, "Data Inserted", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Data not Insert", Toast.LENGTH_LONG).show();
        }
    }
}
