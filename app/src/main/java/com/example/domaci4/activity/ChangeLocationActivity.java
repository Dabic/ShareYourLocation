package com.example.domaci4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.domaci4.R;
import com.example.domaci4.model.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeLocationActivity extends AppCompatActivity {
    public static String LOCATION_REFERENCE_KEY_TWO = "changeLocationKey";
    private TextView title;
    private TextView date;
    private TextView descr;
    private Button save;
    private Button cancel;
    private GoogleMap mMap;
    private String mLocationReferenceString;
    private DatabaseReference mLocationDatabaseReference;
    private Location location;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location_layout);
        init();
        initFirebase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationDatabaseReference.removeEventListener(mValueEventListener);
    }

    public void init(){
        Intent intent = getIntent();
        if (intent != null) {
            mLocationReferenceString = intent.getStringExtra(LOCATION_REFERENCE_KEY_TWO);
        }
        title = findViewById(R.id.add_title);
        date = findViewById(R.id.add_date);
        descr = findViewById(R.id.add_descr);
        save = findViewById(R.id.add_store);
        cancel = findViewById(R.id.add_cancel);

        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map_add, mapFragment).commit();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setZoomControlsEnabled(true);
                double lat = location.getLatitude();
                double lon = location.getLongtitude();
                LatLng latlng = new LatLng(lat, lon);
                updateMap(latlng);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                setResult(1, intent1);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = title.getText().toString();
                String opis = descr.getText().toString();

                if(!(name.length()==0 || opis.length()==0)){
                    Intent intent1 = getIntent();
                    setResult(1, intent1);
                    updateUser(name, opis);

                }
            }
        });

    }

    private void updateMap(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Here we go"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    private void initFirebase() {
        mLocationDatabaseReference = FirebaseDatabase.getInstance().getReference(mLocationReferenceString);
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    location = dataSnapshot.getValue(Location.class);
                    if (location != null) {
                        title.setText(location.getName());
                        date.setText(location.getDate());
                        descr.setText(location.getDesctiption());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mLocationDatabaseReference.addValueEventListener(mValueEventListener);
    }
    private void updateUser(String name, String opis) {
        location.setName(name);
        location.setDesctiption(opis);
        mLocationDatabaseReference.setValue(location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

}
