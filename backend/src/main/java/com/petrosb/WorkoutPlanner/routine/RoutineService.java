package com.petrosb.WorkoutPlanner.routine;

import com.petrosb.WorkoutPlanner.exception.RequestValidationException;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlan;
import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlanDataAccessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutineService {
    private final RoutineDataAccessService routineDataAccessService;
    private final WorkoutPlanDataAccessService workoutPlanDataAccessService;

    public RoutineService(RoutineDataAccessService routineDataAccessService, WorkoutPlanDataAccessService workoutPlanDataAccessService) {
        this.routineDataAccessService = routineDataAccessService;
        this.workoutPlanDataAccessService = workoutPlanDataAccessService;
    }

    public List<Routine> getAllRoutinesByWorkoutPlanId(Long workoutPlanId){
        if(!workoutPlanDataAccessService.existsWorkoutPlanWithId(workoutPlanId)){
            throw new ResourceNotFoundException("Workout plan with id [%s] not found".formatted(workoutPlanId));
        }
        return routineDataAccessService.selectAllRoutinesByWorkoutPlanId(workoutPlanId);
    }

    public void addRoutine(RoutineCreationRequest routineCreationRequest, Long workoutPlanId){
        WorkoutPlan workoutPlan = workoutPlanDataAccessService.selectWorkoutPlanByID(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan with id [%s] not found".formatted(workoutPlanId)
                ));

        Routine routine = new Routine(
                routineCreationRequest.routineName(),
                workoutPlan
        );

        routineDataAccessService.insertRoutine(routine);
    }

    public void deleteRoutineById(Long routineId){
        //check if id exists
        if(!routineDataAccessService.existsRoutineWithId(routineId)){
            throw new ResourceNotFoundException("Routine with id [%s] not found".formatted(routineId));
        }

        //otherwise remove
        routineDataAccessService.deleteRoutineById(routineId);

    }

    public void updateRoutine(RoutineUpdateRequest updateRequest, Long routineId){

        Routine routine = routineDataAccessService.selectRoutineByID(routineId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Routine with id [%s] not found".formatted(routineId)
                ));
        boolean changes = false;
        //check if attributes need change exists
        if (updateRequest.routineName() != null && !updateRequest.routineName().equals(routine.getRoutineName())){
            routine.setRoutineName(updateRequest.routineName());
            changes = true;
        }
        //otherwise update
        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        routineDataAccessService.updateRoutineById(routine);
    }
}
