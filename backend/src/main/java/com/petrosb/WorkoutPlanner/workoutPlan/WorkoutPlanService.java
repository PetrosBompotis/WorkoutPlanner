package com.petrosb.WorkoutPlanner.workoutPlan;

import com.petrosb.WorkoutPlanner.customer.Customer;
import com.petrosb.WorkoutPlanner.customer.CustomerDataAccessService;
import com.petrosb.WorkoutPlanner.exception.RequestValidationException;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import com.petrosb.WorkoutPlanner.exercise.Exercise;
import com.petrosb.WorkoutPlanner.exercise.ExerciseDataAccessService;
import com.petrosb.WorkoutPlanner.exercise.ExerciseService;
import com.petrosb.WorkoutPlanner.routine.Routine;
import com.petrosb.WorkoutPlanner.routine.RoutineDataAccessService;
import com.petrosb.WorkoutPlanner.routine.RoutineService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class WorkoutPlanService {
    private final WorkoutPlanDataAccessService workoutPlanDataAccessService;
    private final CustomerDataAccessService customerDataAccessService;
    private final RoutineService routineService;
    private final ExerciseService exerciseService;
    private final RoutineDataAccessService routineDataAccessService;
    private final ExerciseDataAccessService exerciseDataAccessService;

    public WorkoutPlanService(WorkoutPlanDataAccessService workoutPlanDataAccessService, CustomerDataAccessService customerDataAccessService, RoutineService routineService, ExerciseService exerciseService, RoutineDataAccessService routineDataAccessService, ExerciseDataAccessService exerciseDataAccessService) {
        this.workoutPlanDataAccessService = workoutPlanDataAccessService;
        this.customerDataAccessService = customerDataAccessService;
        this.routineService = routineService;
        this.exerciseService = exerciseService;
        this.routineDataAccessService = routineDataAccessService;
        this.exerciseDataAccessService = exerciseDataAccessService;
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
                        "Customer with id [%s] not found".formatted(customerId)
                ));

        WorkoutPlan workoutPlan = new WorkoutPlan(
                workoutPlanCreationRequest.programName(),
                workoutPlanCreationRequest.difficulty(),
                workoutPlanCreationRequest.gender(),
                customer
        );

        workoutPlanDataAccessService.insertWorkoutPlan(workoutPlan);
    }

    public void deleteWorkoutPlanById(Long workoutPlanId){
        //check if id exists
        if(!workoutPlanDataAccessService.existsWorkoutPlanWithId(workoutPlanId)){
            throw new ResourceNotFoundException("Workout plan with id [%s] not found".formatted(workoutPlanId));
        }

        //otherwise remove
        workoutPlanDataAccessService.deleteWorkoutPlanById(workoutPlanId);

    }

    public void updateWorkoutPlan(WorkoutPlanUpdateRequest updateRequest, Long workoutPlanId){

        WorkoutPlan workoutPlan = workoutPlanDataAccessService.selectWorkoutPlanByID(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan with id [%s] not found".formatted(workoutPlanId)
                ));
        boolean changes = false;
        //check if attributes need change exists
        if (updateRequest.programName() != null && !updateRequest.programName().equals(workoutPlan.getProgramName())){
            workoutPlan.setProgramName(updateRequest.programName());
            changes = true;
        }

        if (updateRequest.difficulty() != null && !updateRequest.difficulty().equals(workoutPlan.getDifficulty())){
            workoutPlan.setDifficulty(updateRequest.difficulty());
            changes = true;
        }

        if (updateRequest.gender() != null && !updateRequest.gender().equals(workoutPlan.getGender())){
            workoutPlan.setGender(updateRequest.gender());
            changes = true;
        }
        //otherwise update
        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        workoutPlanDataAccessService.updateWorkoutPlanById(workoutPlan);
    }

    public WorkoutPlan duplicateWorkoutPlan(Long customerId, Long workoutPlanId) {
        Customer customer = customerDataAccessService.selectCustomerByID(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id [%s] not found".formatted(customerId)
                ));

        WorkoutPlan existingProgram = workoutPlanDataAccessService.selectWorkoutPlanByID(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan with id [%s] not found".formatted(workoutPlanId)
                ));

        // Create a new fitness program with the same details
        WorkoutPlan newProgram = new WorkoutPlan(
            existingProgram.getProgramName(),
            existingProgram.getDifficulty(),
            existingProgram.getGender(),
            customer
        );

        newProgram = workoutPlanDataAccessService.insertWorkoutPlanAndReturnIt(newProgram);
        for (Routine existingRoutine : routineService.getAllRoutinesByWorkoutPlanId(existingProgram.getId())) {
            Routine newRoutine = new Routine(
                existingRoutine.getRoutineName(),
                newProgram
            );
            newRoutine = routineDataAccessService.insertRoutineAndReturnIt(newRoutine);

            for (Exercise existingExercise : exerciseService.getAllExercisesByRoutineId(existingRoutine.getId())) {
                Exercise newExercise = new Exercise(
                    existingExercise.getExerciseName(),
                    existingExercise.getMuscle() ,
                    existingExercise.getEquipment(),
                    existingExercise.getGifUrl(),
                    existingExercise.getInstructions(),
                    newRoutine
                );
                exerciseDataAccessService.insertExercise(newExercise);
            }
        }

        return newProgram;
    }
}
