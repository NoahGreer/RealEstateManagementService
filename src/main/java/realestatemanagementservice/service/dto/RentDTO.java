package realestatemanagementservice.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Rent} entity.
 */
public class RentDTO implements Serializable {
    
    private Long id;

    private LocalDate dueDate;

    private LocalDate receivedDate;

    private BigDecimal amount;


    private Long leaseId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(Long leaseId) {
        this.leaseId = leaseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RentDTO rentDTO = (RentDTO) o;
        if (rentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RentDTO{" +
            "id=" + getId() +
            ", dueDate='" + getDueDate() + "'" +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", amount=" + getAmount() +
            ", leaseId=" + getLeaseId() +
            "}";
    }
}
