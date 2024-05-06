package com.jhipster.novelapp.domain;

import static com.jhipster.novelapp.domain.ChapterTestSamples.*;
import static com.jhipster.novelapp.domain.NovelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.novelapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChapterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chapter.class);
        Chapter chapter1 = getChapterSample1();
        Chapter chapter2 = new Chapter();
        assertThat(chapter1).isNotEqualTo(chapter2);

        chapter2.setId(chapter1.getId());
        assertThat(chapter1).isEqualTo(chapter2);

        chapter2 = getChapterSample2();
        assertThat(chapter1).isNotEqualTo(chapter2);
    }

    @Test
    void novelTest() throws Exception {
        Chapter chapter = getChapterRandomSampleGenerator();
        Novel novelBack = getNovelRandomSampleGenerator();

        chapter.setNovel(novelBack);
        assertThat(chapter.getNovel()).isEqualTo(novelBack);

        chapter.novel(null);
        assertThat(chapter.getNovel()).isNull();
    }
}
