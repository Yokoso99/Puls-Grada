package com.example.prototype2;

import static com.example.prototype2.Utils.compressBitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class Photos extends AppCompatActivity {

    ViewPager2 viewPager2;
    MaterialButton dodajbtn,delbtn;
    String mesto;
    public final int GET_FROM_GALLERY = 1;
    List<Bitmap> selectedImages = new ArrayList<>(); // Store selected images
    ViewPageAdapter viewPageAdapter;
    ImagesDB imagesDB;


    private static final int MAX_ROWS = 20;
    Integer id;

    String user;
    int currentPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        SharedPreferences sharedPreferences = getSharedPreferences("mesto", MODE_PRIVATE);
        mesto = sharedPreferences.getString("mesto", "");
        SharedPreferences sharedPreferences1 = getSharedPreferences("username",MODE_PRIVATE);
        user = sharedPreferences1.getString("user","");
        viewPager2 = findViewById(R.id.ViewPage);
        dodajbtn = findViewById(R.id.dodajbtn);
        delbtn = findViewById(R.id.delbtn);
        imagesDB = new ImagesDB(this);
        selectedImages = new ArrayList<>();

        if(user.contains("Admin") || user.contains("admin")){

            dodajbtn.setVisibility(View.GONE);
            delbtn.setVisibility(View.GONE);
        }
        switch (mesto) {
            case "KruskaPab":
                id = 1;
                break;
            case "SalsaBar":
                id = 2;
                break;
            case "Duomo":
                id = 3;
                break;
            case "BasementBar":
                id = 4;
                break;
        }


        CircleIndicator3 indicator = (CircleIndicator3) findViewById(R.id.indicator);
        dodajbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentPosition = viewPager2.getCurrentItem();

                // Optional: Handle the delete operation if needed
                if (currentPosition >= 0 && currentPosition <= MAX_ROWS) {
                    delDB(currentPosition);
                    selectedImages.remove(currentPosition);
                    viewPageAdapter.notifyItemRemoved(currentPosition);
                    CircleIndicator3 indicator = findViewById(R.id.indicator);
                    indicator.setViewPager(viewPager2);
                }
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                viewPageAdapter = new ViewPageAdapter(selectedImages);
                viewPager2.setAdapter(viewPageAdapter);
                 loadDB();
            }
        });



        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        indicator.setViewPager(viewPager2);

    }
    private void delDB(int position){
        SQLiteDatabase db = imagesDB.getReadableDatabase();
        SharedPreferences sharedPreferences = getSharedPreferences("mesto",MODE_PRIVATE);
        mesto = sharedPreferences.getString("mesto","");

        String tablename = "";

        switch (mesto){
            case "KruskaPab":
                tablename = "KruskaPabImages";
                break;
            case "SalsaBar":
                tablename = "SalsaBarImages";
                break;
            case "Duomo":
                tablename = "DuomoImages";
                break;
            case "BasementBar":
                tablename = "BasementBarImages";
                break;
        }
        Cursor cursor = db.query(tablename, new String[]{"id"}, null, null, null, null, null);
        if (cursor.moveToPosition(position)) {
            int idToDelete = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            int deletedRows = db.delete(tablename, "id = ?", new String[]{String.valueOf(idToDelete)});
            if (deletedRows > 0) {

                reorderIds(db, tablename);
            }
        }
    }

    private void reorderIds(SQLiteDatabase db, String tablename) {
        Cursor cursor = db.query(tablename, new String[]{"id"}, null, null, null, null, "_id");
        int newId = 1;

        if (cursor.moveToFirst()) {
            do {
                int currentId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", newId);
                db.update(tablename, contentValues, "id = ?", new String[]{String.valueOf(currentId)});
                newId++;
            } while (cursor.moveToNext());
        }
        cursor.close();
    }





    private void loadDB(){
        SQLiteDatabase db = imagesDB.getReadableDatabase();
        SharedPreferences sharedPreferences = getSharedPreferences("mesto",MODE_PRIVATE);
        mesto = sharedPreferences.getString("mesto","");

        String tablename = "";

        switch (mesto){
            case "KruskaPab":
                tablename = "KruskaPabImages";
                break;
            case "SalsaBar":
                tablename = "SalsaBarImages";
                break;
            case "Duomo":
                tablename = "DuomoImages";
                break;
            case "BasementBar":
                tablename = "BasementBarImages";
                break;
        }





        Cursor cursor = db.query(tablename,null,null,null,null,null,null);

        if(cursor.moveToFirst()){
            do {
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

                // Convert the byte array to Bitmap
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


                 selectedImages.add(imageBitmap);
            }while(cursor.moveToNext());

            viewPageAdapter = new ViewPageAdapter(selectedImages);
            viewPager2.setAdapter(viewPageAdapter);

            cursor.close();
            db.close();
        }
    }

    private void insertDB(List<Bitmap> list) {
        SQLiteDatabase db = imagesDB.getWritableDatabase();
        SharedPreferences sharedPreferences = getSharedPreferences("mesto", MODE_PRIVATE);
        mesto = sharedPreferences.getString("mesto", "");
        String tablename = "";

        switch (mesto) {
            case "KruskaPab":
                tablename = "KruskaPabImages";
                break;
            case "SalsaBar":
                tablename = "SalsaBarImages";
                break;
            case "Duomo":
                tablename = "DuomoImages";
                break;
            case "BasementBar":
                tablename = "BasementBarImages";
                break;
        }


        Cursor cursor1 = db.query(tablename, new String[]{"COUNT(*)"}, null, null, null, null, null);
        cursor1.moveToFirst();
        int rowCount = cursor1.getInt(0);
        cursor1.close();




        if (rowCount + list.size() > MAX_ROWS) {
            runOnUiThread(() -> Toast.makeText(Photos.this, "Cannot add more than " + MAX_ROWS + " images.", Toast.LENGTH_SHORT).show());
            db.close();
            return;
        }
        int imageID = 4;
        String tablename1 = tablename;



                for (Bitmap bitmap : list) {

                    Bitmap compressedBitmap = compressBitmap(bitmap, 8000);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);




                    ContentValues contentValues = new ContentValues();
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    contentValues.put("_id",imageID);
                    contentValues.put("image", imageBytes); // Assuming "image" is the column name for storing images
                    db.insert(tablename1, null, contentValues);

                }



    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, GET_FROM_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Bitmap> newImages = new ArrayList<>(); // Create a temporary list for new images

                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri uri = clipData.getItemAt(i).getUri();
                            Bitmap bitmap = Utils.getBitmapFromUri(Photos.this, uri);
                            if (bitmap != null) {
                                newImages.add(bitmap);
                            }
                        }
                        // After loading images, insert them into the database
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                insertDB(newImages);
                                selectedImages.addAll(newImages);
                                viewPageAdapter.notifyDataSetChanged();
                                CircleIndicator3 indicator = findViewById(R.id.indicator);
                                indicator.setViewPager(viewPager2);
                            }
                        });
                    }
                }).start();
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Bitmap> newImages = new ArrayList<>();
                        Bitmap bitmap = Utils.getBitmapFromUri(Photos.this, uri);
                        if (bitmap != null) {
                            selectedImages.add(bitmap);
                        }
                        // After loading the image, insert it into the database
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                insertDB(newImages);
                                selectedImages.addAll(newImages);
                                viewPageAdapter.notifyDataSetChanged();
                                CircleIndicator3 indicator = findViewById(R.id.indicator);
                                indicator.setViewPager(viewPager2);

                            }
                        });
                    }
                }).start();
            }
        }
    }

}
