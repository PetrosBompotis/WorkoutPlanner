package com.example.workoutplanner.userActivity.communityFragment;

public class PostResponse {
    private Long postId;
    private String postName;
    private String createdBy;
    private String difficulty;
    private String gender;
    private Long workoutPlanId;
    private Integer downloadCounter;

    public PostResponse(Long postId, String postName, String createdBy, String difficulty, String gender, Long workoutPlanId, Integer downloadCounter) {
        this.postId = postId;
        this.postName = postName;
        this.createdBy = createdBy;
        this.difficulty = difficulty;
        this.gender = gender;
        this.workoutPlanId = workoutPlanId;
        this.downloadCounter = downloadCounter;
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

    public Integer getDownloadCounter() {
        return downloadCounter;
    }

    public void setDownloadCounter(Integer downloadCounter) {
        this.downloadCounter = downloadCounter;
    }
}
