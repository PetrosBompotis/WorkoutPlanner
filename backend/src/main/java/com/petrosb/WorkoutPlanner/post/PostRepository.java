package com.petrosb.WorkoutPlanner.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCustomerId(Long customerId);
    boolean existsPostById(Long Id);
}
