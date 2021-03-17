package com.example.pickdrop;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class placepicker extends AppCompatActivity implements OnMapReadyCallback {


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, " Mas Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getdevicelocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);

            init();
        }
    }






    public static final String TAG = "tag1";
    public static final String TAG2 = "tag2";
    private  static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private  static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static  final float DEFAULT_ZOOM = 15f;
    private GoogleMap mMap;
    private EditText mSearchText;
    private  Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placepicker);
        mSearchText = (EditText) findViewById(R.id.input_search);
        init();
        GetLocationPermission();



    }

    private void init(){
        Log.d(TAG,"init:Initializing");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||actionId == EditorInfo.IME_ACTION_DONE
                        ||keyEvent.getAction()==KeyEvent.ACTION_DOWN
                        ||keyEvent.getAction()==KeyEvent.KEYCODE_ENTER){

                    //execute search method
                    geoLocate();

                }
                return false;
            }
        });
    }

    private void geoLocate() {
        Log.d(TAG,"geoLocate:geolocating..");

        String searchString= mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(placepicker.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e(TAG,"geoLocate:IOException.." + e.getMessage());
        }
        if (list.size()>0){
            Address address = list.get(0);
            Log.d(TAG,"GEOLOCATION:FOUND A LOCATION" + address.toString());
           // Toast.makeText(this,address.toString(),Toast.LENGTH_SHORT).show();
        }

    }


    private void  getdevicelocation(){

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
                if (mLocationPermissionGranted){
                    Task location = mFusedLocationClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                       if (task.isSuccessful()){
                           Log.d(TAG,"onComplete: found location!");
                           Location currentlocation =(Location)task.getResult();
                           movecamera(new LatLng( currentlocation.getLatitude(),currentlocation.getLongitude()),
                                   DEFAULT_ZOOM);

                       }else{
                           Log.d(TAG,"onComplete:current location is null");
                           Toast.makeText(placepicker.this,"unable to get current location" , Toast.LENGTH_SHORT).show();

                       }
                        }
                    });
                }
        }catch (SecurityException e ){
            Log.e(TAG,"getDeviceLocation:SecurityException " + e.getMessage());
        }
    }


    private  void movecamera(LatLng latLng, float zoom){
        Log.d(TAG2,"moveCamera:moving the camera to:lat:"+latLng.latitude +",lng:"+latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
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
                initMap();
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




