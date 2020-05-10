package realestatemanagementservice.service.impl;

import realestatemanagementservice.service.PropertyTaxService;
import realestatemanagementservice.domain.PropertyTax;
import realestatemanagementservice.repository.PropertyTaxRepository;
import realestatemanagementservice.service.dto.PropertyTaxDTO;
import realestatemanagementservice.service.mapper.PropertyTaxMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PropertyTax}.
 */
@Service
@Transactional
public class PropertyTaxServiceImpl implements PropertyTaxService {

    private final Logger log = LoggerFactory.getLogger(PropertyTaxServiceImpl.class);

    private final PropertyTaxRepository propertyTaxRepository;

    private final PropertyTaxMapper propertyTaxMapper;

    public PropertyTaxServiceImpl(PropertyTaxRepository propertyTaxRepository, PropertyTaxMapper propertyTaxMapper) {
        this.propertyTaxRepository = propertyTaxRepository;
        this.propertyTaxMapper = propertyTaxMapper;
    }

    /**
     * Save a propertyTax.
     *
     * @param propertyTaxDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PropertyTaxDTO save(PropertyTaxDTO propertyTaxDTO) {
        log.debug("Request to save PropertyTax : {}", propertyTaxDTO);
        PropertyTax propertyTax = propertyTaxMapper.toEntity(propertyTaxDTO);
        propertyTax = propertyTaxRepository.save(propertyTax);
        return propertyTaxMapper.toDto(propertyTax);
    }

    /**
     * Get all the propertyTaxes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PropertyTaxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PropertyTaxes");
        return propertyTaxRepository.findAll(pageable)
            .map(propertyTaxMapper::toDto);
    }

    /**
     * Get one propertyTax by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PropertyTaxDTO> findOne(Long id) {
        log.debug("Request to get PropertyTax : {}", id);
        return propertyTaxRepository.findById(id)
            .map(propertyTaxMapper::toDto);
    }

    /**
     * Delete the propertyTax by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PropertyTax : {}", id);
        propertyTaxRepository.deleteById(id);
    }
}
