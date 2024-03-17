package com.example.workoutplanner;

public class Exercise {
    private String exerciseName;
    private String muscle;
    private String equipment;
    private String gifUrl;
    private String instructions;
    private Long exerciseId;

    public Exercise(String exerciseName, String muscle, String equipment, String gifUrl, String instructions, Long exerciseId) {
        this.exerciseName = exerciseName;
        this.muscle = muscle;
        this.equipment = equipment;
        this.gifUrl = gifUrl;
        this.instructions = instructions;
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getMuscle() {
        return muscle;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public String getInstructions() {
        return instructions;
    }

    public Long getExerciseId() {
        return exerciseId;
    }
}

