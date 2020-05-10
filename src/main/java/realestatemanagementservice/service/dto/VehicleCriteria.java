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

/**
 * Criteria class for the {@link realestatemanagementservice.domain.Vehicle} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.VehicleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vehicles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VehicleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter make;

    private StringFilter model;

    private IntegerFilter modelYear;

    private StringFilter licensePlateNumber;

    private StringFilter licensePlateState;

    private StringFilter reservedParkingStallNumber;

    private LongFilter leaseId;

    public VehicleCriteria() {
    }

    public VehicleCriteria(VehicleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.make = other.make == null ? null : other.make.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.modelYear = other.modelYear == null ? null : other.modelYear.copy();
        this.licensePlateNumber = other.licensePlateNumber == null ? null : other.licensePlateNumber.copy();
        this.licensePlateState = other.licensePlateState == null ? null : other.licensePlateState.copy();
        this.reservedParkingStallNumber = other.reservedParkingStallNumber == null ? null : other.reservedParkingStallNumber.copy();
        this.leaseId = other.leaseId == null ? null : other.leaseId.copy();
    }

    @Override
    public VehicleCriteria copy() {
        return new VehicleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMake() {
        return make;
    }

    public void setMake(StringFilter make) {
        this.make = make;
    }

    public StringFilter getModel() {
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public IntegerFilter getModelYear() {
        return modelYear;
    }

    public void setModelYear(IntegerFilter modelYear) {
        this.modelYear = modelYear;
    }

    public StringFilter getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(StringFilter licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public StringFilter getLicensePlateState() {
        return licensePlateState;
    }

    public void setLicensePlateState(StringFilter licensePlateState) {
        this.licensePlateState = licensePlateState;
    }

    public StringFilter getReservedParkingStallNumber() {
        return reservedParkingStallNumber;
    }

    public void setReservedParkingStallNumber(StringFilter reservedParkingStallNumber) {
        this.reservedParkingStallNumber = reservedParkingStallNumber;
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
        final VehicleCriteria that = (VehicleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(make, that.make) &&
            Objects.equals(model, that.model) &&
            Objects.equals(modelYear, that.modelYear) &&
            Objects.equals(licensePlateNumber, that.licensePlateNumber) &&
            Objects.equals(licensePlateState, that.licensePlateState) &&
            Objects.equals(reservedParkingStallNumber, that.reservedParkingStallNumber) &&
            Objects.equals(leaseId, that.leaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        make,
        model,
        modelYear,
        licensePlateNumber,
        licensePlateState,
        reservedParkingStallNumber,
        leaseId
        );
    }

    @Override
    public String toString() {
        return "VehicleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (make != null ? "make=" + make + ", " : "") +
                (model != null ? "model=" + model + ", " : "") +
                (modelYear != null ? "modelYear=" + modelYear + ", " : "") +
                (licensePlateNumber != null ? "licensePlateNumber=" + licensePlateNumber + ", " : "") +
                (licensePlateState != null ? "licensePlateState=" + licensePlateState + ", " : "") +
                (reservedParkingStallNumber != null ? "reservedParkingStallNumber=" + reservedParkingStallNumber + ", " : "") +
                (leaseId != null ? "leaseId=" + leaseId + ", " : "") +
            "}";
    }

}
