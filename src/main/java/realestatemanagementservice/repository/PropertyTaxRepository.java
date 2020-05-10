package realestatemanagementservice.repository;

import realestatemanagementservice.domain.PropertyTax;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PropertyTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyTaxRepository extends JpaRepository<PropertyTax, Long>, JpaSpecificationExecutor<PropertyTax> {
}
