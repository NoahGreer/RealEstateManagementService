package realestatemanagementservice.web.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	private final BuildingQueryService buildingQueryService;
	private final BuildingService buildingService;
	private final ContractorService contractorService;
	private final LeaseQueryService leaseQueryService;
	private final MaintenanceQueryService maintenanceQueryService;
	private final InfractionQueryService infractionQueryService;
	private final PersonQueryService personQueryService;
	private final PropertyTaxQueryService propertyTaxQueryService;
	private final RentQueryService rentQueryService;
	private final VehicleQueryService vehicleQueryService;
	private final ContractorQueryService contractorQueryService;;
		
	public ReportResource(ApartmentQueryService apartmentQueryService, 
			ApartmentService apartmentService, 
			BuildingQueryService buildingQueryService, 
			BuildingService buildingService,
			ContractorService contractorService,
			InfractionQueryService infractionQueryService,
			PersonQueryService personQueryService,
			PropertyTaxQueryService propertyTaxQueryService,
			RentQueryService rentQueryService, 
			LeaseQueryService leaseQueryService, 
			MaintenanceQueryService maintenanceQueryService,
			VehicleQueryService vehicleQueryService,
			ContractorQueryService contractorQueryService) {
		this.apartmentQueryService = apartmentQueryService;
		this.apartmentService = apartmentService;
		this.buildingQueryService = buildingQueryService;
		this.buildingService = buildingService;
		this.contractorService = contractorService;
		this.infractionQueryService = infractionQueryService;
		this.personQueryService = personQueryService;
		this.propertyTaxQueryService = propertyTaxQueryService;
		this.leaseQueryService = leaseQueryService;
		this.maintenanceQueryService = maintenanceQueryService;
		this.rentQueryService = rentQueryService;
		this.vehicleQueryService = vehicleQueryService;
		this.contractorQueryService = contractorQueryService;
	}
	
    /**
     * {@code GET  /reportTest} : get all the rents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of report fields in body.
     */
    @GetMapping("/reports/reportTest")
    public ResponseEntity<HashMap<Integer, String>> getTestReport() {
    	
    	HashMap<Integer, String> testReport = new HashMap<>();
    	testReport.put(0, "This");
    	testReport.put(1, "is");
    	testReport.put(2, "a");
    	testReport.put(3, "Test");
    	
        return ResponseEntity.ok().body(testReport);
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
    	
		LongFilter apartmentIdsFilter = new LongFilter();
		apartmentIdsFilter.setIn(apartmentIds);
		
		final LocalDate today = LocalDate.now();
		
		LocalDateFilter dateSignedFilter = new LocalDateFilter();
		dateSignedFilter.setLessThanOrEqual(today);
		
		LocalDateFilter endDateFilter = new LocalDateFilter();
		endDateFilter.setGreaterThan(today);
    	
    	LeaseCriteria leaseCriteria = new LeaseCriteria();
    	leaseCriteria.setApartmentId(apartmentIdsFilter);
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
     * {@code GET  /reports/people/email} : get all the email for all people with active leases.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of all the email addresses of active residents.
     */
	@GetMapping("/reports/people/email")
    public ResponseEntity<List<EmailDTO>> getEmails() {
        log.debug("REST request to get all the Emails of active people");
        
    	final BooleanFilter notMinor = new BooleanFilter();
        notMinor.setEquals(false);
        
        final StringFilter hasEmail = new StringFilter();
        hasEmail.setSpecified(true);
      
        final LocalDate today = LocalDate.now();
		
		LocalDateFilter dateSignedFilter = new LocalDateFilter();
		dateSignedFilter.setLessThanOrEqual(today);
		
		LocalDateFilter endDateFilter = new LocalDateFilter();
		endDateFilter.setGreaterThan(today);
    	
    	LeaseCriteria leaseCriteria = new LeaseCriteria();
    	leaseCriteria.setDateSigned(dateSignedFilter);
    	leaseCriteria.setEndDate(endDateFilter);
    	List<LeaseDTO> leases = leaseQueryService.findByCriteria(leaseCriteria);
    	
		List<Long> leaseIds = new ArrayList<>();
    	for (LeaseDTO lease : leases) {
    		leaseIds.add(lease.getId());
    	}
        
        final LongFilter hasActiveLease = new LongFilter();
        hasActiveLease.setIn(leaseIds);
        
        final PersonCriteria criteria = new PersonCriteria();
        criteria.setIsMinor(notMinor);
        criteria.setEmailAddress(hasEmail);
        criteria.setLeaseId(hasActiveLease);
        
        final List<PersonDTO> peopleWithEmail = personQueryService.findByCriteria(criteria);
        
        final List<EmailDTO> activeEmails = new ArrayList<>();
        for (final PersonDTO personWithEmail : peopleWithEmail) {
        	EmailDTO email = new EmailDTO(personWithEmail);
        	activeEmails.add(email);
        }
        
        return ResponseEntity.ok().body(activeEmails);
	}
	
	/**
     * {@code GET  /reports/people/contact} : get all the contact information for all people with active leases.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of all the email addresses of active residents.
     */
	@GetMapping("/reports/people/contact")
    public ResponseEntity<List<ContactDTO>> getContact() {
        log.debug("REST request to get all the contact information of active people");
      
		final List<Long> activeLeaseIds = leaseQueryService.findActiveLeaseIds();
        
        final LongFilter hasActiveLease = new LongFilter();
        hasActiveLease.setIn(activeLeaseIds);
        
        final PersonCriteria criteria = new PersonCriteria();
        criteria.setLeaseId(hasActiveLease);
        
        final List<PersonDTO> activePeople = personQueryService.findByCriteria(criteria);
        
        final List<ContactDTO> activeContacts = new ArrayList<>();
        for (final PersonDTO contact : activePeople) {
        	ContactDTO info = new ContactDTO(contact);
        	activeContacts.add(info);
        }
        
        return ResponseEntity.ok().body(activeContacts);
	}
    
    /**
     * {@code GET  /reports/infractions} : get all the infractions in a given year.
     * @param year the year which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infractions in a given year.
     */
	@GetMapping("/reports/infractions")
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
		
		final List<Long> moveInReadyApartmentsBuildingIds = new ArrayList<>();
		for (final ApartmentDTO moveInReadyApartment : moveInReadyApartments) {
			moveInReadyApartmentsBuildingIds.add(moveInReadyApartment.getBuildingId());
		}
		
		final LongFilter moveInReadyApartmentBuildingIdsFilter = new LongFilter();
		moveInReadyApartmentBuildingIdsFilter.setIn(moveInReadyApartmentsBuildingIds);
		
		final BuildingCriteria moveInReadyApartmentBuildingCriteria = new BuildingCriteria();
		moveInReadyApartmentBuildingCriteria.setId(moveInReadyApartmentBuildingIdsFilter);
		
		final List<BuildingDTO> moveInReadyApartmentBuildings = buildingQueryService.findByCriteria(moveInReadyApartmentBuildingCriteria);
		
		final Map<Long, BuildingDTO> moveInReadyApartmentBuildingIdToBuildingDTO = new HashMap<>();
		for (final BuildingDTO moveInReadyApartmentBuilding : moveInReadyApartmentBuildings) {
			moveInReadyApartmentBuildingIdToBuildingDTO.put(moveInReadyApartmentBuilding.getId(), moveInReadyApartmentBuilding);
		}
		
		final List<AvailableApartmentDTO> availableApartments = new ArrayList<>();
		for (final ApartmentDTO moveInReadyApartment : moveInReadyApartments) {
			BuildingDTO moveInReadyApartmentBuilding = moveInReadyApartmentBuildingIdToBuildingDTO.get(moveInReadyApartment.getBuildingId());
			if (Objects.nonNull(moveInReadyApartmentBuilding)) {
				AvailableApartmentDTO apartment = new AvailableApartmentDTO(moveInReadyApartment, moveInReadyApartmentBuilding);
				availableApartments.add(apartment);
			}
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
    public ResponseEntity<List<MaintenanceDTO>> getMaintenaceByContractor(@PathVariable("id") Long id) {
		log.debug("REST request to get a list of all work done by one contractor for id criteria: {}", id);
  
    	final LongFilter idFilter = new LongFilter();
    	idFilter.setEquals(id);
    	
    	final MaintenanceCriteria criteria = new MaintenanceCriteria();
    	criteria.setContractorId(idFilter);
    	
		final List<MaintenanceDTO> maintenance = maintenanceQueryService.findByCriteria(criteria);
    	
    	return ResponseEntity.ok().body(maintenance);
    }

	/**
     * {@code GET  /reports/pet/owner} : get all pets and their owners.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list all pets and their owners.
     */
	@GetMapping("/reports/pet/owner")
	public ResponseEntity<List<PetOwnerDTO>> getPetsAndOwners() {
		log.debug("REST request to get a list of all pets and their owners");

		final LocalDate today = LocalDate.now();
		
		LocalDateFilter dateSignedFilter = new LocalDateFilter();
		dateSignedFilter.setLessThanOrEqual(today);
		
		LocalDateFilter endDateFilter = new LocalDateFilter();
		endDateFilter.setGreaterThan(today);
		
		final LongFilter hasPetsOnLease = new LongFilter();
		hasPetsOnLease.setSpecified(true);
    	
    	LeaseCriteria leaseCriteria = new LeaseCriteria();
    	leaseCriteria.setDateSigned(dateSignedFilter);
    	leaseCriteria.setEndDate(endDateFilter);
		leaseCriteria.setPetId(hasPetsOnLease);
		// Wrap in a hash set to guarantee unique lease entries
		Set<LeaseDTO> leases = new HashSet<>(leaseQueryService.findByCriteria(leaseCriteria));
		
		List<Long> leaseIds = new ArrayList<>();
    	for (LeaseDTO lease : leases) {
    		leaseIds.add(lease.getId());
    	}
    	
    	final LongFilter peopleWithActiveLeasesAndPets = new LongFilter();
    	peopleWithActiveLeasesAndPets.setIn(leaseIds);

		final BooleanFilter primaryContact = new BooleanFilter();
		primaryContact.setEquals(true);
    	
    	final PersonCriteria personCriteria = new PersonCriteria();
        personCriteria.setPrimaryContact(primaryContact);
        personCriteria.setLeaseId(peopleWithActiveLeasesAndPets);
        
        final List<PersonDTO> primaryActivePeople = personQueryService.findByCriteria(personCriteria);
    	
        final List<PetOwnerDTO> activePetsAndPeople = new ArrayList<>();
        for (final LeaseDTO lease : leases) {
    		for (final PersonDTO person : primaryActivePeople) {
    			if (lease.getPeople().contains(person)) {
		        	for (final PetDTO pet : lease.getPets()) {
		        		PetOwnerDTO PO = new PetOwnerDTO(person,pet);
		        		activePetsAndPeople.add(PO);	
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
    public ResponseEntity<List<PropertyTaxWithPropertyNumberDTO>> getTaxHistory() {
		log.debug("REST request to get a list of the full property tax historyr");
		
		final LongFilter hasIds = new LongFilter();
		hasIds.setSpecified(true);

    	final PropertyTaxCriteria propertyTaxCriteria = new PropertyTaxCriteria();
    	propertyTaxCriteria.setId(hasIds);
		
		final List<PropertyTaxDTO> propertyTax = propertyTaxQueryService.findByCriteria(propertyTaxCriteria);
		
		final BuildingCriteria buildingCriteria = new BuildingCriteria();
		buildingCriteria.setId(hasIds);
		
		final List<BuildingDTO> propertyTaxBuildings = buildingQueryService.findByCriteria(buildingCriteria);
		
		final List<PropertyTaxWithPropertyNumberDTO> taxHistory = new ArrayList<>();
		for (final PropertyTaxDTO history : propertyTax) {
			for(final BuildingDTO building : propertyTaxBuildings) {
				if(building.getId()==history.getBuildingId()) {
					PropertyTaxWithPropertyNumberDTO fullTax = new PropertyTaxWithPropertyNumberDTO(history, building);
					taxHistory.add(fullTax);
				}
			}
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
    
    /**
     * {@code GET  /reports/lease/expire} : get the next number of leases to expire.
     * @param year the count which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infractions in a given year.
     */
	@GetMapping("/reports/lease/expire")
    public ResponseEntity<List<LeaseDTO>> getLeasesNextToExpire(@RequestParam("count") @Min(1) @Max(50) int count) {
		log.debug("REST request to get the next leases to expire by criteria: {}", count);
    	
		final LocalDate today = LocalDate.now();
		
		LocalDateFilter dateSignedFilter = new LocalDateFilter();
		dateSignedFilter.setLessThanOrEqual(today);
		
		LocalDateFilter endDateFilter = new LocalDateFilter();
		endDateFilter.setGreaterThanOrEqual(today);
    	
    	LeaseCriteria leaseCriteria = new LeaseCriteria();
    	leaseCriteria.setDateSigned(dateSignedFilter);
    	leaseCriteria.setEndDate(endDateFilter);
	
		List<LeaseDTO> leases = leaseQueryService.findByCriteria(leaseCriteria);
		
		leases.sort(Comparator.comparing(LeaseDTO::getEndDate));
		
		List<LeaseDTO> orderedLeases = new ArrayList<LeaseDTO>(leases.subList(0, count));

		return ResponseEntity.ok().body(orderedLeases);
    }
	
	/**
     * {@code GET  /reports/contractor/jobtype} : get the next number of leases to expire.
     * @param year the count which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infractions in a given year.
     */
	@GetMapping("/reports/contractor/jobtype")
    public ResponseEntity<List<ContractorDTO>> getContractorByJobType(@RequestParam("id") Long id) {
		log.debug("REST request to get the next leases to expire by criteria: {}", id);
		
		LongFilter jobType = new LongFilter();
		jobType.setEquals(id);
    	
    	ContractorCriteria contractorCriteria = new ContractorCriteria();
    	contractorCriteria.setJobTypeId(jobType);
	
		List<ContractorDTO> contractors = contractorQueryService.findByCriteria(contractorCriteria);

		return ResponseEntity.ok().body(contractors);
    }
}
