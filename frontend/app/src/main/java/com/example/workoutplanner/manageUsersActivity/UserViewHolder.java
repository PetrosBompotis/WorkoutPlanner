package com.example.workoutplanner.manageUsersActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;
import com.example.workoutplanner.exerciseDetailActivity.ExerciseDetailActivity;

public class UserViewHolder extends RecyclerView.ViewHolder{
    TextView userEmailTextView, userNameTextView, userAgeTextView, userGenderTextView;
    ImageButton deleteUserImageButton;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
        userNameTextView = itemView.findViewById(R.id.userNameValueTextView);
        userAgeTextView = itemView.findViewById(R.id.userAgeValueTextView);
        userGenderTextView = itemView.findViewById(R.id.userGenderValueTextView);
        deleteUserImageButton = itemView.findViewById(R.id.deleteUserButton);

        deleteUserImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setId = (String) v.getTag();
                ((ManageUsersActivity)itemView.getContext()).deleteUser(Long.parseLong(setId));
            }
        });
    }
}
