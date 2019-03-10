package com.example.ajwl.locationtracking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by User on 10/2/2017.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    FirebaseAuth firebaseAuth;
    public String friendRef;

    public void setFriendRef(String friendRef) {
        this.friendRef = friendRef;
    }
    public String getFriendRef() {
        return friendRef;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);

        }
    }

    private static final String TAG = "MapsActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent extras = getIntent();
        getLocationPermission();
        if (extras != null) {
            String emailFriend = extras.getStringExtra("emailFriend");
            setFriendRef(emailFriend);
            Log.i("11111111oncreate", getFriendRef());
        }
        thread.start();
    }

    boolean keepRunning = true;
     Thread thread = new Thread() {
        @Override
        public void run() {
            Log.d("111111111111", "getDeviceLocation: getting the devices current location");

            firebaseAuth = FirebaseAuth.getInstance();
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            while (keepRunning) {
                try {
                    if (mLocationPermissionsGranted) {
                        final Task location = mFusedLocationProviderClient.getLastLocation();
                        location.addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful()) {
                                    Log.d("11111111", "onComplete: found location!");
                                    Location currentLocation = (Location) task.getResult();

                                    if (currentLocation != null && firebaseAuth != null) {
                                        //moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                                        DatabaseReference idRef, name, latitudeRef, longtitudeRef;
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                        idRef = ref.child("ID_all_user");
                                        name = idRef.child(firebaseAuth.getCurrentUser().getEmail().replace(".", "?"));
                                        latitudeRef = name.child("latitude");
                                        latitudeRef.setValue(currentLocation.getLatitude());
                                        longtitudeRef = name.child("longtitude");
                                        longtitudeRef.setValue(currentLocation.getLongitude());

                                        idRef.child(getFriendRef()).addValueEventListener(new ValueEventListener() {
                                            int counter = 0;
                                            double friendLat, friendLong;

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                                    Log.i("11111chidcount", String.valueOf(dsp.getChildren()));
                                                    Log.i("11111lfrlat", getFriendRef());

                                                    switch (dsp.getKey()){
                                                        case("latitude"):friendLat = Double.parseDouble(dsp.getValue().toString());
                                                        case("longtitude"):friendLong = Double.parseDouble(dsp.getValue().toString());
                                                    }
                                                }
                                                Log.i("11111lLat", String.valueOf(friendLat));
                                                Log.i("11111lLong", String.valueOf(friendLong));
                                                //moveCamera(new LatLng(friendLat,friendLong),DEFAULT_ZOOM);
                                                LatLng partnerPos = new LatLng(friendLat, friendLong);
                                                String [] k = getFriendRef().split("\\W+");
                                                Log.i("11111lLong", k[0]);

                                                mMap.addMarker(new MarkerOptions().position(new LatLng(friendLat, friendLong)).title(k[0]));
                                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(partnerPos);
                                                mMap.animateCamera(cameraUpdate);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    Log.d(TAG, "onComplete: current location is null");
                                    Toast.makeText(MapsActivity.this, "unable to get current location",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (SecurityException e) {
                    Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }


}

