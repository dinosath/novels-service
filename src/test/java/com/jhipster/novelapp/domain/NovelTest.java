package com.jhipster.novelapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.TestUtil;
import org.junit.jupiter.api.Test;

public class NovelTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Novel.class);
        Novel novel1 = new Novel();
        novel1.id = 1L;
        Novel novel2 = new Novel();
        novel2.id = novel1.id;
        assertThat(novel1).isEqualTo(novel2);
        novel2.id = 2L;
        assertThat(novel1).isNotEqualTo(novel2);
        novel1.id = null;
        assertThat(novel1).isNotEqualTo(novel2);
    }
}
