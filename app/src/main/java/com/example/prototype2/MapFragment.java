package com.example.prototype2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.graphics.Color;

import com.example.prototype2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment {


    int boja_za_kulturni_sadrzaj;
    int boja_za_svirke;
    int boja_za_zurke;

    float[] hsv;
    float[] hsv2;
    float[] hsv3;

    ArrayList<Marker> markers = new ArrayList<>();




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Drawable vectorDrawable = AppCompatResources.getDrawable(getContext(),R.drawable.acoustic_guitar);
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0,0, canvas.getWidth(),canvas.getHeight());
        vectorDrawable.draw(canvas);


        Drawable vectorDrawable2 = AppCompatResources.getDrawable(getContext(),R.drawable.dj);
        Bitmap bitmap2 = Bitmap.createBitmap(
                vectorDrawable2.getIntrinsicWidth(),
                vectorDrawable2.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas2 = new Canvas(bitmap2);
        vectorDrawable2.setBounds(0,0, canvas2.getWidth(),canvas2.getHeight());
        vectorDrawable2.draw(canvas2);

        Drawable vectorDrawable3 = AppCompatResources.getDrawable(getContext(),R.drawable.theater);
        Bitmap bitmap3 = Bitmap.createBitmap(
                vectorDrawable3.getIntrinsicWidth(),
                vectorDrawable3.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas3 = new Canvas(bitmap3);
        vectorDrawable3.setBounds(0,0, canvas3.getWidth(),canvas3.getHeight());
        vectorDrawable3.draw(canvas3);

        Drawable vectorDrawable4 = AppCompatResources.getDrawable(getContext(),R.drawable.cinema);
        Bitmap bitmap4 = Bitmap.createBitmap(
                vectorDrawable4.getIntrinsicWidth(),
                vectorDrawable4.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas4 = new Canvas(bitmap4);
        vectorDrawable4.setBounds(0,0, canvas4.getWidth(),canvas4.getHeight());
        vectorDrawable4.draw(canvas4);

        Drawable vectorDrawable5 = AppCompatResources.getDrawable(getContext(),R.drawable.theater2);
        Bitmap bitmap5 = Bitmap.createBitmap(
                vectorDrawable5.getIntrinsicWidth(),
                vectorDrawable5.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas5 = new Canvas(bitmap5);
        vectorDrawable5.setBounds(0,0, canvas5.getWidth(),canvas5.getHeight());
        vectorDrawable5.draw(canvas5);


        float[] hsv = new float[3];
        float[] hsv2 = new float[3];
        float[] hsv3 = new float[3];

        boja_za_kulturni_sadrzaj = ContextCompat.getColor(requireContext(),R.color.boja_za_kulturan_sadrzaj);
        boja_za_svirke = ContextCompat.getColor(requireContext(),R.color.boja_za_svirke);
        boja_za_zurke = ContextCompat.getColor(requireContext(),R.color.boja_za_zurke);

        android.graphics.Color.colorToHSV(boja_za_kulturni_sadrzaj,hsv);
        android.graphics.Color.colorToHSV(boja_za_svirke,hsv2);
        android.graphics.Color.colorToHSV(boja_za_zurke,hsv3);

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.MY_MAP);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                LatLng cityLocation = new LatLng(43.5833, 21.3267);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(cityLocation)
                        .zoom(13)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.clear();

                ArrayList<LatLng> places = new ArrayList<>();
                places.add(new LatLng(43.580171513053415, 21.323121772649547));
                places.add(new LatLng(43.58147565520571, 21.325754111117433));
                places.add(new LatLng(43.581650587939244, 21.32602263298394));
                places.add(new LatLng(43.58301148187801, 21.326598082204164));
                places.add(new LatLng(43.5787023534325, 21.33356495284124));
                places.add(new LatLng(43.58394547001702, 21.34587227756259));
                places.add(new LatLng(43.58052234252377, 21.324351522394668));
                places.add(new LatLng(43.580911573864306, 21.3292432616706));
                places.add(new LatLng(43.58195996054952, 21.328462094680507));
                places.add(new LatLng(43.580654389063135, 21.329180579611556));

                String[] placeNames = {"Kruska Pab","Basement Bar","Salsa","Duomo Fashion Night Club"
                ,"Irish Pub","AKC Gnezdo","Bas Celik","Krusevacko Pozoriste","Bioskop Krusevac","Kulturni Centar Krusevac"};

               for (int i = 0; i< places.size();i++) {

                   LatLng place = places.get(i);
                   String placeName = placeNames[i];

                   MarkerOptions markerOptions = new MarkerOptions();
                   markerOptions.position(place);
                   markerOptions.title(placeName);

                   if (place.equals(places.get(0))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                   } else if (place.equals(places.get(1))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                   } else if (place.equals(places.get(2))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap2));
                   } else if (place.equals(places.get(3))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap2));
                   } else if (place.equals(places.get(4))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                   } else if (place.equals(places.get(5))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap2));
                   } else if (place.equals(places.get(6))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap2));
                   } else if (place.equals(places.get(7))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap3));
                   } else if (place.equals(places.get(8))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap4));
                   } else if (place.equals(places.get(9))) {
                       markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap5));
                   }

                   Marker marker = googleMap.addMarker(markerOptions);
                   markers.add(marker);
               }


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {

                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {


                        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
        });
        return view;
    }
}
