package com.petrosb.WorkoutPlanner.measurement;

import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByCustomerId(Long customerId);
    boolean existsMeasurementById(Long id);
}
