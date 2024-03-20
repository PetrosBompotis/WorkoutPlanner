package com.petrosb.WorkoutPlanner.measurement;

import java.util.Date;

public record MeasurementCreationRequest(
        Double bodyFatPercentage,
        Double bodyWeight,
        Date createdAt
) {
}
