package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "primary_contact")
    private Boolean primaryContact;

    @Column(name = "is_minor")
    private Boolean isMinor;

    @Column(name = "ssn")
    private String ssn;

    @Column(name = "background_check_date")
    private LocalDate backgroundCheckDate;

    @Column(name = "background_check_confirmation_number")
    private String backgroundCheckConfirmationNumber;

    @Column(name = "employment_verification_date")
    private LocalDate employmentVerificationDate;

    @Column(name = "employment_verification_confirmation_number")
    private String employmentVerificationConfirmationNumber;

    @ManyToMany(mappedBy = "people")
    @JsonIgnore
    private Set<Lease> leases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Person firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Person lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Person phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Person emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Boolean isPrimaryContact() {
        return primaryContact;
    }

    public Person primaryContact(Boolean primaryContact) {
        this.primaryContact = primaryContact;
        return this;
    }

    public void setPrimaryContact(Boolean primaryContact) {
        this.primaryContact = primaryContact;
    }

    public Boolean isIsMinor() {
        return isMinor;
    }

    public Person isMinor(Boolean isMinor) {
        this.isMinor = isMinor;
        return this;
    }

    public void setIsMinor(Boolean isMinor) {
        this.isMinor = isMinor;
    }

    public String getSsn() {
        return ssn;
    }

    public Person ssn(String ssn) {
        this.ssn = ssn;
        return this;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public LocalDate getBackgroundCheckDate() {
        return backgroundCheckDate;
    }

    public Person backgroundCheckDate(LocalDate backgroundCheckDate) {
        this.backgroundCheckDate = backgroundCheckDate;
        return this;
    }

    public void setBackgroundCheckDate(LocalDate backgroundCheckDate) {
        this.backgroundCheckDate = backgroundCheckDate;
    }

    public String getBackgroundCheckConfirmationNumber() {
        return backgroundCheckConfirmationNumber;
    }

    public Person backgroundCheckConfirmationNumber(String backgroundCheckConfirmationNumber) {
        this.backgroundCheckConfirmationNumber = backgroundCheckConfirmationNumber;
        return this;
    }

    public void setBackgroundCheckConfirmationNumber(String backgroundCheckConfirmationNumber) {
        this.backgroundCheckConfirmationNumber = backgroundCheckConfirmationNumber;
    }

    public LocalDate getEmploymentVerificationDate() {
        return employmentVerificationDate;
    }

    public Person employmentVerificationDate(LocalDate employmentVerificationDate) {
        this.employmentVerificationDate = employmentVerificationDate;
        return this;
    }

    public void setEmploymentVerificationDate(LocalDate employmentVerificationDate) {
        this.employmentVerificationDate = employmentVerificationDate;
    }

    public String getEmploymentVerificationConfirmationNumber() {
        return employmentVerificationConfirmationNumber;
    }

    public Person employmentVerificationConfirmationNumber(String employmentVerificationConfirmationNumber) {
        this.employmentVerificationConfirmationNumber = employmentVerificationConfirmationNumber;
        return this;
    }

    public void setEmploymentVerificationConfirmationNumber(String employmentVerificationConfirmationNumber) {
        this.employmentVerificationConfirmationNumber = employmentVerificationConfirmationNumber;
    }

    public Set<Lease> getLeases() {
        return leases;
    }

    public Person leases(Set<Lease> leases) {
        this.leases = leases;
        return this;
    }

    public Person addLease(Lease lease) {
        this.leases.add(lease);
        lease.getPeople().add(this);
        return this;
    }

    public Person removeLease(Lease lease) {
        this.leases.remove(lease);
        lease.getPeople().remove(this);
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
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Person{" +
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
}
