package realestatemanagementservice.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Person} entity.
 */
public class PersonDTO implements Serializable {
    
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String emailAddress;

    private Boolean primaryContact;

    private Boolean isMinor;

    private String ssn;

    private LocalDate backgroundCheckDate;

    private String backgroundCheckConfirmationNumber;

    private LocalDate employmentVerificationDate;

    private String employmentVerificationConfirmationNumber;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Boolean isPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(Boolean primaryContact) {
        this.primaryContact = primaryContact;
    }

    public Boolean isIsMinor() {
        return isMinor;
    }

    public void setIsMinor(Boolean isMinor) {
        this.isMinor = isMinor;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public LocalDate getBackgroundCheckDate() {
        return backgroundCheckDate;
    }

    public void setBackgroundCheckDate(LocalDate backgroundCheckDate) {
        this.backgroundCheckDate = backgroundCheckDate;
    }

    public String getBackgroundCheckConfirmationNumber() {
        return backgroundCheckConfirmationNumber;
    }

    public void setBackgroundCheckConfirmationNumber(String backgroundCheckConfirmationNumber) {
        this.backgroundCheckConfirmationNumber = backgroundCheckConfirmationNumber;
    }

    public LocalDate getEmploymentVerificationDate() {
        return employmentVerificationDate;
    }

    public void setEmploymentVerificationDate(LocalDate employmentVerificationDate) {
        this.employmentVerificationDate = employmentVerificationDate;
    }

    public String getEmploymentVerificationConfirmationNumber() {
        return employmentVerificationConfirmationNumber;
    }

    public void setEmploymentVerificationConfirmationNumber(String employmentVerificationConfirmationNumber) {
        this.employmentVerificationConfirmationNumber = employmentVerificationConfirmationNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonDTO personDTO = (PersonDTO) o;
        if (personDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), personDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", primaryContact='" + isPrimaryContact() + "'" +
            ", isMinor='" + isIsMinor() + "'" +
            ", ssn='" + getSsn() + "'" +
            ", backgroundCheckDate='" + getBackgroundCheckDate() + "'" +
            ", backgroundCheckConfirmationNumber='" + getBackgroundCheckConfirmationNumber() + "'" +
            ", employmentVerificationDate='" + getEmploymentVerificationDate() + "'" +
            ", employmentVerificationConfirmationNumber='" + getEmploymentVerificationConfirmationNumber() + "'" +
            "}";
    }
    
    public String toShortString() {
        return getFirstName() + " " + getLastName() +
            ", " + getPhoneNumber() +
            ", " + getEmailAddress() +
            ", primaryContact=" + isPrimaryContact();
    }
}
