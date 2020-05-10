package realestatemanagementservice.service;

import realestatemanagementservice.domain.JobType;
import realestatemanagementservice.repository.JobTypeRepository;
import realestatemanagementservice.service.dto.JobTypeDTO;
import realestatemanagementservice.service.mapper.JobTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link JobType}.
 */
@Service
@Transactional
public class JobTypeService {

    private final Logger log = LoggerFactory.getLogger(JobTypeService.class);

    private final JobTypeRepository jobTypeRepository;

    private final JobTypeMapper jobTypeMapper;

    public JobTypeService(JobTypeRepository jobTypeRepository, JobTypeMapper jobTypeMapper) {
        this.jobTypeRepository = jobTypeRepository;
        this.jobTypeMapper = jobTypeMapper;
    }

    /**
     * Save a jobType.
     *
     * @param jobTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public JobTypeDTO save(JobTypeDTO jobTypeDTO) {
        log.debug("Request to save JobType : {}", jobTypeDTO);
        JobType jobType = jobTypeMapper.toEntity(jobTypeDTO);
        jobType = jobTypeRepository.save(jobType);
        return jobTypeMapper.toDto(jobType);
    }

    /**
     * Get all the jobTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<JobTypeDTO> findAll() {
        log.debug("Request to get all JobTypes");
        return jobTypeRepository.findAll().stream()
            .map(jobTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one jobType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobTypeDTO> findOne(Long id) {
        log.debug("Request to get JobType : {}", id);
        return jobTypeRepository.findById(id)
            .map(jobTypeMapper::toDto);
    }

    /**
     * Delete the jobType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete JobType : {}", id);
        jobTypeRepository.deleteById(id);
    }
}
