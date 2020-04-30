package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.BuildingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Building} and its DTO {@link BuildingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BuildingMapper extends EntityMapper<BuildingDTO, Building> {


    @Mapping(target = "propertyTaxes", ignore = true)
    @Mapping(target = "removePropertyTax", ignore = true)
    @Mapping(target = "apartments", ignore = true)
    @Mapping(target = "removeApartment", ignore = true)
    Building toEntity(BuildingDTO buildingDTO);

    default Building fromId(Long id) {
        if (id == null) {
            return null;
        }
        Building building = new Building();
        building.setId(id);
        return building;
    }
}
