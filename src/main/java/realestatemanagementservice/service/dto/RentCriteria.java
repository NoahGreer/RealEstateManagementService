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
 * Criteria class for the {@link realestatemanagementservice.domain.Rent} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.RentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dueDate;

    private LocalDateFilter receivedDate;

    private BigDecimalFilter amount;

    private LongFilter leaseId;

    public RentCriteria() {
    }

    public RentCriteria(RentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.receivedDate = other.receivedDate == null ? null : other.receivedDate.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.leaseId = other.leaseId == null ? null : other.leaseId.copy();
    }

    @Override
    public RentCriteria copy() {
        return new RentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateFilter getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDateFilter receivedDate) {
        this.receivedDate = receivedDate;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public LongFilter getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(LongFilter leaseId) {
        this.leaseId = leaseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RentCriteria that = (RentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(receivedDate, that.receivedDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(leaseId, that.leaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dueDate,
        receivedDate,
        amount,
        leaseId
        );
    }

    @Override
    public String toString() {
        return "RentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
                (receivedDate != null ? "receivedDate=" + receivedDate + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (leaseId != null ? "leaseId=" + leaseId + ", " : "") +
            "}";
    }

}
