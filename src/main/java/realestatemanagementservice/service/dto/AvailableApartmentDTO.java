package realestatemanagementservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import realestatemanagementservice.service.dto.ApartmentDTO;
import realestatemanagementservice.service.BuildingService;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Apartment} entity.
 */
public class AvailableApartmentDTO implements Serializable {

	private BuildingService buildingService;
	
	private ApartmentDTO apartment;
    
    Optional<BuildingDTO> aBuilding = buildingService.findOne(apartment.getBuildingId());
    
    
	
	private Long id = apartment.getId();
	
	private String buildingName = aBuilding.get().getName();

    private String unitNumber = apartment.getUnitNumber();

    private Boolean moveInReady = apartment.isMoveInReady();

    
    public Long getId() {
        return id;
    }

    public String getBuildingName() {
    	return buildingName;
    }
    
    
    public String getUnitNumber() {
        return unitNumber;
    }


    public Boolean isMoveInReady() {
        return moveInReady;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AvailableApartmentDTO availableApartmentDTO = (AvailableApartmentDTO) o;
        if (availableApartmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), availableApartmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApartmentDTO{" +
            "id=" + getId() +
            ", nameBuilding='" + getBuildingName() + "'" +
            ", unitNumber='" + getUnitNumber() + "'" +
            ", moveInReady='" + isMoveInReady() + "'" +
            "}";
    }

}
