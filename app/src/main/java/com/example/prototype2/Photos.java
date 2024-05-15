package com.example.prototype2;

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
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class Photos extends AppCompatActivity {

    ViewPager2 viewPager2;
    MaterialButton dodajbtn;
    String mesto;
    public final int GET_FROM_GALLERY = 1;
    List<Bitmap> selectedImages = new ArrayList<>(); // Store selected images
    ViewPageAdapter viewPageAdapter;
    ImagesDB imagesDB;
    PhotosItem photosItem;

    Integer id;
    List<Bitmap> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        SharedPreferences sharedPreferences = getSharedPreferences("mesto", MODE_PRIVATE);
        mesto = sharedPreferences.getString("mesto", "");
        viewPager2 = findViewById(R.id.ViewPage);
        dodajbtn = findViewById(R.id.dodajbtn);
        imagesDB = new ImagesDB(this);

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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                list = imagesDB.value(selectedImages,id);

                viewPageAdapter = new ViewPageAdapter(selectedImages);
                viewPager2.setAdapter(viewPageAdapter);
                // loadDB(list);
            }
        });

        viewPageAdapter = new ViewPageAdapter(selectedImages);
        viewPager2.setAdapter(viewPageAdapter);

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        indicator.setViewPager(viewPager2);
    }
//    private void loadDB(List<Bitmap> list){
//        SQLiteDatabase db = imagesDB.getReadableDatabase();
//        SharedPreferences sharedPreferences = getSharedPreferences("mesto",MODE_PRIVATE);
//        mesto = sharedPreferences.getString("mesto","");
//
//        String tablename = "";
//
//        switch (mesto){
//            case "KruskaPab":
//                tablename = "KruskaPabImages";
//                break;
//            case "SalsaBar":
//                tablename = "SalsaBarImages";
//                break;
//            case "Duomo":
//                tablename = "DuomoImages";
//                break;
//            case "BasementBar":
//                tablename = "BasementBarImages";
//                break;
//        }
//
//        Cursor cursor = db.query(tablename,null,null,null,null,null,null);
//
//        if(cursor.moveToFirst()){
//            do {
//                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
//
//                // Convert the byte array to Bitmap
//                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//
//                 selectedImages.add(imageBitmap);
//
//            }while(cursor.moveToNext());
//
//            viewPageAdapter = new ViewPageAdapter(selectedImages);
//            viewPager2.setAdapter(viewPageAdapter);
//
//
//            cursor.close();
//            db.close();
//        }
//    }

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

        int imageID = 4;

        for (Bitmap bitmap : list) {

            Bitmap compressedBitmap = Utils.compressBitmap(bitmap, 1024); // Adjust the size as needed

            ContentValues contentValues = new ContentValues();
            byte[] imageBytes = Utils.getBytesFromBitmap(compressedBitmap );
            contentValues.put("_id",imageID);
            contentValues.put("image", imageBytes); // Assuming "image" is the column name for storing images
            db.insert(tablename, null, contentValues);
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
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri uri = clipData.getItemAt(i).getUri();
                            Bitmap bitmap = Utils.getBitmapFromUri(Photos.this, uri);
                            if (bitmap != null) {
                                selectedImages.add(bitmap);
                            }
                        }
                        // After loading images, insert them into the database

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                insertDB(selectedImages);
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
