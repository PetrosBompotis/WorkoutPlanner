package com.petrosb.WorkoutPlanner.set;

import java.time.LocalTime;

public record SetUpdateRequest(
        Integer reps,
        Integer numberOfSets,
        Double weight,
        Double kilometers,
        LocalTime time
) {
}
