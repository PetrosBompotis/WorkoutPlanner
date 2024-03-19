package com.example.workoutplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment {
    private TextView usernameTextView, genderTextView, ageTextView, emailTextView;
    private ShapeableImageView profileImageView;
    private UserActivity userActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userActivity = (UserActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize TextViews
        usernameTextView = view.findViewById(R.id.usernameTextView);
        genderTextView = view.findViewById(R.id.genderTextView);
        ageTextView = view.findViewById(R.id.ageTextView);
        emailTextView = view.findViewById(R.id.emailTextView);

        // Initialize ImageView
        profileImageView = view.findViewById(R.id.profileImageView);

        String gender = userActivity.sharedPreferences.getString("gender", "d");

        // Sample data (replace with your actual data)
        usernameTextView.setText(userActivity.sharedPreferences.getString("name", "d"));
        genderTextView.setText(gender);
        ageTextView.setText(String.valueOf(userActivity.sharedPreferences.getInt("age", 4)));
        emailTextView.setText(userActivity.sharedPreferences.getString("email", "d"));

        if (gender.equals("MALE")){
            profileImageView.setImageResource(R.drawable.man_profile);
        }else {
            profileImageView.setImageResource(R.drawable.woman_profile);
        }

        return view;
    }
}