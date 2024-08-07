package com.example.workoutplanner.adminActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutplanner.AddExerciseActivity;
import com.example.workoutplanner.R;
import com.example.workoutplanner.exerciseActivity.ExerciseActivity;
import com.example.workoutplanner.mainActivity.MainActivity;
import com.example.workoutplanner.manageUsersActivity.ManageUsersActivity;

public class AdminActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void redirectToAddExercise(View view){
        Intent intent = new Intent(AdminActivity.this, AddExerciseActivity.class);
        startActivity(intent);
    }

    public void redirectToExerciseActivity(View view){
        Intent intent = new Intent(AdminActivity.this, ExerciseActivity.class);
        intent.putExtra("routineId", 1L);
        intent.putExtra("isAdmin", true);
        startActivity(intent);
    }

    public void redirectToManageUsers(View view){
        Intent intent = new Intent(AdminActivity.this, ManageUsersActivity.class);
        startActivity(intent);
    }

    public void signOut(View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", "");
        editor.putString("refreshToken", "");
        editor.apply();
        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
        startActivity(intent);
    }
}