package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.RentService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.RentDTO;
import realestatemanagementservice.service.dto.RentCriteria;
import realestatemanagementservice.security.AuthoritiesConstants;
import realestatemanagementservice.service.RentQueryService;

import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link realestatemanagementservice.domain.Rent}.
 */
@RestController
@RequestMapping("/api")
public class RentResource {

    private final Logger log = LoggerFactory.getLogger(RentResource.class);

    private static final String ENTITY_NAME = "rent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentService rentService;

    private final RentQueryService rentQueryService;

    public RentResource(RentService rentService, RentQueryService rentQueryService) {
        this.rentService = rentService;
        this.rentQueryService = rentQueryService;
    }

    /**
     * {@code POST  /rents} : Create a new rent.
     *
     * @param rentDTO the rentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rentDTO, or with status {@code 400 (Bad Request)} if the rent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rents")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<RentDTO> createRent(@RequestBody RentDTO rentDTO) throws URISyntaxException {
        log.debug("REST request to save Rent : {}", rentDTO);
        if (rentDTO.getId() != null) {
            throw new BadRequestAlertException("A new rent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RentDTO result = rentService.save(rentDTO);
        return ResponseEntity.created(new URI("/api/rents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rents} : Updates an existing rent.
     *
     * @param rentDTO the rentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentDTO,
     * or with status {@code 400 (Bad Request)} if the rentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rents")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<RentDTO> updateRent(@RequestBody RentDTO rentDTO) throws URISyntaxException {
        log.debug("REST request to update Rent : {}", rentDTO);
        if (rentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RentDTO result = rentService.save(rentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rents} : get all the rents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents in body.
     */
    @GetMapping("/rentsTEST")
    public ResponseEntity<List<RentDTO>> getAllRentsTEST(RentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Rents by criteria: {}", criteria);
        Page<RentDTO> page = rentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * {@code GET  /rents} : get all the rents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents in body.
     */
    @GetMapping("/rents")
    public ResponseEntity<List<RentDTO>> getAllRents(RentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Rents by criteria: {}", criteria);
        Page<RentDTO> page = rentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rents/count} : count all the rents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rents/count")
    public ResponseEntity<Long> countRents(RentCriteria criteria) {
        log.debug("REST request to count Rents by criteria: {}", criteria);
        return ResponseEntity.ok().body(rentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rents/:id} : get the "id" rent.
     *
     * @param id the id of the rentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rents/{id}")
    public ResponseEntity<RentDTO> getRent(@PathVariable Long id) {
        log.debug("REST request to get Rent : {}", id);
        Optional<RentDTO> rentDTO = rentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rentDTO);
    }

    /**
     * {@code DELETE  /rents/:id} : delete the "id" rent.
     *
     * @param id the id of the rentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rents/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<Void> deleteRent(@PathVariable Long id) {
        log.debug("REST request to delete Rent : {}", id);
        rentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
