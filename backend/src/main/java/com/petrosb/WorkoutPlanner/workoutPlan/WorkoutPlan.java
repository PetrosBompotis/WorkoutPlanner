package com.petrosb.WorkoutPlanner.workoutPlan;

import com.petrosb.WorkoutPlanner.customer.Customer;
import com.petrosb.WorkoutPlanner.customer.Gender;
import jakarta.persistence.*;

@Entity
@Table(name = "WorkoutPlan")
public class WorkoutPlan {
    @Id
    @SequenceGenerator(
            name = "workout_plan_id_seq",
            sequenceName = "workout_plan_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "workout_plan_id_seq"
    )
    @Column(
            name = "id",
            updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "customerID",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "customer_workout_plan_fk"
            )
    )
    private Customer customer;
    @Column(
            nullable = false
    )
    private String programName;
    @Column(
            nullable = false
    )
    private Difficulty difficulty;
    @Column(
            nullable = false
    )
    private Gender gender;

    public WorkoutPlan(Long id, Customer customer, String programName, Difficulty difficulty, Gender gender) {
        this.id = id;
        this.customer = customer;
        this.programName = programName;
        this.difficulty = difficulty;
        this.gender = gender;
    }

    public WorkoutPlan() {
    }


}
