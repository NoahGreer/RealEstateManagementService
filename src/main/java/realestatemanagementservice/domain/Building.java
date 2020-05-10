package realestatemanagementservice.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Building.
 */
@Entity
@Table(name = "building")
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "property_number")
    private String propertyNumber;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "city")
    private String city;

    @Size(min = 2, max = 2)
    @Column(name = "state_code", length = 2)
    private String stateCode;

    @Size(max = 10)
    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @OneToMany(mappedBy = "building")
    private Set<PropertyTax> propertyTaxes = new HashSet<>();

    @OneToMany(mappedBy = "building")
    private Set<Apartment> apartments = new HashSet<>();

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

    public Building name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public Building purchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public Building propertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
        return this;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Building streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public Building city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public Building stateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Building zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Set<PropertyTax> getPropertyTaxes() {
        return propertyTaxes;
    }

    public Building propertyTaxes(Set<PropertyTax> propertyTaxes) {
        this.propertyTaxes = propertyTaxes;
        return this;
    }

    public Building addPropertyTax(PropertyTax propertyTax) {
        this.propertyTaxes.add(propertyTax);
        propertyTax.setBuilding(this);
        return this;
    }

    public Building removePropertyTax(PropertyTax propertyTax) {
        this.propertyTaxes.remove(propertyTax);
        propertyTax.setBuilding(null);
        return this;
    }

    public void setPropertyTaxes(Set<PropertyTax> propertyTaxes) {
        this.propertyTaxes = propertyTaxes;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public Building apartments(Set<Apartment> apartments) {
        this.apartments = apartments;
        return this;
    }

    public Building addApartment(Apartment apartment) {
        this.apartments.add(apartment);
        apartment.setBuilding(this);
        return this;
    }

    public Building removeApartment(Apartment apartment) {
        this.apartments.remove(apartment);
        apartment.setBuilding(null);
        return this;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Building)) {
            return false;
        }
        return id != null && id.equals(((Building) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Building{" +
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
