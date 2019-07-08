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
import androidx.lifecycle.ViewModelProviders;

import com.example.domaci4.R;
import com.example.domaci4.model.Location;
import com.example.domaci4.util.Utils;
import com.example.domaci4.viewmodel.LocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditLocationActivity extends AppCompatActivity {
    public static String LOCATION_REFERENCE_KEY = "locationIdKey";
    private TextView title;
    private TextView date;
    private TextView descr;
    private Button edit;
    private Button remove;
    private Button cancel;
    private DatabaseReference databaseReference;
    private ValueEventListener mValueEventListener;
    private String mLocationReferenceString;
    private Location location;
    private GoogleMap mMap;
    private LocationViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_location_layout);
        init();
        initFirebase();
    }

    public void init(){
        Intent intent = getIntent();
        if (intent != null) {
            mLocationReferenceString = intent.getStringExtra(LOCATION_REFERENCE_KEY);
        }
        title = findViewById(R.id.edit_title);
        date = findViewById(R.id.edit_date);
        descr = findViewById(R.id.edit_descr);
        remove = findViewById(R.id.edit_remove);
        edit = findViewById(R.id.edit_change);
        cancel = findViewById(R.id.edit_cancel);
        viewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        location = new Location();
        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map_edit, mapFragment).commit();
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
                setResult(1, intent);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditLocationActivity.this, ChangeLocationActivity.class);
                String locationReferenceString = mLocationReferenceString;
                intent.putExtra(ChangeLocationActivity.LOCATION_REFERENCE_KEY_TWO, locationReferenceString);
                startActivityForResult(intent, 1);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.removeLocation(mLocationReferenceString);
                Intent intent1 = getIntent();
                setResult(1, intent);
                finish();
            }
        });
    }

    private void updateMap(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Here we go"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    private void initFirebase() {
        databaseReference = Utils.getDatabase().getReference(mLocationReferenceString);
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
        databaseReference.addValueEventListener(mValueEventListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mValueEventListener);
    }
}
