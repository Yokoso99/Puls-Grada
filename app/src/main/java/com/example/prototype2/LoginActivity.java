package com.example.prototype2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;


public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private MaterialButton loginbtn,registerbtn;
    DBHelper DB;
    AdminDB admindb;
    CheckBox remember;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.passwordtxt);
        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.register_btn);
        remember = findViewById(R.id.checkbox);
        DB = new DBHelper(this);
        admindb = new AdminDB(this);
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = sharedPreferences.getString("remember","");
        if(checkbox.equals("true")){
            Intent intent = new Intent(LoginActivity.this, Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }


        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){
                    SharedPreferences sharedPreferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();



                }else if(!compoundButton.isChecked()){
                    SharedPreferences sharedPreferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();

                }
            }
        });
 

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String user = username.getText().toString();
                 String pass = password.getText().toString();

                 if(user.equals("") || pass.equals(""))
                     Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                 else{
                     Boolean checkuserpass = DB.checkusernamepassword(user,pass);
                     Boolean adminDB = admindb.checkusernamepassword(user,pass);

                     if(checkuserpass){

                         Toast.makeText(LoginActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                          if(remember.isChecked()){
                              SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                              SharedPreferences.Editor editor = sharedPreferences.edit();
                              editor.putString("remember", "true");
                              editor.apply();
                          } else {
                              // Clear login state
                              SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                              SharedPreferences.Editor editor = sharedPreferences.edit();
                              editor.putString("remember", "false");
                              editor.apply();
                          }

                          SharedPreferences sharedPreferences1 = getSharedPreferences("username",MODE_PRIVATE);
                          SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                          editor1.putString("user",user);
                          editor1.apply();

                          Intent intent = new Intent(LoginActivity.this, Main.class);
                          startActivity(intent);
                          finish();

                     }else{
                         if(adminDB)
                         {
                             SharedPreferences sharedPreferences2 = getSharedPreferences("username",MODE_PRIVATE);
                             SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                             editor2.putString("user", user);
                             editor2.apply();
                             if (remember.isChecked()) {
                                 SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                                 SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                 editor1.putString("remember", "true");
                                 editor1.apply();

                             } else {

                                 SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                                 SharedPreferences.Editor editor3 = sharedPreferences.edit();
                                 editor3.putString("remember", "false");
                                 editor3.apply();
                             }
                         }else{
                             return;
                         }
                         Intent intent = new Intent(LoginActivity.this, Main.class);
                         startActivity(intent);
                         finish();

                     }
                 }


            }

        });





        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });









    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
