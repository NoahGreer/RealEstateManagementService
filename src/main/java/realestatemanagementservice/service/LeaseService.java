package realestatemanagementservice.service;

import realestatemanagementservice.service.dto.LeaseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link realestatemanagementservice.domain.Lease}.
 */
public interface LeaseService {

    /**
     * Save a lease.
     *
     * @param leaseDTO the entity to save.
     * @return the persisted entity.
     */
    LeaseDTO save(LeaseDTO leaseDTO);

    /**
     * Get all the leases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseDTO> findAll(Pageable pageable);

    /**
     * Get all the leases with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<LeaseDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" lease.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaseDTO> findOne(Long id);

    /**
     * Delete the "id" lease.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
