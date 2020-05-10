package realestatemanagementservice.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Contractor.
 */
@Entity
@Table(name = "contractor")
public class Contractor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "company_name")
    private String companyName;

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

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "contact_person")
    private String contactPerson;

    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "rating_of_work")
    private Integer ratingOfWork;

    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "rating_of_responsiveness")
    private Integer ratingOfResponsiveness;

    @OneToMany(mappedBy = "contractor")
    private Set<Maintenance> maintenances = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "contractor_job_type",
               joinColumns = @JoinColumn(name = "contractor_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "job_type_id", referencedColumnName = "id"))
    private Set<JobType> jobTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Contractor companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Contractor streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public Contractor city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public Contractor stateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Contractor zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Contractor phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public Contractor contactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
        return this;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Integer getRatingOfWork() {
        return ratingOfWork;
    }

    public Contractor ratingOfWork(Integer ratingOfWork) {
        this.ratingOfWork = ratingOfWork;
        return this;
    }

    public void setRatingOfWork(Integer ratingOfWork) {
        this.ratingOfWork = ratingOfWork;
    }

    public Integer getRatingOfResponsiveness() {
        return ratingOfResponsiveness;
    }

    public Contractor ratingOfResponsiveness(Integer ratingOfResponsiveness) {
        this.ratingOfResponsiveness = ratingOfResponsiveness;
        return this;
    }

    public void setRatingOfResponsiveness(Integer ratingOfResponsiveness) {
        this.ratingOfResponsiveness = ratingOfResponsiveness;
    }

    public Set<Maintenance> getMaintenances() {
        return maintenances;
    }

    public Contractor maintenances(Set<Maintenance> maintenances) {
        this.maintenances = maintenances;
        return this;
    }

    public Contractor addMaintenance(Maintenance maintenance) {
        this.maintenances.add(maintenance);
        maintenance.setContractor(this);
        return this;
    }

    public Contractor removeMaintenance(Maintenance maintenance) {
        this.maintenances.remove(maintenance);
        maintenance.setContractor(null);
        return this;
    }

    public void setMaintenances(Set<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }

    public Set<JobType> getJobTypes() {
        return jobTypes;
    }

    public Contractor jobTypes(Set<JobType> jobTypes) {
        this.jobTypes = jobTypes;
        return this;
    }

    public Contractor addJobType(JobType jobType) {
        this.jobTypes.add(jobType);
        jobType.getContractors().add(this);
        return this;
    }

    public Contractor removeJobType(JobType jobType) {
        this.jobTypes.remove(jobType);
        jobType.getContractors().remove(this);
        return this;
    }

    public void setJobTypes(Set<JobType> jobTypes) {
        this.jobTypes = jobTypes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contractor)) {
            return false;
        }
        return id != null && id.equals(((Contractor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Contractor{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", stateCode='" + getStateCode() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", ratingOfWork=" + getRatingOfWork() +
            ", ratingOfResponsiveness=" + getRatingOfResponsiveness() +
            "}";
    }
}
