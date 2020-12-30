package com.farmdiagnostician.web.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.farmdiagnostician.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class RetinaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Retina.class);
        Retina retina1 = new Retina();
        retina1.setId(1L);
        Retina retina2 = new Retina();
        retina2.setId(retina1.getId());
        assertThat(retina1).isEqualTo(retina2);
        retina2.setId(2L);
        assertThat(retina1).isNotEqualTo(retina2);
        retina1.setId(null);
        assertThat(retina1).isNotEqualTo(retina2);
    }
}
