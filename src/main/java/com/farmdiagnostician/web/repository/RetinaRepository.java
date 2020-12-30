package com.farmdiagnostician.web.repository;

import com.farmdiagnostician.web.domain.Retina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Retina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RetinaRepository extends JpaRepository<Retina, Long>, JpaSpecificationExecutor<Retina> {}
