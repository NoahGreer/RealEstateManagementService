package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.PropertyTaxService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.PropertyTaxDTO;
import realestatemanagementservice.service.dto.PropertyTaxCriteria;
import realestatemanagementservice.security.AuthoritiesConstants;
import realestatemanagementservice.service.PropertyTaxQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link realestatemanagementservice.domain.PropertyTax}.
 */
@RestController
@RequestMapping("/api")
public class PropertyTaxResource {

    private final Logger log = LoggerFactory.getLogger(PropertyTaxResource.class);

    private static final String ENTITY_NAME = "propertyTax";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PropertyTaxService propertyTaxService;

    private final PropertyTaxQueryService propertyTaxQueryService;

    public PropertyTaxResource(PropertyTaxService propertyTaxService, PropertyTaxQueryService propertyTaxQueryService) {
        this.propertyTaxService = propertyTaxService;
        this.propertyTaxQueryService = propertyTaxQueryService;
    }

    /**
     * {@code POST  /property-taxes} : Create a new propertyTax.
     *
     * @param propertyTaxDTO the propertyTaxDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new propertyTaxDTO, or with status {@code 400 (Bad Request)} if the propertyTax has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/property-taxes")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<PropertyTaxDTO> createPropertyTax(@Valid @RequestBody PropertyTaxDTO propertyTaxDTO) throws URISyntaxException {
        log.debug("REST request to save PropertyTax : {}", propertyTaxDTO);
        if (propertyTaxDTO.getId() != null) {
            throw new BadRequestAlertException("A new propertyTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PropertyTaxDTO result = propertyTaxService.save(propertyTaxDTO);
        return ResponseEntity.created(new URI("/api/property-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /property-taxes} : Updates an existing propertyTax.
     *
     * @param propertyTaxDTO the propertyTaxDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyTaxDTO,
     * or with status {@code 400 (Bad Request)} if the propertyTaxDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the propertyTaxDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/property-taxes")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<PropertyTaxDTO> updatePropertyTax(@Valid @RequestBody PropertyTaxDTO propertyTaxDTO) throws URISyntaxException {
        log.debug("REST request to update PropertyTax : {}", propertyTaxDTO);
        if (propertyTaxDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PropertyTaxDTO result = propertyTaxService.save(propertyTaxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, propertyTaxDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /property-taxes} : get all the propertyTaxes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of propertyTaxes in body.
     */
    @GetMapping("/property-taxes")
    public ResponseEntity<List<PropertyTaxDTO>> getAllPropertyTaxes(PropertyTaxCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PropertyTaxes by criteria: {}", criteria);
        Page<PropertyTaxDTO> page = propertyTaxQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /property-taxes/count} : count all the propertyTaxes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/property-taxes/count")
    public ResponseEntity<Long> countPropertyTaxes(PropertyTaxCriteria criteria) {
        log.debug("REST request to count PropertyTaxes by criteria: {}", criteria);
        return ResponseEntity.ok().body(propertyTaxQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /property-taxes/:id} : get the "id" propertyTax.
     *
     * @param id the id of the propertyTaxDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the propertyTaxDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/property-taxes/{id}")
    public ResponseEntity<PropertyTaxDTO> getPropertyTax(@PathVariable Long id) {
        log.debug("REST request to get PropertyTax : {}", id);
        Optional<PropertyTaxDTO> propertyTaxDTO = propertyTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(propertyTaxDTO);
    }

    /**
     * {@code DELETE  /property-taxes/:id} : delete the "id" propertyTax.
     *
     * @param id the id of the propertyTaxDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/property-taxes/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<Void> deletePropertyTax(@PathVariable Long id) {
        log.debug("REST request to delete PropertyTax : {}", id);
        propertyTaxService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
