package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.InfractionService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.InfractionDTO;
import realestatemanagementservice.service.dto.InfractionCriteria;
import realestatemanagementservice.service.InfractionQueryService;

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
 * REST controller for managing {@link realestatemanagementservice.domain.Infraction}.
 */
@RestController
@RequestMapping("/api")
public class InfractionResource {

    private final Logger log = LoggerFactory.getLogger(InfractionResource.class);

    private static final String ENTITY_NAME = "infraction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfractionService infractionService;

    private final InfractionQueryService infractionQueryService;

    public InfractionResource(InfractionService infractionService, InfractionQueryService infractionQueryService) {
        this.infractionService = infractionService;
        this.infractionQueryService = infractionQueryService;
    }

    /**
     * {@code POST  /infractions} : Create a new infraction.
     *
     * @param infractionDTO the infractionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infractionDTO, or with status {@code 400 (Bad Request)} if the infraction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/infractions")
    public ResponseEntity<InfractionDTO> createInfraction(@RequestBody InfractionDTO infractionDTO) throws URISyntaxException {
        log.debug("REST request to save Infraction : {}", infractionDTO);
        if (infractionDTO.getId() != null) {
            throw new BadRequestAlertException("A new infraction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InfractionDTO result = infractionService.save(infractionDTO);
        return ResponseEntity.created(new URI("/api/infractions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /infractions} : Updates an existing infraction.
     *
     * @param infractionDTO the infractionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infractionDTO,
     * or with status {@code 400 (Bad Request)} if the infractionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infractionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/infractions")
    public ResponseEntity<InfractionDTO> updateInfraction(@RequestBody InfractionDTO infractionDTO) throws URISyntaxException {
        log.debug("REST request to update Infraction : {}", infractionDTO);
        if (infractionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InfractionDTO result = infractionService.save(infractionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, infractionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /infractions} : get all the infractions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infractions in body.
     */
    @GetMapping("/infractions")
    public ResponseEntity<List<InfractionDTO>> getAllInfractions(InfractionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Infractions by criteria: {}", criteria);
        Page<InfractionDTO> page = infractionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /infractions/count} : count all the infractions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/infractions/count")
    public ResponseEntity<Long> countInfractions(InfractionCriteria criteria) {
        log.debug("REST request to count Infractions by criteria: {}", criteria);
        return ResponseEntity.ok().body(infractionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /infractions/:id} : get the "id" infraction.
     *
     * @param id the id of the infractionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infractionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/infractions/{id}")
    public ResponseEntity<InfractionDTO> getInfraction(@PathVariable Long id) {
        log.debug("REST request to get Infraction : {}", id);
        Optional<InfractionDTO> infractionDTO = infractionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infractionDTO);
    }

    /**
     * {@code DELETE  /infractions/:id} : delete the "id" infraction.
     *
     * @param id the id of the infractionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/infractions/{id}")
    public ResponseEntity<Void> deleteInfraction(@PathVariable Long id) {
        log.debug("REST request to delete Infraction : {}", id);
        infractionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
