package com.example.prototype2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {



    public DBHelper(Context context) {
        super(context, "Login.db", null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase MYDB) {

        MYDB.execSQL("create TABLE users(username TEXT primary key, password TEXT, email TEXT, phoneNumber INTEGER UNIQUE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MYDB, int i, int i1) {
        MYDB.execSQL("drop TABLE if exists users");
        onCreate(MYDB);
    }

    public Boolean insertData(String username, String password,String email,int phoneNumber) {
        SQLiteDatabase MYDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email",email);
        contentValues.put("phoneNumber",phoneNumber);
        long result = MYDB.insert("users", null, contentValues);
        if (result == -1) return false;
        else
            return true;
    }


    public Boolean checkusername(String username,String email,int phoneNumber) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and email = ? and phoneNumber = ?", new String[]{username,email, String.valueOf(phoneNumber)});

        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }



    public Boolean checkusernamepassword(String username, String password) {

        SQLiteDatabase MyDb = this.getWritableDatabase();
        Cursor cursor = MyDb.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}