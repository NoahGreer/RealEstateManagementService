package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Pet.
 */
@Entity
@Table(name = "pet")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "color")
    private String color;

    @Column(name = "certified_assistance_animal")
    private Boolean certifiedAssistanceAnimal;

    @ManyToMany(mappedBy = "pets")
    @JsonIgnore
    private Set<Lease> leases = new HashSet<>();

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

    public Pet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Pet type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public Pet color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean isCertifiedAssistanceAnimal() {
        return certifiedAssistanceAnimal;
    }

    public Pet certifiedAssistanceAnimal(Boolean certifiedAssistanceAnimal) {
        this.certifiedAssistanceAnimal = certifiedAssistanceAnimal;
        return this;
    }

    public void setCertifiedAssistanceAnimal(Boolean certifiedAssistanceAnimal) {
        this.certifiedAssistanceAnimal = certifiedAssistanceAnimal;
    }

    public Set<Lease> getLeases() {
        return leases;
    }

    public Pet leases(Set<Lease> leases) {
        this.leases = leases;
        return this;
    }

    public Pet addLease(Lease lease) {
        this.leases.add(lease);
        lease.getPets().add(this);
        return this;
    }

    public Pet removeLease(Lease lease) {
        this.leases.remove(lease);
        lease.getPets().remove(this);
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
        if (!(o instanceof Pet)) {
            return false;
        }
        return id != null && id.equals(((Pet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Pet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", color='" + getColor() + "'" +
            ", certifiedAssistanceAnimal='" + isCertifiedAssistanceAnimal() + "'" +
            "}";
    }
}
