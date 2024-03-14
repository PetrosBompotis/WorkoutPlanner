package com.example.workoutplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RoutineFragment extends Fragment {

    private Long routineId;
    TextView textViewRoutineId;

    // Constructor to receive routineId
    public RoutineFragment(Long routineId) {
        this.routineId = routineId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        textViewRoutineId = view.findViewById(R.id.textViewRoutineId);
        textViewRoutineId.setText("Routine ID: " + routineId);
        return view;
    }
}