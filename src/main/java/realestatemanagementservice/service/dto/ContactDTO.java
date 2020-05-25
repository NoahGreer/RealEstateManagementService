package realestatemanagementservice.service.dto;

import java.time.LocalDate;
import java.util.Objects;

public class ContactDTO {

	private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String emailAddress;

    private Boolean primaryContact;

    private Boolean isMinor;
    
    private PersonDTO personDTO;
	
	public ContactDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
		this.id = personDTO.getId();
		this.firstName = personDTO.getFirstName();
		this.lastName = personDTO.getLastName();
		this.phoneNumber = personDTO.getPhoneNumber();
		this.emailAddress = personDTO.getEmailAddress();
		this.primaryContact = personDTO.isPrimaryContact();
		this.isMinor = personDTO.isIsMinor();
	}

    
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Boolean isPrimaryContact() {
        return primaryContact;
    }

    public Boolean isIsMinor() {
        return isMinor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactDTO contactDTO = (ContactDTO) o;
        if (contactDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contactDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContactDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", primaryContact='" + isPrimaryContact() + "'" +
            ", isMinor='" + isIsMinor() + "'" +
            "}";
    }
}

}
