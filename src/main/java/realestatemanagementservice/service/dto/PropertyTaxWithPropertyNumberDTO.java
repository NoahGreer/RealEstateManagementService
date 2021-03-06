package realestatemanagementservice.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import javax.validation.constraints.Min;

public class PropertyTaxWithPropertyNumberDTO implements Serializable{
	 private Long id;
	 
    @Min(value = 0)
    private Integer taxYear;

    private BigDecimal amount;

    private LocalDate datePaid;

    private String confirmationNumber;
    
    private String propertyNumber;
    
    public PropertyTaxWithPropertyNumberDTO(PropertyTaxDTO propertyTaxDTO, BuildingDTO buildingDTO) {
    	
    	Objects.requireNonNull(propertyTaxDTO, "propertyTaxDTO must not be null");
		Objects.requireNonNull(buildingDTO, "buildingDTO must not be null");
		
		this.id = propertyTaxDTO.getId();
		this.taxYear = propertyTaxDTO.getTaxYear();
		this.amount = propertyTaxDTO.getAmount();
		this.datePaid = propertyTaxDTO.getDatePaid();
		this.confirmationNumber = propertyTaxDTO.getConfirmationNumber();
		this.propertyNumber = buildingDTO.getPropertyNumber();
	}
    
    public Long getId() {
        return id;
    }
    
    public String getPropertyNumber() {
    	return propertyNumber;
    }

    public Integer getTaxYear() {
        return taxYear;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDatePaid() {
        return datePaid;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PropertyTaxWithPropertyNumberDTO propertyTaxWithPropertyNumberDTO = (PropertyTaxWithPropertyNumberDTO) o;
        if (propertyTaxWithPropertyNumberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), propertyTaxWithPropertyNumberDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PropertyTaxWithPropertyNumberDTO{" +
            "id=" + getId() +
            ", propertyNumber=" + getPropertyNumber() +
            ", taxYear=" + getTaxYear() +
            ", amount=" + getAmount() +
            ", datePaid='" + getDatePaid() + "'" +
            ", confirmationNumber='" + getConfirmationNumber() + "'" +
            "}";
    }
}

