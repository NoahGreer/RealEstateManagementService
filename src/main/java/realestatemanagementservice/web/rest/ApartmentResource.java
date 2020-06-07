package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.ApartmentService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.ApartmentDTO;
import realestatemanagementservice.service.dto.ApartmentCriteria;
import realestatemanagementservice.security.AuthoritiesConstants;
import realestatemanagementservice.service.ApartmentQueryService;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link realestatemanagementservice.domain.Apartment}.
 */
@RestController
@RequestMapping("/api")
public class ApartmentResource {

    private final Logger log = LoggerFactory.getLogger(ApartmentResource.class);

    private static final String ENTITY_NAME = "apartment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApartmentService apartmentService;

    private final ApartmentQueryService apartmentQueryService;

    public ApartmentResource(ApartmentService apartmentService, ApartmentQueryService apartmentQueryService) {
        this.apartmentService = apartmentService;
        this.apartmentQueryService = apartmentQueryService;
    }

    /**
     * {@code POST  /apartments} : Create a new apartment.
     *
     * @param apartmentDTO the apartmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apartmentDTO, or with status {@code 400 (Bad Request)} if the apartment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apartments")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<ApartmentDTO> createApartment(@RequestBody ApartmentDTO apartmentDTO) throws URISyntaxException {
        log.debug("REST request to save Apartment : {}", apartmentDTO);
        if (apartmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new apartment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApartmentDTO result = apartmentService.save(apartmentDTO);
        return ResponseEntity.created(new URI("/api/apartments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apartments} : Updates an existing apartment.
     *
     * @param apartmentDTO the apartmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apartmentDTO,
     * or with status {@code 400 (Bad Request)} if the apartmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apartmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apartments")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<ApartmentDTO> updateApartment(@RequestBody ApartmentDTO apartmentDTO) throws URISyntaxException {
        log.debug("REST request to update Apartment : {}", apartmentDTO);
        if (apartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApartmentDTO result = apartmentService.save(apartmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apartmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /apartments} : get all the apartments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apartments in body.
     */
    @GetMapping("/apartments")
    public ResponseEntity<List<ApartmentDTO>> getAllApartments(ApartmentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Apartments by criteria: {}", criteria);
        Page<ApartmentDTO> page = apartmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apartments/count} : count all the apartments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apartments/count")
    public ResponseEntity<Long> countApartments(ApartmentCriteria criteria) {
        log.debug("REST request to count Apartments by criteria: {}", criteria);
        return ResponseEntity.ok().body(apartmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apartments/:id} : get the "id" apartment.
     *
     * @param id the id of the apartmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apartmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apartments/{id}")
    public ResponseEntity<ApartmentDTO> getApartment(@PathVariable Long id) {
        log.debug("REST request to get Apartment : {}", id);
        Optional<ApartmentDTO> apartmentDTO = apartmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apartmentDTO);
    }

    /**
     * {@code DELETE  /apartments/:id} : delete the "id" apartment.
     *
     * @param id the id of the apartmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apartments/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        log.debug("REST request to delete Apartment : {}", id);
        apartmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
