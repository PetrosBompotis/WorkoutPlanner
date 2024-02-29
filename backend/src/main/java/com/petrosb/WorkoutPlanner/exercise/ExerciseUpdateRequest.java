package com.petrosb.WorkoutPlanner.exercise;

public record ExerciseUpdateRequest(
        String exerciseName,
        String instructions
) {
}
