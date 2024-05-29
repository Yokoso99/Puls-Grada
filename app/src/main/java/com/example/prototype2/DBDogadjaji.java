package com.example.prototype2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DBDogadjaji extends SQLiteOpenHelper {


    public DBDogadjaji(Context context) {
        super(context, "Dogadjaji.db", null, 29);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create TABLE BasementBar(eventTitle TEXT primary key, eventDate TEXT," +
                " eventTime TEXT, eventImage BLOB, eventTip TEXT, eventDetail TEXT)");

        db.execSQL("create TABLE KruskaPab(eventTitle TEXT primary key, eventDate TEXT," +
                " eventTime TEXT, eventImage BLOB, eventTip TEXT, eventDetail TEXT)");

        db.execSQL("create TABLE SalsaBar(eventTitle TEXT primary key, eventDate TEXT," +
                " eventTime TEXT, eventImage BLOB, eventTip TEXT, eventDetail TEXT)");

        db.execSQL("create TABLE Duomo(eventTitle TEXT primary key, eventDate TEXT," +
                " eventTime TEXT, eventImage BLOB, eventTip TEXT, eventDetail TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop TABLE if exists BasementBar");
        db.execSQL("drop TABLE if exists KruskaPab");
        db.execSQL("drop TABLE if exists SalsaBar");
        db.execSQL("drop TABLE if exists Duomo");
        onCreate(db);
    }

    public boolean updateRecord(String title, String newTitle, String newDetails,String ime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("eventTitle", newTitle);
        contentValues.put("eventDetail", newDetails);
        String tablename = "";

        switch (ime){
            case "KruskaPab":
                tablename = "KruskaPab";
                break;
            case "SalsaBar":
                tablename = "SalsaBar";
                break;
            case "Duomo":
                tablename = "Duomo";
                break;
            case "BasementBar":
                tablename = "BasementBar";
                break;


        }

        int rowsAffected = db.update(tablename, contentValues, "eventTitle" + " = ?", new String[]{title});
        int rowsAffected1 = db.update(tablename, contentValues, "eventDetail" + " = ?", new String[]{newDetails});

        if(rowsAffected > 0){
            return rowsAffected > 0;
        }
        if(rowsAffected1 > 0){
            return rowsAffected1 > 0;
        }

        return rowsAffected > 0;
    }

    public boolean isDeleted(String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean isDeleted = false;


        String selectionDogadjaji = "eventTitle = ?";
        String[] selectionArgs = {title};
        int rowsDeletedDogadjaji = db.delete("Dogadjaji", selectionDogadjaji, selectionArgs);

        String selectionKruskaPab = "eventTitle = ?";
        int rowsDeletedKruskaPab = db.delete("KruskaPab", selectionKruskaPab, selectionArgs);

        String selectionSalsaBar = "eventTitle = ?";
        int rowsDeletedSalsaBar = db.delete("SalsaBar", selectionSalsaBar, selectionArgs);

        String selectionBasementBar = "eventTitle = ?";
        int rowsDeletedBasementBar = db.delete("BasementBar", selectionBasementBar, selectionArgs);

        String selectionDuomo = "eventTitle = ?";
        int rowsDeletedDuomo = db.delete("Duomo", selectionDuomo, selectionArgs);

        if(rowsDeletedDogadjaji > 1){
            isDeleted = true;
            return isDeleted;
        } else if (rowsDeletedKruskaPab > 0 ) {
            isDeleted = true;
            return isDeleted;
        } else if (rowsDeletedSalsaBar > 0 ) {
            isDeleted = true;
            return isDeleted;
        } else if (rowsDeletedBasementBar > 0) {
            isDeleted = true;
            return isDeleted;
        } else if (rowsDeletedDuomo > 0) {
            isDeleted = true;
            return isDeleted;
        }


        return isDeleted;
    }

    public String checkTable(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        String tablename = "KruskaPab";
        String tablename1 = "SalsaBar";
        String tablename2 = "Duomo";
        String tablename3 = "BasementBar";

        cursor = db.rawQuery("SELECT * FROM " + tablename + " WHERE eventTitle = ? ", new String[]{title});
        int count = cursor.getCount();
        cursor.close();
        if(count > 0){
            return tablename;
        }
        cursor = db.rawQuery("SELECT * FROM " + tablename1 + " WHERE eventTitle = ? ", new String[]{title});
        int count1 = cursor.getCount();
        cursor.close();
        if(count1 > 0 ){
            return tablename1;
        }
        cursor = db.rawQuery("SELECT * FROM " + tablename2 + " WHERE eventTitle = ? ",new String[]{title});
        int count2 = cursor.getCount();
        if(count2 > 0){
            return  tablename2;
        }
        cursor = db.rawQuery("SELECT * FROM " + tablename3 + " WHERE eventTitle = ? ",new String[]{title});
        int count3 = cursor.getCount();
        if(count3 > 0){
            return  tablename3;
        }

        return null;
    }
    public boolean updateImage(String title, byte[] newImage, String ime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("eventImage", newImage);
        String tablename = "";

        switch (ime){
            case "KruskaPab":
                tablename = "KruskaPab";
                break;
            case "SalsaBar":
                tablename = "SalsaBar";
                break;
            case "Duomo":
                tablename = "Duomo";
                break;
            case "BasementBar":
                tablename = "BasementBar";
                break;


        }

        int rowsAffected = db.update(tablename, contentValues, "eventTitle" + " = ?", new String[]{title});
        db.close();

        return rowsAffected > 0;
    }
}
