package realestatemanagementservice.repository;

import realestatemanagementservice.domain.Contractor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Contractor entity.
 */
@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long>, JpaSpecificationExecutor<Contractor> {

    @Query(value = "select distinct contractor from Contractor contractor left join fetch contractor.jobTypes",
        countQuery = "select count(distinct contractor) from Contractor contractor")
    Page<Contractor> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct contractor from Contractor contractor left join fetch contractor.jobTypes")
    List<Contractor> findAllWithEagerRelationships();

    @Query("select contractor from Contractor contractor left join fetch contractor.jobTypes where contractor.id =:id")
    Optional<Contractor> findOneWithEagerRelationships(@Param("id") Long id);
}
