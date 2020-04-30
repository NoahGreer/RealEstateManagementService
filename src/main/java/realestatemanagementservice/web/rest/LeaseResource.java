package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.LeaseService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.LeaseDTO;
import realestatemanagementservice.service.dto.LeaseCriteria;
import realestatemanagementservice.service.LeaseQueryService;

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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link realestatemanagementservice.domain.Lease}.
 */
@RestController
@RequestMapping("/api")
public class LeaseResource {

    private final Logger log = LoggerFactory.getLogger(LeaseResource.class);

    private static final String ENTITY_NAME = "lease";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseService leaseService;

    private final LeaseQueryService leaseQueryService;

    public LeaseResource(LeaseService leaseService, LeaseQueryService leaseQueryService) {
        this.leaseService = leaseService;
        this.leaseQueryService = leaseQueryService;
    }

    /**
     * {@code POST  /leases} : Create a new lease.
     *
     * @param leaseDTO the leaseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseDTO, or with status {@code 400 (Bad Request)} if the lease has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leases")
    public ResponseEntity<LeaseDTO> createLease(@RequestBody LeaseDTO leaseDTO) throws URISyntaxException {
        log.debug("REST request to save Lease : {}", leaseDTO);
        if (leaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new lease cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseDTO result = leaseService.save(leaseDTO);
        return ResponseEntity.created(new URI("/api/leases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leases} : Updates an existing lease.
     *
     * @param leaseDTO the leaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseDTO,
     * or with status {@code 400 (Bad Request)} if the leaseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leases")
    public ResponseEntity<LeaseDTO> updateLease(@RequestBody LeaseDTO leaseDTO) throws URISyntaxException {
        log.debug("REST request to update Lease : {}", leaseDTO);
        if (leaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeaseDTO result = leaseService.save(leaseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /leases} : get all the leases.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leases in body.
     */
    @GetMapping("/leases")
    public ResponseEntity<List<LeaseDTO>> getAllLeases(LeaseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Leases by criteria: {}", criteria);
        Page<LeaseDTO> page = leaseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leases/count} : count all the leases.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/leases/count")
    public ResponseEntity<Long> countLeases(LeaseCriteria criteria) {
        log.debug("REST request to count Leases by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /leases/:id} : get the "id" lease.
     *
     * @param id the id of the leaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leases/{id}")
    public ResponseEntity<LeaseDTO> getLease(@PathVariable Long id) {
        log.debug("REST request to get Lease : {}", id);
        Optional<LeaseDTO> leaseDTO = leaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseDTO);
    }

    /**
     * {@code DELETE  /leases/:id} : delete the "id" lease.
     *
     * @param id the id of the leaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leases/{id}")
    public ResponseEntity<Void> deleteLease(@PathVariable Long id) {
        log.debug("REST request to delete Lease : {}", id);
        leaseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
