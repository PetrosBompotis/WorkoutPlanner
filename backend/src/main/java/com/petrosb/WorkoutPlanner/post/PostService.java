package com.petrosb.WorkoutPlanner.post;

import com.petrosb.WorkoutPlanner.customer.Customer;
import com.petrosb.WorkoutPlanner.customer.CustomerDataAccessService;
import com.petrosb.WorkoutPlanner.exception.RequestValidationException;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlan;
import com.petrosb.WorkoutPlanner.workoutPlan.WorkoutPlanDataAccessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostDataAccessService postDataAccessService;
    private final CustomerDataAccessService customerDataAccessService;
    private final WorkoutPlanDataAccessService workoutPlanDataAccessService;

    public PostService(PostDataAccessService postDataAccessService, CustomerDataAccessService customerDataAccessService, WorkoutPlanDataAccessService workoutPlanDataAccessService) {
        this.postDataAccessService = postDataAccessService;
        this.customerDataAccessService = customerDataAccessService;
        this.workoutPlanDataAccessService = workoutPlanDataAccessService;
    }

    public List<Post> getAllPosts(){
        return postDataAccessService.selectAllPosts();
    }

    public List<Post> getAllPostsByCustomerId(Long customerId){
        if(!customerDataAccessService.existsPersonWithId(customerId)){
            throw new ResourceNotFoundException("Customer with id [%s] not found".formatted(customerId));
        }

        return postDataAccessService.selectAllPostsByCustomerId(customerId);
    }

    public void addPost(PostCreationRequest postCreationRequest, Long customerId, Long workoutPlanId){
        Customer customer = customerDataAccessService.selectCustomerByID(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id [%s] not found".formatted(customerId)
                ));

        WorkoutPlan workoutPlan = workoutPlanDataAccessService.selectWorkoutPlanByID(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan with id [%s] not found".formatted(workoutPlanId)
                ));

        Post post = new Post(
                postCreationRequest.postName(),
                postCreationRequest.createdBy(),
                customer,
                workoutPlan,
                0);

        postDataAccessService.insertPost(post);
    }

    public void deletePostById(Long postId){
        //check if id exists
        if(!postDataAccessService.existsPostWithId(postId)){
            throw new ResourceNotFoundException("Post with id [%s] not found".formatted(postId));
        }

        //otherwise remove
        postDataAccessService.deletePostById(postId);
    }

    public void updatePost(PostUpdateRequest updateRequest, Long postId){

        Post post = postDataAccessService.selectPostByID(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post with id [%s] not found".formatted(postId)
                ));
        boolean changes = false;
        //check if attributes need change exists
        if (updateRequest.postName() != null && !updateRequest.postName().equals(post.getPostName())){
            post.setPostName(updateRequest.postName());
            changes = true;
        }

        if (updateRequest.downloadCounter() != null && !updateRequest.downloadCounter().equals(post.getDownloadCounter())){
            post.setDownloadCounter(updateRequest.downloadCounter());
            changes = true;
        }

        //otherwise update
        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        postDataAccessService.updatePostById(post);
    }
}
