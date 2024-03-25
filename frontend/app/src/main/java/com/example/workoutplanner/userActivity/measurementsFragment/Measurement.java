package com.example.workoutplanner.userActivity.measurementsFragment;

import java.util.Date;

public class Measurement {
    private Long id;
    private Double bodyFatPercentage;
    private Double bodyWeight;
    private Date createdAt;

    public Measurement(Long id, Double bodyFatPercentage, Double bodyWeight, Date createdAt) {
        this.id = id;
        this.bodyFatPercentage = bodyFatPercentage;
        this.bodyWeight = bodyWeight;
        this.createdAt = createdAt;
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

    public Double getBodyWeight() {
        return bodyWeight;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
