package com.farmdiagnostician.web.service.impl;

import com.farmdiagnostician.web.domain.Retina;
import com.farmdiagnostician.web.repository.RetinaRepository;
import com.farmdiagnostician.web.service.RetinaService;
import com.farmdiagnostician.web.service.dto.RetinaDTO;
import com.farmdiagnostician.web.service.mapper.RetinaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Retina}.
 */
@Service
@Transactional
public class RetinaServiceImpl implements RetinaService {
    private final Logger log = LoggerFactory.getLogger(RetinaServiceImpl.class);

    private final RetinaRepository retinaRepository;

    private final RetinaMapper retinaMapper;

    public RetinaServiceImpl(RetinaRepository retinaRepository, RetinaMapper retinaMapper) {
        this.retinaRepository = retinaRepository;
        this.retinaMapper = retinaMapper;
    }

    @Override
    public RetinaDTO save(RetinaDTO retinaDTO) {
        log.debug("Request to save Retina : {}", retinaDTO);
        Retina retina = retinaMapper.toEntity(retinaDTO);
        retina = retinaRepository.save(retina);
        return retinaMapper.toDto(retina);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RetinaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Retinas");
        return retinaRepository.findAll(pageable).map(retinaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RetinaDTO> findOne(Long id) {
        log.debug("Request to get Retina : {}", id);
        return retinaRepository.findById(id).map(retinaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Retina : {}", id);
        retinaRepository.deleteById(id);
    }
}
