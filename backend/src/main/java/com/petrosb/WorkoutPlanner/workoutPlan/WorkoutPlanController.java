package com.petrosb.WorkoutPlanner.workoutPlan;

import com.petrosb.WorkoutPlanner.customer.CustomerRepository;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping("/customers/{customerId}/workout_plans")
    public List<WorkoutPlan> getAllWorkoutPlansByCustomerId(@PathVariable(value = "customerId") Long customerId) {
        return workoutPlanService.getAllWorkoutPlansByCustomerId(customerId);
    }

    @PostMapping("/customers/{customerId}/workout_plans")
    public void createWorkoutPlan(@PathVariable(value = "customerId") Long customerId,
                                                 @RequestBody WorkoutPlanCreationRequest workoutPlanRequest) {
        workoutPlanService.addWorkoutPlan(workoutPlanRequest, customerId);
    }
}
