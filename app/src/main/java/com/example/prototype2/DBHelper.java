package com.example.prototype2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Base64;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {



    public DBHelper(Context context) {
        super(context, "Login.db", null, 18);
    }

    @Override
    public void onCreate(SQLiteDatabase MYDB) {

        MYDB.execSQL("create TABLE users(username TEXT primary key, password TEXT,novasifra TEXT, profilePic BLOB,email TEXT, phoneNumber INTEGER UNIQUE,imeprez TEXT,brojgodina INTEGER)");

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
    public String[] getValues(String user) {
        SQLiteDatabase MyDb = this.getReadableDatabase();
        Cursor cursor = null;
        String[] values = new String[9];


        cursor = MyDb.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{user});
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve values from the cursor
            values[0] = cursor.getString(cursor.getColumnIndexOrThrow("imeprez"));
            values[1] = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            values[2] = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"));
            values[4] = cursor.getString(cursor.getColumnIndexOrThrow("brojgodina"));
            values[5] = cursor.getString(cursor.getColumnIndexOrThrow("username"));



            byte[] blobData = cursor.getBlob(cursor.getColumnIndexOrThrow("profilePic"));

            // Encode the BLOB data to Base64 and store it in the string array
            if (blobData != null) {
                values[3] = Base64.encodeToString(blobData, Base64.DEFAULT);
            } else {
                values[3] = null;
            }
        }

        return values;
    }
    public Boolean ifUserExists(String userName) {
        SQLiteDatabase MyDb = this.getReadableDatabase();

        Cursor cursor = MyDb.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{userName});
        int count = cursor.getCount();
        cursor.close();
        if (count > 0) {
            return true;
        } else
            return false;
    }
}