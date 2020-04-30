package realestatemanagementservice.repository;

import realestatemanagementservice.domain.Lease;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Lease entity.
 */
@Repository
public interface LeaseRepository extends JpaRepository<Lease, Long>, JpaSpecificationExecutor<Lease> {

    @Query(value = "select distinct lease from Lease lease left join fetch lease.people left join fetch lease.vehicles left join fetch lease.pets",
        countQuery = "select count(distinct lease) from Lease lease")
    Page<Lease> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct lease from Lease lease left join fetch lease.people left join fetch lease.vehicles left join fetch lease.pets")
    List<Lease> findAllWithEagerRelationships();

    @Query("select lease from Lease lease left join fetch lease.people left join fetch lease.vehicles left join fetch lease.pets where lease.id =:id")
    Optional<Lease> findOneWithEagerRelationships(@Param("id") Long id);
}
