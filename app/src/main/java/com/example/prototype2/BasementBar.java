package com.example.prototype2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class BasementBar extends AppCompatActivity {

    MaterialButton pogledaj_dogadjaje;

    TextView ponpet,subota,nedelja,mobilni,adresa,naslov;
    TextView opis;
    String[] values = new String[9];
    ImageView postavisliku;
    String user;
    String mesto = "BasementBar";

    AdminDB adminDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basement_bar);

        pogledaj_dogadjaje = findViewById(R.id.pogledaj_dogadjaje);
        ponpet = findViewById(R.id.ponpetvreme);
        subota = findViewById(R.id.subvreme);
        nedelja = findViewById(R.id.nedvreme);
        mobilni = findViewById(R.id.mobilni);
        adresa = findViewById(R.id.adresa);
        opis = findViewById(R.id.movieSummary);
        naslov = findViewById(R.id.movieTitle);
        postavisliku = findViewById(R.id.imageView3);

        adminDB = new AdminDB(this);

        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        user = sharedPreferences.getString("user","");

        if(user.contains("Admin") || user.contains("admin")){


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String m = "BasementAdmin";
                        values = adminDB.getValues(m);

                        naslov.setText(values[0]);
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

                        } else {

                            return;
                        }
                    }
                });
            }



        else if(!user.contains("Admin") || !user.contains("admin")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String m = "BasementAdmin";
                    values = adminDB.getValues(m);

                    naslov.setText(values[0]);
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

                    } else {

                        return;
                    }
                }
            });
        }



        pogledaj_dogadjaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("mesto",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mesto",mesto);
                editor.apply();
                Intent intent = new Intent(BasementBar.this, Dogadjaji.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                values = adminDB.getValues(user);
            }
        });
    }

}