package com.example.workoutplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementViewHolder>{
    private List<Measurement> measurements;

    public MeasurementAdapter(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    @NonNull
    @Override
    public MeasurementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.measurement_item, parent, false);
        return new MeasurementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementViewHolder holder, int position) {
        Measurement measurement = measurements.get(position);
        String formattedDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate = dateFormat.format(measurement.getCreatedAt());
        } catch (Exception e) {
            formattedDate = "Invalid Date";
            e.printStackTrace();
        }
        holder.dateTextView.setText(formattedDate);
        holder.bodyFatTextView.setText(measurement.getBodyFatPercentage().toString());
        holder.weightTextView.setText(measurement.getBodyWeight().toString());
        holder.deleteMeasurementButton.setTag(measurement.getId());
    }

    @Override
    public int getItemCount() {
        return measurements.size();
    }
}
