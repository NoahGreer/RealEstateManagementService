package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.JobType;
import realestatemanagementservice.repository.JobTypeRepository;
import realestatemanagementservice.service.JobTypeService;
import realestatemanagementservice.service.dto.JobTypeDTO;
import realestatemanagementservice.service.mapper.JobTypeMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JobTypeResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class JobTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private JobTypeRepository jobTypeRepository;

    @Autowired
    private JobTypeMapper jobTypeMapper;

    @Autowired
    private JobTypeService jobTypeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobTypeMockMvc;

    private JobType jobType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobType createEntity(EntityManager em) {
        JobType jobType = new JobType()
            .name(DEFAULT_NAME);
        return jobType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobType createUpdatedEntity(EntityManager em) {
        JobType jobType = new JobType()
            .name(UPDATED_NAME);
        return jobType;
    }

    @BeforeEach
    public void initTest() {
        jobType = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobType() throws Exception {
        int databaseSizeBeforeCreate = jobTypeRepository.findAll().size();

        // Create the JobType
        JobTypeDTO jobTypeDTO = jobTypeMapper.toDto(jobType);
        restJobTypeMockMvc.perform(post("/api/job-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the JobType in the database
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeCreate + 1);
        JobType testJobType = jobTypeList.get(jobTypeList.size() - 1);
        assertThat(testJobType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createJobTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobTypeRepository.findAll().size();

        // Create the JobType with an existing ID
        jobType.setId(1L);
        JobTypeDTO jobTypeDTO = jobTypeMapper.toDto(jobType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobTypeMockMvc.perform(post("/api/job-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobType in the database
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllJobTypes() throws Exception {
        // Initialize the database
        jobTypeRepository.saveAndFlush(jobType);

        // Get all the jobTypeList
        restJobTypeMockMvc.perform(get("/api/job-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getJobType() throws Exception {
        // Initialize the database
        jobTypeRepository.saveAndFlush(jobType);

        // Get the jobType
        restJobTypeMockMvc.perform(get("/api/job-types/{id}", jobType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingJobType() throws Exception {
        // Get the jobType
        restJobTypeMockMvc.perform(get("/api/job-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobType() throws Exception {
        // Initialize the database
        jobTypeRepository.saveAndFlush(jobType);

        int databaseSizeBeforeUpdate = jobTypeRepository.findAll().size();

        // Update the jobType
        JobType updatedJobType = jobTypeRepository.findById(jobType.getId()).get();
        // Disconnect from session so that the updates on updatedJobType are not directly saved in db
        em.detach(updatedJobType);
        updatedJobType
            .name(UPDATED_NAME);
        JobTypeDTO jobTypeDTO = jobTypeMapper.toDto(updatedJobType);

        restJobTypeMockMvc.perform(put("/api/job-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobTypeDTO)))
            .andExpect(status().isOk());

        // Validate the JobType in the database
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeUpdate);
        JobType testJobType = jobTypeList.get(jobTypeList.size() - 1);
        assertThat(testJobType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingJobType() throws Exception {
        int databaseSizeBeforeUpdate = jobTypeRepository.findAll().size();

        // Create the JobType
        JobTypeDTO jobTypeDTO = jobTypeMapper.toDto(jobType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobTypeMockMvc.perform(put("/api/job-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobType in the database
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobType() throws Exception {
        // Initialize the database
        jobTypeRepository.saveAndFlush(jobType);

        int databaseSizeBeforeDelete = jobTypeRepository.findAll().size();

        // Delete the jobType
        restJobTypeMockMvc.perform(delete("/api/job-types/{id}", jobType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
