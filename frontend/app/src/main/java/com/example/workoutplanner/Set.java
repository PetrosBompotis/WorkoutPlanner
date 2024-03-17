package com.example.workoutplanner;

public class Set {
    private Long id;
    private Integer reps;
    private Integer numberOfSets;
    private Double weight;

    public Set(Long id, Integer reps, Integer numberOfSets, Double weight) {
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
}
