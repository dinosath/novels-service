package com.jhipster.novelapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

class TagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagDTO.class);
        TagDTO tagDTO1 = new TagDTO();
        tagDTO1.id = 1L;
        TagDTO tagDTO2 = new TagDTO();
        assertThat(tagDTO1).isNotEqualTo(tagDTO2);
        tagDTO2.id = tagDTO1.id;
        assertThat(tagDTO1).isEqualTo(tagDTO2);
        tagDTO2.id = 2L;
        assertThat(tagDTO1).isNotEqualTo(tagDTO2);
        tagDTO1.id = null;
        assertThat(tagDTO1).isNotEqualTo(tagDTO2);
    }
}
