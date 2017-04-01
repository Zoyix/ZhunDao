package com.zhaohe.zhundao.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteOpenHelperDao extends SQLiteOpenHelper {
    public static String dbName = "zhundao";
    private static CursorFactory factory = null;
    private static int version = 3;

    public SQLiteOpenHelperDao(Context context) {
        super(context, dbName, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS MySignupList(SignTime varchar(20),VCode varchar(20) , CheckInID varchar(20)  , Status varchar(20) , UpdateStatus varchar(20),Name varchar(20),Phone varchar(20),AdminRemark varchar(20), FeeName varchar(20),Fee varchar(20),primary key(VCode,CheckInID));");
        db.execSQL("CREATE TABLE IF NOT EXISTS MySignList(sign_list_id varchar(20) , sign_list_name varchar(20)  , sign_list_time varchar(20) , sign_list_phone varchar(20),sign_list_status varchar(20),act_id varchar(20),mIndex varchar(20),nickname varchar(20)      ,primary key(act_id,sign_list_phone));");
        db.execSQL ("CREATE TABLE IF NOT EXISTS MySignupListMulti(VCode varchar(20) , CheckInID varchar(20), Status varchar(20) , UpdateStatus varchar(20),Name varchar(20),Phone varchar(20),AdminRemark varchar(20), FeeName varchar(20),Fee varchar(20) ,primary key(VCode,CheckInID) );");

    }


    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        arg0.execSQL("DROP TABLE MySignupList");
//        arg0.execSQL("DROP TABLE MySignList");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS MySignupList(SignTime varchar(20),VCode varchar(20) , CheckInID varchar(20)  , Status varchar(20) , UpdateStatus varchar(20),Name varchar(20),Phone varchar(20),AdminRemark varchar(20), FeeName varchar(20),Fee varchar(20),primary key(VCode,CheckInID));");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS MySignList(sign_list_id varchar(20) , sign_list_name varchar(20)  , sign_list_time varchar(20) , sign_list_phone varchar(20),sign_list_status varchar(20),act_id varchar(20),mIndex varchar(20),nickname varchar(20)      ,primary key(act_id,sign_list_phone));");
        arg0.execSQL ("CREATE TABLE IF NOT EXISTS MySignupListMulti(VCode varchar(20) , CheckInID varchar(20), Status varchar(20) , UpdateStatus varchar(20),Name varchar(20),Phone varchar(20),AdminRemark varchar(20), FeeName varchar(20),Fee varchar(20) ,primary key(VCode,CheckInID) );");


    }

}
