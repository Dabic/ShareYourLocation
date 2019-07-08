package com.example.domaci4.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.domaci4.model.Location;
import com.example.domaci4.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocationLiveData extends LiveData<List<Location>> {
    private ValueEventListener mValueEventListener;
    private DatabaseReference databaseReference;

    public LocationLiveData() {
        super();
        initFirebase();
    }

    public void initFirebase(){
        FirebaseDatabase firebaseDatabase = Utils.getDatabase();
        databaseReference = firebaseDatabase.getReference().child("locations/");
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Location> lokacije = new ArrayList<>();

                if (dataSnapshot.getValue() == null) {
                    return;
                }

                for (DataSnapshot childDataSnapshot:dataSnapshot.getChildren()) {
                    Location lokacija = childDataSnapshot.getValue(Location.class);
                    String key = childDataSnapshot.getKey();
                    lokacija.setId(key);
                    lokacije.add(lokacija);
                }

                setValue(lokacije);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(mValueEventListener);

    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(mValueEventListener);
    }
}
