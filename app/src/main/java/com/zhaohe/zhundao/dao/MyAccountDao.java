package com.zhaohe.zhundao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.bean.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:通讯录数据库
 * @Author:邹苏隆
 * @Since:2017/5/22 14:ic_launcher
 */
public class MyAccountDao {
    private SQLiteOpenHelperDao dbOpenHelper;
    private final String TABLE_NAME = "MyAccount";
    private Context mContext;

    public MyAccountDao(Context context) {
        this.dbOpenHelper = new SQLiteOpenHelperDao(context);
        mContext = context;
    }

    public void save(List<AccountBean> list) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction();// 开启事务
        try {
            for (int i = 0; i < list.size(); i++) {
                ContentValues values = new ContentValues();
                AccountBean bean = list.get(i);
                setContentValues(values, bean);

                db.replace(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();// 设置事务的标志为True
        } finally {
            db.endTransaction();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
    }

    public void save(AccountBean bean) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        db.beginTransaction();// 开启事务
//        try {
        ContentValues values = new ContentValues();

        setContentValues(values, bean);

        db.replace(TABLE_NAME, null, values);
//            }
//            db.setTransactionSuccessful();// 设置事务的标志为True
//        } finally {
//            db.endTransaction();// 结束事务,有两种情况：commit,rollback,
        db.close();
    }

    public void deleteTable() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        db.execSQL("delete from " + TABLE_NAME);
        db.close();
    }

    public void deleteGroupByID(String phone) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        db.delete(TABLE_NAME, "phone=?", new String[]{phone});
        db.close();
        ToastUtil.makeText(mContext, "删除成功！");

    }

    public List<AccountBean> queryAll() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<AccountBean> list = new ArrayList<AccountBean>();
        try {
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "status DESC");
            while (cursor.moveToNext()) {
                AccountBean bean = new AccountBean();
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

    public void queryListStatus(String Status) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where Status =?", new String[]{Status});
            while (cursor.moveToNext()) {
                AccountBean bean = new AccountBean();
                bean.setStatus("false");
                setBean(cursor, bean);
                ContentValues values = new ContentValues();

                setContentValues(values, bean);

                db.replace(TABLE_NAME, null, values);
            }
            cursor.close();
            // db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
            // db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }

    }


    private void setContentValues(ContentValues values,
                                  AccountBean bean) {
        values.put("name", bean.getName());
        values.put("phone", bean.getPhone());
        values.put("head", bean.getHead());
        values.put("accessKey", bean.getAccessKey());
        values.put("status", bean.getStatus());


    }

    private void setBean(Cursor cursor, AccountBean bean) {

        String name = cursor.getString(cursor.getColumnIndex("name"));
        String head = cursor.getString(cursor.getColumnIndex("head"));
        String phone = cursor.getString(cursor.getColumnIndex("phone"));
        String accessKey = cursor.getString(cursor.getColumnIndex("accessKey"));
        String status = cursor.getString(cursor.getColumnIndex("status"));


        bean.setName(name);
        bean.setPhone(phone);
        bean.setHead(head);
        bean.setAccessKey(accessKey);
        bean.setStatus(status);
    }
}
