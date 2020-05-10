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
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link realestatemanagementservice.domain.Maintenance} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.MaintenanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /maintenances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MaintenanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private LocalDateFilter notificationDate;

    private LocalDateFilter dateComplete;

    private StringFilter contractorJobNumber;

    private BigDecimalFilter repairCost;

    private LocalDateFilter repairPaidOn;

    private StringFilter receiptOfPayment;

    private LongFilter apartmentId;

    private LongFilter contractorId;

    public MaintenanceCriteria() {
    }

    public MaintenanceCriteria(MaintenanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.notificationDate = other.notificationDate == null ? null : other.notificationDate.copy();
        this.dateComplete = other.dateComplete == null ? null : other.dateComplete.copy();
        this.contractorJobNumber = other.contractorJobNumber == null ? null : other.contractorJobNumber.copy();
        this.repairCost = other.repairCost == null ? null : other.repairCost.copy();
        this.repairPaidOn = other.repairPaidOn == null ? null : other.repairPaidOn.copy();
        this.receiptOfPayment = other.receiptOfPayment == null ? null : other.receiptOfPayment.copy();
        this.apartmentId = other.apartmentId == null ? null : other.apartmentId.copy();
        this.contractorId = other.contractorId == null ? null : other.contractorId.copy();
    }

    @Override
    public MaintenanceCriteria copy() {
        return new MaintenanceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDateFilter notificationDate) {
        this.notificationDate = notificationDate;
    }

    public LocalDateFilter getDateComplete() {
        return dateComplete;
    }

    public void setDateComplete(LocalDateFilter dateComplete) {
        this.dateComplete = dateComplete;
    }

    public StringFilter getContractorJobNumber() {
        return contractorJobNumber;
    }

    public void setContractorJobNumber(StringFilter contractorJobNumber) {
        this.contractorJobNumber = contractorJobNumber;
    }

    public BigDecimalFilter getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(BigDecimalFilter repairCost) {
        this.repairCost = repairCost;
    }

    public LocalDateFilter getRepairPaidOn() {
        return repairPaidOn;
    }

    public void setRepairPaidOn(LocalDateFilter repairPaidOn) {
        this.repairPaidOn = repairPaidOn;
    }

    public StringFilter getReceiptOfPayment() {
        return receiptOfPayment;
    }

    public void setReceiptOfPayment(StringFilter receiptOfPayment) {
        this.receiptOfPayment = receiptOfPayment;
    }

    public LongFilter getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(LongFilter apartmentId) {
        this.apartmentId = apartmentId;
    }

    public LongFilter getContractorId() {
        return contractorId;
    }

    public void setContractorId(LongFilter contractorId) {
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
        final MaintenanceCriteria that = (MaintenanceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(notificationDate, that.notificationDate) &&
            Objects.equals(dateComplete, that.dateComplete) &&
            Objects.equals(contractorJobNumber, that.contractorJobNumber) &&
            Objects.equals(repairCost, that.repairCost) &&
            Objects.equals(repairPaidOn, that.repairPaidOn) &&
            Objects.equals(receiptOfPayment, that.receiptOfPayment) &&
            Objects.equals(apartmentId, that.apartmentId) &&
            Objects.equals(contractorId, that.contractorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        description,
        notificationDate,
        dateComplete,
        contractorJobNumber,
        repairCost,
        repairPaidOn,
        receiptOfPayment,
        apartmentId,
        contractorId
        );
    }

    @Override
    public String toString() {
        return "MaintenanceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (notificationDate != null ? "notificationDate=" + notificationDate + ", " : "") +
                (dateComplete != null ? "dateComplete=" + dateComplete + ", " : "") +
                (contractorJobNumber != null ? "contractorJobNumber=" + contractorJobNumber + ", " : "") +
                (repairCost != null ? "repairCost=" + repairCost + ", " : "") +
                (repairPaidOn != null ? "repairPaidOn=" + repairPaidOn + ", " : "") +
                (receiptOfPayment != null ? "receiptOfPayment=" + receiptOfPayment + ", " : "") +
                (apartmentId != null ? "apartmentId=" + apartmentId + ", " : "") +
                (contractorId != null ? "contractorId=" + contractorId + ", " : "") +
            "}";
    }

}
