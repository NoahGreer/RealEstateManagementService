package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Infraction;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.repository.InfractionRepository;
import realestatemanagementservice.service.InfractionService;
import realestatemanagementservice.service.dto.InfractionDTO;
import realestatemanagementservice.service.mapper.InfractionMapper;
import realestatemanagementservice.service.dto.InfractionCriteria;
import realestatemanagementservice.service.InfractionQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InfractionResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class InfractionResourceIT {

    private static final LocalDate DEFAULT_DATE_OCCURRED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OCCURRED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OCCURRED = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CAUSE = "AAAAAAAAAA";
    private static final String UPDATED_CAUSE = "BBBBBBBBBB";

    private static final String DEFAULT_RESOLUTION = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUTION = "BBBBBBBBBB";

    @Autowired
    private InfractionRepository infractionRepository;

    @Autowired
    private InfractionMapper infractionMapper;

    @Autowired
    private InfractionService infractionService;

    @Autowired
    private InfractionQueryService infractionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfractionMockMvc;

    private Infraction infraction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Infraction createEntity(EntityManager em) {
        Infraction infraction = new Infraction()
            .dateOccurred(DEFAULT_DATE_OCCURRED)
            .cause(DEFAULT_CAUSE)
            .resolution(DEFAULT_RESOLUTION);
        return infraction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Infraction createUpdatedEntity(EntityManager em) {
        Infraction infraction = new Infraction()
            .dateOccurred(UPDATED_DATE_OCCURRED)
            .cause(UPDATED_CAUSE)
            .resolution(UPDATED_RESOLUTION);
        return infraction;
    }

    @BeforeEach
    public void initTest() {
        infraction = createEntity(em);
    }

    @Test
    @Transactional
    public void createInfraction() throws Exception {
        int databaseSizeBeforeCreate = infractionRepository.findAll().size();

        // Create the Infraction
        InfractionDTO infractionDTO = infractionMapper.toDto(infraction);
        restInfractionMockMvc.perform(post("/api/infractions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infractionDTO)))
            .andExpect(status().isCreated());

        // Validate the Infraction in the database
        List<Infraction> infractionList = infractionRepository.findAll();
        assertThat(infractionList).hasSize(databaseSizeBeforeCreate + 1);
        Infraction testInfraction = infractionList.get(infractionList.size() - 1);
        assertThat(testInfraction.getDateOccurred()).isEqualTo(DEFAULT_DATE_OCCURRED);
        assertThat(testInfraction.getCause()).isEqualTo(DEFAULT_CAUSE);
        assertThat(testInfraction.getResolution()).isEqualTo(DEFAULT_RESOLUTION);
    }

    @Test
    @Transactional
    public void createInfractionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = infractionRepository.findAll().size();

        // Create the Infraction with an existing ID
        infraction.setId(1L);
        InfractionDTO infractionDTO = infractionMapper.toDto(infraction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfractionMockMvc.perform(post("/api/infractions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infractionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Infraction in the database
        List<Infraction> infractionList = infractionRepository.findAll();
        assertThat(infractionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInfractions() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList
        restInfractionMockMvc.perform(get("/api/infractions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraction.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOccurred").value(hasItem(DEFAULT_DATE_OCCURRED.toString())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE)))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION)));
    }
    
    @Test
    @Transactional
    public void getInfraction() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get the infraction
        restInfractionMockMvc.perform(get("/api/infractions/{id}", infraction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infraction.getId().intValue()))
            .andExpect(jsonPath("$.dateOccurred").value(DEFAULT_DATE_OCCURRED.toString()))
            .andExpect(jsonPath("$.cause").value(DEFAULT_CAUSE))
            .andExpect(jsonPath("$.resolution").value(DEFAULT_RESOLUTION));
    }


    @Test
    @Transactional
    public void getInfractionsByIdFiltering() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        Long id = infraction.getId();

        defaultInfractionShouldBeFound("id.equals=" + id);
        defaultInfractionShouldNotBeFound("id.notEquals=" + id);

        defaultInfractionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInfractionShouldNotBeFound("id.greaterThan=" + id);

        defaultInfractionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInfractionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInfractionsByDateOccurredIsEqualToSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where dateOccurred equals to DEFAULT_DATE_OCCURRED
        defaultInfractionShouldBeFound("dateOccurred.equals=" + DEFAULT_DATE_OCCURRED);

        // Get all the infractionList where dateOccurred equals to UPDATED_DATE_OCCURRED
        defaultInfractionShouldNotBeFound("dateOccurred.equals=" + UPDATED_DATE_OCCURRED);
    }

    @Test
    @Transactional
    public void getAllInfractionsByDateOccurredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where dateOccurred not equals to DEFAULT_DATE_OCCURRED
        defaultInfractionShouldNotBeFound("dateOccurred.notEquals=" + DEFAULT_DATE_OCCURRED);

        // Get all the infractionList where dateOccurred not equals to UPDATED_DATE_OCCURRED
        defaultInfractionShouldBeFound("dateOccurred.notEquals=" + UPDATED_DATE_OCCURRED);
    }

    @Test
    @Transactional
    public void getAllInfractionsByDateOccurredIsInShouldWork() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where dateOccurred in DEFAULT_DATE_OCCURRED or UPDATED_DATE_OCCURRED
        defaultInfractionShouldBeFound("dateOccurred.in=" + DEFAULT_DATE_OCCURRED + "," + UPDATED_DATE_OCCURRED);

        // Get all the infractionList where dateOccurred equals to UPDATED_DATE_OCCURRED
        defaultInfractionShouldNotBeFound("dateOccurred.in=" + UPDATED_DATE_OCCURRED);
    }

    @Test
    @Transactional
    public void getAllInfractionsByDateOccurredIsNullOrNotNull() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where dateOccurred is not null
        defaultInfractionShouldBeFound("dateOccurred.specified=true");

        // Get all the infractionList where dateOccurred is null
        defaultInfractionShouldNotBeFound("dateOccurred.specified=false");
    }

    @Test
    @Transactional
    public void getAllInfractionsByDateOccurredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where dateOccurred is greater than or equal to DEFAULT_DATE_OCCURRED
        defaultInfractionShouldBeFound("dateOccurred.greaterThanOrEqual=" + DEFAULT_DATE_OCCURRED);

        // Get all the infractionList where dateOccurred is greater than or equal to UPDATED_DATE_OCCURRED
        defaultInfractionShouldNotBeFound("dateOccurred.greaterThanOrEqual=" + UPDATED_DATE_OCCURRED);
    }

    @Test
    @Transactional
    public void getAllInfractionsByDateOccurredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where dateOccurred is less than or equal to DEFAULT_DATE_OCCURRED
        defaultInfractionShouldBeFound("dateOccurred.lessThanOrEqual=" + DEFAULT_DATE_OCCURRED);

        // Get all the infractionList where dateOccurred is less than or equal to SMALLER_DATE_OCCURRED
        defaultInfractionShouldNotBeFound("dateOccurred.lessThanOrEqual=" + SMALLER_DATE_OCCURRED);
    }

    @Test
    @Transactional
    public void getAllInfractionsByDateOccurredIsLessThanSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where dateOccurred is less than DEFAULT_DATE_OCCURRED
        defaultInfractionShouldNotBeFound("dateOccurred.lessThan=" + DEFAULT_DATE_OCCURRED);

        // Get all the infractionList where dateOccurred is less than UPDATED_DATE_OCCURRED
        defaultInfractionShouldBeFound("dateOccurred.lessThan=" + UPDATED_DATE_OCCURRED);
    }

    @Test
    @Transactional
    public void getAllInfractionsByDateOccurredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where dateOccurred is greater than DEFAULT_DATE_OCCURRED
        defaultInfractionShouldNotBeFound("dateOccurred.greaterThan=" + DEFAULT_DATE_OCCURRED);

        // Get all the infractionList where dateOccurred is greater than SMALLER_DATE_OCCURRED
        defaultInfractionShouldBeFound("dateOccurred.greaterThan=" + SMALLER_DATE_OCCURRED);
    }


    @Test
    @Transactional
    public void getAllInfractionsByCauseIsEqualToSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where cause equals to DEFAULT_CAUSE
        defaultInfractionShouldBeFound("cause.equals=" + DEFAULT_CAUSE);

        // Get all the infractionList where cause equals to UPDATED_CAUSE
        defaultInfractionShouldNotBeFound("cause.equals=" + UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void getAllInfractionsByCauseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where cause not equals to DEFAULT_CAUSE
        defaultInfractionShouldNotBeFound("cause.notEquals=" + DEFAULT_CAUSE);

        // Get all the infractionList where cause not equals to UPDATED_CAUSE
        defaultInfractionShouldBeFound("cause.notEquals=" + UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void getAllInfractionsByCauseIsInShouldWork() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where cause in DEFAULT_CAUSE or UPDATED_CAUSE
        defaultInfractionShouldBeFound("cause.in=" + DEFAULT_CAUSE + "," + UPDATED_CAUSE);

        // Get all the infractionList where cause equals to UPDATED_CAUSE
        defaultInfractionShouldNotBeFound("cause.in=" + UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void getAllInfractionsByCauseIsNullOrNotNull() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where cause is not null
        defaultInfractionShouldBeFound("cause.specified=true");

        // Get all the infractionList where cause is null
        defaultInfractionShouldNotBeFound("cause.specified=false");
    }
                @Test
    @Transactional
    public void getAllInfractionsByCauseContainsSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where cause contains DEFAULT_CAUSE
        defaultInfractionShouldBeFound("cause.contains=" + DEFAULT_CAUSE);

        // Get all the infractionList where cause contains UPDATED_CAUSE
        defaultInfractionShouldNotBeFound("cause.contains=" + UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void getAllInfractionsByCauseNotContainsSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where cause does not contain DEFAULT_CAUSE
        defaultInfractionShouldNotBeFound("cause.doesNotContain=" + DEFAULT_CAUSE);

        // Get all the infractionList where cause does not contain UPDATED_CAUSE
        defaultInfractionShouldBeFound("cause.doesNotContain=" + UPDATED_CAUSE);
    }


    @Test
    @Transactional
    public void getAllInfractionsByResolutionIsEqualToSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where resolution equals to DEFAULT_RESOLUTION
        defaultInfractionShouldBeFound("resolution.equals=" + DEFAULT_RESOLUTION);

        // Get all the infractionList where resolution equals to UPDATED_RESOLUTION
        defaultInfractionShouldNotBeFound("resolution.equals=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    public void getAllInfractionsByResolutionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where resolution not equals to DEFAULT_RESOLUTION
        defaultInfractionShouldNotBeFound("resolution.notEquals=" + DEFAULT_RESOLUTION);

        // Get all the infractionList where resolution not equals to UPDATED_RESOLUTION
        defaultInfractionShouldBeFound("resolution.notEquals=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    public void getAllInfractionsByResolutionIsInShouldWork() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where resolution in DEFAULT_RESOLUTION or UPDATED_RESOLUTION
        defaultInfractionShouldBeFound("resolution.in=" + DEFAULT_RESOLUTION + "," + UPDATED_RESOLUTION);

        // Get all the infractionList where resolution equals to UPDATED_RESOLUTION
        defaultInfractionShouldNotBeFound("resolution.in=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    public void getAllInfractionsByResolutionIsNullOrNotNull() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where resolution is not null
        defaultInfractionShouldBeFound("resolution.specified=true");

        // Get all the infractionList where resolution is null
        defaultInfractionShouldNotBeFound("resolution.specified=false");
    }
                @Test
    @Transactional
    public void getAllInfractionsByResolutionContainsSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where resolution contains DEFAULT_RESOLUTION
        defaultInfractionShouldBeFound("resolution.contains=" + DEFAULT_RESOLUTION);

        // Get all the infractionList where resolution contains UPDATED_RESOLUTION
        defaultInfractionShouldNotBeFound("resolution.contains=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    public void getAllInfractionsByResolutionNotContainsSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        // Get all the infractionList where resolution does not contain DEFAULT_RESOLUTION
        defaultInfractionShouldNotBeFound("resolution.doesNotContain=" + DEFAULT_RESOLUTION);

        // Get all the infractionList where resolution does not contain UPDATED_RESOLUTION
        defaultInfractionShouldBeFound("resolution.doesNotContain=" + UPDATED_RESOLUTION);
    }


    @Test
    @Transactional
    public void getAllInfractionsByLeaseIsEqualToSomething() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);
        Lease lease = LeaseResourceIT.createEntity(em);
        em.persist(lease);
        em.flush();
        infraction.setLease(lease);
        infractionRepository.saveAndFlush(infraction);
        Long leaseId = lease.getId();

        // Get all the infractionList where lease equals to leaseId
        defaultInfractionShouldBeFound("leaseId.equals=" + leaseId);

        // Get all the infractionList where lease equals to leaseId + 1
        defaultInfractionShouldNotBeFound("leaseId.equals=" + (leaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInfractionShouldBeFound(String filter) throws Exception {
        restInfractionMockMvc.perform(get("/api/infractions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraction.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOccurred").value(hasItem(DEFAULT_DATE_OCCURRED.toString())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE)))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION)));

        // Check, that the count call also returns 1
        restInfractionMockMvc.perform(get("/api/infractions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInfractionShouldNotBeFound(String filter) throws Exception {
        restInfractionMockMvc.perform(get("/api/infractions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInfractionMockMvc.perform(get("/api/infractions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInfraction() throws Exception {
        // Get the infraction
        restInfractionMockMvc.perform(get("/api/infractions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInfraction() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        int databaseSizeBeforeUpdate = infractionRepository.findAll().size();

        // Update the infraction
        Infraction updatedInfraction = infractionRepository.findById(infraction.getId()).get();
        // Disconnect from session so that the updates on updatedInfraction are not directly saved in db
        em.detach(updatedInfraction);
        updatedInfraction
            .dateOccurred(UPDATED_DATE_OCCURRED)
            .cause(UPDATED_CAUSE)
            .resolution(UPDATED_RESOLUTION);
        InfractionDTO infractionDTO = infractionMapper.toDto(updatedInfraction);

        restInfractionMockMvc.perform(put("/api/infractions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infractionDTO)))
            .andExpect(status().isOk());

        // Validate the Infraction in the database
        List<Infraction> infractionList = infractionRepository.findAll();
        assertThat(infractionList).hasSize(databaseSizeBeforeUpdate);
        Infraction testInfraction = infractionList.get(infractionList.size() - 1);
        assertThat(testInfraction.getDateOccurred()).isEqualTo(UPDATED_DATE_OCCURRED);
        assertThat(testInfraction.getCause()).isEqualTo(UPDATED_CAUSE);
        assertThat(testInfraction.getResolution()).isEqualTo(UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    public void updateNonExistingInfraction() throws Exception {
        int databaseSizeBeforeUpdate = infractionRepository.findAll().size();

        // Create the Infraction
        InfractionDTO infractionDTO = infractionMapper.toDto(infraction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfractionMockMvc.perform(put("/api/infractions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infractionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Infraction in the database
        List<Infraction> infractionList = infractionRepository.findAll();
        assertThat(infractionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInfraction() throws Exception {
        // Initialize the database
        infractionRepository.saveAndFlush(infraction);

        int databaseSizeBeforeDelete = infractionRepository.findAll().size();

        // Delete the infraction
        restInfractionMockMvc.perform(delete("/api/infractions/{id}", infraction.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Infraction> infractionList = infractionRepository.findAll();
        assertThat(infractionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
