package realestatemanagementservice.service.impl;

import realestatemanagementservice.service.MaintenanceService;
import realestatemanagementservice.domain.Maintenance;
import realestatemanagementservice.repository.MaintenanceRepository;
import realestatemanagementservice.service.dto.MaintenanceDTO;
import realestatemanagementservice.service.mapper.MaintenanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Maintenance}.
 */
@Service
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final Logger log = LoggerFactory.getLogger(MaintenanceServiceImpl.class);

    private final MaintenanceRepository maintenanceRepository;

    private final MaintenanceMapper maintenanceMapper;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository, MaintenanceMapper maintenanceMapper) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceMapper = maintenanceMapper;
    }

    /**
     * Save a maintenance.
     *
     * @param maintenanceDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MaintenanceDTO save(MaintenanceDTO maintenanceDTO) {
        log.debug("Request to save Maintenance : {}", maintenanceDTO);
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceDTO);
        maintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(maintenance);
    }

    /**
     * Get all the maintenances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaintenanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Maintenances");
        return maintenanceRepository.findAll(pageable)
            .map(maintenanceMapper::toDto);
    }

    /**
     * Get one maintenance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaintenanceDTO> findOne(Long id) {
        log.debug("Request to get Maintenance : {}", id);
        return maintenanceRepository.findById(id)
            .map(maintenanceMapper::toDto);
    }

    /**
     * Delete the maintenance by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Maintenance : {}", id);
        maintenanceRepository.deleteById(id);
    }
}
