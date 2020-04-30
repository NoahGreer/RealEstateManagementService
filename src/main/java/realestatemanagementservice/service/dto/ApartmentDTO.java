package realestatemanagementservice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Apartment} entity.
 */
public class ApartmentDTO implements Serializable {
    
    private Long id;

    private String unitNumber;

    private Boolean moveInReady;


    private Long buildingId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public Boolean isMoveInReady() {
        return moveInReady;
    }

    public void setMoveInReady(Boolean moveInReady) {
        this.moveInReady = moveInReady;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApartmentDTO apartmentDTO = (ApartmentDTO) o;
        if (apartmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), apartmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApartmentDTO{" +
            "id=" + getId() +
            ", unitNumber='" + getUnitNumber() + "'" +
            ", moveInReady='" + isMoveInReady() + "'" +
            ", buildingId=" + getBuildingId() +
            "}";
    }
}
