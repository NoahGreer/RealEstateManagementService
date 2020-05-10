package realestatemanagementservice.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Vehicle} entity.
 */
public class VehicleDTO implements Serializable {
    
    private Long id;

    private String make;

    private String model;

    @Min(value = 0)
    private Integer modelYear;

    private String licensePlateNumber;

    private String licensePlateState;

    private String reservedParkingStallNumber;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getLicensePlateState() {
        return licensePlateState;
    }

    public void setLicensePlateState(String licensePlateState) {
        this.licensePlateState = licensePlateState;
    }

    public String getReservedParkingStallNumber() {
        return reservedParkingStallNumber;
    }

    public void setReservedParkingStallNumber(String reservedParkingStallNumber) {
        this.reservedParkingStallNumber = reservedParkingStallNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VehicleDTO vehicleDTO = (VehicleDTO) o;
        if (vehicleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehicleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VehicleDTO{" +
            "id=" + getId() +
            ", make='" + getMake() + "'" +
            ", model='" + getModel() + "'" +
            ", modelYear=" + getModelYear() +
            ", licensePlateNumber='" + getLicensePlateNumber() + "'" +
            ", licensePlateState='" + getLicensePlateState() + "'" +
            ", reservedParkingStallNumber='" + getReservedParkingStallNumber() + "'" +
            "}";
    }
}
