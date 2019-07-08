package com.example.domaci4.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.domaci4.R;
import com.example.domaci4.model.Location;
import com.example.domaci4.viewmodel.LocationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddLocationActivity extends AppCompatActivity {
    private LocationViewModel locationViewModel;
    private EditText title;
    private EditText date;
    private EditText descr;
    private Button add;
    private Button cancel;

    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng latLng;
    private double lng;
    private double lat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location_layout);
        init();
        initLocationClient();
    }
    private void initLocationClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
    public void init(){
        String datum = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        title = findViewById(R.id.add_title);
        date = findViewById(R.id.add_date);
        descr = findViewById(R.id.add_descr);
        add = findViewById(R.id.add_store);
        cancel = findViewById(R.id.add_cancel);
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        date.setText(datum);

        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map_add, mapFragment).commit();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setZoomControlsEnabled(true);
                getLocation();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(1, intent);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mTitle = title.getText().toString();
                String mDate = date.getText().toString();
                String mDescr = descr.getText().toString();
                if(mTitle.length() > 0 && mDate.length() > 0 && mDescr.length() > 0){
                    Location location = new Location();
                    location.setDate(mDate);
                    location.setName(mTitle);
                    location.setDesctiption(mDescr);
                    location.setLongtitude(lng);
                    location.setLatitude(lat);
                    locationViewModel.addLocation(location);
                    Intent intent = getIntent();
                    setResult(1, intent);
                    finish();
                }else{
                    showToast("Niste uneli neki od podataka!");
                }
            }
        });
    }

    private void updateMap(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Here we go"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        if (!hasAnyFeature(PackageManager.FEATURE_LOCATION, PackageManager.FEATURE_LOCATION_GPS, PackageManager.FEATURE_LOCATION_NETWORK)) {
            showToast("No feature");
            return;
        }

        if (hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                        @Override
                        public void onComplete(Task<android.location.Location> task) {
                            if (task.isSuccessful()) {
                                android.location.Location location = task.getResult();
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                                latLng = new LatLng(lat, lng);
                                updateMap(latLng);
                            } else {
                                showToast("Something went wrong");
                            }
                        }
                    });
        } else {
            requestPermissions(REQUEST_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    showToast("Permission not granted");
                }
        }
    }

    protected boolean hasAnyFeature(String... features){
        for (String feature : features) {
            if (getPackageManager().hasSystemFeature(feature)){
                return true;
            }
        }
        return false;
    }

    protected boolean hasPermissions(String... permissions){
        for (String permission : permissions) {
            boolean hasPermission = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
            if(!hasPermission) {
                return false;
            }
        }
        return true;
    }

    protected void requestPermissions(int requestCode, String... permissions){
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
