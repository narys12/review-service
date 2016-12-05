package com.naris.project.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.naris.project.domain.ReviewUser;

import com.naris.project.repository.ReviewUserRepository;
import com.naris.project.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ReviewUser.
 */
@RestController
@RequestMapping("/api")
public class ReviewUserResource {

    private final Logger log = LoggerFactory.getLogger(ReviewUserResource.class);
        
    @Inject
    private ReviewUserRepository reviewUserRepository;

    /**
     * POST  /review-users : Create a new reviewUser.
     *
     * @param reviewUser the reviewUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reviewUser, or with status 400 (Bad Request) if the reviewUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/review-users")
    @Timed
    public ResponseEntity<ReviewUser> createReviewUser(@Valid @RequestBody ReviewUser reviewUser) throws URISyntaxException {
        log.debug("REST request to save ReviewUser : {}", reviewUser);
        if (reviewUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("reviewUser", "idexists", "A new reviewUser cannot already have an ID")).body(null);
        }
        ReviewUser result = reviewUserRepository.save(reviewUser);
        return ResponseEntity.created(new URI("/api/review-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("reviewUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /review-users : Updates an existing reviewUser.
     *
     * @param reviewUser the reviewUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reviewUser,
     * or with status 400 (Bad Request) if the reviewUser is not valid,
     * or with status 500 (Internal Server Error) if the reviewUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/review-users")
    @Timed
    public ResponseEntity<ReviewUser> updateReviewUser(@Valid @RequestBody ReviewUser reviewUser) throws URISyntaxException {
        log.debug("REST request to update ReviewUser : {}", reviewUser);
        if (reviewUser.getId() == null) {
            return createReviewUser(reviewUser);
        }
        ReviewUser result = reviewUserRepository.save(reviewUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("reviewUser", reviewUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /review-users : get all the reviewUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reviewUsers in body
     */
    @GetMapping("/review-users")
    @Timed
    public List<ReviewUser> getAllReviewUsers() {
        log.debug("REST request to get all ReviewUsers");
        List<ReviewUser> reviewUsers = reviewUserRepository.findAll();
        return reviewUsers;
    }

    /**
     * GET  /review-users/:id : get the "id" reviewUser.
     *
     * @param id the id of the reviewUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reviewUser, or with status 404 (Not Found)
     */
    @GetMapping("/review-users/{id}")
    @Timed
    public ResponseEntity<ReviewUser> getReviewUser(@PathVariable String id) {
        log.debug("REST request to get ReviewUser : {}", id);
        ReviewUser reviewUser = reviewUserRepository.findOne(id);
        return Optional.ofNullable(reviewUser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /review-users/:id : delete the "id" reviewUser.
     *
     * @param id the id of the reviewUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/review-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteReviewUser(@PathVariable String id) {
        log.debug("REST request to delete ReviewUser : {}", id);
        reviewUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("reviewUser", id.toString())).build();
    }

}
