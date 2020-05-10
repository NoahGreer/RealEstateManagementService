package realestatemanagementservice.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Lease} entity.
 */
public class LeaseDTO implements Serializable {
    
    private Long id;

    private LocalDate dateSigned;

    private LocalDate moveInDate;

    private LocalDate noticeOfRemovalOrMoveoutDate;

    private LocalDate endDate;

    private BigDecimal amount;

    private String leaseType;

    private String notes;


    private Long newLeaseId;
    private Set<PersonDTO> people = new HashSet<>();
    private Set<VehicleDTO> vehicles = new HashSet<>();
    private Set<PetDTO> pets = new HashSet<>();

    private Long apartmentId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(LocalDate dateSigned) {
        this.dateSigned = dateSigned;
    }

    public LocalDate getMoveInDate() {
        return moveInDate;
    }

    public void setMoveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
    }

    public LocalDate getNoticeOfRemovalOrMoveoutDate() {
        return noticeOfRemovalOrMoveoutDate;
    }

    public void setNoticeOfRemovalOrMoveoutDate(LocalDate noticeOfRemovalOrMoveoutDate) {
        this.noticeOfRemovalOrMoveoutDate = noticeOfRemovalOrMoveoutDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLeaseType() {
        return leaseType;
    }

    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getNewLeaseId() {
        return newLeaseId;
    }

    public void setNewLeaseId(Long leaseId) {
        this.newLeaseId = leaseId;
    }

    public Set<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(Set<PersonDTO> people) {
        this.people = people;
    }

    public Set<VehicleDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<VehicleDTO> vehicles) {
        this.vehicles = vehicles;
    }

    public Set<PetDTO> getPets() {
        return pets;
    }

    public void setPets(Set<PetDTO> pets) {
        this.pets = pets;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LeaseDTO leaseDTO = (LeaseDTO) o;
        if (leaseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leaseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeaseDTO{" +
            "id=" + getId() +
            ", dateSigned='" + getDateSigned() + "'" +
            ", moveInDate='" + getMoveInDate() + "'" +
            ", noticeOfRemovalOrMoveoutDate='" + getNoticeOfRemovalOrMoveoutDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", amount=" + getAmount() +
            ", leaseType='" + getLeaseType() + "'" +
            ", notes='" + getNotes() + "'" +
            ", newLeaseId=" + getNewLeaseId() +
            ", people='" + getPeople() + "'" +
            ", vehicles='" + getVehicles() + "'" +
            ", pets='" + getPets() + "'" +
            ", apartmentId=" + getApartmentId() +
            "}";
    }
}
