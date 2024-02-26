package com.petrosb.WorkoutPlanner.workoutPlan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petrosb.WorkoutPlanner.customer.Customer;
import com.petrosb.WorkoutPlanner.customer.Gender;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "workoutPlan")
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
    @Column(
            nullable = false
    )
    private String programName;
    @Column(
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @Column(
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Customer customer;

    public WorkoutPlan() {
    }

    public WorkoutPlan(String programName, Difficulty difficulty, Gender gender, Customer customer) {
        this.programName = programName;
        this.difficulty = difficulty;
        this.gender = gender;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "WorkoutPlan{" +
                "id=" + id +
                ", programName='" + programName + '\'' +
                ", difficulty=" + difficulty +
                ", gender=" + gender +
                ", customer=" + customer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutPlan that = (WorkoutPlan) o;
        return Objects.equals(id, that.id) && Objects.equals(programName, that.programName) && difficulty == that.difficulty && gender == that.gender && Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, programName, difficulty, gender, customer);
    }
}
