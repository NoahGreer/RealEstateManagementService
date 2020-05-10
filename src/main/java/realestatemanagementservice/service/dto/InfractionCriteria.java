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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link realestatemanagementservice.domain.Infraction} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.InfractionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /infractions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InfractionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateOccurred;

    private StringFilter cause;

    private StringFilter resolution;

    private LongFilter leaseId;

    public InfractionCriteria() {
    }

    public InfractionCriteria(InfractionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateOccurred = other.dateOccurred == null ? null : other.dateOccurred.copy();
        this.cause = other.cause == null ? null : other.cause.copy();
        this.resolution = other.resolution == null ? null : other.resolution.copy();
        this.leaseId = other.leaseId == null ? null : other.leaseId.copy();
    }

    @Override
    public InfractionCriteria copy() {
        return new InfractionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDateOccurred() {
        return dateOccurred;
    }

    public void setDateOccurred(LocalDateFilter dateOccurred) {
        this.dateOccurred = dateOccurred;
    }

    public StringFilter getCause() {
        return cause;
    }

    public void setCause(StringFilter cause) {
        this.cause = cause;
    }

    public StringFilter getResolution() {
        return resolution;
    }

    public void setResolution(StringFilter resolution) {
        this.resolution = resolution;
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
        final InfractionCriteria that = (InfractionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateOccurred, that.dateOccurred) &&
            Objects.equals(cause, that.cause) &&
            Objects.equals(resolution, that.resolution) &&
            Objects.equals(leaseId, that.leaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateOccurred,
        cause,
        resolution,
        leaseId
        );
    }

    @Override
    public String toString() {
        return "InfractionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateOccurred != null ? "dateOccurred=" + dateOccurred + ", " : "") +
                (cause != null ? "cause=" + cause + ", " : "") +
                (resolution != null ? "resolution=" + resolution + ", " : "") +
                (leaseId != null ? "leaseId=" + leaseId + ", " : "") +
            "}";
    }

}
