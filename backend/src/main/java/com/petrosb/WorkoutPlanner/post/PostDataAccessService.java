package com.petrosb.WorkoutPlanner.post;

import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlan;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostDataAccessService {
    private final PostRepository postRepository;

    public PostDataAccessService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> selectAllPosts(){
        return postRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")
        );
    }

    public List<Post> selectAllPostsByCustomerId(Long customerId){
        return postRepository.findByCustomerId(customerId);
    }

    public Optional<Post> selectPostByID(Long id) {
        return postRepository.findById(id);
    }

    public void insertPost(Post post) {
        postRepository.save(post);
    }

    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    public boolean existsPostWithId(Long id) {
        return postRepository.existsPostById(id);
    }

    public void updatePostById(Post post) {
        postRepository.save(post);
    }
}
