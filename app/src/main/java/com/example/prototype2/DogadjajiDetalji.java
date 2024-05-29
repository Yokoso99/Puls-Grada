package com.example.prototype2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DogadjajiDetalji extends AppCompatActivity{

    EditText opis;
    int GET_FROM_GALLERY = 3;

    EditText naslov;

    ImageView slika;

    ImageView slika_add,slika_delete;
    int i = 0;
    AppCompatButton editbtn,deletebtn,upisi_me_btn;
    String stari_naslov,novi_naslov,detalji,novi_detalji;
    int updatedPosition;
    boolean bol,update;
    String ime;
    byte [] NewImage;
    byte[] slikaa;

    boolean provera,exists;
    LottieAnimationView animationView;

    String eventTitle,eventDate,eventTime,eventTip,eventDetail,user;
    Bitmap encodedImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogadjaji_detalji);

        animationView = findViewById(R.id.animationView);

        editbtn = findViewById(R.id.editbtn);
        opis = findViewById(R.id.opis);
        naslov = findViewById(R.id.naslov);
        slika_add = findViewById(R.id.slika_add);
        slika_delete = findViewById(R.id.slika_delete);
        slika_add.setVisibility(View.GONE);
        slika_delete.setVisibility(View.GONE);
        deletebtn = findViewById(R.id.delete_btn);
        upisi_me_btn = findViewById(R.id.upisi_me_btn);
        upisi_me_btn.setVisibility(View.GONE);
        AdminDB adminDB = new AdminDB(this);
        DBDogadjaji dbDogadjaji = new DBDogadjaji(this);
        UpcAndPassDB upcAndPassDB = new UpcAndPassDB(this);




        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        user = sharedPreferences.getString("user","");

        SharedPreferences sharedPreferences1 = getSharedPreferences("mesto",MODE_PRIVATE);
        ime = sharedPreferences1.getString("mesto","");









        if(!user.contains("admin") && !user.contains("Admin")){
            editbtn.setVisibility(View.GONE);
            upisi_me_btn.setVisibility(View.VISIBLE);
        }


        slika = findViewById(R.id.slika);
        Intent intent = getIntent();
        if (intent != null) {
            eventTitle = intent.getStringExtra("eventTitle");
            eventDate = intent.getStringExtra("eventDate");
            eventTime = intent.getStringExtra("eventTime");
            eventDetail = intent.getStringExtra("eventDetail");
            eventTip = intent.getStringExtra("eventTip");
            encodedImage = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("encodedImage"), 0, getIntent().getByteArrayExtra("encodedImage").length);



            naslov.setText(eventTitle);
            stari_naslov = naslov.getText().toString();
            opis.setText(eventDetail);
            slika.setImageBitmap(encodedImage);
            detalji = opis.getText().toString();
            editbtn.setVisibility(View.GONE);
        }
        upisi_me_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(upcAndPassDB.checkIfExist(eventTitle,eventDate)){

                    exists = true;
                    if(exists){
                        Toast.makeText(DogadjajiDetalji.this, "Upisani ste vec na dogadjaj", Toast.LENGTH_SHORT).show();

                    }
                    return;
                }else {


                    insertIntoDB();
                    upisi_me_btn.setVisibility(View.GONE);
                    animationView.setVisibility(View.VISIBLE);
                    animationView.playAnimation();
                }



            }

        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!adminDB.ifUserExists(user)){

                    editbtn.setVisibility(View.GONE);
                    provera = false;



                } else{

                    String m;
                    String a = "";
                    a = naslov.getText().toString();
                    m = dbDogadjaji.checkTable(a);

                    if(user.contains("Kruska") && m.contains("Kruska")){
                        editbtn.setVisibility(View.VISIBLE);
                        provera = true;
                    }
                    if(user.contains("Salsa") && m.contains("Salsa")){
                        editbtn.setVisibility(View.VISIBLE);
                        provera = true;
                    }
                    if(user.contains("Basement") && m.contains("Basement")){
                        editbtn.setVisibility(View.VISIBLE);
                        provera = true;
                    }
                    if(user.contains("Duomo") && m.contains("Duomo")){
                        editbtn.setVisibility(View.VISIBLE);
                        provera = true;
                    }
                }
            }
        });


        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DogadjajiDetalji.this);
                builder.setTitle("Delete Event");
                builder.setMessage("Are you sure you want to delete this event?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        deleteFromDataBase(stari_naslov);


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If the user cancels, dismiss the dialog
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if(bol){




                        novi_naslov = naslov.getText().toString();
                        novi_detalji = opis.getText().toString();
                        slika_add.setVisibility(View.GONE);
                        slika_delete.setVisibility(View.GONE);
                        deletebtn.setVisibility(View.GONE);

                        BitmapDrawable drawable = (BitmapDrawable) slika.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        slikaa = stream.toByteArray();
                        updateDataBase(stari_naslov,novi_naslov,detalji,novi_detalji);
                        updateImage(NewImage,stari_naslov);
                        editbtn.setText("Edit");
                        naslov.setEnabled(false);
                        opis.setEnabled(false);

                }
                if(!opis.isEnabled() && provera){

                    bol = true;
                    editbtn.setText("save");
                    opis.setEnabled(true);
                    naslov.setEnabled(true);
                    deletebtn.setVisibility(View.VISIBLE);
                    slika_add.setVisibility(View.VISIBLE);
                    slika_delete.setVisibility(View.VISIBLE);
                }else if(!provera){
                    editbtn.setVisibility(View.GONE);


                }
            }
        });

        slika_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),GET_FROM_GALLERY);

            }
        });
        slika_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (slika.getDrawable() != null) {
                    // Create a dialog asking for confirmation
                    AlertDialog.Builder builder = new AlertDialog.Builder(DogadjajiDetalji.this);
                    builder.setTitle("Delete Image");
                    builder.setMessage("Are you sure you want to delete the image?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            slika.setImageBitmap(null);

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // If the user cancels, dismiss the dialog
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // If there is no image in the ImageView, you may want to show a message to the user
                    Toast.makeText(DogadjajiDetalji.this, "There is no image to delete", Toast.LENGTH_SHORT).show();

                }



            }
        });



    }
    private void animateButton() {
        // First part of animation: shrink and move to the middle
        ObjectAnimator translateXStart = ObjectAnimator.ofFloat(upisi_me_btn, "translationX", -1000);
        ObjectAnimator translateYStart = ObjectAnimator.ofFloat(upisi_me_btn, "translationY", 1000);
        ObjectAnimator scaleXStart = ObjectAnimator.ofFloat(upisi_me_btn, "scaleX", 0.5f);
        ObjectAnimator scaleYStart = ObjectAnimator.ofFloat(upisi_me_btn, "scaleY", 0.5f);

        AnimatorSet startSet = new AnimatorSet();
        startSet.playTogether(scaleXStart, scaleYStart);
        startSet.setInterpolator(new DecelerateInterpolator());
        startSet.setDuration(1000);

        // Second part of animation: move back and grow to original size
        ObjectAnimator translateXEnd = ObjectAnimator.ofFloat(upisi_me_btn, "translationX", 0);
        ObjectAnimator translateYEnd = ObjectAnimator.ofFloat(upisi_me_btn, "translationY", 0);
        ObjectAnimator scaleXEnd = ObjectAnimator.ofFloat(upisi_me_btn, "scaleX", 1f);
        ObjectAnimator scaleYEnd = ObjectAnimator.ofFloat(upisi_me_btn, "scaleY", 1f);

        AnimatorSet endSet = new AnimatorSet();
        endSet.playTogether(scaleXEnd, scaleYEnd);
        endSet.setInterpolator(new DecelerateInterpolator());
        endSet.setDuration(1000);

        // Combine both animations into a sequence
        AnimatorSet combinedSet = new AnimatorSet();
        combinedSet.playSequentially(startSet, endSet);
        combinedSet.start();
    }

    @Override
    public void onBackPressed() {

        if(deletebtn.getVisibility() == View.VISIBLE){
            bol = false;
            deletebtn.setVisibility(View.GONE);
            slika_add.setVisibility(View.GONE);
            slika_delete.setVisibility(View.GONE);
            opis.setEnabled(false);
            naslov.setEnabled(false);

            editbtn.setText("Edit");
        } else {
            super.onBackPressed();

        }
    }

    private void deleteFromDataBase(String title) {
        DBDogadjaji dbHelper = new DBDogadjaji(this);

        boolean isDeleted = dbHelper.isDeleted(title);

        if(isDeleted){
            Intent intent = new Intent();
            intent.putExtra("deletedTitle", title);
            setResult(Activity.RESULT_OK, intent);

        }
    }

    private void insertIntoDB() {

        UpcAndPassDB upcAndPassDB = new UpcAndPassDB(this);
        SQLiteDatabase db = upcAndPassDB.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        encodedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] encodedImageBytes = byteArrayOutputStream.toByteArray();



        contentValues.put("eventTitle", eventTitle);
            contentValues.put("eventDate", eventDate);
            contentValues.put("eventTime", eventTime);
            contentValues.put("eventTip", eventTip);
            contentValues.put("eventDetail", eventDetail);
            contentValues.put("eventImage", encodedImageBytes);
            contentValues.put("username", user);
            db.insert("Upcoming",null,contentValues);

    }










    private void updateDataBase(String currentTitle, String newTitle, String oldDetails,String newDetails) {

        if (!currentTitle.equals(newTitle)) {
            DBDogadjaji dbHelper = new DBDogadjaji(this);
            boolean isUpdated = dbHelper.updateRecord(currentTitle, newTitle, newDetails, ime);

            if (isUpdated) {
                Intent intent = new Intent();
                intent.putExtra("updatedItem", newTitle);
                intent.putExtra("updatedDetail", newDetails);
                intent.putExtra("ime", ime);
                intent.putExtra("updatedPosition", updatedPosition); // Pass the position of the updated item
                setResult(Activity.RESULT_OK, intent);
                isUpdated = false;


            }
        }
        if (!newDetails.equals(oldDetails)) {
            DBDogadjaji dbHelper = new DBDogadjaji(this);
            boolean isUpdated = dbHelper.updateRecord(currentTitle, newTitle, newDetails, ime);

            if (isUpdated) {
                Intent intent = new Intent();
                intent.putExtra("updatedItem", newTitle);
                intent.putExtra("updatedDetail", newDetails);
                intent.putExtra("ime", ime);
                intent.putExtra("updatedPosition", updatedPosition); // Pass the position of the updated item
                setResult(Activity.RESULT_OK, intent);
                isUpdated = false;

                update = false;
            }
        }
    }



    private void updateImage(byte[] newimage,String title){
        if(update) {
            DBDogadjaji dbHelper = new DBDogadjaji(this);
            boolean updateImage = dbHelper.updateImage(title, newimage,ime);
            if (updateImage) {
                updateImage = false;
                Intent intent = new Intent();
                intent.putExtra("updatedItem", newimage);
                intent.putExtra("ime",ime);
                intent.putExtra("updatedPosition", updatedPosition); // Pass the position of the updated item
                setResult(Activity.RESULT_OK, intent);
            }
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

                int targetedWidth = slika.getWidth();
                int targetedHeight = slika.getHeight();

                bitmap = Bitmap.createScaledBitmap(bitmap, targetedWidth, targetedHeight, false);

                slika.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                NewImage = stream.toByteArray();
                update = true;





            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}