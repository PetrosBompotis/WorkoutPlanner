package com.petrosb.WorkoutPlanner.exercise;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/routines/{routineId}/exercises")
    public List<Exercise> getAllExercisesByRoutineId(@PathVariable(value = "routineId") Long routineId) {
        return exerciseService.getAllExercisesByRoutineId(routineId);
    }

    @PostMapping("/routines/{routineId}/exercises")
    public void createExercise(@PathVariable(value = "routineId") Long routineId,
                              @RequestBody ExerciseCreationRequest exerciseCreationRequest) {
        exerciseService.addExercise(exerciseCreationRequest, routineId);
    }

    @DeleteMapping("/exercises/{exerciseId}")
    public void deleteExercise(@PathVariable("exerciseId") Long exerciseId) {
        exerciseService.deleteExerciseById(exerciseId);
    }

    @PutMapping("/exercises/{exerciseId}")
    public void updateExercise(@RequestBody ExerciseUpdateRequest updateRequest,
                              @PathVariable("exerciseId") Long exerciseId) {
        exerciseService.updateExercise(updateRequest, exerciseId);
    }
}
