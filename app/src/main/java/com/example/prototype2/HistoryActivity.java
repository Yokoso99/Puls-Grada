package com.example.prototype2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ImageButton buttonOpenNewPage = findViewById(R.id.nav_home);

        // Set a click listener for the AppCompatImageButton
        buttonOpenNewPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to open the NewPageActivity
                Intent intent = new Intent(HistoryActivity.this, Main.class);
                startActivity(intent);
                finish();
            }
        });

    }
}