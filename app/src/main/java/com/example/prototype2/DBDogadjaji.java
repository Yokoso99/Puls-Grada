package com.example.prototype2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DBDogadjaji extends SQLiteOpenHelper {


    public DBDogadjaji(Context context) {
        super(context, "Dogadjaji.db", null, 28);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create TABLE Dogadjaji(eventTitle TEXT primary key, eventDate TEXT," +
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

        db.execSQL("drop TABLE if exists Dogadjaji");
        db.execSQL("drop TABLE if exists KruskaPab");
        db.execSQL("drop TABLE if exists SalsaBar");
        db.execSQL("drop TABLE if exists Duomo");
        onCreate(db);
    }

    public boolean updateRecord(String title, String newTitle, String newDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("eventTitle", newTitle);
        contentValues.put("eventDetail", newDetails);


        int rowsAffected = db.update("Dogadjaji", contentValues, "eventTitle" + " = ?", new String[]{title});
        db.close();

        return rowsAffected > 0;
    }

    public boolean updateImage(String title, byte[] newImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("eventImage", newImage);
        int rowsAffected = db.update("Dogadjaji", contentValues, "eventTitle" + " = ?", new String[]{title});
        db.close();

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
}
