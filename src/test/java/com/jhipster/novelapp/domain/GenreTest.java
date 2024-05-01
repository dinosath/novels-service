package com.jhipster.novelapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

public class GenreTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Genre.class);
        Genre genre1 = new Genre();
        genre1.id = 1L;
        Genre genre2 = new Genre();
        genre2.id = genre1.id;
        assertThat(genre1).isEqualTo(genre2);
        genre2.id = 2L;
        assertThat(genre1).isNotEqualTo(genre2);
        genre1.id = null;
        assertThat(genre1).isNotEqualTo(genre2);
    }
}
