package com.petrosb.WorkoutPlanner.routine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlan;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "routine")
public class Routine {
    @Id
    @SequenceGenerator(
            name = "routine_id_seq",
            sequenceName = "routine_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "routine_id_seq"
    )
    @Column(
            name = "id",
            updatable = false)
    private Long id;
    @Column(
            nullable = false
    )
    private String routineName;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private WorkoutPlan workoutPlan;

    public Routine() {
    }

    public Routine(String routineName, WorkoutPlan workoutPlan) {
        this.routineName = routineName;
        this.workoutPlan = workoutPlan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    @Override
    public String toString() {
        return "Routine{" +
                "id=" + id +
                ", routineName='" + routineName + '\'' +
                ", workoutPlan=" + workoutPlan +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Routine routine = (Routine) o;
        return Objects.equals(id, routine.id) && Objects.equals(routineName, routine.routineName) && Objects.equals(workoutPlan, routine.workoutPlan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, routineName, workoutPlan);
    }
}
