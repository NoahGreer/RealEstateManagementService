package realestatemanagementservice.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.PropertyTax} entity.
 */
public class PropertyTaxDTO implements Serializable {
    
    private Long id;

    @Min(value = 0)
    private Integer taxYear;

    private BigDecimal amount;

    private LocalDate datePaid;

    private String confirmationNumber;


    private Long buildingId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(Integer taxYear) {
        this.taxYear = taxYear;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(LocalDate datePaid) {
        this.datePaid = datePaid;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PropertyTaxDTO propertyTaxDTO = (PropertyTaxDTO) o;
        if (propertyTaxDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), propertyTaxDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PropertyTaxDTO{" +
            "id=" + getId() +
            ", taxYear=" + getTaxYear() +
            ", amount=" + getAmount() +
            ", datePaid='" + getDatePaid() + "'" +
            ", confirmationNumber='" + getConfirmationNumber() + "'" +
            ", buildingId=" + getBuildingId() +
            "}";
    }
}
