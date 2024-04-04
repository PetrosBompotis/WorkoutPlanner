package com.petrosb.WorkoutPlanner.exercise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petrosb.WorkoutPlanner.routine.Routine;
import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlan;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "exercise")
public class Exercise {
    @Id
    @SequenceGenerator(
            name = "exercise_id_seq",
            sequenceName = "exercise_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_id_seq"
    )
    @Column(
            name = "id",
            updatable = false)
    private Long id;
    @Column(
            nullable = false
    )
    private String exerciseName;
    @Column(
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Muscle muscle;
    @Column(
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Equipment equipment;
    @Column(
            nullable = false
    )
    private String gifUrl;
    @Column(
            nullable = false,
            length = 5000
    )
    private String instructions;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "routine_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Routine routine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Muscle getMuscle() {
        return muscle;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Exercise() {
    }

    public Exercise(String exerciseName, Muscle muscle, Equipment equipment, String gifUrl, String instructions, Routine routine) {
        this.exerciseName = exerciseName;
        this.muscle = muscle;
        this.equipment = equipment;
        this.gifUrl = gifUrl;
        this.instructions = instructions;
        this.routine = routine;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", exerciseName='" + exerciseName + '\'' +
                ", muscle=" + muscle +
                ", equipment=" + equipment +
                ", gifUrl='" + gifUrl + '\'' +
                ", instructions='" + instructions + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.id) && Objects.equals(exerciseName, exercise.exerciseName) && muscle == exercise.muscle && equipment == exercise.equipment && Objects.equals(gifUrl, exercise.gifUrl) && Objects.equals(instructions, exercise.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exerciseName, muscle, equipment, gifUrl, instructions);
    }
}
