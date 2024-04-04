package com.example.workoutplanner.exerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;
import com.example.workoutplanner.exerciseDetailActivity.ExerciseDetailActivity;

import java.util.List;

public class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView gifImageView;
    TextView exerciseNameTextView, muscleTextView, equipmentTextView;
    List<ExerciseResponse> exercises;
    Long routineId;
    Boolean isClickable;
    Boolean isNew;

    public ExerciseViewHolder(@NonNull View itemView, List<ExerciseResponse> exercises, Long routineId, Boolean isNew, Boolean isClickable) {
        super(itemView);
        this.exercises = exercises;
        this.routineId = routineId;
        this.isNew = isNew;
        this.isClickable = isClickable;
        gifImageView = itemView.findViewById(R.id.gifImageView);
        exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
        muscleTextView = itemView.findViewById(R.id.muscleTextView);
        equipmentTextView = itemView.findViewById(R.id.equipmentTextView);

        // Set click listener for the item
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Get the clicked exercise
        ExerciseResponse exercise = exercises.get(getAdapterPosition());
        if (isClickable) {
            // Redirect to ExerciseDetailActivity with exercise data
            Context context = itemView.getContext();
            Intent intent = new Intent(context, ExerciseDetailActivity.class);
            intent.putExtra("exerciseId", exercise.getExerciseId());
            intent.putExtra("exerciseName", exercise.getExerciseName());
            intent.putExtra("muscle", exercise.getMuscle());
            intent.putExtra("equipment", exercise.getEquipment());
            intent.putExtra("gifUrl", exercise.getGifUrl());
            intent.putExtra("instructions", exercise.getInstructions());
            intent.putExtra("routineId", routineId);
            intent.putExtra("isNew", isNew);
            context.startActivity(intent);
        }
    }
}
