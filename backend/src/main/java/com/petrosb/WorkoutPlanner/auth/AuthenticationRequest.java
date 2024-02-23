package com.petrosb.WorkoutPlanner.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
