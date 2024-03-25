package com.example.workoutplanner.exerciseDetailActivity;

public class SetResponse {
    private Long id;
    private Integer reps;
    private Integer numberOfSets;
    private Double weight;

    public SetResponse(Long id, Integer reps, Integer numberOfSets, Double weight) {
        this.id = id;
        this.reps = reps;
        this.numberOfSets = numberOfSets;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public Integer getReps() {
        return reps;
    }

    public Integer getNumberOfSets() {
        return numberOfSets;
    }

    public Double getWeight() {
        return weight;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public void setNumberOfSets(Integer numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
