package com.petrosb.WorkoutPlanner.exercise;

import com.petrosb.WorkoutPlanner.exception.RequestValidationException;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import com.petrosb.WorkoutPlanner.routine.Routine;
import com.petrosb.WorkoutPlanner.routine.RoutineDataAccessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService {
    private final ExerciseDataAccessService exerciseDataAccessService;
    private final RoutineDataAccessService routineDataAccessService;

    public ExerciseService(ExerciseDataAccessService exerciseDataAccessService, RoutineDataAccessService routineDataAccessService) {
        this.exerciseDataAccessService = exerciseDataAccessService;
        this.routineDataAccessService = routineDataAccessService;
    }

    public List<Exercise> getAllExercisesByRoutineId(Long routineId){
        if(!routineDataAccessService.existsRoutineWithId(routineId)){
            throw new ResourceNotFoundException("Routine with id [%s] not found".formatted(routineId));
        }
        return exerciseDataAccessService.selectAllExercisesByRoutineId(routineId);
    }

    public void addExercise(ExerciseCreationRequest exerciseCreationRequest, Long routineId){
        Routine routine = routineDataAccessService.selectRoutineByID(routineId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Routine with id [%s] not found".formatted(routineId)
                ));

        Exercise exercise = new Exercise(
                exerciseCreationRequest.exerciseName(),
                exerciseCreationRequest.muscle(),
                exerciseCreationRequest.equipment(),
                exerciseCreationRequest.gifUrl(),
                exerciseCreationRequest.instructions(),
                routine
        );

        exerciseDataAccessService.insertExercise(exercise);
    }

    public void deleteExerciseById(Long exerciseId){
        //check if id exists
        if(!exerciseDataAccessService.existsExerciseWithId(exerciseId)){
            throw new ResourceNotFoundException("Exercise with id [%s] not found".formatted(exerciseId));
        }

        //otherwise remove
        exerciseDataAccessService.deleteExerciseById(exerciseId);

    }

    public void updateExercise(ExerciseUpdateRequest updateRequest, Long exerciseId){

        Exercise exercise = exerciseDataAccessService.selectExerciseByID(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise with id [%s] not found".formatted(exerciseId)
                ));
        boolean changes = false;
        //check if attributes need change exists
        if (updateRequest.exerciseName() != null && !updateRequest.exerciseName().equals(exercise.getExerciseName())){
            exercise.setExerciseName(updateRequest.exerciseName());
            changes = true;
        }

        if (updateRequest.instructions() != null && !updateRequest.instructions().equals(exercise.getInstructions())){
            exercise.setInstructions(updateRequest.instructions());
            changes = true;
        }
        //otherwise update
        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        exerciseDataAccessService.updateExerciseById(exercise);
    }

    public void addExercises(List<ExerciseCreationRequest> exerciseCreationRequests, Long routineId) {
        Routine routine = routineDataAccessService.selectRoutineByID(routineId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Routine with id [%s] not found".formatted(routineId)
                ));

        List<Exercise> exercises = exerciseCreationRequests.stream()
                .map(request -> new Exercise(
                        request.exerciseName(),
                        request.muscle(),
                        request.equipment(),
                        request.gifUrl(),
                        request.instructions(),
                        routine
                ))
                .collect(Collectors.toList());

        exerciseDataAccessService.insertExercises(exercises);
    }
}
