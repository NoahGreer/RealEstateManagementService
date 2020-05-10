package realestatemanagementservice.web.rest;

import realestatemanagementservice.service.JobTypeService;
import realestatemanagementservice.web.rest.errors.BadRequestAlertException;
import realestatemanagementservice.service.dto.JobTypeDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link realestatemanagementservice.domain.JobType}.
 */
@RestController
@RequestMapping("/api")
public class JobTypeResource {

    private final Logger log = LoggerFactory.getLogger(JobTypeResource.class);

    private static final String ENTITY_NAME = "jobType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobTypeService jobTypeService;

    public JobTypeResource(JobTypeService jobTypeService) {
        this.jobTypeService = jobTypeService;
    }

    /**
     * {@code POST  /job-types} : Create a new jobType.
     *
     * @param jobTypeDTO the jobTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobTypeDTO, or with status {@code 400 (Bad Request)} if the jobType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-types")
    public ResponseEntity<JobTypeDTO> createJobType(@RequestBody JobTypeDTO jobTypeDTO) throws URISyntaxException {
        log.debug("REST request to save JobType : {}", jobTypeDTO);
        if (jobTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobTypeDTO result = jobTypeService.save(jobTypeDTO);
        return ResponseEntity.created(new URI("/api/job-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-types} : Updates an existing jobType.
     *
     * @param jobTypeDTO the jobTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobTypeDTO,
     * or with status {@code 400 (Bad Request)} if the jobTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-types")
    public ResponseEntity<JobTypeDTO> updateJobType(@RequestBody JobTypeDTO jobTypeDTO) throws URISyntaxException {
        log.debug("REST request to update JobType : {}", jobTypeDTO);
        if (jobTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobTypeDTO result = jobTypeService.save(jobTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /job-types} : get all the jobTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobTypes in body.
     */
    @GetMapping("/job-types")
    public List<JobTypeDTO> getAllJobTypes() {
        log.debug("REST request to get all JobTypes");
        return jobTypeService.findAll();
    }

    /**
     * {@code GET  /job-types/:id} : get the "id" jobType.
     *
     * @param id the id of the jobTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-types/{id}")
    public ResponseEntity<JobTypeDTO> getJobType(@PathVariable Long id) {
        log.debug("REST request to get JobType : {}", id);
        Optional<JobTypeDTO> jobTypeDTO = jobTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobTypeDTO);
    }

    /**
     * {@code DELETE  /job-types/:id} : delete the "id" jobType.
     *
     * @param id the id of the jobTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-types/{id}")
    public ResponseEntity<Void> deleteJobType(@PathVariable Long id) {
        log.debug("REST request to delete JobType : {}", id);
        jobTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
