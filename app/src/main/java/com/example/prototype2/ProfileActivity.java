package com.example.prototype2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

public class ProfileActivity extends AppCompatActivity {

    AppCompatButton editbtn;
    ShapeableImageView slika;


    LinearLayout logout;
    TextView ime,mobilni,username;
    AdminDB adminDB;
    DBHelper dbHelper;
    String[] values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        slika = findViewById(R.id.slikaprofila);

        logout = findViewById(R.id.logoutlinear);
        editbtn = findViewById(R.id.editbtn);
        ime = findViewById(R.id.ime);
        mobilni = findViewById(R.id.mobilni);
        username = findViewById(R.id.username);
        adminDB = new AdminDB(this);
        dbHelper = new DBHelper(this);





        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","");

        if(user.contains("admin") || user.contains("Admin")){


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(adminDB.ifUserExists(user)){
                        values = adminDB.getValues(user);
                        ime.setText(values[0]);
                        mobilni.setText(values[1]);
                        username.setText(values[9]);

                        if (values[8] != null && !values[8].isEmpty()) {
                            byte[] slikaa = Base64.decode(values[8], Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(slikaa, 0, slikaa.length);

                            int height = 400;


                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height);
                            slika.setLayoutParams(params);
                            ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                                    .toBuilder()
                                    .setAllCorners(CornerFamily.ROUNDED, 30) // Set your desired corner radius here
                                    .build();


                            slika.setShapeAppearanceModel(shapeAppearanceModel);
                            slika.setImageBitmap(bitmap);
                            editbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProfileActivity.this, EditProfile.class);
                                    startActivity(intent);

                                }
                            });



                        } else {

                            return;
                        }
                    }
                }
            });
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlogoutDialog();

            }
        });


        if(!user.contains("admin") || !user.contains("Admin")){



                    if (dbHelper.ifUserExists(user)) {
                        values = dbHelper.getValues(user);
                        ime.setText(values[5]);
                        mobilni.setText(values[2]);
                        username.setText(values[5]);

                        if (values[3] != null && !values[3].isEmpty()) {
                            byte[] slikaa = Base64.decode(values[3], Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(slikaa, 0, slikaa.length);
                            slika.setImageBitmap(bitmap);


                        }

                        editbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ProfileActivity.this, EditProfileUser.class);
                                startActivity(intent);

                            }
                        });
                    }



        }

    }

    private void showlogoutDialog() {



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Logout Confirmation");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performLogout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void performLogout(){
        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember","false");
        editor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putString("user",null);
        editor1.apply();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","");
        if(user.contains("admin") || user.contains("Admin")){


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(adminDB.ifUserExists(user)){
                        values = adminDB.getValues(user);
                        ime.setText(values[0]);
                        mobilni.setText(values[1]);
                        username.setText(values[9]);

                        if (values[8] != null && !values[8].isEmpty()) {
                            byte[] slikaa = Base64.decode(values[8], Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(slikaa, 0, slikaa.length);

                            int height = 400;


                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height);
                            slika.setLayoutParams(params);
                            ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                                    .toBuilder()
                                    .setAllCorners(CornerFamily.ROUNDED, 30) // Set your desired corner radius here
                                    .build();


                            slika.setShapeAppearanceModel(shapeAppearanceModel);
                            slika.setImageBitmap(bitmap);



                        } else {

                            return;
                        }
                    }
                }
            });
        } else{
            if (dbHelper.ifUserExists(user)) {
                values = dbHelper.getValues(user);
                ime.setText(values[0]);
                mobilni.setText(values[2]);
                username.setText(values[5]);

                if (values[3] != null && !values[3].isEmpty()) {
                    byte[] slikaa = Base64.decode(values[3], Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(slikaa, 0, slikaa.length);
                    slika.setImageBitmap(bitmap);


                }

            }

        }
    }
}

