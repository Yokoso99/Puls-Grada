package com.example.prototype2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.databinding.ActivityDogadjajiBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Dogadjaji extends AppCompatActivity implements ServerRequest.ServerCallback {

    RecyclerView recyclerView;
    List<DogadjajiItem> list = new ArrayList<>();

    List <DogadjajiItem>bazaList = new ArrayList<>();
    AdapterDogadjaji adapter;

    DBDogadjaji dbDogadjaji;
    DogadjajiItem dogadjajiItem;
    boolean provera;
    List<Events> storedEventList = new ArrayList<>();


    String eventTitle1,eventDate1,eventTime1,encodedImage1,eventTip1,eventDetail1;
    Bitmap imageBitmap;
    Events event;
    List<Events> events = new ArrayList<>();
    String eventTitle,eventDate,eventTime,encodedImage,eventTip,eventDetails,mesto;
    byte[] bytearray;


    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogadjaji);


        dbDogadjaji = new DBDogadjaji(this);
        Intent intent = getIntent();
        if (getIntent() != null && getIntent().hasExtra("eventTitle")) {


            eventTitle = intent.getStringExtra("eventTitle");
            eventDate = intent.getStringExtra("eventDate");
            eventTime = intent.getStringExtra("eventTime");
            bytearray = getIntent().getByteArrayExtra("encodedImage");
            eventTip = intent.getStringExtra("eventType");
            eventDetails = intent.getStringExtra("eventDetail");






            encodedImage = Base64.encodeToString(bytearray, Base64.DEFAULT);

        }

        SharedPreferences sharedPreferences = getSharedPreferences("mesto",MODE_PRIVATE);
        mesto = sharedPreferences.getString("mesto","");

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();





         sendRequestToServer();




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Dogadjaji.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadAndDisplayDataBase();
                adapter.notifyDataSetChanged();
            }
        });




    }



    private void sendRequestToServer() {
        // Define your localhost URL
        String localhostUrl = "http://192.168.1.161/serverrequest.php";


        event = new Events(eventTitle,eventDate,eventTime,encodedImage,eventTip,eventDetails);
        storedEventList.add(event);
        Gson gson = new Gson();


        Type listType = new TypeToken<List<Events>>() {
        }.getType();


        String eventListJson = gson.toJson(storedEventList);
        storedEventList = new Gson().fromJson(eventListJson, listType);




            OkHttpClient client = new OkHttpClient();
            JSONArray eventsArray = new JSONArray();
            if(storedEventList != null) {
                for (Events event : storedEventList) {
                    JSONObject eventObject = new JSONObject();
                    try {
                        eventObject.put("eventTitle", event.getEventTitle());
                        eventObject.put("eventDate", event.getEventDate());
                        eventObject.put("eventTime", event.getEventTime());
                        eventObject.put("encodedImage", event.getEncodedImage());
                        eventObject.put("eventTip", event.getEventTip());
                        eventObject.put("eventDetail", event.getEventDetail());

                        eventsArray.put(eventObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }



        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), eventsArray.toString());


        Request request = new Request.Builder()
                .url(localhostUrl)
                .post(requestBody)
                .build();



        // Make the request asynchronously
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();

                    Log.d("Server request", result);
                    try {
                          JSONObject jsonResponse = new JSONObject(result);
                          String status = jsonResponse.getString("status");

                          if("success".equals(status)) {
                              list.clear();
                              int i = 0;

                              for (i = 0; i < eventsArray.length(); i++) {

                                  JSONArray eventsArray = jsonResponse.getJSONArray("events");
                                  JSONObject eventObject = eventsArray.getJSONObject(i);
                                  eventTitle1 = eventObject.getString("eventTitle");

                                  if (!isEventInDatabase(eventTitle1)) {

                                      eventTitle1 = eventObject.getString("eventTitle");
                                      eventDate1 = eventObject.getString("eventDate");
                                      eventTime1 = eventObject.getString("eventTime");
                                      encodedImage1 = eventObject.getString("encodedImage");
                                      eventTip1 = eventObject.getString("eventTip");
                                      eventDetail1 = eventObject.getString("eventDetail");


                                      byte[] decodedBytes = Base64.decode(encodedImage1, Base64.DEFAULT);
                                      imageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                                      dogadjajiItem = new DogadjajiItem(eventTitle1, eventDate1, eventTime1, imageBitmap, eventTip1, eventDetail1);
                                      list.add(dogadjajiItem);

                                  }
                              }

                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {

                                          insertDataIntoDataBase(list);



                                      adapter.notifyDataSetChanged();
                                  }
                              });




                          }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private boolean isEventInDatabase(String eventTitle) {
        SQLiteDatabase db = dbDogadjaji.getReadableDatabase();
        Cursor cursor = db.query("Dogadjaji", new String[]{"eventTitle"}, "eventTitle=?", new String[]{eventTitle}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }



    private void insertDataIntoDataBase(List<DogadjajiItem> datalist){
        SQLiteDatabase db = dbDogadjaji.getWritableDatabase();
        SharedPreferences sharedPreferences = getSharedPreferences("mesto",MODE_PRIVATE);
        mesto = sharedPreferences.getString("mesto","");
        String tablename = "";

        switch (mesto){
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


        for(DogadjajiItem events: datalist){
            ContentValues values = new ContentValues();
            values.put("eventTitle", events.getDataTitle());
            values.put("eventDate", events.getDataDesc());
            values.put("eventTime", events.getDataLang());
            values.put("eventTip", events.getEventTip());
            values.put("eventDetail", events.getEventDetail());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            events.getDataImage().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imagebyte = byteArrayOutputStream.toByteArray();
            values.put("eventImage", imagebyte);

            db.insert(tablename, null, values);
            bazaList.add(events);

        }
        db.close();

    }

    private void  loadAndDisplayDataBase() {
        SQLiteDatabase db = dbDogadjaji.getReadableDatabase();
        List<DogadjajiItem> tempList = new ArrayList<>();
        adapter = new AdapterDogadjaji(Dogadjaji.this, bazaList);
        recyclerView.setAdapter(adapter);

        String tablename = "";
        SharedPreferences sharedPreferences = getSharedPreferences("mesto",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        mesto = sharedPreferences.getString("mesto","");
        editor.putString("mesto",mesto);
        editor.apply();




        switch (mesto){
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




        Cursor cursor = db.query(tablename,null,null,null,null,null,null);

        if(cursor.moveToFirst()) {
            do {
                String eventTitle = cursor.getString(cursor.getColumnIndexOrThrow("eventTitle"));
                String eventDate = cursor.getString(cursor.getColumnIndexOrThrow("eventDate"));
                String eventTime = cursor.getString(cursor.getColumnIndexOrThrow("eventTime"));
                String eventTip = cursor.getString(cursor.getColumnIndexOrThrow("eventTip"));
                String eventDetail = cursor.getString(cursor.getColumnIndexOrThrow("eventDetail"));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("eventImage"));

                // Convert the byte array to Bitmap
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                // Create a DogadjajiItem object
                DogadjajiItem dogadjajiItem = new DogadjajiItem(eventTitle, eventDate, eventTime, imageBitmap,eventTip,eventDetail);

                tempList.add(dogadjajiItem);
            } while( cursor.moveToNext());
            cursor.close();
            db.close();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    synchronized (bazaList) {
                        bazaList.clear();
                        bazaList.addAll(tempList);
                        tempList.clear();

                    }
                }
            });

        }else {


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String ime = data.getStringExtra("ime");

            String updatedItem = data.getStringExtra("updatedItem");
            String updatedDetail = data.getStringExtra("updatedDetail");
            int updatedPosition = data.getIntExtra("updatedPosition", -1);
            byte[] updatedImageBytes = data.getByteArrayExtra("updatedImage");



            String deletedTitle = data.getStringExtra("deletedTitle");

            if(deletedTitle != null) {
                for (int i = 0; i < bazaList.size(); i++) {
                    if (bazaList.get(i).getDataTitle().equals(deletedTitle)) {
                        bazaList.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            if (updatedPosition != -1 && updatedItem != null) {
                // Update the data in bazaList
                bazaList.get(updatedPosition).setDataTitle(updatedItem);
                // Notify the adapter about the data change
                adapter.notifyItemChanged(updatedPosition);

                // Update the data in the database

                String eventDetail = data.getStringExtra("updatedItem");
                if(updatedImageBytes == null){
                    boolean isUpdated = dbDogadjaji.updateRecord(bazaList.get(updatedPosition).getDataTitle(), updatedItem, updatedDetail,ime);

                }else {
                    boolean isUpdated = dbDogadjaji.updateRecord(bazaList.get(updatedPosition).getDataTitle(), updatedItem, updatedDetail,ime);

                }
                 if(updatedImageBytes != null){
                    boolean isImageUpdated = dbDogadjaji.updateImage(updatedItem,updatedImageBytes,ime);
                }
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         loadAndDisplayDataBase();
                         adapter.notifyDataSetChanged();
                     }
                 });



            }
        }
    }




    private void searchList(String text) {
        List<DogadjajiItem> dataSearchList = new ArrayList<>();
        for (DogadjajiItem data : list) {
            if (data.getDataTitle().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            // Optionally, you can update the adapter with the filtered list
            // adapter.updateList(dataSearchList);
        }
    }

    @Override
    public void onResponse(String result) {
        // This method will be called if needed.
    }

    @Override
    public void onError(String error) {
        // This method will be called if needed.
    }

    @Override
    protected void onResume() {
        super.onResume();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadAndDisplayDataBase();
                adapter.notifyDataSetChanged();

            }
        });

    }
}

