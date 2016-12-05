package com.naris.project.repository;

import com.naris.project.domain.ReviewUser;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ReviewUser entity.
 */
@SuppressWarnings("unused")
public interface ReviewUserRepository extends MongoRepository<ReviewUser,String> {

}
