package realestatemanagementservice.service;

import realestatemanagementservice.service.dto.MaintenanceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link realestatemanagementservice.domain.Maintenance}.
 */
public interface MaintenanceService {

    /**
     * Save a maintenance.
     *
     * @param maintenanceDTO the entity to save.
     * @return the persisted entity.
     */
    MaintenanceDTO save(MaintenanceDTO maintenanceDTO);

    /**
     * Get all the maintenances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaintenanceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" maintenance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaintenanceDTO> findOne(Long id);

    /**
     * Delete the "id" maintenance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
