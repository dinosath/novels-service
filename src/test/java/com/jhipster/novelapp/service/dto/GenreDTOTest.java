package com.jhipster.novelapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

class GenreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenreDTO.class);
        GenreDTO genreDTO1 = new GenreDTO();
        genreDTO1.id = 1L;
        GenreDTO genreDTO2 = new GenreDTO();
        assertThat(genreDTO1).isNotEqualTo(genreDTO2);
        genreDTO2.id = genreDTO1.id;
        assertThat(genreDTO1).isEqualTo(genreDTO2);
        genreDTO2.id = 2L;
        assertThat(genreDTO1).isNotEqualTo(genreDTO2);
        genreDTO1.id = null;
        assertThat(genreDTO1).isNotEqualTo(genreDTO2);
    }
}
