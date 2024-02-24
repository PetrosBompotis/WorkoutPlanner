package com.petrosb.WorkoutPlanner.workoutPlan;

import com.petrosb.WorkoutPlanner.customer.Gender;

public record WorkoutPlanCreationRequest(
        String programName,
        Difficulty difficulty,
        Gender gender
) {
}
