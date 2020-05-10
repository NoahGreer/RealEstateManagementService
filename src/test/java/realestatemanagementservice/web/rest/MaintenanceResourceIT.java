package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Maintenance;
import realestatemanagementservice.domain.Apartment;
import realestatemanagementservice.domain.Contractor;
import realestatemanagementservice.repository.MaintenanceRepository;
import realestatemanagementservice.service.MaintenanceService;
import realestatemanagementservice.service.dto.MaintenanceDTO;
import realestatemanagementservice.service.mapper.MaintenanceMapper;
import realestatemanagementservice.service.dto.MaintenanceCriteria;
import realestatemanagementservice.service.MaintenanceQueryService;

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
 * Integration tests for the {@link MaintenanceResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MaintenanceResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NOTIFICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NOTIFICATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_NOTIFICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_COMPLETE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_COMPLETE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_COMPLETE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CONTRACTOR_JOB_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACTOR_JOB_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_REPAIR_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_REPAIR_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_REPAIR_COST = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_REPAIR_PAID_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPAIR_PAID_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPAIR_PAID_ON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_RECEIPT_OF_PAYMENT = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_OF_PAYMENT = "BBBBBBBBBB";

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private MaintenanceMapper maintenanceMapper;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private MaintenanceQueryService maintenanceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaintenanceMockMvc;

    private Maintenance maintenance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintenance createEntity(EntityManager em) {
        Maintenance maintenance = new Maintenance()
            .description(DEFAULT_DESCRIPTION)
            .notificationDate(DEFAULT_NOTIFICATION_DATE)
            .dateComplete(DEFAULT_DATE_COMPLETE)
            .contractorJobNumber(DEFAULT_CONTRACTOR_JOB_NUMBER)
            .repairCost(DEFAULT_REPAIR_COST)
            .repairPaidOn(DEFAULT_REPAIR_PAID_ON)
            .receiptOfPayment(DEFAULT_RECEIPT_OF_PAYMENT);
        return maintenance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintenance createUpdatedEntity(EntityManager em) {
        Maintenance maintenance = new Maintenance()
            .description(UPDATED_DESCRIPTION)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .dateComplete(UPDATED_DATE_COMPLETE)
            .contractorJobNumber(UPDATED_CONTRACTOR_JOB_NUMBER)
            .repairCost(UPDATED_REPAIR_COST)
            .repairPaidOn(UPDATED_REPAIR_PAID_ON)
            .receiptOfPayment(UPDATED_RECEIPT_OF_PAYMENT);
        return maintenance;
    }

    @BeforeEach
    public void initTest() {
        maintenance = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaintenance() throws Exception {
        int databaseSizeBeforeCreate = maintenanceRepository.findAll().size();

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);
        restMaintenanceMockMvc.perform(post("/api/maintenances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO)))
            .andExpect(status().isCreated());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeCreate + 1);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMaintenance.getNotificationDate()).isEqualTo(DEFAULT_NOTIFICATION_DATE);
        assertThat(testMaintenance.getDateComplete()).isEqualTo(DEFAULT_DATE_COMPLETE);
        assertThat(testMaintenance.getContractorJobNumber()).isEqualTo(DEFAULT_CONTRACTOR_JOB_NUMBER);
        assertThat(testMaintenance.getRepairCost()).isEqualTo(DEFAULT_REPAIR_COST);
        assertThat(testMaintenance.getRepairPaidOn()).isEqualTo(DEFAULT_REPAIR_PAID_ON);
        assertThat(testMaintenance.getReceiptOfPayment()).isEqualTo(DEFAULT_RECEIPT_OF_PAYMENT);
    }

    @Test
    @Transactional
    public void createMaintenanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = maintenanceRepository.findAll().size();

        // Create the Maintenance with an existing ID
        maintenance.setId(1L);
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintenanceMockMvc.perform(post("/api/maintenances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMaintenances() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList
        restMaintenanceMockMvc.perform(get("/api/maintenances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notificationDate").value(hasItem(DEFAULT_NOTIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateComplete").value(hasItem(DEFAULT_DATE_COMPLETE.toString())))
            .andExpect(jsonPath("$.[*].contractorJobNumber").value(hasItem(DEFAULT_CONTRACTOR_JOB_NUMBER)))
            .andExpect(jsonPath("$.[*].repairCost").value(hasItem(DEFAULT_REPAIR_COST.intValue())))
            .andExpect(jsonPath("$.[*].repairPaidOn").value(hasItem(DEFAULT_REPAIR_PAID_ON.toString())))
            .andExpect(jsonPath("$.[*].receiptOfPayment").value(hasItem(DEFAULT_RECEIPT_OF_PAYMENT)));
    }
    
    @Test
    @Transactional
    public void getMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get the maintenance
        restMaintenanceMockMvc.perform(get("/api/maintenances/{id}", maintenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maintenance.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.notificationDate").value(DEFAULT_NOTIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.dateComplete").value(DEFAULT_DATE_COMPLETE.toString()))
            .andExpect(jsonPath("$.contractorJobNumber").value(DEFAULT_CONTRACTOR_JOB_NUMBER))
            .andExpect(jsonPath("$.repairCost").value(DEFAULT_REPAIR_COST.intValue()))
            .andExpect(jsonPath("$.repairPaidOn").value(DEFAULT_REPAIR_PAID_ON.toString()))
            .andExpect(jsonPath("$.receiptOfPayment").value(DEFAULT_RECEIPT_OF_PAYMENT));
    }


    @Test
    @Transactional
    public void getMaintenancesByIdFiltering() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        Long id = maintenance.getId();

        defaultMaintenanceShouldBeFound("id.equals=" + id);
        defaultMaintenanceShouldNotBeFound("id.notEquals=" + id);

        defaultMaintenanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMaintenanceShouldNotBeFound("id.greaterThan=" + id);

        defaultMaintenanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMaintenanceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMaintenancesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description equals to DEFAULT_DESCRIPTION
        defaultMaintenanceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the maintenanceList where description equals to UPDATED_DESCRIPTION
        defaultMaintenanceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description not equals to DEFAULT_DESCRIPTION
        defaultMaintenanceShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the maintenanceList where description not equals to UPDATED_DESCRIPTION
        defaultMaintenanceShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMaintenanceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the maintenanceList where description equals to UPDATED_DESCRIPTION
        defaultMaintenanceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description is not null
        defaultMaintenanceShouldBeFound("description.specified=true");

        // Get all the maintenanceList where description is null
        defaultMaintenanceShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaintenancesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description contains DEFAULT_DESCRIPTION
        defaultMaintenanceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the maintenanceList where description contains UPDATED_DESCRIPTION
        defaultMaintenanceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description does not contain DEFAULT_DESCRIPTION
        defaultMaintenanceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the maintenanceList where description does not contain UPDATED_DESCRIPTION
        defaultMaintenanceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllMaintenancesByNotificationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where notificationDate equals to DEFAULT_NOTIFICATION_DATE
        defaultMaintenanceShouldBeFound("notificationDate.equals=" + DEFAULT_NOTIFICATION_DATE);

        // Get all the maintenanceList where notificationDate equals to UPDATED_NOTIFICATION_DATE
        defaultMaintenanceShouldNotBeFound("notificationDate.equals=" + UPDATED_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByNotificationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where notificationDate not equals to DEFAULT_NOTIFICATION_DATE
        defaultMaintenanceShouldNotBeFound("notificationDate.notEquals=" + DEFAULT_NOTIFICATION_DATE);

        // Get all the maintenanceList where notificationDate not equals to UPDATED_NOTIFICATION_DATE
        defaultMaintenanceShouldBeFound("notificationDate.notEquals=" + UPDATED_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByNotificationDateIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where notificationDate in DEFAULT_NOTIFICATION_DATE or UPDATED_NOTIFICATION_DATE
        defaultMaintenanceShouldBeFound("notificationDate.in=" + DEFAULT_NOTIFICATION_DATE + "," + UPDATED_NOTIFICATION_DATE);

        // Get all the maintenanceList where notificationDate equals to UPDATED_NOTIFICATION_DATE
        defaultMaintenanceShouldNotBeFound("notificationDate.in=" + UPDATED_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByNotificationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where notificationDate is not null
        defaultMaintenanceShouldBeFound("notificationDate.specified=true");

        // Get all the maintenanceList where notificationDate is null
        defaultMaintenanceShouldNotBeFound("notificationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaintenancesByNotificationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where notificationDate is greater than or equal to DEFAULT_NOTIFICATION_DATE
        defaultMaintenanceShouldBeFound("notificationDate.greaterThanOrEqual=" + DEFAULT_NOTIFICATION_DATE);

        // Get all the maintenanceList where notificationDate is greater than or equal to UPDATED_NOTIFICATION_DATE
        defaultMaintenanceShouldNotBeFound("notificationDate.greaterThanOrEqual=" + UPDATED_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByNotificationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where notificationDate is less than or equal to DEFAULT_NOTIFICATION_DATE
        defaultMaintenanceShouldBeFound("notificationDate.lessThanOrEqual=" + DEFAULT_NOTIFICATION_DATE);

        // Get all the maintenanceList where notificationDate is less than or equal to SMALLER_NOTIFICATION_DATE
        defaultMaintenanceShouldNotBeFound("notificationDate.lessThanOrEqual=" + SMALLER_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByNotificationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where notificationDate is less than DEFAULT_NOTIFICATION_DATE
        defaultMaintenanceShouldNotBeFound("notificationDate.lessThan=" + DEFAULT_NOTIFICATION_DATE);

        // Get all the maintenanceList where notificationDate is less than UPDATED_NOTIFICATION_DATE
        defaultMaintenanceShouldBeFound("notificationDate.lessThan=" + UPDATED_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByNotificationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where notificationDate is greater than DEFAULT_NOTIFICATION_DATE
        defaultMaintenanceShouldNotBeFound("notificationDate.greaterThan=" + DEFAULT_NOTIFICATION_DATE);

        // Get all the maintenanceList where notificationDate is greater than SMALLER_NOTIFICATION_DATE
        defaultMaintenanceShouldBeFound("notificationDate.greaterThan=" + SMALLER_NOTIFICATION_DATE);
    }


    @Test
    @Transactional
    public void getAllMaintenancesByDateCompleteIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where dateComplete equals to DEFAULT_DATE_COMPLETE
        defaultMaintenanceShouldBeFound("dateComplete.equals=" + DEFAULT_DATE_COMPLETE);

        // Get all the maintenanceList where dateComplete equals to UPDATED_DATE_COMPLETE
        defaultMaintenanceShouldNotBeFound("dateComplete.equals=" + UPDATED_DATE_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDateCompleteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where dateComplete not equals to DEFAULT_DATE_COMPLETE
        defaultMaintenanceShouldNotBeFound("dateComplete.notEquals=" + DEFAULT_DATE_COMPLETE);

        // Get all the maintenanceList where dateComplete not equals to UPDATED_DATE_COMPLETE
        defaultMaintenanceShouldBeFound("dateComplete.notEquals=" + UPDATED_DATE_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDateCompleteIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where dateComplete in DEFAULT_DATE_COMPLETE or UPDATED_DATE_COMPLETE
        defaultMaintenanceShouldBeFound("dateComplete.in=" + DEFAULT_DATE_COMPLETE + "," + UPDATED_DATE_COMPLETE);

        // Get all the maintenanceList where dateComplete equals to UPDATED_DATE_COMPLETE
        defaultMaintenanceShouldNotBeFound("dateComplete.in=" + UPDATED_DATE_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDateCompleteIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where dateComplete is not null
        defaultMaintenanceShouldBeFound("dateComplete.specified=true");

        // Get all the maintenanceList where dateComplete is null
        defaultMaintenanceShouldNotBeFound("dateComplete.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDateCompleteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where dateComplete is greater than or equal to DEFAULT_DATE_COMPLETE
        defaultMaintenanceShouldBeFound("dateComplete.greaterThanOrEqual=" + DEFAULT_DATE_COMPLETE);

        // Get all the maintenanceList where dateComplete is greater than or equal to UPDATED_DATE_COMPLETE
        defaultMaintenanceShouldNotBeFound("dateComplete.greaterThanOrEqual=" + UPDATED_DATE_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDateCompleteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where dateComplete is less than or equal to DEFAULT_DATE_COMPLETE
        defaultMaintenanceShouldBeFound("dateComplete.lessThanOrEqual=" + DEFAULT_DATE_COMPLETE);

        // Get all the maintenanceList where dateComplete is less than or equal to SMALLER_DATE_COMPLETE
        defaultMaintenanceShouldNotBeFound("dateComplete.lessThanOrEqual=" + SMALLER_DATE_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDateCompleteIsLessThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where dateComplete is less than DEFAULT_DATE_COMPLETE
        defaultMaintenanceShouldNotBeFound("dateComplete.lessThan=" + DEFAULT_DATE_COMPLETE);

        // Get all the maintenanceList where dateComplete is less than UPDATED_DATE_COMPLETE
        defaultMaintenanceShouldBeFound("dateComplete.lessThan=" + UPDATED_DATE_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByDateCompleteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where dateComplete is greater than DEFAULT_DATE_COMPLETE
        defaultMaintenanceShouldNotBeFound("dateComplete.greaterThan=" + DEFAULT_DATE_COMPLETE);

        // Get all the maintenanceList where dateComplete is greater than SMALLER_DATE_COMPLETE
        defaultMaintenanceShouldBeFound("dateComplete.greaterThan=" + SMALLER_DATE_COMPLETE);
    }


    @Test
    @Transactional
    public void getAllMaintenancesByContractorJobNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where contractorJobNumber equals to DEFAULT_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldBeFound("contractorJobNumber.equals=" + DEFAULT_CONTRACTOR_JOB_NUMBER);

        // Get all the maintenanceList where contractorJobNumber equals to UPDATED_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldNotBeFound("contractorJobNumber.equals=" + UPDATED_CONTRACTOR_JOB_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByContractorJobNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where contractorJobNumber not equals to DEFAULT_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldNotBeFound("contractorJobNumber.notEquals=" + DEFAULT_CONTRACTOR_JOB_NUMBER);

        // Get all the maintenanceList where contractorJobNumber not equals to UPDATED_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldBeFound("contractorJobNumber.notEquals=" + UPDATED_CONTRACTOR_JOB_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByContractorJobNumberIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where contractorJobNumber in DEFAULT_CONTRACTOR_JOB_NUMBER or UPDATED_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldBeFound("contractorJobNumber.in=" + DEFAULT_CONTRACTOR_JOB_NUMBER + "," + UPDATED_CONTRACTOR_JOB_NUMBER);

        // Get all the maintenanceList where contractorJobNumber equals to UPDATED_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldNotBeFound("contractorJobNumber.in=" + UPDATED_CONTRACTOR_JOB_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByContractorJobNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where contractorJobNumber is not null
        defaultMaintenanceShouldBeFound("contractorJobNumber.specified=true");

        // Get all the maintenanceList where contractorJobNumber is null
        defaultMaintenanceShouldNotBeFound("contractorJobNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaintenancesByContractorJobNumberContainsSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where contractorJobNumber contains DEFAULT_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldBeFound("contractorJobNumber.contains=" + DEFAULT_CONTRACTOR_JOB_NUMBER);

        // Get all the maintenanceList where contractorJobNumber contains UPDATED_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldNotBeFound("contractorJobNumber.contains=" + UPDATED_CONTRACTOR_JOB_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByContractorJobNumberNotContainsSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where contractorJobNumber does not contain DEFAULT_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldNotBeFound("contractorJobNumber.doesNotContain=" + DEFAULT_CONTRACTOR_JOB_NUMBER);

        // Get all the maintenanceList where contractorJobNumber does not contain UPDATED_CONTRACTOR_JOB_NUMBER
        defaultMaintenanceShouldBeFound("contractorJobNumber.doesNotContain=" + UPDATED_CONTRACTOR_JOB_NUMBER);
    }


    @Test
    @Transactional
    public void getAllMaintenancesByRepairCostIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairCost equals to DEFAULT_REPAIR_COST
        defaultMaintenanceShouldBeFound("repairCost.equals=" + DEFAULT_REPAIR_COST);

        // Get all the maintenanceList where repairCost equals to UPDATED_REPAIR_COST
        defaultMaintenanceShouldNotBeFound("repairCost.equals=" + UPDATED_REPAIR_COST);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairCost not equals to DEFAULT_REPAIR_COST
        defaultMaintenanceShouldNotBeFound("repairCost.notEquals=" + DEFAULT_REPAIR_COST);

        // Get all the maintenanceList where repairCost not equals to UPDATED_REPAIR_COST
        defaultMaintenanceShouldBeFound("repairCost.notEquals=" + UPDATED_REPAIR_COST);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairCostIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairCost in DEFAULT_REPAIR_COST or UPDATED_REPAIR_COST
        defaultMaintenanceShouldBeFound("repairCost.in=" + DEFAULT_REPAIR_COST + "," + UPDATED_REPAIR_COST);

        // Get all the maintenanceList where repairCost equals to UPDATED_REPAIR_COST
        defaultMaintenanceShouldNotBeFound("repairCost.in=" + UPDATED_REPAIR_COST);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairCost is not null
        defaultMaintenanceShouldBeFound("repairCost.specified=true");

        // Get all the maintenanceList where repairCost is null
        defaultMaintenanceShouldNotBeFound("repairCost.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairCost is greater than or equal to DEFAULT_REPAIR_COST
        defaultMaintenanceShouldBeFound("repairCost.greaterThanOrEqual=" + DEFAULT_REPAIR_COST);

        // Get all the maintenanceList where repairCost is greater than or equal to UPDATED_REPAIR_COST
        defaultMaintenanceShouldNotBeFound("repairCost.greaterThanOrEqual=" + UPDATED_REPAIR_COST);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairCost is less than or equal to DEFAULT_REPAIR_COST
        defaultMaintenanceShouldBeFound("repairCost.lessThanOrEqual=" + DEFAULT_REPAIR_COST);

        // Get all the maintenanceList where repairCost is less than or equal to SMALLER_REPAIR_COST
        defaultMaintenanceShouldNotBeFound("repairCost.lessThanOrEqual=" + SMALLER_REPAIR_COST);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairCostIsLessThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairCost is less than DEFAULT_REPAIR_COST
        defaultMaintenanceShouldNotBeFound("repairCost.lessThan=" + DEFAULT_REPAIR_COST);

        // Get all the maintenanceList where repairCost is less than UPDATED_REPAIR_COST
        defaultMaintenanceShouldBeFound("repairCost.lessThan=" + UPDATED_REPAIR_COST);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairCost is greater than DEFAULT_REPAIR_COST
        defaultMaintenanceShouldNotBeFound("repairCost.greaterThan=" + DEFAULT_REPAIR_COST);

        // Get all the maintenanceList where repairCost is greater than SMALLER_REPAIR_COST
        defaultMaintenanceShouldBeFound("repairCost.greaterThan=" + SMALLER_REPAIR_COST);
    }


    @Test
    @Transactional
    public void getAllMaintenancesByRepairPaidOnIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairPaidOn equals to DEFAULT_REPAIR_PAID_ON
        defaultMaintenanceShouldBeFound("repairPaidOn.equals=" + DEFAULT_REPAIR_PAID_ON);

        // Get all the maintenanceList where repairPaidOn equals to UPDATED_REPAIR_PAID_ON
        defaultMaintenanceShouldNotBeFound("repairPaidOn.equals=" + UPDATED_REPAIR_PAID_ON);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairPaidOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairPaidOn not equals to DEFAULT_REPAIR_PAID_ON
        defaultMaintenanceShouldNotBeFound("repairPaidOn.notEquals=" + DEFAULT_REPAIR_PAID_ON);

        // Get all the maintenanceList where repairPaidOn not equals to UPDATED_REPAIR_PAID_ON
        defaultMaintenanceShouldBeFound("repairPaidOn.notEquals=" + UPDATED_REPAIR_PAID_ON);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairPaidOnIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairPaidOn in DEFAULT_REPAIR_PAID_ON or UPDATED_REPAIR_PAID_ON
        defaultMaintenanceShouldBeFound("repairPaidOn.in=" + DEFAULT_REPAIR_PAID_ON + "," + UPDATED_REPAIR_PAID_ON);

        // Get all the maintenanceList where repairPaidOn equals to UPDATED_REPAIR_PAID_ON
        defaultMaintenanceShouldNotBeFound("repairPaidOn.in=" + UPDATED_REPAIR_PAID_ON);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairPaidOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairPaidOn is not null
        defaultMaintenanceShouldBeFound("repairPaidOn.specified=true");

        // Get all the maintenanceList where repairPaidOn is null
        defaultMaintenanceShouldNotBeFound("repairPaidOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairPaidOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairPaidOn is greater than or equal to DEFAULT_REPAIR_PAID_ON
        defaultMaintenanceShouldBeFound("repairPaidOn.greaterThanOrEqual=" + DEFAULT_REPAIR_PAID_ON);

        // Get all the maintenanceList where repairPaidOn is greater than or equal to UPDATED_REPAIR_PAID_ON
        defaultMaintenanceShouldNotBeFound("repairPaidOn.greaterThanOrEqual=" + UPDATED_REPAIR_PAID_ON);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairPaidOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairPaidOn is less than or equal to DEFAULT_REPAIR_PAID_ON
        defaultMaintenanceShouldBeFound("repairPaidOn.lessThanOrEqual=" + DEFAULT_REPAIR_PAID_ON);

        // Get all the maintenanceList where repairPaidOn is less than or equal to SMALLER_REPAIR_PAID_ON
        defaultMaintenanceShouldNotBeFound("repairPaidOn.lessThanOrEqual=" + SMALLER_REPAIR_PAID_ON);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairPaidOnIsLessThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairPaidOn is less than DEFAULT_REPAIR_PAID_ON
        defaultMaintenanceShouldNotBeFound("repairPaidOn.lessThan=" + DEFAULT_REPAIR_PAID_ON);

        // Get all the maintenanceList where repairPaidOn is less than UPDATED_REPAIR_PAID_ON
        defaultMaintenanceShouldBeFound("repairPaidOn.lessThan=" + UPDATED_REPAIR_PAID_ON);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByRepairPaidOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where repairPaidOn is greater than DEFAULT_REPAIR_PAID_ON
        defaultMaintenanceShouldNotBeFound("repairPaidOn.greaterThan=" + DEFAULT_REPAIR_PAID_ON);

        // Get all the maintenanceList where repairPaidOn is greater than SMALLER_REPAIR_PAID_ON
        defaultMaintenanceShouldBeFound("repairPaidOn.greaterThan=" + SMALLER_REPAIR_PAID_ON);
    }


    @Test
    @Transactional
    public void getAllMaintenancesByReceiptOfPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where receiptOfPayment equals to DEFAULT_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldBeFound("receiptOfPayment.equals=" + DEFAULT_RECEIPT_OF_PAYMENT);

        // Get all the maintenanceList where receiptOfPayment equals to UPDATED_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldNotBeFound("receiptOfPayment.equals=" + UPDATED_RECEIPT_OF_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByReceiptOfPaymentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where receiptOfPayment not equals to DEFAULT_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldNotBeFound("receiptOfPayment.notEquals=" + DEFAULT_RECEIPT_OF_PAYMENT);

        // Get all the maintenanceList where receiptOfPayment not equals to UPDATED_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldBeFound("receiptOfPayment.notEquals=" + UPDATED_RECEIPT_OF_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByReceiptOfPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where receiptOfPayment in DEFAULT_RECEIPT_OF_PAYMENT or UPDATED_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldBeFound("receiptOfPayment.in=" + DEFAULT_RECEIPT_OF_PAYMENT + "," + UPDATED_RECEIPT_OF_PAYMENT);

        // Get all the maintenanceList where receiptOfPayment equals to UPDATED_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldNotBeFound("receiptOfPayment.in=" + UPDATED_RECEIPT_OF_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByReceiptOfPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where receiptOfPayment is not null
        defaultMaintenanceShouldBeFound("receiptOfPayment.specified=true");

        // Get all the maintenanceList where receiptOfPayment is null
        defaultMaintenanceShouldNotBeFound("receiptOfPayment.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaintenancesByReceiptOfPaymentContainsSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where receiptOfPayment contains DEFAULT_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldBeFound("receiptOfPayment.contains=" + DEFAULT_RECEIPT_OF_PAYMENT);

        // Get all the maintenanceList where receiptOfPayment contains UPDATED_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldNotBeFound("receiptOfPayment.contains=" + UPDATED_RECEIPT_OF_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllMaintenancesByReceiptOfPaymentNotContainsSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where receiptOfPayment does not contain DEFAULT_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldNotBeFound("receiptOfPayment.doesNotContain=" + DEFAULT_RECEIPT_OF_PAYMENT);

        // Get all the maintenanceList where receiptOfPayment does not contain UPDATED_RECEIPT_OF_PAYMENT
        defaultMaintenanceShouldBeFound("receiptOfPayment.doesNotContain=" + UPDATED_RECEIPT_OF_PAYMENT);
    }


    @Test
    @Transactional
    public void getAllMaintenancesByApartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);
        Apartment apartment = ApartmentResourceIT.createEntity(em);
        em.persist(apartment);
        em.flush();
        maintenance.setApartment(apartment);
        maintenanceRepository.saveAndFlush(maintenance);
        Long apartmentId = apartment.getId();

        // Get all the maintenanceList where apartment equals to apartmentId
        defaultMaintenanceShouldBeFound("apartmentId.equals=" + apartmentId);

        // Get all the maintenanceList where apartment equals to apartmentId + 1
        defaultMaintenanceShouldNotBeFound("apartmentId.equals=" + (apartmentId + 1));
    }


    @Test
    @Transactional
    public void getAllMaintenancesByContractorIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);
        Contractor contractor = ContractorResourceIT.createEntity(em);
        em.persist(contractor);
        em.flush();
        maintenance.setContractor(contractor);
        maintenanceRepository.saveAndFlush(maintenance);
        Long contractorId = contractor.getId();

        // Get all the maintenanceList where contractor equals to contractorId
        defaultMaintenanceShouldBeFound("contractorId.equals=" + contractorId);

        // Get all the maintenanceList where contractor equals to contractorId + 1
        defaultMaintenanceShouldNotBeFound("contractorId.equals=" + (contractorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaintenanceShouldBeFound(String filter) throws Exception {
        restMaintenanceMockMvc.perform(get("/api/maintenances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notificationDate").value(hasItem(DEFAULT_NOTIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateComplete").value(hasItem(DEFAULT_DATE_COMPLETE.toString())))
            .andExpect(jsonPath("$.[*].contractorJobNumber").value(hasItem(DEFAULT_CONTRACTOR_JOB_NUMBER)))
            .andExpect(jsonPath("$.[*].repairCost").value(hasItem(DEFAULT_REPAIR_COST.intValue())))
            .andExpect(jsonPath("$.[*].repairPaidOn").value(hasItem(DEFAULT_REPAIR_PAID_ON.toString())))
            .andExpect(jsonPath("$.[*].receiptOfPayment").value(hasItem(DEFAULT_RECEIPT_OF_PAYMENT)));

        // Check, that the count call also returns 1
        restMaintenanceMockMvc.perform(get("/api/maintenances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaintenanceShouldNotBeFound(String filter) throws Exception {
        restMaintenanceMockMvc.perform(get("/api/maintenances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaintenanceMockMvc.perform(get("/api/maintenances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMaintenance() throws Exception {
        // Get the maintenance
        restMaintenanceMockMvc.perform(get("/api/maintenances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Update the maintenance
        Maintenance updatedMaintenance = maintenanceRepository.findById(maintenance.getId()).get();
        // Disconnect from session so that the updates on updatedMaintenance are not directly saved in db
        em.detach(updatedMaintenance);
        updatedMaintenance
            .description(UPDATED_DESCRIPTION)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .dateComplete(UPDATED_DATE_COMPLETE)
            .contractorJobNumber(UPDATED_CONTRACTOR_JOB_NUMBER)
            .repairCost(UPDATED_REPAIR_COST)
            .repairPaidOn(UPDATED_REPAIR_PAID_ON)
            .receiptOfPayment(UPDATED_RECEIPT_OF_PAYMENT);
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(updatedMaintenance);

        restMaintenanceMockMvc.perform(put("/api/maintenances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO)))
            .andExpect(status().isOk());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMaintenance.getNotificationDate()).isEqualTo(UPDATED_NOTIFICATION_DATE);
        assertThat(testMaintenance.getDateComplete()).isEqualTo(UPDATED_DATE_COMPLETE);
        assertThat(testMaintenance.getContractorJobNumber()).isEqualTo(UPDATED_CONTRACTOR_JOB_NUMBER);
        assertThat(testMaintenance.getRepairCost()).isEqualTo(UPDATED_REPAIR_COST);
        assertThat(testMaintenance.getRepairPaidOn()).isEqualTo(UPDATED_REPAIR_PAID_ON);
        assertThat(testMaintenance.getReceiptOfPayment()).isEqualTo(UPDATED_RECEIPT_OF_PAYMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc.perform(put("/api/maintenances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeDelete = maintenanceRepository.findAll().size();

        // Delete the maintenance
        restMaintenanceMockMvc.perform(delete("/api/maintenances/{id}", maintenance.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
