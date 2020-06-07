package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Apartment;
import realestatemanagementservice.domain.Maintenance;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.domain.Building;
import realestatemanagementservice.repository.ApartmentRepository;
import realestatemanagementservice.security.AuthoritiesConstants;
import realestatemanagementservice.service.ApartmentService;
import realestatemanagementservice.service.dto.ApartmentDTO;
import realestatemanagementservice.service.mapper.ApartmentMapper;
import realestatemanagementservice.service.dto.ApartmentCriteria;
import realestatemanagementservice.service.ApartmentQueryService;

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
 * Integration tests for the {@link ApartmentResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.MANAGER)
public class ApartmentResourceIT {

    private static final String DEFAULT_UNIT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MOVE_IN_READY = false;
    private static final Boolean UPDATED_MOVE_IN_READY = true;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ApartmentMapper apartmentMapper;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private ApartmentQueryService apartmentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApartmentMockMvc;

    private Apartment apartment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apartment createEntity(EntityManager em) {
        Apartment apartment = new Apartment()
            .unitNumber(DEFAULT_UNIT_NUMBER)
            .moveInReady(DEFAULT_MOVE_IN_READY);
        return apartment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apartment createUpdatedEntity(EntityManager em) {
        Apartment apartment = new Apartment()
            .unitNumber(UPDATED_UNIT_NUMBER)
            .moveInReady(UPDATED_MOVE_IN_READY);
        return apartment;
    }

    @BeforeEach
    public void initTest() {
        apartment = createEntity(em);
    }

    @Test
    @Transactional
    public void createApartment() throws Exception {
        int databaseSizeBeforeCreate = apartmentRepository.findAll().size();

        // Create the Apartment
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);
        restApartmentMockMvc.perform(post("/api/apartments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(apartmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeCreate + 1);
        Apartment testApartment = apartmentList.get(apartmentList.size() - 1);
        assertThat(testApartment.getUnitNumber()).isEqualTo(DEFAULT_UNIT_NUMBER);
        assertThat(testApartment.isMoveInReady()).isEqualTo(DEFAULT_MOVE_IN_READY);
    }

    @Test
    @Transactional
    public void createApartmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = apartmentRepository.findAll().size();

        // Create the Apartment with an existing ID
        apartment.setId(1L);
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApartmentMockMvc.perform(post("/api/apartments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(apartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllApartments() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList
        restApartmentMockMvc.perform(get("/api/apartments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitNumber").value(hasItem(DEFAULT_UNIT_NUMBER)))
            .andExpect(jsonPath("$.[*].moveInReady").value(hasItem(DEFAULT_MOVE_IN_READY.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getApartment() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get the apartment
        restApartmentMockMvc.perform(get("/api/apartments/{id}", apartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apartment.getId().intValue()))
            .andExpect(jsonPath("$.unitNumber").value(DEFAULT_UNIT_NUMBER))
            .andExpect(jsonPath("$.moveInReady").value(DEFAULT_MOVE_IN_READY.booleanValue()));
    }


    @Test
    @Transactional
    public void getApartmentsByIdFiltering() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        Long id = apartment.getId();

        defaultApartmentShouldBeFound("id.equals=" + id);
        defaultApartmentShouldNotBeFound("id.notEquals=" + id);

        defaultApartmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultApartmentShouldNotBeFound("id.greaterThan=" + id);

        defaultApartmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultApartmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllApartmentsByUnitNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where unitNumber equals to DEFAULT_UNIT_NUMBER
        defaultApartmentShouldBeFound("unitNumber.equals=" + DEFAULT_UNIT_NUMBER);

        // Get all the apartmentList where unitNumber equals to UPDATED_UNIT_NUMBER
        defaultApartmentShouldNotBeFound("unitNumber.equals=" + UPDATED_UNIT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApartmentsByUnitNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where unitNumber not equals to DEFAULT_UNIT_NUMBER
        defaultApartmentShouldNotBeFound("unitNumber.notEquals=" + DEFAULT_UNIT_NUMBER);

        // Get all the apartmentList where unitNumber not equals to UPDATED_UNIT_NUMBER
        defaultApartmentShouldBeFound("unitNumber.notEquals=" + UPDATED_UNIT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApartmentsByUnitNumberIsInShouldWork() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where unitNumber in DEFAULT_UNIT_NUMBER or UPDATED_UNIT_NUMBER
        defaultApartmentShouldBeFound("unitNumber.in=" + DEFAULT_UNIT_NUMBER + "," + UPDATED_UNIT_NUMBER);

        // Get all the apartmentList where unitNumber equals to UPDATED_UNIT_NUMBER
        defaultApartmentShouldNotBeFound("unitNumber.in=" + UPDATED_UNIT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApartmentsByUnitNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where unitNumber is not null
        defaultApartmentShouldBeFound("unitNumber.specified=true");

        // Get all the apartmentList where unitNumber is null
        defaultApartmentShouldNotBeFound("unitNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllApartmentsByUnitNumberContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where unitNumber contains DEFAULT_UNIT_NUMBER
        defaultApartmentShouldBeFound("unitNumber.contains=" + DEFAULT_UNIT_NUMBER);

        // Get all the apartmentList where unitNumber contains UPDATED_UNIT_NUMBER
        defaultApartmentShouldNotBeFound("unitNumber.contains=" + UPDATED_UNIT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApartmentsByUnitNumberNotContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where unitNumber does not contain DEFAULT_UNIT_NUMBER
        defaultApartmentShouldNotBeFound("unitNumber.doesNotContain=" + DEFAULT_UNIT_NUMBER);

        // Get all the apartmentList where unitNumber does not contain UPDATED_UNIT_NUMBER
        defaultApartmentShouldBeFound("unitNumber.doesNotContain=" + UPDATED_UNIT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllApartmentsByMoveInReadyIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where moveInReady equals to DEFAULT_MOVE_IN_READY
        defaultApartmentShouldBeFound("moveInReady.equals=" + DEFAULT_MOVE_IN_READY);

        // Get all the apartmentList where moveInReady equals to UPDATED_MOVE_IN_READY
        defaultApartmentShouldNotBeFound("moveInReady.equals=" + UPDATED_MOVE_IN_READY);
    }

    @Test
    @Transactional
    public void getAllApartmentsByMoveInReadyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where moveInReady not equals to DEFAULT_MOVE_IN_READY
        defaultApartmentShouldNotBeFound("moveInReady.notEquals=" + DEFAULT_MOVE_IN_READY);

        // Get all the apartmentList where moveInReady not equals to UPDATED_MOVE_IN_READY
        defaultApartmentShouldBeFound("moveInReady.notEquals=" + UPDATED_MOVE_IN_READY);
    }

    @Test
    @Transactional
    public void getAllApartmentsByMoveInReadyIsInShouldWork() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where moveInReady in DEFAULT_MOVE_IN_READY or UPDATED_MOVE_IN_READY
        defaultApartmentShouldBeFound("moveInReady.in=" + DEFAULT_MOVE_IN_READY + "," + UPDATED_MOVE_IN_READY);

        // Get all the apartmentList where moveInReady equals to UPDATED_MOVE_IN_READY
        defaultApartmentShouldNotBeFound("moveInReady.in=" + UPDATED_MOVE_IN_READY);
    }

    @Test
    @Transactional
    public void getAllApartmentsByMoveInReadyIsNullOrNotNull() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where moveInReady is not null
        defaultApartmentShouldBeFound("moveInReady.specified=true");

        // Get all the apartmentList where moveInReady is null
        defaultApartmentShouldNotBeFound("moveInReady.specified=false");
    }

    @Test
    @Transactional
    public void getAllApartmentsByMaintenanceIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);
        Maintenance maintenance = MaintenanceResourceIT.createEntity(em);
        em.persist(maintenance);
        em.flush();
        apartment.addMaintenance(maintenance);
        apartmentRepository.saveAndFlush(apartment);
        Long maintenanceId = maintenance.getId();

        // Get all the apartmentList where maintenance equals to maintenanceId
        defaultApartmentShouldBeFound("maintenanceId.equals=" + maintenanceId);

        // Get all the apartmentList where maintenance equals to maintenanceId + 1
        defaultApartmentShouldNotBeFound("maintenanceId.equals=" + (maintenanceId + 1));
    }


    @Test
    @Transactional
    public void getAllApartmentsByLeaseIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);
        Lease lease = LeaseResourceIT.createEntity(em);
        em.persist(lease);
        em.flush();
        apartment.addLease(lease);
        apartmentRepository.saveAndFlush(apartment);
        Long leaseId = lease.getId();

        // Get all the apartmentList where lease equals to leaseId
        defaultApartmentShouldBeFound("leaseId.equals=" + leaseId);

        // Get all the apartmentList where lease equals to leaseId + 1
        defaultApartmentShouldNotBeFound("leaseId.equals=" + (leaseId + 1));
    }


    @Test
    @Transactional
    public void getAllApartmentsByBuildingIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);
        Building building = BuildingResourceIT.createEntity(em);
        em.persist(building);
        em.flush();
        apartment.setBuilding(building);
        apartmentRepository.saveAndFlush(apartment);
        Long buildingId = building.getId();

        // Get all the apartmentList where building equals to buildingId
        defaultApartmentShouldBeFound("buildingId.equals=" + buildingId);

        // Get all the apartmentList where building equals to buildingId + 1
        defaultApartmentShouldNotBeFound("buildingId.equals=" + (buildingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApartmentShouldBeFound(String filter) throws Exception {
        restApartmentMockMvc.perform(get("/api/apartments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitNumber").value(hasItem(DEFAULT_UNIT_NUMBER)))
            .andExpect(jsonPath("$.[*].moveInReady").value(hasItem(DEFAULT_MOVE_IN_READY.booleanValue())));

        // Check, that the count call also returns 1
        restApartmentMockMvc.perform(get("/api/apartments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApartmentShouldNotBeFound(String filter) throws Exception {
        restApartmentMockMvc.perform(get("/api/apartments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApartmentMockMvc.perform(get("/api/apartments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingApartment() throws Exception {
        // Get the apartment
        restApartmentMockMvc.perform(get("/api/apartments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApartment() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();

        // Update the apartment
        Apartment updatedApartment = apartmentRepository.findById(apartment.getId()).get();
        // Disconnect from session so that the updates on updatedApartment are not directly saved in db
        em.detach(updatedApartment);
        updatedApartment
            .unitNumber(UPDATED_UNIT_NUMBER)
            .moveInReady(UPDATED_MOVE_IN_READY);
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(updatedApartment);

        restApartmentMockMvc.perform(put("/api/apartments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(apartmentDTO)))
            .andExpect(status().isOk());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
        Apartment testApartment = apartmentList.get(apartmentList.size() - 1);
        assertThat(testApartment.getUnitNumber()).isEqualTo(UPDATED_UNIT_NUMBER);
        assertThat(testApartment.isMoveInReady()).isEqualTo(UPDATED_MOVE_IN_READY);
    }

    @Test
    @Transactional
    public void updateNonExistingApartment() throws Exception {
        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();

        // Create the Apartment
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApartmentMockMvc.perform(put("/api/apartments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(apartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApartment() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        int databaseSizeBeforeDelete = apartmentRepository.findAll().size();

        // Delete the apartment
        restApartmentMockMvc.perform(delete("/api/apartments/{id}", apartment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
