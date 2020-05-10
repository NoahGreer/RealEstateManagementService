package realestatemanagementservice.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Building} entity.
 */
public class BuildingDTO implements Serializable {
    
    private Long id;

    private String name;

    private LocalDate purchaseDate;

    private String propertyNumber;

    private String streetAddress;

    private String city;

    @Size(min = 2, max = 2)
    private String stateCode;

    @Size(max = 10)
    private String zipCode;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BuildingDTO buildingDTO = (BuildingDTO) o;
        if (buildingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), buildingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BuildingDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", propertyNumber='" + getPropertyNumber() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", stateCode='" + getStateCode() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            "}";
    }
}
