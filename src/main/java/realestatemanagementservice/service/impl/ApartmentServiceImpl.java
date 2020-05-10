package realestatemanagementservice.service.impl;

import realestatemanagementservice.service.ApartmentService;
import realestatemanagementservice.domain.Apartment;
import realestatemanagementservice.repository.ApartmentRepository;
import realestatemanagementservice.service.dto.ApartmentDTO;
import realestatemanagementservice.service.mapper.ApartmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Apartment}.
 */
@Service
@Transactional
public class ApartmentServiceImpl implements ApartmentService {

    private final Logger log = LoggerFactory.getLogger(ApartmentServiceImpl.class);

    private final ApartmentRepository apartmentRepository;

    private final ApartmentMapper apartmentMapper;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ApartmentMapper apartmentMapper) {
        this.apartmentRepository = apartmentRepository;
        this.apartmentMapper = apartmentMapper;
    }

    /**
     * Save a apartment.
     *
     * @param apartmentDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ApartmentDTO save(ApartmentDTO apartmentDTO) {
        log.debug("Request to save Apartment : {}", apartmentDTO);
        Apartment apartment = apartmentMapper.toEntity(apartmentDTO);
        apartment = apartmentRepository.save(apartment);
        return apartmentMapper.toDto(apartment);
    }

    /**
     * Get all the apartments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApartmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Apartments");
        return apartmentRepository.findAll(pageable)
            .map(apartmentMapper::toDto);
    }

    /**
     * Get one apartment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ApartmentDTO> findOne(Long id) {
        log.debug("Request to get Apartment : {}", id);
        return apartmentRepository.findById(id)
            .map(apartmentMapper::toDto);
    }

    /**
     * Delete the apartment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Apartment : {}", id);
        apartmentRepository.deleteById(id);
    }
}
