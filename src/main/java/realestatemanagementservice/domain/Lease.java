package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Lease.
 */
@Entity
@Table(name = "lease")
public class Lease implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_signed")
    private LocalDate dateSigned;

    @Column(name = "move_in_date")
    private LocalDate moveInDate;

    @Column(name = "notice_of_removal_or_moveout_date")
    private LocalDate noticeOfRemovalOrMoveoutDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "lease_type")
    private String leaseType;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "lease")
    private Set<Rent> rents = new HashSet<>();

    @OneToMany(mappedBy = "lease")
    private Set<Infraction> infractions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("leases")
    private Lease newLease;

    @ManyToMany
    @JoinTable(name = "lease_person",
               joinColumns = @JoinColumn(name = "lease_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"))
    private Set<Person> people = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "lease_vehicle",
               joinColumns = @JoinColumn(name = "lease_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "vehicle_id", referencedColumnName = "id"))
    private Set<Vehicle> vehicles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "lease_pet",
               joinColumns = @JoinColumn(name = "lease_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "pet_id", referencedColumnName = "id"))
    private Set<Pet> pets = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("leases")
    private Apartment apartment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateSigned() {
        return dateSigned;
    }

    public Lease dateSigned(LocalDate dateSigned) {
        this.dateSigned = dateSigned;
        return this;
    }

    public void setDateSigned(LocalDate dateSigned) {
        this.dateSigned = dateSigned;
    }

    public LocalDate getMoveInDate() {
        return moveInDate;
    }

    public Lease moveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
        return this;
    }

    public void setMoveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
    }

    public LocalDate getNoticeOfRemovalOrMoveoutDate() {
        return noticeOfRemovalOrMoveoutDate;
    }

    public Lease noticeOfRemovalOrMoveoutDate(LocalDate noticeOfRemovalOrMoveoutDate) {
        this.noticeOfRemovalOrMoveoutDate = noticeOfRemovalOrMoveoutDate;
        return this;
    }

    public void setNoticeOfRemovalOrMoveoutDate(LocalDate noticeOfRemovalOrMoveoutDate) {
        this.noticeOfRemovalOrMoveoutDate = noticeOfRemovalOrMoveoutDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Lease endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Lease amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLeaseType() {
        return leaseType;
    }

    public Lease leaseType(String leaseType) {
        this.leaseType = leaseType;
        return this;
    }

    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }

    public String getNotes() {
        return notes;
    }

    public Lease notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Rent> getRents() {
        return rents;
    }

    public Lease rents(Set<Rent> rents) {
        this.rents = rents;
        return this;
    }

    public Lease addRent(Rent rent) {
        this.rents.add(rent);
        rent.setLease(this);
        return this;
    }

    public Lease removeRent(Rent rent) {
        this.rents.remove(rent);
        rent.setLease(null);
        return this;
    }

    public void setRents(Set<Rent> rents) {
        this.rents = rents;
    }

    public Set<Infraction> getInfractions() {
        return infractions;
    }

    public Lease infractions(Set<Infraction> infractions) {
        this.infractions = infractions;
        return this;
    }

    public Lease addInfraction(Infraction infraction) {
        this.infractions.add(infraction);
        infraction.setLease(this);
        return this;
    }

    public Lease removeInfraction(Infraction infraction) {
        this.infractions.remove(infraction);
        infraction.setLease(null);
        return this;
    }

    public void setInfractions(Set<Infraction> infractions) {
        this.infractions = infractions;
    }

    public Lease getNewLease() {
        return newLease;
    }

    public Lease newLease(Lease lease) {
        this.newLease = lease;
        return this;
    }

    public void setNewLease(Lease lease) {
        this.newLease = lease;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public Lease people(Set<Person> people) {
        this.people = people;
        return this;
    }

    public Lease addPerson(Person person) {
        this.people.add(person);
        person.getLeases().add(this);
        return this;
    }

    public Lease removePerson(Person person) {
        this.people.remove(person);
        person.getLeases().remove(this);
        return this;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public Lease vehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
        return this;
    }

    public Lease addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        vehicle.getLeases().add(this);
        return this;
    }

    public Lease removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
        vehicle.getLeases().remove(this);
        return this;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Set<Pet> getPets() {
        return pets;
    }

    public Lease pets(Set<Pet> pets) {
        this.pets = pets;
        return this;
    }

    public Lease addPet(Pet pet) {
        this.pets.add(pet);
        pet.getLeases().add(this);
        return this;
    }

    public Lease removePet(Pet pet) {
        this.pets.remove(pet);
        pet.getLeases().remove(this);
        return this;
    }

    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public Lease apartment(Apartment apartment) {
        this.apartment = apartment;
        return this;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lease)) {
            return false;
        }
        return id != null && id.equals(((Lease) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Lease{" +
            "id=" + getId() +
            ", dateSigned='" + getDateSigned() + "'" +
            ", moveInDate='" + getMoveInDate() + "'" +
            ", noticeOfRemovalOrMoveoutDate='" + getNoticeOfRemovalOrMoveoutDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", amount=" + getAmount() +
            ", leaseType='" + getLeaseType() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
