package com.petrosb.WorkoutPlanner.measurement;

public record MeasurementUpdateRequest(
        Double bodyFatPercentage,
        Double bodyWeight
) {
}
