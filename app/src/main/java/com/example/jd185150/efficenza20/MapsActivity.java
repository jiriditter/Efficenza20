package com.example.jd185150.efficenza20;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String adresahriste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent y = getIntent();
        adresahriste = y.getStringExtra("adresa");
        Log.d("MapsActivity", "dostal jsem intent parametr adresa="+adresahriste);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Address> adr = null;
        Geocoder god = new Geocoder(this);
        try {
            adr = god.getFromLocationName(adresahriste, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Add a marker in Sydney and move the camera
        Address adresa = adr.get(0);
        LatLng hriste = new LatLng(adresa.getLatitude(), adresa.getLongitude());
        mMap.addMarker(new MarkerOptions().position(hriste).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hriste, 18));
    }
}
