package com.example.workoutplanner.userActivity.workoutFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class RoutinePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Long> routineIdsList;
    private ArrayList<String> routineNamesList;

    public RoutinePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        routineIdsList = new ArrayList<>();
        routineNamesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Create a new fragment for each routine
        Long routineId = routineIdsList.get(position);
        return new RoutineFragment(routineId);
    }

    @Override
    public int getCount() {
        // Return the total number of routines
        return routineIdsList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // Return the title for each tab (You can use routine names here)
        return routineNamesList.get(position);
    }

    public void setRoutineIdsList(ArrayList<Long> routineIdsList) {
        this.routineIdsList.clear();
        this.routineIdsList.addAll(routineIdsList);
        notifyDataSetChanged();
    }

    public void setRoutineNamesList(ArrayList<String> routineNamesList) {
        this.routineNamesList.clear();
        this.routineNamesList.addAll(routineNamesList);
        notifyDataSetChanged();
    }
}
