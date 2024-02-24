package com.petrosb.WorkoutPlanner.workoutPlan;

import com.petrosb.WorkoutPlanner.customer.Customer;
import com.petrosb.WorkoutPlanner.customer.CustomerDataAccessService;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutPlanService {
    private final WorkoutPlanDataAccessService workoutPlanDataAccessService;
    private final CustomerDataAccessService customerDataAccessService;

    public WorkoutPlanService(WorkoutPlanDataAccessService workoutPlanDataAccessService, CustomerDataAccessService customerDataAccessService) {
        this.workoutPlanDataAccessService = workoutPlanDataAccessService;
        this.customerDataAccessService = customerDataAccessService;
    }

    public List<WorkoutPlan> getAllWorkoutPlansByCustomerId(Long customerId){
        if(!customerDataAccessService.existsPersonWithId(customerId)){
            throw new ResourceNotFoundException("Customer with id [%s] not found".formatted(customerId));
        }
        return workoutPlanDataAccessService.selectAllWorkoutPlansByCustomerId(customerId);
    }

    public void addWorkoutPlan(WorkoutPlanCreationRequest workoutPlanCreationRequest, Long customerId){
        Customer customer = customerDataAccessService.selectCustomerByID(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(customerId)
                ));

        WorkoutPlan workoutPlan = new WorkoutPlan(
                workoutPlanCreationRequest.programName(),
                workoutPlanCreationRequest.difficulty(),
                workoutPlanCreationRequest.gender(),
                customer
        );

        workoutPlanDataAccessService.insertWorkoutPlan(workoutPlan);
    }
}
