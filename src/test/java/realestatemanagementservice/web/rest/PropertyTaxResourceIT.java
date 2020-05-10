package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.PropertyTax;
import realestatemanagementservice.domain.Building;
import realestatemanagementservice.repository.PropertyTaxRepository;
import realestatemanagementservice.service.PropertyTaxService;
import realestatemanagementservice.service.dto.PropertyTaxDTO;
import realestatemanagementservice.service.mapper.PropertyTaxMapper;
import realestatemanagementservice.service.dto.PropertyTaxCriteria;
import realestatemanagementservice.service.PropertyTaxQueryService;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PropertyTaxResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PropertyTaxResourceIT {

    private static final Integer DEFAULT_TAX_YEAR = 0;
    private static final Integer UPDATED_TAX_YEAR = 1;
    private static final Integer SMALLER_TAX_YEAR = 0 - 1;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_DATE_PAID = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PAID = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_PAID = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CONFIRMATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONFIRMATION_NUMBER = "BBBBBBBBBB";

    @Autowired
    private PropertyTaxRepository propertyTaxRepository;

    @Autowired
    private PropertyTaxMapper propertyTaxMapper;

    @Autowired
    private PropertyTaxService propertyTaxService;

    @Autowired
    private PropertyTaxQueryService propertyTaxQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPropertyTaxMockMvc;

    private PropertyTax propertyTax;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyTax createEntity(EntityManager em) {
        PropertyTax propertyTax = new PropertyTax()
            .taxYear(DEFAULT_TAX_YEAR)
            .amount(DEFAULT_AMOUNT)
            .datePaid(DEFAULT_DATE_PAID)
            .confirmationNumber(DEFAULT_CONFIRMATION_NUMBER);
        return propertyTax;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyTax createUpdatedEntity(EntityManager em) {
        PropertyTax propertyTax = new PropertyTax()
            .taxYear(UPDATED_TAX_YEAR)
            .amount(UPDATED_AMOUNT)
            .datePaid(UPDATED_DATE_PAID)
            .confirmationNumber(UPDATED_CONFIRMATION_NUMBER);
        return propertyTax;
    }

    @BeforeEach
    public void initTest() {
        propertyTax = createEntity(em);
    }

    @Test
    @Transactional
    public void createPropertyTax() throws Exception {
        int databaseSizeBeforeCreate = propertyTaxRepository.findAll().size();

        // Create the PropertyTax
        PropertyTaxDTO propertyTaxDTO = propertyTaxMapper.toDto(propertyTax);
        restPropertyTaxMockMvc.perform(post("/api/property-taxes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyTaxDTO)))
            .andExpect(status().isCreated());

        // Validate the PropertyTax in the database
        List<PropertyTax> propertyTaxList = propertyTaxRepository.findAll();
        assertThat(propertyTaxList).hasSize(databaseSizeBeforeCreate + 1);
        PropertyTax testPropertyTax = propertyTaxList.get(propertyTaxList.size() - 1);
        assertThat(testPropertyTax.getTaxYear()).isEqualTo(DEFAULT_TAX_YEAR);
        assertThat(testPropertyTax.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPropertyTax.getDatePaid()).isEqualTo(DEFAULT_DATE_PAID);
        assertThat(testPropertyTax.getConfirmationNumber()).isEqualTo(DEFAULT_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void createPropertyTaxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propertyTaxRepository.findAll().size();

        // Create the PropertyTax with an existing ID
        propertyTax.setId(1L);
        PropertyTaxDTO propertyTaxDTO = propertyTaxMapper.toDto(propertyTax);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropertyTaxMockMvc.perform(post("/api/property-taxes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyTaxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PropertyTax in the database
        List<PropertyTax> propertyTaxList = propertyTaxRepository.findAll();
        assertThat(propertyTaxList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPropertyTaxes() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList
        restPropertyTaxMockMvc.perform(get("/api/property-taxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyTax.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxYear").value(hasItem(DEFAULT_TAX_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].datePaid").value(hasItem(DEFAULT_DATE_PAID.toString())))
            .andExpect(jsonPath("$.[*].confirmationNumber").value(hasItem(DEFAULT_CONFIRMATION_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getPropertyTax() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get the propertyTax
        restPropertyTaxMockMvc.perform(get("/api/property-taxes/{id}", propertyTax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(propertyTax.getId().intValue()))
            .andExpect(jsonPath("$.taxYear").value(DEFAULT_TAX_YEAR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.datePaid").value(DEFAULT_DATE_PAID.toString()))
            .andExpect(jsonPath("$.confirmationNumber").value(DEFAULT_CONFIRMATION_NUMBER));
    }


    @Test
    @Transactional
    public void getPropertyTaxesByIdFiltering() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        Long id = propertyTax.getId();

        defaultPropertyTaxShouldBeFound("id.equals=" + id);
        defaultPropertyTaxShouldNotBeFound("id.notEquals=" + id);

        defaultPropertyTaxShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPropertyTaxShouldNotBeFound("id.greaterThan=" + id);

        defaultPropertyTaxShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPropertyTaxShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPropertyTaxesByTaxYearIsEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where taxYear equals to DEFAULT_TAX_YEAR
        defaultPropertyTaxShouldBeFound("taxYear.equals=" + DEFAULT_TAX_YEAR);

        // Get all the propertyTaxList where taxYear equals to UPDATED_TAX_YEAR
        defaultPropertyTaxShouldNotBeFound("taxYear.equals=" + UPDATED_TAX_YEAR);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByTaxYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where taxYear not equals to DEFAULT_TAX_YEAR
        defaultPropertyTaxShouldNotBeFound("taxYear.notEquals=" + DEFAULT_TAX_YEAR);

        // Get all the propertyTaxList where taxYear not equals to UPDATED_TAX_YEAR
        defaultPropertyTaxShouldBeFound("taxYear.notEquals=" + UPDATED_TAX_YEAR);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByTaxYearIsInShouldWork() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where taxYear in DEFAULT_TAX_YEAR or UPDATED_TAX_YEAR
        defaultPropertyTaxShouldBeFound("taxYear.in=" + DEFAULT_TAX_YEAR + "," + UPDATED_TAX_YEAR);

        // Get all the propertyTaxList where taxYear equals to UPDATED_TAX_YEAR
        defaultPropertyTaxShouldNotBeFound("taxYear.in=" + UPDATED_TAX_YEAR);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByTaxYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where taxYear is not null
        defaultPropertyTaxShouldBeFound("taxYear.specified=true");

        // Get all the propertyTaxList where taxYear is null
        defaultPropertyTaxShouldNotBeFound("taxYear.specified=false");
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByTaxYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where taxYear is greater than or equal to DEFAULT_TAX_YEAR
        defaultPropertyTaxShouldBeFound("taxYear.greaterThanOrEqual=" + DEFAULT_TAX_YEAR);

        // Get all the propertyTaxList where taxYear is greater than or equal to UPDATED_TAX_YEAR
        defaultPropertyTaxShouldNotBeFound("taxYear.greaterThanOrEqual=" + UPDATED_TAX_YEAR);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByTaxYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where taxYear is less than or equal to DEFAULT_TAX_YEAR
        defaultPropertyTaxShouldBeFound("taxYear.lessThanOrEqual=" + DEFAULT_TAX_YEAR);

        // Get all the propertyTaxList where taxYear is less than or equal to SMALLER_TAX_YEAR
        defaultPropertyTaxShouldNotBeFound("taxYear.lessThanOrEqual=" + SMALLER_TAX_YEAR);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByTaxYearIsLessThanSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where taxYear is less than DEFAULT_TAX_YEAR
        defaultPropertyTaxShouldNotBeFound("taxYear.lessThan=" + DEFAULT_TAX_YEAR);

        // Get all the propertyTaxList where taxYear is less than UPDATED_TAX_YEAR
        defaultPropertyTaxShouldBeFound("taxYear.lessThan=" + UPDATED_TAX_YEAR);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByTaxYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where taxYear is greater than DEFAULT_TAX_YEAR
        defaultPropertyTaxShouldNotBeFound("taxYear.greaterThan=" + DEFAULT_TAX_YEAR);

        // Get all the propertyTaxList where taxYear is greater than SMALLER_TAX_YEAR
        defaultPropertyTaxShouldBeFound("taxYear.greaterThan=" + SMALLER_TAX_YEAR);
    }


    @Test
    @Transactional
    public void getAllPropertyTaxesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where amount equals to DEFAULT_AMOUNT
        defaultPropertyTaxShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the propertyTaxList where amount equals to UPDATED_AMOUNT
        defaultPropertyTaxShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where amount not equals to DEFAULT_AMOUNT
        defaultPropertyTaxShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the propertyTaxList where amount not equals to UPDATED_AMOUNT
        defaultPropertyTaxShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPropertyTaxShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the propertyTaxList where amount equals to UPDATED_AMOUNT
        defaultPropertyTaxShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where amount is not null
        defaultPropertyTaxShouldBeFound("amount.specified=true");

        // Get all the propertyTaxList where amount is null
        defaultPropertyTaxShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultPropertyTaxShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the propertyTaxList where amount is greater than or equal to UPDATED_AMOUNT
        defaultPropertyTaxShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where amount is less than or equal to DEFAULT_AMOUNT
        defaultPropertyTaxShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the propertyTaxList where amount is less than or equal to SMALLER_AMOUNT
        defaultPropertyTaxShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where amount is less than DEFAULT_AMOUNT
        defaultPropertyTaxShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the propertyTaxList where amount is less than UPDATED_AMOUNT
        defaultPropertyTaxShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where amount is greater than DEFAULT_AMOUNT
        defaultPropertyTaxShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the propertyTaxList where amount is greater than SMALLER_AMOUNT
        defaultPropertyTaxShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllPropertyTaxesByDatePaidIsEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where datePaid equals to DEFAULT_DATE_PAID
        defaultPropertyTaxShouldBeFound("datePaid.equals=" + DEFAULT_DATE_PAID);

        // Get all the propertyTaxList where datePaid equals to UPDATED_DATE_PAID
        defaultPropertyTaxShouldNotBeFound("datePaid.equals=" + UPDATED_DATE_PAID);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByDatePaidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where datePaid not equals to DEFAULT_DATE_PAID
        defaultPropertyTaxShouldNotBeFound("datePaid.notEquals=" + DEFAULT_DATE_PAID);

        // Get all the propertyTaxList where datePaid not equals to UPDATED_DATE_PAID
        defaultPropertyTaxShouldBeFound("datePaid.notEquals=" + UPDATED_DATE_PAID);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByDatePaidIsInShouldWork() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where datePaid in DEFAULT_DATE_PAID or UPDATED_DATE_PAID
        defaultPropertyTaxShouldBeFound("datePaid.in=" + DEFAULT_DATE_PAID + "," + UPDATED_DATE_PAID);

        // Get all the propertyTaxList where datePaid equals to UPDATED_DATE_PAID
        defaultPropertyTaxShouldNotBeFound("datePaid.in=" + UPDATED_DATE_PAID);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByDatePaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where datePaid is not null
        defaultPropertyTaxShouldBeFound("datePaid.specified=true");

        // Get all the propertyTaxList where datePaid is null
        defaultPropertyTaxShouldNotBeFound("datePaid.specified=false");
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByDatePaidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where datePaid is greater than or equal to DEFAULT_DATE_PAID
        defaultPropertyTaxShouldBeFound("datePaid.greaterThanOrEqual=" + DEFAULT_DATE_PAID);

        // Get all the propertyTaxList where datePaid is greater than or equal to UPDATED_DATE_PAID
        defaultPropertyTaxShouldNotBeFound("datePaid.greaterThanOrEqual=" + UPDATED_DATE_PAID);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByDatePaidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where datePaid is less than or equal to DEFAULT_DATE_PAID
        defaultPropertyTaxShouldBeFound("datePaid.lessThanOrEqual=" + DEFAULT_DATE_PAID);

        // Get all the propertyTaxList where datePaid is less than or equal to SMALLER_DATE_PAID
        defaultPropertyTaxShouldNotBeFound("datePaid.lessThanOrEqual=" + SMALLER_DATE_PAID);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByDatePaidIsLessThanSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where datePaid is less than DEFAULT_DATE_PAID
        defaultPropertyTaxShouldNotBeFound("datePaid.lessThan=" + DEFAULT_DATE_PAID);

        // Get all the propertyTaxList where datePaid is less than UPDATED_DATE_PAID
        defaultPropertyTaxShouldBeFound("datePaid.lessThan=" + UPDATED_DATE_PAID);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByDatePaidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where datePaid is greater than DEFAULT_DATE_PAID
        defaultPropertyTaxShouldNotBeFound("datePaid.greaterThan=" + DEFAULT_DATE_PAID);

        // Get all the propertyTaxList where datePaid is greater than SMALLER_DATE_PAID
        defaultPropertyTaxShouldBeFound("datePaid.greaterThan=" + SMALLER_DATE_PAID);
    }


    @Test
    @Transactional
    public void getAllPropertyTaxesByConfirmationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where confirmationNumber equals to DEFAULT_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldBeFound("confirmationNumber.equals=" + DEFAULT_CONFIRMATION_NUMBER);

        // Get all the propertyTaxList where confirmationNumber equals to UPDATED_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldNotBeFound("confirmationNumber.equals=" + UPDATED_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByConfirmationNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where confirmationNumber not equals to DEFAULT_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldNotBeFound("confirmationNumber.notEquals=" + DEFAULT_CONFIRMATION_NUMBER);

        // Get all the propertyTaxList where confirmationNumber not equals to UPDATED_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldBeFound("confirmationNumber.notEquals=" + UPDATED_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByConfirmationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where confirmationNumber in DEFAULT_CONFIRMATION_NUMBER or UPDATED_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldBeFound("confirmationNumber.in=" + DEFAULT_CONFIRMATION_NUMBER + "," + UPDATED_CONFIRMATION_NUMBER);

        // Get all the propertyTaxList where confirmationNumber equals to UPDATED_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldNotBeFound("confirmationNumber.in=" + UPDATED_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByConfirmationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where confirmationNumber is not null
        defaultPropertyTaxShouldBeFound("confirmationNumber.specified=true");

        // Get all the propertyTaxList where confirmationNumber is null
        defaultPropertyTaxShouldNotBeFound("confirmationNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllPropertyTaxesByConfirmationNumberContainsSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where confirmationNumber contains DEFAULT_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldBeFound("confirmationNumber.contains=" + DEFAULT_CONFIRMATION_NUMBER);

        // Get all the propertyTaxList where confirmationNumber contains UPDATED_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldNotBeFound("confirmationNumber.contains=" + UPDATED_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPropertyTaxesByConfirmationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        // Get all the propertyTaxList where confirmationNumber does not contain DEFAULT_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldNotBeFound("confirmationNumber.doesNotContain=" + DEFAULT_CONFIRMATION_NUMBER);

        // Get all the propertyTaxList where confirmationNumber does not contain UPDATED_CONFIRMATION_NUMBER
        defaultPropertyTaxShouldBeFound("confirmationNumber.doesNotContain=" + UPDATED_CONFIRMATION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPropertyTaxesByBuildingIsEqualToSomething() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);
        Building building = BuildingResourceIT.createEntity(em);
        em.persist(building);
        em.flush();
        propertyTax.setBuilding(building);
        propertyTaxRepository.saveAndFlush(propertyTax);
        Long buildingId = building.getId();

        // Get all the propertyTaxList where building equals to buildingId
        defaultPropertyTaxShouldBeFound("buildingId.equals=" + buildingId);

        // Get all the propertyTaxList where building equals to buildingId + 1
        defaultPropertyTaxShouldNotBeFound("buildingId.equals=" + (buildingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPropertyTaxShouldBeFound(String filter) throws Exception {
        restPropertyTaxMockMvc.perform(get("/api/property-taxes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyTax.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxYear").value(hasItem(DEFAULT_TAX_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].datePaid").value(hasItem(DEFAULT_DATE_PAID.toString())))
            .andExpect(jsonPath("$.[*].confirmationNumber").value(hasItem(DEFAULT_CONFIRMATION_NUMBER)));

        // Check, that the count call also returns 1
        restPropertyTaxMockMvc.perform(get("/api/property-taxes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPropertyTaxShouldNotBeFound(String filter) throws Exception {
        restPropertyTaxMockMvc.perform(get("/api/property-taxes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPropertyTaxMockMvc.perform(get("/api/property-taxes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPropertyTax() throws Exception {
        // Get the propertyTax
        restPropertyTaxMockMvc.perform(get("/api/property-taxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePropertyTax() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        int databaseSizeBeforeUpdate = propertyTaxRepository.findAll().size();

        // Update the propertyTax
        PropertyTax updatedPropertyTax = propertyTaxRepository.findById(propertyTax.getId()).get();
        // Disconnect from session so that the updates on updatedPropertyTax are not directly saved in db
        em.detach(updatedPropertyTax);
        updatedPropertyTax
            .taxYear(UPDATED_TAX_YEAR)
            .amount(UPDATED_AMOUNT)
            .datePaid(UPDATED_DATE_PAID)
            .confirmationNumber(UPDATED_CONFIRMATION_NUMBER);
        PropertyTaxDTO propertyTaxDTO = propertyTaxMapper.toDto(updatedPropertyTax);

        restPropertyTaxMockMvc.perform(put("/api/property-taxes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyTaxDTO)))
            .andExpect(status().isOk());

        // Validate the PropertyTax in the database
        List<PropertyTax> propertyTaxList = propertyTaxRepository.findAll();
        assertThat(propertyTaxList).hasSize(databaseSizeBeforeUpdate);
        PropertyTax testPropertyTax = propertyTaxList.get(propertyTaxList.size() - 1);
        assertThat(testPropertyTax.getTaxYear()).isEqualTo(UPDATED_TAX_YEAR);
        assertThat(testPropertyTax.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPropertyTax.getDatePaid()).isEqualTo(UPDATED_DATE_PAID);
        assertThat(testPropertyTax.getConfirmationNumber()).isEqualTo(UPDATED_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingPropertyTax() throws Exception {
        int databaseSizeBeforeUpdate = propertyTaxRepository.findAll().size();

        // Create the PropertyTax
        PropertyTaxDTO propertyTaxDTO = propertyTaxMapper.toDto(propertyTax);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropertyTaxMockMvc.perform(put("/api/property-taxes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyTaxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PropertyTax in the database
        List<PropertyTax> propertyTaxList = propertyTaxRepository.findAll();
        assertThat(propertyTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePropertyTax() throws Exception {
        // Initialize the database
        propertyTaxRepository.saveAndFlush(propertyTax);

        int databaseSizeBeforeDelete = propertyTaxRepository.findAll().size();

        // Delete the propertyTax
        restPropertyTaxMockMvc.perform(delete("/api/property-taxes/{id}", propertyTax.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PropertyTax> propertyTaxList = propertyTaxRepository.findAll();
        assertThat(propertyTaxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
