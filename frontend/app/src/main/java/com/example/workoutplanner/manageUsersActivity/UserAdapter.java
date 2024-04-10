package com.example.workoutplanner.manageUsersActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private List<UserResponse> users;

    public UserAdapter(List<UserResponse> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserResponse user = users.get(position);
        holder.userNameTextView.setText(user.getName());
        holder.userAgeTextView.setText(String.valueOf(user.getAge()));
        holder.userGenderTextView.setText(user.getGender());
        holder.userEmailTextView.setText(user.getEmail());
        holder.deleteUserImageButton.setTag(user.getUserId().toString());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void filterList(List<UserResponse> filteredExercises) {
        users = filteredExercises;
        notifyDataSetChanged();
    }
}
