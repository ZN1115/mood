package com.example.zn.emotion_awareness2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zn.emotion_awareness2.urlhttp.CallBackUtil;
import com.example.zn.emotion_awareness2.urlhttp.UrlHttpUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.zn.emotion_awareness2.GlobalConfig.AUDIO_FORMAT;
import static com.example.zn.emotion_awareness2.GlobalConfig.CHANNEL_CONFIG;
import static com.example.zn.emotion_awareness2.GlobalConfig.SAMPLE_RATE_INHZ;

public class RecordNormal extends AppCompatActivity {

    private String string="為提升學子們的道德標準\n由連叔叔領銜之連署書\n提倡每位十歲至十四歲之學子\n皆需精讀四書。";
    private String string2="保育魚種專家黃昂發\n在濁水溪一帶發現\n稀有魚種藍鯉魚與綠鱸魚\n是台灣特有種。";
    private String string3="我選擇看什麼\n我的世界裡就有什麼。";


    private Integer FileSum=0;

    private ImageView img_recordpeople;
    private TextView tv_introduction;
    private TextView tv_introduction2;
    private TextView tv_introduction3;
    private TextView tv_introduction4;
    private TextView tv_introduction5;
    private TextView tv_introduction6_title;
    private Button btn_startrecord;
    private Button btn_circle;
    private Button btn_rectangle;
    private Button btn_next;
    private RelativeLayout rela_record;

    private AudioRecord audioRecord;
    private static final String TAG = "jqd";
    private boolean isRecording;

    private boolean moving=false;
    private boolean statusRecord=false;
    private boolean permission_record;
    private boolean permission_write;
    private List<String> mPermissionList = new ArrayList<>();
    /**
     * 需要申请的运行时权限
     */
    private String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_normal);

        img_recordpeople=(ImageView)findViewById(R.id.img_recordpeople);
        tv_introduction=(TextView)findViewById(R.id.tv_introduction);
        tv_introduction2=(TextView)findViewById(R.id.tv_introduction2);
        tv_introduction3=(TextView)findViewById(R.id.tv_introduction3);
        tv_introduction4=(TextView)findViewById(R.id.tv_introduction4);
        tv_introduction5=(TextView)findViewById(R.id.tv_introduction5);
        tv_introduction6_title=(TextView)findViewById(R.id.tv_introduction6_title);
        btn_startrecord=(Button)findViewById(R.id.btn_startrecord);
        btn_circle=(Button)findViewById(R.id.button2);
        btn_rectangle=(Button)findViewById(R.id.button3);
        btn_next=(Button)findViewById(R.id.btn_next);
        rela_record=(RelativeLayout)findViewById(R.id.rela_record);


        btn_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moving==true){
                }
                else {
                    if(permission_write==true&&permission_record==true){
                        Log.i("btn_circle","ok");
                        circleAnim();
                        //pepleAnim();
                        startRecord();
                        statusRecord=true;
                    }
                    else{
                        checkPermissions();
                    }
                }
            }
        });


        //按下紅色方形按鈕的事件
        btn_rectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moving==true){
                }
                else {
                    rectangleAnim();
                    if(statusRecord==true){
                        stopRecord();
                        statusRecord=false;
                    }
                }
            }
        });
    }

    private void pepleAnim() {
        Log.i("peopleanim","ok");
        img_recordpeople.setBackgroundResource(R.drawable.flagpeople);
        final AnimationDrawable frameAnimation=(AnimationDrawable)img_recordpeople.getBackground();
        img_recordpeople.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
    }

    public void StartRecord_Normal(View view) {
        tv_introduction.setVisibility(View.INVISIBLE);
        tv_introduction2.setVisibility(View.INVISIBLE);
        Animation a= AnimationUtils.loadAnimation(this,R.anim.trans);
        tv_introduction3.setText(string);
        tv_introduction3.startAnimation(a);
        tv_introduction6_title.setVisibility(View.VISIBLE);
        tv_introduction6_title.startAnimation(a);
        btn_startrecord.setVisibility(View.INVISIBLE);
        rela_record.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);
        img_recordpeople.startAnimation(alphaAnimation);
    }

    //開始錄音的紅色圓形動畫
    private void circleAnim() {
        ScaleAnimation anim2=new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,btn_circle.getWidth()/2f,btn_circle.getWidth()/2f);
        anim2.setDuration(800);
        btn_circle.startAnimation(anim2);
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btn_rectangle.setVisibility(View.VISIBLE);//方形顯示
                btn_next.setVisibility(View.INVISIBLE);
                moving=true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn_circle.setVisibility(View.INVISIBLE);//圓形隱藏
                moving=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //開始錄音的紅色方形動畫
    private void rectangleAnim() {
        ScaleAnimation anim=new ScaleAnimation(0.7f,1f,0.7f,1f,btn_circle.getWidth()/2f,btn_circle.getWidth()/2f);
        anim.setDuration(500);

        btn_circle.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                moving=true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn_rectangle.setVisibility(View.INVISIBLE);//方形隱藏
                btn_circle.setVisibility(View.VISIBLE);//圓形顯示
                btn_next.setVisibility(View.VISIBLE);
                //statusRecord=false;
                moving=false;
                img_recordpeople.setBackground(getResources().getDrawable(R.drawable.normal_people_smallear));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    //檢查權限
    private void checkPermissions() {

        Log.i("check","permission");
        Log.i("check", String.valueOf(permission_record));
        Log.i("check", String.valueOf(permission_write));

        // Marshmallow开始才用申请运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("check","Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()) {
                Log.i("check","isEmpty()");
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                for (int i=0;i<permissions.length;i++){
                    int state = ContextCompat.checkSelfPermission(this, permissions[i]);
                    if (state != PackageManager.PERMISSION_GRANTED) { // 判断权限的状态
                        requestPermissions(permissions, 200); // 申请权限
                        return;
                    }
                }
            }
            else{//當權限都有之後
                circleAnim();
                startRecord();
                statusRecord=true;
            }
        }
        Log.i("check","checkPermissions");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("check","onRequestPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == 200) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) { // 用户点的拒绝，仍未拥有权限
                Toast.makeText(this, "錄音權限未開啟", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.i("check","permission_record=true");
                permission_record=true;

            }

            if (grantResults[1] != PackageManager.PERMISSION_GRANTED) { // 用户点的拒绝，仍未拥有权限
                Toast.makeText(this, "儲存權限未開啟", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.i("check","permission_write=true");
                permission_write=true;
            }

        }
        else {
            checkPermissions();
        }
    }

    public void NextText(View view) {
        FileSum++;
        Animation a= AnimationUtils.loadAnimation(this,R.anim.trans);//字退出的動畫
        Animation aa= AnimationUtils.loadAnimation(this,R.anim.trans2);//字進來的動畫
        switch (FileSum){
            case 1:
                tv_introduction3.startAnimation(aa);
                btn_next.setVisibility(View.INVISIBLE);

                tv_introduction4.setText(string2);
                tv_introduction4.startAnimation(a);
                PcmToWav(FileSum);
                break;
            case 2:
                btn_next.setVisibility(View.INVISIBLE);
                tv_introduction4.startAnimation(aa);
                tv_introduction5.setText(string3);
                tv_introduction5.startAnimation(a);
                PcmToWav(FileSum);
                break;
            case 3:
                //Toast.makeText(this,"其他",Toast.LENGTH_LONG).show();
                PcmToWav(FileSum);
                break;
        }
    }
    //開始錄音
    public void startRecord() {
        normalAnim();
        final int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        Log.i("record","recordStart");
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_INHZ,
                CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);

        final byte data[] = new byte[minBufferSize];
        //Context c=getActivity();
        String filename =null;
        switch (FileSum){
            case 0:
                filename="normal1.pcm";
                break;
            case 1:
                filename="normal2.pcm";
                break;
            case 2:
                filename="normal3.pcm";
                break;
        }
        final File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC), filename);
        //Toast.makeText(c, "Current Dir: "+c.getExternalFilesDir(Environment.DIRECTORY_MUSIC)+": "+file.getName(), Toast.LENGTH_SHORT).show();
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        if (file.exists()) {
            file.delete();
        }

        audioRecord.startRecording();
        isRecording = true;

        // TODO: 2018/3/10 pcm数据无法直接播放，保存为WAV格式。

        new Thread(new Runnable() {
            @Override
            public void run() {

                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (null != os) {
                    while (isRecording) {
                        int read = audioRecord.read(data, 0, minBufferSize);
                        // 如果读取音频数据没有出现错误，就将数据写入到文件
                        if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                            try {
                                os.write(data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Log.i(TAG, "run: close file output stream !");
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void normalAnim() {
        img_recordpeople.setBackgroundResource(R.drawable.flagnormal);
        final AnimationDrawable frameAnimation=(AnimationDrawable)img_recordpeople.getBackground();
        img_recordpeople.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
    }

    //停止錄音
    public void stopRecord() {
        Log.i("record","recordStop");
        isRecording = false;
        // 释放资源
        if (null != audioRecord) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    //pcm轉wav
    private void PcmToWav(Integer a) {
        PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        String filename=null;String filename2 =null;
        switch (a){
            case 1:
                filename="normal1.pcm";
                filename2="normal1.wav";
                break;
            case 2:
                filename="normal2.pcm";
                filename2="normal2.wav";
                break;
            case 3:
                filename="normal3.pcm";
                filename2="normal3.wav";
                break;
        }
        File pcmFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC), filename);
        File wavFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC), filename2);
        if (!wavFile.mkdirs()) {
            Log.e(TAG, "wavFile Directory not created");
        }
        if (wavFile.exists()) {
            wavFile.delete();
        }
        pcmToWavUtil.pcmToWav(pcmFile.getAbsolutePath(), wavFile.getAbsolutePath());
        Log.i("file","pcmtowav");
        UploadToServer();
    }

    private void UploadToServer() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String filename=null;
                switch (FileSum){
                    case 1:
                        filename="normal1.wav";
                        break;
                    case 2:
                        filename="normal2.wav";
                        break;
                    case 3:
                        filename="normal3.wav";
                        break;

                }
                File f  = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),filename);
                String content_type  = getMimeType(f.getPath());
                String file_path = f.getAbsolutePath();
                OkHttpClient client = new OkHttpClient();

                RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);
                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type",content_type)
                        .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                        .build();

                Request request = new Request.Builder()
                        .url("http://192.168.137.1/FileUpload/SaveFile.php")
                        .post(request_body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    if(!response.isSuccessful()){
                        throw new IOException("Error : "+response);
                    }
                    if(FileSum.equals(3))
                    {
                        postData();
                        Intent getresultintent=new Intent(RecordNormal.this,GetResult.class);
                        startActivity(getresultintent);
                        finish();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        t.start();

    }
    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    private void postData() {
        String url = "http://192.168.137.1/FileUpload/test.php";
        final HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("data","pro");
        UrlHttpUtil.post(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {
                Log.i("recordnormal pro:","fail");

            }

            @Override
            public void onResponse(String response) {
                //Toast.makeText(RecordNormal.this,"上傳成功",Toast.LENGTH_SHORT).show();
                Log.i("recordnormal pro: ","success");
                //check();
            }
        });
    }

    private void check() {
        String url = "http://192.168.137.1/FileUpload/images/a.txt";
        Log.i("check upload","check");

        UrlHttpUtil.get(url, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {
                Log.i("check upload","fail");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){

                    @Override
                    public void run() {

                        //過兩秒後要做的事情
                        Log.d("tag","我要打掃了");
                        check();

                    }}, 3000);

            }

            @Override
            public void onResponse(String response) {
                Log.i("check reupload","success");
                Intent getresultintent=new Intent(RecordNormal.this,GetResult.class);
                startActivity(getresultintent);
                finish();
            }
        });
    }
}
