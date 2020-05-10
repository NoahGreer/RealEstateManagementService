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
 * Criteria class for the {@link realestatemanagementservice.domain.Person} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.PersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter phoneNumber;

    private StringFilter emailAddress;

    private BooleanFilter primaryContact;

    private BooleanFilter isMinor;

    private StringFilter ssn;

    private LocalDateFilter backgroundCheckDate;

    private StringFilter backgroundCheckConfirmationNumber;

    private LocalDateFilter employmentVerificationDate;

    private StringFilter employmentVerificationConfirmationNumber;

    private LongFilter leaseId;

    public PersonCriteria() {
    }

    public PersonCriteria(PersonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.primaryContact = other.primaryContact == null ? null : other.primaryContact.copy();
        this.isMinor = other.isMinor == null ? null : other.isMinor.copy();
        this.ssn = other.ssn == null ? null : other.ssn.copy();
        this.backgroundCheckDate = other.backgroundCheckDate == null ? null : other.backgroundCheckDate.copy();
        this.backgroundCheckConfirmationNumber = other.backgroundCheckConfirmationNumber == null ? null : other.backgroundCheckConfirmationNumber.copy();
        this.employmentVerificationDate = other.employmentVerificationDate == null ? null : other.employmentVerificationDate.copy();
        this.employmentVerificationConfirmationNumber = other.employmentVerificationConfirmationNumber == null ? null : other.employmentVerificationConfirmationNumber.copy();
        this.leaseId = other.leaseId == null ? null : other.leaseId.copy();
    }

    @Override
    public PersonCriteria copy() {
        return new PersonCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(StringFilter emailAddress) {
        this.emailAddress = emailAddress;
    }

    public BooleanFilter getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(BooleanFilter primaryContact) {
        this.primaryContact = primaryContact;
    }

    public BooleanFilter getIsMinor() {
        return isMinor;
    }

    public void setIsMinor(BooleanFilter isMinor) {
        this.isMinor = isMinor;
    }

    public StringFilter getSsn() {
        return ssn;
    }

    public void setSsn(StringFilter ssn) {
        this.ssn = ssn;
    }

    public LocalDateFilter getBackgroundCheckDate() {
        return backgroundCheckDate;
    }

    public void setBackgroundCheckDate(LocalDateFilter backgroundCheckDate) {
        this.backgroundCheckDate = backgroundCheckDate;
    }

    public StringFilter getBackgroundCheckConfirmationNumber() {
        return backgroundCheckConfirmationNumber;
    }

    public void setBackgroundCheckConfirmationNumber(StringFilter backgroundCheckConfirmationNumber) {
        this.backgroundCheckConfirmationNumber = backgroundCheckConfirmationNumber;
    }

    public LocalDateFilter getEmploymentVerificationDate() {
        return employmentVerificationDate;
    }

    public void setEmploymentVerificationDate(LocalDateFilter employmentVerificationDate) {
        this.employmentVerificationDate = employmentVerificationDate;
    }

    public StringFilter getEmploymentVerificationConfirmationNumber() {
        return employmentVerificationConfirmationNumber;
    }

    public void setEmploymentVerificationConfirmationNumber(StringFilter employmentVerificationConfirmationNumber) {
        this.employmentVerificationConfirmationNumber = employmentVerificationConfirmationNumber;
    }

    public LongFilter getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(LongFilter leaseId) {
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
        final PersonCriteria that = (PersonCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(primaryContact, that.primaryContact) &&
            Objects.equals(isMinor, that.isMinor) &&
            Objects.equals(ssn, that.ssn) &&
            Objects.equals(backgroundCheckDate, that.backgroundCheckDate) &&
            Objects.equals(backgroundCheckConfirmationNumber, that.backgroundCheckConfirmationNumber) &&
            Objects.equals(employmentVerificationDate, that.employmentVerificationDate) &&
            Objects.equals(employmentVerificationConfirmationNumber, that.employmentVerificationConfirmationNumber) &&
            Objects.equals(leaseId, that.leaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        phoneNumber,
        emailAddress,
        primaryContact,
        isMinor,
        ssn,
        backgroundCheckDate,
        backgroundCheckConfirmationNumber,
        employmentVerificationDate,
        employmentVerificationConfirmationNumber,
        leaseId
        );
    }

    @Override
    public String toString() {
        return "PersonCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
                (primaryContact != null ? "primaryContact=" + primaryContact + ", " : "") +
                (isMinor != null ? "isMinor=" + isMinor + ", " : "") +
                (ssn != null ? "ssn=" + ssn + ", " : "") +
                (backgroundCheckDate != null ? "backgroundCheckDate=" + backgroundCheckDate + ", " : "") +
                (backgroundCheckConfirmationNumber != null ? "backgroundCheckConfirmationNumber=" + backgroundCheckConfirmationNumber + ", " : "") +
                (employmentVerificationDate != null ? "employmentVerificationDate=" + employmentVerificationDate + ", " : "") +
                (employmentVerificationConfirmationNumber != null ? "employmentVerificationConfirmationNumber=" + employmentVerificationConfirmationNumber + ", " : "") +
                (leaseId != null ? "leaseId=" + leaseId + ", " : "") +
            "}";
    }

}
