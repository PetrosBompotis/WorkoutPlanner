package com.petrosb.WorkoutPlanner.set;

import com.petrosb.WorkoutPlanner.exercise.Exercise;

import java.time.LocalTime;

public record SetCreationRequest(
        Integer reps,
        Integer numberOfSets,
        Double weight,
        Double kilometers,
        LocalTime time
) {
}
