package com.petrosb.WorkoutPlanner.workoutPlan;

import com.petrosb.WorkoutPlanner.customer.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WorkoutPlanDataAccessService {
    private final WorkoutPlanRepository workoutPlanRepository;

    public WorkoutPlanDataAccessService(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    public List<WorkoutPlan> selectAllWorkoutPlansByCustomerId(Long customerId){
        return workoutPlanRepository.findByCustomerId(customerId);
    }

    public void insertWorkoutPlan(WorkoutPlan workoutPlan) {
        workoutPlanRepository.save(workoutPlan);
    }
}
