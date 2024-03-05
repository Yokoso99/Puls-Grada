package com.example.prototype2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class Register extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText confirmpassword;
    EditText phonenumber;

    MaterialButton register;
    DBHelper DB;
    AdminDB adminDB;

    int phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.emailtxt);
        password = findViewById(R.id.passwordtxt);
        confirmpassword = findViewById(R.id.password_confirm);
        register = findViewById(R.id.register_btn);
        phonenumber = findViewById(R.id.phone_number);
        DB = new DBHelper(this);
        adminDB = new AdminDB(this);




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phonenumber.getText().toString();
                phone = Integer.parseInt(number);

                String user = username.getText().toString();
                String emaila = email.getText().toString();
                String pass = password.getText().toString();
                String repass = confirmpassword.getText().toString();

                char[] numbersAndSymbols = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '+', '=', '[', ']', '{', '}', ';', ':', ',', '.', '/', '<', '>', '?', '|', '~'};

                if (user.equals("") || emaila.equals("") || pass.equals("") || number.equals("")) {
                    Toast.makeText(Register.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else if (user.contains("admin") || user.contains("Admin")) {
                    if(pass.equals(repass)) {
                        boolean containsSymbol = false;
                        for (char symbol : numbersAndSymbols) {
                            if (user.contains(String.valueOf(symbol))) {
                                containsSymbol = true;
                                break;
                            }
                        }
                        if (containsSymbol) {
                            Toast.makeText(Register.this, "Nevalidan username", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Boolean insert = adminDB.insertData(user, pass, phone);
                    if (insert) {
                        Toast.makeText(Register.this, "Registered successfully as admin", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Vec postoji slican korisnik", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {


                    if (pass.equals(repass) || user.contains("admin") || user.contains("Admin")) {
                        finish();
                        Boolean checkuser = DB.checkusername(user, emaila, phone);
                        if (checkuser == false) {

                            Boolean insert = DB.insertData(user, pass, emaila, phone);
                            if (insert == true) {
                                Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "User already exists please sign in", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(Register.this, "Password not matching", Toast.LENGTH_SHORT).show();

                    }

                    finish();
                }
            }
        });






    }
}