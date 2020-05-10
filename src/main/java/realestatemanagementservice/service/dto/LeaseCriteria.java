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
 * Criteria class for the {@link realestatemanagementservice.domain.Lease} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.LeaseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leases?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateSigned;

    private LocalDateFilter moveInDate;

    private LocalDateFilter noticeOfRemovalOrMoveoutDate;

    private LocalDateFilter endDate;

    private BigDecimalFilter amount;

    private StringFilter leaseType;

    private StringFilter notes;

    private LongFilter rentId;

    private LongFilter infractionId;

    private LongFilter newLeaseId;

    private LongFilter personId;

    private LongFilter vehicleId;

    private LongFilter petId;

    private LongFilter apartmentId;

    public LeaseCriteria() {
    }

    public LeaseCriteria(LeaseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateSigned = other.dateSigned == null ? null : other.dateSigned.copy();
        this.moveInDate = other.moveInDate == null ? null : other.moveInDate.copy();
        this.noticeOfRemovalOrMoveoutDate = other.noticeOfRemovalOrMoveoutDate == null ? null : other.noticeOfRemovalOrMoveoutDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.leaseType = other.leaseType == null ? null : other.leaseType.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.rentId = other.rentId == null ? null : other.rentId.copy();
        this.infractionId = other.infractionId == null ? null : other.infractionId.copy();
        this.newLeaseId = other.newLeaseId == null ? null : other.newLeaseId.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
        this.vehicleId = other.vehicleId == null ? null : other.vehicleId.copy();
        this.petId = other.petId == null ? null : other.petId.copy();
        this.apartmentId = other.apartmentId == null ? null : other.apartmentId.copy();
    }

    @Override
    public LeaseCriteria copy() {
        return new LeaseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(LocalDateFilter dateSigned) {
        this.dateSigned = dateSigned;
    }

    public LocalDateFilter getMoveInDate() {
        return moveInDate;
    }

    public void setMoveInDate(LocalDateFilter moveInDate) {
        this.moveInDate = moveInDate;
    }

    public LocalDateFilter getNoticeOfRemovalOrMoveoutDate() {
        return noticeOfRemovalOrMoveoutDate;
    }

    public void setNoticeOfRemovalOrMoveoutDate(LocalDateFilter noticeOfRemovalOrMoveoutDate) {
        this.noticeOfRemovalOrMoveoutDate = noticeOfRemovalOrMoveoutDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getLeaseType() {
        return leaseType;
    }

    public void setLeaseType(StringFilter leaseType) {
        this.leaseType = leaseType;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public LongFilter getRentId() {
        return rentId;
    }

    public void setRentId(LongFilter rentId) {
        this.rentId = rentId;
    }

    public LongFilter getInfractionId() {
        return infractionId;
    }

    public void setInfractionId(LongFilter infractionId) {
        this.infractionId = infractionId;
    }

    public LongFilter getNewLeaseId() {
        return newLeaseId;
    }

    public void setNewLeaseId(LongFilter newLeaseId) {
        this.newLeaseId = newLeaseId;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }

    public LongFilter getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(LongFilter vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LongFilter getPetId() {
        return petId;
    }

    public void setPetId(LongFilter petId) {
        this.petId = petId;
    }

    public LongFilter getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(LongFilter apartmentId) {
        this.apartmentId = apartmentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LeaseCriteria that = (LeaseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateSigned, that.dateSigned) &&
            Objects.equals(moveInDate, that.moveInDate) &&
            Objects.equals(noticeOfRemovalOrMoveoutDate, that.noticeOfRemovalOrMoveoutDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(leaseType, that.leaseType) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(rentId, that.rentId) &&
            Objects.equals(infractionId, that.infractionId) &&
            Objects.equals(newLeaseId, that.newLeaseId) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(petId, that.petId) &&
            Objects.equals(apartmentId, that.apartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateSigned,
        moveInDate,
        noticeOfRemovalOrMoveoutDate,
        endDate,
        amount,
        leaseType,
        notes,
        rentId,
        infractionId,
        newLeaseId,
        personId,
        vehicleId,
        petId,
        apartmentId
        );
    }

    @Override
    public String toString() {
        return "LeaseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateSigned != null ? "dateSigned=" + dateSigned + ", " : "") +
                (moveInDate != null ? "moveInDate=" + moveInDate + ", " : "") +
                (noticeOfRemovalOrMoveoutDate != null ? "noticeOfRemovalOrMoveoutDate=" + noticeOfRemovalOrMoveoutDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (leaseType != null ? "leaseType=" + leaseType + ", " : "") +
                (notes != null ? "notes=" + notes + ", " : "") +
                (rentId != null ? "rentId=" + rentId + ", " : "") +
                (infractionId != null ? "infractionId=" + infractionId + ", " : "") +
                (newLeaseId != null ? "newLeaseId=" + newLeaseId + ", " : "") +
                (personId != null ? "personId=" + personId + ", " : "") +
                (vehicleId != null ? "vehicleId=" + vehicleId + ", " : "") +
                (petId != null ? "petId=" + petId + ", " : "") +
                (apartmentId != null ? "apartmentId=" + apartmentId + ", " : "") +
            "}";
    }

}
