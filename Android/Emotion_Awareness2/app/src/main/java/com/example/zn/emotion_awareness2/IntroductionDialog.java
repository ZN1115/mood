package com.example.zn.emotion_awareness2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class IntroductionDialog extends DialogFragment {


    private static IntroductionDialog dialog;
    private Button btn_recordpage;
    private TextView tv_tro;
    private IntroductionDialog listen;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.x=30;
        p.y=-250;
        getDialog().getWindow().setAttributes(p);
        View view = inflater.inflate(R.layout.activity_introduction_dialog, container, false);
        final Context c=getActivity();
        ((Button) view.findViewById(R.id.ToRecordPage)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                Intent intent=new Intent(c,RecordPage.class);
                startActivity(intent);
                dismiss();
            }
        });
        tv_tro=((TextView)view.findViewById(R.id.tv_intro));
        tv_tro.setText("介紹:\n透過語料庫分析、心理諮商師的經驗，\n計算使用者說話時的負面情緒分數。\n\n如何使用:\n點擊人物嘴巴即可開始錄製情緒語音。");
        return view;
    }



}
