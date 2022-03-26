package com.example.drivetracking.ui.Util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivetracking.BE.Trip;
import com.example.drivetracking.R;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<Trip> tripList;

    public recyclerAdapter(ArrayList<Trip> tripList) {
        this.tripList = tripList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView distance;
        private TextView time;
        private TextView gas;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            distance = itemView.findViewById(R.id.tripDistance);
            time = itemView.findViewById(R.id.tripTime);
            gas = itemView.findViewById(R.id.tripGas);
        }
    }
    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_drive, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String time = tripList.get(position).getTime();
        Double miles = tripList.get(position).getMiles();
        int gallons = tripList.get(position).getGallons();
        holder.time.setText(time);
        holder.distance.setText(String.valueOf(miles) + " miles");
        holder.gas.setText(String.valueOf(gallons) + " gallons");

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }


}
