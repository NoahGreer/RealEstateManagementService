package realestatemanagementservice.repository;

import realestatemanagementservice.domain.Infraction;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Infraction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfractionRepository extends JpaRepository<Infraction, Long>, JpaSpecificationExecutor<Infraction> {
}
