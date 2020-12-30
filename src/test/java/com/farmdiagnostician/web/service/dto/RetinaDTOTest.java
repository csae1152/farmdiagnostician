package com.farmdiagnostician.web.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.farmdiagnostician.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class RetinaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RetinaDTO.class);
        RetinaDTO retinaDTO1 = new RetinaDTO();
        retinaDTO1.setId(1L);
        RetinaDTO retinaDTO2 = new RetinaDTO();
        assertThat(retinaDTO1).isNotEqualTo(retinaDTO2);
        retinaDTO2.setId(retinaDTO1.getId());
        assertThat(retinaDTO1).isEqualTo(retinaDTO2);
        retinaDTO2.setId(2L);
        assertThat(retinaDTO1).isNotEqualTo(retinaDTO2);
        retinaDTO1.setId(null);
        assertThat(retinaDTO1).isNotEqualTo(retinaDTO2);
    }
}
