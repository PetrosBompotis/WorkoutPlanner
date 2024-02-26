package com.petrosb.WorkoutPlanner.routine;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoutineDataAccessService {
    private final RoutineRepository routineRepository;

    public RoutineDataAccessService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public List<Routine> selectAllRoutinesByWorkoutPlanId(Long workoutPlanId){
        return routineRepository.findByWorkoutPlanId(workoutPlanId);
    }

    public Optional<Routine> selectRoutineByID(Long id) {
        return routineRepository.findById(id);
    }

    public void insertRoutine(Routine routine) {
        routineRepository.save(routine);
    }

    public boolean existsRoutineWithId(Long id) {
        return routineRepository.existsRoutineById(id);
    }

    public void deleteRoutineById(Long routineId) {
        routineRepository.deleteById(routineId);
    }

    public void updateRoutineById(Routine routine) {
        routineRepository.save(routine);
    }
}
