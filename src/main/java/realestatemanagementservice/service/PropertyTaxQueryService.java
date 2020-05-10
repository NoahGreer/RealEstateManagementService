package realestatemanagementservice.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import realestatemanagementservice.domain.PropertyTax;
import realestatemanagementservice.domain.*; // for static metamodels
import realestatemanagementservice.repository.PropertyTaxRepository;
import realestatemanagementservice.service.dto.PropertyTaxCriteria;
import realestatemanagementservice.service.dto.PropertyTaxDTO;
import realestatemanagementservice.service.mapper.PropertyTaxMapper;

/**
 * Service for executing complex queries for {@link PropertyTax} entities in the database.
 * The main input is a {@link PropertyTaxCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PropertyTaxDTO} or a {@link Page} of {@link PropertyTaxDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PropertyTaxQueryService extends QueryService<PropertyTax> {

    private final Logger log = LoggerFactory.getLogger(PropertyTaxQueryService.class);

    private final PropertyTaxRepository propertyTaxRepository;

    private final PropertyTaxMapper propertyTaxMapper;

    public PropertyTaxQueryService(PropertyTaxRepository propertyTaxRepository, PropertyTaxMapper propertyTaxMapper) {
        this.propertyTaxRepository = propertyTaxRepository;
        this.propertyTaxMapper = propertyTaxMapper;
    }

    /**
     * Return a {@link List} of {@link PropertyTaxDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PropertyTaxDTO> findByCriteria(PropertyTaxCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PropertyTax> specification = createSpecification(criteria);
        return propertyTaxMapper.toDto(propertyTaxRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PropertyTaxDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PropertyTaxDTO> findByCriteria(PropertyTaxCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PropertyTax> specification = createSpecification(criteria);
        return propertyTaxRepository.findAll(specification, page)
            .map(propertyTaxMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PropertyTaxCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PropertyTax> specification = createSpecification(criteria);
        return propertyTaxRepository.count(specification);
    }

    /**
     * Function to convert {@link PropertyTaxCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PropertyTax> createSpecification(PropertyTaxCriteria criteria) {
        Specification<PropertyTax> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PropertyTax_.id));
            }
            if (criteria.getTaxYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaxYear(), PropertyTax_.taxYear));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PropertyTax_.amount));
            }
            if (criteria.getDatePaid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatePaid(), PropertyTax_.datePaid));
            }
            if (criteria.getConfirmationNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfirmationNumber(), PropertyTax_.confirmationNumber));
            }
            if (criteria.getBuildingId() != null) {
                specification = specification.and(buildSpecification(criteria.getBuildingId(),
                    root -> root.join(PropertyTax_.building, JoinType.LEFT).get(Building_.id)));
            }
        }
        return specification;
    }
}
