package com.example.workoutplanner.userActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.workoutplanner.userActivity.communityFragment.CommunityFragment;
import com.example.workoutplanner.userActivity.measurementsFragment.MeasurementsFragment;
import com.example.workoutplanner.R;
import com.example.workoutplanner.userActivity.workoutFragment.WorkoutFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        replaceFragment(new WorkoutFragment());

        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.workout) {
                replaceFragment(new WorkoutFragment());
            } else if (itemId == R.id.community) {
                replaceFragment(new CommunityFragment());
            } else if (itemId == R.id.measurements) {
                replaceFragment(new MeasurementsFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutUser, fragment);
        fragmentTransaction.commit();
    }
}