package com.example.domaci4.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.domaci4.R;
import com.example.domaci4.adapter.LocationAdapter;
import com.example.domaci4.model.Location;
import com.example.domaci4.viewmodel.LocationViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LocationAdapter locationAdapter;
    private LocationViewModel locationViewModel;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);
        init();
    }
    public void init(){
        recyclerView = findViewById(R.id.location_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        locationAdapter = new LocationAdapter();
        recyclerView.setAdapter(locationAdapter);
        imageView = findViewById(R.id.fab_btn);
        locationAdapter.setmOnLocationClickedListener(new LocationAdapter.OnLocationClickedListener() {
            @Override
            public void onLocationEdit(Location locationModel) {
                Intent intent = new Intent(MainActivity.this, EditLocationActivity.class);
                intent.putExtra(EditLocationActivity.LOCATION_REFERENCE_KEY, "locations/"+locationModel.getId());
                startActivityForResult(intent, 1);
            }
        });
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        locationViewModel.getLocationLiveData().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                locationAdapter.setData(locations);
            }
        });
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddLocationActivity.class);
            startActivityForResult(intent, 1);
        });
    }
}
