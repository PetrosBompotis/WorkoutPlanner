package com.petrosb.WorkoutPlanner.post;

import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlanUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/customers/{customerId}/posts")
    public List<Post> getAllPostsByCustomerId(@PathVariable(value = "customerId") Long customerId) {
        return postService.getAllPostsByCustomerId(customerId);
    }

    @PostMapping("/customers/{customerId}/workoutPlans/{workoutPlanId}/posts")
    public void createPost(@PathVariable(value = "customerId") Long customerId,
                           @PathVariable(value = "workoutPlanId") Long workoutPlanId,
                           @RequestBody PostCreationRequest postCreationRequest) {
        postService.addPost(postCreationRequest, customerId, workoutPlanId);
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable("postId") Long postId) {
        postService.deletePostById(postId);
    }

    @PutMapping("/posts/{postId}")
    public void updatePost(@RequestBody PostUpdateRequest updateRequest,
                           @PathVariable("postId") Long postId) {
        postService.updatePost(updateRequest, postId);
    }
}
