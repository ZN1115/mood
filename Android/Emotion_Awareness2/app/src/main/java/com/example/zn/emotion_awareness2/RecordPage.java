package com.example.zn.emotion_awareness2;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class RecordPage extends AppCompatActivity implements testDialog.testDialogListener {

    private static final String TAG = RecordPage.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_page);
    }

    public void ToRecord(View view) {
        //Toast.makeText(RecordPage.this,"okok",Toast.LENGTH_LONG).show();
        testDialog dialog = testDialog.instance();
        dialog.show(getSupportFragmentManager(), "MySelfDialog");
    }

    //當dialog點叉叉後的事件
    @Override
    public void onDialogCancelClick(DialogFragment dialog) {
        dialog.dismiss();
        logStr("onDialogCancelClick");
    }

    private void logStr(String str) {
        Log.d(TAG, str);
    }
}
