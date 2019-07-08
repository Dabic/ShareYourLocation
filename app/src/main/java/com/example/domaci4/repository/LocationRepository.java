package com.example.domaci4.repository;

import androidx.annotation.NonNull;

import com.example.domaci4.livedata.LocationLiveData;
import com.example.domaci4.model.Location;
import com.example.domaci4.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocationRepository {
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private LocationLiveData locationLiveData;


    public LocationRepository() {
        FirebaseDatabase firebaseDatabase = Utils.getDatabase();
        databaseReference = firebaseDatabase.getReference("locations");
        locationLiveData = new LocationLiveData();
    }

    public LocationLiveData getLocationLiveData() {
        return locationLiveData;
    }

    public void addLocation(Location location) {
        databaseReference.push().setValue(location);
    }

    public void removeLocation(String location){
        FirebaseDatabase firebaseDatabase = Utils.getDatabase();
        databaseReference2 = firebaseDatabase.getReference(location);
        databaseReference2.setValue(null);
    }
}
