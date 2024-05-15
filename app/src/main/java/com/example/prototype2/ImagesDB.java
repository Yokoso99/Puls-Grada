package com.example.prototype2;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.util.List;

public class ImagesDB extends SQLiteOpenHelper {

    public ImagesDB(Context context){
        super(context,"ImagesDB.db",null,12);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE DuomoImages (_id INTEGER , name TEXT, image BLOB)");
        db.execSQL("CREATE TABLE SalsaBarImages (_id INTEGER , name TEXT, image BLOB)");
        db.execSQL("CREATE TABLE KruskaPabImages (_id INTEGER , name TEXT, image BLOB)");
        db.execSQL("CREATE TABLE BasementBarImages (_id INTEGER , name TEXT, image BLOB)");






    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop TABLE if exists BasementBarImages");
        db.execSQL("drop TABLE if exists KruskaPabImages");
        db.execSQL("drop TABLE if exists SalsaBarImages");
        db.execSQL("drop TABLE if exists DuomoImages");
        onCreate(db);
    }
    public List<Bitmap> value(List<Bitmap> list,Integer id) {
        if (list.isEmpty()) { // If the list is empty, fetch values from the database
            SQLiteDatabase db = this.getReadableDatabase();









            Cursor cursor = null;

            switch (id){
                case (1):
                    cursor = db.rawQuery("SELECT * FROM DuomoImages WHERE _id = ?", new String[]{String.valueOf(id)});
                    break;
                case (2):
                    cursor = db.rawQuery("SELECT * FROM KruskaPabImages WHERE _id = ?", new String[]{String.valueOf(id)});
                    break;
                case (3):
                    cursor = db.rawQuery("SELECT * FROM SalsaBarImages WHERE _id = ?", new String[]{String.valueOf(id)});
                    break;
                case (4):
                    cursor = db.rawQuery("SELECT * FROM BasementBarImages WHERE _id = ?", new String[]{String.valueOf(id)});

            }

            if (cursor.moveToFirst()) {
                do {

                    byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

                    // Convert the byte array to Bitmap
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    list.add(imageBitmap);

                } while (cursor.moveToLast());

                cursor.close();
            }
            db.close();
        }
        return list; // Return the list, either populated from the database or unchanged
    }
}
