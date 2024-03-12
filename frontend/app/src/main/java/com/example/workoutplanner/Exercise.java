package com.example.workoutplanner;

public class Exercise {
    private String exerciseName;
    private String muscle;
    private String equipment;
    private String gifUrl;

    public Exercise(String exerciseName, String muscle, String equipment, String gifUrl) {
        this.exerciseName = exerciseName;
        this.muscle = muscle;
        this.equipment = equipment;
        this.gifUrl = gifUrl;
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
}

