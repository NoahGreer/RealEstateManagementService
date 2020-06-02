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

import realestatemanagementservice.domain.Rent;
import realestatemanagementservice.domain.*; // for static metamodels
import realestatemanagementservice.repository.RentRepository;
import realestatemanagementservice.service.dto.RentCriteria;
import realestatemanagementservice.service.dto.RentDTO;
import realestatemanagementservice.service.mapper.RentMapper;

/**
 * Service for executing complex queries for {@link Rent} entities in the database.
 * The main input is a {@link RentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RentDTO} or a {@link Page} of {@link RentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RentQueryService extends QueryService<Rent> {

    private final Logger log = LoggerFactory.getLogger(RentQueryService.class);

    private final RentRepository rentRepository;

    private final RentMapper rentMapper;

    public RentQueryService(RentRepository rentRepository, RentMapper rentMapper) {
        this.rentRepository = rentRepository;
        this.rentMapper = rentMapper;
    }

    /**
     * Return a {@link List} of {@link RentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RentDTO> findByCriteria(RentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Rent> specification = createSpecification(criteria);
        return rentMapper.toDto(rentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RentDTO> findByCriteria(RentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rent> specification = createSpecification(criteria);
        return rentRepository.findAll(specification, page)
            .map(rentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rent> specification = createSpecification(criteria);
        return rentRepository.count(specification);
    }

    /**
     * Function to convert {@link RentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rent> createSpecification(RentCriteria criteria) {
        Specification<Rent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rent_.id));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), Rent_.dueDate));
            }
            if (criteria.getReceivedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReceivedDate(), Rent_.receivedDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Rent_.amount));
            }
            if (criteria.getLeaseId() != null) {
                specification = specification.and(buildSpecification(criteria.getLeaseId(),
                    root -> root.join(Rent_.lease, JoinType.LEFT).get(Lease_.id)));
            }
        }
        return specification;
    }
}
