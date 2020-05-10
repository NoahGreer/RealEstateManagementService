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

import realestatemanagementservice.domain.Person;
import realestatemanagementservice.domain.*; // for static metamodels
import realestatemanagementservice.repository.PersonRepository;
import realestatemanagementservice.service.dto.PersonCriteria;
import realestatemanagementservice.service.dto.PersonDTO;
import realestatemanagementservice.service.mapper.PersonMapper;

/**
 * Service for executing complex queries for {@link Person} entities in the database.
 * The main input is a {@link PersonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonDTO} or a {@link Page} of {@link PersonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonQueryService extends QueryService<Person> {

    private final Logger log = LoggerFactory.getLogger(PersonQueryService.class);

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    public PersonQueryService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    /**
     * Return a {@link List} of {@link PersonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonDTO> findByCriteria(PersonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personMapper.toDto(personRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PersonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonDTO> findByCriteria(PersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.findAll(specification, page)
            .map(personMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Person> createSpecification(PersonCriteria criteria) {
        Specification<Person> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Person_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Person_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Person_.lastName));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Person_.phoneNumber));
            }
            if (criteria.getEmailAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAddress(), Person_.emailAddress));
            }
            if (criteria.getPrimaryContact() != null) {
                specification = specification.and(buildSpecification(criteria.getPrimaryContact(), Person_.primaryContact));
            }
            if (criteria.getIsMinor() != null) {
                specification = specification.and(buildSpecification(criteria.getIsMinor(), Person_.isMinor));
            }
            if (criteria.getSsn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSsn(), Person_.ssn));
            }
            if (criteria.getBackgroundCheckDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBackgroundCheckDate(), Person_.backgroundCheckDate));
            }
            if (criteria.getBackgroundCheckConfirmationNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBackgroundCheckConfirmationNumber(), Person_.backgroundCheckConfirmationNumber));
            }
            if (criteria.getEmploymentVerificationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmploymentVerificationDate(), Person_.employmentVerificationDate));
            }
            if (criteria.getEmploymentVerificationConfirmationNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmploymentVerificationConfirmationNumber(), Person_.employmentVerificationConfirmationNumber));
            }
            if (criteria.getLeaseId() != null) {
                specification = specification.and(buildSpecification(criteria.getLeaseId(),
                    root -> root.join(Person_.leases, JoinType.LEFT).get(Lease_.id)));
            }
        }
        return specification;
    }
}
