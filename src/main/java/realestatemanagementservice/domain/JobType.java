package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A JobType.
 */
@Entity
@Table(name = "job_type")
public class JobType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "jobTypes")
    @JsonIgnore
    private Set<Contractor> contractors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public JobType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Contractor> getContractors() {
        return contractors;
    }

    public JobType contractors(Set<Contractor> contractors) {
        this.contractors = contractors;
        return this;
    }

    public JobType addContractor(Contractor contractor) {
        this.contractors.add(contractor);
        contractor.getJobTypes().add(this);
        return this;
    }

    public JobType removeContractor(Contractor contractor) {
        this.contractors.remove(contractor);
        contractor.getJobTypes().remove(this);
        return this;
    }

    public void setContractors(Set<Contractor> contractors) {
        this.contractors = contractors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobType)) {
            return false;
        }
        return id != null && id.equals(((JobType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "JobType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
