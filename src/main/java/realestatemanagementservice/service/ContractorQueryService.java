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

import realestatemanagementservice.domain.Contractor;
import realestatemanagementservice.domain.*; // for static metamodels
import realestatemanagementservice.repository.ContractorRepository;
import realestatemanagementservice.service.dto.ContractorCriteria;
import realestatemanagementservice.service.dto.ContractorDTO;
import realestatemanagementservice.service.mapper.ContractorMapper;

/**
 * Service for executing complex queries for {@link Contractor} entities in the database.
 * The main input is a {@link ContractorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContractorDTO} or a {@link Page} of {@link ContractorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContractorQueryService extends QueryService<Contractor> {

    private final Logger log = LoggerFactory.getLogger(ContractorQueryService.class);

    private final ContractorRepository contractorRepository;

    private final ContractorMapper contractorMapper;

    public ContractorQueryService(ContractorRepository contractorRepository, ContractorMapper contractorMapper) {
        this.contractorRepository = contractorRepository;
        this.contractorMapper = contractorMapper;
    }

    /**
     * Return a {@link List} of {@link ContractorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContractorDTO> findByCriteria(ContractorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Contractor> specification = createSpecification(criteria);
        return contractorMapper.toDto(contractorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContractorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractorDTO> findByCriteria(ContractorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contractor> specification = createSpecification(criteria);
        return contractorRepository.findAll(specification, page)
            .map(contractorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContractorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Contractor> specification = createSpecification(criteria);
        return contractorRepository.count(specification);
    }

    /**
     * Function to convert {@link ContractorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Contractor> createSpecification(ContractorCriteria criteria) {
        Specification<Contractor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Contractor_.id));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), Contractor_.companyName));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), Contractor_.streetAddress));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Contractor_.city));
            }
            if (criteria.getStateCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateCode(), Contractor_.stateCode));
            }
            if (criteria.getZipCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipCode(), Contractor_.zipCode));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Contractor_.phoneNumber));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), Contractor_.contactPerson));
            }
            if (criteria.getRatingOfWork() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRatingOfWork(), Contractor_.ratingOfWork));
            }
            if (criteria.getRatingOfResponsiveness() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRatingOfResponsiveness(), Contractor_.ratingOfResponsiveness));
            }
            if (criteria.getMaintenanceId() != null) {
                specification = specification.and(buildSpecification(criteria.getMaintenanceId(),
                    root -> root.join(Contractor_.maintenances, JoinType.LEFT).get(Maintenance_.id)));
            }
            if (criteria.getJobTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobTypeId(),
                    root -> root.join(Contractor_.jobTypes, JoinType.LEFT).get(JobType_.id)));
            }
        }
        return specification;
    }
}
