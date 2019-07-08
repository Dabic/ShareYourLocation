package com.example.domaci4.util;

import androidx.recyclerview.widget.DiffUtil;

import com.example.domaci4.model.Location;

import java.util.List;

public class LocationDiffUtilCallback extends DiffUtil.Callback {

    private List<Location> mOldList;
    private List<Location> mNewList;

    public LocationDiffUtilCallback(List<Location> oldList, List<Location> newList){
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Location oldEmployee = mOldList.get(oldItemPosition);
        Location newEmployee = mNewList.get(newItemPosition);
        //return  oldEmployee.getId().equals(newEmployee.getId());
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Location employee = mOldList.get(oldItemPosition);
        Location newMovie = mNewList.get(newItemPosition);
        //return employee.getName().equals(newMovie.getName());
        return false;
    }
}