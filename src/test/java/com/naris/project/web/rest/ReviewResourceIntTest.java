package com.naris.project.web.rest;

import com.naris.project.ReviewApp;

import com.naris.project.domain.Review;
import com.naris.project.repository.ReviewRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.naris.project.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReviewResource REST controller.
 *
 * @see ReviewResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReviewApp.class)
public class ReviewResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_PUBLISHED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISHED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_STAR = 1;
    private static final Integer UPDATED_STAR = 2;

    private static final String DEFAULT_PROS = "AAAAAAAAAA";
    private static final String UPDATED_PROS = "BBBBBBBBBB";

    private static final String DEFAULT_CONS = "AAAAAAAAAA";
    private static final String UPDATED_CONS = "BBBBBBBBBB";

    @Inject
    private ReviewRepository reviewRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restReviewMockMvc;

    private Review review;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReviewResource reviewResource = new ReviewResource();
        ReflectionTestUtils.setField(reviewResource, "reviewRepository", reviewRepository);
        this.restReviewMockMvc = MockMvcBuilders.standaloneSetup(reviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createEntity() {
        Review review = new Review()
                .title(DEFAULT_TITLE)
                .comment(DEFAULT_COMMENT)
                .created(DEFAULT_CREATED)
                .published(DEFAULT_PUBLISHED)
                .star(DEFAULT_STAR)
                .pros(DEFAULT_PROS)
                .cons(DEFAULT_CONS);
        return review;
    }

    @Before
    public void initTest() {
        reviewRepository.deleteAll();
        review = createEntity();
    }

    @Test
    public void createReview() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // Create the Review

        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isCreated());

        // Validate the Review in the database
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeCreate + 1);
        Review testReview = reviews.get(reviews.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReview.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testReview.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testReview.getPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testReview.getStar()).isEqualTo(DEFAULT_STAR);
        assertThat(testReview.getPros()).isEqualTo(DEFAULT_PROS);
        assertThat(testReview.getCons()).isEqualTo(DEFAULT_CONS);
    }

    @Test
    public void getAllReviews() throws Exception {
        // Initialize the database
        reviewRepository.save(review);

        // Get all the reviews
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].published").value(hasItem(sameInstant(DEFAULT_PUBLISHED))))
            .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR)))
            .andExpect(jsonPath("$.[*].pros").value(hasItem(DEFAULT_PROS.toString())))
            .andExpect(jsonPath("$.[*].cons").value(hasItem(DEFAULT_CONS.toString())));
    }

    @Test
    public void getReview() throws Exception {
        // Initialize the database
        reviewRepository.save(review);

        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(review.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.published").value(sameInstant(DEFAULT_PUBLISHED)))
            .andExpect(jsonPath("$.star").value(DEFAULT_STAR))
            .andExpect(jsonPath("$.pros").value(DEFAULT_PROS.toString()))
            .andExpect(jsonPath("$.cons").value(DEFAULT_CONS.toString()));
    }

    @Test
    public void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateReview() throws Exception {
        // Initialize the database
        reviewRepository.save(review);
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review
        Review updatedReview = reviewRepository.findOne(review.getId());
        updatedReview
                .title(UPDATED_TITLE)
                .comment(UPDATED_COMMENT)
                .created(UPDATED_CREATED)
                .published(UPDATED_PUBLISHED)
                .star(UPDATED_STAR)
                .pros(UPDATED_PROS)
                .cons(UPDATED_CONS);

        restReviewMockMvc.perform(put("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReview)))
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviews.get(reviews.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReview.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testReview.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testReview.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testReview.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testReview.getPros()).isEqualTo(UPDATED_PROS);
        assertThat(testReview.getCons()).isEqualTo(UPDATED_CONS);
    }

    @Test
    public void deleteReview() throws Exception {
        // Initialize the database
        reviewRepository.save(review);
        int databaseSizeBeforeDelete = reviewRepository.findAll().size();

        // Get the review
        restReviewMockMvc.perform(delete("/api/reviews/{id}", review.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeDelete - 1);
    }
}
