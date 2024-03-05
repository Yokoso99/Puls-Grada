package com.example.prototype2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    MaterialButton savebtn;

    ImageView dodajsliku,postavisliku;
    EditText naziv,adresa,ponpet,subota,nedelja,opis,mobilni;
    int GET_FROM_GALLERY = 3;

    Spinner spinner;
    View view;
    String nazivv,adresaa,ponpett,subotaa,nedeljaa,opiss,tip,mobilnii;
    String[] values;
    Boolean bo;
    AdminDB adminDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        savebtn = findViewById(R.id.sacuvajtbn);
        naziv = findViewById(R.id.nazivLokala);
        adresa = findViewById(R.id.adresaLokala);
        ponpet = findViewById(R.id.ponpet);
        subota = findViewById(R.id.subota);
        nedelja = findViewById(R.id.nedelja);
        opis = findViewById(R.id.opisEditText);
        spinner = findViewById(R.id.spinner_dogadjaj);
        dodajsliku = findViewById(R.id.dodaj_sliku);
        postavisliku = findViewById(R.id.postavi_sliku);
        view =  findViewById(R.id.ram);
        mobilni = findViewById(R.id.mobilni);
        adminDB = new AdminDB(this);


        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","");

        if(user.contains("admin") || user.contains("Admin")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(adminDB.ifUserExists(user)){
                        values = adminDB.getValues(user);
                        naziv.setText(values[0]);
                        mobilni.setText(values[1]);
                        adresa.setText(values[3]);
                        ponpet.setText(values[4]);
                        subota.setText(values[5]);
                        nedelja.setText(values[6]);
                        opis.setText(values[7]);
                        if (values[8] != null && !values[8].isEmpty()) {
                            byte[] slika = Base64.decode(values[8], Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(slika, 0, slika.length);
                            postavisliku.setImageBitmap(bitmap);
                            dodajsliku.setVisibility(View.GONE);
                            view.setVisibility(View.GONE);
                        } else {

                            return;
                        }

                    }
                }
            });



        }










        dodajsliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);


            }
        });

        postavisliku.setOnClickListener(new View.OnClickListener() {
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

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nazivv = naziv.getText().toString();
                adresaa = adresa.getText().toString();
                opiss = opis.getText().toString();
                ponpett = ponpet.getText().toString();
                subotaa = subota.getText().toString();
                nedeljaa = nedelja.getText().toString();
                tip = spinner.getSelectedItem().toString();
                mobilnii = mobilni.getText().toString();

                if(checkTime(ponpett,subotaa,nedeljaa)){

                    insertIntoDB(user);
                }else{
                    showToast();
                    return;
                }



            }

        });




    }

    private void insertIntoDB(String user) {
        SQLiteDatabase db = adminDB.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("imelokala",nazivv);
        values.put("adresa",adresaa);
        values.put("detalji",opiss);
        values.put("ponpet",ponpett);
        values.put("subota",subotaa);
        values.put("nedelja",nedeljaa);
        values.put("broj",mobilnii);
        values.put("tip",tip);
        Bitmap bitmap = ((BitmapDrawable) postavisliku.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        if(byteArray == null){

        }else {
            values.put("slika",byteArray);
        }

        db.update("Admini", values, "ime = ?", new String[]{user});
        db.close();



    }

    private void showToast() {
        Toast.makeText(this,"Radno vreme nije validno", Toast.LENGTH_SHORT).show();
    }

    private boolean checkTime(String ponpet, String subota, String nedelja) {
        String[] ponpetParts = ponpet.split("-");
        String[] subotaParts = subota.split("-");
        String[] nedeljaParts = nedelja.split("-");

        // Check if each part of the time is within the valid range
        if (isValidTimePart(ponpetParts) && isValidTimePart(subotaParts) && isValidTimePart(nedeljaParts)) {
            bo = true;
            return true;

        } else {
            return false; // Time is not valid
        }
    }
    private boolean isValidTimePart(String[] timeParts) {
        // Check if the time parts array has exactly 2 parts
        if (timeParts.length != 2) {
            return false;
        }

        try {
            int hour = Integer.parseInt(timeParts[0]);
            int hour2 = Integer.parseInt(timeParts[1]);

            // Check if hour is between 0 and 23 and minute is between 0 and 59
            if (hour >= 0 && hour <= 23 && hour2 >= 0 && hour2 <= 24) {

                return true;
            } else {
                return false; // Time part is invalid
            }
        } catch (NumberFormatException e) {
            // Parsing error occurred
            return false;
        }
    }

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
}