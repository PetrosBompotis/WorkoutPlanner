package com.example.workoutplanner.userActivity.communityFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private List<PostResponse> posts;
    private Boolean isEditable;

    public PostAdapter(List<PostResponse> posts, Boolean isEditable) {
        this.posts = posts;
        this.isEditable = isEditable;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view, posts, isEditable);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostResponse post = posts.get(position);
        holder.postNameTextView.setText(post.getPostName());
        holder.createdByTextView.setText(post.getCreatedBy());
        holder.difficultyTextView.setText(post.getDifficulty());
        holder.genderTextView.setText(post.getGender());
        holder.downloadValueTextView.setText(String.valueOf(post.getDownloadCounter()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void filterList(List<PostResponse> filteredPosts) {
        posts = filteredPosts;
        notifyDataSetChanged();
    }
}
