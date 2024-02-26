package com.petrosb.WorkoutPlanner.routine;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class RoutineController {
    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping("/workoutPlans/{workoutPlanId}/routines")
    public List<Routine> getAllRoutinesByWorkoutPlanId(@PathVariable(value = "workoutPlanId") Long workoutPlanId) {
        return routineService.getAllRoutinesByWorkoutPlanId(workoutPlanId);
    }

    @PostMapping("/workoutPlans/{workoutPlanId}/routines")
    public void createRoutine(@PathVariable(value = "workoutPlanId") Long workoutPlanId,
                                  @RequestBody RoutineCreationRequest routineCreationRequest) {
        routineService.addRoutine(routineCreationRequest, workoutPlanId);
    }

    @DeleteMapping("/routines/{routineId}")
    public void deleteRoutine(@PathVariable("routineId") Long routineId) {
        routineService.deleteRoutineById(routineId);
    }

    @PutMapping("/routines/{routineId}")
    public void updateRoutine(@RequestBody RoutineUpdateRequest updateRequest,
                                  @PathVariable("routineId") Long routineId) {
        routineService.updateRoutine(updateRequest, routineId);
    }
}
