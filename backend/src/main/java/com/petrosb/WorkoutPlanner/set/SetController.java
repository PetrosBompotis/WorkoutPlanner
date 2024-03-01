package com.petrosb.WorkoutPlanner.set;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class SetController {
    private final SetService setService;

    public SetController(SetService setService) {
        this.setService = setService;
    }

    @GetMapping("/exercises/{exerciseId}/sets")
    public List<Set> getAllSetsByExerciseId(@PathVariable(value = "exerciseId") Long exerciseId) {
        return setService.getAllSetsByExerciseId(exerciseId);
    }

    @PostMapping("/exercises/{exerciseId}/sets")
    public void createSet(@PathVariable(value = "exerciseId") Long exerciseId,
                               @RequestBody SetCreationRequest setCreationRequest) {
        setService.addSet(setCreationRequest, exerciseId);
    }

    @DeleteMapping("/sets/{setId}")
    public void deleteSet(@PathVariable("setId") Long setId) {
        setService.deleteSetById(setId);
    }

    @PutMapping("/sets/{setId}")
    public void updateSet(@RequestBody SetUpdateRequest updateRequest,
                               @PathVariable("setId") Long setId) {
        setService.updateSet(updateRequest, setId);
    }
}
