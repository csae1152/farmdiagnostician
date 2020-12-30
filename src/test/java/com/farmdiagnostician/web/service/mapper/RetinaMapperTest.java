package com.farmdiagnostician.web.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RetinaMapperTest {
    private RetinaMapper retinaMapper;

    @BeforeEach
    public void setUp() {
        retinaMapper = new RetinaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(retinaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(retinaMapper.fromId(null)).isNull();
    }
}
