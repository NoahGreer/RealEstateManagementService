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
	private final Logger log = LoggerFactory.getLogger(ActiveLeaseResource.class);
	
	LocalDate today = LocalDate.now();
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	
	private LeaseService leaseService;

    private LeaseQueryService leaseQueryService;
	
	public void ActvieLeaseResource(LeaseService leaseService, LeaseQueryService leaseQueryService) {
		this.leaseService = leaseService;
		this.leaseQueryService = leaseQueryService;
	}
	
	/**
     * {@code GET  /rents/paid} : get all the rents paid through the specified date for that month.
     *
     * @param date the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents paid in body.
     */
    @GetMapping("/rents/paid")
    public ResponseEntity<List<LeaseDTO>> getActiveLeases() {
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

}

