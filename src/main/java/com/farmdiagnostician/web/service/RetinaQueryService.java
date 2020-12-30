package com.farmdiagnostician.web.service;

import com.farmdiagnostician.web.domain.Retina;
import com.farmdiagnostician.web.domain.Retina_;
import com.farmdiagnostician.web.repository.RetinaRepository;
import com.farmdiagnostician.web.service.dto.RetinaCriteria;
import com.farmdiagnostician.web.service.dto.RetinaDTO;
import com.farmdiagnostician.web.service.mapper.RetinaMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link Retina} entities in the database.
 * The main input is a {@link RetinaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RetinaDTO} or a {@link Page} of {@link RetinaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RetinaQueryService extends QueryService<Retina> {
    private final Logger log = LoggerFactory.getLogger(RetinaQueryService.class);

    private final RetinaRepository retinaRepository;

    private final RetinaMapper retinaMapper;

    public RetinaQueryService(RetinaRepository retinaRepository, RetinaMapper retinaMapper) {
        this.retinaRepository = retinaRepository;
        this.retinaMapper = retinaMapper;
    }

    /**
     * Return a {@link List} of {@link RetinaDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RetinaDTO> findByCriteria(RetinaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Retina> specification = createSpecification(criteria);
        return retinaMapper.toDto(retinaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RetinaDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RetinaDTO> findByCriteria(RetinaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Retina> specification = createSpecification(criteria);
        return retinaRepository.findAll(specification, page).map(retinaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RetinaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Retina> specification = createSpecification(criteria);
        return retinaRepository.count(specification);
    }

    /**
     * Function to convert {@link RetinaCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Retina> createSpecification(RetinaCriteria criteria) {
        Specification<Retina> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Retina_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Retina_.name));
            }
            if (criteria.getAnimal() != null) {
                specification = specification.and(buildSpecification(criteria.getAnimal(), Retina_.animal));
            }
        }
        return specification;
    }
}
