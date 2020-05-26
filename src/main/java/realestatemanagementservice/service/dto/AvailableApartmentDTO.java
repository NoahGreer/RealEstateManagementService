package realestatemanagementservice.service.dto;

import java.io.Serializable;
import java.util.Objects;

import realestatemanagementservice.service.dto.ApartmentDTO;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Apartment} entity.
 */
public class AvailableApartmentDTO implements Serializable {

	private Long id;
	
    private String unitNumber;

    private Boolean moveInReady;
    
    private String buildingName;
	
	public AvailableApartmentDTO(ApartmentDTO apartmentDTO, BuildingDTO buildingDTO) {
		Objects.requireNonNull(apartmentDTO, "apartmentDTO must not be null");
		Objects.requireNonNull(buildingDTO, "buildingDTO must not be null");
		
		this.id = apartmentDTO.getId();
		this.unitNumber = apartmentDTO.getUnitNumber();
		this.moveInReady = apartmentDTO.isMoveInReady();
		this.buildingName = buildingDTO.getName();
	}
    
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
        return "AvailableApartmentDTO{" +
            "id=" + getId() +
            ", nameBuilding='" + getBuildingName() + "'" +
            ", unitNumber='" + getUnitNumber() + "'" +
            ", moveInReady='" + isMoveInReady() + "'" +
            "}";
    }
}
