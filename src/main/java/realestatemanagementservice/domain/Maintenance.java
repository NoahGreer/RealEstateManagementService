package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A Maintenance.
 */
@Entity
@Table(name = "maintenance")
public class Maintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "notification_date")
    private LocalDate notificationDate;

    @Column(name = "date_complete")
    private LocalDate dateComplete;

    @Column(name = "contractor_job_number")
    private String contractorJobNumber;

    @Column(name = "repair_cost", precision = 21, scale = 2)
    private BigDecimal repairCost;

    @Column(name = "repair_paid_on")
    private LocalDate repairPaidOn;

    @Column(name = "receipt_of_payment")
    private String receiptOfPayment;

    @ManyToOne
    @JsonIgnoreProperties("maintenances")
    private Apartment apartment;

    @ManyToOne
    @JsonIgnoreProperties("maintenances")
    private Contractor contractor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Maintenance description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getNotificationDate() {
        return notificationDate;
    }

    public Maintenance notificationDate(LocalDate notificationDate) {
        this.notificationDate = notificationDate;
        return this;
    }

    public void setNotificationDate(LocalDate notificationDate) {
        this.notificationDate = notificationDate;
    }

    public LocalDate getDateComplete() {
        return dateComplete;
    }

    public Maintenance dateComplete(LocalDate dateComplete) {
        this.dateComplete = dateComplete;
        return this;
    }

    public void setDateComplete(LocalDate dateComplete) {
        this.dateComplete = dateComplete;
    }

    public String getContractorJobNumber() {
        return contractorJobNumber;
    }

    public Maintenance contractorJobNumber(String contractorJobNumber) {
        this.contractorJobNumber = contractorJobNumber;
        return this;
    }

    public void setContractorJobNumber(String contractorJobNumber) {
        this.contractorJobNumber = contractorJobNumber;
    }

    public BigDecimal getRepairCost() {
        return repairCost;
    }

    public Maintenance repairCost(BigDecimal repairCost) {
        this.repairCost = repairCost;
        return this;
    }

    public void setRepairCost(BigDecimal repairCost) {
        this.repairCost = repairCost;
    }

    public LocalDate getRepairPaidOn() {
        return repairPaidOn;
    }

    public Maintenance repairPaidOn(LocalDate repairPaidOn) {
        this.repairPaidOn = repairPaidOn;
        return this;
    }

    public void setRepairPaidOn(LocalDate repairPaidOn) {
        this.repairPaidOn = repairPaidOn;
    }

    public String getReceiptOfPayment() {
        return receiptOfPayment;
    }

    public Maintenance receiptOfPayment(String receiptOfPayment) {
        this.receiptOfPayment = receiptOfPayment;
        return this;
    }

    public void setReceiptOfPayment(String receiptOfPayment) {
        this.receiptOfPayment = receiptOfPayment;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public Maintenance apartment(Apartment apartment) {
        this.apartment = apartment;
        return this;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public Maintenance contractor(Contractor contractor) {
        this.contractor = contractor;
        return this;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Maintenance)) {
            return false;
        }
        return id != null && id.equals(((Maintenance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Maintenance{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", notificationDate='" + getNotificationDate() + "'" +
            ", dateComplete='" + getDateComplete() + "'" +
            ", contractorJobNumber='" + getContractorJobNumber() + "'" +
            ", repairCost=" + getRepairCost() +
            ", repairPaidOn='" + getRepairPaidOn() + "'" +
            ", receiptOfPayment='" + getReceiptOfPayment() + "'" +
            "}";
    }
}
