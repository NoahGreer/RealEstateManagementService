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

import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.domain.*; // for static metamodels
import realestatemanagementservice.repository.LeaseRepository;
import realestatemanagementservice.service.dto.LeaseCriteria;
import realestatemanagementservice.service.dto.LeaseDTO;
import realestatemanagementservice.service.mapper.LeaseMapper;

/**
 * Service for executing complex queries for {@link Lease} entities in the database.
 * The main input is a {@link LeaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseDTO} or a {@link Page} of {@link LeaseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseQueryService extends QueryService<Lease> {

    private final Logger log = LoggerFactory.getLogger(LeaseQueryService.class);

    private final LeaseRepository leaseRepository;

    private final LeaseMapper leaseMapper;

    public LeaseQueryService(LeaseRepository leaseRepository, LeaseMapper leaseMapper) {
        this.leaseRepository = leaseRepository;
        this.leaseMapper = leaseMapper;
    }

    /**
     * Return a {@link List} of {@link LeaseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseDTO> findByCriteria(LeaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lease> specification = createSpecification(criteria);
        return leaseMapper.toDto(leaseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseDTO> findByCriteria(LeaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lease> specification = createSpecification(criteria);
        return leaseRepository.findAll(specification, page)
            .map(leaseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Lease> specification = createSpecification(criteria);
        return leaseRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lease> createSpecification(LeaseCriteria criteria) {
        Specification<Lease> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Lease_.id));
            }
            if (criteria.getDateSigned() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateSigned(), Lease_.dateSigned));
            }
            if (criteria.getMoveInDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMoveInDate(), Lease_.moveInDate));
            }
            if (criteria.getNoticeOfRemovalOrMoveoutDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNoticeOfRemovalOrMoveoutDate(), Lease_.noticeOfRemovalOrMoveoutDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Lease_.endDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Lease_.amount));
            }
            if (criteria.getLeaseType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeaseType(), Lease_.leaseType));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Lease_.notes));
            }
            if (criteria.getRentId() != null) {
                specification = specification.and(buildSpecification(criteria.getRentId(),
                    root -> root.join(Lease_.rents, JoinType.LEFT).get(Rent_.id)));
            }
            if (criteria.getInfractionId() != null) {
                specification = specification.and(buildSpecification(criteria.getInfractionId(),
                    root -> root.join(Lease_.infractions, JoinType.LEFT).get(Infraction_.id)));
            }
            if (criteria.getNewLeaseId() != null) {
                specification = specification.and(buildSpecification(criteria.getNewLeaseId(),
                    root -> root.join(Lease_.newLease, JoinType.LEFT).get(Lease_.id)));
            }
            if (criteria.getPersonId() != null) {
                specification = specification.and(buildSpecification(criteria.getPersonId(),
                    root -> root.join(Lease_.people, JoinType.LEFT).get(Person_.id)));
            }
            if (criteria.getVehicleId() != null) {
                specification = specification.and(buildSpecification(criteria.getVehicleId(),
                    root -> root.join(Lease_.vehicles, JoinType.LEFT).get(Vehicle_.id)));
            }
            if (criteria.getPetId() != null) {
                specification = specification.and(buildSpecification(criteria.getPetId(),
                    root -> root.join(Lease_.pets, JoinType.LEFT).get(Pet_.id)));
            }
            if (criteria.getApartmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getApartmentId(),
                    root -> root.join(Lease_.apartment, JoinType.LEFT).get(Apartment_.id)));
            }
        }
        return specification;
    }
}
