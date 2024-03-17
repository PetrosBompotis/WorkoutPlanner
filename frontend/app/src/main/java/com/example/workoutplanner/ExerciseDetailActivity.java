package com.example.workoutplanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ExerciseDetailActivity extends AppCompatActivity {
    Button exerciseDoneButton, exerciseDeleteButton, addSetButton;
    TextView exerciseNameTextView, muscleTextView, setTextView;
    ImageView exerciseGifImageView;
    RecyclerView setRecyclerView;
    EditText instructionsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        exerciseDoneButton = findViewById(R.id.exerciseDoneButton);
        exerciseDeleteButton = findViewById(R.id.exerciseDeleteButton);
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView2);
        muscleTextView = findViewById(R.id.muscleTextView2);
        exerciseGifImageView = findViewById(R.id.exerciseGifImageView);
        setTextView = findViewById(R.id.setTextView);
        setRecyclerView = findViewById(R.id.setRecyclerView);
        addSetButton = findViewById(R.id.sddSetButton);
        instructionsEditText = findViewById(R.id.instructionsEditText);

        setUpListeners();
        initializeExtras();
    }

    private void initializeExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String exerciseName = extras.getString("exerciseName");
            String muscle = extras.getString("muscle");
            String equipment = extras.getString("equipment");
            String gifUrl = extras.getString("gifUrl");

            exerciseNameTextView.setText(exerciseName);
            muscleTextView.setText(muscle);
            Glide.with(this).load(gifUrl).into(exerciseGifImageView);
        }
    }

    private void setUpListeners() {
        exerciseDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for exerciseDoneButton
            }
        });

        exerciseDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for exerciseDeleteButton
            }
        });

        addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for addSetButton
            }
        });
    }
}