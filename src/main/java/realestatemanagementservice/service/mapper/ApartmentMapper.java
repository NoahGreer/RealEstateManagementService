package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.ApartmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Apartment} and its DTO {@link ApartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {BuildingMapper.class})
public interface ApartmentMapper extends EntityMapper<ApartmentDTO, Apartment> {

    @Mapping(source = "building.id", target = "buildingId")
    ApartmentDTO toDto(Apartment apartment);

    @Mapping(target = "maintenances", ignore = true)
    @Mapping(target = "removeMaintenance", ignore = true)
    @Mapping(target = "leases", ignore = true)
    @Mapping(target = "removeLease", ignore = true)
    @Mapping(source = "buildingId", target = "building")
    Apartment toEntity(ApartmentDTO apartmentDTO);

    default Apartment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Apartment apartment = new Apartment();
        apartment.setId(id);
        return apartment;
    }
}
