package com.zhaohe.zhundao.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteOpenHelperDao extends SQLiteOpenHelper{
	public static String     dbName="zhundao";
	private static CursorFactory factory=null;
	 private static int           version = 1;
	 public SQLiteOpenHelperDao(Context context){
		 super(context,dbName,factory,version);
	 }
	 public void onCreate(SQLiteDatabase db){
		 db.execSQL ("CREATE TABLE IF NOT EXISTS MySignupList(id integer primary key autoincrement ,VCode varchar(20) NOT NULL, CheckInID varchar(20)  , Status varchar(20) , UpdateStatus varchar(20));");

	 }
	 
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
