package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.domain.Rent;
import realestatemanagementservice.domain.Infraction;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.domain.Person;
import realestatemanagementservice.domain.Vehicle;
import realestatemanagementservice.domain.Pet;
import realestatemanagementservice.domain.Apartment;
import realestatemanagementservice.repository.LeaseRepository;
import realestatemanagementservice.security.AuthoritiesConstants;
import realestatemanagementservice.service.LeaseService;
import realestatemanagementservice.service.dto.LeaseDTO;
import realestatemanagementservice.service.mapper.LeaseMapper;
import realestatemanagementservice.service.dto.LeaseCriteria;
import realestatemanagementservice.service.LeaseQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LeaseResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.MANAGER)
public class LeaseResourceIT {

    private static final LocalDate DEFAULT_DATE_SIGNED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SIGNED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_SIGNED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MOVE_IN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MOVE_IN_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MOVE_IN_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_LEASE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private LeaseRepository leaseRepository;

    @Mock
    private LeaseRepository leaseRepositoryMock;

    @Autowired
    private LeaseMapper leaseMapper;

    @Mock
    private LeaseService leaseServiceMock;

    @Autowired
    private LeaseService leaseService;

    @Autowired
    private LeaseQueryService leaseQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseMockMvc;

    private Lease lease;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lease createEntity(EntityManager em) {
        Lease lease = new Lease()
            .dateSigned(DEFAULT_DATE_SIGNED)
            .moveInDate(DEFAULT_MOVE_IN_DATE)
            .noticeOfRemovalOrMoveoutDate(DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE)
            .endDate(DEFAULT_END_DATE)
            .amount(DEFAULT_AMOUNT)
            .leaseType(DEFAULT_LEASE_TYPE)
            .notes(DEFAULT_NOTES);
        return lease;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lease createUpdatedEntity(EntityManager em) {
        Lease lease = new Lease()
            .dateSigned(UPDATED_DATE_SIGNED)
            .moveInDate(UPDATED_MOVE_IN_DATE)
            .noticeOfRemovalOrMoveoutDate(UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE)
            .endDate(UPDATED_END_DATE)
            .amount(UPDATED_AMOUNT)
            .leaseType(UPDATED_LEASE_TYPE)
            .notes(UPDATED_NOTES);
        return lease;
    }

    @BeforeEach
    public void initTest() {
        lease = createEntity(em);
    }

    @Test
    @Transactional
    public void createLease() throws Exception {
        int databaseSizeBeforeCreate = leaseRepository.findAll().size();

        // Create the Lease
        LeaseDTO leaseDTO = leaseMapper.toDto(lease);
        restLeaseMockMvc.perform(post("/api/leases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leaseDTO)))
            .andExpect(status().isCreated());

        // Validate the Lease in the database
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeCreate + 1);
        Lease testLease = leaseList.get(leaseList.size() - 1);
        assertThat(testLease.getDateSigned()).isEqualTo(DEFAULT_DATE_SIGNED);
        assertThat(testLease.getMoveInDate()).isEqualTo(DEFAULT_MOVE_IN_DATE);
        assertThat(testLease.getNoticeOfRemovalOrMoveoutDate()).isEqualTo(DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);
        assertThat(testLease.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLease.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testLease.getLeaseType()).isEqualTo(DEFAULT_LEASE_TYPE);
        assertThat(testLease.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createLeaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaseRepository.findAll().size();

        // Create the Lease with an existing ID
        lease.setId(1L);
        LeaseDTO leaseDTO = leaseMapper.toDto(lease);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseMockMvc.perform(post("/api/leases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lease in the database
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLeases() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList
        restLeaseMockMvc.perform(get("/api/leases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lease.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateSigned").value(hasItem(DEFAULT_DATE_SIGNED.toString())))
            .andExpect(jsonPath("$.[*].moveInDate").value(hasItem(DEFAULT_MOVE_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].noticeOfRemovalOrMoveoutDate").value(hasItem(DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].leaseType").value(hasItem(DEFAULT_LEASE_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllLeasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(leaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaseMockMvc.perform(get("/api/leases?eagerload=true"))
            .andExpect(status().isOk());

        verify(leaseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllLeasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(leaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaseMockMvc.perform(get("/api/leases?eagerload=true"))
            .andExpect(status().isOk());

        verify(leaseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getLease() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get the lease
        restLeaseMockMvc.perform(get("/api/leases/{id}", lease.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lease.getId().intValue()))
            .andExpect(jsonPath("$.dateSigned").value(DEFAULT_DATE_SIGNED.toString()))
            .andExpect(jsonPath("$.moveInDate").value(DEFAULT_MOVE_IN_DATE.toString()))
            .andExpect(jsonPath("$.noticeOfRemovalOrMoveoutDate").value(DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.leaseType").value(DEFAULT_LEASE_TYPE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }


    @Test
    @Transactional
    public void getLeasesByIdFiltering() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        Long id = lease.getId();

        defaultLeaseShouldBeFound("id.equals=" + id);
        defaultLeaseShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLeasesByDateSignedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where dateSigned equals to DEFAULT_DATE_SIGNED
        defaultLeaseShouldBeFound("dateSigned.equals=" + DEFAULT_DATE_SIGNED);

        // Get all the leaseList where dateSigned equals to UPDATED_DATE_SIGNED
        defaultLeaseShouldNotBeFound("dateSigned.equals=" + UPDATED_DATE_SIGNED);
    }

    @Test
    @Transactional
    public void getAllLeasesByDateSignedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where dateSigned not equals to DEFAULT_DATE_SIGNED
        defaultLeaseShouldNotBeFound("dateSigned.notEquals=" + DEFAULT_DATE_SIGNED);

        // Get all the leaseList where dateSigned not equals to UPDATED_DATE_SIGNED
        defaultLeaseShouldBeFound("dateSigned.notEquals=" + UPDATED_DATE_SIGNED);
    }

    @Test
    @Transactional
    public void getAllLeasesByDateSignedIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where dateSigned in DEFAULT_DATE_SIGNED or UPDATED_DATE_SIGNED
        defaultLeaseShouldBeFound("dateSigned.in=" + DEFAULT_DATE_SIGNED + "," + UPDATED_DATE_SIGNED);

        // Get all the leaseList where dateSigned equals to UPDATED_DATE_SIGNED
        defaultLeaseShouldNotBeFound("dateSigned.in=" + UPDATED_DATE_SIGNED);
    }

    @Test
    @Transactional
    public void getAllLeasesByDateSignedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where dateSigned is not null
        defaultLeaseShouldBeFound("dateSigned.specified=true");

        // Get all the leaseList where dateSigned is null
        defaultLeaseShouldNotBeFound("dateSigned.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesByDateSignedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where dateSigned is greater than or equal to DEFAULT_DATE_SIGNED
        defaultLeaseShouldBeFound("dateSigned.greaterThanOrEqual=" + DEFAULT_DATE_SIGNED);

        // Get all the leaseList where dateSigned is greater than or equal to UPDATED_DATE_SIGNED
        defaultLeaseShouldNotBeFound("dateSigned.greaterThanOrEqual=" + UPDATED_DATE_SIGNED);
    }

    @Test
    @Transactional
    public void getAllLeasesByDateSignedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where dateSigned is less than or equal to DEFAULT_DATE_SIGNED
        defaultLeaseShouldBeFound("dateSigned.lessThanOrEqual=" + DEFAULT_DATE_SIGNED);

        // Get all the leaseList where dateSigned is less than or equal to SMALLER_DATE_SIGNED
        defaultLeaseShouldNotBeFound("dateSigned.lessThanOrEqual=" + SMALLER_DATE_SIGNED);
    }

    @Test
    @Transactional
    public void getAllLeasesByDateSignedIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where dateSigned is less than DEFAULT_DATE_SIGNED
        defaultLeaseShouldNotBeFound("dateSigned.lessThan=" + DEFAULT_DATE_SIGNED);

        // Get all the leaseList where dateSigned is less than UPDATED_DATE_SIGNED
        defaultLeaseShouldBeFound("dateSigned.lessThan=" + UPDATED_DATE_SIGNED);
    }

    @Test
    @Transactional
    public void getAllLeasesByDateSignedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where dateSigned is greater than DEFAULT_DATE_SIGNED
        defaultLeaseShouldNotBeFound("dateSigned.greaterThan=" + DEFAULT_DATE_SIGNED);

        // Get all the leaseList where dateSigned is greater than SMALLER_DATE_SIGNED
        defaultLeaseShouldBeFound("dateSigned.greaterThan=" + SMALLER_DATE_SIGNED);
    }


    @Test
    @Transactional
    public void getAllLeasesByMoveInDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where moveInDate equals to DEFAULT_MOVE_IN_DATE
        defaultLeaseShouldBeFound("moveInDate.equals=" + DEFAULT_MOVE_IN_DATE);

        // Get all the leaseList where moveInDate equals to UPDATED_MOVE_IN_DATE
        defaultLeaseShouldNotBeFound("moveInDate.equals=" + UPDATED_MOVE_IN_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByMoveInDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where moveInDate not equals to DEFAULT_MOVE_IN_DATE
        defaultLeaseShouldNotBeFound("moveInDate.notEquals=" + DEFAULT_MOVE_IN_DATE);

        // Get all the leaseList where moveInDate not equals to UPDATED_MOVE_IN_DATE
        defaultLeaseShouldBeFound("moveInDate.notEquals=" + UPDATED_MOVE_IN_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByMoveInDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where moveInDate in DEFAULT_MOVE_IN_DATE or UPDATED_MOVE_IN_DATE
        defaultLeaseShouldBeFound("moveInDate.in=" + DEFAULT_MOVE_IN_DATE + "," + UPDATED_MOVE_IN_DATE);

        // Get all the leaseList where moveInDate equals to UPDATED_MOVE_IN_DATE
        defaultLeaseShouldNotBeFound("moveInDate.in=" + UPDATED_MOVE_IN_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByMoveInDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where moveInDate is not null
        defaultLeaseShouldBeFound("moveInDate.specified=true");

        // Get all the leaseList where moveInDate is null
        defaultLeaseShouldNotBeFound("moveInDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesByMoveInDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where moveInDate is greater than or equal to DEFAULT_MOVE_IN_DATE
        defaultLeaseShouldBeFound("moveInDate.greaterThanOrEqual=" + DEFAULT_MOVE_IN_DATE);

        // Get all the leaseList where moveInDate is greater than or equal to UPDATED_MOVE_IN_DATE
        defaultLeaseShouldNotBeFound("moveInDate.greaterThanOrEqual=" + UPDATED_MOVE_IN_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByMoveInDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where moveInDate is less than or equal to DEFAULT_MOVE_IN_DATE
        defaultLeaseShouldBeFound("moveInDate.lessThanOrEqual=" + DEFAULT_MOVE_IN_DATE);

        // Get all the leaseList where moveInDate is less than or equal to SMALLER_MOVE_IN_DATE
        defaultLeaseShouldNotBeFound("moveInDate.lessThanOrEqual=" + SMALLER_MOVE_IN_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByMoveInDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where moveInDate is less than DEFAULT_MOVE_IN_DATE
        defaultLeaseShouldNotBeFound("moveInDate.lessThan=" + DEFAULT_MOVE_IN_DATE);

        // Get all the leaseList where moveInDate is less than UPDATED_MOVE_IN_DATE
        defaultLeaseShouldBeFound("moveInDate.lessThan=" + UPDATED_MOVE_IN_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByMoveInDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where moveInDate is greater than DEFAULT_MOVE_IN_DATE
        defaultLeaseShouldNotBeFound("moveInDate.greaterThan=" + DEFAULT_MOVE_IN_DATE);

        // Get all the leaseList where moveInDate is greater than SMALLER_MOVE_IN_DATE
        defaultLeaseShouldBeFound("moveInDate.greaterThan=" + SMALLER_MOVE_IN_DATE);
    }


    @Test
    @Transactional
    public void getAllLeasesByNoticeOfRemovalOrMoveoutDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate equals to DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldBeFound("noticeOfRemovalOrMoveoutDate.equals=" + DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate equals to UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldNotBeFound("noticeOfRemovalOrMoveoutDate.equals=" + UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByNoticeOfRemovalOrMoveoutDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate not equals to DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldNotBeFound("noticeOfRemovalOrMoveoutDate.notEquals=" + DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate not equals to UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldBeFound("noticeOfRemovalOrMoveoutDate.notEquals=" + UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByNoticeOfRemovalOrMoveoutDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate in DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE or UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldBeFound("noticeOfRemovalOrMoveoutDate.in=" + DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE + "," + UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate equals to UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldNotBeFound("noticeOfRemovalOrMoveoutDate.in=" + UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByNoticeOfRemovalOrMoveoutDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is not null
        defaultLeaseShouldBeFound("noticeOfRemovalOrMoveoutDate.specified=true");

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is null
        defaultLeaseShouldNotBeFound("noticeOfRemovalOrMoveoutDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesByNoticeOfRemovalOrMoveoutDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is greater than or equal to DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldBeFound("noticeOfRemovalOrMoveoutDate.greaterThanOrEqual=" + DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is greater than or equal to UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldNotBeFound("noticeOfRemovalOrMoveoutDate.greaterThanOrEqual=" + UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByNoticeOfRemovalOrMoveoutDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is less than or equal to DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldBeFound("noticeOfRemovalOrMoveoutDate.lessThanOrEqual=" + DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is less than or equal to SMALLER_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldNotBeFound("noticeOfRemovalOrMoveoutDate.lessThanOrEqual=" + SMALLER_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByNoticeOfRemovalOrMoveoutDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is less than DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldNotBeFound("noticeOfRemovalOrMoveoutDate.lessThan=" + DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is less than UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldBeFound("noticeOfRemovalOrMoveoutDate.lessThan=" + UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByNoticeOfRemovalOrMoveoutDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is greater than DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldNotBeFound("noticeOfRemovalOrMoveoutDate.greaterThan=" + DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);

        // Get all the leaseList where noticeOfRemovalOrMoveoutDate is greater than SMALLER_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE
        defaultLeaseShouldBeFound("noticeOfRemovalOrMoveoutDate.greaterThan=" + SMALLER_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);
    }


    @Test
    @Transactional
    public void getAllLeasesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where endDate equals to DEFAULT_END_DATE
        defaultLeaseShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the leaseList where endDate equals to UPDATED_END_DATE
        defaultLeaseShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where endDate not equals to DEFAULT_END_DATE
        defaultLeaseShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the leaseList where endDate not equals to UPDATED_END_DATE
        defaultLeaseShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultLeaseShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the leaseList where endDate equals to UPDATED_END_DATE
        defaultLeaseShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where endDate is not null
        defaultLeaseShouldBeFound("endDate.specified=true");

        // Get all the leaseList where endDate is null
        defaultLeaseShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultLeaseShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leaseList where endDate is greater than or equal to UPDATED_END_DATE
        defaultLeaseShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where endDate is less than or equal to DEFAULT_END_DATE
        defaultLeaseShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leaseList where endDate is less than or equal to SMALLER_END_DATE
        defaultLeaseShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where endDate is less than DEFAULT_END_DATE
        defaultLeaseShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the leaseList where endDate is less than UPDATED_END_DATE
        defaultLeaseShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeasesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where endDate is greater than DEFAULT_END_DATE
        defaultLeaseShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the leaseList where endDate is greater than SMALLER_END_DATE
        defaultLeaseShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllLeasesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where amount equals to DEFAULT_AMOUNT
        defaultLeaseShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the leaseList where amount equals to UPDATED_AMOUNT
        defaultLeaseShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllLeasesByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where amount not equals to DEFAULT_AMOUNT
        defaultLeaseShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the leaseList where amount not equals to UPDATED_AMOUNT
        defaultLeaseShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllLeasesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultLeaseShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the leaseList where amount equals to UPDATED_AMOUNT
        defaultLeaseShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllLeasesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where amount is not null
        defaultLeaseShouldBeFound("amount.specified=true");

        // Get all the leaseList where amount is null
        defaultLeaseShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultLeaseShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the leaseList where amount is greater than or equal to UPDATED_AMOUNT
        defaultLeaseShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllLeasesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where amount is less than or equal to DEFAULT_AMOUNT
        defaultLeaseShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the leaseList where amount is less than or equal to SMALLER_AMOUNT
        defaultLeaseShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllLeasesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where amount is less than DEFAULT_AMOUNT
        defaultLeaseShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the leaseList where amount is less than UPDATED_AMOUNT
        defaultLeaseShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllLeasesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where amount is greater than DEFAULT_AMOUNT
        defaultLeaseShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the leaseList where amount is greater than SMALLER_AMOUNT
        defaultLeaseShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllLeasesByLeaseTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where leaseType equals to DEFAULT_LEASE_TYPE
        defaultLeaseShouldBeFound("leaseType.equals=" + DEFAULT_LEASE_TYPE);

        // Get all the leaseList where leaseType equals to UPDATED_LEASE_TYPE
        defaultLeaseShouldNotBeFound("leaseType.equals=" + UPDATED_LEASE_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesByLeaseTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where leaseType not equals to DEFAULT_LEASE_TYPE
        defaultLeaseShouldNotBeFound("leaseType.notEquals=" + DEFAULT_LEASE_TYPE);

        // Get all the leaseList where leaseType not equals to UPDATED_LEASE_TYPE
        defaultLeaseShouldBeFound("leaseType.notEquals=" + UPDATED_LEASE_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesByLeaseTypeIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where leaseType in DEFAULT_LEASE_TYPE or UPDATED_LEASE_TYPE
        defaultLeaseShouldBeFound("leaseType.in=" + DEFAULT_LEASE_TYPE + "," + UPDATED_LEASE_TYPE);

        // Get all the leaseList where leaseType equals to UPDATED_LEASE_TYPE
        defaultLeaseShouldNotBeFound("leaseType.in=" + UPDATED_LEASE_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesByLeaseTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where leaseType is not null
        defaultLeaseShouldBeFound("leaseType.specified=true");

        // Get all the leaseList where leaseType is null
        defaultLeaseShouldNotBeFound("leaseType.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeasesByLeaseTypeContainsSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where leaseType contains DEFAULT_LEASE_TYPE
        defaultLeaseShouldBeFound("leaseType.contains=" + DEFAULT_LEASE_TYPE);

        // Get all the leaseList where leaseType contains UPDATED_LEASE_TYPE
        defaultLeaseShouldNotBeFound("leaseType.contains=" + UPDATED_LEASE_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesByLeaseTypeNotContainsSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where leaseType does not contain DEFAULT_LEASE_TYPE
        defaultLeaseShouldNotBeFound("leaseType.doesNotContain=" + DEFAULT_LEASE_TYPE);

        // Get all the leaseList where leaseType does not contain UPDATED_LEASE_TYPE
        defaultLeaseShouldBeFound("leaseType.doesNotContain=" + UPDATED_LEASE_TYPE);
    }


    @Test
    @Transactional
    public void getAllLeasesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where notes equals to DEFAULT_NOTES
        defaultLeaseShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the leaseList where notes equals to UPDATED_NOTES
        defaultLeaseShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void getAllLeasesByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where notes not equals to DEFAULT_NOTES
        defaultLeaseShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the leaseList where notes not equals to UPDATED_NOTES
        defaultLeaseShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void getAllLeasesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultLeaseShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the leaseList where notes equals to UPDATED_NOTES
        defaultLeaseShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void getAllLeasesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where notes is not null
        defaultLeaseShouldBeFound("notes.specified=true");

        // Get all the leaseList where notes is null
        defaultLeaseShouldNotBeFound("notes.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeasesByNotesContainsSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where notes contains DEFAULT_NOTES
        defaultLeaseShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the leaseList where notes contains UPDATED_NOTES
        defaultLeaseShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void getAllLeasesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList where notes does not contain DEFAULT_NOTES
        defaultLeaseShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the leaseList where notes does not contain UPDATED_NOTES
        defaultLeaseShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }


    @Test
    @Transactional
    public void getAllLeasesByRentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);
        Rent rent = RentResourceIT.createEntity(em);
        em.persist(rent);
        em.flush();
        lease.addRent(rent);
        leaseRepository.saveAndFlush(lease);
        Long rentId = rent.getId();

        // Get all the leaseList where rent equals to rentId
        defaultLeaseShouldBeFound("rentId.equals=" + rentId);

        // Get all the leaseList where rent equals to rentId + 1
        defaultLeaseShouldNotBeFound("rentId.equals=" + (rentId + 1));
    }


    @Test
    @Transactional
    public void getAllLeasesByInfractionIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);
        Infraction infraction = InfractionResourceIT.createEntity(em);
        em.persist(infraction);
        em.flush();
        lease.addInfraction(infraction);
        leaseRepository.saveAndFlush(lease);
        Long infractionId = infraction.getId();

        // Get all the leaseList where infraction equals to infractionId
        defaultLeaseShouldBeFound("infractionId.equals=" + infractionId);

        // Get all the leaseList where infraction equals to infractionId + 1
        defaultLeaseShouldNotBeFound("infractionId.equals=" + (infractionId + 1));
    }


    @Test
    @Transactional
    public void getAllLeasesByNewLeaseIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);
        Lease newLease = LeaseResourceIT.createEntity(em);
        em.persist(newLease);
        em.flush();
        lease.setNewLease(newLease);
        leaseRepository.saveAndFlush(lease);
        Long newLeaseId = newLease.getId();

        // Get all the leaseList where newLease equals to newLeaseId
        defaultLeaseShouldBeFound("newLeaseId.equals=" + newLeaseId);

        // Get all the leaseList where newLease equals to newLeaseId + 1
        defaultLeaseShouldNotBeFound("newLeaseId.equals=" + (newLeaseId + 1));
    }


    @Test
    @Transactional
    public void getAllLeasesByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);
        Person person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        lease.addPerson(person);
        leaseRepository.saveAndFlush(lease);
        Long personId = person.getId();

        // Get all the leaseList where person equals to personId
        defaultLeaseShouldBeFound("personId.equals=" + personId);

        // Get all the leaseList where person equals to personId + 1
        defaultLeaseShouldNotBeFound("personId.equals=" + (personId + 1));
    }


    @Test
    @Transactional
    public void getAllLeasesByVehicleIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);
        Vehicle vehicle = VehicleResourceIT.createEntity(em);
        em.persist(vehicle);
        em.flush();
        lease.addVehicle(vehicle);
        leaseRepository.saveAndFlush(lease);
        Long vehicleId = vehicle.getId();

        // Get all the leaseList where vehicle equals to vehicleId
        defaultLeaseShouldBeFound("vehicleId.equals=" + vehicleId);

        // Get all the leaseList where vehicle equals to vehicleId + 1
        defaultLeaseShouldNotBeFound("vehicleId.equals=" + (vehicleId + 1));
    }


    @Test
    @Transactional
    public void getAllLeasesByPetIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);
        Pet pet = PetResourceIT.createEntity(em);
        em.persist(pet);
        em.flush();
        lease.addPet(pet);
        leaseRepository.saveAndFlush(lease);
        Long petId = pet.getId();

        // Get all the leaseList where pet equals to petId
        defaultLeaseShouldBeFound("petId.equals=" + petId);

        // Get all the leaseList where pet equals to petId + 1
        defaultLeaseShouldNotBeFound("petId.equals=" + (petId + 1));
    }


    @Test
    @Transactional
    public void getAllLeasesByApartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);
        Apartment apartment = ApartmentResourceIT.createEntity(em);
        em.persist(apartment);
        em.flush();
        lease.setApartment(apartment);
        leaseRepository.saveAndFlush(lease);
        Long apartmentId = apartment.getId();

        // Get all the leaseList where apartment equals to apartmentId
        defaultLeaseShouldBeFound("apartmentId.equals=" + apartmentId);

        // Get all the leaseList where apartment equals to apartmentId + 1
        defaultLeaseShouldNotBeFound("apartmentId.equals=" + (apartmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseShouldBeFound(String filter) throws Exception {
        restLeaseMockMvc.perform(get("/api/leases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lease.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateSigned").value(hasItem(DEFAULT_DATE_SIGNED.toString())))
            .andExpect(jsonPath("$.[*].moveInDate").value(hasItem(DEFAULT_MOVE_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].noticeOfRemovalOrMoveoutDate").value(hasItem(DEFAULT_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].leaseType").value(hasItem(DEFAULT_LEASE_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restLeaseMockMvc.perform(get("/api/leases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseShouldNotBeFound(String filter) throws Exception {
        restLeaseMockMvc.perform(get("/api/leases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseMockMvc.perform(get("/api/leases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLease() throws Exception {
        // Get the lease
        restLeaseMockMvc.perform(get("/api/leases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLease() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        int databaseSizeBeforeUpdate = leaseRepository.findAll().size();

        // Update the lease
        Lease updatedLease = leaseRepository.findById(lease.getId()).get();
        // Disconnect from session so that the updates on updatedLease are not directly saved in db
        em.detach(updatedLease);
        updatedLease
            .dateSigned(UPDATED_DATE_SIGNED)
            .moveInDate(UPDATED_MOVE_IN_DATE)
            .noticeOfRemovalOrMoveoutDate(UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE)
            .endDate(UPDATED_END_DATE)
            .amount(UPDATED_AMOUNT)
            .leaseType(UPDATED_LEASE_TYPE)
            .notes(UPDATED_NOTES);
        LeaseDTO leaseDTO = leaseMapper.toDto(updatedLease);

        restLeaseMockMvc.perform(put("/api/leases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leaseDTO)))
            .andExpect(status().isOk());

        // Validate the Lease in the database
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeUpdate);
        Lease testLease = leaseList.get(leaseList.size() - 1);
        assertThat(testLease.getDateSigned()).isEqualTo(UPDATED_DATE_SIGNED);
        assertThat(testLease.getMoveInDate()).isEqualTo(UPDATED_MOVE_IN_DATE);
        assertThat(testLease.getNoticeOfRemovalOrMoveoutDate()).isEqualTo(UPDATED_NOTICE_OF_REMOVAL_OR_MOVEOUT_DATE);
        assertThat(testLease.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLease.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLease.getLeaseType()).isEqualTo(UPDATED_LEASE_TYPE);
        assertThat(testLease.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingLease() throws Exception {
        int databaseSizeBeforeUpdate = leaseRepository.findAll().size();

        // Create the Lease
        LeaseDTO leaseDTO = leaseMapper.toDto(lease);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseMockMvc.perform(put("/api/leases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lease in the database
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLease() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        int databaseSizeBeforeDelete = leaseRepository.findAll().size();

        // Delete the lease
        restLeaseMockMvc.perform(delete("/api/leases/{id}", lease.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
