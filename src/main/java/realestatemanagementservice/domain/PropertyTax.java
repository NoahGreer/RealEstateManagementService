package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A PropertyTax.
 */
@Entity
@Table(name = "property_tax")
public class PropertyTax implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 0)
    @Column(name = "tax_year")
    private Integer taxYear;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "date_paid")
    private LocalDate datePaid;

    @Column(name = "confirmation_number")
    private String confirmationNumber;

    @ManyToOne
    @JsonIgnoreProperties("propertyTaxes")
    private Building building;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTaxYear() {
        return taxYear;
    }

    public PropertyTax taxYear(Integer taxYear) {
        this.taxYear = taxYear;
        return this;
    }

    public void setTaxYear(Integer taxYear) {
        this.taxYear = taxYear;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PropertyTax amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDatePaid() {
        return datePaid;
    }

    public PropertyTax datePaid(LocalDate datePaid) {
        this.datePaid = datePaid;
        return this;
    }

    public void setDatePaid(LocalDate datePaid) {
        this.datePaid = datePaid;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public PropertyTax confirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
        return this;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public Building getBuilding() {
        return building;
    }

    public PropertyTax building(Building building) {
        this.building = building;
        return this;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyTax)) {
            return false;
        }
        return id != null && id.equals(((PropertyTax) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PropertyTax{" +
            "id=" + getId() +
            ", taxYear=" + getTaxYear() +
            ", amount=" + getAmount() +
            ", datePaid='" + getDatePaid() + "'" +
            ", confirmationNumber='" + getConfirmationNumber() + "'" +
            "}";
    }
}
