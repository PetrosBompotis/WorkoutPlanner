package com.petrosb.WorkoutPlanner.auth;

import com.petrosb.WorkoutPlanner.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {
}
