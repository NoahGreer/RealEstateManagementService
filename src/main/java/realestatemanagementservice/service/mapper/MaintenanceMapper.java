package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.MaintenanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Maintenance} and its DTO {@link MaintenanceDTO}.
 */
@Mapper(componentModel = "spring", uses = {ApartmentMapper.class, ContractorMapper.class})
public interface MaintenanceMapper extends EntityMapper<MaintenanceDTO, Maintenance> {

    @Mapping(source = "apartment.id", target = "apartmentId")
    @Mapping(source = "contractor.id", target = "contractorId")
    MaintenanceDTO toDto(Maintenance maintenance);

    @Mapping(source = "apartmentId", target = "apartment")
    @Mapping(source = "contractorId", target = "contractor")
    Maintenance toEntity(MaintenanceDTO maintenanceDTO);

    default Maintenance fromId(Long id) {
        if (id == null) {
            return null;
        }
        Maintenance maintenance = new Maintenance();
        maintenance.setId(id);
        return maintenance;
    }
}
