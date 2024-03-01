package com.petrosb.WorkoutPlanner.set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petrosb.WorkoutPlanner.exercise.Exercise;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "set")
public class Set {
    @Id
    @SequenceGenerator(
            name = "set_id_seq",
            sequenceName = "set_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "set_id_seq"
    )
    @Column(
            name = "id",
            updatable = false)
    private Long id;
    private Integer reps;
    private Integer numberOfSets;
    private Double weight;
    private Double kilometers;
    private LocalTime time;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Exercise exercise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getNumberOfSets() {
        return numberOfSets;
    }

    public void setNumberOfSets(Integer numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Set() {
    }

    public Set(Integer reps, Integer numberOfSets, Double weight, Double kilometers, LocalTime time, Exercise exercise) {
        this.reps = reps;
        this.numberOfSets = numberOfSets;
        this.weight = weight;
        this.kilometers = kilometers;
        this.time = time;
        this.exercise = exercise;
    }

    @Override
    public String toString() {
        return "Set{" +
                "id=" + id +
                ", reps=" + reps +
                ", numberOfSets=" + numberOfSets +
                ", weight=" + weight +
                ", kilometers=" + kilometers +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set set = (Set) o;
        return Objects.equals(id, set.id) && Objects.equals(reps, set.reps) && Objects.equals(numberOfSets, set.numberOfSets) && Objects.equals(weight, set.weight) && Objects.equals(kilometers, set.kilometers) && Objects.equals(time, set.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reps, numberOfSets, weight, kilometers, time);
    }
}
