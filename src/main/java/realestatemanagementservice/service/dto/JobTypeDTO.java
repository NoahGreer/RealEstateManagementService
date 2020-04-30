package realestatemanagementservice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link realestatemanagementservice.domain.JobType} entity.
 */
public class JobTypeDTO implements Serializable {
    
    private Long id;

    private String name;

    
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobTypeDTO jobTypeDTO = (JobTypeDTO) o;
        if (jobTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
