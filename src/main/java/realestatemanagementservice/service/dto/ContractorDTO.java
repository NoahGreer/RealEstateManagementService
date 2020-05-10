package realestatemanagementservice.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Contractor} entity.
 */
public class ContractorDTO implements Serializable {
    
    private Long id;

    private String companyName;

    private String streetAddress;

    private String city;

    @Size(min = 2, max = 2)
    private String stateCode;

    @Size(max = 10)
    private String zipCode;

    private String phoneNumber;

    private String contactPerson;

    @Min(value = 0)
    @Max(value = 5)
    private Integer ratingOfWork;

    @Min(value = 0)
    @Max(value = 5)
    private Integer ratingOfResponsiveness;

    private Set<JobTypeDTO> jobTypes = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Integer getRatingOfWork() {
        return ratingOfWork;
    }

    public void setRatingOfWork(Integer ratingOfWork) {
        this.ratingOfWork = ratingOfWork;
    }

    public Integer getRatingOfResponsiveness() {
        return ratingOfResponsiveness;
    }

    public void setRatingOfResponsiveness(Integer ratingOfResponsiveness) {
        this.ratingOfResponsiveness = ratingOfResponsiveness;
    }

    public Set<JobTypeDTO> getJobTypes() {
        return jobTypes;
    }

    public void setJobTypes(Set<JobTypeDTO> jobTypes) {
        this.jobTypes = jobTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContractorDTO contractorDTO = (ContractorDTO) o;
        if (contractorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contractorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContractorDTO{" +
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
            ", jobTypes='" + getJobTypes() + "'" +
            "}";
    }
}
