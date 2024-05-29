package com.example.prototype2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UpcAndPassDB extends SQLiteOpenHelper {
    public UpcAndPassDB(Context context){
        super(context,"UpcAndPass.db",null,6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Upcoming (eventTitle TEXT, eventDate TEXT, eventTime TEXT," +
                " eventImage BLOB, eventTip TEXT, eventDetail TEXT, username TEXT)");
        db.execSQL("CREATE TABLE Passed (eventTitle TEXT, eventDate TEXT, eventTime TEXT," +
                " eventImage BLOB, eventTip TEXT, eventDetail TEXT,username TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE if Exists Upcoming");
        db.execSQL("DROP TABLE if Exists Passed");
        onCreate(db);

    }
    public boolean checkIfExist(String eventTitle1,String eventDate1) {
        SQLiteDatabase db = getReadableDatabase();
        boolean exists = false;

        Cursor cursor,cursor1 = null;

        cursor = db.rawQuery("SELECT * FROM Upcoming WHERE eventTitle = ?", new String[]{eventTitle1});
        cursor1 = db.rawQuery("SELECT * FROM Upcoming WHERE eventDate = ?", new String[]{eventDate1});




        if (cursor != null && cursor.getCount() > 0 && cursor1 != null && cursor1.getCount() > 0) {

            exists = true;
            return exists;
        }else{
            exists = false;
        }
        return  exists;
    }
}
