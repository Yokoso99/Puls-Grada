package com.example.prototype2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    AppCompatButton editbtn,deletebtn;
    String stari_naslov,novi_naslov,detalji,novi_detalji;
    int updatedPosition;
    boolean bol,update;
    byte [] NewImage;
    byte[] slikaa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogadjaji_detalji);

        editbtn = findViewById(R.id.editbtn);
        opis = findViewById(R.id.opis);
        naslov = findViewById(R.id.naslov);
        slika_add = findViewById(R.id.slika_add);
        slika_delete = findViewById(R.id.slika_delete);
        slika_add.setVisibility(View.GONE);
        slika_delete.setVisibility(View.GONE);
        deletebtn = findViewById(R.id.delete_btn);

        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","");

        if(!user.contains("admin") && !user.contains("Admin")){
            editbtn.setVisibility(View.GONE);
        }




        slika = findViewById(R.id.slika);
        Intent intent = getIntent();
        if (intent != null) {
            String eventTitle = intent.getStringExtra("eventTitle");
            String eventDate = intent.getStringExtra("eventDate");
            String eventTime = intent.getStringExtra("eventTime");
            String eventDetail = intent.getStringExtra("eventDetail");
            String eventTip = intent.getStringExtra("eventTip");
            Bitmap encodedImage = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("encodedImage"), 0, getIntent().getByteArrayExtra("encodedImage").length);



            naslov.setText(eventTitle);
            stari_naslov = naslov.getText().toString();
            opis.setText(eventDetail);
            slika.setImageBitmap(encodedImage);
            detalji = opis.getText().toString();
        }


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

                }
                if(!opis.isEnabled()){

                    bol = true;
                    editbtn.setText("save");
                    opis.setEnabled(true);
                    naslov.setEnabled(true);
                    deletebtn.setVisibility(View.VISIBLE);
                    slika_add.setVisibility(View.VISIBLE);
                    slika_delete.setVisibility(View.VISIBLE);
                }else {
                    editbtn.setText("Edit");
                    opis.setEnabled(false);
                    naslov.setEnabled(false);

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

    private void updateDataBase(String currentTitle, String newTitle, String oldDetails,String newDetails) {

        if (!currentTitle.equals(newTitle) || !newDetails.equals(oldDetails)) {
            DBDogadjaji dbHelper = new DBDogadjaji(this);
            boolean isUpdated = dbHelper.updateRecord(currentTitle, newTitle, newDetails);

            if (isUpdated) {
                Toast.makeText(this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("updatedItem", newTitle);
                intent.putExtra("updatedPosition", updatedPosition); // Pass the position of the updated item
                setResult(Activity.RESULT_OK, intent);


            } else {
                Toast.makeText(this, "Failed to update record", Toast.LENGTH_SHORT).show();
            }
            update = false;
        } else if (!currentTitle.equals(newTitle) || !newDetails.equals(oldDetails) || update) {
            DBDogadjaji dbHelper = new DBDogadjaji(this);
            boolean isUpdated = dbHelper.updateRecord(currentTitle, newTitle, newDetails);

            if (isUpdated) {
                Toast.makeText(this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("updatedItem", newTitle);
                intent.putExtra("updatedPosition", updatedPosition); // Pass the position of the updated item
                setResult(Activity.RESULT_OK, intent);

            }
        }
    }
    private void updateImage(byte[] newimage,String title){
        if(update) {
            DBDogadjaji dbHelper = new DBDogadjaji(this);
            boolean updateImage = dbHelper.updateImage(title, newimage);
            if (updateImage) {
                Toast.makeText(this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("updatedItem", newimage);
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