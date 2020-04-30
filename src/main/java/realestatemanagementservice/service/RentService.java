package realestatemanagementservice.service;

import realestatemanagementservice.service.dto.RentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link realestatemanagementservice.domain.Rent}.
 */
public interface RentService {

    /**
     * Save a rent.
     *
     * @param rentDTO the entity to save.
     * @return the persisted entity.
     */
    RentDTO save(RentDTO rentDTO);

    /**
     * Get all the rents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RentDTO> findOne(Long id);

    /**
     * Delete the "id" rent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
