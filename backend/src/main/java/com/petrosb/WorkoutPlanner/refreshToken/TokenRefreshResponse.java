package com.petrosb.WorkoutPlanner.refreshToken;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken
) {
}
