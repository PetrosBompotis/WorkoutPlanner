package com.example.workoutplanner.userActivity.measurementsFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementViewHolder>{
    private List<MeasurementResponse> measurements;
    private MeasurementsFragment fragment;

    public MeasurementAdapter(List<MeasurementResponse> measurements, MeasurementsFragment fragment) {
        this.measurements = measurements;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MeasurementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.measurement_item, parent, false);
        return new MeasurementViewHolder(view, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementViewHolder holder, int position) {
        MeasurementResponse measurement = measurements.get(position);
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
