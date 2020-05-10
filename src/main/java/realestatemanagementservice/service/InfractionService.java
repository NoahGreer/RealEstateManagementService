package realestatemanagementservice.service;

import realestatemanagementservice.service.dto.InfractionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link realestatemanagementservice.domain.Infraction}.
 */
public interface InfractionService {

    /**
     * Save a infraction.
     *
     * @param infractionDTO the entity to save.
     * @return the persisted entity.
     */
    InfractionDTO save(InfractionDTO infractionDTO);

    /**
     * Get all the infractions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InfractionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" infraction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InfractionDTO> findOne(Long id);

    /**
     * Delete the "id" infraction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
