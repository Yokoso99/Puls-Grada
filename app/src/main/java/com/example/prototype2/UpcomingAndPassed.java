package com.example.prototype2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpcomingAndPassed extends AppCompatActivity {


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
        setContentView(R.layout.activity_upcoming_and_passed);

        recyclerView = findViewById(R.id.recyclerview);
        passed = findViewById(R.id.passed);

        Intent intent = new Intent();
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);

        UpcAndPassDB upcAndPassDB = new UpcAndPassDB(this);
        SharedPreferences sharedPreferences1 = getSharedPreferences("username",MODE_PRIVATE);
        user = sharedPreferences1.getString("user","");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(UpcomingAndPassed.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadDB();
                adapter = new AdapterUpcAndPass(UpcomingAndPassed.this,bazaList);
                adapter.notifyDataSetChanged();
            }
        });

        passed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpcomingAndPassed.this, Passed.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);



            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    private void loadDB(){
        UpcAndPassDB upcAndPassDB = new UpcAndPassDB(this);
        SQLiteDatabase db = upcAndPassDB.getReadableDatabase();
        List<UpcPassDogadjajiItem> tempList = new ArrayList<>();
        adapter = new AdapterUpcAndPass(UpcomingAndPassed.this, bazaList);
        recyclerView.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = sdf.format(new Date());

        Date date = new Date();

        try {
            date = sdf.parse(currentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Cursor cursor;

        cursor = db.query("Upcoming",null,null,null,null,null,null);

        if(cursor.moveToFirst()){
            do {


                String eventTitle = cursor.getString(cursor.getColumnIndexOrThrow("eventTitle"));
                String eventDateStr = cursor.getString(cursor.getColumnIndexOrThrow("eventDate"));
                String eventTime = cursor.getString(cursor.getColumnIndexOrThrow("eventTime"));
                String eventTip = cursor.getString(cursor.getColumnIndexOrThrow("eventTip"));
                String eventDetail = cursor.getString(cursor.getColumnIndexOrThrow("eventDetail"));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("eventImage"));

                // Convert the byte array to Bitmap
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);



                Date eventDate = null;
                try {
                    eventDate = sdf.parse(eventDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (eventDate != null && eventDate.after(date)) {


                    UpcPassDogadjajiItem upcPassDogadjajiItem1 = new UpcPassDogadjajiItem(eventTitle, eventDateStr, eventTime, imageBitmap, eventTip, eventDetail);
                    tempList.add(upcPassDogadjajiItem1);
                }
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

}