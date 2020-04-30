package realestatemanagementservice.service;

import realestatemanagementservice.service.dto.VehicleDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link realestatemanagementservice.domain.Vehicle}.
 */
public interface VehicleService {

    /**
     * Save a vehicle.
     *
     * @param vehicleDTO the entity to save.
     * @return the persisted entity.
     */
    VehicleDTO save(VehicleDTO vehicleDTO);

    /**
     * Get all the vehicles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vehicle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleDTO> findOne(Long id);

    /**
     * Delete the "id" vehicle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
