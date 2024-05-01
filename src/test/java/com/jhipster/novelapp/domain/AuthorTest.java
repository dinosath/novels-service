package com.jhipster.novelapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

public class AuthorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Author.class);
        Author author1 = new Author();
        author1.id = 1L;
        Author author2 = new Author();
        author2.id = author1.id;
        assertThat(author1).isEqualTo(author2);
        author2.id = 2L;
        assertThat(author1).isNotEqualTo(author2);
        author1.id = null;
        assertThat(author1).isNotEqualTo(author2);
    }
}
