package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.RentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rent} and its DTO {@link RentDTO}.
 */
@Mapper(componentModel = "spring", uses = {LeaseMapper.class})
public interface RentMapper extends EntityMapper<RentDTO, Rent> {

    @Mapping(source = "lease.id", target = "leaseId")
    RentDTO toDto(Rent rent);

    @Mapping(source = "leaseId", target = "lease")
    Rent toEntity(RentDTO rentDTO);

    default Rent fromId(Long id) {
        if (id == null) {
            return null;
        }
        Rent rent = new Rent();
        rent.setId(id);
        return rent;
    }
}
