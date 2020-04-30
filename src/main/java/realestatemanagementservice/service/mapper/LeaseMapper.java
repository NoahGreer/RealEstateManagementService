package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.LeaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lease} and its DTO {@link LeaseDTO}.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class, VehicleMapper.class, PetMapper.class, ApartmentMapper.class})
public interface LeaseMapper extends EntityMapper<LeaseDTO, Lease> {

    @Mapping(source = "newLease.id", target = "newLeaseId")
    @Mapping(source = "apartment.id", target = "apartmentId")
    LeaseDTO toDto(Lease lease);

    @Mapping(target = "rents", ignore = true)
    @Mapping(target = "removeRent", ignore = true)
    @Mapping(target = "infractions", ignore = true)
    @Mapping(target = "removeInfraction", ignore = true)
    @Mapping(source = "newLeaseId", target = "newLease")
    @Mapping(target = "removePerson", ignore = true)
    @Mapping(target = "removeVehicle", ignore = true)
    @Mapping(target = "removePet", ignore = true)
    @Mapping(source = "apartmentId", target = "apartment")
    Lease toEntity(LeaseDTO leaseDTO);

    default Lease fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lease lease = new Lease();
        lease.setId(id);
        return lease;
    }
}
