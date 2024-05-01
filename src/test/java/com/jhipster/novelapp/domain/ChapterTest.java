package com.jhipster.novelapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

public class ChapterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chapter.class);
        Chapter chapter1 = new Chapter();
        chapter1.id = 1L;
        Chapter chapter2 = new Chapter();
        chapter2.id = chapter1.id;
        assertThat(chapter1).isEqualTo(chapter2);
        chapter2.id = 2L;
        assertThat(chapter1).isNotEqualTo(chapter2);
        chapter1.id = null;
        assertThat(chapter1).isNotEqualTo(chapter2);
    }
}
