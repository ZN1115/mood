package com.example.zn.emotion_awareness2;


import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
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

public class testDialog extends DialogFragment {


    private static testDialog dialog;
    private Button bt_exit;
    private Button bt_radio;//radio圖片
    private testDialogListener listener;//監聽
    private TextView tv_question;
    private Button bt_nextpage;

    private Button bt_circle;
    private Button bt_rectangle;
    private Boolean statusRecord=false;
    private Boolean moving=false;

    private AudioRecord audioRecord;
    private static final String TAG = "jqd";
    private boolean isRecording;

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

    private String[] text=new String[]{
            "今天過的好嗎?",
            "最近遇到什麼事嗎?",
            "最在意或困擾的事?"
    };

    public static testDialog instance() {
        if (dialog == null) {
            synchronized (testDialog.class) {
                if (dialog == null) {
                    dialog = new testDialog();
                }
            }
        }
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_test_dialog, container, false);
        bt_exit=(Button)view.findViewById(R.id.exit);
        bt_radio=(Button)view.findViewById(R.id.radio_off);
        tv_question=(TextView)view.findViewById(R.id.tv_question);

        bt_circle=(Button)view.findViewById(R.id.button2);
        bt_rectangle=(Button)view.findViewById(R.id.button3);
        bt_nextpage=(Button)view.findViewById(R.id.btn_nextpage);

        //點了叉叉的事件
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDialogCancelClick(testDialog.this);
                }
            }
        });
        
        //按下紅色圓形按鈕的事件
        bt_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bt_nextpage.setVisibility(View.INVISIBLE);

                if(moving==true){
                }
                else {
                    if(permission_write==true&&permission_record==true){
                        circleAnim();
                        radioAnim();
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
        bt_rectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moving==true){
                    Log.i("moving", String.valueOf(moving));
                }
                else {
                    Log.i("moving", String.valueOf(moving));
                    rectangleAnim();
                    if(statusRecord==true){
                        Log.i("statusRecord", String.valueOf(statusRecord));
                        stopRecord();
                        statusRecord=false;
                        bt_radio.setBackground(getResources().getDrawable(R.drawable.radio_off));

                        final Context c=getActivity();
                        String path;
                        path=c.getExternalFilesDir(Environment.DIRECTORY_MUSIC)+"/test.pcm";
                        Log.i("filepath",path);
                        if(fileIsExists(path)==true){
                            Log.i("file","檔案存在");
                            bt_nextpage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PcmToWav(c);
                                    Toast.makeText(getActivity(),"資料上傳中...",Toast.LENGTH_LONG).show();
//                                    Handler handler = new Handler();
//                                    handler.postDelayed(new Runnable(){
//
//                                        @Override
//                                        public void run() {
//
//                                            //過兩秒後要做的事情
//                                            Log.d("tag","我要打掃了");
//                                            Intent getresultintent=new Intent(getActivity(),GetResult2.class);
//                                            startActivity(getresultintent);
//                                            dismiss();
//
//                                        }}, 2000);

                                }
                            });
                            final Handler samHandler=new Handler();
                            samHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bt_nextpage.setVisibility(View.VISIBLE);

                                }
                            },800);


                        }
                        else{
                            Log.i("file","檔案不存在");
                        }
                    }
                }
            }
        });
        setText();
        return  view;
    }

    //pcm轉wav
    private void PcmToWav(Context c) {
        PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        File pcmFile = new File(c.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");
        File wavFile = new File(c.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.wav");
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
        final Context c=getContext();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                File f  = new File(c.getExternalFilesDir(Environment.DIRECTORY_MUSIC),"test.wav");
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
                    postToserver();//發命令給server
                    //Toast.makeText(getActivity(),"資料上傳中...",Toast.LENGTH_LONG).show();
                    //Intent getresultintent=new Intent(getActivity(),GetResult2.class);
                    //startActivity(getresultintent);
                    //dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        t.start();
    }


    private void postToserver() {
        String url = "http://192.168.137.1/FileUpload/test.php";
        final HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("data","promood");
        Log.i("postToserver","promood ok");
        UrlHttpUtil.post(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {
                Log.i("testdailog post","fail");//這邊會死

            }

            @Override
            public void onResponse(String response) {
                Log.i("testdailog post","success");
                getData();//檢查預處理做完了沒
            }
        });
    }

    private void getData()
    {
        String url = "http://192.168.137.1/FileUpload/images/ok.txt";
        UrlHttpUtil.get(url, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {
                Log.i("check okfile","ok.txt fail");
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
                //Toast.makeText(getResult.this,"Success",Toast.LENGTH_SHORT).show();
                Log.i("check okfile","ok.txt success");
                Log.d("kwwl",response);
                Intent getresultintent=new Intent(getActivity(),GetResult2.class);
                startActivity(getresultintent);
                dismiss();
            }
        });
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    //判断文件是否存在
    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
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
            //recordingThread = null;
        }
    }

    //開始錄音的紅色方形動畫
    private void rectangleAnim() {

        ScaleAnimation anim=new ScaleAnimation(0.7f,1f,0.7f,1f,bt_circle.getWidth()/2f,bt_circle.getWidth()/2f);
        anim.setDuration(500);

        bt_circle.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                moving=true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bt_rectangle.setVisibility(View.INVISIBLE);//方形隱藏
                bt_circle.setVisibility(View.VISIBLE);//圓形顯示
                statusRecord=false;
                moving=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //設定開頭問句
    private void setText() {
        int random=(int)(Math.random()*3);
        Animation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(2500);
        tv_question.startAnimation(animation);
        tv_question.setText(text[random]);
    }

    private void checkPermissions() {
        Log.i("check","permission");
        Log.i("check", String.valueOf(permission_record));
        Log.i("check", String.valueOf(permission_write));

        // Marshmallow开始才用申请运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("check","Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(getActivity(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()) {
                Log.i("check","isEmpty()");
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                for (int i=0;i<permissions.length;i++){
                    int state = ContextCompat.checkSelfPermission(getActivity(), permissions[i]);
                    if (state != PackageManager.PERMISSION_GRANTED) { // 判断权限的状态
                        requestPermissions(permissions, 200); // 申请权限
                        return;
                    }
                }
            }
            else{//當權限都有之後
                circleAnim();
                radioAnim();
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
                Toast.makeText(getActivity(), "錄音權限未開啟", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.i("check","permission_record=true");
                permission_record=true;

            }

            if (grantResults[1] != PackageManager.PERMISSION_GRANTED) { // 用户点的拒绝，仍未拥有权限
                Toast.makeText(getActivity(), "儲存權限未開啟", Toast.LENGTH_SHORT).show();
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

    private void startRecord() {
        final int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        Log.i("record","recordStart");
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_INHZ,
                CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);

        final byte data[] = new byte[minBufferSize];
        Context c=getActivity();
        final File file = new File(c.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");
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

    private void radioAnim() {
        bt_radio.setBackgroundResource(R.drawable.flag);
        final AnimationDrawable frameAnimation=(AnimationDrawable)bt_radio.getBackground();
        bt_radio.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
    }

    //開始錄音的紅色圓形動畫
    private void circleAnim() {
        ScaleAnimation anim2=new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,bt_circle.getWidth()/2f,bt_circle.getWidth()/2f);
        anim2.setDuration(800);
        bt_circle.startAnimation(anim2);
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bt_rectangle.setVisibility(View.VISIBLE);//方形顯示
                moving=true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bt_circle.setVisibility(View.INVISIBLE);//圓形隱藏
                moving=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //設定dialog版面的
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        int width = getResources().getDisplayMetrics().widthPixels;
        params.width=width;
        Log.i("params: ", String.valueOf(params.width));
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (testDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    public interface testDialogListener {

        void onDialogCancelClick(DialogFragment dialog);
    }
}
