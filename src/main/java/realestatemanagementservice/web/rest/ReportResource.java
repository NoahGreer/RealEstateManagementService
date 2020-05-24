package realestatemanagementservice.web.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import realestatemanagementservice.service.*;
import realestatemanagementservice.service.dto.*;

/**
 * REST controller for managing {@link realestatemanagementservice.domain.Reports}.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {
	
	private final Logger log = LoggerFactory.getLogger(ReportResource.class);
	
	private final ApartmentQueryService apartmentQueryService;
	private final ApartmentService apartmentService;
	private final BuildingService buildingService;
	private final ContractorService contractorService;
	private final LeaseQueryService leaseQueryService;
	private final MaintenanceQueryService maintenanceQueryService;
	private final InfractionQueryService infractionQueryService;
	private final PersonQueryService personQueryService;
	private final PropertyTaxQueryService propertyTaxQueryService;
	private final RentQueryService rentQueryService;
	private final VehicleQueryService vehicleQueryService;
	
	public ReportResource(ApartmentQueryService apartmentQueryService, 
			ApartmentService apartmentService, 
			BuildingService buildingService,
			ContractorService contractorService,
			InfractionQueryService infractionQueryService,
			PersonQueryService personQueryService,
			PropertyTaxQueryService propertyTaxQueryService,
			RentQueryService rentQueryService, 
			LeaseQueryService leaseQueryService, 
			MaintenanceQueryService maintenanceQueryService,
			VehicleQueryService vehicleQueryService) {
		this.apartmentQueryService = apartmentQueryService;
		this.apartmentService = apartmentService;
		this.buildingService = buildingService;
		this.contractorService = contractorService;
		this.infractionQueryService = infractionQueryService;
		this.personQueryService = personQueryService;
		this.propertyTaxQueryService = propertyTaxQueryService;
		this.leaseQueryService = leaseQueryService;
		this.maintenanceQueryService = maintenanceQueryService;
		this.rentQueryService = rentQueryService;
		this.vehicleQueryService = vehicleQueryService;
	}
	
	/**
	 * {@code GET  /reports/rents/paid} : get all the rents paid through the specified date for that month.
     *
     * @param date the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents paid in body.
     */
	@GetMapping("/reports/rents/paid")
    public ResponseEntity<List<RentDTO>> getRentsPaid(@RequestParam("date") 
    		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.debug("REST request to get Rents paid for date criteria: {}", date);
        
        LocalDateFilter dueDateFilter = new LocalDateFilter();
        dueDateFilter.setEquals(date.withDayOfMonth(1));
        
        LocalDateFilter receivedDateFilter = new LocalDateFilter();
        receivedDateFilter.setLessThanOrEqual(date);
        
        RentCriteria criteria = new RentCriteria();
        criteria.setDueDate(dueDateFilter);
        criteria.setRecievedDate(receivedDateFilter);
        
        List<RentDTO> rents = rentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(rents);
    }
    
	/**
     * {@code GET  /reports/buildings/:id/vehicles/authorized} : get the authorized vehicles for the "id" building.
     *
     * @param id the id of the buildingDTO to retrieve authorized vehicles for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with list of authorized vehicles in body.
     */
    @GetMapping("/reports/buildings/{id}/vehicles/authorized")
    public ResponseEntity<List<VehicleDTO>> getBuildingAuthorizedVehicles(@PathVariable Long id) {
		log.debug("REST request to get authorized Vehicles for Building : {}", id);
    	
    	LongFilter buildingIdFilter = new LongFilter();
    	buildingIdFilter.setEquals(id);
    	
    	ApartmentCriteria apartmentCriteria = new ApartmentCriteria();
    	apartmentCriteria.setBuildingId(buildingIdFilter);
    	List<ApartmentDTO> apartments = apartmentQueryService.findByCriteria(apartmentCriteria);
    	
    	List<Long> apartmentIds = new ArrayList<>();
    	for (ApartmentDTO apartment : apartments) {
    		apartmentIds.add(apartment.getId());
    	}
    	
		LongFilter appartmentIdsFilter = new LongFilter();
		appartmentIdsFilter.setIn(apartmentIds);
		
		final LocalDate today = LocalDate.now();
		
		LocalDateFilter dateSignedFilter = new LocalDateFilter();
		dateSignedFilter.setLessThanOrEqual(today);
		
		LocalDateFilter endDateFilter = new LocalDateFilter();
		endDateFilter.setGreaterThan(today);
    	
    	LeaseCriteria leaseCriteria = new LeaseCriteria();
    	leaseCriteria.setApartmentId(appartmentIdsFilter);
    	leaseCriteria.setDateSigned(dateSignedFilter);
    	leaseCriteria.setEndDate(endDateFilter);
    	List<LeaseDTO> leases = leaseQueryService.findByCriteria(leaseCriteria);
    	
		List<Long> leaseIds = new ArrayList<>();
    	for (LeaseDTO lease : leases) {
    		leaseIds.add(lease.getId());
    	}
    	
		LongFilter leaseIdsFilter = new LongFilter();
		leaseIdsFilter.setIn(leaseIds);
    	
    	VehicleCriteria vehicleCriteria = new VehicleCriteria();
    	vehicleCriteria.setLeaseId(leaseIdsFilter);
    	List<VehicleDTO> vehicles = vehicleQueryService.findByCriteria(vehicleCriteria);
    	
    	return ResponseEntity.ok().body(vehicles);
    }
    
	/**
     * {@code GET  /reports/person/email} : get all the email for all people with active leases.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of all the email addresses of active residents.
     */
	@GetMapping("/reports/person/email")
    public ResponseEntity<List<String>> getEmails() {
        log.debug("REST request to get all the Emails of active people");
        
    	final BooleanFilter notMinor = new BooleanFilter();
        notMinor.setEquals(true);
        
        final StringFilter hasEmail = new StringFilter();
        hasEmail.setSpecified(true);
      
		final List<Long> activeLeaseIds = leaseQueryService.findActiveLeaseIds();
        
        final LongFilter hasActiveLease = new LongFilter();
        hasActiveLease.setIn(activeLeaseIds);
        
        final PersonCriteria criteria = new PersonCriteria();
        criteria.setIsMinor(notMinor);
        criteria.setEmailAddress(hasEmail);
        criteria.setLeaseId(hasActiveLease);
        
        final List<PersonDTO> peoplewithemail = personQueryService.findByCriteria(criteria);
        
        final List<String> activeEmails = new ArrayList<>();
        for (final PersonDTO peopleemail : peoplewithemail) {
        	activeEmails.add(peopleemail.getEmailAddress());
        }
        
        return ResponseEntity.ok().body(activeEmails);
	}
	
	/**
     * {@code GET  /reports/person/contact} : get all the contact information for all people with active leases.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of all the email addresses of active residents.
     */
	@GetMapping("/reports/person/contact")
    public ResponseEntity<List<PersonDTO>> getContact() {
        log.debug("REST request to get all the contact information of active people");
      
		final List<Long> activeLeaseIds = leaseQueryService.findActiveLeaseIds();
        
        final LongFilter hasActiveLease = new LongFilter();
        hasActiveLease.setIn(activeLeaseIds);
        
        final PersonCriteria criteria = new PersonCriteria();
        criteria.setLeaseId(hasActiveLease);
        
        final List<PersonDTO> activePeople = personQueryService.findByCriteria(criteria);
        
        return ResponseEntity.ok().body(activePeople);
	}
    
    /**
     * {@code GET  /reports/infractions/year/:id} : get all the infractions in a given year.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infractions in a given year.
     */
	@GetMapping("/reports/infractions/year/{id}")
    public ResponseEntity<List<InfractionDTO>> getInfractionsByYear(@RequestParam("year") @Min(1900) @Max(2100) int year) {
		log.debug("REST request to get Infraction for year criteria: {}", year);
    	
		final LocalDate startOfYear = LocalDate.of(year, 1, 1);
    	final LocalDate endOfYear = LocalDate.of(year, 12, 31);
    	
    	final LocalDateFilter dateFilter = new LocalDateFilter();
    	dateFilter.setGreaterThanOrEqual(startOfYear);
    	dateFilter.setLessThanOrEqual(endOfYear);
    	
    	final InfractionCriteria criteria = new InfractionCriteria();
    	criteria.setDateOccurred(dateFilter);
    	
		final List<InfractionDTO> infractionsThisYear = infractionQueryService.findByCriteria(criteria);
    	
    	return ResponseEntity.ok().body(infractionsThisYear);
    }
	
	/**
     * {@code GET  /reports/apartments/available} : get all the currently available apartments.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of available apartments.
     */
	@GetMapping("/reports/apartments/available")
    public ResponseEntity<List<AvailableApartmentDTO>> getAvailableApartments() {
		log.debug("REST request to get all currently available apartments");
  
    	final BooleanFilter moveInReady = new BooleanFilter();
    	moveInReady.setEquals(true);
    	
    	final ApartmentCriteria criteria = new ApartmentCriteria();
    	criteria.setMoveInReady(moveInReady);
    	
		final List<ApartmentDTO> moveInReadyApartments = apartmentQueryService.findByCriteria(criteria);
		
		final List<AvailableApartmentDTO> availableApartments = new ArryList<>();
		
		for(ApartmentDTO apartments : moveInReadyApartments) {
			AvailableApartmentDTO apartment = new AvailableApartmentDTO(apartments);
			availableApartments.add(apartment);
		}
    	
    	return ResponseEntity.ok().body(availableApartments);
    }
	
	/**
     * {@code GET  /reports/maintenance/open} : get all the open maintenance orders.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of open maintenance orders.
     */
	@GetMapping("/reports/maintenance/open")
    public ResponseEntity<List<MaintenanceDTO>> getOpenMaintenace() {
		log.debug("REST request to get a list of all maintenance entities that do not show the the contractor has recieved payment for work done");
  
    	final StringFilter receiptOfPayment = new StringFilter();
    	receiptOfPayment.setSpecified(true);
    	
    	final MaintenanceCriteria criteria = new MaintenanceCriteria();
    	criteria.setReceiptOfPayment(receiptOfPayment);
    	
		final List<MaintenanceDTO> maintenanceProcessIncomplete = maintenanceQueryService.findByCriteria(criteria);
    	
    	return ResponseEntity.ok().body(maintenanceProcessIncomplete);
    }

	/**
     * {@code GET  /reports/contractors/:id/maintenance/history} : get all work done by a particular contractor.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of all work done by a particular contractor.
     */
	@GetMapping("/reports/contractors/{id}/maintenance/history")
    public ResponseEntity<List<MaintenanceDTO>> getMaintenaceByContractor(@RequestParam("id") Long id) {
		log.debug("REST request to get a list of all work done by one contractor for id criteria: {}", id);
  
    	final LongFilter idFilter = new LongFilter();
    	idFilter.setEquals(id);
    	
    	final MaintenanceCriteria criteria = new MaintenanceCriteria();
    	criteria.setContractorId(idFilter);
    	
		final List<MaintenanceDTO> maintenance = maintenanceQueryService.findByCriteria(criteria);
    	
    	return ResponseEntity.ok().body(maintenance);
    }

	/**
     * {@code GET  /reports/pet/owner} : get all pets and their owner's.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list all pets and their owner's.
     */
	@GetMapping("/reports/pet/owner")
	public ResponseEntity<List<String>> getPetsAndOwners() {
		log.debug("REST request to get a list of all pets and their owner's");

		final List<Long> activeLeaseIds = leaseQueryService.findActiveLeaseIds();

		final LongFilter hasActiveLease = new LongFilter();
		hasActiveLease.setIn(activeLeaseIds);

		final LongFilter hasPetsOnLease = new LongFilter();
		hasPetsOnLease.setSpecified(true);

		LeaseCriteria leaseCriteria = new LeaseCriteria();
		leaseCriteria.setId(hasActiveLease);
		leaseCriteria.setPetId(hasPetsOnLease);
		List<LeaseDTO> leases = leaseQueryService.findByCriteria(leaseCriteria);

		final BooleanFilter primaryContact = new BooleanFilter();
		primaryContact.setEquals(true);
    	
    	final PersonCriteria personCriteria = new PersonCriteria();
        personCriteria.setPrimaryContact(primaryContact);
        personCriteria.setLeaseId(hasActiveLease);
        
        final List<PersonDTO> primaryActivePeople = personQueryService.findByCriteria(personCriteria);
    	
        final List<String> activePetsAndPeople = new ArrayList<>();
        for (final LeaseDTO lease : leases) {
    		for (final PersonDTO person : primaryActivePeople) {
    			if (lease.getPeople().contains(person)) {
		        	activePetsAndPeople.add(person);
		        	for (final PetDTO pet : lease.getPets()) {
		        		activePetsAndPeople.add(pet.toString());	
		        	}
	        	}
    		}
        }
    	
    	return ResponseEntity.ok().body(activePetsAndPeople);
    }

	/**
     * {@code GET  /reports/tax/property} : get the full property tax history.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list the full property tax history.
     */
	@GetMapping("/reports/tax/property")
    public ResponseEntity<List<String>> getTaxHistory() {
		log.debug("REST request to get a list of the full property tax historyr");
		
		final List<PropertyTaxDTO> propertyTax = propertyTaxQueryService.findByCriteria(null);
		
		final List<String> taxHistory = new ArrayList<>();
		
		for (final PropertyTaxDTO history : propertyTax) {
			
			Optional<BuildingDTO> govTaxID = buildingService.findOne(history.getBuildingId());
			
			taxHistory.add(history.toString()+" "+govTaxID.get().toString());
        }
    	
    	return ResponseEntity.ok().body(taxHistory);
    }
	
    @GetMapping("/reports/apartments/{id}/maintenance/history")
    public ResponseEntity<List<MaintenanceDTO>> getApartmentMaintenanceHistory(@PathVariable Long id) {
    	log.debug("REST request to get Apartment Maintenance history for id criteria: {}", id);
    	
    	LongFilter apartmentIdFilter = new LongFilter();
    	apartmentIdFilter.setEquals(id);
    	
    	MaintenanceCriteria maintenanceCriteria = new MaintenanceCriteria();
    	maintenanceCriteria.setApartmentId(apartmentIdFilter);
    	
    	List<MaintenanceDTO> repairs = maintenanceQueryService.findByCriteria(maintenanceCriteria);
    	
    	return ResponseEntity.ok().body(repairs);
    }
    
    @GetMapping("/reports/apartments/{id}/tenants")
    public ResponseEntity<List<PersonDTO>> getApartmentTenants(@PathVariable Long id) {
    	log.debug("REST request to get Apartment tenants for id criteria: {}", id);
    	
    	LongFilter apartmentIdFilter = new LongFilter();
    	apartmentIdFilter.setEquals(id);
    	
    	final LocalDate today = LocalDate.now();
    	
    	LocalDateFilter dateSignedFilter = new LocalDateFilter();
    	dateSignedFilter.setLessThanOrEqual(today);
    	
    	LocalDateFilter endDateFilter = new LocalDateFilter();
    	endDateFilter.setGreaterThanOrEqual(today);
    	
    	LeaseCriteria leaseCriteria = new LeaseCriteria();
    	leaseCriteria.setApartmentId(apartmentIdFilter);
    	leaseCriteria.setDateSigned(dateSignedFilter);
    	leaseCriteria.setEndDate(endDateFilter);
    	List<LeaseDTO> leases = leaseQueryService.findByCriteria(leaseCriteria);
    	
    	List<Long> leaseIds = new ArrayList<>();
    	for (LeaseDTO lease : leases) {
    		leaseIds.add(lease.getId());
    	}
    	
		LongFilter leaseIdsFilter = new LongFilter();
		leaseIdsFilter.setIn(leaseIds);
		
    	PersonCriteria personCriteria = new PersonCriteria();
    	personCriteria.setLeaseId(leaseIdsFilter);
    	List<PersonDTO> people = personQueryService.findByCriteria(personCriteria);
    	
    	return ResponseEntity.ok().body(people);
    }
}
