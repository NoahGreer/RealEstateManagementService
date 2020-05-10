package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Min(value = 0)
    @Column(name = "model_year")
    private Integer modelYear;

    @Column(name = "license_plate_number")
    private String licensePlateNumber;

    @Column(name = "license_plate_state")
    private String licensePlateState;

    @Column(name = "reserved_parking_stall_number")
    private String reservedParkingStallNumber;

    @ManyToMany(mappedBy = "vehicles")
    @JsonIgnore
    private Set<Lease> leases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public Vehicle make(String make) {
        this.make = make;
        return this;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public Vehicle model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public Vehicle modelYear(Integer modelYear) {
        this.modelYear = modelYear;
        return this;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public Vehicle licensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
        return this;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getLicensePlateState() {
        return licensePlateState;
    }

    public Vehicle licensePlateState(String licensePlateState) {
        this.licensePlateState = licensePlateState;
        return this;
    }

    public void setLicensePlateState(String licensePlateState) {
        this.licensePlateState = licensePlateState;
    }

    public String getReservedParkingStallNumber() {
        return reservedParkingStallNumber;
    }

    public Vehicle reservedParkingStallNumber(String reservedParkingStallNumber) {
        this.reservedParkingStallNumber = reservedParkingStallNumber;
        return this;
    }

    public void setReservedParkingStallNumber(String reservedParkingStallNumber) {
        this.reservedParkingStallNumber = reservedParkingStallNumber;
    }

    public Set<Lease> getLeases() {
        return leases;
    }

    public Vehicle leases(Set<Lease> leases) {
        this.leases = leases;
        return this;
    }

    public Vehicle addLease(Lease lease) {
        this.leases.add(lease);
        lease.getVehicles().add(this);
        return this;
    }

    public Vehicle removeLease(Lease lease) {
        this.leases.remove(lease);
        lease.getVehicles().remove(this);
        return this;
    }

    public void setLeases(Set<Lease> leases) {
        this.leases = leases;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return id != null && id.equals(((Vehicle) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
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
