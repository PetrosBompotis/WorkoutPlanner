package com.petrosb.WorkoutPlanner.exercise;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExerciseDataAccessService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseDataAccessService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<Exercise> selectAllExercisesByRoutineId(Long routineId){
        return exerciseRepository.findByRoutineId(routineId);
    }

    public Optional<Exercise> selectExerciseByID(Long id) {
        return exerciseRepository.findById(id);
    }

    public void insertExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public boolean existsExerciseWithId(Long id) {
        return exerciseRepository.existsExerciseById(id);
    }

    public void deleteExerciseById(Long exerciseId) {
        exerciseRepository.deleteById(exerciseId);
    }

    public void updateExerciseById(Exercise exercise) {
        exerciseRepository.save(exercise);
    }
    public void insertExercises(List<Exercise> exercises) {
        exerciseRepository.saveAll(exercises);
    }
}
