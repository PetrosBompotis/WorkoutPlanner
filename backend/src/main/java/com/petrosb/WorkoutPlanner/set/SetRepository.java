package com.petrosb.WorkoutPlanner.set;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetRepository extends JpaRepository<Set, Long> {
    List<Set> findByExerciseId(Long exerciseId);
    boolean existsSetById(Long id);
}
