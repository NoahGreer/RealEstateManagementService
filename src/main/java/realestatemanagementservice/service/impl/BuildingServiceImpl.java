package realestatemanagementservice.service.impl;

import realestatemanagementservice.service.BuildingService;
import realestatemanagementservice.domain.Building;
import realestatemanagementservice.repository.BuildingRepository;
import realestatemanagementservice.service.dto.BuildingDTO;
import realestatemanagementservice.service.mapper.BuildingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Building}.
 */
@Service
@Transactional
public class BuildingServiceImpl implements BuildingService {

    private final Logger log = LoggerFactory.getLogger(BuildingServiceImpl.class);

    private final BuildingRepository buildingRepository;

    private final BuildingMapper buildingMapper;

    public BuildingServiceImpl(BuildingRepository buildingRepository, BuildingMapper buildingMapper) {
        this.buildingRepository = buildingRepository;
        this.buildingMapper = buildingMapper;
    }

    /**
     * Save a building.
     *
     * @param buildingDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BuildingDTO save(BuildingDTO buildingDTO) {
        log.debug("Request to save Building : {}", buildingDTO);
        Building building = buildingMapper.toEntity(buildingDTO);
        building = buildingRepository.save(building);
        return buildingMapper.toDto(building);
    }

    /**
     * Get all the buildings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BuildingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Buildings");
        return buildingRepository.findAll(pageable)
            .map(buildingMapper::toDto);
    }

    /**
     * Get one building by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BuildingDTO> findOne(Long id) {
        log.debug("Request to get Building : {}", id);
        return buildingRepository.findById(id)
            .map(buildingMapper::toDto);
    }

    /**
     * Delete the building by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Building : {}", id);
        buildingRepository.deleteById(id);
    }
}
