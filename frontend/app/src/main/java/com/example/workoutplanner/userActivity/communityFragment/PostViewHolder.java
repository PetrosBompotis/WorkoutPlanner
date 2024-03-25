package com.example.workoutplanner.userActivity.communityFragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;
import com.example.workoutplanner.postDetailActivity.PostDetailActivity;

import java.util.List;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView postNameTextView, createdByTextView, difficultyTextView, genderTextView, downloadValueTextView;
    List<PostResponse> posts;
    Boolean isEditable;

    public PostViewHolder(@NonNull View itemView, List<PostResponse> posts, Boolean isEditable) {
        super(itemView);
        this.posts = posts;
        this.isEditable = isEditable;
        postNameTextView = itemView.findViewById(R.id.postNameTextView);
        createdByTextView = itemView.findViewById(R.id.createdByTextView);
        difficultyTextView = itemView.findViewById(R.id.difficultyValueTextView);
        genderTextView = itemView.findViewById(R.id.genderValueTextView);
        downloadValueTextView = itemView.findViewById(R.id.downloadValueTextView);

        // Set click listener for the item
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Get the clicked post
        PostResponse post = posts.get(getAdapterPosition());

        // Redirect to PostDetailActivity with post data
        Context context = itemView.getContext();
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra("postId", post.getPostId());
        intent.putExtra("postName", post.getPostName());
        intent.putExtra("createdBy", post.getCreatedBy());
        intent.putExtra("difficulty", post.getDifficulty());
        intent.putExtra("gender", post.getGender());
        intent.putExtra("workoutPlanId", post.getWorkoutPlanId());
        intent.putExtra("downloadCounter", post.getDownloadCounter());
        intent.putExtra("isEditable", isEditable);
        context.startActivity(intent);
    }
}
