package com.petrosb.WorkoutPlanner.workoutPlan;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping("/customers/{customerId}/workoutPlans")
    public List<WorkoutPlan> getAllWorkoutPlansByCustomerId(@PathVariable(value = "customerId") Long customerId) {
        return workoutPlanService.getAllWorkoutPlansByCustomerId(customerId);
    }

    @PostMapping("/customers/{customerId}/workoutPlans")
    public void createWorkoutPlan(@PathVariable(value = "customerId") Long customerId,
                                  @RequestBody WorkoutPlanCreationRequest workoutPlanRequest) {
        workoutPlanService.addWorkoutPlan(workoutPlanRequest, customerId);
    }

    @DeleteMapping("/workoutPlans/{workoutPlanId}")
    public void deleteWorkoutPlan(@PathVariable("workoutPlanId") Long workoutPlanId) {
        workoutPlanService.deleteWorkoutPlanById(workoutPlanId);
    }

    @PutMapping("/workoutPlans/{workoutPlanId}")
    public void updateWorkoutPlan(@RequestBody WorkoutPlanUpdateRequest updateRequest,
                                  @PathVariable("workoutPlanId") Long workoutPlanId) {
        workoutPlanService.updateWorkoutPlan(updateRequest, workoutPlanId);
    }

    @PostMapping("/customers/{customerId}/workoutPlans/{workoutPlanId}")
    public WorkoutPlan duplicateWorkoutPlan(@PathVariable Long workoutPlanId, @PathVariable Long customerId) {
        return workoutPlanService.duplicateWorkoutPlan(customerId, workoutPlanId);
    }
}
