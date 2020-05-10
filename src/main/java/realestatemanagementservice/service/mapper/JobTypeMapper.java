package realestatemanagementservice.service.mapper;


import realestatemanagementservice.domain.*;
import realestatemanagementservice.service.dto.JobTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobType} and its DTO {@link JobTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobTypeMapper extends EntityMapper<JobTypeDTO, JobType> {


    @Mapping(target = "contractors", ignore = true)
    @Mapping(target = "removeContractor", ignore = true)
    JobType toEntity(JobTypeDTO jobTypeDTO);

    default JobType fromId(Long id) {
        if (id == null) {
            return null;
        }
        JobType jobType = new JobType();
        jobType.setId(id);
        return jobType;
    }
}
