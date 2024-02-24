package com.petrosb.WorkoutPlanner.workoutPlan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    List<WorkoutPlan> findByCustomerId(Long customerId);
}
