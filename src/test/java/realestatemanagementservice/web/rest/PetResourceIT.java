package realestatemanagementservice.web.rest;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Pet;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.repository.PetRepository;
import realestatemanagementservice.security.AuthoritiesConstants;
import realestatemanagementservice.service.PetService;
import realestatemanagementservice.service.dto.PetDTO;
import realestatemanagementservice.service.mapper.PetMapper;
import realestatemanagementservice.service.dto.PetCriteria;
import realestatemanagementservice.service.PetQueryService;

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
 * Integration tests for the {@link PetResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.MANAGER)
public class PetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL = false;
    private static final Boolean UPDATED_CERTIFIED_ASSISTANCE_ANIMAL = true;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private PetService petService;

    @Autowired
    private PetQueryService petQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPetMockMvc;

    private Pet pet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pet createEntity(EntityManager em) {
        Pet pet = new Pet()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .color(DEFAULT_COLOR)
            .certifiedAssistanceAnimal(DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL);
        return pet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pet createUpdatedEntity(EntityManager em) {
        Pet pet = new Pet()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .color(UPDATED_COLOR)
            .certifiedAssistanceAnimal(UPDATED_CERTIFIED_ASSISTANCE_ANIMAL);
        return pet;
    }

    @BeforeEach
    public void initTest() {
        pet = createEntity(em);
    }

    @Test
    @Transactional
    public void createPet() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);
        restPetMockMvc.perform(post("/api/pets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isCreated());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate + 1);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPet.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testPet.isCertifiedAssistanceAnimal()).isEqualTo(DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL);
    }

    @Test
    @Transactional
    public void createPetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();

        // Create the Pet with an existing ID
        pet.setId(1L);
        PetDTO petDTO = petMapper.toDto(pet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetMockMvc.perform(post("/api/pets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPets() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList
        restPetMockMvc.perform(get("/api/pets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].certifiedAssistanceAnimal").value(hasItem(DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get the pet
        restPetMockMvc.perform(get("/api/pets/{id}", pet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.certifiedAssistanceAnimal").value(DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL.booleanValue()));
    }


    @Test
    @Transactional
    public void getPetsByIdFiltering() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        Long id = pet.getId();

        defaultPetShouldBeFound("id.equals=" + id);
        defaultPetShouldNotBeFound("id.notEquals=" + id);

        defaultPetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPetShouldNotBeFound("id.greaterThan=" + id);

        defaultPetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPetShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name equals to DEFAULT_NAME
        defaultPetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the petList where name equals to UPDATED_NAME
        defaultPetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPetsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name not equals to DEFAULT_NAME
        defaultPetShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the petList where name not equals to UPDATED_NAME
        defaultPetShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the petList where name equals to UPDATED_NAME
        defaultPetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name is not null
        defaultPetShouldBeFound("name.specified=true");

        // Get all the petList where name is null
        defaultPetShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPetsByNameContainsSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name contains DEFAULT_NAME
        defaultPetShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the petList where name contains UPDATED_NAME
        defaultPetShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPetsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name does not contain DEFAULT_NAME
        defaultPetShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the petList where name does not contain UPDATED_NAME
        defaultPetShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPetsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where type equals to DEFAULT_TYPE
        defaultPetShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the petList where type equals to UPDATED_TYPE
        defaultPetShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPetsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where type not equals to DEFAULT_TYPE
        defaultPetShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the petList where type not equals to UPDATED_TYPE
        defaultPetShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPetsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPetShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the petList where type equals to UPDATED_TYPE
        defaultPetShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPetsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where type is not null
        defaultPetShouldBeFound("type.specified=true");

        // Get all the petList where type is null
        defaultPetShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllPetsByTypeContainsSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where type contains DEFAULT_TYPE
        defaultPetShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the petList where type contains UPDATED_TYPE
        defaultPetShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPetsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where type does not contain DEFAULT_TYPE
        defaultPetShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the petList where type does not contain UPDATED_TYPE
        defaultPetShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllPetsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where color equals to DEFAULT_COLOR
        defaultPetShouldBeFound("color.equals=" + DEFAULT_COLOR);

        // Get all the petList where color equals to UPDATED_COLOR
        defaultPetShouldNotBeFound("color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllPetsByColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where color not equals to DEFAULT_COLOR
        defaultPetShouldNotBeFound("color.notEquals=" + DEFAULT_COLOR);

        // Get all the petList where color not equals to UPDATED_COLOR
        defaultPetShouldBeFound("color.notEquals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllPetsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where color in DEFAULT_COLOR or UPDATED_COLOR
        defaultPetShouldBeFound("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR);

        // Get all the petList where color equals to UPDATED_COLOR
        defaultPetShouldNotBeFound("color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllPetsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where color is not null
        defaultPetShouldBeFound("color.specified=true");

        // Get all the petList where color is null
        defaultPetShouldNotBeFound("color.specified=false");
    }
                @Test
    @Transactional
    public void getAllPetsByColorContainsSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where color contains DEFAULT_COLOR
        defaultPetShouldBeFound("color.contains=" + DEFAULT_COLOR);

        // Get all the petList where color contains UPDATED_COLOR
        defaultPetShouldNotBeFound("color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllPetsByColorNotContainsSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where color does not contain DEFAULT_COLOR
        defaultPetShouldNotBeFound("color.doesNotContain=" + DEFAULT_COLOR);

        // Get all the petList where color does not contain UPDATED_COLOR
        defaultPetShouldBeFound("color.doesNotContain=" + UPDATED_COLOR);
    }


    @Test
    @Transactional
    public void getAllPetsByCertifiedAssistanceAnimalIsEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where certifiedAssistanceAnimal equals to DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL
        defaultPetShouldBeFound("certifiedAssistanceAnimal.equals=" + DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL);

        // Get all the petList where certifiedAssistanceAnimal equals to UPDATED_CERTIFIED_ASSISTANCE_ANIMAL
        defaultPetShouldNotBeFound("certifiedAssistanceAnimal.equals=" + UPDATED_CERTIFIED_ASSISTANCE_ANIMAL);
    }

    @Test
    @Transactional
    public void getAllPetsByCertifiedAssistanceAnimalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where certifiedAssistanceAnimal not equals to DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL
        defaultPetShouldNotBeFound("certifiedAssistanceAnimal.notEquals=" + DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL);

        // Get all the petList where certifiedAssistanceAnimal not equals to UPDATED_CERTIFIED_ASSISTANCE_ANIMAL
        defaultPetShouldBeFound("certifiedAssistanceAnimal.notEquals=" + UPDATED_CERTIFIED_ASSISTANCE_ANIMAL);
    }

    @Test
    @Transactional
    public void getAllPetsByCertifiedAssistanceAnimalIsInShouldWork() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where certifiedAssistanceAnimal in DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL or UPDATED_CERTIFIED_ASSISTANCE_ANIMAL
        defaultPetShouldBeFound("certifiedAssistanceAnimal.in=" + DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL + "," + UPDATED_CERTIFIED_ASSISTANCE_ANIMAL);

        // Get all the petList where certifiedAssistanceAnimal equals to UPDATED_CERTIFIED_ASSISTANCE_ANIMAL
        defaultPetShouldNotBeFound("certifiedAssistanceAnimal.in=" + UPDATED_CERTIFIED_ASSISTANCE_ANIMAL);
    }

    @Test
    @Transactional
    public void getAllPetsByCertifiedAssistanceAnimalIsNullOrNotNull() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where certifiedAssistanceAnimal is not null
        defaultPetShouldBeFound("certifiedAssistanceAnimal.specified=true");

        // Get all the petList where certifiedAssistanceAnimal is null
        defaultPetShouldNotBeFound("certifiedAssistanceAnimal.specified=false");
    }

    @Test
    @Transactional
    public void getAllPetsByLeaseIsEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);
        Lease lease = LeaseResourceIT.createEntity(em);
        em.persist(lease);
        em.flush();
        pet.addLease(lease);
        petRepository.saveAndFlush(pet);
        Long leaseId = lease.getId();

        // Get all the petList where lease equals to leaseId
        defaultPetShouldBeFound("leaseId.equals=" + leaseId);

        // Get all the petList where lease equals to leaseId + 1
        defaultPetShouldNotBeFound("leaseId.equals=" + (leaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPetShouldBeFound(String filter) throws Exception {
        restPetMockMvc.perform(get("/api/pets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].certifiedAssistanceAnimal").value(hasItem(DEFAULT_CERTIFIED_ASSISTANCE_ANIMAL.booleanValue())));

        // Check, that the count call also returns 1
        restPetMockMvc.perform(get("/api/pets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPetShouldNotBeFound(String filter) throws Exception {
        restPetMockMvc.perform(get("/api/pets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPetMockMvc.perform(get("/api/pets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPet() throws Exception {
        // Get the pet
        restPetMockMvc.perform(get("/api/pets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet
        Pet updatedPet = petRepository.findById(pet.getId()).get();
        // Disconnect from session so that the updates on updatedPet are not directly saved in db
        em.detach(updatedPet);
        updatedPet
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .color(UPDATED_COLOR)
            .certifiedAssistanceAnimal(UPDATED_CERTIFIED_ASSISTANCE_ANIMAL);
        PetDTO petDTO = petMapper.toDto(updatedPet);

        restPetMockMvc.perform(put("/api/pets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPet.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testPet.isCertifiedAssistanceAnimal()).isEqualTo(UPDATED_CERTIFIED_ASSISTANCE_ANIMAL);
    }

    @Test
    @Transactional
    public void updateNonExistingPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetMockMvc.perform(put("/api/pets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeDelete = petRepository.findAll().size();

        // Delete the pet
        restPetMockMvc.perform(delete("/api/pets/{id}", pet.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
