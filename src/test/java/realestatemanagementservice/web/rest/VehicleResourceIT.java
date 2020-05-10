package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Vehicle;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.repository.VehicleRepository;
import realestatemanagementservice.service.VehicleService;
import realestatemanagementservice.service.dto.VehicleDTO;
import realestatemanagementservice.service.mapper.VehicleMapper;
import realestatemanagementservice.service.dto.VehicleCriteria;
import realestatemanagementservice.service.VehicleQueryService;

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
 * Integration tests for the {@link VehicleResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class VehicleResourceIT {

    private static final String DEFAULT_MAKE = "AAAAAAAAAA";
    private static final String UPDATED_MAKE = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_MODEL_YEAR = 0;
    private static final Integer UPDATED_MODEL_YEAR = 1;
    private static final Integer SMALLER_MODEL_YEAR = 0 - 1;

    private static final String DEFAULT_LICENSE_PLATE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_PLATE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_PLATE_STATE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_PLATE_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_RESERVED_PARKING_STALL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RESERVED_PARKING_STALL_NUMBER = "BBBBBBBBBB";

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleQueryService vehicleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleMockMvc;

    private Vehicle vehicle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createEntity(EntityManager em) {
        Vehicle vehicle = new Vehicle()
            .make(DEFAULT_MAKE)
            .model(DEFAULT_MODEL)
            .modelYear(DEFAULT_MODEL_YEAR)
            .licensePlateNumber(DEFAULT_LICENSE_PLATE_NUMBER)
            .licensePlateState(DEFAULT_LICENSE_PLATE_STATE)
            .reservedParkingStallNumber(DEFAULT_RESERVED_PARKING_STALL_NUMBER);
        return vehicle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createUpdatedEntity(EntityManager em) {
        Vehicle vehicle = new Vehicle()
            .make(UPDATED_MAKE)
            .model(UPDATED_MODEL)
            .modelYear(UPDATED_MODEL_YEAR)
            .licensePlateNumber(UPDATED_LICENSE_PLATE_NUMBER)
            .licensePlateState(UPDATED_LICENSE_PLATE_STATE)
            .reservedParkingStallNumber(UPDATED_RESERVED_PARKING_STALL_NUMBER);
        return vehicle;
    }

    @BeforeEach
    public void initTest() {
        vehicle = createEntity(em);
    }

    @Test
    @Transactional
    public void createVehicle() throws Exception {
        int databaseSizeBeforeCreate = vehicleRepository.findAll().size();

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);
        restVehicleMockMvc.perform(post("/api/vehicles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleDTO)))
            .andExpect(status().isCreated());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeCreate + 1);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getMake()).isEqualTo(DEFAULT_MAKE);
        assertThat(testVehicle.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testVehicle.getModelYear()).isEqualTo(DEFAULT_MODEL_YEAR);
        assertThat(testVehicle.getLicensePlateNumber()).isEqualTo(DEFAULT_LICENSE_PLATE_NUMBER);
        assertThat(testVehicle.getLicensePlateState()).isEqualTo(DEFAULT_LICENSE_PLATE_STATE);
        assertThat(testVehicle.getReservedParkingStallNumber()).isEqualTo(DEFAULT_RESERVED_PARKING_STALL_NUMBER);
    }

    @Test
    @Transactional
    public void createVehicleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehicleRepository.findAll().size();

        // Create the Vehicle with an existing ID
        vehicle.setId(1L);
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleMockMvc.perform(post("/api/vehicles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVehicles() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList
        restVehicleMockMvc.perform(get("/api/vehicles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].make").value(hasItem(DEFAULT_MAKE)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].modelYear").value(hasItem(DEFAULT_MODEL_YEAR)))
            .andExpect(jsonPath("$.[*].licensePlateNumber").value(hasItem(DEFAULT_LICENSE_PLATE_NUMBER)))
            .andExpect(jsonPath("$.[*].licensePlateState").value(hasItem(DEFAULT_LICENSE_PLATE_STATE)))
            .andExpect(jsonPath("$.[*].reservedParkingStallNumber").value(hasItem(DEFAULT_RESERVED_PARKING_STALL_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get the vehicle
        restVehicleMockMvc.perform(get("/api/vehicles/{id}", vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicle.getId().intValue()))
            .andExpect(jsonPath("$.make").value(DEFAULT_MAKE))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.modelYear").value(DEFAULT_MODEL_YEAR))
            .andExpect(jsonPath("$.licensePlateNumber").value(DEFAULT_LICENSE_PLATE_NUMBER))
            .andExpect(jsonPath("$.licensePlateState").value(DEFAULT_LICENSE_PLATE_STATE))
            .andExpect(jsonPath("$.reservedParkingStallNumber").value(DEFAULT_RESERVED_PARKING_STALL_NUMBER));
    }


    @Test
    @Transactional
    public void getVehiclesByIdFiltering() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        Long id = vehicle.getId();

        defaultVehicleShouldBeFound("id.equals=" + id);
        defaultVehicleShouldNotBeFound("id.notEquals=" + id);

        defaultVehicleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVehicleShouldNotBeFound("id.greaterThan=" + id);

        defaultVehicleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVehicleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllVehiclesByMakeIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where make equals to DEFAULT_MAKE
        defaultVehicleShouldBeFound("make.equals=" + DEFAULT_MAKE);

        // Get all the vehicleList where make equals to UPDATED_MAKE
        defaultVehicleShouldNotBeFound("make.equals=" + UPDATED_MAKE);
    }

    @Test
    @Transactional
    public void getAllVehiclesByMakeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where make not equals to DEFAULT_MAKE
        defaultVehicleShouldNotBeFound("make.notEquals=" + DEFAULT_MAKE);

        // Get all the vehicleList where make not equals to UPDATED_MAKE
        defaultVehicleShouldBeFound("make.notEquals=" + UPDATED_MAKE);
    }

    @Test
    @Transactional
    public void getAllVehiclesByMakeIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where make in DEFAULT_MAKE or UPDATED_MAKE
        defaultVehicleShouldBeFound("make.in=" + DEFAULT_MAKE + "," + UPDATED_MAKE);

        // Get all the vehicleList where make equals to UPDATED_MAKE
        defaultVehicleShouldNotBeFound("make.in=" + UPDATED_MAKE);
    }

    @Test
    @Transactional
    public void getAllVehiclesByMakeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where make is not null
        defaultVehicleShouldBeFound("make.specified=true");

        // Get all the vehicleList where make is null
        defaultVehicleShouldNotBeFound("make.specified=false");
    }
                @Test
    @Transactional
    public void getAllVehiclesByMakeContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where make contains DEFAULT_MAKE
        defaultVehicleShouldBeFound("make.contains=" + DEFAULT_MAKE);

        // Get all the vehicleList where make contains UPDATED_MAKE
        defaultVehicleShouldNotBeFound("make.contains=" + UPDATED_MAKE);
    }

    @Test
    @Transactional
    public void getAllVehiclesByMakeNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where make does not contain DEFAULT_MAKE
        defaultVehicleShouldNotBeFound("make.doesNotContain=" + DEFAULT_MAKE);

        // Get all the vehicleList where make does not contain UPDATED_MAKE
        defaultVehicleShouldBeFound("make.doesNotContain=" + UPDATED_MAKE);
    }


    @Test
    @Transactional
    public void getAllVehiclesByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model equals to DEFAULT_MODEL
        defaultVehicleShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the vehicleList where model equals to UPDATED_MODEL
        defaultVehicleShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model not equals to DEFAULT_MODEL
        defaultVehicleShouldNotBeFound("model.notEquals=" + DEFAULT_MODEL);

        // Get all the vehicleList where model not equals to UPDATED_MODEL
        defaultVehicleShouldBeFound("model.notEquals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultVehicleShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the vehicleList where model equals to UPDATED_MODEL
        defaultVehicleShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model is not null
        defaultVehicleShouldBeFound("model.specified=true");

        // Get all the vehicleList where model is null
        defaultVehicleShouldNotBeFound("model.specified=false");
    }
                @Test
    @Transactional
    public void getAllVehiclesByModelContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model contains DEFAULT_MODEL
        defaultVehicleShouldBeFound("model.contains=" + DEFAULT_MODEL);

        // Get all the vehicleList where model contains UPDATED_MODEL
        defaultVehicleShouldNotBeFound("model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model does not contain DEFAULT_MODEL
        defaultVehicleShouldNotBeFound("model.doesNotContain=" + DEFAULT_MODEL);

        // Get all the vehicleList where model does not contain UPDATED_MODEL
        defaultVehicleShouldBeFound("model.doesNotContain=" + UPDATED_MODEL);
    }


    @Test
    @Transactional
    public void getAllVehiclesByModelYearIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear equals to DEFAULT_MODEL_YEAR
        defaultVehicleShouldBeFound("modelYear.equals=" + DEFAULT_MODEL_YEAR);

        // Get all the vehicleList where modelYear equals to UPDATED_MODEL_YEAR
        defaultVehicleShouldNotBeFound("modelYear.equals=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear not equals to DEFAULT_MODEL_YEAR
        defaultVehicleShouldNotBeFound("modelYear.notEquals=" + DEFAULT_MODEL_YEAR);

        // Get all the vehicleList where modelYear not equals to UPDATED_MODEL_YEAR
        defaultVehicleShouldBeFound("modelYear.notEquals=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelYearIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear in DEFAULT_MODEL_YEAR or UPDATED_MODEL_YEAR
        defaultVehicleShouldBeFound("modelYear.in=" + DEFAULT_MODEL_YEAR + "," + UPDATED_MODEL_YEAR);

        // Get all the vehicleList where modelYear equals to UPDATED_MODEL_YEAR
        defaultVehicleShouldNotBeFound("modelYear.in=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is not null
        defaultVehicleShouldBeFound("modelYear.specified=true");

        // Get all the vehicleList where modelYear is null
        defaultVehicleShouldNotBeFound("modelYear.specified=false");
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is greater than or equal to DEFAULT_MODEL_YEAR
        defaultVehicleShouldBeFound("modelYear.greaterThanOrEqual=" + DEFAULT_MODEL_YEAR);

        // Get all the vehicleList where modelYear is greater than or equal to UPDATED_MODEL_YEAR
        defaultVehicleShouldNotBeFound("modelYear.greaterThanOrEqual=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is less than or equal to DEFAULT_MODEL_YEAR
        defaultVehicleShouldBeFound("modelYear.lessThanOrEqual=" + DEFAULT_MODEL_YEAR);

        // Get all the vehicleList where modelYear is less than or equal to SMALLER_MODEL_YEAR
        defaultVehicleShouldNotBeFound("modelYear.lessThanOrEqual=" + SMALLER_MODEL_YEAR);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelYearIsLessThanSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is less than DEFAULT_MODEL_YEAR
        defaultVehicleShouldNotBeFound("modelYear.lessThan=" + DEFAULT_MODEL_YEAR);

        // Get all the vehicleList where modelYear is less than UPDATED_MODEL_YEAR
        defaultVehicleShouldBeFound("modelYear.lessThan=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    public void getAllVehiclesByModelYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is greater than DEFAULT_MODEL_YEAR
        defaultVehicleShouldNotBeFound("modelYear.greaterThan=" + DEFAULT_MODEL_YEAR);

        // Get all the vehicleList where modelYear is greater than SMALLER_MODEL_YEAR
        defaultVehicleShouldBeFound("modelYear.greaterThan=" + SMALLER_MODEL_YEAR);
    }


    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateNumber equals to DEFAULT_LICENSE_PLATE_NUMBER
        defaultVehicleShouldBeFound("licensePlateNumber.equals=" + DEFAULT_LICENSE_PLATE_NUMBER);

        // Get all the vehicleList where licensePlateNumber equals to UPDATED_LICENSE_PLATE_NUMBER
        defaultVehicleShouldNotBeFound("licensePlateNumber.equals=" + UPDATED_LICENSE_PLATE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateNumber not equals to DEFAULT_LICENSE_PLATE_NUMBER
        defaultVehicleShouldNotBeFound("licensePlateNumber.notEquals=" + DEFAULT_LICENSE_PLATE_NUMBER);

        // Get all the vehicleList where licensePlateNumber not equals to UPDATED_LICENSE_PLATE_NUMBER
        defaultVehicleShouldBeFound("licensePlateNumber.notEquals=" + UPDATED_LICENSE_PLATE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateNumberIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateNumber in DEFAULT_LICENSE_PLATE_NUMBER or UPDATED_LICENSE_PLATE_NUMBER
        defaultVehicleShouldBeFound("licensePlateNumber.in=" + DEFAULT_LICENSE_PLATE_NUMBER + "," + UPDATED_LICENSE_PLATE_NUMBER);

        // Get all the vehicleList where licensePlateNumber equals to UPDATED_LICENSE_PLATE_NUMBER
        defaultVehicleShouldNotBeFound("licensePlateNumber.in=" + UPDATED_LICENSE_PLATE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateNumber is not null
        defaultVehicleShouldBeFound("licensePlateNumber.specified=true");

        // Get all the vehicleList where licensePlateNumber is null
        defaultVehicleShouldNotBeFound("licensePlateNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllVehiclesByLicensePlateNumberContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateNumber contains DEFAULT_LICENSE_PLATE_NUMBER
        defaultVehicleShouldBeFound("licensePlateNumber.contains=" + DEFAULT_LICENSE_PLATE_NUMBER);

        // Get all the vehicleList where licensePlateNumber contains UPDATED_LICENSE_PLATE_NUMBER
        defaultVehicleShouldNotBeFound("licensePlateNumber.contains=" + UPDATED_LICENSE_PLATE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateNumberNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateNumber does not contain DEFAULT_LICENSE_PLATE_NUMBER
        defaultVehicleShouldNotBeFound("licensePlateNumber.doesNotContain=" + DEFAULT_LICENSE_PLATE_NUMBER);

        // Get all the vehicleList where licensePlateNumber does not contain UPDATED_LICENSE_PLATE_NUMBER
        defaultVehicleShouldBeFound("licensePlateNumber.doesNotContain=" + UPDATED_LICENSE_PLATE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateStateIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateState equals to DEFAULT_LICENSE_PLATE_STATE
        defaultVehicleShouldBeFound("licensePlateState.equals=" + DEFAULT_LICENSE_PLATE_STATE);

        // Get all the vehicleList where licensePlateState equals to UPDATED_LICENSE_PLATE_STATE
        defaultVehicleShouldNotBeFound("licensePlateState.equals=" + UPDATED_LICENSE_PLATE_STATE);
    }

    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateState not equals to DEFAULT_LICENSE_PLATE_STATE
        defaultVehicleShouldNotBeFound("licensePlateState.notEquals=" + DEFAULT_LICENSE_PLATE_STATE);

        // Get all the vehicleList where licensePlateState not equals to UPDATED_LICENSE_PLATE_STATE
        defaultVehicleShouldBeFound("licensePlateState.notEquals=" + UPDATED_LICENSE_PLATE_STATE);
    }

    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateStateIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateState in DEFAULT_LICENSE_PLATE_STATE or UPDATED_LICENSE_PLATE_STATE
        defaultVehicleShouldBeFound("licensePlateState.in=" + DEFAULT_LICENSE_PLATE_STATE + "," + UPDATED_LICENSE_PLATE_STATE);

        // Get all the vehicleList where licensePlateState equals to UPDATED_LICENSE_PLATE_STATE
        defaultVehicleShouldNotBeFound("licensePlateState.in=" + UPDATED_LICENSE_PLATE_STATE);
    }

    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateState is not null
        defaultVehicleShouldBeFound("licensePlateState.specified=true");

        // Get all the vehicleList where licensePlateState is null
        defaultVehicleShouldNotBeFound("licensePlateState.specified=false");
    }
                @Test
    @Transactional
    public void getAllVehiclesByLicensePlateStateContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateState contains DEFAULT_LICENSE_PLATE_STATE
        defaultVehicleShouldBeFound("licensePlateState.contains=" + DEFAULT_LICENSE_PLATE_STATE);

        // Get all the vehicleList where licensePlateState contains UPDATED_LICENSE_PLATE_STATE
        defaultVehicleShouldNotBeFound("licensePlateState.contains=" + UPDATED_LICENSE_PLATE_STATE);
    }

    @Test
    @Transactional
    public void getAllVehiclesByLicensePlateStateNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlateState does not contain DEFAULT_LICENSE_PLATE_STATE
        defaultVehicleShouldNotBeFound("licensePlateState.doesNotContain=" + DEFAULT_LICENSE_PLATE_STATE);

        // Get all the vehicleList where licensePlateState does not contain UPDATED_LICENSE_PLATE_STATE
        defaultVehicleShouldBeFound("licensePlateState.doesNotContain=" + UPDATED_LICENSE_PLATE_STATE);
    }


    @Test
    @Transactional
    public void getAllVehiclesByReservedParkingStallNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where reservedParkingStallNumber equals to DEFAULT_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldBeFound("reservedParkingStallNumber.equals=" + DEFAULT_RESERVED_PARKING_STALL_NUMBER);

        // Get all the vehicleList where reservedParkingStallNumber equals to UPDATED_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldNotBeFound("reservedParkingStallNumber.equals=" + UPDATED_RESERVED_PARKING_STALL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVehiclesByReservedParkingStallNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where reservedParkingStallNumber not equals to DEFAULT_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldNotBeFound("reservedParkingStallNumber.notEquals=" + DEFAULT_RESERVED_PARKING_STALL_NUMBER);

        // Get all the vehicleList where reservedParkingStallNumber not equals to UPDATED_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldBeFound("reservedParkingStallNumber.notEquals=" + UPDATED_RESERVED_PARKING_STALL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVehiclesByReservedParkingStallNumberIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where reservedParkingStallNumber in DEFAULT_RESERVED_PARKING_STALL_NUMBER or UPDATED_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldBeFound("reservedParkingStallNumber.in=" + DEFAULT_RESERVED_PARKING_STALL_NUMBER + "," + UPDATED_RESERVED_PARKING_STALL_NUMBER);

        // Get all the vehicleList where reservedParkingStallNumber equals to UPDATED_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldNotBeFound("reservedParkingStallNumber.in=" + UPDATED_RESERVED_PARKING_STALL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVehiclesByReservedParkingStallNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where reservedParkingStallNumber is not null
        defaultVehicleShouldBeFound("reservedParkingStallNumber.specified=true");

        // Get all the vehicleList where reservedParkingStallNumber is null
        defaultVehicleShouldNotBeFound("reservedParkingStallNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllVehiclesByReservedParkingStallNumberContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where reservedParkingStallNumber contains DEFAULT_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldBeFound("reservedParkingStallNumber.contains=" + DEFAULT_RESERVED_PARKING_STALL_NUMBER);

        // Get all the vehicleList where reservedParkingStallNumber contains UPDATED_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldNotBeFound("reservedParkingStallNumber.contains=" + UPDATED_RESERVED_PARKING_STALL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVehiclesByReservedParkingStallNumberNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where reservedParkingStallNumber does not contain DEFAULT_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldNotBeFound("reservedParkingStallNumber.doesNotContain=" + DEFAULT_RESERVED_PARKING_STALL_NUMBER);

        // Get all the vehicleList where reservedParkingStallNumber does not contain UPDATED_RESERVED_PARKING_STALL_NUMBER
        defaultVehicleShouldBeFound("reservedParkingStallNumber.doesNotContain=" + UPDATED_RESERVED_PARKING_STALL_NUMBER);
    }


    @Test
    @Transactional
    public void getAllVehiclesByLeaseIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);
        Lease lease = LeaseResourceIT.createEntity(em);
        em.persist(lease);
        em.flush();
        vehicle.addLease(lease);
        vehicleRepository.saveAndFlush(vehicle);
        Long leaseId = lease.getId();

        // Get all the vehicleList where lease equals to leaseId
        defaultVehicleShouldBeFound("leaseId.equals=" + leaseId);

        // Get all the vehicleList where lease equals to leaseId + 1
        defaultVehicleShouldNotBeFound("leaseId.equals=" + (leaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehicleShouldBeFound(String filter) throws Exception {
        restVehicleMockMvc.perform(get("/api/vehicles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].make").value(hasItem(DEFAULT_MAKE)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].modelYear").value(hasItem(DEFAULT_MODEL_YEAR)))
            .andExpect(jsonPath("$.[*].licensePlateNumber").value(hasItem(DEFAULT_LICENSE_PLATE_NUMBER)))
            .andExpect(jsonPath("$.[*].licensePlateState").value(hasItem(DEFAULT_LICENSE_PLATE_STATE)))
            .andExpect(jsonPath("$.[*].reservedParkingStallNumber").value(hasItem(DEFAULT_RESERVED_PARKING_STALL_NUMBER)));

        // Check, that the count call also returns 1
        restVehicleMockMvc.perform(get("/api/vehicles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehicleShouldNotBeFound(String filter) throws Exception {
        restVehicleMockMvc.perform(get("/api/vehicles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehicleMockMvc.perform(get("/api/vehicles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingVehicle() throws Exception {
        // Get the vehicle
        restVehicleMockMvc.perform(get("/api/vehicles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

        // Update the vehicle
        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId()).get();
        // Disconnect from session so that the updates on updatedVehicle are not directly saved in db
        em.detach(updatedVehicle);
        updatedVehicle
            .make(UPDATED_MAKE)
            .model(UPDATED_MODEL)
            .modelYear(UPDATED_MODEL_YEAR)
            .licensePlateNumber(UPDATED_LICENSE_PLATE_NUMBER)
            .licensePlateState(UPDATED_LICENSE_PLATE_STATE)
            .reservedParkingStallNumber(UPDATED_RESERVED_PARKING_STALL_NUMBER);
        VehicleDTO vehicleDTO = vehicleMapper.toDto(updatedVehicle);

        restVehicleMockMvc.perform(put("/api/vehicles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleDTO)))
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getMake()).isEqualTo(UPDATED_MAKE);
        assertThat(testVehicle.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testVehicle.getModelYear()).isEqualTo(UPDATED_MODEL_YEAR);
        assertThat(testVehicle.getLicensePlateNumber()).isEqualTo(UPDATED_LICENSE_PLATE_NUMBER);
        assertThat(testVehicle.getLicensePlateState()).isEqualTo(UPDATED_LICENSE_PLATE_STATE);
        assertThat(testVehicle.getReservedParkingStallNumber()).isEqualTo(UPDATED_RESERVED_PARKING_STALL_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc.perform(put("/api/vehicles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeDelete = vehicleRepository.findAll().size();

        // Delete the vehicle
        restVehicleMockMvc.perform(delete("/api/vehicles/{id}", vehicle.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
