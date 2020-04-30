package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.PetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pet} and its DTO {@link PetDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PetMapper extends EntityMapper<PetDTO, Pet> {


    @Mapping(target = "leases", ignore = true)
    @Mapping(target = "removeLease", ignore = true)
    Pet toEntity(PetDTO petDTO);

    default Pet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pet pet = new Pet();
        pet.setId(id);
        return pet;
    }
}
