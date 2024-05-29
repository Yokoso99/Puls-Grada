package com.example.prototype2;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

public class ImagesDB extends SQLiteOpenHelper {

    public ImagesDB(Context context) {
        super(context, "Images.db", null, 30);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE DuomoImages (id INTEGER PRIMARY KEY AUTOINCREMENT, _id INTEGER, name TEXT, image BLOB)");
        db.execSQL("CREATE TABLE SalsaBarImages (id INTEGER PRIMARY KEY AUTOINCREMENT, _id INTEGER, name TEXT, image BLOB)");
        db.execSQL("CREATE TABLE KruskaPabImages (id INTEGER PRIMARY KEY AUTOINCREMENT, _id INTEGER, name TEXT, image BLOB)");
        db.execSQL("CREATE TABLE BasementBarImages (id INTEGER PRIMARY KEY AUTOINCREMENT, _id INTEGER, name TEXT, image BLOB)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop TABLE if exists BasementBarImages");
        db.execSQL("drop TABLE if exists KruskaPabImages");
        db.execSQL("drop TABLE if exists SalsaBarImages");
        db.execSQL("drop TABLE if exists DuomoImages");
        onCreate(db);
    }

}


