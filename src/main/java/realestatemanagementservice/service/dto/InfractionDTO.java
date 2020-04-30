package realestatemanagementservice.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Infraction} entity.
 */
public class InfractionDTO implements Serializable {
    
    private Long id;

    private LocalDate dateOccurred;

    private String cause;

    private String resolution;


    private Long leaseId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOccurred() {
        return dateOccurred;
    }

    public void setDateOccurred(LocalDate dateOccurred) {
        this.dateOccurred = dateOccurred;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
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

        InfractionDTO infractionDTO = (InfractionDTO) o;
        if (infractionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), infractionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InfractionDTO{" +
            "id=" + getId() +
            ", dateOccurred='" + getDateOccurred() + "'" +
            ", cause='" + getCause() + "'" +
            ", resolution='" + getResolution() + "'" +
            ", leaseId=" + getLeaseId() +
            "}";
    }
}
