package com.example.domaci4.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.domaci4.livedata.LocationLiveData;
import com.example.domaci4.model.Location;
import com.example.domaci4.repository.LocationRepository;
import com.google.firebase.database.annotations.NotNull;

public class LocationViewModel extends AndroidViewModel {
    private LocationRepository locationRepository;
    private LocationLiveData locationLiveData;
    public LocationViewModel(@NotNull Application application){
        super(application);
        locationRepository = new LocationRepository();
        locationLiveData = new LocationLiveData();
    }

    public LocationLiveData getLocationLiveData() {
        return locationLiveData;
    }

    public void addLocation(Location location){
        locationRepository.addLocation(location);
    }
    public void removeLocation(String location){
        locationRepository.removeLocation(location);
    }
}
