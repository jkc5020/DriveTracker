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

/**
 * Recycler adapter used for RecyclerView to log all drives
 */
public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<Trip> tripList; // list of all trips

    /**
     * Initializes trip list
     * @param tripList
     */
    public recyclerAdapter(ArrayList<Trip> tripList) {
        this.tripList = tripList;
    }

    /**
     * Creates viewholder containing the information required
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView distance;
        private TextView time;
        private TextView gas;

        /**
         * Constructor
         * @param itemView - the view
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            distance = itemView.findViewById(R.id.tripDistance);
            time = itemView.findViewById(R.id.tripTime);
            gas = itemView.findViewById(R.id.tripGas);
        }
    }

    /**
     * Adds itemView to the layout
     * @param parent - parent layout
     * @param viewType - type of layout
     * @return - the new view to add
     */
    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_drive, parent, false);
        return new MyViewHolder(itemView);
    }


    /**
     * sets text to the view on screen
     * @param holder - holder
     * @param position - position
     */
    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String time = tripList.get(position).getTime();
        Double miles = tripList.get(position).getMiles();
        int gallons = tripList.get(position).getGallons();
        holder.time.setText(time);
        holder.distance.setText(String.valueOf(miles) + " miles");
        holder.gas.setText(String.valueOf(gallons) + " gallons");

    }

    /**
     * Returns item count
     * @return - size of arrayList
     */
    @Override
    public int getItemCount() {
        return tripList.size();
    }


}
