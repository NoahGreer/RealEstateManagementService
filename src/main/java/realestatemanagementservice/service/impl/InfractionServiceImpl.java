package realestatemanagementservice.service.impl;

import realestatemanagementservice.service.InfractionService;
import realestatemanagementservice.domain.Infraction;
import realestatemanagementservice.repository.InfractionRepository;
import realestatemanagementservice.service.dto.InfractionDTO;
import realestatemanagementservice.service.mapper.InfractionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Infraction}.
 */
@Service
@Transactional
public class InfractionServiceImpl implements InfractionService {

    private final Logger log = LoggerFactory.getLogger(InfractionServiceImpl.class);

    private final InfractionRepository infractionRepository;

    private final InfractionMapper infractionMapper;

    public InfractionServiceImpl(InfractionRepository infractionRepository, InfractionMapper infractionMapper) {
        this.infractionRepository = infractionRepository;
        this.infractionMapper = infractionMapper;
    }

    /**
     * Save a infraction.
     *
     * @param infractionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public InfractionDTO save(InfractionDTO infractionDTO) {
        log.debug("Request to save Infraction : {}", infractionDTO);
        Infraction infraction = infractionMapper.toEntity(infractionDTO);
        infraction = infractionRepository.save(infraction);
        return infractionMapper.toDto(infraction);
    }

    /**
     * Get all the infractions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InfractionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Infractions");
        return infractionRepository.findAll(pageable)
            .map(infractionMapper::toDto);
    }

    /**
     * Get one infraction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InfractionDTO> findOne(Long id) {
        log.debug("Request to get Infraction : {}", id);
        return infractionRepository.findById(id)
            .map(infractionMapper::toDto);
    }

    /**
     * Delete the infraction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Infraction : {}", id);
        infractionRepository.deleteById(id);
    }
}
