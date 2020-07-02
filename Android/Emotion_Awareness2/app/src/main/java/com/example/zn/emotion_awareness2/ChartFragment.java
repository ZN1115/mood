package com.example.zn.emotion_awareness2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.Position;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    mooddatabaseHelper mooddatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Context c=getContext();
        mooddatabaseHelper=new mooddatabaseHelper(c);

        View view=inflater.inflate(R.layout.fragment_chart,container,false);
        BottomNavigationView bottomNavigationView=view.findViewById(R.id.mood);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_chart,new AngryFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(Listener);

        recyclerView=view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        List<List<String>> aList = new ArrayList<List<String>>();
        Cursor res=mooddatabaseHelper.getAllData();

        while(res.moveToNext()){
            List<String> aListData = new ArrayList<String>();
            aListData.add(res.getString(6));
            aListData.add(res.getString(7));
            //Log.i("res.moveToNext()", res.getString(6));
            //Log.i("res.moveToNext()", res.getString(7));

            aList.add(aListData);
        }

        myAdapter=new MyAdapter(c,aList);
        recyclerView.setAdapter(myAdapter);

        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener Listener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selected=null;
            switch (menuItem.getItemId()){
                case R.id.nav_angry:
                    selected=new AngryFragment();
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_chart,selected).commit();
                    break;
                case R.id.nav_sad:
                    selected=new SadFragment();
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_chart,selected).commit();
                    break;
                case R.id.nav_anxious:
                    selected=new AnxiousFragment();
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_chart,selected).commit();
                    break;
                case R.id.nav_scared:
                    selected=new ScaredFragment();
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_chart,selected).commit();
                    break;
                case R.id.nav_depressed:
                    selected=new DepressedFragment();
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_chart,selected).commit();
                    break;
            }


            return true;
        }
    };
}
