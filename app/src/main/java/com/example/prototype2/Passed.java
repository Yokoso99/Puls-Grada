package com.example.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Passed extends AppCompatActivity {

    UpcPassDogadjajiItem upcPassDogadjajiItem;
    List <UpcPassDogadjajiItem>bazaList = new ArrayList<>();
    List<UpcPassDogadjajiItem> list = new ArrayList<>();
    AdapterUpcAndPass adapter;
    String user;
    RecyclerView recyclerView;
    ImageButton upcoming,passed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passed);

        recyclerView = findViewById(R.id.recyclerview);
        upcoming = findViewById(R.id.upcoming);
        passed = findViewById(R.id.passed);

        Intent intent = new Intent();
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);

        UpcAndPassDB upcAndPassDB = new UpcAndPassDB(this);
        SharedPreferences sharedPreferences1 = getSharedPreferences("username",MODE_PRIVATE);
        user = sharedPreferences1.getString("user","");



        GridLayoutManager gridLayoutManager = new GridLayoutManager(Passed.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadDB();
                loadDB2();
                adapter = new AdapterUpcAndPass(Passed.this,bazaList);
                adapter.notifyDataSetChanged();
            }
        });
        passed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAlert();
            }
        });



        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                

            }
        });


    }
    private void showDialogAlert() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogAlert dialogAlert = new DialogAlert();
        dialogAlert.show(fragmentManager, "DialogAlert");
    }


    private void loadDB(){
        UpcAndPassDB upcAndPassDB = new UpcAndPassDB(this);
        SQLiteDatabase db = upcAndPassDB.getWritableDatabase();
        List<UpcPassDogadjajiItem> tempList = new ArrayList<>();
        adapter = new AdapterUpcAndPass(Passed.this,bazaList);
        recyclerView.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = sdf.format(new Date());

        Date date = new Date();

        try {
            date = sdf.parse(currentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Cursor cursor = db.query("Upcoming", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String eventTitle = cursor.getString(cursor.getColumnIndexOrThrow("eventTitle"));
                String eventDateStr = cursor.getString(cursor.getColumnIndexOrThrow("eventDate"));
                String eventTime = cursor.getString(cursor.getColumnIndexOrThrow("eventTime"));
                String eventTip = cursor.getString(cursor.getColumnIndexOrThrow("eventTip"));
                String eventDetail = cursor.getString(cursor.getColumnIndexOrThrow("eventDetail"));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("eventImage"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));



                Date eventDate = null;
                try {
                    eventDate = sdf.parse(eventDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Check if the event date is before the current date
                if (eventDate != null && eventDate.before(date)) {
                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("eventTitle", eventTitle);
                                    contentValues.put("eventDate", eventDateStr);
                                    contentValues.put("eventTime", eventTime);
                                    contentValues.put("eventImage", imageBytes);
                                    contentValues.put("eventTip", eventTip);
                                    contentValues.put("eventDetail", eventDetail);
                                    contentValues.put("username", username);
                                    db.insert("Passed", null, contentValues);

                                    // Delete event from the "Upcoming" table
                                    db.delete("Upcoming", "eventTitle=? AND eventDate=? AND username=?", new String[]{eventTitle, eventDateStr,username});

                                    // Add to tempList for displaying in RecyclerView
                                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                    UpcPassDogadjajiItem upcPassDogadjajiItem1 = new UpcPassDogadjajiItem(eventTitle, eventDateStr, eventTime, imageBitmap, eventTip, eventDetail);
                                    tempList.add(upcPassDogadjajiItem1);
                }

            } while (cursor.moveToNext());
            cursor.close();
            db.close();

            loadDB2();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    synchronized (bazaList) {
                        bazaList.clear();
                        bazaList.addAll(tempList);
                        tempList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }


    }

    private void loadDB2() {
        UpcAndPassDB upcAndPassDB = new UpcAndPassDB(this);
        SQLiteDatabase db = upcAndPassDB.getReadableDatabase();
        List<UpcPassDogadjajiItem> tempList = new ArrayList<>();
        adapter = new AdapterUpcAndPass(Passed.this,bazaList);
        recyclerView.setAdapter(adapter);

        Cursor cursor = db.query("Passed", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{

                String eventTitle = cursor.getString(cursor.getColumnIndexOrThrow("eventTitle"));
                String eventDateStr = cursor.getString(cursor.getColumnIndexOrThrow("eventDate"));
                String eventTime = cursor.getString(cursor.getColumnIndexOrThrow("eventTime"));
                String eventTip = cursor.getString(cursor.getColumnIndexOrThrow("eventTip"));
                String eventDetail = cursor.getString(cursor.getColumnIndexOrThrow("eventDetail"));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("eventImage"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));

                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


                UpcPassDogadjajiItem upcPassDogadjajiItem1 = new UpcPassDogadjajiItem(eventTitle, eventDateStr, eventTime, imageBitmap, eventTip, eventDetail);
                tempList.add(upcPassDogadjajiItem1);

            }while(cursor.moveToNext());
            cursor.close();
            db.close();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    synchronized (bazaList) {
                        bazaList.clear();
                        bazaList.addAll(tempList);
                        tempList.clear();
                        adapter.notifyDataSetChanged();

                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

}