package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.PropertyTaxDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PropertyTax} and its DTO {@link PropertyTaxDTO}.
 */
@Mapper(componentModel = "spring", uses = {BuildingMapper.class})
public interface PropertyTaxMapper extends EntityMapper<PropertyTaxDTO, PropertyTax> {

    @Mapping(source = "building.id", target = "buildingId")
    PropertyTaxDTO toDto(PropertyTax propertyTax);

    @Mapping(source = "buildingId", target = "building")
    PropertyTax toEntity(PropertyTaxDTO propertyTaxDTO);

    default PropertyTax fromId(Long id) {
        if (id == null) {
            return null;
        }
        PropertyTax propertyTax = new PropertyTax();
        propertyTax.setId(id);
        return propertyTax;
    }
}
