package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Contractor;
import realestatemanagementservice.domain.Maintenance;
import realestatemanagementservice.domain.JobType;
import realestatemanagementservice.repository.ContractorRepository;
import realestatemanagementservice.security.AuthoritiesConstants;
import realestatemanagementservice.service.ContractorService;
import realestatemanagementservice.service.dto.ContractorDTO;
import realestatemanagementservice.service.mapper.ContractorMapper;
import realestatemanagementservice.service.dto.ContractorCriteria;
import realestatemanagementservice.service.ContractorQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContractorResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.MANAGER)
public class ContractorResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_CODE = "AA";
    private static final String UPDATED_STATE_CODE = "BB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING_OF_WORK = 0;
    private static final Integer UPDATED_RATING_OF_WORK = 1;
    private static final Integer SMALLER_RATING_OF_WORK = 0 - 1;

    private static final Integer DEFAULT_RATING_OF_RESPONSIVENESS = 0;
    private static final Integer UPDATED_RATING_OF_RESPONSIVENESS = 1;
    private static final Integer SMALLER_RATING_OF_RESPONSIVENESS = 0 - 1;

    @Autowired
    private ContractorRepository contractorRepository;

    @Mock
    private ContractorRepository contractorRepositoryMock;

    @Autowired
    private ContractorMapper contractorMapper;

    @Mock
    private ContractorService contractorServiceMock;

    @Autowired
    private ContractorService contractorService;

    @Autowired
    private ContractorQueryService contractorQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractorMockMvc;

    private Contractor contractor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contractor createEntity(EntityManager em) {
        Contractor contractor = new Contractor()
            .companyName(DEFAULT_COMPANY_NAME)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .stateCode(DEFAULT_STATE_CODE)
            .zipCode(DEFAULT_ZIP_CODE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .contactPerson(DEFAULT_CONTACT_PERSON)
            .ratingOfWork(DEFAULT_RATING_OF_WORK)
            .ratingOfResponsiveness(DEFAULT_RATING_OF_RESPONSIVENESS);
        return contractor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contractor createUpdatedEntity(EntityManager em) {
        Contractor contractor = new Contractor()
            .companyName(UPDATED_COMPANY_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .stateCode(UPDATED_STATE_CODE)
            .zipCode(UPDATED_ZIP_CODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .ratingOfWork(UPDATED_RATING_OF_WORK)
            .ratingOfResponsiveness(UPDATED_RATING_OF_RESPONSIVENESS);
        return contractor;
    }

    @BeforeEach
    public void initTest() {
        contractor = createEntity(em);
    }

    @Test
    @Transactional
    public void createContractor() throws Exception {
        int databaseSizeBeforeCreate = contractorRepository.findAll().size();

        // Create the Contractor
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);
        restContractorMockMvc.perform(post("/api/contractors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractorDTO)))
            .andExpect(status().isCreated());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeCreate + 1);
        Contractor testContractor = contractorList.get(contractorList.size() - 1);
        assertThat(testContractor.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testContractor.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testContractor.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testContractor.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
        assertThat(testContractor.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testContractor.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testContractor.getContactPerson()).isEqualTo(DEFAULT_CONTACT_PERSON);
        assertThat(testContractor.getRatingOfWork()).isEqualTo(DEFAULT_RATING_OF_WORK);
        assertThat(testContractor.getRatingOfResponsiveness()).isEqualTo(DEFAULT_RATING_OF_RESPONSIVENESS);
    }

    @Test
    @Transactional
    public void createContractorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contractorRepository.findAll().size();

        // Create the Contractor with an existing ID
        contractor.setId(1L);
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractorMockMvc.perform(post("/api/contractors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllContractors() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList
        restContractorMockMvc.perform(get("/api/contractors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractor.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].ratingOfWork").value(hasItem(DEFAULT_RATING_OF_WORK)))
            .andExpect(jsonPath("$.[*].ratingOfResponsiveness").value(hasItem(DEFAULT_RATING_OF_RESPONSIVENESS)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllContractorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contractorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractorMockMvc.perform(get("/api/contractors?eagerload=true"))
            .andExpect(status().isOk());

        verify(contractorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllContractorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contractorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractorMockMvc.perform(get("/api/contractors?eagerload=true"))
            .andExpect(status().isOk());

        verify(contractorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getContractor() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get the contractor
        restContractorMockMvc.perform(get("/api/contractors/{id}", contractor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractor.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateCode").value(DEFAULT_STATE_CODE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.ratingOfWork").value(DEFAULT_RATING_OF_WORK))
            .andExpect(jsonPath("$.ratingOfResponsiveness").value(DEFAULT_RATING_OF_RESPONSIVENESS));
    }


    @Test
    @Transactional
    public void getContractorsByIdFiltering() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        Long id = contractor.getId();

        defaultContractorShouldBeFound("id.equals=" + id);
        defaultContractorShouldNotBeFound("id.notEquals=" + id);

        defaultContractorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContractorShouldNotBeFound("id.greaterThan=" + id);

        defaultContractorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContractorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContractorsByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where companyName equals to DEFAULT_COMPANY_NAME
        defaultContractorShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the contractorList where companyName equals to UPDATED_COMPANY_NAME
        defaultContractorShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllContractorsByCompanyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where companyName not equals to DEFAULT_COMPANY_NAME
        defaultContractorShouldNotBeFound("companyName.notEquals=" + DEFAULT_COMPANY_NAME);

        // Get all the contractorList where companyName not equals to UPDATED_COMPANY_NAME
        defaultContractorShouldBeFound("companyName.notEquals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllContractorsByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultContractorShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the contractorList where companyName equals to UPDATED_COMPANY_NAME
        defaultContractorShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllContractorsByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where companyName is not null
        defaultContractorShouldBeFound("companyName.specified=true");

        // Get all the contractorList where companyName is null
        defaultContractorShouldNotBeFound("companyName.specified=false");
    }
                @Test
    @Transactional
    public void getAllContractorsByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where companyName contains DEFAULT_COMPANY_NAME
        defaultContractorShouldBeFound("companyName.contains=" + DEFAULT_COMPANY_NAME);

        // Get all the contractorList where companyName contains UPDATED_COMPANY_NAME
        defaultContractorShouldNotBeFound("companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllContractorsByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where companyName does not contain DEFAULT_COMPANY_NAME
        defaultContractorShouldNotBeFound("companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);

        // Get all the contractorList where companyName does not contain UPDATED_COMPANY_NAME
        defaultContractorShouldBeFound("companyName.doesNotContain=" + UPDATED_COMPANY_NAME);
    }


    @Test
    @Transactional
    public void getAllContractorsByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultContractorShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the contractorList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultContractorShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllContractorsByStreetAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where streetAddress not equals to DEFAULT_STREET_ADDRESS
        defaultContractorShouldNotBeFound("streetAddress.notEquals=" + DEFAULT_STREET_ADDRESS);

        // Get all the contractorList where streetAddress not equals to UPDATED_STREET_ADDRESS
        defaultContractorShouldBeFound("streetAddress.notEquals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllContractorsByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultContractorShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the contractorList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultContractorShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllContractorsByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where streetAddress is not null
        defaultContractorShouldBeFound("streetAddress.specified=true");

        // Get all the contractorList where streetAddress is null
        defaultContractorShouldNotBeFound("streetAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllContractorsByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where streetAddress contains DEFAULT_STREET_ADDRESS
        defaultContractorShouldBeFound("streetAddress.contains=" + DEFAULT_STREET_ADDRESS);

        // Get all the contractorList where streetAddress contains UPDATED_STREET_ADDRESS
        defaultContractorShouldNotBeFound("streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllContractorsByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where streetAddress does not contain DEFAULT_STREET_ADDRESS
        defaultContractorShouldNotBeFound("streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS);

        // Get all the contractorList where streetAddress does not contain UPDATED_STREET_ADDRESS
        defaultContractorShouldBeFound("streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllContractorsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where city equals to DEFAULT_CITY
        defaultContractorShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the contractorList where city equals to UPDATED_CITY
        defaultContractorShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllContractorsByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where city not equals to DEFAULT_CITY
        defaultContractorShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the contractorList where city not equals to UPDATED_CITY
        defaultContractorShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllContractorsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where city in DEFAULT_CITY or UPDATED_CITY
        defaultContractorShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the contractorList where city equals to UPDATED_CITY
        defaultContractorShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllContractorsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where city is not null
        defaultContractorShouldBeFound("city.specified=true");

        // Get all the contractorList where city is null
        defaultContractorShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllContractorsByCityContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where city contains DEFAULT_CITY
        defaultContractorShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the contractorList where city contains UPDATED_CITY
        defaultContractorShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllContractorsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where city does not contain DEFAULT_CITY
        defaultContractorShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the contractorList where city does not contain UPDATED_CITY
        defaultContractorShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllContractorsByStateCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where stateCode equals to DEFAULT_STATE_CODE
        defaultContractorShouldBeFound("stateCode.equals=" + DEFAULT_STATE_CODE);

        // Get all the contractorList where stateCode equals to UPDATED_STATE_CODE
        defaultContractorShouldNotBeFound("stateCode.equals=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllContractorsByStateCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where stateCode not equals to DEFAULT_STATE_CODE
        defaultContractorShouldNotBeFound("stateCode.notEquals=" + DEFAULT_STATE_CODE);

        // Get all the contractorList where stateCode not equals to UPDATED_STATE_CODE
        defaultContractorShouldBeFound("stateCode.notEquals=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllContractorsByStateCodeIsInShouldWork() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where stateCode in DEFAULT_STATE_CODE or UPDATED_STATE_CODE
        defaultContractorShouldBeFound("stateCode.in=" + DEFAULT_STATE_CODE + "," + UPDATED_STATE_CODE);

        // Get all the contractorList where stateCode equals to UPDATED_STATE_CODE
        defaultContractorShouldNotBeFound("stateCode.in=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllContractorsByStateCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where stateCode is not null
        defaultContractorShouldBeFound("stateCode.specified=true");

        // Get all the contractorList where stateCode is null
        defaultContractorShouldNotBeFound("stateCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllContractorsByStateCodeContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where stateCode contains DEFAULT_STATE_CODE
        defaultContractorShouldBeFound("stateCode.contains=" + DEFAULT_STATE_CODE);

        // Get all the contractorList where stateCode contains UPDATED_STATE_CODE
        defaultContractorShouldNotBeFound("stateCode.contains=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllContractorsByStateCodeNotContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where stateCode does not contain DEFAULT_STATE_CODE
        defaultContractorShouldNotBeFound("stateCode.doesNotContain=" + DEFAULT_STATE_CODE);

        // Get all the contractorList where stateCode does not contain UPDATED_STATE_CODE
        defaultContractorShouldBeFound("stateCode.doesNotContain=" + UPDATED_STATE_CODE);
    }


    @Test
    @Transactional
    public void getAllContractorsByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where zipCode equals to DEFAULT_ZIP_CODE
        defaultContractorShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the contractorList where zipCode equals to UPDATED_ZIP_CODE
        defaultContractorShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllContractorsByZipCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where zipCode not equals to DEFAULT_ZIP_CODE
        defaultContractorShouldNotBeFound("zipCode.notEquals=" + DEFAULT_ZIP_CODE);

        // Get all the contractorList where zipCode not equals to UPDATED_ZIP_CODE
        defaultContractorShouldBeFound("zipCode.notEquals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllContractorsByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultContractorShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the contractorList where zipCode equals to UPDATED_ZIP_CODE
        defaultContractorShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllContractorsByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where zipCode is not null
        defaultContractorShouldBeFound("zipCode.specified=true");

        // Get all the contractorList where zipCode is null
        defaultContractorShouldNotBeFound("zipCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllContractorsByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where zipCode contains DEFAULT_ZIP_CODE
        defaultContractorShouldBeFound("zipCode.contains=" + DEFAULT_ZIP_CODE);

        // Get all the contractorList where zipCode contains UPDATED_ZIP_CODE
        defaultContractorShouldNotBeFound("zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllContractorsByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where zipCode does not contain DEFAULT_ZIP_CODE
        defaultContractorShouldNotBeFound("zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);

        // Get all the contractorList where zipCode does not contain UPDATED_ZIP_CODE
        defaultContractorShouldBeFound("zipCode.doesNotContain=" + UPDATED_ZIP_CODE);
    }


    @Test
    @Transactional
    public void getAllContractorsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultContractorShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the contractorList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultContractorShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractorsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultContractorShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the contractorList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultContractorShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractorsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultContractorShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the contractorList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultContractorShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractorsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where phoneNumber is not null
        defaultContractorShouldBeFound("phoneNumber.specified=true");

        // Get all the contractorList where phoneNumber is null
        defaultContractorShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllContractorsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultContractorShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the contractorList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultContractorShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractorsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultContractorShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the contractorList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultContractorShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllContractorsByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where contactPerson equals to DEFAULT_CONTACT_PERSON
        defaultContractorShouldBeFound("contactPerson.equals=" + DEFAULT_CONTACT_PERSON);

        // Get all the contractorList where contactPerson equals to UPDATED_CONTACT_PERSON
        defaultContractorShouldNotBeFound("contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    public void getAllContractorsByContactPersonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where contactPerson not equals to DEFAULT_CONTACT_PERSON
        defaultContractorShouldNotBeFound("contactPerson.notEquals=" + DEFAULT_CONTACT_PERSON);

        // Get all the contractorList where contactPerson not equals to UPDATED_CONTACT_PERSON
        defaultContractorShouldBeFound("contactPerson.notEquals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    public void getAllContractorsByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where contactPerson in DEFAULT_CONTACT_PERSON or UPDATED_CONTACT_PERSON
        defaultContractorShouldBeFound("contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON);

        // Get all the contractorList where contactPerson equals to UPDATED_CONTACT_PERSON
        defaultContractorShouldNotBeFound("contactPerson.in=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    public void getAllContractorsByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where contactPerson is not null
        defaultContractorShouldBeFound("contactPerson.specified=true");

        // Get all the contractorList where contactPerson is null
        defaultContractorShouldNotBeFound("contactPerson.specified=false");
    }
                @Test
    @Transactional
    public void getAllContractorsByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where contactPerson contains DEFAULT_CONTACT_PERSON
        defaultContractorShouldBeFound("contactPerson.contains=" + DEFAULT_CONTACT_PERSON);

        // Get all the contractorList where contactPerson contains UPDATED_CONTACT_PERSON
        defaultContractorShouldNotBeFound("contactPerson.contains=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    public void getAllContractorsByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where contactPerson does not contain DEFAULT_CONTACT_PERSON
        defaultContractorShouldNotBeFound("contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON);

        // Get all the contractorList where contactPerson does not contain UPDATED_CONTACT_PERSON
        defaultContractorShouldBeFound("contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON);
    }


    @Test
    @Transactional
    public void getAllContractorsByRatingOfWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfWork equals to DEFAULT_RATING_OF_WORK
        defaultContractorShouldBeFound("ratingOfWork.equals=" + DEFAULT_RATING_OF_WORK);

        // Get all the contractorList where ratingOfWork equals to UPDATED_RATING_OF_WORK
        defaultContractorShouldNotBeFound("ratingOfWork.equals=" + UPDATED_RATING_OF_WORK);
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfWorkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfWork not equals to DEFAULT_RATING_OF_WORK
        defaultContractorShouldNotBeFound("ratingOfWork.notEquals=" + DEFAULT_RATING_OF_WORK);

        // Get all the contractorList where ratingOfWork not equals to UPDATED_RATING_OF_WORK
        defaultContractorShouldBeFound("ratingOfWork.notEquals=" + UPDATED_RATING_OF_WORK);
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfWorkIsInShouldWork() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfWork in DEFAULT_RATING_OF_WORK or UPDATED_RATING_OF_WORK
        defaultContractorShouldBeFound("ratingOfWork.in=" + DEFAULT_RATING_OF_WORK + "," + UPDATED_RATING_OF_WORK);

        // Get all the contractorList where ratingOfWork equals to UPDATED_RATING_OF_WORK
        defaultContractorShouldNotBeFound("ratingOfWork.in=" + UPDATED_RATING_OF_WORK);
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfWorkIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfWork is not null
        defaultContractorShouldBeFound("ratingOfWork.specified=true");

        // Get all the contractorList where ratingOfWork is null
        defaultContractorShouldNotBeFound("ratingOfWork.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfWorkIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfWork is greater than or equal to DEFAULT_RATING_OF_WORK
        defaultContractorShouldBeFound("ratingOfWork.greaterThanOrEqual=" + DEFAULT_RATING_OF_WORK);

        // Get all the contractorList where ratingOfWork is greater than or equal to (DEFAULT_RATING_OF_WORK + 1)
        defaultContractorShouldNotBeFound("ratingOfWork.greaterThanOrEqual=" + (DEFAULT_RATING_OF_WORK + 1));
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfWorkIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfWork is less than or equal to DEFAULT_RATING_OF_WORK
        defaultContractorShouldBeFound("ratingOfWork.lessThanOrEqual=" + DEFAULT_RATING_OF_WORK);

        // Get all the contractorList where ratingOfWork is less than or equal to SMALLER_RATING_OF_WORK
        defaultContractorShouldNotBeFound("ratingOfWork.lessThanOrEqual=" + SMALLER_RATING_OF_WORK);
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfWorkIsLessThanSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfWork is less than DEFAULT_RATING_OF_WORK
        defaultContractorShouldNotBeFound("ratingOfWork.lessThan=" + DEFAULT_RATING_OF_WORK);

        // Get all the contractorList where ratingOfWork is less than (DEFAULT_RATING_OF_WORK + 1)
        defaultContractorShouldBeFound("ratingOfWork.lessThan=" + (DEFAULT_RATING_OF_WORK + 1));
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfWorkIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfWork is greater than DEFAULT_RATING_OF_WORK
        defaultContractorShouldNotBeFound("ratingOfWork.greaterThan=" + DEFAULT_RATING_OF_WORK);

        // Get all the contractorList where ratingOfWork is greater than SMALLER_RATING_OF_WORK
        defaultContractorShouldBeFound("ratingOfWork.greaterThan=" + SMALLER_RATING_OF_WORK);
    }


    @Test
    @Transactional
    public void getAllContractorsByRatingOfResponsivenessIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfResponsiveness equals to DEFAULT_RATING_OF_RESPONSIVENESS
        defaultContractorShouldBeFound("ratingOfResponsiveness.equals=" + DEFAULT_RATING_OF_RESPONSIVENESS);

        // Get all the contractorList where ratingOfResponsiveness equals to UPDATED_RATING_OF_RESPONSIVENESS
        defaultContractorShouldNotBeFound("ratingOfResponsiveness.equals=" + UPDATED_RATING_OF_RESPONSIVENESS);
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfResponsivenessIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfResponsiveness not equals to DEFAULT_RATING_OF_RESPONSIVENESS
        defaultContractorShouldNotBeFound("ratingOfResponsiveness.notEquals=" + DEFAULT_RATING_OF_RESPONSIVENESS);

        // Get all the contractorList where ratingOfResponsiveness not equals to UPDATED_RATING_OF_RESPONSIVENESS
        defaultContractorShouldBeFound("ratingOfResponsiveness.notEquals=" + UPDATED_RATING_OF_RESPONSIVENESS);
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfResponsivenessIsInShouldWork() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfResponsiveness in DEFAULT_RATING_OF_RESPONSIVENESS or UPDATED_RATING_OF_RESPONSIVENESS
        defaultContractorShouldBeFound("ratingOfResponsiveness.in=" + DEFAULT_RATING_OF_RESPONSIVENESS + "," + UPDATED_RATING_OF_RESPONSIVENESS);

        // Get all the contractorList where ratingOfResponsiveness equals to UPDATED_RATING_OF_RESPONSIVENESS
        defaultContractorShouldNotBeFound("ratingOfResponsiveness.in=" + UPDATED_RATING_OF_RESPONSIVENESS);
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfResponsivenessIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfResponsiveness is not null
        defaultContractorShouldBeFound("ratingOfResponsiveness.specified=true");

        // Get all the contractorList where ratingOfResponsiveness is null
        defaultContractorShouldNotBeFound("ratingOfResponsiveness.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfResponsivenessIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfResponsiveness is greater than or equal to DEFAULT_RATING_OF_RESPONSIVENESS
        defaultContractorShouldBeFound("ratingOfResponsiveness.greaterThanOrEqual=" + DEFAULT_RATING_OF_RESPONSIVENESS);

        // Get all the contractorList where ratingOfResponsiveness is greater than or equal to (DEFAULT_RATING_OF_RESPONSIVENESS + 1)
        defaultContractorShouldNotBeFound("ratingOfResponsiveness.greaterThanOrEqual=" + (DEFAULT_RATING_OF_RESPONSIVENESS + 1));
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfResponsivenessIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfResponsiveness is less than or equal to DEFAULT_RATING_OF_RESPONSIVENESS
        defaultContractorShouldBeFound("ratingOfResponsiveness.lessThanOrEqual=" + DEFAULT_RATING_OF_RESPONSIVENESS);

        // Get all the contractorList where ratingOfResponsiveness is less than or equal to SMALLER_RATING_OF_RESPONSIVENESS
        defaultContractorShouldNotBeFound("ratingOfResponsiveness.lessThanOrEqual=" + SMALLER_RATING_OF_RESPONSIVENESS);
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfResponsivenessIsLessThanSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfResponsiveness is less than DEFAULT_RATING_OF_RESPONSIVENESS
        defaultContractorShouldNotBeFound("ratingOfResponsiveness.lessThan=" + DEFAULT_RATING_OF_RESPONSIVENESS);

        // Get all the contractorList where ratingOfResponsiveness is less than (DEFAULT_RATING_OF_RESPONSIVENESS + 1)
        defaultContractorShouldBeFound("ratingOfResponsiveness.lessThan=" + (DEFAULT_RATING_OF_RESPONSIVENESS + 1));
    }

    @Test
    @Transactional
    public void getAllContractorsByRatingOfResponsivenessIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList where ratingOfResponsiveness is greater than DEFAULT_RATING_OF_RESPONSIVENESS
        defaultContractorShouldNotBeFound("ratingOfResponsiveness.greaterThan=" + DEFAULT_RATING_OF_RESPONSIVENESS);

        // Get all the contractorList where ratingOfResponsiveness is greater than SMALLER_RATING_OF_RESPONSIVENESS
        defaultContractorShouldBeFound("ratingOfResponsiveness.greaterThan=" + SMALLER_RATING_OF_RESPONSIVENESS);
    }


    @Test
    @Transactional
    public void getAllContractorsByMaintenanceIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);
        Maintenance maintenance = MaintenanceResourceIT.createEntity(em);
        em.persist(maintenance);
        em.flush();
        contractor.addMaintenance(maintenance);
        contractorRepository.saveAndFlush(contractor);
        Long maintenanceId = maintenance.getId();

        // Get all the contractorList where maintenance equals to maintenanceId
        defaultContractorShouldBeFound("maintenanceId.equals=" + maintenanceId);

        // Get all the contractorList where maintenance equals to maintenanceId + 1
        defaultContractorShouldNotBeFound("maintenanceId.equals=" + (maintenanceId + 1));
    }


    @Test
    @Transactional
    public void getAllContractorsByJobTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);
        JobType jobType = JobTypeResourceIT.createEntity(em);
        em.persist(jobType);
        em.flush();
        contractor.addJobType(jobType);
        contractorRepository.saveAndFlush(contractor);
        Long jobTypeId = jobType.getId();

        // Get all the contractorList where jobType equals to jobTypeId
        defaultContractorShouldBeFound("jobTypeId.equals=" + jobTypeId);

        // Get all the contractorList where jobType equals to jobTypeId + 1
        defaultContractorShouldNotBeFound("jobTypeId.equals=" + (jobTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContractorShouldBeFound(String filter) throws Exception {
        restContractorMockMvc.perform(get("/api/contractors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractor.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].ratingOfWork").value(hasItem(DEFAULT_RATING_OF_WORK)))
            .andExpect(jsonPath("$.[*].ratingOfResponsiveness").value(hasItem(DEFAULT_RATING_OF_RESPONSIVENESS)));

        // Check, that the count call also returns 1
        restContractorMockMvc.perform(get("/api/contractors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContractorShouldNotBeFound(String filter) throws Exception {
        restContractorMockMvc.perform(get("/api/contractors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContractorMockMvc.perform(get("/api/contractors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingContractor() throws Exception {
        // Get the contractor
        restContractorMockMvc.perform(get("/api/contractors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContractor() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();

        // Update the contractor
        Contractor updatedContractor = contractorRepository.findById(contractor.getId()).get();
        // Disconnect from session so that the updates on updatedContractor are not directly saved in db
        em.detach(updatedContractor);
        updatedContractor
            .companyName(UPDATED_COMPANY_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .stateCode(UPDATED_STATE_CODE)
            .zipCode(UPDATED_ZIP_CODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .ratingOfWork(UPDATED_RATING_OF_WORK)
            .ratingOfResponsiveness(UPDATED_RATING_OF_RESPONSIVENESS);
        ContractorDTO contractorDTO = contractorMapper.toDto(updatedContractor);

        restContractorMockMvc.perform(put("/api/contractors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractorDTO)))
            .andExpect(status().isOk());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
        Contractor testContractor = contractorList.get(contractorList.size() - 1);
        assertThat(testContractor.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testContractor.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testContractor.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testContractor.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
        assertThat(testContractor.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testContractor.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testContractor.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
        assertThat(testContractor.getRatingOfWork()).isEqualTo(UPDATED_RATING_OF_WORK);
        assertThat(testContractor.getRatingOfResponsiveness()).isEqualTo(UPDATED_RATING_OF_RESPONSIVENESS);
    }

    @Test
    @Transactional
    public void updateNonExistingContractor() throws Exception {
        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();

        // Create the Contractor
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractorMockMvc.perform(put("/api/contractors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContractor() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        int databaseSizeBeforeDelete = contractorRepository.findAll().size();

        // Delete the contractor
        restContractorMockMvc.perform(delete("/api/contractors/{id}", contractor.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
