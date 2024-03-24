package com.petrosb.WorkoutPlanner.post;

import java.util.Optional;

public record PostUpdateRequest(
        Integer downloadCounter,
        String postName
) {
}
