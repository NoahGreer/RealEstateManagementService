package realestatemanagementservice.web.rest;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.jhipster.service.filter.LocalDateFilter;
import realestatemanagementservice.service.LeaseQueryService;
import realestatemanagementservice.service.LeaseService;
import realestatemanagementservice.service.dto.LeaseCriteria;
import realestatemanagementservice.service.dto.LeaseDTO;

@RestController
@RequestMapping("/api")
public class ActiveLeaseResource {
	private final static Logger log = LoggerFactory.getLogger(ActiveLeaseResource.class);
	
	static LocalDate today = LocalDate.now();
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	
	private final LeaseService leaseService;

    private final LeaseQueryService leaseQueryService;
	
	public ActiveLeaseResource(LeaseService leaseService, LeaseQueryService leaseQueryService) {
		this.leaseService = leaseService;
		this.leaseQueryService = leaseQueryService;
	}
	
	/**
     * {@code GET  /leases/active} : get all the active leases, ie today is between date signed and move out date.
     *
     * @param date the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents paid in body.
     */
    @GetMapping("/leases/active")
    public ResponseEntity<List<LeaseDTO>> getActiveLeasesDTO() {
        log.debug("REST request to get active Leases");
        
        LocalDateFilter datesignedFilter = new LocalDateFilter();
        datesignedFilter.setGreaterThanOrEqual(today);
        
        LocalDateFilter endDateFilter = new LocalDateFilter();
        endDateFilter.setLessThan(today);
        
        LeaseCriteria criteria = new LeaseCriteria();
        criteria.setDateSigned(datesignedFilter);
        criteria.setEndDate(endDateFilter);
        
        List<LeaseDTO> activeLeases = leaseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(activeLeases);
    }
    /**
     * {@code GET  /leases/activeID} : get all the active lease IDs, ie today is between date signed and move out date.
     *
     * @param date the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents paid in body.
     */
    @GetMapping("/leases/activeID")
    @SuppressWarnings("null")
	public static ResponseEntity<List<Long>> getActiveLeasesID() {
        log.debug("REST request to get only the active Lease IDs");
        
        LocalDateFilter datesignedFilter = new LocalDateFilter();
        datesignedFilter.setGreaterThanOrEqual(today);
        
        LocalDateFilter endDateFilter = new LocalDateFilter();
        endDateFilter.setLessThan(today);
        
        LeaseCriteria criteria = new LeaseCriteria();
        criteria.setDateSigned(datesignedFilter);
        criteria.setEndDate(endDateFilter);
        
        List<LeaseDTO> activeLeasesDTO = leaseQueryService.findByCriteria(criteria);

        List<Long> activeLeases = null;
        for(LeaseDTO lease : activeLeasesDTO) {
        	activeLeases.add(lease.getId());
        }
        return ResponseEntity.ok().body(activeLeases);
    }
    
}

