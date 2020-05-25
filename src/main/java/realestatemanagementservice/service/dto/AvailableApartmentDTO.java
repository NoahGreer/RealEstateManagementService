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

	private ApartmentDTO apartmentDTO;

	private BuildingService buildingService;
	
	Optional<BuildingDTO> aBuilding = buildingService.findOne(apartmentDTO.getBuildingId());
	
	private Long id;
	
	private String buildingName;

    private String unitNumber;

    private Boolean moveInReady;
	
	public AvailableApartmentDTO(ApartmentDTO apartmentDTO) {
		this.apartmentDTO = apartmentDTO;
		this.buildingName = aBuilding.get().getName();
		this.moveInReady = apartmentDTO.isMoveInReady();
		this.unitNumber = apartmentDTO.getUnitNumber();
		this.id = apartmentDTO.getId();
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
