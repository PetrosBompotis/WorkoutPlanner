package com.petrosb.WorkoutPlanner.set;

import com.petrosb.WorkoutPlanner.exception.RequestValidationException;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import com.petrosb.WorkoutPlanner.exercise.Exercise;
import com.petrosb.WorkoutPlanner.exercise.ExerciseDataAccessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetService {
    private final SetDataAccessService setDataAccessService;
    private final ExerciseDataAccessService exerciseDataAccessService;

    public SetService(SetDataAccessService setDataAccessService, ExerciseDataAccessService exerciseDataAccessService) {
        this.setDataAccessService = setDataAccessService;
        this.exerciseDataAccessService = exerciseDataAccessService;
    }

    public List<Set> getAllSetsByExerciseId(Long exerciseId){
        if(!exerciseDataAccessService.existsExerciseWithId(exerciseId)){
            throw new ResourceNotFoundException("Exercise with id [%s] not found".formatted(exerciseId));
        }
        return setDataAccessService.selectAllSetsByExerciseId(exerciseId);
    }

    public void addSet(SetCreationRequest setCreationRequest, Long exerciseId){
        Exercise exercise = exerciseDataAccessService.selectExerciseByID(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise with id [%s] not found".formatted(exerciseId)
                ));

        Set set = new Set(
                setCreationRequest.reps(),
                setCreationRequest.numberOfSets(),
                setCreationRequest.weight(),
                setCreationRequest.kilometers(),
                setCreationRequest.time(),
                exercise
        );

        setDataAccessService.insertSet(set);
    }

    public void deleteSetById(Long setId){
        //check if id exists
        if(!setDataAccessService.existsSetWithId(setId)){
            throw new ResourceNotFoundException("Exercise with id [%s] not found".formatted(setId));
        }

        //otherwise remove
        setDataAccessService.deleteSetById(setId);

    }

    public void updateSet(SetUpdateRequest updateRequest, Long setId){

        Set set = setDataAccessService.selectSetByID(setId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise with id [%s] not found".formatted(setId)
                ));
        boolean changes = false;
        //check if attributes need change exists
        if (updateRequest.reps() != null && !updateRequest.reps().equals(set.getReps())){
            set.setReps(updateRequest.reps());
            changes = true;
        }

        if (updateRequest.numberOfSets() != null && !updateRequest.numberOfSets().equals(set.getNumberOfSets())){
            set.setNumberOfSets(updateRequest.numberOfSets());
            changes = true;
        }

        if (updateRequest.weight() != null && !updateRequest.weight().equals(set.getWeight())){
            set.setWeight(updateRequest.weight());
            changes = true;
        }

        if (updateRequest.kilometers() != null && !updateRequest.kilometers().equals(set.getKilometers())){
            set.setKilometers(updateRequest.kilometers());
            changes = true;
        }

        if (updateRequest.time() != null && !updateRequest.time().equals(set.getTime())){
            set.setTime(updateRequest.time());
            changes = true;
        }
        //otherwise update
        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        setDataAccessService.updateSetById(set);
    }
}
