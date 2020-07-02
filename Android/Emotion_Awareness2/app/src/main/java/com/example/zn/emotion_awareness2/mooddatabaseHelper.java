package com.example.zn.emotion_awareness2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mooddatabaseHelper extends SQLiteOpenHelper {

    public static  final String DATABASE_NAME= "mood.db";
    public static  final String TABLE_NAME= "mood_table";
    public static  final String COL_1="ID";
    public static  final String COL_2="ANGRY";
    public static  final String COL_3="SAD";
    public static  final String COL_4="ANXIOUS";
    public static  final String COL_5="DEPRESSED";
    public static  final String COL_6="SCARED";
    public static  final String COL_7="SCORE";
    public static  final String COL_8="date";


    public mooddatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,ANGRY TEXT,SAD TEXT ,ANXIOUS TEXT ,DEPRESSED TEXT,SCARED TEXT,SCORE TEXT,DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String ANGRY, String SAD, String ANXIOUS, String DEPRESSED, String SCARED, String SCORE, String DATE){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,ANGRY);
        contentValues.put(COL_3,SAD);
        contentValues.put(COL_4,ANXIOUS);
        contentValues.put(COL_5,DEPRESSED);
        contentValues.put(COL_6,SCARED);
        contentValues.put(COL_7,SCORE);
        contentValues.put(COL_8,DATE);
        Log.i("mooddatabase",ANGRY);
        Log.i("mooddatabase",SAD);
        Log.i("mooddatabase",ANXIOUS);
        Log.i("mooddatabase",DEPRESSED);
        Log.i("mooddatabase",SCARED);
        Log.i("mooddatabase",SCORE);
        Log.i("mooddatabase",DATE);

        long reslut = db.insert(TABLE_NAME,null,contentValues);

        if(reslut==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);
        return  res;
    }

}
