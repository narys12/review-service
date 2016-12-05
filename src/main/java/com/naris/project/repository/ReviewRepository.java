package com.naris.project.repository;

import com.naris.project.domain.Review;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Review entity.
 */
@SuppressWarnings("unused")
public interface ReviewRepository extends MongoRepository<Review,String> {

}
