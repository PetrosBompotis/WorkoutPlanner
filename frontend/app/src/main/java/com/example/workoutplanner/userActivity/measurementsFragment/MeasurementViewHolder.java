package com.example.workoutplanner.userActivity.measurementsFragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;

public class MeasurementViewHolder extends RecyclerView.ViewHolder{
    TextView dateTextView;
    TextView bodyFatTextView;
    TextView weightTextView;
    ImageButton deleteMeasurementButton;


    public MeasurementViewHolder(@NonNull View itemView, MeasurementsFragment fragment) {
        super(itemView);

        dateTextView = itemView.findViewById(R.id.dateTextView);
        bodyFatTextView = itemView.findViewById(R.id.bodyFatTextView);
        weightTextView = itemView.findViewById(R.id.weightTextView);
        deleteMeasurementButton = itemView.findViewById(R.id.deleteMeasurementButton);

        deleteMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long measurementId = (Long) v.getTag();
                Log.d("ssss",measurementId.toString());
                fragment.deleteMeasurement(measurementId);
            }
        });
    }
}
