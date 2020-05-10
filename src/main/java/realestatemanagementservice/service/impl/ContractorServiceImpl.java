package realestatemanagementservice.service.impl;

import realestatemanagementservice.service.ContractorService;
import realestatemanagementservice.domain.Contractor;
import realestatemanagementservice.repository.ContractorRepository;
import realestatemanagementservice.service.dto.ContractorDTO;
import realestatemanagementservice.service.mapper.ContractorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Contractor}.
 */
@Service
@Transactional
public class ContractorServiceImpl implements ContractorService {

    private final Logger log = LoggerFactory.getLogger(ContractorServiceImpl.class);

    private final ContractorRepository contractorRepository;

    private final ContractorMapper contractorMapper;

    public ContractorServiceImpl(ContractorRepository contractorRepository, ContractorMapper contractorMapper) {
        this.contractorRepository = contractorRepository;
        this.contractorMapper = contractorMapper;
    }

    /**
     * Save a contractor.
     *
     * @param contractorDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ContractorDTO save(ContractorDTO contractorDTO) {
        log.debug("Request to save Contractor : {}", contractorDTO);
        Contractor contractor = contractorMapper.toEntity(contractorDTO);
        contractor = contractorRepository.save(contractor);
        return contractorMapper.toDto(contractor);
    }

    /**
     * Get all the contractors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContractorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contractors");
        return contractorRepository.findAll(pageable)
            .map(contractorMapper::toDto);
    }

    /**
     * Get all the contractors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ContractorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contractorRepository.findAllWithEagerRelationships(pageable).map(contractorMapper::toDto);
    }

    /**
     * Get one contractor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContractorDTO> findOne(Long id) {
        log.debug("Request to get Contractor : {}", id);
        return contractorRepository.findOneWithEagerRelationships(id)
            .map(contractorMapper::toDto);
    }

    /**
     * Delete the contractor by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contractor : {}", id);
        contractorRepository.deleteById(id);
    }
}
