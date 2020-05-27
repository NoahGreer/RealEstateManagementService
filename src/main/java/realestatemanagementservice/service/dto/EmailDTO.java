package realestatemanagementservice.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;


public class EmailDTO implements Serializable{

    private String emailAddress;
	private PersonDTO personDTO;
	
	public EmailDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
		this.emailAddress = personDTO.getEmailAddress();
	}

    public String getEmailAddress() {
        return emailAddress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmailDTO emailDTO = (EmailDTO) o;
        if (emailDTO.getEmailAddress() == null || getEmailAddress() == null) {
            return false;
        }
        return Objects.equals(getEmailAddress(), personDTO.getEmailAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEmailAddress());
    }

    @Override
    public String toString() {
        return "EmailDTO{" +
            "emailAddress='" + getEmailAddress() + "'" +
            "}";
    }
}


