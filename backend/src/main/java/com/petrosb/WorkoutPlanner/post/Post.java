package com.petrosb.WorkoutPlanner.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petrosb.WorkoutPlanner.customer.Customer;
import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlan;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @SequenceGenerator(
            name = "post_id_seq",
            sequenceName = "post_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_id_seq"
    )
    @Column(
            name = "id",
            updatable = false)
    private Long id;
    @Column(
            nullable = false
    )
    private String postName;
    @Column(
            nullable = false
    )
    private String createdBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WorkoutPlan workoutPlan;

    public Post() {
    }

    public Post(String postName, String createdBy, Customer customer, WorkoutPlan workoutPlan) {
        this.postName = postName;
        this.createdBy = createdBy;
        this.customer = customer;
        this.workoutPlan = workoutPlan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", postName='" + postName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", customer=" + customer +
                ", workoutPlan=" + workoutPlan +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(postName, post.postName) && Objects.equals(createdBy, post.createdBy) && Objects.equals(customer, post.customer) && Objects.equals(workoutPlan, post.workoutPlan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postName, createdBy, customer, workoutPlan);
    }
}
