package com.jhipster.novelapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

class NovelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NovelDTO.class);
        NovelDTO novelDTO1 = new NovelDTO();
        novelDTO1.id = 1L;
        NovelDTO novelDTO2 = new NovelDTO();
        assertThat(novelDTO1).isNotEqualTo(novelDTO2);
        novelDTO2.id = novelDTO1.id;
        assertThat(novelDTO1).isEqualTo(novelDTO2);
        novelDTO2.id = 2L;
        assertThat(novelDTO1).isNotEqualTo(novelDTO2);
        novelDTO1.id = null;
        assertThat(novelDTO1).isNotEqualTo(novelDTO2);
    }
}
