package com.petrosb.WorkoutPlanner.routine;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByWorkoutPlanId(Long workoutPlanId);
    boolean existsRoutineById(Long id);
}
