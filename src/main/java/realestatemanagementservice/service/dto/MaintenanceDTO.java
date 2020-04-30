package realestatemanagementservice.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Maintenance} entity.
 */
public class MaintenanceDTO implements Serializable {
    
    private Long id;

    private String description;

    private LocalDate notificationDate;

    private LocalDate dateComplete;

    private String contractorJobNumber;

    private BigDecimal repairCost;

    private LocalDate repairPaidOn;

    private String receiptOfPayment;


    private Long apartmentId;

    private Long contractorId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDate notificationDate) {
        this.notificationDate = notificationDate;
    }

    public LocalDate getDateComplete() {
        return dateComplete;
    }

    public void setDateComplete(LocalDate dateComplete) {
        this.dateComplete = dateComplete;
    }

    public String getContractorJobNumber() {
        return contractorJobNumber;
    }

    public void setContractorJobNumber(String contractorJobNumber) {
        this.contractorJobNumber = contractorJobNumber;
    }

    public BigDecimal getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(BigDecimal repairCost) {
        this.repairCost = repairCost;
    }

    public LocalDate getRepairPaidOn() {
        return repairPaidOn;
    }

    public void setRepairPaidOn(LocalDate repairPaidOn) {
        this.repairPaidOn = repairPaidOn;
    }

    public String getReceiptOfPayment() {
        return receiptOfPayment;
    }

    public void setReceiptOfPayment(String receiptOfPayment) {
        this.receiptOfPayment = receiptOfPayment;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MaintenanceDTO maintenanceDTO = (MaintenanceDTO) o;
        if (maintenanceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), maintenanceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaintenanceDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", notificationDate='" + getNotificationDate() + "'" +
            ", dateComplete='" + getDateComplete() + "'" +
            ", contractorJobNumber='" + getContractorJobNumber() + "'" +
            ", repairCost=" + getRepairCost() +
            ", repairPaidOn='" + getRepairPaidOn() + "'" +
            ", receiptOfPayment='" + getReceiptOfPayment() + "'" +
            ", apartmentId=" + getApartmentId() +
            ", contractorId=" + getContractorId() +
            "}";
    }
}
