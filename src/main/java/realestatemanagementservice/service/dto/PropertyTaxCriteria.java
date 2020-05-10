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
 * Criteria class for the {@link realestatemanagementservice.domain.PropertyTax} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.PropertyTaxResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /property-taxes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PropertyTaxCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter taxYear;

    private BigDecimalFilter amount;

    private LocalDateFilter datePaid;

    private StringFilter confirmationNumber;

    private LongFilter buildingId;

    public PropertyTaxCriteria() {
    }

    public PropertyTaxCriteria(PropertyTaxCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taxYear = other.taxYear == null ? null : other.taxYear.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.datePaid = other.datePaid == null ? null : other.datePaid.copy();
        this.confirmationNumber = other.confirmationNumber == null ? null : other.confirmationNumber.copy();
        this.buildingId = other.buildingId == null ? null : other.buildingId.copy();
    }

    @Override
    public PropertyTaxCriteria copy() {
        return new PropertyTaxCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(IntegerFilter taxYear) {
        this.taxYear = taxYear;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public LocalDateFilter getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(LocalDateFilter datePaid) {
        this.datePaid = datePaid;
    }

    public StringFilter getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(StringFilter confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public LongFilter getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(LongFilter buildingId) {
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
        final PropertyTaxCriteria that = (PropertyTaxCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(taxYear, that.taxYear) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(datePaid, that.datePaid) &&
            Objects.equals(confirmationNumber, that.confirmationNumber) &&
            Objects.equals(buildingId, that.buildingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        taxYear,
        amount,
        datePaid,
        confirmationNumber,
        buildingId
        );
    }

    @Override
    public String toString() {
        return "PropertyTaxCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (taxYear != null ? "taxYear=" + taxYear + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (datePaid != null ? "datePaid=" + datePaid + ", " : "") +
                (confirmationNumber != null ? "confirmationNumber=" + confirmationNumber + ", " : "") +
                (buildingId != null ? "buildingId=" + buildingId + ", " : "") +
            "}";
    }

}
