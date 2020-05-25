package realestatemanagementservice.service.dto;

import java.time.LocalDate;
import java.util.Objects;


public class PetOwnerDTO {
	
	private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String emailAddress;

    private String petName;

    private String type;

    private String color;

    private Boolean certifiedAssistanceAnimal;
    
    private PersonDTO personDTO;
    
    private PetDTO petDTO;
    
    public PetOwnerDTO(PersonDTO personDTO, PetDTO petDTO) {
    	this.personDTO = personDTO;
    	this.petDTO = petDTO;
    	this.id = petDTO.getId();
    	this.firstName = personDTO.getFirstName();
    	this.lastName = personDTO.getLastName();
    	this.phoneNumber = personDTO.getPhoneNumber();
    	this.emailAddress = personDTO.getEmailAddress();
    	this.petName = petDTO.getName();
    	this.type = petDTO.getType();
    	this.color = petDTO.getColor();
    	this.certifiedAssistanceAnimal = petDTO.isCertifiedAssistanceAnimal();
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

    public String getPetName() {
        return petName;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public Boolean isCertifiedAssistanceAnimal() {
        return certifiedAssistanceAnimal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PetOwnerDTO petOwnerDTO = (PetOwnerDTO) o;
        if (petOwnerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), petOwnerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PetOwnerDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", petName='" + getPetName() + "'" +
            ", type='" + getType() + "'" +
            ", color='" + getColor() + "'" +
            ", certifiedAssistanceAnimal='" + isCertifiedAssistanceAnimal() + "'" +
            "}";
    }

}
