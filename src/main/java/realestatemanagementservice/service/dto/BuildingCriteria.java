package realestatemanagementservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link realestatemanagementservice.domain.Building} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.BuildingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /buildings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BuildingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter purchaseDate;

    private StringFilter propertyNumber;

    private StringFilter streetAddress;

    private StringFilter city;

    private StringFilter stateCode;

    private StringFilter zipCode;

    private LongFilter propertyTaxId;

    private LongFilter apartmentId;

    public BuildingCriteria() {
    }

    public BuildingCriteria(BuildingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.purchaseDate = other.purchaseDate == null ? null : other.purchaseDate.copy();
        this.propertyNumber = other.propertyNumber == null ? null : other.propertyNumber.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.stateCode = other.stateCode == null ? null : other.stateCode.copy();
        this.zipCode = other.zipCode == null ? null : other.zipCode.copy();
        this.propertyTaxId = other.propertyTaxId == null ? null : other.propertyTaxId.copy();
        this.apartmentId = other.apartmentId == null ? null : other.apartmentId.copy();
    }

    @Override
    public BuildingCriteria copy() {
        return new BuildingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public StringFilter getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(StringFilter propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateCode() {
        return stateCode;
    }

    public void setStateCode(StringFilter stateCode) {
        this.stateCode = stateCode;
    }

    public StringFilter getZipCode() {
        return zipCode;
    }

    public void setZipCode(StringFilter zipCode) {
        this.zipCode = zipCode;
    }

    public LongFilter getPropertyTaxId() {
        return propertyTaxId;
    }

    public void setPropertyTaxId(LongFilter propertyTaxId) {
        this.propertyTaxId = propertyTaxId;
    }

    public LongFilter getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(LongFilter apartmentId) {
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
        final BuildingCriteria that = (BuildingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(propertyNumber, that.propertyNumber) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(city, that.city) &&
            Objects.equals(stateCode, that.stateCode) &&
            Objects.equals(zipCode, that.zipCode) &&
            Objects.equals(propertyTaxId, that.propertyTaxId) &&
            Objects.equals(apartmentId, that.apartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        purchaseDate,
        propertyNumber,
        streetAddress,
        city,
        stateCode,
        zipCode,
        propertyTaxId,
        apartmentId
        );
    }

    @Override
    public String toString() {
        return "BuildingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (purchaseDate != null ? "purchaseDate=" + purchaseDate + ", " : "") +
                (propertyNumber != null ? "propertyNumber=" + propertyNumber + ", " : "") +
                (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (stateCode != null ? "stateCode=" + stateCode + ", " : "") +
                (zipCode != null ? "zipCode=" + zipCode + ", " : "") +
                (propertyTaxId != null ? "propertyTaxId=" + propertyTaxId + ", " : "") +
                (apartmentId != null ? "apartmentId=" + apartmentId + ", " : "") +
            "}";
    }

}
