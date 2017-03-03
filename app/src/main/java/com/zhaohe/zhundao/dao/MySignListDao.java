package com.zhaohe.zhundao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhaohe.zhundao.bean.SignListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:存放报名用户名单
 * @Author:邹苏隆
 * @Since:2017/2/27 16:16
 */
public class MySignListDao {
    private SQLiteOpenHelperDao dbOpenHelper;
    private final String TABLE_NAME = "MySignList";

    public MySignListDao(Context context) {
        this.dbOpenHelper = new SQLiteOpenHelperDao(context);
    }

    public long save(SignListBean bean) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        // db.beginTransaction ();// 开启事务
        try {
            ContentValues values = new ContentValues();
            setContentValues(values, bean);
            return db.replace(TABLE_NAME, null, values);
            // db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
            // db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
    }
    public void save(List<SignListBean> list) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
         db.beginTransaction ();// 开启事务
        try {
            for(int i=0;i<list.size();i++){
                ContentValues values = new ContentValues();
                SignListBean bean=list.get(i);
            setContentValues(values, bean);

             db.replace(TABLE_NAME, null, values);}
             db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
             db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
    }


    private void setContentValues(ContentValues values,
                                 SignListBean bean) {
        values.put("sign_list_id", bean.getSign_list_id());
        values.put("sign_list_name", bean.getSign_list_name());
        values.put("sign_list_time", bean.getSign_list_time());
        values.put("sign_list_status", bean.getSign_list_status());
        values.put("sign_list_phone", bean.getSign_list_phone());
        values.put("mIndex", bean.getmIndex());
        values.put("act_id", bean.getAct_id());
        values.put("nickname", bean.getNickname());



    }

    public List<SignListBean> queryAll() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<SignListBean> list = new ArrayList<SignListBean>();
        try {
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                SignListBean bean = new SignListBean();
                setBean(cursor, bean);
                list.add(bean);
            }
            cursor.close();
            // db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
            // db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
        return list;
    }


    public List<SignListBean> queryListActID(String act_id) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<SignListBean> list = new ArrayList<SignListBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where act_id = ? "+"ORDER BY sign_list_time DESC", new String[]{act_id});
            while (cursor.moveToNext()) {
                SignListBean bean = new SignListBean();
                setBean(cursor, bean);
                list.add(bean);
            }
            cursor.close();
            // db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
            // db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
        return list;
    }
    public List<SignListBean> queryListActIDAndPhoneOrName(String act_id,String param) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<SignListBean> list = new ArrayList<SignListBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where act_id = ? and (sign_list_name||nickname||sign_list_phone) like ?", new String[]{act_id,"%"+param+"%"});
            while (cursor.moveToNext()) {
                SignListBean bean = new SignListBean();
                setBean(cursor, bean);
                list.add(bean);
            }
            cursor.close();
            // db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
            // db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
        return list;
    }







    private void setBean(Cursor cursor, SignListBean bean) {

        String sign_list_id = cursor.getString(cursor.getColumnIndex("sign_list_id"));
        String sign_list_name = cursor.getString(cursor.getColumnIndex("sign_list_name"));
        String sign_list_time = cursor.getString(cursor.getColumnIndex("sign_list_time"));
        String sign_list_phone = cursor.getString(cursor.getColumnIndex("sign_list_phone"));
        String sign_list_status = cursor.getString(cursor.getColumnIndex("sign_list_status"));
        String act_id = cursor.getString(cursor.getColumnIndex("act_id"));
        int mIndex = cursor.getInt(cursor.getColumnIndex("mIndex"));
        String nickname=cursor.getString(cursor.getColumnIndex("nickname"));
        bean.setSign_list_id(sign_list_id);
        bean.setSign_list_name(sign_list_name);
        bean.setSign_list_phone(sign_list_phone);
        bean.setSign_list_status(sign_list_status);
        bean.setSign_list_time(sign_list_time);
        bean.setAct_id(act_id);
        bean.setmIndex(mIndex);
        bean.setNickname(nickname);

    }

}
