package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.InfractionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Infraction} and its DTO {@link InfractionDTO}.
 */
@Mapper(componentModel = "spring", uses = {LeaseMapper.class})
public interface InfractionMapper extends EntityMapper<InfractionDTO, Infraction> {

    @Mapping(source = "lease.id", target = "leaseId")
    InfractionDTO toDto(Infraction infraction);

    @Mapping(source = "leaseId", target = "lease")
    Infraction toEntity(InfractionDTO infractionDTO);

    default Infraction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Infraction infraction = new Infraction();
        infraction.setId(id);
        return infraction;
    }
}
