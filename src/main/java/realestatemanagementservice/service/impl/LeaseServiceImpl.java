package realestatemanagementservice.service.impl;

import realestatemanagementservice.service.LeaseService;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.repository.LeaseRepository;
import realestatemanagementservice.service.dto.LeaseDTO;
import realestatemanagementservice.service.mapper.LeaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Lease}.
 */
@Service
@Transactional
public class LeaseServiceImpl implements LeaseService {

    private final Logger log = LoggerFactory.getLogger(LeaseServiceImpl.class);

    private final LeaseRepository leaseRepository;

    private final LeaseMapper leaseMapper;

    public LeaseServiceImpl(LeaseRepository leaseRepository, LeaseMapper leaseMapper) {
        this.leaseRepository = leaseRepository;
        this.leaseMapper = leaseMapper;
    }

    /**
     * Save a lease.
     *
     * @param leaseDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LeaseDTO save(LeaseDTO leaseDTO) {
        log.debug("Request to save Lease : {}", leaseDTO);
        Lease lease = leaseMapper.toEntity(leaseDTO);
        lease = leaseRepository.save(lease);
        return leaseMapper.toDto(lease);
    }

    /**
     * Get all the leases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LeaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Leases");
        return leaseRepository.findAll(pageable)
            .map(leaseMapper::toDto);
    }

    /**
     * Get all the leases with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LeaseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return leaseRepository.findAllWithEagerRelationships(pageable).map(leaseMapper::toDto);
    }

    /**
     * Get one lease by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseDTO> findOne(Long id) {
        log.debug("Request to get Lease : {}", id);
        return leaseRepository.findOneWithEagerRelationships(id)
            .map(leaseMapper::toDto);
    }

    /**
     * Delete the lease by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lease : {}", id);
        leaseRepository.deleteById(id);
    }
}
