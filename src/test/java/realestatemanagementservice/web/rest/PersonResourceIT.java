package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Person;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.repository.PersonRepository;
import realestatemanagementservice.security.AuthoritiesConstants;
import realestatemanagementservice.service.PersonService;
import realestatemanagementservice.service.dto.PersonDTO;
import realestatemanagementservice.service.mapper.PersonMapper;
import realestatemanagementservice.service.dto.PersonCriteria;
import realestatemanagementservice.service.PersonQueryService;

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
 * Integration tests for the {@link PersonResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.MANAGER)
public class PersonResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PRIMARY_CONTACT = false;
    private static final Boolean UPDATED_PRIMARY_CONTACT = true;

    private static final Boolean DEFAULT_IS_MINOR = false;
    private static final Boolean UPDATED_IS_MINOR = true;

    private static final String DEFAULT_SSN = "AAAAAAAAAA";
    private static final String UPDATED_SSN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BACKGROUND_CHECK_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BACKGROUND_CHECK_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BACKGROUND_CHECK_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EMPLOYMENT_VERIFICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMPLOYMENT_VERIFICATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EMPLOYMENT_VERIFICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER = "BBBBBBBBBB";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonQueryService personQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonMockMvc;

    private Person person;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createEntity(EntityManager em) {
        Person person = new Person()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .primaryContact(DEFAULT_PRIMARY_CONTACT)
            .isMinor(DEFAULT_IS_MINOR)
            .ssn(DEFAULT_SSN)
            .backgroundCheckDate(DEFAULT_BACKGROUND_CHECK_DATE)
            .backgroundCheckConfirmationNumber(DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER)
            .employmentVerificationDate(DEFAULT_EMPLOYMENT_VERIFICATION_DATE)
            .employmentVerificationConfirmationNumber(DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
        return person;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createUpdatedEntity(EntityManager em) {
        Person person = new Person()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .primaryContact(UPDATED_PRIMARY_CONTACT)
            .isMinor(UPDATED_IS_MINOR)
            .ssn(UPDATED_SSN)
            .backgroundCheckDate(UPDATED_BACKGROUND_CHECK_DATE)
            .backgroundCheckConfirmationNumber(UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER)
            .employmentVerificationDate(UPDATED_EMPLOYMENT_VERIFICATION_DATE)
            .employmentVerificationConfirmationNumber(UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
        return person;
    }

    @BeforeEach
    public void initTest() {
        person = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);
        restPersonMockMvc.perform(post("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPerson.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPerson.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testPerson.isPrimaryContact()).isEqualTo(DEFAULT_PRIMARY_CONTACT);
        assertThat(testPerson.isIsMinor()).isEqualTo(DEFAULT_IS_MINOR);
        assertThat(testPerson.getSsn()).isEqualTo(DEFAULT_SSN);
        assertThat(testPerson.getBackgroundCheckDate()).isEqualTo(DEFAULT_BACKGROUND_CHECK_DATE);
        assertThat(testPerson.getBackgroundCheckConfirmationNumber()).isEqualTo(DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER);
        assertThat(testPerson.getEmploymentVerificationDate()).isEqualTo(DEFAULT_EMPLOYMENT_VERIFICATION_DATE);
        assertThat(testPerson.getEmploymentVerificationConfirmationNumber()).isEqualTo(DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void createPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person with an existing ID
        person.setId(1L);
        PersonDTO personDTO = personMapper.toDto(person);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc.perform(post("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList
        restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].primaryContact").value(hasItem(DEFAULT_PRIMARY_CONTACT.booleanValue())))
            .andExpect(jsonPath("$.[*].isMinor").value(hasItem(DEFAULT_IS_MINOR.booleanValue())))
            .andExpect(jsonPath("$.[*].ssn").value(hasItem(DEFAULT_SSN)))
            .andExpect(jsonPath("$.[*].backgroundCheckDate").value(hasItem(DEFAULT_BACKGROUND_CHECK_DATE.toString())))
            .andExpect(jsonPath("$.[*].backgroundCheckConfirmationNumber").value(hasItem(DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER)))
            .andExpect(jsonPath("$.[*].employmentVerificationDate").value(hasItem(DEFAULT_EMPLOYMENT_VERIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].employmentVerificationConfirmationNumber").value(hasItem(DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.primaryContact").value(DEFAULT_PRIMARY_CONTACT.booleanValue()))
            .andExpect(jsonPath("$.isMinor").value(DEFAULT_IS_MINOR.booleanValue()))
            .andExpect(jsonPath("$.ssn").value(DEFAULT_SSN))
            .andExpect(jsonPath("$.backgroundCheckDate").value(DEFAULT_BACKGROUND_CHECK_DATE.toString()))
            .andExpect(jsonPath("$.backgroundCheckConfirmationNumber").value(DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER))
            .andExpect(jsonPath("$.employmentVerificationDate").value(DEFAULT_EMPLOYMENT_VERIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.employmentVerificationConfirmationNumber").value(DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER));
    }


    @Test
    @Transactional
    public void getPeopleByIdFiltering() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        Long id = person.getId();

        defaultPersonShouldBeFound("id.equals=" + id);
        defaultPersonShouldNotBeFound("id.notEquals=" + id);

        defaultPersonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPeopleByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName equals to DEFAULT_FIRST_NAME
        defaultPersonShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the personList where firstName equals to UPDATED_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName not equals to DEFAULT_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the personList where firstName not equals to UPDATED_FIRST_NAME
        defaultPersonShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultPersonShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the personList where firstName equals to UPDATED_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName is not null
        defaultPersonShouldBeFound("firstName.specified=true");

        // Get all the personList where firstName is null
        defaultPersonShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName contains DEFAULT_FIRST_NAME
        defaultPersonShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the personList where firstName contains UPDATED_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName does not contain DEFAULT_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the personList where firstName does not contain UPDATED_FIRST_NAME
        defaultPersonShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllPeopleByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName equals to DEFAULT_LAST_NAME
        defaultPersonShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName equals to UPDATED_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName not equals to DEFAULT_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName not equals to UPDATED_LAST_NAME
        defaultPersonShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultPersonShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the personList where lastName equals to UPDATED_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName is not null
        defaultPersonShouldBeFound("lastName.specified=true");

        // Get all the personList where lastName is null
        defaultPersonShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByLastNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName contains DEFAULT_LAST_NAME
        defaultPersonShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName contains UPDATED_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName does not contain DEFAULT_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName does not contain UPDATED_LAST_NAME
        defaultPersonShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllPeopleByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultPersonShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the personList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPersonShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultPersonShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the personList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultPersonShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultPersonShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the personList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPersonShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phoneNumber is not null
        defaultPersonShouldBeFound("phoneNumber.specified=true");

        // Get all the personList where phoneNumber is null
        defaultPersonShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultPersonShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the personList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultPersonShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultPersonShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the personList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultPersonShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPeopleByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultPersonShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the personList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultPersonShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmailAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where emailAddress not equals to DEFAULT_EMAIL_ADDRESS
        defaultPersonShouldNotBeFound("emailAddress.notEquals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the personList where emailAddress not equals to UPDATED_EMAIL_ADDRESS
        defaultPersonShouldBeFound("emailAddress.notEquals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultPersonShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the personList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultPersonShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where emailAddress is not null
        defaultPersonShouldBeFound("emailAddress.specified=true");

        // Get all the personList where emailAddress is null
        defaultPersonShouldNotBeFound("emailAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultPersonShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the personList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultPersonShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultPersonShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the personList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultPersonShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllPeopleByPrimaryContactIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where primaryContact equals to DEFAULT_PRIMARY_CONTACT
        defaultPersonShouldBeFound("primaryContact.equals=" + DEFAULT_PRIMARY_CONTACT);

        // Get all the personList where primaryContact equals to UPDATED_PRIMARY_CONTACT
        defaultPersonShouldNotBeFound("primaryContact.equals=" + UPDATED_PRIMARY_CONTACT);
    }

    @Test
    @Transactional
    public void getAllPeopleByPrimaryContactIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where primaryContact not equals to DEFAULT_PRIMARY_CONTACT
        defaultPersonShouldNotBeFound("primaryContact.notEquals=" + DEFAULT_PRIMARY_CONTACT);

        // Get all the personList where primaryContact not equals to UPDATED_PRIMARY_CONTACT
        defaultPersonShouldBeFound("primaryContact.notEquals=" + UPDATED_PRIMARY_CONTACT);
    }

    @Test
    @Transactional
    public void getAllPeopleByPrimaryContactIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where primaryContact in DEFAULT_PRIMARY_CONTACT or UPDATED_PRIMARY_CONTACT
        defaultPersonShouldBeFound("primaryContact.in=" + DEFAULT_PRIMARY_CONTACT + "," + UPDATED_PRIMARY_CONTACT);

        // Get all the personList where primaryContact equals to UPDATED_PRIMARY_CONTACT
        defaultPersonShouldNotBeFound("primaryContact.in=" + UPDATED_PRIMARY_CONTACT);
    }

    @Test
    @Transactional
    public void getAllPeopleByPrimaryContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where primaryContact is not null
        defaultPersonShouldBeFound("primaryContact.specified=true");

        // Get all the personList where primaryContact is null
        defaultPersonShouldNotBeFound("primaryContact.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByIsMinorIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isMinor equals to DEFAULT_IS_MINOR
        defaultPersonShouldBeFound("isMinor.equals=" + DEFAULT_IS_MINOR);

        // Get all the personList where isMinor equals to UPDATED_IS_MINOR
        defaultPersonShouldNotBeFound("isMinor.equals=" + UPDATED_IS_MINOR);
    }

    @Test
    @Transactional
    public void getAllPeopleByIsMinorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isMinor not equals to DEFAULT_IS_MINOR
        defaultPersonShouldNotBeFound("isMinor.notEquals=" + DEFAULT_IS_MINOR);

        // Get all the personList where isMinor not equals to UPDATED_IS_MINOR
        defaultPersonShouldBeFound("isMinor.notEquals=" + UPDATED_IS_MINOR);
    }

    @Test
    @Transactional
    public void getAllPeopleByIsMinorIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isMinor in DEFAULT_IS_MINOR or UPDATED_IS_MINOR
        defaultPersonShouldBeFound("isMinor.in=" + DEFAULT_IS_MINOR + "," + UPDATED_IS_MINOR);

        // Get all the personList where isMinor equals to UPDATED_IS_MINOR
        defaultPersonShouldNotBeFound("isMinor.in=" + UPDATED_IS_MINOR);
    }

    @Test
    @Transactional
    public void getAllPeopleByIsMinorIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isMinor is not null
        defaultPersonShouldBeFound("isMinor.specified=true");

        // Get all the personList where isMinor is null
        defaultPersonShouldNotBeFound("isMinor.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleBySsnIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where ssn equals to DEFAULT_SSN
        defaultPersonShouldBeFound("ssn.equals=" + DEFAULT_SSN);

        // Get all the personList where ssn equals to UPDATED_SSN
        defaultPersonShouldNotBeFound("ssn.equals=" + UPDATED_SSN);
    }

    @Test
    @Transactional
    public void getAllPeopleBySsnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where ssn not equals to DEFAULT_SSN
        defaultPersonShouldNotBeFound("ssn.notEquals=" + DEFAULT_SSN);

        // Get all the personList where ssn not equals to UPDATED_SSN
        defaultPersonShouldBeFound("ssn.notEquals=" + UPDATED_SSN);
    }

    @Test
    @Transactional
    public void getAllPeopleBySsnIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where ssn in DEFAULT_SSN or UPDATED_SSN
        defaultPersonShouldBeFound("ssn.in=" + DEFAULT_SSN + "," + UPDATED_SSN);

        // Get all the personList where ssn equals to UPDATED_SSN
        defaultPersonShouldNotBeFound("ssn.in=" + UPDATED_SSN);
    }

    @Test
    @Transactional
    public void getAllPeopleBySsnIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where ssn is not null
        defaultPersonShouldBeFound("ssn.specified=true");

        // Get all the personList where ssn is null
        defaultPersonShouldNotBeFound("ssn.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleBySsnContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where ssn contains DEFAULT_SSN
        defaultPersonShouldBeFound("ssn.contains=" + DEFAULT_SSN);

        // Get all the personList where ssn contains UPDATED_SSN
        defaultPersonShouldNotBeFound("ssn.contains=" + UPDATED_SSN);
    }

    @Test
    @Transactional
    public void getAllPeopleBySsnNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where ssn does not contain DEFAULT_SSN
        defaultPersonShouldNotBeFound("ssn.doesNotContain=" + DEFAULT_SSN);

        // Get all the personList where ssn does not contain UPDATED_SSN
        defaultPersonShouldBeFound("ssn.doesNotContain=" + UPDATED_SSN);
    }


    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckDateIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckDate equals to DEFAULT_BACKGROUND_CHECK_DATE
        defaultPersonShouldBeFound("backgroundCheckDate.equals=" + DEFAULT_BACKGROUND_CHECK_DATE);

        // Get all the personList where backgroundCheckDate equals to UPDATED_BACKGROUND_CHECK_DATE
        defaultPersonShouldNotBeFound("backgroundCheckDate.equals=" + UPDATED_BACKGROUND_CHECK_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckDate not equals to DEFAULT_BACKGROUND_CHECK_DATE
        defaultPersonShouldNotBeFound("backgroundCheckDate.notEquals=" + DEFAULT_BACKGROUND_CHECK_DATE);

        // Get all the personList where backgroundCheckDate not equals to UPDATED_BACKGROUND_CHECK_DATE
        defaultPersonShouldBeFound("backgroundCheckDate.notEquals=" + UPDATED_BACKGROUND_CHECK_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckDateIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckDate in DEFAULT_BACKGROUND_CHECK_DATE or UPDATED_BACKGROUND_CHECK_DATE
        defaultPersonShouldBeFound("backgroundCheckDate.in=" + DEFAULT_BACKGROUND_CHECK_DATE + "," + UPDATED_BACKGROUND_CHECK_DATE);

        // Get all the personList where backgroundCheckDate equals to UPDATED_BACKGROUND_CHECK_DATE
        defaultPersonShouldNotBeFound("backgroundCheckDate.in=" + UPDATED_BACKGROUND_CHECK_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckDate is not null
        defaultPersonShouldBeFound("backgroundCheckDate.specified=true");

        // Get all the personList where backgroundCheckDate is null
        defaultPersonShouldNotBeFound("backgroundCheckDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckDate is greater than or equal to DEFAULT_BACKGROUND_CHECK_DATE
        defaultPersonShouldBeFound("backgroundCheckDate.greaterThanOrEqual=" + DEFAULT_BACKGROUND_CHECK_DATE);

        // Get all the personList where backgroundCheckDate is greater than or equal to UPDATED_BACKGROUND_CHECK_DATE
        defaultPersonShouldNotBeFound("backgroundCheckDate.greaterThanOrEqual=" + UPDATED_BACKGROUND_CHECK_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckDate is less than or equal to DEFAULT_BACKGROUND_CHECK_DATE
        defaultPersonShouldBeFound("backgroundCheckDate.lessThanOrEqual=" + DEFAULT_BACKGROUND_CHECK_DATE);

        // Get all the personList where backgroundCheckDate is less than or equal to SMALLER_BACKGROUND_CHECK_DATE
        defaultPersonShouldNotBeFound("backgroundCheckDate.lessThanOrEqual=" + SMALLER_BACKGROUND_CHECK_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckDateIsLessThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckDate is less than DEFAULT_BACKGROUND_CHECK_DATE
        defaultPersonShouldNotBeFound("backgroundCheckDate.lessThan=" + DEFAULT_BACKGROUND_CHECK_DATE);

        // Get all the personList where backgroundCheckDate is less than UPDATED_BACKGROUND_CHECK_DATE
        defaultPersonShouldBeFound("backgroundCheckDate.lessThan=" + UPDATED_BACKGROUND_CHECK_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckDate is greater than DEFAULT_BACKGROUND_CHECK_DATE
        defaultPersonShouldNotBeFound("backgroundCheckDate.greaterThan=" + DEFAULT_BACKGROUND_CHECK_DATE);

        // Get all the personList where backgroundCheckDate is greater than SMALLER_BACKGROUND_CHECK_DATE
        defaultPersonShouldBeFound("backgroundCheckDate.greaterThan=" + SMALLER_BACKGROUND_CHECK_DATE);
    }


    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckConfirmationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckConfirmationNumber equals to DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("backgroundCheckConfirmationNumber.equals=" + DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER);

        // Get all the personList where backgroundCheckConfirmationNumber equals to UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("backgroundCheckConfirmationNumber.equals=" + UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckConfirmationNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckConfirmationNumber not equals to DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("backgroundCheckConfirmationNumber.notEquals=" + DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER);

        // Get all the personList where backgroundCheckConfirmationNumber not equals to UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("backgroundCheckConfirmationNumber.notEquals=" + UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckConfirmationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckConfirmationNumber in DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER or UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("backgroundCheckConfirmationNumber.in=" + DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER + "," + UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER);

        // Get all the personList where backgroundCheckConfirmationNumber equals to UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("backgroundCheckConfirmationNumber.in=" + UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckConfirmationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckConfirmationNumber is not null
        defaultPersonShouldBeFound("backgroundCheckConfirmationNumber.specified=true");

        // Get all the personList where backgroundCheckConfirmationNumber is null
        defaultPersonShouldNotBeFound("backgroundCheckConfirmationNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckConfirmationNumberContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckConfirmationNumber contains DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("backgroundCheckConfirmationNumber.contains=" + DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER);

        // Get all the personList where backgroundCheckConfirmationNumber contains UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("backgroundCheckConfirmationNumber.contains=" + UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByBackgroundCheckConfirmationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where backgroundCheckConfirmationNumber does not contain DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("backgroundCheckConfirmationNumber.doesNotContain=" + DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER);

        // Get all the personList where backgroundCheckConfirmationNumber does not contain UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("backgroundCheckConfirmationNumber.doesNotContain=" + UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationDate equals to DEFAULT_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldBeFound("employmentVerificationDate.equals=" + DEFAULT_EMPLOYMENT_VERIFICATION_DATE);

        // Get all the personList where employmentVerificationDate equals to UPDATED_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldNotBeFound("employmentVerificationDate.equals=" + UPDATED_EMPLOYMENT_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationDate not equals to DEFAULT_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldNotBeFound("employmentVerificationDate.notEquals=" + DEFAULT_EMPLOYMENT_VERIFICATION_DATE);

        // Get all the personList where employmentVerificationDate not equals to UPDATED_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldBeFound("employmentVerificationDate.notEquals=" + UPDATED_EMPLOYMENT_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationDateIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationDate in DEFAULT_EMPLOYMENT_VERIFICATION_DATE or UPDATED_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldBeFound("employmentVerificationDate.in=" + DEFAULT_EMPLOYMENT_VERIFICATION_DATE + "," + UPDATED_EMPLOYMENT_VERIFICATION_DATE);

        // Get all the personList where employmentVerificationDate equals to UPDATED_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldNotBeFound("employmentVerificationDate.in=" + UPDATED_EMPLOYMENT_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationDate is not null
        defaultPersonShouldBeFound("employmentVerificationDate.specified=true");

        // Get all the personList where employmentVerificationDate is null
        defaultPersonShouldNotBeFound("employmentVerificationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationDate is greater than or equal to DEFAULT_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldBeFound("employmentVerificationDate.greaterThanOrEqual=" + DEFAULT_EMPLOYMENT_VERIFICATION_DATE);

        // Get all the personList where employmentVerificationDate is greater than or equal to UPDATED_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldNotBeFound("employmentVerificationDate.greaterThanOrEqual=" + UPDATED_EMPLOYMENT_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationDate is less than or equal to DEFAULT_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldBeFound("employmentVerificationDate.lessThanOrEqual=" + DEFAULT_EMPLOYMENT_VERIFICATION_DATE);

        // Get all the personList where employmentVerificationDate is less than or equal to SMALLER_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldNotBeFound("employmentVerificationDate.lessThanOrEqual=" + SMALLER_EMPLOYMENT_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationDate is less than DEFAULT_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldNotBeFound("employmentVerificationDate.lessThan=" + DEFAULT_EMPLOYMENT_VERIFICATION_DATE);

        // Get all the personList where employmentVerificationDate is less than UPDATED_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldBeFound("employmentVerificationDate.lessThan=" + UPDATED_EMPLOYMENT_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationDate is greater than DEFAULT_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldNotBeFound("employmentVerificationDate.greaterThan=" + DEFAULT_EMPLOYMENT_VERIFICATION_DATE);

        // Get all the personList where employmentVerificationDate is greater than SMALLER_EMPLOYMENT_VERIFICATION_DATE
        defaultPersonShouldBeFound("employmentVerificationDate.greaterThan=" + SMALLER_EMPLOYMENT_VERIFICATION_DATE);
    }


    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationConfirmationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationConfirmationNumber equals to DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("employmentVerificationConfirmationNumber.equals=" + DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);

        // Get all the personList where employmentVerificationConfirmationNumber equals to UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("employmentVerificationConfirmationNumber.equals=" + UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationConfirmationNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationConfirmationNumber not equals to DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("employmentVerificationConfirmationNumber.notEquals=" + DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);

        // Get all the personList where employmentVerificationConfirmationNumber not equals to UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("employmentVerificationConfirmationNumber.notEquals=" + UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationConfirmationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationConfirmationNumber in DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER or UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("employmentVerificationConfirmationNumber.in=" + DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER + "," + UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);

        // Get all the personList where employmentVerificationConfirmationNumber equals to UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("employmentVerificationConfirmationNumber.in=" + UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationConfirmationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationConfirmationNumber is not null
        defaultPersonShouldBeFound("employmentVerificationConfirmationNumber.specified=true");

        // Get all the personList where employmentVerificationConfirmationNumber is null
        defaultPersonShouldNotBeFound("employmentVerificationConfirmationNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationConfirmationNumberContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationConfirmationNumber contains DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("employmentVerificationConfirmationNumber.contains=" + DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);

        // Get all the personList where employmentVerificationConfirmationNumber contains UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("employmentVerificationConfirmationNumber.contains=" + UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmploymentVerificationConfirmationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where employmentVerificationConfirmationNumber does not contain DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldNotBeFound("employmentVerificationConfirmationNumber.doesNotContain=" + DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);

        // Get all the personList where employmentVerificationConfirmationNumber does not contain UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER
        defaultPersonShouldBeFound("employmentVerificationConfirmationNumber.doesNotContain=" + UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPeopleByLeaseIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Lease lease = LeaseResourceIT.createEntity(em);
        em.persist(lease);
        em.flush();
        person.addLease(lease);
        personRepository.saveAndFlush(person);
        Long leaseId = lease.getId();

        // Get all the personList where lease equals to leaseId
        defaultPersonShouldBeFound("leaseId.equals=" + leaseId);

        // Get all the personList where lease equals to leaseId + 1
        defaultPersonShouldNotBeFound("leaseId.equals=" + (leaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonShouldBeFound(String filter) throws Exception {
        restPersonMockMvc.perform(get("/api/people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].primaryContact").value(hasItem(DEFAULT_PRIMARY_CONTACT.booleanValue())))
            .andExpect(jsonPath("$.[*].isMinor").value(hasItem(DEFAULT_IS_MINOR.booleanValue())))
            .andExpect(jsonPath("$.[*].ssn").value(hasItem(DEFAULT_SSN)))
            .andExpect(jsonPath("$.[*].backgroundCheckDate").value(hasItem(DEFAULT_BACKGROUND_CHECK_DATE.toString())))
            .andExpect(jsonPath("$.[*].backgroundCheckConfirmationNumber").value(hasItem(DEFAULT_BACKGROUND_CHECK_CONFIRMATION_NUMBER)))
            .andExpect(jsonPath("$.[*].employmentVerificationDate").value(hasItem(DEFAULT_EMPLOYMENT_VERIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].employmentVerificationConfirmationNumber").value(hasItem(DEFAULT_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER)));

        // Check, that the count call also returns 1
        restPersonMockMvc.perform(get("/api/people/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonShouldNotBeFound(String filter) throws Exception {
        restPersonMockMvc.perform(get("/api/people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonMockMvc.perform(get("/api/people/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = personRepository.findById(person.getId()).get();
        // Disconnect from session so that the updates on updatedPerson are not directly saved in db
        em.detach(updatedPerson);
        updatedPerson
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .primaryContact(UPDATED_PRIMARY_CONTACT)
            .isMinor(UPDATED_IS_MINOR)
            .ssn(UPDATED_SSN)
            .backgroundCheckDate(UPDATED_BACKGROUND_CHECK_DATE)
            .backgroundCheckConfirmationNumber(UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER)
            .employmentVerificationDate(UPDATED_EMPLOYMENT_VERIFICATION_DATE)
            .employmentVerificationConfirmationNumber(UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
        PersonDTO personDTO = personMapper.toDto(updatedPerson);

        restPersonMockMvc.perform(put("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPerson.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPerson.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testPerson.isPrimaryContact()).isEqualTo(UPDATED_PRIMARY_CONTACT);
        assertThat(testPerson.isIsMinor()).isEqualTo(UPDATED_IS_MINOR);
        assertThat(testPerson.getSsn()).isEqualTo(UPDATED_SSN);
        assertThat(testPerson.getBackgroundCheckDate()).isEqualTo(UPDATED_BACKGROUND_CHECK_DATE);
        assertThat(testPerson.getBackgroundCheckConfirmationNumber()).isEqualTo(UPDATED_BACKGROUND_CHECK_CONFIRMATION_NUMBER);
        assertThat(testPerson.getEmploymentVerificationDate()).isEqualTo(UPDATED_EMPLOYMENT_VERIFICATION_DATE);
        assertThat(testPerson.getEmploymentVerificationConfirmationNumber()).isEqualTo(UPDATED_EMPLOYMENT_VERIFICATION_CONFIRMATION_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc.perform(put("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Delete the person
        restPersonMockMvc.perform(delete("/api/people/{id}", person.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
