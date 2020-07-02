package com.example.zn.emotion_awareness2;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class information extends AppCompatActivity {

    private Button tohome;

    RelativeLayout lin1;
    RelativeLayout lin2;
    RelativeLayout lin3;
    RelativeLayout lin4;
    RelativeLayout lin5;

    RoundCornerProgressBar progress1;
    RoundCornerProgressBar progress2;
    RoundCornerProgressBar progress3;
    RoundCornerProgressBar progress4;
    RoundCornerProgressBar progress5;

    //Data userdata;
    mooddatabaseHelper mooddatabaseHelper;
    private int angryvalue;
    private int sadvalue;
    private int anxiousvalue;
    private int depressedvalue;
    private int scaredvalue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        tohome=(Button)findViewById(R.id.tohome);
        tohome.setVisibility(View.VISIBLE);
        mooddatabaseHelper=new mooddatabaseHelper(this);

        lin1 = (RelativeLayout) findViewById(R.id.angrylayout);
        lin2 = (RelativeLayout) findViewById(R.id.sadlayout);
        lin3 = (RelativeLayout) findViewById(R.id.anxiouslayout);
        lin4 = (RelativeLayout) findViewById(R.id.depressedlayout);
        lin5 = (RelativeLayout) findViewById(R.id.scaredlayout);

        progress1 = (RoundCornerProgressBar) findViewById(R.id.angryprogressbar);
        progress2 = (RoundCornerProgressBar) findViewById(R.id.sadprogressbar);
        progress3 = (RoundCornerProgressBar) findViewById(R.id.anxiousprogressbar);
        progress4 = (RoundCornerProgressBar) findViewById(R.id.scaredprogressbar);//有做修改
        progress5 = (RoundCornerProgressBar) findViewById(R.id.depressedprogressbar);


        viewAll();
        //userdata = (Data) getApplicationContext();
//        angryvalue = Integer.parseInt(userdata.getAngry());
//        sadvalue=Integer.parseInt(userdata.getSad());
//        anxiousvalue=Integer.parseInt(userdata.getAnxious());
//        depressedvalue=Integer.parseInt(userdata.getDepressed());
//        scaredvalue=Integer.parseInt(userdata.getScared());
    }

    private void viewAll() {
        Cursor res=mooddatabaseHelper.getAllData();
        StringBuffer buffer=new StringBuffer();
        int sum=0;
        while (res.moveToNext()){
            sum++;
            if(res.getCount()==sum)
            {
                //Log.i("res.moveToNext()", res.getString(1));
                float f=Float.parseFloat(res.getString(1))*100;
                angryvalue=(int)f;
                float f1=Float.parseFloat(res.getString(2))*100;
                sadvalue=(int)f1;
                float f2=Float.parseFloat(res.getString(3))*100;
                anxiousvalue=(int)f2;
                float f3=Float.parseFloat(res.getString(4))*100;
                scaredvalue=(int)f3;
                float f4=Float.parseFloat(res.getString(5))*100;
                depressedvalue=(int)f4;
            }

        }
        Log.i("size", String.valueOf(buffer.length()));
        Log.i("getColumnNames", String.valueOf(res.getCount()));


    }

    @Override
    protected void onResume() {
        super.onResume();
        final Handler samHandler = new Handler();
        samHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setBackground();
                Log.i("1", "1");
            }
        }, 1000);

    }

    private void setBackground() {
        final AlphaAnimation anim1 = new AlphaAnimation(0.0f, 3.0f);
        final AlphaAnimation anim2 = new AlphaAnimation(0.0f, 3.0f);
        final AlphaAnimation anim3 = new AlphaAnimation(0.0f, 3.0f);
        final AlphaAnimation anim4 = new AlphaAnimation(0.0f, 3.0f);
        final AlphaAnimation anim5 = new AlphaAnimation(0.0f, 3.0f);
        anim1.setDuration(800);
        anim2.setDuration(800);
        anim3.setDuration(800);
        anim4.setDuration(800);
        anim5.setDuration(800);
        lin1.startAnimation(anim1);

        //anim1
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lin1.setVisibility(View.VISIBLE);
                lin2.startAnimation(anim2);
                //lin2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //anim2
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lin2.setVisibility(View.VISIBLE);
                lin3.startAnimation(anim3);
                //lin3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //anim3
        anim3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lin3.setVisibility(View.VISIBLE);
                lin4.startAnimation(anim4);
                //lin4.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //anim4
        anim4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lin4.setVisibility(View.VISIBLE);
                lin5.startAnimation(anim5);
                lin5.setVisibility(View.VISIBLE);

                setvalue();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void setvalue() {
        //angry
        ValueAnimator animator1 = ValueAnimator.ofInt(0, angryvalue);
        animator1.setDuration(1000);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress1.setProgress((int) animation.getAnimatedValue());
            }
        });
        animator1.start();

        //sad
        ValueAnimator animator2 = ValueAnimator.ofInt(0, sadvalue);
        animator2.setDuration(1000);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress2.setProgress((int) animation.getAnimatedValue());
            }
        });
        animator2.start();

        //anxious
        ValueAnimator animator3 = ValueAnimator.ofInt(0, anxiousvalue);
        animator3.setDuration(1000);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress3.setProgress((int) animation.getAnimatedValue());
            }
        });
        animator3.start();

        //depressed
        ValueAnimator animator4 = ValueAnimator.ofInt(0, depressedvalue);
        animator4.setDuration(1000);
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress4.setProgress((int) animation.getAnimatedValue());
            }
        });
        animator4.start();


        //scared
        ValueAnimator animator5 = ValueAnimator.ofInt(0, scaredvalue);
        animator5.setDuration(1000);
        animator5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress5.setProgress((int) animation.getAnimatedValue());
            }
        });
        animator5.start();

    }


    public void tohmoe(View view) {
        Intent intent=new Intent(information.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
