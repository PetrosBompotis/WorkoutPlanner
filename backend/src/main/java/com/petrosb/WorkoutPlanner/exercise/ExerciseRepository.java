package com.petrosb.WorkoutPlanner.exercise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByRoutineId(Long routineId);
    boolean existsExerciseById(Long id);

}
