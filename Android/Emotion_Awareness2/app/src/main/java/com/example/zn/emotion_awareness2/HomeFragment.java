package com.example.zn.emotion_awareness2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.zn.emotion_awareness2.urlhttp.CallBackUtil;
import com.example.zn.emotion_awareness2.urlhttp.UrlHttpUtil;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    Button btn_test;
    private static final String TAG = HomeFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home,container,false);

        btn_test=(Button)view.findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(view);
            }
        });
        return view;

    }

    public void showEditDialog(View view)
    {
        IntroductionDialog editNameDialog = new IntroductionDialog();
        editNameDialog.show(getFragmentManager(), "EditNameDialog");
    }

}
