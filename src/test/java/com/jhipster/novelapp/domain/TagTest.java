package com.jhipster.novelapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = new Tag();
        tag1.id = 1L;
        Tag tag2 = new Tag();
        tag2.id = tag1.id;
        assertThat(tag1).isEqualTo(tag2);
        tag2.id = 2L;
        assertThat(tag1).isNotEqualTo(tag2);
        tag1.id = null;
        assertThat(tag1).isNotEqualTo(tag2);
    }
}
