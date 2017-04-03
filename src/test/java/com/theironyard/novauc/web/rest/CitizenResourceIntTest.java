package com.theironyard.novauc.web.rest;

import com.theironyard.novauc.ToDoJhipsterApp;

import com.theironyard.novauc.domain.Citizen;
import com.theironyard.novauc.repository.CitizenRepository;
import com.theironyard.novauc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CitizenResource REST controller.
 *
 * @see CitizenResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToDoJhipsterApp.class)
public class CitizenResourceIntTest {

    private static final String DEFAULT_CITIZEN = "AAAAAAAAAA";
    private static final String UPDATED_CITIZEN = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 10;
    private static final Integer UPDATED_AGE = 11;

    private static final String DEFAULT_JOB = "AAAAAAAAAA";
    private static final String UPDATED_JOB = "BBBBBBBBBB";

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCitizenMockMvc;

    private Citizen citizen;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CitizenResource citizenResource = new CitizenResource(citizenRepository);
        this.restCitizenMockMvc = MockMvcBuilders.standaloneSetup(citizenResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Citizen createEntity(EntityManager em) {
        Citizen citizen = new Citizen()
            .citizen(DEFAULT_CITIZEN)
            .age(DEFAULT_AGE)
            .job(DEFAULT_JOB);
        return citizen;
    }

    @Before
    public void initTest() {
        citizen = createEntity(em);
    }

    @Test
    @Transactional
    public void createCitizen() throws Exception {
        int databaseSizeBeforeCreate = citizenRepository.findAll().size();

        // Create the Citizen
        restCitizenMockMvc.perform(post("/api/citizens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(citizen)))
            .andExpect(status().isCreated());

        // Validate the Citizen in the database
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeCreate + 1);
        Citizen testCitizen = citizenList.get(citizenList.size() - 1);
        assertThat(testCitizen.getCitizen()).isEqualTo(DEFAULT_CITIZEN);
        assertThat(testCitizen.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testCitizen.getJob()).isEqualTo(DEFAULT_JOB);
    }

    @Test
    @Transactional
    public void createCitizenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = citizenRepository.findAll().size();

        // Create the Citizen with an existing ID
        citizen.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitizenMockMvc.perform(post("/api/citizens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(citizen)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = citizenRepository.findAll().size();
        // set the field null
        citizen.setAge(null);

        // Create the Citizen, which fails.

        restCitizenMockMvc.perform(post("/api/citizens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(citizen)))
            .andExpect(status().isBadRequest());

        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCitizens() throws Exception {
        // Initialize the database
        citizenRepository.saveAndFlush(citizen);

        // Get all the citizenList
        restCitizenMockMvc.perform(get("/api/citizens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(citizen.getId().intValue())))
            .andExpect(jsonPath("$.[*].citizen").value(hasItem(DEFAULT_CITIZEN.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB.toString())));
    }

    @Test
    @Transactional
    public void getCitizen() throws Exception {
        // Initialize the database
        citizenRepository.saveAndFlush(citizen);

        // Get the citizen
        restCitizenMockMvc.perform(get("/api/citizens/{id}", citizen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(citizen.getId().intValue()))
            .andExpect(jsonPath("$.citizen").value(DEFAULT_CITIZEN.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.job").value(DEFAULT_JOB.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCitizen() throws Exception {
        // Get the citizen
        restCitizenMockMvc.perform(get("/api/citizens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCitizen() throws Exception {
        // Initialize the database
        citizenRepository.saveAndFlush(citizen);
        int databaseSizeBeforeUpdate = citizenRepository.findAll().size();

        // Update the citizen
        Citizen updatedCitizen = citizenRepository.findOne(citizen.getId());
        updatedCitizen
            .citizen(UPDATED_CITIZEN)
            .age(UPDATED_AGE)
            .job(UPDATED_JOB);

        restCitizenMockMvc.perform(put("/api/citizens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCitizen)))
            .andExpect(status().isOk());

        // Validate the Citizen in the database
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeUpdate);
        Citizen testCitizen = citizenList.get(citizenList.size() - 1);
        assertThat(testCitizen.getCitizen()).isEqualTo(UPDATED_CITIZEN);
        assertThat(testCitizen.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testCitizen.getJob()).isEqualTo(UPDATED_JOB);
    }

    @Test
    @Transactional
    public void updateNonExistingCitizen() throws Exception {
        int databaseSizeBeforeUpdate = citizenRepository.findAll().size();

        // Create the Citizen

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCitizenMockMvc.perform(put("/api/citizens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(citizen)))
            .andExpect(status().isCreated());

        // Validate the Citizen in the database
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCitizen() throws Exception {
        // Initialize the database
        citizenRepository.saveAndFlush(citizen);
        int databaseSizeBeforeDelete = citizenRepository.findAll().size();

        // Get the citizen
        restCitizenMockMvc.perform(delete("/api/citizens/{id}", citizen.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Citizen.class);
    }
}
