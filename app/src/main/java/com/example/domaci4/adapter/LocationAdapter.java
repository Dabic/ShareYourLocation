package com.example.domaci4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domaci4.R;
import com.example.domaci4.model.Location;
import com.example.domaci4.util.LocationDiffUtilCallback;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder>{
    private List<Location> mDataSet;
    private OnLocationClickedListener mOnLocationClickedListener;

    public LocationAdapter() {
        mDataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        Location locationModel = mDataSet.get(position);
        holder.mDate.setText(locationModel.getDate());
        holder.mName.setText(locationModel.getName());
        holder.mOpis.setText(locationModel.getDesctiption());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setData(List<Location> locationModelList){
        LocationDiffUtilCallback callback = new LocationDiffUtilCallback(mDataSet, locationModelList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        mDataSet.clear();
        mDataSet.addAll(locationModelList);
        result.dispatchUpdatesTo(this);
    }

    public void updateItem(Location locationModel) {

        List<Location> oldDataSet = new ArrayList<>(mDataSet);

        for (Location e : mDataSet) {
            if (e.getId().equals(locationModel.getId())){
                e.setName(locationModel.getName());
            }
        }

        LocationDiffUtilCallback callback = new LocationDiffUtilCallback(oldDataSet, mDataSet);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        mDataSet.clear();
        mDataSet.addAll(oldDataSet);
        result.dispatchUpdatesTo(this);
    }

    public class LocationHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mOpis;
        TextView mDate;
        ImageView mOko;


        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_list_item_location);
            mOpis = itemView.findViewById(R.id.tv_list_item_opis);
            mOko = itemView.findViewById(R.id.iv_list_item_view);
            mDate = itemView.findViewById(R.id.tv_list_item_date);

            mOko.setOnClickListener(v -> {
                if (mOnLocationClickedListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Location locationModel = mDataSet.get(position);
                        mOnLocationClickedListener.onLocationEdit(locationModel);
                    }
                }
            });

        }
    }

    public void setmOnLocationClickedListener(OnLocationClickedListener listener) {
        mOnLocationClickedListener = listener;
    }

    public interface OnLocationClickedListener {
        void onLocationEdit(Location locationModel);
    }
}
