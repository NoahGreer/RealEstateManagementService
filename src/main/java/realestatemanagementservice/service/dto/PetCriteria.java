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
 * Criteria class for the {@link realestatemanagementservice.domain.Pet} entity. This class is used
 * in {@link realestatemanagementservice.web.rest.PetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter type;

    private StringFilter color;

    private BooleanFilter certifiedAssistanceAnimal;

    private LongFilter leaseId;

    public PetCriteria() {
    }

    public PetCriteria(PetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.color = other.color == null ? null : other.color.copy();
        this.certifiedAssistanceAnimal = other.certifiedAssistanceAnimal == null ? null : other.certifiedAssistanceAnimal.copy();
        this.leaseId = other.leaseId == null ? null : other.leaseId.copy();
    }

    @Override
    public PetCriteria copy() {
        return new PetCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getColor() {
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public BooleanFilter getCertifiedAssistanceAnimal() {
        return certifiedAssistanceAnimal;
    }

    public void setCertifiedAssistanceAnimal(BooleanFilter certifiedAssistanceAnimal) {
        this.certifiedAssistanceAnimal = certifiedAssistanceAnimal;
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
        final PetCriteria that = (PetCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(color, that.color) &&
            Objects.equals(certifiedAssistanceAnimal, that.certifiedAssistanceAnimal) &&
            Objects.equals(leaseId, that.leaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        type,
        color,
        certifiedAssistanceAnimal,
        leaseId
        );
    }

    @Override
    public String toString() {
        return "PetCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (color != null ? "color=" + color + ", " : "") +
                (certifiedAssistanceAnimal != null ? "certifiedAssistanceAnimal=" + certifiedAssistanceAnimal + ", " : "") +
                (leaseId != null ? "leaseId=" + leaseId + ", " : "") +
            "}";
    }

}
