package realestatemanagementservice.service;

import realestatemanagementservice.service.dto.ContractorDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link realestatemanagementservice.domain.Contractor}.
 */
public interface ContractorService {

    /**
     * Save a contractor.
     *
     * @param contractorDTO the entity to save.
     * @return the persisted entity.
     */
    ContractorDTO save(ContractorDTO contractorDTO);

    /**
     * Get all the contractors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractorDTO> findAll(Pageable pageable);

    /**
     * Get all the contractors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<ContractorDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" contractor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContractorDTO> findOne(Long id);

    /**
     * Delete the "id" contractor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
