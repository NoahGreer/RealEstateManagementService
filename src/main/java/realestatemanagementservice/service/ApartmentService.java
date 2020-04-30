package realestatemanagementservice.service;

import realestatemanagementservice.service.dto.ApartmentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link realestatemanagementservice.domain.Apartment}.
 */
public interface ApartmentService {

    /**
     * Save a apartment.
     *
     * @param apartmentDTO the entity to save.
     * @return the persisted entity.
     */
    ApartmentDTO save(ApartmentDTO apartmentDTO);

    /**
     * Get all the apartments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApartmentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" apartment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApartmentDTO> findOne(Long id);

    /**
     * Delete the "id" apartment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
