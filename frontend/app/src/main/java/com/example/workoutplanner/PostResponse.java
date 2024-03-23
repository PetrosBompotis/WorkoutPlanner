package com.example.workoutplanner;

public class PostResponse {
    private Long postId;
    private String postName;
    private String createdBy;
    private String difficulty;
    private String gender;
    private Long workoutPlanId;

    public PostResponse(Long postId, String postName, String createdBy, String difficulty, String gender, Long workoutPlanId) {
        this.postId = postId;
        this.postName = postName;
        this.createdBy = createdBy;
        this.difficulty = difficulty;
        this.gender = gender;
        this.workoutPlanId = workoutPlanId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getWorkoutPlanId() {
        return workoutPlanId;
    }

    public void setWorkoutPlanId(Long workoutPlanId) {
        this.workoutPlanId = workoutPlanId;
    }
}
