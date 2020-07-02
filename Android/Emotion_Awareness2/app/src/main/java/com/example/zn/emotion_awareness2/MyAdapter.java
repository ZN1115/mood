package com.example.zn.emotion_awareness2;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    List<List<String>>  data;
    private LayoutInflater layoutInflater;
    RoundCornerProgressBar progress;

    MyAdapter(Context context, List data)
    {
        this.data=data;
        layoutInflater = LayoutInflater.from(context);
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RoundCornerProgressBar progress;
        ItemHolder(View View)
        {
            super(View);
            textView = View.findViewById(R.id.textview);
            progress=(RoundCornerProgressBar)View.findViewById(R.id.progressbar);
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item/*這邊是指item的xml檔*/,viewGroup/*你要先幫我挖一塊之後會讓我放*/,false/*是指部要顯示*/);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ItemHolder)viewHolder)/*這邊把值設定進來*/.textView.setText(data.get(i).get(1));
        float f=Float.parseFloat(data.get(i).get(0))*100;
        ((ItemHolder)viewHolder)/*這邊把值設定進來*/.progress.setProgress(f);


//        ValueAnimator animator1 = ValueAnimator.ofInt(0,(int)f);
//        animator1.setDuration(1000);
//        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                progress.setProgress((int) animation.getAnimatedValue());
//            }
//        });
//        animator1.start();
        float getSecondaryProgress=100;
//        setSecondaryProgress((int) getSecondaryProgress);
//
//        ValueAnimator animator2 = ValueAnimator.ofInt(100, 100-(int)f);
//        animator2.setDuration(1000);
//        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                progress.setSecondaryProgress((int) animation.getAnimatedValue());
//            }
//        });
//        animator2.start();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
