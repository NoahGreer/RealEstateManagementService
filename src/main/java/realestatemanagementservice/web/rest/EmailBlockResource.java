package realestatemanagementservice.web.rest;

import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import realestatemanagementservice.service.PersonQueryService;
import realestatemanagementservice.service.PersonService;
import realestatemanagementservice.service.dto.PersonCriteria;
import realestatemanagementservice.service.dto.PersonDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmailBlockResource {
	//private final Logger log = LoggerFactory.getLogger(EmailBlockBuilderDTO.class);
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	
	private final PersonService personService;

    private final PersonQueryService personQueryService;
	
	public EmailBlockResource(PersonService personService, PersonQueryService personQueryService) {
		this.personService = personService;
		this.personQueryService = personQueryService;
	}
	
	/**
     * {@code GET  /person/email} : get all the email for all people with active leases.
     *
     * @param date the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents paid in body.
     */
    @SuppressWarnings("null")
	@GetMapping("/person/email")
    public ResponseEntity<List<String>> getEmails() {
        //log.debug("REST request to get all the Emails of active people");
        
    	BooleanFilter notMinor = new BooleanFilter();
        notMinor.setEquals(true);
        
        StringFilter hasEmail = new StringFilter();
        hasEmail.setContains("@");

        //ActiveLeaseResource.getActiveLeases().
        
        @SuppressWarnings("unchecked")
		List<Long> leaseIDList = (List<Long>) ActiveLeaseResource.getActiveLeasesID();
        LongFilter hasActiveLease = new LongFilter();
        hasActiveLease.setIn(leaseIDList);
        
        PersonCriteria criteria = new PersonCriteria();
        criteria.setIsMinor(notMinor);
        criteria.setEmailAddress(hasEmail);
        criteria.setLeaseId(hasActiveLease);
        
        List<PersonDTO> peoplewithemail = personQueryService.findByCriteria(criteria);
        
        List<String> activeEmails = null;
        for(PersonDTO people : peoplewithemail) {
        	activeEmails.add(people.getEmailAddress());
        }
        
        return ResponseEntity.ok().body(activeEmails);
    }

}