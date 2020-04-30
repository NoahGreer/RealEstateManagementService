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
 * Criteria class for the {@link realestatemanagementservice.domain.Apartment} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.ApartmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /apartments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApartmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter unitNumber;

    private BooleanFilter moveInReady;

    private LongFilter maintenanceId;

    private LongFilter leaseId;

    private LongFilter buildingId;

    public ApartmentCriteria() {
    }

    public ApartmentCriteria(ApartmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.unitNumber = other.unitNumber == null ? null : other.unitNumber.copy();
        this.moveInReady = other.moveInReady == null ? null : other.moveInReady.copy();
        this.maintenanceId = other.maintenanceId == null ? null : other.maintenanceId.copy();
        this.leaseId = other.leaseId == null ? null : other.leaseId.copy();
        this.buildingId = other.buildingId == null ? null : other.buildingId.copy();
    }

    @Override
    public ApartmentCriteria copy() {
        return new ApartmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(StringFilter unitNumber) {
        this.unitNumber = unitNumber;
    }

    public BooleanFilter getMoveInReady() {
        return moveInReady;
    }

    public void setMoveInReady(BooleanFilter moveInReady) {
        this.moveInReady = moveInReady;
    }

    public LongFilter getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(LongFilter maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public LongFilter getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(LongFilter leaseId) {
        this.leaseId = leaseId;
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
        final ApartmentCriteria that = (ApartmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(unitNumber, that.unitNumber) &&
            Objects.equals(moveInReady, that.moveInReady) &&
            Objects.equals(maintenanceId, that.maintenanceId) &&
            Objects.equals(leaseId, that.leaseId) &&
            Objects.equals(buildingId, that.buildingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        unitNumber,
        moveInReady,
        maintenanceId,
        leaseId,
        buildingId
        );
    }

    @Override
    public String toString() {
        return "ApartmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (unitNumber != null ? "unitNumber=" + unitNumber + ", " : "") +
                (moveInReady != null ? "moveInReady=" + moveInReady + ", " : "") +
                (maintenanceId != null ? "maintenanceId=" + maintenanceId + ", " : "") +
                (leaseId != null ? "leaseId=" + leaseId + ", " : "") +
                (buildingId != null ? "buildingId=" + buildingId + ", " : "") +
            "}";
    }

}
