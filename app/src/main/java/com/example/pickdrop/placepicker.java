package com.example.pickdrop;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class placepicker extends AppCompatActivity  implements OnMapReadyCallback{
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, " Map is Ready", Toast.LENGTH_SHORT).show();

      mMap = googleMap;
    }

    private  static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private  static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private GoogleMap mMap;
    private  Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placepicker);

        GetLocationPermission();



    }

    private  void initMap(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(placepicker.this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode){

            case LOCATION_PERMISSION_REQUEST_CODE:{
                    if (grantResults.length>0) {
                        for (int i = 0; i < grantResults.length; i++) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                mLocationPermissionGranted = false;
                                return;
                            }
                        }
                        mLocationPermissionGranted = true;
                        //init map
                        initMap();
                    }

            }

        }
    }



    private void GetLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);

            }

        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);


        }

    }
}




