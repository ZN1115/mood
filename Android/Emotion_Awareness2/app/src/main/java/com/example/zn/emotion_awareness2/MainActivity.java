package com.example.zn.emotion_awareness2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    databaseHelper CheckFirstdb;

    private ImageView logo;
    private static int SPLASH_TIME_OUT=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckFirstdb=new databaseHelper(this);

        logo=findViewById(R.id.LogoView);
        Animation logoanim= AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);
        logo.startAnimation(logoanim);
        viewALl();
    }

    public void viewALl(){
        Cursor res =CheckFirstdb.getAllData();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent =new Intent(MainActivity.this,RecordNormal.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

        return;*/
        if(res.getCount()==0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeIntent =new Intent(MainActivity.this,RecordNormal.class);
                    startActivity(homeIntent);
                    finish();
                }
            },SPLASH_TIME_OUT);

            return;
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeIntent =new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            },SPLASH_TIME_OUT);
            return;
        }


    }

    public void Add(){
        boolean isInsert=CheckFirstdb.insertData("0");
        if (isInsert == true) {
            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Data not Insert", Toast.LENGTH_LONG).show();
        }
    }
}
