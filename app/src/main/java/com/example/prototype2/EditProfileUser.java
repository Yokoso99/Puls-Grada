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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.protobuf.Int64Value;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class EditProfileUser extends AppCompatActivity {


    ShapeableImageView profil;

    EditText imeprez,mobilni,starasifra,novasifra,brojgodina,username;
    MaterialButton savebtn;
    DBHelper dbHelper;
    String imePrez,Mobilni,staraSifra,novaSifra,brojGodina,userName;

    String[] values;
    int BROJGODINA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);
        profil = findViewById(R.id.profil);
        savebtn = findViewById(R.id.sacuvajtbn);
        imeprez = findViewById(R.id.imeprez);
        mobilni = findViewById(R.id.mobilni);
        starasifra = findViewById(R.id.sifra);
        novasifra = findViewById(R.id.novasifra);
        brojgodina = findViewById(R.id.brojgodina);
        username = findViewById(R.id.username);


        dbHelper = new DBHelper(this);



        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","");
        if(!user.contains("Admin")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(dbHelper.ifUserExists(user)){
                        values = dbHelper.getValues(user);
                        imeprez.setText(values[0]);
                        starasifra.setText(values[1]);
                        mobilni.setText(values[2]);
                        brojgodina.setText(values[4]);
                        username.setText(values[5]);

                        if (values[3] != null && !values[3].isEmpty()) {
                            byte[] slikaa = Base64.decode(values[3], Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(slikaa, 0, slikaa.length);
                            profil.setImageBitmap(bitmap);


                        }else{
                            return;
                        }
                    }

                }
            });
        }

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                imePrez = imeprez.getText().toString();
                Mobilni = mobilni.getText().toString();
                staraSifra = starasifra.getText().toString();
                novaSifra = novasifra.getText().toString();
                userName = username.getText().toString();
                BROJGODINA = Integer.parseInt(brojgodina.getText().toString());
                if(staraSifra != novaSifra){
                    insertIntoDB(user);
                }
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.with(EditProfileUser.this)
                        .crop()
                        .compress(5120)
                        .maxResultSize(1920,1920)
                        .start();
            }
        });
    }

    private void insertIntoDB(String user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("username",userName);
        contentValues.put("imeprez",imePrez);
        contentValues.put("phoneNumber",Mobilni);
        contentValues.put("password",staraSifra);
        contentValues.put("novasifra",novaSifra);
        contentValues.put("brojgodina",BROJGODINA);

        Bitmap bitmap = ((BitmapDrawable) profil.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        if(byteArray == null){

            return;
        }else {
            contentValues.put("profilePic",byteArray);
        }

        db.update("users", contentValues, "username = ?", new String[]{user});
        db.close();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            Uri selectedImage = data.getData();
            profil.setImageURI(selectedImage);

    }

}