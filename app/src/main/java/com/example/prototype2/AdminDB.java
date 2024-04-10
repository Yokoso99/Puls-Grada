package com.example.prototype2;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

public class AdminDB extends SQLiteOpenHelper {

    public AdminDB(Context context) {
        super(context, "Admin.db", null, 18);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE Admini(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ime TEXT UNIQUE,imelokala TEXT, sifra TEXT, broj TEXT, slika BLOB, tip TEXT," +
                "detalji TEXT, ponpet TEXT, subota TEXT, nedelja TEXT, adresa TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop TABLE if exists Admini");
        onCreate(db);
    }

    public Boolean insertData(String username, String password, int phoneNumber) {
        SQLiteDatabase MYDB = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put("ime", username);
        contentValues.put("sifra", password);
        contentValues.put("broj", phoneNumber);
        long result = MYDB.insert("Admini", null, contentValues);
        if (result == -1) return false;
        else
            return true;
    }

    public Boolean checkusernamepassword(String username, String password) {

        SQLiteDatabase MyDb = this.getWritableDatabase();
        Cursor cursor = MyDb.rawQuery("Select * from Admini where ime = ? and sifra = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean ifUserExists(String username) {
        SQLiteDatabase MyDb = this.getReadableDatabase();

        Cursor cursor = MyDb.rawQuery("SELECT * FROM Admini WHERE ime = ?", new String[]{username});
        int count = cursor.getCount();
        cursor.close();
        if (count > 0) {
            return true;
        } else
            return false;
    }





    public String[] getValues(String user) {
        SQLiteDatabase MyDb = this.getReadableDatabase();
        Cursor cursor = null;
        String[] values = new String[10];


            cursor = MyDb.rawQuery("SELECT * FROM Admini WHERE ime = ?", new String[]{user});
            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve values from the cursor
                values[0] = cursor.getString(cursor.getColumnIndexOrThrow("imelokala"));
                values[1] = cursor.getString(cursor.getColumnIndexOrThrow("broj"));
                values[2] = cursor.getString(cursor.getColumnIndexOrThrow("tip"));
                values[3] = cursor.getString(cursor.getColumnIndexOrThrow("adresa"));
                values[4] = cursor.getString(cursor.getColumnIndexOrThrow("ponpet"));
                values[5] = cursor.getString(cursor.getColumnIndexOrThrow("subota"));
                values[6] = cursor.getString(cursor.getColumnIndexOrThrow("nedelja"));
                values[7] = cursor.getString(cursor.getColumnIndexOrThrow("detalji"));
                values[9] = cursor.getString(cursor.getColumnIndexOrThrow("ime"));

                byte[] blobData = cursor.getBlob(cursor.getColumnIndexOrThrow("slika"));

                // Encode the BLOB data to Base64 and store it in the string array
                if (blobData != null) {
                    values[8] = Base64.encodeToString(blobData, Base64.DEFAULT);
                } else {
                    values[8] = null;
                }
            }

        return values;
    }

}






