package com.jhipster.novelapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

class ChapterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChapterDTO.class);
        ChapterDTO chapterDTO1 = new ChapterDTO();
        chapterDTO1.id = 1L;
        ChapterDTO chapterDTO2 = new ChapterDTO();
        assertThat(chapterDTO1).isNotEqualTo(chapterDTO2);
        chapterDTO2.id = chapterDTO1.id;
        assertThat(chapterDTO1).isEqualTo(chapterDTO2);
        chapterDTO2.id = 2L;
        assertThat(chapterDTO1).isNotEqualTo(chapterDTO2);
        chapterDTO1.id = null;
        assertThat(chapterDTO1).isNotEqualTo(chapterDTO2);
    }
}
