package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Building;
import realestatemanagementservice.domain.PropertyTax;
import realestatemanagementservice.domain.Apartment;
import realestatemanagementservice.repository.BuildingRepository;
import realestatemanagementservice.service.BuildingService;
import realestatemanagementservice.service.dto.BuildingDTO;
import realestatemanagementservice.service.mapper.BuildingMapper;
import realestatemanagementservice.service.dto.BuildingCriteria;
import realestatemanagementservice.service.BuildingQueryService;

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
 * Integration tests for the {@link BuildingResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class BuildingResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PURCHASE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PROPERTY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_CODE = "AA";
    private static final String UPDATED_STATE_CODE = "BB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingQueryService buildingQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBuildingMockMvc;

    private Building building;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createEntity(EntityManager em) {
        Building building = new Building()
            .name(DEFAULT_NAME)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .propertyNumber(DEFAULT_PROPERTY_NUMBER)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .stateCode(DEFAULT_STATE_CODE)
            .zipCode(DEFAULT_ZIP_CODE);
        return building;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createUpdatedEntity(EntityManager em) {
        Building building = new Building()
            .name(UPDATED_NAME)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .propertyNumber(UPDATED_PROPERTY_NUMBER)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .stateCode(UPDATED_STATE_CODE)
            .zipCode(UPDATED_ZIP_CODE);
        return building;
    }

    @BeforeEach
    public void initTest() {
        building = createEntity(em);
    }

    @Test
    @Transactional
    public void createBuilding() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);
        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isCreated());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate + 1);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBuilding.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testBuilding.getPropertyNumber()).isEqualTo(DEFAULT_PROPERTY_NUMBER);
        assertThat(testBuilding.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testBuilding.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testBuilding.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
        assertThat(testBuilding.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
    }

    @Test
    @Transactional
    public void createBuildingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // Create the Building with an existing ID
        building.setId(1L);
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBuildings() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(building.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].propertyNumber").value(hasItem(DEFAULT_PROPERTY_NUMBER)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)));
    }
    
    @Test
    @Transactional
    public void getBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", building.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(building.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.propertyNumber").value(DEFAULT_PROPERTY_NUMBER))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateCode").value(DEFAULT_STATE_CODE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE));
    }


    @Test
    @Transactional
    public void getBuildingsByIdFiltering() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        Long id = building.getId();

        defaultBuildingShouldBeFound("id.equals=" + id);
        defaultBuildingShouldNotBeFound("id.notEquals=" + id);

        defaultBuildingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBuildingShouldNotBeFound("id.greaterThan=" + id);

        defaultBuildingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBuildingShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBuildingsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name equals to DEFAULT_NAME
        defaultBuildingShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the buildingList where name equals to UPDATED_NAME
        defaultBuildingShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name not equals to DEFAULT_NAME
        defaultBuildingShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the buildingList where name not equals to UPDATED_NAME
        defaultBuildingShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBuildingShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the buildingList where name equals to UPDATED_NAME
        defaultBuildingShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name is not null
        defaultBuildingShouldBeFound("name.specified=true");

        // Get all the buildingList where name is null
        defaultBuildingShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllBuildingsByNameContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name contains DEFAULT_NAME
        defaultBuildingShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the buildingList where name contains UPDATED_NAME
        defaultBuildingShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name does not contain DEFAULT_NAME
        defaultBuildingShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the buildingList where name does not contain UPDATED_NAME
        defaultBuildingShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllBuildingsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where purchaseDate equals to DEFAULT_PURCHASE_DATE
        defaultBuildingShouldBeFound("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE);

        // Get all the buildingList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultBuildingShouldNotBeFound("purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPurchaseDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where purchaseDate not equals to DEFAULT_PURCHASE_DATE
        defaultBuildingShouldNotBeFound("purchaseDate.notEquals=" + DEFAULT_PURCHASE_DATE);

        // Get all the buildingList where purchaseDate not equals to UPDATED_PURCHASE_DATE
        defaultBuildingShouldBeFound("purchaseDate.notEquals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where purchaseDate in DEFAULT_PURCHASE_DATE or UPDATED_PURCHASE_DATE
        defaultBuildingShouldBeFound("purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE);

        // Get all the buildingList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultBuildingShouldNotBeFound("purchaseDate.in=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where purchaseDate is not null
        defaultBuildingShouldBeFound("purchaseDate.specified=true");

        // Get all the buildingList where purchaseDate is null
        defaultBuildingShouldNotBeFound("purchaseDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBuildingsByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where purchaseDate is greater than or equal to DEFAULT_PURCHASE_DATE
        defaultBuildingShouldBeFound("purchaseDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the buildingList where purchaseDate is greater than or equal to UPDATED_PURCHASE_DATE
        defaultBuildingShouldNotBeFound("purchaseDate.greaterThanOrEqual=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPurchaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where purchaseDate is less than or equal to DEFAULT_PURCHASE_DATE
        defaultBuildingShouldBeFound("purchaseDate.lessThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the buildingList where purchaseDate is less than or equal to SMALLER_PURCHASE_DATE
        defaultBuildingShouldNotBeFound("purchaseDate.lessThanOrEqual=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where purchaseDate is less than DEFAULT_PURCHASE_DATE
        defaultBuildingShouldNotBeFound("purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the buildingList where purchaseDate is less than UPDATED_PURCHASE_DATE
        defaultBuildingShouldBeFound("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPurchaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where purchaseDate is greater than DEFAULT_PURCHASE_DATE
        defaultBuildingShouldNotBeFound("purchaseDate.greaterThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the buildingList where purchaseDate is greater than SMALLER_PURCHASE_DATE
        defaultBuildingShouldBeFound("purchaseDate.greaterThan=" + SMALLER_PURCHASE_DATE);
    }


    @Test
    @Transactional
    public void getAllBuildingsByPropertyNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where propertyNumber equals to DEFAULT_PROPERTY_NUMBER
        defaultBuildingShouldBeFound("propertyNumber.equals=" + DEFAULT_PROPERTY_NUMBER);

        // Get all the buildingList where propertyNumber equals to UPDATED_PROPERTY_NUMBER
        defaultBuildingShouldNotBeFound("propertyNumber.equals=" + UPDATED_PROPERTY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPropertyNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where propertyNumber not equals to DEFAULT_PROPERTY_NUMBER
        defaultBuildingShouldNotBeFound("propertyNumber.notEquals=" + DEFAULT_PROPERTY_NUMBER);

        // Get all the buildingList where propertyNumber not equals to UPDATED_PROPERTY_NUMBER
        defaultBuildingShouldBeFound("propertyNumber.notEquals=" + UPDATED_PROPERTY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPropertyNumberIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where propertyNumber in DEFAULT_PROPERTY_NUMBER or UPDATED_PROPERTY_NUMBER
        defaultBuildingShouldBeFound("propertyNumber.in=" + DEFAULT_PROPERTY_NUMBER + "," + UPDATED_PROPERTY_NUMBER);

        // Get all the buildingList where propertyNumber equals to UPDATED_PROPERTY_NUMBER
        defaultBuildingShouldNotBeFound("propertyNumber.in=" + UPDATED_PROPERTY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPropertyNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where propertyNumber is not null
        defaultBuildingShouldBeFound("propertyNumber.specified=true");

        // Get all the buildingList where propertyNumber is null
        defaultBuildingShouldNotBeFound("propertyNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllBuildingsByPropertyNumberContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where propertyNumber contains DEFAULT_PROPERTY_NUMBER
        defaultBuildingShouldBeFound("propertyNumber.contains=" + DEFAULT_PROPERTY_NUMBER);

        // Get all the buildingList where propertyNumber contains UPDATED_PROPERTY_NUMBER
        defaultBuildingShouldNotBeFound("propertyNumber.contains=" + UPDATED_PROPERTY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBuildingsByPropertyNumberNotContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where propertyNumber does not contain DEFAULT_PROPERTY_NUMBER
        defaultBuildingShouldNotBeFound("propertyNumber.doesNotContain=" + DEFAULT_PROPERTY_NUMBER);

        // Get all the buildingList where propertyNumber does not contain UPDATED_PROPERTY_NUMBER
        defaultBuildingShouldBeFound("propertyNumber.doesNotContain=" + UPDATED_PROPERTY_NUMBER);
    }


    @Test
    @Transactional
    public void getAllBuildingsByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultBuildingShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the buildingList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultBuildingShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllBuildingsByStreetAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where streetAddress not equals to DEFAULT_STREET_ADDRESS
        defaultBuildingShouldNotBeFound("streetAddress.notEquals=" + DEFAULT_STREET_ADDRESS);

        // Get all the buildingList where streetAddress not equals to UPDATED_STREET_ADDRESS
        defaultBuildingShouldBeFound("streetAddress.notEquals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllBuildingsByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultBuildingShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the buildingList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultBuildingShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllBuildingsByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where streetAddress is not null
        defaultBuildingShouldBeFound("streetAddress.specified=true");

        // Get all the buildingList where streetAddress is null
        defaultBuildingShouldNotBeFound("streetAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllBuildingsByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where streetAddress contains DEFAULT_STREET_ADDRESS
        defaultBuildingShouldBeFound("streetAddress.contains=" + DEFAULT_STREET_ADDRESS);

        // Get all the buildingList where streetAddress contains UPDATED_STREET_ADDRESS
        defaultBuildingShouldNotBeFound("streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllBuildingsByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where streetAddress does not contain DEFAULT_STREET_ADDRESS
        defaultBuildingShouldNotBeFound("streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS);

        // Get all the buildingList where streetAddress does not contain UPDATED_STREET_ADDRESS
        defaultBuildingShouldBeFound("streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllBuildingsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where city equals to DEFAULT_CITY
        defaultBuildingShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the buildingList where city equals to UPDATED_CITY
        defaultBuildingShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllBuildingsByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where city not equals to DEFAULT_CITY
        defaultBuildingShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the buildingList where city not equals to UPDATED_CITY
        defaultBuildingShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllBuildingsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where city in DEFAULT_CITY or UPDATED_CITY
        defaultBuildingShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the buildingList where city equals to UPDATED_CITY
        defaultBuildingShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllBuildingsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where city is not null
        defaultBuildingShouldBeFound("city.specified=true");

        // Get all the buildingList where city is null
        defaultBuildingShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllBuildingsByCityContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where city contains DEFAULT_CITY
        defaultBuildingShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the buildingList where city contains UPDATED_CITY
        defaultBuildingShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllBuildingsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where city does not contain DEFAULT_CITY
        defaultBuildingShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the buildingList where city does not contain UPDATED_CITY
        defaultBuildingShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllBuildingsByStateCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where stateCode equals to DEFAULT_STATE_CODE
        defaultBuildingShouldBeFound("stateCode.equals=" + DEFAULT_STATE_CODE);

        // Get all the buildingList where stateCode equals to UPDATED_STATE_CODE
        defaultBuildingShouldNotBeFound("stateCode.equals=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByStateCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where stateCode not equals to DEFAULT_STATE_CODE
        defaultBuildingShouldNotBeFound("stateCode.notEquals=" + DEFAULT_STATE_CODE);

        // Get all the buildingList where stateCode not equals to UPDATED_STATE_CODE
        defaultBuildingShouldBeFound("stateCode.notEquals=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByStateCodeIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where stateCode in DEFAULT_STATE_CODE or UPDATED_STATE_CODE
        defaultBuildingShouldBeFound("stateCode.in=" + DEFAULT_STATE_CODE + "," + UPDATED_STATE_CODE);

        // Get all the buildingList where stateCode equals to UPDATED_STATE_CODE
        defaultBuildingShouldNotBeFound("stateCode.in=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByStateCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where stateCode is not null
        defaultBuildingShouldBeFound("stateCode.specified=true");

        // Get all the buildingList where stateCode is null
        defaultBuildingShouldNotBeFound("stateCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllBuildingsByStateCodeContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where stateCode contains DEFAULT_STATE_CODE
        defaultBuildingShouldBeFound("stateCode.contains=" + DEFAULT_STATE_CODE);

        // Get all the buildingList where stateCode contains UPDATED_STATE_CODE
        defaultBuildingShouldNotBeFound("stateCode.contains=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByStateCodeNotContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where stateCode does not contain DEFAULT_STATE_CODE
        defaultBuildingShouldNotBeFound("stateCode.doesNotContain=" + DEFAULT_STATE_CODE);

        // Get all the buildingList where stateCode does not contain UPDATED_STATE_CODE
        defaultBuildingShouldBeFound("stateCode.doesNotContain=" + UPDATED_STATE_CODE);
    }


    @Test
    @Transactional
    public void getAllBuildingsByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where zipCode equals to DEFAULT_ZIP_CODE
        defaultBuildingShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the buildingList where zipCode equals to UPDATED_ZIP_CODE
        defaultBuildingShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByZipCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where zipCode not equals to DEFAULT_ZIP_CODE
        defaultBuildingShouldNotBeFound("zipCode.notEquals=" + DEFAULT_ZIP_CODE);

        // Get all the buildingList where zipCode not equals to UPDATED_ZIP_CODE
        defaultBuildingShouldBeFound("zipCode.notEquals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultBuildingShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the buildingList where zipCode equals to UPDATED_ZIP_CODE
        defaultBuildingShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where zipCode is not null
        defaultBuildingShouldBeFound("zipCode.specified=true");

        // Get all the buildingList where zipCode is null
        defaultBuildingShouldNotBeFound("zipCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllBuildingsByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where zipCode contains DEFAULT_ZIP_CODE
        defaultBuildingShouldBeFound("zipCode.contains=" + DEFAULT_ZIP_CODE);

        // Get all the buildingList where zipCode contains UPDATED_ZIP_CODE
        defaultBuildingShouldNotBeFound("zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where zipCode does not contain DEFAULT_ZIP_CODE
        defaultBuildingShouldNotBeFound("zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);

        // Get all the buildingList where zipCode does not contain UPDATED_ZIP_CODE
        defaultBuildingShouldBeFound("zipCode.doesNotContain=" + UPDATED_ZIP_CODE);
    }


    @Test
    @Transactional
    public void getAllBuildingsByPropertyTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);
        PropertyTax propertyTax = PropertyTaxResourceIT.createEntity(em);
        em.persist(propertyTax);
        em.flush();
        building.addPropertyTax(propertyTax);
        buildingRepository.saveAndFlush(building);
        Long propertyTaxId = propertyTax.getId();

        // Get all the buildingList where propertyTax equals to propertyTaxId
        defaultBuildingShouldBeFound("propertyTaxId.equals=" + propertyTaxId);

        // Get all the buildingList where propertyTax equals to propertyTaxId + 1
        defaultBuildingShouldNotBeFound("propertyTaxId.equals=" + (propertyTaxId + 1));
    }


    @Test
    @Transactional
    public void getAllBuildingsByApartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);
        Apartment apartment = ApartmentResourceIT.createEntity(em);
        em.persist(apartment);
        em.flush();
        building.addApartment(apartment);
        buildingRepository.saveAndFlush(building);
        Long apartmentId = apartment.getId();

        // Get all the buildingList where apartment equals to apartmentId
        defaultBuildingShouldBeFound("apartmentId.equals=" + apartmentId);

        // Get all the buildingList where apartment equals to apartmentId + 1
        defaultBuildingShouldNotBeFound("apartmentId.equals=" + (apartmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBuildingShouldBeFound(String filter) throws Exception {
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(building.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].propertyNumber").value(hasItem(DEFAULT_PROPERTY_NUMBER)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)));

        // Check, that the count call also returns 1
        restBuildingMockMvc.perform(get("/api/buildings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBuildingShouldNotBeFound(String filter) throws Exception {
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBuildingMockMvc.perform(get("/api/buildings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBuilding() throws Exception {
        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Update the building
        Building updatedBuilding = buildingRepository.findById(building.getId()).get();
        // Disconnect from session so that the updates on updatedBuilding are not directly saved in db
        em.detach(updatedBuilding);
        updatedBuilding
            .name(UPDATED_NAME)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .propertyNumber(UPDATED_PROPERTY_NUMBER)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .stateCode(UPDATED_STATE_CODE)
            .zipCode(UPDATED_ZIP_CODE);
        BuildingDTO buildingDTO = buildingMapper.toDto(updatedBuilding);

        restBuildingMockMvc.perform(put("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isOk());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBuilding.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testBuilding.getPropertyNumber()).isEqualTo(UPDATED_PROPERTY_NUMBER);
        assertThat(testBuilding.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testBuilding.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBuilding.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
        assertThat(testBuilding.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuildingMockMvc.perform(put("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        int databaseSizeBeforeDelete = buildingRepository.findAll().size();

        // Delete the building
        restBuildingMockMvc.perform(delete("/api/buildings/{id}", building.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
