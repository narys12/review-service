package com.naris.project.web.rest;

import com.naris.project.ReviewApp;

import com.naris.project.domain.ReviewUser;
import com.naris.project.repository.ReviewUserRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReviewUserResource REST controller.
 *
 * @see ReviewUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReviewApp.class)
public class ReviewUserResourceIntTest {

    private static final String DEFAULT_NICK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NICK_NAME = "BBBBBBBBBB";

    @Inject
    private ReviewUserRepository reviewUserRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restReviewUserMockMvc;

    private ReviewUser reviewUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReviewUserResource reviewUserResource = new ReviewUserResource();
        ReflectionTestUtils.setField(reviewUserResource, "reviewUserRepository", reviewUserRepository);
        this.restReviewUserMockMvc = MockMvcBuilders.standaloneSetup(reviewUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewUser createEntity() {
        ReviewUser reviewUser = new ReviewUser()
                .nickName(DEFAULT_NICK_NAME);
        return reviewUser;
    }

    @Before
    public void initTest() {
        reviewUserRepository.deleteAll();
        reviewUser = createEntity();
    }

    @Test
    public void createReviewUser() throws Exception {
        int databaseSizeBeforeCreate = reviewUserRepository.findAll().size();

        // Create the ReviewUser

        restReviewUserMockMvc.perform(post("/api/review-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewUser)))
            .andExpect(status().isCreated());

        // Validate the ReviewUser in the database
        List<ReviewUser> reviewUsers = reviewUserRepository.findAll();
        assertThat(reviewUsers).hasSize(databaseSizeBeforeCreate + 1);
        ReviewUser testReviewUser = reviewUsers.get(reviewUsers.size() - 1);
        assertThat(testReviewUser.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
    }

    @Test
    public void checkNickNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewUserRepository.findAll().size();
        // set the field null
        reviewUser.setNickName(null);

        // Create the ReviewUser, which fails.

        restReviewUserMockMvc.perform(post("/api/review-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewUser)))
            .andExpect(status().isBadRequest());

        List<ReviewUser> reviewUsers = reviewUserRepository.findAll();
        assertThat(reviewUsers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllReviewUsers() throws Exception {
        // Initialize the database
        reviewUserRepository.save(reviewUser);

        // Get all the reviewUsers
        restReviewUserMockMvc.perform(get("/api/review-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewUser.getId())))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME.toString())));
    }

    @Test
    public void getReviewUser() throws Exception {
        // Initialize the database
        reviewUserRepository.save(reviewUser);

        // Get the reviewUser
        restReviewUserMockMvc.perform(get("/api/review-users/{id}", reviewUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reviewUser.getId()))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME.toString()));
    }

    @Test
    public void getNonExistingReviewUser() throws Exception {
        // Get the reviewUser
        restReviewUserMockMvc.perform(get("/api/review-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateReviewUser() throws Exception {
        // Initialize the database
        reviewUserRepository.save(reviewUser);
        int databaseSizeBeforeUpdate = reviewUserRepository.findAll().size();

        // Update the reviewUser
        ReviewUser updatedReviewUser = reviewUserRepository.findOne(reviewUser.getId());
        updatedReviewUser
                .nickName(UPDATED_NICK_NAME);

        restReviewUserMockMvc.perform(put("/api/review-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReviewUser)))
            .andExpect(status().isOk());

        // Validate the ReviewUser in the database
        List<ReviewUser> reviewUsers = reviewUserRepository.findAll();
        assertThat(reviewUsers).hasSize(databaseSizeBeforeUpdate);
        ReviewUser testReviewUser = reviewUsers.get(reviewUsers.size() - 1);
        assertThat(testReviewUser.getNickName()).isEqualTo(UPDATED_NICK_NAME);
    }

    @Test
    public void deleteReviewUser() throws Exception {
        // Initialize the database
        reviewUserRepository.save(reviewUser);
        int databaseSizeBeforeDelete = reviewUserRepository.findAll().size();

        // Get the reviewUser
        restReviewUserMockMvc.perform(delete("/api/review-users/{id}", reviewUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReviewUser> reviewUsers = reviewUserRepository.findAll();
        assertThat(reviewUsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
