package realestatemanagementservice.web.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import realestatemanagementservice.service.*;
import realestatemanagementservice.service.dto.*;

@RestController
@RequestMapping("/api")
public class ReportResource {
	
	private final Logger log = LoggerFactory.getLogger(ReportResource.class);
	
	private final ApartmentQueryService apartmentQueryService;
	private final LeaseQueryService leaseQueryService;
	private final RentQueryService rentQueryService;
	private final VehicleQueryService vehicleQueryService;
	
	public ReportResource(ApartmentQueryService apartmentQueryService, 
			RentQueryService rentQueryService, 
			LeaseQueryService leaseQueryService, 
			VehicleQueryService vehicleQueryService) {
		this.apartmentQueryService = apartmentQueryService;
		this.leaseQueryService = leaseQueryService;
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
    	
    	LongFilter lf = new LongFilter();
    	lf.setEquals(id);
    	
    	ApartmentCriteria apartmentCriteria = new ApartmentCriteria();
    	apartmentCriteria.setBuildingId(lf);
    	List<ApartmentDTO> apartments = apartmentQueryService.findByCriteria(apartmentCriteria);
    	
    	List<Long> longs = new ArrayList<>();
    	for (ApartmentDTO apartment : apartments) {
    		longs.add(apartment.getId());
    	}
    	
    	lf.setIn(longs);
    	longs.clear();
    	
    	LeaseCriteria leaseCriteria = new LeaseCriteria();
    	leaseCriteria.setApartmentId(lf);
    	List<LeaseDTO> leases = leaseQueryService.findByCriteria(leaseCriteria);
    	
    	for (LeaseDTO lease : leases) {
    		longs.add(lease.getId());
    	}
    	
    	lf.setIn(longs);
    	
    	VehicleCriteria vehicleCriteria = new VehicleCriteria();
    	vehicleCriteria.setLeaseId(lf);
    	List<VehicleDTO> vehicles = vehicleQueryService.findByCriteria(vehicleCriteria);
    	
    	return ResponseEntity.ok().body(vehicles);
    }
}
