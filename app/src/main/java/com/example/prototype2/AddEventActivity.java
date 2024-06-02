package com.example.prototype2;
import android.util.Base64;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddEventActivity extends AppCompatActivity implements ServerRequest.ServerCallback {

    int GET_FROM_GALLERY = 3;

    View view;

    Spinner spinner;
    ImageView dodajsliku,postavisliku;

    EditText opis;
    Button dodajbtn;
    List<Events> eventsList;
    boolean bol1,bol2;
    String mesto;
    EditText editTextDay,editTextMonth,editTextYear,opisEditText,pocetakEditText,krajEditText,nazivEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_page);
        dodajsliku = findViewById(R.id.dodaj_sliku);
        postavisliku = findViewById(R.id.postavi_sliku);
        view = findViewById(R.id.ram);
        spinner = findViewById(R.id.spinner_dogadjaj);
        eventsList = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user",user);
        editor.apply();
        if(user.contains("admin") || user.contains("Admin")){

            switch (user){
                case "KruskaAdmin":
                    mesto = "KruskaPab";
                    break;
                case "BasementAdmin":
                    mesto = "BasementBar";
                    break;
                case "SalsaAdmin":
                    mesto = "SalsaBar";
                    break;
                case "DuomoAdmin":
                    mesto = "Duomo";
                    break;
            }
            SharedPreferences sharedPreferences1 = getSharedPreferences("mesto", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putString("mesto",mesto);
            editor1.apply();

        }
        dodajsliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);


            }
        });


        List<String> items = new ArrayList<>();
        items.add("Zurka");
        items.add("Svirka");
        items.add("Predstava");
        items.add("Koncert");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = items.get(position);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        opis = findViewById(R.id.opisEditText);
        spinner = findViewById(R.id.spinner_dogadjaj);
        editTextMonth = findViewById(R.id.editTextMonth);
        editTextDay = findViewById(R.id.editTextDay);
        editTextYear = findViewById(R.id.editTextYear);
        pocetakEditText = findViewById(R.id.vreme_pocetak);
        krajEditText = findViewById(R.id.vreme_kraj);
        nazivEditText = findViewById(R.id.nazivEditText);
        opisEditText = findViewById(R.id.opisEditText);
        dodajbtn = findViewById(R.id.dodajbtn);

        dodajbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = nazivEditText.getText().toString();
                String eventDate = editTextDay.getText().toString() + "-" +
                        editTextMonth.getText().toString() + "-" +
                        editTextYear.getText().toString();
                String eventTime = pocetakEditText.getText().toString() + "-" + krajEditText.getText().toString();
                String eventDetail = opis.getText().toString();
                String tip = spinner.getSelectedItem().toString();
                Bitmap bitmap = ((BitmapDrawable) postavisliku.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                checkDateValidity();
                checkTimeValidity(eventTime);

                if(bol1 && bol2){

                    Intent intent = new Intent(AddEventActivity.this, Dogadjaji.class);
                    intent.putExtra("eventTitle", eventTitle);
                    intent.putExtra("eventDate", eventDate);
                    intent.putExtra("eventTime", eventTime);
                    intent.putExtra("encodedImage", byteArray);
                    intent.putExtra("eventType", tip);
                    intent.putExtra("eventDetail", eventDetail);
                    intent.putExtra("mesto",mesto);
                    startActivity(intent);
                    finish();
                } else{
                    checkTime();
                    checkDate();
                    return;
                }


            }




        });


    }

    private void checkTime(){
        if(bol1 == false) {
            Toast.makeText(this,"Unesite validno vreme", Toast.LENGTH_SHORT).show();

        }
    }
    private void checkDate(){
        if(bol2 == false){
            Toast.makeText(this,"Unesite validan datum", Toast.LENGTH_SHORT).show();

        }
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                int targetedWidth = postavisliku.getWidth();
                int targetedHeight = postavisliku.getHeight();

                bitmap = Bitmap.createScaledBitmap(bitmap, targetedWidth, targetedHeight, false);

                postavisliku.setImageBitmap(bitmap);
                dodajsliku.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String checkTimeValidity(String eventTime1){

         String eventTime2 = pocetakEditText.getText().toString();
         String eventTime3 = krajEditText.getText().toString();

        try {
            int start = Integer.parseInt(eventTime2);
            int end = Integer.parseInt(eventTime3);

            if (start >= 0 && start <= 24 && end >= 0 && end <= 24) {
                bol1 = true;
                return eventTime1;

            } else {
                bol1 = false;
                return  null;
            }

        } catch (NumberFormatException e) {

        }
        return null;
    }










    private void checkDateValidity() {
        // Get the text from the EditTexts
        String monthText = editTextMonth.getText().toString();
        String dayText = editTextDay.getText().toString();
        String yearText = editTextYear.getText().toString();

        if (monthText.equals("") || dayText.equals("") || yearText.equals("")) {
            Toast.makeText(this, "Molimo Vas popunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate month, day, and year input
        int month, day, year;
        try {
            month = Integer.parseInt(monthText);
            day = Integer.parseInt(dayText);
            year = Integer.parseInt(yearText);
            if (month < 1 || month > 12 || day < 1 || day > 31 || year < 2024 || year > 2024) {
                Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
                bol2 = false;
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            bol2 = false;
            return;
        }

        // Create a date from the input
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String inputDateString = String.format("%02d-%02d-%04d", day, month, year);
        Date inputDate;
        try {
            inputDate = sdf.parse(inputDateString);
        } catch (ParseException e) {
            Toast.makeText(this, "Error parsing date", Toast.LENGTH_SHORT).show();
            bol2 = false;
            return;
        }

        // Get the current date without the time part
        Date currentDate = new Date();
        try {
            currentDate = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Compare the input date with the current date
        if (inputDate.before(currentDate)) {
             Toast.makeText(this, "Date is in the past", Toast.LENGTH_SHORT).show();
            bol2 = false;
        } else {
            bol2 = true;
        }
    }



    @Override
    public void onResponse(String result) {


        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}


