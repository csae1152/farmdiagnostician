package com.farmdiagnostician.web.service.mapper;

import com.farmdiagnostician.web.domain.Retina;
import com.farmdiagnostician.web.service.dto.RetinaDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Retina} and its DTO {@link RetinaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RetinaMapper extends EntityMapper<RetinaDTO, Retina> {
    default Retina fromId(Long id) {
        if (id == null) {
            return null;
        }
        Retina retina = new Retina();
        retina.setId(id);
        return retina;
    }
}
