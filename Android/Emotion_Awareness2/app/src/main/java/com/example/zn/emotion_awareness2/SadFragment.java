package com.example.zn.emotion_awareness2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class SadFragment extends Fragment{

    RoundCornerProgressBar progress;
    mooddatabaseHelper mooddatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Context c=getContext();
        mooddatabaseHelper=new mooddatabaseHelper(c);

        View view=inflater.inflate(R.layout.fragment_sad,container,false);
        //AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);

        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Cursor res=mooddatabaseHelper.getAllData();
        StringBuffer buffer=new StringBuffer();
        List<DataEntry> data = new ArrayList<>();
        while (res.moveToNext()){

            Log.i("res.moveToNext()", res.getString(2));
            Log.i("res.moveToNext()", res.getString(7));

            float f=Float.parseFloat(res.getString(2))*100;
            data.add(new ValueDataEntry(res.getString(7), f));


        }
        Cartesian cartesian = AnyChart.column();


        Column column = cartesian.column(data);
        column.color("#fdf5d1");
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d);

        cartesian.animation(true);
        cartesian.title("Sad Chart");

        cartesian.yScale().minimum(0d);
        cartesian.yScale().maximum(100d);


        anyChartView.setChart(cartesian);


        return view;
    }
}
