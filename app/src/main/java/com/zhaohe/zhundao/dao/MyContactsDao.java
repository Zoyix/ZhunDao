package com.zhaohe.zhundao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhaohe.zhundao.bean.dao.MyContactsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:通讯录数据库
 * @Author:邹苏隆
 * @Since:2017/5/22 14:ic_launcher
 */
public class MyContactsDao {
    private SQLiteOpenHelperDao dbOpenHelper;
    private final String TABLE_NAME = "MyContacts";
    public MyContactsDao(Context context) {
        this.dbOpenHelper = new SQLiteOpenHelperDao(context);
    }
    public void save(List<MyContactsBean> list) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction ();// 开启事务
        try {
            for(int i=0;i<list.size();i++){
                ContentValues values = new ContentValues();
                MyContactsBean bean=list.get(i);
                setContentValues(values, bean);

                db.replace(TABLE_NAME, null, values);}
            db.setTransactionSuccessful ();// 设置事务的标志为True
        } finally {
            db.endTransaction ();// 结束事务,有两种情况：commit,rollback,
            db.close();
        }
    }
    public void deleteTable(){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase ();

        db.execSQL("delete from "+TABLE_NAME);
        db.close();
    }
    public void deleteGroupByID(String ID){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        db.delete(TABLE_NAME,"ID=?", new String[] {ID});
        db.close();

    }
    public List<MyContactsBean> queryAll() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MyContactsBean> list = new ArrayList<MyContactsBean>();
        try {
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                MyContactsBean bean = new MyContactsBean();
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

    public List<MyContactsBean> queryGroupID(String GroupID) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MyContactsBean> list = new ArrayList<MyContactsBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where GroupID = ? ", new String[]{GroupID});
            while (cursor.moveToNext()) {
                MyContactsBean bean = new MyContactsBean();
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
    public List<MyContactsBean> queryListGroupIDAndPhoneOrName(String GroupID , String param) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MyContactsBean> list = new ArrayList<MyContactsBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where GroupID = ? and (name||Phone) like ?", new String[]{GroupID,"%"+param+"%"});
            while (cursor.moveToNext()) {
                MyContactsBean bean = new MyContactsBean();
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

    public List<MyContactsBean> queryListdPhoneOrName( String param) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // db.beginTransaction ();// 开启事务
        List<MyContactsBean> list = new ArrayList<MyContactsBean>();
        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " where (name||Phone) like ?", new String[]{"%"+param+"%"});
            while (cursor.moveToNext()) {
                MyContactsBean bean = new MyContactsBean();
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
    private void setContentValues(ContentValues values,
                                  MyContactsBean bean) {
        values.put("name", bean.getName());
        values.put("pinyin", bean.getPinyin());
        values.put("firstLetter", bean.getFirstLetter());
        values.put("Phone", bean.getPhone());
        values.put("Address", bean.getAddress());
        values.put("Email", bean.getEmail());
        values.put("GroupName", bean.getGroupName());
        values.put("GroupID", bean.getGroupID());
        values.put("ID", bean.getID());
        values.put("Sex", bean.getSex());
        values.put("HeadImgurl", bean.getHeadImgurl());
        values.put("Company", bean.getCompany());
        values.put("Duty", bean.getDuty());
        values.put("IDcard", bean.getIDcard());
        values.put("Remark", bean.getRemark());
        values.put("SerialNo", bean.getSerialNo());


    }
    private void setBean(Cursor cursor, MyContactsBean bean) {

        String name = cursor.getString(cursor.getColumnIndex("name"));
        String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
        String firstLetter = cursor.getString(cursor.getColumnIndex("firstLetter"));
        String Phone = cursor.getString(cursor.getColumnIndex("Phone"));
        String Address = cursor.getString(cursor.getColumnIndex("Address"));
        String Email = cursor.getString(cursor.getColumnIndex("Email"));
        String GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
        String GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
        String ID = cursor.getString(cursor.getColumnIndex("ID"));
        String UpdateStatus = cursor.getString(cursor.getColumnIndex("UpdateStatus"));
        String Sex=cursor.getString(cursor.getColumnIndex("Sex"));
        String HeadImgurl=cursor.getString(cursor.getColumnIndex("HeadImgurl"));
        String Company=cursor.getString(cursor.getColumnIndex("Company"));
        String Duty=cursor.getString(cursor.getColumnIndex("Duty"));
        String IDcard=cursor.getString(cursor.getColumnIndex("IDcard"));
        String Remark=cursor.getString(cursor.getColumnIndex("Remark"));
        String SerialNo=cursor.getString(cursor.getColumnIndex("SerialNo"));







        bean.setName(name);
        bean.setPinyin(pinyin);
        bean.setFirstLetter(firstLetter);
        bean.setPhone(Phone);
        bean.setAddress(Address);
        bean.setEmail(Email);
        bean.setGroupName(GroupName);
        bean.setGroupID(GroupID);
        bean.setID(ID);
        bean.setUpdateStatus(UpdateStatus);
        bean.setSex(Sex);
        bean.setHeadImgurl(HeadImgurl);
bean.setCompany(Company);
        bean.setDuty(Duty);
        bean.setIDcard(IDcard);
        bean.setRemark(Remark);
        bean.setSerialNo(SerialNo);
    }
}
