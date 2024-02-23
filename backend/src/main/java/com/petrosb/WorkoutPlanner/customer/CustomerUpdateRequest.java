package com.petrosb.WorkoutPlanner.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
