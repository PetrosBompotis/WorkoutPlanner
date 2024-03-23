package com.petrosb.WorkoutPlanner.post;

public record PostCreationRequest(
        String postName,
        String createdBy
) {
}
