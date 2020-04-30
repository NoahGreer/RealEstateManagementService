package realestatemanagementservice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.Pet} entity.
 */
public class PetDTO implements Serializable {
    
    private Long id;

    private String name;

    private String type;

    private String color;

    private Boolean certifiedAssistanceAnimal;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean isCertifiedAssistanceAnimal() {
        return certifiedAssistanceAnimal;
    }

    public void setCertifiedAssistanceAnimal(Boolean certifiedAssistanceAnimal) {
        this.certifiedAssistanceAnimal = certifiedAssistanceAnimal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PetDTO petDTO = (PetDTO) o;
        if (petDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), petDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", color='" + getColor() + "'" +
            ", certifiedAssistanceAnimal='" + isCertifiedAssistanceAnimal() + "'" +
            "}";
    }
}
