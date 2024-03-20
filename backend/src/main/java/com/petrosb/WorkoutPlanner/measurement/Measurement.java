package com.petrosb.WorkoutPlanner.measurement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petrosb.WorkoutPlanner.customer.Customer;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "measurement")
public class Measurement {
    @Id
    @SequenceGenerator(
            name = "measurement_id_seq",
            sequenceName = "measurement_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "measurement_id_seq"
    )
    @Column(
            name = "id",
            updatable = false)
    private Long id;
    @Column(
            nullable = false
    )
    private Double bodyFatPercentage;
    @Column(
            nullable = false
    )
    private Double bodyWeight;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Customer customer;

    public Measurement() {
    }

    public Measurement(Double bodyFatPercentage, Double bodyWeight, Date createdAt, Customer customer) {
        this.bodyFatPercentage = bodyFatPercentage;
        this.bodyWeight = bodyWeight;
        this.createdAt = createdAt;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public Double getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(Double bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", bodyFatPercentage=" + bodyFatPercentage +
                ", bodyWeight=" + bodyWeight +
                ", createdAt=" + createdAt +
                ", customer=" + customer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return Objects.equals(id, that.id) && Objects.equals(bodyFatPercentage, that.bodyFatPercentage) && Objects.equals(bodyWeight, that.bodyWeight) && Objects.equals(createdAt, that.createdAt) && Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bodyFatPercentage, bodyWeight, createdAt, customer);
    }
}
