package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.ContractorService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.ContractorDTO;
import realestatemanagementservice.service.dto.ContractorCriteria;
import realestatemanagementservice.security.AuthoritiesConstants;
import realestatemanagementservice.service.ContractorQueryService;

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
 * REST controller for managing {@link realestatemanagementservice.domain.Contractor}.
 */
@RestController
@RequestMapping("/api")
public class ContractorResource {

    private final Logger log = LoggerFactory.getLogger(ContractorResource.class);

    private static final String ENTITY_NAME = "contractor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractorService contractorService;

    private final ContractorQueryService contractorQueryService;

    public ContractorResource(ContractorService contractorService, ContractorQueryService contractorQueryService) {
        this.contractorService = contractorService;
        this.contractorQueryService = contractorQueryService;
    }

    /**
     * {@code POST  /contractors} : Create a new contractor.
     *
     * @param contractorDTO the contractorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractorDTO, or with status {@code 400 (Bad Request)} if the contractor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contractors")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<ContractorDTO> createContractor(@Valid @RequestBody ContractorDTO contractorDTO) throws URISyntaxException {
        log.debug("REST request to save Contractor : {}", contractorDTO);
        if (contractorDTO.getId() != null) {
            throw new BadRequestAlertException("A new contractor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractorDTO result = contractorService.save(contractorDTO);
        return ResponseEntity.created(new URI("/api/contractors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contractors} : Updates an existing contractor.
     *
     * @param contractorDTO the contractorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractorDTO,
     * or with status {@code 400 (Bad Request)} if the contractorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contractors")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<ContractorDTO> updateContractor(@Valid @RequestBody ContractorDTO contractorDTO) throws URISyntaxException {
        log.debug("REST request to update Contractor : {}", contractorDTO);
        if (contractorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContractorDTO result = contractorService.save(contractorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contractors} : get all the contractors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractors in body.
     */
    @GetMapping("/contractors")
    public ResponseEntity<List<ContractorDTO>> getAllContractors(ContractorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Contractors by criteria: {}", criteria);
        Page<ContractorDTO> page = contractorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contractors/count} : count all the contractors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contractors/count")
    public ResponseEntity<Long> countContractors(ContractorCriteria criteria) {
        log.debug("REST request to count Contractors by criteria: {}", criteria);
        return ResponseEntity.ok().body(contractorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contractors/:id} : get the "id" contractor.
     *
     * @param id the id of the contractorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contractors/{id}")
    public ResponseEntity<ContractorDTO> getContractor(@PathVariable Long id) {
        log.debug("REST request to get Contractor : {}", id);
        Optional<ContractorDTO> contractorDTO = contractorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractorDTO);
    }

    /**
     * {@code DELETE  /contractors/:id} : delete the "id" contractor.
     *
     * @param id the id of the contractorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contractors/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<Void> deleteContractor(@PathVariable Long id) {
        log.debug("REST request to delete Contractor : {}", id);
        contractorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
