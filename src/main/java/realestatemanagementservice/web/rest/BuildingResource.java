package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.BuildingService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.BuildingDTO;
import realestatemanagementservice.service.dto.BuildingCriteria;
import realestatemanagementservice.service.BuildingQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link realestatemanagementservice.domain.Building}.
 */
@RestController
@RequestMapping("/api")
public class BuildingResource {

    private final Logger log = LoggerFactory.getLogger(BuildingResource.class);

    private static final String ENTITY_NAME = "building";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BuildingService buildingService;

    private final BuildingQueryService buildingQueryService;

    public BuildingResource(BuildingService buildingService, BuildingQueryService buildingQueryService) {
        this.buildingService = buildingService;
        this.buildingQueryService = buildingQueryService;
    }

    /**
     * {@code POST  /buildings} : Create a new building.
     *
     * @param buildingDTO the buildingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new buildingDTO, or with status {@code 400 (Bad Request)} if the building has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/buildings")
    public ResponseEntity<BuildingDTO> createBuilding(@Valid @RequestBody BuildingDTO buildingDTO) throws URISyntaxException {
        log.debug("REST request to save Building : {}", buildingDTO);
        if (buildingDTO.getId() != null) {
            throw new BadRequestAlertException("A new building cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BuildingDTO result = buildingService.save(buildingDTO);
        return ResponseEntity.created(new URI("/api/buildings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /buildings} : Updates an existing building.
     *
     * @param buildingDTO the buildingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated buildingDTO,
     * or with status {@code 400 (Bad Request)} if the buildingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the buildingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/buildings")
    public ResponseEntity<BuildingDTO> updateBuilding(@Valid @RequestBody BuildingDTO buildingDTO) throws URISyntaxException {
        log.debug("REST request to update Building : {}", buildingDTO);
        if (buildingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BuildingDTO result = buildingService.save(buildingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, buildingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /buildings} : get all the buildings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of buildings in body.
     */
    @GetMapping("/buildings")
    public ResponseEntity<List<BuildingDTO>> getAllBuildings(BuildingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Buildings by criteria: {}", criteria);
        Page<BuildingDTO> page = buildingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /buildings/count} : count all the buildings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/buildings/count")
    public ResponseEntity<Long> countBuildings(BuildingCriteria criteria) {
        log.debug("REST request to count Buildings by criteria: {}", criteria);
        return ResponseEntity.ok().body(buildingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /buildings/:id} : get the "id" building.
     *
     * @param id the id of the buildingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the buildingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/buildings/{id}")
    public ResponseEntity<BuildingDTO> getBuilding(@PathVariable Long id) {
        log.debug("REST request to get Building : {}", id);
        Optional<BuildingDTO> buildingDTO = buildingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(buildingDTO);
    }

    /**
     * {@code DELETE  /buildings/:id} : delete the "id" building.
     *
     * @param id the id of the buildingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/buildings/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        log.debug("REST request to delete Building : {}", id);
        buildingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
