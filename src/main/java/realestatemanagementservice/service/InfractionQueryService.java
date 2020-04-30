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

import realestatemanagementservice.domain.Infraction;
import realestatemanagementservice.domain.*; // for static metamodels
import realestatemanagementservice.repository.InfractionRepository;
import realestatemanagementservice.service.dto.InfractionCriteria;
import realestatemanagementservice.service.dto.InfractionDTO;
import realestatemanagementservice.service.mapper.InfractionMapper;

/**
 * Service for executing complex queries for {@link Infraction} entities in the database.
 * The main input is a {@link InfractionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InfractionDTO} or a {@link Page} of {@link InfractionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InfractionQueryService extends QueryService<Infraction> {

    private final Logger log = LoggerFactory.getLogger(InfractionQueryService.class);

    private final InfractionRepository infractionRepository;

    private final InfractionMapper infractionMapper;

    public InfractionQueryService(InfractionRepository infractionRepository, InfractionMapper infractionMapper) {
        this.infractionRepository = infractionRepository;
        this.infractionMapper = infractionMapper;
    }

    /**
     * Return a {@link List} of {@link InfractionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InfractionDTO> findByCriteria(InfractionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Infraction> specification = createSpecification(criteria);
        return infractionMapper.toDto(infractionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InfractionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InfractionDTO> findByCriteria(InfractionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Infraction> specification = createSpecification(criteria);
        return infractionRepository.findAll(specification, page)
            .map(infractionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InfractionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Infraction> specification = createSpecification(criteria);
        return infractionRepository.count(specification);
    }

    /**
     * Function to convert {@link InfractionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Infraction> createSpecification(InfractionCriteria criteria) {
        Specification<Infraction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Infraction_.id));
            }
            if (criteria.getDateOccurred() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOccurred(), Infraction_.dateOccurred));
            }
            if (criteria.getCause() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCause(), Infraction_.cause));
            }
            if (criteria.getResolution() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResolution(), Infraction_.resolution));
            }
            if (criteria.getLeaseId() != null) {
                specification = specification.and(buildSpecification(criteria.getLeaseId(),
                    root -> root.join(Infraction_.lease, JoinType.LEFT).get(Lease_.id)));
            }
        }
        return specification;
    }
}
