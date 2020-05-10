package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.ContractorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contractor} and its DTO {@link ContractorDTO}.
 */
@Mapper(componentModel = "spring", uses = {JobTypeMapper.class})
public interface ContractorMapper extends EntityMapper<ContractorDTO, Contractor> {


    @Mapping(target = "maintenances", ignore = true)
    @Mapping(target = "removeMaintenance", ignore = true)
    @Mapping(target = "removeJobType", ignore = true)
    Contractor toEntity(ContractorDTO contractorDTO);

    default Contractor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contractor contractor = new Contractor();
        contractor.setId(id);
        return contractor;
    }
}
