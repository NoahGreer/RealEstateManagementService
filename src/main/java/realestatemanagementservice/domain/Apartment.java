package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Apartment.
 */
@Entity
@Table(name = "apartment")
public class Apartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "unit_number")
    private String unitNumber;

    @Column(name = "move_in_ready")
    private Boolean moveInReady;

    @OneToMany(mappedBy = "apartment")
    private Set<Maintenance> maintenances = new HashSet<>();

    @OneToMany(mappedBy = "apartment")
    private Set<Lease> leases = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("apartments")
    private Building building;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public Apartment unitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
        return this;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public Boolean isMoveInReady() {
        return moveInReady;
    }

    public Apartment moveInReady(Boolean moveInReady) {
        this.moveInReady = moveInReady;
        return this;
    }

    public void setMoveInReady(Boolean moveInReady) {
        this.moveInReady = moveInReady;
    }

    public Set<Maintenance> getMaintenances() {
        return maintenances;
    }

    public Apartment maintenances(Set<Maintenance> maintenances) {
        this.maintenances = maintenances;
        return this;
    }

    public Apartment addMaintenance(Maintenance maintenance) {
        this.maintenances.add(maintenance);
        maintenance.setApartment(this);
        return this;
    }

    public Apartment removeMaintenance(Maintenance maintenance) {
        this.maintenances.remove(maintenance);
        maintenance.setApartment(null);
        return this;
    }

    public void setMaintenances(Set<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }

    public Set<Lease> getLeases() {
        return leases;
    }

    public Apartment leases(Set<Lease> leases) {
        this.leases = leases;
        return this;
    }

    public Apartment addLease(Lease lease) {
        this.leases.add(lease);
        lease.setApartment(this);
        return this;
    }

    public Apartment removeLease(Lease lease) {
        this.leases.remove(lease);
        lease.setApartment(null);
        return this;
    }

    public void setLeases(Set<Lease> leases) {
        this.leases = leases;
    }

    public Building getBuilding() {
        return building;
    }

    public Apartment building(Building building) {
        this.building = building;
        return this;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Apartment)) {
            return false;
        }
        return id != null && id.equals(((Apartment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Apartment{" +
            "id=" + getId() +
            ", unitNumber='" + getUnitNumber() + "'" +
            ", moveInReady='" + isMoveInReady() + "'" +
            "}";
    }
}
