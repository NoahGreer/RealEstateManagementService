package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A Infraction.
 */
@Entity
@Table(name = "infraction")
public class Infraction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_occurred")
    private LocalDate dateOccurred;

    @Column(name = "cause")
    private String cause;

    @Column(name = "resolution")
    private String resolution;

    @ManyToOne
    @JsonIgnoreProperties("infractions")
    private Lease lease;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOccurred() {
        return dateOccurred;
    }

    public Infraction dateOccurred(LocalDate dateOccurred) {
        this.dateOccurred = dateOccurred;
        return this;
    }

    public void setDateOccurred(LocalDate dateOccurred) {
        this.dateOccurred = dateOccurred;
    }

    public String getCause() {
        return cause;
    }

    public Infraction cause(String cause) {
        this.cause = cause;
        return this;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getResolution() {
        return resolution;
    }

    public Infraction resolution(String resolution) {
        this.resolution = resolution;
        return this;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Lease getLease() {
        return lease;
    }

    public Infraction lease(Lease lease) {
        this.lease = lease;
        return this;
    }

    public void setLease(Lease lease) {
        this.lease = lease;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Infraction)) {
            return false;
        }
        return id != null && id.equals(((Infraction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Infraction{" +
            "id=" + getId() +
            ", dateOccurred='" + getDateOccurred() + "'" +
            ", cause='" + getCause() + "'" +
            ", resolution='" + getResolution() + "'" +
            "}";
    }
}
