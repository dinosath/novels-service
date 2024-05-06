package com.jhipster.novelapp.domain;

import static com.jhipster.novelapp.domain.NovelTestSamples.*;
import static com.jhipster.novelapp.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void novelTest() throws Exception {
        Tag tag = getTagRandomSampleGenerator();
        Novel novelBack = getNovelRandomSampleGenerator();

        tag.addNovel(novelBack);
        assertThat(tag.getNovels()).containsOnly(novelBack);
        assertThat(novelBack.getTags()).containsOnly(tag);

        tag.removeNovel(novelBack);
        assertThat(tag.getNovels()).doesNotContain(novelBack);
        assertThat(novelBack.getTags()).doesNotContain(tag);

        tag.novels(new HashSet<>(Set.of(novelBack)));
        assertThat(tag.getNovels()).containsOnly(novelBack);
        assertThat(novelBack.getTags()).containsOnly(tag);

        tag.setNovels(new HashSet<>());
        assertThat(tag.getNovels()).doesNotContain(novelBack);
        assertThat(novelBack.getTags()).doesNotContain(tag);
    }
}
