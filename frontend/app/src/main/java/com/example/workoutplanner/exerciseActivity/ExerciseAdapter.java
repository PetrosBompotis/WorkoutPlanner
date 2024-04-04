package com.example.workoutplanner.exerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workoutplanner.exerciseDetailActivity.ExerciseDetailActivity;
import com.example.workoutplanner.R;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

    private List<ExerciseResponse> exercises;
    private Long routineId;
    private Boolean isClickable;
    private Boolean isNew;

    public ExerciseAdapter(List<ExerciseResponse> exercises, Long routineId, Boolean isNew, Boolean isClickable) {
        this.exercises = exercises;
        this.routineId = routineId;
        this.isNew = isNew;
        this.isClickable = isClickable;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(view, exercises, routineId, isNew, isClickable);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseResponse exercise = exercises.get(position);
        holder.exerciseNameTextView.setText(exercise.getExerciseName());
        holder.muscleTextView.setText(exercise.getMuscle());
        holder.equipmentTextView.setText(exercise.getEquipment());
        Glide.with(holder.itemView)
                .load(exercise.getGifUrl())
                .into(holder.gifImageView);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void filterList(List<ExerciseResponse> filteredExercises) {
        exercises = filteredExercises;
        notifyDataSetChanged();
    }
}

