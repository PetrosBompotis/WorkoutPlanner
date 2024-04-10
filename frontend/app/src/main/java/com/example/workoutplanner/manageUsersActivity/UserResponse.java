package com.example.workoutplanner.manageUsersActivity;

public class UserResponse {
    private Long userId;
    private String name, email, gender;
    private Integer age;
    public UserResponse(Long userId, String name, String email, String gender, Integer age) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}
