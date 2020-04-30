package realestatemanagementservice.service;

import realestatemanagementservice.service.dto.PropertyTaxDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link realestatemanagementservice.domain.PropertyTax}.
 */
public interface PropertyTaxService {

    /**
     * Save a propertyTax.
     *
     * @param propertyTaxDTO the entity to save.
     * @return the persisted entity.
     */
    PropertyTaxDTO save(PropertyTaxDTO propertyTaxDTO);

    /**
     * Get all the propertyTaxes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PropertyTaxDTO> findAll(Pageable pageable);

    /**
     * Get the "id" propertyTax.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PropertyTaxDTO> findOne(Long id);

    /**
     * Delete the "id" propertyTax.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
