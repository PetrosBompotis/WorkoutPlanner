package com.petrosb.WorkoutPlanner.set;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SetDataAccessService {
    private final SetRepository setRepository;

    public SetDataAccessService(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    public List<Set> selectAllSetsByExerciseId(Long exerciseId){
        return setRepository.findByExerciseId(exerciseId);
    }

    public Optional<Set> selectSetByID(Long id) {
        return setRepository.findById(id);
    }

    public void insertSet(Set set) {
        setRepository.save(set);
    }

    public boolean existsSetWithId(Long id) {
        return setRepository.existsSetById(id);
    }

    public void deleteSetById(Long setId) {
        setRepository.deleteById(setId);
    }

    public void updateSetById(Set set) {
        setRepository.save(set);
    }
}
