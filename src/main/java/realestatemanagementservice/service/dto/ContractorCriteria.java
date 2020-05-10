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

/**
 * Criteria class for the {@link realestatemanagementservice.domain.Contractor} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.ContractorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contractors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContractorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyName;

    private StringFilter streetAddress;

    private StringFilter city;

    private StringFilter stateCode;

    private StringFilter zipCode;

    private StringFilter phoneNumber;

    private StringFilter contactPerson;

    private IntegerFilter ratingOfWork;

    private IntegerFilter ratingOfResponsiveness;

    private LongFilter maintenanceId;

    private LongFilter jobTypeId;

    public ContractorCriteria() {
    }

    public ContractorCriteria(ContractorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.companyName = other.companyName == null ? null : other.companyName.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.stateCode = other.stateCode == null ? null : other.stateCode.copy();
        this.zipCode = other.zipCode == null ? null : other.zipCode.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.contactPerson = other.contactPerson == null ? null : other.contactPerson.copy();
        this.ratingOfWork = other.ratingOfWork == null ? null : other.ratingOfWork.copy();
        this.ratingOfResponsiveness = other.ratingOfResponsiveness == null ? null : other.ratingOfResponsiveness.copy();
        this.maintenanceId = other.maintenanceId == null ? null : other.maintenanceId.copy();
        this.jobTypeId = other.jobTypeId == null ? null : other.jobTypeId.copy();
    }

    @Override
    public ContractorCriteria copy() {
        return new ContractorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCompanyName() {
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
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

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(StringFilter contactPerson) {
        this.contactPerson = contactPerson;
    }

    public IntegerFilter getRatingOfWork() {
        return ratingOfWork;
    }

    public void setRatingOfWork(IntegerFilter ratingOfWork) {
        this.ratingOfWork = ratingOfWork;
    }

    public IntegerFilter getRatingOfResponsiveness() {
        return ratingOfResponsiveness;
    }

    public void setRatingOfResponsiveness(IntegerFilter ratingOfResponsiveness) {
        this.ratingOfResponsiveness = ratingOfResponsiveness;
    }

    public LongFilter getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(LongFilter maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public LongFilter getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(LongFilter jobTypeId) {
        this.jobTypeId = jobTypeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContractorCriteria that = (ContractorCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(city, that.city) &&
            Objects.equals(stateCode, that.stateCode) &&
            Objects.equals(zipCode, that.zipCode) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(contactPerson, that.contactPerson) &&
            Objects.equals(ratingOfWork, that.ratingOfWork) &&
            Objects.equals(ratingOfResponsiveness, that.ratingOfResponsiveness) &&
            Objects.equals(maintenanceId, that.maintenanceId) &&
            Objects.equals(jobTypeId, that.jobTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        companyName,
        streetAddress,
        city,
        stateCode,
        zipCode,
        phoneNumber,
        contactPerson,
        ratingOfWork,
        ratingOfResponsiveness,
        maintenanceId,
        jobTypeId
        );
    }

    @Override
    public String toString() {
        return "ContractorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (companyName != null ? "companyName=" + companyName + ", " : "") +
                (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (stateCode != null ? "stateCode=" + stateCode + ", " : "") +
                (zipCode != null ? "zipCode=" + zipCode + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (contactPerson != null ? "contactPerson=" + contactPerson + ", " : "") +
                (ratingOfWork != null ? "ratingOfWork=" + ratingOfWork + ", " : "") +
                (ratingOfResponsiveness != null ? "ratingOfResponsiveness=" + ratingOfResponsiveness + ", " : "") +
                (maintenanceId != null ? "maintenanceId=" + maintenanceId + ", " : "") +
                (jobTypeId != null ? "jobTypeId=" + jobTypeId + ", " : "") +
            "}";
    }

}
