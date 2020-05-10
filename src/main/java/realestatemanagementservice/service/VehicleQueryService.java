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

import realestatemanagementservice.domain.Vehicle;
import realestatemanagementservice.domain.*; // for static metamodels
import realestatemanagementservice.repository.VehicleRepository;
import realestatemanagementservice.service.dto.VehicleCriteria;
import realestatemanagementservice.service.dto.VehicleDTO;
import realestatemanagementservice.service.mapper.VehicleMapper;

/**
 * Service for executing complex queries for {@link Vehicle} entities in the database.
 * The main input is a {@link VehicleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VehicleDTO} or a {@link Page} of {@link VehicleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehicleQueryService extends QueryService<Vehicle> {

    private final Logger log = LoggerFactory.getLogger(VehicleQueryService.class);

    private final VehicleRepository vehicleRepository;

    private final VehicleMapper vehicleMapper;

    public VehicleQueryService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    /**
     * Return a {@link List} of {@link VehicleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VehicleDTO> findByCriteria(VehicleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleMapper.toDto(vehicleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VehicleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleDTO> findByCriteria(VehicleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.findAll(specification, page)
            .map(vehicleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehicleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.count(specification);
    }

    /**
     * Function to convert {@link VehicleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vehicle> createSpecification(VehicleCriteria criteria) {
        Specification<Vehicle> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vehicle_.id));
            }
            if (criteria.getMake() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMake(), Vehicle_.make));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Vehicle_.model));
            }
            if (criteria.getModelYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModelYear(), Vehicle_.modelYear));
            }
            if (criteria.getLicensePlateNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicensePlateNumber(), Vehicle_.licensePlateNumber));
            }
            if (criteria.getLicensePlateState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicensePlateState(), Vehicle_.licensePlateState));
            }
            if (criteria.getReservedParkingStallNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReservedParkingStallNumber(), Vehicle_.reservedParkingStallNumber));
            }
            if (criteria.getLeaseId() != null) {
                specification = specification.and(buildSpecification(criteria.getLeaseId(),
                    root -> root.join(Vehicle_.leases, JoinType.LEFT).get(Lease_.id)));
            }
        }
        return specification;
    }
}
