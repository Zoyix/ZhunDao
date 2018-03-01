package com.zhaohe.zhundao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhaohe.zhundao.bean.dao.MyGroupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:群组数据库
 * @Author:邹苏隆
 * @Since:2017/5/23 14:11
 */
public class MyGroupDao {
    private SQLiteOpenHelperDao dbOpenHelper;
    private final String TABLE_NAME = "MyGroup";

    public MyGroupDao(Context context) {
        this.dbOpenHelper = new SQLiteOpenHelperDao(context);
    }

    public void save(List<MyGroupBean> list) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction();// 开启事务
        try {
            for (int i = 0; i < list.size(); i++) {
                ContentValues values = new ContentValues();
                MyGroupBean bean = list.get(i);
                setContentValues(values, bean);

                db.replace(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();// 设置事务的标志为True
        } finally {
            db.endTransaction();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
    }

    public void deleteTable() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        db.execSQL("delete from " + TABLE_NAME);
        db.close();
    }

    public List<MyGroupBean> queryAll() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MyGroupBean> list = new ArrayList<MyGroupBean>();
        try {
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                MyGroupBean bean = new MyGroupBean();
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

    public void deleteGroupByID(String ID) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{ID});
        db.close();
    }


    private void setContentValues(ContentValues values,
                                  MyGroupBean bean) {
        values.put("Name", bean.getName());
        values.put("Sequence", bean.getSequence());
        values.put("TotalCount", bean.getTotalCount());
        values.put("ID", bean.getID());
        values.put("AdminUserID", bean.getAdminUserID());
        values.put("UpdateStatus", bean.getUpdateStatus());


    }

    private void setBean(Cursor cursor, MyGroupBean bean) {
        bean.setName(cursor.getString(cursor.getColumnIndex("Name")));
        bean.setSequence(cursor.getString(cursor.getColumnIndex("Sequence")));
        bean.setTotalCount(cursor.getString(cursor.getColumnIndex("TotalCount")));
        bean.setID(cursor.getString(cursor.getColumnIndex("ID")));
        bean.setAdminUserID(cursor.getString(cursor.getColumnIndex("AdminUserID")));
        bean.setUpdateStatus(cursor.getString(cursor.getColumnIndex("UpdateStatus")));


    }
}
