package com.example.ahmadbarakat.phonebook;

/**
 * Created by ahmadbarakat on 219 / 7 / 14.
 */


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.database.DatabaseUtils;
import android.graphics.BitmapFactory;
import android.util.Log;


public class DB_SQLite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String CONTACTS_COLUMN_Photo = "photo";


    public DB_SQLite(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text, phone text, email text, photo blob)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean addContact  (String name, String phone, String email, byte[] photo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("photo", photo);

        db.insert("contacts", null, contentValues);

        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, byte[] photo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("photo", photo);

        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList getAllContacts()
    {
        ArrayList<Contact> array_list = new ArrayList<Contact>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Contact item = new Contact();
            //Log.e("DB_SQLite:", "ID = " + Integer.parseInt(res.getString(0)));
            item.setId(Integer.parseInt(res.getString(0)));
            item.setName(((res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)))));
            item.setPhone(((res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE)))));
            item.setEmail(((res.getString(res.getColumnIndex(CONTACTS_COLUMN_EMAIL)))));
            try {
                item.setBitmap(BitmapFactory.decodeByteArray((res.getBlob(res.getColumnIndex(CONTACTS_COLUMN_Photo))), 0, (res.getBlob(res.getColumnIndex(CONTACTS_COLUMN_Photo))).length));
            }catch (NullPointerException e){};

            array_list.add(item);
            res.moveToNext();
        }

        return array_list;
    }

}