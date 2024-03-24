package com.petrosb.WorkoutPlanner.post;

public record PostCreationRequest(
        Integer downloadCounter,
        String postName,
        String createdBy
) {
}
