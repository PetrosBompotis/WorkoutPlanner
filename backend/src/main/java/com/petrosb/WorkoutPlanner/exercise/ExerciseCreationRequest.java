package com.petrosb.WorkoutPlanner.exercise;

public record ExerciseCreationRequest(
        String exerciseName,
        Muscle muscle,
        Equipment equipment,
        String gifUrl,
        String instructions
) {
}
