package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.MaintenanceService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.MaintenanceDTO;
import realestatemanagementservice.service.dto.MaintenanceCriteria;
import realestatemanagementservice.service.MaintenanceQueryService;

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
 * REST controller for managing {@link realestatemanagementservice.domain.Maintenance}.
 */
@RestController
@RequestMapping("/api")
public class MaintenanceResource {

    private final Logger log = LoggerFactory.getLogger(MaintenanceResource.class);

    private static final String ENTITY_NAME = "maintenance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaintenanceService maintenanceService;

    private final MaintenanceQueryService maintenanceQueryService;

    public MaintenanceResource(MaintenanceService maintenanceService, MaintenanceQueryService maintenanceQueryService) {
        this.maintenanceService = maintenanceService;
        this.maintenanceQueryService = maintenanceQueryService;
    }

    /**
     * {@code POST  /maintenances} : Create a new maintenance.
     *
     * @param maintenanceDTO the maintenanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maintenanceDTO, or with status {@code 400 (Bad Request)} if the maintenance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/maintenances")
    public ResponseEntity<MaintenanceDTO> createMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) throws URISyntaxException {
        log.debug("REST request to save Maintenance : {}", maintenanceDTO);
        if (maintenanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new maintenance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaintenanceDTO result = maintenanceService.save(maintenanceDTO);
        return ResponseEntity.created(new URI("/api/maintenances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /maintenances} : Updates an existing maintenance.
     *
     * @param maintenanceDTO the maintenanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintenanceDTO,
     * or with status {@code 400 (Bad Request)} if the maintenanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maintenanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/maintenances")
    public ResponseEntity<MaintenanceDTO> updateMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) throws URISyntaxException {
        log.debug("REST request to update Maintenance : {}", maintenanceDTO);
        if (maintenanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaintenanceDTO result = maintenanceService.save(maintenanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maintenanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /maintenances} : get all the maintenances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maintenances in body.
     */
    @GetMapping("/maintenances")
    public ResponseEntity<List<MaintenanceDTO>> getAllMaintenances(MaintenanceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Maintenances by criteria: {}", criteria);
        Page<MaintenanceDTO> page = maintenanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /maintenances/count} : count all the maintenances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/maintenances/count")
    public ResponseEntity<Long> countMaintenances(MaintenanceCriteria criteria) {
        log.debug("REST request to count Maintenances by criteria: {}", criteria);
        return ResponseEntity.ok().body(maintenanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /maintenances/:id} : get the "id" maintenance.
     *
     * @param id the id of the maintenanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maintenanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maintenances/{id}")
    public ResponseEntity<MaintenanceDTO> getMaintenance(@PathVariable Long id) {
        log.debug("REST request to get Maintenance : {}", id);
        Optional<MaintenanceDTO> maintenanceDTO = maintenanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maintenanceDTO);
    }

    /**
     * {@code DELETE  /maintenances/:id} : delete the "id" maintenance.
     *
     * @param id the id of the maintenanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maintenances/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        log.debug("REST request to delete Maintenance : {}", id);
        maintenanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
