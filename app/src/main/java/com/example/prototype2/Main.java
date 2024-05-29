package com.example.prototype2;

        import android.app.Activity;
        import android.content.ContentValues;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.location.LocationProvider;
        import android.os.Bundle;
        import android.content.Context;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.view.View;
        import android.view.Window;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ListView;
        import android.widget.SearchView;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.libraries.places.api.model.AutocompletePrediction;
        import com.google.android.libraries.places.api.model.Place;
        import com.google.android.libraries.places.api.Places;
        import com.google.android.libraries.places.api.model.PlaceLikelihood;
        import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
        import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
        import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
        import com.google.android.libraries.places.api.net.PlacesClient;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;


public class Main extends AppCompatActivity {

    String kruskapab;
    String basement;
    String duomo;
    String salsa;
    String bascelik;
    String irishpub;
    String pozoriste;
    String kck;

    RecyclerView recyclerView;
    SearchView searchView;
    ImageButton buttonOpenNewPage,buttonOpenNewPage1,buttonOpenNewPage2,buttonOpenNewPage5;



    Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_bottom);

        kruskapab = getString(R.string.kruskapab);
        basement = getString(R.string.basementbar);
        duomo = getString(R.string.duomo);
        salsa = getString(R.string.salsa);
        bascelik = getString(R.string.bas_celik);
        irishpub = getString(R.string.irish_pub);
        pozoriste = getString(R.string.pozoriste);
        kck = getString(R.string.kck);

        List<Item> item = new ArrayList<Item>();
        item.add(new Item(kruskapab,R.drawable.slika1));
        item.add(new Item(basement,R.drawable.slika2));
        item.add(new Item(duomo,R.drawable.slika3));
        item.add(new Item(salsa,R.drawable.slika4));
        item.add(new Item(bascelik,R.drawable.slika5));
        item.add(new Item(irishpub,R.drawable.slika6));
        item.add(new Item(pozoriste,R.drawable.slika7));
        item.add(new Item(kck,R.drawable.slika8));

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerview);
        buttonOpenNewPage = findViewById(R.id.nav_add_events);
        buttonOpenNewPage1 = findViewById(R.id.nav_map);
        buttonOpenNewPage2 = findViewById(R.id.nav_history);
        buttonOpenNewPage5 = findViewById(R.id.nav_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(getApplicationContext(), item);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String user = sharedPreferences.getString("user","");
        editor.putString("user",user);
        editor.apply();

        if(!user.contains("admin") && !user.contains("Admin")){
            buttonOpenNewPage.setVisibility(View.GONE);
        }


        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                String itemName = item.getName();
                Intent intent = null;

                if(itemName.equals(kruskapab)){
                    intent = new Intent(Main.this, KruskaPab.class);
                }  else if (itemName.equals(salsa)) {
                    intent = new Intent(Main.this, SalsaBar.class);
                }  else if (itemName.equals(basement)) {
                    intent = new Intent(Main.this, BasementBar.class);

                } else if (itemName.equals(duomo)){
                    intent = new Intent(Main.this, Duomo.class);

                } else if (itemName.equals(bascelik)){

                } else if (itemName.equals(pozoriste)){

                } else if (itemName.equals(kck)){

                }
                startActivity(intent);
            }
        });






        buttonOpenNewPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Main.this, AddEventActivity.class);
                startActivity(intent);


            }


        });




        buttonOpenNewPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, MapActivity.class);
                startActivity(intent);

            }
        });


        buttonOpenNewPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, UpcomingAndPassed.class);
                startActivity(intent);

            }
        });




        buttonOpenNewPage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, ProfileActivity.class);
                startActivity(intent);

            }
        });


        View circularButton = findViewById(R.id.circular_button);

        recyclerView.setVisibility(View.GONE);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return true;
            }
        });


        circularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchView.getVisibility() == View.VISIBLE) {
                    searchView.setVisibility(View.GONE);
                } else {

                    searchView.setVisibility(View.VISIBLE);
                }
            }
        });


    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearFocus();

    }
}








