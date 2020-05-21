package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.PersonService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.PersonDTO;
import realestatemanagementservice.service.dto.LeaseDTO;
import realestatemanagementservice.service.dto.PersonCriteria;
import realestatemanagementservice.service.PersonQueryService;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link realestatemanagementservice.domain.Person}.
 */
@RestController
@RequestMapping("/api")
public class PersonResource {

    private final Logger log = LoggerFactory.getLogger(PersonResource.class);

    private static final String ENTITY_NAME = "person";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonService personService;

    private final PersonQueryService personQueryService;

    public PersonResource(PersonService personService, PersonQueryService personQueryService) {
        this.personService = personService;
        this.personQueryService = personQueryService;
    }

    /**
     * {@code POST  /people} : Create a new person.
     *
     * @param personDTO the personDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personDTO, or with status {@code 400 (Bad Request)} if the person has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/people")
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) throws URISyntaxException {
        log.debug("REST request to save Person : {}", personDTO);
        if (personDTO.getId() != null) {
            throw new BadRequestAlertException("A new person cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonDTO result = personService.save(personDTO);
        return ResponseEntity.created(new URI("/api/people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /people} : Updates an existing person.
     *
     * @param personDTO the personDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personDTO,
     * or with status {@code 400 (Bad Request)} if the personDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/people")
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody PersonDTO personDTO) throws URISyntaxException {
        log.debug("REST request to update Person : {}", personDTO);
        if (personDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PersonDTO result = personService.save(personDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /people} : get all the people.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of people in body.
     */
    @GetMapping("/people")
    public ResponseEntity<List<PersonDTO>> getAllPeople(PersonCriteria criteria, Pageable pageable) {
        log.debug("REST request to get People by criteria: {}", criteria);
        Page<PersonDTO> page = personQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /person/email} : get all the email for all people with active leases.
     *
     * @param date the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents paid in body.
     */
    @SuppressWarnings("null")
	@GetMapping("/people/email")
    public ResponseEntity<List<String>> getEmails() {
        //log.debug("REST request to get all the Emails of active people");
        
    	BooleanFilter notMinor = new BooleanFilter();
        notMinor.setEquals(true);
        
        StringFilter hasEmail = new StringFilter();
        hasEmail.setContains("@");
        
        @SuppressWarnings("unchecked")
       
		List<Long> leaseIDList = (List<Long>) LeaseResource.class.getActiveLeaseID();
        
        LongFilter hasActiveLease = new LongFilter();
        hasActiveLease.setIn(leaseIDList);
        
        PersonCriteria criteria = new PersonCriteria();
        criteria.setIsMinor(notMinor);
        criteria.setEmailAddress(hasEmail);
        criteria.setLeaseId(hasActiveLease);
        
        List<PersonDTO> peoplewithemail = personQueryService.findByCriteria(criteria);
        
        List<String> activeEmails =  new ArrayList<String>();
        for(PersonDTO person : peoplewithemail) {
        	activeEmails.add(person.getEmailAddress());
        }
        
        return ResponseEntity.ok().body(activeEmails);
    }
    
    /**
     * {@code GET  /people/count} : count all the people.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/people/count")
    public ResponseEntity<Long> countPeople(PersonCriteria criteria) {
        log.debug("REST request to count People by criteria: {}", criteria);
        return ResponseEntity.ok().body(personQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /people/:id} : get the "id" person.
     *
     * @param id the id of the personDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/people/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable Long id) {
        log.debug("REST request to get Person : {}", id);
        Optional<PersonDTO> personDTO = personService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personDTO);
    }

    /**
     * {@code DELETE  /people/:id} : delete the "id" person.
     *
     * @param id the id of the personDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/people/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        log.debug("REST request to delete Person : {}", id);
        personService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
