package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Rent;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.repository.RentRepository;
import realestatemanagementservice.service.RentService;
import realestatemanagementservice.service.dto.RentDTO;
import realestatemanagementservice.service.mapper.RentMapper;
import realestatemanagementservice.service.dto.RentCriteria;
import realestatemanagementservice.service.RentQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RentResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class RentResourceIT {

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_RECEIVED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECEIVED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RECEIVED_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private RentMapper rentMapper;

    @Autowired
    private RentService rentService;

    @Autowired
    private RentQueryService rentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentMockMvc;

    private Rent rent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rent createEntity(EntityManager em) {
        Rent rent = new Rent()
            .dueDate(DEFAULT_DUE_DATE)
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .amount(DEFAULT_AMOUNT);
        return rent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rent createUpdatedEntity(EntityManager em) {
        Rent rent = new Rent()
            .dueDate(UPDATED_DUE_DATE)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .amount(UPDATED_AMOUNT);
        return rent;
    }

    @BeforeEach
    public void initTest() {
        rent = createEntity(em);
    }

    @Test
    @Transactional
    public void createRent() throws Exception {
        int databaseSizeBeforeCreate = rentRepository.findAll().size();

        // Create the Rent
        RentDTO rentDTO = rentMapper.toDto(rent);
        restRentMockMvc.perform(post("/api/rents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentDTO)))
            .andExpect(status().isCreated());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeCreate + 1);
        Rent testRent = rentList.get(rentList.size() - 1);
        assertThat(testRent.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testRent.getReceivedDate()).isEqualTo(DEFAULT_RECEIVED_DATE);
        assertThat(testRent.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createRentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rentRepository.findAll().size();

        // Create the Rent with an existing ID
        rent.setId(1L);
        RentDTO rentDTO = rentMapper.toDto(rent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentMockMvc.perform(post("/api/rents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRents() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList
        restRentMockMvc.perform(get("/api/rents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rent.getId().intValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getRent() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get the rent
        restRentMockMvc.perform(get("/api/rents/{id}", rent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rent.getId().intValue()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getRentsByIdFiltering() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        Long id = rent.getId();

        defaultRentShouldBeFound("id.equals=" + id);
        defaultRentShouldNotBeFound("id.notEquals=" + id);

        defaultRentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRentShouldNotBeFound("id.greaterThan=" + id);

        defaultRentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRentsByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where dueDate equals to DEFAULT_DUE_DATE
        defaultRentShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the rentList where dueDate equals to UPDATED_DUE_DATE
        defaultRentShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByDueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where dueDate not equals to DEFAULT_DUE_DATE
        defaultRentShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

        // Get all the rentList where dueDate not equals to UPDATED_DUE_DATE
        defaultRentShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultRentShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the rentList where dueDate equals to UPDATED_DUE_DATE
        defaultRentShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where dueDate is not null
        defaultRentShouldBeFound("dueDate.specified=true");

        // Get all the rentList where dueDate is null
        defaultRentShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRentsByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where dueDate is greater than or equal to DEFAULT_DUE_DATE
        defaultRentShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the rentList where dueDate is greater than or equal to UPDATED_DUE_DATE
        defaultRentShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where dueDate is less than or equal to DEFAULT_DUE_DATE
        defaultRentShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the rentList where dueDate is less than or equal to SMALLER_DUE_DATE
        defaultRentShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where dueDate is less than DEFAULT_DUE_DATE
        defaultRentShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the rentList where dueDate is less than UPDATED_DUE_DATE
        defaultRentShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where dueDate is greater than DEFAULT_DUE_DATE
        defaultRentShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the rentList where dueDate is greater than SMALLER_DUE_DATE
        defaultRentShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
    }


    @Test
    @Transactional
    public void getAllRentsByReceivedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where receivedDate equals to DEFAULT_RECEIVED_DATE
        defaultRentShouldBeFound("receivedDate.equals=" + DEFAULT_RECEIVED_DATE);

        // Get all the rentList where receivedDate equals to UPDATED_RECEIVED_DATE
        defaultRentShouldNotBeFound("receivedDate.equals=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByReceivedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where receivedDate not equals to DEFAULT_RECEIVED_DATE
        defaultRentShouldNotBeFound("receivedDate.notEquals=" + DEFAULT_RECEIVED_DATE);

        // Get all the rentList where receivedDate not equals to UPDATED_RECEIVED_DATE
        defaultRentShouldBeFound("receivedDate.notEquals=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByReceivedDateIsInShouldWork() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where receivedDate in DEFAULT_RECEIVED_DATE or UPDATED_RECEIVED_DATE
        defaultRentShouldBeFound("receivedDate.in=" + DEFAULT_RECEIVED_DATE + "," + UPDATED_RECEIVED_DATE);

        // Get all the rentList where receivedDate equals to UPDATED_RECEIVED_DATE
        defaultRentShouldNotBeFound("receivedDate.in=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByReceivedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where receivedDate is not null
        defaultRentShouldBeFound("receivedDate.specified=true");

        // Get all the rentList where receivedDate is null
        defaultRentShouldNotBeFound("receivedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRentsByReceivedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where receivedDate is greater than or equal to DEFAULT_RECEIVED_DATE
        defaultRentShouldBeFound("receivedDate.greaterThanOrEqual=" + DEFAULT_RECEIVED_DATE);

        // Get all the rentList where receivedDate is greater than or equal to UPDATED_RECEIVED_DATE
        defaultRentShouldNotBeFound("receivedDate.greaterThanOrEqual=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByReceivedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where receivedDate is less than or equal to DEFAULT_RECEIVED_DATE
        defaultRentShouldBeFound("receivedDate.lessThanOrEqual=" + DEFAULT_RECEIVED_DATE);

        // Get all the rentList where receivedDate is less than or equal to SMALLER_RECEIVED_DATE
        defaultRentShouldNotBeFound("receivedDate.lessThanOrEqual=" + SMALLER_RECEIVED_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByReceivedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where receivedDate is less than DEFAULT_RECEIVED_DATE
        defaultRentShouldNotBeFound("receivedDate.lessThan=" + DEFAULT_RECEIVED_DATE);

        // Get all the rentList where receivedDate is less than UPDATED_RECEIVED_DATE
        defaultRentShouldBeFound("receivedDate.lessThan=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    public void getAllRentsByReceivedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where receivedDate is greater than DEFAULT_RECEIVED_DATE
        defaultRentShouldNotBeFound("receivedDate.greaterThan=" + DEFAULT_RECEIVED_DATE);

        // Get all the rentList where receivedDate is greater than SMALLER_RECEIVED_DATE
        defaultRentShouldBeFound("receivedDate.greaterThan=" + SMALLER_RECEIVED_DATE);
    }


    @Test
    @Transactional
    public void getAllRentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where amount equals to DEFAULT_AMOUNT
        defaultRentShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the rentList where amount equals to UPDATED_AMOUNT
        defaultRentShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRentsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where amount not equals to DEFAULT_AMOUNT
        defaultRentShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the rentList where amount not equals to UPDATED_AMOUNT
        defaultRentShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultRentShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the rentList where amount equals to UPDATED_AMOUNT
        defaultRentShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where amount is not null
        defaultRentShouldBeFound("amount.specified=true");

        // Get all the rentList where amount is null
        defaultRentShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllRentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultRentShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the rentList where amount is greater than or equal to UPDATED_AMOUNT
        defaultRentShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where amount is less than or equal to DEFAULT_AMOUNT
        defaultRentShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the rentList where amount is less than or equal to SMALLER_AMOUNT
        defaultRentShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where amount is less than DEFAULT_AMOUNT
        defaultRentShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the rentList where amount is less than UPDATED_AMOUNT
        defaultRentShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList where amount is greater than DEFAULT_AMOUNT
        defaultRentShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the rentList where amount is greater than SMALLER_AMOUNT
        defaultRentShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllRentsByLeaseIsEqualToSomething() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);
        Lease lease = LeaseResourceIT.createEntity(em);
        em.persist(lease);
        em.flush();
        rent.setLease(lease);
        rentRepository.saveAndFlush(rent);
        Long leaseId = lease.getId();

        // Get all the rentList where lease equals to leaseId
        defaultRentShouldBeFound("leaseId.equals=" + leaseId);

        // Get all the rentList where lease equals to leaseId + 1
        defaultRentShouldNotBeFound("leaseId.equals=" + (leaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRentShouldBeFound(String filter) throws Exception {
        restRentMockMvc.perform(get("/api/rents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rent.getId().intValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restRentMockMvc.perform(get("/api/rents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRentShouldNotBeFound(String filter) throws Exception {
        restRentMockMvc.perform(get("/api/rents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRentMockMvc.perform(get("/api/rents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRent() throws Exception {
        // Get the rent
        restRentMockMvc.perform(get("/api/rents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRent() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        int databaseSizeBeforeUpdate = rentRepository.findAll().size();

        // Update the rent
        Rent updatedRent = rentRepository.findById(rent.getId()).get();
        // Disconnect from session so that the updates on updatedRent are not directly saved in db
        em.detach(updatedRent);
        updatedRent
            .dueDate(UPDATED_DUE_DATE)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .amount(UPDATED_AMOUNT);
        RentDTO rentDTO = rentMapper.toDto(updatedRent);

        restRentMockMvc.perform(put("/api/rents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentDTO)))
            .andExpect(status().isOk());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeUpdate);
        Rent testRent = rentList.get(rentList.size() - 1);
        assertThat(testRent.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testRent.getReceivedDate()).isEqualTo(UPDATED_RECEIVED_DATE);
        assertThat(testRent.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingRent() throws Exception {
        int databaseSizeBeforeUpdate = rentRepository.findAll().size();

        // Create the Rent
        RentDTO rentDTO = rentMapper.toDto(rent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentMockMvc.perform(put("/api/rents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRent() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        int databaseSizeBeforeDelete = rentRepository.findAll().size();

        // Delete the rent
        restRentMockMvc.perform(delete("/api/rents/{id}", rent.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
