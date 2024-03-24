package com.example.workoutplanner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

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
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostResponse post = posts.get(position);
        holder.postNameTextView.setText(post.getPostName());
        holder.createdByTextView.setText(post.getCreatedBy());
        holder.difficultyTextView.setText(post.getDifficulty());
        holder.genderTextView.setText(post.getGender());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView postNameTextView, createdByTextView, difficultyTextView, genderTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postNameTextView = itemView.findViewById(R.id.postNameTextView);
            createdByTextView = itemView.findViewById(R.id.createdByTextView);
            difficultyTextView = itemView.findViewById(R.id.difficultyValueTextView);
            genderTextView = itemView.findViewById(R.id.genderValueTextView);

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
            intent.putExtra("isEditable", isEditable);
            context.startActivity(intent);
        }
    }

    public void filterList(List<PostResponse> filteredPosts) {
        posts = filteredPosts;
        notifyDataSetChanged();
    }
}
