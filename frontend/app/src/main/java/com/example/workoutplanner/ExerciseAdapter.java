package com.example.workoutplanner;

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

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises;

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
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

    class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView gifImageView;
        TextView exerciseNameTextView, muscleTextView, equipmentTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
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
            Exercise exercise = exercises.get(getAdapterPosition());

            // Redirect to ExerciseDetailActivity with exercise data
            Context context = itemView.getContext();
            Intent intent = new Intent(context, ExerciseDetailActivity.class);
            intent.putExtra("exerciseName", exercise.getExerciseName());
            intent.putExtra("muscle", exercise.getMuscle());
            intent.putExtra("equipment", exercise.getEquipment());
            intent.putExtra("gifUrl", exercise.getGifUrl());
            context.startActivity(intent);
        }
    }

    public void filterList(List<Exercise> filteredExercises) {
        exercises = filteredExercises;
        notifyDataSetChanged();
    }

}
