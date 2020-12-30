package com.farmdiagnostician.web.service;

import com.farmdiagnostician.web.service.dto.RetinaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.farmdiagnostician.web.domain.Retina}.
 */
public interface RetinaService {
    /**
     * Save a retina.
     *
     * @param retinaDTO the entity to save.
     * @return the persisted entity.
     */
    RetinaDTO save(RetinaDTO retinaDTO);

    /**
     * Get all the retinas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RetinaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" retina.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RetinaDTO> findOne(Long id);

    /**
     * Delete the "id" retina.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
