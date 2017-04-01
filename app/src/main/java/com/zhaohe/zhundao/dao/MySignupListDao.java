package com.zhaohe.zhundao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhaohe.zhundao.bean.dao.MySignListupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:存放签到数据的Vcode和checkinId
 * @Author:邹苏隆
 * @Since:2016/12/26 16:39
 */
public class MySignupListDao {

    private SQLiteOpenHelperDao dbOpenHelper;
    private final String TABLE_NAME = "MySignupList";

    public MySignupListDao(Context context) {
        this.dbOpenHelper = new SQLiteOpenHelperDao(context);
    }

    public long save(MySignListupBean bean) {
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
    public void save(List<MySignListupBean> list) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction ();// 开启事务
        try {
            for(int i=0;i<list.size();i++){
                ContentValues values = new ContentValues();
                MySignListupBean bean=list.get(i);
                setContentValues(values, bean);

                db.replace(TABLE_NAME, null, values);}
            db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
            db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
    }

    private void setContentValues(ContentValues values,
                                  MySignListupBean bean) {
        values.put("VCode", bean.getVCode());
        values.put("CheckInID", bean.getCheckInID());
        values.put("Status", bean.getStatus());
        values.put("UpdateStatus", bean.getUpdateStatus());
        values.put("Name", bean.getName());
        values.put("Phone", bean.getPhone());
        values.put("AdminRemark", bean.getAdminRemark());
        values.put("FeeName", bean.getFeeName());
        values.put("Fee", bean.getFee());
        values.put("SignTime",bean.getSignTime());

    }

    public List<MySignListupBean> queryAll() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        try {
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                MySignListupBean bean = new MySignListupBean();
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


    //根据Vcode更新数据
    public int update(MySignListupBean bean) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        // db.beginTransaction ();// 开启事务
        try {
            ContentValues values = new ContentValues();
            values.put("Status", bean.getStatus());
            values.put("UpdateStatus", bean.getUpdateStatus());
            return db.update(TABLE_NAME, values, "VCode = ? and CheckInID = ?", new String[]{bean.getVCode(), bean.getCheckInID()});
            // db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
            // db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
    }
    //根据Phone更新数据
    public int updateByPhone(MySignListupBean bean) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        // db.beginTransaction ();// 开启事务
        try {
            ContentValues values = new ContentValues();
            values.put("Status", bean.getStatus());
            values.put("UpdateStatus", bean.getUpdateStatus());
//            values.put("SignTime",bean.getSignTime());
            return db.update(TABLE_NAME, values, "Phone = ? and CheckInID = ?", new String[]{bean.getPhone(), bean.getCheckInID()});
            // db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
            // db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
    }

    //    查找VCode是否存在
    public List<MySignListupBean> queryListByVCode(String VCode) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where VCode like ?", new String[]{VCode});
            while (cursor.moveToNext()) {
                MySignListupBean bean = new MySignListupBean();
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

    //    查找是否已经扫码
    public List<MySignListupBean> queryListByVCodeAndCheckInID(String VCode, String CheckInID) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where VCode = ? and CheckInID = ?", new String[]{VCode, CheckInID});
            while (cursor.moveToNext()) {
                MySignListupBean bean = new MySignListupBean();
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
    public List<MySignListupBean> queryListByPhoneAndCheckInID(String Phone, String CheckInID) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where Phone = ? and CheckInID = ?", new String[]{Phone, CheckInID});
            while (cursor.moveToNext()) {
                MySignListupBean bean = new MySignListupBean();
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
    public List<MySignListupBean> queryListByPhoneNameAndCheckInID( String CheckInID,String param) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where  CheckInID = ? and(Phone||Name)like ?", new String[]{CheckInID,"%"+param+"%"});
            while (cursor.moveToNext()) {
                MySignListupBean bean = new MySignListupBean();
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


    //    查询用户扫码状态
    public List<MySignListupBean> queryListStatus(String VCode, String CheckInID, String Status) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where VCode = ? and CheckInID = ? and Status = ?", new String[]{VCode, CheckInID, Status});
            while (cursor.moveToNext()) {
                MySignListupBean bean = new MySignListupBean();
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

    //    查询用户扫码状态
    public List<MySignListupBean> queryListStatusByPhone(String Phone, String CheckInID, String Status) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where Phone = ? and CheckInID = ? and Status = ?", new String[]{Phone, CheckInID, Status});
            while (cursor.moveToNext()) {
                MySignListupBean bean = new MySignListupBean();
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
//    根据签到状态查询列表
    public List<MySignListupBean> queryListByCheckinIDAndStatus(String CheckInID, String Status) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where  CheckInID = ? and Status like ?", new String[]{ CheckInID, Status});
            while (cursor.moveToNext()) {
                MySignListupBean bean = new MySignListupBean();
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

    //查找需要更新上传服务器的数据
    public List<MySignListupBean> queryUpdateStatus() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where UpdateStatus like ?", new String[]{"true"});
            while (cursor.moveToNext()) {
                MySignListupBean bean = new MySignListupBean();
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


    private void setBean(Cursor cursor, MySignListupBean bean) {

        String VCode = cursor.getString(cursor.getColumnIndex("VCode"));
        String CheckInID = cursor.getString(cursor.getColumnIndex("CheckInID"));
        String Status = cursor.getString(cursor.getColumnIndex("Status"));
        String UpdateStatus = cursor.getString(cursor.getColumnIndex("UpdateStatus"));
        String Name = cursor.getString(cursor.getColumnIndex("Name"));
        String Phone = cursor.getString(cursor.getColumnIndex("Phone"));
        String AdminRemark = cursor.getString(cursor.getColumnIndex("AdminRemark"));
        String FeeName = cursor.getString(cursor.getColumnIndex("FeeName"));
        String Fee = cursor.getString(cursor.getColumnIndex("Fee"));
        String SignTime=cursor.getString(cursor.getColumnIndex("SignTime"));
        bean.setVCode(VCode);
        bean.setCheckInID(CheckInID);
        bean.setStatus(Status);
        bean.setUpdateStatus(UpdateStatus);
        bean.setName(Name);
        bean.setPhone(Phone);
        bean.setAdminRemark(AdminRemark);
        bean.setFeeName(FeeName);
        bean.setFee(Fee);
        bean.setSignTime(SignTime);
    }


}
