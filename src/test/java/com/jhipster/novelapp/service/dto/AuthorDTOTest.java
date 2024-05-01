package com.jhipster.novelapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorDTO.class);
        AuthorDTO authorDTO1 = new AuthorDTO();
        authorDTO1.id = 1L;
        AuthorDTO authorDTO2 = new AuthorDTO();
        assertThat(authorDTO1).isNotEqualTo(authorDTO2);
        authorDTO2.id = authorDTO1.id;
        assertThat(authorDTO1).isEqualTo(authorDTO2);
        authorDTO2.id = 2L;
        assertThat(authorDTO1).isNotEqualTo(authorDTO2);
        authorDTO1.id = null;
        assertThat(authorDTO1).isNotEqualTo(authorDTO2);
    }
}
