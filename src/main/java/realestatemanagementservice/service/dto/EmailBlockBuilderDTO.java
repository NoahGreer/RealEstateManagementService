package realestatemanagementservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.domain.Person;
import realestatemanagementservice.service.PersonQueryService;
import realestatemanagementservice.service.PersonService;
import realestatemanagementservice.web.rest.ActiveLeaseResource;

import javax.persistence.*;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;


/*public class EmailBlockBuilderDTO {
	
	private int count;
	/*Could turn the string into a set to remove duplicates but letting the email software prevent the double send
	*Could become a problem if number of email addresses goes over the allowable number of email addresses ~500 for a message 
	*but unlikely at this point
	*
	private String emails;
	LocalDate today = LocalDate.now();
	
	public void generateBlock(){
		EntityManagerFactory emf =
			     Persistence.createEntityManagerFactory(
			          "objectdb:$objectdb/db/points.odb");
		
		EntityManager session = emf.createEntityManager();;
		//querying the person entity
		TypedQuery<Person> query = session.createQuery("SELECT * from Person",
	            Person.class);
		
		//turning the person entity query into a list of persons
		List<Person> people = query.getResultList();
		
		//stepping through the people list
		for(Person nextperson: people){
		
			//getting rid of the most broad categories, not an adult or no email or no lease on record 
			if(!(nextperson.isIsMinor() || nextperson.getEmailAddress() == null || nextperson.getLeases().isEmpty())) {
			
				//get leases returns a set, grabbing the lease with the max end date which should be then most recent signed
				Lease nextpersonlease = Collections.max(nextperson.getLeases(), Comparator.comparing(s -> s.getEndDate()));
				
				//testing to see if the most recent lease is active today
				if(today.isBefore(nextpersonlease.getEndDate()) && 
						today.isAfter(nextpersonlease.getDateSigned())) {
				
					//adding the email to the active list, likely turned into presenting the object to the front end
					emails += nextperson.getEmailAddress()+"; ";
					
					//added count as a verification measure, knowing how many emails we expect to be on the list
					count++;
				}
			}
		}
		session.close();
		emf.close();
	}
	
	public String getemails(){
		return emails;
	}
	
	public int getcount(){
		return count;
	}
}*/


@RestController
@RequestMapping("/api")
public class EmailBlockBuilderDTO {
	//private final Logger log = LoggerFactory.getLogger(EmailBlockBuilderDTO.class);
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	
	private final PersonService personService;

    private final PersonQueryService personQueryService;
	
	public EmailBlockBuilderDTO(PersonService personService, PersonQueryService personQueryService) {
		this.personService = personService;
		this.personQueryService = personQueryService;
	}
	
	/**
     * {@code GET  /person/email} : get all the email for all people with active leases.
     *
     * @param date the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents paid in body.
     */
    @GetMapping("/person/email")
    public ResponseEntity<List<PersonDTO>> getEmails() {
        //log.debug("REST request to get Rents paid for date criteria: {}", date);
        
    	BooleanFilter notMinor = new BooleanFilter();
        notMinor.setEquals(false);
        
        StringFilter hasEmail = new StringFilter();
        hasEmail.setContains("@");
        
        
        List<Long> leaseIDList = ActiveLeaseResource.getActiveLeases().stream()
        		   .map( -> Objects.toString(object, null))
        		   .collect(Collectors.toList());
        
        LongFilter hasActiveLease = new LongFilter();
        hasActiveLease.setIn(leaseIDList);
        
        PersonCriteria criteria = new PersonCriteria();
        criteria.setIsMinor(notMinor);
        criteria.setEmailAddress(hasEmail);
        criteria.setLeaseId(hasActiveLease);
        
        List<PersonDTO> peoplewithemail = personQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(peoplewithemail);
    }

}