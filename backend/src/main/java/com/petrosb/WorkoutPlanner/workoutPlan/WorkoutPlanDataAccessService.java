package com.petrosb.WorkoutPlanner.workoutPlan;

import com.petrosb.WorkoutPlanner.customer.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WorkoutPlanDataAccessService {
    private final WorkoutPlanRepository workoutPlanRepository;

    public WorkoutPlanDataAccessService(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    public List<WorkoutPlan> selectAllWorkoutPlansByCustomerId(Long customerId){
        return workoutPlanRepository.findByCustomerId(customerId);
    }

    public Optional<WorkoutPlan> selectWorkoutPlanByID(Long id) {
        return workoutPlanRepository.findById(id);
    }

    public void insertWorkoutPlan(WorkoutPlan workoutPlan) {
        workoutPlanRepository.save(workoutPlan);
    }

    public boolean existsWorkoutPlanWithId(Long id) {
        return workoutPlanRepository.existsWorkoutPlanById(id);
    }

    public void deleteWorkoutPlanById(Long workoutPlanId) {
        workoutPlanRepository.deleteById(workoutPlanId);
    }

    public void updateWorkoutPlanById(WorkoutPlan workoutPlan) {
        workoutPlanRepository.save(workoutPlan);
    }
}
