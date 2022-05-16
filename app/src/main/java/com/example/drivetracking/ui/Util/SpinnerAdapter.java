package com.example.drivetracking.ui.Util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drivetracking.BE.Car;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<Car> {
    private Context context;
    private ArrayList<Car> cars;

    public SpinnerAdapter(Context context, int textViewResourceId, ArrayList<Car> cars){
        super(context, textViewResourceId, cars);
        this.context = context;
        this.cars = cars;

    }

    @Override
    public int getCount() {
        return cars.size();
    }

    @Nullable
    @Override
    public Car getItem(int position) {
        return cars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.WHITE);
        label.setText(cars.get(position).toString());
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setTextColor(Color.WHITE);
        textView.setText(cars.get(position).toString());
        return  textView;
    }
}

